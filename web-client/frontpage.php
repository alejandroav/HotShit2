<?php session_start(); if (isset($_SESSION["uid"])) {header("location: lobby.php"); exit;} ?>
<!DOCTYPE html>
<html>
	<head>
		<title>Grizz.ly - frontpage</title>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="resources/css/materialize.min.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/frontpage.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/circle.css"	media="screen,projection"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	</head>
	<body>
	<ul id="dropdown1" class="dropdown-content">
	  <li><a href="#!">Configuraci&oacute;n</a></li>
	  <li class="divider"></li>
	  <li><a href="#!">Cerrar sesi&oacute;n</a></li>
	</ul>
	<div class="navbar-fixed">
		<nav>
			<div class="nav-wrapper">
				<a href="#" class="brand-logo center"><img class="logo-central" src="resources/img/logo-2-negativo.png"></a>
				<ul id="nav-mobile" class="left hide-on-med-and-down">
					<li><a href="javaScript:void(0)" id="inicio-but">Inicio</a></li>
					<li><a href="javaScript:void(0)" id="mapa-but">Mapa</a></li>
				</ul>
				<ul id="nav-mobile" class="right hide-on-med-and-down">
					<li><a href="javaScript:void(0)" id="planes-but">Planes</a></li>
					<li><a href="javaScript:void(0)" id="retos-but">Retos</a></li>
					
					<li><a class='dropdown-button' href="#." data-beloworigin="true"  data-activates='dropdown1'><img class="foto-perfil" src="resources/img/usuario-provisional.jpg"></img></a></li>
					<li class="buscador">
						<form style="height:62px;">
							<input type="text" value="Buscar..." onfocus="if (this.value == 'Buscar...') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Buscar...';}" />
						</form>
					</li>
					<li class="boton-crear"><a href="javaScript:void(0)"></a>
					</li>
				</ul>
				
					<div class="boton-crear"><a href="javaScript:void(0)" class="waves-effect waves-light btn orange white-text">Crear</a></div>
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
		<script>
		$('.dropdown-button').dropdown({
			inDuration: 300,
			outDuration: 225,
			constrain_width: false, 
			hover: true, 
			gutter: 0, 
			belowOrigin: true, 
			alignment: 'left' 
			}
		);
		</script>
	</body>
</html>