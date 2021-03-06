<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Registration Confirmation Page</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
	<div class="generic-container">
		<%@include file="utils/authheader.jsp" %>
		
		<div class="alert alert-success lead">
	    	${success}
		</div>
		<c:choose>
			<c:when test="${isAnonymous}">
				<span class="well floatRight">
					Go to <a href="<c:url value='/login' />">Login</a>
				</span>
			</c:when>
			<c:otherwise>
				<span class="well floatRight">
					Go to <a href="<c:url value='/list' />">List</a>
				</span>
			</c:otherwise>
		</c:choose>
	</div>
</body>

</html>