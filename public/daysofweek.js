function daysofweek() {
	"use strict";

	mondayclosed();
	tuesdayclosed();
	wednesdayclosed();
	thursdayclosed();
	fridayclosed();
	saturdayclosed();
	sundayclosed();

	$("#monday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#monday_open').prop("disabled", false);
			$('#monday_close').prop("disabled", false);
		} else {
			mondayclosed();
		}
	});

	$("#tuesday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#tuesday_open').prop("disabled", false);
			$('#tuesday_close').prop("disabled", false);
		} else {
			tuesdayclosed();
		}
	});

	$("#wednesday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#wednesday_open').prop("disabled", false);
			$('#wednesday_close').prop("disabled", false);
		} else {
			wednesdayclosed();
		}
	});

	$("#thursday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#thursday_open').prop("disabled", false);
			$('#thursday_close').prop("disabled", false);
		} else {
			thursdayclosed();
		}
	});

	$("#friday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#friday_open').prop("disabled", false);
			$('#friday_close').prop("disabled", false);
		} else {
			fridayclosed();
		}
	});

	$("#saturday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#saturday_open').prop("disabled", false);
			$('#saturday_close').prop("disabled", false);
		} else {
			saturdayclosed();
		}
	});

	$("#sunday_checkbox").click(function () {
		if ($(this).is(":checked")) {
			$('#sunday_open').prop("disabled", false);
			$('#sunday_close').prop("disabled", false);
		} else {
			sundayclosed();
		}
	});
}

function mondayclosed() {
	"use strict";
	$('#monday_open').prop("disabled", true);
	$('#monday_close').prop("disabled", true);
	$('#monday_open').val("");
	$('#monday_close').val("");
	$('#monday_checkbox').prop("checked", false);
}

function tuesdayclosed() {
	"use strict";
	$('#tuesday_open').prop("disabled", true);
	$('#tuesday_close').prop("disabled", true);
	$('#tuesday_open').val("");
	$('#tuesday_close').val("");
	$('#tuesday_checkbox').prop("checked", false);
}

function wednesdayclosed() {
	"use strict";
	$('#wednesday_open').prop("disabled", true);
	$('#wednesday_close').prop("disabled", true);
	$('#wednesday_open').val("");
	$('#wednesday_close').val("");
	$('#wednesday_checkbox').prop("checked", false);
}

function thursdayclosed() {
	"use strict";
	$('#thursday_open').prop("disabled", true);
	$('#thursday_close').prop("disabled", true);
	$('#thursday_open').val("");
	$('#thursday_close').val("");
	$('#thursday_checkbox').prop("checked", false);
}

function fridayclosed() {
	"use strict";
	$('#friday_open').prop("disabled", true);
	$('#friday_close').prop("disabled", true);
	$('#friday_open').val("");
	$('#friday_close').val("");
	$('#friday_checkbox').prop("checked", false);
}

function saturdayclosed() {
	"use strict";
	$('#saturday_open').prop("disabled", true);
	$('#saturday_close').prop("disabled", true);
	$('#saturday_open').val("");
	$('#saturday_close').val("");
	$('#saturday_checkbox').prop("checked", false);
}

function sundayclosed() {
	"use strict";
	$('#sunday_open').prop("disabled", true);
	$('#sunday_close').prop("disabled", true);
	$('#sunday_open').val("");
	$('#sunday_close').val("");
	$('#sunday_checkbox').prop("checked", false);
}

$(document).ready(daysofweek);
