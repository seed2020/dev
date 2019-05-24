package com.innobiz.orange.web.wj.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wj.vo.WjWorkReprtBVo;
import com.innobiz.orange.web.wj.vo.WjWorkReprtFileDVo;

@Service
public class WjWorkReprtSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WjDailyLogSvc.class);
	
	/** 공통 서비스 */
	@Autowired
	private WjCmSvc wjCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wjFileSvc")
	private WjFileSvc wjFileSvc;
	
	/** 조직 공통 서비스 *//*
	@Autowired
	private OrCmSvc orCmSvc;*/
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, CommonVo commonVo, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("taskLogAllCompEnable")==null || !"Y".equals(sysPlocMap.get("taskLogAllCompEnable"))){ // 전사여부가 'N' 이면 내 회사만 조회
			VoUtil.setValue(commonVo, "compId", compId);
			return;
		}
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전체 조회
			VoUtil.setValue(commonVo, "compId", null);
			return;
		}
		// 전사여부 'Y' and 계열사 여부 'Y' 일 경우 계열사 데이터 조회(계열사 없으면 내 회사)
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
			List<String> affiliateIds=ptCompBVo.getAffiliateIds();
			affiliateIds.add(compId);
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(affiliateIds);
			affiliateIds = new ArrayList<String>(hs);
			VoUtil.setValue(commonVo, "compId", null);
			VoUtil.setValue(commonVo, "compIdList", affiliateIds);
		}else{
			VoUtil.setValue(commonVo, "compId", compId);
		}
	}
	
	/** 저장 */
	public String save(HttpServletRequest request, QueryQueue queryQueue, String reprtNo) throws CmException, SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WjWorkReprtBVo wjWorkReprtBVo = new WjWorkReprtBVo();
		VoUtil.bind(request, wjWorkReprtBVo);
		wjWorkReprtBVo.setQueryLang(langTypCd);
		
		// 등록자UID
		String regrUid = userVo.getUserUid();
		if(reprtNo==null || reprtNo.isEmpty()){
			reprtNo=wjCmSvc.createNo("WJ_WORK_REPRT_B").toString();
			wjWorkReprtBVo.setCompId(userVo.getCompId());
			wjWorkReprtBVo.setReprtNo(reprtNo);
			wjWorkReprtBVo.setOrgId(userVo.getOrgId());
			wjWorkReprtBVo.setRegrUid(regrUid);
			wjWorkReprtBVo.setRegDt("sysdate");
			queryQueue.insert(wjWorkReprtBVo);
			
		}else{
			wjWorkReprtBVo.setModrUid(regrUid);
			wjWorkReprtBVo.setModDt("sysdate");
			queryQueue.update(wjWorkReprtBVo);
		}
		
		return reprtNo;
	}
	
	/** 삭제 */
	public void delete(QueryQueue queryQueue, UserVo userVo, String reprtNo, List<CommonFileVo> deletedFileList) throws SQLException{
		if(deletedFileList!=null){
			// 파일 삭제
			List<CommonFileVo> list=wjFileSvc.deleteFile(reprtNo, queryQueue);
			if(list!=null && list.size()>0) deletedFileList.addAll(list);
		}else{
			// 파일 삭제
			wjFileSvc.deleteFile(reprtNo, queryQueue);
		}
					
		// 업무보고 삭제
		WjWorkReprtBVo wjWorkReprtBVo = new WjWorkReprtBVo();
		wjWorkReprtBVo.setCompId(userVo.getCompId());
		wjWorkReprtBVo.setReprtNo(reprtNo);
		
		queryQueue.delete(wjWorkReprtBVo);
		
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("wj");
		if(bodySize==null) bodySize=0;
		// 시스템 설정 조회 - 본문 사이즈
		bodySize = bodySize * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 첨부 여부 세팅 */
	public void setFileCnt(String compId, List<WjWorkReprtBVo> list) throws SQLException{
		if(list==null || list.size()==0) return;
			
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		WjWorkReprtFileDVo wjWorkReprtFileDVo = new WjWorkReprtFileDVo();
		
		for(WjWorkReprtBVo storedWjWorkReprtBVo : list){
			wjWorkReprtFileDVo.setRefId(storedWjWorkReprtBVo.getReprtNo());
			storedWjWorkReprtBVo.setFileCnt(commonDao.count(wjWorkReprtFileDVo));
		}
	}
}
