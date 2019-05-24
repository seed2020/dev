package com.innobiz.orange.web.wf.utils;

import com.innobiz.orange.web.wf.vo.WfFormRegDVo;

public class WfConstant {
	
	/** 시스템 설정의 카테고리ID - 포털 기능 이용 저장 (PT_SYS_SETUP_D) */
	public static final String SYS_CONFIG = "wf.envConfig";
	
	/** 사용자 설정의 카테고리ID - 공통 기능 이용 저장 (EM_USER_SETUP_D) */
	public static final String USER_CONFIG = "wf.userConfig";
	
	/** 모듈정보 */
	public final static String GRP = "FORM_GRP";
	
	/** 코드목록맵 prefix */
	public final static String CODE_LIST_PREFIX = "CD_LIST_";
	
	/** SELECT 제외 컬럼 목록 */
	public final static String[] EXCLUDE_SELECT = new String[]{"file", "image"};
	
	/** 검색 제외 컬럼 목록 */
	public final static String[] EXCLUDE_SRCH = new String[]{"file", "calculate", "image", "readCnt"};
	
	/** [기본검색] 텍스트 컬럼 목록 */
	public final static String[] SRCH_TXT = new String[]{"text", "textarea", "editor"};
	
	/** [기본검색] 날짜 컬럼 목록 */
	public final static String[] SRCH_DATE = new String[]{"date", "period"};
	
	/** 코드테이블에 저장될 컬럼명 목록[사용자,부서] */
	public final static String[] CODE_COLM_LIST = new String[]{"user", "dept"};
	
	/** 코드테이블에 저장될 컬럼명 목록[SELECT, RADIO, CHECKBOX] - 양식코드를 선택하면 별도 저장 그렇지 않으면 json 으로 저장*/
	public final static String[] CD_TO_TBL_COLM_LIST = new String[]{"select", "radio", "check"};
	
	/** 구분자로 저장된 데이터 컬럼명 목록[RADIO SINGLE, CHECKBOX SINGLE] */
	public final static String[] SINGLE_TO_TBL_COLM_LIST = new String[]{"radioSingle", "checkboxSingle"};
	
	/** 코드그룹을 사용할 수 있는 컬럼 목록  */
	public final static String[] CODE_GRP_LIST = new String[]{"select", "radio", "checkbox"};
	
	/** 분할해야될 컬럼 목록 - 테이블 생성, 데이터 조회 [컬럼명,분할갯수] */
	public final static String[][] DIVISION_COLM_LIST = new String[][]{{"period","2"}, {"addr", "2"}};
	
	/** 분할해야될 컬럼 목록 - 테이블 생성, 데이터 조회 [컬럼명] */
	public final static String[] DIVISION_COLMS = new String[]{"period", "addr"};
	
	/** [검색] 텍스트 LIKE 컬럼 목록 */
	public final static String[] SRCH_LIKE_TXT = new String[]{"text", "textarea", "editor", "number", "calculate", "tel"};
	
	/** [검색] 텍스트 OR LIKE 컬럼 목록 */
	public final static String[] SRCH_OR_LIKE = new String[]{"addr"};
	
	/** [검색] 텍스트 EQUAL 컬럼 목록 */
	public final static String[] SRCH_EQ_TXT = new String[]{"time", "regrNm", "modrNm"};
	
	/** [검색] 날짜 컬럼 목록 */
	public final static String[] SRCH_FROM_DATE = new String[]{"date", "period", "datetime", "regDt", "modDt"};
	
	/** [검색] 코드 컬럼 목록 [select,radio,checkbox 는 코드 또는 like 검색]*/
	public final static String[] SRCH_EQ_CODE = new String[]{"user", "dept"};
	
	/** [검색] 코드 컬럼 목록 [select,radio,checkbox 는 코드 또는 like 검색]*/
	public final static String[] SRCH_LIKE_CODE = new String[]{"select", "radio", "checkbox"};
	
	/** [검색] 사용자 테이블 LIKE 컬럼 목록 */
	public final static String[] SRCH_LIKE_USER = new String[]{"regrNm", "modrNm"};
	
	/** 제외 컬럼 목록 - [등록자UID, 등록일, 수정자, 수정일] */
	public final static String[] EXCLUDE_COLM_LIST = new String[]{"readCnt", "regrUid", "regDt", "modrUid", "modDt"};
	
	/** 기본 컬럼 목록 - [등록자UID, 등록일, 수정자, 수정일] */
	public final static String[][] DFT_COLM_LIST = new String[][]{{"readCnt", "readCnt", "cols.readCnt"}, {"regrUid", "regrNm", "cols.regr"}, {"regDt", "regDt","cols.regDt"}, {"modrUid", "modrNm", "cols.modr"}, {"modDt", "modDt","cols.modDt"}};
	
	/** 테이블명 길이 */
	public final static int TBLNM_LEN = 4;
	
	/** zip 으로 묶어서 다운로드 하는 파일 명 */
	public static final String DOWN_ZIP_FILE_NAME = "attachFiles.zip";
	
	/** 미리보기 데이터 객체 */
	private static WfFormRegDVo previewVo=null;

	public static WfFormRegDVo getPreviewVo() {
		return previewVo;
	}

	public static void setPreviewVo(WfFormRegDVo previewVo) {
		WfConstant.previewVo = previewVo;
	}
	
	
	
	
}
