package com.innobiz.orange.web.cm.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.StringUtil;

/** 배포 핸들러 */
public class DistHandler {
	
	/** 디렉토리 생성용 날짜포멧 */
	private static final SimpleDateFormat EDITOR_DIR_FORMAT = new SimpleDateFormat("/yyyyMM");
	
	/** DistManager */
	private DistManager distManager;
	
	/** 배포할 경로 */
	private String distDir = null;
	
	/** WEB 에 배포 파일의 클라이언트 패스 목록 */
	private List<String> webClientPathList = new ArrayList<String>();
	
	/** WEB 의 배포 패스 목록 */
	private List<String> webDistPathList = new ArrayList<String>();
	
	/** WAS 에 배포 파일의 클라이언트 패스 목록 */
	private List<String> wasClientPathList = new ArrayList<String>();
	
	/** WAS 의 배포 패스 목록 */
	private List<String> wasDistPathList = new ArrayList<String>();
	
	/** Locale (언어셋) */
	private Locale locale = null;
	
	/** 생성자 */
	public DistHandler(DistManager distManager, String dir, Locale locale){
		this.distManager = distManager;
		this.locale = locale;
		this.distDir = distManager.getDistDir(dir);
	}
	
	/** WEB 배포 목록에 더함 */
	public String addWebList(String clientAbsolutePath){
		if(clientAbsolutePath!=null && !clientAbsolutePath.isEmpty()){
			webClientPathList.add(clientAbsolutePath);
			String absolutePath = clientAbsolutePath.replace('\\', '/');
			String distServerPath = distDir + absolutePath.substring(absolutePath.lastIndexOf('/'));
			webDistPathList.add(distServerPath);
			return distServerPath;
		}
		return null;
	}
	
	/** WEB 배포 목록에 더함 */
	public String addWebList(String clientAbsolutePath, String distServerPath){
		if(clientAbsolutePath!=null && !clientAbsolutePath.isEmpty()){
			webClientPathList.add(clientAbsolutePath);
			webDistPathList.add(distServerPath);
			return distServerPath;
		}
		return null;
	}
	
	/** WAS 배포 목록에 더함 */
	public String addWasList(String clientAbsolutePath){
		if(clientAbsolutePath!=null && !clientAbsolutePath.isEmpty()){
			wasClientPathList.add(clientAbsolutePath);
			String absolutePath = clientAbsolutePath.replace('\\', '/');
			String distServerPath = distDir + absolutePath.substring(absolutePath.lastIndexOf('/'));
			wasDistPathList.add(distServerPath);
			return distServerPath;
		}
		return null;
	}
	
	/** 컨텐츠 배포, 물리적으로 WEB, WAS 서버에 파일을 가져다 놓음 */
	public void distribute() throws IOException, CmException {
		
		// WEB 배포
		if(webClientPathList!=null && webClientPathList.size() > 0){
			List<DistTarget> webDistTargetList = distManager.getWebList();
			if(distManager.isWebSharedDisk()){
				distributeByCopy(webDistTargetList, webClientPathList, webDistPathList, distManager.getWebCopyBaseDir());
			} else {
				distributeByFtp(webDistTargetList, webClientPathList, webDistPathList, distManager.getWebDistBaseDir());
			}
		}
		
		// WAS 배포
		if(wasClientPathList!=null && wasClientPathList.size() > 0){
			List<DistTarget> wasDistTargetList = distManager.getWasList();
			if(distManager.isWasSharedDisk()){
				distributeByCopy(wasDistTargetList, wasClientPathList, wasDistPathList, distManager.getWasCopyBaseDir());
			} else {
				distributeByFtp(wasDistTargetList, wasClientPathList, wasDistPathList, distManager.getWasDistBaseDir());
			}
		}
	}
	
	/** FTP로 배포함 */
	private void distributeByFtp(
			List<DistTarget> distTargetList,
			List<String> clientPathList,
			List<String> distPathList,
			String serverBaseDir) throws IOException, CmException{
		
		SimpleFTP ftp = null;
		DistTarget distTarget;
		String absolutePath;
		boolean result;
		
		int i, size = distTargetList==null ? 0 : distTargetList.size();
		int j, jsize = clientPathList==null ? 0 : clientPathList.size();
		try {
			for(i=0;i<size;i++){
				distTarget = distTargetList.get(i);
				ftp = new SimpleFTP(distTarget.getIp(), distTarget.getId(), distTarget.getPwd(), locale,
						"Y".equals(distManager.getContextProperty("distribute.ftp.passivemode")));
				ftp.setBaseDir(serverBaseDir.isEmpty() ? "/" : serverBaseDir, "");
				
				for(j=0;j<jsize;j++){
					absolutePath = clientPathList.get(j);
					if(absolutePath==null || absolutePath.isEmpty()) continue;
					if(new File(absolutePath).isFile()){
						result = ftp.sendFromAbsolutePath(absolutePath, serverBaseDir + distPathList.get(j));
						if(!result){
							//cm.msg.ftp.fail.processError=FTP 파일 전송중 오류가 발생 하였습니다.({0})
							throw new CmException("cm.msg.ftp.fail.processError", new String[]{ftp.getReplyMessage()}, locale);
						}
					}
				}
				ftp.close();
				ftp = null;
			}
		} finally {
			try{ if(ftp!=null) ftp.close(); }
			catch(Exception ignore){}
		}
	}
	
	/** 복사에 의해 배포함 */
	private void distributeByCopy(
			List<DistTarget> distTargetList, 
			List<String> clientPathList,
			List<String> distPathList,
			String serverBaseDir) throws IOException {
		
		String absolutePath;
		int j, jsize = clientPathList==null ? 0 : clientPathList.size();
		for(j=0;j<jsize;j++){
			absolutePath = clientPathList.get(j);
			if(absolutePath==null || absolutePath.isEmpty()) continue;
			if(new File(absolutePath).isFile()){
				copyFile(absolutePath, serverBaseDir + distPathList.get(j));
			}
		}
	}
	
	/** 컨텍스트 프로퍼티 값 리턴 */
	public String getContextProperty(String key){
		return distManager.getContextProperty(key);
	}
	
	/** Editor 용 이미지 경로를 생성함 */
	public String getEditorDistPath(String realPath){
		return EDITOR_DIR_FORMAT.format(new Date())+"/"+StringUtil.getNextHexa()+StringUtil.getNextIntHexa()
			+realPath.substring(realPath.lastIndexOf('.')).toLowerCase();
	}
	
	/** srcPath의 파일을 toPath 파일로 복사함 */
	public boolean copyFile(String srcPath, String toPath) throws IOException{
		return copyStream(new FileInputStream(srcPath), toPath);
	}
	
	/** InputStream 을 읽어서 toPath 경로의 파일을 생성함 */
	public boolean copyStream(InputStream in, String toPath) throws IOException{
		
		// 디렉토리 생성
		File dir = new File(toPath.substring(0, toPath.lastIndexOf('/')));
		if(!dir.isDirectory()) dir.mkdirs();
		
		FileOutputStream out = null;
		try{
			int len;
			byte[] bytes = new byte[2048];
			out = new FileOutputStream(toPath);
			while((len = in.read(bytes, 0, 2048))>0){
				out.write(bytes, 0, len);
			}
			return true;
		} finally {
			if(in!=null){
				try{ in.close(); } catch(Exception ignore){}
			}
			if(out!=null){
				try{ out.close(); } catch(Exception ignore){}
			}
		}
	}
	
	/** 기본 경로 조회 */
	public String getBaseDir(){
		return distManager.getWebCopyBaseDir();
	}
}
