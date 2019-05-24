package com.innobiz.orange.web.ap.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApStorSvc;
import com.innobiz.orange.web.ap.utils.ApCmUtil;
import com.innobiz.orange.web.ap.utils.ApParamUtil;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApStorCompRVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;

/** 결재함 컨트롤러 - 결재 */
@Controller
public class ApBxCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApBxCtrl.class);
	
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
	
	/** 저장소 서비스 */
	@Autowired
	private ApStorSvc apStorSvc;
	
	/** 함별 목록 조회 */
	@RequestMapping(value = { "/ap/box/listApvBx", "/ap/box/listApvBxFrm", "/ap/box/listApvBxPltFrm" })
	public String listApvBx(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="recLstDeptId", required=false) String recLstDeptId,
			@Parameter(name="colYn", required=false) String colYn,
			@Parameter(name="hghtPx", required=false) String hghtPx,
			@Parameter(name="pageRowCnt", required=false) String pageRowCnt,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 
		
		// RSA 암호화 스크립트 추가 - 문서 비밀번호 확인용
		model.addAttribute("JS_OPTS", new String[]{"pt.rsa"});
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 저장소 조회
		if("apvdBx".equals(bxId) || "myBx".equals(bxId) || "postApvdBx".equals(bxId) || // 완료함, 기안함, 통보함 
				"regRecLst".equals(bxId) || "recvRecLst".equals(bxId) || "distRecLst".equals(bxId)){ // 등록, 접수, 배부 대장
			List<ApStorCompRVo> apStorCompRVoList = apStorSvc.getStorageListByComp(userVo.getCompId(), langTypCd);
			if(apStorCompRVoList != null) model.put("apStorCompRVoList", apStorCompRVoList);
		}
		
		// 모델에 옵션 설정
		Map<String, String> configMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		String uri = request.getRequestURI();
		// 기안함 - 개인 문서 분류 사용 할 경우
		if("myBx".equals(bxId) && uri.startsWith("/ap/box/listApvBx.do") && "Y".equals(configMap.get("psnCatEnab"))){
			return listApvRecBx(request, bxId, storId, model);
		}
		// 접수함 - 접수 범위 확장
		if("recvBx".equals(bxId) && uri.startsWith("/ap/box/listApvBx.do") && "Y".equals(configMap.get("exRecvRange"))){
			return listApvRecBx(request, bxId, storId, model);
		}
		
		// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
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
		if(model.get("forRefDocPop") != null){
			apOngdBVo.setFormId(null);
		}
		
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
			} else if("formNm".equals(schCat)){
				apOngdBVo.setFormNm(ApCmUtil.toSrchFormNm(schWord));
			}
		} else {
			apOngdBVo.setSchCat(null);
			apOngdBVo.setSchWord(null);
			if(apOngdBVo.getFormNm()!=null && !apOngdBVo.getFormNm().isEmpty()){
				apOngdBVo.setFormNm(ApCmUtil.toSrchFormNm(apOngdBVo.getFormNm()));
			}
			
		}
		// 파라미터로 세팅된 날짜 - 데이터 정비
		apBxSvc.setDurDt(apOngdBVo);
		// 저장소ID
		if(storId!=null && !storId.isEmpty()){
			apOngdBVo.setStorage(storId);
		}
		
		// 접수대장에서 - 관련부서 세팅
		if("recvRecLst".equals(bxId) && recLstDeptId!=null && !recLstDeptId.isEmpty()){
			apOngdBVo.setRecLstDeptId(recLstDeptId);
		}
		
		// 리스트 옵션 조회 - 리스트에 보일 항목 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = apCmSvc.setListQueryOptions(apOngdBVo, bxId);
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
		// 함별 조회 조건 세팅
		boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
		
		if(valid){
			// 참조문서 추가할 때 - 기안함 선택시 : 승인된 것만 조회함
			if("myBx".equals(bxId) && model.get("apvdOnly") == Boolean.TRUE){
				apOngdBVo.setDocProsStatCdList(null);
				apOngdBVo.setDocProsStatCd("apvd");
			}
			
			// 카운트 조회
			Integer recodeCount = commonSvc.count(apOngdBVo);
			
			// 참조문서 팝업용 - 문서작성 - 첨부 - 참조문서 의 경우 - 종이문서 제외함
			if("Y".equals(model.get("forRefDocPop"))){
				// 한 페이지 레코드수 - 10개 고정
				PersonalUtil.setFixedPaging(request, apOngdBVo, 10, recodeCount);
				apOngdBVo.setWhereSqllet("AND DOC_TYP_CD IN ('intro', 'extro')");
			// 포틀릿용
			} else if(isPlt || "Y".equals(model.get("forPltFrm"))){
				int rowCnt = 0;
				if(pageRowCnt!=null && !pageRowCnt.isEmpty()){
					try{
						rowCnt = Integer.parseInt(pageRowCnt);
						model.addAttribute("pageRowCnt", pageRowCnt);
					} catch(Exception ignore){}
				}
				
				if(rowCnt==0){
					// 한 페이지 레코드수 - 높이에 의한 계산
					int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
					int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
					rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
					model.addAttribute("pageRowCnt", Integer.valueOf(rowCnt));
				}
				
				PersonalUtil.setFixedPaging(request, apOngdBVo, rowCnt, recodeCount);
			} else {
				PersonalUtil.setPaging(request, apOngdBVo, recodeCount);
			}
			model.put("recodeCount", recodeCount);
			
			// 함별 문서 갯수 - 목록 조회시 캐쉬를 목록카운트로
			if("Y".equals(configMap.get("docCntInBx")) && (storId==null || storId.isEmpty())){
				
				// 상세조회를 펼침
				String srchDetl = request.getParameter("srchDetl");
				// 상세조회를 펼치지 않음 + 검색어가 없거나 비어 있으면
				//  ==> 검색 조건이 있어서 검색 결과가 달라질때 - 함의 문서갯수에 반영 안함
				if(srchDetl==null && (schWord==null || schWord.isEmpty())){
					// 개인분류
					String psnClsInfoId = request.getParameter("psnClsInfoId");
					// 참조열람상태코드
					String refVwStatCd = request.getParameter("refVwStatCd");
					
					if(ArrayUtil.isInArray(new String[]{"waitBx","ongoBx","apvdBx","rejtBx","postApvdBx","drftBx","deptBx"}, bxId)
							|| ("myBx".equals(bxId) && (psnClsInfoId==null || psnClsInfoId.isEmpty()))
							|| ("refVwBx".equals(bxId) && (refVwStatCd==null || refVwStatCd.equals(model.get("refVwStatCdDefault"))) )
							|| ("recvBx".equals(bxId) && "Y".equals(configMap.get("docCntInRecvBx")) && !"Y".equals(configMap.get("exRecvRange")))){
						model.put("adjustCachedBxCount", (isFrame?"parent.":"")+"setApCachedBxCount('"+bxId+"',"+recodeCount+");");
					}
				}
			}
			
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
					
					boolean isWaitBx = "waitBx".equals(bxId);
					
					// 긴급 여부(붉은색), 볼드 여부(안읽음 표시)
					boolean isRed, isBold;
					
					// 맵 목록으로 전환
					Map<String, Object> apOngdBVoMap;
					List<Map<String, Object>> apOngdBVoMapList = new ArrayList<Map<String, Object>>();
					for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
						
						apOngdBVoMap = VoUtil.toMap(storedApOngdBVo, null);
						
						isRed  = skipRed ? false :  "Y".equals(storedApOngdBVo.getUgntDocYn());
						isBold = skipBold ? false : (storedApOngdBVo.getVwDt()==null || storedApOngdBVo.getVwDt().isEmpty());
						
						if(isBold && isRed){
							apOngdBVoMap.put("fontStyle", "font-weight:bold; color:red;");
						} else if(isBold){
							apOngdBVoMap.put("fontStyle", "font-weight:bold;");
						} else if(isRed){
							apOngdBVoMap.put("fontStyle", "color:red;");
						}
						
						if(isWaitBx){
							if(		!userVo.getUserUid().equals(storedApOngdBVo.getCurApvrId()) &&
									!ArrayUtil.isIn2Array(userVo.getAdurs(), 1, storedApOngdBVo.getCurApvrId())){
								
								String apvrRoleCd = storedApOngdBVo.getApvrRoleCd();
								// paralPubVw:동시공람 psnParalAgr:개인병렬합의
								if(!"paralPubVw".equals(apvrRoleCd) && !"psnParalAgr".equals(apvrRoleCd)){
									apOngdBVoMap.put("isAgnt", Boolean.TRUE);
								}
							}
						}
						
						apOngdBVoMapList.add(apOngdBVoMap);
					}
					model.put("apOngdBVoMapList", apOngdBVoMapList);
				}
			}
		} else {
			LOGGER.error("Not valid bxId -  bxId:"+bxId+"  queryString:"+request.getQueryString());
		}
		

		// 코드 조회 : 조회 조건용
		if(!isFrame && !isPlt){
			// 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			List<PtCdBVo> docTypCdList = ptCmSvc.getCdList("DOC_TYP_CD", langTypCd, "Y");
			model.put("docTypCdList", docTypCdList);
			// 시행범위코드 - dom:대내, for:대외, both:대내외
			List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
			model.put("enfcScopCdList", enfcScopCdList);
			// 문서상태코드
			List<PtCdBVo> docStatCdList = ptCmSvc.getCdList("DOC_STAT_CD", langTypCd, "Y");
			model.put("docStatCdList", docStatCdList);
			// 시행상태코드
			List<PtCdBVo> enfcStatCdList = ptCmSvc.getCdList("ENFC_STAT_CD", langTypCd, "Y");
			model.put("enfcStatCdList", enfcStatCdList);
			
			// 참조열람 함
			if("refVwBx".equals(bxId)){
				List<PtCdBVo> refVwStatCdList = ptCmSvc.getCdList("REF_VW_STAT_CD", langTypCd, "Y");
				model.put("refVwStatCdList", refVwStatCdList);
			}
			
		}
		
		HttpSession session = request.getSession();
		String message = (String)session.getAttribute("message");
		if(message != null){
			session.removeAttribute("message");
			model.put("message", message);
		}
		
		// 메뉴ID 제거한 queryString 세팅
		ApParamUtil.setQueryString(request);
		
		if(isFrame){
			return LayoutUtil.getJspPath("/ap/box/listApvBx", "Frm");
		} else if(isPlt){
			return LayoutUtil.getJspPath("/ap/plt/listApvBxPltFrm");
		} else {
			return LayoutUtil.getJspPath("/ap/box/listApvBx");
		}
		
	}

	/** 대장 목록 - 함별목록 을 프레임으로 가지고 있는 대장용 페이지 */
	@RequestMapping(value = "/ap/box/listApvRecBx")
	public String listApvRecBx(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="storId", required=false) String storId,
			ModelMap model) throws Exception {
		
		// RSA 암호화 스크립트 추가 - 문서 비밀번호 확인용
		model.addAttribute("JS_OPTS", new String[]{"pt.rsa"});
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		// 함ID 세팅
		model.put("bxId", bxId);
		
		// 저장소 조회
		if("apvdBx".equals(bxId) || "myBx".equals(bxId) || "postApvdBx".equals(bxId) || // 완료함, 기안함, 통보함 
				"regRecLst".equals(bxId) || "recvRecLst".equals(bxId) || "distRecLst".equals(bxId)){ // 등록, 접수, 배부 대장
			List<ApStorCompRVo> apStorCompRVoList = apStorSvc.getStorageListByComp(userVo.getCompId(), langTypCd);
			if(apStorCompRVoList != null) model.put("apStorCompRVoList", apStorCompRVoList);
		}
		
		// 코드 조회 : 조회 조건용
		
		// 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
		List<PtCdBVo> docTypCdList = ptCmSvc.getCdList("DOC_TYP_CD", langTypCd, "Y");
		model.put("docTypCdList", docTypCdList);
		// 시행범위코드 - dom:대내, for:대외, both:대내외
		List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
		model.put("enfcScopCdList", enfcScopCdList);
		// 문서상태코드
		List<PtCdBVo> docStatCdList = ptCmSvc.getCdList("DOC_STAT_CD", langTypCd, "Y");
		model.put("docStatCdList", docStatCdList);
		// 시행상태코드
		List<PtCdBVo> enfcStatCdList = ptCmSvc.getCdList("ENFC_STAT_CD", langTypCd, "Y");
		model.put("enfcStatCdList", enfcStatCdList);
		
		// 메뉴ID 제거한 queryString 세팅
		ApParamUtil.setQueryString(request);
		
		// 옵션 조회(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ap/box/listApvRecBx");
	}
	
	/** [AJX] 함 목록 URL 조회 */
	@RequestMapping(value = {"/ap/box/getBxUrlAjx", "/ap/adm/box/getBxUrlAjx"})
	public String getBxUrlAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="menuId", required=false) String menuId,
			Locale locale,
			ModelMap model) throws Exception{
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("url", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		}
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String bxId = (String)jsonObject.get("bxId");
		String keepLoc = (String)jsonObject.get("keepLoc");
		
		String paramMnuId = "Y".equals(keepLoc) && menuId!=null && menuId.startsWith("Y") ? menuId : null;
		
		String uri = request.getRequestURI();
		boolean isAdminPage = uri.indexOf("/adm/")>0;
		
		String strMnuParam = (String)request.getAttribute("strMnuParam");
		
		if(isAdminPage){
			String url = apBxSvc.getAdmBxUrlByBxId(userVo, bxId, strMnuParam);
			model.put("url", url);
		} else {
			String url = apBxSvc.getBxUrlByBxId(userVo, bxId, paramMnuId);
			model.put("url", url);
		}
		
		return LayoutUtil.returnJson(model);
	}
}
