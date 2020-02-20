function noPreview() {
  $('#image-preview-div').css("display", "none");
  $('#preview-img').attr('src', 'noimage');
  $('addItem').attr('disabled', '');
}

function selectImage(e){
  $('#file').css("color", "green");

  $('#image-preview-div').css("display", "block");
  $('#preview-img').attr('src', e.target.result);
  $('#preview-img').css('max-width', '550px');
}


  var maxsize = 500 * 1024; // 500 KB

  $('#max-size').html((maxsize/1024).toFixed(2));

  $('#itemAdBtn').click( function() {
	//generete random image id
    $('#message').empty();
    $('#loading').show();
    var itemAd = {}
	itemAd.name = $('#itemName').val();
    itemAd.description = $('#itemDescription').val();
    itemAd.expiryDate = $("#itemExpiryDate").val();
    alert(itemAd.expiryDate);
    $.ajax({
      url: "api/uploadImage",
      type: "POST",
      data: new FormData($("#formItem")[0]),
      contentType: false,
      cache: false,
      processData: false,
      success: function(pictureName)
      {
    	  itemAd.picture = pictureName;
        $('#loading').hide();
        $.ajax({
    	    contentType: 'application/json',
    	    data: JSON.stringify(itemAd),
    	    dataType: 'json',
    	    success: function(data){
    			$('#newItem').modal('hide');
    			$("#image-preview-div").css("display", "none");
    			myAds();
    	    },
    	    error: function(data){
    	    	alert("greska");
    	    },
    	    processData: false,
    	    type: 'POST',
    	    url: '/api/itemAds'
    	});
      }
    });
  });