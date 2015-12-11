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
					<li><a href="pages/perfil.html">Inicio</a></li>
					<li><a href="pages/mapa.html">Mapa</a></li>
				</ul>
				<ul id="nav-mobile" class="right hide-on-med-and-down">
					<li><a href="pages/planes.html">Planes</a></li>
					<li><a href="pages/retos.html">Retos</a></li>
					<li><a href="pages/imagen_perfil.html">imagen_perfil</a></li>
					<li>buscador</li>
				</ul>
			</div>
		</nav>
	</div>
	<script>
		$(document).ready(function(){
			var page = "perfil";
			$.ajax({
						url: "pages/perfil.html",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
			$('#chg_button').click(function(){
				if (page == "login"){
					page = "register";
					$.ajax({
						url: "pages/register.php",
						success: function(res) {
							$("#formcontent").html(res);
						}
					});
				} else if (page == "register"){
					page = "login";
					$.ajax({
						url: "pages/login.php",
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