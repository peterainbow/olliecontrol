#!/usr/bin/env python
import pexpect
import sys
import time
import json
import select
import binascii
from time import sleep

def floatfromhex(h):
    t = float.fromhex(h)
    if t > float.fromhex('7FFF'):
        t = -(float.fromhex('FFFF') - t)
        pass
    return t

class Sphero:

    def __init__( self, bluetooth_adr ):
        self.sequence = 1
        pexpect.run('sudo hciconfig hci0 down')
        pexpect.run('sudo hciconfig hci0 up')
        self.con = pexpect.spawn('gatttool --listen -t random -b ' + bluetooth_adr + ' --interactive')
        self.con.expect('\[LE\]>', timeout=10)
        print("Preparing to connect.")
        self.con.sendline('connect')
        # test for success of connect
        self.con.expect('Connection successful.*\[LE\]>')
        print("connection success")
        
        self.con.setecho(False)
        self.cb = {}
        return

    def char_write_cmd_str( self, handle, value ):
        # The 0%x for value is VERY naughty!  Fix this!
        cmd = 'char-write-cmd 0x%02x %s' % (handle, value)
        print(cmd)
        self.con.sendline( cmd )
        return

    def char_write_req_str( self, handle, value ):
        # The 0%x for value is VERY naughty!  Fix this!
        cmd = 'char-write-req 0x%02x %s' % (handle, value)
        #print(cmd)
        self.con.sendline( cmd )
        self.con.readline(  )
        return

    def char_write_cmd( self, handle, value ):
        # The 0%x for value is VERY naughty!  Fix this!
        cmd = 'char-write-cmd 0x%02x 0x%02x' % (handle, value)
        print(cmd)
        self.con.sendline( cmd )
        return

    def char_write_req( self, handle, value ):
        # The 0%x for value is VERY naughty!  Fix this!
        cmd = 'char-write-req 0x%02x 0x%02x' % (handle, value)
        print(cmd)
        self.con.sendline( cmd )
        return

    def char_read_hnd( self, handle ):
        #print('char-read-hnd 0x%02x' % handle)
        self.con.sendline('char-read-hnd 0x%02x' % handle)
        self.con.expect('descriptor: .*? \r')
        print(self.con.after)
        after = self.con.after


    def char_read_version( self ):
        print('reading version')
        self.con.sendline('char-read-uuid 0x%02x' % 0x2A24)
        self.con.expect('value: .*? \r')
        after = self.con.after
        print(self.con.before)
        print(self.con.after[7:-2])
    
    def char_read_firmware_version( self ):
        print('reading firmware version')
        self.con.sendline('char-read-uuid 0x%02x' % 0x2A26)
        self.con.expect('value: .*? \r')
        after = self.con.after
        print(self.con.after)
   
    def write_to_commander( self , value ):
        print('command value 0x%02x' % value )
        #22bb746F-2ba1-7554-2D6F-726568705327
        self.char_write_req( 0x000d, value ) 
   
    def readall( self ):
        try:
            return self.con.readline(1)
        except pexpect.TIMEOUT:
            print("timeout on readline")
            return "timeout on readline"

    def printreadall( self ):
        try:
            while True:
                print("printreadall")
                print(self.con.readline(1))
                print("printreadall next")
        except pexpect.TIMEOUT:
            return

    # Notification handle = 0x0025 value: 9b ff 54 07
    def notification_loop( self ):
        while True:
            try:
                pnum = self.con.expect('Notification', timeout=4)
            except pexpect.TIMEOUT:
                print("TIMEOUT exception!")
                break
            if pnum==0:
                after = self.con.after
                hxstr = after.split()[3:]
                handle = long(float.fromhex(hxstr[0]))
                #try:
                if True:
                  self.cb[handle]([long(float.fromhex(n)) for n in hxstr[2:]])
            	#except:
                #  print("Error in callback for %x" % handle)
                #  print sys.argv[1]
                pass
            else:
                print("TIMEOUT!!")
        pass

    def register_cb( self, handle, fn ):
        self.cb[handle]=fn;
        return

    def send_command( self, handle, command,data , response):
        self.sequence += 1
        commandstr = command+('%02X'%(self.sequence))+('%02X'%((1+len(data)/2)))+data
        commandstr += self.get_csum(commandstr)
        if response:
            res = "FFFE"+commandstr
        else:
            res = "FFFF"+commandstr
        print("sendinf commans: " + res)
        self.char_write_req_str(handle,res)
        try:
            self.con.expect('Notification handle = .*? \r', timeout=1)
        except pexpect.TIMEOUT:
            print("TIMEOUT exception!")
            self.printreadall()
        #print(self.con.before)
        print(self.con.after)


    def spin( self, speed, time):
            self.send_command(0x000e,"0242","01",False)
            self.send_command(0x000e,"0233","01"+speed+"02"+speed,False)
            sleep(time)
            self.send_command(0x000e,"0242","00",False)

    def roll( self, control,time):
            self.send_command(0x000e,"0230",control,False)
            sleep(time)

    def collisiondet( self, flags):
            self.send_command(0x000e,"0212",flags,False)

    def notifies( self, flags):
            self.send_command(0x000e,"0211","00FF000100000000000C000000",False)

    def flash( self, len):
            for x in range(0, len):
                self.send_command(0x000e,"0220","FF000000",False)
                sleep(1)
                self.send_command(0x000e,"0220","00FF0000",False)
                sleep(1)
                self.send_command(0x000e,"0220","0000FF00",False)
                sleep(1)

    def get_csum( self, data):
        #print("coverting " + data)
        checksum = 0
        for x in range(0, len(data),2):
           #print("getting part for "+str(x))
           bit = data[x:x+2]
           #print(bit)
           checksum += int(bit,16) 
           #print(checksum)
           #print( int(bit,16))
        #print(checksum)
        checksum =~ checksum % 256
        #print(checksum)
        hex = '%02X' % ( checksum )
        #print(hex)
        return hex

barometer = None
datalog = sys.stdout

class SensorCallbacks:

    data = {}

    def __init__(self,addr):
        self.data['addr'] = addr

def main():
    global datalog
    global barometer

    bluetooth_adr = "E4:4F:5A:51:87:B7"
    #data['addr'] = bluetooth_adr
    if len(sys.argv) > 2:
        datalog = open(sys.argv[2], 'w+')

    while True:
        try: 
            print("[re]starting..")
            tag = Sphero(bluetooth_adr)
            tag.char_write_req_str(0x002d,"3031316933")
            print(tag.readall())
            tag.char_write_req_str(0x0017,"07")
            print(tag.readall())
            tag.char_write_req_str(0x0032,"01")
            print(tag.readall())
            tag.char_read_hnd(0x004f)
            tag.char_read_hnd(0x0053)
            tag.char_read_hnd(0x004c)
            tag.send_command(0x000e,"0202","01",False)
            tag.send_command(0x000e,"0203","FF",False)
            tag.send_command(0x000e,"0201","0000",False)
            #tag.notifies("00FF000100000000000C000000",False)
            #tag.collisiondet("000000000000")
            #tag.spin("55",1)
            #tag.roll("5500B401",1)
            #tag.roll("FF00B400",1)
            #tag.roll("55000001",1)
            tag.roll("00000000",1)
            #tag.flash(5)
        except:
            e = sys.exc_info()[0]
            print(e)
            print( sys.exc_info())
        finally:
            tag.char_write_cmd_str(0x0017,"02")
            tag.send_command(0x000e,"0242","00",False)
            tag.roll("00000000",0)
            tag.con.sendline('disconnect')
            tag.con.sendline('quit')
            pexpect.run('sudo hciconfig hci0 down')
            pexpect.run('sudo hciconfig hci0 up')
            break;

if __name__ == "__main__":
    main()

