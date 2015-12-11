<?php session_start(); if (isset($_SESSION["uid"])) {header("location: lobby.php"); exit;} ?>
<!DOCTYPE html>
<html>
	<head>
		<title>Grizz.ly - Login</title>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="resources/css/materialize.min.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/outter.css"	media="screen,projection"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	</head>
	<body>
		<div class="lr-button" id="lr-button">
			<a href="javascript:void(0)" onclick="changeLRLayer('register')" class="waves-effect waves-light btn black white-text"><i class="material-icons left">add_box</i>Registrate!</a>
		</div>
		<div class="row center-align">
			<div class="col s8 m6 l4 offset-s2 offset-m3 offset-l4">
				<div class="card blue-white lr-layer" id="lr-layer">
					<?php include("pages/login.html"); ?>
					<p class="z-depth-2"></p>
				</div>
			</div>
		</div>
		<div class="slider fullscreen" style="z-index: -3; position: absolute;">
			<ul class="slides">
				<li>
					<img src="resources/img/fondo.jpg">
				</li>
			</ul>
		</div>
		<script type="text/javascript" src="resources/js/jquery.min.js"></script>
		<script type="text/javascript" src="resources/js/jquery.form.min.js"></script>
		<script type="text/javascript" src="resources/js/materialize.min.js"></script>
		<script type="text/javascript" src="resources/js/functions.js"></script>
		<script>
			$(document).ready(function(){
				$('.slider').slider({full_width: true, indicators:false});
			});
		</script>
	</body>
</html>