package com.innobiz.orange.web.cm.files;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;

/** apache 업로드 기반의 업로드 구현체, UploadManager에서 해당 설정의 Handler 를 생성하여 사용함 */
public class UploadHandler {

//	/** UploadManager */
//	private UploadManager uploadManager;
	
	/** HttpServletRequest */
	private HttpServletRequest request;
	
	/** MultipartHttpServletRequest */
	private MultipartHttpServletRequest multipartRequest;
	
	/** multipart 파일 맵 */
	private MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
	
	/** multipart 파라미터 맵 */
	private Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
	
	/** multipart 파라미터 contentType 맵 */
	private Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();
	
	/** 파라미터 맵 */
	private Map<String, String> paramMap = new HashMap<String, String>();
	
	/** 파라미터 리스트 맵 */
	private Map<String, List<String>> paramListMap = new HashMap<String, List<String>>();
	
	/** 파일 맵 */
	private Map<String, File> fileMap = new HashMap<String, File>();
	
	/** 파일 리스트 맵 */
	private Map<String, List<File>> fileListMap = new HashMap<String, List<File>>();
	
	/** 임시 저장 경로 */
	private String uploadDir;
	
	/** FileItem 목록 */
	private List<FileItem> fileItemList;
	
	/** 임시 디렉토리를 업로드 경로에 포함하는지 여부 */
	private boolean hasTempDir;
	
	/** 경로 - 로그용 */
	private String dir;
	
	/** 파일 Prefix */
	private char filePrefix = 'F';
	
	/** 최대 용량 */
	private int maxMegaBytes = 0;
	
	private boolean lowVerIe = false;
	
	/** 생성자 */
	public UploadHandler(UploadManager uploadManager, HttpServletRequest request, String dir, boolean hasYearDir, boolean hasDateDir, boolean hasTempDir) {
//		this.uploadManager = uploadManager;
		this.request = request;
		this.uploadDir = uploadManager.getUploadBaseDir() + uploadManager.getDistDir(dir, hasYearDir, hasDateDir, hasTempDir);
		this.hasTempDir = hasTempDir;
		this.dir = dir;
		this.lowVerIe = VoUtil.isIe8(request);
	}
	
	/** 업로드 최대 용량 설정 */
	public void setMaxMegaBytes(int maxMegaBytes){
		this.maxMegaBytes = maxMegaBytes;
	}
	
	/** 업로드 경로 리턴 */
	public String getUploadDir(){
		return this.uploadDir;
	}

	/** 파일을 저장하고 해당 파일정보를 리턴함 */
	public Map<String, File> upload() throws CmException {
		
		init();
		
		// 파라미터와 파일들을 맵에 셋팅
		parseFileItems(fileItemList);
		
		// 파일들의 사이즈 체크
		checkSizeLimitation();
		
		// multipartRequest 초기화
		initMultipartRequest();

		return fileMap;
	}

	/** multipartRequest 생성 */
	private void initMultipartRequest() {
		multipartRequest = new DefaultMultipartHttpServletRequest(request, multipartFiles, multipartParameters, multipartParameterContentTypes);
	}

	/** MultipartRequest를 리턴 */
	public MultipartHttpServletRequest getMultipartRequest() {
		return multipartRequest;
	}
	
	/** 파일 Map 리턴 */
	public Map<String, File> getFileMap(){
		return fileMap;
	}
	
	/** 파라미터 Map 리턴 */
	public Map<String, String> getParamMap(){
		return paramMap;
	}
	
	/** 파일 리스트 Map 리턴 */
	public Map<String, List<File>> getFileListMap() {
		return fileListMap;
	}
	
	/** 파라미터 리스트 Map 리턴 */
	public Map<String, List<String>> getParamListMap(){
		return paramListMap;
	}
	
	/** 배포 경로 리턴 */
	public String getAbsolutePath(String paramName){
		File file = fileMap==null ? null : fileMap.get(paramName);
		return (file==null) ? null : file.getAbsolutePath();
	}
	
	/** 파라미터 맵 세팅 */
	public void setParamMap(Map<String, String> paramMap, Map<String, List<String>> paramListMap){
		this.paramMap = paramMap;
		this.paramListMap = paramListMap;

		File tempDirFile = new File(uploadDir);
		if(!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		}
	}
	
	/** 임시 저장 디렉토리 삭제 */
	public void removeTempDir() throws CmException{
		if(!hasTempDir){
			throw new CmException("cm.msg.upload.notDeleteTemp", new String[]{"context.properties - upload."+dir.replace('/', '.')+".tempDir=Y"}, request);
		}
		File dir = new File(uploadDir);
		File[] files = dir.listFiles();
		for(int i=0;files!=null && i<files.length;i++){
			files[i].delete();
		}
		dir.delete();
	}
	
	/** 파일 Prefix 세팅 */
	public void setFilePrefix(char prefix){
		filePrefix = prefix;
	}
	
	/** 초기화 */
	@SuppressWarnings("unchecked")
	private void init() throws CmException {

		if(!ServletFileUpload.isMultipartContent(request)){
			throw new CmException("cm.msg.upload.notMultipart", request);
		}
		
		File tempDirFile = new File(uploadDir);
		if(!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		}
		
		//임시저장공간 생성
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(2048);			// 메모리에 저장할 최대 size
		factory.setRepository(tempDirFile);		// 임시 저장할 위치
		
		//업로드 핸들러 생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 총 업로드 사이즈
		if(maxMegaBytes>0){
			upload.setSizeMax(maxMegaBytes * (1024L * 1024L));
		}
		
		
		try {
			fileItemList = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			//cm.msg.upload.processError=업로드 도중 오류가 발생 하였습니다.({0})
			throw new CmException("cm.msg.upload.processError", new String[]{e.getMessage()}, request);
		}
		
	}
	
	/** 파라미터를 파싱하여 paramMap 에 저장함 */
	private void parseFileItems(List<FileItem> items) throws CmException {
		String name, value;
		int p;
		String filePath, fileName, ext, newfileName;
		File newFile;
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (item.isFormField()) {
				name = item.getFieldName();
				value = toUtf8(item.getString());
				if(lowVerIe && !ArrayUtil.isInArray(VoUtil.NONCHK_ATTRS, name)) value = EscapeUtil.unescapeValue(value);
				
				// 파라미터 맵과 파라미터 리스트 맵에 추가
				putToParamMap(name, value);
				// multipart 파라미터 맵에 넣기
				putToMultipartParametersMap(name, value);
				// multipart 파라미터 contentType 맵에 넣기
				multipartParameterContentTypes.put(name, item.getContentType());
			} else {
				filePath = item.getName()==null ? "" : item.getName();
				p = filePath.replace('\\', '/').lastIndexOf('/');
				if (p >= 0) fileName = filePath.substring(p + 1);
				else fileName = filePath;
				p = fileName.lastIndexOf('.');
				ext = p <= 0 ? "" : fileName.substring(p + 1);
				
				if (fileName != null && !"".equals(fileName)) {
					newfileName = filePrefix + StringUtil.getNextHexa()+"."+ext;
					newFile = new File(uploadDir, newfileName);
					// 파일 맵과 파일 리스트 맵에 추가
					putToFileMap(item.getFieldName(), newFile);
					// 파라미터 맵과 파라미터 리스트 맵에 추가
					putToParamMap(item.getFieldName(), fileName);
					// multipart 파일 맵에 넣기
					putToMultipartFilesMap(item, newFile.getAbsolutePath(), ext);
					
					try {
						item.write(newFile);
					} catch (Exception e) {
						//cm.msg.upload.processError=업로드 도중 오류가 발생 하였습니다.({0})
						throw new CmException("cm.msg.upload.processError", new String[]{e.getMessage()}, request);
					}
				}
			}
		}
	}
	
	/** 파일 맵과 파일 리스트 맵에 추가 */
	private void putToFileMap(String name, File newFile) {
		File fileInMap = fileMap.get(name);
		if (fileInMap == null) {
			fileMap.put(name, newFile);
		}
		List<File> listInMap = fileListMap.get(name);
		if (listInMap == null) {
			listInMap = new ArrayList<File>();
			fileListMap.put(name, listInMap);
		}
		listInMap.add(newFile);
	}

	/** 파라미터 맵과 파라미터 리스트 맵에 추가 */
	private void putToParamMap(String name, String value) {
		String valueInMap = paramMap.get(name);
		if (valueInMap == null) {
			paramMap.put(name, value);
		}
		List<String> listInMap = paramListMap.get(name);
		if (listInMap == null) {
			listInMap = new ArrayList<String>();
			paramListMap.put(name, listInMap);
		}
		listInMap.add(value);
	}

	/** multipart 파라미터 맵에 넣기 */
	private void putToMultipartParametersMap(String name, String value) {
		String[] curParam = multipartParameters.get(name);
		if (curParam == null) {
			// simple form field
			multipartParameters.put(name, new String[] {value});
		}
		else {
			// array of simple form fields
			String[] newParam = StringUtils.addStringToArray(curParam, value);
			multipartParameters.put(name, newParam);
		}
	}

	/** multipart 파일 맵에 넣기 */
	private void putToMultipartFilesMap(FileItem fileItem, String savePath, String ext) {
		// multipart file field
		CommonsMultipartFile file = new CmMultipartFile(fileItem, savePath, ext);
		multipartFiles.add(file.getName(), file);
	}
	
	/** UTF8 케렉터로 변환 */
	private String toUtf8(String text){
		try{
			 return new String((text).getBytes("8859_1"),"utf-8");
		} catch(Exception ignore){}
		return text;
	}
	
	/** 개별 파일 용량, 총 파일 용량 체크 */
	private void checkSizeLimitation() throws CmException {
		Iterator<FileItem> iter = fileItemList.iterator();
		
//		long totalLimit = uploadManager.getTotalLimit();
//		long singlelLimit = uploadManager.getSingleLimit();
		long totalLimit = maxMegaBytes * (1024L * 1024L);
		long sum = 0, size = 0;
		
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (!item.isFormField() && !item.getName().isEmpty()) {
				
				size = item.getSize();
				sum += size;
//				if(singlelLimit!=0 && size > singlelLimit){
//					//cm.msg.upload.oneFileLimit=하나의 파일의 첨부 용량을 초과 하였습니다.({0} K-Bytes)
//					throw new CmException("cm.msg.upload.oneFileLimit", new String[]{Integer.toString((int)(singlelLimit / 1024))}, request);
//				}
				if(totalLimit!=0 && sum > totalLimit){
					//cm.msg.upload.totalFileLimit=전체 파일의 첨부 용량을 초과 하였습니다.({0} M-Bytes)
					throw new CmException("cm.msg.upload.totalFileLimit", new String[]{Integer.toString(maxMegaBytes)}, request);
				}
			}
		}
	}

	public char getFilePrefix() {
		return filePrefix;
	}
	
	
}
