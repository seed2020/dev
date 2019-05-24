package com.innobiz.orange.mobile.ct.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

@Service
public class MoCtSecuSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	//왼쪽 메뉴 권한별 검색
	public void getCtMenuList(HttpServletRequest request) throws SQLException, CmException {
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		// 커뮤니티 기본정보
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(ctEstbBVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 커뮤니티 메뉴정보
		CtMbshDVo ctMbshD = new CtMbshDVo();
		ctMbshD.setCompId(ctEstbBVo.getCompId());
		ctMbshD.setCtId(ctId);
		ctMbshD.setUserUid(userVo.getUserUid());
		ctMbshD = (CtMbshDVo) commonDao.queryVo(ctMbshD);
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCompId(ctEstbBVo.getCompId());
		ctFncDVo.setCtId(ctId);
		if(ctMbshD != null){
			ctFncDVo.setSeculCd(ctMbshD.getUserSeculCd());
			ctFncDVo.setAuthCd("R");
		}
		
		ctFncDVo.setLangTyp(langTypCd);
		ctFncDVo.setOrderBy("CT_FNC_ORDR, CT_FNC_LOC_STEP, CT_FNC_UID");
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
		request.setAttribute("_ctMenuList", ctFncList);
		request.setAttribute("_ctEstbBVo", ctEstbBVo);
		
		for (CtFncDVo vo : ctFncList) {
			if(vo.getCtFncId().equals("CTSECESSION")){
				// 커뮤니티 탈퇴
				request.setAttribute("footer", vo);
				break;
			}
		}
	}
}
