package com.innobiz.orange.web.em.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.svc.EmCmSvc;
import com.innobiz.orange.web.em.vo.EmAdrBldInfoBVo;
import com.innobiz.orange.web.em.vo.EmAdrCommVo;

/** 우편번호 조회 */
@Controller
public class EmZipCtrl {		
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "emCmSvc")
	private EmCmSvc emCmSvc;
	
	/** [AJAX] 우편번호 검색 관련 시도,시군구 코드 조회 []*/
	@RequestMapping(value = "/cm/selectCommonSidoAjx")
	public String selectCommonSidoAjx(HttpServletRequest request,
			HttpServletResponse response,
			@Parameter(name="schCat", required=true) String schCat,
    		@Parameter(name="schWord", required=true) String schWord,
			ModelMap model) throws Exception {
		
		String key = "gugun".equals(schCat) ? schWord : "SIDO";
		List<EmAdrCommVo> ptAdrCommVoList = emCmSvc.getSidoList(key, "ko");
        model.put("list", ptAdrCommVoList);
        return JsonUtil.returnJson(model);
	}
	
	/** [POPUP] 우편번호검색팝업 [직접입력] */
	@RequestMapping(value = "/cm/setZipCodePop")
	public String setZipCodePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/em/setZipCodePop");
	}
	
	/** [POPUP] 우편번호검색팝업 [DB조회] */
	@RequestMapping(value = "/cm/findZipCodePop")
	public String zipCodePopup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		List<EmAdrCommVo> ptAdrCommVoList = emCmSvc.getSidoList("SIDO", "ko");
        model.put("sidoList", ptAdrCommVoList);
		
		return LayoutUtil.getJspPath("/em/findZipCodePop");
	}
	
	
	/** 문자열 병합 */
	public String mergeString(String... strings ){
		String returnString = "";
		for(String str : strings){
			if(str==null) continue;
			returnString+=str;
		}
		return returnString;
	}
	
	/** 주소 조회 */
	@RequestMapping("/cm/getZipCode")
    public String getZipCode(HttpServletRequest request, 
    		@Parameter(name="schCat", required=true) String schCat,
    		@Parameter(name="schWord", required=true) String schWord,
    		@Parameter(name="schSido", required=false) String schSido,
    		@Parameter(name="schGugun", required=false) String schGugun,
    		@Parameter(name="pageRowCnt", required=false) Integer pageRowCnt,
    		ModelMap model) throws Exception {

        if(StringUtil.getBytes(schWord) < 6){
        	model.put("errorCode", 2);
        	return JsonUtil.returnJson(model);
        }
        
        // set, list 로 시작하는 경우 처리
 		// 세션의 언어코드
// 		String langTypCd = LoginSession.getLangTypCd(request);
 		
 		// 조회조건 매핑
 		EmAdrBldInfoBVo searchVO = new EmAdrBldInfoBVo();
 		searchVO.setSchCat(schCat);
 		//schWord.replaceAll("^[\\x{ac00}-\\x{d7af}\\s]", "");
 		String[] schWords = schWord.split(" ");

 		searchVO.setSchWord(schWords[0]);
 		searchVO.setSchSido(schSido);
 		searchVO.setSchGugun(schGugun);
 		if(pageRowCnt != null ) searchVO.setPageRowCnt(pageRowCnt);
 		else searchVO.setPageRowCnt(1000);
 		
 		if(schWords.length > 1){
 			if("oldpost".equals(schCat)){
 				if(schWords[1].indexOf("-")>-1){
 					searchVO.setMainLotNo(schWords[1].split("-")[0]);
 					searchVO.setSubLotNo(schWords[1].split("-")[1]);
 				}else{
 					searchVO.setMainLotNo(schWords[1]);
 	 				if(schWords.length>2) searchVO.setSubLotNo(schWords[2]);
 				}
 	 		}else if("bldNm".equals(schCat)){
 	 			searchVO.setBldNm(schWords[1]);
 	 		}else{
 	 			if(schWords[1].indexOf("-")>-1){
 					searchVO.setMainBldNo(schWords[1].split("-")[0]);
 					searchVO.setSubBldNo(schWords[1].split("-")[1]);
 				}else{
 					searchVO.setMainBldNo(schWords[1]);
 	 	 			if(schWords.length>2) searchVO.setSubBldNo(schWords[2]);
 				}
 	 		}
 		}
 		//VoUtil.bind(request, searchVO);
 		
 		Integer recodeCount = commonSvc.count(searchVO);
 		
 		if(recodeCount.intValue() == 0 ){
        	model.put("errorCode", 1);
        	return JsonUtil.returnJson(model);
        }
        
        if(recodeCount.intValue() >= (pageRowCnt != null ? pageRowCnt : 1000) ){
        	model.put("errorCode", 3);
        	return JsonUtil.returnJson(model);
        }
        
        List<EmAdrBldInfoBVo> ptAdrBldInfoBVoList = new ArrayList<EmAdrBldInfoBVo>();
        
        // 검색 시작
        long start = System.currentTimeMillis();
 		
 		@SuppressWarnings("unchecked")
		List<EmAdrBldInfoBVo> tmpList = (List<EmAdrBldInfoBVo>)commonSvc.queryList(searchVO);
 		
 		if(tmpList != null && tmpList.size()>0){
 			Map<String,Boolean> chkMap = new HashMap<String,Boolean>();
 			String mergeString = null;
 			for(EmAdrBldInfoBVo objVo : tmpList){
 				mergeString = mergeString(objVo.getSido(),objVo.getGugun(),objVo.getMainLotNo(),objVo.getSubLotNo(), objVo.getRoadNmCd(),objVo.getMainBldNo(),objVo.getSubBldNo(), objVo.getCityPublicBldNm());
 				if(chkMap.containsKey(mergeString)) continue;
 				chkMap.put(mergeString, Boolean.TRUE);
 				ptAdrBldInfoBVoList.add(objVo);
 			}
 		}
        model.put("ptAdrBldInfoBVoList", ptAdrBldInfoBVoList);
        model.put("recodeCount", recodeCount);
        //추후 페이징 기능 필요할시.
       // Integer recodeCount = commonSvc.count(searchVO);
		//PersonalUtil.setPaging(request, searchVO, recodeCount);
        
        // 검색 종료
        long end = System.currentTimeMillis(); 
        model.put("time", (end-start)/1000.0f);
        
        return JsonUtil.returnJson(model);
    }
}
