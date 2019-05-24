package com.innobiz.orange.web.ap.utils;

public class ApConstant {

	/** 시스템 설정의 카테고리ID - 포털 기능 이용 저장 (PT_SYS_SETUP_D) */
	public static final String AP_SYS_CONFIG = "ap.optConfig";
	
	/** 시스템 설정의 JSON용 카테고리ID */
	public static final String AP_SYS_CONFIG_JSON = "ap.optConfigJson";
	
	/** 결재 저장소 */
	public static final String AP_STORAGE = "AP_STORAGE";
	
	/** 월 */
	public static final int[] MONTHS = {1,2,3,4,5,6,7,8,9,10,11,12};
	
	/** 일 */
	public static final int[] DAYS = {
		1,2,3,4,5,6,7,8,9,10,
		11,12,13,14,15,16,17,18,19,20,
		21,22,23,24,25,26,27,28,29,30,
		31};
	
	/** ERP 연계 구분 코드
	 * 
	 * ERP : 일반적인 ERP 연계 (uniERP)
	 * ERP_XML : 본문 대신 XML을 입력 - 그룹웨어에서 XML 기반으로 HTML 생성
	 * ERP_CHIT : ERP + 전표보기 형태 - 전표를 위한 전표 html 을 별도 테이블에 입력 (프라코)
	 * ERP_HANWHA : 결재 진행을 ERP 테이블에 직접 UPDATE 하는 방식 (한화제약)
	 *  */
	public static final String[] INTG_TYP_CDS = {"ERP", "ERP_XML", "ERP_CHIT", "ERP_HANWHA", "ERP_ONECARD"};
	
	/** ERP 전표보기 - 프라코용 */
	public static final String ERP_CHIT = "ERP_CHIT";
	
	/** 디폴트 서명 방법 코드 */
	public static final String DFT_SIGN_MTHD_CD = "03";
	
	/** 디폴트 서명 영역 날자 표시 */
	public static final String DFT_SIGN_AREA_DT = "yyyy-MM-dd";
	
	/** zip 으로 묶어서 다운로드 하는 파일 명 */
	public static final String DOWN_ZIP_FILE_NAME = "attachFiles.zip";
	
	/** 파일 모듈 - apFiles.tag, viewAttHisPop.jsp 에서 사용 목적 */
	public static final String AP_FILE_MODULE = "ap/box";
	
	/** 파일 모듈 - apFiles.tag, viewAttHisPop.jsp 에서 사용 목적 */
	public static final String AP_ADM_FILE_MODULE = "ap/adm/box";
	
	/** 파일 모듈 - apFiles.tag, viewAttHisPop.jsp 에서 사용 목적 */
	public static final String DM_FILE_MODULE = "dm/box";
	
	/** 파일 모듈 - apFiles.tag, viewAttHisPop.jsp 에서 사용 목적 */
	public static final String DM_ADM_FILE_MODULE = "dm/adm/box";
	
	/** 결재 용어 */
	public static final String[] AP_TERMS = {
		"byOne","mak","revw","abs","apv","agnt","pred","reRevw","apvd","rejt",
		"agr","makAgr","deptAgr","ordrdAgr","paralAgr","psnOrdrdAgr","deptOrdrdAgr","psnParalAgr","deptParalAgr","pros","cons",
		"entu","postApvd","infm","psnInfm","deptInfm",
		"dblApv","req","reqDept","prc","prcDept",
		"makVw","fstVw","pubVw","paralPubVw","revw2","addInfm", 
		"refVw", "cfrmRefVw", "refVwr", "refVwDt"
	};
	
	/** 결재 행위를 한 결재 상태 코드 */
	public static String[] CMPL_APV_STAT_CDS = { "apvd","rejt","cons","pros","cmplVw" };
	
	/** 결재자 별 함(결재라인 테이블 기준) - 대기함, 진행함, 완료함, 기안함, 반려함, 통보함 */
	public static String[] APV_LN_BXES = {"waitBx", "ongoBx", "apvdBx", "myBx", "rejtBx", "postApvdBx"};
	
	/** 겸직 통합 표시 대상 -  대기함, 진행함, 완료함, 기안함, 반려함, 통보함, 참조열람 */
	public static String[] ADUR_BXES = {"waitBx", "ongoBx", "apvdBx", "myBx", "rejtBx", "postApvdBx", "refVwBx"};
	
	
	/** 업무관리 - 등록/수정 경로 */
	public static String WF_REG_PATH = "wf/works/setMdWorks";
	/** 업무관리 - 조회 */
	public static String WF_VIEW_PATH = "wf/works/viewWorks";
	/** 연차관리 타입ID */
	public static String[] WD_XML_TYPE_IDS = { "wdLeaveReq","wdLeaveCan","wdRepbReq","wdRepbCan" };
}
