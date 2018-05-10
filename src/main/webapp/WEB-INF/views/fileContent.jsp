<%@ page
	import="java.io.File,java.io.FilenameFilter,java.util.Arrays, java.nio.charset.Charset,
			java.nio.file.Files, java.nio.file.Paths"%>
<%
	String path = request.getParameter("dir");
	if (path == null) {
		return;
	}
  	byte[] encoded = Files.readAllBytes(Paths.get(path));
  	out.print("<textarea id ='fileContent'>");
	out.print(new String(encoded, Charset.defaultCharset()));
	out.print("</textarea>");
%>