$(document).on('submit', '.emergencyForm', function(e){
	e.preventDefault();
 
	var emergencySituation = {};
	emergencySituation.placeName = $("#placeName").val();
	emergencySituation.township = $("#township").val();
	emergencySituation.description = $("#desc").val();
	emergencySituation.address = $("#address").val();
	var area = {};
	area.name = $("#areaSelect").val();
	emergencySituation["area"] = {};
	emergencySituation["area"] = area;
	emergencySituation.urgency = $("#urgencySelect").val();
	var volunteer = {};
	if($("#volunteersSelect").val() != null){
		volunteer.id = $("#volunteersSelect").val();
		emergencySituation["volunteer"] = {};
		emergencySituation["volunteer"] = volunteer;
	}else{
		volunteer.id = 0;
		emergencySituation["volunteer"] = {};
		emergencySituation["volunteer"] = volunteer;
	}
	
	$.ajax({
		type: 'POST',
		url : "../WebProject/rest/emergencies/add",
		contentType : 'application/json',
		dataType : "json",
		data : JSON.stringify(emergencySituation),
		processData : false,
		success : function(data){
			location.reload();
		},
		error : function(err){
			swal("Sorry,", "You can not add a new emergency situation!", "error");
		}
	});
});

$(document).on('submit', '.changeVolunteerForm', function(e){
	e.preventDefault();
	
	var emergency = {};
	emergency.id = $("#emergencyId").val();
	var volunteer = {};
	volunteer.id = $("#changeVolunteerSelect").val();
	emergency["volunteer"] = {};
	emergency["volunteer"] = volunteer;
	
	$.ajax({
			type : 'POST',
			url : "../WebProject/rest/emergencies/changeVolunteer",
			contentType : 'application/json',
			dataType : "json",
			data : JSON.stringify(emergency),
			processData: false,
			success: function(data){
				 $("#changeVolunteerModal").modal("hide");
				 location.reload();
			},
			error: function(e){
				swal("Error", "", "error");
			}
	 });
});

function loadInformationsAboutUser(){
	$.ajax({
		type: "GET",
		url: '../WebProject/rest/users/loggedUser',
		success: function(user){
			var active;
			if(user.blocked == false){
				active = "active";
			}else{
				active = "blocked"
			}
			document.getElementById("title").innerHTML  = user.name + " " + user.surname + " ";
			document.getElementById("active").innerHTML = active;
			
			document.getElementById("id").value = user.id;
			document.getElementById("nameChange").value = user.name;
			document.getElementById("lastNameChange").value = user.surname;
			document.getElementById("usernameChange").value = user.username;
			document.getElementById("passwordChange").value = user.password;
			document.getElementById("confirmPasswordChange").value = user.password;
			document.getElementById("phoneNumberChange").value = user.phoneNumber;
			document.getElementById("emailChange").value = user.email;
			document.getElementById("areaNameChange").value = user.area.name;
			document.getElementById("photoChange").value = user.photo;
		}
  	});
}
	 
function fillVolunteersSelect(){
	var areaName = $("#areaSelect").val();
	$("#volunteersSelect").empty();
	$.ajax({
		type: "GET",
		url: "../WebProject/rest/users/volunteers/" + areaName,
		success: function(users){
			var select = document.getElementById("volunteersSelect");
			document.getElementById("info").innerHTML = "";
			var opt = document.createElement("OPTION");
				select.add(opt);
				opt.text = " ";
				opt.selected = true;
				opt.value = 0;
			$.each(users, function(i, user){
				var option = document.createElement("OPTION");
				select.add(option);
				option.value = user.id;
				option.text = user.name + " " + user.surname;
			});
		},
		error: function(e){
			document.getElementById("info").innerHTML = "(The volunteer in this territory still does not exist! You can then post a volunteer for this emergency situation.)";
		}
	});
}

 function checkUser(){
		$.ajax({
			type: "GET",
			url: "../WebProject/rest/users/loggedUser",
			success: function(user){
				if(user.userType == "VOLUNTEER"){
					document.getElementById("volunteersSelect").disabled = true;
				}
			}
		});
		
	}
 
 function changeToArchived(id){
		var em = {};
		em.id = id;
	    em.active = false;
	    
	    $.ajax({
			type : 'POST',
			url : "../WebProject/rest/emergencies/changeActivity",
			contentType : 'application/json',
			dataType : "json",
			data : JSON.stringify(em),
			processData: false,
			success: function(data){
				location.reload();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				swal("Error", "", "error");
			}
		});
	}
 
 function listVolunteers(areaNameemId){
	 	$("#changeVolunteerSelect").empty();
	 	var areaName = areaNameemId.split(",")[0];
	 	var emId = areaNameemId.split(",")[1];
	 	document.getElementById("emergencyId").value = emId;
		$.ajax({
			type: "GET",
			url: "../WebProject/rest/users/volunteers/" + areaName,
			success: function(users){
				var select = document.getElementById("changeVolunteerSelect");
				$.each(users, function(i, user){
					var option = document.createElement("OPTION");
					select.add(option);
					option.value = user.id;
					option.text = user.name + " " + user.surname + " (" + user.username + ")";
				});
			},
			error: function(e){
				swal("The volunteer in this territory still does not exist!");
			}
		});
 }
 
 function addComment(emId){
	 var comment = {};
	 var emergency = {};
	 emergency.id = emId;
	 comment["emergencySituation"] = {};
	 comment["emergencySituation"] = emergency;
	 var text = $("#text" + emId).val();
	 comment.text = text;
	 if(text == ""){
		 return false;
	 }
	 $.ajax({
			type : 'POST',
			url : "../WebProject/rest/comments/add",
			contentType : 'application/json',
			dataType : "json",
			data : JSON.stringify(comment),
			processData: false,
			success: function(data){
				location.reload();
			},
			error: function(e) {
				swal("Sorry", "You can not comment!", "error");
			}
		});
 }

 function addCommenta(emId){
	 var comment = {};
	 var emergency = {};
	 emergency.id = emId;
	 comment["emergencySituation"] = {};
	 comment["emergencySituation"] = emergency;
	 var text = $("#texta" + emId).val();
	 comment.text = text;
	 if(text == ""){
		 return false;
	 }
	 $.ajax({
			type : 'POST',
			url : "../WebProject/rest/comments/add",
			contentType : 'application/json',
			dataType : "json",
			data : JSON.stringify(comment),
			processData: false,
			success: function(data){
				location.reload();
			},
			error: function(e) {
				swal("Sorry", "You can not comment!", "error");
			}
		});
 }
 
 function logout(){
	 $.ajax({
			type: "GET",
			url: "../WebProject/rest/users/logout"
	 }); 
 }
 
 function getfilterOption(){
	 var value = $("#filterOption").val();
	 $("#criteriaSelect").empty();
	 var select = document.getElementById("criteriaSelect");
	 if(value == "urgency"){
		var option = document.createElement("OPTION");
		select.add(option);
		option.value = "red";
		option.text = "RED";
		$("#criteriaSelect").add(option);
		option = document.createElement("OPTION");
		select.add(option);
		option.value = "orange";
		option.text = "ORANGE";
		$("#criteriaSelect").add(option);
		option = document.createElement("OPTION");
		select.add(option);
		option.value = "blue";
		option.text = "BLUE";
		$("#criteriaSelect").add(option);
	 }else if(value == "area"){
		 $.ajax({
				type: "GET",
				url: "../WebProject/rest/areas/getAllAreas",
				success: function(areas){
					var select = document.getElementById("criteriaSelect");
					$.each(areas, function(i, area){
						var option = document.createElement("OPTION");
						select.add(option);
						option.value = area.id;
						option.text = area.name;
					});
				},
				error: function(e){
					swal("Areas still do not exist.");
				}
			});
	 }
 }
 
 function filter(){
	 var criterion1 = $("#filterOption").val();
	 var criterion2 = $("#criteriaSelect").val();
	 var criterion = criterion1 + "," + criterion2;
	 $.ajax({
			type: "GET",
			url: "../WebProject/rest/emergencies/filter/" + criterion,
			success: function(emergencies){
				document.getElementById("emergencies").innerHTML = " ";
				$.ajax({
					type: "GET",
					url: "../WebProject/rest/users/loggedUser",
					success: function(user){
						
							if(user.userType == "VOLUNTEER"){

								$.each(emergencies, function(i, em){
							
									if(em.volunteer != null && em.volunteer.username == user.username){
										var volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
										
										var date = em.datetime.split("T")[0];
										var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
										
										var panel = "";
										if(em.urgency == "BLUE"){
											panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
										}else if(em.urgency == "ORANGE"){
											panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
										}else{
											panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
										}
										panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '</div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div><br>';
										panel += '<div class="row"><div class="col-sm-9"><textarea id="text' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addComment(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
										panel += '<div id="body' + em.id + '"></div></div></div>';
										$("#emergencies").append(panel);
										
										$.ajax({
											type: "GET",
											url: "../WebProject/rest/comments/emergency/" + em.id,
											success: function(comments){
												$.each(comments, function(i, comment){
													var date = comment.date.split("T")[0];
													var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
													
													var com = '';
													com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
													com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
													$("#body" + em.id).append(com);
												});
											}
										});
									}
								});
							}else{
								$.each(emergencies, function(i, em){
									var volunteer = "";
									if(em.volunteer != null){
										volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
									}
									var date = em.datetime.split("T")[0];
									var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
									
									var panel = "";
									if(em.urgency == "BLUE"){
										panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
									}else if(em.urgency == "ORANGE"){
										panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
									}else{
										panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
									}
									panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Archived&nbsp;<input type="radio" id="archived" name="archived" class="big" value="archived" onclick="changeToArchived(' + em.id + ');"/>';
									panel += '<button id="listVolunteers" class="btn btn-primary" onclick="listVolunteers(\'' + em.area.name + ',' + em.id + '\');" data-toggle="modal" data-target="#changeVolunteerModal">Change volunteer</button></div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div>';
									panel += '<div class="row"><div class="col-sm-9"><textarea id="texta' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addCommenta(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
									panel += '<div id="bodya' + em.id + '"></div></div></div>';
									$("#emergencies").append(panel);
									
									$.ajax({
										type: "GET",
										url: "../WebProject/rest/comments/emergency/" + em.id,
										success: function(comments){
											$.each(comments, function(i, comment){
												var date = comment.date.split("T")[0];
												var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
												
												var com = '';
												com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
												com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
												$("#bodya" + em.id).append(com);
											});
										}
									});
								});
							}
					}
				});
			},
			error: function(e){
				swal("Emergency with this criterion does not exist.");
			}
		});
 }

 function search(){
	 
	 var criterion1 = $("#searchSelect").val();
	 var criterion2 = $("#searchInput").val();
	 var criterion = criterion1 + "," + criterion2;
	 
	 $.ajax({
			type: "GET",
			url: "../WebProject/rest/emergencies/search/" + criterion,
			success: function(emergencies){
				document.getElementById("emergencies").innerHTML = " ";
				$.ajax({
					type: "GET",
					url: "../WebProject/rest/users/loggedUser",
					success: function(user){
						
							if(user.userType == "VOLUNTEER"){

								$.each(emergencies, function(i, em){
							
									if(em.volunteer != null && em.volunteer.username == user.username){
										var volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
										
										var date = em.datetime.split("T")[0];
										var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
										
										var panel = "";
										if(em.urgency == "BLUE"){
											panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
										}else if(em.urgency == "ORANGE"){
											panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
										}else{
											panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
										}
										panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '</div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div><br>';
										panel += '<div class="row"><div class="col-sm-9"><textarea id="text' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addComment(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
										panel += '<div id="body' + em.id + '"></div></div></div>';
										$("#emergencies").append(panel);
										
										$.ajax({
											type: "GET",
											url: "../WebProject/rest/comments/emergency/" + em.id,
											success: function(comments){
												$.each(comments, function(i, comment){
													var date = comment.date.split("T")[0];
													var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
													
													var com = '';
													com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
													com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
													$("#body" + em.id).append(com);
												});
											}
										});
									}
								});
							}else{
								$.each(emergencies, function(i, em){
									var volunteer = "";
									if(em.volunteer != null){
										volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
									}
									var date = em.datetime.split("T")[0];
									var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
									
									var panel = "";
									if(em.urgency == "BLUE"){
										panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
									}else if(em.urgency == "ORANGE"){
										panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
									}else{
										panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
									}
									panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Archived&nbsp;<input type="radio" id="archived" name="archived" class="big" value="archived" onclick="changeToArchived(' + em.id + ');"/>';
									panel += '<button id="listVolunteers" class="btn btn-primary" onclick="listVolunteers(\'' + em.area.name + ',' + em.id + '\');" data-toggle="modal" data-target="#changeVolunteerModal">Change volunteer</button></div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div>';
									panel += '<div class="row"><div class="col-sm-9"><textarea id="texta' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addCommenta(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
									panel += '<div id="bodya' + em.id + '"></div></div></div>';
									$("#emergencies").append(panel);
									
									$.ajax({
										type: "GET",
										url: "../WebProject/rest/comments/emergency/" + em.id,
										success: function(comments){
											$.each(comments, function(i, comment){
												var date = comment.date.split("T")[0];
												var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
												
												var com = '';
												com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
												com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
												$("#bodya" + em.id).append(com);
											});
										}
									});
								});
							}
					}
				});
			}
	 });
 }
 
 function filterSearch(){
	 
	 var criterion1 = $("#filterOption").val();
	 var criterion2 = $("#criteriaSelect").val();
	 var criterion3 = $("#searchSelect").val();
	 var criterion4 = $("#searchInput").val();

	 var criterion = criterion1 + "," + criterion2 + "," + criterion3 + "," + criterion4;
	 $.ajax({
			type: "GET",
			url: "../WebProject/rest/emergencies/filterSearch/" + criterion,
			success: function(emergencies){
				document.getElementById("emergencies").innerHTML = " ";
				$.ajax({
					type: "GET",
					url: "../WebProject/rest/users/loggedUser",
					success: function(user){
						
							if(user.userType == "VOLUNTEER"){

								$.each(emergencies, function(i, em){
							
									if(em.volunteer != null && em.volunteer.username == user.username){
										var volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
										
										var date = em.datetime.split("T")[0];
										var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
										
										var panel = "";
										if(em.urgency == "BLUE"){
											panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
										}else if(em.urgency == "ORANGE"){
											panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
										}else{
											panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
										}
										panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '</div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div><br>';
										panel += '<div class="row"><div class="col-sm-9"><textarea id="text' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addComment(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
										panel += '<div id="body' + em.id + '"></div></div></div>';
										$("#emergencies").append(panel);
										
										$.ajax({
											type: "GET",
											url: "../WebProject/rest/comments/emergency/" + em.id,
											success: function(comments){
												$.each(comments, function(i, comment){
													var date = comment.date.split("T")[0];
													var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
													
													var com = '';
													com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
													com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
													$("#body" + em.id).append(com);
												});
											}
										});
									}
								});
							}else{
								$.each(emergencies, function(i, em){
									var volunteer = "";
									if(em.volunteer != null){
										volunteer = em.volunteer.name + ' ' + em.volunteer.surname + '(username: ' + em.volunteer.username + ')';
									}
									var date = em.datetime.split("T")[0];
									var time = ((em.datetime.split("T")[1]).split("Z")[0]).split(".")[0];
									
									var panel = "";
									if(em.urgency == "BLUE"){
										panel += '<div class="panel panel-info col-md-6" style="width:90%;margin-left:60px;">';
									}else if(em.urgency == "ORANGE"){
										panel += '<div class="panel panel-warning col-md-6" style="width:90%;margin-left:60px;">';
									}else{
										panel += '<div class="panel panel-danger col-md-6" style="width:90%;margin-left:60px;">';
									}
									panel += '<div class="panel-heading">Date and time: ' + date + " " + time + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Archived&nbsp;<input type="radio" id="archived" name="archived" class="big" value="archived" onclick="changeToArchived(' + em.id + ');"/>';
									panel += '<button id="listVolunteers" class="btn btn-primary" onclick="listVolunteers(\'' + em.area.name + ',' + em.id + '\');" data-toggle="modal" data-target="#changeVolunteerModal">Change volunteer</button></div><div class="panel-body"><div class="row"><div class="col-md-6"><label>Address: ' + em.address + '<br>Area: ' + em.area.name + '<br>Place of emergency: ' + em.placeName + '<br>Description: ' + em.description + '<br>Volunteer: ' + volunteer + '</label></div></div>';
									panel += '<div class="row"><div class="col-sm-9"><textarea id="texta' + em.id + '" rows="2" placeholder="Add a comment..."></textarea>&nbsp;<button onclick="addCommenta(\'' + em.id + '\')" class="btn btn-success green"><i class="fa fa-share"></i>Post</button></div></div><br>';
									panel += '<div id="bodya' + em.id + '"></div></div></div>';
									$("#emergencies").append(panel);
									
									$.ajax({
										type: "GET",
										url: "../WebProject/rest/comments/emergency/" + em.id,
										success: function(comments){
											$.each(comments, function(i, comment){
												var date = comment.date.split("T")[0];
												var time = ((comment.date.split("T")[1]).split("Z")[0]).split(".")[0];
												
												var com = '';
												com += '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="images/' + comment.user.photo + '"></div></div>';
												com += '<div class="col-sm-9"><div class="panel panel-default"><div class="panel-heading"><strong>' + comment.user.name + ' ' + comment.user.surname + '</strong><span class="text-muted"> commented at ' + time + ' ' + date + '</span></div><div class="panel-body" id="commentBody">' + comment.text + '</div></div></div></div><br>';
												$("#bodya" + em.id).append(com);
											});
										}
									});
								});
							}
						}
				});
			}
	 });
 }
 