

/* ----------------------------
    WD
---------------------------- */


CREATE TABLE WD_ANB_B (
    YEAR                             INT NOT NULL COMMENT '기준년도',
    ANB_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '연차구분코드',
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    COMP_ID                          VARCHAR (3) COMMENT '회사ID',
    FORW_CNT                         FLOAT (5.1) COMMENT '이월수',
    FORW_MOD_CNT                     FLOAT (5.1) COMMENT '이월차감수',
    CRE_CNT                          FLOAT (5.1) COMMENT '발생수',
    CRE_MOD_CNT                      FLOAT (5.1) COMMENT '발생차감수',
    USE_CNT                          FLOAT (5.1) COMMENT '사용수',
    USE_MOD_CNT                      FLOAT (5.1) COMMENT '사용차감수',
    ONGO_CNT                         FLOAT (5.1) COMMENT '진행수',
    ONGO_MOD_CNT                     FLOAT (5.1) COMMENT '진행차감수',
    CRE_YEAR                         INT COMMENT '발생년도',
    PRIMARY KEY (YEAR, ANB_TYP_CD, ODUR_UID)
)
COMMENT = '연차기본';
CREATE INDEX IDX_WD_ANB_B_1 ON WD_ANB_B (ODUR_UID, ANB_TYP_CD);


CREATE TABLE WD_ANB_MOD_L (
    YEAR                             INT NOT NULL COMMENT '기준년도',
    ANB_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '연차구분코드',
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    MOD_TYP_CD                       VARCHAR (30) COMMENT '차감구분코드',
    MOD_CNT                          FLOAT (5.1) COMMENT '차감수',
    USE_YMD                          DATETIME COMMENT '사용년월일',
    MONTH                            INT COMMENT '월',
    APV_NO                           BIGINT COMMENT '결재번호',
    RSON                             VARCHAR (400) COMMENT '근거',
    MOD_UID                          VARCHAR (30) COMMENT '차감자UID',
    MOD_DT                           DATETIME COMMENT '차감일시',
    NOTE                             VARCHAR (400) COMMENT '비고',
    PRIMARY KEY (YEAR, ANB_TYP_CD, ODUR_UID, SEQ)
)
COMMENT = '연차차감내역';


CREATE TABLE WD_ANB_USE_L (
    YEAR                             INT NOT NULL COMMENT '기준년도',
    ANB_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '연차구분코드',
    ODUR_UID                         VARCHAR (30) NOT NULL COMMENT '원직자UID',
    USE_YMD                          DATETIME NOT NULL COMMENT '사용년월일',
    CMPL_YN                          CHAR (1) COMMENT '완결여부',
    USE_CNT                          FLOAT (5.1) COMMENT '사용수',
    REG_DT                           DATETIME COMMENT '등록일',
    APV_NO                           BIGINT COMMENT '결재번호',
    RSON                             VARCHAR (400) COMMENT '근거',
    PRIMARY KEY (YEAR, ANB_TYP_CD, ODUR_UID, USE_YMD)
)
COMMENT = '연차사용내역';
CREATE INDEX IDX_WD_ANB_USE_L_1 ON WD_ANB_USE_L (ODUR_UID, USE_YMD);
