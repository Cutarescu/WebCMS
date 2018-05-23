<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
	<head>
		<title>Add file</title>
		<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
		<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
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
	</head>
	<body>
		<div class="example">
			<h2>Default Files</h2>
			<div id="fileTreeDemo_1" class="demo">
				<%@include file="utils/jqueryFileTree.jsp"%>
			</div>
		</div>
			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable">File name</label>
					<div class="col-md-7">
						<input type="text" class="form-control input-sm" id="filename"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable">File</label>
					<div class="col-md-7">
						<input type="file" class="form-control input-sm" id="file"/>
					</div>
				</div>
			</div>
		</div>	</body>
</html>
