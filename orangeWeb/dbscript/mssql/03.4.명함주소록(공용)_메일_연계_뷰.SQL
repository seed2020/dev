
-- 그룹웨어 계정 접속해서 실행할것
-- 'hwpharm.com' 를 고객사 도메인으로 변경

-- 개인주소록(그룹)
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
       'hwpharm.com' AS DOMAIN,
       0, 
       NULL,
       0,
       0,
       0,
       '전체',
       0,
       NULL
UNION
SELECT 2,
       'hwpharm.com' AS DOMAIN,
       1, 
       NULL,
       1,
       1,
       1,
       '미분류',
       0,
       NULL
UNION
SELECT dbo.FN_TOINT(FLD_ID),
          'hwpharm.com',
	  CASE WHEN FLD_PID = 'ROOT' THEN 1 ELSE dbo.FN_TOINT(FLD_PID) END FLD_PID,
          NULL,
          1,
          2,
          1,
          (SELECT RESC_VA FROM dbo.WM_RESC_B WHERE COMP_ID = T.COMP_ID AND RESC_ID = T.RESC_ID AND LANG_TYP_CD = 'ko') AS FLD_NM,
          SORT_ORDR,
          NULL
   FROM dbo.WB_PUB_FLD_B T
   WHERE T.USE_YN = 'Y';
GO

-- 개인주소록(사용자)
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
)AS SELECT dbo.FN_TOINT(T.BC_ID),
	CASE WHEN T.FLD_ID = 'NONE' THEN 2 ELSE dbo.FN_TOINT(T.FLD_ID) END FLD_ID,
    'hwpharm.com',
    BC_NM,
    0,
    BC_EN_NM,
    NULL,
    NULL,
    ( SELECT CNTC_CONT FROM dbo.WB_BC_CNTC_D C WHERE C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1 ) AS EMAIL,
    ( SELECT CNTC_CONT FROM dbo.WB_BC_CNTC_D C WHERE C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'CNTC' AND CNTC_TYP_CD = 'mbno' AND C.BC_CNTC_SEQ = 1 ) AS MBNO,
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
FROM dbo.WB_PUB_BC_B T INNER JOIN dbo.WB_BC_CNTC_D C ON C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1;
GO


-- 개인주소록(명함그룹 매핑)
CREATE VIEW ZT_C_GROUP_PERSON (
    GROUP_SN,
    PERSON_SN
    ) AS 
SELECT CASE WHEN B.FLD_ID = 'NONE' THEN 2 ELSE dbo.FN_TOINT(B.FLD_ID) END FLD_ID, dbo.FN_TOINT(B.BC_ID)       
FROM dbo.WB_PUB_BC_B B INNER JOIN dbo.WB_BC_CNTC_D C ON C.BC_ID = B.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1;
GO

--FROM dbo.WB_PUB_FLD_B T RIGHT OUTER JOIN dbo.WB_PUB_BC_B B ON T.FLD_ID = B.FLD_ID;
