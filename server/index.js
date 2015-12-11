var io = require('socket.io')(80);
var mysql = require('mysql');
var connection = mysql.createConnection({
	host     : 'localhost',
	user     : 'root',
	password : '',
	database : 'weplan'
});
connection.connect();

io.on('connection', function (socket) {
	socket.on('s-login', function(data){
		
	});
	socket.on('c-event-list', function (data)) {
		connection.query('SELECT 1 + 1 AS solution', function(err, rows, fields) {
			if (err) throw err;
			socket.emit('s-event-list', data);
			console.log('The solution is: ', rows[0].solution);
		});
	});
	socket.on('c-event-map', function (data)) {
		socket.emit('s-event-map', data);
	});
	socket.on('c-event-create', function (data)) {
		socket.emit('s-event-create', data);
	});
	socket.on('c-event-edit', function (data)) {
		socket.emit('s-event-edit', data);
	});
	socket.on('c-event-delete', function (data)) {
		socket.emit('s-event-delete', data);
	});
	socket.on('c-event-details', function (data)) {
		socket.emit('s-event-details', data);
	});
	socket.on('c-event-subscribe', function (data)) {
		socket.emit('s-event-subscribe', data);
	});
});