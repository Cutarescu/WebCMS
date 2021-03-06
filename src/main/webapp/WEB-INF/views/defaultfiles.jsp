<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>
	<head>
		<title>Default Files</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link href="<c:url value='/static/css/defaultFiles.css' />" rel="stylesheet" />
		<link href="<c:url value='/static/css/jqueryFileTree.css' />" rel="stylesheet" />
		<link href="<c:url value='/static/css/main.css' />" rel="stylesheet" />
		<link href="<c:url value='/static/css/jquery-ui.min.css' />" rel="stylesheet" />
		
		<script src="<c:url value="/static/js/fileTree/jquery-1.12.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jquery.easing.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jqueryFileTree.js" />" ></script>
		<script src="<c:url value="/static/js/jquery-ui.min.js" />" ></script>
		<script src="<c:url value="/static/js/displayer.js" />" ></script>
		
		<script type="text/javascript">
			$(document).ready( function() {
				$('#fileTreeDemo_1').fileTree({ root: "${dir}", script: 'jqueryFileTree-default', jqueryFileTreeId : "#fileTreeDemo_1" });
				$("#fileContent").bind( "keydown", function( event ) {
					globalVariable.contentChanged = true;
				});
				$("#checkBoxFolder").bind( "click", function( event ) {
					$("#fileInput")[0].disabled = event.target.checked;
				});
				$("#createAtRoot").bind( "click", function( event ) {
					if($("#fileTreeDemo_2 .highlighted").length != 0){
						$("#fileTreeDemo_2 .highlighted").removeClass('highlighted');
					}
				});
				$("#fileInput").change(function(){
					$("#filenameInput")[0].value = $("#fileInput")[0].files[0].name;
				});
			});
		</script>
	</head>
	<%@include file="utils/navbar.jsp"%>
	<body>
			<div class="example">
				<h2>Default Files</h2>
				<div id="fileTreeDemo_1" class="demo"></div>
				<button class="button" id="addButton" onclick="openAddDialog()">Add</button>
				<button class="button" id="deleteButton" onclick="deleteFile()">Delete</button>
			</div>
			<div id="container">
				<div id="resourcePath" style="font-size: large;font-style: oblique;"></div>
				<div id="content">
					<textarea id='fileContent'>
					</textarea>
				</div>
				<button class="button hidden" id="saveButton" onclick="saveFile()">Save</button>
				<button class="button hidden" id="cancelButton" onclick="reloadFile()">Cancel</button>
				<button class="button hidden" id="previewButton" onclick="preview()">Preview</button>
			</div>
			<div id="dialog-message" ></div>
			<div id="confirm-message" ></div>
			<div id="add-dialog" class="hidden">			
				<div class="example">
					<h2>Directories</h2>
					<div id="fileTreeDemo_2" class="demo">
					</div>
				</div>
				<div id="addFileFields">
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-3 control-lable">File name</label>
							<div class="col-md-7">
								<input type="text" class="form-control input-sm" id="filenameInput"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-3 control-lable">File</label>
							<div class="col-md-7">
								<input type="file" class="form-control input-sm" id="fileInput"/>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-3 control-lable">Is folder </label>
							<div class="col-md-7">
								<input type="checkbox" class="form-control input-sm" id="checkBoxFolder"/>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-3 control-lable">Create file at root '/' </label>
							<div class="col-md-7">
								<input type="checkbox" class="form-control input-sm" id="createAtRoot"/>
							</div>
						</div>
					</div>	
				</div>	
			</div>
			<span id="_csrf" class="hidden">${_csrf.token}</span>
	</body>
</html>