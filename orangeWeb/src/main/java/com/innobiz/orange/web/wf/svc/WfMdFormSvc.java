package com.innobiz.orange.web.wf.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfMdFormDVo;
import com.innobiz.orange.web.wf.vo.WfMdWorksDVo;
import com.innobiz.orange.web.wf.vo.WfWorksLVo;

@Service
public class WfMdFormSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfMdFormSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wfFileSvc")
	private WfFileSvc wfFileSvc;
	
	/** 코드 서비스 */
	@Resource(name = "wfCdSvc")
	private WfCdSvc wfCdSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 등록/양식편집 - 결재 */
	public void setFormByAP(HttpServletRequest request, ModelMap model, String formNo, String mdRefId, String workNo, boolean formEdit) throws SQLException{
		setFormByReleaseDt(request, model, "AP", formNo, mdRefId, null, workNo, formEdit);
	}
	
	/** 등록 - 결재 */
	public void setFormByBB(HttpServletRequest request, ModelMap model, String formNo, String mdRefId, String mdNo) throws SQLException{
		setFormByReleaseDt(request, model, "BB", formNo, mdRefId, mdNo, null, false);
	}
	
	/** 모듈별 양식 등록/수정/편집 화면 세팅
	 * 1.결재
	 * - 양식관리 > 양식편집 ==>  mdCd:AP, formNo:formNo, mdRefId:formId(양식ID), mdNo:null, workNo:null, formEdit:true
	 * - 최초 상신 ==> mdCd:AP, formNo:formNo, mdRefId:formId(양식ID), mdNo:null, workNo:null, formEdit:false
	 * - 진행중 편집 ==> mdCd:AP, formNo:formNo, mdRefId:formId(양식ID), mdNo:null, workNo:workNo, formEdit:false
	 * 2.게시판
	 * - 등록 ==> mdCd:BB, formNo:formNo, mdRefId:brdId(게시판ID), mdNo:null, workNo:null, formEdit:false
	 * - 수정 ==> mdCd:BB, formNo:formNo, mdRefId:brdId(게시판ID), mdNo:bullId(게시글ID), workNo:null, formEdit:false
	 * */
	private void setFormByReleaseDt(HttpServletRequest request, ModelMap model, String mdCd, String formNo, String mdRefId, String mdNo, String workNo, boolean formEdit) throws SQLException{
		// 모듈별 양식 화면은 버튼 제거 [해당 모듈의 기능으로 대체]
		model.put("isOnlyMd", Boolean.TRUE);
		model.put("mdCd", mdCd);
		if(formEdit) model.put("formEdit", formEdit);
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			LOGGER.error("[ERROR] - formNo==null || formNo.isEmpty()");
			return;
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormBVo==null || wfFormBVo.getGenId()==null");
			return;
		}
		
		// 외부모듈코드(컨텐츠의 크기 제한, 파일 크기제한, 목록갯수 등 해당 모듈의 설정을 위한 코드)
		String mdTypCd = wfFormBVo.getMdTypCd();
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - !isAllComp && !wfFormBVo.getCompId().equals(compId)");
			return;
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		// 생성ID
		String genId = null;
		
		// 모듈번호로 업무번호 조회[게시판]
		if(!"AP".equals(mdCd) && mdRefId!=null && !mdRefId.isEmpty() && mdNo!=null && !mdNo.isEmpty()){
			WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
			wfMdWorksDVo.setMdCd(mdCd);
			wfMdWorksDVo.setMdRefId(mdRefId);
			wfMdWorksDVo.setMdNo(mdNo);
			wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
			if(wfMdWorksDVo!=null)
				workNo=wfMdWorksDVo.getWorkNo();
		}
				
		if(workNo!=null && !workNo.isEmpty()){
			// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
			WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(genId, formNo, null);
			wfWorksLVo.setQueryLang(langTypCd);
			wfWorksLVo.setWorkNo(workNo);
			Map<String, Object> wfWorksLVoMap = commonDao.queryMap(wfWorksLVo);
			//wfWorksLVo=(WfWorksLVo)commonSvc.queryVo(wfWorksLVo);
			if(wfWorksLVoMap==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				LOGGER.error("[ERROR] - wfWorksLVoMap==null");
				return;
			}
			
			model.put("wfWorksLVoMap", wfWorksLVoMap);
			
			if(!"AP".equals(mdCd)){
				String workGenId=(String)wfWorksLVoMap.get("genId");
				
				if(!workGenId.equals(genId))
					model.put("notGenId", Boolean.TRUE);
			}
			
			// 결재일 경우 등록된 genId 조회
			if("AP".equals(mdCd))
				genId=(String)wfWorksLVoMap.get("genId");
			
			// 코드 목록 조회 세팅
			Map<String, String[]> cdListMap = wfFormSvc.getCodeListMap(formNo, workNo, langTypCd);
			if(cdListMap!=null) model.put("cdListMap", cdListMap);
			
			// 이미지 목록 조회
			wfFormSvc.setImgVoMap(langTypCd, formNo, null, wfWorksLVoMap);
			
		}
		
		if(genId==null || genId.isEmpty())
			genId=wfFormBVo.getGenId();
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormRegDVo==null");
			return;
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 결재양식 등에서 관리자 모드(양식 기본값을 변경 저장할 경우)
		if("AP".equals(mdCd) && (workNo==null || workNo.isEmpty())){
			WfMdFormDVo wfMdFormDVo = getWfMdFormDVo(mdCd, mdRefId, null);
			if(wfMdFormDVo!=null){
				//wfFormRegDVo.setLoutVa(wfMdFormDVo.getLoutVa());
				//wfFormRegDVo.setAttrVa(wfMdFormDVo.getAttrVa());
				model.put("wfMdFormDVo", wfMdFormDVo);
			}
		}
				
		// 양식 코드 목록 세팅
		wfCdSvc.setAllFormCdListMap(model, langTypCd, isAllComp ? null : compId, "Y", true);
		
		// 최대 본문 사이즈 model에 추가
		wfFormSvc.putBodySizeToModel(request, model, mdTypCd);
				
	}
	
	/** [결재] - 상세보기 */
	public void viewFormByAP(HttpServletRequest request, ModelMap model, String formNo, String workNo) throws SQLException{
		viewFormByReleaseDt(request, model, "AP", formNo, null, null, workNo);
	}
	
	/** [게시판] - 상세보기 */
	public void viewFormByBB(HttpServletRequest request, ModelMap model, String formNo, String mdRefId, String mdNo) throws SQLException{
		viewFormByReleaseDt(request, model, "BB", formNo, mdRefId, mdNo, null);
	}
	
	/** 모듈별 양식 상세보기 HTML 세팅 - setFormByReleaseDt 
	 * 1.결재
	 * - workNo 생성된 경우(상신,편집) ==> mdCd:AP, formNo:formNo, mdRefId:null, mdNo:null, workNo:workNo
	 * 2.게시판
	 * - 상세보기(view) ==> mdCd:BB, formNo:formNo, mdRefId:brdId, mdNo:bullId, workNo:null
	 * */
	private void viewFormByReleaseDt(HttpServletRequest request, ModelMap model, String mdCd, String formNo, String mdRefId, String mdNo, String workNo) throws SQLException{
		// 모듈별 양식 화면은 버튼 제거 [해당 모듈의 기능으로 대체]
		model.put("isOnlyMd", Boolean.TRUE);
		model.put("mdCd", mdCd);
		model.put("isMobile", ServerConfig.IS_MOBILE); // 모바일 여부
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			LOGGER.error("[ERROR] - formNo==null || formNo.isEmpty() || mdNo==null || mdNo.isEmpty()");
			return;
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			//return LayoutUtil.getErrorJsp(404);
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormBVo==null || wfFormBVo.getGenId()==null");
			return;
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - !isAllComp && !wfFormBVo.getCompId().equals(compId)");
			return;
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		Map<String, Object> wfWorksLVoMap = null;
		
		// 생성ID
		String genId=null;
		
		// 결재가 아닌 경우 workNo 및 genId 조회
		if(!"AP".equals(mdCd) && mdRefId!=null && !mdRefId.isEmpty() && mdNo!=null && !mdNo.isEmpty()){
			// 모듈번호로 업무번호 조회
			WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
			wfMdWorksDVo.setMdCd(mdCd);
			wfMdWorksDVo.setMdRefId(mdRefId);
			wfMdWorksDVo.setMdNo(mdNo);
			wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
			
			if(wfMdWorksDVo!=null){
				workNo=wfMdWorksDVo.getWorkNo();
			}
		}
				
		if(workNo!=null && !workNo.isEmpty()){
			// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
			WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, null);
			wfWorksLVo.setQueryLang(langTypCd);
			wfWorksLVo.setWorkNo(workNo);
			wfWorksLVoMap = commonDao.queryMap(wfWorksLVo);
			if(wfWorksLVoMap!=null){
				genId=(String)wfWorksLVoMap.get("genId");
				model.put("wfWorksLVoMap", wfWorksLVoMap);
			}
			
		}
		
		// 생성ID
		if(genId==null || genId.isEmpty())
			genId = wfFormBVo.getGenId();
				
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormRegDVo==null");
			return;
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
				
		if(wfWorksLVoMap!=null){
			// 코드 목록 조회
			wfFormSvc.setCodeVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
			
			// 이미지 목록 조회
			wfFormSvc.setImgVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
		}
	}
	
	/** 모듈별 양식 상세보기 화면 세팅 - [mdRefId:모듈참조ID(결재양식번호/게시판ID),mdNo:모듈번호(결재번호,게시번호)*//*
	public void setViewByMdToForm(HttpServletRequest request, ModelMap model, String mdCd, String genId, String formNo, String mdRefId, String mdNo) throws SQLException{
		// 모듈별 양식 화면은 버튼 제거 [해당 모듈의 기능으로 대체]
		model.put("isOnlyMd", Boolean.TRUE);
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty() || mdNo==null || mdNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			LOGGER.error("[ERROR] - formNo==null || formNo.isEmpty() || workNo==null || workNo.isEmpty()");
			return;
		}
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wf/adm/");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			//return LayoutUtil.getErrorJsp(404);
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormBVo==null || wfFormBVo.getGenId()==null");
			return;
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - !isAllComp && !wfFormBVo.getCompId().equals(compId)");
			return;
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		String workNo=null;
		// 모듈번호로 업무번호 조회
		if(mdNo!=null && !mdNo.isEmpty()){
			WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
			wfMdWorksDVo.setMdCd(mdCd);
			wfMdWorksDVo.setMdRefId(mdRefId);
			wfMdWorksDVo.setMdNo(mdNo);
			wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
			if(wfMdWorksDVo==null) return;
			
			workNo=wfMdWorksDVo.getWorkNo();			
		}
		
		if(workNo==null || workNo.isEmpty()) return;
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, null);
		wfWorksLVo.setQueryLang(langTypCd);
		wfWorksLVo.setWorkNo(workNo);
		Map<String, Object> wfWorksLVoMap = commonDao.queryMap(wfWorksLVo);
		//wfWorksLVo=(WfWorksLVo)commonSvc.queryVo(wfWorksLVo);
		if(wfWorksLVoMap==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfWorksLVoMap==null");
			return;
		}
		// 목록 권한 세팅
		wfFormSvc.setAuthVoMap(userVo, wfWorksLVoMap, isAdmin);
				
		model.put("wfWorksLVoMap", wfWorksLVoMap);
		
		if(genId==null) genId=(String)wfWorksLVoMap.get("genId");
		
		if(genId==null) genId=wfFormBVo.getGenId();
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormRegDVo==null");
			return;
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 조회수 저장
		//wfFormSvc.setWorksReadCnt(formNo, workNo, userVo.getUserUid(), wfWorksLVo, isAdmin);
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
				
		// 코드 목록 조회
		wfFormSvc.setCodeVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
		
		// 이미지 목록 조회
		wfFormSvc.setImgVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
					
		// 첨부파일 리스트 model에 추가
		wfFileSvc.putFileListToModel(formNo, workNo, model, userVo.getCompId(), null);
	}*/
	
	/** 모듈별 양식 저장(관리자 화면에서 양식 수정 데이타) */
	public void saveByMdFormData(HttpServletRequest request, QueryQueue queryQueue, String mdCd, String genId, String formNo, String mdRefId) throws SQLException, CmException{
		
		// 모듈양식상세
		WfMdFormDVo wfMdFormDVo = new WfMdFormDVo();
		wfMdFormDVo.setMdCd(mdCd);
		wfMdFormDVo.setMdRefId(mdRefId);
		wfMdFormDVo.setFormNo(formNo);
		wfMdFormDVo.setGenId(genId);
		String[] paramKeys=new String[]{"jsonVa", "loutVa", "attrVa"};
		for(String key : paramKeys){
			if(request.getParameter(key)!=null){
				VoUtil.setValue(wfMdFormDVo, key, request.getParameter(key));
			}
		}
		queryQueue.store(wfMdFormDVo);
		
	}
	
	/** 모듈별 양식 삭제 */
	public void deleteByMdFormData(QueryQueue queryQueue, String mdCd, String mdRefId) throws SQLException, CmException{
		// 모듈양식상세
		WfMdFormDVo wfMdFormDVo = new WfMdFormDVo();
		wfMdFormDVo.setMdCd(mdCd);
		wfMdFormDVo.setMdRefId(mdRefId);
		queryQueue.delete(wfMdFormDVo);
	}
	
	/** 모듈별 양식 데이터 조회 */
	public WfMdFormDVo getWfMdFormDVo(String mdCd, String mdRefId, String formNo) throws SQLException{
		if(mdCd==null || mdRefId==null) return null;
		// 모듈양식상세
		WfMdFormDVo wfMdFormDVo = new WfMdFormDVo();
		wfMdFormDVo.setMdCd(mdCd);
		wfMdFormDVo.setMdRefId(mdRefId);
		if(formNo!=null) wfMdFormDVo.setFormNo(formNo);
		return (WfMdFormDVo)commonDao.queryVo(wfMdFormDVo);
	}
	
	/** 모듈별 업무 매핑 데이타 저장 */
	public void saveByMdWorksData(HttpServletRequest request, QueryQueue queryQueue, String mdCd, String genId, String formNo, String mdRefId, String mdNo, String workNo) throws SQLException, CmException{
		
		// 모듈양식상세
		WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
		wfMdWorksDVo.setMdCd(mdCd);
		wfMdWorksDVo.setMdRefId(mdRefId);
		wfMdWorksDVo.setMdNo(mdNo);
		wfMdWorksDVo.setFormNo(formNo);
		wfMdWorksDVo.setGenId(genId);
		wfMdWorksDVo.setWorkNo(workNo);
		queryQueue.store(wfMdWorksDVo);
	}
	
	/** 모듈별 업무 컬럼 데이터 리턴 */
	public List<String> getByMdWorksColmData(String langTypCd, String mdCd, String formNo, String mdRefId, String mdNo, String workNo, String colmTypCd) throws SQLException{
		// 모듈번호로 업무번호 조회[게시판]
		if(!"AP".equals(mdCd) && mdRefId!=null && !mdRefId.isEmpty() && mdNo!=null && !mdNo.isEmpty()){
			WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
			wfMdWorksDVo.setMdCd(mdCd);
			wfMdWorksDVo.setMdRefId(mdRefId);
			wfMdWorksDVo.setMdNo(mdNo);
			wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
			if(wfMdWorksDVo!=null)
				workNo=wfMdWorksDVo.getWorkNo();
		}
		if(workNo!=null && !workNo.isEmpty()){
			// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
			WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, null);
			wfWorksLVo.setQueryLang(langTypCd);
			wfWorksLVo.setWorkNo(workNo);
			Map<String, Object> wfWorksLVoMap = commonDao.queryMap(wfWorksLVo);
			if(wfWorksLVoMap==null || !wfWorksLVoMap.containsKey("jsonVa")){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				LOGGER.error("[ERROR] - wfWorksLVoMap==null");
				return null;
			}			
			String genId=(String)wfWorksLVoMap.get("genId");
			if(genId==null) return null;
			List<String> returnList=new ArrayList<String>();
			
			List<String> useColmNmList=new ArrayList<String>();
			
			// 컬럼 목록 조회
			List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				if(colmTypCd.equals(storedWfFormColmLVo.getColmTypCd()))
					useColmNmList.add(storedWfFormColmLVo.getColmNm());
			}
			
			JSONObject jsonVa = (JSONObject) JSONValue.parse((String)wfWorksLVoMap.get("jsonVa"));
			
			if(jsonVa!=null){
				for(String colmNm : useColmNmList){
					if(jsonVa.containsKey(colmNm) && jsonVa.get(colmNm)!=null)
						returnList.add((String)jsonVa.get(colmNm));
				}
			}
			
			if(returnList.size()>0) return returnList;
			
		}
		return null;
	}
	
	/** 결재 저장 */
	public Map<String,String> saveFormByAP(HttpServletRequest request, QueryQueue queryQueue, String genId, String formNo, String workNo, Map<String, Object> paramMap) throws SQLException, IOException, CmException{
		return saveFormByReleaseDt(request, queryQueue, "AP", genId, formNo, null, null, workNo, paramMap);
	}
	
	/** 게시판 저장 */
	public Map<String,String> saveFormByBB(HttpServletRequest request, QueryQueue queryQueue, String genId, String formNo, String mdRefId, String mdNo) throws SQLException, IOException, CmException{
		return saveFormByReleaseDt(request, queryQueue, "BB", null, formNo, mdRefId, mdNo, null, null);
	}
	
	/** 모듈별 양식 데이타 저장[사용자화면에서 결재(상신)/게시판(글쓰기) 데이타]
	 * 1.결재
	 * - 최초 상신시 mdCd:AP, genId:null, formNo:formNo, mdRefId:null, mdNo:null, workNo:null
	 * - 상신 이후 : mdCd:AP, genId:기존 genId, formNo:formNo, mdRefId:null, mdNo:null, workNo:수정일 경우 not null
	 * 2.게시판
	 * - 저장 : mdCd:BB, genId:null, formNo:formNo, mdRefId:brdId, mdNo:bullId(신규면null), workNo:null
	 * */
	private Map<String,String> saveFormByReleaseDt(HttpServletRequest request, QueryQueue queryQueue, String mdCd, String genId, String formNo, String mdRefId, String mdNo, String workNo, Map<String, Object> paramMap) throws SQLException, IOException, CmException{
		
		boolean nullQueryQueue=queryQueue==null;
		
		if(nullQueryQueue)
			queryQueue=new QueryQueue();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
		
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - wfFormBVo==null || wfFormBVo.getGenId()==null");
			return null;
			//throw new CmException("cm.msg.noData", request);
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			LOGGER.error("[ERROR] - !isAllComp && !wfFormBVo.getCompId().equals(compId)");
			return null;
			//throw new CmException("cm.msg.noData", request);
		}
		
		// 생성ID
		if(genId==null || genId.isEmpty())
			genId = wfFormBVo.getGenId();
		
		// 모듈번호로 업무번호 조회
		if(!"AP".equals(mdCd) && mdNo!=null && !mdNo.isEmpty()){
			WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
			wfMdWorksDVo.setMdCd(mdCd);
			wfMdWorksDVo.setMdRefId(mdRefId);
			wfMdWorksDVo.setMdNo(mdNo);
			wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
			if(wfMdWorksDVo!=null)			
				workNo=wfMdWorksDVo.getWorkNo();			
		}
		
		// 상태코드[결재에서만 신규등록시 임시저장 상태]
		String statCd = "AP".equals(mdCd) && (workNo==null || workNo.isEmpty()) ? "T" : null;
		
		// 저장
		String refId=wfFormSvc.saveWorks(request, queryQueue, genId, formNo, workNo, statCd, paramMap, false);
		
		if(!"AP".equals(mdCd) && (workNo==null || workNo.isEmpty())){
			saveByMdWorksData(request, queryQueue, mdCd, genId, formNo, mdRefId, mdNo, refId);
		}
		
		List<String> deleteFilePathList=null;
		
		if(workNo!=null){
			// 삭제할 이미지 번호 목록
			String delImgNo = ParamUtil.getRequestParam(request, "delImgNoList", false);
			
			if(delImgNo!=null && !delImgNo.isEmpty()){
				String[] delImgNos=delImgNo.split(",");
				List<String> imgNoList=new ArrayList<String>();
				for(String imgNo : delImgNos){
					imgNoList.add(imgNo.trim());
				}
				// 이미지 삭제
				deleteFilePathList=wfFormSvc.deleteImgList(queryQueue, formNo, workNo, imgNoList);
			}
		}
		
		if(nullQueryQueue && !queryQueue.isEmpty())
			commonDao.execute(queryQueue);
		
		// 실제 이미지 파일 삭제	
		if(deleteFilePathList!=null && deleteFilePathList.size()>0)
			wfFileSvc.deleteWebFiles(deleteFilePathList);
		
		Map<String, String> returnMap=new HashMap<String, String>();
		returnMap.put("genId", genId);
		returnMap.put("workNo", refId);
					
		return returnMap;
	}
	
	
	/** 결재 관련 양식 삭제 */
	public void deleteFormByAP(QueryQueue queryQueue, String formNo, String workNo) throws SQLException, CmException{
		System.out.println("[deleteFormByAP] - formNo : "+formNo+"\tworkNo : "+workNo);
		deleteWorks(queryQueue, formNo, workNo);
	}
	
	/** 모듈별 업무 데이터 삭제 */
	public void deleteMdByWorks(QueryQueue queryQueue, String formNo, String mdCd, String mdRefId, String mdNo) throws SQLException, CmException{
		System.out.println("[deleteMdByWorks] - formNo : "+formNo+"\tmdNo : "+mdNo);
		if(formNo==null || mdRefId==null || mdNo==null) return;
		
		String workNo=getMdByWorkNo(queryQueue, mdCd, mdRefId, mdNo, true);
		if(workNo==null || workNo.isEmpty()) return;
		
		deleteWorks(queryQueue, formNo, workNo);
	}
	
	/** 모듈별 업무 번호 조회 */
	private String getMdByWorkNo(QueryQueue queryQueue, String mdCd, String mdRefId, String mdNo, boolean isDel) throws SQLException{
		// 모듈번호로 업무번호 조회
		WfMdWorksDVo wfMdWorksDVo = new WfMdWorksDVo();
		wfMdWorksDVo.setMdCd(mdCd);
		wfMdWorksDVo.setMdRefId(mdRefId);
		wfMdWorksDVo.setMdNo(mdNo);
		wfMdWorksDVo=(WfMdWorksDVo)commonDao.queryVo(wfMdWorksDVo);
		if(wfMdWorksDVo!=null){
			if(isDel) queryQueue.delete(wfMdWorksDVo); // 모듈 데이터도 삭제
			return wfMdWorksDVo.getWorkNo();
		}
		return null;
	}
	
	/** 업무 데이터 삭제 */
	private void deleteWorks(QueryQueue queryQueue, String formNo, String workNo) throws SQLException, CmException{
		// 삭제할 이미지 경로
		List<String> deleteFilePathList=new ArrayList<String>();
		
		// 데이터 삭제
		wfFormSvc.deleteWorks(queryQueue, null, formNo, workNo, null, deleteFilePathList, false);
		
		// 이미지 제거
		if(deleteFilePathList.size()>0)
			wfFileSvc.deleteWebFiles(deleteFilePathList);
					
	}
	
	/** 결재 관련 양식 수정[완료 컬럼 커밋] - 완료(C) 로 변경 */
	public void commitFormByAP(QueryQueue queryQueue, String formNo, String workNo) throws SQLException, CmException{
		System.out.println("[updateCommitByAP] - formNo : "+formNo+"\tworkNo : "+workNo);
		if(formNo==null || formNo.isEmpty() || workNo==null || workNo.isEmpty()) return;
		
		// 테이블관리 기본(W) 테이블 - BIND
		WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, false);
		wfWorksLVo.setFormNo(formNo);
		wfWorksLVo.setWorkNo(workNo);
		wfWorksLVo.setStatCd("C"); // 완료
		queryQueue.update(wfWorksLVo);
	}
	
}
