
$(document).ready(function() {

	// Append the option elements
	/*for ( var i = 0; i <= 100; i++ ){

		var option = document.createElement("option");
			option.text = i;
			option.value = i;

		select.appendChild(option);
	}*/
	
	var html5Slider = document.getElementById('distancia');
	var inputNumber = document.getElementById('input-number1');
	var inputNumber2 = document.getElementById('input-number2');

	noUiSlider.create(html5Slider, {
		start: [ 0, 20 ],
		connect: true,
		range: {
			'min': 0,
			'max': 100
		}
	});

	html5Slider.noUiSlider.on('update', function( values, handle ) {

		var value = values[handle];

		if ( handle ) {
			inputNumber.value = value;
		} else {
			inputNumber2.value = Math.round(value);
		}
	});

	inputNumber2.addEventListener('change', function(){
		html5Slider.noUiSlider.set([this.value, null]);
	});

	inputNumber.addEventListener('change', function(){
		html5Slider.noUiSlider.set([null, this.value]);
	});
	
	function timestamp(str){
		return new Date(str).getTime();   
	}
	
	var dateSlider = document.getElementById('slider-date');

	noUiSlider.create(dateSlider, {
		range: {
			min: timestamp('2015'),
			max: timestamp('2020')
		},
		step: 7 * 24 * 60 * 60 * 1000,
		start: [ timestamp('2015'), timestamp('2018') ],

		format: wNumb({
			decimals: 0
		})
	})
	var dateValues = [
		document.getElementById('event-start'),
		document.getElementById('event-end')
	];

	dateSlider.noUiSlider.on('update', function( values, handle ) {
		dateValues[handle].innerHTML = formatDate(new Date(+values[handle]));
	});
	var
		weekdays = [
			"Domingo", "Lunes", "Martes",
			"Miércoles", "Jueves", "Viernes",
			"Sábado"
		],
		months = [
			"Enero", "Febrero", "Marzo",
			"Abril", "Mayo", "Junio", "Julio",
			"Augosto", "Septiembre", "Octubre",
			"Noviembre", "Diciembre"
		];

	// Append a suffix to dates.
	// Example: 23 => 23rd, 1 => 1st.
	

	// Create a string representation of the date.
	function formatDate ( date ) {
		return weekdays[date.getDay()] + ", " +
			date.getDate()+ " " +
			months[date.getMonth()] + " " +
			date.getFullYear();
	}
});

	