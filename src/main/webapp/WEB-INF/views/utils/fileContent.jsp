<%@ page
	import="java.io.File,java.io.FilenameFilter,java.util.Arrays, java.nio.charset.Charset,
			java.nio.file.Files, java.nio.file.Paths"%>
<%
	String path = request.getParameter("dir");
	if (path == null) {
		return;
	}
  	byte[] encoded = Files.readAllBytes(Paths.get(path));
  	if(path.contains("images")){
  		String[] split = path.split("webapp");
  		String imgPath = request.getContextPath()+ split[1];
  		out.print("<img src='");
  		out.print(imgPath);
  		out.print("' alt='ShowImage'>");
  	} else {
  	  	if(path.contains("video")){
  	  		String[] split = path.split("webapp");
  	  		String imgPath = request.getContextPath()+ split[1];
  	  		out.print("<video width='320' height='240' controls> <source src='");
  	  		out.print(imgPath);
  	  		out.print("' type='video/" + imgPath.substring(imgPath.length() - 3) + "'> Your browser does not support the video tag.</video>");
  	  	} else {
  	  		out.print(new String(encoded, Charset.defaultCharset()));
  	  	}
  	}
%>