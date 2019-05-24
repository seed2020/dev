package com.innobiz.orange.web.ap.ctrl;

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
import org.springframework.web.servlet.ModelAndView;

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
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;

/** 관리자 결재 문서 목록 */
@Controller
public class ApAdmBxCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApAdmBxCtrl.class);
	
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
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 함별 목록 조회 */
	@RequestMapping(value = "/ap/adm/box/listApvBx")
	public String listApvBx(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="prgStat", required=false) String prgStat,
			ModelMap model) throws Exception {
		
		// RSA 암호화 스크립트 추가 - 문서 비밀번호 확인용
		model.addAttribute("JS_OPTS", new String[]{"pt.rsa"});
		
		String uri = request.getRequestURI();
		boolean isExcel = uri.indexOf("excelDownLoad")>0;
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 관리자 완료문서
		if("admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId)){
			// 회사관리자 또는 시스템 관리자면
			if(userVo.isAdmin() || userVo.isSysAdmin()){
				model.put("hasDelPw", Boolean.TRUE);
			}
		}
		
		// 저장소 조회 - 완료문서, 접수문서, 배부문서
		if("admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId) || "admRecvRecLst".equals(bxId) || "postApvdBx".equals(bxId)){
			List<ApStorCompRVo> apStorCompRVoList = apStorSvc.getStorageListByComp(userVo.getCompId(), langTypCd);
			if(apStorCompRVoList != null) model.put("apStorCompRVoList", apStorCompRVoList);
		}
		
		// 모델에 옵션 설정
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		model.put("adminPage", Boolean.TRUE);
		
		
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
		
		// 리스트 항목용 함ID
		String listOptBxId = bxId==null || bxId.equals("admOngoBx") || bxId.equals("admOngoFormBx") ? "ongoBx" :
			bxId.equals("admApvdBx") || bxId.equals("admRegRecLst") || bxId.equals("admApvdFormBx") ? "regRecLst" :
				bxId.equals("admRejtBx") ? "rejtBx" :
					bxId.equals("admRecvRecLst") ? "recvRecLst" :
						bxId.equals("admDistRecLst") ? "distRecLst" :
							bxId.equals("admOpinBx") ? "opinBx" :
								//bxId.equals("admAllFormBx") ? "ongoBx" :
								"ongoBx";
		
		// 리스트 옵션 조회 - 리스트에 보일 항목 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = apCmSvc.setListQueryOptions(apOngdBVo, listOptBxId);
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		// 관리자 - 양식별 진행문서, 양식별 완료문서
		if("admOngoFormBx".equals(bxId) || "admApvdFormBx".equals(bxId) || "admAllFormBx".equals(bxId)){
			apOngdBVo.setFormId(formId);
		} else if("admOpinBx".equals(bxId)){
			apOngdBVo.setDocProsStatCd(prgStat==null ? "apvd" : prgStat);
		}
		
		String makDeptId = apOngdBVo.getMakDeptId();
		if(makDeptId!=null && !makDeptId.isEmpty()){
			List<String> makDeptIdList = ArrayUtil.toList(makDeptId, ",", true);
			if(makDeptIdList.isEmpty()){
				apOngdBVo.setMakDeptId(null);
			} else if(makDeptIdList.size()==1){
				apOngdBVo.setMakDeptId(makDeptIdList.get(0));
			} else {
				apOngdBVo.setMakDeptId(null);
				apOngdBVo.setMakDeptIdList(makDeptIdList);
			}
		}
		
		if(bxId.equals("admOpinBx")){
			List<String> apvrUidList = apBxSvc.getOpinApvrList(userVo, bxId);
			apOngdBVo.setApvrUidList(apvrUidList);
			if(apvrUidList!=null && !apvrUidList.isEmpty()){
				StringBuilder builder = new StringBuilder(128);
				boolean first = true;
				for(String apvrUid : apvrUidList){
					if(first) first = false;
					else builder.append(',');
					builder.append(apvrUid);
				}
				model.put("apvrUids", builder.toString());
				model.put("apvrUidCnt", apvrUidList.size());
			}
		}
		
		// 관리자 여부 - 보안등급 체크 쿼리 안들어감
		boolean hasAdmin = true;
		// 함별 조회 조건 세팅
		boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
		
		if(valid){
			// 카운트 조회
			Integer recodeCount = commonSvc.count(apOngdBVo);
			model.put("recodeCount", recodeCount);
			
			if(!isExcel){
				PersonalUtil.setPaging(request, apOngdBVo, recodeCount);
			} else {
				if(recodeCount>1000){
					apOngdBVo.setPageNo(1);
					apOngdBVo.setPageRowCnt(1000);
				}
			}
			
			// 레코드 조회
			if(recodeCount.intValue()>0){
				apOngdBVo.setQueryLang(langTypCd);
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
				if(isExcel){
					model.put("apOngdBVoList", apOngdBVoList);
					return null;
				}
				
				if(apOngdBVoList!=null){
					
//					// 볼드 표시 생략 - 개인함, 등록대장, 접수대장, 배부대장, 공람게시
//					boolean skipBold = "drftBx".equals(bxId) || "regRecLst".equals(bxId) 
//							|| "recvRecLst".equals(bxId) || "distRecLst".equals(bxId) || "pubBx".equals(bxId);
//					
//					// 긴급 붉은색 표시 생략 - 대기함, 진행함, 완료함, 기안함, 부서대기함 - 제외한 나머지 함
//					boolean skipRed = !("waitBx".equals(bxId) || "ongoBx".equals(bxId) 
//							|| "apvdBx".equals(bxId) || "myBx".equals(bxId) || "deptBx".equals(bxId));
					
					// 긴급 여부(붉은색), 볼드 여부(안읽음 표시)
					boolean isRed = false, isBold = false;
					
					// 맵 목록으로 전환
					Map<String, Object> apOngdBVoMap;
					List<Map<String, Object>> apOngdBVoMapList = new ArrayList<Map<String, Object>>();
					for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
						
						apOngdBVoMap = VoUtil.toMap(storedApOngdBVo, null);
						
						//isRed  = skipRed ? false :  "Y".equals(storedApOngdBVo.getUgntDocYn());
						//isBold = skipBold ? false : (storedApOngdBVo.getVwDt()==null || storedApOngdBVo.getVwDt().isEmpty());
						
						if(isBold && isRed){
							apOngdBVoMap.put("fontStyle", "font-weight:bold; color:red;");
						} else if(isBold){
							apOngdBVoMap.put("fontStyle", "font-weight:bold;");
						} else if(isRed){
							apOngdBVoMap.put("fontStyle", "color:red;");
						}
						apOngdBVoMapList.add(apOngdBVoMap);
					}
					model.put("apOngdBVoMapList", apOngdBVoMapList);
				}
			}
		} else {
			LOGGER.error("Not valid bxId -  bxId:"+bxId+"  queryString:"+request.getQueryString());
		}
		if(isExcel){
			return null;
		}
		
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
		
		return LayoutUtil.getJspPath("/ap/adm/box/listApvBx");
	}
	
	/** 함별 목록 조회 */
	@RequestMapping(value = "/ap/adm/box/excelDownLoad")
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="prgStat", required=false) String prgStat,
			ModelMap model) throws Exception {
		
		listApvBx(request, bxId, storId, compId, schCat, schWord, formId, prgStat, model);
		
		// 리스트 옵션 조회
		@SuppressWarnings("unchecked")
		List<PtLstSetupDVo> ptLstSetupDVoList = (List<PtLstSetupDVo>)model.get("ptLstSetupDVoList");
		
		// 목록
		@SuppressWarnings("unchecked")
		List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)model.get("apOngdBVoList");

		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		PtMnuDVo ptMnuDVo = ptSecuSvc.getMenuByMenuId(userVo.getCompId(), menuId, userVo.getLangTypCd());
		String bxNm = ptMnuDVo==null ? bxId : ptMnuDVo.getRescNm();
		
		if(apOngdBVoList==null || apOngdBVoList.size()==0){
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
			return mv;
		}
		
		// 컬럼명
		String colName;
		List<String> colNames = new ArrayList<String>();
		
		// 데이터
		List<Object> colValue = null;
		Map<String,Object> colValues = new HashMap<String,Object>();
		List<String> attrIdList = new ArrayList<String>();
		
		// 엑셀 셀 넓이 계산
		int wdth;
		String wdthPerc;
		List<Integer> widthList = new ArrayList<Integer>();
		
		if(ptLstSetupDVoList!=null){
			for(PtLstSetupDVo ptLstSetupDVo : ptLstSetupDVoList){
				if("Y".equals(ptLstSetupDVo.getDispYn())){
					
					// 셀 넓이
					wdthPerc = ptLstSetupDVo.getWdthPerc();
					if(wdthPerc!=null && wdthPerc.endsWith("%")){
						wdth = Integer.parseInt(wdthPerc.substring(0, wdthPerc.length()-1));
					} else {
						wdth = 40;
					}
					widthList.add(wdth * 450);
					
					// 컬럼명
					colName = messageProperties.getMessage(ptLstSetupDVo.getMsgId(), request);
					colNames.add(colName);
					
					// attribute id
					attrIdList.add(ptLstSetupDVo.getAtrbId());
				}
			}
		}
		
		
		Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", userVo.getLangTypCd());
		
		
		String temp;
		Object value;
		ApOngdBVo apOngdBVo;
		int i, size = apOngdBVoList.size();
		for(i=0; i<size; i++){
			apOngdBVo = apOngdBVoList.get(i);
			
			colValue = new ArrayList<Object>();
			for(String attrId : attrIdList){
				
				if("regrNm".equals(attrId)){
					attrId = "makrNm";
				}
				
				if("docStatNm".equals(attrId)){
					
					attrId = apOngdBVo.getDocStatCd();
					temp = termMap.get(attrId);
					if(temp==null){
						temp = messageProperties.getMessage("ap.term."+attrId, request);
					}
					colValue.add(temp==null ? "" : temp);
					
				} else if(attrId.endsWith("Dd")){
					
					attrId = attrId.substring(0, attrId.length()-2)+"Dt";
					value = VoUtil.getValue(apOngdBVo, attrId);
					if(value!=null){
						temp = value.toString();
						if(temp.length()>10){
							temp = temp.substring(0, 10);
						}
						colValue.add(temp);
					} else {
						colValue.add("");
					}
					
				} else if("attFileYn".equals(attrId)){
					
					if("Y".equals(apOngdBVo.getAttFileYn())){
						colValue.add("Y");
					} else {
						colValue.add("");
					}
				} else {
					value = VoUtil.getValue(apOngdBVo, attrId);
					colValue.add(value==null ? "" : value);
				}
			}
			colValues.put("col"+i, colValue);
		}
		
		if(size >= 999){
			colValues.put("col1000", new ArrayList<Object>());
			
			colValue = new ArrayList<Object>();
			// ap.msg.notOver1000=1000건 이상은 표시되지 않습니다.
			colValue.add(messageProperties.getMessage("ap.msg.notOver1000", request));
			colValues.put("col1000", colValue);
		}
		
		
		ModelAndView mv = new ModelAndView("excelDownloadView");
		
		mv.addObject("sheetName", bxNm);//시트명
		mv.addObject("colNames", colNames);//컬럼명
		mv.addObject("colValues", colValues);//데이터
		mv.addObject("fileName", bxNm);//파일명
		mv.addObject("ext", "xlsx");//파일 확장자(없으면 xls)
		mv.addObject("widthList", widthList);//넓이
		
		return mv;
	}
	
	/** [AJAX] 의견목록 결재자 세팅 */
	@RequestMapping(value = "/ap/adm/box/transOpinApvrAjx")
	public String transOpinApvrAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String apvrUids = (String)jsonObject.get("apvrUids");
		
		try {
			
			apBxSvc.storeOpinApvrList(userVo, "admOpinBx", apvrUids);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
}
