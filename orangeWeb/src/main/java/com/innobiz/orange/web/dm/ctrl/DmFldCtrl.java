package com.innobiz.orange.web.dm.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
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
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmCatDispDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

@Controller
public class DmFldCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmFldCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;
	
	/** 문서 서비스 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 폴더관리 */
	@RequestMapping(value = "/dm/fld/setFld")
	public String setFld(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/fld/setFld");
	}
	
	/** [팝업] 폴더 목록 조회 */
	@RequestMapping(value = {"/dm/doc/findFldPop","/dm/fld/findFldPop","/dm/adm/doc/findFldPop"})
	public String listClsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/fld/findFldPop");
	}
	
	/** 폴더 트리 조회 */
	@RequestMapping(value = {"/dm/doc/treeFldFrm","/dm/fld/treeFldFrm","/dm/adm/doc/treeFldFrm"})
	public String treeFldFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Fld");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		boolean isSrchChk = true;
		// 관리자일 경우에는 조회조건에 등록자UID가 있어야만 조회 가능함.
		if(isAdmin){
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
			if(regrUid != null && !regrUid.isEmpty()) {
				// 폴더조회
				if(isSrchChk) model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, regrUid, null));
			}
		}else{
			// 폴더조회
			model.put("dmFldBVoList", dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null));
		}
		
		return LayoutUtil.getJspPath("/dm/fld/treeFldFrm");
	}
	
	/** 폴더 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@RequestMapping(value = "/dm/fld/listFldFrm")
	public String listFldFrm(HttpServletRequest request,
			@Parameter(name="fldPid", required=false) String fldPid,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		if(fldPid==null || fldPid.isEmpty()){
			List<DmFldBVo> dmFldBVoList = new ArrayList<DmFldBVo>();
			// 화면 구성용 2개의 빈 vo 넣음
			dmFldBVoList.add(new DmFldBVo(null));
			dmFldBVoList.add(new DmFldBVo(null));
			model.put("dmFldBVoList", dmFldBVoList);
		}else{
			//fldPid = "ROOT".equals(fldPid) ? DmConstant.FLD_PSN : fldPid;
			DmFldBVo dmFldBVo = new DmFldBVo(null);
			dmFldBVo.setQueryLang(langTypCd);
			dmFldBVo.setFldPid(fldPid);
			dmFldBVo.setRegrUid(userVo.getUserUid());
			dmFldBVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<DmFldBVo> dmFldBVoList = (List<DmFldBVo>)commonSvc.queryList(dmFldBVo);
			DmCatBVo storDmCatBVo;
			for (DmFldBVo storedDmFldBVo : dmFldBVoList) {
				if (storedDmFldBVo.getRescId() != null) {
					// 리소스기본(DM_RESC_B) 테이블 - 조회
					dmRescSvc.queryRescBVo(null, storedDmFldBVo.getRescId(), model);
				}
				storDmCatBVo = dmDocSvc.getDmFldCatBVo(langTypCd, storedDmFldBVo.getStorId(), storedDmFldBVo.getCatId(), null);
				if(storDmCatBVo != null){
					storedDmFldBVo.setCatNm(storDmCatBVo.getCatNm());
				}
				
			}
			// 화면 구성용 2개의 빈 vo 넣음
			dmFldBVoList.add(new DmFldBVo(null));
			dmFldBVoList.add(new DmFldBVo(null));
			model.put("dmFldBVoList", dmFldBVoList);
		}
				
		return LayoutUtil.getJspPath("/dm/fld/listFldFrm");
	}
	
	
	/** [히든프레임] 폴더목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/fld/transFldList")
	public String transFldList(HttpServletRequest request,			
			@Parameter(name="fldPid", required=false)String fldPid,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 폴더기본(DM_FLD_B) 테이블 VO
			DmFldBVo dmFldBVo, newDmFldBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int count, i, size;
			String fldId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			DmDocLVo dmDocLVo = null;
			
			for(i=0;i<delCds.length;i++){
				
				fldId = delCds[i];
				// 해당 폴더에 문서가 속해 있는지 조회
				dmDocLVo = dmDocSvc.newDmDocLVo();
				dmDocLVo.setFldId(fldId);
				count = commonSvc.count(dmDocLVo);
				if(count>0){
					// dm.msg.not.del.withFld=문서가 있는 폴더는 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.withFld", request);
					LOGGER.error("fail to del fld - doc count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				//하위 폴더가 있는지 조회
				dmFldBVo = new DmFldBVo(null);
				dmFldBVo.setFldPid(fldId);
				count = commonSvc.count(dmFldBVo);
				if(count>0){
					// dm.msg.not.del.childFld=하위폴더가 있어서 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.childFld", request);
					LOGGER.error("fail to del fld - child fld count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				// 폴더기본(DM_FLD_B) 테이블 - 삭제
				dmFldBVo = new DmFldBVo(null);
				dmFldBVo.setFldId(fldId);
				queryQueue.delete(dmFldBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 폴더기본(DM_FLD_B) 테이블
			List<DmFldBVo> dmFldBVoList = (List<DmFldBVo>)VoUtil.bindList(request, DmFldBVo.class, new String[]{"valid"});
			size = dmFldBVoList==null ? 0 : dmFldBVoList.size();
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				dmRescSvc.collectDmRescBVoList(request, queryQueue, dmFldBVoList, "valid", "rescId", "fldNm", null);
			}
			
			for(i=0;i<size;i++){
				newDmFldBVo = new DmFldBVo(null);
				dmFldBVo = dmFldBVoList.get(i);
				// VO 속성 복사
				BeanUtils.copyProperties(dmFldBVo, newDmFldBVo, new String[]{"prefix"});
				//newDmFldBVo.setStorId(storId);
				newDmFldBVo.setFldPid(fldPid);
				
				newDmFldBVo.setRegrUid(userVo.getUserUid());
				newDmFldBVo.setRegDt("sysdate");
				// 수정자, 수정일시
				newDmFldBVo.setModrUid(userVo.getUserUid());
				newDmFldBVo.setModDt("sysdate");
				
				if(newDmFldBVo.getFldId() == null || newDmFldBVo.getFldId().isEmpty()){
					newDmFldBVo.setFldId(dmCmSvc.createId("DM_COMM_FLD_B"));
				}
				queryQueue.store(newDmFldBVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+fldPid+"');");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 폴더삭제 */
	@RequestMapping(value = "/dm/adm/doc/transDelDocFldAjx")
	public String transDelDocFldAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String regrUid = (String) object.get("regrUid");
			String fldId = (String) object.get("fldId");
			if ( fldId == null || fldId.isEmpty() || regrUid == null || regrUid.isEmpty()) {
				LOGGER.error("[ERROR] fldId == null || fldId.isEmpty() || regrUid == null || regrUid.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 삭제할 폴더정보 조회
			DmFldBVo dmFldBVo = dmDocSvc.getDmFldBVo(null, fldId, langTypCd);
			if(dmFldBVo == null){
				LOGGER.error("[ERROR] dmFldBVo == null");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(!dmFldBVo.getRegrUid().equals(regrUid)){
				LOGGER.error("[ERROR] !dmFldBVo.getRegrUid().equals(regrUid)");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			// 개인문서 삭제
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// DmDocLVo 초기화
			DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo();
			dmDocLVo.setRegrUid(regrUid);
			dmDocLVo.setFldId(fldId);
			@SuppressWarnings("unchecked")
			List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>)commonSvc.queryList(dmDocLVo);
			// 삭제할 문서ID 목록
			List<String> docIdList = new ArrayList<String>();
			for(DmDocLVo storedDmDocLVo : dmDocLVoList){
				docIdList.add(storedDmDocLVo.getDocId());
			}
			// 삭제할 파일 목록			
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			String[] docIds = ArrayUtil.toArray(docIdList);
			for(String id : docIds){
				dmDocLVo = dmDocSvc.newDmDocLVo();
				dmDocLVo.setDocId(id);
				dmDocLVo.setRegrUid(regrUid);
				dmDocSvc.delPsnDoc(request, queryQueue, dmDocLVo, id, userVo.getUserUid(), deletedFileList);
			}
			
			// 폴더 삭제
			DmFldBVo newDmFldBVo = new DmFldBVo(null);
			newDmFldBVo.setFldId(fldId);
			newDmFldBVo.setRegrUid(regrUid);
			queryQueue.delete(newDmFldBVo);
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			if(deletedFileList.size()>0) dmFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
						
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 유형 목록 조회 */
	@RequestMapping(value = {"/dm/fld/findCatPop","/dm/adm/fld/findCatPop"})
	public String findCatPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 조회조건 매핑
		DmCatBVo dmCatBVo = new DmCatBVo();
		VoUtil.bind(request, dmCatBVo);
		dmCatBVo.setQueryLang(langTypCd);
		dmCatBVo.setStorId(storId);
		
		@SuppressWarnings("unchecked")
		List<DmCatBVo> dmCatBVoList = (List<DmCatBVo>)commonSvc.queryList(dmCatBVo);
		model.put("dmCatBVoList", dmCatBVoList);
		
		return LayoutUtil.getJspPath("/dm/fld/findCatPop");
	}
	
	/** [FRAME] 분류체계,폴더 목록 조회(오른쪽 프레임) */
	@RequestMapping(value = {"/dm/fld/findCatFrm","/dm/adm/fld/findCatFrm"})
	public String findCatFrm(HttpServletRequest request,
			@Parameter(name="catId", required=false) String catId,
			ModelMap model) throws Exception {
		
		if (catId == null || catId.isEmpty()) {
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
		
		// 항목표시여부 리스트
		List<DmCatDispDVo> itemDispList = dmAdmSvc.getDmCatDispDVoList(request, storId, catId, true, "Y", "list", null);
		model.put("itemDispList", itemDispList);
				
		return LayoutUtil.getJspPath("/dm/fld/findCatFrm");
	}
	
	/** [AJAX] 폴더이동 */
	@RequestMapping(value = {"/dm/fld/transFldMoveAjx","/dm/adm/fld/transFldMoveAjx"})
	public String transFldMoveAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String fldPid = (String) object.get("fldPid");
			String fldId = (String) object.get("fldId");
			if ( fldId == null || fldId.isEmpty() || fldPid == null || fldPid.isEmpty()) {
				LOGGER.error("[ERROR] fldId == null || fldId.isEmpty() || fldPid == null || fldPid.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 저장소ID
			String storId = null;
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 이동할 폴더정보 조회
			DmFldBVo dmFldBVo = dmDocSvc.getDmFldBVo(storId, fldId, langTypCd);
			if(dmFldBVo == null){
				LOGGER.error("[ERROR] dmFldBVo == null");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 부모폴더와 이동할 폴더가 같을떄
			if(fldPid.equals(dmFldBVo.getFldId())){
				LOGGER.error("[ERROR] fldPid.equals(dmFldBVo.getFldId())");
				// dm.msg.not.save.duplFldParent=동일한 상위폴더로 저장할수 없습니다.
				throw new CmException("dm.msg.not.save.duplFldParent", request);
			}
			// 부모폴더가 같을때
			if(fldPid.equals(dmFldBVo.getFldPid())){
				LOGGER.error("[ERROR] fldPid.equals(dmFldBVo.getFldId())");
				// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.
				throw new CmException("dm.msg.not.save.duplFld", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 폴더 이동[저장]
			DmFldBVo newDmFldBVo = new DmFldBVo(storId),storedDmFldBVo;
			newDmFldBVo.setFldPid(fldPid);
			newDmFldBVo.setOrderBy("SORT_ORDR");
			// 정렬순서
			Integer sortOrdr = 1;
			// 부모폴더ID를 가지고 있는 폴더 목록 조회
			@SuppressWarnings("unchecked")
			List<DmFldBVo> fldList = (List<DmFldBVo>)commonSvc.queryList(newDmFldBVo);
			int i, size = fldList==null ? 0 : fldList.size();
			// 순서를 정렬한다.
			for(i=0;i<size;i++){
				storedDmFldBVo = fldList.get(i);
				newDmFldBVo = new DmFldBVo(storId);
				newDmFldBVo.setFldId(storedDmFldBVo.getFldId());
				newDmFldBVo.setSortOrdr(sortOrdr++);
				queryQueue.update(newDmFldBVo);
			}
			
			// 부모 폴더 정보 조회
			DmFldBVo parentVo = dmDocSvc.getDmFldBVo(storId, fldPid, langTypCd);
			// 폴더그룹ID
			String fldGrpId = parentVo == null ? fldPid : dmFldBVo.getFldGrpId();
			newDmFldBVo = new DmFldBVo(storId);			
			newDmFldBVo.setFldPid(fldPid);
			newDmFldBVo.setFldGrpId(fldGrpId);
			newDmFldBVo.setFldId(fldId);
			// 이동할 폴더의 순서 정의
			newDmFldBVo.setSortOrdr(size+1);
			queryQueue.update(newDmFldBVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
						
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
