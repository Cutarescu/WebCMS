globalVariable = {
};

labelsDialog = {
		"title" :{
			0 : "Success",
			1 : "Error",
			2 : "Info"
		},
		"messages":{
			"addSuccess" : "You're file was succesfully added!"
		}
}


function clearContent(){
	$("#fileContent")[0].value = "";  
}
function saveFile(){
	var fileContent = $("#fileContent")[0].value;
	var _csrfToken = $("#_csrf")[0].innerHTML;
	if(globalVariable.currentFilePath && globalVariable.contentChanged == true){
		$.post("edit-file", { file: globalVariable.currentFilePath, content:fileContent, _csrf : _csrfToken}, function(resp) {
			if(resp.succesful == true){
				displayMessage(resp.message, 0);
				globalVariable.contentChanged = false;
			}else{
				displayMessage(resp.message, 1);
			}
		});
	}
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
        	  globalVariable.selectedFolder = undefined;
    	  },
    	  Save: function() {
    		  var result = addFile();
    		  if(result.successful){
    			  displayMessage(labelsDialog.messages.addSuccess,0, function(){
    				  $("#add-dialog").dialog("close");
    				  globalVariable.displayOnlyDirs = false;
    				  globalVariable.selectedFolder = undefined;
    			  });	  
    		  }else{
    			  displayMessage(result.errMessage,1);
    		  }
    	  }
       },
       title: "Add file",
       position: {
          my: "left center",
          at: "left center"
       }
    }).dialog('open');
	globalVariable.displayOnlyDirs = true;
	$('#fileTreeDemo_2').fileTree({ script: 'jqueryFileTree' , showOnlyDirs : true});
}
function addFile(){
	return {
		successful : true
	}
}
