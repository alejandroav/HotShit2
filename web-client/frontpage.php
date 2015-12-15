<?php session_start(); if (!isset($_SESSION["uid"])) {header("location: index.php"); exit;} ?>
<!DOCTYPE html>
<html>
	<head>
		<title>Grizz.ly - frontpage</title>
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="resources/css/materialize.min.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/frontpage.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/nouislider.min.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/nouislider.pips.css"	media="screen,projection"/>
		<link type="text/css" rel="stylesheet" href="resources/css/nouislider.tooltips.css"	media="screen,projection"/>		
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<script type="text/javascript" src="resources/js/jquery.min.js"></script>
	</head>
	<body>
	<ul id="dropdown1" class="dropdown-content">
	  <li><a href="#!">Configuraci&oacute;n</a></li>
	  <li class="divider"></li>
	  <li><a href="operations.php?op=logout">Cerrar sesi&oacute;n</a></li>
	</ul>
	<div class="navbar-fixed">
		<nav>
			<ul id="slide-out" class="side-nav">
				<li><img class="foto-perfil" style="height:100px;width:100px;margin-left:75px;" src="resources/img/usuario-provisional.jpg"></img></a></li>
				<li class="buscador">
					<form style="height:62px;">
						<input style="color:black;" type="text" value="Buscar..." onfocus="if (this.value == 'Buscar...') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Buscar...';}" />
					</form>
				</li>
				<li><a href="javaScript:changeContent('perfil')">Inicio</a></li>
				<li><a href="javaScript:changeContent('mapa')">Mapa</a></li>
				<li><a href="javaScript:changeContent('planes')">Planes</a></li>
				<li><a href="javaScript:changeContent('retos')">Retos</a></li>
				<li><hr></li>
				<li><a href="#!">Configuraci&oacute;n</a></li>
				<li><a href="operations.php?op=logout">Cerrar sesi&oacute;n</a></li>
			</ul>
			<a href="#" data-activates="slide-out" class="button-collapse"><i class="mdi-navigation-menu"></i></a>
			<div class="nav-wrapper">
				<a href="#" class="brand-logo center"><img class="logo-central" src="resources/img/logo-2-negativo.png"></a>
				<ul id="nav-mobile" class="left hide-on-med-and-down">
					<li><a href="javaScript:void(0)" id="inicio-but">Inicio</a></li>
					<li><a href="javaScript:void(0)" id="mapa-but">Mapa</a></li>
					<li><a href="javaScript:void(0)" id="planes-but">Planes</a></li>
					<li><a href="javaScript:void(0)" id="retos-but">Retos</a></li>
				</ul>
				<ul id="nav-mobile" class="right hide-on-med-and-down">
					
					<li><a style="height:64px;" class='dropdown-button' href="javascript:void(0)" data-beloworigin="true"  data-activates='dropdown1'><img class="foto-perfil" src="resources/img/usuario-provisional.jpg"></img></a></li>
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
	<script type="text/javascript" src="resources/js/jquery.form.min.js"></script>
	<script type="text/javascript" src="resources/js/materialize.min.js"></script>
	<script type="text/javascript" src="resources/js/functions.js"></script>
	<script type="text/javascript" src="resources/js/nouislider.min.js"></script>
	<script>
		function changeContent(page){
			$.ajax({
				url: "pages/"+page+".html",
				success: function(res) {
					$("#formcontent").html(res);
				}
			});
		}
		$(document).ready(function(){
			  $('.button-collapse').sideNav({
					  menuWidth: 300, // Default is 240
					  edge: 'left', // Choose the horizontal origin
					  closeOnClick: true // Closes side-nav on <a> clicks, useful for Angular/Meteor
					}
				  );
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
			$( "#slider-range" ).slider({
				range: true,
				min: 0,
				max: 500,
				values: [ 75, 300 ],
				slide: function( event, ui ) {
					$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
				}
			});
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