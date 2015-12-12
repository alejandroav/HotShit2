var io = require('socket.io')(80);
var mysql = require('mysql');
var connection = mysql.createConnection({
	host     : 'localhost',
	user     : 'root',
	password : '',
	database : 'weplan'
});
connection.connect();

var rooms = [];

io.on('connection', function (socket) {
	socket.valid = false;
	socket.on('c-login', function(data){
		connection.query('SELECT count(*) AS cuantos FROM users WHERE id='+data.user_id+' AND session_id='+data.session_id, function(err, rows, fields) {
			if (err) socket.emit('s-login', {status: 'ERROR', msg: err});
			else {
				if (rows[0].cuantos > 0) {
					socket.emit('s-login', {status: 'OK'});
					socket.valid = true;
				} else socket.emit('s-login', {status: 'ERROR', msg: 'Usuario no valido'});
			}
		});
	});
	socket.on('c-event-list', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-list', {status: 'ERROR', msg: err});
				socket.emit('s-event-list', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-list', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-map', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-map', {status: 'ERROR', msg: err});
				socket.emit('s-event-map', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-map', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-create', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-create', {status: 'ERROR', msg: err});
				rooms.push('id');
				socket.emit('s-event-create', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-create', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-edit', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-edit', {status: 'ERROR', msg: err});
				socket.emit('s-event-edit', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-edit', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-delete', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-delete', {status: 'ERROR', msg: err});
				socket.emit('s-event-delete', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-delete', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-details', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-details', {status: 'ERROR', msg: err});
				socket.emit('s-event-details', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-details', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	socket.on('c-event-subscribe', function (data)) {
		if (socket.valid){
			connection.query('', function(err, rows, fields) {
				if (err) socket.emit('s-event-subscribe', {status: 'ERROR', msg: err});
				socket.join('id');
				socket.emit('s-event-subscribe', {status: 'OK', data: rows[0].data});
			});
		} else socket.emit('s-event-subscribe', {status: 'ERROR', msg: 'Usuario no valido'});
	});
});