var globalVariable = {};

function display(content, target){
	alert(content);
}

function clearContent(){
	$("#fileContent")[0].value = "";  
}
function editFile(rel){
	var fileContent = $("#fileContent")[0].value;
	$.put("jqueryFileTree", { file: t, content:fileContent }, function(resp) {
		alert(data);
	});
}