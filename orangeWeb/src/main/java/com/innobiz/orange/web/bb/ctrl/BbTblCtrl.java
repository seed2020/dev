package com.innobiz.orange.web.bb.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbRescSvc;
import com.innobiz.orange.web.bb.svc.BbTblSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaCdGrpBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaRescBVo;
import com.innobiz.orange.web.bb.vo.BaTblBVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/* 테이블 관리 */
@Controller
public class BbTblCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbTblCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "bbRescSvc")
	private BbRescSvc bbRescSvc;

	/** 테이블관리 서비스 */
	@Resource(name = "bbTblSvc")
	private BbTblSvc bbTblSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;
	
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 테이블관리 목록조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/listTbl")
	public String listTbl(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baTblBVo);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baTblBVo.setCompId(userVo.getCompId());
		//}
				
		Integer recodeCount = commonSvc.count(baTblBVo);
		PersonalUtil.setPaging(request, baTblBVo, recodeCount);

		List<BaTblBVo> list = (List<BaTblBVo>) commonSvc.queryList(baTblBVo);

		model.put("recodeCount", recodeCount);
		model.put("baTblBVoList", list);

		return LayoutUtil.getJspPath("/bb/adm/listTbl");
	}

	/** [팝업] 테이블관리 수정용 조회 및 등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setTblPop")
	public String setTblPop(HttpServletRequest request,
			@RequestParam(value = "tblId", required = false) String tblId,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setTblId(tblId);
		baTblBVo.setQueryLang(langTypCd);

		// 수정인 경우
		if (tblId != null && !tblId.isEmpty()) {
			// 테이블관리 기본(BA_TBL_B) 테이블 - SELECT
			baTblBVo = (BaTblBVo) commonSvc.queryVo(baTblBVo);
			if (baTblBVo.getRescId() != null) {
				// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
				bbRescSvc.queryRescBVo(baTblBVo.getRescId(), model);
			}
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
			baTblColmDVo.setQueryLang(langTypCd);
			baTblColmDVo.setTblId(baTblBVo.getTblId());
			baTblColmDVo.setExColmYn("Y");
			List<BaTblColmDVo> colmVoList = (List<BaTblColmDVo>) commonSvc.queryList(baTblColmDVo);
			for (BaTblColmDVo colmVo : colmVoList) {
				if (colmVo.getRescId() != null) {
					// 리소스기본(BA_RESC_B) 테이블 - 조회
					bbRescSvc.queryRescBVo(colmVo.getRescId(), model);
				}
			}
			
			// 테이블 사용 게시물수, 게시물(BB_X000X_L) - COUNT
			BbBullLVo bbBullLVo = new BbBullLVo(bbTblSvc.getFullTblNm(baTblBVo.getTblNm()), null);
			Integer count = commonSvc.count(bbBullLVo);
			baTblBVo.setBullCnt(count);
			
			// 화면 구성용 빈 vo 넣음
			colmVoList.add(new BaTblColmDVo());
			
			baTblBVo.setColmVoList(colmVoList);
		} else {
			// 테이블명 생성
			baTblBVo.setTblNm(bbTblSvc.createTblNm());
			
			List<BaTblColmDVo> colmVoList = new ArrayList<BaTblColmDVo>();
			
			// 화면 구성용 빈 vo 넣음
			colmVoList.add(new BaTblColmDVo());
			
			baTblBVo.setColmVoList(colmVoList);
		}

		model.put("baTblBVo", baTblBVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 코드그룹(BA_CD_GRP_B) 테이블 - BIND
		BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
		baCdGrpBVo.setQueryLang(langTypCd);
		baCdGrpBVo.setCompId(userVo.getCompId());
		baCdGrpBVo.setGrpUseYn("Y");//사용중
		//List<BaCdGrpBVo> cdList = ptCmSvc.getCdList("GW_BB", langTypCd, "Y");
		// 코드그룹(BA_CD_GRP_B) 테이블 - SELECT
		List<BaCdGrpBVo> cdList = (List<BaCdGrpBVo>) commonSvc.queryList(baCdGrpBVo);
		model.put("cdList", cdList);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/bb/adm/setTblPop");
	}

	/** 테이블관리 저장 (관리자) */
	@RequestMapping(value = "/bb/adm/transTbl")
	public String transTbl(HttpServletRequest request,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			String tblId = ParamUtil.getRequestParam(request, "tblId", false);
			
			// 회사별 어권에 대한 테이블 컬럼 리소스가 등록되어 있는지 확인
			if (tblId != null && !tblId.isEmpty()) {
				bbTblSvc.setColmResc(request, userVo, queryQueue, tblId);
			}
			
			BaRescBVo baRescBVo = bbRescSvc.collectBaRescBVo(request, "tbl", queryQueue);
			if (baRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 테이블관리(BA_TBL_B) 테이블 - BIND
			BaTblBVo baTblBVo = new BaTblBVo();
			VoUtil.bind(request, baTblBVo);

			// 리소스 조회 후 리소스ID와 리소스명 세팅
			baTblBVo.setRescId(baRescBVo.getRescId());
			baTblBVo.setTblDispNm(baRescBVo.getRescVa());

			// 수정자, 수정일시
			baTblBVo.setModrUid(userVo.getUserUid());
			baTblBVo.setModDt("sysdate");
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			//if(!isSysAdmin){
				baTblBVo.setCompId(userVo.getCompId());
			//}
			
			if (baTblBVo.getTblId() == null || baTblBVo.getTblId().isEmpty()) {
				//테이블명 중복체크
				BaTblBVo schBaTblBVo = new BaTblBVo();
				schBaTblBVo.setWhereSqllet(" AND T.TBL_NM = '"+baTblBVo.getTblNm()+"'");
				Integer recodeCount = commonSvc.count(schBaTblBVo);
				if(recodeCount != null && recodeCount.intValue() > 0){
					// 테이블명 생성
					baTblBVo.setTblNm(bbTblSvc.createTblNm());
				}
				// 테이블 등록
				bbTblSvc.insertTbl(request, baTblBVo, queryQueue);
			} else {
				// 테이블 수정
				List<BaTblColmDVo> addColmList = bbTblSvc.updateTbl(request, baTblBVo, queryQueue);
				
				if(addColmList!=null && addColmList.size()>0){ // 추가
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					
					// 게시판관리(BA_BRD_B) 테이블 - BIND
					BaBrdBVo baBrdBVo = new BaBrdBVo();
					baBrdBVo.setQueryLang(langTypCd);
					baBrdBVo.setCompId(userVo.getCompId()); // 회사ID
					baBrdBVo.setTblId(tblId);
					
					if(commonSvc.count(baBrdBVo)>0){ // 해당 테이블을 사용하는 게시판 조회
						// 게시판관리(BA_BRD_B) 테이블 - SELECT
						@SuppressWarnings("unchecked")
						List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
						BaColmDispDVo dispVo = null;
						for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
							for (BaTblColmDVo colmVo : addColmList) {
								// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - INSERT
								dispVo = new BaColmDispDVo();
								dispVo.setBrdId(storedBaBrdBVo.getBrdId());
								dispVo.setUseYn("Y");
								
								// 속성 복사
								String[] ignores = new String[] {"regrUid", "regDt", "modrUid", "modDt"};
								BeanUtils.copyProperties(colmVo, dispVo, ignores);
								 
								dispVo.setListDispOrdr(99);
								dispVo.setReadDispOrdr(99);
								
								// 수정자, 수정일시
								dispVo.setModrUid(userVo.getUserUid());
								dispVo.setModDt("sysdate");
								
								// 컬럼표시 목록표시여부 세팅
								bbBrdSvc.setListDispYn(storedBaBrdBVo, colmVo, dispVo);
								
								queryQueue.insert(dispVo);
							}
						}
						
					}
				}
				if(!queryQueue.isEmpty())
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

	/** [AJAX] 테이블관리 삭제 (관리자) */
	@RequestMapping(value = "/bb/adm/transTblDelAjx")
	public String transTblDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String tblId = (String) object.get("tblId");
			if (tblId == null || tblId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setTblId(tblId);
			if (commonSvc.count(baBrdBVo) > 0) {
				// bb.msg.bbExists=해당 테이블을 사용하는 게시판이 존재합니다.
				throw new CmException("bb.msg.bbExists", request);
			}

			// 테이블 삭제
			QueryQueue queryQueue = new QueryQueue();
			bbTblSvc.deleteTbl(request, tblId, queryQueue);

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

	/** [팝업] 테이블 상세조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/listTblBbPop")
	public String listTblBbPop(HttpServletRequest request,
			@RequestParam(value = "tblId", required = false) String tblId,
			ModelMap model) throws Exception {
		
		if (tblId == null || tblId.isEmpty()) {
			// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
			throw new CmException("cm.msg.notValidPage", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 테이블관리(BA_TBL_B) 테이블 - SELECT
		BaTblBVo baTblBVo = bbTblSvc.getBaTblBVo(langTypCd, tblId);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		baBrdBVo.setCompId(null);   // 회사ID
		baBrdBVo.setTblId(tblId);
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		model.put("baTblBVo",  baTblBVo);
		model.put("baBrdBVoList", baBrdBVoList);
		
		return LayoutUtil.getJspPath("/bb/adm/listTblBbPop");
	}
}
