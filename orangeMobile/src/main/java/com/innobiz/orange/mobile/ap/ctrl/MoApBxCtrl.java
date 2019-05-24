package com.innobiz.orange.mobile.ap.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApParamUtil;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;

/** 결재함 컨트롤러 - 결재 */
@Controller
public class MoApBxCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoApBxCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 함별 목록 조회 */
	@RequestMapping(value = "/ap/box/listApvBx")
	public String listApvBx(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="colYn", required=false) String colYn,
			@Parameter(name="hghtPx", required=false) String hghtPx,
			ModelMap model) throws Exception {
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 모델에 옵션 설정
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		String uri = request.getRequestURI();
		// 프레임 여부
		boolean isFrame = uri.indexOf("/listApvBxFrm") > 0;
		if(isFrame) model.put("isFrame", Boolean.TRUE);
		// 포틀릿 여부
		boolean isPlt = uri.indexOf("/listApvBxPltFrm") > 0;
		
		// 함ID 세팅
		model.put("bxId", bxId);
		
		// 진행문서기본(AP_ONGD_B) 테이블
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		VoUtil.bind(request, apOngdBVo);
		apOngdBVo.setBxId(bxId);
		if(schWord != null && !schWord.isEmpty()){
			if("srchDocNo".equals(schCat)){
				apOngdBVo.setSrchDocNo(schWord);
			} else if("srchRecvDocNo".equals(schCat)){
				apOngdBVo.setSrchRecvDocNo(schWord);
			} else if("docSubj".equals(schCat)){
				apOngdBVo.setDocSubj(schWord);
			} else if("makrNm".equals(schCat)){
				apOngdBVo.setMakrNm(schWord);
			} else if("bodyHtml".equals(schCat)){
				apOngdBVo.setBodyHtml(schWord);
			}
		} else {
			apOngdBVo.setSchCat(null);
			apOngdBVo.setSchWord(null);
		}
		// 파라미터로 세팅된 날짜 - 데이터 정비
		apBxSvc.setDurDt(apOngdBVo);
		
		// 리스트 옵션 조회 - 리스트에 보일 항목 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = apCmSvc.setListQueryOptions(apOngdBVo, bxId);
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
		// 함별 조회 조건 세팅
		boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
		
		if(valid){
			// 카운트 조회
			Integer recodeCount = commonSvc.count(apOngdBVo);
			
			// 참조문서 팝업용 - 문서작성 - 첨부 - 참조문서 의 경우 - 종이문서 제외함
			if("Y".equals(model.get("forRefDocPop"))){
				// 한 페이지 레코드수 - 10개 고정
				PersonalUtil.setFixedPaging(request, apOngdBVo, 10, recodeCount);
				apOngdBVo.setWhereSqllet("AND DOC_TYP_CD IN ('intro', 'extro')");
			// 포틀릿용
			} else if(isPlt || "Y".equals(model.get("forPltFrm"))){
				// 한 페이지 레코드수 - 높이에 의한 계산
				int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
				int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
				int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
				PersonalUtil.setFixedPaging(request, apOngdBVo, rowCnt, recodeCount);
			} else {
				PersonalUtil.setPaging(request, apOngdBVo, recodeCount);
			}
			model.put("recodeCount", recodeCount);
			
			// 레코드 조회
			if(recodeCount.intValue()>0){
				apOngdBVo.setQueryLang(langTypCd);
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
				if(apOngdBVoList!=null){
					
					// 볼드 표시 생략 - 개인함, 등록대장, 접수대장, 배부대장, 공람게시
					boolean skipBold = "drftBx".equals(bxId) || "regRecLst".equals(bxId) 
							|| "recvRecLst".equals(bxId) || "distRecLst".equals(bxId) || "pubBx".equals(bxId);
					
					// 긴급 붉은색 표시 생략 - 대기함, 진행함, 완료함, 기안함, 부서대기함 - 제외한 나머지 함
					boolean skipRed = !("waitBx".equals(bxId) || "ongoBx".equals(bxId) 
							|| "apvdBx".equals(bxId) || "myBx".equals(bxId) || "deptBx".equals(bxId));
					
					boolean skipAgnt = !"waitBx".equals(bxId);
					
					// 긴급 여부(붉은색), 볼드 여부(안읽음 표시)
					boolean isRed, isBold, isSecu, isAgnt;
					
					// 맵 목록으로 전환
					Map<String, Object> apOngdBVoMap;
					List<Map<String, Object>> apOngdBVoMapList = new ArrayList<Map<String, Object>>();
					for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
						
						apOngdBVoMap = VoUtil.toMap(storedApOngdBVo, null);
						
						isRed  = skipRed ? false :  "Y".equals(storedApOngdBVo.getUgntDocYn());
						apOngdBVoMap.put("ugntYn", isRed ? "Y" : "N");

						isSecu = storedApOngdBVo.getDocPwEnc()!=null && !storedApOngdBVo.getDocPwEnc().isEmpty();
						apOngdBVoMap.put("secuYn", isSecu ? "Y" : "N");
						
						isBold = skipBold ? false : (storedApOngdBVo.getVwDt()==null || storedApOngdBVo.getVwDt().isEmpty());
						apOngdBVoMap.put("notReadYn", isBold ? "Y" : "N");
						
						isAgnt = skipAgnt ? false : !userVo.getUserUid().equals(storedApOngdBVo.getApvrUid());
						apOngdBVoMap.put("agntYn", isAgnt ? "Y" : "N");
						
//						if(isBold && isRed){
//							apOngdBVoMap.put("fontStyle", "font-weight:bold; color:red;");
//						} else if(isBold){
//							apOngdBVoMap.put("fontStyle", "font-weight:bold;");
//						} else if(isRed){
//							apOngdBVoMap.put("fontStyle", "color:red;");
//						}
						apOngdBVoMapList.add(apOngdBVoMap);
					}
					model.put("apOngdBVoMapList", apOngdBVoMapList);
				}
			}
		} else {
			LOGGER.error("Not valid bxId -  bxId:"+bxId+"  queryString:"+request.getQueryString());
		}
		
		// 참조기안 함
		if("refVwBx".equals(bxId)){
			List<PtCdBVo> refVwStatCdList = ptCmSvc.getCdList("REF_VW_STAT_CD", langTypCd, "Y");
			model.put("refVwStatCdList", refVwStatCdList);
		}

//		// 코드 조회 : 조회 조건용
//		if(!isFrame && !isPlt){
//			// 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
//			List<PtCdBVo> docTypCdList = ptCmSvc.getCdList("DOC_TYP_CD", langTypCd, "Y");
//			model.put("docTypCdList", docTypCdList);
//			// 시행범위코드 - dom:대내, for:대외, both:대내외
//			List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
//			model.put("enfcScopCdList", enfcScopCdList);
//			// 문서상태코드
//			List<PtCdBVo> docStatCdList = ptCmSvc.getCdList("DOC_STAT_CD", langTypCd, "Y");
//			model.put("docStatCdList", docStatCdList);
//			// 시행상태코드
//			List<PtCdBVo> enfcStatCdList = ptCmSvc.getCdList("ENFC_STAT_CD", langTypCd, "Y");
//			model.put("enfcStatCdList", enfcStatCdList);
//		}
		
		// 메뉴ID 제거한 queryString 세팅
		ApParamUtil.setQueryString(request);
		
		if(isFrame){
			return MoLayoutUtil.getJspPath("/ap/box/listApvBx", "Frm");
		} else if(isPlt){
			return MoLayoutUtil.getJspPath("/ap/plt/listApvBxPltFrm");
		} else {
			return MoLayoutUtil.getJspPath("/ap/box/listApvBx");
		}
		
	}

	///** 함별 목록 조회 */
	//@RequestMapping(value = "/ap/box/viewApv")
	
}
