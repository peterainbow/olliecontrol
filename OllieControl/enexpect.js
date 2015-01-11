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
var spawn = require('child_process').spawn;
var util = require('util');

var context;

function enspawn(command, params, options) {
    if (arguments.length === 2) {
        if (Array.isArray(arguments[1])) {
            options = {};
        }
        else {
            options = arguments[1];
            params = null;
        }
    }
    
    if (Array.isArray(command)) {
        params = command;
        command = params.shift();
    }
    else if (typeof command === 'string') {
        command = command.split(' ');
        params = params || command.slice(1);
        command = command[0];
    }
    
    options = options || {};
    context = {
        date: new Date(),
        command: command,
        cwd: options.cwd || undefined,
        env: options.env || undefined,
        ignoreCase: options.ignoreCase,
        params: params,
        stripColors: options.stripColors,
        before: [],
        lines: [],
        filters: [],
        currentCommand: null,
        waitingCommands: [],
        stream: options.stream || 'stdout',
	clearDownLines: function() { lines.clear(); },
        addFilterCallback: function(pattern, stripOutLine,filterCallback){
            context.filters.push({Pattern: pattern, Callback:filterCallback,StripOutLine:stripOutLine})
        },
        
        readline: function ( timeout , callback ){
            if (context.currentCommand != null)
                context.waitingCommands.push({ StartTime: context.date.getTime(), Pattern: null, Callback: callback, Timeout: timeout });
            else {
                context.currentCommand = { StartTime: context.date.getTime(), Pattern: null, Callback: callback, Timeout: timeout };
                (function (command) {
                    setTimeout(function () {
                                        if (command == context.currentCommand) {
                                            context.currentCommand.Callback({ Err: true, Command: context.currentCommand, Message: "Timeout" })
                                            commandDone(context);
                                        }
                    },timeout)
                })(context.currentCommand);
            }
            process();
        },

        sendline: function (line, callback) {
            try {
                context.process.stdin.write(line + '\n');
            }
            catch(err) {
                console.error("problem writing to stdin => " + err);
            }
        },

        expect: function(pattern, timeout,callback){
            if (context.currentCommand != null)
                context.waitingCommands.push({ StartTime: context.date.getTime(), Pattern: pattern, Callback: callback, Timeout: timeout });
            else {
                context.currentCommand = { StartTime: context.date.getTime(), Pattern: pattern, Callback: callback, Timeout: timeout };
                (function (command) {
                    setTimeout(function () {
                        if (command == context.currentCommand) {
                            context.currentCommand.Callback({ Err: true, Command: context.currentCommand, Message: "Timeout" })
                            commandDone( context );
                        }
                    }, timeout)
                })(context.currentCommand);
            }
            process();
        },
        kill: function () {
	        if (context != null && context.process != null )
                    context.process.kill('SIGINT');
        },
        close: function(){
            try {
                context.process.stdin.write('disconnect\n');
                context.process.stdin.write('quit\n');
                while (context.waitingCommands.length > 0)
		   context.waitingCommands.pop();
            }
            catch(err) {
                console.error("problem writing to stdin => " + err);
            }
        },
        start: function (endOfProcessCallback) {
            var errState = null,
                responded = false,
                stdout = [],
                options;
            
            function onError(err, kill) {
                if (errState || responded) {
                    return;
                }
                
                errState = err;
                responded = true;
                
                if (kill) {
                    try { context.process.kill(); }
                    catch (ex) { }
                }
                
                endOfProcessCallback(err);
            }
            
            function onLine(data) {
                data = data.toString();
                
                if (context.stripColors) {
                    data = data.replace(/\u001b\[\d{0,2}m/g, '');
                }
                
                if (context.ignoreCase) {
                    data = data.toLowerCase();
                }
                
                var lines = data.split('\r\n').filter(function (line) { return line.length > 0; });

                context.lines = context.lines.concat(lines);

                process();
            }
            
            options = {
                cwd: context.cwd,
                env: context.env
            };

            context.process = spawn(context.command, context.params, options);

            context.process.stdout.on('data', onLine);
            
            context.process.on('error', onError);
            
            //
            // When the process exits, check the output `code` and `signal`,
            // flush `context.queue` (if necessary) and respond to the callback
            // appropriately.
            //
            context.process.on('close', function (code, signal) {
                if (code === 127) {
                    // XXX(sam) Not how node works (anymore?), 127 is what /bin/sh returns,
                    // but it appears node does not, or not in all conditions, blithely
                    // return 127 to user, it emits an 'error' from the child_process.
                    
                    //
                    // If the response code is `127` then `context.command` was not found.
                    //
                    return onError(new Error('Command not found: ' + context.command));
                }
                else if (context.currentCommand != null || context.waitingCommands.length > 0) {
                    flushQueue();
                    if (context.currentCommand != null || context.waitingCommands.length > 0)
                        endOfProcessCallback({ Err: true, Message: "expect not found" , Code:code, Signal:signal , Command: context.currentCommand});
                    return;
                }
                
                endOfProcessCallback({ Err: false, Code: code, Signal: signal });
            });

        }
    };
    
    function flushQueue() {
        do {
            process();
            if (context.currentCommand != null)
                return;

            if (context.waitingCommands.length > 0)
                context.currentCommand = context.waitingCommands.pop();
        }
        while (context.waitingCommands.length > 0);
    }
    
    function testExpectation(data, expectation) {
        if (expectation == null) {
            return true;
        }
        else if (util.isRegExp(expectation)) {
            return expectation.test(data);
        } else {
            return data.indexOf(expectation) > -1;
        }
    }
    function commandDone(context){
        if (context.waitingCommands.length > 0)
            context.currentCommand = context.waitingCommands.pop();
        else
            context.currentCommand = null;
    }
    function process() {
        var toRemove = [];
        context.lines.forEach(function (line) {
            context.filters.forEach(function (filter) {
                if (testExpectation(line, filter.Pattern)) {
                    if (filter.StripOutLine)
                        toRemove.push(line);
                    filter.Callback({ Err: false, Filter: filter , Line: line })
                }
            })
        })
        toRemove.forEach(function (line) {
            context.lines.splice(context.lines.indexOf(line), 1);
        })
        console.log("curr command in process: " + JSON.stringify(context.currentCommand));
        if (context.currentCommand != null) {
            var now = context.date.getTime();
            if (now - context.currentCommand.StartTime > context.currentCommand.Timeout) {
                context.currentCommand.Callback({ Err: true, Command: context.currentCommand, Message: "Timeout" })
                context.currentCommand = null;
            }
            else {
                while (context.lines.length > 0) {
                    var line = context.lines.shift();
                    if (testExpectation(line, context.currentCommand.Pattern)) {
                        var b = context.before;
                        var current = context.currentCommand;
                        context.currentCommand = null;
                        context.before = [];
                        current.Callback({ Err: false, Command: current, Line: line , Before: b, After: context.lines });
                        break;
                    }
                    context.before.push(line);
                }
            }
        }
    }
    
   
    return context;
}

module.exports.spawn = enspawn;
module.exports.enspawn = {
    spawn: enspawn
};
