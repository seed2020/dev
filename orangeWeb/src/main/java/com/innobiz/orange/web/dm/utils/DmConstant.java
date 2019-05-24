package com.innobiz.orange.web.dm.utils;

public class DmConstant {
	
	/** 문서 구분자 */
	public static final String SPRIT = ":";
	
	/** 시스템 설정의 카테고리ID - 포털 기능 이용 저장 (PT_SYS_SETUP_D) */
	public static final String SYS_CONFIG = "dm.envConfig";
	
	/** 저장소정보 */
	public final static String STOR = "DM_STOR";
	
	/** 폴더정보 */
	public final static String FLD = "FLD";
	
	/** 분류정보 */
	public final static String CLS = "CLS";
	
	/** 회사 최상위 폴더명 */
	public final static String FLD_COMP = "COMP";
	
	/** 부서 최상위 폴더명 */
	public final static String FLD_DEPT = "DEPT";
	
	/** 개인 최상위 폴더명 */
	public final static String FLD_PSN = "PSN";
	
	/** 미분류 최상위 폴더명 */
	public final static String FLD_NONE = "NONE";
	
	/** 문서번호 연도 기본 */
	public final static String DOC_NO_YEAR = "0000";
	
	/** 문서번호 조직(회사) 기본 */
	public final static String DOC_NO_ORG = "ORG";
	
	/** 탭 속성ID */
	public static final String[][] TAB_LIST = {{"L","list"},{"F","fld"},{"C","cls"}};
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "dmfiles";
	
	/** 테이블명 prefix */
	public static final String TBl_NM_PREFIX = "DM_";

	/** 테이블명 suffix */
	public static final String TBl_NM_SUFFIX = "_DOC_L";
	
	/** 개인문서 테이블명 suffix */
	public static final String PSN_TBl_NM_SUFFIX = "PSN";
	
	/** 코드 컬럼명 목록 */
	public static final String[] COLM_NM_CDS = new String[]{"CD","UID","ID"};
	
	/** 테이블 생성시 제외할 컬럼명 목록 */
	public static final String[] COLM_NM_EXCLUDE = new String[]{"CLS_ID","ATT_FILE_ID","FLD_ID","DOC_NO","OWNR_UID","DOC_KEEP_PRD_CD","SECUL_CD","CMPL_DT","VER_VA","FILE_CNT","READ_CNT"};
			
	/** 코드 컬럼명 변경 SUFFIX */
	public static final String COLM_NM_SUFFIX = "Nm";
	
	/** 권한그룹 코드 */
	public static final String AUTH_GRP_CD = "DM_AUTH_GRP_CD"; //공통코드에 등록
	
	/** 보안등급 분류코드 */
	public static final String SECUL_CD = "SECUL_CD";
	
	/** 인수인계 상태코드 */
	public static final String TAK_STAT_CD = "TAK_STAT_CD";
	
	/** 열람요청 상태코드 */
	public static final String VIEW_STAT_CD = "VIEW_STAT_CD";
	
	/** 열람요청대상코드 */
	public static final String VIEW_TGT_CD = "VIEW_TGT_CD";
	
	/** 문서보존기간코드 */
	public static final String DOC_KEEP_PRD_CD = "DOC_KEEP_PRD_CD";
	
	/** 권한그룹 - Prefix */
	public static final String AUTH_GRP_PREFIX = "AUTH_GRP";
	
	/** 권한그룹 - 웹 */
	public static final String AUTH_GRP_U = "AUTH_GRP_U";
	
	/** 권한그룹 - 모바일 */
	public static final String AUTH_GRP_M = "AUTH_GRP_M";
	
	/** 기타 권한분류코드 */
	public static final String AUTH_CLS_CD = "DM_AUTH_CD";
	
	/** 권한적용 URL SUFFIX - 관리자가 권한적용 설정시 요청URL을 구분 : [최신문서,보존연한문서,즐겨찾기,문서조회]*/
	public static final String[] AUTH_URL_SUFFIX = new String[]{"New","Kprd","Bumk","Doc"};
	
	/** 읽음 표시 URL SUFFIX  - 읽음표시 및 조회수 체크 URL을 구분 : [최신문서,보존연한문서,즐겨찾기,문서조회,열람요청,이관문서,소유문서]*/
	public static final String[] READ_URL_SUFFIX = new String[]{"New","Kprd","Bumk","Doc","OpenReq","Transfer","Own"};
	
	/** 관련문서 제외 URL SUFFIX - 요청URL을 구분 : [열람요청,열람승인,임시]*/
	//public static final String[] SUB_NOT_URL = new String[]{"OpenReq","OpenApv","Tmp","Disc"};
	
	/** 공통 기본 Prefix */
	public static final String COMM_PREFIX = "COMM_";
	
	/** 미분류코드 */
	public static final String EMPTY_CLS = "NONE";
	
	/** 조직 대표 코드 */
	public static final String ORG_ID = "ORG_ID";
	
	/** 권한 코드 목록 */
	public static final String[] AUTH_CDS = {"view","update","send","email","move","recycle","recovery","disuse","download","owner","docHst","docNoMod","cls","keepDdln","seculCd","print"};
	
	/** 작업 코드 목록 */
	public static final String[] TASK_CDS = {"view","insert","update","email","copy","move","recycle","recovery","download"};
	
	/** 권한 체크 제외 Suffix */
	public static final String[] NOT_AUTH_SUFFIX = {"Tmp","Disc","Subm","Recycle","Sub","Psn","TransferTgt","TransferHst","TransTgtDoc","TransWaitDoc","TakovrDoc","OpenReq","OpenApv"};
	
	/** 목록에 멀티체크 보여야할 URL SUFFIX*/
	public static final String[] LIST_MULTI_SUFFIX = {"TransferTgt","TransferHst","TransTgtDoc","TransWaitDoc","TakovrDoc"};
	
	/** 생성 테이블 VO */
	public static final String[] CREATE_TBLS = {"DmDocL","DmClsR","DmDocD","DmDocVerL","DmKwdL","DmFileD","DmTaskH"};
	
	/** 생성 테이블 인덱스 VO */
	public static final String[] CREATE_IDXS = {"DmDocL","DmDocD"};
	
	/** 이관할 VO[테이블] 명*/
	//public static final String[] COPY_VOS = {"DmCatBVo","DmCatDispDVo","DmCdDVo","DmCdGrpBVo","DmClsBVo","DmFldBVo","DmItemBVo", "DmRescBVo","DmDocNoDVo","DmSubmLVo","DmTmpSaveLVo"};
	public static final String[] COPY_VOS = {"DmCatBVo","DmCatDispDVo","DmCdDVo","DmCdGrpBVo","DmClsBVo","DmFldBVo","DmItemBVo", "DmRescBVo"};
	
	/** 생성자에 인자를 가지는 VO[테이블]명 */
	public static final String[] STOR_VOS = {"DmFldBVo","DmRescBVo"};
	
	/** 부모문서에서 복사할 속성명 */
	public static final String[] SUB_COPY_ATTRS = {"fldId","docKeepPrdCd","seculCd","ownrUid","statCd"};
	
	/** 문서상태 중 수정되면 안되는 상태 목록 */
	public static final String[] NOT_MOD_STAT_CD = {"A","R","W","O","M","D"};
	
	/** 개인문서 기본항목 속성ID*/
	public static final String[] PSN_ITEM_DISP_ATRBS = {"fldNm","subj","fileCnt","regDt"};
	//public static final String[] PSN_ITEM_DISP_ATRBS = {"fldNm","subj","cont","fileCnt","regrNm","regDt","modrNm","modDt"};
	
	/** 소스 패키지 경로 [추후변경]*/
	public static final String pakage= "com.innobiz.orange.web.dm.vo";
	
	/** 월 */
	public static final int[] MONTHS = {1,2,3,4,5,6,7,8,9,10,11,12};
	
	/** 일 */
	public static final int[] DAYS = {
		1,2,3,4,5,6,7,8,9,10,
		11,12,13,14,15,16,17,18,19,20,
		21,22,23,24,25,26,27,28,29,30,
		31};
	
	/** zip 으로 묶어서 다운로드 하는 파일 명 */
	public static final String DOWN_ZIP_FILE_NAME = "attachFiles.zip";
}
