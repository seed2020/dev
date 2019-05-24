package com.innobiz.orange.web.ap.ctrl;

import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** 결재함 건수 표시 */
@Controller
public class ApCountCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 사용자 설정 관련 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메신저에서 결재 대기함 건수 조회 */
	@RequestMapping(value = "/cm/ap/countWaitBx")
	public String countWaitBx(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(value = "bxId", required = false) String bxId,
			ModelMap model) throws Exception {
		
		PrintWriter writer = null;
		try{
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
						
			boolean isLinux = ServerConfig.IS_LINUX || (sysPlocMap!=null && sysPlocMap.get("messengerUcEnable")!=null && "Y".equals(sysPlocMap.get("messengerUcEnable")));
			
			writer = response.getWriter();
			if(userUid == null || userUid.isEmpty()) { 
				writer.print(getCnt(null, isLinux));
				return null;
			}
			
			//사용자 UID 복호화
			userUid = RC4.getDecrypt(userUid);
			
			//사용자 정보 조회
			OrUserBVo orUserBVo = new OrUserBVo();
    		orUserBVo.setUserUid(userUid);
    		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
    		
    		if(orUserBVo == null){
    			writer.print(getCnt(null, isLinux));
				return null;
    		}
    		
    		// 원직자UID
    		String odurUid = orUserBVo.getOdurUid();
    		
    		// 원직자의 개인설정 조회 - 디폴트 로그인 계정, 비밀번호변경일
			Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, odurUid, PtConstant.PT_LOGIN, true);
			userUid = odurLoginMap.get("defUserUid");
			if(userUid != null && !userUid.isEmpty()){
				OrUserBVo storedOrUserBVo = new OrUserBVo();
				storedOrUserBVo.setUserUid(userUid);
				storedOrUserBVo = (OrUserBVo)commonSvc.queryVo(storedOrUserBVo);
	    		if(storedOrUserBVo == null) userUid = null;
			}
			
			//기본설정 사용자 UID가 없을경우 원직자 UID를 사용자UID에 세팅
			if(userUid == null || userUid.isEmpty()) userUid = orUserBVo.getOdurUid();
			
			if(bxId == null || "".equals(bxId)) bxId = "waitBx";
			// 사용자 정보
			UserVo userVo = new UserVo();
			userVo.setUserUid(userUid);
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			VoUtil.bind(request, apOngdBVo);
			apOngdBVo.setBxId(bxId);
			
			// 함별 조회 조건 세팅
			boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, false, model);
			
			Integer recodeCount = 0;
			if(valid){
				// 카운트 조회
				recodeCount = commonSvc.count(apOngdBVo);
			}
			//결재 건수
			writer.print(getCnt(String.valueOf(recodeCount.intValue()), isLinux));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
		return null;
	}
	
	/** 건수 포맷 변경 */
	public String getCnt(String cnt, boolean isLinux){
		if(cnt==null) cnt = "0";
		if(isLinux){// 리눅스일 경우 XML 포맷으로 변경하여 리턴한다.
			XMLOutputter xo = new XMLOutputter();
			xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
			Element root = new Element("COUNTLIST");
			Element systemName = new Element("SYSTEMNAME");
			systemName.addContent("APPROVAL"); // 시스템명(결재)
			Element count = new Element("COUNT");
			count.addContent(cnt);
			root.addContent(systemName);
			root.addContent(count);
			Document doc = new Document().setRootElement(root);
			return xo.outputString(doc);
		}
		return cnt;
	}
	
}
