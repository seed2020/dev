

/* ----------------------------
    BA
---------------------------- */


CREATE TABLE BA_BRD_B (
	BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',    
    BRD_NM                           VARCHAR (120) COMMENT '게시판명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    BRD_DESC                         VARCHAR (400) COMMENT '설명',
    DEPT_BRD_YN                      CHAR (1) COMMENT '부서게시판여부',
    PHOTO_YN                         CHAR (1) COMMENT '포토게시판여부',
    TBL_ID                           VARCHAR (30) COMMENT '테이블ID',
    CAT_YN                           CHAR (1) COMMENT '카테고리사용여부',
    CAT_GRP_ID                       VARCHAR (30) COMMENT '카테고리그룹ID',
    BRD_TYP_CD                       CHAR (1) COMMENT '게시판타입코드',
    REPLY_YN                         CHAR (1) COMMENT '답변형여부',
    DISC_YN                          CHAR (1) COMMENT '심의여부',
    DISCR_UID                        VARCHAR (30) COMMENT '심의자UID',
    KND_CD                           CHAR (1) COMMENT '게시판종류코드',
    REZV_PRD                         INT COMMENT '게시예약기간',
    BODY_SIZE_LIM                    INT COMMENT '본문사이즈제한',
    ATT_SIZE_LIM                     INT COMMENT '첨부사이즈제한',
    ATT_CAPA_LIM                     INT COMMENT '첨부용량제한',
    NEW_DISP_YN                      CHAR (1) COMMENT '신규게시물표시여부',
    NEW_DISP_PRD                     INT COMMENT '신규게시물표시기간',
    PREV_NEXT_YN                     CHAR (1) COMMENT '이전다음표시여부',
    CMT_YN                           CHAR (1) COMMENT '한줄답변사용여부',
    FAVOT_YN                         CHAR (1) COMMENT '찬반투표사용여부',
    PICH_DISP_YN                     CHAR (1) COMMENT '담당자표시여부',
    PICH_UID                         VARCHAR (30) COMMENT '담당자UID',
    READ_HST_USE_YN                  CHAR (1) COMMENT '조회이력사용여부',
    RECMD_USE_YN                     CHAR (1) COMMENT '추천사용여부',
    SCRE_USE_YN                      CHAR (1) COMMENT '점수사용여부',
    LAST_BULL_YN                     CHAR (1) COMMENT '최근게시물여부',
    NOTC_DISP_PRD                    INT COMMENT '공지표시기간',
    ALL_COMP_YN                      CHAR (1) COMMENT '전사게시판여부',
    PLT_YN                           CHAR (1) COMMENT '포틀릿여부',
    OPT_VA							VARCHAR (240) COMMENT '옵션값',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',    
    PRIMARY KEY (BRD_ID)
)
COMMENT = '게시판관리';


CREATE TABLE BA_BULL_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    BB_REF_ID                        INT COMMENT '참조ID',
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
COMMENT = '게시물첨부파일';


CREATE TABLE BA_BULL_OPT_D (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    SECU_YN                          CHAR (1) COMMENT '보안여부',
    UGNT_YN                          CHAR (1) COMMENT '긴급여부',
    NOTC_YN                          CHAR (1) COMMENT '공지여부',
    PRIMARY KEY (BULL_ID)
)
COMMENT = '게시옵션';


CREATE TABLE BA_BULL_PHOTO_D (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    IMG_WDTH                         INT COMMENT '이미지폭',
    IMG_HGHT                         INT COMMENT '이미지높이',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID)
)
COMMENT = '게시물사진';


CREATE TABLE BA_BULL_POPUP_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    START_DT                         DATETIME COMMENT '시작일',
    END_DT                           DATETIME COMMENT '종료일',
    WIDTH                            INT COMMENT '팝업넓이',
    DISP_YN                          CHAR (1) COMMENT '표시안함',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일',
    PRIMARY KEY (COMP_ID, BULL_ID, BRD_ID)
)
COMMENT = '팝업설정';


CREATE TABLE BA_BULL_POPUP_DISP_D (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자ID',
    UPDT_DT                          DATETIME COMMENT '처리일',
    PRIMARY KEY (BULL_ID, BRD_ID, USER_UID)
)
COMMENT = '게시판팝업표시상세';


CREATE TABLE BA_BULL_SUBM_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    CONT                             LONGTEXT COMMENT '본문',
    BULL_STAT_CD                     CHAR (1) COMMENT '게시물상태코드',
    BRD_ID_LIST                      VARCHAR (400) COMMENT '게시판ID목록',
    RJT_OPIN                         VARCHAR (400) COMMENT '반려의견',
    DISCR_UID                        VARCHAR (30) COMMENT '심의자UID',
    DISC_DT                          DATETIME COMMENT '심의일시',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BULL_ID, BRD_ID)
)
COMMENT = '게시상신함';


CREATE TABLE BA_BULL_TGT_D (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    TGT_ID                           VARCHAR (30) NOT NULL COMMENT '대상ID',
    TGT_TYP                          CHAR (1) COMMENT '대상구분',
    WITH_SUB_YN                      CHAR (1) COMMENT '하위포함여부',
    PRIMARY KEY (BULL_ID, TGT_ID)
)
COMMENT = '게시대상';


CREATE TABLE BA_CAT_D (
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '카테고리ID',
    CAT_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '카테고리그룹ID',
    CAT_NM                           VARCHAR (120) COMMENT '카테고리명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    DISP_ORDR                        INT COMMENT '표시순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CAT_ID, CAT_GRP_ID)
)
COMMENT = '카테고리';


CREATE TABLE BA_CAT_GRP_B (
    CAT_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '카테고리그룹ID',
    CAT_GRP_NM                       VARCHAR (120) COMMENT '카테고리그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    PRIMARY KEY (CAT_GRP_ID)
)
COMMENT = '카테고리그룹';


CREATE TABLE BA_CD_D (
    CD_GRP_ID                        VARCHAR (30) NOT NULL COMMENT '코드그룹ID',
    CD_ID                            VARCHAR (30) NOT NULL COMMENT '코드ID',
    CD_VA                            VARCHAR (400) COMMENT '코드값',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CD_GRP_ID, CD_ID)
)
COMMENT = '코드상세';


CREATE TABLE BA_CD_GRP_B (
    CD_GRP_ID                        VARCHAR (30) NOT NULL COMMENT '코드그룹ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CD_GRP_NM                        VARCHAR (200) COMMENT '코드그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    GRP_USE_YN                       CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CD_GRP_ID)
)
COMMENT = '코드그룹기본';


CREATE TABLE BA_CMT_D (
    CMT_ID                           BIGINT NOT NULL COMMENT '한줄답변ID',
    BULL_ID                          INT COMMENT '게시물ID',
    CMT                              VARCHAR (1000) COMMENT '한줄답변',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (CMT_ID)
)
COMMENT = '한줄답변';


CREATE TABLE BA_COLM_DISP_D (
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    COLM_ID                          VARCHAR (30) NOT NULL COMMENT '컬럼ID',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REG_DISP_YN                      CHAR (1) COMMENT '등록표시여부',
    MOD_DISP_YN                      CHAR (1) COMMENT '수정표시여부',
    READ_DISP_YN                     CHAR (1) COMMENT '조회표시여부',
    LIST_DISP_YN                     CHAR (1) COMMENT '목록표시여부',
    LIST_DISP_ORDR                   INT COMMENT '목록표시순서',
    WDTH_PERC 				VARCHAR(30)	COMMENT '넓이퍼센트',
	ALN_VA 						VARCHAR(30)	COMMENT '줄맞춤값',
	SORT_OPT_VA 			VARCHAR(30)	COMMENT '정렬옵션값',
	DATA_SORT_VA 			VARCHAR(30)	COMMENT '데이터정렬값',
	READ_DISP_ORDR 		INT	COMMENT '조회표시순서',
	COLS_WDTH_VA			VARCHAR(30)	COMMENT '병합넒이값',
	TITL_COL_YN			CHAR (1) COMMENT '대표컬럼여부',
	MANDATORY_YN			CHAR (1) COMMENT '필수여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BRD_ID, COLM_ID)
)
COMMENT = '컬럼표시여부';


CREATE TABLE BA_FAVOT_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    FAVOT_VA                         CHAR (1) COMMENT '찬반투표값',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '찬반투표이력';


CREATE TABLE BA_FORM_B (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    FORM_NM                          VARCHAR (120) NOT NULL COMMENT '양식명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    FORM_URL                         VARCHAR (1000) NOT NULL COMMENT '양식URL',
    TBL_NM                           VARCHAR (60) NOT NULL COMMENT '테이블명',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (FORM_ID, REGR_UID)
)
COMMENT = '양식';


CREATE TABLE BA_MY_BULL_M (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    PRIMARY KEY (USER_UID, BRD_ID)
)
COMMENT = '나의게시물설정';


CREATE TABLE BA_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE BA_POP_USER_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PRIMARY KEY (COMP_ID, USER_UID)
)
COMMENT = '팝업사용자';


CREATE TABLE BA_READ_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    READ_DT                          DATETIME COMMENT '조회일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '조회이력';


CREATE TABLE BA_RECMD_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '추천이력';


CREATE TABLE BA_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE BA_REZV_SAVE_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    BULL_REZV_DT                     DATETIME COMMENT '게시예약일시',
    CONT                             LONGTEXT COMMENT '본문',
    BRD_ID_LIST                      VARCHAR (400) COMMENT '게시판ID목록',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BULL_ID, BRD_ID)
)
COMMENT = '게시물 예약저장';


CREATE TABLE BA_SCRE_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SCRE                             INT COMMENT '점수',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '점수주기이력';


CREATE TABLE BA_SETUP_B (
    DDLN                             INT COMMENT '기한',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시'
)
COMMENT = '설정';


CREATE TABLE BA_SNS_R (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    ATRB_ID                          VARCHAR (30) NOT NULL COMMENT '속성ID',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    SORT_ORDR                           INT COMMENT '정렬순서',
    PRIMARY KEY (BULL_ID, BRD_ID, ATRB_ID)
)
COMMENT = 'SNS관계';


CREATE TABLE BA_TBL_B (
    TBL_ID                           VARCHAR (30) NOT NULL COMMENT '테이블ID',
    TBL_NM                           VARCHAR (120) COMMENT '테이블명',
    TBL_DISP_NM                      VARCHAR (120) COMMENT '테이블표시명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    EX_YN                            CHAR (1) COMMENT '확장여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    PRIMARY KEY (TBL_ID)
)
COMMENT = '테이블관리';


CREATE TABLE BA_TBL_COLM_D (
    COLM_ID                          VARCHAR (30) NOT NULL COMMENT '컬럼ID',
    TBL_ID                           VARCHAR (30) COMMENT '테이블ID',
    COLM_NM                          VARCHAR (120) COMMENT '컬럼명',
    COLM_DISP_NM                     VARCHAR (120) COMMENT '컬럼표시명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    DATA_TYP                         VARCHAR (10) COMMENT '데이터타입',
    COLM_LEN                         INT COMMENT '길이',
    EX_COLM_YN                       CHAR (1) COMMENT '확장컬럼여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    COLM_TYP                         VARCHAR (30) COMMENT '컬럼구분',
    COLM_TYP_VAL                     VARCHAR (30) COMMENT '컬럼구분값',
    MOD_DISP_YN                      CHAR (1) COMMENT '수정표시여부',
    READ_DISP_YN                     CHAR (1) COMMENT '조회표시여부',
    LIST_DISP_YN                     CHAR (1) COMMENT '목록표시여부',
    REG_DISP_YN                      CHAR (1) COMMENT '등록표시여부',
    PRIMARY KEY (COLM_ID)
)
COMMENT = '테이블컬럼';


CREATE TABLE BA_TMP_SAVE_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    BRD_ID                           VARCHAR (30) NOT NULL COMMENT '게시판ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    CONT                             LONGTEXT COMMENT '본문',
    BRD_ID_LIST                      VARCHAR (400) COMMENT '게시판ID목록',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BULL_ID, BRD_ID)
)
COMMENT = '게시물 임시저장';


CREATE TABLE BA_USER_SETUP_B (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    DDLN                             INT COMMENT '기한',
    PRIMARY KEY (USER_UID)
)
COMMENT = '사용자설정';


/* ----------------------------
    CT
---------------------------- */


CREATE TABLE CT_ADM_NOTC_B (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    BULL_PID                         INT COMMENT '부모게시물ID',
    REPLY_GRP_ID                     INT COMMENT '답변그룹ID',
    REPLY_ORDR                       INT COMMENT '답변순서',
    REPLY_DPTH                       INT COMMENT '답변단계',
    BULL_STAT_CD                     CHAR (1) COMMENT '게시물상태코드',
    BULL_REZV_YN                     CHAR (1) COMMENT '게시물예약여부',
    TGT_DEPT_YN                      CHAR (1) COMMENT '대상부서지정여부',
    TGT_USER_YN                      CHAR (1) COMMENT '대상사용자지정여부',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    BULL_REZV_DT                     DATETIME COMMENT '게시예약일시',
    BULL_EXPR_DT                     DATETIME COMMENT '게시완료일시',
    CONT                             LONGTEXT COMMENT '본문',
    READ_CNT                         INT COMMENT '조회수',
    PROS_CNT                         INT COMMENT '찬성수',
    CONS_CNT                         INT COMMENT '반대수',
    RECMD_CNT                        INT COMMENT '추천수',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    PRIMARY KEY (BULL_ID, COMP_ID)
)
COMMENT = '관리자 커뮤니티 공지 기본';


CREATE TABLE CT_BLON_CAT_D (
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '카테고리ID',
    PRIMARY KEY (CT_ID, COMP_ID)
)
COMMENT = '커뮤니티 소속 카테고리 상세';


CREATE TABLE CT_BULL_MAST_B (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    BULL_PID                         INT COMMENT '부모게시물ID',
    REPLY_GRP_ID                     INT COMMENT '답변그룹ID',
    REPLY_ORDR                       INT COMMENT '답변순서',
    REPLY_DPTH                       INT COMMENT '답변단계',
    BULL_STAT_CD                     CHAR (1) COMMENT '게시물상태코드',
    BULL_REZV_YN                     CHAR (1) COMMENT '게시물예약여부',
    TGT_DEPT_YN                      CHAR (1) COMMENT '대상부서지정여부',
    TGT_USER_YN                      CHAR (1) COMMENT '대상사용자지정여부',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    BULL_REZV_DT                     DATETIME COMMENT '게시예약일시',
    BULL_EXPR_DT                     DATETIME COMMENT '게시완료일시',
    CONT                             LONGTEXT COMMENT '본문',
    READ_CNT                         INT COMMENT '조회수',
    PROS_CNT                         INT COMMENT '찬성수',
    CONS_CNT                         INT COMMENT '반대수',
    RECMD_CNT                        INT COMMENT '추천수',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CT_FNC_ID                        VARCHAR (30) COMMENT '커뮤니티기능ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티기능순서',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '기능 위치 단계',
    CT_FNC_UID                       VARCHAR (30) COMMENT '커뮤니티 기능 UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티 기능 부모ID',
    SCRE                             INT COMMENT '컬럼설명',
    PRIMARY KEY (BULL_ID)
)
COMMENT = '커뮤니티 게시판 마스터 기본';


CREATE TABLE CT_BULL_MAST_CMD_D (
    CMT_ID                           BIGINT NOT NULL COMMENT '한줄답변ID',
    BULL_ID                          INT COMMENT '게시물ID',
    CMT                              VARCHAR (1000) COMMENT '한줄답변',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (CMT_ID)
)
COMMENT = '게시판 마스터 한줄 답변 상세';


CREATE TABLE CT_CAT_B (
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '카테고리 ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CAT_SUBJ_RESC_ID                 VARCHAR (30) COMMENT '카테고리 제목 리소스 ID',
    CAT_PID                          VARCHAR (30) COMMENT '카테고리 부모 ID',
    CAT_LOC_STEP                     INT COMMENT '카테고리 위치 단계',
    CHLD_YN                          CHAR (1) COMMENT '자식여부',
    EXTN_OPEN_YN                     CHAR (1) COMMENT '외부공개여부',
    FLD_TYP_CD                       VARCHAR (30) COMMENT '폴더분류구분코드',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    CAT_ORDR                         INT COMMENT '카테고리순서',
    PRIMARY KEY (CAT_ID, COMP_ID)
)
COMMENT = '커뮤니티 카테고리 기본';


CREATE TABLE CT_DEBR_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CT_FNC_ID                        VARCHAR (30) NOT NULL COMMENT '커뮤니티기능ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티 기능 순서',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    DEBR_ID                          VARCHAR (30) NOT NULL COMMENT '컬럼설명',
    ESTB_ITNT                        VARCHAR (1000) COMMENT '컬럼설명',
    SITN                             INT COMMENT '컬럼설명',
    FIN_YN                           CHAR (1) COMMENT '컬럼설명',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '기능 위치 단계',
    CT_FNC_UID                       VARCHAR (30) COMMENT '커뮤니티 기능 UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티 기능 부모ID',
    PRIMARY KEY (COMP_ID, CT_ID, DEBR_ID)
)
COMMENT = '토론실';


CREATE TABLE CT_DEBR_OPIN_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티 ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    OPIN                             LONGTEXT COMMENT '의견',
    READ_CNT                         INT COMMENT '조회수',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일자',
    MOD_DT                           DATETIME COMMENT '수정일자',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자 UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자 UID',
    DEBR_ID                          VARCHAR (30) NOT NULL COMMENT '토론실 ID',
    OPIN_ORDR                        INT NOT NULL COMMENT '의견순서',
    PROS_CONS_CD                     CHAR (1) COMMENT '찬성/반대',
    PRIMARY KEY (COMP_ID, CT_ID, DEBR_ID, OPIN_ORDR)
)
COMMENT = '커뮤니티 토론실 의견 상세';


CREATE TABLE CT_ESTB_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티 ID',
    MAST_UID                         VARCHAR (30) COMMENT '마스터 UID',
    CT_SUBJ_RESC                     VARCHAR (30) COMMENT '커뮤니티 제목 리소스 ID',
    CT_ITRO                          LONGTEXT COMMENT '커뮤니티 소개',
    CT_KWD                           VARCHAR (30) COMMENT '커뮤니티 키워드',
    MNG_TGT_YN                       VARCHAR (1) COMMENT '관리대상 여부',
    JOIN_MET                         VARCHAR (1) COMMENT '가입방법',
    DFT_AUTH                         VARCHAR (1) COMMENT '기본권한',
    EXTN_OPEN_YN                     CHAR (1) COMMENT '외부 공개여부',
    ATT_LIM_SIZE                     INT COMMENT '첨부제한용량',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    MBSH_CNT                         INT COMMENT '회원수',
    CT_STAT                          VARCHAR (1) COMMENT '커뮤니티 상태',
    CT_APVD_DT                       DATETIME COMMENT '커뮤니티 승인일시',
    RJT_OPIN_CONT                    VARCHAR (255) COMMENT '반려의견내용',
    CT_ACT_STAT                      VARCHAR (1) COMMENT '커뮤니티 활동 상태',
    CT_END_CONT                      VARCHAR (255) COMMENT '커뮤니티 종료 내용',
    RECMD_YN                         CHAR (1) COMMENT '추천여부',
    RECMD_DT                         DATETIME COMMENT '추천일자',
    CT_EVAL_CD                       VARCHAR (30) COMMENT '커뮤니티 평가코드',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MODIFY_YN                        CHAR (1) COMMENT '컬럼설명',
    CLS_REQS_YN                      CHAR (1) COMMENT '컬럼설명',
    ALL_COMP_YN                      CHAR (1) COMMENT '전사여부',
    PRIMARY KEY (COMP_ID, CT_ID)
)
COMMENT = '커뮤니티 개설 기본';


CREATE TABLE CT_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    REF_ID                           VARCHAR (30) COMMENT '참조ID',
    DISP_NM                          VARCHAR (240) COMMENT '표시이름',
    DISP_ORDR                        INT COMMENT '표시순서',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    CT_ID                            VARCHAR (30) COMMENT '커뮤니티ID',
    CT_FNC_UID                       VARCHAR (30) COMMENT '커뮤니티 기능 UID',
    PRIMARY KEY (FILE_ID)
)
COMMENT = '첨부파일상세';


CREATE TABLE CT_FNC_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_FNC_UID                       VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 UID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티 ID',
    CT_FNC_ID                        VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 ID',
    CT_FNC_PID                       VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 부모 ID',
    CT_FNC_ORDR                      INT NOT NULL COMMENT '커뮤니티 기능 순서',
    CT_FNC_LOC_STEP                  VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 위치 단계',
    CT_FNC_TYP                       VARCHAR (1) COMMENT '커뮤니티 기능 구분',
    CT_FNC_SUBJ_RESC_ID              VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 제목 리소스 ID',
    ORDR                             INT COMMENT '순서',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자 UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자 UID',
    PRIMARY KEY (COMP_ID, CT_FNC_UID)
)
COMMENT = '커뮤니티 기능 상세';


CREATE TABLE CT_FNC_MBSH_AUTH_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_FNC_UID                       VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 UID',
    SECUL_CD                         VARCHAR (30) NOT NULL COMMENT '보안등급코드',
    AUTH_CD                          VARCHAR (30) COMMENT '권한코드',
    PRIMARY KEY (COMP_ID, CT_FNC_UID, SECUL_CD)
)
COMMENT = '커뮤니티 기능 회원 권한 상세';


CREATE TABLE CT_FNC_MNG_B (
    CT_FNC_ID                        VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 ID',
    CT_FNC_SUBJ_RESC_ID              VARCHAR (30) COMMENT '커뮤니티 기능 제목 리소스 ID',
    CT_FNC_TYP                       VARCHAR (1) COMMENT '커뮤니티 기능 구분',
    URL                              VARCHAR (1000) COMMENT 'URL',
    REL_TBL_SUBJ                     VARCHAR (200) COMMENT '관계 테이블 제목',
    DFT_YN                           CHAR (1) COMMENT '기본여부',
    MUL_CHOI_YN                      CHAR (1) COMMENT '멀티 선택 여부',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    CT_MNG_YN                        CHAR (1) COMMENT '커뮤니티 메뉴 여부',
    PT_URL                           VARCHAR (1000) COMMENT '컬럼설명',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    PRIMARY KEY (CT_FNC_ID)
)
COMMENT = '커뮤니티 기능 관리 기본';


CREATE TABLE CT_MBSH_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티 ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자 UID',
    USER_SECUL_CD                    VARCHAR (30) COMMENT '사용자 보안등급 코드',
    JOIN_DT                          DATETIME COMMENT '가입일시',
    JOIN_STAT                        VARCHAR (1) COMMENT '가입상태',
    PRIMARY KEY (COMP_ID, CT_ID, USER_UID)
)
COMMENT = '커뮤니티 회원 상세';


CREATE TABLE CT_PLT_CM_LIST_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE CT_PLT_FNC_LIST_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE CT_PLT_NOPR_LIST_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE CT_PLT_SETUP_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CT_FNC_ID                        VARCHAR (30) NOT NULL COMMENT '커뮤니티기능ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    PERD                             INT COMMENT '컬럼설명',
    CT_FUNC_SUBJ_RESC_ID             VARCHAR (30) COMMENT '컬럼설명'
)
COMMENT = '환경설정';


CREATE TABLE CT_POLC_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME NOT NULL COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) COMMENT '등록자 UID',
    MODR_UID                         VARCHAR (30) COMMENT '수정자 UID',
    APVD_YN                          CHAR (1) COMMENT '승인여부',
    PRIMARY KEY (COMP_ID)
)
COMMENT = '커뮤니티 정책 상세';


CREATE TABLE CT_PR_B (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    BULL_PID                         INT COMMENT '부모게시물ID',
    REPLY_GRP_ID                     INT COMMENT '답변그룹ID',
    REPLY_ORDR                       INT COMMENT '답변순서',
    REPLY_DPTH                       INT COMMENT '답변단계',
    BULL_STAT_CD                     CHAR (1) COMMENT '게시물상태코드',
    BULL_REZV_YN                     CHAR (1) COMMENT '게시물예약여부',
    TGT_DEPT_YN                      CHAR (1) COMMENT '대상부서지정여부',
    TGT_USER_YN                      CHAR (1) COMMENT '대상사용자지정여부',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    BULL_REZV_DT                     DATETIME COMMENT '게시예약일시',
    BULL_EXPR_DT                     DATETIME COMMENT '게시완료일시',
    CONT                             LONGTEXT COMMENT '본문',
    READ_CNT                         INT COMMENT '조회수',
    PROS_CNT                         INT COMMENT '찬성수',
    CONS_CNT                         INT COMMENT '반대수',
    RECMD_CNT                        INT COMMENT '추천수',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CT_SUBJ_RESC_ID                  VARCHAR (50) COMMENT '커뮤니티제목리소스ID',
    CT_ITRO                          VARCHAR (500) COMMENT '커뮤니티소개',
    PRIMARY KEY (BULL_ID, COMP_ID)
)
COMMENT = '커뮤니티 홍보마당 게시물 기본';


CREATE TABLE CT_QUES_EXAM_D (
    EXAM_NO                          INT NOT NULL COMMENT '보기번호',
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    EXAM_ORDR                        INT COMMENT '보기순서',
    EXAM_DISP_NM                     VARCHAR (256) COMMENT '보기표시명',
    INPUT_YN                         CHAR (1) COMMENT '입력 여부',
    EXAM_IMG_USE_YN                  CHAR (1) COMMENT '보기이미지사용여부',
    IMG_SURV_FILE_ID                 VARCHAR (30) COMMENT '이미지설문파일ID',
    PRIMARY KEY (EXAM_NO, QUES_ID, COMP_ID, SURV_ID)
)
COMMENT = '질문보기상세';


CREATE TABLE CT_REC_MAST_B (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    BULL_PID                         INT COMMENT '부모게시물ID',
    REPLY_GRP_ID                     INT COMMENT '답변그룹ID',
    REPLY_ORDR                       INT COMMENT '답변순서',
    REPLY_DPTH                       INT COMMENT '답변단계',
    BULL_STAT_CD                     CHAR (1) COMMENT '게시물상태코드',
    BULL_REZV_YN                     CHAR (1) COMMENT '게시물예약여부',
    TGT_DEPT_YN                      CHAR (1) COMMENT '대상부서지정여부',
    TGT_USER_YN                      CHAR (1) COMMENT '대상사용자지정여부',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    BULL_REZV_DT                     DATETIME COMMENT '게시예약일시',
    BULL_EXPR_DT                     DATETIME COMMENT '게시완료일시',
    CONT                             LONGTEXT COMMENT '본문',
    READ_CNT                         INT COMMENT '조회수',
    PROS_CNT                         INT COMMENT '찬성수',
    CONS_CNT                         INT COMMENT '반대수',
    RECMD_CNT                        INT COMMENT '추천수',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CT_FNC_ID                        VARCHAR (30) COMMENT '커뮤니티기능ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티기능순서',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '기능 위치 단계',
    CT_FNC_UID                       VARCHAR (30) COMMENT '커뮤니티 기능 UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티 기능 부모ID',
    PRIMARY KEY (BULL_ID)
)
COMMENT = '커뮤니티 게시판 마스터 기본';


CREATE TABLE CT_REC_MAST_CMD_D (
    CMT_ID                           BIGINT NOT NULL COMMENT '한줄답변ID',
    BULL_ID                          INT COMMENT '게시물ID',
    CMT                              VARCHAR (1000) COMMENT '한줄답변',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (CMT_ID)
)
COMMENT = '게시판 마스터 한줄 답변 상세';


CREATE TABLE CT_RECMD_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '추천이력';


CREATE TABLE CT_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE CT_RESC_D (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE CT_SCHDL_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SCHDL_TYP_CD                     VARCHAR (30) NOT NULL COMMENT '일정구분코드',
    SUBJ                             VARCHAR (240) NOT NULL COMMENT '제목',
    LOC_NM                           VARCHAR (60) COMMENT '장소명',
    OPEN_GRAD_CD                     VARCHAR (30) COMMENT '공개등급코드',
    SCHDL_STAT_CD                    VARCHAR (30) COMMENT '일정상태코드',
    WORK_PRIO_ORDR                   VARCHAR (1) COMMENT '할일우선순서',
    WORK_CMLT_YMD                    DATETIME COMMENT '할일완료년월일',
    CONT                             LONGTEXT COMMENT '내용',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    SELF_REG_YN                      CHAR (1) COMMENT '본인입력여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    EDITOR_TYP_CD                    VARCHAR (30) COMMENT '에디터구분코드',
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    SCHDL_FILE_ID                    VARCHAR (30) COMMENT '일정첨부파일ID',
    SCHDL_START_DT                   DATETIME NOT NULL COMMENT '일정시작일시',
    SCHDL_END_DT                     DATETIME NOT NULL COMMENT '일정종료일시',
    REPET_YN                         CHAR (1) COMMENT '반복여부',
    SCHDL_PID                        VARCHAR (30) COMMENT '일정 부모ID',
    SCHDL_KND_CD                     VARCHAR (30) COMMENT '일정종류코드',
    GRP_ID                           VARCHAR (30) COMMENT '그룹ID',
    DEPT_ID                          VARCHAR (30) COMMENT '부서ID',
    HOLI_YN                          CHAR (1) NOT NULL COMMENT '휴일여부',
    SOLA_LUNA_YN                     CHAR (1) NOT NULL COMMENT '양음력여부',
    LOC_LATD_LOTD_VA                 VARCHAR (20) COMMENT '장소위도경도값',
    REGR_NM                          VARCHAR (120) COMMENT '등록자명',
    ALLDAY_YN                        CHAR (1) COMMENT '종일여부',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CT_FNC_ID                        VARCHAR (30) COMMENT '커뮤니티기능ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티 기능 순서',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '기능 위치 단계',
    CT_FNC_UID                       VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티 기능 부모ID',
    PRIMARY KEY (SCHDL_ID)
)
COMMENT = '일정';


CREATE TABLE CT_SCHDL_CAT_CLS_B (
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '카테고리ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    BGCOL_CD                         VARCHAR (10) COMMENT '배경색코드',
    FONT_COLR_CD                     VARCHAR (10) COMMENT '글자색코드',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    CAT_NM                           VARCHAR (120) COMMENT '카테고리명',
    PRIMARY KEY (CAT_ID, COMP_ID)
)
COMMENT = '카테고리분류기본';


CREATE TABLE CT_SCHDL_PROM_GUEST_D (
    GUEST_UID                        VARCHAR (30) NOT NULL COMMENT '참석자 ID',
    GUEST_NM                         VARCHAR (20) NOT NULL COMMENT '참석자 이름',
    GUEST_POSIT_NM                   VARCHAR (100) COMMENT '참석자 직위명',
    GUEST_DEPT_NM                    VARCHAR (100) COMMENT '참석자 부서명',
    GUEST_COMP_NM                    VARCHAR (100) COMMENT '참석자 회사명',
    STAT_CD                          VARCHAR (30) COMMENT '상태 코드',
    OPIN_CONT                        VARCHAR (255) COMMENT '의견내용',
    GUEST_EMPL_YN                    VARCHAR (30) COMMENT '참석자_임직원 여부',
    EMAIL                            VARCHAR (240) COMMENT '이메일',
    EMAIL_SEND_YN                    CHAR (1) COMMENT '이메일발송여부',
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    PRIMARY KEY (GUEST_UID, SCHDL_ID)
)
COMMENT = '약속 참석자 상세';


CREATE TABLE CT_SCHDL_REPET_SETUP_D (
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    REPET_PERD_CD                    VARCHAR (30) NOT NULL COMMENT '반복주기코드',
    APNT_MM                          VARCHAR (4) COMMENT '지정월',
    REPET_MM                         VARCHAR (4) COMMENT '반복월',
    APNT_WK                          VARCHAR (4) COMMENT '지정주',
    REPET_WK                         VARCHAR (4) COMMENT '반복주',
    APNT_DD                          VARCHAR (4) COMMENT '지정일',
    REPET_DD                         VARCHAR (4) COMMENT '반복일',
    APNT_DY                          VARCHAR (30) COMMENT '지정요일',
    REPET_START_DT                   DATETIME COMMENT '반복시작일시',
    REPET_END_DT                     DATETIME COMMENT '반복종료일시',
    PRIMARY KEY (SCHDL_ID)
)
COMMENT = '반복설정 상세';


CREATE TABLE CT_SCRE_HST_L (
    BULL_ID                          INT NOT NULL COMMENT '게시물ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SCRE                             INT COMMENT '점수',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (BULL_ID, USER_UID)
)
COMMENT = '점수주기이력';


CREATE TABLE CT_SCRN_SETUP_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    TPL_FILE_SUBJ                    VARCHAR (255) NOT NULL COMMENT '템플릿 파일 제목',
    CT_ITRO                          LONGTEXT COMMENT '커뮤니티 소개',
    LOC_CNT                          INT COMMENT '위치수',
    LOC_1_CT_FNC_ID                  VARCHAR (30) COMMENT '위치1 ID',
    LOC_2_CT_FNC_ID                  VARCHAR (30) COMMENT '위치2 ID',
    LOC_3_CT_FNC_ID                  VARCHAR (30) COMMENT '위치3 ID',
    LOC_4_CT_FNC_ID                  VARCHAR (30) COMMENT '위치4 ID',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME NOT NULL COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) NOT NULL COMMENT '수정자UID',
    EDITOR_TYP_CD                    VARCHAR (30) COMMENT '에디터구분코드',
    LOC_5_CT_FNC_ID                  VARCHAR (30) COMMENT '위치5 ID',
    LOC_6_CT_FNC_ID                  VARCHAR (30) COMMENT '위치6 ID',
    LOC_7_CT_FNC_ID                  VARCHAR (30) COMMENT '위치7 ID',
    LOC_8_CT_FNC_ID                  VARCHAR (30) COMMENT '위치8 ID',
    LOC_9_CT_FNC_ID                  VARCHAR (30) COMMENT '위치9 ID',
    LOC_10_CT_FNC_ID                 VARCHAR (30) COMMENT '위치10 ID',
    LOC_11_CT_FNC_ID                 VARCHAR (30) COMMENT '위치11 ID',
    LOC_12_CT_FNC_ID                 VARCHAR (30) COMMENT '위치12 ID',
    IMG_FILE_ID                      VARCHAR (30) COMMENT '이미지 파일 아이디',
    BGCOL                            VARCHAR (50) COMMENT '배경색'
)
COMMENT = '메인설정';


CREATE TABLE CT_SECUL_CD_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    USER_SECUL_CD                    VARCHAR (30) NOT NULL COMMENT '사용자 보안등급코드',
    USER_SECUL_CD_SUBJ_RESC_ID       VARCHAR (50) NOT NULL COMMENT '사용자 보안등급코드 제목 리소스 ID',
    PRIMARY KEY (COMP_ID, USER_SECUL_CD)
)
COMMENT = '커뮤니티 회원 등급 코드 기본';


CREATE TABLE CT_SITE_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT ' 회사ID ',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    SITE_ID                          VARCHAR (30) NOT NULL COMMENT 'COOL SITE ID',
    CT_FNC_ID                        VARCHAR (30) NOT NULL COMMENT '커뮤니티 기능 ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티 기능순서',
    SUBJ                             VARCHAR (240) COMMENT ' 제목 ',
    REG_DT                           DATETIME NOT NULL COMMENT ' 등록일시 ',
    MOD_DT                           DATETIME COMMENT ' 수정일시 ',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT ' 등록자UID ',
    MODR_UID                         VARCHAR (30) COMMENT ' 수정자UID ',
    CAT_ID                           VARCHAR (30) COMMENT ' 카테고리 ID ',
    URL                              VARCHAR (1000) COMMENT ' URL ',
    CONT                             LONGTEXT COMMENT ' 본문 ',
    READ_CNT                         INT COMMENT ' 조회수 ',
    EDITOR_TYP_CD                    VARCHAR (30) COMMENT ' 에디터 타입 ',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '기능 위치 단계',
    CT_FNC_UID                       VARCHAR (30) COMMENT '커뮤니티 기능 UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티 기능 부모ID',
    PRIMARY KEY (COMP_ID, CT_ID, SITE_ID)
)
COMMENT = '커뮤니티 사이트 기본';


CREATE TABLE CT_SITE_CAT_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT ' 회사ID ',
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT ' 카테고리 ID ',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    CAT_NM_RESC_ID                   VARCHAR (30) COMMENT ' 카테고리명 리소스 ID ',
    REG_DT                           DATETIME NOT NULL COMMENT ' 등록일시 ',
    MOD_DT                           DATETIME COMMENT ' 수정일시 ',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT ' 등록자UID ',
    MODR_UID                         VARCHAR (30) COMMENT ' 수정자UID ',
    PRIMARY KEY (COMP_ID, CAT_ID, CT_ID)
)
COMMENT = '커뮤니티 사이트 카테고리 상세';


CREATE TABLE CT_SURV_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    QUES_CNT                         INT COMMENT '질문 개수',
    SURV_START_DT                    DATETIME COMMENT '설문 시작일시',
    SURV_END_DT                      DATETIME COMMENT '설문 종료일시',
    PLT_ID                           VARCHAR (30) COMMENT '포틀릿ID',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME NOT NULL COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) NOT NULL COMMENT '수정자UID',
    SURV_PRG_STAT_CD                 VARCHAR (1) COMMENT '설문진행상태코드',
    OPEN_YN                          CHAR (1) COMMENT '공개여부',
    REPET_SURV_YN                    CHAR (1) COMMENT '재설문여부',
    SURV_ITNT                        LONGTEXT COMMENT '설문취지',
    SURV_FTR                         LONGTEXT COMMENT '설문바닥글',
    ATT_YN                           CHAR (1) COMMENT '첨부여부',
    ADM_RJT_OPIN_CONT                VARCHAR (2000) COMMENT '관리자반려의견내용',
    ADM_FILE_ID                      VARCHAR (30) COMMENT '설문파일ID',
    CT_FNC_ID                        VARCHAR (30) COMMENT '커뮤니티기능ID',
    CT_FNC_ORDR                      INT COMMENT '커뮤니티기능순서',
    CT_FNC_LOC_STEP                  VARCHAR (30) COMMENT '커뮤니티위치단계',
    CT_FNC_UID                       VARCHAR (30) NOT NULL COMMENT '커뮤니티UID',
    CT_FNC_PID                       VARCHAR (30) COMMENT '커뮤니티PID',
    SURV_TGT_CD                      VARCHAR (30) COMMENT '설문 대상 코드',
    PRIMARY KEY (COMP_ID, SURV_ID)
)
COMMENT = '설문기본';


CREATE TABLE CT_SURV_QUES_D (
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    QUES_SORT_ORDR                   INT COMMENT '질문정렬순서',
    QUES_CONT                        VARCHAR (2000) COMMENT '질문내용',
    QUES_IMG_ATT_YN                  CHAR (1) COMMENT '질문이미지첨부여부',
    IMG_SURV_FILE_ID                 VARCHAR (30) COMMENT '이미지파일ID',
    EXAM_CHOI_ITEM_CD                VARCHAR (30) COMMENT '보기선택항목코드',
    MUL_CHOI_YN                      CHAR (1) COMMENT '복수선택여부',
    MANDA_REPLY_YN                   CHAR (1) COMMENT '필수답변여부',
    PRIMARY KEY (QUES_ID, COMP_ID, SURV_ID)
)
COMMENT = '질문상세';


CREATE TABLE CT_SURV_REPLY_D (
    REPLY_NO                         INT NOT NULL COMMENT '답변번호',
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    REPLYR_UID                       VARCHAR (30) NOT NULL COMMENT '답변자ID',
    REPLYR_DEPT_ID                   VARCHAR (30) COMMENT '답변자 부서 ID',
    REPLY_DT                         DATETIME COMMENT '답변일시',
    MULC_INPUT_REPLY_CONT            VARCHAR (256) COMMENT '객관식입력항목답변내용',
    OEND_REPLY_CONT                  VARCHAR (1000) COMMENT '주관식답변내용',
    PRIMARY KEY (REPLY_NO, QUES_ID, COMP_ID, SURV_ID, REPLYR_UID)
)
COMMENT = '답변상세';


CREATE TABLE CT_VISTR_HST_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사 ID',
    CT_ID                            VARCHAR (30) NOT NULL COMMENT '커뮤니티 ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자 UID',
    ACCS_DT                          DATETIME NOT NULL COMMENT '접속 일시',
    ACCS_TYP_CD                      VARCHAR (30) COMMENT '접속 구분 코드',
    PRIMARY KEY (COMP_ID, CT_ID, USER_UID, ACCS_DT)
)
COMMENT = '커뮤니티 방문자 이력 상세';
