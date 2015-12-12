<?php session_start(); if (isset($_SESSION["uid"])) {header("location: lobby.php"); exit;} ?>
<!DOCTYPE html>
<html>
	<head>
		<title>Grizz.ly - frontpage</title>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="resources/css/materialize.min.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/frontpage.css"	media="screen,projection"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	</head>
	<body>
	<div class="navbar-fixed">
		<nav>
			<div class="nav-wrapper">
				<a href="#" class="brand-logo center">Logo</a>
				<ul id="nav-mobile" class="left hide-on-med-and-down">
					<li><a href="javaScript:void(0)" id="inicio-but">Inicio</a></li>
					<li><a href="javaScript:void(0)" id="mapa-but">Mapa</a></li>
				</ul>
				<ul id="nav-mobile" class="right hide-on-med-and-down">
					<li><a href="javaScript:void(0)" id="planes-but">Planes</a></li>
					<li><a href="javaScript:void(0)" id="retos-but">Retos</a></li>
					<li><a href="javaScript:void(0)">imagen_perfil</a></li>
					<li>buscador</li>
				</ul>
			</div>
		</nav>
		
	</div>
	<div id="formcontent">
	</div>
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery.form.min.js"></script>
	<script type="text/javascript" src="resources/js/materialize.min.js"></script>
	<script type="text/javascript" src="resources/js/functions.js"></script>
	<script>
		$(document).ready(function(){
			var page = "perfil";
			$.ajax({
				url: "pages/perfil.html",
				success: function(res) {
					$("#formcontent").html(res);
				}
			});
			$('#inicio-but').click(function(){
				if (page != "perfil"){
					page = "perfil";
					$.ajax({
						url: "pages/perfil.html",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
				}
			});
			$('#mapa-but').click(function(){
				if (page != "mapa"){
					page = "mapa";
					$.ajax({
						url: "pages/mapa.html",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
				}
			});
			$('#planes-but').click(function(){
				if (page != "planes"){
					page = "planes";
					$.ajax({
						url: "pages/planes.html",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
				}
			});
			$('#retos-but').click(function(){
				if (page != "retos"){
					page = "retos";
					$.ajax({
						url: "pages/retos.html",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
				}
			});
		});
		</script>
	</body>
</html>