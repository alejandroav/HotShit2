 <?php session_start(); if (isset($_SESSION["uid"])) {header("location: lobby.php"); exit;} ?>
 <!DOCTYPE html>
<html>
	<head>
		<title>WePlan - Login</title>
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
			<div class="col s4 offset-s4">
				<div class="card blue-white lr-layer" id="lr-layer">
					<?php include("pages/login.html"); ?>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="resources/js/jquery.min.js"></script>
		<script type="text/javascript" src="resources/js/jquery.form.min.js"></script>
		<script type="text/javascript" src="resources/js/materialize.min.js"></script>
		<script type="text/javascript" src="resources/js/functions.js"></script>
	</body>
</html>