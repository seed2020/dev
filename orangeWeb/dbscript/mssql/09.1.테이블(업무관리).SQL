/****** Object:  Table [dbo].[WF_FORM_FILE_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_FILE_D](
[FILE_ID] INT NOT NULL,
[REF_ID] INT,
[DISP_NM] NVARCHAR(240),
[DISP_ORDR] INT,
[FILE_EXT] VARCHAR(30),
[FILE_SIZE] INT,
[SAVE_PATH] VARCHAR(240),
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
CONSTRAINT [PK_WF_FORM_FILE_D] PRIMARY KEY CLUSTERED
(
[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_FILE_D'
GO

/****** Object:  Table [dbo].[WF_FORM_GEN_PROC_L]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_GEN_PROC_L](
[GEN_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[SEQ] INT NOT NULL,
[PROC_CONT] NVARCHAR(1000),
[ALL_CNT] BIGINT,
[PROC_CNT] BIGINT,
[STRT_DT] DATETIME,
[END_DT] DATETIME,
[ERR_CONT] NVARCHAR(1000),
[ERR_YN] CHAR(1),
CONSTRAINT [PK_WF_FORM_GEN_PROC_L] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[FORM_NO] ASC,
[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'순번' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'PROC_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'전체건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'ALL_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행건수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'PROC_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'오류내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'ERR_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'오류여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L', @level2type=N'COLUMN',@level2name=N'ERR_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식생성진행내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_PROC_L'
GO

/****** Object:  Table [dbo].[WF_FORM_B]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_B](
[FORM_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[FORM_ID] VARCHAR(30) NOT NULL,
[GRP_ID] VARCHAR(30) NOT NULL,
[FORM_NM] NVARCHAR(120) NOT NULL,
[FORM_DESC] NVARCHAR(200),
[RESC_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[FORM_TYP] CHAR(1) NOT NULL,
[REG_YN] CHAR(1),
[LST_YN] CHAR(1),
[MD_TYP_CD] CHAR(2),
[ALL_COMP_YN] CHAR(1),
[LST_TYP_CD] CHAR(1),
[GEN_ID] VARCHAR(30),
[STAT_CD] CHAR(1) NOT NULL,
[DEPLOY_DT] DATETIME,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WF_FORM_B] PRIMARY KEY CLUSTERED
(
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'FORM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'FORM_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식구분' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'FORM_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'REG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'LST_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'MD_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'전사여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'ALL_COMP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'LST_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배포일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'DEPLOY_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_B'
GO

/****** Object:  Table [dbo].[WF_FORM_GEN_TGT_T]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_GEN_TGT_T](
[GEN_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[SEQ] BIGINT NOT NULL,
[WORK_NO] INT NOT NULL,
CONSTRAINT [PK_WF_FORM_GEN_TGT_T] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[FORM_NO] ASC,
[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_TGT_T', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_TGT_T', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_TGT_T', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'업무번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_TGT_T', @level2type=N'COLUMN',@level2name=N'WORK_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식생성대상임시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_TGT_T'
GO

/****** Object:  Table [dbo].[WF_MD_WORKS_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_MD_WORKS_D](
[MD_CD] CHAR(2) NOT NULL,
[MD_REF_ID] VARCHAR(30) NOT NULL,
[MD_NO] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[GEN_ID] VARCHAR(30) NOT NULL,
[WORK_NO] INT NOT NULL,
CONSTRAINT [PK_WF_MD_WORKS_D] PRIMARY KEY CLUSTERED
(
[MD_CD] ASC,
[MD_REF_ID] ASC,
[MD_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'MD_REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'MD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'업무번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D', @level2type=N'COLUMN',@level2name=N'WORK_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈업무상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_WORKS_D'
GO

/****** Object:  Table [dbo].[WF_FORM_GEN_B]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_GEN_B](
[GEN_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[STRT_DT] DATETIME,
[END_DT] DATETIME,
[PROC_STAT_CD] VARCHAR(30),
[DEFRAG_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
CONSTRAINT [PK_WF_FORM_GEN_B] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'PROC_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조각모음여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'DEFRAG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식생성기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_B'
GO

/****** Object:  Table [dbo].[WF_FORM_REG_H]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_REG_H](
[GEN_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[LOUT_VA] NTEXT NOT NULL,
[ATTR_VA] NTEXT,
[TAB_VA] NTEXT,
[MOB_REG_TYP_CD] CHAR(1),
CONSTRAINT [PK_WF_FORM_REG_H] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'레이아웃값 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'LOUT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'ATTR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'탭값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'TAB_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모바일등록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H', @level2type=N'COLUMN',@level2name=N'MOB_REG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식등록이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_H'
GO

/****** Object:  Table [dbo].[WF_FORM_REG_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_REG_D](
[FORM_NO] INT NOT NULL,
[LOUT_VA] NTEXT NOT NULL,
[ATTR_VA] NTEXT,
[TAB_VA] NTEXT,
[MOB_REG_TYP_CD] CHAR(1),
CONSTRAINT [PK_WF_FORM_REG_D] PRIMARY KEY CLUSTERED
(
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'레이아웃값 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D', @level2type=N'COLUMN',@level2name=N'LOUT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D', @level2type=N'COLUMN',@level2name=N'ATTR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'탭값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D', @level2type=N'COLUMN',@level2name=N'TAB_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모바일등록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D', @level2type=N'COLUMN',@level2name=N'MOB_REG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식등록상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_REG_D'
GO

/****** Object:  Table [dbo].[WF_PLT_SETUP_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_PLT_SETUP_D](
[USER_UID] VARCHAR(120) NOT NULL,
[PLT_ID] VARCHAR(30) NOT NULL,
[BX_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WF_PLT_SETUP_D] PRIMARY KEY CLUSTERED
(
[USER_UID] ASC,
[PLT_ID] ASC,
[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_PLT_SETUP_D'
GO

/****** Object:  Table [dbo].[WF_FORM_LST_H]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_LST_H](
[GEN_ID] VARCHAR(30) NOT NULL,
[COLM_ID] VARCHAR(30) NOT NULL,
[MD_CD] CHAR(1) NOT NULL,
[FORM_NO] INT NOT NULL,
[WDTH_PERC] VARCHAR(30),
[ALN_VA] VARCHAR(30),
[SORT_OPT_VA] VARCHAR(30),
[DATA_SORT_VA] VARCHAR(30),
[SORT_ORDR] INT NOT NULL,
[SRCH_YN] CHAR(1),
CONSTRAINT [PK_WF_FORM_LST_H] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[COLM_ID] ASC,
[MD_CD] ASC,
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H', @level2type=N'COLUMN',@level2name=N'SRCH_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식목록이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_H'
GO

/****** Object:  Table [dbo].[WF_FORM_GEN_L]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_GEN_L](
[GEN_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[FORM_RESC_ID] VARCHAR(30),
[PROC_STAT_CD] VARCHAR(30),
[REG_DT] DATETIME,
CONSTRAINT [PK_WF_FORM_GEN_L] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'FORM_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'진행상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'PROC_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식생성목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GEN_L'
GO

/****** Object:  Table [dbo].[WF_CD_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_CD_D](
[CD_ID] VARCHAR(30) NOT NULL,
[CD_GRP_ID] VARCHAR(30) NOT NULL,
[CD_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WF_CD_D] PRIMARY KEY CLUSTERED
(
[CD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'CD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'CD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_D'
GO

/****** Object:  Table [dbo].[WF_FORM_MOB_H]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_MOB_H](
[GEN_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[LOUT_VA] NTEXT NOT NULL,
[TAB_VA] NTEXT,
CONSTRAINT [PK_WF_FORM_MOB_H] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_H', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_H', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'레이아웃값 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_H', @level2type=N'COLUMN',@level2name=N'LOUT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'탭값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_H', @level2type=N'COLUMN',@level2name=N'TAB_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식모바일이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_H'
GO

/****** Object:  Table [dbo].[WF_FORM_LST_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_LST_D](
[COLM_ID] VARCHAR(30) NOT NULL,
[MD_CD] CHAR(1) NOT NULL,
[FORM_NO] INT NOT NULL,
[WDTH_PERC] VARCHAR(30),
[ALN_VA] VARCHAR(30),
[SORT_OPT_VA] VARCHAR(30),
[DATA_SORT_VA] VARCHAR(30),
[SORT_ORDR] INT NOT NULL,
[SRCH_YN] CHAR(1),
CONSTRAINT [PK_WF_FORM_LST_D] PRIMARY KEY CLUSTERED
(
[COLM_ID] ASC,
[MD_CD] ASC,
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D', @level2type=N'COLUMN',@level2name=N'SRCH_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식목록상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_LST_D'
GO

/****** Object:  Table [dbo].[WF_FORM_GRP_B]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_GRP_B](
[GRP_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[GRP_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[GRP_PID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WF_FORM_GRP_B] PRIMARY KEY CLUSTERED
(
[GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식그룹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_GRP_B'
GO

/****** Object:  Table [dbo].[WF_FORM_COLM_H]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_COLM_H](
[GEN_ID] VARCHAR(30) NOT NULL,
[COLM_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[COLM_TYP_CD] VARCHAR(30) NOT NULL,
[COLM_NM] VARCHAR(120) NOT NULL,
[ITEM_NM] VARCHAR(200) NOT NULL,
[RESC_ID] VARCHAR(30),
[SORT_ORDR] INT NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[LST_YN] CHAR(1),
[DEPLOY_YN] CHAR(1),
[JSON_PATH] NVARCHAR(200),
CONSTRAINT [PK_WF_FORM_COLM_H] PRIMARY KEY CLUSTERED
(
[GEN_ID] ASC,
[COLM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'COLM_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'COLM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'ITEM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'LST_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배포여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'DEPLOY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'JSON_PATH' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H', @level2type=N'COLUMN',@level2name=N'JSON_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식컬럼이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_H'
GO

/****** Object:  Table [dbo].[WF_CD_GRP_B]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_CD_GRP_B](
[CD_GRP_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CD_GRP_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[CD_USE_YN] CHAR(1) NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WF_CD_GRP_B] PRIMARY KEY CLUSTERED
(
[CD_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_CD_GRP_B'
GO

/****** Object:  Table [dbo].[WF_MD_FORM_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_MD_FORM_D](
[MD_CD] CHAR(2) NOT NULL,
[MD_REF_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[GEN_ID] VARCHAR(30) NOT NULL,
[JSON_VA] NTEXT,
[LOUT_VA] NTEXT,
[ATTR_VA] NTEXT,
CONSTRAINT [PK_WF_MD_FORM_D] PRIMARY KEY CLUSTERED
(
[MD_CD] ASC,
[MD_REF_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'MD_REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'GEN_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'JSON값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'JSON_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'레이아웃값 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'LOUT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D', @level2type=N'COLUMN',@level2name=N'ATTR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'모듈양식상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_MD_FORM_D'
GO

/****** Object:  Table [dbo].[WF_FORM_MOB_D]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_MOB_D](
[FORM_NO] INT NOT NULL,
[LOUT_VA] NTEXT NOT NULL,
[TAB_VA] NTEXT,
CONSTRAINT [PK_WF_FORM_MOB_D] PRIMARY KEY CLUSTERED
(
[FORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_D', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'레이아웃값 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_D', @level2type=N'COLUMN',@level2name=N'LOUT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'탭값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_D', @level2type=N'COLUMN',@level2name=N'TAB_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식모바일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_MOB_D'
GO

/****** Object:  Table [dbo].[WF_RESC_B]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_RESC_B](
[RESC_ID] VARCHAR(30) NOT NULL,
[LANG_TYP_CD] VARCHAR(30) NOT NULL,
[RESC_VA] NVARCHAR(400),
CONSTRAINT [PK_WF_RESC_B] PRIMARY KEY CLUSTERED
(
[RESC_ID] ASC,
[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_RESC_B'
GO

/****** Object:  Table [dbo].[WF_FORM_COLM_L]    Script Date: 2018/08/03 12:45 ******/
CREATE TABLE [dbo].[WF_FORM_COLM_L](
[COLM_ID] VARCHAR(30) NOT NULL,
[FORM_NO] INT NOT NULL,
[COLM_TYP_CD] VARCHAR(30) NOT NULL,
[COLM_NM] VARCHAR(120) NOT NULL,
[ITEM_NM] VARCHAR(200) NOT NULL,
[RESC_ID] VARCHAR(30),
[SORT_ORDR] INT NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[LST_YN] CHAR(1),
[DEPLOY_YN] CHAR(1),
[JSON_PATH] NVARCHAR(200),
CONSTRAINT [PK_WF_FORM_COLM_L] PRIMARY KEY CLUSTERED
(
[COLM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'FORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'COLM_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'COLM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'ITEM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'LST_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배포여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'DEPLOY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'JSON_PATH' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L', @level2type=N'COLUMN',@level2name=N'JSON_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양식컬럼목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WF_FORM_COLM_L'
GO

