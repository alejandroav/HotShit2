var io = require('socket.io')(80);
io.on('connection', function (socket) {
	socket.on('event-listen', function (data)) {
		socket.emit('event-send', data);
	});
});