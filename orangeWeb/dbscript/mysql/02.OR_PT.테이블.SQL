

/* ----------------------------
    OR
---------------------------- */


CREATE TABLE OR_DB_INTG_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    DB_USER_ID                       VARCHAR (30) COMMENT '데이터베이스사용자ID',
    DB_PW                            VARCHAR (30) COMMENT '데이터베이스비밀번호',
    DB_DRIVER                        VARCHAR (60) COMMENT '데이터베이스드라이버',
    DB_URL                           VARCHAR (60) COMMENT '데이터베이스URL',
    DB_TEST_SQL                      VARCHAR (60) COMMENT '데이터베이스테스트SQL',
    CD_DEL_CD                        VARCHAR (30) COMMENT '코드삭제코드',
    ORG_DEL_CD                       VARCHAR (30) COMMENT '조직삭제코드',
    USER_DEL_CD                      VARCHAR (30) COMMENT '사용자삭제코드',
    DEL_ORG_MOVE_ORG_ID              VARCHAR (30) COMMENT '삭제조직이동조직ID',
    DEL_USER_MOVE_ORG_ID             VARCHAR (30) COMMENT '삭제사용자이동조직ID',
    NO_DEPT_ORG_ID                   VARCHAR (30) COMMENT '부서없음조직ID',
    ORG_SQL                          LONGTEXT COMMENT '조직도SQL',
    CD_SQL                           LONGTEXT COMMENT '코드SQL',
    USER_SQL                         LONGTEXT COMMENT '사용자SQL',
    ADU_SQL                          LONGTEXT COMMENT '겸직SQL',
    INTG_TM                          VARCHAR (10) COMMENT '연계시간',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    PRIMARY KEY (COMP_ID)
)
COMMENT = 'DB연계상세';


CREATE TABLE OR_ODUR_B (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    LGIN_ID                          VARCHAR (40) NOT NULL COMMENT '로그인ID',
    USER_NM                          VARCHAR (120) COMMENT '사용자명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    RID                              VARCHAR (30) COMMENT '참조ID',
    EIN                              VARCHAR (20) COMMENT '사원번호',
    USER_STAT_CD                     VARCHAR (30) NOT NULL COMMENT '사용자상태코드',
    USER_DESC                        VARCHAR (1000) COMMENT '사용자설명',
    ENTRA_YMD                        DATETIME COMMENT '입사년월일',
    RESIG_YMD                        DATETIME COMMENT '퇴사년월일',
    GEN_CD                           VARCHAR (30) NOT NULL COMMENT '성별코드',
    APV_PW_TYP_CD                    VARCHAR (30) NOT NULL COMMENT '결재비밀번호구분코드',
    SYNC_STEP_CD                     VARCHAR (30) COMMENT '동기화단계코드',
    REF_VA1                          VARCHAR (400) COMMENT '참조값1',
    REF_VA2                          VARCHAR (400) COMMENT '참조값2',
    REF_VA3                          VARCHAR (400) COMMENT '참조값3',
    SYS_USER_YN                      CHAR (1) COMMENT '시스템사용자여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (ODUR_UID)
)
COMMENT = '원직자기본';
CREATE INDEX IDX_OR_ODUR_B_1 ON OR_ODUR_B (LGIN_ID);
CREATE INDEX IDX_OR_ODUR_B_2 ON OR_ODUR_B (RID);
CREATE INDEX IDX_OR_ODUR_B_3 ON OR_ODUR_B (EIN);


CREATE TABLE OR_ODUR_SECU_D (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    SECU_ID                          VARCHAR (60) NOT NULL COMMENT '보안ID',
    SECU_VA                          VARCHAR (200) COMMENT '보안값',
    PRIMARY KEY (ODUR_UID, SECU_ID)
)
COMMENT = '원직자보안상세';


CREATE TABLE OR_OFSE_D (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    OFSE_TYP_CD                      VARCHAR (30) COMMENT '관인구분코드',
    OFSE_NM                          VARCHAR (120) COMMENT '관인명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    DFT_OFSE_YN                      CHAR (1) COMMENT '기본관인여부',
    DISU_YN                          CHAR (1) NOT NULL COMMENT '폐기여부',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT NOT NULL COMMENT '이미지넓이',
    IMG_HGHT                         INT NOT NULL COMMENT '이미지높이',
    CHN_RSON                         VARCHAR (800) COMMENT '변경사유',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    DISUR_UID                        VARCHAR (30) COMMENT '폐기자UID',
    DISU_DT                          DATETIME COMMENT '폐기일시',
    PRIMARY KEY (ORG_ID, SEQ)
)
COMMENT = '관인상세';


CREATE TABLE OR_ORG_APV_D (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    CRD_ORG_ID                       VARCHAR (30) COMMENT '문서과조직ID',
    CRD_RESC_ID                      VARCHAR (30) COMMENT '문서과리소스ID',
    AGN_CRD_ORG_ID                   VARCHAR (30) COMMENT '대리문서과조직ID',
    AGN_CRD_RESC_ID                  VARCHAR (30) COMMENT '대리문서과리소스ID',
    REAL_CRD_ORG_ID                  VARCHAR (30) COMMENT '실제문서과조직ID',
    HODP_POSIT_RESC_ID               VARCHAR (30) COMMENT '부서장직위리소스ID',
    SENDR_NM_RESC_ID                 VARCHAR (50) COMMENT '발신명의리소스ID',
    RECV_DEPT_ID                     VARCHAR (30) COMMENT '수신처ID',
    INSP_YN                          CHAR (1) NOT NULL COMMENT '감사과여부',
    OFSE_ID                          VARCHAR (30) COMMENT '관인ID',
    PRIMARY KEY (ORG_ID)
)
COMMENT = '조직결재상세';
CREATE INDEX IDX_OR_ORG_APV_D_1 ON OR_ORG_APV_D (AGN_CRD_ORG_ID);


CREATE TABLE OR_ORG_B (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    ORG_NM                           VARCHAR (120) COMMENT '조직명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    ORG_PID                          VARCHAR (30) NOT NULL COMMENT '조직부모ID',
    DEPT_ID                          VARCHAR (30) NOT NULL COMMENT '부서ID',
    DEPT_RESC_ID                     VARCHAR (30) NOT NULL COMMENT '부서리소스ID',
    RID                              VARCHAR (30) COMMENT '참조ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    ORG_TYP_CD                       VARCHAR (30) COMMENT '조직구분코드',
    SYNC_STEP_CD                     VARCHAR (30) COMMENT '동기화단계코드',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    SYS_ORG_YN                       CHAR (1) NOT NULL COMMENT '시스템조직여부',
    ORG_ABBR_RESC_ID                 VARCHAR (30) COMMENT '조직약어리소스ID',
    REF_VA1                          VARCHAR (400) COMMENT '참조값1',
    REF_VA2                          VARCHAR (400) COMMENT '참조값2',
    REF_VA3                          VARCHAR (400) COMMENT '참조값3',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (ORG_ID)
)
COMMENT = '조직기본';


CREATE TABLE OR_ORG_CNTC_D (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    REP_EMAIL                        VARCHAR (240) COMMENT '대표이메일',
    PHON                             VARCHAR (50) COMMENT '전화번호',
    FNO                              VARCHAR (50) COMMENT '팩스번호',
    ZIP_NO                           VARCHAR (10) COMMENT '우편번호',
    ADR                              VARCHAR (240) COMMENT '주소',
    DETL_ADR                         VARCHAR (240) COMMENT '상세주소',
    REP_HPAGE_URL                    VARCHAR (1000) COMMENT '대표홈페이지URL',
    PRIMARY KEY (ORG_ID)
)
COMMENT = '조직연락처상세';


CREATE TABLE OR_ORG_HST_R (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    HST_ORG_ID                       VARCHAR (30) NOT NULL COMMENT '이력조직ID',
    HST_RESC_ID                      VARCHAR (30) NOT NULL COMMENT '이력리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (ORG_ID, HST_ORG_ID)
)
COMMENT = '조직이력관계';


CREATE TABLE OR_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE OR_USER_B (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    ORG_RESC_ID                      VARCHAR (30) NOT NULL COMMENT '조직리소스ID',
    DEPT_ID                          VARCHAR (30) NOT NULL COMMENT '부서ID',
    DEPT_RESC_ID                     VARCHAR (30) NOT NULL COMMENT '부서리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    SECUL_CD                         VARCHAR (30) COMMENT '보안등급코드',
    GRADE_CD                         VARCHAR (30) COMMENT '직급코드',
    TITLE_CD                         VARCHAR (30) COMMENT '직책코드',
    POSIT_CD                         VARCHAR (30) COMMENT '직위코드',
    DUTY_CD                          VARCHAR (30) COMMENT '직무코드',
    USER_NM                          VARCHAR (120) COMMENT '사용자명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    ADU_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '겸직구분코드',
    STRT_YMD                         DATETIME COMMENT '시작년월일',
    END_YMD                          DATETIME COMMENT '종료년월일',
    USER_STAT_CD                     VARCHAR (30) NOT NULL COMMENT '사용자상태코드',
    END_RSON                         VARCHAR (800) COMMENT '종료사유',
    TICH_CONT                        VARCHAR (800) COMMENT '담당업무내용',
    SIGN_MTHD_CD                     VARCHAR (30) COMMENT '서명방법코드',
    RID                              VARCHAR (30) COMMENT '참조ID',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    SYNC_STEP_CD                     VARCHAR (30) COMMENT '동기화단계코드',
    REF_VA1                          VARCHAR (400) COMMENT '참조값1',
    REF_VA2                          VARCHAR (400) COMMENT '참조값2',
    REF_VA3                          VARCHAR (400) COMMENT '참조값3',
    SYS_USER_YN                      CHAR (1) COMMENT '시스템사용자여부',
    AUTO_APV_LN_CD                   VARCHAR (30) COMMENT '자동결재선코드',
    PRIMARY KEY (USER_UID)
)
COMMENT = '사용자기본';
CREATE INDEX IDX_OR_USER_B_1 ON OR_USER_B (ODUR_UID, USER_UID);
CREATE INDEX IDX_OR_USER_B_2 ON OR_USER_B (ORG_ID);
CREATE INDEX IDX_OR_USER_B_3 ON OR_USER_B (DEPT_ID);


CREATE TABLE OR_USER_IMG_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    USER_IMG_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '사용자이미지구분코드',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT COMMENT '이미지넓이',
    IMG_HGHT                         INT COMMENT '이미지높이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (USER_UID, USER_IMG_TYP_CD)
)
COMMENT = '사용자이미지상세';


CREATE TABLE OR_USER_LGIN_H (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SESSION_ID                       VARCHAR (120) NOT NULL COMMENT '세션ID',
    LGIN_DT                          DATETIME NOT NULL COMMENT '로그인일시',
    LGOT_DT                          DATETIME COMMENT '로그아웃일시',
    ACCS_IP                          VARCHAR (30) COMMENT '접속IP',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    DEVI_NM                          VARCHAR (10) COMMENT '장치명',
    PRIMARY KEY (USER_UID, SESSION_ID, LGIN_DT)
)
COMMENT = '사용자로그인이력';


CREATE TABLE OR_USER_LGIN_SETUP_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    REPET_TYP_CD                     VARCHAR (10) COMMENT '반복구분코드',
    STRT_YMD                         DATETIME COMMENT '시작년월일',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    NEXT_REPET_YMD                   DATETIME COMMENT '다음반복연월일',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    DEL_YN                           CHAR (1) COMMENT '삭제여부',
    PRIMARY KEY (COMP_ID)
)
COMMENT = '사용자로그인이력설정';


CREATE TABLE OR_USER_PINFO_D (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    MBNO_ENC                         VARCHAR (120) COMMENT '휴대전화번호암호값',
    EMAIL_ENC                        VARCHAR (400) COMMENT '이메일암호값',
    EXTN_EMAIL_ENC                   VARCHAR (400) COMMENT '외부이메일암호값',
    HOME_PHON_ENC                    VARCHAR (120) COMMENT '자택전화번호암호값',
    HOME_FNO_ENC                     VARCHAR (120) COMMENT '자택팩스번호암호값',
    HOME_ZIP_NO_ENC                  VARCHAR (80) COMMENT '자택우편번호암호값',
    HOME_ADR_ENC                     VARCHAR (400) COMMENT '자택주소암호값',
    HOME_DETL_ADR_ENC                VARCHAR (400) COMMENT '자택상세주소암호값',
    COMP_PHON_ENC                    VARCHAR (120) COMMENT '회사전화번호암호값',
    COMP_FNO_ENC                     VARCHAR (120) COMMENT '회사팩스번호암호값',
    COMP_ZIP_NO_ENC                  VARCHAR (80) COMMENT '회사우편번호암호값',
    COMP_ADR_ENC                     VARCHAR (400) COMMENT '회사주소암호값',
    COMP_DETL_ADR_ENC                VARCHAR (400) COMMENT '회사상세주소암호값',
    SSN_ENC                          VARCHAR (40) COMMENT '주민등록번호암호값',
    HPAGE_URL_ENC                    VARCHAR (400) COMMENT '홈페이지URL암호값',
    PRIMARY KEY (ODUR_UID)
)
COMMENT = '사용자개인정보상세';


CREATE TABLE OR_USER_PW_D (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    PW_TYP_CD                        VARCHAR (30) NOT NULL COMMENT '비밀번호구분코드',
    MOD_DT                           DATETIME COMMENT '수정일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    PW_ENC                           VARCHAR (400) COMMENT '비밀번호암호값',
    PRIMARY KEY (ODUR_UID, PW_TYP_CD)
)
COMMENT = '사용자비밀번호상세';


CREATE TABLE OR_USER_PW_H (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    PW_TYP_CD                        VARCHAR (30) NOT NULL COMMENT '비밀번호구분코드',
    MOD_DT                           DATETIME NOT NULL COMMENT '수정일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    PW_ENC                           VARCHAR (400) COMMENT '비밀번호암호값',
    PRIMARY KEY (ODUR_UID, PW_TYP_CD, MOD_DT)
)
COMMENT = '사용자비밀번호이력';


CREATE TABLE OR_USER_PW_LOST_D (
    LOST_ID                          VARCHAR (40) NOT NULL COMMENT '분실ID',
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    LANG_TYP_CD                      VARCHAR (30) COMMENT '언어구분코드',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (LOST_ID, ODUR_UID)
)
COMMENT = '사용자비밀번호분실상세';


CREATE TABLE OR_USER_ROLE_R (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    ROLE_CD                          VARCHAR (30) NOT NULL COMMENT '역할코드',
    PRIMARY KEY (USER_UID, ROLE_CD)
)
COMMENT = '사용자역할관계';


/* ----------------------------
    PT
---------------------------- */


CREATE TABLE PT_APPLE_PUSH_D (
    CUST_CD                          VARCHAR (30) NOT NULL COMMENT '고객코드',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    CUST_NM                          VARCHAR (200) COMMENT '고객명',
    STRT_YMD                         DATETIME COMMENT '시작년월일',
    END_YMD                          DATETIME COMMENT '종료년월일',
    IPA_PATH                         VARCHAR (240) COMMENT 'IPA경로',
    DISP_NM                          VARCHAR (200) COMMENT '표시명',
    DESC_CONT                        VARCHAR (2000) COMMENT '설명내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    PRIMARY KEY (CUST_CD, SEQ)
)
COMMENT = '애플푸쉬상세';


CREATE TABLE PT_AUTH_GRP_AUTH_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    AUTH_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '권한그룹ID',
    EXCLI_YN                         CHAR (1) NOT NULL COMMENT '제외대상여부',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    GRP_KND_CD                       VARCHAR (30) NOT NULL COMMENT '그룹종류코드',
    GRP_ID                           VARCHAR (30) NOT NULL COMMENT '그룹ID',
    AUTH_GRP_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '권한그룹구분코드',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SUB_INCL_YN                      CHAR (1) COMMENT '하위포함여부',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (COMP_ID, AUTH_GRP_ID, EXCLI_YN, SEQ, GRP_KND_CD, GRP_ID)
)
COMMENT = '권한그룹권한상세';


CREATE TABLE PT_AUTH_GRP_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    AUTH_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '권한그룹ID',
    AUTH_GRP_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '권한그룹구분코드',
    AUTH_GRP_CAT_CD                  VARCHAR (30) NOT NULL COMMENT '권한그룹카테고리코드',
    AUTH_GRP_NM                      VARCHAR (120) COMMENT '권한그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    AUTH_GRP_DESC                    VARCHAR (400) COMMENT '권한그룹설명',
    SYS_AUTH_GRP_YN                  CHAR (1) COMMENT '시스템권한그룹여부',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (COMP_ID, AUTH_GRP_ID)
)
COMMENT = '권한그룹기본';


CREATE TABLE PT_AUTH_GRP_MNU_PLT_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    AUTH_SCOP_CD                     VARCHAR (30) NOT NULL COMMENT '권한범위코드',
    AUTH_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '권한그룹ID',
    MNU_PLT_ID                       VARCHAR (30) NOT NULL COMMENT '메뉴포틀릿ID',
    AUTH_GRP_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '권한그룹구분코드',
    MNU_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '메뉴그룹ID',
    MNU_PLT_TYP_CD                   VARCHAR (30) NOT NULL COMMENT '메뉴포틀릿구분코드',
    AUTH_CD                          VARCHAR (30) NOT NULL COMMENT '권한코드',
    PRIMARY KEY (COMP_ID, AUTH_SCOP_CD, AUTH_GRP_ID, MNU_PLT_ID)
)
COMMENT = '권한그룹메뉴포틀릿관계';


CREATE TABLE PT_AUTH_GRP_USER_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    AUTH_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '권한그룹ID',
    EXCLI_YN                         CHAR (1) NOT NULL COMMENT '제외대상여부',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    AUTH_GRP_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '권한그룹구분코드',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (COMP_ID, AUTH_GRP_ID, EXCLI_YN, USER_UID)
)
COMMENT = '권한그룹사용자관계';


CREATE TABLE PT_CACHE_T (
    CACHE_CAT_ID                     VARCHAR (30) NOT NULL COMMENT '캐쉬유형ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    CACHE_ID                         VARCHAR (30) NOT NULL COMMENT '캐쉬ID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CACHE_CAT_ID, LANG_TYP_CD, CACHE_ID)
)
COMMENT = '캐쉬임시';


CREATE TABLE PT_CD_B (
    CLS_CD                           VARCHAR (30) NOT NULL COMMENT '분류코드',
    CD                               VARCHAR (30) NOT NULL COMMENT '코드',
    CD_VA                            VARCHAR (400) COMMENT '코드값',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    COMP_IDS                         VARCHAR (400) COMMENT '회사ID목록',
    REF_VA1                          VARCHAR (400) COMMENT '참조값1',
    REF_VA2                          VARCHAR (400) COMMENT '참조값2',
    CD_DESC                          VARCHAR (400) COMMENT '코드설명',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    FLD_YN                           CHAR (1) COMMENT '폴더여부',
    SYS_CD_YN                        CHAR (1) COMMENT '시스템코드여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CLS_CD, CD)
)
COMMENT = '코드기본';


CREATE TABLE PT_COMP_B (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RCD                              VARCHAR (30) COMMENT '참조코드',
    COMP_NM                          VARCHAR (120) COMMENT '회사명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    DEL_YN                           CHAR (1) COMMENT '삭제여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    MAIL_DOMAIN                      VARCHAR (50) NOT NULL COMMENT '메일도메인',
    LIC_CNT                          INT COMMENT '라이선스수',
    PRIMARY KEY (COMP_ID)
)
COMMENT = '회사기본';


CREATE TABLE PT_COMP_LANG_R (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    PRIMARY KEY (COMP_ID, LANG_TYP_CD)
)
COMMENT = '회사언어관계';


CREATE TABLE PT_LGIN_IMG_D (
    LGIN_IMG_ID                      VARCHAR (30) NOT NULL COMMENT '로그인이미지ID',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    LGIN_IMG_NM                      VARCHAR (120) COMMENT '로그인이미지명',
    STRT_YMD                         DATETIME COMMENT '시작년월일',
    END_YMD                          DATETIME COMMENT '종료년월일',
    DFT_IMG_YN                       CHAR (1) NOT NULL COMMENT '기본이미지여부',
    TOP_PX                           INT COMMENT 'TOP픽셀',
    LEFT_PX                          INT COMMENT 'LEFT픽셀',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    LOGO_IMG_PATH                    VARCHAR (240) COMMENT '로고이미지경로',
    BG_IMG_PATH                      VARCHAR (240) NOT NULL COMMENT '배경이미지경로',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (LGIN_IMG_ID)
)
COMMENT = '로그인이미지상세';


CREATE TABLE PT_LST_SETUP_D (
    LST_SETUP_META_ID                VARCHAR (30) NOT NULL COMMENT '목록설정메타ID',
    DFT_VA_YN                        CHAR (1) NOT NULL COMMENT '기본값여부',
    ATRB_ID                          VARCHAR (30) NOT NULL COMMENT '속성ID',
    MSG_ID                           VARCHAR (30) NOT NULL COMMENT '메시지ID',
    WDTH_PERC                        VARCHAR (30) COMMENT '넓이퍼센트',
    ALN_VA                           VARCHAR (30) COMMENT '줄맞춤값',
    SORT_OPT_VA                      VARCHAR (30) COMMENT '정렬옵션값',
    DATA_SORT_VA                     VARCHAR (30) COMMENT '데이터정렬값',
    DISP_YN                          CHAR (1) COMMENT '표시여부',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (LST_SETUP_META_ID, DFT_VA_YN, ATRB_ID)
)
COMMENT = '목록설정상세';


CREATE TABLE PT_LST_SETUP_META_D (
    LST_SETUP_META_ID                VARCHAR (30) NOT NULL COMMENT '목록설정메타ID',
    ATRB_ID                          VARCHAR (30) NOT NULL COMMENT '속성ID',
    MSG_ID                           VARCHAR (30) NOT NULL COMMENT '메시지ID',
    SORT_OPT_VA                      VARCHAR (30) COMMENT '정렬옵션값',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (LST_SETUP_META_ID, ATRB_ID)
)
COMMENT = '목록설정메타상세';


CREATE TABLE PT_MNU_D (
    MNU_ID                           VARCHAR (30) NOT NULL COMMENT '메뉴ID',
    MNU_GRP_MD_CD                    VARCHAR (30) NOT NULL COMMENT '메뉴그룹모듈코드',
    MNU_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '메뉴그룹ID',
    MNU_NM                           VARCHAR (120) COMMENT '메뉴명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    MNU_PID                          VARCHAR (30) COMMENT '메뉴부모ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MNU_URL                          VARCHAR (1000) COMMENT '메뉴URL',
    MNU_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '메뉴구분코드',
    POP_SETUP_CONT                   VARCHAR (400) COMMENT '팝업설정내용',
    MNU_DESC                         VARCHAR (400) COMMENT '메뉴설명',
    FLD_YN                           CHAR (1) COMMENT '폴더여부',
    SYS_MNU_YN                       CHAR (1) COMMENT '시스템메뉴여부',
    MD_RID                           VARCHAR (30) COMMENT '모듈참조ID',
    MD_ID                            VARCHAR (30) COMMENT '모듈ID',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (MNU_ID)
)
COMMENT = '메뉴상세';
CREATE INDEX IDX_PT_MNU_D_1 ON PT_MNU_D (MNU_GRP_ID);
CREATE INDEX IDX_PT_MNU_D_2 ON PT_MNU_D (MNU_PID);
CREATE INDEX IDX_PT_MNU_D_3 ON PT_MNU_D (MD_RID, MD_ID);


CREATE TABLE PT_MNU_GRP_B (
    MNU_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '메뉴그룹ID',
    MNU_GRP_MD_CD                    VARCHAR (30) NOT NULL COMMENT '메뉴그룹모듈코드',
    MNU_GRP_NM                       VARCHAR (120) COMMENT '메뉴그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SYSG_YN                          CHAR (1) COMMENT '시스템생성여부',
    MNU_GRP_TYP_CD                   VARCHAR (30) COMMENT '메뉴그룹구분코드',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MNU_URL                          VARCHAR (1000) COMMENT '메뉴URL',
    POP_SETUP_CONT                   VARCHAR (400) COMMENT '팝업설정내용',
    PLT_LOUT_CD                      VARCHAR (30) COMMENT '포틀릿레이아웃코드',
    OPEN_COMP_ID                     VARCHAR (3) COMMENT '공개회사ID',
    MD_RID                           VARCHAR (30) COMMENT '모듈참조ID',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (MNU_GRP_ID)
)
COMMENT = '메뉴그룹기본';


CREATE TABLE PT_MNU_LOUT_COMB_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    MNU_LOUT_COMB_ID                 VARCHAR (30) NOT NULL COMMENT '메뉴레이아웃조합ID',
    MNU_LOUT_ID                      VARCHAR (30) NOT NULL COMMENT '메뉴레이아웃ID',
    LOUT_CAT_ID                      CHAR (1) NOT NULL COMMENT '레이아웃유형ID',
    MNU_LOUT_COMB_PID                VARCHAR (30) COMMENT '메뉴레이아웃조합부모ID',
    FLD_YN                           CHAR (1) COMMENT '폴더여부',
    FLD_NM                           VARCHAR (120) COMMENT '폴더명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    MNU_ID                           VARCHAR (30) COMMENT '메뉴ID',
    MNU_RESC_ID                      VARCHAR (30) COMMENT '메뉴리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (COMP_ID, MNU_LOUT_COMB_ID)
)
COMMENT = '메뉴레이아웃조합상세';
CREATE INDEX IDX_PT_MNU_LOUT_COMB_D_1 ON PT_MNU_LOUT_COMB_D (COMP_ID, MNU_LOUT_ID, MNU_LOUT_COMB_PID, SORT_ORDR);


CREATE TABLE PT_MNU_LOUT_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    MNU_LOUT_ID                      VARCHAR (30) NOT NULL COMMENT '메뉴레이아웃ID',
    LOUT_CAT_ID                      CHAR (1) NOT NULL COMMENT '레이아웃유형ID',
    MNU_LOUT_PID                     VARCHAR (30) COMMENT '메뉴레이아웃부모ID',
    LOUT_LOC_CD                      VARCHAR (30) COMMENT '레이아웃위치코드',
    MNU_LOUT_KND_CD                  VARCHAR (30) COMMENT '레이아웃종류코드',
    MNU_GRP_ID                       VARCHAR (30) COMMENT '메뉴그룹ID',
    MNU_GRP_RESC_ID                  VARCHAR (30) COMMENT '메뉴그룹리소스ID',
    IMG_KND_VA                       VARCHAR (100) COMMENT '이미지종류값',
    MNU_LOUT_NM                      VARCHAR (120) COMMENT '메뉴레이아웃명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (COMP_ID, MNU_LOUT_ID)
)
COMMENT = '메뉴레이아웃상세';
CREATE INDEX IDX_PT_MNU_LOUT_D_1 ON PT_MNU_LOUT_D (COMP_ID, LOUT_CAT_ID, MNU_LOUT_PID, SORT_ORDR);


CREATE TABLE PT_MY_MNU_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    MNU_LOUT_COMB_ID                 VARCHAR (30) NOT NULL COMMENT '메뉴레이아웃조합ID',
    MNU_LOUT_COMB_PID                VARCHAR (30) COMMENT '메뉴레이아웃조합부모ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    FLD_YN                           CHAR (1) COMMENT '폴더여부',
    MNU_NM                           VARCHAR (120) COMMENT '메뉴명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    MNU_ID                           VARCHAR (30) COMMENT '메뉴ID',
    MNU_RESC_ID                      VARCHAR (30) COMMENT '메뉴리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, MNU_LOUT_COMB_ID)
)
COMMENT = '나의메뉴상세';


CREATE TABLE PT_MY_PLT_R (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    PLT_ZONE_CD                      VARCHAR (30) NOT NULL COMMENT '포틀릿지역코드',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    TOP_PX                           INT COMMENT 'TOP픽셀',
    LEFT_PX                          INT COMMENT 'LEFT픽셀',
    WDTH_PX                          INT COMMENT '넓이픽셀',
    HGHT_PX                          INT COMMENT '높이픽셀',
    ZIDX                             INT COMMENT 'Z-INDEX',
    PRIMARY KEY (USER_UID, PLT_ID)
)
COMMENT = '나의포틀릿관계';


CREATE TABLE PT_PLT_D (
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    PLT_CAT_CD                       VARCHAR (30) COMMENT '포틀릿카테고리코드',
    OPEN_COMP_ID                     VARCHAR (3) COMMENT '공개회사ID',
    PLT_NM                           VARCHAR (400) COMMENT '포틀릿명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    PLT_URL                          VARCHAR (1000) COMMENT '포틀릿URL',
    MORE_URL                         VARCHAR (1000) COMMENT '모어URL',
    SETUP_URL                        VARCHAR (1000) COMMENT '설정URL',
    WDTH_PX                          INT COMMENT '넓이픽셀',
    HGHT_PX                          INT COMMENT '높이픽셀',
    PLT_DESC                         VARCHAR (400) COMMENT '포틀릿설명',
    PLT_TITL_YN                      CHAR (1) COMMENT '포틀릿타이틀여부',
    ITEM_TITL_YN                     CHAR (1) COMMENT '항목타이틀여부',
    BORD_YN                          CHAR (1) COMMENT '테두리선여부',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (PLT_ID)
)
COMMENT = '포틀릿상세';


CREATE TABLE PT_PLT_SETUP_R (
    MNU_GRP_ID                       VARCHAR (30) NOT NULL COMMENT '메뉴그룹ID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    PLT_ZONE_CD                      VARCHAR (30) NOT NULL COMMENT '포틀릿지역코드',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT COMMENT '정렬순서',
    TOP_PX                           INT COMMENT 'TOP픽셀',
    LEFT_PX                          INT COMMENT 'LEFT픽셀',
    WDTH_PX                          INT COMMENT '넓이픽셀',
    HGHT_PX                          INT COMMENT '높이픽셀',
    ZIDX                             INT COMMENT 'Z-INDEX',
    PRIMARY KEY (MNU_GRP_ID, PLT_ID)
)
COMMENT = '포틀릿설정관계';


CREATE TABLE PT_PUSH_APP_D (
    CUST_CD                          VARCHAR (30) NOT NULL COMMENT '고객코드',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    CUST_NM                          VARCHAR (200) COMMENT '고객명',
    STRT_YMD                         DATETIME COMMENT '시작년월일',
    END_YMD                          DATETIME COMMENT '종료년월일',
    IPA_PATH                         VARCHAR (240) COMMENT 'IPA경로',
    APK_PATH                         VARCHAR (240) COMMENT 'APK경로',
    DISP_NM                          VARCHAR (200) COMMENT '표시명',
    DESC_CONT                        VARCHAR (2000) COMMENT '설명내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    PRIMARY KEY (CUST_CD, SEQ)
)
COMMENT = '푸쉬앱상세';


CREATE TABLE PT_PUSH_MSG_D (
    PUSH_MSG_ID                      VARCHAR (64) NOT NULL COMMENT '푸쉬메시지ID',
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    MD_RID                           VARCHAR (30) NOT NULL COMMENT '모듈참조ID',
    PUSH_SUBJ                        VARCHAR (400) COMMENT '푸쉬제목',
    WEB_URL                          VARCHAR (1000) COMMENT '웹URL',
    WEB_AUTH_URL                     VARCHAR (1000) COMMENT '웹권한URL',
    MOB_URL                          VARCHAR (1000) COMMENT '모바일URL',
    MOB_AUTH_URL                     VARCHAR (1000) COMMENT '모방일권한URL',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    ISU_DT                           DATETIME COMMENT '발행일시',
    VALD_LGIN_CNT                    INT COMMENT '유효로그인수',
    PRIMARY KEY (PUSH_MSG_ID)
)
COMMENT = '푸쉬메시지상세';
CREATE INDEX IDX_PT_PUSH_MSG_D_1 ON PT_PUSH_MSG_D (ISU_DT, USER_UID);


CREATE TABLE PT_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE PT_RESC_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (COMP_ID, RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스상세';


CREATE TABLE PT_SSO_MSG_T (
    MSG_ID                           VARCHAR (30) NOT NULL COMMENT '메세지ID',
    MSG_VA                           VARCHAR (500) NOT NULL COMMENT '메세지값',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    PRIMARY KEY (MSG_ID)
)
COMMENT = 'SSO메세지임시';


CREATE TABLE PT_SSO_T (
    SSO_ID                           VARCHAR (30) NOT NULL COMMENT 'SSO ID',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    TGT_URL                          VARCHAR (100) COMMENT '대상URL',
    AUTH_URL                         VARCHAR (100) COMMENT '권한URL',
    REG_DT                           DATETIME NOT NULL COMMENT '등록일시',
    PRIMARY KEY (SSO_ID)
)
COMMENT = 'SSO임시';


CREATE TABLE PT_SYS_SETUP_D (
    SETUP_CLS_ID                     VARCHAR (30) NOT NULL COMMENT '설정분류ID',
    SETUP_ID                         VARCHAR (100) NOT NULL COMMENT '설정ID',
    SETUP_VA                         VARCHAR (800) COMMENT '설정값',
    SORT_ORDR                        INT COMMENT '정렬순서',
    PRIMARY KEY (SETUP_CLS_ID, SETUP_ID)
)
COMMENT = '시스템설정상세';


CREATE TABLE PT_TERM_SETUP_D (
    SETUP_CLS_ID                     VARCHAR (30) NOT NULL COMMENT '설정분류ID',
    SETUP_ID                         VARCHAR (100) NOT NULL COMMENT '설정ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    TERM_VA                          VARCHAR (400) COMMENT '용어값',
    PRIMARY KEY (SETUP_CLS_ID, SETUP_ID, LANG_TYP_CD)
)
COMMENT = '용어설정상세';


CREATE TABLE PT_USER_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SETUP_CLS_ID                     VARCHAR (30) NOT NULL COMMENT '설정분류ID',
    SETUP_ID                         VARCHAR (100) NOT NULL COMMENT '설정ID',
    SETUP_VA                         VARCHAR (800) COMMENT '설정값',
    CACHE_YN                         CHAR (1) COMMENT '캐쉬여부',
    PRIMARY KEY (USER_UID, SETUP_CLS_ID, SETUP_ID)
)
COMMENT = '사용자설정상세';


CREATE TABLE PT_USER_UAGNT_D (
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    UAGNT_HASH_VA                    INT NOT NULL COMMENT '유저에이전트해쉬값',
    UAGNT_TKN_VA                     VARCHAR (30) NOT NULL COMMENT '유저에이전트토큰값',
    PRIMARY KEY (ODUR_UID, UAGNT_HASH_VA)
)
COMMENT = '사용자유저에이전트상세';
