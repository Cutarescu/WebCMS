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
		
		<script src="<c:url value="/static/js/fileTree/jquery.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jquery.easing.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jqueryFileTree.js" />" ></script>
		<script src="<c:url value="/static/js/displayer.js" />" ></script>
		
		<script type="text/javascript">
			$(document).ready( function() {
				//window.location + /jqueryFileTree
				$('#fileTreeDemo_1').fileTree({ root: "${dir}", script: 'jqueryFileTree' });
			});
		</script>
	</head>
	<%@include file="utils/navbar.jsp"%>
	<body>
			<div class="example">
				<h2>Default Files</h2>
				<div id="fileTreeDemo_1" class="demo"></div>
			</div>
			<div id="container">
				<div id="content">
				</div>
			</div>
	</body>
</html>