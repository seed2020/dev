<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.regex.PatternSyntaxException"%>
<%@page import="java.net.*"%>
<%@page import="java.awt.*"%>
<%@page import="java.awt.Image"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="javax.swing.ImageIcon"%>
<%@page import="java.io.File"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileUploadBase"%>
<%@include file="Util.jsp"%>
<%@include file="SecurityTool.jsp"%>
<%@include file="Vaccine.jsp"%>
<%//@page import="org.apache.commons.codec.binary.Base64"%>
<%
	String encType = "utf-8"; 
	
	/*
	if(detectXSSEx(request.getParameter("licenseCheck")) != null){
		if(detectXSSEx(request.getParameter("licenseCheck")).toLowerCase().equalsIgnoreCase("true")){
			out.println(InetAddress.getLocalHost().getHostAddress());
			return;
		}
	}
	*/
	
	int maxSize = Integer.parseInt(detectXSSEx(request.getParameter("fileSizeLimit")));
	String defaultUPath = detectXSSEx(request.getParameter("defaultUPath"));
	String imageUPath = detectXSSEx(request.getParameter("fileUPath"));
	String fileUPathHost = "http://" + request.getHeader("host");
	String imagePhysicalPath = "";
	String useExternalServer =  detectXSSEx(request.getParameter("useExternalServer"));
	String strVaccinePath = "";
%>
<%@include file="VaccinePath.jsp"%>
<%@include file="ImagePath.jsp"%>
<%
	String fileModify = ""; 
	if ( detectXSSEx(request.getParameter("filemodify")) != null)
		fileModify =  detectXSSEx(request.getParameter("filemodify"));
	
	String fileEditorFlag = "";
	if ( detectXSSEx(request.getParameter("fileEditorFlag")) != null)
		fileEditorFlag =  detectXSSEx(request.getParameter("fileEditorFlag"));
	
	String uploadFileSubDir = "";
	if ( detectXSSEx(request.getParameter("uploadFileSubDir")) != null)
		uploadFileSubDir =  detectXSSEx(request.getParameter("uploadFileSubDir"));
	
	String fileDomain = ""; 
	if ( detectXSSEx(request.getParameter("fileDomain")) != null)
		fileDomain =  detectXSSEx(request.getParameter("fileDomain"));
/*
	String useExternalServer = "";
	if ( detectXSSEx(request.getParameter("useExternalServer")) != null)
		useExternalServer =  detectXSSEx(request.getParameter("useExternalServer"));
*/
	String checkPlugin = "false";
	
	String fileTemp = "";
	String scriptValue = "";
	String fileRealPath = "";
	String saveFolder = "";
	String returnParam ="";
	String scriptTag = "";
	String ContextPath = request.getContextPath();
	String ServerName = request.getServerName();
	String fileSize = "";
	
	ServletContext context = getServletConfig().getServletContext();

	if (!imageUPath.equalsIgnoreCase("")) {
		if (imageUPath.length() > 7) {
			if (imageUPath.substring(0, 7).equalsIgnoreCase("http://")) {
				fileTemp = imageUPath.substring(7);
				imageUPath = fileTemp.substring(fileTemp.indexOf("/"));
				fileUPathHost = "http://" + fileTemp.substring(0, fileTemp.indexOf("/"));
			}
			else if (imageUPath.substring(0, 8).equalsIgnoreCase("https://")) {
				fileTemp = imageUPath.substring(8);
				imageUPath = fileTemp.substring(fileTemp.indexOf("/"));
				fileUPathHost = "https://" + fileTemp.substring(0, fileTemp.indexOf("/"));
			}
			else if (!imageUPath.substring(0, 1).equalsIgnoreCase("/")) {
				scriptValue = executeFileScript(response, "invalid_path", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			}
		} else {
			if (!imageUPath.substring(0, 1).equalsIgnoreCase("/")) {
				scriptValue = executeFileScript(response, "invalid_path", "" , useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			}
		}
	} else {
		if (defaultUPath.length() > 7) {
			if (defaultUPath.substring(0, 7).equalsIgnoreCase("http://")) {
				fileTemp = defaultUPath.substring(7);
				imageUPath = fileTemp.substring(fileTemp.indexOf("/"));
			}
			else if (defaultUPath.substring(0, 8).equalsIgnoreCase("https://")) {
				fileTemp = defaultUPath.substring(8);
				imageUPath = fileTemp.substring(fileTemp.indexOf("/"));
			} else if (defaultUPath.substring(0, 1).equalsIgnoreCase("/"))
				imageUPath = defaultUPath;
			else {
				scriptValue = executeFileScript(response, "invalid_path", "" , useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			}
		} else {
			if (defaultUPath.substring(0, 1).equalsIgnoreCase("/"))
				imageUPath = defaultUPath;
			else {
				scriptValue = executeFileScript(response, "invalid_path", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			}
		}
	}

	if (imageUPath.lastIndexOf("/") != imageUPath.length() - 1)
		imageUPath = imageUPath + "/";

	if (imagePhysicalPath.equalsIgnoreCase("")) {
		String DompaserValue = Dompaser(imageUPath);
		if (DompaserValue.equalsIgnoreCase("")) {
			imagePhysicalPath = context.getRealPath(imageUPath);

			// 2013.08.26 [2.0.5.23] mwhong tomcat8.0
			if(imagePhysicalPath == null && imageUPath != null && ContextPath != null){
				imagePhysicalPath = context.getRealPath(imageUPath.substring(ContextPath.length()));
			}

			if (!ContextPath.equalsIgnoreCase("") && !ContextPath.equalsIgnoreCase("/")) {
				File tempFileRealDIR = new File(imagePhysicalPath);
				if (!tempFileRealDIR.exists()){
					if (imageUPath.indexOf(ContextPath) != -1)
						imagePhysicalPath = context.getRealPath(imageUPath.substring(ContextPath.length()));
				}
			}
		}
		else
			imagePhysicalPath = DompaserValue;
	}
		
	File fileRealFolderWriteCheck = new File(imagePhysicalPath);
	if (!fileRealFolderWriteCheck.exists()) {
		scriptValue = executeFileScript(response, "invalid_path", "" , useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
		response.getWriter().println(scriptValue);
		return;
	}
	if (!fileRealFolderWriteCheck.canWrite()) {
		scriptValue = executeFileScript(response, "canWriteErr", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
		response.getWriter().println(scriptValue);
		return;
	}

	if (imagePhysicalPath.lastIndexOf(File.separator) != imagePhysicalPath.length() - 1)
		imagePhysicalPath += File.separator;

	String filePhysicalPathsubFolder = imagePhysicalPath;
	File SaveSubFolder = new File(filePhysicalPathsubFolder + "upload");
	if(!SaveSubFolder.exists()){
		//SaveSubFolder.setExecutable(false, true);
		//SaveSubFolder.setReadable(true);
		//SaveSubFolder.setWritable(false, true);

		SaveSubFolder.mkdir();
	}
	filePhysicalPathsubFolder += "upload" + File.separator;
	File DeleteTempFolder = null;
	
	try {
		String tempFileFolder = "";

		if (uploadFileSubDir.equalsIgnoreCase("false") && !imageUPath.equalsIgnoreCase(""))
			tempFileFolder = tempFolderCreate(imagePhysicalPath);
		else
			tempFileFolder = imagePhysicalPath;
					
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			String realDir = filePhysicalPathsubFolder;
			DiskFileItemFactory factory = new DiskFileItemFactory();                                   
			factory.setSizeThreshold(2 * 1024 * 1024); 
			ServletFileUpload upload = new ServletFileUpload(factory);                               
			upload.setSizeMax(-1); 
			upload.setHeaderEncoding("utf-8");
			List items = upload.parseRequest(request);       
			Iterator iter=items.iterator();                                                                            

			
			String fileMaxCount = "";
			String fileTitle = "";
			String fileId = "";
			String fileClass = "";
			String fileKind = "";
			String fileTempFName = "";
			String fileUNameType = "";
			String editorFrame = "";
			String filename = "";
			String type = "";

			while(iter.hasNext()){
				FileItem fileItem = (FileItem) iter.next();    
				if(fileItem.isFormField()){          
						if( fileItem.getFieldName().equalsIgnoreCase("fileMaxCount") ) fileMaxCount =  detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileTitle") ) fileTitle = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileId") ) fileId = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileClass") ) fileClass = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileKind") ) fileKind = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileTempFName") ) fileTempFName = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("fileUNameType") ) fileUNameType = detectXSSEx(toString(fileItem.getString("utf-8")));
						if( fileItem.getFieldName().equalsIgnoreCase("editorFrame") ) editorFrame = detectXSSEx(toString(fileItem.getString("utf-8")));
													
				} else { 
					if(fileItem.getSize()>0) { 
						fileSize = Long.toString(fileItem.getSize());
						if(fileItem.getSize() > maxSize){
							scriptValue = executeFileScript(response, "invalid_size", Integer.toString(maxSize), useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
		
							response.getWriter().println(scriptValue);
							return;
						}
						filename = fileItem.getName();
						if (filename.lastIndexOf("\\") != -1) {
							filename = filename.substring(filename.lastIndexOf("\\"), filename.length());
						}
						type = fileItem.getContentType();
						
						
						if (filename.toLowerCase().endsWith(".jsp") || filename.toLowerCase().endsWith(".jspx") || filename.toLowerCase().endsWith(".js") || filename.toLowerCase().endsWith(".html") || filename.toLowerCase().endsWith(".htm")) {
							//scriptValue = executeFileScript(response, "fail_image", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
							//scriptValue = executeFileScript(response, "invalid_file", "prohibited : jsp, js, html, htm", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
							// [4.0.0.22] [한국인터넷진흥원 보안 취약점] 제한된 확장자 목록 alert에 보이지 않도록 처리
							//scriptValue = executeFileScript(response, "fail_image", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
							scriptValue = executeFileScript(response, "UploadFileExtBlock", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
							response.getWriter().println(scriptValue);
							return;
						}

						try{
							File uploadedFile=new File(realDir,filename); 
							fileItem.write(uploadedFile);
							fileItem.delete(); 
							DeleteTempFolder = uploadedFile;
						}catch(IOException ex) {
							//System.out.println("An internal exception occured!");
						} 
					}
				}
			}
			String fileTempName = "";
			String fileKindSubFolder = ""; 

			fileKindSubFolder = "files";
			if (fileUNameType.equalsIgnoreCase("real")) 
				fileTempName = filename.substring(0, filename.lastIndexOf("."));
			else if(fileUNameType.equalsIgnoreCase("random"))
				fileTempName = fileNameTimeSetting();
			else {
				//fileTempName = fileTempFName;
				fileTempName = filename.substring(0, filename.lastIndexOf("."));
				sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				byte[] keyByte = fileTempName.getBytes("utf-8");
				fileTempName = encoder.encode(keyByte);
				//라이브러리 추가 요함 -> https://commons.apache.org/proper/commons-codec/download_codec.cgi
				//byte[] encoded = Base64.encodeBase64(fileTempName.getBytes());
				//fileTempName = new String(encoded);

				if (fileTempName.indexOf("/") != -1)
					fileTempName = fileTempName.replaceAll("/", "==NamOSeSlaSH=="); 
			}
			String realFileName = fileTempName.replace(' ', '_');
			String fileCheck =filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			fileCheck = detectXSSEx(fileCheck);
			String typeCheck = type.substring(0,type.indexOf("/")); 
/*
			if (!isImageValid("file", fileCheck)) {
				if(uploadFileSubDir.equalsIgnoreCase("false") && !imageUPath.equalsIgnoreCase(""))
					tempFolderDelete(tempFileFolder);

				scriptValue = executeFileScript(response, "invalid_file", getImageKind("file"), useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			}
*/
			if(uploadFileSubDir.equalsIgnoreCase("false")) { 
				if(imageUPath.equalsIgnoreCase("")) {
					File fileSaveSubFolder = new File(imagePhysicalPath + fileKindSubFolder);
					if(!fileSaveSubFolder.exists()){
						//fileSaveSubFolder.setExecutable(false, true);
						//fileSaveSubFolder.setReadable(true);
						//fileSaveSubFolder.setWritable(false, true);

						fileSaveSubFolder.mkdir();
					}
					imagePhysicalPath += fileKindSubFolder + File.separator;
				}
			} else {
				File fileSaveSubFolder = new File(imagePhysicalPath + fileKindSubFolder);
				if(!fileSaveSubFolder.exists()){
					//fileSaveSubFolder.setExecutable(false, true);
					//fileSaveSubFolder.setReadable(true);
					//fileSaveSubFolder.setWritable(false, true);

					fileSaveSubFolder.mkdir();
				}
				imagePhysicalPath += fileKindSubFolder + File.separator;

				saveFolder = getChildDirectory(imagePhysicalPath, fileMaxCount); 
				
				if (saveFolder.equalsIgnoreCase("")) {	
					if(uploadFileSubDir.equalsIgnoreCase("false") && !imageUPath.equalsIgnoreCase(""))
						tempFolderDelete(tempFileFolder);

					scriptValue = executeFileScript(response, "invalid_path", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
					response.getWriter().println(scriptValue);
					return;
				} else
					imagePhysicalPath += saveFolder;	
			}
			String filenamecheck = checkFileUniqueName(realFileName, imagePhysicalPath, fileCheck);

			String imgLinkParams = "";
			String urlFilePath = fileUPathHost + imageUPath;

			if(uploadFileSubDir.equalsIgnoreCase("false")) {
				if(imageUPath.equalsIgnoreCase(""))
					urlFilePath += fileKindSubFolder + File.separator;
			} else
				urlFilePath += fileKindSubFolder + File.separator + saveFolder + File.separator;
			urlFilePath = urlFilePath.replace('\\', '/');

            filenamecheck = filenamecheck.replace('\\', ' ').trim();

			urlFilePath += filenamecheck;
			imgLinkParams = urlFilePath; 
	
			if (fileTitle == null)
				fileTitle ="";
			if (fileId == null)
				fileId ="";
			if (fileClass == null)
				fileClass = "";
			if (editorFrame == null)
				editorFrame ="";

			if (fileSize == null)
				fileSize = "";


			returnParam = "{";
			//returnParam += "\"fileURL\":\"" + urlFilePath.replaceAll("'", "\\\\\"") + "\",";
			returnParam += "\"fileURL\":\"" + urlFilePath + "\",";
			returnParam += "\"fileTitle\":\"" + fileTitle + "\",";
			returnParam += "\"fileId\":\"" + fileId + "\",";
			returnParam += "\"fileClass\":\"" + fileClass + "\",";
			returnParam += "\"fileKind\":\"" + fileKind + "\",";
			returnParam += "\"fileType\":\"" + fileCheck + "\",";
			returnParam += "\"fileSize\":\"" + fileSize + "\",";
			if (fileModify.equalsIgnoreCase("true"))
				returnParam += "\"fileModify\":\"true\",";
			returnParam += "\"editorFrame\":\"" + editorFrame + "\"";
			returnParam += "}";	
			
			String moveFilePath = imagePhysicalPath + File.separator + filenamecheck;
			int check = fileCopy(filePhysicalPathsubFolder + filename, moveFilePath);

			if(DeleteTempFolder != null){
				DeleteTempFolder.delete();
			}

			if (check == 1) {
				if (strVaccinePath.length() <= 0) {
					strVaccinePath = imagePhysicalPath + "/../../../vse";
				}
 				String strName = checkVirusFile (moveFilePath, imagePhysicalPath + File.separator, strVaccinePath);

				if(uploadFileSubDir.equalsIgnoreCase("false") && !imageUPath.equalsIgnoreCase(""))
					tempFolderDelete(tempFileFolder);
				if (SaveSubFolder.exists()){
					SaveSubFolder.delete();
				}

				if (strName.length() > 0) {
					String msg = "found virus (";
					msg += strName + ")";
					scriptValue = executeFileScript(response, "virus", msg, useExternalServer, fileDomain, fileEditorFlag, "false");	
					response.getWriter().println(scriptValue);
					return;
				}

				
				scriptValue = executeFileScript(response, "success", returnParam, useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
				response.getWriter().println(scriptValue);
				return;
			} else {
				if(uploadFileSubDir.equalsIgnoreCase("false") && !imageUPath.equalsIgnoreCase(""))
					tempFolderDelete(tempFileFolder);

				scriptValue = executeFileScript(response, "fileCopyFail", "", useExternalServer, fileDomain, fileEditorFlag, checkPlugin);	
				response.getWriter().println(scriptValue);
				return;			
			}
		}else{
			response.getWriter().println("not encoding type multipart/form-data");
		}
	} catch (IOException ioe) {
		scriptValue = executeFileScript(response, "invalid_size", Integer.toString(maxSize), useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
		
		response.getWriter().println(scriptValue);
		return;
	} catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
		scriptValue = executeFileScript(response, "invalid_size", Integer.toString(maxSize), useExternalServer, fileDomain, fileEditorFlag, checkPlugin);

        response.getWriter().println(scriptValue);
		return;
    } catch (RuntimeException e) {	
		String messageText = "RuntimeException";
		messageText = "<System Error>" + messageText;
		
		scriptValue = executeFileScript(response, "", messageText, useExternalServer, fileDomain, fileEditorFlag, checkPlugin);
		response.getWriter().println(scriptValue);
		return;
	}

%>
