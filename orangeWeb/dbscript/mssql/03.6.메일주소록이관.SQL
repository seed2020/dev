/****** Object:  Table [dbo].[EM_MAIL_ADR_TRAN_TGT_T]    Script Date: 2017/03/20 15:28 ******/
CREATE TABLE [dbo].[EM_MAIL_ADR_TRAN_TGT_T](
[TRAN_ID] VARCHAR(30) NOT NULL,
[SEQ] INT NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[USER_SN] VARCHAR(30) NOT NULL,
[FLD_CNT] INT,
[ADR_CNT] INT,
[ERR_YN] CHAR(1),
CONSTRAINT [PK_EM_MAIL_ADR_TRAN_TGT_T] PRIMARY KEY CLUSTERED
(
[TRAN_ID] ASC,
[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'TRAN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'순번' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'USER_SN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'FLD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주소록건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'ADR_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'오류여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T', @level2type=N'COLUMN',@level2name=N'ERR_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관대상임시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_TGT_T'
GO

/****** Object:  Table [dbo].[EM_MAIL_ADR_TRAN_B]    Script Date: 2017/03/20 15:28 ******/
CREATE TABLE [dbo].[EM_MAIL_ADR_TRAN_B](
[TRAN_ID] VARCHAR(30) NOT NULL,
[USER_CNT] INT,
[FLD_CNT] INT,
[ADR_CNT] INT,
[TRAN_STRT_DT] DATETIME,
[TRAN_END_DT] DATETIME,
[TRAN_PROC_STAT_CD] VARCHAR(30),
[TRAN_CONT] NVARCHAR(1000),
[ERR_CONT] NVARCHAR(1000),
[USER_UID] VARCHAR(30),
CONSTRAINT [PK_EM_MAIL_ADR_TRAN_B] PRIMARY KEY CLUSTERED
(
[TRAN_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'TRAN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'USER_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'FLD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주소록건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'ADR_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'TRAN_STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'TRAN_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관진행상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'TRAN_PROC_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이관내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'TRAN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'오류내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'ERR_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메일주소록이관' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_MAIL_ADR_TRAN_B'
GO


-- 초기 데이터
INSERT INTO CM_SEQ_B VALUES ('EM_MAIL_ADR_TRAN_B', '1');


/*
-- 이관폴더 조회
WITH C_TBLX AS (
	SELECT 0 AS EMPLEVEL , FLD_NM , ABV_FLD_ID , BC_FLD_ID ,
	   CONVERT(VARCHAR(100), ' / ' +CAST(BC_FLD_ID AS VARCHAR(100))) 
	   PATH_ORDER -- SIBLINGS BY
	FROM WB_BC_FLD_B 
	WHERE FLD_NM LIKE '20170320150501%' -- 이관ID
	--WHERE ABV_FLD_ID = 'ROOT'  -- START WITH 
	UNION ALL
	SELECT EMPLEVEL + 1 , A.FLD_NM , A.ABV_FLD_ID , A.BC_FLD_ID ,
		   CONVERT(VARCHAR(100), B.PATH_ORDER + ' / ' + CAST(A.BC_FLD_ID AS VARCHAR(100))) -- SIBLINGS BY 
	FROM WB_BC_FLD_B A JOIN C_TBLX B ON A.ABV_FLD_ID = B.BC_FLD_ID -- CONNECT BY PRIOR 
)
SELECT STUFF(( SELECT ',''' + P.BC_FLD_ID+''''
               FROM C_TBLX P              
               FOR XML PATH ('')),1,1,'') AS FLD_IDS

-- 폴더에 속한 명함 조회
SELECT BC_ID
FROM WB_BC_B
WHERE FLD_ID IN([폴더ID조건])

--연락처 삭제
DELETE FROM WB_BC_CNTC_D
WHERE BC_ID IN(
SELECT BC_ID
FROM WB_BC_B
WHERE FLD_ID IN([폴더ID조건])
)

-- 명함삭제
DELETE FROM WB_BC_B
WHERE FLD_ID IN([폴더ID조건])

-- 폴더삭제
DELETE FROM WB_BC_FLD_B
WHERE BC_FLD_ID IN([폴더ID조건])

*/

