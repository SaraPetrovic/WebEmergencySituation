$.ajax({
		type : 'GET',
		url : "../WebProject/rest/areas/getAllAreas",
		dataType : "json",
		success : function(areas) {
			$.each(areas, function(index, area) {
				var panel = '<div class="panel panel-default" style="width:90%; margin-left:60px;">';
				panel += '<div class="panel-heading"><h3>' + area.name + '</h3></div>';
				panel += '<div class="panel-body">&nbsp; Population: ' + area.population + '&nbsp;&nbsp;&nbsp;Surface area: ' + area.surface + '<button id="change" onclick="fillFormForChangeArea('+area.id+');" value="' + area.id + '" data-toggle="modal" data-target="#changeAreaModal" style="margin-left:80%;" class="btn btn-primary">Change</button>&nbsp;<button id="delete" onclick="deleteArea('+area.id+');" value="' + area.id + '" class="btn btn-danger">Delete</button>';
				
				panel += '</div></div>';
				$('#group').append(panel);
			});
		}
	});


$(document).on('submit', '.areaForm', function(e) {
	e.preventDefault(); // if you're using AJAX
    
	var area = {};
	area.name = $("#areaName").val();
	area.surface = $("#surface").val();
	area.population = $("#population").val();
	
		$.ajax({
			type : 'POST',
			url : "../WebProject/rest/areas/add",
			contentType : 'application/json',
			dataType : "json",
			processData : false,
			data : JSON.stringify(area),
			success : function(data) {
				location.reload();
			},
			error : function() {
				swal("Try again", "Area with the same name already exists", "error");
			}
		});
});

$(document).on('submit', '.changeAreaForm', function(e) {
	e.preventDefault(); // if you're using AJAX
	    
	var area = {};
	area.id = $("#idArea").val();
	area.name = $("#changeAreaName").val();
	area.surface = $("#changeSurface").val();
	area.population = $("#changePopulation").val();

	$.ajax({
		type : 'POST',
		url : "../WebProject/rest/areas/change",
		contentType: 'application/json',
		dataType : "json",
		data: JSON.stringify(area),
		processData: false,
		success : function(area) {
			location.reload();
		},
		error: function(e){
			swal("error", "", "error");
		}
	});

});


function deleteArea(id){
	$.ajax({
		type : 'DELETE',
		url : "../WebProject/rest/areas/delete/" + id,
		contentType: 'application/json',
		dataType : "json",
		processData: false,
		success : function(area) {
			location.reload();
		},
		error: function(e){
			swal("error", "", "error");
		}
	});
}

function fillFormForChangeArea(id){
	$.ajax({
		type : 'POST',
		processData: false,
		contentType: 'application/json',
		url : "../WebProject/rest/areas/getArea/" + id,
		success : function(area) {
			
			document.getElementById("changeAreaName").value = area.name;
			document.getElementById("changePopulation").value = area.population;
			document.getElementById("changeSurface").value = area.surface;
			document.getElementById("idArea").value = area.id;
		},
		error: function(e){
			swal("error", "", "error");
		}
	});
}

