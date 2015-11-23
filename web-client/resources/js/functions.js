/*GENERIC FUNCTIONS*/
function changeLayer(layer, page, action, callback){
	switch (action){
		case "change":
			$.ajax({
				url: page,
				success: function(res) {
					$(layer).html(res);
					if (typeof callback === "function") callback();
				},
				error: function(res) {
					Materialize.toast('Se ha producido un error', 3000);
					console.log(res);
				}
			});
		break;
		case "append":
			$.ajax({
				url: page,
				success: function(res) {
					$(layer).append(res);
					if (typeof callback === "function") callback();
				},
				error: function(res) {
					Materialize.toast('Se ha producido un error', 3000);
					console.log(res);
				}
			});
		break;
		case "prepend":
			$.ajax({
				url: page,
				success: function(res) {
					$(layer).prepend(res);
					if (typeof callback === "function") callback();
				},
				error: function(res) {
					Materialize.toast('Se ha producido un error', 3000);
					console.log(res);
				}
			});
		break;
		default:
			Materialize.toast('No tienes permitido hacer eso', 3000);
		break;
	}
	return false;
}
/*OUTTER FUNCTIONS*/
function changeLRLayer(type){
	if (type == "register"){
		if (changeLayer('#lr-layer', 'pages/register.html', 'change', function() {
			$("#lr-button").html('<a href="javascript:void(0)" onclick="changeLRLayer(\'login\')" class="waves-effect waves-light btn black white-text"><i class="material-icons left">account_box</i>Entra</a>');
		}));
	} else if (type == "login"){
		if (changeLayer('#lr-layer', 'pages/login.html', 'change', function() {
			$("#lr-button").html('<a href="javascript:void(0)" onclick="changeLRLayer(\'register\')" class="waves-effect waves-light btn black white-text"><i class="material-icons left">add_box</i>Registrate!</a>');
		}));
	} else if (type == "recpass"){
		if (changeLayer('#lr-layer', 'pages/recpass.html', 'change', function() {
			$("#lr-button").html('<a href="javascript:void(0)" onclick="changeLRLayer(\'login\')" class="waves-effect waves-light btn black white-text"><i class="material-icons left">account_box</i>Entra</a>');
		}));
	} else Materialize.toast('No tienes permitido hacer eso', 3000);
}