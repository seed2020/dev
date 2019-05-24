package com.innobiz.orange.mobile.st.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.st.svc.StSiteSvc;
import com.innobiz.orange.web.st.vo.StCatBVo;
import com.innobiz.orange.web.st.vo.StSiteBVo;

@Controller
public class MoStSiteCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 이미지 저장 서비스 */
	@Resource(name = "stSiteSvc")
	private StSiteSvc stSiteSvc;
	
	/** 조회 */
	@RequestMapping(value = "/st/site/listSite")
	public String listSiteFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		StSiteBVo stSiteBVo = new StSiteBVo();
		VoUtil.bind(request, stSiteBVo);
		stSiteBVo.setQueryLang(langTypCd);
		stSiteBVo.setCompId(userVo.getCompId());
		
		if(stSiteBVo.getCatId()!=null){
			stSiteBVo.setOrderBy("SORT_ORDR ASC");
		}
				
		// 카운트 조회
		Integer recodeCount = commonSvc.count(stSiteBVo);
		PersonalUtil.setPaging(request, stSiteBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<StSiteBVo> stSiteBVoList = (List<StSiteBVo>)commonSvc.queryList(stSiteBVo);
		model.put("stSiteBVoList", stSiteBVoList);
		model.put("recodeCount", recodeCount);
		
		StCatBVo stCatBVo = new StCatBVo();
		stCatBVo.setQueryLang(langTypCd);
		stCatBVo.setCompId(userVo.getCompId());
		
		// 카테고리조회
		@SuppressWarnings("unchecked")
		List<StCatBVo> stCatBVoList = (List<StCatBVo>)commonSvc.queryList(stCatBVo);
		model.put("stCatBVoList", stCatBVoList);
		
		// 이미지 조회
		for(StSiteBVo storedStSiteBVo : stSiteBVoList){
			stSiteSvc.setSiteImgDVo(storedStSiteBVo);
		}
		
		return MoLayoutUtil.getJspPath("/st/site/listSite");
	}
	
}
