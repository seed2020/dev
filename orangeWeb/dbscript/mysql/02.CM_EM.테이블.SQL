

/* ----------------------------
    CM
---------------------------- */


CREATE TABLE CM_ATT_FILE_D (
    ATT_ID                           VARCHAR (30) NOT NULL COMMENT '첨부 ID',
    ATT_FILE_ID                      VARCHAR (30) NOT NULL COMMENT '첨부 파일 ID',
    ATT_FILE_EXT                     VARCHAR (10) COMMENT '첨부 파일 확장자',
    ATT_FILE_SIZE                    INT COMMENT '첨부 파일 크기',
    ATT_FILE_ORDR                    INT COMMENT '첨부 파일 순서',
    ATT_FILE_SAVE_PATH               VARCHAR (240) COMMENT '첨부 파일 저장 위치',
    ATT_FILE_DISP_NM                 VARCHAR (240) COMMENT '첨부 파일 표시명',
    REF_ID                           VARCHAR (30) COMMENT '문서 ID',
    REG_DT                           DATETIME COMMENT '등록 일시',
    REGR_UID                         VARCHAR (30) COMMENT '등록자 UID',
    MOD_DT                           DATETIME COMMENT '수정 일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자 UID',
    PRIMARY KEY (ATT_ID, ATT_FILE_ID)
)
COMMENT = '첨부 파일 상세';


CREATE TABLE CM_EMAIL_B (
    EMAIL_ID                         INT NOT NULL COMMENT '이메일ID',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    SEND_NM                          VARCHAR (120) COMMENT '발송명',
    RECV_NM                          VARCHAR (2000) COMMENT '수신명',
    CC_NM                          VARCHAR (2000) COMMENT '참조명',
    BCC_NM                          VARCHAR (2000) COMMENT '숨은참조명',
    CONT                             LONGTEXT COMMENT '내용',
    FILE_YN                          CHAR (1) COMMENT '파일여부',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (EMAIL_ID)
)
COMMENT = '이메일기본';


CREATE TABLE CM_EMAIL_FILE_D (
    EMAIL_ID                         INT NOT NULL COMMENT '이메일ID',
    DISP_ORDR                        INT NOT NULL COMMENT '표시순서',
    DISP_NM                          VARCHAR (240) NOT NULL COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    FILE_CONT                        LONGBLOB COMMENT '파일내용',
    PRIMARY KEY (EMAIL_ID, DISP_ORDR)
)
COMMENT = '이메일첨부파일상세';


CREATE TABLE CM_SRCH_B (
    SRCH_NO                          BIGINT NOT NULL COMMENT '검색번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    MD_RID                           VARCHAR (30) NOT NULL COMMENT '모듈참조ID',
    MD_BX_ID                         VARCHAR (30) COMMENT '모듈함ID',
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    MD_BX_RESC_ID                    VARCHAR (30) COMMENT '모듈함리소스ID',
    TBL_ID                           VARCHAR (30) COMMENT '테이블ID',
    ACT_ID                           CHAR (1) NOT NULL COMMENT '액션ID',
    URL                              VARCHAR (1000) COMMENT 'URL',
    REG_DT                           DATETIME COMMENT '등록일시',
    PRIMARY KEY (SRCH_NO)
)
COMMENT = '검색기본';
CREATE INDEX IDX_CM_SRCH_B_1 ON CM_SRCH_B (MD_RID, MD_BX_ID, MD_ID);


CREATE TABLE CM_SRCH_ERR_D (
    SRCH_NO                          BIGINT NOT NULL COMMENT '검색번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    MD_RID                           VARCHAR (30) NOT NULL COMMENT '모듈참조ID',
    MD_BX_ID                         VARCHAR (30) COMMENT '모듈함ID',
    MD_ID                            VARCHAR (30) NOT NULL COMMENT '모듈ID',
    MD_BX_RESC_ID                    VARCHAR (30) COMMENT '모듈함리소스ID',
    TBL_ID                           VARCHAR (30) COMMENT '테이블ID',
    ACT_ID                           CHAR (1) NOT NULL COMMENT '액션ID',
    URL                              VARCHAR (1000) COMMENT 'URL',
    REG_DT                           DATETIME COMMENT '등록일시',
    MSG                              VARCHAR (2000) COMMENT '메시지',
    PRIMARY KEY (SRCH_NO)
)
COMMENT = '검색오류상세';


/* ----------------------------
    EM
---------------------------- */


CREATE TABLE EM_ADR_BLD_INFO_B (
    LOW_TOWN_CD                      VARCHAR (10) NOT NULL COMMENT '법정동코드',
    MT_YN                            CHAR (1) COMMENT '산여부',
    MAIN_LOT_NO                      INT COMMENT '지번본번(번지)',
    SUB_LOT_NO                       INT COMMENT '지번부번(호)',
    ROAD_NM_CD                       VARCHAR (12) COMMENT '도로명코드',
    BASEMENT_YN                      CHAR (1) COMMENT '지하여부',
    MAIN_BLD_NO                      INT COMMENT '건물본번',
    SUB_BLD_NO                       INT COMMENT '건물부번',
    BLD_MNG_NO                       VARCHAR (25) NOT NULL COMMENT '건물관리번호',
    ZIP_NO                           VARCHAR (10) COMMENT '우편번호',
    CITY_PUBLIC_BLD_NM               VARCHAR (200) COMMENT '시군구용 건물명',
    APT_HOURSE_YN                    CHAR (1) COMMENT '공동주택여부',
    DFT_ZONE_NO                      VARCHAR (5) COMMENT '기초구역번호',
    PRIMARY KEY (BLD_MNG_NO)
)
COMMENT = '건물주소기본';
CREATE INDEX IDX_EM_ADR_BLD_INFO_B_1 ON EM_ADR_BLD_INFO_B(ROAD_NM_CD, MAIN_BLD_NO);
CREATE INDEX IDX_EM_ADR_BLD_INFO_B_2 ON EM_ADR_BLD_INFO_B(LOW_TOWN_CD);
-- CREATE INDEX IDX_EM_ADR_BLD_INFO_B_1 ON EM_ADR_BLD_INFO_B (LOW_TOWN_CD, MAIN_LOT_NO, ROAD_NM_CD, MAIN_BLD_NO, CITY_PUBLIC_BLD_NM);


CREATE TABLE EM_ADR_DONG_B (
    LOW_TOWN_CD                      VARCHAR (10) NOT NULL COMMENT '법정동코드',
    DONG                             VARCHAR (40) NOT NULL COMMENT '법정읍면동명',
    RI                               VARCHAR (40) COMMENT '법정리명',
    PRIMARY KEY (LOW_TOWN_CD)
)
COMMENT = '동기본';


CREATE TABLE EM_ADR_GUGUN_B (
    GUGUN_CD                         VARCHAR (5) NOT NULL COMMENT '시군구코드',
    SIDO                             VARCHAR (40) NOT NULL COMMENT '시도명',
    GUGUN                            VARCHAR (40) COMMENT '시군구명',
    PRIMARY KEY (GUGUN_CD)
)
COMMENT = '시군구기본';


CREATE TABLE EM_ADR_ROAD_B (
    ROAD_NM_CD                       VARCHAR (12) NOT NULL COMMENT '도로명코드',
    ROAD_NM                          VARCHAR (80) NOT NULL COMMENT '도로명',
    PRIMARY KEY (ROAD_NM_CD)
)
COMMENT = '도로기본';


CREATE TABLE EM_MAIL_ADR_TRAN_B (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    USER_CNT                         INT COMMENT '사용자수',
    FLD_CNT                          INT COMMENT '폴더건수',
    ADR_CNT                          INT COMMENT '주소록건수',
    TRAN_STRT_DT                     DATETIME COMMENT '이관시작일시',
    TRAN_END_DT                      DATETIME COMMENT '이관종료일시',
    TRAN_PROC_STAT_CD                VARCHAR (30) COMMENT '이관진행상태코드',
    TRAN_CONT                        VARCHAR (1000) COMMENT '이관내용',
    ERR_CONT                         VARCHAR (1000) COMMENT '오류내용',
    USER_UID                         VARCHAR (30) COMMENT '사용자UID',
    PRIMARY KEY (TRAN_ID)
)
COMMENT = '메일주소록이관';


CREATE TABLE EM_MAIL_ADR_TRAN_TGT_T (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    SEQ                              INT NOT NULL COMMENT '순번',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    USER_SN                          VARCHAR (30) NOT NULL COMMENT '사용자번호',
    FLD_CNT                          INT COMMENT '폴더건수',
    ADR_CNT                          INT COMMENT '주소록건수',
    ERR_YN                           CHAR (1) COMMENT '오류여부',
    PRIMARY KEY (TRAN_ID, SEQ)
)
COMMENT = '이관대상임시';


CREATE TABLE EM_SEND_B (
    SEND_NO                          BIGINT NOT NULL COMMENT '보내기번호',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    CONT                             LONGTEXT COMMENT '내용',
    PRIMARY KEY (SEND_NO)
)
COMMENT = '보내기';


CREATE TABLE EM_SEND_EMAIL_B (
    EMAIL_NO                         BIGINT NOT NULL COMMENT '이메일번호 ',
    SUBJ                             VARCHAR (240) COMMENT '제목',
    CONT                             LONGTEXT COMMENT '내용',
    FILE_YN                          CHAR (1) COMMENT '파일여부',
    PRIMARY KEY (EMAIL_NO)
)
COMMENT = '이메일보내기';


CREATE TABLE EM_SEND_EMAIL_FILE_D (
    EMAIL_NO                         BIGINT NOT NULL COMMENT '이메일번호 ',
    FILE_SEQ                         INT NOT NULL COMMENT '파일일련번호',
    DISP_NM                          VARCHAR (240) NOT NULL COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    FILE_CONT                        LONGBLOB NOT NULL COMMENT '파일내용',
    PRIMARY KEY (EMAIL_NO, FILE_SEQ)
)
COMMENT = '이메일보내기첨부';


CREATE TABLE EM_SEND_FILE_D (
    SEND_NO                          BIGINT NOT NULL COMMENT '보내기번호',
    FILE_SEQ                         INT NOT NULL COMMENT '파일일련번호',
    DISP_NM                          VARCHAR (240) COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_SIZE                        INT COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) COMMENT '저장경로',
    PRIMARY KEY (SEND_NO, FILE_SEQ)
)
COMMENT = '보내기파일';


CREATE TABLE EM_TMP_FILE_T (
    TMP_FILE_ID                      INT NOT NULL COMMENT '임시파일ID',
    DISP_NM                          VARCHAR (240) NOT NULL COMMENT '표시이름',
    FILE_EXT                         VARCHAR (30) NOT NULL COMMENT '파일확장자',
    FILE_SIZE                        INT NOT NULL COMMENT '파일크기',
    SAVE_PATH                        VARCHAR (240) NOT NULL COMMENT '저장경로',
    PRIMARY KEY (TMP_FILE_ID)
)
COMMENT = '임시파일';


CREATE TABLE EM_USER_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    SETUP_CLS_ID                     VARCHAR (30) NOT NULL COMMENT '설정분류ID',
    SETUP_ID                         VARCHAR (60) NOT NULL COMMENT '설정ID',
    SETUP_VA                         VARCHAR (120) COMMENT '설정값',
    PRIMARY KEY (USER_UID, SETUP_CLS_ID, SETUP_ID)
)
COMMENT = '사용자설정상세';
