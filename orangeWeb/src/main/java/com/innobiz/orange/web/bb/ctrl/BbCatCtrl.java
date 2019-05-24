package com.innobiz.orange.web.bb.ctrl;

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

import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.svc.BbRescSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaCatDVo;
import com.innobiz.orange.web.bb.vo.BaCatGrpBVo;
import com.innobiz.orange.web.bb.vo.BaRescBVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/* 카테고리관리 */
@Controller
public class BbCatCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbCatCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "bbRescSvc")
	private BbRescSvc bbRescSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 카테고리관리 목록조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/listBullCatMng")
	public String listBullCatMng(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - BIND
		BaCatGrpBVo baCatGrpBVo = new BaCatGrpBVo();
		baCatGrpBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baCatGrpBVo);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baCatGrpBVo.setCompId(userVo.getCompId());
		//}
				
		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCatGrpBVo);
		PersonalUtil.setPaging(request, baCatGrpBVo, recodeCount);

		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - SELECT
		List<BaCatGrpBVo> baCatGrpBVoList = (List<BaCatGrpBVo>) commonSvc.queryList(baCatGrpBVo);

		for (BaCatGrpBVo grpVo : baCatGrpBVoList) {
			// 카테고리(BA_CAT_D) 테이블 - SELECT
			BaCatDVo baCatDVo = new BaCatDVo();
			baCatDVo.setQueryLang(langTypCd);
			baCatDVo.setCatGrpId(grpVo.getCatGrpId());
			List<BaCatDVo> catVoList = (List<BaCatDVo>) commonSvc.queryList(baCatDVo);
			grpVo.setCatVoList(catVoList);
		}


		model.put("recodeCount", recodeCount);
		model.put("baCatGrpBVoList", baCatGrpBVoList);

		return LayoutUtil.getJspPath("/bb/adm/listBullCatMng");
	}

	/** [팝업] 카테고리관리 수정용 조회 및 등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setBullCatMngPop")
	public String setBullCatMngPop(HttpServletRequest request,
			@RequestParam(value = "catGrpId", required = false) String catGrpId,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - BIND
		BaCatGrpBVo baCatGrpBVo = new BaCatGrpBVo();
		baCatGrpBVo.setCatGrpId(catGrpId);
		baCatGrpBVo.setQueryLang(langTypCd);

		// 수정인 경우
		if (catGrpId != null && !catGrpId.isEmpty()) {
			// 카테고리그룹(BA_CAT_GRP_B) 테이블 - SELECT
			baCatGrpBVo = (BaCatGrpBVo) commonSvc.queryVo(baCatGrpBVo);
			if (baCatGrpBVo.getRescId() != null) {
				// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
				bbRescSvc.queryRescBVo(baCatGrpBVo.getRescId(), model);
			}

			// 카테고리(BA_CAT_D) 테이블 - SELECT
			BaCatDVo baCatDVo = new BaCatDVo();
			baCatDVo.setQueryLang(langTypCd);
			baCatDVo.setCatGrpId(baCatGrpBVo.getCatGrpId());
			List<BaCatDVo> catVoList = (List<BaCatDVo>) commonSvc.queryList(baCatDVo);
			for (BaCatDVo catVo : catVoList) {
				if (catVo.getRescId() != null) {
					// 리소스기본(BA_RESC_B) 테이블 - 조회
					bbRescSvc.queryRescBVo(catVo.getRescId(), model);
				}
			}

			// 카테고리 사용 게시판 수, 게시판(BA_BRD_B) - COUNT
			BaBrdBVo bbBrdBVo = new BaBrdBVo();
			bbBrdBVo.setCatGrpId(catGrpId);
			Integer count = commonSvc.count(bbBrdBVo);
			baCatGrpBVo.setBrdCnt(count);

			// 화면 구성용 빈 vo 넣음
			catVoList.add(new BaCatDVo());

			baCatGrpBVo.setCatVoList(catVoList);
		} else {
			List<BaCatDVo> catVoList = new ArrayList<BaCatDVo>();

			// 화면 구성용 빈 vo 넣음
			catVoList.add(new BaCatDVo());

			baCatGrpBVo.setCatVoList(catVoList);
		}

		model.put("baCatGrpBVo", baCatGrpBVo);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/bb/adm/setBullCatMngPop");
	}

	/** 카테고리관리 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transCat")
	public String transCat(HttpServletRequest request,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			BaRescBVo baRescBVo = bbRescSvc.collectBaRescBVo(request, "grp", queryQueue);
			if (baRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 카테고리관리(BA_CAT_GRP_B) 테이블 - BIND
			BaCatGrpBVo baCatGrpBVo = new BaCatGrpBVo();
			VoUtil.bind(request, baCatGrpBVo);

			// 리소스 조회 후 리소스ID와 리소스명 세팅
			baCatGrpBVo.setRescId(baRescBVo.getRescId());
			baCatGrpBVo.setCatGrpNm(baRescBVo.getRescVa());

			// 수정자, 수정일시
			baCatGrpBVo.setModrUid(userVo.getUserUid());
			baCatGrpBVo.setModDt("sysdate");
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			//if(!isSysAdmin){
				baCatGrpBVo.setCompId(userVo.getCompId());
			//}
						
			if (baCatGrpBVo.getCatGrpId() == null || baCatGrpBVo.getCatGrpId().isEmpty()) {
				// 카테고리ID 생성
				baCatGrpBVo.setCatGrpId(bbCmSvc.createId("BA_CAT_GRP_B"));
				// 카테고리관리(BA_CAT_GRP_B) 테이블 - INSERT
				queryQueue.insert(baCatGrpBVo);

				// 카테고리(BA_CAT_D) 테이블 - BIND
				List<BaCatDVo> boundCatVoList = (List<BaCatDVo>) VoUtil.bindList(request, BaCatDVo.class, new String[]{"valid"});

				// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 카테고리(BA_CAT_D) 테이블 - BIND
				bbRescSvc.collectBaRescBVoList(request, queryQueue, boundCatVoList, "valid", "rescId", "catNm");

				// 카테고리(BA_CAT_D) 테이블 - INSERT
				for (BaCatDVo catVo : boundCatVoList) {
					// 카테고리ID, 카테고리그룹ID
					catVo.setCatId(bbCmSvc.createId("BA_CAT_D"));
					catVo.setCatGrpId(baCatGrpBVo.getCatGrpId());
					// 수정자, 수정일시
					catVo.setModrUid(baCatGrpBVo.getModrUid());
					catVo.setModDt("sysdate");

					queryQueue.insert(catVo);
				}

				commonSvc.execute(queryQueue);

			} else {
				// 카테고리관리(BA_CAT_GRP_B) 테이블 - UPDATE
				queryQueue.update(baCatGrpBVo);

				// 카테고리(BA_CAT_D) 테이블 - BIND
				List<BaCatDVo> boundCatVoList = (List<BaCatDVo>) VoUtil.bindList(request, BaCatDVo.class, new String[]{"valid"});

				// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 카테고리(BA_CAT_D) 테이블 - BIND
				bbRescSvc.collectBaRescBVoList(request, queryQueue, boundCatVoList, "valid", "rescId", "catNm");

				// 카테고리(BA_CAT_D) 테이블
				for (BaCatDVo catVo : boundCatVoList) {
					// 카테고리그룹ID
					catVo.setCatGrpId(baCatGrpBVo.getCatGrpId());
					// 수정자, 수정일시
					catVo.setModrUid(baCatGrpBVo.getModrUid());
					catVo.setModDt("sysdate");

					String deleted = catVo.getDeleted();
					if (catVo.getCatId() == null || catVo.getCatId().isEmpty()) {
						// 카테고리ID
						catVo.setCatId(bbCmSvc.createId("BA_CAT_D"));
						// 카테고리(BA_CAT_D) 테이블 - INSERT
						queryQueue.insert(catVo);
					} else if ("Y".equals(deleted)) {
						// 리소스기본(BA_RESC_B) 테이블 - DELETE
						BaRescBVo rescVo = new BaRescBVo();
						rescVo.setRescId(catVo.getRescId());
						queryQueue.delete(rescVo);
						// 카테고리(BA_CAT_D) 테이블 - DELETE
						queryQueue.delete(catVo);
					} else {
						// 카테고리(BA_CAT_D) 테이블 - UPDATE
						queryQueue.update(catVo);
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

	/** [AJAX] 카테고리관리 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transCatDelAjx")
	public String transCatDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String catGrpId = (String) object.get("catGrpId");
			if (catGrpId == null || catGrpId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setCatGrpId(catGrpId);
			if (commonSvc.count(baBrdBVo) > 0) {
				// bb.msg.bbExists=해당 테이블을 사용하는 게시판이 존재합니다.
				throw new CmException("bb.msg.bbExists", request);
			}

			// 카테고리그룹(BA_CAT_GRP_B) 테이블 - SELECT
			QueryQueue queryQueue = new QueryQueue();
			BaCatGrpBVo baCatGrpBVo = new BaCatGrpBVo();
			baCatGrpBVo.setCatGrpId(catGrpId);
			BaCatGrpBVo storedBaCatGrpBVo = (BaCatGrpBVo) commonSvc.queryVo(baCatGrpBVo);

			// 리소스기본(BA_RESC_B) 테이블 - DELETE
			if (storedBaCatGrpBVo.getRescId() != null && !storedBaCatGrpBVo.getRescId().isEmpty()) {
				BaRescBVo baRescBVo = new BaRescBVo();
				baRescBVo.setRescId(storedBaCatGrpBVo.getRescId());
				queryQueue.delete(baRescBVo);
			}

			// 카테고리그룹(BA_CAT_GRP_B) 테이블 - DELETE
			queryQueue.delete(baCatGrpBVo);

			// 카테고리(BA_CAT_D) 테이블 - SELECT
			BaCatDVo baCatDVo = new BaCatDVo();
			baCatDVo.setCatGrpId(catGrpId);
			List<BaCatDVo> catVoList = (List<BaCatDVo>) commonSvc.queryList(baCatDVo);

			// 리소스기본(BA_RESC_B) 테이블 - DELETE
			for (BaCatDVo catVo : catVoList) {
				if (catVo.getRescId() != null && !catVo.getRescId().isEmpty()) {
					BaRescBVo baRescBVo = new BaRescBVo();
					baRescBVo.setRescId(catVo.getRescId());
					queryQueue.delete(baRescBVo);
				}
			}

			// 카테고리(BA_CAT_D) 테이블 - DELETE
			queryQueue.delete(baCatDVo);

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

	/** [팝업] 카테고리그룹 검색 화면 (관리자) */
	@RequestMapping(value = "/bb/adm/selectBullCatPop")
	public String selectBullCatPop(HttpServletRequest request,
			ModelMap model) throws Exception {

		return LayoutUtil.getJspPath("/bb/adm/selectBullCatPop");
	}

	/** [프레임] 카테고리그룹 검색 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/selectBullCatFrm")
	public String selectBullCatFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - BIND
		BaCatGrpBVo baCatGrpBVo = new BaCatGrpBVo();
		VoUtil.bind(request, baCatGrpBVo);
		
		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCatGrpBVo);
		PersonalUtil.setPaging(request, baCatGrpBVo, recodeCount);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baCatGrpBVo.setCompId(userVo.getCompId());
		//}
		
		// 카테고리그룹(BA_CAT_GRP_B) 테이블 - SELECT
		List<BaCatGrpBVo> baCatGrpBVoList = (List<BaCatGrpBVo>) commonSvc.queryList(baCatGrpBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("baCatGrpBVoList", baCatGrpBVoList);
		
		return LayoutUtil.getJspPath("/bb/adm/selectBullCatFrm");
	}
}
