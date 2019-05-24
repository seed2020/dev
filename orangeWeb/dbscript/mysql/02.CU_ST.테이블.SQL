

/* ----------------------------
    CU
---------------------------- */


CREATE TABLE CU_PSN_CARD_B (
    CARD_NO                          BIGINT NOT NULL COMMENT '카드번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    EIN                              VARCHAR (30) COMMENT '사번',
    COMP_NM                          VARCHAR (120) COMMENT '소속법인',
    DEPT_NM                          VARCHAR (120) COMMENT '부서명',
    JOIN_DT                          DATETIME COMMENT '입사일자',
    TEAM_NM                          VARCHAR (120) COMMENT '팀명',
    POSIT_NM                         VARCHAR (60) COMMENT '직위명',
    KO_NM                            VARCHAR (30) COMMENT '한글명',
    ZN_NM                            VARCHAR (30) COMMENT '한문명',
    EN_NM                            VARCHAR (60) COMMENT '영문명',
    BODY_XML                         LONGTEXT COMMENT '본문XML',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CARD_NO)
)
COMMENT = '인사기록카드';


CREATE TABLE CU_PSN_IMG_D (
    CARD_NO                          BIGINT NOT NULL COMMENT '카드번호',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT NOT NULL COMMENT '가로길이',
    IMG_HGHT                         INT NOT NULL COMMENT '세로길이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CARD_NO)
)
COMMENT = '이미지';


CREATE TABLE CU_PSN_INFO_D (
    CARD_NO                          BIGINT NOT NULL COMMENT '카드번호',
    SSN_ENC                          VARCHAR (40) COMMENT '주민등록번호암호값',
    BIRTH_ENC                        VARCHAR (40) COMMENT '생년월일암호값',
    HOME_ZIP_NO_ENC                  VARCHAR (80) COMMENT '자택우편번호암호값',
    HOME_ADR_ENC                     VARCHAR (400) COMMENT '자택주소암호값',
    HOME_DETL_ADR_ENC                VARCHAR (400) COMMENT '자택상세주소암호값',
    MBNO_ENC                         VARCHAR (120) COMMENT '휴대전화번호암호값',
    PRIMARY KEY (CARD_NO)
)
COMMENT = '인사정보상세';


/* ----------------------------
    ST
---------------------------- */


CREATE TABLE ST_CAT_B (
    CAT_ID                           VARCHAR (30) NOT NULL COMMENT '카테고리ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CAT_NM                           VARCHAR (120) NOT NULL COMMENT '카테고리명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) COMMENT '등록자',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (CAT_ID)
)
COMMENT = '카테고리';


CREATE TABLE ST_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE ST_SITE_B (
    SITE_ID                          VARCHAR (30) NOT NULL COMMENT '사이트ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    CAT_ID                           VARCHAR (30) COMMENT '카테고리ID',
    SITE_NM                          VARCHAR (120) NOT NULL COMMENT '사이트명 ',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    CONT                             VARCHAR (240) COMMENT '내용',
    SITE_URL                         VARCHAR (240) COMMENT '사이트URL',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (SITE_ID)
)
COMMENT = '사이트';


CREATE TABLE ST_SITE_IMG_D (
    SITE_ID                          VARCHAR (30) NOT NULL COMMENT '사이트ID',
    IMG_PATH                         VARCHAR (240) NOT NULL COMMENT '이미지경로',
    IMG_WDTH                         INT NOT NULL COMMENT '가로길이',
    IMG_HGHT                         INT NOT NULL COMMENT '세로길이',
    MODR_UID                         VARCHAR (30) COMMENT '수정자',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (SITE_ID)
)
COMMENT = '이미지';


CREATE TABLE ST_SITE_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';
