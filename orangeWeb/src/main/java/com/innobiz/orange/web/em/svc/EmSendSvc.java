package com.innobiz.orange.web.em.svc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.em.vo.EmSendBVo;
import com.innobiz.orange.web.em.vo.EmSendFileDVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 게시파일 서비스 */
@Service
public class EmSendSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmFileUploadSvc.class);
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "emfiles";

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 저장된 보내기 데이터 조회 */
	public EmSendBVo getEmSendBVo(String sendNo, Boolean isMail) throws SQLException {
		EmSendBVo emSendBVo = new EmSendBVo();
		if(isMail!=null && isMail) emSendBVo.setEmailNo(sendNo);
		else emSendBVo.setSendNo(sendNo); 
		if(isMail!=null) emSendBVo.setMail(isMail.booleanValue());
		return (EmSendBVo)commonDao.queryVo(emSendBVo);
	}
	
	/** 첨부파일 리턴 (DB) */
	public EmSendFileDVo getFileVo(String sendNo, Integer fileSeq, Boolean isMail) throws SQLException {
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		EmSendFileDVo emSendFileDVo = new EmSendFileDVo();
		if(isMail!=null && isMail) emSendFileDVo.setEmailNo(sendNo);
		else emSendFileDVo.setSendNo(sendNo); 
		emSendFileDVo.setFileSeq(fileSeq);
		if(isMail!=null) emSendFileDVo.setMail(isMail.booleanValue());
		emSendFileDVo = (EmSendFileDVo) commonDao.queryVo(emSendFileDVo);
		return emSendFileDVo;
	}
	
	/** 첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<EmSendFileDVo> getFileVoList(String sendNo, Boolean isMail) throws SQLException {
		if (sendNo == null) return new ArrayList<EmSendFileDVo>();
		
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		EmSendFileDVo emSendFileDVo = new EmSendFileDVo();
		if(isMail!=null && isMail) emSendFileDVo.setEmailNo(sendNo);
		else emSendFileDVo.setSendNo(sendNo); 
		if(isMail!=null) emSendFileDVo.setMail(isMail.booleanValue());
		return (List<EmSendFileDVo>) commonDao.queryList(emSendFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(ModelMap model, String compId, String module, String filesId) throws SQLException {
		model.put("filesId", filesId);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, module);
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 첨부파일 복사 */
	public void setFileVo(CommonVo fromVo, CommonVo toVo, String... atts) {
		for(String attNm : atts){
			VoUtil.setValue(toVo, attNm, VoUtil.getValue(fromVo, attNm));
		}
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	public String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		String distPath = distHandler.addWasList(wasCopyBaseDir+savePath);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		return distPath;
	}
	
	/** binary 를 파일로 변환저장 후 임시저장경로 리턴*/
	public String saveByteToFile(String tmpDir, byte[] bytes, String ext) throws IOException, CmException {
		
		FileOutputStream fos = null;
		ByteArrayInputStream bais = null;
		// 새이름
		String distPath = tmpDir.replace('\\', '/').substring(0, tmpDir.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
		try{
			fos = new FileOutputStream(new File(distPath));
			bais = new ByteArrayInputStream(bytes);
			int character;
			while( (character = bais.read() ) != -1 ){
				fos.write(character);// 파일에 쓰기
			}
		}catch(IOException e){
			System.out.println("Error message: " + e.getMessage());
		}finally{
			if(bais != null){
				bais.close();
			}
			if(fos != null){
				fos.close();
			}
		}
		
		return distPath;
	}
	
}
