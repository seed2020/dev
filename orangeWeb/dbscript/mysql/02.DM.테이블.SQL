

/* ----------------------------
    DM
---------------------------- */


CREATE TABLE DM_AUTH_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CLS_CD                           VARCHAR (30) NOT NULL COMMENT '분류코드',
    CD                               VARCHAR (30) NOT NULL COMMENT '코드',
    AUTH_VA                          VARCHAR (30) NOT NULL COMMENT '권한값',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    PRIMARY KEY (COMP_ID, CLS_CD, CD, AUTH_VA)
)
COMMENT = '문서권한상세';


CREATE TABLE DM_BUMK_B (
    BUMK_ID                          VARCHAR (30) NOT NULL COMMENT '즐겨찾기ID',
    BUMK_NM                          VARCHAR (120) NOT NULL COMMENT '즐겨찾기명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    BUMK_CAT                         CHAR (1) NOT NULL COMMENT '즐겨찾기유형',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BUMK_ID)
)
COMMENT = '즐겨찾기기본';


CREATE TABLE DM_BUMK_L (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    BUMK_ID                          VARCHAR (30) NOT NULL COMMENT '즐겨찾기ID',
    REG_CAT                          VARCHAR (10) NOT NULL COMMENT '등록유형',
    CAT_VA                           VARCHAR (30) NOT NULL COMMENT '유형값',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, BUMK_ID, REG_CAT, CAT_VA)
)
COMMENT = '즐겨찾기목록';


CREATE TABLE DM_CAT_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '유형ID',
    CAT_NM                           VARCHAR (120) COMMENT '유형명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    CAT_DESC                         VARCHAR (400) COMMENT '설명',
    DISC_YN                          CHAR (1) COMMENT '심의여부',
    DISCR_UID                        VARCHAR (30) COMMENT '심의자UID',
    REGR_DISP_YN                     CHAR (1) COMMENT '작성자표시여부',
    OWNR_DISP_YN                     CHAR (1) COMMENT '소유자표시여부',
    READ_HST_USE_YN                  CHAR (1) COMMENT '열람이력사용여부',
    ITEM_USE_YN                      CHAR (1) COMMENT '항목사용여부',
    DFT_YN                           CHAR (1) COMMENT '기본여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, CAT_ID)
)
COMMENT = '유형기본';


CREATE TABLE DM_CAT_DISP_D (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '유형ID',
    ITEM_ID                          VARCHAR (30) NOT NULL COMMENT '항목ID',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    ATRB_ID                          VARCHAR (30) COMMENT '속성ID',
    WDTH_PERC                        VARCHAR (30) COMMENT '넓이퍼센트',
    ALN_VA                           VARCHAR (30) COMMENT '줄맞춤값',
    SORT_OPT_VA                      VARCHAR (30) COMMENT '정렬옵션값',
    DATA_SORT_VA                     VARCHAR (30) COMMENT '데이터정렬값',
    REG_DISP_YN                      CHAR (1) COMMENT '등록표시여부',
    MOD_DISP_YN                      CHAR (1) COMMENT '수정표시여부',
    READ_DISP_YN                     CHAR (1) COMMENT '조회표시여부',
    LIST_DISP_YN                     CHAR (1) COMMENT '목록표시여부',
    LIST_DISP_ORDR                   INT COMMENT '목록표시순서',
    ADD_ITEM_YN                      CHAR (1) COMMENT '추가항목여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, CAT_ID, ITEM_ID)
)
COMMENT = '유형표시상세';


CREATE TABLE DM_CD_D (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CD_GRP_ID                        VARCHAR (30) NOT NULL COMMENT '코드그룹ID',
    CD_ID                            VARCHAR (30) NOT NULL COMMENT '코드ID',
    CD_VA                            VARCHAR (400) COMMENT '코드값',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, CD_GRP_ID, CD_ID)
)
COMMENT = '코드상세';


CREATE TABLE DM_CD_GRP_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CD_GRP_ID                        VARCHAR (30) NOT NULL COMMENT '코드그룹ID',
    CD_GRP_NM                        VARCHAR (200) COMMENT '코드그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    GRP_USE_YN                       CHAR (1) COMMENT '그룹사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, CD_GRP_ID)
)
COMMENT = '코드그룹기본';


CREATE TABLE DM_CLS_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CLS_ID                           VARCHAR (30) NOT NULL COMMENT '분류체계ID',
    CLS_NM                           VARCHAR (120) COMMENT '분류체계명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    CLS_PID                          VARCHAR (30) NOT NULL COMMENT '상위분류체계ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    HST_YN                           CHAR (1) COMMENT '이력여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, CLS_ID)
)
COMMENT = '분류체계기본';


CREATE TABLE DM_COMM_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    DM_REF_ID                        INT COMMENT '참조ID',
    DISP_NM                          VARCHAR (240) COMMENT '표시이름',
    DISP_ORDR                        INT COMMENT '표시순서',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (FILE_ID)
)
COMMENT = '공통파일상세';


CREATE TABLE DM_COMM_FLD_B (
    FLD_ID                           VARCHAR (30) NOT NULL COMMENT '폴더ID',
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '유형ID',
    FLD_NM                           VARCHAR (120) COMMENT '폴더명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    FLD_PID                          VARCHAR (30) NOT NULL COMMENT '상위폴더ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (FLD_ID)
)
COMMENT = '공통폴더기본';


CREATE TABLE DM_COMM_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '공통리소스기본';


CREATE TABLE DM_FLD_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    FLD_ID                           VARCHAR (30) NOT NULL COMMENT '폴더ID',
    FLD_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '폴더그룹ID',
    CAT_ID                           VARCHAR (30) COMMENT '유형ID',
    FLD_NM                           VARCHAR (120) NOT NULL COMMENT '폴더명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    FLD_PID                          VARCHAR (30) NOT NULL COMMENT '상위폴더ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    FLD_TYP_CD                       VARCHAR (30) COMMENT '폴더구분코드',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, FLD_ID)
)
COMMENT = '폴더기본';


CREATE TABLE DM_ITEM_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    ITEM_ID                          VARCHAR (30) NOT NULL COMMENT '항목ID',
    ITEM_NM                          VARCHAR (120) COMMENT '항목명',
    ITEM_DISP_NM                     VARCHAR (120) COMMENT '항목표시명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    DATA_TYP                         VARCHAR (10) COMMENT '데이터타입',
    COLM_LEN                         INT COMMENT '길이',
    ITEM_TYP                         VARCHAR (30) COMMENT '항목구분',
    ITEM_TYP_VA                      VARCHAR (30) COMMENT '항목구분값',
    REG_DISP_YN                      CHAR (1) COMMENT '등록표시여부',
    MOD_DISP_YN                      CHAR (1) COMMENT '수정표시여부',
    READ_DISP_YN                     CHAR (1) COMMENT '조회표시여부',
    LIST_DISP_YN                     CHAR (1) COMMENT '목록표시여부',
    LIST_DISP_ORDR                   INT COMMENT '목록표시순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    ADD_ITEM_YN                      CHAR (1) COMMENT '추가항목여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID, ITEM_ID)
)
COMMENT = '항목기본';


CREATE TABLE DM_ITEM_DISP_D (
    ITEM_ID                          VARCHAR (30) NOT NULL COMMENT '항목ID',
    ITEM_TYP_CD                      CHAR (1) NOT NULL COMMENT '항목구분코드',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    ATRB_ID                          VARCHAR (30) COMMENT '속성ID',
    WDTH_PERC                        VARCHAR (30) COMMENT '넓이퍼센트',
    ALN_VA                           VARCHAR (30) COMMENT '줄맞춤값',
    SORT_OPT_VA                      VARCHAR (30) COMMENT '정렬옵션값',
    DATA_SORT_VA                     VARCHAR (30) COMMENT '데이터정렬값',
    READ_DISP_YN                     CHAR (1) COMMENT '조회표시여부',
    LIST_DISP_YN                     CHAR (1) COMMENT '목록표시여부',
    LIST_DISP_ORDR                   INT COMMENT '목록표시순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (ITEM_ID)
)
COMMENT = '항목표시상세';


CREATE TABLE DM_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE DM_PSN_DOC_L (
    DOC_ID                           BIGINT NOT NULL COMMENT '문서ID',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    FLD_ID                           VARCHAR (30) COMMENT '폴더ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    CONT                             LONGTEXT COMMENT '내용',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    C1                               VARCHAR (120) COMMENT '컬럼1',
    C2                               VARCHAR (120) COMMENT '컬럼2',
    C3                               VARCHAR (2000) COMMENT '컬럼3',
    C4                               VARCHAR (2000) COMMENT '컬럼4',
    C5                               VARCHAR (14) COMMENT '컬럼5',
    C6                               VARCHAR (14) COMMENT '컬럼6',
    C7                               VARCHAR (10) COMMENT '컬럼7',
    C8                               VARCHAR (10) COMMENT '컬럼8',
    C9                               VARCHAR (30) COMMENT '컬럼9',
    CA                               VARCHAR (30) COMMENT '컬럼10',
    PRIMARY KEY (DOC_ID)
)
COMMENT = '개인문서기본';


CREATE TABLE DM_PUB_DOC_TGT_D (
    DOC_GRP_ID                       BIGINT NOT NULL COMMENT '문서그룹ID',
    TGT_ID                           VARCHAR (30) NOT NULL COMMENT '대상ID',
    TGT_TYP_CD                       CHAR (1) NOT NULL COMMENT '대상구분코드',
    STAT_CD                          VARCHAR (30) COMMENT '상태코드',
    READ_STRT_DT                     DATETIME COMMENT '열람시작일시',
    READ_END_DT                      DATETIME COMMENT '열람종료일시',
    RJT_OPIN                         VARCHAR (400) COMMENT '반려의견',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (DOC_GRP_ID, TGT_ID)
)
COMMENT = '공용문서대상상세';


CREATE TABLE DM_RESC_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (STOR_ID, RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE DM_STOR_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    TBL_NM                           VARCHAR (120) NOT NULL COMMENT '테이블명',
    STOR_NM                          VARCHAR (120) COMMENT '저장소명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    DFT_YN                           CHAR (1) NOT NULL COMMENT '기본여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (STOR_ID)
)
COMMENT = '저장소기본';


CREATE TABLE DM_STOR_DOC_R (
    DOC_GRP_ID                       BIGINT NOT NULL COMMENT '문서그룹ID',
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    PRIMARY KEY (DOC_GRP_ID)
)
COMMENT = '저장소문서관계';


CREATE TABLE DM_SUBM_L (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    DOC_GRP_ID                       BIGINT NOT NULL COMMENT '문서그룹ID',
    STAT_CD                          VARCHAR (30) COMMENT '상태코드',
    RJT_OPIN                         VARCHAR (400) COMMENT '반려의견',
    DISCR_UID                        VARCHAR (30) COMMENT '심의자UID',
    DISC_DT                          DATETIME COMMENT '심의일시',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (COMP_ID, DOC_GRP_ID)
)
COMMENT = '상신목록';


CREATE TABLE DM_TAKOVR_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    DOC_GRP_ID                       BIGINT NOT NULL COMMENT '문서그룹ID',
    TAK_ORG_ID                       VARCHAR (30) NOT NULL COMMENT '인계조직ID',
    TGT_ORG_ID                       VARCHAR (30) NOT NULL COMMENT '대상조직ID',
    COMP_YN                          CHAR (1) NOT NULL COMMENT '회사여부',
    TAK_STAT_CD                      CHAR (1) NOT NULL COMMENT '인계상태코드',
    TAK_REGR_UID                     VARCHAR (30) NOT NULL COMMENT '인계등록자UID',
    TAK_REG_DT                       DATETIME COMMENT '인계등록일시',
    PRIMARY KEY (STOR_ID, DOC_GRP_ID)
)
COMMENT = '인수인계기본';


CREATE TABLE DM_TRAN_B (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    STOR_RESC_ID                     VARCHAR (30) COMMENT '저장소리소스ID',
    TRAN_TGT_STRT_YMD                DATETIME COMMENT '이관대상시작년월일',
    TRAN_TGT_END_YMD                 DATETIME COMMENT '이관대상종료년월일',
    DOC_TGT_CNT                      BIGINT COMMENT '문서대상건수',
    TRAN_STRT_DT                     DATETIME COMMENT '이관시작일시',
    TRAN_END_DT                      DATETIME COMMENT '이관종료일시',
    TRAN_PROC_STAT_CD                VARCHAR (30) COMMENT '이관진행상태코드',
    DEFRAG_YN                        CHAR (1) COMMENT '조각모음여부',
    USER_UID                         VARCHAR (30) COMMENT '사용자UID',
    PRIMARY KEY (TRAN_ID)
)
COMMENT = '이관기본';


CREATE TABLE DM_TRAN_PROC_L (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    SEQ                              INT NOT NULL COMMENT '순번',
    TRAN_CONT                        VARCHAR (1000) COMMENT '이관내용',
    ALL_CNT                          BIGINT COMMENT '전체건수',
    PROC_CNT                         BIGINT COMMENT '진행건수',
    STRT_DT                          DATETIME COMMENT '시작일시',
    END_DT                           DATETIME COMMENT '종류일시',
    ERR_CONT                         VARCHAR (1000) COMMENT '오류내용',
    ERR_YN                           CHAR (1) COMMENT '오류여부',
    PRIMARY KEY (TRAN_ID, SEQ)
)
COMMENT = '이관진행내역';


CREATE TABLE DM_TRAN_TGT_T (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    TRAN_SEQ                         BIGINT NOT NULL COMMENT '이관일련번호',
    DOC_GRP_ID                       BIGINT NOT NULL COMMENT '문서그룹ID',
    PRIMARY KEY (TRAN_ID, TRAN_SEQ)
)
COMMENT = '이관대상임시';
