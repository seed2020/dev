package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.vo.DmCdDVo;
import com.innobiz.orange.web.dm.vo.DmCdGrpBVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmItemBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/* 코드관리 (관리자) */
@Controller
public class DmCdCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmCdCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** 코드관리 목록조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/listCd")
	public String listCd(HttpServletRequest request,
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
				
		// 코드그룹(DM_CD_GRP_B) 테이블 - BIND
		DmCdGrpBVo dmCdGrpBVo = new DmCdGrpBVo();
		dmCdGrpBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, dmCdGrpBVo);
		dmCdGrpBVo.setStorId(storId);
				
		// 코드그룹(DM_CD_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(dmCdGrpBVo);
		PersonalUtil.setPaging(request, dmCdGrpBVo, recodeCount);

		// 코드그룹(DM_CD_GRP_B) 테이블 - SELECT
		List<DmCdGrpBVo> dmCdGrpBVoList = (List<DmCdGrpBVo>) commonSvc.queryList(dmCdGrpBVo);

		for (DmCdGrpBVo storedDmCdGrpBVo : dmCdGrpBVoList) {
			// 코드(DM_CD_D) 테이블 - SELECT
			DmCdDVo dmCdDVo = new DmCdDVo();
			dmCdDVo.setQueryLang(langTypCd);
			dmCdDVo.setStorId(storedDmCdGrpBVo.getStorId());
			dmCdDVo.setCdGrpId(storedDmCdGrpBVo.getCdGrpId());
			List<DmCdDVo> cdVoList = (List<DmCdDVo>) commonSvc.queryList(dmCdDVo);
			storedDmCdGrpBVo.setDmCdDVoList(cdVoList);
		}

		model.put("recodeCount", recodeCount);
		model.put("dmCdGrpBVoList", dmCdGrpBVoList);

		return LayoutUtil.getJspPath("/dm/adm/cat/listCd");
	}

	/** [팝업] 코드관리 수정용 조회 및 등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/setCdPop")
	public String setCdPop(HttpServletRequest request,
			@RequestParam(value = "cdGrpId", required = false) String cdGrpId,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 코드그룹(DM_CD_GRP_B) 테이블 - BIND
		DmCdGrpBVo dmCdGrpBVo = new DmCdGrpBVo();
		dmCdGrpBVo.setCdGrpId(cdGrpId);
		dmCdGrpBVo.setQueryLang(langTypCd);
		dmCdGrpBVo.setStorId(storId);

		// 수정인 경우
		if (cdGrpId != null && !cdGrpId.isEmpty()) {
			// 코드그룹(DM_CD_GRP_B) 테이블 - SELECT
			dmCdGrpBVo = (DmCdGrpBVo) commonSvc.queryVo(dmCdGrpBVo);
			if (dmCdGrpBVo.getRescId() != null) {
				// 리소스기본(DM_RESC_B) 테이블 - 조회, 모델에 추가
				dmRescSvc.queryRescBVo(storId, dmCdGrpBVo.getRescId(), model);
			}

			// 코드(DM_CD_D) 테이블 - SELECT
			DmCdDVo dmCdDVo = new DmCdDVo();
			dmCdDVo.setQueryLang(langTypCd);
			dmCdDVo.setCdGrpId(dmCdGrpBVo.getCdGrpId());
			dmCdDVo.setStorId(dmCdGrpBVo.getStorId());
			List<DmCdDVo> cdVoList = (List<DmCdDVo>) commonSvc.queryList(dmCdDVo);
			for (DmCdDVo cdVo : cdVoList) {
				if (cdVo.getRescId() != null) {
					// 리소스기본(DM_RESC_B) 테이블 - 조회
					dmRescSvc.queryRescBVo(storId, cdVo.getRescId(), model);
				}
			}

			// 화면 구성용 빈 vo 넣음
			cdVoList.add(new DmCdDVo());
			dmCdGrpBVo.setDmCdDVoList(cdVoList);
		} else {
			List<DmCdDVo> cdVoList = new ArrayList<DmCdDVo>();
			// 화면 구성용 빈 vo 넣음
			cdVoList.add(new DmCdDVo());
			dmCdGrpBVo.setDmCdDVoList(cdVoList);
		}
		model.put("dmCdGrpBVo", dmCdGrpBVo);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/dm/adm/cat/setCdPop");
	}

	/** 코드관리 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/transCd")
	public String transCd(HttpServletRequest request,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			DmRescBVo dmRescBVo = dmRescSvc.collectDmRescBVo(request, "grp", queryQueue, storId);
			if (dmRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 코드관리(DM_CD_GRP_B) 테이블 - BIND
			DmCdGrpBVo dmCdGrpBVo = new DmCdGrpBVo();
			VoUtil.bind(request, dmCdGrpBVo);
			dmCdGrpBVo.setStorId(storId);
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			dmCdGrpBVo.setRescId(dmRescBVo.getRescId());
			dmCdGrpBVo.setCdGrpNm(dmRescBVo.getRescVa());

			// 수정자, 수정일시
			dmCdGrpBVo.setModrUid(userVo.getUserUid());
			dmCdGrpBVo.setModDt("sysdate");
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			//if(!isSysAdmin){
				//dmCdGrpBVo.setCompId(userVo.getCompId());
			//}
						
			if (dmCdGrpBVo.getCdGrpId() == null || dmCdGrpBVo.getCdGrpId().isEmpty()) {
				// 코드ID 생성
				dmCdGrpBVo.setCdGrpId(dmCmSvc.createId("DM_CD_GRP_B"));
				// 코드관리(DM_CD_GRP_B) 테이블 - INSERT
				queryQueue.insert(dmCdGrpBVo);

				// 코드(DM_CD_D) 테이블 - BIND
				List<DmCdDVo> boundDmCdDVoList = (List<DmCdDVo>) VoUtil.bindList(request, DmCdDVo.class, new String[]{"valid"});

				// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT, 코드(DM_CD_D) 테이블 - BIND
				dmRescSvc.collectDmRescBVoList(request, queryQueue, boundDmCdDVoList, "valid", "rescId", "cdVa", storId);

				// 코드(DM_CD_D) 테이블 - INSERT
				for (DmCdDVo cdVo : boundDmCdDVoList) {
					cdVo.setStorId(storId);
					// 코드ID, 코드그룹ID
					cdVo.setCdId(dmCmSvc.createId("DM_CD_D"));
					cdVo.setCdGrpId(dmCdGrpBVo.getCdGrpId());
					// 수정자, 수정일시
					cdVo.setModrUid(dmCdGrpBVo.getModrUid());
					cdVo.setModDt("sysdate");

					queryQueue.insert(cdVo);
				}

				commonSvc.execute(queryQueue);

			} else {
				// 코드관리(DM_CD_GRP_B) 테이블 - UPDATE
				queryQueue.update(dmCdGrpBVo);

				// 코드(DM_CD_D) 테이블 - BIND
				List<DmCdDVo> boundDmCdDVoList = (List<DmCdDVo>) VoUtil.bindList(request, DmCdDVo.class, new String[]{"valid"});

				// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT, 코드(DM_CD_D) 테이블 - BIND
				dmRescSvc.collectDmRescBVoList(request, queryQueue, boundDmCdDVoList, "valid", "rescId", "cdVa", storId);

				// 코드(DM_CD_D) 테이블
				for (DmCdDVo cdVo : boundDmCdDVoList) {
					cdVo.setStorId(storId);
					// 코드그룹ID
					cdVo.setCdGrpId(dmCdGrpBVo.getCdGrpId());
					// 수정자, 수정일시
					cdVo.setModrUid(dmCdGrpBVo.getModrUid());
					cdVo.setModDt("sysdate");

					String deleted = cdVo.getDeleted();
					if (cdVo.getCdId() == null || cdVo.getCdId().isEmpty()) {
						// 코드ID
						cdVo.setCdId(dmCmSvc.createId("DM_CD_D"));
						// 코드(DM_CD_D) 테이블 - INSERT
						queryQueue.insert(cdVo);
					} else if ("Y".equals(deleted)) {
						// 리소스기본(DM_RESC_B) 테이블 - DELETE
						DmRescBVo rescVo = new DmRescBVo(storId);
						rescVo.setStorId(storId);
						rescVo.setRescId(cdVo.getRescId());
						queryQueue.delete(rescVo);
						// 코드(DM_CD_D) 테이블 - DELETE
						queryQueue.delete(cdVo);
					} else {
						// 코드(DM_CD_D) 테이블 - UPDATE
						queryQueue.update(cdVo);
					}
				}
				commonSvc.execute(queryQueue);
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

	/** [AJAX] 코드관리 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/transCdDelAjx")
	public String transCdDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String cdGrpId = (String) object.get("cdGrpId");
			if (cdGrpId == null || cdGrpId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
						
			// 테이블컬럼(DM_ITEM_B) 테이블 - SELECT
			DmItemBVo dmItemBVo = new DmItemBVo();
			dmItemBVo.setStorId(storId);
			dmItemBVo.setWhereSqllet(" AND T.ITEM_TYP = 'CODE' AND T.ITEM_TYP_VA = '"+cdGrpId+"'" );
			
			if (commonSvc.count(dmItemBVo) > 0) {
				// bb.msg.codeExists=해당 코드그룹을 사용하는 테이블이 존재합니다.
				throw new CmException("bb.msg.codeExists", request);
			}

			// 코드그룹(DM_CD_GRP_B) 테이블 - SELECT
			QueryQueue queryQueue = new QueryQueue();
			DmCdGrpBVo dmCdGrpBVo = new DmCdGrpBVo();
			dmCdGrpBVo.setStorId(storId);
			dmCdGrpBVo.setCdGrpId(cdGrpId);
			DmCdGrpBVo storedDmCdGrpBVo = (DmCdGrpBVo) commonSvc.queryVo(dmCdGrpBVo);

			// 리소스기본(DM_RESC_B) 테이블 - DELETE
			if (storedDmCdGrpBVo.getRescId() != null && !storedDmCdGrpBVo.getRescId().isEmpty()) {
				DmRescBVo dmRescBVo = new DmRescBVo(storId);
				dmRescBVo.setStorId(storId);
				dmRescBVo.setRescId(storedDmCdGrpBVo.getRescId());
				queryQueue.delete(dmRescBVo);
			}

			// 코드그룹(DM_CD_GRP_B) 테이블 - DELETE
			queryQueue.delete(dmCdGrpBVo);

			// 코드(DM_CD_D) 테이블 - SELECT
			DmCdDVo dmCdDVo = new DmCdDVo();
			dmCdDVo.setStorId(storId);
			dmCdDVo.setCdGrpId(cdGrpId);
			List<DmCdDVo> cdVoList = (List<DmCdDVo>) commonSvc.queryList(dmCdDVo);

			// 리소스기본(DM_RESC_B) 테이블 - DELETE
			for (DmCdDVo cdVo : cdVoList) {
				if (cdVo.getRescId() != null && !cdVo.getRescId().isEmpty()) {
					DmRescBVo dmRescBVo = new DmRescBVo(storId);
					dmRescBVo.setStorId(storId);
					dmRescBVo.setRescId(cdVo.getRescId());
					queryQueue.delete(dmRescBVo);
				}
			}

			// 코드(DM_CD_D) 테이블 - DELETE
			queryQueue.delete(dmCdDVo);

			commonSvc.execute(queryQueue);

			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	
	/** [AJAX] 코드관리 삭제시 사용여부 체크 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/chkUseCdAjx")
	public String chkUseCdAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String cdGrpId = (String) object.get("cdGrpId");
			String cdId = (String) object.get("cdId");
			String msgCode = (String) object.get("msgCode");
			if (cdGrpId == null || cdGrpId.isEmpty() || cdId == null || cdId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
						
			// 테이블컬럼(DM_ITEM_B) 테이블 - SELECT
			DmItemBVo dmItemBVo = new DmItemBVo();
			dmItemBVo.setStorId(storId);
			dmItemBVo.setWhereSqllet(" AND T.ITEM_TYP = 'CODE' AND T.ITEM_TYP_VA = '"+cdGrpId+"'" );
			
			List<DmItemBVo> dmItemBVoList = (ArrayList<DmItemBVo>)commonSvc.queryList(dmItemBVo);
			if(dmItemBVoList.size() == 0) return JsonUtil.returnJson(model);
			
			//해당 코드그룹을 사용하는 테이블 목록이 있을경우
			//컬럼 목록 
			int count;
			for(DmItemBVo storedDmItemBVo : dmItemBVoList){
				DmDocLVo paramVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				paramVo.setWhereSqllet(" AND "+storedDmItemBVo.getItemNm().toUpperCase()+" = '"+cdId+"'");
				count = 0;
				try{
					count = commonSvc.count(paramVo);
				} catch(Exception ignore){
					ignore.printStackTrace();
				}
				if(count>0){
					//pt.msg.not.del.cd.inUse=사용중인 코드는 삭제 할 수 없습니다.
					if(msgCode == null) msgCode = "pt.msg.not.del.cd.inUse";
					String msg = messageProperties.getMessage(msgCode, request);
					LOGGER.error(msg);
					throw new CmException(msg);
				}
			}
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
