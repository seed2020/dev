

/* ----------------------------
    WA
---------------------------- */


CREATE TABLE WA_CAR_IMG_D (
    CAR_KND_NO                       INT NOT NULL COMMENT '차종번호',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT NOT NULL COMMENT '가로길이',
    IMG_HGHT                         INT NOT NULL COMMENT '세로길이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CAR_KND_NO)
)
COMMENT = '이미지';


CREATE TABLE WA_CAR_KND_L (
    CAR_KND_NO                       INT NOT NULL COMMENT '차종번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CORP_NO                          INT NOT NULL COMMENT '법인번호',
    CAR_KND_NM                       VARCHAR (120) NOT NULL COMMENT '차종명',
    CAR_REG_NO                       VARCHAR (60) NOT NULL COMMENT '자동차등록번호',
    CONT                             VARCHAR (300) COMMENT '내용',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (CAR_KND_NO)
)
COMMENT = '차종목록';


CREATE TABLE WA_CORP_B (
    CORP_NO                          INT NOT NULL COMMENT '법인번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CORP_NM                          VARCHAR (120) NOT NULL COMMENT '법인명',
    CORP_REG_NO                      VARCHAR (60) NOT NULL COMMENT '사업자등록번호',
    PRIMARY KEY (CORP_NO)
)
COMMENT = '법인기본';


CREATE TABLE WA_DRIV_REC_B (
    REC_NO                           BIGINT NOT NULL COMMENT '기록번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CORP_NO                          INT NOT NULL COMMENT '법인번호',
    CAR_KND_NO                       INT NOT NULL COMMENT '차종번호',
    STRT_DT                          DATETIME NOT NULL COMMENT '시작일',
    END_DT                           DATETIME NOT NULL COMMENT '종료일',
    BODY_XML                         LONGTEXT NOT NULL COMMENT '본문XML',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (REC_NO)
)
COMMENT = '운행기록기본';


/* ----------------------------
    WB
---------------------------- */


CREATE TABLE WB_BC_AGNT_ADM_B (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    AGNT_ADM_UID                     VARCHAR (30) NOT NULL COMMENT '대리관리자UID',
    AUTH_CD                          VARCHAR (30) NOT NULL COMMENT '권한코드',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (REGR_UID, AGNT_ADM_UID)
)
COMMENT = '명함대리관리자기본';


CREATE TABLE WB_BC_AGNT_SETUP_B (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    BC_REGR_UID                      VARCHAR (30) NOT NULL COMMENT '명함등록자UID',
    PRIMARY KEY (REGR_UID, BC_REGR_UID)
)
COMMENT = '명함대리설정기본';


CREATE TABLE WB_BC_APNTR_R (
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    BC_APNTR_UID                     VARCHAR (30) NOT NULL COMMENT '명함지정인UID',
    PRIMARY KEY (BC_ID, BC_APNTR_UID)
)
COMMENT = '명함지정인관계';


CREATE TABLE WB_BC_B (
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    OPEN_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '공개구분코드',
    FLD_ID                           VARCHAR (30) COMMENT '폴더ID',
    BC_NM                            VARCHAR (30) NOT NULL COMMENT '명함성명',
    COMP_NM                          VARCHAR (120) COMMENT '회사명',
    DEPT_NM                          VARCHAR (120) COMMENT '부서명',
    GRADE_NM                         VARCHAR (30) COMMENT '직급명',
    TICH_CONT                        VARCHAR (800) COMMENT '담당업무내용',
    IPTFG_YN                         CHAR (1) COMMENT '주요인사여부',
    FNO                              VARCHAR (20) COMMENT '팩스번호',
    COMP_ZIP_NO                      VARCHAR (10) COMMENT '회사_우편번호',
    COMP_ADR                         VARCHAR (240) COMMENT '회사_주소',
    COMP_DETL_ADR                    VARCHAR (240) COMMENT '회사_상세주소',
    HOME_ZIP_NO                      VARCHAR (10) COMMENT '자택우편번호',
    HOME_ADR                         VARCHAR (240) COMMENT '자택주소',
    HOME_DETL_ADR                    VARCHAR (240) COMMENT '자택상세주소',
    BUMK_YN                          CHAR (1) COMMENT '즐겨찾기여부',
    PHOTO_FILE_ID                    VARCHAR (30) COMMENT '사진파일ID',
    GEN_CD                           VARCHAR (30) COMMENT '성별코드',
    NATY_CD                          VARCHAR (30) COMMENT '국적코드',
    BIRTH                            DATETIME COMMENT '생년월일',
    BIRTH_SCLC_CD                    VARCHAR (30) COMMENT '생년월일월력코드',
    WEDD_ANNV                        DATETIME COMMENT '결혼기념일',
    WEDD_ANNV_SCLC_CD                VARCHAR (30) COMMENT '결혼기념일월력코드',
    PSN_HPAGE_URL                    VARCHAR (1000) COMMENT '개인홈페이지URL',
    COMP_HPAGE_URL                   VARCHAR (1000) COMMENT '회사홈페이지URL',
    HOBY_CONT                        VARCHAR (400) COMMENT '취미내용',
    SPECT_CONT                       VARCHAR (400) COMMENT '특기사항내용',
    ESCHL_NM                         VARCHAR (120) COMMENT '초등학교명',
    MSCHL_NM                         VARCHAR (120) COMMENT '중학교명',
    HSCHL_NM                         VARCHAR (120) COMMENT '고등학교명',
    UNIV_CONT                        VARCHAR (240) COMMENT '대학교내용',
    BHIST_CONT                       VARCHAR (400) COMMENT '약력내용',
    CLNS_ID                          VARCHAR (30) COMMENT '친밀도내용',
    NOTE_CONT                        VARCHAR (400) COMMENT '비고',
    ATT_FILE_ID                      VARCHAR (30) COMMENT '첨부파일ID',
    BC_REG_TYP_CD                    VARCHAR (30) COMMENT '명함등록구분코드',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    DEL_YN                           CHAR (1) COMMENT '삭제여부',
    EMAIL                            VARCHAR (240) COMMENT '이메일',
    BC_EN_NM                         VARCHAR (30) COMMENT '명함영문성명',
    DFT_CNTC_TYP_CD                  VARCHAR (30) COMMENT '기본연락처구분코드',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    ORIGINAL_BC_ID                   VARCHAR (30) COMMENT '원본명함ID',
    MAIN_SETUP_YN                    CHAR (1) COMMENT '메인설정여부',
    DUPLICATE_BC_ID                  VARCHAR (30) COMMENT '사본명함ID',
    INITIAL_NM                       VARCHAR (3) COMMENT '초성명',
    PRIMARY KEY (BC_ID)
)
COMMENT = '명함기본';


CREATE TABLE WB_BC_BUMK_R (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (REGR_UID, BC_ID)
)
COMMENT = '명함즐겨찾기관계';


CREATE TABLE WB_BC_CLNS_C (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    CLNS_NM                          VARCHAR (120) NOT NULL COMMENT '친밀도명',
    CLNS_ID                          VARCHAR (30) NOT NULL COMMENT '친밀도ID',
    PRIMARY KEY (REGR_UID, CLNS_ID)
)
COMMENT = '명함친밀도코드';


CREATE TABLE WB_BC_CNTC_D (
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    CNTC_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '연락처구분코드',
    BC_CNTC_SEQ                      INT NOT NULL COMMENT '명함연락처일련번호',
    CNTC_CONT                        VARCHAR (120) NOT NULL COMMENT '연락처내용',
    DFT_CNTC_YN                      CHAR (1) COMMENT '우선순위여부',
    CNTC_CLS_CD                      VARCHAR (10) NOT NULL COMMENT '연락처분류코드',
    PRIMARY KEY (BC_ID, CNTC_TYP_CD, BC_CNTC_SEQ)
)
COMMENT = '명함연락처상세';


CREATE TABLE WB_BC_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    REF_ID                           VARCHAR (30) NOT NULL COMMENT '참조ID',
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
COMMENT = '명함첨부파일';


CREATE TABLE WB_BC_FLD_B (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    BC_FLD_ID                        VARCHAR (30) NOT NULL COMMENT '명함폴더ID',
    FLD_NM                           VARCHAR (120) COMMENT '폴더명',
    ABV_FLD_ID                       VARCHAR (30) COMMENT '상위폴더ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    COMP_ID                          VARCHAR (30) NOT NULL COMMENT '회사ID',
    PRIMARY KEY (REGR_UID, BC_FLD_ID, COMP_ID)
)
COMMENT = '명함폴더기본';


CREATE TABLE WB_BC_IMG_D (
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT NOT NULL COMMENT '이미지넓이',
    IMG_HGHT                         INT NOT NULL COMMENT '이미지높이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (BC_ID)
)
COMMENT = '명함이미지상세';


CREATE TABLE WB_BC_LST_SETUP_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    ATRB_ID                          VARCHAR (30) NOT NULL COMMENT '속성ID',
    MSG_ID                           VARCHAR (30) NOT NULL COMMENT '메시지ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    DFT_SETUP_YN                     CHAR (1) COMMENT '기본설정여부',
    ALN_VA                           VARCHAR (30) COMMENT '줄마춤값',
    WDTH_PERC                        VARCHAR (30) COMMENT '넓이퍼센트',
    DISP_YN                          CHAR (1) COMMENT '표시여부',
    PRIMARY KEY (COMP_ID, ATRB_ID)
)
COMMENT = '명함목록설정기본';


CREATE TABLE WB_BC_METNG_ATND_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    BC_METNG_DETL_ID                 VARCHAR (30) NOT NULL COMMENT '명함미팅상세ID',
    BC_METNG_ATND_DETL_ID            VARCHAR (30) NOT NULL COMMENT '명함미팅참석자상세ID',
    EMPL_TYP_CD                      VARCHAR (10) COMMENT '임직원구분코드',
    EMPL_NM                          VARCHAR (50) COMMENT '임직원성명',
    EMPL_PHON                        VARCHAR (120) COMMENT '임직원전화번호',
    EMPL_ID                          VARCHAR (30) COMMENT '임직원ID',
    COMP_NM                          VARCHAR (120) COMMENT '회사명',
    COMP_PHON                        VARCHAR (120) COMMENT '회사전화번호',
    EMAIL                            VARCHAR (240) COMMENT '이메일',
    PRIMARY KEY (COMP_ID, REGR_UID, BC_METNG_DETL_ID, BC_METNG_ATND_DETL_ID)
)
COMMENT = '명함미팅참석자관계';


CREATE TABLE WB_BC_METNG_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    BC_METNG_DETL_ID                 VARCHAR (30) NOT NULL COMMENT '명함미팅상세ID',
    BC_ID                            VARCHAR (30) COMMENT '명함ID',
    AGN_YN                           CHAR (1) COMMENT '대리여부',
    METNG_YMD                        DATETIME NOT NULL COMMENT '미팅년월일',
    OPEN_YN                          CHAR (1) NOT NULL COMMENT '공개여부',
    METNG_CLS_CD                     VARCHAR (30) NOT NULL COMMENT '미팅분류코드',
    METNG_SUBJ                       VARCHAR (240) NOT NULL COMMENT '미팅제목',
    METNG_CONT                       VARCHAR (1000) NOT NULL COMMENT '미팅내용',
    ATT_FILE_ID                      VARCHAR (30) COMMENT '첨부파일ID',
    READ_CNT                         INT COMMENT '조회수',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (COMP_ID, REGR_UID, BC_METNG_DETL_ID)
)
COMMENT = '명함미팅상세';


CREATE TABLE WB_BC_METNG_FILE_D (
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
    PRIMARY KEY (FILE_ID)
)
COMMENT = '명함미팅첨부파일';


CREATE TABLE WB_BC_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE WB_BC_USER_LST_SETUP_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    ATRB_ID                          VARCHAR (30) NOT NULL COMMENT '속성ID',
    MSG_ID                           VARCHAR (30) NOT NULL COMMENT '메시지ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    ALN_VA                           VARCHAR (30) COMMENT '줄마춤값',
    WDTH_PERC                        VARCHAR (30) COMMENT '넓이퍼센트',
    DISP_YN                          CHAR (1) COMMENT '표시여부',
    PRIMARY KEY (COMP_ID, REGR_UID, ATRB_ID)
)
COMMENT = '명함사용자화면설정관계';


CREATE TABLE WB_BC_USER_SCRN_SETUP_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    INPUT_WND_LOC_CD                 VARCHAR (30) COMMENT '입력창위치코드',
    LST_TYP_CD                       VARCHAR (30) COMMENT '목록구분코드',
    PRIMARY KEY (COMP_ID, REGR_UID)
)
COMMENT = '명함사용자화면설정관계';


CREATE TABLE WB_METNG_CLS_CD_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    CD_VA                            VARCHAR (400) COMMENT '코드값',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (COMP_ID, RESC_ID)
)
COMMENT = '미팅분류코드기본';


CREATE TABLE WB_PUB_BC_B (
    BC_ID                            VARCHAR (30) NOT NULL COMMENT '명함ID',
    OPEN_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '공개구분코드',
    FLD_ID                           VARCHAR (30) COMMENT '폴더ID',
    BC_NM                            VARCHAR (30) NOT NULL COMMENT '명함성명',
    COMP_NM                          VARCHAR (120) COMMENT '회사명',
    DEPT_NM                          VARCHAR (120) COMMENT '부서명',
    GRADE_NM                         VARCHAR (30) COMMENT '직급명',
    TICH_CONT                        VARCHAR (800) COMMENT '담당업무내용',
    IPTFG_YN                         CHAR (1) COMMENT '주요인사여부',
    FNO                              VARCHAR (20) COMMENT '팩스번호',
    COMP_ZIP_NO                      VARCHAR (10) COMMENT '회사_우편번호',
    COMP_ADR                         VARCHAR (240) COMMENT '회사_주소',
    COMP_DETL_ADR                    VARCHAR (240) COMMENT '회사_상세주소',
    HOME_ZIP_NO                      VARCHAR (10) COMMENT '자택우편번호',
    HOME_ADR                         VARCHAR (240) COMMENT '자택주소',
    HOME_DETL_ADR                    VARCHAR (240) COMMENT '자택상세주소',
    BUMK_YN                          CHAR (1) COMMENT '즐겨찾기여부',
    PHOTO_FILE_ID                    VARCHAR (30) COMMENT '사진파일ID',
    GEN_CD                           VARCHAR (30) COMMENT '성별코드',
    NATY_CD                          VARCHAR (30) COMMENT '국적코드',
    BIRTH                            DATETIME COMMENT '생년월일',
    BIRTH_SCLC_CD                    VARCHAR (30) COMMENT '생년월일월력코드',
    WEDD_ANNV                        DATETIME COMMENT '결혼기념일',
    WEDD_ANNV_SCLC_CD                VARCHAR (30) COMMENT '결혼기념일월력코드',
    PSN_HPAGE_URL                    VARCHAR (1000) COMMENT '개인홈페이지URL',
    COMP_HPAGE_URL                   VARCHAR (1000) COMMENT '회사홈페이지URL',
    HOBY_CONT                        VARCHAR (400) COMMENT '취미내용',
    SPECT_CONT                       VARCHAR (400) COMMENT '특기사항내용',
    ESCHL_NM                         VARCHAR (120) COMMENT '초등학교명',
    MSCHL_NM                         VARCHAR (120) COMMENT '중학교명',
    HSCHL_NM                         VARCHAR (120) COMMENT '고등학교명',
    UNIV_CONT                        VARCHAR (240) COMMENT '대학교내용',
    BHIST_CONT                       VARCHAR (400) COMMENT '약력내용',
    CLNS_ID                          VARCHAR (30) COMMENT '친밀도내용',
    NOTE_CONT                        VARCHAR (400) COMMENT '비고',
    ATT_FILE_ID                      VARCHAR (30) COMMENT '첨부파일ID',
    BC_REG_TYP_CD                    VARCHAR (30) COMMENT '명함등록구분코드',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    DEL_YN                           CHAR (1) COMMENT '삭제여부',
    EMAIL                            VARCHAR (240) COMMENT '이메일',
    BC_EN_NM                         VARCHAR (30) COMMENT '명함영문성명',
    DFT_CNTC_TYP_CD                  VARCHAR (30) COMMENT '기본연락처구분코드',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    ORIGINAL_BC_ID                   VARCHAR (30) COMMENT '원본명함ID',
    MAIN_SETUP_YN                    CHAR (1) COMMENT '메인설정여부',
    DUPLICATE_BC_ID                  VARCHAR (30) COMMENT '사본명함ID',
    INITIAL_NM                       VARCHAR (3) COMMENT '초성명',
    PRIMARY KEY (BC_ID)
)
COMMENT = '공유명함';


CREATE TABLE WB_PUB_FLD_B (
    FLD_ID                           VARCHAR (30) NOT NULL COMMENT '폴더ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    FLD_NM                           VARCHAR (120) NOT NULL COMMENT '폴더명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    FLD_PID                          VARCHAR (30) NOT NULL COMMENT '상위폴더ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (FLD_ID)
)
COMMENT = '공유폴더';


/* ----------------------------
    WC
---------------------------- */


CREATE TABLE WC_AGNT_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    AGNT_ID                          VARCHAR (30) NOT NULL COMMENT '대리인UID',
    PRIMARY KEY (COMP_ID, USER_UID, AGNT_ID)
)
COMMENT = '대리인관계';


CREATE TABLE WC_ANNV_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    NAT_CD                           VARCHAR (30) NOT NULL COMMENT '국가코드',
    PRIMARY KEY (COMP_ID, SCHDL_ID)
)
COMMENT = '기념일상세';


CREATE TABLE WC_APNTR_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    APNTR_UID                        VARCHAR (30) NOT NULL COMMENT '지정인UID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PRIMARY KEY (COMP_ID, APNTR_UID, USER_UID)
)
COMMENT = '지정인_관계';


CREATE TABLE WC_APNTR_SCHDL_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    APNTR_UID                        VARCHAR (30) NOT NULL COMMENT '지정인UID',
    ORDR                             INT COMMENT '순서',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    PRIMARY KEY (USER_UID, APNTR_UID, COMP_ID)
)
COMMENT = '지정인관계';


CREATE TABLE WC_BUMK_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    BUMK_ID                          VARCHAR (30) NOT NULL COMMENT '즐겨찾기ID',
    BUMK_DISP_NM                     VARCHAR (100) COMMENT '즐겨찾기표시명',
    BUMK_TGT_UID                     VARCHAR (30) COMMENT '즐겨찾기 대상자 UID',
    BUMK_TGT_DEPT_ID                 VARCHAR (30) COMMENT '즐겨찾기 대상 부서ID',
    USER_DEPT_TYP_CD                 VARCHAR (30) COMMENT '사용자 부서 구분 코드',
    PRIMARY KEY (COMP_ID, USER_UID, BUMK_ID)
)
COMMENT = '즐겨찾기_상세';


CREATE TABLE WC_CAT_CLS_B (
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
    PRIMARY KEY (CAT_ID)
)
COMMENT = '카테고리분류기본';


CREATE TABLE WC_ENV_SETUP_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PAGE_PLT_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '페이지/포틀릿 구분코드',
    SCHDL_TYP_CD                     VARCHAR (30) COMMENT '일정구분코드',
    SCHDL_KND_CD                     VARCHAR (30) COMMENT '일정종류코드',
    SOLA_LUNA_YN                     CHAR (1) COMMENT '양력여부',
    PRIMARY KEY (COMP_ID, USER_UID, PAGE_PLT_TYP_CD)
)
COMMENT = '환경_설정_상세';


CREATE TABLE WC_ERP_SCHDL_B (
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SCHDL_TYP_CD                     VARCHAR (30) NOT NULL COMMENT '일정구분코드',
    SUBJ                             VARCHAR (120) NOT NULL COMMENT '제목',
    CONT                             VARCHAR (500) COMMENT '내용',
    STRT_DT                          DATETIME NOT NULL COMMENT '시작일시',
    END_DT                           DATETIME NOT NULL COMMENT '종료일시',
    ALLDAY_YN                        CHAR (1) COMMENT '종일여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (SCHDL_ID)
)
COMMENT = 'ERP일정';


CREATE TABLE WC_MAIL_SCHDL_R (
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    ACPT_YN                          CHAR (1) COMMENT '수락여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    CMPL_DT                          DATETIME COMMENT '완료일시',    
    MAIL_ID                            VARCHAR (30) COMMENT '메일ID',
    COPY_SCHDL_ID                 VARCHAR (30) COMMENT '복사일정ID',    
    PRIMARY KEY (SCHDL_ID, USER_UID)
)
COMMENT = '메일일정';


CREATE TABLE WC_NAT_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CD                               VARCHAR (30) NOT NULL COMMENT '코드',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    DFT_YN                           CHAR (1) COMMENT '기본여부',
    PRIMARY KEY (COMP_ID, CD)
)
COMMENT = '국가설정';


CREATE TABLE WC_PROM_GUEST_D (
    GUEST_UID                        VARCHAR (30) NOT NULL COMMENT '참석자UID',
    GUEST_NM                         VARCHAR (20) NOT NULL COMMENT '참석자 성명',
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
COMMENT = '약속_참석자_상세';


CREATE TABLE WC_REPET_SETUP_D (
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    REPET_PERD_CD                    VARCHAR (30) NOT NULL COMMENT '반복주기코드',
    APNT_MM                          VARCHAR (4) COMMENT '지정월',
    REPET_MM                         VARCHAR (4) COMMENT '반복월',
    APNT_WK                          VARCHAR (4) COMMENT '지정주',
    REPET_WK                         VARCHAR (4) COMMENT '반복주',
    APNT_DY                          VARCHAR (30) COMMENT '지정요일',
    REPET_DD                         VARCHAR (4) COMMENT '반복일',
    APNT_DD                          VARCHAR (4) COMMENT '지정일',
    REPET_START_DT                   DATETIME COMMENT '반복시작일시',
    REPET_END_DT                     DATETIME COMMENT '반복종료일시',
    PRIMARY KEY (SCHDL_ID)
)
COMMENT = '반복설정_상세';


CREATE TABLE WC_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE WC_SCHDL_B (
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
    SCHDL_FILE_ID                    VARCHAR (30) COMMENT '일정파일ID',
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
    PRIMARY KEY (SCHDL_ID)
)
COMMENT = '일정 기본';


CREATE TABLE WC_SCHDL_DEPT_R (
    SCHDL_ID                         VARCHAR (30) NOT NULL COMMENT '일정ID',
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    PRIMARY KEY (SCHDL_ID, ORG_ID)
)
COMMENT = '부서일정관계';


CREATE TABLE WC_SCHDL_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    DISP_ORDR                        INT COMMENT '표시순서',
    REF_ID                           VARCHAR (30) NOT NULL COMMENT '참조ID',
    DISP_NM                          VARCHAR (240) COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (FILE_ID)
)
COMMENT = '일정첨부파일';


CREATE TABLE WC_SCHDL_GRP_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SCHDL_GRP_ID                     VARCHAR (30) NOT NULL COMMENT '일정 그룹ID',
    GRP_NM                           VARCHAR (100) COMMENT '그룹명',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일자',
    MASTR_UID                        VARCHAR (30) COMMENT '마스터UID',
    REGR_NM                          VARCHAR (120) COMMENT '등록자명',
    PRIMARY KEY (COMP_ID, SCHDL_GRP_ID)
)
COMMENT = '일정_그룹_기본';


CREATE TABLE WC_SCHDL_GRP_MBSH_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자 UID',
    MBSH_TYP_CD                      VARCHAR (30) COMMENT '회원구분코드',
    AUTH_GRAD_CD                     VARCHAR (30) COMMENT '권한등급코드',
    EMAIL_SEND_YN                    CHAR (1) COMMENT '이메일발송여부',
    SCHDL_GRP_ID                     VARCHAR (30) NOT NULL COMMENT '일정 그룹ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_NM                          VARCHAR (120) COMMENT '사용자명',
    PRIMARY KEY (USER_UID, SCHDL_GRP_ID, COMP_ID)
)
COMMENT = '일정_그룹_회원_상세';


CREATE TABLE WC_USER_GRP_B (
    USER_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '사용자그룹ID',
    COMP_ID                          VARCHAR (30) NOT NULL COMMENT '회사ID',
    USER_GRP_NM                      VARCHAR (120) NOT NULL COMMENT '사용자그룹명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MODR_UID                         VARCHAR (30) NOT NULL COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (USER_GRP_ID)
)
COMMENT = '사용자그룹';


CREATE TABLE WC_USER_GRP_L (
    USER_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '사용자그룹ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_GRP_ID, USER_UID)
)
COMMENT = '사용자그룹목록';


CREATE TABLE WC_USER_NAT_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    CD                               VARCHAR (30) NOT NULL COMMENT '코드',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    PRIMARY KEY (COMP_ID, USER_UID)
)
COMMENT = '사용자국가설정';


/* ----------------------------
    WH
---------------------------- */


CREATE TABLE WH_CAT_GRP_B (
    CAT_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '유형그룹ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CAT_GRP_NM                       VARCHAR (120) NOT NULL COMMENT '유형그룹명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    DFT_YN                           CHAR (1) NOT NULL COMMENT '기본여부',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CAT_GRP_ID)
)
COMMENT = '유형그룹기본';


CREATE TABLE WH_CAT_GRP_L (
    CAT_NO                           INT NOT NULL COMMENT '유형번호',
    CAT_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '유형그룹ID',
    CAT_NM                           VARCHAR (120) NOT NULL COMMENT '유형명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (CAT_NO)
)
COMMENT = '유형그룹목록';


CREATE TABLE WH_DOC_NO_D (
    YY                               INT NOT NULL COMMENT '년',
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    DOC_SEQ                          BIGINT COMMENT '문서일련번호',
    PRIMARY KEY (YY, ORG_ID)
)
COMMENT = '문서번호상세';


CREATE TABLE WH_MD_B (
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    MD_NM                            VARCHAR (120) NOT NULL COMMENT '모듈명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    MD_PID                           VARCHAR (30) NOT NULL COMMENT '상위모듈ID',
    MD_TYP_CD                        CHAR (1) NOT NULL COMMENT '모듈구분코드',
    CAT_GRP_ID                       VARCHAR (30) COMMENT '유형그룹ID',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (MD_ID)
)
COMMENT = '모듈기본';


CREATE TABLE WH_MD_PICH_L (
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    PICH_TYP_CD                      CHAR (1) NOT NULL COMMENT '담당자구분코드',
    ID_VA                            VARCHAR (30) NOT NULL COMMENT 'ID값',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (MD_ID, PICH_TYP_CD, ID_VA)
)
COMMENT = '모듈담당자목록';


CREATE TABLE WH_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE WH_REQ_B(
	REQ_NO 	INT NOT NULL COMMENT '요청번호',
	COMP_ID 	VARCHAR(3) NOT NULL COMMENT '회사ID',
	DEPT_ID 	VARCHAR(30) NOT NULL COMMENT '부서ID',
	DOC_NO 	VARCHAR(120) COMMENT '문서번호',
	SUBJ 	VARCHAR(240) NOT NULL COMMENT '제목',
	CONT 		LONGTEXT COMMENT '내용',
	REQ_DT 		DATETIME COMMENT '요청일',
	CMPL_PDT 		DATETIME COMMENT '완료희망일',
	PROGRM_ID 		VARCHAR(120) COMMENT '프로그램ID',
	PROGRM_NM 		VARCHAR(240) COMMENT '프로그램명',
	WRITTEN_REQ_YN 	CHAR(1) COMMENT '의뢰서여부',
	WRITTEN_REQ_NO 	VARCHAR(120) COMMENT '의뢰서번호',
	UPLOAD_NO 	INT COMMENT '업로드번호',
	REGR_UID 	VARCHAR(30) NOT NULL COMMENT '등록자UID',
	REG_DT 	DATETIME COMMENT '등록일시',
	MODR_UID 	VARCHAR(30) COMMENT '수정자UID',
	MOD_DT 	DATETIME COMMENT '수정일시',
	PRIMARY KEY (REQ_NO)
)
COMMENT = '요청기본';



CREATE TABLE WH_REQ_CMPL_D (
    REQ_NO                           INT NOT NULL COMMENT '요청번호',
    HDL_CONT                         VARCHAR (400) COMMENT '처리내용',
    DEV_HOUR_VA 					VARCHAR(10) COMMENT '공수값',
    HDL_COMP_NM                      VARCHAR (120) COMMENT '처리업체명',
    HDL_COST                         INT COMMENT '처리비용',
    STRT_DT                          DATETIME COMMENT '시작일',
    END_DT                           DATETIME COMMENT '종료일',
    TEST_PICH_UID                    VARCHAR (30) COMMENT '테스트담당자UID',
    TEST_ONGO_CONT                   VARCHAR (4000) COMMENT '테스트진행내용',
    TEST_RES_CONT                    VARCHAR (4000) COMMENT '테스트결과내용',
    PRIMARY KEY (REQ_NO)
)
COMMENT = '요청완료상세';


CREATE TABLE WH_REQ_EVAL_D (
    REQ_NO                           INT NOT NULL COMMENT '요청번호',
    EVAL_NO                          INT COMMENT '평가번호',
    EVAL_RSON                        VARCHAR (400) COMMENT '평가사유',
    ADD_REQR                         VARCHAR (400) COMMENT '추가요청사항',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (REQ_NO)
)
COMMENT = '요청평가상세';


CREATE TABLE WH_REQ_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    TYP_CD                           CHAR (1) NOT NULL COMMENT '구분코드',
    REF_ID                           INT NOT NULL COMMENT '참조ID',
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
COMMENT = '첨부파일상세';


CREATE TABLE WH_REQ_ONGD_D (
    REQ_NO                           INT NOT NULL COMMENT '요청번호',
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    STAT_CD                          CHAR (1) NOT NULL COMMENT '상태코드',
    PREV_STAT_CD 				  CHAR(1) COMMENT '이전상태코드',
    PICH_UID                         VARCHAR (30) COMMENT '담당자UID',
    RECV_UID                         VARCHAR (30) COMMENT '접수자UID',
    RECV_DT                          DATETIME COMMENT '접수일',
    RECV_CONT                        VARCHAR (4000) COMMENT '접수내용 ',
    HDLR_UID                         VARCHAR (30) COMMENT '처리자UID',
    CAT_NO                           INT COMMENT '유형번호',
    CMPL_DUE_DT                      DATETIME COMMENT '완료예정일',
    ONGO_CONT                        VARCHAR (4000) COMMENT '진행내용',
    CMPL_DT                          DATETIME COMMENT '완료일시',
    EVAL_YN                          CHAR (1) NOT NULL COMMENT '평가여부',
    PRIMARY KEY (REQ_NO)
)
COMMENT = '요청진행상세';


CREATE TABLE WH_RES_EVAL_B (
    EVAL_NO                          INT NOT NULL COMMENT '평가번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    EVAL_NM                          VARCHAR (120) NOT NULL COMMENT '평가명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (EVAL_NO)
)
COMMENT = ' 결과평가기본';


CREATE TABLE WH_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';

CREATE TABLE WH_REQ_REL_D(
	REQ_NO 						INT NOT NULL COMMENT '요청번호',
	REL_NO 							INT NOT NULL COMMENT '관련번호',
	SORT_ORDR 					INT NOT NULL COMMENT '정렬순서',
	PRIMARY KEY (REQ_NO, REL_NO)
)
COMMENT = '요청관련상세';


CREATE TABLE WH_PICH_GRP_B(
	PICH_GRP_ID 				VARCHAR(30) NOT NULL COMMENT '담당자그룹ID',
	COMP_ID 					VARCHAR(3) NOT NULL COMMENT '회사ID',
	PICH_GRP_NM 			VARCHAR(120) NOT NULL COMMENT '담당자그룹명',
	RESC_ID 					VARCHAR(30) NOT NULL COMMENT '리소스ID',
	USE_YN 						CHAR(1) NOT NULL COMMENT '사용여부',
	DFT_YN 						CHAR(1) NOT NULL COMMENT '기본여부',
	REGR_UID 					VARCHAR(30) NOT NULL COMMENT '등록자UID',
	REG_DT 						DATETIME COMMENT '등록일시',
	MODR_UID 				VARCHAR(30) COMMENT '수정자UID',
	MOD_DT 					DATETIME COMMENT '수정일시',
	PRIMARY KEY (PICH_GRP_ID)
)
COMMENT = '담당자그룹기본';


CREATE TABLE WH_PICH_GRP_L(
	PICH_GRP_ID 				VARCHAR(30) NOT NULL COMMENT '담당자그룹ID',
	USER_UID 					VARCHAR(30) NOT NULL COMMENT '사용자UID',
	SORT_ORDR				 INT NOT NULL COMMENT '정렬순서',
	PRIMARY KEY (PICH_GRP_ID, USER_UID)
)
COMMENT = '담당자그룹목록';


CREATE TABLE WH_REQ_STAT_D(
	REQ_NO 				INT NOT NULL COMMENT '요청번호',
	STAT_CD 				CHAR(1) NOT NULL COMMENT '상태코드',
	HTML_YN 				CHAR(1) NOT NULL COMMENT 'HTML여부',
	PRIMARY KEY (REQ_NO, STAT_CD)
)
COMMENT = '요청상태상세';


CREATE TABLE WH_REQ_CMPL_H(
	HST_NO 				INT NOT NULL COMMENT '이력번호',
	REQ_NO 				INT NOT NULL COMMENT '요청번호',
	CMPL_DT				DATETIME COMMENT '완료일시',
	HDL_CONT 			TEXT COMMENT '처리내용',
	DEV_HOUR_VA 		VARCHAR(10) COMMENT '공수값',
	HDL_COMP_NM 		VARCHAR(120) COMMENT '처리업체명',
	HDL_COST 				INT COMMENT '처리비용',
	STRT_DT 				DATETIME COMMENT '시작일',
	END_DT 				DATETIME COMMENT '종료일',
	TEST_PICH_UID 		VARCHAR(30) COMMENT '테스트담당자UID',
	TEST_ONGO_CONT 	VARCHAR(4000) COMMENT '테스트진행내용',
	TEST_RES_CONT 		VARCHAR(4000) COMMENT '테스트결과내용',
	REGR_UID 				VARCHAR(30) NOT NULL COMMENT '등록자UID',
	REG_DT 					DATETIME COMMENT '등록일시',
	PRIMARY KEY (HST_NO)
)
COMMENT = '요청완료이력';

/* ----------------------------
    WL
---------------------------- */


CREATE TABLE WL_LOG_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    REF_ID                           INT COMMENT '참조ID',
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
COMMENT = '첨부파일상세';


CREATE TABLE WL_REPRT_GRP_B (
    GRP_NO                           INT NOT NULL COMMENT '그룹번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    GRP_NM                           VARCHAR (120) NOT NULL COMMENT '그룹명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    DFT_YN                           CHAR (1) NOT NULL COMMENT '기본여부',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (GRP_NO)
)
COMMENT = '보고그룹기본';


CREATE TABLE WL_REPRT_GRP_L (
    GRP_NO                           INT NOT NULL COMMENT '그룹번호',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    TGT_TYP_CD                       CHAR (1) NOT NULL COMMENT '대상구분코드',
    REPRT_TYP_CD                     VARCHAR (30) NOT NULL COMMENT '보고구분코드',
    SORT_ORDR                        INT COMMENT '정렬순서',
    PRIMARY KEY (GRP_NO, USER_UID, TGT_TYP_CD)
)
COMMENT = '보고그룹목록';


CREATE TABLE WL_REPRT_USER_L (
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    TYP_CD                           VARCHAR (30) NOT NULL COMMENT '구분코드',
    SORT_ORDR                        INT COMMENT '정렬순서',
    PRIMARY KEY (REGR_UID, USER_UID)
)
COMMENT = '보고자목록';


CREATE TABLE WL_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE WL_TASK_CONSOL_R (
    LOG_NO                           INT NOT NULL COMMENT '일지번호',
    LST_NO                           INT NOT NULL COMMENT '목록번호',
    SORT_ORDR                        INT COMMENT '정렬순서',
    PRIMARY KEY (LOG_NO, LST_NO)
)
COMMENT = '업무일지취합관계';


CREATE TABLE WL_TASK_LOG_B (
    LOG_NO                           INT NOT NULL COMMENT '일지번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    DEPT_ID                          VARCHAR (30) COMMENT '부서ID',
    TYP_CD                           VARCHAR (30) NOT NULL COMMENT '구분코드',
    REPRT_DT                         DATETIME COMMENT '보고일자',
    GRP_NO                           INT COMMENT '그룹번호',
    SUBJ                             VARCHAR (240) NOT NULL COMMENT '제목',
    RESULT_CONT                      LONGTEXT COMMENT '실적',
    PLAN_CONT                        LONGTEXT COMMENT '계획',
    ETC_CONT                         LONGTEXT COMMENT '기타내용',
    STAT_CD                          CHAR (1) NOT NULL COMMENT '상태코드',
    CONSOL_YN                        CHAR (1) NOT NULL COMMENT '취합여부',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (LOG_NO)
)
COMMENT = '업무일지기본';


CREATE TABLE WL_TASK_LOG_OPIN_L (
    LOG_NO                           INT NOT NULL COMMENT '일지번호',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    OPIN_CONT                        VARCHAR (300) NOT NULL COMMENT '의견내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (LOG_NO, USER_UID)
)
COMMENT = '업무일지의견목록';


CREATE TABLE WL_TASK_LOG_R (
    LOG_NO                           INT NOT NULL COMMENT '일지번호',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    PRIMARY KEY (LOG_NO, REGR_UID)
)
COMMENT = '업무일지관계';


CREATE TABLE WL_TASK_LOG_USER_L (
    LOG_NO                           INT NOT NULL COMMENT '일지번호',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    READ_YN                          CHAR (1) COMMENT '열람여부',
    SORT_ORDR                        INT COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (LOG_NO, USER_UID)
)
COMMENT = '업무일지사용자목록';


/* ----------------------------
    WM
---------------------------- */


CREATE TABLE WM_RESC_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (COMP_ID, RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


/* ----------------------------
    WR
---------------------------- */


CREATE TABLE WR_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE WR_RESC_IMG_D (
    RESC_MNG_ID                      VARCHAR (30) NOT NULL COMMENT '자원관리ID',
    IMG_PATH                         VARCHAR (240) COMMENT '이미지경로',
    IMG_WDTH                         INT COMMENT '이미지넓이',
    IMG_HGHT                         INT COMMENT '이미지높이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (RESC_MNG_ID)
)
COMMENT = '자원이미지상세';


CREATE TABLE WR_RESC_KND_B (
    RESC_KND_ID                      VARCHAR (30) NOT NULL COMMENT '자원종류ID',
    COMP_ID                          VARCHAR (30) COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    KND_NM                           VARCHAR (30) COMMENT '종류명',
    BGCOL_CD                         VARCHAR (10) COMMENT '배경색코드',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    SORT_ORDR                        INT COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (RESC_KND_ID)
)
COMMENT = '자원종류기본';


CREATE TABLE WR_RESC_MNG_B (
    RESC_MNG_ID                      VARCHAR (30) NOT NULL COMMENT '자원관리ID',
    RESC_KND_ID                      VARCHAR (30) COMMENT '자원종류ID',
    COMP_ID                          VARCHAR (30) COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    RESC_NM                          VARCHAR (120) COMMENT '자원명',
    RESC_LOC                         VARCHAR (240) COMMENT '자원위치',
    SUPL_STAT                        VARCHAR (240) COMMENT '비품현황',
    RESC_ADM_UID                     VARCHAR (30) COMMENT '자원관리자UID',
    DISC_YN                          CHAR (1) COMMENT '심의여부',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    SORT_ORDR                        INT COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (RESC_MNG_ID)
)
COMMENT = '자원관리기본';


CREATE TABLE WR_RESC_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE WR_REZV_B (
    REZV_ID                          VARCHAR (30) NOT NULL COMMENT '예약ID',
    COMP_ID                          VARCHAR (30) NOT NULL COMMENT '회사ID',
    RESC_KND_ID                      VARCHAR (30) NOT NULL COMMENT '자원종류ID',
    RESC_MNG_ID                      VARCHAR (30) NOT NULL COMMENT '자원관리ID',
    REZV_STRT_DT                     DATETIME NOT NULL COMMENT '예약시작일시',
    REZV_END_DT                      DATETIME NOT NULL COMMENT '예약종료일시',
    SUBJ                             VARCHAR (240) NOT NULL COMMENT '제목',
    CONT                             LONGTEXT COMMENT '내용',
    DISC_STAT_CD                     CHAR (1) COMMENT '심의상태코드',
    DISCR_UID                        VARCHAR (30) COMMENT '심의자UID',
    DISC_DT                          DATETIME COMMENT '심의일시',
    DISC_CONT                        VARCHAR (240) COMMENT '심의내용',
    SCHDL_ID                         VARCHAR (30) COMMENT '일정ID',
    SCHDL_KND_CD                     VARCHAR (30) COMMENT '일정대상코드',
    BGCOL_CD                         VARCHAR (10) COMMENT '배경색코드',
    RESQ_EMAIL_YN                    CHAR (1) COMMENT '신청메일여부',
    RES_EMAIL_YN                     CHAR (1) COMMENT '결과메일여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (REZV_ID)
)
COMMENT = '예약기본';


/* ----------------------------
    WV
---------------------------- */


CREATE TABLE WV_QUES_EXAM_D (
    EXAM_NO                          INT NOT NULL COMMENT '보기번호',
    EXAM_ORDR                        INT COMMENT '보기순서',
    EXAM_DISP_NM                     VARCHAR (256) COMMENT '보기표시명',
    INPUT_YN                         CHAR (1) COMMENT '입력 여부',
    EXAM_IMG_USE_YN                  CHAR (1) COMMENT '보기이미지사용여부',
    IMG_SURV_FILE_ID                 VARCHAR (30) COMMENT '이미지설문파일ID',
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    PRIMARY KEY (EXAM_NO, QUES_ID, COMP_ID, SURV_ID)
)
COMMENT = '질문보기상세';


CREATE TABLE WV_SURV_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    SURV_SUBJ                        VARCHAR (256) COMMENT '설문 제목',
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
    SURV_FILE_ID                     VARCHAR (30) COMMENT '설문파일ID',
    NO_AUTH_YN                       CHAR (1) COMMENT '비권한여부',
    PRIMARY KEY (COMP_ID, SURV_ID)
)
COMMENT = '설문기본';


CREATE TABLE WV_SURV_FILE_D (
    FILE_ID                          INT NOT NULL COMMENT '파일ID',
    DISP_ORDR                        INT COMMENT '표시순서',
    REF_ID                           VARCHAR (30) NOT NULL COMMENT '참조ID',
    DISP_NM                          VARCHAR (240) COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일사이즈',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (FILE_ID)
)
COMMENT = '첨부파일';


CREATE TABLE WV_SURV_POLC_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    APVD_YN                          CHAR (1) COMMENT '승인여부',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    MOD_DT                           DATETIME NOT NULL COMMENT '수정일시',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    MODR_UID                         VARCHAR (30) NOT NULL COMMENT '수정자UID',
    PRIMARY KEY (COMP_ID)
)
COMMENT = '설문정책상세';


CREATE TABLE WV_SURV_QUES_D (
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    QUES_SORT_ORDR                   INT COMMENT '질문정렬순서',
    QUES_CONT                        VARCHAR (2000) COMMENT '질문내용',
    QUES_IMG_ATT_YN                  CHAR (1) COMMENT '질문이미지첨부여부',
    IMG_SURV_FILE_ID                 VARCHAR (30) COMMENT '이미지파일ID',
    EXAM_CHOI_ITEM_CD                VARCHAR (30) COMMENT '보기선택항목코드',
    MUL_CHOI_YN                      CHAR (1) COMMENT '복수선택여부',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    MANDA_REPLY_YN                   CHAR (1) COMMENT '필수답변여부',
    PRIMARY KEY (QUES_ID, COMP_ID, SURV_ID)
)
COMMENT = '질문상세';


CREATE TABLE WV_SURV_REPLY_D (
    REPLY_NO                         INT NOT NULL COMMENT '답변번호',
    REPLYR_UID                       VARCHAR (30) NOT NULL COMMENT '답변자ID',
    REPLYR_DEPT_ID                   VARCHAR (30) COMMENT '답변자 부서 ID',
    REPLY_DT                         DATETIME COMMENT '답변일시',
    MULC_INPUT_REPLY_CONT            VARCHAR (256) COMMENT '객관식입력항목답변내용',
    OEND_REPLY_CONT                  VARCHAR (1000) COMMENT '주관식답변내용',
    QUES_ID                          VARCHAR (30) NOT NULL COMMENT '질문ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    PRIMARY KEY (REPLY_NO, REPLYR_UID, QUES_ID, COMP_ID, SURV_ID)
)
COMMENT = '답변상세';


CREATE TABLE WV_SURV_USE_AUTH_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    SURV_ID                          VARCHAR (30) NOT NULL COMMENT '설문ID',
    AUTH_TGT_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '권한대상구분코드',
    AUTH_TGT_UID                     VARCHAR (10) NOT NULL COMMENT '권한대상UID',
    AUTH_INCL_YN                     CHAR (1) COMMENT '권한 포함여부',
    AUTH_GRAD_CD                     VARCHAR (30) NOT NULL COMMENT '권한등급코드',
    PRIMARY KEY (COMP_ID, SURV_ID, AUTH_TGT_TYP_CD, AUTH_TGT_UID, AUTH_GRAD_CD)
)
COMMENT = '설문권한';


/****** Object:  Table WH_REQ_ONGD_H    Script Date: 2017/09/29 17:14 ******/
CREATE TABLE WH_REQ_ONGD_H(
	HST_NO INT NOT NULL COMMENT '이력번호',
	REQ_NO INT NOT NULL COMMENT '요청번호',
	PICH_UID VARCHAR(30) COMMENT '담당자UID',
	CMPL_DUE_DT DATETIME COMMENT '완료예정일',
	ONGO_CONT VARCHAR(4000) COMMENT '진행내용',
	REGR_UID VARCHAR(30) NOT NULL COMMENT '등록자',
	REG_DT DATETIME NOT NULL COMMENT '등록일시',
    PRIMARY KEY (HST_NO)
)
COMMENT = '요청진행이력';


/****** Object:  Table WV_SURV_POPUP_D    Script Date: 2017/11/08 11:17 ******/
CREATE TABLE WV_SURV_POPUP_D(
SURV_ID VARCHAR(30) NOT NULL COMMENT '설문ID',
COMP_ID VARCHAR(3) NOT NULL COMMENT '회사ID',
WDTH_VA INT COMMENT '넓이값',
USE_YN CHAR(1) NOT NULL COMMENT '사용여부',
MODR_UID VARCHAR(30) NOT NULL COMMENT '수정자',
MOD_DT DATETIME NOT NULL COMMENT '수정일시',
PRIMARY KEY (SURV_ID)
)
COMMENT = '팝업설정상세';


/****** Object:  Table WC_WORK_SCHDL_B  Script Date: 2018-12-18 16:18:37 ******/
CREATE TABLE WC_WORK_SCHDL_B(
	SCHDL_ID	VARCHAR(30)	NOT NULL	COMMENT '일정ID',
	COMP_ID	VARCHAR(3)	NOT NULL	COMMENT '회사ID',
	USER_UID	VARCHAR(30)	NOT NULL	COMMENT '사용자UID',
	SCHDL_TYP_CD	VARCHAR(30)	NOT NULL	COMMENT '일정구분코드',
	SUBJ	VARCHAR(120)	NOT NULL	COMMENT '제목',
	CONT	LONGTEXT	COMMENT '내용',
	STRT_DT		DATETIME	NOT NULL	COMMENT '시작일시',
	END_DT		DATETIME	NOT NULL	COMMENT '종료일시',
	ALLDAY_YN	CHAR(1) 	COMMENT '종일여부',
	REGR_UID	VARCHAR(30)	COMMENT '등록자UID',
	REG_DT	DATETIME	COMMENT '등록일시',
	MODR_UID	VARCHAR(30)	COMMENT '수정자UID',
	MOD_DT	DATETIME	COMMENT '수정일시',
	PRIMARY KEY (SCHDL_ID)
)
COMMENT = '근태일정';