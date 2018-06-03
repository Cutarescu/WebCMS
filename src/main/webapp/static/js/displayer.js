globalVariable = {
};

labelsDialog = {
		"title" :{
			0 : "Success",
			1 : "Error",
			2 : "Info",
			"confirm" : "Please confirm!"
		}
}
messages = {
	"addSuccess" : "You're file was succesfully added!",
	"noSelectedFolder" : "Please select a folder as a parent for the new file/folder!",
	"incorrectName" : "Please select a valid name!",
	"serverError": "A server error has occurred!",
	"noFileToDelete" : "Please select a file/folder to delete!",
	"confirmDelete" : "Are you sure you want to delete current selection?"
}



function clearContent(){
	$("#fileContent")[0].value = "";  
}
function saveFile(){
	var fileContent = $("#fileContent")[0].value;
	var _csrfToken = $("#_csrf")[0].innerHTML;
	if(globalVariable.currentFilePath && globalVariable.contentChanged == true){
		$.ajax({
			type: "PUT",
			url: "http://localhost:8080/WebCMS/edit-file",
			processData : false,
			contentType: false,
			headers:
	        	{ 
				'X-CSRF-TOKEN': _csrfToken 
				},
			data: {
				file: globalVariable.currentFilePath,
				content:fileContent
			},
			success: function(data) {
				if(data.success == true){
					displayMessage(data.message, 0);
					globalVariable.contentChanged = false;
				}else{
					displayMessage(data.message, 1);
				}
			},
			error: function(e) {
				displayMessage(messages.serverError, 1);
			}
		});
	}
}

function preview(){
	var link = globalVariable.currentFilePath.split("WebCMS");
	window.open("/"+location.pathname.split('/')[1] + link[1],'_blank');
}

function reloadFile(){
	if(globalVariable.currentFilePath){
		$.get("get-content", { dir: globalVariable.currentFilePath }, function(data) {
			$("#fileContent")[0].value = removeLeadingNewLines(data);
			globalVariable.contentChanged = false;
		});
	}
}
/* 0 - success
 * 1 - error
 * 2 - info
 * */
function displayMessage(message, type, callBack){
	if(type == undefined) type = 2;
	$("#dialog-message")[0].innerHTML = message;
    $("#dialog-message").dialog({
       autoOpen: false, 
       buttons: {
          OK: function() {
        	  $("#dialog-message")[0].innerHTML = "";
        	  $(this).dialog("close");
        	  if(callBack) callBack();
    	  }
       },
       title: labelsDialog.title[type],
       position: {
          my: "left center",
          at: "left center"
       }
    }).dialog('open');
}

function confirmationDialog(options, confirmCb, cancelCb){
	$("#confirm-message")[0].innerHTML = options.message;
    $("#confirm-message").dialog({
       autoOpen: false, 
       buttons: {
          OK: function() {
        	  $("#confirm-message")[0].innerHTML = "";
        	  $(this).dialog("close");
        	  if(confirmCb) confirmCb();
    	  },
    	  Cancel: function (){
    		  $("#confirm-message")[0].innerHTML = "";
        	  $(this).dialog("close");
        	  if(cancelCb) cancelCb();
    	  }
       },
       title: options.title,
       position: {
          my: "left center",
          at: "left center"
       }
    }).dialog('open');
}

function removeLeadingNewLines(array){
	var newArray = "", tranfer = false;
	for(var i = 0; i < array.length; i++){
		if(array[i] != "\n" || tranfer){
			newArray += array[i];
			tranfer = true;
		}
	}
	return newArray;
}

function openAddDialog(){
	$("#add-dialog").removeClass("hidden");
	$("#add-dialog").dialog({
       autoOpen: false,
       maxWidth: 580,
       maxHeight: 350,
       width: 580,
       height: 350,
       modal: true,
       buttons: {
          Cancel: function() {
        	  $(this).dialog("close");
        	  globalVariable.displayOnlyDirs = false;
    	  },
    	  Save: function(){
			  if($("#filenameInput")[0].value.trim().length == 0){
				  	displayMessage(messages.incorrectName, 1);
				  	return;
			  }
    		  if(!$("#createAtRoot")[0].checked && $("#fileTreeDemo_2 .highlighted").length == 0){
    			  displayMessage(messages.noSelectedFolder, 1);
    			  return;
    		  }
			  if(!$("#checkBoxFolder")[0].checked && $("#fileInput")[0].files.length == 0){
				  displayMessage(messages.noSelectedFile, 1);
				  return;
			  }
			  addFile();
    	  }
       },
       title: "Add file",
       position: {
          my: "left center",
          at: "left center"
       }
    }).dialog('open');
	globalVariable.displayOnlyDirs = true;
	$('#fileTreeDemo_2').fileTree({ script: 'jqueryFileTree-default' , showOnlyDirs : true,  jqueryFileTreeId : "#fileTreeDemo_2" });
}
function addFile(){
	var formData = new FormData();
	var parentFolder = $("#fileTreeDemo_2 .highlighted").attr('rel');
	var isFolder = $("#checkBoxFolder")[0].checked;
	var rootLevel = $("#createAtRoot")[0].checked;
	var file = $("#fileInput")[0].files[0];
	var fileName = $("#filenameInput")[0].value.trim();
	var _csrfToken = $("#_csrf")[0].innerHTML;
	if(!isFolder)
		formData.append("fileInput", file);
	formData.append("parentFolder", parentFolder);
	formData.append("isFolder", isFolder);
	formData.append("name", fileName);
	formData.append("rootLevel", rootLevel);
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/WebCMS/create-file",
		processData : false,
		contentType: false,
		headers:
        	{ 
			'X-CSRF-TOKEN': _csrfToken
			},
		data: formData,
		success: function(data) {
			if(data.success == true){
				displayMessage(data.message, 0, function(){
					window.location.reload(true); 
				});
			}else{
				displayMessage(data.message, 1);
			}
		},
		error: function(e) {
			displayMessage(messages.serverError, 1);
		}
	});
}

function deleteFile(){
	if($("#fileTreeDemo_1 .highlighted").length == 0){
	  	displayMessage(messages.noFileToDelete, 1);
	  	return;
	}
	confirmationDialog({
		message : messages.confirmDelete,
		title : labelsDialog.title.confirm
	}, function(){
		var target = $("#fileTreeDemo_1 .highlighted").attr('rel');
		var _csrfToken = $("#_csrf")[0].innerHTML;
		$.ajax({
			type: "DELETE",
			url: "http://localhost:8080/WebCMS/delete-file" + '?' + $.param({'target': target}),
			headers:
	        	{ 
				'X-CSRF-TOKEN': _csrfToken
				},
			data: {
				target : target
			},
			success: function(data) {
				if(data.success == true){
					displayMessage(data.message, 0, function(){
						window.location.reload(true); 
					});
				}else{
					displayMessage(data.message, 1);
				}
			},
			error: function(e) {
				displayMessage(messages.serverError, 1);
			}
		});
	});
}
