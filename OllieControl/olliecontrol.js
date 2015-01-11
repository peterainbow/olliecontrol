/*
The MIT License(MIT)

Copyright(c) [2014] [Peter Davies]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files(the "Software"), to deal 
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject tothe following conditions:

    The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
var enexpect = require('./enexpect');
var fs = require('fs');
var sio = require('./socketiohandler');

function ollie() {
    var bluetooth_adr = "E4:4F:5A:51:87:B7";
    var OllieTrue = "01";
    var OllieFalse = "00";
    var MagicOllieAntiDos = "3031316933";
    var TXPowerNumber = "07";
    var AntiDOSCharacteristicHandle = 0x002d;
    var TxPowerCharacteristicHandle = 0x0017;
    var WakeCharacteristicHandle = 0x0032;
    var OllieCommandHandle = 0x000E;

    ollie = {
        //        gatt: null,
        notify:function (req,res) {
            notify(" a test msg");
        },
        test: function (req,res) {
            var runner = enexpect.spawn("test.bat");
            runner.addFilterCallback(null, false, function (info) {
                res.write(JSON.stringify(info) + "\r\n");
                console.log("Filter=>" + JSON.stringify(info));
            })
            runner.readline(1000, function (info) {
                res.write(JSON.stringify(info) + "\r\n");
                runner.expect("hello", 1000, function (info) {
                    res.write(JSON.stringify(info) + "\r\n");
                });
            })
            runner.start(function (info) {
                res.end("Closed: " + JSON.stringify(info) + "\r\n");
            })

        },
        findaddresses: function (req, res){
            console.log("findAddresses called");
            findAddresses(req, res);
        },
        init: function (req, res){
            res.write("init called");
            if (req.query.address != null) {
                console.log("!!!!!!!!!!!!setting bluetooth address to => " + req.query.address);
                bluetooth_adr = req.query.address;
            }
            var bouncer = enexpect.spawn("sudo hciconfig hci0 down");
            bouncer.start(function (info) {
                var bouncer2 = enexpect.spawn("sudo hciconfig hci0 up");
                bouncer2.start(function (info) {
                    console.log("hc bounced");
                    var command = "gatttool --listen -t random -b " + bluetooth_adr + " --interactive";
                    gatt = enexpect.spawn(command);
                    gatt.addFilterCallback(null, false, function (info) {
                        res.write(JSON.stringify(info) + "\r\n");
                        console.log("                                       Filter=>" + JSON.stringify(info.Line));
                    });
                    gatt.expect("Connection successful", 5000, function (info) {
                        console.log("connected");
                        res.write("connected");
                        char_write_req_str(gatt, AntiDOSCharacteristicHandle, MagicOllieAntiDos);
                        gatt.expect("Characteristic value was written successfully", 10000, function (info) {
                            res.write("ollie magic sent");
                            console.log("ollie magic sent");
                            char_write_req_str(gatt, TxPowerCharacteristicHandle, TXPowerNumber);
                            gatt.expect("Characteristic value was written successfully", 10000, function (info) {
                                res.write("TX power sent");
                                console.log("TX power sent");
                                char_write_req_str(gatt, WakeCharacteristicHandle, OllieTrue);
                                gatt.expect("Characteristic value was written successfully", 10000, function (info) {
                                    res.write("wake up sent");
                                    console.log("wake up sent");
                                    send_command(gatt,"0220", "00FFFF00", false)
                                    res.end("setup done");
                                });
                            });
                        });
                    });
                    gatt.start(function (info) {
                        res.write(JSON.stringify(info) + "\r\n");
                        console.log("closedown on gatt =>" + JSON.stringify(info));
                    });
                    gatt.sendline("connect");
                });
            });
        },
        close: function (req, res) {
            console.log("close called");
            this.doclose();
            res.send("close called");
        },
        doclose: function(){
            console.log("doclose called");
            if (gatt != null) {
                console.log("gatt closing");
                gatt.close();
                gatt = null;
            }
            else
                console.log("gatt was null");
        },
        flash: function (req, res) {
            console.log("flash called");
            if (gatt != null) {
                flash(gatt,req.query.time)
                res.send("flash called for " + req.query.time);
            }
            else
                res.send("ERROR flash called without init");
        },
        datastream: function (req, res) {
            console.log("datastream called");
            if (gatt != null) {
                res.send("================ datastream called ==================");
                datastream(gatt, req.query.enable, req.query.posenable, req.query.velenable)
            }
            else
                res.send("ERROR datastream called without init");
        },
        roll: function(req,res) {
            console.log("roll called");
            if (gatt != null) {
                res.send("================ roll called ==================");
                roll(gatt, req.query.speed, req.query.direction , req.query.time)
            }
            else
                res.send("ERROR roll called without init");
        },
        spin: function (req, res) {
            console.log("spin called");
            if (gatt != null) {
                res.send("================ spin called ==================");
                spin(gatt, req.query.speed, req.query.clockwise, req.query.time)
            }
            else
                res.send("ERROR spin called without init");
        },
        directmotor: function (req, res) {
            console.log("directmotor called : " + JSON.stringify(req.query));
            if (gatt != null) {
                res.send("================ directmotor called ==================");
                directmotor(gatt, req.query.speedleft, req.query.dirleft, req.query.speedright, req.query.dirright, req.query.time)
            }
            else
                res.send("ERROR directmotor called without init");
        }
    }
    var gatt = null;
    var sequence = 1;
    function send_command(gatt, command, data , response) {
        sequence += 1
        var commandstr = command + toHex2(sequence) + toHex2(1 + data.length / 2) + data
        commandstr += get_csum(commandstr)
        if (response)
            res = "FFFE" + commandstr
        else
            res = "FFFF" + commandstr
        console.log("sending command: " + sequence + "," + res)
        char_write_req_str(gatt, OllieCommandHandle, res)
        gatt.expect("Characteristic value was written successfully", 10000, function (info) {
            console.log("sending command success found: " + res)
        });
    }
    function get_csum(data) {
        var checksum = 0
        for (var x = 0; x < data.length; x += 2) {
            // console.log("getting part for "+x)
            var bit = data.substring(x, x + 2)
            //         console.log("getting part for bit"+bit)
            checksum += parseInt(bit, 16)
            //   		console.log(checksum)
        }
        //console.log(checksum)
        checksum = ~checksum % 256
        //console.log(checksum)
        checksum &= 0xFF
        //console.log(checksum)
        hex = toHex2(checksum).toUpperCase()
        //console.log(hex)
        return hex
    }
    var finder = null;
    var addresses = [];
    function findAddresses(req, res) {
        addresses = [];
        finder = enexpect.spawn("sh ./getaddr");
        console.log("==========> sh ./getaddr")
        finder.start(function (info) {
            console.log("get addr finished");
            console.log("start file read");
            fs.readFile("addr.txt", function (error, data) {
                console.log("Reading File");
                
                if (error) {
                    console.log("There is an error reading file..." + error)
                }
                else {
                    console.log("File Data is ::\n\n" + data);
                    addAddressesFromLine(data.toString(), res);
                }
            });
        });
        setTimeout(function () {
            console.log("==========> killing finder ")
            if (finder != null) {
                finder.kill();
            }
        }, 15000);
    }
    function addAddressesFromLine(data,res)
    {
        var lines = data.split('\n').filter(function (line) { return line.length > 0; });
        console.log("==========> addresses add lines" + JSON.stringify(lines))
        lines.forEach(function (line) {
            console.log("==========> addresses add line " + JSON.stringify(line))
            if (line.indexOf(" 2B-") > -1) {
                console.log("==========> addresses add 2b line " + JSON.stringify(line));
                var addr = line.substring(0, line.indexOf(" 2B-"));
                console.log("==========> addresses add 2b addr line " + JSON.stringify(addr));
                if (addresses.indexOf(addr) == -1) {
                    addresses.push(addr);
                    console.log("==========> addresses add 2b addr line UN" + JSON.stringify(addr));
                }
            }
        });
		try{
                    res.send({ Error: false, Addresses: addresses});
		}
                catch (err) {
                    console.error("didnt like that end send => "+err)
                }
    }
    
    function toHex4(str) {
        var res = str.toString(16);
        if (res.length == 1)
            return "000" + res;
        else if (res.length == 2)
            return "00" + res;
        else if (res.length == 3)
            return "0" + res;
        else
            return res;
    }
    function toHex2(str) {
        var res = str.toString(16);
        if (res.length == 1)
            return "0" + res;
        else
            return res;
    }
    function char_write_req_str(gatt, handle, value){
        var cmd = "char-write-req " + toHex2(handle) + " " + value;
        console.log(cmd);
        gatt.sendline(cmd);
    }
    function flash(gatt,len) {
        setTimeout(flasher, 1000, gatt,3 * len, 1);
    }
    function flasher(gatt,len, col) {
        console.log("flasher: " + len + "," + col);
        if (len == 0)
            return;
        else {
            if (col == 1)
                send_command(gatt, "0220", "FF000000", false)
            else if (col == 2)
                send_command(gatt, "0220", "00FF0000", false)
            else {
                send_command(gatt, "0220", "0000FF00", false)
                col = 0;
            }
            setTimeout(flasher, 1000, gatt,len - 1, ++col);
        }
    }
    var QUATERNION_Q0 = 0x80000000,
        QUATERNION_Q1 = 0x40000000,
        QUATERNION_Q2 = 0x20000000,
        QUATERNION_Q3 = 0x10000000,
        ODOM_X = 0x08000000,
        ODOM_Y = 0x04000000,
        ACCELONE = 0x02000000,
        VELOCITY_X = 0x01000000,
        VELOCITY_Y = 0x00800000;

    function datastream(gatt, enable, pos, vel) {
        if (pos == null && vel == null)
            enable = null;
        if (enable != null) {
            console.log("=====================> data stream on => " + enable + " pos " + pos + " vel " + vel);
            // 2bytes n
            // 2bytes m
            // 4bytes mask1
            // 1byte packet count zero==infinity
            // 4 bytes mask2
            // here turning on odo x,y and vel x,y
            gatt.addFilterCallback("Notification handle", true, function (info) {
                notify(info.Line);
            });
            if (pos != null && vel != null)
                mask = "0D800000";
            else if (pos != null)
                mask = "0C000000";
            else (vel != null)
                mask = "01800000";
            send_command(gatt, "0211", "0400" + "0001" + "00000000" + "00" + mask, false);
            gatt.clearDownLines();
            gatt.expect("Characteristic value was written successfully", 10000, function (info) {
                console.log("clear down after stream on")
            });

        }
        else {
            send_command(gatt, "0211", "00FF" + "0001" + "00000000" + "00" + "00000000", false);
            gatt.clearDownLines();
            gatt.expect("Characteristic value was written successfully", 10000, function (info) {
                console.log("clear down after stream on")
            });

        }
    }
    function roll(gatt, speed, direction, time) {
        console.log("=====================> roll called " + speed + "," + direction + "," + time);
        speed = toHex2(parseInt(speed));
        time = parseInt(time);
        direction = toHex4(parseInt(direction));
        console.log("=====================> roll called " + speed + "," + direction + "," + time);
        if (time == 0)
            send_command(gatt, "0230", speed + direction + "00", false);
        else {
            send_command(gatt, "0230", speed + direction + "01", false);
            setTimeout(function () {
                console.log("roll STOPPING NOW");
                send_command(gatt, "0230", speed + direction + "00", false);
            }, 
        time * 1000);        }
    }
    function spin(gatt, speed, clockwise, time) {
        speed = toHex2(parseInt(speed));
        console.log("=====================> spin called " + speed + "," + clockwise + "," + time);
        send_command(gatt, "0242", "01", false)
        if (clockwise != null)
            send_command(gatt, "0233", "01" + speed + "02" + speed, false)
        else
            send_command(gatt, "0233", "02" + speed + "01" + speed, false)
        console.log("running for " + time);
        setTimeout(function () {
            console.log("spin STOPPING NOW");
            send_command(gatt, "0233", "01000200", false)
            send_command(gatt, "0242", "00", false)
        }, 
        time * 1000);
    }
    function directmotor(gatt, speedleft, dirleft, speedright, dirright, time) {
        speedleft = toHex2(parseInt(speedleft));
        speedright = toHex2(parseInt(speedright));
        console.log("=====================> spin called " + speedleft + "," + dirleft + "," + speedright + "," + dirright + "," + time);
        send_command(gatt, "0242", "01", false)
        send_command(gatt, "0233", dirleft + speedleft + dirright + speedright, false)
        console.log("running for " + time);
        if ( time != 0 )
        setTimeout(function () {
            console.log("spin STOPPING NOW");
            send_command(gatt, "0233", "01000200", false)
            send_command(gatt, "0242", "00", false)
        }, 
        time * 1000);
    }
    function notify(msg)
    {
                sio.socketiohandler.socketiohandler(null).io.sockets.emit('info', { msg: msg });
    }
    return ollie;
}

module.exports.ollie = ollie;
module.exports.olliecontrol = {
    ollie: ollie
};
