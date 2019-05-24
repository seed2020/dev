package com.innobiz.orange.web.cm.ctrl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;

/** 타시스템 로그인 체크용 */
@Controller
public class CmSsoApiCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 사용자 확인 - 타시스템에서 로그인ID + 비번 또는  사번+비번 을 전달하여 사용자 확인용
	 *   - 한화제약 - 타시스템에서 로그인 할때 - ID/비밀번호 체크용
	 *  */
	@RequestMapping(value = "/cm/sso/checkUser")
	public String getPathUrl( HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "lginId", required = false) String lginId,
			@RequestParam(value = "ein", required = false) String ein,
			@RequestParam(value = "pw", required = false) String pw,
			ModelMap model) throws IOException{
		
		try {
			OrOdurBVo orOdurBVo = null;
			if(lginId!=null && !lginId.isEmpty()){
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setLginId(lginId);
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			} else if(lginId!=null && !lginId.isEmpty()){
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setEin(ein);
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			}
			
			if(orOdurBVo==null){
				response.getWriter().print("false");
				return null;
			}
			
			if("02".equals(orOdurBVo.getUserStatCd())){
				response.getWriter().print("false");
				return null;
			}
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
			OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(orOdurBVo.getOdurUid());
			orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
			orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
			
			if(orUserPwDVo==null){
				response.getWriter().print("false");
				return null;
			}
			
			String encPw = License.ins.encryptPw(pw, orOdurBVo.getOdurUid());
			if(encPw.equals(orUserPwDVo.getPwEnc())){
				response.getWriter().print("true");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.getWriter().print("false");
		return null;
	}
}
