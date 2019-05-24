-- 그룹웨어 계정 접속해서 실행할것
-- mysql.com 를 고객사 도메인으로 변경한 후 실행할것

-- 공용주소록(그룹)
CREATE VIEW ZT_C_ADDRESS_GROUP (
    GROUP_SN,
    DOMAIN_GROUP_SN,
    UPPER_GROUP_SN,
    GROUP_PATH,
    LEVELS,
    GROUP_RANK,
    ISLEAF,
    GROUP_NAME,
    SORT_CODE,
    COMMENTS
    )
AS SELECT 1,
       'mysql.com' AS DOMAIN,
       0, 
       NULL,
       0,
       0,
       0,
       '전체',
       0,
       NULL
FROM DUAL
UNION
SELECT 2,
       'mysql.com' AS DOMAIN,
       1, 
       NULL,
       1,
       1,
       1,
       '미분류',
       0,
       NULL
FROM DUAL
UNION
SELECT FN_TOINT(FLD_ID),
          'mysql.com',
      IF(FLD_PID='ROOT',1,FN_TOINT(FLD_PID)),
          NULL,
          1,
          2,
          1,
          (SELECT RESC_VA FROM WM_RESC_B WHERE COMP_ID = T.COMP_ID AND RESC_ID = T.RESC_ID AND LANG_TYP_CD = 'ko') AS FLD_NM,
          IFNULL(SORT_ORDR, '1'),
          NULL
   FROM WB_PUB_FLD_B T
   WHERE T.USE_YN = 'Y';


-- 공용주소록(사용자)
CREATE VIEW ZT_C_ADDRESS_PERSON( 
    PERSON_SN,
    GROUP_SN,
    DOMAIN_GROUP_SN,
    PERSON_NAME,
    SORT_CODE,
    ENG_NAME,
    HANJA_NAME,
    NICKNAME,
    EMAIL,
    MOBILE,
    GENDER_CODE,
    BIRTHDAY,
    BIRTHDAY_TYPE,
    ANNIVERSARY,
    HOME_ZIPCODE,
    HOME_ADDRESS1,
    HOME_ADDRESS2,
    HOME_PHONE,
    HOMEPAGE,
    COMPANY_CODE,
    ORGAN_NAME,
    DEPT_NAME,
    ROLE_NAME,
    POSITION_NAME,
    OFFICE_ZIPCODE,
    OFFICE_ADDRESS1,
    OFFICE_ADDRESS2,
    OFFICE_PHONE,
    OFFICE_LOCAL_PHONE,
    OFFICE_D_PHONE,
    OFFICE_FAX,
    PICTURE_FNAME,
    WORKS,
    COMMENTS
)AS SELECT FN_TOINT(T.BC_ID),
	IF(T.FLD_ID='NONE',2,FN_TOINT(T.FLD_ID)) AS FLD_ID,
    'mysql.com',
    BC_NM,
    0,
    BC_EN_NM,
    NULL,
    NULL,
    ( SELECT CNTC_CONT FROM WB_BC_CNTC_D C WHERE C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1 ) AS EMAIL,
    ( SELECT CNTC_CONT FROM WB_BC_CNTC_D C WHERE C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'CNTC' AND CNTC_TYP_CD = 'mbno' AND C.BC_CNTC_SEQ = 1 ) AS MBNO,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL
FROM WB_PUB_BC_B T INNER JOIN WB_BC_CNTC_D C ON C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1;



-- 공용주소록(명함그룹 매핑)
CREATE VIEW ZT_C_GROUP_PERSON (
    GROUP_SN,
    PERSON_SN
    ) AS 
SELECT IF(B.FLD_ID='NONE',2,FN_TOINT(B.FLD_ID)) AS FLD_ID, FN_TOINT(B.BC_ID)
FROM WB_PUB_BC_B B INNER JOIN WB_BC_CNTC_D C ON C.BC_ID = B.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1;
