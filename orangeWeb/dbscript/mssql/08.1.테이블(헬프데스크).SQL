/****** Object:  Table [dbo].[WH_REQ_ONGD_D]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_REQ_ONGD_D](
[REQ_NO] INT NOT NULL,
[MD_ID] VARCHAR(30) NOT NULL,
[STAT_CD] CHAR(1) NOT NULL,
[PREV_STAT_CD] CHAR(1),
[PICH_UID] VARCHAR(30),
[RECV_UID] VARCHAR(30),
[RECV_DT] DATETIME,
[RECV_CONT] NVARCHAR(4000),
[HDLR_UID] VARCHAR(30),
[CAT_NO] INT,
[CMPL_DUE_DT] DATETIME,
[ONGO_CONT] NVARCHAR(4000),
[CMPL_DT] DATETIME,
[EVAL_YN] CHAR(1) NOT NULL,
CONSTRAINT [PK_WH_REQ_ONGD_D] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'MD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이전상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'PREV_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'접수자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'RECV_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'접수일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'RECV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'접수내용 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'RECV_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'HDLR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'CAT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료예정일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'CMPL_DUE_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'ONGO_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'CMPL_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'평가여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D', @level2type=N'COLUMN',@level2name=N'EVAL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청진행상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_D'
GO

/****** Object:  Table [dbo].[WH_MD_PICH_L]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_MD_PICH_L](
[MD_ID] VARCHAR(30) NOT NULL,
[PICH_TYP_CD] CHAR(1) NOT NULL,
[ID_VA] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WH_MD_PICH_L] PRIMARY KEY CLUSTERED
(
[MD_ID] ASC,
[PICH_TYP_CD] ASC,
[ID_VA] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_PICH_L', @level2type=N'COLUMN',@level2name=N'MD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_PICH_L', @level2type=N'COLUMN',@level2name=N'PICH_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_PICH_L', @level2type=N'COLUMN',@level2name=N'ID_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_PICH_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈담당자목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_PICH_L'
GO

/****** Object:  Table [dbo].[WH_CAT_GRP_B]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_CAT_GRP_B](
[CAT_GRP_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CAT_GRP_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[DFT_YN] CHAR(1) NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WH_CAT_GRP_B] PRIMARY KEY CLUSTERED
(
[CAT_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_B'
GO

/****** Object:  Table [dbo].[WH_REQ_CMPL_D]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_REQ_CMPL_D](
[REQ_NO] INT NOT NULL,
[HDL_CONT] NTEXT,
[DEV_HOUR_VA] VARCHAR(10),
[HDL_COMP_NM] NVARCHAR(120),
[HDL_COST] INT,
[STRT_DT] DATETIME,
[END_DT] DATETIME,
[TEST_PICH_UID] VARCHAR(30),
[TEST_ONGO_CONT] NVARCHAR(4000),
[TEST_RES_CONT] NVARCHAR(4000),
CONSTRAINT [PK_WH_REQ_CMPL_D] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'HDL_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공수값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'DEV_HOUR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리업체명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'HDL_COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리비용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'HDL_COST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'TEST_PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트진행내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'TEST_ONGO_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트결과내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D', @level2type=N'COLUMN',@level2name=N'TEST_RES_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청완료상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_D'
GO


/****** Object:  Table [dbo].[WH_REQ_CMPL_H]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_REQ_CMPL_H](
[HST_NO] INT NOT NULL,
[REQ_NO] INT NOT NULL,
[CMPL_DT] DATETIME,
[HDL_CONT] NTEXT,
[DEV_HOUR_VA] VARCHAR(10),
[HDL_COMP_NM] NVARCHAR(120),
[HDL_COST] INT,
[STRT_DT] DATETIME,
[END_DT] DATETIME,
[TEST_PICH_UID] VARCHAR(30),
[TEST_ONGO_CONT] NVARCHAR(4000),
[TEST_RES_CONT] NVARCHAR(4000),
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
CONSTRAINT [PK_WH_REQ_CMPL_H] PRIMARY KEY CLUSTERED
(
[HST_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'CMPL_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'HDL_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공수값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'DEV_HOUR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리업체명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'HDL_COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'처리비용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'HDL_COST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'TEST_PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트진행내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'TEST_ONGO_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테스트결과내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'TEST_RES_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청완료이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_CMPL_H'
GO

/****** Object:  Table [dbo].[WH_REQ_B]    Script Date: 03/27/2015 18:03:31 ******/
CREATE TABLE [dbo].[WH_REQ_B](
[REQ_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[DEPT_ID] VARCHAR(30) NOT NULL,
[DOC_NO] NVARCHAR(120),
[SUBJ] NVARCHAR(240) NOT NULL,
[CONT] NTEXT,
[REQ_DT] DATETIME,
[CMPL_PDT] DATETIME,
[PROGRM_ID] NVARCHAR(120),
[PROGRM_NM] NVARCHAR(240),
[WRITTEN_REQ_YN] CHAR(1),
[WRITTEN_REQ_NO] NVARCHAR(120),
[UPLOAD_NO] INT,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WH_REQ_B] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'DOC_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'REQ_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료희망일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'CMPL_PDT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'프로그램ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'PROGRM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'프로그램명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'PROGRM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'의뢰서여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'WRITTEN_REQ_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'의뢰서번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'WRITTEN_REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'업로드번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'UPLOAD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_B'
GO

/****** Object:  Table [dbo].[WH_RES_EVAL_B]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_RES_EVAL_B](
[EVAL_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[EVAL_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WH_RES_EVAL_B] PRIMARY KEY CLUSTERED
(
[EVAL_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'평가번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B', @level2type=N'COLUMN',@level2name=N'EVAL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'평가명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B', @level2type=N'COLUMN',@level2name=N'EVAL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N' 결과평가기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RES_EVAL_B'
GO

/****** Object:  Table [dbo].[WH_MD_B]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_MD_B](
[MD_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[MD_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[MD_PID] VARCHAR(30) NOT NULL,
[MD_TYP_CD] CHAR(1) NOT NULL,
[CAT_GRP_ID] VARCHAR(30),
[USE_YN] CHAR(1) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WH_MD_B] PRIMARY KEY CLUSTERED
(
[MD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위모듈ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MD_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MD_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_MD_B'
GO

/****** Object:  Table [dbo].[WH_CAT_GRP_L]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_CAT_GRP_L](
[CAT_NO] INT NOT NULL,
[CAT_GRP_ID] VARCHAR(30) NOT NULL,
[CAT_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WH_CAT_GRP_L] PRIMARY KEY CLUSTERED
(
[CAT_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L', @level2type=N'COLUMN',@level2name=N'CAT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형그룹목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_CAT_GRP_L'
GO

/****** Object:  Table [dbo].[WH_RESC_B]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_RESC_B](
[RESC_ID] VARCHAR(30) NOT NULL,
[LANG_TYP_CD] VARCHAR(30) NOT NULL,
[RESC_VA] NVARCHAR(400),
CONSTRAINT [PK_WH_RESC_B] PRIMARY KEY CLUSTERED
(
[RESC_ID] ASC,
[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_RESC_B'
GO

/****** Object:  Table [dbo].[WH_REQ_FILE_D]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_REQ_FILE_D](
[FILE_ID] INT NOT NULL,
[TYP_CD] CHAR(1) NOT NULL,
[REF_ID] INT NOT NULL,
[DISP_NM] NVARCHAR(240),
[DISP_ORDR] INT,
[FILE_EXT] VARCHAR(30),
[FILE_SIZE] INT,
[SAVE_PATH] VARCHAR(240),
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
CONSTRAINT [PK_WH_REQ_FILE_D] PRIMARY KEY CLUSTERED
(
[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_FILE_D'
GO

/****** Object:  Table [dbo].[WH_REQ_EVAL_D]    Script Date: 2017/05/18 13:54 ******/
CREATE TABLE [dbo].[WH_REQ_EVAL_D](
[REQ_NO] INT NOT NULL,
[EVAL_NO] INT,
[EVAL_RSON] NVARCHAR(400),
[ADD_REQR] NVARCHAR(400),
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
CONSTRAINT [PK_WH_REQ_EVAL_D] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'평가번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'EVAL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'평가사유' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'EVAL_RSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'추가요청사항' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'ADD_REQR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청평가상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_EVAL_D'
GO


/****** Object:  Table [dbo].[WH_PLT_SETUP_D]    Script Date: 05/22/2017 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WH_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_WH_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PLT_SETUP_D'
GO

/****** Object:  Table [dbo].[WH_REQ_REL_D]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_REQ_REL_D](
[REQ_NO] INT NOT NULL,
[REL_NO] INT NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WH_REQ_REL_D] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC,
[REL_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_REL_D', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'관련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_REL_D', @level2type=N'COLUMN',@level2name=N'REL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_REL_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청관련상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_REL_D'
GO


/****** Object:  Table [dbo].[WH_DOC_NO_D]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_DOC_NO_D](
[YY] INT NOT NULL,
[ORG_ID] VARCHAR(30) NOT NULL,
[DOC_SEQ] BIGINT,
CONSTRAINT [PK_WH_DOC_NO_D] PRIMARY KEY CLUSTERED
(
[YY] ASC,
[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'년' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'YY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'DOC_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서번호상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_DOC_NO_D'
GO


/****** Object:  Table [dbo].[WH_REQ_STAT_D]    Script Date: 2017/07/06 14:34 ******/
CREATE TABLE [dbo].[WH_REQ_STAT_D](
[REQ_NO] INT NOT NULL,
[STAT_CD] CHAR(1) NOT NULL,
[HTML_YN] CHAR(1) NOT NULL,
CONSTRAINT [PK_WH_REQ_STAT_D] PRIMARY KEY CLUSTERED
(
[REQ_NO] ASC,
[STAT_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_STAT_D', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_STAT_D', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HTML여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_STAT_D', @level2type=N'COLUMN',@level2name=N'HTML_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청상태상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_STAT_D'
GO



/****** Object:  Table [dbo].[WH_PICH_GRP_B]    Script Date: 2017/07/10 11:26 ******/
CREATE TABLE [dbo].[WH_PICH_GRP_B](
[PICH_GRP_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[PICH_GRP_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[DFT_YN] CHAR(1) NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WH_PICH_GRP_B] PRIMARY KEY CLUSTERED
(
[PICH_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'PICH_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'PICH_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_B'
GO

/****** Object:  Table [dbo].[WH_PICH_GRP_L]    Script Date: 2017/07/10 11:26 ******/
CREATE TABLE [dbo].[WH_PICH_GRP_L](
[PICH_GRP_ID] VARCHAR(30) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WH_PICH_GRP_L] PRIMARY KEY CLUSTERED
(
[PICH_GRP_ID] ASC,
[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_L', @level2type=N'COLUMN',@level2name=N'PICH_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자그룹목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_PICH_GRP_L'
GO


/****** Object:  Table [dbo].[WH_REQ_ONGD_H]    Script Date: 2017/09/29 15:45 ******/
CREATE TABLE [dbo].[WH_REQ_ONGD_H](
[HST_NO] INT NOT NULL,
[REQ_NO] INT NOT NULL,
[PICH_UID] VARCHAR(30),
[CMPL_DUE_DT] DATETIME,
[ONGO_CONT] NVARCHAR(4000),
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME NOT NULL,
CONSTRAINT [PK_WH_REQ_ONGD_H] PRIMARY KEY CLUSTERED
(
[HST_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'REQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료예정일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'CMPL_DUE_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'ONGO_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'요청진행이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WH_REQ_ONGD_H'
GO

