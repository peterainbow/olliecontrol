var nexpect = require('./nexpect');

nexpect.spawn("sudo hciconfig hci0 down",{ verbose: true })
         .run(function (err, stdout, exitcode) {
           if (!err) {
             console.log("hci0 down did this work");
nexpect.spawn("sudo hciconfig hci0 up",{ verbose: true })
         .run(function (err, stdout, exitcode) {
           if (!err) {
             console.log("hci0 up did this work");
		runMain();
           }
         });
           }
         });

console.log("eek");
var gt;
var sequence = 1;

function runMain()
{
var bluetooth_adr = "E4:4F:5A:51:87:B7";
var command = "gatttool --listen -t random -b " + bluetooth_adr + " --interactive";
console.log("running: " + command);
gt = nexpect.spawn(command,{ verbose: true });

//gt.sendline("quit");
gt.expect("[E4:4F:5A:51:87:B7][LE]>");
console.log("connecting");
gt.sendline("connect");
//gt.expect("connect");
//gt.expect("Attempting to connect");
gt.wait("Connection successful");
char_write_req_str(0x002d,"3031316933");
char_write_req_str(0x0017,"07");
char_write_req_str(0x0032,"01");
send_command(0x000e,"0202","01",false)
send_command(0x000e,"0203","FF",false)
send_command(0x000e,"0201","0000",false)
send_command(0x000e,"0220","00FFFF00",false)

//gt.sendline("quit");
console.log("connected");
gt.run(function (err, stdout, exitcode) {
           if (!err) {
             console.log("gatttool did this work");
           }
           else{
             console.log("gatttool didnt this work");
             console.log(err);
}
         });
//flash(5);
spin("55",true,5);
            setTimeout( function(){ 
spin("55",false,5); },5000 );

            setTimeout( function(){ 
		close()},10000 );

}

console.log("at end");

function toHex2(str){
   var res = str.toString(16);
	if (res.length == 1)
		return "0"+res;
	else
		return res;
}

function char_write_req_str( handle, value )
{
var cmd = "char-write-req " + toHex2(handle) + " " + value;
console.log(cmd);
gt.sendline(cmd);
//gt.readline();
};


function send_command( handle, command,data , response){
        sequence += 1
        var commandstr = command+toHex2(sequence)+toHex2(1+data.length/2)+data
        commandstr += get_csum(commandstr)
        if (response)
            res = "FFFE"+commandstr
        else
            res = "FFFF"+commandstr
        console.log("sendinf commans: " +sequence+"," + res)
        char_write_req_str(handle,res)
//        try:
//            self.con.expect('Notification handle = .*? \r', timeout=1)
//        except pexpect.TIMEOUT:
//            print("TIMEOUT exception!")
//            self.printreadall()
//        #print(self.con.before)
//        print(self.con.after)
}

function get_csum(  data){
        var checksum = 0
        for (var x=0; x<  data.length;x+=2){
          // console.log("getting part for "+x)
           var bit = data.substring(x,x+2)
 //         console.log("getting part for bit"+bit)
           checksum += parseInt(bit,16) 
//   		console.log(checksum)
    //       #print( int(bit,16))
}
//console.log(checksum)
        checksum =~ checksum % 256
//console.log(checksum)
        checksum &= 0xFF
//console.log(checksum)
        hex = toHex2(checksum).toUpperCase()
//console.log(hex)
        return hex
}

function flash( len){
	setTimeout(flasher,1000,3*len,1);
}

function close()
{
	gt.sendline("disconnect");
	gt.bump();
	gt.sendline("quit");
	//gt.bump();
}

function flasher(len,col){
	console.log("flsher: "+len+","+col);
	if (len==0)
		return;
	else
	{
		if(col==1)
                	send_command(0x000e,"0220","FF000000",false)
		else if (col==2)
                	send_command(0x000e,"0220","00FF0000",false)
		else {
                	send_command(0x000e,"0220","0000FF00",false)
			col=0;
		}
		gt.bump();
		setTimeout(flasher,1000,len-1,++col);
		}
}

function spin( speed,clockwise, time){
            setTimeout( doSpin, 1000,speed,time); 
}

function doSpin( speed, clockwise,time){
            send_command(0x000E,"0242","01",false)
		gt.bump();
		if (clockwise)
            send_command(0x000E,"0233","01"+speed+"02"+speed,false)
		else
            send_command(0x000E,"0233","02"+speed+"01"+speed,false)
		gt.bump();
	console.log("running for "+time);
	flash(time/3);
            setTimeout( function(){
	console.log("running NOW");
            send_command(0x000E,"0233","01000200",false)
		send_command(0x000E,"0242","00",false) 
		gt.bump();
}, 
time*1000 );
}

