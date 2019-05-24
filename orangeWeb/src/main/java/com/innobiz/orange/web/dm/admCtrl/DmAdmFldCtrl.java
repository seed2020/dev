package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.List;

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
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;

@Controller
public class DmAdmFldCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmFldCtrl.class);
	
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
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 문서 서비스 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 폴더관리 */
	@RequestMapping(value = {"/dm/fld/setDocFld","/dm/adm/fld/setDocFld"})
	public String setFld(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자 여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
		if(!isAdmin) model.put("fldGrpId", "DEPT");
		model.put("isAdmin", isAdmin);
		
		return LayoutUtil.getJspPath("/dm/adm/fld/setFld");
	}
	
	/** 폴더 트리 조회 */
	@RequestMapping(value = {"/dm/fld/treeDocFldFrm","/dm/doc/treeDocFldFrm","/dm/adm/doc/treeDocFldFrm","/dm/fld/treeDocFldFrm","/dm/adm/fld/treeDocFldFrm","/dm/adm/fld/treeFldFrm","/cm/doc/treeDocFldFrm"})
	public String treeFldFrm(HttpServletRequest request,
			@Parameter(name="psnYn", required=false) String psnYn,
			@Parameter(name="popYn", required=false) String popYn,
			@Parameter(name="fldGrpId", required=false) String fldGrpId,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Fld");
				
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 팝업에서 회사ID가 넘어올경우 해당회사의 폴더 정보를 로드함
		/*if(popYn == null && "N".equals(popYn) && request.getRequestURI().startsWith("/dm/adm/") && compId != null && !compId.isEmpty()){
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
			model.put("ptCompBVoList", ptCompBVoList);
		}*/

		if(compId == null || compId.isEmpty()) compId = userVo.getCompId();
		String orgId = userVo.getOrgId();
		// 전체 조회 여부
		boolean allChk = request.getRequestURI().startsWith("/dm/adm/") && (fldGrpId == null || fldGrpId.isEmpty());
		if(!allChk && fldGrpId != null && !fldGrpId.isEmpty() ){
			if(DmConstant.FLD_DEPT.equals(fldGrpId)) compId = null;
			if(DmConstant.FLD_COMP.equals(fldGrpId)) orgId = null;
		}
		// 미분류 조회 여부
		boolean noneChk = (request.getRequestURI().startsWith("/dm/adm/doc/") || request.getRequestURI().startsWith("/dm/doc/")) 
				&& ((popYn == null || "N".equals(popYn)) && (fldGrpId == null || fldGrpId.isEmpty()));
		List<DmFldBVo> dmFldBVoList = dmAdmSvc.getDmFldBVoList(storId, langTypCd, compId, orgId, allChk, noneChk);
		if(psnYn != null && !psnYn.isEmpty() && "Y".equals(psnYn) && !allChk){
			dmDocSvc.setPsnFldList(dmFldBVoList, storId, langTypCd, userVo.getUserUid());
		}
		model.put("dmFldBVoList", dmFldBVoList);
		
		return LayoutUtil.getJspPath("/dm/adm/fld/treeFldFrm");
	}
	
	/** 폴더 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@RequestMapping(value = {"/dm/fld/listDocFldFrm","/dm/adm/fld/listDocFldFrm"})
	public String listFldFrm(HttpServletRequest request,
			@Parameter(name="fldPid", required=false) String fldPid,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 유형목록 조회
		DmCatBVo dmCatBVo = new DmCatBVo();
		dmCatBVo.setQueryLang(langTypCd);
		dmCatBVo.setStorId(storId);
		
		@SuppressWarnings("unchecked")
		List<DmCatBVo> dmCatBVoList = (List<DmCatBVo>)commonSvc.queryList(dmCatBVo);
		if(dmCatBVoList.size() == 0){
			// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
						
			// dm.msg.nodata.cat=문서유형이 없습니다.\n문서유형을 등록해주십시요.
			String message = messageProperties.getMessage("dm.msg.nodata.cat", request);
			LOGGER.error(message);
			model.put("message", message);
			model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/adm/cat/listCat.do"));
			return LayoutUtil.getResultJsp();
		}
		model.put("dmCatBVoList", dmCatBVoList);
		
		if(fldPid==null || fldPid.isEmpty()){
			List<DmFldBVo> dmFldBVoList = new ArrayList<DmFldBVo>();
			// 화면 구성용 2개의 빈 vo 넣음
			dmFldBVoList.add(new DmFldBVo(storId));
			dmFldBVoList.add(new DmFldBVo(storId));
			model.put("dmFldBVoList", dmFldBVoList);
		}else{
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setQueryLang(langTypCd);
			dmFldBVo.setStorId(storId);
			dmFldBVo.setFldPid(fldPid);
			dmFldBVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<DmFldBVo> dmFldBVoList = (List<DmFldBVo>)commonSvc.queryList(dmFldBVo);
			// 화면 구성용 2개의 빈 vo 넣음
			dmFldBVoList.add(new DmFldBVo(storId));
			dmFldBVoList.add(new DmFldBVo(storId));
			for (DmFldBVo storedDmFldBVo : dmFldBVoList) {
				if (storedDmFldBVo.getRescId() != null) {
					// 리소스기본(DM_RESC_B) 테이블 - 조회
					dmRescSvc.queryRescBVo(storId, storedDmFldBVo.getRescId(), model);
				}
			}
			model.put("dmFldBVoList", dmFldBVoList);
		}
				
		return LayoutUtil.getJspPath("/dm/adm/fld/listFldFrm");
	}
	
	
	/** [히든프레임] 폴더목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/dm/fld/transDocFldList","/dm/adm/fld/transDocFldList"})
	public String transFldList(HttpServletRequest request,			
			@Parameter(name="fldPid", required=false)String fldPid,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			boolean isRoot = "ROOT".equals(fldPid) || "comp".equals(fldPid) || "dept".equals(fldPid);
			
			// 최상위 조직일 경우
			if(isRoot){
				// dm.msg.not.addFld.root=최상위에는 폴더를 추가 할 수 없습니다.
				String msg = messageProperties.getMessage("dm.msg.not.addFld.root", request);
				LOGGER.error("fail to save root fld - "+"\n"+msg);
				throw new CmException(msg);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 폴더기본(DM_FLD_B) 테이블 VO
			DmFldBVo dmFldBVo;
			
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
				dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				dmDocLVo.setFldId(fldId);
				count = commonSvc.count(dmDocLVo);
				if(count>0){
					// dm.msg.not.del.withFld=문서가 있는 폴더는 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.withFld", request);
					LOGGER.error("fail to del fld - doc count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				//하위 폴더가 있는지 조회
				dmFldBVo = new DmFldBVo(storId);
				dmFldBVo.setStorId(storId);
				dmFldBVo.setFldPid(fldId);
				count = commonSvc.count(dmFldBVo);
				if(count>0){
					// dm.msg.not.del.childFld=하위폴더가 있어서 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.childFld", request);
					LOGGER.error("fail to del fld - child fld count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				// 폴더기본(DM_FLD_B) 테이블 - 삭제
				dmFldBVo = new DmFldBVo(storId);
				dmFldBVo.setStorId(storId);
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
				dmRescSvc.collectDmRescBVoList(request, queryQueue, dmFldBVoList, "valid", "rescId", "fldNm", storId);
			}
			
			// 폴더그룹ID 조회
			dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setStorId(storId);
			dmFldBVo.setFldId(fldPid);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			String fldGrpId = dmFldBVo == null ? fldPid : dmFldBVo.getFldGrpId();
			
			for(i=0;i<size;i++){
				dmFldBVo = dmFldBVoList.get(i);
				dmFldBVo.setStorId(storId);
				dmFldBVo.setFldPid(fldPid);
				
				if(dmFldBVo.getFldId() == null || dmFldBVo.getFldId().isEmpty()){
					// 등록자, 등록일시
					dmFldBVo.setRegrUid(userVo.getUserUid());
					dmFldBVo.setRegDt("sysdate");
					dmFldBVo.setFldId(dmCmSvc.createId("DM_FLD_B"));
					dmFldBVo.setFldGrpId(fldGrpId);
				}else{
					// 수정자, 수정일시
					dmFldBVo.setModrUid(userVo.getUserUid());
					dmFldBVo.setModDt("sysdate");
				}
				
				queryQueue.store(dmFldBVo);
			}
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.FLD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.FLD);
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
	
	/** [AJAX] 폴더이동 */
	@RequestMapping(value = {"/dm/fld/transDocFldMoveAjx","/dm/adm/fld/transDocFldMoveAjx"})
	public String transDocFldMoveAjx(HttpServletRequest request,			
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
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
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
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.FLD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.FLD);
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
