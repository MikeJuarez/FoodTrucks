/*funtion writeNewPost() {
	var postData = {
		name: name,
		address: address,
		days_openn: days_open,
		food_type: food_type,
		image_location: image_location,
		lat: lat,
		longitude: longitude,
		menu_location: menu_location,
		time_open: time_open,
		time_closed: time_closed,
		type: type,
		website: website		
	};
	
	var newPostKey = firebase.database().ref().child('food_trucks').push.key;
}*/


init();

function init() {
	"use strict";
	
var config = {	
    apiKey: "AIzaSyDxF17BhW1gjKXUxobFZqgo2rDQ7u_rb0c",
    authDomain: "foodtrucksandmore.firebaseapp.com",
    databaseURL: "https://foodtrucksandmore.firebaseio.com",
    projectId: "foodtrucksandmore",
    storageBucket: "foodtrucksandmore.appspot.com",
    messagingSenderId: "158761242293"
  };
	  
  firebase.initializeApp(config);
	
  var storage = firebase.storage();
}
