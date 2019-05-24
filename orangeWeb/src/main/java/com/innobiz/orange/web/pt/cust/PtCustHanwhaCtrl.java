package com.innobiz.orange.web.pt.cust;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.IpChecker;

/**
 * 한화제약 요구 - 비밀번호 체크 URL 제공
 * */
@Controller
public class PtCustHanwhaCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;

	/** 비밀번호 체크 */
	@RequestMapping(value = {"/cm/pt/checkPw", "/cm/pt/checkApPw"})
	public String checkPw(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="id", required=false) String id,
			@Parameter(name="pw", required=false) String pw,
			ModelMap model) throws Exception {
		
		String uri = request.getRequestURI();
		String pwTypCd = uri.indexOf("checkApPw") > 0 ? "APV" : "SYS";
		
		// 한화제약 - E78CEB
		if(ServerConfig.IS_LOC || CustConfig.CUST_HANWHA){
			
			String ip = ipChecker.getIp(request);
			if(!ip.startsWith("192.168.") && !ip.startsWith("10.10.") && !ip.equals("127.0.0.1")){
				response.getWriter().write("false");
				return null;
			}
			
			if(checkSysPw(id, pw, pwTypCd)){
				response.getWriter().write("true");
				return null;
			} else {
				response.getWriter().write("false");
				return null;
			}
		}
		
		response.getWriter().write("false");
		return null;
	}
	
	/** 비밀번호 체크 */
	private boolean checkSysPw(String lginId, String pw, String pwTypCd) throws SQLException, CmException, IOException{
		
		if(lginId==null || lginId.isEmpty()) return false;
		if(pw    ==null ||     pw.isEmpty()) return false;
		
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setLginId(lginId);
		orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
		if(orOdurBVo==null) return false;
		
		String odurUid = orOdurBVo.getOdurUid();
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		orUserPwDVo.setPwTypCd(pwTypCd);//SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
		if(orUserPwDVo==null) return false;
		
		if("SYS".equals(pwTypCd)){
			// 로그인 비밀번호
			String paramEncPw = License.ins.encryptPw(pw, odurUid);
			return paramEncPw.equals(orUserPwDVo.getPwEnc());
		} else if("APV".equals(pwTypCd)){
			// 결재 비밀번호
			String paramEncPw = License.ins.encryptPw(pw, odurUid);
			return paramEncPw.equals(orUserPwDVo.getPwEnc());
		}
		
		return false;
	}
	
}
