var express = require('express');
var olliecontrol = require('../olliecontrol');
var ollie = new olliecontrol.ollie();
var router = express.Router();

/* GET home page. */
function exitHandler(options, err) {
    try {
        console.error("try and shutdown ollie")
        if (ollie != null) {
            console.error("ollie is not null so call close")
            ollie.doclose();
        }
        else
            console.error("ollie is NULL ???")
    }
    catch (err) {
        console.error("exception on call => " + err)
        res.end("Error: " + err + "\r\n");
    }
    if (options.cleanup) console.log('exit cleanup');
    if (err) console.log(err.stack);
    if (options.exit) process.exit();
}

//do something when app is closing
process.on('exit', exitHandler.bind(null, { cleanup: true }));

//catches ctrl+c event
process.on('SIGINT', exitHandler.bind(null, { exit: true }));

//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind(null, { exit: true }));

router.get('/', function (req, res) {
    try {
        if (req.query.fn != null) {
            switch (req.query.fn) {
                case "notify":
                    ollie.notify(req, res)
                    res.send("done");
                    break;
                case "test":
                    ollie.test(req, res)
                    break;
                case "init":
                    ollie.init(req, res)
                    break;
                case "findollies":
                    ollie.findaddresses(req, res)
                    break;
                case "shutdown":
                    ollie.close(req, res);
                    break;
                case "flash":
                    ollie.flash(req, res);
                    break;
                case "roll":
                    ollie.roll(req, res);
                    break;
                case "datastream":
                    ollie.datastream(req, res);
                    break;
                case "directmotor":
                    ollie.directmotor(req, res);
                    break;
                case "spin":
                    ollie.spin(req, res);
                    break;
                default:
                    for (var propName in req.query) {
                        if (req.query.hasOwnProperty(propName)) {
                            console.log(propName, req.query[propName]);
                            res.write("prop: " + propName + " => " + req.query[propName] + "\r\n")
                        }
                    }
                    res.end("unknown fn called: " + req.query.fn);
                    break;
            }
        }
        else {
            ollie.test(res);
        }
    }
    catch (err) {
        console.error("exception on call => " + err)
        res.end("Error: " + err + "\r\n");
    }
});

module.exports = router;