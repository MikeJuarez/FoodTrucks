function saveRestaurant() {
	"use strict";

	//var response = JSON.parse(Get("https://maps.googleapis.com/maps/api/geocode/json?address="));


	var $latitude = "";
	var $longitude = "";

	var $email = "@email.com";

	var dbRef = firebase.database().ref('food_trucks');
	$("#submit").click(function () {
		var url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + $("#address").val() + "," + $("#city").val() + "," + $("#state").val() + "," + $("#zip").val() + "&" + "key=AIzaSyCC4_9U_rU9oprUFpqPp9C9RbjuB8oAHsk";
		alert(url);
		var response = JSON.parse(Get(url));

		var $mon_open = "null";
		var $tue_open = "null";
		var $wed_open = "null";
		var $thur_open = "null";
		var $fri_open = "null";
		var $sat_open = "null";
		var $sun_open = "null";

		var $mon_close = "null";
		var $tue_close = "null";
		var $wed_close = "null";
		var $thur_close = "null";
		var $fri_close = "null";
		var $sat_close = "null";
		var $sun_close = "null";

		if ($("#monday_open").val() !== null) {
			$mon_open = $("#monday_open").val();
		} else {
			$("#monday_close").val("");
		}

		if ($("#tuesday_open").val() !== null) {
			$tue_open = $("#tuesday_open").val();
		} else {
			$("#tuesday_close").val("");
		}

		if ($("#wednesday_open").val() !== null) {
			$wed_open = $("#wednesday_open").val();
		} else {
			$("#wednesday_close").val("");
		}

		if ($("#thursday_open").val() !== null) {
			$thur_open = $("#thursday_open").val();
		} else {
			$("#thursday_close").val("");
		}

		if ($("#friday_open").val() !== null) {
			$fri_open = $("#friday_open").val();
		} else {
			$("#friday_close").val("");
		}

		if ($("#saturday_open").val() !== null) {
			$sat_open = $("#saturday_open").val();
		} else {
			$("#saturday_close").val("");
		}

		if ($("#sunday_open").val() !== null) {
			$sun_open = $("#sunday_open").val();
		} else {
			$("#sunday_close").val("");
		}

		if ($("#monday_close").val() !== null) {
			$mon_close = $("#monday_close").val();
		} else {
			mondayclosed();
			$("#monday_open").val("");
			$mon_open = "null";
		}

		if ($("#tuesday_close").val() !== null) {
			$tue_close = $("#tuesday_close").val();
		} else {
			tuesdayclosed();
			$("#tuesday_open").val("");
			$tue_open = "null";
		}

		if ($("#wednesday_close").val() !== null) {
			$wed_close = $("#wednesday_close").val();
		} else {
			wednesdayclosed();
			$("#wednesday_open").val("");
			$wed_open = "null";
		}

		if ($("#thursday_close").val() !== null) {
			$thur_close = $("#thursday_close").val();
		} else {
			thursdayclosed();
			$("#thursday_open").val("");
			$thur_open = "null";
		}

		if ($("#friday_close").val() !== null) {
			$fri_close = $("#friday_close").val();
		} else {
			fridayclosed();
			$("#friday_open").val("");
			$fri_open = "null";
		}

		if ($("#saturday_close").val() !== null) {
			$sat_close = $("#saturday_close").val();
		} else {
			saturdayclosed();
			$("#saturday_open").val("");
			$sat_open = "null";
		}

		if ($("#sunday_close").val() !== null) {
			$sun_close = $("#sunday_close").val();
		} else {
			sundayclosed();
			$("#sunday_open").val("");
			$sun_open = "null";
		}

		var $open_time = [
			$mon_open,
			$tue_open,
			$wed_open,
			$thur_open,
			$fri_open,
			$sat_open,
			$sun_open
		];


		var $close_time = ["", "", "", "", "", "", ""];
		$close_time = [
			$mon_close,
			$tue_close,
			$wed_close,
			$thur_close,
			$fri_close,
			$sat_close,
			$sun_close
		];


		var $newMessageRef = dbRef.push();
		var $newMessageRefKey = $newMessageRef.key;
		
		$latitude = (response.results[0].geometry.location.lat);
		$longitude = (response.results[0].geometry.location.lng);

		var firebaseRef = firebase.database().ref();
		var geoFire = new GeoFire(firebaseRef.child("path/to/geofire"));		

		geoFire.set($newMessageRefKey, [$latitude, $longitude]);

		// Create a storage reference from our storage service
		var storage = firebase.storage();
		var storageRef = storage.ref();

		var $image_file = $("#image").prop('files')[0];
		var $menu_file = $("#menu_image").prop('files')[0];

		var $foodspot_url = "";
		var $menu_url = "";

		$newMessageRef.set({
			description: $("#description").val(),
			email: $email,
			name: $("#name").val(),
			address: $("#address").val(),
			city: $("#city").val(),
			state: $("#state").val(),
			zip: $("#zip").val(),
			lat: $latitude,
			lng: $longitude,
			website: $("#website").val(),
			food_type: $("#food_type").val(),
			open_time: $open_time,
			close_time: $close_time,
			image_location: $foodspot_url,
			menu_location: $menu_url
		});

		var imagesRef = storageRef.child('foodspot_images/' + $newMessageRefKey);
		var menuRef = storageRef.child('menu_images/' + $newMessageRefKey);

		imagesRef.put($image_file).then(function (snapshot) {
			$foodspot_url = snapshot.downloadURL;

			$newMessageRef.update({
				image_location: $foodspot_url
			});
		});

		menuRef.put($menu_file).then(function (snapshot) {
			$menu_url = snapshot.downloadURL;
			$newMessageRef.update({
				menu_location: $menu_url
			});
		});
	});
}

function Get(yourUrl) {
	"use strict";
	var Httpreq = new XMLHttpRequest(); // a new request
	Httpreq.open("GET", yourUrl, false);
	Httpreq.send(null);
	return Httpreq.responseText;
}

$(document).ready(saveRestaurant);
