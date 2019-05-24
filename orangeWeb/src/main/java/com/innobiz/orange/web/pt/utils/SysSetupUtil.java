package com.innobiz.orange.web.pt.utils;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 시스템셋업 유틸 - JSP에서 PtSysSvc 사용 목적으로 static으로 감싸 놓은 유틸 */
@Component
public class SysSetupUtil {

	/** 자기자신의 객체 - static 함수에서 용하기 위한것 */
	private static SysSetupUtil ins = null;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 생성자 */
	public SysSetupUtil(){
		ins = this;
	}
	
	/** 용어 맵 조회 */
	public static Map<String, String> getTermMap(String setupClsId, HttpServletRequest request) throws SQLException{
		String langTypCd = LoginSession.getLangTypCd(request);
		return ins.ptSysSvc.getTermMap(setupClsId, langTypCd);
	}
	/** 용어 맵 조회 */
	public static Map<String, String> getTermMap(String setupClsId, String langTypCd) throws SQLException{
		return ins.ptSysSvc.getTermMap(setupClsId, langTypCd);
	}
	/** 어권별 폰트 리턴 */
	public static String getFonts(String langTypCd) throws SQLException{
		Map<String, String> fontMap = ins.ptSysSvc.getSysSetupMap(PtConstant.PT_LANG_FONT, true);
		String fonts = fontMap==null ? null : fontMap.get(langTypCd);
		if("ko".equals(langTypCd) && fonts==null){
			fonts = "바탕체|굴림체|돋움체|궁서체|Arial,geneva,sans-serif|Geneva,arial,sans-serif|Courier,monospace";
		}
		return fonts;
	}
	/** 데모용 사이트 여부 */
	public static boolean isDemoSite(){
		try {
			// 개발서버 - AD8227
			if(CustConfig.DEV_SVR){
				return true;
			}
		} catch(Exception ignore){}
		return false;
	}
	/** 시스템 정책 조회 */
	public static Map<String, String> getSysPlocMap() throws SQLException {
		return ins.ptSysSvc.getSysPlocMap();
	}
	/** 시스템 정책 조회 */
	public static String getSysPloc(String key) throws SQLException {
		return ins.ptSysSvc.getSysPlocMap().get(key);
	}
	/** 서버 설정 목록 조회 */
	public static Map<String, String> getSvrEnvMap() throws SQLException {
		return ins.ptSysSvc.getSvrEnvMap();
	}
	/** 서버 설정 조회 */
	public static String getSvrEnv(String key) throws SQLException {
		return ins.ptSysSvc.getSvrEnvMap().get(key);
	}
	/** 빈번한 Refresh 사용자 인지 */
	public static boolean isFrequentRefreshUser(UserVo userVo){
		String userUids = ins.ptSysSvc.getFrequentRefreshUser(userVo.getCompId());
		return userUids!=null && userUids.indexOf(userVo.getUserUid()) >= 0;
	}
	/** 빈번한 Refresh 사용자 인지 */
	public static boolean isApBodyViewUser(UserVo userVo){
		String userUids = ins.ptSysSvc.getApViewBodyUser(userVo.getCompId());
		return userUids!=null && userUids.indexOf(userVo.getUserUid()) >= 0;
	}
}
