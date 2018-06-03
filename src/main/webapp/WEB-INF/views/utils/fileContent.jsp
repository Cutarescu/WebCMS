<%@ page
	import="java.io.File,java.io.FilenameFilter,java.util.*, java.nio.charset.Charset,
			java.nio.file.Files, java.nio.file.Paths"%>
<%!
public boolean containsAKeyword(String myString, List<String> keywords){
	   for(String keyword : keywords){
	      if(myString.toLowerCase().contains(keyword.toLowerCase())){
	         return true;
	      }
	   }
	   return false;
	}
%>			
<%

ArrayList<String> images = new ArrayList<String>();
images.add("JPEG");
images.add("JFIF");
images.add("jpg");
images.add("png");
images.add("gif");
images.add("PPM");
images.add("PGM");
images.add("BPG");
images.add("PNM");
images.add("PBM");

ArrayList<String> video = new ArrayList<String>();
video.add("avi");
video.add("mov");
video.add("mp4");
video.add("ogg");

	String path = request.getParameter("dir");
	if (path == null) {
		return;
	}
  	byte[] encoded = Files.readAllBytes(Paths.get(path));
  	if(containsAKeyword(path, images)){
  		String[] split = path.split("WebCMS");
  		String imgPath = request.getContextPath()+ split[1];
  		out.print("<img src='");
  		out.print(imgPath);
  		out.print("' alt='ShowImage'>");
  	} else {
  	  	if(containsAKeyword(path, video)){
  	  		String[] split = path.split("WebCMS");
  	  		String imgPath = request.getContextPath()+ split[1];
  	  		out.print("<video width='320' height='240' controls> <source src='");
  	  		out.print(imgPath);
  	  		out.print("' type='video/" + imgPath.substring(imgPath.length() - 3) + "'> Your browser does not support the video tag.</video>");
  	  	} else {
  	  		out.print(new String(encoded, Charset.defaultCharset()));
  	  	}
  	}
%>