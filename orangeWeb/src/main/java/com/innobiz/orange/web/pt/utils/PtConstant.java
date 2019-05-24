package com.innobiz.orange.web.pt.utils;

import com.innobiz.orange.web.ap.utils.ApConstant;

/** 포털 상수 */
public class PtConstant {
	
	// pt.~~ 로 설정된 항목은
	//  : PT_SYS_SETUP_D.SETUP_CLS_ID (시스템 설정의 카테고리)
	//  : PT_USER_SETUP_D.USER_UID (사용자 설정의 카테고리)    에 해당
	
	/** 웹 로그인 */
	public static final String PT_LOGIN = "pt.login";
	
	/** 모바일 로그인 */
	public static final String MB_LOGIN = "mb.login";
	
	/** 비밀번호 정책 */
	public static final String PT_PW_PLOC = "pt.pwPolc";
	
	/** 포틀릿 정책 */
	public static final String PT_PLT_PLOC = "pt.pltPolc";
	
	/** 보안 정책 - 외부 IP 차단 */
	public static final String PT_SECU_PLOC = "pt.secuPolc";
	
	/** 해외 IP 차단 - 웹 */
	public static final String PT_FOREIGN_IP_PLOC = "pt.webForeignIpPloc";
	
	/** 해외 IP 차단 - 모바일*/
	public static final String MB_FOREIGN_IP_PLOC = "mb.mobileForeignIpPloc";
	
	/** 복사 방지 기능 */
	public static final String PT_ANTI_COPY = "pt.antiCopy";
	
	/** 페이지별 레코드 수 설정 */
	public static final String PT_PAGE_RCNT = "pt.pageRecCnt";
	
	/** 페이지별 레코드 수 설정 - 모바일 */
	public static final String MB_PAGE_RCNT = "mb.pageRecCnt";
	
	/** 레이아웃 설정 */
	public static final String PT_LOUT_SETUP = "pt.layoutSetup";
	
	/** 레이아웃 설정 */
	public static final String PT_LOUT_DEFLT = "pt.defaultLayout";
	
	/** 사용자 레이아웃 설정 */
	public static final String PT_LOUT_USER = "pt.userLayout";

	/** 관리 범위 설정 - 전체회사, 소속회사 */
	public static final String PT_MNG_COMP = "pt.mngCompSetup";

	/** 본문 용량 */
	public static final String PT_BODY_SIZE = "pt.bodySize";
	
	/** 첨부 용량 */
	public static final String PT_ATTC_SIZE = "pt.attachSize";
	
	/** 첨부 확장자 */
	public static final String PT_ATTC_EXT = "pt.attachExt";
	
	/** 첨부 설정 */
	public static final String PT_ATTC_SETUP = "pt.attachSetup";
	
	/** 스킨 이미지 */
	public static final String PT_SKIN_IMG = "pt.skinImage";
	
	/** 스킨 이미지 */
	public static final String PT_LANG_FONT = "pt.langFont";
	
	/** 관리자 로그인 */
	public static final String PT_ADMIN_PW = "pt.secu";
	
	/** 연계, 도메인 - 연계(메일,메신저 - IP:PORT), 도메인(웹 도메인, 메일생성용 도메인) */
	public static final String PT_SVR_ENV = "pt.svrEnv";
	
	/** 시스템 정책 */
	public static final String PT_SYS_PLOC = "pt.sysPloc";
	
	/** 시스템 중단 */
	public static final String PT_SYS_HALT = "pt.sysHalt";
	
	/** 로그인 이미지 - 웹용 */
	public static final String PT_LGIN_IMG = "pt.lginImg";
	
	/** 로그인 이미지 - 모바일용 */
	public static final String MB_MOB_LGIN = "mb.mobLgin";
	
	/** 메세지 로그인 - 모바일용 */
	public static final String MB_MOB_MSG_LGIN = "mb.mobMsgLgin";
	
	/** 나에 메뉴 설정 */
	public static final String PT_MY_MNU = "pt.myMnu";
	
	/** 비밀번호 찾기 */
	public static final String PT_LOST_PW = "pt.lostPw";
	
	/** 로그인 페이지 URL */
	public static final String URL_LOGIN = "/cm/login/viewLogin.do";
	
	/** ERP SSO 로그인 페이지 URL */
	public static final String URL_SSO_LOGIN = "/cm/login/viewSsoLogin.do";
	
	/** 초기 페이지 설정 URL */
	public static final String URL_SET_INIT = "/pt/psn/init/setInitPage.do";
	
	/** 모바일 초기 페이지 설정 URL */
	public static final String URL_SET_MO_INIT = "/pt/psn/setEnv.do";
	
	/** 비밀번호 변경 URL */
	public static final String URL_SET_PW = "/pt/psn/pw/setPw.do";
	
	/** 나의메뉴 설정 URL */
	public static final String URL_SET_MY_MNU = "/pt/psn/my/setMyMnu.do";
	
	/** 총괄 관리자 그룹 */
	public static final String AUTH_ADMIN = "ADMIN";

	/** 시스템 관리자 그룹(그룹 총괄 관리자) */
	public static final String AUTH_SYS_ADMIN = "SYS_ADMIN";
	
	/** 모든 사용자 그룹 */
	public static final String AUTH_USERS = "USERS";
	
	/** 그룹의 회사ID */
	public static final String SYS_COMP_ID = "0";
	
	/** 총 관리계정 로그인 아이디 */
	public static final String ADMIN_ID = "admin";
	
	/** 메뉴그룹 참조ID - 메일 */
	public static final String MNU_GRP_REF_MAIL = "MAIL";
	
	/** 메뉴그룹 참조ID - 커뮤니티 */
	public static final String MNU_GRP_REF_CT = "CT";
	
	/** 메뉴그룹 참조ID - 나의메뉴 */
	public static final String MNU_GRP_REF_MY = "MY";
	
	/** 디폴트 메인메뉴 최대 갯수 */
	public static final int MAIN_MNU_MAX_CNT = 10;
	
	/** 포틀릿 이동 최대 픽셀 - 포틀릿 설정 Free Zone 에서 가로 세로 위치 변경시 이동하는 픽셀 단위의 최대값 */
	public static final Integer PT_PLT_MOVE_MAX_PX = 10;
	
	/** 권한구분 - 내부망 */
	public static final String AUTH_IP_IN = "IP_IN";
	/** 권한구분 - 외부망 */
	public static final String AUTH_IP_EX = "IP_EX";
	
	/** 메신저 자동 로그인용 메세지ID */
	public static final String UCWARE_FOR_MSN = "ucware";
	
	/** 빈번한 Refresh 사용자 */
	public static final String PT_FREQUENT_REFRESH = "pt.frequentRefresh";
	
	/** 결재 본문보기 사용자 */
	public static final String AP_BODY_VIEW_USER = "ap.bodyViewUser";

	/** 비밀번호 정책 예외 */
	public static final String PT_PW_EXCEPTION_USER = "pt.pwExceptionUser";
	
	/** ERP SSO */
	public static final String PT_ERP_SSO = "pt.erpSso";
	
	/** 겸직 전용 - 11:해제 */
	public static final String[] USER_STAT_ADU_ONLY = new String[]{"11"};
	/** 겸직 전용 + 로그인 - 91 ~ 98, 98:숨김, 97:읽기전용, 96:SSO(ERP) */
	public static final String[] USER_STAT_ADU_LGIN = new String[]{"91","92","93","94","95","96","97","98"};
	/** 겸직 원직 둘다 사용 - 02:근무중, 99:삭제 */
	public static final String[] USER_STAT_BOTH = new String[]{"02","99"};
	
	/** 조직도 용어  */
	private static final String[] OR_TIRMS = new String[]{"siteName","grade","title","posit","duty","secul","role"};
	
	/** 권한 체크할 메뉴그룹 모듈 참조 ID 목록 */
	public static final String[] AUTH_CHK_MNU_GRP_MD_RIDS = { "MAIL", "AP", "WC", "DM", "BB" };
	
	/** PC 알림 모듈 */
	public static final String[] PC_NOTI_MDS = {"AP", "MAIL", "BB"};
	
	/** 메일 SSL 포트 */
	public static final String MAIL_SSL_PORT = "4443";
	
	/** 용어목록 리턴 */
	public static String[] getTerms(String setupClsId){
		if("ap.term".equals(setupClsId)){
			return ApConstant.AP_TERMS;
		} else if("or.term".equals(setupClsId)){
			return OR_TIRMS;
		}
		return null;
	}
	
}
