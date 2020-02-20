$(document).on('submit', '.signupForm', function(e) {
	e.preventDefault();

	if($("#password").val() != $("#confirmPassword").val()){
		swal("Please, confirm your password again.");
		return false;
	}
	
	var volunteer = {};
	volunteer.username = $("#username").val();
	volunteer.surname = $("#last_name").val();
	volunteer.name = $("#first_name").val();
	volunteer.password = $("#password").val();
	volunteer.phoneNumber = $("#phoneNumber").val();
	volunteer.email = $("#email").val();
	var area = {};
	area.name = $("#area").val();
	volunteer["area"] = {};
	volunteer["area"] = area;
	
	$.ajax({
		type : 'POST',
		url : "../WebProject/rest/users/signup",
		contentType : 'application/json',
		dataType : "json",
		data : JSON.stringify(volunteer),
		processData: false,
		success : function(data) {
			swal("Success", "You have successfully signed up!", "success");
			setTimeout(function (){
				window.location.href = 'index.html';
				}, 3000); 
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			swal("Try again", "User with the same username already exists!", "error");
		}
	});
});

$(document).on('submit', '.profileForm', function(e) {
	e.preventDefault();

	if($("#passwordChange").val() != $("#confirmPasswordChange").val()){
		swal("Please, confirm your password again.");
		return false;
	}
	
	var user = {};
	user.id = $("#id").val();
	user.name = $("#nameChange").val();
	user.surname = $("#lastNameChange").val();
	user.username = $("#usernameChange").val();
	user.password = $("#passwordChange").val();
	user.phoneNumber = $("#phoneNumberChange").val();
	user.email = $("#emailChange").val();
	
	var area = {};
	area.name = $("#areaNameChange").val();
	user["area"] = {};
	user["area"] = area;
	
	$.ajax({
		type : 'POST',
		url : "../WebProject/rest/users/changeProfile",
		contentType : 'application/json',
		dataType : "json",
		data : JSON.stringify(user),
		processData: false,
		success : function(data) {
			swal("Success", "", "success");
		},
		error: function(data) {
			swal("Try again", "User with the same username already exists", "error");
		}
	});
});