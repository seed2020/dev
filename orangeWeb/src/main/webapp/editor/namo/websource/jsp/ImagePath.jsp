<%!
	String customFileBase = null;
%><%

String webPath = "/images/upload/editor/namo";

if(customFileBase==null){
	ServletContext servletContext = this.getServletContext();
	org.springframework.web.context.WebApplicationContext wac = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	java.util.Properties contextProperties = (java.util.Properties)wac.getBean("contextProperties");
	
	customFileBase = contextProperties.getProperty("distribute.web.local.root");
	java.io.File uploadDir = new java.io.File(customFileBase+webPath);
	if(!uploadDir.isDirectory()){
		uploadDir.mkdirs();
	}
}

imagePhysicalPath = customFileBase + webPath;
imageUPath = webPath;

%><%
/*
String namoFileKind = request.getParameter("namofilekind");

//filelink
String namoFilePhysicalPath = "D:\\cejava\\htdocs\\ce3\\namofile";
String namoFileUPath = "/ce3/namofile";

//movie
String namoFlashPhysicalPath = "D:\\cejava\\htdocs\\ce3\\namomovie";
String namoFlashUPath = "/ce3/namomovie";

//image
String namoImagePhysicalPath = "D:\\cejava\\htdocs\\ce3\\namoimage";
String namoImageUPath = "/ce3/namoimage";

System.out.println("namoFileKind: "+ namoFileKind);

if(namoFileKind != null && "file".equals(namoFileKind)){
	imagePhysicalPath = namoFilePhysicalPath;
	imageUPath = namoFileUPath;
}else if(namoFileKind != null && "flash".equals(namoFileKind)){
	imagePhysicalPath = namoFlashPhysicalPath;
	imageUPath = namoFlashUPath;
}else{
	imagePhysicalPath = namoImagePhysicalPath;
	imageUPath = namoImageUPath;
}
*/
%>