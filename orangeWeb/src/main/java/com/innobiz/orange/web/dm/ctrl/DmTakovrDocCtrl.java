package com.innobiz.orange.web.dm.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmTaskSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.dm.vo.DmDocDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmTakovrBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Controller
public class DmTakovrDocCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmTakovrDocCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 작업이력 서비스 */
	@Resource(name = "dmTaskSvc")
	private DmTaskSvc dmTaskSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** [AJAX] 인수인계 - 인계,인계취소,인수(승인,반려) */
	@RequestMapping(value = {"/dm/doc/transTakovrAjx","/dm/adm/doc/transTakovrAjx"})
	public String transTakovrAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docGrpId = (String) object.get("docGrpId");
			String mode = (String) object.get("mode");
			// 상태코드
			String statCd = (String) object.get("statCd");
			if ( docGrpId == null || docGrpId.isEmpty() || mode == null || mode.isEmpty() || statCd == null || statCd.isEmpty()) {
				LOGGER.error("[ERROR] docGrpId == null || docGrpId.isEmpty() || mode == null || mode.isEmpty() || statCd == null || statCd.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 대상조직ID
			String tgtOrgId = (String) object.get("tgtOrgId");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 인계문서 저장
			if("transSave".equals(mode) && (tgtOrgId == null || tgtOrgId.isEmpty() )){
				LOGGER.error("[ERROR] mode is 'transSave' - tgtOrgId == null || tgtOrgId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 회사여부 - 회사관리자
			String compYn = (String) object.get("compYn");
			if(compYn == null || compYn.isEmpty()) compYn = "N";
			
			// 선택인계 시 대상조직ID와 현재 조직ID가 같으면 오류처리
			if("transSave".equals(mode) && (("N".equals(compYn) && tgtOrgId.equals(userVo.getOrgId())) || ("Y".equals(compYn) && tgtOrgId.equals(userVo.getCompId())))){
				LOGGER.error("[ERROR] mode is 'transSave' - tgtOrgId.equals(userVo.getOrgId())");
				// dm.msg.not.trans.compDup=동일한 회사에 인계할 수 없습니다.
				// dm.msg.not.trans.orgDup=동일한 부서에 인계할 수 없습니다.
				throw new CmException("dm.msg.not.trans."+("Y".equals(compYn) ? "comp" : "org")+"Dup", request);
			}
			/** 회사ID */
			String paramCompId = (String) object.get("paramCompId");
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 문서그룹ID 배열
			String[] docGrpIds = docGrpId.split(",");
			// 관리자여부
			boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
			
			// 인계대상 변경시 - 사용자
			if(!isAdmin && "transSave".equals(mode)){
				DmFldBVo dmFldBVo = null;
				DmDocLVo dmDocLVo = null;
				List<String> docGrpList = new ArrayList<String>();
				for(String grpId : docGrpIds){
					dmDocLVo = dmDocSvc.getDmDocLVo("ko", dmStorBVo, null, grpId);
					if(dmDocLVo.getFldId().equals(DmConstant.EMPTY_CLS)) continue;
					// 폴더 조회
					dmFldBVo = dmDocSvc.getDmFldBVo(storId, dmDocLVo.getFldId(), "ko");
					if(dmFldBVo == null) continue;
					if(!userVo.getOrgId().equals(dmFldBVo.getFldGrpId())) continue;
					docGrpList.add(grpId);
				}
				docGrpIds = ArrayUtil.toArray(docGrpList);
			}
			boolean withSub = false;
			// 문서이관시 하위문서여부 조회
			if(withSub && "transSave".equals(mode)){
				String[] statCds = null;
				boolean isWithSubYn = false;
				// 관리자 환경설정에서 조회
				Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
				if(envConfigMap!=null){
					// 하위문서포함여부
					String withSubYn = envConfigMap.get("takovrWithSubYn");
					if(withSubYn != null && !withSubYn.isEmpty() && "Y".equals(withSubYn)) {
						isWithSubYn = true;
						statCds = new String[]{"C"};
					}
				}
				// 하위문서 포함여부
				if(isWithSubYn){
					DmDocDVo srchDmDocDVo = new DmDocDVo();
					srchDmDocDVo.setTableName(tableName);
					// 문서이동,복사,인수등 - 하위문서포함여부가 'Y' 면 관련문서그룹ID를 배열에 병합한다.
					// 하위문서그룹ID를 조회한다.
					docGrpIds = dmDocSvc.getSubDocIdList(srchDmDocDVo, docGrpIds, statCds);
				}
			}
			
			// 문서상세
			DmDocDVo dmDocDVo = null;
			// 인수인계기본
			DmTakovrBVo dmTakovrBVo = null;
			for(String id : docGrpIds){
				if("transSave".equals(mode) || "transCancel".equals(mode)){
					// 문서 상태코드 변경
					dmDocDVo = new DmDocDVo();
					dmDocDVo.setTableName(tableName);
					dmDocDVo.setDocGrpId(id);
					dmDocDVo.setStatCd(statCd);
					queryQueue.update(dmDocDVo);
					// 문서이력을 담을지 여부?
				}
				
				// 인수인계기본 저장
				dmTakovrBVo = new DmTakovrBVo();
				dmTakovrBVo.setStorId(storId);
				dmTakovrBVo.setDocGrpId(id);				
				if(commonSvc.count(dmTakovrBVo)>0){
					// 인계취소일 경우에는 인수인계기본 목록을 삭제한다.
					if("transCancel".equals(mode)){
						queryQueue.delete(dmTakovrBVo);
						continue;
					}
					// 선택인계 시 대상조직ID를 변경해준다.
					if("transSave".equals(mode)){
						dmTakovrBVo.setTgtOrgId(tgtOrgId);
					}
					dmTakovrBVo.setTakStatCd(statCd);
					dmTakovrBVo.setTakRegDt("sysdate");
					queryQueue.update(dmTakovrBVo);
					continue;
				}
				dmTakovrBVo.setTakStatCd(statCd);
				dmTakovrBVo.setTakOrgId("Y".equals(compYn) ? userVo.getCompId() : userVo.getOrgId());
				dmTakovrBVo.setTgtOrgId(tgtOrgId);
				dmTakovrBVo.setTakRegrUid(userVo.getUserUid());
				dmTakovrBVo.setTakRegDt("sysdate");
				dmTakovrBVo.setCompYn(compYn);
				queryQueue.insert(dmTakovrBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// 상황에 맞는 메세지코드
			String msgCode = (String) object.get("msgCode");
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage(msgCode != null && !msgCode.isEmpty() ? msgCode : "cm.msg.save.success", request));
						
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 문서 인수 - 부서(동일한 저장소 내에서)*/
	@RequestMapping(value = "/dm/doc/transTakovrDoc")
	public String transTakovrDoc(HttpServletRequest request,
			@RequestParam(value = "docGrpId", required = false) String docGrpId,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {

		try {
			if (docGrpId == null || docGrpId.isEmpty()) {
				LOGGER.error("[ERROR] docGrpId == null || docGrpId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			
			String storId = dmStorBVo.getStorId();
			// 테이블명
			String tableName = dmStorBVo.getTblNm();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 문서ID배열
			String[] docGrpIds = docGrpId.split(",");
			
			// 관련문서그룹 맵
			Map<String,List<DmDocDVo>> subDocVoMap = new HashMap<String,List<DmDocDVo>>();
			
			// 문서그룹 맵 세팅[그룹별] 
			dmDocSvc.setSubDocGrpMap(tableName, subDocVoMap, docGrpIds, null, null, false);
			
			// 최상위 문서그룹ID 맵
			Map<String,String> topDocMap = new HashMap<String,String>();
			// 관련문서 전체 목록
			List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
			// 관련문서 전체 목록맵
			Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			DmDocDVo parentVo = null;
			if(docPid != null && !docPid.isEmpty()) parentVo = dmDocSvc.getDmDocDVo(langTypCd, tableName, docPid);
			
			// 관련 문서맵
			//Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
			// 시작 정렬순서
			Integer strtSortOrdr = parentVo != null ? dmDocSvc.getStrtSortOrdr(tableName, parentVo) : 0;
			
			// 최종 정렬
			Integer totalCnt = dmDocSvc.setSubDocGrpSortOrdrs(null, subDocVoMap, topDocMap, subGrpList, subGrpListMap, parentVo, strtSortOrdr);
			/*System.out.println("totalCnt : "+totalCnt);
			for(DmDocDVo storedDmDocDVo : subGrpList){
				System.out.println("subGrpId : "+storedDmDocDVo.getSubDocGrpId()+"\t sortDpth : "+storedDmDocDVo.getSortDpth() + "\tsortOrdr : "+storedDmDocDVo.getSortOrdr());
			}*/
			// 부모문서가 있으면 삽입된 문서 다음으로 정렬순서를 변경
			if(parentVo != null) {
				DmDocDVo updateDmDocDVo = new DmDocDVo();
				updateDmDocDVo.setTableName(tableName);
				updateDmDocDVo.setSubDocGrpId(parentVo.getSubDocGrpId());
				updateDmDocDVo.setFldId(parentVo.getFldId());
				updateDmDocDVo.setSortOrdr(strtSortOrdr+"");
				updateDmDocDVo.setMaxSortOrdr(totalCnt+"");
				//dmDocSvc.updateMaxSortOrdr(queryQueue, updateDmDocDVo);
				updateDmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateMaxSortOrdr");
				queryQueue.update(updateDmDocDVo);
			}
			if(subGrpList.size()>0){
				// 폴더ID[보내기옵션]
				String fldId = ParamUtil.getRequestParam(request, "fldId", false);
						
				//미분류로의 복사 이동은 안됨
				if(DmConstant.EMPTY_CLS.equals(fldId)){
					// dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.
					throw new CmException("dm.msg.not.save.emptyCls", request);
				}
				
				// 관리자 여부
				boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
				// 인수는 이동으로 저장
				String mode = "move";
				// 이력 저장여부
				boolean isTask = !isAdmin && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), mode);
				// 폴더 조회[문서번호 채번 조건중 폴더명이 있을 경우에 대하여 폴더명을 조회해서 삽입한다.]
				DmFldBVo dmFldBVo = dmDocSvc.getDmFldBVo(storId, fldId, langTypCd);
				
				DmDocLVo originVo = null;
				for(DmDocDVo storedDmDocDVo : subGrpList){
					// 문서조회
					originVo = dmDocSvc.newDmDocLVo(dmStorBVo);
					originVo.setSubYn(storedDmDocDVo.getSubYn());
					originVo.setDocPid(storedDmDocDVo.getDocPid());
					originVo.setSubDocGrpId(storedDmDocDVo.getSubDocGrpId());
					originVo.setFldId(fldId);
					originVo.setSortOrdr(storedDmDocDVo.getSortOrdr());// 순서
					originVo.setSortDpth(storedDmDocDVo.getSortDpth());// 단계
					originVo.setDocGrpId(storedDmDocDVo.getDocGrpId());
					// 상태코드 'C' 로 저장 
					originVo.setStatCd("C");
					if(dmFldBVo != null) originVo.setFldNm(dmFldBVo.getFldNm());
					dmDocSvc.updateTakovrDoc(request, queryQueue, storId, originVo, langTypCd, userVo, tableName);
					
					// 작업 이력저장[원본]
					if(isTask) dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, originVo.getDocGrpId(), userVo.getUserUid(), mode, null);
					
					// 인수인계기본 삭제
					dmDocSvc.delTakovrVo(queryQueue, storId, null, storedDmDocDVo.getDocGrpId());
				}
			}
			// 기존 문서의 정렬순서를 변경한다.
			//dmDocSvc.updateMinSortOrdr(queryQueue, tableName, subDocVoMap);
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadDocFrm(null,'all');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 문서 인수 - 회사 */
	@RequestMapping(value = "/dm/adm/doc/transTakovrDoc")
	public String transTakovrDoc(HttpServletRequest request,
			@RequestParam(value = "docGrpId", required = false) String docGrpId,
			@RequestParam(value = "dialog", required = false) String dialog,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			ModelMap model) throws Exception {

		try {
			if (docGrpId == null || docGrpId.isEmpty() || paramCompId == null || paramCompId.isEmpty()) {
				LOGGER.error("[ERROR] docGrpId == null || docGrpId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			
			String storId = dmStorBVo.getStorId();
			// 테이블명
			String tableName = dmStorBVo.getTblNm();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 파일 복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
			
			// 폴더ID
			String fldId = ParamUtil.getRequestParam(request, "fldId", false);
			
			DmDocLVo tmpOriginVo = null;
			// 정렬할 문서그룹ID 목록
			List<String> docGrpIdList = new ArrayList<String>();
			
			// 기본 저장소 조회
			DmStorBVo dftDmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			String[] docGrpIds = docGrpId.split(",");
			// 복사할 원본 목록맵
			Map<String,DmDocLVo> originVoListMap = new HashMap<String,DmDocLVo>();
			for(String grpId : docGrpIds){
				tmpOriginVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, grpId, true);
				if(tmpOriginVo == null) continue;
				tmpOriginVo.setStatCd(null); // 상태코드를 초기화 한다. - 
				docGrpIdList.add(tmpOriginVo.getDocGrpId());
				originVoListMap.put(tmpOriginVo.getDocGrpId(), tmpOriginVo);
				// 인계이력 삭제
				dmDocSvc.delTakovrVo(queryQueue, null, userVo.getCompId(), grpId);
			}
			if(docGrpIdList.size()>0 && originVoListMap.size()>0 && docGrpIdList.size() == originVoListMap.size()){
				dmDocSvc.copyDoc(request, queryQueue, langTypCd, dmStorBVo, userVo, storId, tableName, "copy", fldId, copyFileList, originVoListMap, docGrpIdList, dftDmStorBVo.getStorId());
			}
			
			// 디스크에서 삭제할 파일 목록 VO
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			// 문서인수 완료후 해당 문서를 삭제한다.
			if(docGrpIdList.size()>0){
				for(String delGrpId : docGrpIdList){
					dmDocSvc.saveTransferDoc(request, queryQueue, dmStorBVo.getTblNm(), null, delGrpId, null, "delete", null, null, deletedFileList);
				}
			}
						
			commonSvc.execute(queryQueue);
			
			// 파일 복사
			if(copyFileList.size()>0){
				dmFileSvc.copyFileList(request, "doc", copyFileList);
			}
			// 파일 삭제
			if(deletedFileList.size()>0){
				dmFileSvc.deleteDiskFiles(deletedFileList);
			}
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 회사목록 화면 출력 */
	@RequestMapping(value = "/dm/adm/doc/findCompPop")
	public String findCompPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/adm/doc/findCompPop");
	}
	
	/** [FRAME] 회사목록 화면 출력 */
	@RequestMapping(value = "/dm/adm/doc/findCompFrm")
	public String findCompFrm(HttpServletRequest request,
			@Parameter(name="compNm", required=false) String compNm,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 회사ID		
		String compId = userVo.getCompId();
		// 전체회사목록 조회
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(compNm, "Y", langTypCd);
		// 저장소가 있는 회사목록
		List<PtCompBVo> compList = new ArrayList<PtCompBVo>(); 
		List<DmStorBVo> storList = null; // 저장소 목록
		for(PtCompBVo storedPtCompBVo : ptCompBVoList){
			// 사용자가 속한 회사 제외
			if(compId.equals(storedPtCompBVo.getCompId())) continue;
			// 해당 회사의 저장소 목록을 조회한다
			storList = dmStorSvc.getFilteredStorList(langTypCd, storedPtCompBVo.getCompId(), null);
			if(storList.size()==0) continue;
			compList.add(storedPtCompBVo);
		}
		model.put("ptCompBVoList", compList);
		
		return LayoutUtil.getJspPath("/dm/adm/doc/findCompFrm");
	}
}
