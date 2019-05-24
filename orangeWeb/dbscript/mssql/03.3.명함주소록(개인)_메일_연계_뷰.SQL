
-- 그룹웨어 계정 접속해서 실행할것

-- 개인주소록(그룹)
CREATE VIEW ZT_ADDRESS_GROUP (
    GROUP_SN,
    USER_SN,
    UPPER_GROUP_SN,
    GROUP_PATH,
    LEVELS,
    GROUP_RANK,
    ISLEAF,
    GROUP_NAME,
    SORT_CODE,
    COMMENTS
    )
AS SELECT CAST(CAST(dbo.FN_TOINT(P.USER_UID) AS VARCHAR)+'1' AS INT),
          P.USER_UID,
          0,
          NULL,
          0,
          0,
          0,
          '전체',
          0,
          NULL
FROM(   
    SELECT USER_UID
    FROM OR_USER_B
    WHERE USER_STAT_CD IN('02','03','04')
) P 
UNION
SELECT CAST(CAST(dbo.FN_TOINT(P.USER_UID) AS  VARCHAR)+'2' AS INT),
          P.USER_UID,
          CAST(CAST(dbo.FN_TOINT(P.USER_UID) AS  VARCHAR)+'1' AS INT),
          NULL,
          1,
          1,
          1,
          '미분류',
          0,
          NULL
FROM(   
    SELECT USER_UID
    FROM OR_USER_B
    WHERE USER_STAT_CD IN('02','03','04')
) P
UNION
SELECT 
      dbo.FN_TOINT(BC_FLD_ID),
          REGR_UID,
	  CASE WHEN ABV_FLD_ID = 'ROOT' THEN CAST(CAST(dbo.FN_TOINT(REGR_UID) AS VARCHAR)+'1' AS INT) ELSE dbo.FN_TOINT(ABV_FLD_ID) END ABV_FLD_ID,
          NULL,
          1,
          2,
          1,
          FLD_NM,
          SORT_ORDR,
          NULL
   FROM dbo.WB_BC_FLD_B;
GO

-- 개인주소록(사용자)
CREATE VIEW ZT_ADDRESS_PERSON( 
    PERSON_SN,
    GROUP_SN,
    USER_SN,
    CREATE_USER_SN,
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
)
AS 
SELECT dbo.FN_TOINT(T.BC_ID),
	CASE WHEN T.FLD_ID = 'NONE' THEN CAST(CAST(dbo.FN_TOINT(T.REGR_UID) AS VARCHAR)+'2' AS INT) ELSE dbo.FN_TOINT(T.FLD_ID) END AS FLD_ID,
    T.REGR_UID,
    T.REGR_UID,
    T.BC_NM,
    0,
    T.BC_EN_NM,
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
FROM dbo.WB_BC_B T INNER JOIN dbo.OR_USER_B O ON T.REGR_UID = O.USER_UID AND O.USER_STAT_CD IN('02','03','04')
								INNER JOIN dbo.WB_BC_CNTC_D C ON C.BC_ID = T.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1
GO



-- 개인주소록(명함그룹 매핑)
CREATE VIEW ZT_GROUP_PERSON (
    GROUP_SN,
    PERSON_SN
    ) AS 
SELECT CASE WHEN B.FLD_ID = 'NONE' THEN CAST(CAST(dbo.FN_TOINT(B.REGR_UID) AS VARCHAR)+'2' AS INT) ELSE dbo.FN_TOINT(B.FLD_ID) END AS FLD_ID, dbo.FN_TOINT(B.BC_ID)
FROM dbo.WB_BC_B B INNER JOIN dbo.OR_USER_B O ON B.REGR_UID = O.USER_UID AND O.USER_STAT_CD IN('02','03','04')
								INNER JOIN dbo.WB_BC_CNTC_D C ON C.BC_ID = B.BC_ID AND C.CNTC_CLS_CD = 'EMAIL' AND CNTC_TYP_CD = 'email' AND C.BC_CNTC_SEQ = 1
GO

--FROM dbo.WB_BC_FLD_B T RIGHT OUTER JOIN dbo.WB_BC_B B ON T.BC_FLD_ID = B.FLD_ID
