var express  = require('express')
    , app    = express()
    , server = require('http').createServer(app)
	, io = require('socket.io').listen(server)
	, mysql = require('mysql')
	, xss = require('xss')
	, connection = mysql.createConnection({
		host     : 'localhost',
		user     : 'grizzly',
		password : 'weplan',
		database : 'weplan'
	});
connection.connect();
server.listen(8080);

var rooms = [];

io.on('connection', function (socket) {
	socket.valid = false;
	/*Evento comprobacion de login*/
	socket.on('c-login', function(data){
		/*Comprobamos que se nos pasen los datos correctos*/
		if (('user_id' in data && !isNaN(data.user_id)) && ('session_id' in data && typeof data.session_id == "string")){
			/*consulta a la bd*/
			connection.query("SELECT count(*) AS cuantos FROM users WHERE id="+data.user_id+" AND session_id="+mysql.escape(data.session_id), function(err, rows, fields) {
				if (err) socket.emit('s-login', {status: 'ERROR', query: "SELECT count(*) AS cuantos FROM users WHERE id="+data.user_id+" AND session_id='"+mysql.escape(data.session_id)+"'", msg: err}); //ERROR en la consulta
				else { //Exito
					if (rows[0].cuantos > 0) {
						socket.uid = data.user_id;
						socket.valid = true;
						socket.emit('s-login', {status: 'OK'});
					} else socket.emit('s-login', {status: 'ERROR', msg: 'Usuario no valido'});
				}
			});
		} else socket.emit('s-login', {status: 'ERROR', msg: 'Error en el objeto JSON'});
	});
	/*Evento para listas de eventos*/
	socket.on('c-event-list', function (data) {
		if (socket.valid){
			if (('loc_long' in data && !isNaN(data.loc_long)) && ('loc_lat' in data && !isNaN(data.loc_lat)) && ('radio' in data && !isNaN(data.radio))){
				connection.query("SELECT id, title, description, geom, cost, date, (SELECT count(*) FROM attendance WHERE event_id = id) as current, capacity, image, "+
					"creator_id, (SELECT username FROM users WHERE id = creator_id) as creator_name, ST_Distance_Sphere(ST_GeomFromText('POINT("+data.loc_lat+" "+data.loc_long+")'), geom)/1000 as distance FROM events "+
					"WHERE ST_Distance_Sphere(ST_GeomFromText('POINT("+data.loc_lat+" "+data.loc_long+")'), geom) < "+data.radio*1000+" ORDER BY distance ASC LIMIT 30", function(err, rows, fields) {
					if (err) socket.emit('s-event-list', {status: 'ERROR', msg: err});
					socket.emit('s-event-list', {status: 'OK', data: rows});
				});
			} else socket.emit('s-event-list', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-list', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para mapa de eventos*/
	socket.on('c-event-map', function (data) {
		if (socket.valid){
			if (('loc_long' in data && !isNaN(data.loc_long)) && ('loc_lat' in data && !isNaN(data.loc_lat)) && ('radio' in data && !isNaN(data.radio))){
				connection.query("SELECT id, title, description, geom, date FROM events WHERE ST_Distance_Sphere(ST_GeomFromText('POINT("+data.loc_lat+" "+data.loc_long+")'), geom) < "+data.radio*1000, function(err, rows, fields) {
					if (err) socket.emit('s-event-map', {status: 'ERROR', msg: err});
					socket.emit('s-event-map', {status: 'OK', data: rows});
				});
			} else socket.emit('s-event-map', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-map', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para crear eventos*/
	socket.on('c-event-create', function (data) {
		if (socket.valid){
			if (('title' in data && typeof data.title == "string") && ('description' in data && typeof data.description == "string") && ('loc_long' in data && !isNaN(data.loc_long)) 
				&& ('loc_lat' in data && !isNaN(data.loc_lat)) && ('date' in data && typeof data.date == "string") && ('cost' in data && !isNaN(data.cost)) 
				&& ('capacity' in data && !isNaN(data.capacity))){
				connection.query("INSERT INTO events (title, description, geom, date, cost, capacity, creator_id) VALUES"+
					" ('"+mysql.escape(xss(data.title))+"', '"+mysql.escape(xss(data.description))+"', ST_GeomFromText('POINT("+data.loc_lat+" "+data.loc_long+")'), "+
					"'"+mysql.escape(data.date)+"', "+data.cost+", "+data.capacity+", "+socket.uid+")", function(err, result) {
					if (err) socket.emit('s-event-create', {status: 'ERROR', msg: err});
					socket.emit('s-event-create', {status: 'OK', data: {id:result.insertId}});
				});
			} else socket.emit('s-event-create', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-create', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para editar eventos*/
	socket.on('c-event-edit', function (data) {
		if (socket.valid){
			if (('event_id' in data && !isNaN(data.event_id)) && ('title' in data.data && typeof data.data.title == "string") && ('description' in data.data && typeof data.data.description == "string") 
				&& ('loc_long' in data.data && !isNaN(data.data.loc_long)) && ('loc_lat' in data.data && !isNaN(data.data.loc_lat)) 
				&& ('date' in data.data && typeof data.data.date == "string") && ('cost' in data.data && !isNaN(data.data.cost)) && ('capacity' in data.data && !isNaN(data.data.capacity))){
				connection.query("UPDATE events SET title='"+mysql.escape(xss(data.data.title))+"', description='"+mysql.escape(xss(data.data.description))+"', ST_GeomFromText('POINT("+data.data.loc_lat+" "+data.data.loc_long+")')"+
					", date='"+mysql.escape(data.data.date)+"', cost="+data.data.cost+" WHERE id="+data.event_id+" AND creator_id="+socket.uid, function(err, rows, fields) {
					if (err) socket.emit('s-event-edit', {status: 'ERROR', msg: err});
					socket.emit('s-event-edit', {status: 'OK'});
				});
			} else socket.emit('s-event-edit', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-edit', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para borrar eventos*/
	socket.on('c-event-delete', function (data) {
		if (socket.valid){
			if ('event_id' in data && !isNaN(data.event_id)){
				connection.query('DELETE FROM events WHERE id='+data.event_id+" AND creator_id="+socket.uid, function(err, rows, fields) {
					if (err) socket.emit('s-event-delete', {status: 'ERROR', msg: err});
					socket.emit('s-event-delete', {status: 'OK'});
				});
			} else socket.emit('s-event-delete', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-delete', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para obtener detalles de eventos*/
	socket.on('c-event-details', function (data) {
		if (socket.valid){
			if ('event_id' in data && !isNaN(data.event_id)){
				connection.query('SELECT id, title, description, geom, cost, date, (SELECT count(*) FROM attendance WHERE event_id = id) as current, capacity, image, '+
					'creator_id, (SELECT username FROM users WHERE id = creator_id) as creator_name FROM events WHERE id='+data.event_id, function(err, rows, fields) {
					if (err) socket.emit('s-event-details', {status: 'ERROR', msg: err});
					socket.emit('s-event-details', {status: 'OK', data: rows[0]});
				});
			} else socket.emit('s-event-details', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-details', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para suscribirse a eventos*/
	socket.on('c-event-subscribe', function (data) {
		if (socket.valid){
			if ('event_id' in data && !isNaN(data.event_id)){
				connection.query('INSERT INTO attendance VALUES ('+socket.uid+', '+data.event_id+')', function(err, rows, fields) {
					if (err) socket.emit('s-event-subscribe', {status: 'ERROR', msg: err});
					socket.join("room_"+data.event_id);
					io.to("room_"+data.event_id).emit('s-event-useradd');
					socket.emit('s-event-subscribe', {status: 'OK'});
				});
			} else socket.emit('s-event-subscribe', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-subscribe', {status: 'ERROR', msg: 'Usuario no valido'});
	});
	/*Evento para desuscribirse de eventos*/
	socket.on('c-event-subscribe', function (data) {
		if (socket.valid){
			if ('event_id' in data && !isNaN(data.event_id)){
				connection.query('DELETE FROM attendance WHERE user_id='+socket.uid+' AND event_id='+data.event_id, function(err, rows, fields) {
					if (err) socket.emit('s-event-desubscribe', {status: 'ERROR', msg: err});
					socket.leave("room_"+data.event_id);
					io.to("room_"+data.event_id).emit('s-event-userdel');
					socket.emit('s-event-desubscribe', {status: 'OK'});
				});
			} else socket.emit('s-event-desubscribe', {status: 'ERROR', msg: 'Error en el objeto JSON'});
		} else socket.emit('s-event-desubscribe', {status: 'ERROR', msg: 'Usuario no valido'});
	});
});