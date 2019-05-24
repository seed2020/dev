package com.innobiz.orange.web.cu.svc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.utils.CuConstant;
import com.innobiz.orange.web.cu.vo.CuTaskStatBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 나우스 서비스 */
@Service
public class CuNausSvc {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuNausSvc.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNausFileSvc")
	private CuNausFileSvc cuNausFileSvc;
	
	/** 시퀀스ID 생성 */
	public String createId(String typCd) throws SQLException {
		if(typCd==null || typCd.isEmpty())
			return String.valueOf(commonSvc.nextVal("CU_TASK_STAT_B").intValue());
		else if("REPRT".equals(typCd))
			return String.valueOf(commonSvc.nextVal("CU_WEEK_REPRT_B").intValue());
		return null;
	}
	
	/** 첨부파일 ID 생성 */
	public Integer createFileId(String typCd) throws SQLException {
		if(typCd==null || typCd.isEmpty())
			return commonSvc.nextVal("CU_TASK_STAT_FILE_D").intValue();
		else if("REPRT".equals(typCd))
			return commonSvc.nextVal("CU_WEEK_REPRT_FILE_D").intValue();
		return null;
	}
	
	/** 테이블 구분 세팅 */
	public String setTypCd(HttpServletRequest request, CuTaskStatBVo cuTaskStatBVo){
		String typCd=null;
		if(request.getRequestURI().startsWith("/cu/reprt/") || request.getRequestURI().startsWith("/cu/adm/reprt/"))
			typCd="REPRT";
		if(cuTaskStatBVo!=null)		
			cuTaskStatBVo.setTypCd(typCd);
		return typCd;
	}
	
	/** 저장 */
	public String saveTask(HttpServletRequest request, QueryQueue queryQueue, String statNo, String typCd) throws CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 조회조건 매핑
		CuTaskStatBVo cuTaskStatBVo = new CuTaskStatBVo();
		VoUtil.bind(request, cuTaskStatBVo);
		cuTaskStatBVo.setCompId(userVo.getCompId()); // 회사ID
		
		// 테이블 구분(업무현황, 주간보고)
		if(typCd!=null) cuTaskStatBVo.setTypCd(typCd);
		
		// 신규 여부
		boolean isNew=statNo==null || statNo.isEmpty();
		
		if (isNew) {
			statNo=createId(typCd);
			// ID 생성
			cuTaskStatBVo.setStatNo(statNo);				
			// 등록자, 등록일시
			cuTaskStatBVo.setRegrUid(userVo.getUserUid());
			cuTaskStatBVo.setRegDt("sysdate");
			// INSERT
			queryQueue.insert(cuTaskStatBVo);
			
		} else{
			// 비공개여부
			if(cuTaskStatBVo.getPrivYn()==null)
				cuTaskStatBVo.setPrivYn("");
			
			// 수정자, 수정일시
			cuTaskStatBVo.setModrUid(userVo.getUserUid());
			cuTaskStatBVo.setModDt("sysdate");
			// UPDATE
			queryQueue.update(cuTaskStatBVo);
		}
					
		return statNo;
	}
	
	/** 삭제 */
	public void deleteTask(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, 
			String statNo, List<CommonFileVo> deletedFileList, boolean isAdmin, String typCd) throws SQLException, CmException{
		
		// 파일 삭제
		deleteFile(queryQueue, deletedFileList, statNo, typCd);
		
		// 현황 삭제
		CuTaskStatBVo cuTaskStatBVo = new CuTaskStatBVo();
		cuTaskStatBVo.setStatNo(statNo);
		if(typCd!=null) cuTaskStatBVo.setTypCd(typCd);
		queryQueue.delete(cuTaskStatBVo);
	}
	
	/** 파일 삭제 */
	public void deleteFile(QueryQueue queryQueue, List<CommonFileVo> deletedFileList, String reqNo, String typCd) throws SQLException{
		List<CommonFileVo> list=null;
		if(deletedFileList!=null){
			list=cuNausFileSvc.deleteTaskFile(reqNo, queryQueue, typCd);
			if(list!=null && list.size()>0) deletedFileList.addAll(list);
		}else{
			// 파일 삭제
			cuNausFileSvc.deleteTaskFile(reqNo, queryQueue, typCd);
		}
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("cu") * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 권한 체크 */
	public boolean hasAuth(HttpServletRequest request, String... chkList){
		boolean isAuth=false;
		for(String auth : chkList){
			isAuth=SecuUtil.hasAuth(request, auth, null);
			if(isAuth) break;				
		}
		
		return isAuth;
	}
	
	
	/** [주간보고] - 환경 설정 로드 */
	public Map<String, String> getReprtConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(CuConstant.REPRT_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty())
			return null;
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
}
