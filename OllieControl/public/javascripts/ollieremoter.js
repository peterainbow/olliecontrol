var socket;

window.onload = function () {
//    init();
    socket = io();
    
    // if we get an "info" emit from the socket server then console.log the data we recive
    socket.on('info', function (data) {
        console.log(data);
    });

    $("#joystick").mousedown(handlemousedown);
    $("#joystick").mouseup(handlemouseup);
    $("#joystick").mousemove(handlemousemove);
}

var isactive = false;
var lastx = 0;
var lasty = 0;
var size=250;
var hsize=125;

function handlemousedown(e) {
    isactive = true;
    lastx = -1000;
    lasty = -1000;
    console.log("mouse down");
    handlemousemove(e);
}

function handlemouseup(e) {
    isactive = false;
    var ctx = $("#joystick").get(0).getContext("2d");
    ctx.clearRect(0, 0, 200, 200);
    console.log("mouse up");

    var url = '/ollieremote?fn=directmotor&' + 
            'speedleft=0'+ 
            '&dirleft=00' +
            '&speedright=0'+ 
            '&dirright=00' +
            '&time=0';
    
    console.log(url);
    
    $.ajax({
        url: url
    }).done(function (data) {
        console.log("ollie joystick done => " + data)
    })
}

var radius = 10;
function handlemousemove(e) {
    if (isactive) {
       
        var offset = $("#joystick").offset(); 
        var x = e.pageX - offset.left;
        var y = e.pageY - offset.top;

        if ( Math.abs(x-lastx) > 5 || Math.abs(y-lasty) > 5 ){
        console.log("mouse over: " + x + " x " + y);
        var ctx = $("#joystick").get(0).getContext("2d");
        ctx.clearRect(0, 0, size, size);
        ctx.beginPath();
        ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
        ctx.fillStyle = 'red';
        ctx.fill();
        ctx.lineWidth = 1;
        ctx.strokeStyle = '#003300';
        ctx.stroke();        lastx = x;
        lasty = y;
        
        y = y - hsize;
        var pwr = 255*(Math.abs(y) / hsize);
        var ls = x/size;
        var rs = 1.0-ls;
        var url = '/ollieremote?fn=directmotor&' + 
            'speedleft=' + Math.floor(ls*pwr) + 
            (y >= 0?'&dirleft=02':'&dirleft=01') +
            '&speedright=' + Math.floor(rs * pwr) + 
            (y >= 0?'&dirright=02':'&dirright=01') +
            '&time=0';

        console.log(url);

        $.ajax({
            url: url
        }).done(function (data) {
            console.log("ollie joystick done => " + data)
        });
}
    }
}

function init() {
    $.ajax({ url: '/ollieremote?fn=init' + ( $("#addresslist").val()!=null?'&address='+$("#addresslist").val():'') }).done(function (data) {
        console.log("ollie init done => " + data)
    })
}

function findollies() {
    $.ajax({ url: '/ollieremote?fn=findollies' }).done(function (data) {
        console.log("ollie findollies done => " + data)
        $("#addresslist").empty();
        $.each(data.Addresses, function (key,value) {
            $('#addresslist')
         .append($("<option></option>")
         .attr("value", value)
         .text(value));
        });

    })
}

function shutdown() {
    $.ajax({ url: '/ollieremote?fn=shutdown' }).done(function (data) {
        console.log("ollie shutdown done => " + data)
    })
}

function spinner() {
    $.ajax({
        url: '/ollieremote?fn=spin&speed=' + 
            $("#spinspeed").val() + 
            '&time=' + $("#spinstime").val() +
            ($("#clockwisespin")[0].checked?'&clockwise=true':'')
    }).done(function (data) {
        console.log("ollie spnnner done => " + data)
    })
}

function directmotor() {
    $.ajax({
        url: '/ollieremote?fn=directmotor&speedleft=' + 
            $("#speedleft").val() + 
            ($("#leftdir")[0].checked?'&dirleft=01':'&dirleft=02') +
            "&speedright="+$("#speedright").val() + 
            ($("#rightdir")[0].checked?'&dirright=01':'&dirright=02') +
            '&time=' + $("#spinstime").val() 
    }).done(function (data) {
        console.log("ollie spnnner done => " + data)
    })
}

function streamer() {
    $.ajax({
        url: '/ollieremote?fn=datastream' 
            + ($("#streamerenable").val() == "enable"?'&enable=true':'')
            + ($("#streampos")[0].checked?'&posenable=true':'')
            + ($("#streamvel")[0].checked?'&velenable=true':'')
    }).done(function (data) {
        console.log("ollie streamer done => " + data)
    })
}

function flasher() {
    $.ajax({ url: '/ollieremote?fn=flash&time='+ $("#flashtime").val() }).done(function (data) {
        console.log("ollie flasher done => " + data)
    })
}

function roller() {
    $.ajax({ url: '/ollieremote?fn=roll&speed=' + $("#rollspeed").val() + '&direction=' + $("#rolldirection").val() + '&time=' + $("#rolltime").val() }).done(function (data) {
        console.log("ollie roll done => " + data)
    })
}
