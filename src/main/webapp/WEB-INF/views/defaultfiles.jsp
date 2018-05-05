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
		
		<script src="<c:url value="/static/js/fileTree/jquery.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jquery.easing.js" />" ></script>
		<script src="<c:url value="/static/js/fileTree/jqueryFileTree.js" />" ></script>
	
		<script type="text/javascript">
			$(document).ready( function() {
				//window.location + /jqueryFileTree
				$('#fileTreeDemo_1').fileTree({ root: 'C:/xampp/htdocs/file/', script: 'jqueryFileTree.jsp' }, function(file) { 
					alert(file);
				});
			});
		</script>
	</head>
	
	<body>
			<h1>jQuery File Tree Demo</h1>
			<p>
				test.
			</p>
			<div class="example">
				<h2>Default options</h2>
				<div id="fileTreeDemo_1" class="demo"></div>
			</div>
	</body>
</html>