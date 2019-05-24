package com.innobiz.orange.web.sample.ctrl;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

@Controller
public class SamplePtCmSvcCtrl {

	@Autowired
	private PtCmSvc ptCmSvc;
	
	/**
	 * 코드 조회
	 * 
	 * */
	@RequestMapping(value = "/sample/ptcmsvc/test1")
	public String test1(Locale locale, Model model) throws Exception {
		
		System.out.println("--- 코드 목록 조회 ---");
		List<PtCdBVo> cdList = ptCmSvc.getCdList("LANG_TYP_CD", locale.getLanguage(), "Y");
		for(PtCdBVo ptCdBVo: cdList){
			System.out.println(ptCdBVo);
		}
		
		System.out.println("--- 코드 단건 조회 ---");
		PtCdBVo ptCdBVo2 = ptCmSvc.getCd("LANG_TYP_CD", locale.getLanguage(), "ko");
		System.out.println(ptCdBVo2);
		
		System.out.println("--- 코드 목록 조회 : Filtered ---");
		//파라미터 : clsCd, langTypCd, compId, refVa1="KR", refVa2
		//DB 데이터는 :refVa1=KR,US 등으로 콤마(,)로 구별되어 값이 들어 있음
		cdList = ptCmSvc.getFilteredCdList("LANG_TYP_CD", locale.getLanguage(), null, "KR", null, null);
		for(PtCdBVo ptCdBVo: cdList){
			System.out.println(ptCdBVo);
		}
		
		return "sample/tilesSample";
	}
	
}
