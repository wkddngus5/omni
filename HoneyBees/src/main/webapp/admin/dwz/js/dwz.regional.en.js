/**
 * @author 张慧华 z@j-ui.com
 */
(function($){
	// jQuery validate
	if ($.validator) {
		$.extend($.validator.messages, {
			required: "Required fields",
			remote: "Please correct this field",
			email: "Please enter a valid email",
			url: "Please enter a valid web address",
			date: "Please enter a valid date",
			dateISO: "Please enter a valid date (ISO)",
			number: "Please enter a valid number",
			digits: "You can only enter an integer",
			creditcard: "Please enter a valid credit card",
			equalTo: "Please enter the same value again",
			accept: "Please enter a string with a legal suffix",
			maxlength: $.validator.format("A string of up to {0} length"),
			minlength: $.validator.format("A string of at least {0} length"),
			rangelength: $.validator.format("A string of length between {0} and {1}"),
			range: $.validator.format("Please enter a value between {0} and {1}"),
			max: $.validator.format("Please enter a value up to {0}"),
			min: $.validator.format("Please enter a minimum value of {0}"),
			alphanumeric: "Letters, numbers, underlines",
			lettersonly: "Must be a letter",
			phone: "Numeric, space, parentheses"
		});
	}
	
	// DWZ regional
	$.setRegional("datepicker", {
		dayNames: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		monthNames: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	});
	$.setRegional("alertMsg", {
		title:{error:"error", info:"tips", warn:"warn", correct:"success", confirm:"confirm tips"},
		butMsg:{ok:"cofirm", yes:"yes", no:"no", cancel:"cancel"}
	});
})(jQuery);