

/* ----------------------------
    AP
---------------------------- */


CREATE TABLE AP_AGN_APNT_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    AGNT_UID                         VARCHAR (30) NOT NULL COMMENT '대리인UID',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    END_DT                           DATETIME COMMENT '종료일시',
    STRT_DT                          DATETIME COMMENT '시작일시',
    AGNT_RESC_ID                     VARCHAR (30) COMMENT '대리인리소스ID',
    ABS_RSON_CD                      VARCHAR (30) COMMENT '부재사유코드',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (USER_UID, AGNT_UID, SEQ)
)
COMMENT = '대리지정상세';
CREATE INDEX IDX_AP_AGN_APNT_D_1 ON AP_AGN_APNT_D (AGNT_UID, END_DT, STRT_DT);


CREATE TABLE AP_AGN_APNT_R (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    AGNT_UID                         VARCHAR (30) NOT NULL COMMENT '대리인UID',
    AGNT_RESC_ID                     VARCHAR (30) COMMENT '대리인리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, AGNT_UID)
)
COMMENT = '대리지정관계';


CREATE TABLE AP_APV_LN_GRP_B (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    APV_LN_GRP_ID                    VARCHAR (30) NOT NULL COMMENT '결재라인그룹ID',
    APV_LN_GRP_NM                    VARCHAR (120) COMMENT '결재라인그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    APV_LN_GRP_TYP_CD                VARCHAR (30) COMMENT '결재라인그룹구분코드',
    PRIMARY KEY (USER_UID, APV_LN_GRP_ID)
)
COMMENT = '결재라인그룹기본';


CREATE TABLE AP_APV_LN_GRP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    APV_LN_GRP_ID                    VARCHAR (30) NOT NULL COMMENT '결재라인그룹ID',
    APV_LN_GRP_SEQ                   INT NOT NULL COMMENT '결재라인그룹일련번호',
    APVR_UID                         VARCHAR (30) COMMENT '결재자UID',
    APV_DEPT_ID                      VARCHAR (30) COMMENT '결재부서ID',
    APVR_DEPT_YN                     CHAR (1) COMMENT '결재자부서여부',
    APVR_ROLE_CD                     VARCHAR (30) COMMENT '결재자역할코드',
    DBL_APV_TYP_CD                   VARCHAR (30) COMMENT '이중결재구분코드',
    ABS_RSON_CD                      VARCHAR (30) COMMENT '부재사유코드',
    PRIMARY KEY (USER_UID, APV_LN_GRP_ID, APV_LN_GRP_SEQ)
)
COMMENT = '결재라인그룹상세';


CREATE TABLE AP_CLS_INFO_D (
    ORG_ID                           VARCHAR (30) NOT NULL COMMENT '조직ID',
    CLS_INFO_ID                      VARCHAR (30) NOT NULL COMMENT '분류정보ID',
    CLS_INFO_PID                     VARCHAR (30) NOT NULL COMMENT '분류정보부모ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    CLS_INFO_NM                      VARCHAR (120) COMMENT '분류정보명',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    SYS_CLS_INFO_YN                  CHAR (1) NOT NULL COMMENT '시스템분류정보여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (ORG_ID, CLS_INFO_ID)
)
COMMENT = '분류정보상세';


CREATE TABLE AP_ERP_FORM_B (
    ERP_FORM_ID                      VARCHAR (30) NOT NULL COMMENT 'ERP양식ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    ERP_FORM_TYP_CD                  VARCHAR (30) COMMENT 'ERP양식구분코드',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    REG_URL                          VARCHAR (1000) COMMENT '등록URL',
    INFM_URL                         VARCHAR (1000) COMMENT '통보URL',
    PRIMARY KEY (ERP_FORM_ID)
)
COMMENT = 'ERP양식기본';


CREATE TABLE AP_ERP_INTG_B (
    INTG_NO                          BIGINT NOT NULL COMMENT '연계번호',
    APV_NO                           BIGINT COMMENT '결재번호',
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    RID                              VARCHAR (400) COMMENT '참조ID',
    INTG_STAT_CD                     VARCHAR (30) NOT NULL COMMENT '연계상태코드',
    DOC_SUBJ                         VARCHAR (400) COMMENT '문서제목',
    BODY_HTML                        LONGTEXT COMMENT '본문HTML',
    REG_DT                           DATETIME COMMENT '등록일시',
    INTG_URL                         VARCHAR (1000) COMMENT '연계URL',
    INTG_TYP_CD                      VARCHAR (30) COMMENT '연계구분코드',
    FORM_NM                          VARCHAR (240) COMMENT '양식명',
    REF_VA                           VARCHAR (2000) COMMENT '참조값',
    PRIMARY KEY (INTG_NO)
)
COMMENT = 'ERP연계기본';


CREATE TABLE AP_ERP_INTG_CHIT_D (
    INTG_NO                          BIGINT NOT NULL COMMENT '연계번호',
    CHIT_FORM_ID                     VARCHAR (30) NOT NULL COMMENT '전표양식번호',
    FORM_NM                          VARCHAR (240) COMMENT '양식명',
    CHIT_BODY_HTML                   LONGTEXT COMMENT '전표본문HTML',
    PRIMARY KEY (INTG_NO)
)
COMMENT = 'ERP연계전표상세';


CREATE TABLE AP_ERP_INTG_FILE_D (
    INTG_NO                          BIGINT NOT NULL COMMENT '연계번호',
    SEQ                              INT NOT NULL COMMENT '일련번호',
    ATT_DISP_NM                      VARCHAR (240) NOT NULL COMMENT '첨부표시명',
    FILE_EXT                         VARCHAR (30) NOT NULL COMMENT '파일확장자',
    FILE_KB                          INT COMMENT '파일K바이트',
    FILE_CONT                        LONGBLOB COMMENT '파일내용',
    PRIMARY KEY (INTG_NO, SEQ)
)
COMMENT = 'ERP연계파일상세';


CREATE TABLE AP_FORM_APV_LN_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    APV_LN_TITL_TYP_CD               VARCHAR (30) NOT NULL COMMENT '결재라인타이틀구분코드',
    APV_LN_DISP_TYP_CD               VARCHAR (30) COMMENT '결재라인표시구분코드',
    MAX_CNT                          INT COMMENT '최대개수',
    ALN_VA                           VARCHAR (30) COMMENT '줄맞춤값',
    BORD_USE_YN                      CHAR (1) COMMENT '테두리선사용여부',
    TITL_USE_YN                      CHAR (1) COMMENT '타이틀사용여부',
    LST_DUP_DISP_YN                  CHAR (1) COMMENT '목록중복표시여부',
    PRIMARY KEY (FORM_ID, FORM_COMB_SEQ, APV_LN_TITL_TYP_CD)
)
COMMENT = '양식결재라인상세';


CREATE TABLE AP_FORM_B (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    FORM_NM                          VARCHAR (240) COMMENT '양식명',
    FORM_BX_ID                       VARCHAR (30) NOT NULL COMMENT '양식함ID',
    FORM_TYP_CD                      VARCHAR (30) COMMENT '양식구분코드',
    INTG_ID                          VARCHAR (30) COMMENT '연계ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    BODY_HGHT_PX                     INT COMMENT '본문높이픽셀',
    FORM_WDTH_TYP_CD                 VARCHAR (30) COMMENT '양식넓이구분코드',
    FORM_APV_LN_TYP_CD               VARCHAR (30) COMMENT '양식결재라인구분코드',
    TPL_YN                           CHAR (1) NOT NULL COMMENT '템플릿여부',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    BODY_HTML                        LONGTEXT COMMENT '본문HTML',
    ERP_FORM_ID                      VARCHAR (30) COMMENT 'ERP양식ID',
    ERP_FORM_TYP_CD                  VARCHAR (30) COMMENT 'ERP양식구분코드',
    DOC_KEEP_PRD_CD                  VARCHAR (30) COMMENT '문서보존기간코드',
    FORM_MAGN_VA                     VARCHAR (80) COMMENT '양식여백값',
    INTG_FORM_YN                     CHAR (1) COMMENT '연계양식여부',
    BODY_OLINE_YN                    CHAR (1) COMMENT '본문외곽선여부',
    APV_LN_GRP_ID                    VARCHAR (30) COMMENT '결재라인그룹ID',
    FIXD_APVR_YN                     CHAR (1) COMMENT '고정결재자여부',
    AUTO_APV_LN_CD                   VARCHAR (30) COMMENT '자동결재선코드',
    DOC_SUBJ_CD                      VARCHAR (30) COMMENT '문서제목코드',
    REF_VW_GRP_ID                    VARCHAR (30) COMMENT '참조열람그룹ID',
    REF_VW_FIXD_APVR_YN              CHAR (1) COMMENT '참조열람고정결재자여부',
    PRIMARY KEY (FORM_ID)
)
COMMENT = '양식기본';
CREATE INDEX IDX_AP_FORM_B_1 ON AP_FORM_B (FORM_BX_ID, FORM_TYP_CD);
CREATE INDEX IDX_AP_FORM_B_2 ON AP_FORM_B (INTG_ID);
CREATE INDEX IDX_AP_FORM_B_3 ON AP_FORM_B (COMP_ID, TPL_YN);
CREATE INDEX IDX_AP_FORM_B_4 ON AP_FORM_B (ERP_FORM_ID);


CREATE TABLE AP_FORM_BX_D (
    FORM_BX_ID                       VARCHAR (30) NOT NULL COMMENT '양식함ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    FORM_BX_NM                       VARCHAR (240) COMMENT '양식함명',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    FORM_BX_PID                      VARCHAR (30) NOT NULL COMMENT '양식함부모ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (FORM_BX_ID)
)
COMMENT = '양식함상세';
CREATE INDEX IDX_AP_FORM_BX_D_1 ON AP_FORM_BX_D (COMP_ID);


CREATE TABLE AP_FORM_BX_DEPT_R (
    FORM_BX_ID                       VARCHAR (30) NOT NULL COMMENT '양식함ID',
    FORM_BX_DEPT_ID                  VARCHAR (30) NOT NULL COMMENT '양식부서ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (FORM_BX_ID, FORM_BX_DEPT_ID)
)
COMMENT = '양식함부서관계';


CREATE TABLE AP_FORM_COMB_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_COMB_ID                     VARCHAR (30) NOT NULL COMMENT '양식구성ID',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (FORM_ID, FORM_COMB_ID, FORM_COMB_SEQ)
)
COMMENT = '양식구성상세';


CREATE TABLE AP_FORM_IMG_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_IMG_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '양식이미지구분코드',
    IMG_PATH                         VARCHAR (240) COMMENT '이미지경로',
    IMG_WDTH                         INT COMMENT '이미지넓이',
    IMG_HGHT                         INT COMMENT '이미지높이',
    PRIMARY KEY (FORM_ID, FORM_IMG_TYP_CD)
)
COMMENT = '양식이미지상세';


CREATE TABLE AP_FORM_ITEM_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    ROW_CNT                          INT NOT NULL COMMENT '줄수',
    COL_CNT                          INT NOT NULL COMMENT '열수',
    PRIMARY KEY (FORM_ID, FORM_COMB_SEQ)
)
COMMENT = '양식항목상세';


CREATE TABLE AP_FORM_ITEM_L (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    ROW_NO                           INT NOT NULL COMMENT '줄번호',
    COL_NO                           INT NOT NULL COMMENT '열번호',
    CSPN_VA                          INT NOT NULL COMMENT '콜스판값',
    ITEM_ID                          VARCHAR (30) COMMENT '항목ID',
    PRIMARY KEY (FORM_ID, FORM_COMB_SEQ, ROW_NO, COL_NO)
)
COMMENT = '양식항목내역';


CREATE TABLE AP_FORM_TXT_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_TXT_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '양식텍스트구분코드',
    TXT_CONT                         VARCHAR (240) COMMENT '텍스트내용',
    TXT_FONT_VA                      VARCHAR (50) COMMENT '텍스트폰트값',
    TXT_STYL_VA                      VARCHAR (50) COMMENT '텍스트스타일값',
    TXT_SIZE                         VARCHAR (20) COMMENT '텍스트크기',
    TXT_COLR_VA                      VARCHAR (20) COMMENT '텍스트색상값',
    PRIMARY KEY (FORM_ID, FORM_TXT_TYP_CD)
)
COMMENT = '양식텍스트상세';


CREATE TABLE AP_FORM_XML_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_XML_CD                      VARCHAR (30) NOT NULL COMMENT '양식XML코드',
    FORM_XML_CONT                    LONGTEXT COMMENT '양식XML내용',
    PRIMARY KEY (FORM_ID, FORM_XML_CD)
)
COMMENT = '양식XML상세';


CREATE TABLE AP_ONGD_APV_LN_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    APV_LN_HST_NO                    INT NOT NULL COMMENT '결재라인이력번호',
    APV_LN_PNO                       INT NOT NULL COMMENT '결재라인부모번호',
    APV_LN_NO                        INT NOT NULL COMMENT '결재라인번호',
    DBL_APV_TYP_CD                   VARCHAR (30) COMMENT '이중결재구분코드',
    APVR_ROLE_CD                     VARCHAR (30) NOT NULL COMMENT '결재자역할코드',
    APV_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '결재상태코드',
    ABS_RSON_CD                      VARCHAR (30) COMMENT '부재사유코드',
    ABS_RSON_NM                      VARCHAR (120) COMMENT '부재사유명',
    APVR_DEPT_YN                     CHAR (1) NOT NULL COMMENT '결재자부서여부',
    PICH_APNT_YN                     CHAR (1) COMMENT '담당자지정여부',
    PRED_YN                          CHAR (1) COMMENT '전결여부',
    AGNT_UID                         VARCHAR (30) COMMENT '대결자UID',
    APVR_UID                         VARCHAR (30) COMMENT '결재자UID',
    APVR_NM                          VARCHAR (120) COMMENT '결재자명',
    APVR_POSIT_CD                    VARCHAR (30) COMMENT '결재자직위코드',
    APVR_POSIT_NM                    VARCHAR (120) COMMENT '결재자직위명',
    APVR_TITLE_CD                    VARCHAR (30) COMMENT '결재자직책코드',
    APVR_TITLE_NM                    VARCHAR (120) COMMENT '결재자직책명',
    APV_DEPT_ID                      VARCHAR (30) COMMENT '결재부서ID',
    APV_DEPT_NM                      VARCHAR (120) COMMENT '결재부서명',
    APV_DEPT_ABBR_NM                 VARCHAR (120) COMMENT '결재부서약어명',
    POSIT_DISP_VA                    VARCHAR (120) COMMENT '직위표시값',
    SIGN_DISP_VA                     VARCHAR (120) COMMENT '서명표시값',
    DT_DISP_VA                       VARCHAR (120) COMMENT '일시표시값',
    SIGN_IMG_PATH                    VARCHAR (240) COMMENT '서명이미지경로',
    VW_DT                            DATETIME COMMENT '조회일시',
    APV_DT                           DATETIME COMMENT '결재일시',
    PREV_APVR_APV_DT                 DATETIME COMMENT '이전결재자결재일시',
    APV_OPIN_CONT                    VARCHAR (2000) COMMENT '결재의견내용',
    APV_OPIN_DISP_YN                 CHAR (1) COMMENT '결재의견표시여부',
    PREV_BODY_HST_NO                 INT COMMENT '이전본문이력번호',
    HOLD_BODY_HST_NO                 INT COMMENT '보류본문이력번호',
    PREV_APV_LN_HST_NO               INT COMMENT '이전결재라인이력번호',
    HOLD_APV_LN_HST_NO               INT COMMENT '보류결재라인이력번호',
    PREV_ATT_HST_NO                  INT COMMENT '이전첨부이력번호',
    HOLD_ATT_HST_NO                  INT COMMENT '보류첨부이력번호',
    PREV_RECV_DEPT_HST_NO            INT COMMENT '이전수신처이력번호',
    HOLD_RECV_DEPT_HST_NO            INT COMMENT '보류수신처이력번호',
    PREV_REF_DOC_HST_NO              INT COMMENT '이전참조문서이력번호',
    HOLD_REF_DOC_HST_NO              INT COMMENT '보류참조문서이력번호',
    FIXD_APVR_YN                     CHAR (1) COMMENT '고정결재자여부',
    PRIMARY KEY (APV_NO, APV_LN_PNO, APV_LN_NO)
)
COMMENT = '진행문서결재라인상세';
CREATE INDEX IDX_AP_ONGD_APV_LN_D_1 ON AP_ONGD_APV_LN_D (APVR_UID, APV_STAT_CD, APVR_ROLE_CD, APV_NO);
CREATE INDEX IDX_AP_ONGD_APV_LN_D_2 ON AP_ONGD_APV_LN_D (APV_DEPT_ID, APV_STAT_CD, APVR_ROLE_CD, APV_NO);


CREATE TABLE AP_ONGD_APV_LN_E (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    APV_LN_HST_NO                    INT NOT NULL COMMENT '결재라인이력번호',
    APV_LN_PNO                       INT NOT NULL COMMENT '결재라인부모번호',
    APV_LN_NO                        INT NOT NULL COMMENT '결재라인번호',
    DBL_APV_TYP_CD                   VARCHAR (30) COMMENT '이중결재구분코드',
    APVR_ROLE_CD                     VARCHAR (30) NOT NULL COMMENT '결재자역할코드',
    APV_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '결재상태코드',
    ABS_RSON_CD                      VARCHAR (30) COMMENT '부재사유코드',
    ABS_RSON_NM                      VARCHAR (120) COMMENT '부재사유명',
    APVR_DEPT_YN                     CHAR (1) NOT NULL COMMENT '결재자부서여부',
    PICH_APNT_YN                     CHAR (1) COMMENT '담당자지정여부',
    PRED_YN                          CHAR (1) COMMENT '전결여부',
    AGNT_UID                         VARCHAR (30) COMMENT '대결자UID',
    APVR_UID                         VARCHAR (30) COMMENT '결재자UID',
    APVR_NM                          VARCHAR (120) COMMENT '결재자명',
    APVR_POSIT_CD                    VARCHAR (30) COMMENT '결재자직위코드',
    APVR_POSIT_NM                    VARCHAR (120) COMMENT '결재자직위명',
    APVR_TITLE_CD                    VARCHAR (30) COMMENT '결재자직책코드',
    APVR_TITLE_NM                    VARCHAR (120) COMMENT '결재자직책명',
    APV_DEPT_ID                      VARCHAR (30) COMMENT '결재부서ID',
    APV_DEPT_NM                      VARCHAR (120) COMMENT '결재부서명',
    APV_DEPT_ABBR_NM                 VARCHAR (120) COMMENT '결재부서약어명',
    POSIT_DISP_VA                    VARCHAR (120) COMMENT '직위표시값',
    SIGN_DISP_VA                     VARCHAR (120) COMMENT '서명표시값',
    DT_DISP_VA                       VARCHAR (120) COMMENT '일시표시값',
    SIGN_IMG_PATH                    VARCHAR (240) COMMENT '서명이미지경로',
    VW_DT                            DATETIME COMMENT '조회일시',
    APV_DT                           DATETIME COMMENT '결재일시',
    PREV_APVR_APV_DT                 DATETIME COMMENT '이전결재자결재일시',
    APV_OPIN_CONT                    VARCHAR (2000) COMMENT '결재의견내용',
    APV_OPIN_DISP_YN                 CHAR (1) COMMENT '결재의견표시여부',
    PREV_BODY_HST_NO                 INT COMMENT '이전본문이력번호',
    HOLD_BODY_HST_NO                 INT COMMENT '보류본문이력번호',
    PREV_APV_LN_HST_NO               INT COMMENT '이전결재라인이력번호',
    HOLD_APV_LN_HST_NO               INT COMMENT '보류결재라인이력번호',
    PREV_ATT_HST_NO                  INT COMMENT '이전첨부이력번호',
    HOLD_ATT_HST_NO                  INT COMMENT '보류첨부이력번호',
    PREV_RECV_DEPT_HST_NO            INT COMMENT '이전수신처이력번호',
    HOLD_RECV_DEPT_HST_NO            INT COMMENT '보류수신처이력번호',
    PREV_REF_DOC_HST_NO              INT COMMENT '이전참조문서이력번호',
    HOLD_REF_DOC_HST_NO              INT COMMENT '보류참조문서이력번호',
    FIXD_APVR_YN                     CHAR (1) COMMENT '고정결재자여부',
    PRIMARY KEY (APV_NO, APV_LN_PNO, APV_LN_NO)
)
COMMENT = '진행문서결재라인이행';


CREATE TABLE AP_ONGD_APV_LN_H (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    APV_LN_HST_NO                    INT NOT NULL COMMENT '결재라인이력번호',
    APV_LN_PNO                       INT NOT NULL COMMENT '결재라인부모번호',
    APV_LN_NO                        INT NOT NULL COMMENT '결재라인번호',
    DBL_APV_TYP_CD                   VARCHAR (30) COMMENT '이중결재구분코드',
    APVR_ROLE_CD                     VARCHAR (30) NOT NULL COMMENT '결재자역할코드',
    APV_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '결재상태코드',
    ABS_RSON_CD                      VARCHAR (30) COMMENT '부재사유코드',
    ABS_RSON_NM                      VARCHAR (120) COMMENT '부재사유명',
    APVR_DEPT_YN                     CHAR (1) NOT NULL COMMENT '결재자부서여부',
    PICH_APNT_YN                     CHAR (1) COMMENT '담당자지정여부',
    AGNT_UID                         VARCHAR (30) COMMENT '대결자UID',
    PRED_YN                          CHAR (1) COMMENT '전결여부',
    APVR_UID                         VARCHAR (30) COMMENT '결재자UID',
    APVR_NM                          VARCHAR (120) COMMENT '결재자명',
    APVR_POSIT_CD                    VARCHAR (30) COMMENT '결재자직위코드',
    APVR_POSIT_NM                    VARCHAR (120) COMMENT '결재자직위명',
    APVR_TITLE_CD                    VARCHAR (30) COMMENT '결재자직책코드',
    APVR_TITLE_NM                    VARCHAR (120) COMMENT '결재자직책명',
    APV_DEPT_ID                      VARCHAR (30) COMMENT '결재부서ID',
    APV_DEPT_NM                      VARCHAR (120) COMMENT '결재부서명',
    APV_DEPT_ABBR_NM                 VARCHAR (120) COMMENT '결재부서약어명',
    POSIT_DISP_VA                    VARCHAR (120) COMMENT '직위표시값',
    SIGN_DISP_VA                     VARCHAR (120) COMMENT '서명표시값',
    DT_DISP_VA                       VARCHAR (120) COMMENT '일시표시값',
    SIGN_IMG_PATH                    VARCHAR (240) COMMENT '서명이미지경로',
    VW_DT                            DATETIME COMMENT '조회일시',
    APV_DT                           DATETIME COMMENT '결재일시',
    PREV_APVR_APV_DT                 DATETIME COMMENT '이전결재자결재일시',
    APV_OPIN_CONT                    VARCHAR (2000) COMMENT '결재의견내용',
    APV_OPIN_DISP_YN                 CHAR (1) COMMENT '결재의견표시여부',
    PREV_BODY_HST_NO                 INT COMMENT '이전본문이력번호',
    HOLD_BODY_HST_NO                 INT COMMENT '보류본문이력번호',
    PREV_APV_LN_HST_NO               INT COMMENT '이전결재라인이력번호',
    HOLD_APV_LN_HST_NO               INT COMMENT '보류결재라인이력번호',
    PREV_ATT_HST_NO                  INT COMMENT '이전첨부이력번호',
    HOLD_ATT_HST_NO                  INT COMMENT '보류첨부이력번호',
    PREV_RECV_DEPT_HST_NO            INT COMMENT '이전수신처이력번호',
    HOLD_RECV_DEPT_HST_NO            INT COMMENT '보류수신처이력번호',
    PREV_REF_DOC_HST_NO              INT COMMENT '이전참조문서이력번호',
    HOLD_REF_DOC_HST_NO              INT COMMENT '보류참조문서이력번호',
    FIXD_APVR_YN                     CHAR (1) COMMENT '고정결재자여부',
    PRIMARY KEY (APV_NO, APV_LN_HST_NO, APV_LN_PNO, APV_LN_NO)
)
COMMENT = '진행문서결재라인이력';
CREATE INDEX IDX_AP_ONGD_APV_LN_H_1 ON AP_ONGD_APV_LN_H (APV_NO, APV_LN_PNO, APV_LN_HST_NO);


CREATE TABLE AP_ONGD_ATT_FILE_L (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    ATT_HST_NO                       INT NOT NULL COMMENT '첨부이력번호',
    ATT_SEQ                          INT NOT NULL COMMENT '첨부일련번호',
    ATT_DISP_NM                      VARCHAR (240) NOT NULL COMMENT '첨부표시명',
    FILE_EXT                         VARCHAR (30) COMMENT '파일확장자',
    FILE_KB                          INT COMMENT '파일K바이트',
    FILE_PATH                        VARCHAR (240) COMMENT '파일경로',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (APV_NO, ATT_HST_NO, ATT_SEQ)
)
COMMENT = '진행문서첨부파일내역';


CREATE TABLE AP_ONGD_B (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    DOC_NO                           VARCHAR (60) COMMENT '문서번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    DOC_SUBJ                         VARCHAR (400) COMMENT '문서제목',
    ATT_FILE_YN                      CHAR (1) NOT NULL COMMENT '첨부파일여부',
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_APV_LN_TYP_CD               VARCHAR (30) NOT NULL COMMENT '양식결재라인구분코드',
    FORM_NM                          VARCHAR (240) COMMENT '양식명',
    BODY_HGHT_PX                     INT COMMENT '본문높이픽셀',
    FORM_WDTH_TYP_CD                 VARCHAR (30) COMMENT '양식넓이구분코드',
    DOC_LANG_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '문서언어구분코드',
    DOC_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '문서상태코드',
    DOC_PROS_STAT_CD                 VARCHAR (30) NOT NULL COMMENT '문서승인상태코드',
    DOC_KEEP_PRD_CD                  VARCHAR (30) COMMENT '문서보존기간코드',
    DOC_TYP_CD                       VARCHAR (30) NOT NULL COMMENT '문서구분코드',
    SECUL_CD                         VARCHAR (30) COMMENT '보안등급코드',
    UGNT_DOC_YN                      CHAR (1) COMMENT '긴급문서여부',
    ALL_READ_YN                      CHAR (1) COMMENT '전체열람여부',
    REG_REC_LST_REG_YN               CHAR (1) COMMENT '등록대장등록여부',
    REG_REC_LST_REG_SKED_YMD         DATETIME COMMENT '등록대장등록예정년월일',
    SECU_DOC_YN                      CHAR (1) COMMENT '보안문서여부',
    DOC_PW_ENC                       VARCHAR (240) COMMENT '문서비밀번호암호값',
    SENDR_NM_RESC_ID                 VARCHAR (50) COMMENT '발신명의리소스ID',
    SENDR_NM_RESC_NM                 VARCHAR (400) COMMENT '발신명의리소스명',
    OFSE_PATH                        VARCHAR (240) COMMENT '관인경로',
    OFSE_HGHT_PX                     INT COMMENT '관인높이픽셀',
    CLS_INFO_ID                      VARCHAR (30) COMMENT '분류정보ID',
    PSN_CLS_INFO_ID                  VARCHAR (30) COMMENT '개인분류정보ID',
    APV_LN_TYP_CD                    VARCHAR (30) NOT NULL COMMENT '결재라인구분코드',
    BODY_HST_NO                      INT NOT NULL COMMENT '본문이력번호',
    ATT_HST_NO                       INT NOT NULL COMMENT '첨부이력번호',
    RECV_DEPT_HST_NO                 INT NOT NULL COMMENT '수신처이력번호',
    REF_DOC_HST_NO                   INT NOT NULL COMMENT '참조문서이력번호',
    MAK_DT                           DATETIME COMMENT '기안일시',
    MAKR_UID                         VARCHAR (30) COMMENT '기안자UID',
    MAKR_NM                          VARCHAR (120) COMMENT '기안자명',
    MAK_DEPT_ID                      VARCHAR (30) COMMENT '기안부서ID',
    MAK_DEPT_NM                      VARCHAR (120) COMMENT '기안부서명',
    CUR_APVR_ID                      VARCHAR (30) COMMENT '현재결재자ID',
    CUR_APVR_NM                      VARCHAR (120) COMMENT '현재결재자명',
    CUR_APVR_DEPT_YN                 CHAR (1) COMMENT '현재결재자부서여부',
    CMPL_DT                          DATETIME COMMENT '완결일시',
    CMPLR_UID                        VARCHAR (30) COMMENT '완결자UID',
    CMPLR_NM                         VARCHAR (120) COMMENT '완결자명',
    ENFC_SCOP_CD                     VARCHAR (30) COMMENT '시행범위코드',
    ENFC_DT                          DATETIME COMMENT '시행일시',
    RECV_DOC_NO                      VARCHAR (60) COMMENT '접수문서번호',
    RECV_DT                          DATETIME COMMENT '접수일시',
    ENFC_DOC_KEEP_PRD_CD             VARCHAR (30) COMMENT '시행문서보존기간코드',
    ENFC_STAT_CD                     VARCHAR (30) COMMENT '시행상태코드',
    SEND_INST_ID                     VARCHAR (30) COMMENT '발송기관ID',
    SEND_INST_NM                     VARCHAR (120) COMMENT '발송기관명',
    REC_LST_TYP_CD                   VARCHAR (30) COMMENT '대장구분코드',
    REC_LST_DEPT_ID                  VARCHAR (30) COMMENT '대장부서ID',
    ORGN_APV_NO                      BIGINT COMMENT '원본결재번호',
    TRX_APV_NO                       BIGINT COMMENT '변환결재번호',
    INTG_TYP_CD                      VARCHAR (30) COMMENT '연계구분코드',
    INTG_NO                          BIGINT COMMENT '연계번호',
    SENDR_NM_ORG_ID                  VARCHAR (30) COMMENT '발신명의조직ID',
    SEND_WITH_REF_DOC_YN             CHAR (1) COMMENT '참조문서포함발송여부',
    FOOTER_VA                        VARCHAR (400) COMMENT '바닥글값',
    XML_TYP_ID                       VARCHAR (30) COMMENT 'XML구분ID',
    PRIMARY KEY (APV_NO)
)
COMMENT = '진행문서기본';
CREATE INDEX IDX_AP_ONGD_B_1 ON AP_ONGD_B (REC_LST_DEPT_ID, REC_LST_TYP_CD, ALL_READ_YN);
CREATE INDEX IDX_AP_ONGD_B_2 ON AP_ONGD_B (CLS_INFO_ID, REC_LST_TYP_CD, REC_LST_DEPT_ID);
CREATE INDEX IDX_AP_ONGD_B_3 ON AP_ONGD_B (DOC_PROS_STAT_CD, APV_NO);
CREATE INDEX IDX_AP_ONGD_B_4 ON AP_ONGD_B (ENFC_STAT_CD, APV_NO);
CREATE INDEX IDX_AP_ONGD_B_5 ON AP_ONGD_B (ORGN_APV_NO, APV_NO);
CREATE INDEX IDX_AP_ONGD_B_6 ON AP_ONGD_B (REG_REC_LST_REG_SKED_YMD, REG_REC_LST_REG_YN);
CREATE INDEX IDX_AP_ONGD_B_7 ON AP_ONGD_B (PSN_CLS_INFO_ID);
CREATE INDEX IDX_AP_ONGD_B_8 ON AP_ONGD_B (XML_TYP_ID);

CREATE TABLE AP_ONGD_BODY_L (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    BODY_HST_NO                      INT NOT NULL COMMENT '본문이력번호',
    BODY_HTML                        LONGTEXT NOT NULL COMMENT '본문HTML',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (APV_NO, BODY_HST_NO)
)
COMMENT = '진행문서본문내역';


CREATE TABLE AP_ONGD_CNCL_D (
    MAKR_UID                         VARCHAR (30) NOT NULL COMMENT '기안자UID',
    XML_TYP_ID                       VARCHAR (30) NOT NULL COMMENT 'XML구분ID',
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    PRIMARY KEY (MAKR_UID, XML_TYP_ID, APV_NO)
)
COMMENT = '진행문서취소상세';


CREATE TABLE AP_ONGD_ERP_FORM_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    ERP_VA_TYP_CD                    VARCHAR (30) NOT NULL COMMENT 'ERP값구분코드',
    ERP_VA                           LONGTEXT COMMENT 'ERP값',
    PRIMARY KEY (APV_NO, ERP_VA_TYP_CD)
)
COMMENT = '진행문서ERP양식상세';


CREATE TABLE AP_ONGD_EX_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    EX_ID                            VARCHAR (30) NOT NULL COMMENT '확장ID',
    EX_VA                            VARCHAR (4000) COMMENT '확장값',
    PRIMARY KEY (APV_NO, EX_ID)
)
COMMENT = '진행문서확장상세';


CREATE TABLE AP_ONGD_EXTN_DOC_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    EXTN_DOC_TYP_CD                  VARCHAR (30) COMMENT '외부문서구분코드',
    EXTN_DOC_CONT_TYP_CD             VARCHAR (30) COMMENT '외부문서컨텐츠구분코드',
    PICH_UID                         VARCHAR (30) COMMENT '담당자UID',
    PICH_NM                          VARCHAR (120) COMMENT '담당자명',
    MAK_YY                           INT COMMENT '생산년도',
    SENDR_NM                         VARCHAR (240) COMMENT '발신자명',
    PAGE_CNT                         INT COMMENT '페이지수',
    MAK_ZONE_NM                      VARCHAR (240) COMMENT '생산지역명',
    ORGN_APV_NO                      BIGINT COMMENT '원본결재번호',
    REG_SEQ                          INT COMMENT '등록일련번호',
    PRIMARY KEY (APV_NO)
)
COMMENT = '진행문서외부문서상세';
CREATE INDEX IDX_AP_ONGD_EXTN_DOC_D_1 ON AP_ONGD_EXTN_DOC_D (ORGN_APV_NO);


CREATE TABLE AP_ONGD_HOLD_OPIN_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    APVR_UID                         VARCHAR (30) NOT NULL COMMENT '결재자UID',
    APV_OPIN_CONT                    VARCHAR (2000) COMMENT '결재의견내용',
    APV_OPIN_DISP_YN                 CHAR (1) COMMENT '결재의견표시여부',
    PRIMARY KEY (APV_NO, APVR_UID)
)
COMMENT = '진행문서보류의견상세';


CREATE TABLE AP_ONGD_PICH_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    PICH_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '담당자구분코드',
    PICH_UID                         VARCHAR (30) COMMENT '담당자UID',
    PICH_DEPT_ID                     VARCHAR (30) COMMENT '담당자부서ID',
    PICH_NM                          VARCHAR (120) COMMENT '담당자명',
    PICH_DEPT_NM                     VARCHAR (120) COMMENT '담당자부서명',
    PICH_OPIN_CONT                   VARCHAR (800) COMMENT '담당자의견내용',
    HDL_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '처리상태코드',
    HDL_DT                           DATETIME COMMENT '처리일시',
    VW_DT                            DATETIME COMMENT '조회일시',
    PREV_APVR_APV_DT                 DATETIME COMMENT '이전결재자결재일시',
    PRIMARY KEY (APV_NO, PICH_TYP_CD)
)
COMMENT = '진행문서담당자상세';
CREATE INDEX IDX_AP_ONGD_PICH_D_1 ON AP_ONGD_PICH_D (PICH_TYP_CD, PICH_UID, HDL_STAT_CD, APV_NO);
CREATE INDEX IDX_AP_ONGD_PICH_D_2 ON AP_ONGD_PICH_D (PICH_TYP_CD, PICH_DEPT_ID, HDL_STAT_CD, APV_NO);


CREATE TABLE AP_ONGD_PUB_BX_CNFM_L (
    PUB_BX_DEPT_ID                   VARCHAR (30) NOT NULL COMMENT '공람게시부서ID',
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    USER_NM                          VARCHAR (120) COMMENT '사용자명',
    DEPT_ID                          VARCHAR (30) COMMENT '부서ID',
    DEPT_NM                          VARCHAR (120) COMMENT '부서명',
    POSIT_CD                         VARCHAR (30) COMMENT '직위코드',
    POSIT_NM                         VARCHAR (120) COMMENT '직위명',
    VW_DT                            DATETIME COMMENT '조회일시',
    PRIMARY KEY (PUB_BX_DEPT_ID, APV_NO, USER_UID)
)
COMMENT = '진행문서공람게시확인내역';


CREATE TABLE AP_ONGD_PUB_BX_D (
    PUB_BX_DEPT_ID                   VARCHAR (30) NOT NULL COMMENT '공람게시부서ID',
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    PUB_BX_DT                        DATETIME NOT NULL COMMENT '공람게시일시',
    PUB_BX_END_YMD                   DATETIME COMMENT '공람게시종료년월일',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REGR_NM                          VARCHAR (120) COMMENT '등록자명',
    REG_DEPT_ID                      VARCHAR (30) NOT NULL COMMENT '등록부서ID',
    REG_DEPT_NM                      VARCHAR (120) COMMENT '등록부서명',
    PRIMARY KEY (PUB_BX_DEPT_ID, APV_NO)
)
COMMENT = '진행문서공람게시상세';
CREATE INDEX IDX_AP_ONGD_PUB_BX_D_1 ON AP_ONGD_PUB_BX_D (PUB_BX_DEPT_ID, PUB_BX_END_YMD, APV_NO);


CREATE TABLE AP_ONGD_RECV_DEPT_L (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    RECV_DEPT_HST_NO                 INT NOT NULL COMMENT '수신처이력번호',
    RECV_DEPT_SEQ                    INT NOT NULL COMMENT '수신처일련번호',
    RECV_DEPT_TYP_CD                 VARCHAR (30) NOT NULL COMMENT '수신처구분코드',
    RECV_DEPT_ID                     VARCHAR (30) COMMENT '수신처ID',
    RECV_DEPT_NM                     VARCHAR (400) COMMENT '수신처명',
    REF_DEPT_ID                      VARCHAR (30) COMMENT '참조처ID',
    REF_DEPT_NM                      VARCHAR (400) COMMENT '참조처명',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    SEND_YN                          CHAR (1) COMMENT '발송여부',
    ADD_SEND_YN                      CHAR (1) NOT NULL COMMENT '추가발송여부',
    PRIMARY KEY (APV_NO, RECV_DEPT_HST_NO, RECV_DEPT_SEQ)
)
COMMENT = '진행문서수신처내역';


CREATE TABLE AP_ONGD_REF_DOC_L (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    REF_DOC_HST_NO                   INT NOT NULL COMMENT '참조문서이력번호',
    REF_APV_NO                       BIGINT NOT NULL COMMENT '참조결재번호',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (APV_NO, REF_DOC_HST_NO, REF_APV_NO)
)
COMMENT = '진행문서참조문서내역';


CREATE TABLE AP_ONGD_REF_VW_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REF_VWR_UID                      VARCHAR (30) NOT NULL COMMENT '참조열람자UID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REF_VW_STAT_CD                   VARCHAR (30) NOT NULL COMMENT '참조열람상태코드',
    REF_VWR_NM                       VARCHAR (120) COMMENT '참조열람자명',
    REF_VWR_POSIT_CD                 VARCHAR (30) COMMENT '참조열람자직위코드',
    REF_VWR_POSIT_NM                 VARCHAR (120) COMMENT '참조열람자직위명',
    REF_VWR_TITLE_CD                 VARCHAR (30) COMMENT '참조열람자직책코드',
    REF_VWR_TITLE_NM                 VARCHAR (120) COMMENT '참조열람자직책명',
    REF_VWR_DEPT_ID                  VARCHAR (30) COMMENT '참조열람자부서ID',
    REF_VWR_DEPT_NM                  VARCHAR (120) COMMENT '참조열람자부서명',
    REF_VW_DT                        DATETIME COMMENT '참조열람일시',
    REF_VW_OPIN_CONT                 VARCHAR (2000) COMMENT '참조열람의견내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    VW_DT                            DATETIME COMMENT '조회일시',
    REF_VW_FIXD_APVR_YN              CHAR (1) COMMENT '참조열람고정결재자여부',
    PRIMARY KEY (APV_NO, REF_VWR_UID)
)
COMMENT = '진행문서참조열람상세';
CREATE INDEX IDX_AP_ONGD_REF_VW_D_1 ON AP_ONGD_REF_VW_D (REF_VWR_UID, REF_VW_STAT_CD, APV_NO);


CREATE TABLE AP_ONGD_REF_VW_E (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REF_VWR_UID                      VARCHAR (30) NOT NULL COMMENT '참조열람자UID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REF_VW_STAT_CD                   VARCHAR (30) NOT NULL COMMENT '참조열람상태코드',
    REF_VWR_NM                       VARCHAR (120) COMMENT '참조열람자명',
    REF_VWR_POSIT_CD                 VARCHAR (30) COMMENT '참조열람자직위코드',
    REF_VWR_POSIT_NM                 VARCHAR (120) COMMENT '참조열람자직위명',
    REF_VWR_TITLE_CD                 VARCHAR (30) COMMENT '참조열람자직책코드',
    REF_VWR_TITLE_NM                 VARCHAR (120) COMMENT '참조열람자직책명',
    REF_VWR_DEPT_ID                  VARCHAR (30) COMMENT '참조열람자부서ID',
    REF_VWR_DEPT_NM                  VARCHAR (120) COMMENT '참조열람자부서명',
    REF_VW_DT                        DATETIME COMMENT '참조열람일시',
    REF_VW_OPIN_CONT                 VARCHAR (2000) COMMENT '참조열람의견내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    VW_DT                            DATETIME COMMENT '조회일시',
    REF_VW_FIXD_APVR_YN              CHAR (1) COMMENT '참조열람고정결재자여부',
    PRIMARY KEY (APV_NO, REF_VWR_UID)
)
COMMENT = '진행문서참조열람이행';
CREATE INDEX IDX_AP_ONGD_REF_VW_E_1 ON AP_ONGD_REF_VW_E (REF_VWR_UID, REF_VW_STAT_CD, APV_NO);


CREATE TABLE AP_ONGD_REF_VW_H (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    REGR_UID                         VARCHAR (30) NOT NULL COMMENT '등록자UID',
    REF_VWR_UID                      VARCHAR (30) NOT NULL COMMENT '참조열람자UID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    REF_VW_STAT_CD                   VARCHAR (30) NOT NULL COMMENT '참조열람상태코드',
    REF_VWR_NM                       VARCHAR (120) COMMENT '참조열람자명',
    REF_VWR_POSIT_CD                 VARCHAR (30) COMMENT '참조열람자직위코드',
    REF_VWR_POSIT_NM                 VARCHAR (120) COMMENT '참조열람자직위명',
    REF_VWR_TITLE_CD                 VARCHAR (30) COMMENT '참조열람자직책코드',
    REF_VWR_TITLE_NM                 VARCHAR (120) COMMENT '참조열람자직책명',
    REF_VWR_DEPT_ID                  VARCHAR (30) COMMENT '참조열람자부서ID',
    REF_VWR_DEPT_NM                  VARCHAR (120) COMMENT '참조열람자부서명',
    REF_VW_DT                        DATETIME COMMENT '참조열람일시',
    REF_VW_OPIN_CONT                 VARCHAR (2000) COMMENT '참조열람의견내용',
    REG_DT                           DATETIME COMMENT '등록일시',
    VW_DT                            DATETIME COMMENT '조회일시',
    REF_VW_FIXD_APVR_YN              CHAR (1) COMMENT '참조열람고정결재자여부',
    PRIMARY KEY (APV_NO, REGR_UID, REF_VWR_UID)
)
COMMENT = '진행문서참조열람이력';


CREATE TABLE AP_ONGD_SEND_D (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    SEND_SEQ                         INT NOT NULL COMMENT '발송일련번호',
    RECV_DEPT_ID                     VARCHAR (30) COMMENT '수신처ID',
    RECV_DEPT_NM                     VARCHAR (400) COMMENT '수신처명',
    REF_DEPT_NM                      VARCHAR (400) COMMENT '참조처명',
    RECV_DEPT_TYP_CD                 VARCHAR (30) COMMENT '수신처구분코드',
    HDL_STAT_CD                      VARCHAR (30) NOT NULL COMMENT '처리상태코드',
    HDLR_UID                         VARCHAR (30) COMMENT '처리자UID',
    HDLR_NM                          VARCHAR (120) COMMENT '처리자명',
    HDLR_OPIN_CONT                   VARCHAR (800) COMMENT '처리자의견내용',
    HDL_DT                           DATETIME COMMENT '처리일시',
    VW_DT                            DATETIME COMMENT '조회일시',
    PREV_APVR_APV_DT                 DATETIME COMMENT '이전결재자결재일시',
    PRIMARY KEY (APV_NO, SEND_SEQ)
)
COMMENT = '진행문서발송상세';
CREATE INDEX IDX_AP_ONGD_SEND_D_1 ON AP_ONGD_SEND_D (RECV_DEPT_ID, HDL_STAT_CD, APV_NO);


CREATE TABLE AP_ONGD_TRX_D (
    TRX_APV_NO                       BIGINT NOT NULL COMMENT '변환결재번호',
    ORGN_APV_NO                      BIGINT NOT NULL COMMENT '원본결재번호',
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    BODY_HGHT_PX                     INT COMMENT '본문높이픽셀',
    FORM_WDTH_TYP_CD                 VARCHAR (30) COMMENT '양식넓이구분코드',
    FOOTER_VA                        VARCHAR (400) COMMENT '바닥글값',
    PRIMARY KEY (TRX_APV_NO)
)
COMMENT = '진행문서변환상세';
CREATE INDEX IDX_AP_ONGD_TRX_D_1 ON AP_ONGD_TRX_D (ORGN_APV_NO, TRX_APV_NO);


CREATE TABLE AP_ONGO_FORM_APV_LN_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    APV_LN_TITL_TYP_CD               VARCHAR (30) NOT NULL COMMENT '결재라인타이틀구분코드',
    APV_LN_DISP_TYP_CD               VARCHAR (30) COMMENT '결재라인표시구분코드',
    MAX_CNT                          INT COMMENT '최대개수',
    ALN_VA                           VARCHAR (30) COMMENT '줄맞춤값',
    BORD_USE_YN                      CHAR (1) COMMENT '테두리선사용여부',
    TITL_USE_YN                      CHAR (1) COMMENT '타이틀사용여부',
    LST_DUP_DISP_YN                  CHAR (1) COMMENT '목록중복표시여부',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_COMB_SEQ, APV_LN_TITL_TYP_CD)
)
COMMENT = '진행양식결재라인상세';


CREATE TABLE AP_ONGO_FORM_B (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    FORM_NM                          VARCHAR (240) COMMENT '양식명',
    FORM_BX_ID                       VARCHAR (30) NOT NULL COMMENT '양식함ID',
    FORM_TYP_CD                      VARCHAR (30) COMMENT '양식구분코드',
    INTG_ID                          VARCHAR (30) COMMENT '연계ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    BODY_HGHT_PX                     INT COMMENT '본문높이픽셀',
    FORM_WDTH_TYP_CD                 VARCHAR (30) COMMENT '양식넓이구분코드',
    FORM_APV_LN_TYP_CD               VARCHAR (30) COMMENT '양식결재라인구분코드',
    TPL_YN                           CHAR (1) NOT NULL COMMENT '템플릿여부',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    REGR_UID                         VARCHAR (30) COMMENT '등록자UID',
    REG_DT                           DATETIME COMMENT '등록일시',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    BODY_HTML                        LONGTEXT COMMENT '본문HTML',
    ERP_FORM_ID                      VARCHAR (30) COMMENT 'ERP양식ID',
    ERP_FORM_TYP_CD                  VARCHAR (30) COMMENT 'ERP양식구분코드',
    DOC_KEEP_PRD_CD                  VARCHAR (30) COMMENT '문서보존기간코드',
    FORM_MAGN_VA                     VARCHAR (80) COMMENT '양식여백값',
    INTG_FORM_YN                     CHAR (1) COMMENT '연계양식여부',
    BODY_OLINE_YN                    CHAR (1) COMMENT '본문외곽선여부',
    APV_LN_GRP_ID                    VARCHAR (30) COMMENT '결재라인그룹ID',
    FIXD_APVR_YN                     CHAR (1) COMMENT '고정결재자여부',
    AUTO_APV_LN_CD                   VARCHAR (30) COMMENT '자동결재선코드',
    DOC_SUBJ_CD                      VARCHAR (30) COMMENT '문서제목코드',
    REF_VW_GRP_ID                    VARCHAR (30) COMMENT '참조열람그룹ID',
    REF_VW_FIXD_APVR_YN              CHAR (1) COMMENT '참조열람고정결재자여부',
    PRIMARY KEY (FORM_ID, FORM_SEQ)
)
COMMENT = '진행양식기본';
CREATE INDEX IDX_AP_ONGO_FORM_B_1 ON AP_ONGO_FORM_B (ERP_FORM_ID);


CREATE TABLE AP_ONGO_FORM_COMB_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_COMB_ID                     VARCHAR (30) NOT NULL COMMENT '양식구성ID',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_COMB_ID, FORM_COMB_SEQ)
)
COMMENT = '진행양식구성상세';


CREATE TABLE AP_ONGO_FORM_IMG_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_IMG_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '양식이미지구분코드',
    IMG_PATH                         VARCHAR (240) COMMENT '이미지경로',
    IMG_WDTH                         INT COMMENT '이미지넓이',
    IMG_HGHT                         INT COMMENT '이미지높이',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_IMG_TYP_CD)
)
COMMENT = '진행양식이미지상세';


CREATE TABLE AP_ONGO_FORM_ITEM_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    ROW_CNT                          INT NOT NULL COMMENT '줄수',
    COL_CNT                          INT NOT NULL COMMENT '열수',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_COMB_SEQ)
)
COMMENT = '진행양식항목상세';


CREATE TABLE AP_ONGO_FORM_ITEM_L (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_COMB_SEQ                    INT NOT NULL COMMENT '양식구성일련번호',
    ROW_NO                           INT NOT NULL COMMENT '줄번호',
    COL_NO                           INT NOT NULL COMMENT '열번호',
    CSPN_VA                          INT NOT NULL COMMENT '콜스판값',
    ITEM_ID                          VARCHAR (30) COMMENT '항목ID',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_COMB_SEQ, ROW_NO, COL_NO)
)
COMMENT = '진행양식항목내역';


CREATE TABLE AP_ONGO_FORM_TXT_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_SEQ                         INT NOT NULL COMMENT '양식일련번호',
    FORM_TXT_TYP_CD                  VARCHAR (30) NOT NULL COMMENT '양식텍스트구분코드',
    TXT_CONT                         VARCHAR (240) COMMENT '텍스트내용',
    TXT_FONT_VA                      VARCHAR (50) COMMENT '텍스트폰트값',
    TXT_STYL_VA                      VARCHAR (50) COMMENT '텍스트스타일값',
    TXT_SIZE                         VARCHAR (20) COMMENT '텍스트크기',
    TXT_COLR_VA                      VARCHAR (20) COMMENT '텍스트색상값',
    PRIMARY KEY (FORM_ID, FORM_SEQ, FORM_TXT_TYP_CD)
)
COMMENT = '진행양식텍스트상세';


CREATE TABLE AP_ONGO_FORM_XML_D (
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    FORM_XML_CD                      VARCHAR (30) NOT NULL COMMENT '양식XML코드',
    FORM_XML_CONT                    LONGTEXT COMMENT '양식XML내용',
    PRIMARY KEY (FORM_ID, FORM_XML_CD)
)
COMMENT = '진행양식XML상세';


CREATE TABLE AP_PLT_SETUP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PLT_ID                           VARCHAR (30) NOT NULL COMMENT '포틀릿ID',
    BX_ID                            VARCHAR (30) NOT NULL COMMENT '함ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    PRIMARY KEY (USER_UID, PLT_ID, BX_ID)
)
COMMENT = '포틀릿설정상세';


CREATE TABLE AP_PSN_CLS_INFO_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    PSN_CLS_INFO_ID                  VARCHAR (30) NOT NULL COMMENT '개인분류정보ID',
    PSN_CLS_INFO_PID                 VARCHAR (30) NOT NULL COMMENT '개인분류정보부모ID',
    PSN_CLS_INFO_NM                  VARCHAR (120) COMMENT '개인분류정보명',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    USE_YN                           CHAR (1) NOT NULL COMMENT '사용여부',
    PRIMARY KEY (USER_UID, PSN_CLS_INFO_ID)
)
COMMENT = '개인분류정보상세';


CREATE TABLE AP_RECV_GRP_B (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    RECV_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '수신그룹ID',
    RECV_GRP_NM                      VARCHAR (120) COMMENT '수신그룹명',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    SORT_ORDR                        INT NOT NULL COMMENT '정렬순서',
    MODR_UID                         VARCHAR (30) COMMENT '수정자UID',
    MOD_DT                           DATETIME COMMENT '수정일시',
    PRIMARY KEY (USER_UID, RECV_GRP_ID)
)
COMMENT = '수신그룹기본';


CREATE TABLE AP_RECV_GRP_D (
    USER_UID                         VARCHAR (30) NOT NULL COMMENT '사용자UID',
    RECV_GRP_ID                      VARCHAR (30) NOT NULL COMMENT '수신그룹ID',
    RECV_GRP_SEQ                     INT NOT NULL COMMENT '수신그룹일련번호',
    RECV_DEPT_TYP_CD                 VARCHAR (30) NOT NULL COMMENT '수신처구분코드',
    RECV_DEPT_ID                     VARCHAR (30) COMMENT '수신처ID',
    RECV_DEPT_NM                     VARCHAR (400) COMMENT '수신처명',
    REF_DEPT_ID                      VARCHAR (30) COMMENT '참조처ID',
    REF_DEPT_NM                      VARCHAR (400) COMMENT '참조처명',
    PRIMARY KEY (USER_UID, RECV_GRP_ID, RECV_GRP_SEQ)
)
COMMENT = '수신그룹상세';


CREATE TABLE AP_RESC_B (
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스기본';


CREATE TABLE AP_RESC_D (
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    RESC_ID                          VARCHAR (30) NOT NULL COMMENT '리소스ID',
    LANG_TYP_CD                      VARCHAR (30) NOT NULL COMMENT '언어구분코드',
    RESC_VA                          VARCHAR (400) COMMENT '리소스값',
    PRIMARY KEY (COMP_ID, RESC_ID, LANG_TYP_CD)
)
COMMENT = '리소스상세';


CREATE TABLE AP_STOR_B (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    CRT_YN                           CHAR (1) COMMENT '생성여부',
    RESC_ID                          VARCHAR (30) COMMENT '리소스ID',
    ALL_DOC_CNT                      BIGINT COMMENT '전체문서건수',
    PRIMARY KEY (STOR_ID)
)
COMMENT = '저장소기본';


CREATE TABLE AP_STOR_COMP_R (
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    STOR_RESC_ID                     VARCHAR (30) COMMENT '저장소리소스ID',
    COMP_DOC_CNT                     BIGINT COMMENT '회사문서건수',
    USE_YN                           CHAR (1) COMMENT '사용여부',
    PRIMARY KEY (STOR_ID, COMP_ID)
)
COMMENT = '저장소회사관계';


CREATE TABLE AP_STOR_R (
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    PRIMARY KEY (APV_NO, STOR_ID)
)
COMMENT = '저장소관계';


CREATE TABLE AP_TRAN_B (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    COMP_ID                          VARCHAR (3) NOT NULL COMMENT '회사ID',
    STOR_ID                          VARCHAR (30) NOT NULL COMMENT '저장소ID',
    STOR_RESC_ID                     VARCHAR (30) COMMENT '저장소리소스ID',
    TRAN_TGT_STRT_YMD                DATETIME COMMENT '이관대상시작년월일',
    TRAN_TGT_END_YMD                 DATETIME COMMENT '이관대상종료년월일',
    DOC_INCL_YN                      CHAR (1) COMMENT '문서포함여부',
    PAP_DOC_INCL_YN                  CHAR (1) COMMENT '종이문서포함여부',
    DOC_TGT_CNT                      BIGINT COMMENT '문서대상건수',
    TRX_TGT_CNT                      BIGINT COMMENT '변환대상건수',
    PAP_TGT_CNT                      BIGINT COMMENT '종이대상건수',
    TRAN_STRT_DT                     DATETIME COMMENT '이관시작일시',
    TRAN_END_DT                      DATETIME COMMENT '이관종료일시',
    TRAN_PROC_STAT_CD                VARCHAR (30) COMMENT '이관진행상태코드',
    DEFRAG_YN                        CHAR (1) COMMENT '조각모음여부',
    USER_UID                         VARCHAR (30) COMMENT '사용자UID',
    PRIMARY KEY (TRAN_ID)
)
COMMENT = '이관기본';


CREATE TABLE AP_TRAN_FROM_R (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    PRIMARY KEY (TRAN_ID, FORM_ID)
)
COMMENT = '이관양식관계';


CREATE TABLE AP_TRAN_PROC_L (
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


CREATE TABLE AP_TRAN_TGT_T (
    TRAN_ID                          VARCHAR (30) NOT NULL COMMENT '이관ID',
    TRAN_DATA_TYP_CD                 VARCHAR (30) NOT NULL COMMENT '이관데이터구분코드',
    TRAN_SEQ                         BIGINT NOT NULL COMMENT '이관일련번호',
    APV_NO                           BIGINT NOT NULL COMMENT '결재번호',
    PRIMARY KEY (TRAN_ID, TRAN_DATA_TYP_CD, TRAN_SEQ)
)
COMMENT = '이관대상임시';


-- 2019-01-31_결재_양식JSP상세_추가_MYSQL.txt
CREATE TABLE AP_FORM_JSP_D (
    JSP_ID                           VARCHAR (30) NOT NULL COMMENT 'JSP ID',
    FORM_ID                          VARCHAR (30) NOT NULL COMMENT '양식ID',
    JSP_PATH                         VARCHAR (240) COMMENT 'JSP 경로',
    PRIMARY KEY (JSP_ID, FORM_ID)
)
COMMENT = '양식JSP상세';
