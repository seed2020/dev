
/****** Object:  Table [dbo].[WV_SURV_USE_AUTH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_USE_AUTH_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[AUTH_TGT_TYP_CD] [varchar](30) NOT NULL,
	[AUTH_TGT_UID] [varchar](10) NOT NULL,
	[AUTH_INCL_YN] [char](1) NULL,
	[AUTH_GRAD_CD] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WV_SURV_USE_AUTH_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[SURV_ID] ASC,
	[AUTH_TGT_TYP_CD] ASC,
	[AUTH_TGT_UID] ASC,
	[AUTH_GRAD_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한대상구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_TGT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한대상UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_TGT_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한 포함여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_INCL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_GRAD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문권한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_USE_AUTH_D'
GO
/****** Object:  Table [dbo].[WV_SURV_REPLY_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_REPLY_D](
	[REPLY_NO] [int] NOT NULL,
	[REPLYR_UID] [varchar](30) NOT NULL,
	[REPLYR_DEPT_ID] [varchar](30) NULL,
	[REPLY_DT] [datetime] NULL,
	[MULC_INPUT_REPLY_CONT] [nvarchar](256) NULL,
	[OEND_REPLY_CONT] [nvarchar](1000) NULL,
	[QUES_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WV_SURV_REPLY_D] PRIMARY KEY CLUSTERED 
(
	[REPLY_NO] ASC,
	[REPLYR_UID] ASC,
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLY_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변자ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLYR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변자 부서 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLYR_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLY_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'객관식입력항목답변내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'MULC_INPUT_REPLY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주관식답변내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'OEND_REPLY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_REPLY_D'
GO
/****** Object:  Table [dbo].[WV_SURV_QUES_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_QUES_D](
	[QUES_ID] [varchar](30) NOT NULL,
	[QUES_SORT_ORDR] [int] NULL,
	[QUES_CONT] [nvarchar](2000) NULL,
	[QUES_IMG_ATT_YN] [char](1) NULL,
	[IMG_SURV_FILE_ID] [varchar](30) NULL,
	[EXAM_CHOI_ITEM_CD] [varchar](30) NULL,
	[MUL_CHOI_YN] [char](1) NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[MANDA_REPLY_YN] [char](1) NULL,
 CONSTRAINT [PK_WV_SURV_QUES_D] PRIMARY KEY CLUSTERED 
(
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문이미지첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_IMG_ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'IMG_SURV_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보기선택항목코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'EXAM_CHOI_ITEM_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'복수선택여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'MUL_CHOI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'필수답변여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'MANDA_REPLY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_QUES_D'
GO
/****** Object:  Table [dbo].[WV_SURV_POLC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_POLC_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[APVD_YN] [char](1) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WV_SURV_POLC_D] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'승인여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'APVD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문정책상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POLC_D'
GO
/****** Object:  Table [dbo].[WV_SURV_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[DISP_ORDR] [int] NULL,
	[REF_ID] [varchar](30) NOT NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_WV_SURV_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일사이즈' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_FILE_D'
GO
/****** Object:  Table [dbo].[WV_SURV_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_SURV_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[SURV_SUBJ] [nvarchar](256) NULL,
	[QUES_CNT] [int] NULL,
	[SURV_START_DT] [datetime] NULL,
	[SURV_END_DT] [datetime] NULL,
	[PLT_ID] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NOT NULL,
	[SURV_PRG_STAT_CD] [varchar](1) NULL,
	[OPEN_YN] [char](1) NULL,
	[REPET_SURV_YN] [char](1) NULL,
	[SURV_ITNT] [ntext] NULL,
	[SURV_FTR] [ntext] NULL,
	[ATT_YN] [char](1) NULL,
	[ADM_RJT_OPIN_CONT] [nvarchar](2000) NULL,
	[SURV_FILE_ID] [varchar](30) NULL,
	[NO_AUTH_YN] [char](1) NULL,
 CONSTRAINT [PK_WV_SURV_B] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문 제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문 개수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'QUES_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문 시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문 종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문진행상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_PRG_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공개여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'OPEN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'재설문여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'REPET_SURV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문취지' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_ITNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문바닥글' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_FTR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관리자반려의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'ADM_RJT_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'비권한여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B', @level2type=N'COLUMN',@level2name=N'NO_AUTH_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_B'
GO
/****** Object:  Table [dbo].[WV_QUES_EXAM_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WV_QUES_EXAM_D](
	[EXAM_NO] [int] NOT NULL,
	[EXAM_ORDR] [int] NULL,
	[EXAM_DISP_NM] [nvarchar](256) NULL,
	[INPUT_YN] [char](1) NULL,
	[EXAM_IMG_USE_YN] [char](1) NULL,
	[IMG_SURV_FILE_ID] [varchar](30) NULL,
	[QUES_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WV_QUES_EXAM_D] PRIMARY KEY CLUSTERED 
(
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC,
	[EXAM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'입력 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'INPUT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기이미지사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_IMG_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지설문파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'IMG_SURV_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문보기상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_QUES_EXAM_D'
GO
/****** Object:  Table [dbo].[WR_REZV_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_REZV_B](
	[REZV_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](30) NOT NULL,
	[RESC_KND_ID] [varchar](30) NOT NULL,
	[RESC_MNG_ID] [varchar](30) NOT NULL,
	[REZV_STRT_DT] [datetime] NOT NULL,
	[REZV_END_DT] [datetime] NOT NULL,
	[SUBJ] [nvarchar](240) NOT NULL,
	[CONT] [ntext] NULL,
	[DISC_STAT_CD] [char](1) NULL,
	[DISCR_UID] [varchar](30) NULL,
	[DISC_DT] [datetime] NULL,
	[DISC_CONT] [varchar](240) NULL,
	[SCHDL_ID] [varchar](30) NULL,
	[SCHDL_KND_CD] [varchar](30) NULL,
	[BGCOL_CD] [varchar](10) NULL,
	[RESQ_EMAIL_YN] [char](1) NULL,
	[RES_EMAIL_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,	
 CONSTRAINT [PK_WR_REZV_B] PRIMARY KEY CLUSTERED 
(
	[REZV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'예약ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'REZV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원종류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'RESC_KND_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원관리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'RESC_MNG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'예약시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'REZV_STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'예약종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'REZV_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'DISC_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'DISCR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'DISC_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'DISC_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정대상코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'SCHDL_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배경색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'BGCOL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'신청메일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'RESQ_EMAIL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'결과메일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'RES_EMAIL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'예약기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_REZV_B'
GO
/****** Object:  Table [dbo].[WR_RESC_PLT_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_RESC_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_WR_RESC_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_PLT_SETUP_D'
GO
/****** Object:  Table [dbo].[WR_RESC_MNG_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_RESC_MNG_B](
	[RESC_MNG_ID] [varchar](30) NOT NULL,
	[RESC_KND_ID] [varchar](30) NULL,
	[COMP_ID] [varchar](30) NULL,
	[RESC_ID] [varchar](30) NULL,
	[RESC_NM] [nvarchar](120) NULL,
	[RESC_LOC] [varchar](240) NULL,
	[SUPL_STAT] [varchar](240) NULL,
	[RESC_ADM_UID] [varchar](30) NULL,
	[DISC_YN] [char](1) NULL,
	[USE_YN] [char](1) NULL,
	[SORT_ORDR]  [INT] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WR_RESC_MNG_B] PRIMARY KEY CLUSTERED 
(
	[RESC_MNG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원관리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_MNG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원종류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_KND_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원위치' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_LOC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'비품현황' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'SUPL_STAT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원관리자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'RESC_ADM_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'DISC_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원관리기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_MNG_B'
GO
/****** Object:  Table [dbo].[WR_RESC_KND_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_RESC_KND_B](
	[RESC_KND_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](30) NULL,
	[RESC_ID] [varchar](30) NULL,
	[KND_NM] [nvarchar](30) NULL,
	[BGCOL_CD] [varchar](10) NULL,
	[USE_YN] [char](1) NULL,
	[SORT_ORDR] [INT] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WR_RESC_KND_B] PRIMARY KEY CLUSTERED 
(
	[RESC_KND_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원종류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'RESC_KND_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종류명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'KND_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배경색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'BGCOL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원종류기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_KND_B'
GO
/****** Object:  Table [dbo].[WR_RESC_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_RESC_IMG_D](
	[RESC_MNG_ID] [varchar](30) NOT NULL,
	[IMG_PATH] [varchar](240) NULL,
	[IMG_WDTH] [int] NULL,
	[IMG_HGHT] [int] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WR_RESC_IMG_D] PRIMARY KEY CLUSTERED 
(
	[RESC_MNG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원관리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'RESC_MNG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자원이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_IMG_D'
GO
/****** Object:  Table [dbo].[WR_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WR_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_WR_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WR_RESC_B'
GO
/****** Object:  Table [dbo].[WM_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WM_RESC_B](
	[COMP_ID] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NOT NULL,
 CONSTRAINT [PK_WM_RESC_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WM_RESC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WM_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WM_RESC_B'
GO
/****** Object:  Table [dbo].[WC_SCHDL_GRP_MBSH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_SCHDL_GRP_MBSH_D](
	[USER_UID] [varchar](30) NOT NULL,
	[MBSH_TYP_CD] [varchar](30) NULL,
	[AUTH_GRAD_CD] [varchar](30) NULL,
	[EMAIL_SEND_YN] [char](1) NULL,
	[SCHDL_GRP_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_NM] [nvarchar](120) NULL,
 CONSTRAINT [PK_WC_SCHDL_GRP_MBSH_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[SCHDL_GRP_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회원구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'MBSH_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'AUTH_GRAD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이메일발송여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'EMAIL_SEND_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정 그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'SCHDL_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D', @level2type=N'COLUMN',@level2name=N'USER_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정_그룹_회원_상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_MBSH_D'
GO
/****** Object:  Table [dbo].[WC_SCHDL_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_SCHDL_GRP_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[SCHDL_GRP_ID] [varchar](30) NOT NULL,
	[GRP_NM] [nvarchar](100) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MASTR_UID] [varchar](30) NULL,
	[REGR_NM] [nvarchar](120) NULL,
 CONSTRAINT [PK_WC_SCHDL_GRP_B] PRIMARY KEY NONCLUSTERED 
(
	[SCHDL_GRP_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정 그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'SCHDL_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'마스터UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'MASTR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정_그룹_기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_GRP_B'
GO
/****** Object:  Table [dbo].[WC_SCHDL_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_SCHDL_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[DISP_ORDR] [int] NULL,
	[REF_ID] [varchar](30) NOT NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_WC_SCHDL_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_FILE_D'
GO
/****** Object:  Table [dbo].[WC_SCHDL_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_SCHDL_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[SCHDL_TYP_CD] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NOT NULL,
	[LOC_NM] [nvarchar](60) NULL,
	[OPEN_GRAD_CD] [varchar](30) NULL,
	[SCHDL_STAT_CD] [varchar](30) NULL,
	[WORK_PRIO_ORDR] [varchar](1) NULL,
	[WORK_CMLT_YMD] [datetime] NULL,
	[CONT] [ntext] NULL,
	[ATT_YN] [char](1) NULL,
	[SELF_REG_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[EDITOR_TYP_CD] [varchar](30) NULL,
	[SCHDL_ID] [varchar](30) NOT NULL,
	[MOD_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[SCHDL_FILE_ID] [varchar](30) NULL,
	[SCHDL_START_DT] [datetime] NOT NULL,
	[SCHDL_END_DT] [datetime] NOT NULL,
	[REPET_YN] [char](1) NULL,
	[SCHDL_PID] [varchar](30) NULL,
	[SCHDL_KND_CD] [varchar](30) NULL,
	[GRP_ID] [varchar](30) NULL,
	[DEPT_ID] [varchar](30) NULL,
	[HOLI_YN] [char](1) NOT NULL,
	[SOLA_LUNA_YN] [char](1) NOT NULL,
	[LOC_LATD_LOTD_VA] [varchar](20) NULL,
	[REGR_NM] [nvarchar](120) NULL,
	[ALLDAY_YN] [char](1) NULL,
 CONSTRAINT [PK_WC_SCHDL_B] PRIMARY KEY CLUSTERED 
(
	[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'장소명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'LOC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공개등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'OPEN_GRAD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'할일우선순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'WORK_PRIO_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'할일완료년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'WORK_CMLT_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본인입력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SELF_REG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'에디터구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'EDITOR_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반복여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REPET_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'휴일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'HOLI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양음력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SOLA_LUNA_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'장소위도경도값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'LOC_LATD_LOTD_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REGR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B', @level2type=N'COLUMN',@level2name=N'ALLDAY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_B'
GO
/****** Object:  Table [dbo].[WC_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NOT NULL,
	[COMP_ID] [varchar](30) NULL,
 CONSTRAINT [PK_WC_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_RESC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_RESC_B'
GO
/****** Object:  Table [dbo].[WC_REPET_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_REPET_SETUP_D](
	[SCHDL_ID] [varchar](30) NOT NULL,
	[REPET_PERD_CD] [varchar](30) NOT NULL,
	[APNT_MM] [varchar](4) NULL,
	[REPET_MM] [varchar](4) NULL,
	[APNT_WK] [varchar](4) NULL,
	[REPET_WK] [varchar](4) NULL,
	[APNT_DD] [varchar](4) NULL,
	[REPET_DD] [varchar](4) NULL,
	[APNT_DY] [varchar](30) NULL,
	[REPET_START_DT] [datetime] NULL,
	[REPET_END_DT] [datetime] NULL,
 CONSTRAINT [PK_WC_REPET_SETUP_D] PRIMARY KEY NONCLUSTERED 
(
	[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복주기코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_PERD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정월' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_MM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반복월' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_MM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정주' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_WK'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반복주' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_WK'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정요일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_DY'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반복일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_DD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_DD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반복설정_상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_REPET_SETUP_D'
GO
/****** Object:  Table [dbo].[WC_PROM_GUEST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_PROM_GUEST_D](
	[GUEST_UID] [varchar](30) NOT NULL,
	[GUEST_NM] [nvarchar](20) NOT NULL,
	[GUEST_POSIT_NM] [nvarchar](100) NULL,
	[GUEST_DEPT_NM] [nvarchar](100) NULL,
	[GUEST_COMP_NM] [nvarchar](100) NULL,
	[STAT_CD] [varchar](30) NOT NULL,
	[OPIN_CONT] [nvarchar](255) NULL,
	[GUEST_EMPL_YN] [varchar](30) NULL,
	[EMAIL] [varchar](240) NULL,
	[EMAIL_SEND_YN] [char](1) NULL,
	[SCHDL_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WC_PROM_GUEST_D] PRIMARY KEY CLUSTERED 
(
	[SCHDL_ID] ASC,
	[GUEST_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자 성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자 직위명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_POSIT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자 부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자 회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'상태 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참석자_임직원 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_EMPL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이메일발송여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'EMAIL_SEND_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'약속_참석자_상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_PROM_GUEST_D'
GO
/****** Object:  Table [dbo].[WC_ENV_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_ENV_SETUP_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[PAGE_PLT_TYP_CD] [varchar](30) NOT NULL,
	[SCHDL_TYP_CD] [varchar](30) NULL,
	[SCHDL_KND_CD] [varchar](30) NULL,
	[SOLA_LUNA_YN] [char](1) NULL,
 CONSTRAINT [PK_WC_ENV_SETUP_D] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[USER_UID] ASC,
	[PAGE_PLT_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'페이지/포틀릿 구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'PAGE_PLT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'SCHDL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일정종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'SCHDL_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D', @level2type=N'COLUMN',@level2name=N'SOLA_LUNA_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'환경_설정_상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ENV_SETUP_D'
GO
/****** Object:  Table [dbo].[WC_CAT_CLS_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_CAT_CLS_B](
	[CAT_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NULL,
	[RESC_ID] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[BGCOL_CD] [varchar](10) NULL,
	[FONT_COLR_CD] [varchar](10) NULL,
	[USE_YN] [char](1) NULL,
	[CAT_NM] [nvarchar](120) NULL,
 CONSTRAINT [PK_WC_CAT_CLS_B] PRIMARY KEY CLUSTERED 
(
	[CAT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배경색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'BGCOL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'글자색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'FONT_COLR_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리분류기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_CAT_CLS_B'
GO
/****** Object:  Table [dbo].[WC_BUMK_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_BUMK_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[BUMK_ID] [varchar](30) NOT NULL,
	[BUMK_DISP_NM] [nvarchar](100) NULL,
	[BUMK_TGT_UID] [varchar](30) NULL,
	[BUMK_TGT_DEPT_ID] [varchar](30) NULL,
	[USER_DEPT_TYP_CD] [varchar](30) NULL,
 CONSTRAINT [PK_WC_BUMK_D] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[USER_UID] ASC,
	[BUMK_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'즐겨찾기ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'BUMK_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'즐겨찾기표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'BUMK_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'즐겨찾기 대상자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'BUMK_TGT_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'즐겨찾기 대상 부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'BUMK_TGT_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 부서 구분 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D', @level2type=N'COLUMN',@level2name=N'USER_DEPT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'즐겨찾기_상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_BUMK_D'
GO
/****** Object:  Table [dbo].[WC_APNTR_SCHDL_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_APNTR_SCHDL_D](
	[USER_UID] [varchar](30) NOT NULL,
	[APNTR_UID] [varchar](30) NOT NULL,
	[ORDR] [int] NULL,
	[COMP_ID] [varchar](3) NOT NULL,
 CONSTRAINT [PK_WC_APNTR_SCHDL_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[APNTR_UID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_SCHDL_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_SCHDL_D', @level2type=N'COLUMN',@level2name=N'APNTR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_SCHDL_D', @level2type=N'COLUMN',@level2name=N'ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_SCHDL_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정인관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_SCHDL_D'
GO
/****** Object:  Table [dbo].[WC_APNTR_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_APNTR_R](
	[COMP_ID] [varchar](3) NOT NULL,
	[APNTR_UID] [varchar](30) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WC_APNTR_R] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[USER_UID] ASC,
	[APNTR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_R', @level2type=N'COLUMN',@level2name=N'APNTR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'지정인_관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_APNTR_R'
GO
/****** Object:  Table [dbo].[WC_AGNT_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WC_AGNT_R](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[AGNT_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WC_AGNT_R] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[USER_UID] ASC,
	[AGNT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_AGNT_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_AGNT_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대리인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_AGNT_R', @level2type=N'COLUMN',@level2name=N'AGNT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대리인관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_AGNT_R'
GO
/****** Object:  Table [dbo].[WB_METNG_CLS_CD_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_METNG_CLS_CD_B](
	[COMP_ID] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[CD_VA] [nvarchar](400) NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_METNG_CLS_CD_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[RESC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'CD_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'미팅분류코드기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_METNG_CLS_CD_B'
GO
/****** Object:  Table [dbo].[WB_BC_USER_SCRN_SETUP_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_USER_SCRN_SETUP_R](
	[COMP_ID] [varchar](30) NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[INPUT_WND_LOC_CD] [varchar](30) NULL,
	[LST_TYP_CD] [varchar](30) NULL,
 CONSTRAINT [PK_WB_BC_USER_SCRN_SETUP_R] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[REGR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_SCRN_SETUP_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_SCRN_SETUP_R', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'입력창위치코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_SCRN_SETUP_R', @level2type=N'COLUMN',@level2name=N'INPUT_WND_LOC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_SCRN_SETUP_R', @level2type=N'COLUMN',@level2name=N'LST_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함사용자화면설정관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_SCRN_SETUP_R'
GO
/****** Object:  Table [dbo].[WB_BC_USER_LST_SETUP_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_USER_LST_SETUP_R](
	[COMP_ID] [varchar](30) NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[ATRB_ID] [varchar](30) NOT NULL,
	[MSG_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NULL,
	[ALN_VA] [varchar](30) NULL,
	[WDTH_PERC] [varchar](30) NULL,
	[DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_WB_BC_USER_LST_SETUP_R] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[REGR_UID] ASC,
	[ATRB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메시지ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'MSG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄마춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R', @level2type=N'COLUMN',@level2name=N'DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함사용자화면설정관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_USER_LST_SETUP_R'
GO
/****** Object:  Table [dbo].[WB_BC_PLT_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_WB_BC_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_PLT_SETUP_D'
GO
/****** Object:  Table [dbo].[WB_BC_METNG_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_METNG_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[REF_ID] [varchar](30) NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[DISP_ORDR] [int] NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_BC_METNG_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_FILE_D'
GO
/****** Object:  Table [dbo].[WB_BC_METNG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_METNG_D](
	[COMP_ID] [varchar](30) NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[BC_METNG_DETL_ID] [varchar](30) NOT NULL,
	[BC_ID] [varchar](30) NULL,
	[AGN_YN] [char](1) NULL,
	[METNG_YMD] [datetime] NOT NULL,
	[OPEN_YN] [char](1) NOT NULL,
	[METNG_CLS_CD] [varchar](30) NOT NULL,
	[METNG_SUBJ] [varchar](240) NOT NULL,
	[METNG_CONT] [varchar](1000) NOT NULL,
	[ATT_FILE_ID] [varchar](30) NULL,
	[READ_CNT] [int] NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_BC_METNG_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[REGR_UID] ASC,
	[BC_METNG_DETL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅상세ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'BC_METNG_DETL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대리여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'AGN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'미팅년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'METNG_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공개여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'OPEN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'미팅분류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'METNG_CLS_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'미팅제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'METNG_SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'미팅내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'METNG_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_D'
GO
/****** Object:  Table [dbo].[WB_BC_METNG_ATND_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_METNG_ATND_R](
	[COMP_ID] [varchar](30) NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[BC_METNG_DETL_ID] [varchar](30) NOT NULL,
	[BC_METNG_ATND_DETL_ID] [varchar](30) NOT NULL,
	[EMPL_TYP_CD] [varchar](10) NULL,
	[EMPL_NM] [nvarchar](50) NULL,
	[EMPL_PHON] [varchar](120) NULL,
	[EMPL_ID] [varchar](30) NULL,
	[COMP_NM] [nvarchar](120) NULL,
	[COMP_PHON] [varchar](120) NULL,
	[EMAIL] [varchar](240) NULL,
 CONSTRAINT [PK_WB_BC_METNG_ATND_R] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[REGR_UID] ASC,
	[BC_METNG_DETL_ID] ASC,
	[BC_METNG_ATND_DETL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅상세ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'BC_METNG_DETL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅참석자상세ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'BC_METNG_ATND_DETL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임직원구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'EMPL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임직원성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'EMPL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임직원전화번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'EMPL_PHON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임직원ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'EMPL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사전화번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'COMP_PHON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함미팅참석자관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_METNG_ATND_R'
GO
/****** Object:  Table [dbo].[WB_BC_LST_SETUP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_LST_SETUP_B](
	[COMP_ID] [varchar](30) NOT NULL,
	[ATRB_ID] [varchar](30) NOT NULL,
	[MSG_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NULL,
	[DFT_SETUP_YN] [char](1) NULL,
	[ALN_VA] [varchar](30) NULL,
	[WDTH_PERC] [varchar](30) NULL,
	[DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_WB_BC_LST_SETUP_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[ATRB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메시지ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'MSG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본설정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'DFT_SETUP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄마춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B', @level2type=N'COLUMN',@level2name=N'DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함목록설정기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_LST_SETUP_B'
GO
/****** Object:  Table [dbo].[WB_BC_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_IMG_D](
	[BC_ID] [varchar](30) NOT NULL,
	[IMG_PATH] [varchar](240) NOT NULL,
	[IMG_WDTH] [int] NOT NULL,
	[IMG_HGHT] [int] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_BC_IMG_D] PRIMARY KEY CLUSTERED 
(
	[BC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_IMG_D'
GO
/****** Object:  Table [dbo].[WB_BC_FLD_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_FLD_B](
	[REGR_UID] [varchar](30) NOT NULL,
	[BC_FLD_ID] [varchar](30) NOT NULL,
	[FLD_NM] [nvarchar](120) NULL,
	[ABV_FLD_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[COMP_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WB_BC_FLD_B] PRIMARY KEY CLUSTERED 
(
	[REGR_UID] ASC,
	[BC_FLD_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'BC_FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'ABV_FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함폴더기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FLD_B'
GO
/****** Object:  Table [dbo].[WB_BC_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[REF_ID] [varchar](30) NOT NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[DISP_ORDR] [int] NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_BC_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_FILE_D'
GO
/****** Object:  Table [dbo].[WB_BC_CNTC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_CNTC_D](
	[BC_ID] [varchar](30) NOT NULL,
	[CNTC_TYP_CD] [varchar](30) NOT NULL,
	[BC_CNTC_SEQ] [int] NOT NULL,
	[CNTC_CONT] [varchar](120) NOT NULL,
	[DFT_CNTC_YN] [char](1) NULL,
	[CNTC_CLS_CD] [varchar](10) NOT NULL,
 CONSTRAINT [PK_WB_BC_CNTC_D] PRIMARY KEY CLUSTERED 
(
	[BC_ID] ASC,
	[CNTC_TYP_CD] ASC,
	[BC_CNTC_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'연락처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'CNTC_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함연락처일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'BC_CNTC_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'연락처내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'CNTC_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'우선순위여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'DFT_CNTC_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'연락처분류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D', @level2type=N'COLUMN',@level2name=N'CNTC_CLS_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함연락처상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CNTC_D'
GO
/****** Object:  Table [dbo].[WB_BC_CLNS_C]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_CLNS_C](
	[REGR_UID] [varchar](30) NOT NULL,
	[CLNS_NM] [nvarchar](120) NOT NULL,
	[CLNS_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WB_BC_CLNS_C] PRIMARY KEY CLUSTERED 
(
	[REGR_UID] ASC,
	[CLNS_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CLNS_C', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'친밀도명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CLNS_C', @level2type=N'COLUMN',@level2name=N'CLNS_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'친밀도ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CLNS_C', @level2type=N'COLUMN',@level2name=N'CLNS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함친밀도코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_CLNS_C'
GO
/****** Object:  Table [dbo].[WB_BC_BUMK_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_BUMK_R](
	[REGR_UID] [varchar](30) NOT NULL,
	[BC_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_WB_BC_BUMK_R] PRIMARY KEY CLUSTERED 
(
	[REGR_UID] ASC,
	[BC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_BUMK_R', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_BUMK_R', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_BUMK_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함즐겨찾기관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_BUMK_R'
GO
/****** Object:  Table [dbo].[WB_BC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_B](
	[BC_ID] [varchar](30) NOT NULL,
	[OPEN_TYP_CD] [varchar](30) NOT NULL,
	[FLD_ID] [varchar](30) NULL,
	[BC_NM] [nvarchar](30) NOT NULL,
	[COMP_NM] [nvarchar](120) NULL,
	[DEPT_NM] [nvarchar](120) NULL,
	[GRADE_NM] [nvarchar](30) NULL,
	[TICH_CONT] [nvarchar](800) NULL,
	[IPTFG_YN] [char](1) NULL,
	[FNO] [varchar](20) NULL,
	[COMP_ZIP_NO] [varchar](10) NULL,
	[COMP_ADR] [nvarchar](240) NULL,
	[COMP_DETL_ADR] [nvarchar](240) NULL,
	[HOME_ZIP_NO] [varchar](10) NULL,
	[HOME_ADR] [nvarchar](240) NULL,
	[HOME_DETL_ADR] [nvarchar](240) NULL,
	[BUMK_YN] [char](1) NULL,
	[PHOTO_FILE_ID] [varchar](30) NULL,
	[GEN_CD] [varchar](30) NULL,
	[NATY_CD] [varchar](30) NULL,
	[BIRTH] [datetime] NULL,
	[BIRTH_SCLC_CD] [varchar](30) NULL,
	[WEDD_ANNV] [datetime] NULL,
	[WEDD_ANNV_SCLC_CD] [varchar](30) NULL,
	[PSN_HPAGE_URL] [nvarchar](1000) NULL,
	[COMP_HPAGE_URL] [nvarchar](1000) NULL,
	[HOBY_CONT] [nvarchar](400) NULL,
	[SPECT_CONT] [nvarchar](400) NULL,
	[ESCHL_NM] [nvarchar](120) NULL,
	[MSCHL_NM] [nvarchar](120) NULL,
	[HSCHL_NM] [nvarchar](120) NULL,
	[UNIV_CONT] [nvarchar](240) NULL,
	[BHIST_CONT] [nvarchar](400) NULL,
	[CLNS_ID] [varchar](30) NULL,
	[NOTE_CONT] [nvarchar](400) NULL,
	[ATT_FILE_ID] [varchar](30) NULL,
	[BC_REG_TYP_CD] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[DEL_YN] [char](1) NULL,
	[EMAIL] [varchar](240) NULL,
	[BC_EN_NM] [nvarchar](30) NULL,
	[DFT_CNTC_TYP_CD] [varchar](30) NULL,
	[COMP_ID] [varchar](3) NULL,
	[ORIGINAL_BC_ID] [varchar](30) NULL,
	[MAIN_SETUP_YN] [char](1) NULL,
	[DUPLICATE_BC_ID] [varchar](30) NULL,
	[INITIAL_NM] [nvarchar](3) NULL,
 CONSTRAINT [PK_WB_BC_B] PRIMARY KEY CLUSTERED 
(
	[BC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공개구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'OPEN_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'직급명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'GRADE_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당업무내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'TICH_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주요인사여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'IPTFG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팩스번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'FNO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_상세주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_DETL_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택상세주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_DETL_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BUMK_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사진파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'PHOTO_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'성별코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'GEN_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'국적코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'NATY_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BIRTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생년월일월력코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BIRTH_SCLC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'결혼기념일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'WEDD_ANNV'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'결혼기념일월력코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'WEDD_ANNV_SCLC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'개인홈페이지URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'PSN_HPAGE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사홈페이지URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_HPAGE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'취미내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'HOBY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'특기사항내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'SPECT_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'초등학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'ESCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'중학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'MSCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'고등학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'HSCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대학교내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'UNIV_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'약력내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BHIST_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'친밀도내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'CLNS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'비고' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'NOTE_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'ATT_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함등록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_REG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'삭제여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'DEL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함영문성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_EN_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본연락처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'DFT_CNTC_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'원본명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'ORIGINAL_BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메인설정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'MAIN_SETUP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사본명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'DUPLICATE_BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'초성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B', @level2type=N'COLUMN',@level2name=N'INITIAL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_B'
GO
/****** Object:  Table [dbo].[WB_BC_APNTR_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_APNTR_R](
	[BC_ID] [varchar](30) NOT NULL,
	[BC_APNTR_UID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WB_BC_APNTR_R] PRIMARY KEY CLUSTERED 
(
	[BC_ID] ASC,
	[BC_APNTR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_APNTR_R', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함지정인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_APNTR_R', @level2type=N'COLUMN',@level2name=N'BC_APNTR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함지정인관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_APNTR_R'
GO
/****** Object:  Table [dbo].[WB_BC_AGNT_SETUP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_AGNT_SETUP_B](
	[REGR_UID] [varchar](30) NOT NULL,
	[BC_REGR_UID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_WB_BC_AGNT_SETUP_B] PRIMARY KEY CLUSTERED 
(
	[REGR_UID] ASC,
	[BC_REGR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_SETUP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_SETUP_B', @level2type=N'COLUMN',@level2name=N'BC_REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함대리설정기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_SETUP_B'
GO
/****** Object:  Table [dbo].[WB_BC_AGNT_ADM_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_BC_AGNT_ADM_B](
	[REGR_UID] [varchar](30) NOT NULL,
	[AGNT_ADM_UID] [varchar](30) NOT NULL,
	[AUTH_CD] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_WB_BC_AGNT_ADM_B] PRIMARY KEY CLUSTERED 
(
	[REGR_UID] ASC,
	[AGNT_ADM_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대리관리자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'AGNT_ADM_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'AUTH_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함대리관리자기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_BC_AGNT_ADM_B'
GO
/****** Object:  Table [dbo].[PT_USER_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_USER_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[SETUP_CLS_ID] [varchar](30) NOT NULL,
	[SETUP_ID] [varchar](100) NOT NULL,
	[SETUP_VA] [nvarchar](800) NULL,
	[CACHE_YN] [char](1) NULL,
 CONSTRAINT [PK_PT_USER_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[SETUP_CLS_ID] ASC,
	[SETUP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정분류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_CLS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'캐쉬여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D', @level2type=N'COLUMN',@level2name=N'CACHE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_USER_SETUP_D'
GO
/****** Object:  Table [dbo].[PT_TERM_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_TERM_SETUP_D](
	[SETUP_CLS_ID] [varchar](30) NOT NULL,
	[SETUP_ID] [varchar](100) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[TERM_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_PT_TERM_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[SETUP_CLS_ID] ASC,
	[SETUP_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정분류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_TERM_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_CLS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_TERM_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_TERM_SETUP_D', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'용어값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_TERM_SETUP_D', @level2type=N'COLUMN',@level2name=N'TERM_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'용어설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_TERM_SETUP_D'
GO
/****** Object:  Table [dbo].[PT_SYS_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_SYS_SETUP_D](
	[SETUP_CLS_ID] [varchar](30) NOT NULL,
	[SETUP_ID] [varchar](100) NOT NULL,
	[SETUP_VA] [nvarchar](800) NULL,
	[SORT_ORDR] [int] NULL,
 CONSTRAINT [PK_PT_SYS_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[SETUP_CLS_ID] ASC,
	[SETUP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정분류ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_SYS_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_CLS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_SYS_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_SYS_SETUP_D', @level2type=N'COLUMN',@level2name=N'SETUP_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_SYS_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_SYS_SETUP_D'
GO
/****** Object:  Table [dbo].[PT_RESC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_RESC_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_PT_RESC_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_D', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_D'
GO
/****** Object:  Table [dbo].[PT_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_PT_RESC_B] PRIMARY KEY NONCLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_RESC_B'
GO
/****** Object:  Table [dbo].[PT_PLT_SETUP_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_PLT_SETUP_R](
	[MNU_GRP_ID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[PLT_ZONE_CD] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NULL,
	[TOP_PX] [int] NULL,
	[LEFT_PX] [int] NULL,
	[WDTH_PX] [int] NULL,
	[HGHT_PX] [int] NULL,
	[ZIDX] [int] NULL,
 CONSTRAINT [PK_PT_PLT_SETUP_R] PRIMARY KEY CLUSTERED 
(
	[MNU_GRP_ID] ASC,
	[PLT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'MNU_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿지역코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'PLT_ZONE_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'TOP픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'TOP_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'LEFT픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'LEFT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'넓이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'WDTH_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'높이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'HGHT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'Z-INDEX' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R', @level2type=N'COLUMN',@level2name=N'ZIDX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_SETUP_R'
GO
/****** Object:  Table [dbo].[PT_PLT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_PLT_D](
	[PLT_ID] [varchar](30) NOT NULL,
	[PLT_CAT_CD] [varchar](30) NULL,
	[OPEN_COMP_ID] [varchar](3) NULL,
	[PLT_NM] [nvarchar](400) NULL,
	[RESC_ID] [varchar](30) NULL,
	[PLT_URL] [nvarchar](1000) NULL,
	[MORE_URL] [nvarchar](1000) NULL,
	[SETUP_URL] [nvarchar](1000) NULL,
	[WDTH_PX] [int] NULL,
	[HGHT_PX] [int] NULL,
	[PLT_DESC] [nvarchar](400) NULL,
	[PLT_TITL_YN] [char](1) NULL,
	[ITEM_TITL_YN] [char](1) NULL,
	[BORD_YN] [char](1) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_PLT_D] PRIMARY KEY CLUSTERED 
(
	[PLT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿카테고리코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_CAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공개회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'OPEN_COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'모어URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'MORE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설정URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'SETUP_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'넓이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'WDTH_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'높이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'HGHT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿타이틀여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'PLT_TITL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'항목타이틀여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'ITEM_TITL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테두리선여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'BORD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_PLT_D'
GO
/****** Object:  Table [dbo].[PT_MY_PLT_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MY_PLT_R](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[PLT_ZONE_CD] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NULL,
	[TOP_PX] [int] NULL,
	[LEFT_PX] [int] NULL,
	[WDTH_PX] [int] NULL,
	[HGHT_PX] [int] NULL,
	[ZIDX] [int] NULL,
 CONSTRAINT [PK_PT_MY_PLT_R] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿지역코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'PLT_ZONE_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'TOP픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'TOP_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'LEFT픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'LEFT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'넓이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'WDTH_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'높이픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'HGHT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'Z-INDEX' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R', @level2type=N'COLUMN',@level2name=N'ZIDX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'나의포틀릿관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_PLT_R'
GO
/****** Object:  Table [dbo].[PT_MY_MNU_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MY_MNU_D](
	[USER_UID] [varchar](30) NOT NULL,
	[MNU_LOUT_COMB_ID] [varchar](30) NOT NULL,
	[MNU_LOUT_COMB_PID] [varchar](30) NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[FLD_YN] [char](1) NULL,
	[MNU_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[MNU_ID] [varchar](30) NULL,
	[MNU_RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_MY_MNU_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[MNU_LOUT_COMB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃조합ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_COMB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃조합부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_COMB_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'FLD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'나의메뉴상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MY_MNU_D'
GO
/****** Object:  Table [dbo].[PT_MNU_LOUT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MNU_LOUT_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[MNU_LOUT_ID] [varchar](30) NOT NULL,
	[LOUT_CAT_ID] [char](1) NOT NULL,
	[MNU_LOUT_PID] [varchar](30) NULL,
	[LOUT_LOC_CD] [varchar](30) NULL,
	[MNU_LOUT_KND_CD] [varchar](30) NULL,
	[MNU_GRP_ID] [varchar](30) NULL,
	[MNU_GRP_RESC_ID] [varchar](30) NULL,
	[IMG_KND_VA] [varchar](100) NULL,
	[MNU_LOUT_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_MNU_LOUT_D] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[MNU_LOUT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'레이아웃유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'LOUT_CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'레이아웃위치코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'LOUT_LOC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'레이아웃종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_GRP_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지종류값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'IMG_KND_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_D'
GO
/****** Object:  Table [dbo].[PT_MNU_LOUT_COMB_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MNU_LOUT_COMB_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[MNU_LOUT_COMB_ID] [varchar](30) NOT NULL,
	[MNU_LOUT_ID] [varchar](30) NOT NULL,
	[LOUT_CAT_ID] [char](1) NOT NULL,
	[MNU_LOUT_COMB_PID] [varchar](30) NULL,
	[FLD_YN] [char](1) NULL,
	[FLD_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[MNU_ID] [varchar](30) NULL,
	[MNU_RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_MNU_LOUT_COMB_D] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[MNU_LOUT_COMB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃조합ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_COMB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'레이아웃유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'LOUT_CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃조합부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'MNU_LOUT_COMB_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'FLD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'FLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'MNU_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'MNU_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴레이아웃조합상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_LOUT_COMB_D'
GO
/****** Object:  Table [dbo].[PT_MNU_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MNU_GRP_B](
	[MNU_GRP_ID] [varchar](30) NOT NULL,
	[MNU_GRP_MD_CD] [varchar](30) NOT NULL,
	[MNU_GRP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[SYSG_YN] [char](1) NULL,
	[MNU_GRP_TYP_CD] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[MNU_URL] [nvarchar](1000) NULL,
	[POP_SETUP_CONT] [varchar](400) NULL,
	[PLT_LOUT_CD] [varchar](30) NULL,
	[OPEN_COMP_ID] [varchar](3) NULL,
	[MD_RID] [varchar](30) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_MNU_GRP_B] PRIMARY KEY CLUSTERED 
(
	[MNU_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MNU_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MNU_GRP_MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MNU_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템생성여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'SYSG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MNU_GRP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MNU_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'팝업설정내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'POP_SETUP_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿레이아웃코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'PLT_LOUT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공개회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'OPEN_COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'모듈참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MD_RID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_GRP_B'
GO
/****** Object:  Table [dbo].[PT_MNU_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_MNU_D](
	[MNU_ID] [varchar](30) NOT NULL,
	[MNU_GRP_MD_CD] [varchar](30) NOT NULL,
	[MNU_GRP_ID] [varchar](30) NOT NULL,
	[MNU_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[MNU_PID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[MNU_URL] [nvarchar](1000) NULL,
	[MNU_TYP_CD] [varchar](30) NULL,
	[POP_SETUP_CONT] [varchar](400) NULL,
	[MNU_DESC] [nvarchar](400) NULL,
	[FLD_YN] [char](1) NULL,
	[SYS_MNU_YN] [char](1) NULL,
	[MD_RID] [varchar](30) NULL,
	[MD_ID] [varchar](30) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_MNU_D] PRIMARY KEY CLUSTERED 
(
	[MNU_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹모듈코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_GRP_MD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'팝업설정내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'POP_SETUP_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MNU_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'FLD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템메뉴여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'SYS_MNU_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'모듈참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MD_RID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'모듈ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메뉴상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_MNU_D'
GO
/****** Object:  Table [dbo].[PT_LST_SETUP_META_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_LST_SETUP_META_D](
	[LST_SETUP_META_ID] [varchar](30) NOT NULL,
	[ATRB_ID] [varchar](30) NOT NULL,
	[MSG_ID] [varchar](30) NOT NULL,
	[SORT_OPT_VA] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_LST_SETUP_META_D] PRIMARY KEY CLUSTERED 
(
	[LST_SETUP_META_ID] ASC,
	[ATRB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록설정메타ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D', @level2type=N'COLUMN',@level2name=N'LST_SETUP_META_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메시지ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D', @level2type=N'COLUMN',@level2name=N'MSG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록설정메타상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_META_D'
GO
/****** Object:  Table [dbo].[PT_LST_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_LST_SETUP_D](
	[LST_SETUP_META_ID] [varchar](30) NOT NULL,
	[DFT_VA_YN] [char](1) NOT NULL,
	[ATRB_ID] [varchar](30) NOT NULL,
	[MSG_ID] [varchar](30) NOT NULL,
	[WDTH_PERC] [varchar](30) NULL,
	[ALN_VA] [varchar](30) NULL,
	[SORT_OPT_VA] [varchar](30) NULL,
	[DATA_SORT_VA] [varchar](30) NULL,
	[DISP_YN] [char](1) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_LST_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[LST_SETUP_META_ID] ASC,
	[DFT_VA_YN] ASC,
	[ATRB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록설정메타ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'LST_SETUP_META_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기본값여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'DFT_VA_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'메시지ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'MSG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LST_SETUP_D'
GO
/****** Object:  Table [dbo].[PT_LGIN_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_LGIN_IMG_D](
	[LGIN_IMG_ID] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NULL,
	[LGIN_IMG_NM] [nvarchar](120) NULL,
	[STRT_YMD] [datetime] NULL,
	[END_YMD] [datetime] NULL,
	[DFT_IMG_YN] [char](1) NOT NULL,
	[TOP_PX] [int] NULL,
	[LEFT_PX] [int] NULL,
	[USE_YN] [char](1) NOT NULL,
	[LOGO_IMG_PATH] [varchar](240) NULL,
	[BG_IMG_PATH] [varchar](240) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_LGIN_IMG_D] PRIMARY KEY CLUSTERED 
(
	[LGIN_IMG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'로그인이미지ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'LGIN_IMG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'로그인이미지명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'LGIN_IMG_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시작년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'STRT_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'종료년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'END_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기본이미지여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'DFT_IMG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'TOP픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'TOP_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'LEFT픽셀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'LEFT_PX'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'로고이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'LOGO_IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'배경이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'BG_IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'로그인이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_LGIN_IMG_D'
GO
/****** Object:  Table [dbo].[PT_COMP_LANG_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_COMP_LANG_R](
	[COMP_ID] [varchar](3) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
 CONSTRAINT [PK_PT_COMP_LANG_R] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_LANG_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_LANG_R', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사언어관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_LANG_R'
GO
/****** Object:  Table [dbo].[PT_COMP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_COMP_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[RCD] [varchar](30) NULL,
	[COMP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[USE_YN] [char](1) NULL,
	[DEL_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[MAIL_DOMAIN] [varchar](50) NOT NULL,
	[LIC_CNT] [int] NULL,
 CONSTRAINT [PK_PT_COMP_B] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'RCD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'삭제여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'DEL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메일도메인' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'MAIL_DOMAIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'라이선스수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_COMP_B', @level2type=N'COLUMN',@level2name=N'LIC_CNT'
GO
/****** Object:  Table [dbo].[PT_CD_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_CD_B](
	[CLS_CD] [varchar](30) NOT NULL,
	[CD] [varchar](30) NOT NULL,
	[CD_VA] [nvarchar](400) NULL,
	[RESC_ID] [varchar](30) NULL,
	[COMP_IDS] [varchar](120) NULL,
	[REF_VA1] [nvarchar](400) NULL,
	[REF_VA2] [nvarchar](400) NULL,
	[CD_DESC] [nvarchar](400) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[USE_YN] [char](1) NULL,
	[FLD_YN] [char](1) NULL,
	[SYS_CD_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_CD_B] PRIMARY KEY NONCLUSTERED 
(
	[CLS_CD] ASC,
	[CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'분류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'CLS_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'CD_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'COMP_IDS'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조값1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'REF_VA1'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조값2' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'REF_VA2'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'CD_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'FLD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템코드여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'SYS_CD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CD_B'
GO
/****** Object:  Table [dbo].[PT_CACHE_T]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_CACHE_T](
	[CACHE_CAT_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[CACHE_ID] [varchar](30) NOT NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_CACHE_T] PRIMARY KEY NONCLUSTERED 
(
	[CACHE_CAT_ID] ASC,
	[LANG_TYP_CD] ASC,
	[CACHE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'캐쉬유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CACHE_T', @level2type=N'COLUMN',@level2name=N'CACHE_CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CACHE_T', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'캐쉬ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CACHE_T', @level2type=N'COLUMN',@level2name=N'CACHE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CACHE_T', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'캐쉬임시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_CACHE_T'
GO
/****** Object:  Table [dbo].[PT_AUTH_GRP_USER_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_AUTH_GRP_USER_R](
	[COMP_ID] [varchar](3) NOT NULL,
	[AUTH_GRP_ID] [varchar](30) NOT NULL,
	[EXCLI_YN] [char](1) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[AUTH_GRP_TYP_CD] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_AUTH_GRP_USER_R] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[AUTH_GRP_ID] ASC,
	[EXCLI_YN] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제외대상여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'EXCLI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹사용자관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_USER_R'
GO
/****** Object:  Table [dbo].[PT_AUTH_GRP_MNU_PLT_R]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[PT_AUTH_GRP_MNU_PLT_R];
CREATE TABLE [dbo].[PT_AUTH_GRP_MNU_PLT_R](
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [AUTH_SCOP_CD]                [varchar]     (30) NOT NULL,
    [AUTH_GRP_ID]                 [varchar]     (30) NOT NULL,
    [MNU_PLT_ID]                  [varchar]     (30) NOT NULL,
    [AUTH_GRP_TYP_CD]             [varchar]     (30) NOT NULL,
    [MNU_GRP_ID]                  [varchar]     (30) NOT NULL,
    [MNU_PLT_TYP_CD]              [varchar]     (30) NOT NULL,
    [AUTH_CD]                     [varchar]     (30) NOT NULL,
    CONSTRAINT [PK_PT_AUTH_GRP_MNU_PLT_R] PRIMARY KEY CLUSTERED (
    [COMP_ID] ASC,
    [AUTH_SCOP_CD] ASC,
    [AUTH_GRP_ID] ASC,
    [MNU_PLT_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한그룹메뉴포틀릿관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한범위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'AUTH_SCOP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'AUTH_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메뉴포틀릿ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'MNU_PLT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한그룹구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'AUTH_GRP_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메뉴그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'MNU_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메뉴포틀릿구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'MNU_PLT_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_AUTH_GRP_MNU_PLT_R', @level2Type = N'Column', @Level2Name = 'AUTH_CD';
GO
/****** Object:  Table [dbo].[PT_AUTH_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_AUTH_GRP_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[AUTH_GRP_ID] [varchar](30) NOT NULL,
	[AUTH_GRP_TYP_CD] [varchar](30) NOT NULL,
	[AUTH_GRP_CAT_CD] [varchar](30) NOT NULL,
	[AUTH_GRP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[AUTH_GRP_DESC] [nvarchar](400) NULL,
	[SYS_AUTH_GRP_YN] [char](1) NULL,
	[USE_YN] [char](1) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_PT_AUTH_GRP_B] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[AUTH_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹카테고리코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_CAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템권한그룹여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'SYS_AUTH_GRP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_B'
GO
/****** Object:  Table [dbo].[PT_AUTH_GRP_AUTH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PT_AUTH_GRP_AUTH_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[AUTH_GRP_ID] [varchar](30) NOT NULL,
	[EXCLI_YN] [char](1) NOT NULL,
	[SEQ] [int] NOT NULL,
	[GRP_KND_CD] [varchar](30) NOT NULL,
	[GRP_ID] [varchar](30) NOT NULL,
	[AUTH_GRP_TYP_CD] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NULL,
	[SUB_INCL_YN] [char](1) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_PT_AUTH_GRP_AUTH_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[AUTH_GRP_ID] ASC,
	[EXCLI_YN] ASC,
	[SEQ] ASC,
	[GRP_KND_CD] ASC,
	[GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제외대상여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'EXCLI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'그룹종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'GRP_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_GRP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'하위포함여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'SUB_INCL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한그룹권한상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PT_AUTH_GRP_AUTH_D'
GO
/****** Object:  Table [dbo].[OR_USER_ROLE_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_ROLE_R](
	[USER_UID] [varchar](30) NOT NULL,
	[ROLE_CD] [varchar](30) NOT NULL,
 CONSTRAINT [PK_OR_USER_ROLE_R] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[ROLE_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_ROLE_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'역할코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_ROLE_R', @level2type=N'COLUMN',@level2name=N'ROLE_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자역할관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_ROLE_R'
GO
/****** Object:  Table [dbo].[OR_USER_PW_H]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_PW_H](
	[ODUR_UID] [varchar](30) NOT NULL,
	[PW_TYP_CD] [varchar](30) NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[PW_ENC] [varchar](400) NULL,
 CONSTRAINT [PK_OR_USER_PW_H] PRIMARY KEY NONCLUSTERED 
(
	[ODUR_UID] ASC,
	[PW_TYP_CD] ASC,
	[MOD_DT] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'원직자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H', @level2type=N'COLUMN',@level2name=N'ODUR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'비밀번호구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H', @level2type=N'COLUMN',@level2name=N'PW_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'비밀번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H', @level2type=N'COLUMN',@level2name=N'PW_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자비밀번호이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_H'
GO
/****** Object:  Table [dbo].[OR_USER_PW_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_PW_D](
	[ODUR_UID] [varchar](30) NOT NULL,
	[PW_TYP_CD] [varchar](30) NOT NULL,
	[MOD_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[PW_ENC] [varchar](400) NULL,
 CONSTRAINT [PK_OR_USER_PW_D] PRIMARY KEY NONCLUSTERED 
(
	[ODUR_UID] ASC,
	[PW_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'원직자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D', @level2type=N'COLUMN',@level2name=N'ODUR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'비밀번호구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D', @level2type=N'COLUMN',@level2name=N'PW_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'비밀번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D', @level2type=N'COLUMN',@level2name=N'PW_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자비밀번호상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PW_D'
GO
/****** Object:  Table [dbo].[OR_USER_PINFO_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_PINFO_D](
	[ODUR_UID] [varchar](30) NOT NULL,
	[MBNO_ENC] [varchar](120) NULL,
	[EMAIL_ENC] [varchar](400) NULL,
	[EXTN_EMAIL_ENC] [varchar](400) NULL,
	[HOME_PHON_ENC] [varchar](120) NULL,
	[HOME_FNO_ENC] [varchar](120) NULL,
	[HOME_ZIP_NO_ENC] [varchar](80) NULL,
	[HOME_ADR_ENC] [varchar](400) NULL,
	[HOME_DETL_ADR_ENC] [varchar](400) NULL,
	[COMP_PHON_ENC] [varchar](120) NULL,
	[COMP_FNO_ENC] [varchar](120) NULL,
	[COMP_ZIP_NO_ENC] [varchar](80) NULL,
	[COMP_ADR_ENC] [varchar](400) NULL,
	[COMP_DETL_ADR_ENC] [varchar](400) NULL,
	[SSN_ENC] [varchar](40) NULL,
	[HPAGE_URL_ENC] [varchar](400) NULL,
 CONSTRAINT [PK_OR_USER_PINFO_D] PRIMARY KEY NONCLUSTERED 
(
	[ODUR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'원직자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'ODUR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'휴대전화번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'MBNO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이메일암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'EMAIL_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'외부이메일암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'EXTN_EMAIL_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자택전화번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HOME_PHON_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자택팩스번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HOME_FNO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자택우편번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HOME_ZIP_NO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자택주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HOME_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자택상세주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HOME_DETL_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사전화번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'COMP_PHON_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사팩스번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'COMP_FNO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사우편번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'COMP_ZIP_NO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'COMP_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사상세주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'COMP_DETL_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'주민등록번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'SSN_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'홈페이지URL암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D', @level2type=N'COLUMN',@level2name=N'HPAGE_URL_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자개인정보상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_PINFO_D'
GO
/****** Object:  Table [dbo].[OR_USER_LGIN_SETUP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_LGIN_SETUP_B](
	[COMP_ID] [varchar](30) NOT NULL,
	[REPET_TYP_CD] [varchar](10) NULL,
	[STRT_YMD] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[NEXT_REPET_YMD] [datetime] NULL,
	[USE_YN] [char](1) NULL,
	[DEL_YN] [char](1) NULL,
 CONSTRAINT [PK_OR_USER_LGIN_SETUP_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'REPET_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'STRT_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'다음반복연월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'NEXT_REPET_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'삭제여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B', @level2type=N'COLUMN',@level2name=N'DEL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자로그인이력설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_SETUP_B'
GO
/****** Object:  Table [dbo].[OR_USER_LGIN_H]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_LGIN_H](
	[USER_UID] [varchar](30) NOT NULL,
	[SESSION_ID] [varchar](120) NOT NULL,
	[LGIN_DT] [datetime] NOT NULL,
	[LGOT_DT] [datetime] NULL,
	[ACCS_IP] [varchar](30) NULL,
	[COMP_ID] [varchar](30) NULL,
	[DEVI_NM] [varchar](10) NULL,
 CONSTRAINT [PK_OR_USER_LGIN_H] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[SESSION_ID] ASC,
	[LGIN_DT] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'세션ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'SESSION_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'로그인일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'LGIN_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'로그아웃일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'LGOT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'접속IP' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'ACCS_IP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'장치명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H', @level2type=N'COLUMN',@level2name=N'DEVI_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자로그인이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_LGIN_H'
GO
/****** Object:  Table [dbo].[OR_USER_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_USER_IMG_D](
	[USER_UID] [varchar](30) NOT NULL,
	[USER_IMG_TYP_CD] [varchar](30) NOT NULL,
	[IMG_PATH] [varchar](240) NOT NULL,
	[IMG_WDTH] [int] NULL,
	[IMG_HGHT] [int] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_OR_USER_IMG_D] PRIMARY KEY NONCLUSTERED 
(
	[USER_UID] ASC,
	[USER_IMG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자이미지구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'USER_IMG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_USER_IMG_D'
GO

--DROP TABLE [dbo].[OR_USER_B];
CREATE TABLE [dbo].[OR_USER_B](
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [ORG_ID]                      [varchar]     (30) NOT NULL,
    [ORG_RESC_ID]                 [varchar]     (30) NOT NULL,
    [DEPT_ID]                     [varchar]     (30) NOT NULL,
    [DEPT_RESC_ID]                [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [SECUL_CD]                    [varchar]     (30) NULL,
    [GRADE_CD]                    [varchar]     (30) NULL,
    [TITLE_CD]                    [varchar]     (30) NULL,
    [POSIT_CD]                    [varchar]     (30) NULL,
    [DUTY_CD]                     [varchar]     (30) NULL,
    [USER_NM]                     [nvarchar]   (120) NULL,
    [RESC_ID]                     [varchar]     (30) NOT NULL,
    [ADU_TYP_CD]                  [varchar]     (30) NOT NULL,
    [STRT_YMD]                    [datetime]         NULL,
    [END_YMD]                     [datetime]         NULL,
    [USER_STAT_CD]                [varchar]     (30) NOT NULL,
    [END_RSON]                    [nvarchar]   (800) NULL,
    [TICH_CONT]                   [nvarchar]   (800) NULL,
    [SIGN_MTHD_CD]                [varchar]     (30) NULL,
    [RID]                         [varchar]     (30) NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [MODR_UID]                    [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    [SYNC_STEP_CD]                [varchar]     (30) NULL,
    [REF_VA1]                     [nvarchar]   (400) NULL,
    [REF_VA2]                     [nvarchar]   (400) NULL,
    [REF_VA3]                     [nvarchar]   (400) NULL,
    [SYS_USER_YN]                 [char]         (1) NULL,
    [AUTO_APV_LN_CD]              [varchar]     (30) NULL,
    CONSTRAINT [PK_OR_USER_B] PRIMARY KEY NONCLUSTERED (
    [USER_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'ORG_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부서리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'DEPT_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보안등급코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'SECUL_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직급코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'GRADE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직무코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'DUTY_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'USER_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'겸직구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'ADU_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'USER_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료사유', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'END_RSON';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당업무내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'TICH_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명방법코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'SIGN_MTHD_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'동기화단계코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'SYNC_STEP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값1', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'REF_VA1';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값2', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'REF_VA2';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값3', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'REF_VA3';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시스템사용자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'SYS_USER_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'자동결재선코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_B', @level2Type = N'Column', @Level2Name = 'AUTO_APV_LN_CD';
GO
/****** Object:  Table [dbo].[OR_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_OR_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_RESC_B'
GO
/****** Object:  Table [dbo].[OR_ORG_CNTC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_ORG_CNTC_D](
	[ORG_ID] [varchar](30) NOT NULL,
	[REP_EMAIL] [varchar](240) NULL,
	[PHON] [varchar](50) NULL,
	[FNO] [varchar](50) NULL,
	[ZIP_NO] [varchar](10) NULL,
	[ADR] [nvarchar](240) NULL,
	[DETL_ADR] [nvarchar](240) NULL,
	[REP_HPAGE_URL] [nvarchar](1000) NULL,
 CONSTRAINT [PK_OR_ORG_CNTC_D] PRIMARY KEY CLUSTERED 
(
	[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대표이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'REP_EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'전화번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'PHON'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'팩스번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'FNO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'상세주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'DETL_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대표홈페이지URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D', @level2type=N'COLUMN',@level2name=N'REP_HPAGE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직연락처상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_CNTC_D'
GO
/****** Object:  Table [dbo].[OR_ORG_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_ORG_B](
	[ORG_ID] [varchar](30) NOT NULL,
	[ORG_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[ORG_PID] [varchar](30) NOT NULL,
	[DEPT_ID] [varchar](30) NOT NULL,
	[DEPT_RESC_ID] [varchar](30) NOT NULL,
	[RID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[ORG_TYP_CD] [varchar](30) NULL,
	[SYNC_STEP_CD] [varchar](30) NULL,
	[USE_YN] [char](1) NOT NULL,
	[SYS_ORG_YN] [char](1) NOT NULL,
	[ORG_ABBR_RESC_ID] [varchar](30) NULL,
	[REF_VA1] [nvarchar](400) NULL,
	[REF_VA2] [nvarchar](400) NULL,
	[REF_VA3] [nvarchar](400) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_OR_ORG_B] PRIMARY KEY NONCLUSTERED 
(
	[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'ORG_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'ORG_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'DEPT_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'RID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'ORG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'동기화단계코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'SYNC_STEP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템조직여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'SYS_ORG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직약어리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'ORG_ABBR_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조값1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'REF_VA1'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조값2' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'REF_VA2'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조값3' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'REF_VA3'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_B'
GO
/****** Object:  Table [dbo].[OR_ORG_APV_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_ORG_APV_D](
	[ORG_ID] [varchar](30) NOT NULL,
	[CRD_ORG_ID] [varchar](30) NULL,
	[CRD_RESC_ID] [varchar](30) NULL,
	[AGN_CRD_ORG_ID] [varchar](30) NULL,
	[AGN_CRD_RESC_ID] [varchar](30) NULL,
	[REAL_CRD_ORG_ID] [varchar](30) NULL,
	[HODP_POSIT_RESC_ID] [varchar](30) NULL,
	[SENDR_NM_RESC_ID] [varchar](50) NULL,
	[RECV_DEPT_ID] [varchar](30) NULL,
	[INSP_YN] [char](1) NOT NULL,
	[OFSE_ID] [varchar](30) NULL,
 CONSTRAINT [PK_OR_ORG_APV_D] PRIMARY KEY NONCLUSTERED 
(
	[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'문서과조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'CRD_ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'문서과리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'CRD_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리문서과조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'AGN_CRD_ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리문서과리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'AGN_CRD_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'실제문서과조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'REAL_CRD_ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서장직위리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'HODP_POSIT_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'발신명의리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'SENDR_NM_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'감사과여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'INSP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관인ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D', @level2type=N'COLUMN',@level2name=N'OFSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직결재상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_ORG_APV_D'
GO
/****** Object:  Table [dbo].[OR_OFSE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_OFSE_D](
	[ORG_ID] [varchar](30) NOT NULL,
	[SEQ] [int] NOT NULL,
	[OFSE_TYP_CD] [varchar](30) NULL,
	[OFSE_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[DFT_OFSE_YN] [char](1) NULL,
	[DISU_YN] [char](1) NOT NULL,
	[IMG_PATH] [varchar](240) NOT NULL,
	[IMG_WDTH] [int] NOT NULL,
	[IMG_HGHT] [int] NOT NULL,
	[CHN_RSON] [nvarchar](800) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[DISUR_UID] [varchar](30) NULL,
	[DISU_DT] [datetime] NULL,
 CONSTRAINT [PK_OR_OFSE_D] PRIMARY KEY NONCLUSTERED 
(
	[ORG_ID] ASC,
	[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관인구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'OFSE_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관인명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'OFSE_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기본관인여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'DFT_OFSE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폐기여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'DISU_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'변경사유' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'CHN_RSON'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폐기자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'DISUR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폐기일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D', @level2type=N'COLUMN',@level2name=N'DISU_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관인상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'OR_OFSE_D'
GO
/****** Object:  Table [dbo].[OR_ODUR_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OR_ODUR_B](
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [LGIN_ID]                     [varchar]     (40) NOT NULL,
    [USER_NM]                     [nvarchar]   (120) NULL,
    [RESC_ID]                     [varchar]     (30) NOT NULL,
    [RID]                         [varchar]     (30) NULL,
    [EIN]                         [varchar]     (20) NULL,
    [USER_STAT_CD]                [varchar]     (30) NOT NULL,
    [USER_DESC]                   [nvarchar]  (1000) NULL,
    [ENTRA_YMD]                   [datetime]         NULL,
    [RESIG_YMD]                   [datetime]         NULL,
    [GEN_CD]                      [varchar]     (30) NOT NULL,
    [APV_PW_TYP_CD]               [varchar]     (30) NOT NULL,
    [SYNC_STEP_CD]                [varchar]     (30) NULL,
    [REF_VA1]                     [nvarchar]   (400) NULL,
    [REF_VA2]                     [nvarchar]   (400) NULL,
    [REF_VA3]                     [nvarchar]   (400) NULL,
    [SYS_USER_YN]                 [char]         (1) NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [MODR_UID]                    [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_OR_ODUR_B] PRIMARY KEY NONCLUSTERED (
    [ODUR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'로그인ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'LGIN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'USER_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사원번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'EIN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'USER_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자설명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'USER_DESC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'입사년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'ENTRA_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'퇴사년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'RESIG_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'성별코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'GEN_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재비밀번호구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'APV_PW_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'동기화단계코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'SYNC_STEP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값1', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'REF_VA1';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값2', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'REF_VA2';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값3', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'REF_VA3';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시스템사용자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'SYS_USER_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
GO

/****** Object:  Table [dbo].[CT_VISTR_HST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_VISTR_HST_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[ACCS_DT] [datetime] NOT NULL,
	[ACCS_TYP_CD] [varchar](30) NULL,
 CONSTRAINT [PK_CT_VISTR_HST_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC,
	[USER_UID] ASC,
	[ACCS_DT] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'접속 일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D', @level2type=N'COLUMN',@level2name=N'ACCS_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'접속 구분 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D', @level2type=N'COLUMN',@level2name=N'ACCS_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 방문자 이력 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_VISTR_HST_D'
GO
/****** Object:  Table [dbo].[CT_SURV_REPLY_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SURV_REPLY_D](
	[REPLY_NO] [int] NOT NULL,
	[QUES_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[REPLYR_UID] [varchar](30) NOT NULL,
	[REPLYR_DEPT_ID] [varchar](30) NULL,
	[REPLY_DT] [datetime] NULL,
	[MULC_INPUT_REPLY_CONT] [nvarchar](256) NULL,
	[OEND_REPLY_CONT] [nvarchar](1000) NULL,
 CONSTRAINT [PK_CT_SURV_REPLY_D] PRIMARY KEY CLUSTERED 
(
	[REPLY_NO] ASC,
	[REPLYR_UID] ASC,
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLY_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변자ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLYR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변자 부서 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLYR_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'REPLY_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'객관식입력항목답변내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'MULC_INPUT_REPLY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주관식답변내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D', @level2type=N'COLUMN',@level2name=N'OEND_REPLY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'답변상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_REPLY_D'
GO
/****** Object:  Table [dbo].[CT_SURV_QUES_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SURV_QUES_D](
	[QUES_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[QUES_SORT_ORDR] [int] NULL,
	[QUES_CONT] [nvarchar](2000) NULL,
	[QUES_IMG_ATT_YN] [char](1) NULL,
	[IMG_SURV_FILE_ID] [varchar](30) NULL,
	[EXAM_CHOI_ITEM_CD] [varchar](30) NULL,
	[MUL_CHOI_YN] [char](1) NULL,
	[MANDA_REPLY_YN] [char](1) NULL,
 CONSTRAINT [PK_CT_SURV_QUES_D] PRIMARY KEY CLUSTERED 
(
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문이미지첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'QUES_IMG_ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'IMG_SURV_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보기선택항목코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'EXAM_CHOI_ITEM_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'복수선택여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'MUL_CHOI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'필수답변여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D', @level2type=N'COLUMN',@level2name=N'MANDA_REPLY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'질문상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_QUES_D'
GO
/****** Object:  Table [dbo].[CT_SURV_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SURV_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[QUES_CNT] [int] NULL,
	[SURV_START_DT] [datetime] NULL,
	[SURV_END_DT] [datetime] NULL,
	[PLT_ID] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NOT NULL,
	[SURV_PRG_STAT_CD] [varchar](1) NULL,
	[OPEN_YN] [char](1) NULL,
	[REPET_SURV_YN] [char](1) NULL,
	[SURV_ITNT] [ntext] NULL,
	[SURV_FTR] [ntext] NULL,
	[ATT_YN] [char](1) NULL,
	[ADM_RJT_OPIN_CONT] [nvarchar](2000) NULL,
	[ADM_FILE_ID] [varchar](30) NULL,
	[CT_FNC_ID] [varchar](30) NULL,
	[CT_FNC_ORDR] [int] NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NOT NULL,
	[CT_FNC_PID] [varchar](30) NULL,
	[SURV_TGT_CD] [varchar](30) NULL,
 CONSTRAINT [PK_CT_SURV_B] PRIMARY KEY NONCLUSTERED 
(
	[COMP_ID] ASC,
	[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문 개수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'QUES_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문 시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문 종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문진행상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_PRG_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공개여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'OPEN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'재설문여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'REPET_SURV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문취지' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_ITNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문바닥글' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_FTR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관리자반려의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'ADM_RJT_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'ADM_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티위치단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티PID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문 대상 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B', @level2type=N'COLUMN',@level2name=N'SURV_TGT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SURV_B'
GO
/****** Object:  Table [dbo].[CT_SITE_CAT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SITE_CAT_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CAT_ID] [varchar](30) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CAT_NM_RESC_ID] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_SITE_CAT_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CAT_ID] ASC,
	[CT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 회사ID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 카테고리 ID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 카테고리명 리소스 ID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_NM_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 등록일시 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 수정일시 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 등록자UID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 수정자UID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 사이트 카테고리 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_CAT_D'
GO
/****** Object:  Table [dbo].[CT_SITE_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SITE_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[SITE_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NOT NULL,
	[CT_FNC_ORDR] [int] NULL,
	[SUBJ] [nvarchar](240) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[CAT_ID] [varchar](30) NULL,
	[URL] [nvarchar](1000) NULL,
	[CONT] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[EDITOR_TYP_CD] [varchar](30) NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NULL,
	[CT_FNC_PID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_SITE_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC,
	[SITE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 회사ID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 제목 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 등록일시 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 수정일시 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 등록자UID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 수정자UID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'COOL SITE ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'SITE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 카테고리 ID ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' URL ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 본문 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 조회수 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N' 에디터 타입 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'EDITOR_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 사이트 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SITE_B'
GO
/****** Object:  Table [dbo].[CT_SECUL_CD_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SECUL_CD_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_SECUL_CD] [varchar](30) NOT NULL,
	[USER_SECUL_CD_SUBJ_RESC_ID] [varchar](50) NOT NULL,
 CONSTRAINT [PK_CT_SECUL_CD_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[USER_SECUL_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SECUL_CD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 보안등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SECUL_CD_B', @level2type=N'COLUMN',@level2name=N'USER_SECUL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 보안등급코드 제목 리소스 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SECUL_CD_B', @level2type=N'COLUMN',@level2name=N'USER_SECUL_CD_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 회원 등급 코드 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SECUL_CD_B'
GO
/****** Object:  Table [dbo].[CT_SCRN_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCRN_SETUP_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[TPL_FILE_SUBJ] [varchar](255) NOT NULL,
	[CT_ITRO] NTEXT NULL,
	[LOC_CNT] [int] NULL,
	[LOC_1_CT_FNC_ID] [varchar](30) NULL,
	[LOC_2_CT_FNC_ID] [varchar](30) NULL,
	[LOC_3_CT_FNC_ID] [varchar](30) NULL,
	[LOC_4_CT_FNC_ID] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NOT NULL,
	[EDITOR_TYP_CD] [varchar](30) NULL,
	[LOC_5_CT_FNC_ID] [varchar](30) NULL,
	[LOC_6_CT_FNC_ID] [varchar](30) NULL,
	[LOC_7_CT_FNC_ID] [varchar](30) NULL,
	[LOC_8_CT_FNC_ID] [varchar](30) NULL,
	[LOC_9_CT_FNC_ID] [varchar](30) NULL,
	[LOC_10_CT_FNC_ID] [varchar](30) NULL,
	[LOC_11_CT_FNC_ID] [varchar](30) NULL,
	[LOC_12_CT_FNC_ID] [varchar](30) NULL,
	[IMG_FILE_ID] [varchar](30) NULL,
	[BGCOL] [varchar](50) NULL,
 CONSTRAINT [PK_CT_SCRN_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'템플릿 파일 제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'TPL_FILE_SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 소개' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'CT_ITRO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치1 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_1_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치2 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_2_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치3 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_3_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치4 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_4_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'에디터구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'EDITOR_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치5 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_5_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치6 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_6_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치7 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_7_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치8 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_8_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치9 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_9_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치10 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_10_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치11 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_11_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'위치12 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'LOC_12_CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지 파일 아이디' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'IMG_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배경색' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D', @level2type=N'COLUMN',@level2name=N'BGCOL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메인설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRN_SETUP_D'
GO
/****** Object:  Table [dbo].[CT_SCRE_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCRE_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[SCRE] [int] NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_CT_SCRE_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'점수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'SCRE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'점수주기이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCRE_HST_L'
GO
/****** Object:  Table [dbo].[CT_SCHDL_REPET_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCHDL_REPET_SETUP_D](
	[SCHDL_ID] [varchar](30) NOT NULL,
	[REPET_PERD_CD] [varchar](30) NOT NULL,
	[APNT_MM] [varchar](4) NULL,
	[REPET_MM] [varchar](4) NULL,
	[APNT_WK] [varchar](4) NULL,
	[REPET_WK] [varchar](4) NULL,
	[APNT_DD] [varchar](4) NULL,
	[REPET_DD] [varchar](4) NULL,
	[APNT_DY] [varchar](30) NULL,
	[REPET_START_DT] [datetime] NULL,
	[REPET_END_DT] [datetime] NULL,
 CONSTRAINT [PK_CT_SCHDL_REPET_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복주기코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_PERD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정월' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_MM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복월' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_MM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정주' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_WK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복주' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_WK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정요일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_DY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_DD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지정일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'APNT_DD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D', @level2type=N'COLUMN',@level2name=N'REPET_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복설정 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_REPET_SETUP_D'
GO
/****** Object:  Table [dbo].[CT_SCHDL_PROM_GUEST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCHDL_PROM_GUEST_D](
	[GUEST_UID] [varchar](30) NOT NULL,
	[GUEST_NM] [nvarchar](20) NOT NULL,
	[GUEST_POSIT_NM] [nvarchar](100) NULL,
	[GUEST_DEPT_NM] [nvarchar](100) NULL,
	[GUEST_COMP_NM] [nvarchar](100) NULL,
	[STAT_CD] [varchar](30) NOT NULL,
	[OPIN_CONT] [nvarchar](255) NULL,
	[GUEST_EMPL_YN] [varchar](30) NULL,
	[EMAIL] [varchar](240) NULL,
	[EMAIL_SEND_YN] [char](1) NULL,
	[SCHDL_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_CT_SCHDL_PROM_GUEST_D] PRIMARY KEY CLUSTERED 
(
	[GUEST_UID] ASC,
	[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자 이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자 직위명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_POSIT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자 부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자 회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참석자_임직원 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'GUEST_EMPL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일발송여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'EMAIL_SEND_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'약속 참석자 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_PROM_GUEST_D'
GO
/****** Object:  Table [dbo].[CT_SCHDL_CAT_CLS_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCHDL_CAT_CLS_B](
	[CAT_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NULL,
	[RESC_ID] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[BGCOL_CD] [varchar](10) NULL,
	[FONT_COLR_CD] [varchar](10) NULL,
	[USE_YN] [char](1) NULL,
	[CAT_NM] [nvarchar](120) NULL,
 CONSTRAINT [PK_CT_SCHDL_CAT_CLS_B] PRIMARY KEY CLUSTERED 
(
	[CAT_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'배경색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'BGCOL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'글자색코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'FONT_COLR_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리분류기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_CAT_CLS_B'
GO
/****** Object:  Table [dbo].[CT_SCHDL_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_SCHDL_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[SCHDL_TYP_CD] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NOT NULL,
	[LOC_NM] [nvarchar](60) NULL,
	[OPEN_GRAD_CD] [varchar](30) NULL,
	[SCHDL_STAT_CD] [varchar](30) NULL,
	[WORK_PRIO_ORDR] [varchar](1) NULL,
	[WORK_CMLT_YMD] [datetime] NULL,
	[CONT] [ntext] NULL,
	[ATT_YN] [char](1) NULL,
	[SELF_REG_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[EDITOR_TYP_CD] [varchar](30) NULL,
	[SCHDL_ID] [varchar](30) NOT NULL,
	[MOD_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[SCHDL_FILE_ID] [varchar](30) NULL,
	[SCHDL_START_DT] [datetime] NOT NULL,
	[SCHDL_END_DT] [datetime] NOT NULL,
	[REPET_YN] [char](1) NULL,
	[SCHDL_PID] [varchar](30) NULL,
	[SCHDL_KND_CD] [varchar](30) NULL,
	[GRP_ID] [varchar](30) NULL,
	[DEPT_ID] [varchar](30) NULL,
	[HOLI_YN] [char](1) NOT NULL,
	[SOLA_LUNA_YN] [char](1) NOT NULL,
	[LOC_LATD_LOTD_VA] [varchar](20) NULL,
	[REGR_NM] [nvarchar](120) NULL,
	[ALLDAY_YN] [char](1) NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NULL,
	[CT_FNC_ORDR] [int] NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NOT NULL,
	[CT_FNC_PID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_SCHDL_B] PRIMARY KEY CLUSTERED 
(
	[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'장소명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'LOC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공개등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'OPEN_GRAD_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'할일우선순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'WORK_PRIO_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'할일완료년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'WORK_CMLT_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'본인입력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SELF_REG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'에디터구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'EDITOR_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정첨부파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반복여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REPET_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'휴일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'HOLI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'양음력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SOLA_LUNA_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'장소위도경도값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'LOC_LATD_LOTD_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REGR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'ALLDAY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_SCHDL_B'
GO
/****** Object:  Table [dbo].[CT_RESC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_RESC_D](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_CT_RESC_D] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_D', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_D'
GO
/****** Object:  Table [dbo].[CT_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_CT_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RESC_B'
GO
/****** Object:  Table [dbo].[CT_RECMD_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_RECMD_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_CT_RECMD_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'추천이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_RECMD_HST_L'
GO
/****** Object:  Table [dbo].[CT_REC_MAST_CMD_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_REC_MAST_CMD_D](
	[CMT_ID] [bigint] NOT NULL,
	[BULL_ID] [int] NULL,
	[CMT] [nvarchar](1000) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_CT_REC_MAST_CMD_D] PRIMARY KEY CLUSTERED 
(
	[CMT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'CMT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'CMT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판 마스터 한줄 답변 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_CMD_D'
GO
/****** Object:  Table [dbo].[CT_REC_MAST_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_REC_MAST_B](
	[BULL_ID] [int] NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[BULL_PID] [int] NULL,
	[REPLY_GRP_ID] [int] NULL,
	[REPLY_ORDR] [int] NULL,
	[REPLY_DPTH] [int] NULL,
	[BULL_STAT_CD] [char](1) NULL,
	[BULL_REZV_YN] [char](1) NULL,
	[TGT_DEPT_YN] [char](1) NULL,
	[TGT_USER_YN] [char](1) NULL,
	[SUBJ] [nvarchar](240) NULL,
	[BULL_REZV_DT] [datetime] NULL,
	[BULL_EXPR_DT] [datetime] NULL,
	[CONT] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[PROS_CNT] [int] NULL,
	[CONS_CNT] [int] NULL,
	[RECMD_CNT] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[ATT_YN] [char](1) NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NULL,
	[CT_FNC_ORDR] [int] NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NULL,
	[CT_FNC_PID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_REC_MAST_B] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부모게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_DPTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물예약여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상부서지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'TGT_DEPT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상사용자지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'TGT_USER_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시예약일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_EXPR_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬성수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'PROS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반대수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CONS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'RECMD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 게시판 마스터 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_REC_MAST_B'
GO
/****** Object:  Table [dbo].[CT_QUES_EXAM_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_QUES_EXAM_D](
	[EXAM_NO] [int] NOT NULL,
	[QUES_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[SURV_ID] [varchar](30) NOT NULL,
	[EXAM_ORDR] [int] NULL,
	[EXAM_DISP_NM] [nvarchar](256) NULL,
	[INPUT_YN] [char](1) NULL,
	[EXAM_IMG_USE_YN] [char](1) NULL,
	[IMG_SURV_FILE_ID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_QUES_EXAM_D] PRIMARY KEY CLUSTERED 
(
	[QUES_ID] ASC,
	[COMP_ID] ASC,
	[SURV_ID] ASC,
	[EXAM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'QUES_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'입력 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'INPUT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보기이미지사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'EXAM_IMG_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지설문파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D', @level2type=N'COLUMN',@level2name=N'IMG_SURV_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'질문보기상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_QUES_EXAM_D'
GO
/****** Object:  Table [dbo].[CT_PR_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_PR_B](
	[BULL_ID] [int] NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[BULL_PID] [int] NULL,
	[REPLY_GRP_ID] [int] NULL,
	[REPLY_ORDR] [int] NULL,
	[REPLY_DPTH] [int] NULL,
	[BULL_STAT_CD] [char](1) NULL,
	[BULL_REZV_YN] [char](1) NULL,
	[TGT_DEPT_YN] [char](1) NULL,
	[TGT_USER_YN] [char](1) NULL,
	[SUBJ] [nvarchar](240) NULL,
	[BULL_REZV_DT] [datetime] NULL,
	[BULL_EXPR_DT] [datetime] NULL,
	[CONT] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[PROS_CNT] [int] NULL,
	[CONS_CNT] [int] NULL,
	[RECMD_CNT] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[ATT_YN] [char](1) NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_SUBJ_RESC_ID] [varchar](50) NULL,
	[CT_ITRO] [nvarchar](500) NULL,
 CONSTRAINT [PK_CT_PR_B] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부모게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'REPLY_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'REPLY_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'REPLY_DPTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물예약여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상부서지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'TGT_DEPT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상사용자지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'TGT_USER_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시예약일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'BULL_EXPR_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬성수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'PROS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반대수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'CONS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'RECMD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티제목리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'CT_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티소개' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B', @level2type=N'COLUMN',@level2name=N'CT_ITRO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 홍보마당 게시물 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PR_B'
GO
/****** Object:  Table [dbo].[CT_POLC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_POLC_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NOT NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NOT NULL,
	[APVD_YN] [char](1) NULL,
 CONSTRAINT [PK_CT_POLC_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'승인여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D', @level2type=N'COLUMN',@level2name=N'APVD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 정책 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_POLC_D'
GO
/****** Object:  Table [dbo].[CT_PLT_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_PLT_SETUP_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_FNC_ID] [varchar](30) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[PERD] [int] NULL,
	[CT_FUNC_SUBJ_RESC_ID] [varchar](30) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PERD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'CT_FUNC_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'환경설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_SETUP_D'
GO
/****** Object:  Table [dbo].[CT_PLT_NOPR_LIST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_PLT_NOPR_LIST_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_CT_PLT_NOPR_LIST_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_NOPR_LIST_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_NOPR_LIST_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_NOPR_LIST_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_NOPR_LIST_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_NOPR_LIST_D'
GO
/****** Object:  Table [dbo].[CT_PLT_FNC_LIST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_PLT_FNC_LIST_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_CT_PLT_FNC_LIST_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_FNC_LIST_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_FNC_LIST_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_FNC_LIST_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_FNC_LIST_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_FNC_LIST_D'
GO
/****** Object:  Table [dbo].[CT_PLT_CM_LIST_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_PLT_CM_LIST_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_CT_PLT_CM_LIST_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_CM_LIST_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_CM_LIST_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_CM_LIST_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_CM_LIST_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_PLT_CM_LIST_D'
GO
/****** Object:  Table [dbo].[CT_MBSH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_MBSH_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[USER_SECUL_CD] [varchar](30) NULL,
	[JOIN_DT] [datetime] NULL,
	[JOIN_STAT] [varchar](1) NULL,
 CONSTRAINT [PK_CT_MBSH_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자 보안등급 코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'USER_SECUL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'가입일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'JOIN_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'가입상태' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D', @level2type=N'COLUMN',@level2name=N'JOIN_STAT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 회원 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_MBSH_D'
GO
/****** Object:  Table [dbo].[CT_FNC_MNG_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_FNC_MNG_B](
	[CT_FNC_ID] [varchar](30) NOT NULL,
	[CT_FNC_SUBJ_RESC_ID] [varchar](30) NULL,
	[CT_FNC_TYP] [varchar](1) NULL,
	[URL] [nvarchar](1000) NULL,
	[REL_TBL_SUBJ] [nvarchar](200) NULL,
	[DFT_YN] [char](1) NULL,
	[MUL_CHOI_YN] [char](1) NULL,
	[USE_YN] [char](1) NULL,
	[CT_MNG_YN] [char](1) NULL,
	[PT_URL] [nvarchar](1000) NULL,
	[COMP_ID] [varchar](3) NULL,
 CONSTRAINT [PK_CT_FNC_MNG_B] PRIMARY KEY CLUSTERED 
(
	[CT_FNC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 제목 리소스 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 구분' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'URL'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관계 테이블 제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'REL_TBL_SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'멀티 선택 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'MUL_CHOI_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 메뉴 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'CT_MNG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'PT_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 관리 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MNG_B'
GO
/****** Object:  Table [dbo].[CT_FNC_MBSH_AUTH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_FNC_MBSH_AUTH_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_FNC_UID] [varchar](30) NOT NULL,
	[SECUL_CD] [varchar](30) NOT NULL,
	[AUTH_CD] [varchar](30) NULL,
 CONSTRAINT [PK_CT_FNC_MBSH_AUTH_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_FNC_UID] ASC,
	[SECUL_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MBSH_AUTH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MBSH_AUTH_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보안등급코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MBSH_AUTH_D', @level2type=N'COLUMN',@level2name=N'SECUL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'권한코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MBSH_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 회원 권한 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_MBSH_AUTH_D'
GO
/****** Object:  Table [dbo].[CT_FNC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_FNC_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_FNC_UID] [varchar](30) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NOT NULL,
	[CT_FNC_PID] [varchar](30) NOT NULL,
	[CT_FNC_ORDR] [int] NOT NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NOT NULL,
	[CT_FNC_TYP] [varchar](1) NULL,
	[CT_FNC_SUBJ_RESC_ID] [varchar](30) NOT NULL,
	[ORDR] [int] NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_FNC_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_FNC_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 부모 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 구분' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 제목 리소스 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 기능 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FNC_D'
GO
/****** Object:  Table [dbo].[CT_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[REF_ID] [varchar](30) NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[DISP_ORDR] [int] NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[CT_ID] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_FILE_D'
GO
/****** Object:  Table [dbo].[CT_ESTB_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_ESTB_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[MAST_UID] [varchar](30) NULL,
	[CT_SUBJ_RESC] [varchar](30) NULL,
	[CT_ITRO] NTEXT NULL,
	[CT_KWD] [varchar](30) NULL,
	[MNG_TGT_YN] [varchar](1) NULL,
	[JOIN_MET] [varchar](1) NULL,
	[DFT_AUTH] [varchar](1) NULL,
	[EXTN_OPEN_YN] [char](1) NULL,
	[ATT_LIM_SIZE] [int] NULL,
	[ATT_YN] [char](1) NULL,
	[MBSH_CNT] [int] NULL,
	[CT_STAT] [varchar](1) NULL,
	[CT_APVD_DT] [datetime] NULL,
	[RJT_OPIN_CONT] [varchar](255) NULL,
	[CT_ACT_STAT] [varchar](1) NULL,
	[CT_END_CONT] [varchar](255) NULL,
	[RECMD_YN] [char](1) NULL,
	[RECMD_DT] [datetime] NULL,
	[CT_EVAL_CD] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MODIFY_YN] [char](1) NULL,
	[CLS_REQS_YN] [char](1) NULL,
	[ALL_COMP_YN] [char](1) NULL,
 CONSTRAINT [PK_CT_ESTB_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'마스터 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MAST_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 제목 리소스 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_SUBJ_RESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 소개' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_ITRO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 키워드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_KWD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관리대상 여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MNG_TGT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'가입방법' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'JOIN_MET'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기본권한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'DFT_AUTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'외부 공개여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'EXTN_OPEN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부제한용량' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'ATT_LIM_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회원수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MBSH_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 상태' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_STAT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 승인일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_APVD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반려의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'RJT_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 활동 상태' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_ACT_STAT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 종료 내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_END_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'RECMD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천일자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'RECMD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 평가코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CT_EVAL_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'MODIFY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'CLS_REQS_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'전체회사여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B', @level2type=N'COLUMN',@level2name=N'ALL_COMP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 개설 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ESTB_B'
GO
/****** Object:  Table [dbo].[CT_DEBR_OPIN_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_DEBR_OPIN_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[OPIN] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[DEBR_ID] [varchar](30) NOT NULL,
	[OPIN_ORDR] [int] NOT NULL,
	[PROS_CONS_CD] [char](1) NULL,
 CONSTRAINT [PK_CT_DEBR_OPIN_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC,
	[DEBR_ID] ASC,
	[OPIN_ORDR] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'의견' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'OPIN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'토론실 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'DEBR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'의견순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'OPIN_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬성/반대' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D', @level2type=N'COLUMN',@level2name=N'PROS_CONS_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 토론실 의견 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_OPIN_D'
GO
/****** Object:  Table [dbo].[CT_DEBR_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_DEBR_B](
	[COMP_ID] [varchar](3) NOT NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NOT NULL,
	[CT_FNC_ORDR] [int] NULL,
	[SUBJ] [nvarchar](240) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[DEBR_ID] [varchar](30) NOT NULL,
	[ESTB_ITNT] [varchar](1000) NULL,
	[SITN] [int] NULL,
	[FIN_YN] [char](1) NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NULL,
	[CT_FNC_PID] [varchar](30) NULL,
 CONSTRAINT [PK_CT_DEBR_B] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[CT_ID] ASC,
	[DEBR_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'DEBR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'ESTB_ITNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'SITN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'FIN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'토론실' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_DEBR_B'
GO
/****** Object:  Table [dbo].[CT_CAT_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_CAT_B](
	[CAT_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[CAT_SUBJ_RESC_ID] [varchar](30) NULL,
	[CAT_PID] [varchar](30) NULL,
	[CAT_LOC_STEP] [int] NULL,
	[CHLD_YN] [char](1) NULL,
	[EXTN_OPEN_YN] [char](1) NULL,
	[FLD_TYP_CD] [varchar](30) NULL,
	[REG_DT] [datetime] NOT NULL,
	[MOD_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[CAT_ORDR] [int] NULL,
 CONSTRAINT [PK_CT_CAT_B] PRIMARY KEY CLUSTERED 
(
	[CAT_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리 제목 리소스 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_SUBJ_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리 부모 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'자식여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CHLD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'외부공개여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'EXTN_OPEN_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'폴더분류구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'FLD_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 카테고리 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_CAT_B'
GO
/****** Object:  Table [dbo].[CT_BULL_MAST_CMD_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_BULL_MAST_CMD_D](
	[CMT_ID] [bigint] NOT NULL,
	[BULL_ID] [int] NULL,
	[CMT] [nvarchar](1000) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_CT_BULL_MAST_CMD_D] PRIMARY KEY CLUSTERED 
(
	[CMT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'CMT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'CMT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판 마스터 한줄 답변 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_CMD_D'
GO
/****** Object:  Table [dbo].[CT_BULL_MAST_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_BULL_MAST_B](
	[BULL_ID] [int] NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[BULL_PID] [int] NULL,
	[REPLY_GRP_ID] [int] NULL,
	[REPLY_ORDR] [int] NULL,
	[REPLY_DPTH] [int] NULL,
	[BULL_STAT_CD] [char](1) NULL,
	[BULL_REZV_YN] [char](1) NULL,
	[TGT_DEPT_YN] [char](1) NULL,
	[TGT_USER_YN] [char](1) NULL,
	[SUBJ] [nvarchar](240) NULL,
	[BULL_REZV_DT] [datetime] NULL,
	[BULL_EXPR_DT] [datetime] NULL,
	[CONT] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[PROS_CNT] [int] NULL,
	[CONS_CNT] [int] NULL,
	[RECMD_CNT] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[ATT_YN] [char](1) NULL,
	[CT_ID] [varchar](30) NOT NULL,
	[CT_FNC_ID] [varchar](30) NULL,
	[CT_FNC_ORDR] [int] NULL,
	[CT_FNC_LOC_STEP] [varchar](30) NULL,
	[CT_FNC_UID] [varchar](30) NULL,
	[CT_FNC_PID] [varchar](30) NULL,
	[SCRE] [int] NULL,
 CONSTRAINT [PK_CT_BULL_MAST_B] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부모게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'REPLY_DPTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물예약여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상부서지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'TGT_DEPT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상사용자지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'TGT_USER_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시예약일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'BULL_EXPR_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬성수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'PROS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반대수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CONS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'RECMD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티기능순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기능 위치 단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_LOC_STEP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 기능 부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'CT_FNC_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B', @level2type=N'COLUMN',@level2name=N'SCRE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'커뮤니티 게시판 마스터 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BULL_MAST_B'
GO
/****** Object:  Table [dbo].[CT_BLON_CAT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_BLON_CAT_D](
	[CT_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[CAT_ID] [varchar](30) NOT NULL,
 CONSTRAINT [PK_CT_BLON_CAT_D] PRIMARY KEY CLUSTERED 
(
	[CT_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BLON_CAT_D', @level2type=N'COLUMN',@level2name=N'CT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BLON_CAT_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BLON_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'커뮤니티 소속 카테고리 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_BLON_CAT_D'
GO
/****** Object:  Table [dbo].[CT_ADM_NOTC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CT_ADM_NOTC_B](
	[BULL_ID] [int] NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[BULL_PID] [int] NULL,
	[REPLY_GRP_ID] [int] NULL,
	[REPLY_ORDR] [int] NULL,
	[REPLY_DPTH] [int] NULL,
	[BULL_STAT_CD] [char](1) NULL,
	[BULL_REZV_YN] [char](1) NULL,
	[TGT_DEPT_YN] [char](1) NULL,
	[TGT_USER_YN] [char](1) NULL,
	[SUBJ] [nvarchar](240) NULL,
	[BULL_REZV_DT] [datetime] NULL,
	[BULL_EXPR_DT] [datetime] NULL,
	[CONT] [ntext] NULL,
	[READ_CNT] [int] NULL,
	[PROS_CNT] [int] NULL,
	[CONS_CNT] [int] NULL,
	[RECMD_CNT] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[ATT_YN] [char](1) NULL,
 CONSTRAINT [PK_CT_ADM_NOTC_B] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[COMP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부모게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'REPLY_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'REPLY_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변단계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'REPLY_DPTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물예약여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상부서지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'TGT_DEPT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상사용자지정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'TGT_USER_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시예약일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_REZV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'BULL_EXPR_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'READ_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬성수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'PROS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반대수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'CONS_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'RECMD_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B', @level2type=N'COLUMN',@level2name=N'ATT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'관리자 커뮤니티 공지 기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CT_ADM_NOTC_B'
GO
/****** Object:  Table [dbo].[CM_SEQ_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CM_SEQ_B](
	[SEQ_ID] [varchar](30) NOT NULL,
	[SEQ_NO] [bigint] NULL,
 CONSTRAINT [PK_CM_SEQ_B] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_SEQ_B', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_SEQ_B', @level2type=N'COLUMN',@level2name=N'SEQ_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_SEQ_B'
GO
/****** Object:  Table [dbo].[CM_EMAIL_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CM_EMAIL_FILE_D](
	[EMAIL_ID] [int] NOT NULL,
	[DISP_ORDR] [int] NOT NULL,
	[DISP_NM] [nvarchar](240) NOT NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[FILE_CONT] [image] NULL,
 CONSTRAINT [PK_CM_EMAIL_FILE_D] PRIMARY KEY CLUSTERED 
(
	[EMAIL_ID] ASC,
	[DISP_ORDR] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이메일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'EMAIL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일첨부파일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_FILE_D'
GO
/****** Object:  Table [dbo].[CM_EMAIL_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CM_EMAIL_B](
	[EMAIL_ID] [int] NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[SEND_NM] [nvarchar](120) NULL,
	[RECV_NM] [nvarchar](2000) NULL,
	[CC_NM] [nvarchar](2000) NULL,
	[BCC_NM] [nvarchar](2000) NULL,
	[CONT] [ntext] NULL,
	[FILE_YN] [char](1) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_CM_EMAIL_B] PRIMARY KEY CLUSTERED 
(
	[EMAIL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'EMAIL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'발송명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'SEND_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수신명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'RECV_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'CC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'숨은참조명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'BCC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'FILE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_EMAIL_B'
GO
/****** Object:  Table [dbo].[CM_ATT_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CM_ATT_FILE_D](
	[ATT_ID] [varchar](30) NOT NULL,
	[ATT_FILE_ID] [varchar](30) NOT NULL,
	[ATT_FILE_EXT] [varchar](10) NULL,
	[ATT_FILE_SIZE] [int] NULL,
	[ATT_FILE_ORDR] [int] NULL,
	[ATT_FILE_SAVE_PATH] [varchar](240) NULL,
	[ATT_FILE_DISP_NM] [nvarchar](240) NOT NULL,
	[REF_ID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
 CONSTRAINT [PK_CM_ATT_FILE_D] PRIMARY KEY CLUSTERED 
(
	[ATT_ID] ASC,
	[ATT_FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 저장 위치' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_FILE_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'문서 ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록 일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정 일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자 UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부 파일 상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CM_ATT_FILE_D'
GO

/****** Object:  Table [dbo].[BA_USER_SETUP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_USER_SETUP_B](
	[USER_UID] [varchar](30) NOT NULL,
	[DDLN] [int] NULL,
 CONSTRAINT [PK_BA_USER_SETUP_B] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_USER_SETUP_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_USER_SETUP_B', @level2type=N'COLUMN',@level2name=N'DDLN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_USER_SETUP_B'
GO
/****** Object:  Table [dbo].[BA_TMP_SAVE_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_TMP_SAVE_L](
	[BULL_ID] [int] NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[CONT] [ntext] NULL,
	[BRD_ID_LIST] [varchar](400) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_TMP_SAVE_L] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC,
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'BRD_ID_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시물 임시저장' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TMP_SAVE_L'
GO
/****** Object:  Table [dbo].[BA_TBL_COLM_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_TBL_COLM_D](
	[COLM_ID] [varchar](30) NOT NULL,
	[TBL_ID] [varchar](30) NULL,
	[COLM_NM] [varchar](120) NULL,
	[COLM_DISP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[DATA_TYP] [varchar](10) NULL,
	[COLM_LEN] [int] NULL,
	[EX_COLM_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[COLM_TYP] [varchar](30) NULL,
	[COLM_TYP_VAL] [varchar](30) NULL,
	[MOD_DISP_YN] [char](1) NULL,
	[READ_DISP_YN] [char](1) NULL,
	[LIST_DISP_YN] [char](1) NULL,
	[REG_DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_BA_TBL_COLM_D] PRIMARY KEY CLUSTERED 
(
	[COLM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테이블ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'TBL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'컬럼명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'COLM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'컬럼표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'COLM_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'데이터타입' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'DATA_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'COLM_LEN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'확장컬럼여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'EX_COLM_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테이블컬럼' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_COLM_D'
GO
/****** Object:  Table [dbo].[BA_TBL_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_TBL_B](
	[TBL_ID] [varchar](30) NOT NULL,
	[TBL_NM] [varchar](120) NULL,
	[TBL_DISP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[EX_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[COMP_ID] [varchar](3) NOT NULL,
 CONSTRAINT [PK_BA_TBL_B] PRIMARY KEY CLUSTERED 
(
	[TBL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테이블ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'TBL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테이블명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'TBL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테이블표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'TBL_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'확장여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'EX_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테이블관리' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_TBL_B'
GO
/****** Object:  Table [dbo].[BA_SETUP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_SETUP_B](
	[DDLN] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'기한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B', @level2type=N'COLUMN',@level2name=N'DDLN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SETUP_B'
GO
/****** Object:  Table [dbo].[BA_SCRE_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_SCRE_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[SCRE] [int] NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_SCRE_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'점수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'SCRE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SCRE_HST_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'점수주기이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SCRE_HST_L'
GO
/****** Object:  Table [dbo].[BA_REZV_SAVE_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_REZV_SAVE_L](
	[BULL_ID] [int] NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[BULL_REZV_DT] [datetime] NULL,
	[CONT] [ntext] NULL,
	[BRD_ID_LIST] [varchar](400) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_REZV_SAVE_L] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC,
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'BRD_ID_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시물 예약저장' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_REZV_SAVE_L'
GO
/****** Object:  Table [dbo].[BA_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_BA_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RESC_B'
GO
/****** Object:  Table [dbo].[BA_RECMD_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_RECMD_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_RECMD_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RECMD_HST_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'추천이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_RECMD_HST_L'
GO
/****** Object:  Table [dbo].[BA_READ_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_READ_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[READ_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_READ_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_READ_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_READ_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_READ_HST_L', @level2type=N'COLUMN',@level2name=N'READ_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_READ_HST_L'
GO
/****** Object:  Table [dbo].[BA_PLT_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_BA_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_PLT_SETUP_D'
GO
/****** Object:  Table [dbo].[BA_MY_BULL_M]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_MY_BULL_M](
	[USER_UID] [varchar](30) NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NULL,
 CONSTRAINT [PK_BA_MY_BULL_M] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[BRD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_MY_BULL_M', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_MY_BULL_M', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_MY_BULL_M', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'나의게시물설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_MY_BULL_M'
GO
/****** Object:  Table [dbo].[BA_FAVOT_HST_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_FAVOT_HST_L](
	[BULL_ID] [int] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[FAVOT_VA] [char](1) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_FAVOT_HST_L] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_FAVOT_HST_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_FAVOT_HST_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬반투표값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_FAVOT_HST_L', @level2type=N'COLUMN',@level2name=N'FAVOT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_FAVOT_HST_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'찬반투표이력' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_FAVOT_HST_L'
GO
/****** Object:  Table [dbo].[BA_COLM_DISP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_COLM_DISP_D](
	[BRD_ID] [varchar](30) NOT NULL,
	[COLM_ID] [varchar](30) NOT NULL,
	[USE_YN] [char](1) NULL,
	[REG_DISP_YN] [char](1) NULL,
	[MOD_DISP_YN] [char](1) NULL,
	[READ_DISP_YN] [char](1) NULL,
	[LIST_DISP_YN] [char](1) NULL,
	[LIST_DISP_ORDR] [int] NULL,	
	[WDTH_PERC]		[VARCHAR](30) NULL,
	[ALN_VA]		[VARCHAR](30) NULL,
	[SORT_OPT_VA]	[VARCHAR](30) NULL,
	[DATA_SORT_VA]	[VARCHAR](30) NULL,	
	[READ_DISP_ORDR] [int] NULL,
	[COLS_WDTH_VA]		[VARCHAR](30) NULL,
	[TITL_COL_YN]		[CHAR](1) NULL,
	[MANDATORY_YN]		[CHAR](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_COLM_DISP_D] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC,
	[COLM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'컬럼ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'COLM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'REG_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'MOD_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'READ_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'READ_DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'병합넒이값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'COLS_WDTH_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대표컬럼여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'TITL_COL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'필수여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'MANDATORY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_COLM_DISP_D'
GO
/****** Object:  Table [dbo].[BA_CMT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_CMT_D](
	[CMT_ID] [bigint] NOT NULL,
	[BULL_ID] [int] NULL,
	[CMT] [nvarchar](1000) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_CMT_D] PRIMARY KEY CLUSTERED 
(
	[CMT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D', @level2type=N'COLUMN',@level2name=N'CMT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D', @level2type=N'COLUMN',@level2name=N'CMT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'한줄답변' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CMT_D'
GO
/****** Object:  Table [dbo].[BA_CAT_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_CAT_GRP_B](
	[CAT_GRP_ID] [varchar](30) NOT NULL,
	[CAT_GRP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[COMP_ID] [varchar](3) NOT NULL,
 CONSTRAINT [PK_BA_CAT_GRP_B] PRIMARY KEY CLUSTERED 
(
	[CAT_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리그룹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_GRP_B'
GO
/****** Object:  Table [dbo].[BA_CAT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_CAT_D](
	[CAT_ID] [varchar](30) NOT NULL,
	[CAT_GRP_ID] [varchar](30) NOT NULL,
	[CAT_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[DISP_ORDR] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_CAT_D] PRIMARY KEY CLUSTERED 
(
	[CAT_GRP_ID] ASC,
	[CAT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CAT_D'
GO
/****** Object:  Table [dbo].[BA_BULL_TGT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_TGT_D](
	[BULL_ID] [int] NOT NULL,
	[TGT_ID] [varchar](30) NOT NULL,
	[TGT_TYP] [char](1) NULL,
	[WITH_SUB_YN] [char](1) NULL,
 CONSTRAINT [PK_BA_BULL_TGT_D] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC,
	[TGT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_TGT_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_TGT_D', @level2type=N'COLUMN',@level2name=N'TGT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대상구분' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_TGT_D', @level2type=N'COLUMN',@level2name=N'TGT_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'하위포함여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_TGT_D', @level2type=N'COLUMN',@level2name=N'WITH_SUB_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시대상' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_TGT_D'
GO
/****** Object:  Table [dbo].[BA_BULL_SUBM_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_SUBM_L](
	[BULL_ID] [int] NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[SUBJ] [nvarchar](240) NULL,
	[CONT] [ntext] NULL,
	[BULL_STAT_CD] [char](1) NULL,
	[BRD_ID_LIST] [varchar](400) NULL,
	[RJT_OPIN] [varchar](400) NULL,
	[DISCR_UID] [varchar](30) NULL,
	[DISC_DT] [datetime] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_BULL_SUBM_L] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC,
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'BULL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'BRD_ID_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'반려의견' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'RJT_OPIN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'심의자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'DISCR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'심의일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'DISC_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시상신함' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_SUBM_L'
GO
/****** Object:  Table [dbo].[BA_BULL_POPUP_DISP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_POPUP_DISP_D](
	[BULL_ID] [int] NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[UPDT_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_BULL_POPUP_DISP_D] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC,
	[BULL_ID] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_DISP_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_DISP_D', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_DISP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_DISP_D', @level2type=N'COLUMN',@level2name=N'UPDT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시팝업표시상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_DISP_D'
GO
/****** Object:  Table [dbo].[BA_BULL_POPUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_POPUP_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[BULL_ID] [int] NOT NULL,
	[BRD_ID] [varchar](30) NOT NULL,
	[START_DT] [datetime] NULL,
	[END_DT] [datetime] NULL,
	[WIDTH] [int] NULL,
	[DISP_YN] [char](1) NULL,
	[USE_YN] [char](1) NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_BULL_POPUP_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[BRD_ID] ASC,
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시작일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'START_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'종료일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'팝업넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'WIDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시안함' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팝업설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_POPUP_D'
GO
/****** Object:  Table [dbo].[BA_BULL_PHOTO_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_PHOTO_D](
	[BULL_ID] [int] NOT NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[IMG_WDTH] [int] NULL,
	[IMG_HGHT] [int] NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_BULL_PHOTO_D] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지폭' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시물사진' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_PHOTO_D'
GO
/****** Object:  Table [dbo].[BA_BULL_OPT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_OPT_D](
	[BULL_ID] [int] NOT NULL,
	[SECU_YN] [char](1) NULL,
	[UGNT_YN] [char](1) NULL,
	[NOTC_YN] [char](1) NULL,
 CONSTRAINT [PK_BA_BULL_OPT_D] PRIMARY KEY CLUSTERED 
(
	[BULL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_OPT_D', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'보안여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_OPT_D', @level2type=N'COLUMN',@level2name=N'SECU_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'긴급여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_OPT_D', @level2type=N'COLUMN',@level2name=N'UGNT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공지여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_OPT_D', @level2type=N'COLUMN',@level2name=N'NOTC_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시옵션' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_OPT_D'
GO
/****** Object:  Table [dbo].[BA_BULL_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BULL_FILE_D](
	[FILE_ID] [int] NOT NULL,
	[BB_REF_ID] [int] NULL,
	[DISP_NM] [nvarchar](240) NULL,
	[DISP_ORDR] [int] NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_SIZE] [int] NULL,
	[SAVE_PATH] [varchar](240) NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_BULL_FILE_D] PRIMARY KEY CLUSTERED 
(
	[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'BB_REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시물첨부파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BULL_FILE_D'
GO
/****** Object:  Table [dbo].[BA_BRD_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BA_BRD_B](
	[BRD_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NULL,	
	[BRD_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[BRD_DESC] [nvarchar](400) NULL,
	[DEPT_BRD_YN] [char](1) NULL,
	[PHOTO_YN] [char](1) NULL,
	[TBL_ID] [varchar](30) NULL,
	[CAT_YN] [char](1) NULL,
	[CAT_GRP_ID] [varchar](30) NULL,
	[BRD_TYP_CD] [char](1) NULL,
	[REPLY_YN] [char](1) NULL,
	[DISC_YN] [char](1) NULL,
	[DISCR_UID] [varchar](30) NULL,
	[KND_CD] [char](1) NULL,
	[REZV_PRD] [int] NULL,
	[BODY_SIZE_LIM] [int] NULL,
	[ATT_SIZE_LIM] [int] NULL,
	[ATT_CAPA_LIM] [int] NULL,
	[NEW_DISP_YN] [char](1) NULL,
	[NEW_DISP_PRD] [int] NULL,
	[PREV_NEXT_YN] [char](1) NULL,
	[CMT_YN] [char](1) NULL,
	[FAVOT_YN] [char](1) NULL,
	[PICH_DISP_YN] [char](1) NULL,
	[PICH_UID] [varchar](30) NULL,
	[READ_HST_USE_YN] [char](1) NULL,
	[RECMD_USE_YN] [char](1) NULL,
	[SCRE_USE_YN] [char](1) NULL,
	[LAST_BULL_YN] [char](1) NULL,
	[NOTC_DISP_PRD] [int] NULL,
	[ALL_COMP_YN] [char](1) NULL,
	[PLT_YN] [char](1) NULL,
	[OPT_VA] [varchar](240) NULL,	
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,	
 CONSTRAINT [PK_BA_BRD_B] PRIMARY KEY CLUSTERED 
(
	[BRD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'BRD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'BRD_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서게시판여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'DEPT_BRD_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포토게시판여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'PHOTO_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테이블ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'TBL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'CAT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'카테고리그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'CAT_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판타입코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'BRD_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'답변형여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'REPLY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'심의여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'DISC_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'심의자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'DISCR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시판종류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'KND_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'게시예약기간' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'REZV_PRD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문사이즈제한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'BODY_SIZE_LIM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부사이즈제한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'ATT_SIZE_LIM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부용량제한' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'ATT_CAPA_LIM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'신규게시물표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'NEW_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'신규게시물표시기간' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'NEW_DISP_PRD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이전다음표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'PREV_NEXT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'한줄답변사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'CMT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'찬반투표사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'FAVOT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'PICH_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회이력사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'READ_HST_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추천사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'RECMD_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'점수사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'SCRE_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'최근게시물여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'LAST_BULL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공지표시기간' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'NOTC_DISP_PRD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'전사여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'ALL_COMP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'PLT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시판관리' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_BRD_B'
GO
/****** Object:  Table [dbo].[AP_STOR_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_STOR_R](
    [APV_NO]                      [bigint]           NOT NULL,
    [STOR_ID]                     [varchar]     (30) NOT NULL,
    CONSTRAINT [PK_AP_STOR_R] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [STOR_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_R', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_R', @level2Type = N'Column', @Level2Name = 'STOR_ID';
GO
/****** Object:  Table [dbo].[AP_RESC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_RESC_D](
	[COMP_ID] [varchar](3) NOT NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_AP_RESC_D] PRIMARY KEY CLUSTERED 
(
	[COMP_ID] ASC,
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_D', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_D', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_D'
GO
/****** Object:  Table [dbo].[AP_RESC_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_RESC_B](
	[RESC_ID] [varchar](30) NOT NULL,
	[LANG_TYP_CD] [varchar](30) NOT NULL,
	[RESC_VA] [nvarchar](400) NULL,
 CONSTRAINT [PK_AP_RESC_B] PRIMARY KEY CLUSTERED 
(
	[RESC_ID] ASC,
	[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RESC_B'
GO
/****** Object:  Table [dbo].[AP_RECV_GRP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_RECV_GRP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[RECV_GRP_ID] [varchar](30) NOT NULL,
	[RECV_GRP_SEQ] [int] NOT NULL,
	[RECV_DEPT_TYP_CD] [varchar](30) NOT NULL,
	[RECV_DEPT_ID] [varchar](30) NULL,
	[RECV_DEPT_NM] [nvarchar](400) NULL,
	[REF_DEPT_ID] [varchar](30) NULL,
	[REF_DEPT_NM] [nvarchar](400) NULL,
 CONSTRAINT [PK_AP_RECV_GRP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[RECV_GRP_ID] ASC,
	[RECV_GRP_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'RECV_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'RECV_GRP_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'REF_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D', @level2type=N'COLUMN',@level2name=N'REF_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_D'
GO
/****** Object:  Table [dbo].[AP_RECV_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_RECV_GRP_B](
	[USER_UID] [varchar](30) NOT NULL,
	[RECV_GRP_ID] [varchar](30) NOT NULL,
	[RECV_GRP_NM] [nvarchar](120) NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_RECV_GRP_B] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[RECV_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'RECV_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'RECV_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_RECV_GRP_B'
GO
/****** Object:  Table [dbo].[AP_PLT_SETUP_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_AP_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_PLT_SETUP_D'
GO
/****** Object:  Table [dbo].[AP_ONGO_FORM_TXT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_TXT_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_TXT_TYP_CD] [varchar](30) NOT NULL,
	[TXT_CONT] [nvarchar](240) NULL,
	[TXT_FONT_VA] [nvarchar](50) NULL,
	[TXT_STYL_VA] [nvarchar](50) NULL,
	[TXT_SIZE] [varchar](20) NULL,
	[TXT_COLR_VA] [varchar](20) NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_TXT_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_TXT_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식텍스트구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'FORM_TXT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트폰트값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_FONT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트스타일값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_STYL_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트색상값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_COLR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식텍스트상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_TXT_D'
GO
/****** Object:  Table [dbo].[AP_ONGO_FORM_ITEM_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_ITEM_L](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[ROW_NO] [int] NOT NULL,
	[COL_NO] [int] NOT NULL,
	[CSPN_VA] [int] NOT NULL,
	[ITEM_ID] [varchar](30) NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_ITEM_L] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_COMB_SEQ] ASC,
	[ROW_NO] ASC,
	[COL_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'ROW_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'열번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'COL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'콜스판값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'CSPN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'항목ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식항목내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_L'
GO
/****** Object:  Table [dbo].[AP_ONGO_FORM_ITEM_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_ITEM_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[ROW_CNT] [int] NOT NULL,
	[COL_CNT] [int] NOT NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_ITEM_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_COMB_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'ROW_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'열수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'COL_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식항목상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_ITEM_D'
GO
/****** Object:  Table [dbo].[AP_ONGO_FORM_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_IMG_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_IMG_TYP_CD] [varchar](30) NOT NULL,
	[IMG_PATH] [varchar](240) NULL,
	[IMG_WDTH] [int] NULL,
	[IMG_HGHT] [int] NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_IMG_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_IMG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식이미지구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'FORM_IMG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_IMG_D'
GO
/****** Object:  Table [dbo].[AP_ONGO_FORM_COMB_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_COMB_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_COMB_ID] [varchar](30) NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_COMB_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_COMB_ID] ASC,
	[FORM_COMB_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식구성상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_COMB_D'
GO

--DROP TABLE [dbo].[AP_ONGO_FORM_B];
CREATE TABLE [dbo].[AP_ONGO_FORM_B](
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_SEQ]                    [int]              NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [RESC_ID]                     [varchar]     (30) NOT NULL,
    [FORM_NM]                     [nvarchar]   (240) NULL,
    [FORM_BX_ID]                  [varchar]     (30) NOT NULL,
    [FORM_TYP_CD]                 [varchar]     (30) NULL,
    [INTG_ID]                     [varchar]     (30) NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [BODY_HGHT_PX]                [int]              NULL,
    [FORM_WDTH_TYP_CD]            [varchar]     (30) NULL,
    [FORM_APV_LN_TYP_CD]          [varchar]     (30) NULL,
    [TPL_YN]                      [char]         (1) NOT NULL,
    [USE_YN]                      [char]         (1) NOT NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [MODR_UID]                    [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    [BODY_HTML]                   [ntext]            NULL,
    [ERP_FORM_ID]                 [varchar]     (30) NULL,
    [ERP_FORM_TYP_CD]             [varchar]     (30) NULL,
    [DOC_KEEP_PRD_CD]             [varchar]     (30) NULL,
    [FORM_MAGN_VA]                [varchar]     (80) NULL,
    [INTG_FORM_YN]                [char]         (1) NULL,
    [BODY_OLINE_YN]               [char]         (1) NULL,
    [APV_LN_GRP_ID]               [varchar]     (30) NULL,
    [FIXD_APVR_YN]                [char]         (1) NULL,
    [AUTO_APV_LN_CD]              [varchar]     (30) NULL,
    [DOC_SUBJ_CD]                 [varchar]     (30) NULL,
    [REF_VW_GRP_ID]               [varchar]     (30) NULL,
    [REF_VW_FIXD_APVR_YN]         [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGO_FORM_B] PRIMARY KEY CLUSTERED (
    [FORM_ID] ASC,
    [FORM_SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행양식기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식함ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_BX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'INTG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문높이픽셀', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_HGHT_PX';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식넓이구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_WDTH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식결재라인구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_APV_LN_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'템플릿여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'TPL_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'USE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문HTML', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_HTML';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서보존기간코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'DOC_KEEP_PRD_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식여백값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_MAGN_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계양식여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'INTG_FORM_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문외곽선여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_OLINE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'FIXD_APVR_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'자동결재선코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'AUTO_APV_LN_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서제목코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'DOC_SUBJ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'REF_VW_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_B', @level2Type = N'Column', @Level2Name = 'REF_VW_FIXD_APVR_YN';
GO

/****** Object:  Table [dbo].[AP_ONGO_FORM_APV_LN_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGO_FORM_APV_LN_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_SEQ] [int] NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[APV_LN_TITL_TYP_CD] [varchar](30) NOT NULL,
	[APV_LN_DISP_TYP_CD] [varchar](30) NULL,
	[MAX_CNT] [int] NULL,
	[ALN_VA] [varchar](30) NULL,
	[BORD_USE_YN] [char](1) NULL,
	[TITL_USE_YN] [char](1) NULL,
	[LST_DUP_DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_AP_ONGO_FORM_APV_LN_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_SEQ] ASC,
	[FORM_COMB_SEQ] ASC,
	[APV_LN_TITL_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'FORM_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재라인타이틀구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'APV_LN_TITL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재라인표시구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'APV_LN_DISP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'최대개수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'MAX_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테두리선사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'BORD_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'타이틀사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'TITL_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록중복표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'LST_DUP_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행양식결재라인상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGO_FORM_APV_LN_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_TRX_D]    Script Date: 03/27/2015 18:03:31 ******/
CREATE TABLE [dbo].[AP_ONGD_TRX_D](
    [TRX_APV_NO]                  [bigint]           NOT NULL,
    [ORGN_APV_NO]                 [bigint]           NOT NULL,
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_SEQ]                    [int]              NOT NULL,
    [BODY_HGHT_PX]                [int]              NULL,
    [FORM_WDTH_TYP_CD]            [varchar]     (30) NULL,
    [FOOTER_VA]                   [nvarchar]   (400) NULL,
    CONSTRAINT [PK_AP_ONGD_TRX_D] PRIMARY KEY CLUSTERED (
    [TRX_APV_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서변환상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'변환결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'TRX_APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원본결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'ORGN_APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'FORM_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문높이픽셀', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'BODY_HGHT_PX';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식넓이구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'FORM_WDTH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'바닥글값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_TRX_D', @level2Type = N'Column', @Level2Name = 'FOOTER_VA';
GO
/****** Object:  Table [dbo].[AP_ONGD_SEND_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_SEND_D](
	[APV_NO] [bigint] NOT NULL,
	[SEND_SEQ] [int] NOT NULL,
	[RECV_DEPT_ID] [varchar](30) NULL,
	[RECV_DEPT_NM] [nvarchar](400) NULL,
	[REF_DEPT_NM] [nvarchar](400) NULL,
	[RECV_DEPT_TYP_CD] [varchar](30) NULL,
	[HDL_STAT_CD] [varchar](30) NOT NULL,
	[HDLR_UID] [varchar](30) NULL,
	[HDLR_NM] [nvarchar](120) NULL,
	[HDLR_OPIN_CONT] [nvarchar](800) NULL,
	[HDL_DT] [datetime] NULL,
	[VW_DT] [datetime] NULL,
	[PREV_APVR_APV_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_SEND_D] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[SEND_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'발송일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'SEND_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'REF_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'HDL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'HDLR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'HDLR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리자의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'HDLR_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'HDL_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'VW_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이전결재자결재일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D', @level2type=N'COLUMN',@level2name=N'PREV_APVR_APV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서발송상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_SEND_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_REF_DOC_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_REF_DOC_L](
	[APV_NO] [bigint] NOT NULL,
	[REF_DOC_HST_NO] [int] NOT NULL,
	[REF_APV_NO] [bigint] NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_REF_DOC_L] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[REF_DOC_HST_NO] ASC,
	[REF_APV_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조문서이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'REF_DOC_HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'REF_APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서참조문서내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_REF_DOC_L'
GO
/****** Object:  Table [dbo].[AP_ONGD_RECV_DEPT_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_RECV_DEPT_L](
	[APV_NO] [bigint] NOT NULL,
	[RECV_DEPT_HST_NO] [int] NOT NULL,
	[RECV_DEPT_SEQ] [int] NOT NULL,
	[RECV_DEPT_TYP_CD] [varchar](30) NOT NULL,
	[RECV_DEPT_ID] [varchar](30) NULL,
	[RECV_DEPT_NM] [nvarchar](400) NULL,
	[REF_DEPT_ID] [varchar](30) NULL,
	[REF_DEPT_NM] [nvarchar](400) NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[SEND_YN] [char](1) NULL,
	[ADD_SEND_YN] [char](1) NOT NULL,
 CONSTRAINT [PK_AP_ONGD_RECV_DEPT_L] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[RECV_DEPT_HST_NO] ASC,
	[RECV_DEPT_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수신처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'RECV_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조처ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'REF_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'참조처명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'REF_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'발송여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'SEND_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'추가발송여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L', @level2type=N'COLUMN',@level2name=N'ADD_SEND_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서수신처내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_RECV_DEPT_L'
GO
/****** Object:  Table [dbo].[AP_ONGD_PUB_BX_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_PUB_BX_D](
	[PUB_BX_DEPT_ID] [varchar](30) NOT NULL,
	[APV_NO] [bigint] NOT NULL,
	[PUB_BX_DT] [datetime] NOT NULL,
	[PUB_BX_END_YMD] [datetime] NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REGR_NM] [nvarchar](120) NULL,
	[REG_DEPT_ID] [varchar](30) NOT NULL,
	[REG_DEPT_NM] [nvarchar](120) NULL,
 CONSTRAINT [PK_AP_ONGD_PUB_BX_D] PRIMARY KEY CLUSTERED 
(
	[PUB_BX_DEPT_ID] ASC,
	[APV_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공람게시부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'PUB_BX_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공람게시일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'PUB_BX_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공람게시종료년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'PUB_BX_END_YMD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'REGR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'REG_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D', @level2type=N'COLUMN',@level2name=N'REG_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서공람게시상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_PUB_BX_CNFM_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_PUB_BX_CNFM_L](
	[PUB_BX_DEPT_ID] [varchar](30) NOT NULL,
	[APV_NO] [bigint] NOT NULL,
	[USER_UID] [varchar](30) NOT NULL,
	[USER_NM] [nvarchar](120) NULL,
	[DEPT_ID] [varchar](30) NULL,
	[DEPT_NM] [nvarchar](120) NULL,
	[POSIT_CD] [varchar](30) NULL,
	[POSIT_NM] [nvarchar](120) NULL,
	[VW_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_PUB_BX_CNFM_L] PRIMARY KEY CLUSTERED 
(
	[PUB_BX_DEPT_ID] ASC,
	[APV_NO] ASC,
	[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'공람게시부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'PUB_BX_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'USER_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'직위코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'POSIT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'직위명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'POSIT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L', @level2type=N'COLUMN',@level2name=N'VW_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서공람게시확인내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PUB_BX_CNFM_L'
GO
/****** Object:  Table [dbo].[AP_ONGD_PICH_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_PICH_D](
	[APV_NO] [bigint] NOT NULL,
	[PICH_TYP_CD] [varchar](30) NOT NULL,
	[PICH_UID] [varchar](30) NULL,
	[PICH_DEPT_ID] [varchar](30) NULL,
	[PICH_NM] [nvarchar](120) NULL,
	[PICH_DEPT_NM] [nvarchar](120) NULL,
	[PICH_OPIN_CONT] [nvarchar](800) NULL,
	[HDL_STAT_CD] [varchar](30) NOT NULL,
	[HDL_DT] [datetime] NULL,
	[VW_DT] [datetime] NULL,
	[PREV_APVR_APV_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_PICH_D] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[PICH_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자부서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_DEPT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PICH_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'HDL_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'처리일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'HDL_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조회일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'VW_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이전결재자결재일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D', @level2type=N'COLUMN',@level2name=N'PREV_APVR_APV_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서담당자상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_PICH_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_HOLD_OPIN_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_HOLD_OPIN_D](
	[APV_NO] [bigint] NOT NULL,
	[APVR_UID] [varchar](30) NOT NULL,
	[APV_OPIN_CONT] [nvarchar](2000) NULL,
	[APV_OPIN_DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_AP_ONGD_HOLD_OPIN_D] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[APVR_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_HOLD_OPIN_D', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_HOLD_OPIN_D', @level2type=N'COLUMN',@level2name=N'APVR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재의견내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_HOLD_OPIN_D', @level2type=N'COLUMN',@level2name=N'APV_OPIN_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재의견표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_HOLD_OPIN_D', @level2type=N'COLUMN',@level2name=N'APV_OPIN_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서보류의견상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_HOLD_OPIN_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_EXTN_DOC_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_EXTN_DOC_D](
	[APV_NO] [bigint] NOT NULL,
	[EXTN_DOC_TYP_CD] [varchar](30) NULL,
	[EXTN_DOC_CONT_TYP_CD] [varchar](30) NULL,
	[PICH_UID] [varchar](30) NULL,
	[PICH_NM] [nvarchar](120) NULL,
	[MAK_YY] [int] NULL,
	[SENDR_NM] [nvarchar](240) NULL,
	[PAGE_CNT] [int] NULL,
	[MAK_ZONE_NM] [nvarchar](240) NULL,
	[ORGN_APV_NO] [bigint] NULL,
	[REG_SEQ] [int] NULL,
 CONSTRAINT [PK_AP_ONGD_EXTN_DOC_D] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'외부문서구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'EXTN_DOC_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'외부문서컨텐츠구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'EXTN_DOC_CONT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'PICH_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'담당자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'PICH_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'생산년도' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'MAK_YY'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'발신자명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'SENDR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'페이지수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'PAGE_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'생산지역명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'MAK_ZONE_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'원본결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'ORGN_APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D', @level2type=N'COLUMN',@level2name=N'REG_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서외부문서상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_EXTN_DOC_D'
GO
/****** Object:  Table [dbo].[AP_ONGD_BODY_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_BODY_L](
	[APV_NO] [bigint] NOT NULL,
	[BODY_HST_NO] [int] NOT NULL,
	[BODY_HTML] [ntext] NOT NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_BODY_L] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[BODY_HST_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L', @level2type=N'COLUMN',@level2name=N'BODY_HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'본문HTML' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L', @level2type=N'COLUMN',@level2name=N'BODY_HTML'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서본문내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_BODY_L'
GO
/****** Object:  Table [dbo].[AP_ONGD_B]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_ONGD_B];
CREATE TABLE [dbo].[AP_ONGD_B](
    [APV_NO]                      [bigint]           NOT NULL,
    [DOC_NO]                      [nvarchar]    (60) NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [DOC_SUBJ]                    [nvarchar]   (400) NULL,
    [ATT_FILE_YN]                 [char]         (1) NOT NULL,
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_SEQ]                    [int]              NOT NULL,
    [FORM_APV_LN_TYP_CD]          [varchar]     (30) NOT NULL,
    [FORM_NM]                     [nvarchar]   (240) NULL,
    [BODY_HGHT_PX]                [int]              NULL,
    [FORM_WDTH_TYP_CD]            [varchar]     (30) NULL,
    [DOC_LANG_TYP_CD]             [varchar]     (30) NOT NULL,
    [DOC_STAT_CD]                 [varchar]     (30) NOT NULL,
    [DOC_PROS_STAT_CD]            [varchar]     (30) NOT NULL,
    [DOC_KEEP_PRD_CD]             [varchar]     (30) NULL,
    [DOC_TYP_CD]                  [varchar]     (30) NOT NULL,
    [SECUL_CD]                    [varchar]     (30) NULL,
    [UGNT_DOC_YN]                 [char]         (1) NULL,
    [ALL_READ_YN]                 [char]         (1) NULL,
    [REG_REC_LST_REG_YN]          [char]         (1) NULL,
    [REG_REC_LST_REG_SKED_YMD]    [datetime]         NULL,
    [SECU_DOC_YN]                 [char]         (1) NULL,
    [DOC_PW_ENC]                  [nvarchar]   (240) NULL,
    [SENDR_NM_RESC_ID]            [varchar]     (50) NULL,
    [SENDR_NM_RESC_NM]            [nvarchar]   (400) NULL,
    [OFSE_PATH]                   [varchar]    (240) NULL,
    [OFSE_HGHT_PX]                [int]              NULL,
    [CLS_INFO_ID]                 [varchar]     (30) NULL,
    [PSN_CLS_INFO_ID]             [varchar]     (30) NULL,
    [APV_LN_TYP_CD]               [varchar]     (30) NOT NULL,
    [BODY_HST_NO]                 [int]              NOT NULL,
    [ATT_HST_NO]                  [int]              NOT NULL,
    [RECV_DEPT_HST_NO]            [int]              NOT NULL,
    [REF_DOC_HST_NO]              [int]              NOT NULL,
    [MAK_DT]                      [datetime]         NULL,
    [MAKR_UID]                    [varchar]     (30) NULL,
    [MAKR_NM]                     [nvarchar]   (120) NULL,
    [MAK_DEPT_ID]                 [varchar]     (30) NULL,
    [MAK_DEPT_NM]                 [nvarchar]   (120) NULL,
    [CUR_APVR_ID]                 [varchar]     (30) NULL,
    [CUR_APVR_NM]                 [nvarchar]   (120) NULL,
    [CUR_APVR_DEPT_YN]            [char]         (1) NULL,
    [CMPL_DT]                     [datetime]         NULL,
    [CMPLR_UID]                   [varchar]     (30) NULL,
    [CMPLR_NM]                    [nvarchar]   (120) NULL,
    [ENFC_SCOP_CD]                [varchar]     (30) NULL,
    [ENFC_DT]                     [datetime]         NULL,
    [RECV_DOC_NO]                 [varchar]     (60) NULL,
    [RECV_DT]                     [datetime]         NULL,
    [ENFC_DOC_KEEP_PRD_CD]        [varchar]     (30) NULL,
    [ENFC_STAT_CD]                [varchar]     (30) NULL,
    [SEND_INST_ID]                [varchar]     (30) NULL,
    [SEND_INST_NM]                [nvarchar]   (120) NULL,
    [REC_LST_TYP_CD]              [varchar]     (30) NULL,
    [REC_LST_DEPT_ID]             [varchar]     (30) NULL,
    [ORGN_APV_NO]                 [bigint]           NULL,
    [TRX_APV_NO]                  [bigint]           NULL,
    [INTG_TYP_CD]                 [varchar]     (30) NULL,
    [INTG_NO]                     [bigint]           NULL,
    [SENDR_NM_ORG_ID]             [varchar]     (30) NULL,
    [SEND_WITH_REF_DOC_YN]        [char]         (1) NULL,
    [FOOTER_VA]                   [nvarchar]   (400) NULL,
    [XML_TYP_ID]                  [varchar]     (30) NULL,
    CONSTRAINT [PK_AP_ONGD_B] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서제목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_SUBJ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'첨부파일여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ATT_FILE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FORM_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식결재라인구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FORM_APV_LN_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FORM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문높이픽셀', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'BODY_HGHT_PX';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식넓이구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FORM_WDTH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서언어구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_LANG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서승인상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_PROS_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서보존기간코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_KEEP_PRD_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보안등급코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SECUL_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'긴급문서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'UGNT_DOC_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전체열람여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ALL_READ_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록대장등록여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'REG_REC_LST_REG_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록대장등록예정년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'REG_REC_LST_REG_SKED_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보안문서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SECU_DOC_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서비밀번호암호값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'DOC_PW_ENC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발신명의리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SENDR_NM_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발신명의리소스명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SENDR_NM_RESC_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'관인경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'OFSE_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'관인높이픽셀', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'OFSE_HGHT_PX';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'분류정보ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CLS_INFO_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개인분류정보ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'PSN_CLS_INFO_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'APV_LN_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'MAK_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'MAKR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'MAKR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'MAK_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'MAK_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'현재결재자ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CUR_APVR_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'현재결재자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CUR_APVR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'현재결재자부서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CUR_APVR_DEPT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완결일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CMPL_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완결자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CMPLR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완결자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'CMPLR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시행범위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ENFC_SCOP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시행일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ENFC_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'접수문서번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'RECV_DOC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'접수일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'RECV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시행문서보존기간코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ENFC_DOC_KEEP_PRD_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시행상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ENFC_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발송기관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SEND_INST_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발송기관명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SEND_INST_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대장구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'REC_LST_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대장부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'REC_LST_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원본결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'ORGN_APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'변환결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'TRX_APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'INTG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'INTG_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발신명의조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SENDR_NM_ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조문서포함발송여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'SEND_WITH_REF_DOC_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'바닥글값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'FOOTER_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'XML구분ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_B', @level2Type = N'Column', @Level2Name = 'XML_TYP_ID';
GO
/****** Object:  Table [dbo].[AP_ONGD_ATT_FILE_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ONGD_ATT_FILE_L](
	[APV_NO] [bigint] NOT NULL,
	[ATT_HST_NO] [int] NOT NULL,
	[ATT_SEQ] [int] NOT NULL,
	[ATT_DISP_NM] [nvarchar](240) NOT NULL,
	[FILE_EXT] [varchar](30) NULL,
	[FILE_KB] [int] NULL,
	[FILE_PATH] [varchar](240) NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_ONGD_ATT_FILE_L] PRIMARY KEY CLUSTERED 
(
	[APV_NO] ASC,
	[ATT_HST_NO] ASC,
	[ATT_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'APV_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부이력번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'ATT_HST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'ATT_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'ATT_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일K바이트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'FILE_KB'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'FILE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'진행문서첨부파일내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ONGD_ATT_FILE_L'
GO
/****** Object:  Table [dbo].[AP_ONGD_APV_LN_H]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_ONGD_APV_LN_H];
CREATE TABLE [dbo].[AP_ONGD_APV_LN_H](
    [APV_NO]                      [bigint]           NOT NULL,
    [APV_LN_HST_NO]               [int]              NOT NULL,
    [APV_LN_PNO]                  [int]              NOT NULL,
    [APV_LN_NO]                   [int]              NOT NULL,
    [DBL_APV_TYP_CD]              [varchar]     (30) NULL,
    [APVR_ROLE_CD]                [varchar]     (30) NOT NULL,
    [APV_STAT_CD]                 [varchar]     (30) NOT NULL,
    [ABS_RSON_CD]                 [varchar]     (30) NULL,
    [ABS_RSON_NM]                 [nvarchar]   (120) NULL,
    [APVR_DEPT_YN]                [char]         (1) NOT NULL,
    [PICH_APNT_YN]                [char]         (1) NULL,
    [AGNT_UID]                    [varchar]     (30) NULL,
    [PRED_YN]                     [char]         (1) NULL,
    [APVR_UID]                    [varchar]     (30) NULL,
    [APVR_NM]                     [nvarchar]   (120) NULL,
    [APVR_POSIT_CD]               [varchar]     (30) NULL,
    [APVR_POSIT_NM]               [nvarchar]   (120) NULL,
    [APVR_TITLE_CD]               [varchar]     (30) NULL,
    [APVR_TITLE_NM]               [nvarchar]   (120) NULL,
    [APV_DEPT_ID]                 [varchar]     (30) NULL,
    [APV_DEPT_NM]                 [nvarchar]   (120) NULL,
    [APV_DEPT_ABBR_NM]            [nvarchar]   (120) NULL,
    [POSIT_DISP_VA]               [nvarchar]   (120) NULL,
    [SIGN_DISP_VA]                [nvarchar]   (120) NULL,
    [DT_DISP_VA]                  [varchar]    (120) NULL,
    [SIGN_IMG_PATH]               [varchar]    (240) NULL,
    [VW_DT]                       [datetime]         NULL,
    [APV_DT]                      [datetime]         NULL,
    [PREV_APVR_APV_DT]            [datetime]         NULL,
    [APV_OPIN_CONT]               [nvarchar]  (2000) NULL,
    [APV_OPIN_DISP_YN]            [char]         (1) NULL,
    [PREV_BODY_HST_NO]            [int]              NULL,
    [HOLD_BODY_HST_NO]            [int]              NULL,
    [PREV_APV_LN_HST_NO]          [int]              NULL,
    [HOLD_APV_LN_HST_NO]          [int]              NULL,
    [PREV_ATT_HST_NO]             [int]              NULL,
    [HOLD_ATT_HST_NO]             [int]              NULL,
    [PREV_RECV_DEPT_HST_NO]       [int]              NULL,
    [HOLD_RECV_DEPT_HST_NO]       [int]              NULL,
    [PREV_REF_DOC_HST_NO]         [int]              NULL,
    [HOLD_REF_DOC_HST_NO]         [int]              NULL,
    [FIXD_APVR_YN]                [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_APV_LN_H] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [APV_LN_HST_NO] ASC,
    [APV_LN_PNO] ASC,
    [APV_LN_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서결재라인이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인부모번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_LN_PNO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_LN_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이중결재구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'DBL_APV_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자역할코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_ROLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'ABS_RSON_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'ABS_RSON_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자부서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_DEPT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자지정여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PICH_APNT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대결자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'AGNT_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전결여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PRED_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서약어명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ABBR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직위표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'POSIT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'SIGN_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일시표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'DT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명이미지경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'SIGN_IMG_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재자결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_APVR_APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견표시여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'APV_OPIN_DISP_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'HOLD_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'HOLD_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'HOLD_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'HOLD_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'PREV_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'HOLD_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_H', @level2Type = N'Column', @Level2Name = 'FIXD_APVR_YN';
GO
/****** Object:  Table [dbo].[AP_ONGD_APV_LN_E]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_ONGD_APV_LN_E];
CREATE TABLE [dbo].[AP_ONGD_APV_LN_E](
    [APV_NO]                      [bigint]           NOT NULL,
    [APV_LN_HST_NO]               [int]              NOT NULL,
    [APV_LN_PNO]                  [int]              NOT NULL,
    [APV_LN_NO]                   [int]              NOT NULL,
    [DBL_APV_TYP_CD]              [varchar]     (30) NULL,
    [APVR_ROLE_CD]                [varchar]     (30) NOT NULL,
    [APV_STAT_CD]                 [varchar]     (30) NOT NULL,
    [ABS_RSON_CD]                 [varchar]     (30) NULL,
    [ABS_RSON_NM]                 [nvarchar]   (120) NULL,
    [APVR_DEPT_YN]                [char]         (1) NOT NULL,
    [PICH_APNT_YN]                [char]         (1) NULL,
    [PRED_YN]                     [char]         (1) NULL,
    [AGNT_UID]                    [varchar]     (30) NULL,
    [APVR_UID]                    [varchar]     (30) NULL,
    [APVR_NM]                     [nvarchar]   (120) NULL,
    [APVR_POSIT_CD]               [varchar]     (30) NULL,
    [APVR_POSIT_NM]               [nvarchar]   (120) NULL,
    [APVR_TITLE_CD]               [varchar]     (30) NULL,
    [APVR_TITLE_NM]               [nvarchar]   (120) NULL,
    [APV_DEPT_ID]                 [varchar]     (30) NULL,
    [APV_DEPT_NM]                 [nvarchar]   (120) NULL,
    [APV_DEPT_ABBR_NM]            [nvarchar]   (120) NULL,
    [POSIT_DISP_VA]               [nvarchar]   (120) NULL,
    [SIGN_DISP_VA]                [nvarchar]   (120) NULL,
    [DT_DISP_VA]                  [nvarchar]   (120) NULL,
    [SIGN_IMG_PATH]               [varchar]    (240) NULL,
    [VW_DT]                       [datetime]         NULL,
    [APV_DT]                      [datetime]         NULL,
    [PREV_APVR_APV_DT]            [datetime]         NULL,
    [APV_OPIN_CONT]               [nvarchar]  (2000) NULL,
    [APV_OPIN_DISP_YN]            [char]         (1) NULL,
    [PREV_BODY_HST_NO]            [int]              NULL,
    [HOLD_BODY_HST_NO]            [int]              NULL,
    [PREV_APV_LN_HST_NO]          [int]              NULL,
    [HOLD_APV_LN_HST_NO]          [int]              NULL,
    [PREV_ATT_HST_NO]             [int]              NULL,
    [HOLD_ATT_HST_NO]             [int]              NULL,
    [PREV_RECV_DEPT_HST_NO]       [int]              NULL,
    [HOLD_RECV_DEPT_HST_NO]       [int]              NULL,
    [PREV_REF_DOC_HST_NO]         [int]              NULL,
    [HOLD_REF_DOC_HST_NO]         [int]              NULL,
    [FIXD_APVR_YN]                [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_APV_LN_E] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [APV_LN_PNO] ASC,
    [APV_LN_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서결재라인이행', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인부모번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_LN_PNO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_LN_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이중결재구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'DBL_APV_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자역할코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_ROLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'ABS_RSON_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'ABS_RSON_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자부서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_DEPT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자지정여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PICH_APNT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전결여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PRED_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대결자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'AGNT_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서약어명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ABBR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직위표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'POSIT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'SIGN_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일시표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'DT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명이미지경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'SIGN_IMG_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재자결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_APVR_APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견표시여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'APV_OPIN_DISP_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'HOLD_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'HOLD_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'HOLD_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'HOLD_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'PREV_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'HOLD_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_E', @level2Type = N'Column', @Level2Name = 'FIXD_APVR_YN';
GO
/****** Object:  Table [dbo].[AP_ONGD_APV_LN_D]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_ONGD_APV_LN_D];
CREATE TABLE [dbo].[AP_ONGD_APV_LN_D](
    [APV_NO]                      [bigint]           NOT NULL,
    [APV_LN_HST_NO]               [int]              NOT NULL,
    [APV_LN_PNO]                  [int]              NOT NULL,
    [APV_LN_NO]                   [int]              NOT NULL,
    [DBL_APV_TYP_CD]              [varchar]     (30) NULL,
    [APVR_ROLE_CD]                [varchar]     (30) NOT NULL,
    [APV_STAT_CD]                 [varchar]     (30) NOT NULL,
    [ABS_RSON_CD]                 [varchar]     (30) NULL,
    [ABS_RSON_NM]                 [nvarchar]   (120) NULL,
    [APVR_DEPT_YN]                [char]         (1) NOT NULL,
    [PICH_APNT_YN]                [char]         (1) NULL,
    [PRED_YN]                     [char]         (1) NULL,
    [AGNT_UID]                    [varchar]     (30) NULL,
    [APVR_UID]                    [varchar]     (30) NULL,
    [APVR_NM]                     [nvarchar]   (120) NULL,
    [APVR_POSIT_CD]               [varchar]     (30) NULL,
    [APVR_POSIT_NM]               [nvarchar]   (120) NULL,
    [APVR_TITLE_CD]               [varchar]     (30) NULL,
    [APVR_TITLE_NM]               [nvarchar]   (120) NULL,
    [APV_DEPT_ID]                 [varchar]     (30) NULL,
    [APV_DEPT_NM]                 [nvarchar]   (120) NULL,
    [APV_DEPT_ABBR_NM]            [nvarchar]   (120) NULL,
    [POSIT_DISP_VA]               [nvarchar]   (120) NULL,
    [SIGN_DISP_VA]                [nvarchar]   (120) NULL,
    [DT_DISP_VA]                  [nvarchar]   (120) NULL,
    [SIGN_IMG_PATH]               [varchar]    (240) NULL,
    [VW_DT]                       [datetime]         NULL,
    [APV_DT]                      [datetime]         NULL,
    [PREV_APVR_APV_DT]            [datetime]         NULL,
    [APV_OPIN_CONT]               [nvarchar]  (2000) NULL,
    [APV_OPIN_DISP_YN]            [char]         (1) NULL,
    [PREV_BODY_HST_NO]            [int]              NULL,
    [HOLD_BODY_HST_NO]            [int]              NULL,
    [PREV_APV_LN_HST_NO]          [int]              NULL,
    [HOLD_APV_LN_HST_NO]          [int]              NULL,
    [PREV_ATT_HST_NO]             [int]              NULL,
    [HOLD_ATT_HST_NO]             [int]              NULL,
    [PREV_RECV_DEPT_HST_NO]       [int]              NULL,
    [HOLD_RECV_DEPT_HST_NO]       [int]              NULL,
    [PREV_REF_DOC_HST_NO]         [int]              NULL,
    [HOLD_REF_DOC_HST_NO]         [int]              NULL,
    [FIXD_APVR_YN]                [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_APV_LN_D] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [APV_LN_PNO] ASC,
    [APV_LN_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서결재라인상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인부모번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_LN_PNO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_LN_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이중결재구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'DBL_APV_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자역할코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_ROLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'ABS_RSON_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'ABS_RSON_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자부서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_DEPT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자지정여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PICH_APNT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전결여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PRED_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대결자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'AGNT_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서약어명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ABBR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직위표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'POSIT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'SIGN_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일시표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'DT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명이미지경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'SIGN_IMG_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재자결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_APVR_APV_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재의견표시여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_OPIN_DISP_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류본문이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'HOLD_BODY_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류결재라인이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'HOLD_APV_LN_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류첨부이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'HOLD_ATT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류수신처이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'HOLD_RECV_DEPT_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이전참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PREV_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보류참조문서이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'HOLD_REF_DOC_HST_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_APV_LN_D', @level2Type = N'Column', @Level2Name = 'FIXD_APVR_YN';
GO
/****** Object:  Table [dbo].[AP_FORM_TXT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_TXT_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_TXT_TYP_CD] [varchar](30) NOT NULL,
	[TXT_CONT] [nvarchar](240) NULL,
	[TXT_FONT_VA] [nvarchar](50) NULL,
	[TXT_STYL_VA] [nvarchar](50) NULL,
	[TXT_SIZE] [varchar](20) NULL,
	[TXT_COLR_VA] [varchar](20) NULL,
 CONSTRAINT [PK_AP_FORM_TXT_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_TXT_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식텍스트구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'FORM_TXT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트폰트값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_FONT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트스타일값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_STYL_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'텍스트색상값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D', @level2type=N'COLUMN',@level2name=N'TXT_COLR_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식텍스트상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_TXT_D'
GO
/****** Object:  Table [dbo].[AP_FORM_ITEM_L]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_ITEM_L](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[ROW_NO] [int] NOT NULL,
	[COL_NO] [int] NOT NULL,
	[CSPN_VA] [int] NOT NULL,
	[ITEM_ID] [varchar](30) NULL,
 CONSTRAINT [PK_AP_FORM_ITEM_L] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_COMB_SEQ] ASC,
	[ROW_NO] ASC,
	[COL_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'ROW_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'열번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'COL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'콜스판값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'CSPN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'항목ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식항목내역' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_L'
GO
/****** Object:  Table [dbo].[AP_FORM_ITEM_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_ITEM_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[ROW_CNT] [int] NOT NULL,
	[COL_CNT] [int] NOT NULL,
 CONSTRAINT [PK_AP_FORM_ITEM_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_COMB_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'ROW_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'열수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_D', @level2type=N'COLUMN',@level2name=N'COL_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식항목상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_ITEM_D'
GO
/****** Object:  Table [dbo].[AP_FORM_IMG_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_IMG_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_IMG_TYP_CD] [varchar](30) NOT NULL,
	[IMG_PATH] [varchar](240) NULL,
	[IMG_WDTH] [int] NULL,
	[IMG_HGHT] [int] NULL,
 CONSTRAINT [PK_AP_FORM_IMG_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_IMG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식이미지구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'FORM_IMG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지넓이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'이미지높이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식이미지상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_IMG_D'
GO
/****** Object:  Table [dbo].[AP_FORM_COMB_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_COMB_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_COMB_ID] [varchar](30) NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_AP_FORM_COMB_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_COMB_ID] ASC,
	[FORM_COMB_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_COMB_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_COMB_D'
GO
/****** Object:  Table [dbo].[AP_FORM_BX_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_BX_D](
	[FORM_BX_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[FORM_BX_NM] [nvarchar](240) NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[FORM_BX_PID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
	[USE_YN] [char](1) NOT NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_FORM_BX_D] PRIMARY KEY CLUSTERED 
(
	[FORM_BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'FORM_BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식함명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'FORM_BX_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식함부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'FORM_BX_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식함상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_BX_D'
GO

--DROP TABLE [dbo].[AP_FORM_B];
CREATE TABLE [dbo].[AP_FORM_B](
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_SEQ]                    [int]              NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [RESC_ID]                     [varchar]     (30) NOT NULL,
    [FORM_NM]                     [nvarchar]   (240) NULL,
    [FORM_BX_ID]                  [varchar]     (30) NOT NULL,
    [FORM_TYP_CD]                 [varchar]     (30) NULL,
    [INTG_ID]                     [varchar]     (30) NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [BODY_HGHT_PX]                [int]              NULL,
    [FORM_WDTH_TYP_CD]            [varchar]     (30) NULL,
    [FORM_APV_LN_TYP_CD]          [varchar]     (30) NULL,
    [TPL_YN]                      [char]         (1) NOT NULL,
    [USE_YN]                      [char]         (1) NOT NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [MODR_UID]                    [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    [BODY_HTML]                   [ntext]            NULL,
    [ERP_FORM_ID]                 [varchar]     (30) NULL,
    [ERP_FORM_TYP_CD]             [varchar]     (30) NULL,
    [DOC_KEEP_PRD_CD]             [varchar]     (30) NULL,
    [FORM_MAGN_VA]                [varchar]     (80) NULL,
    [INTG_FORM_YN]                [char]         (1) NULL,
    [BODY_OLINE_YN]               [char]         (1) NULL,
    [APV_LN_GRP_ID]               [varchar]     (30) NULL,
    [FIXD_APVR_YN]                [char]         (1) NULL,
    [AUTO_APV_LN_CD]              [varchar]     (30) NULL,
    [DOC_SUBJ_CD]                 [varchar]     (30) NULL,
    [REF_VW_GRP_ID]               [varchar]     (30) NULL,
    [REF_VW_FIXD_APVR_YN]         [char]         (1) NULL,
    CONSTRAINT [PK_AP_FORM_B] PRIMARY KEY NONCLUSTERED (
    [FORM_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식함ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_BX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'INTG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문높이픽셀', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_HGHT_PX';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식넓이구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_WDTH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식결재라인구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_APV_LN_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'템플릿여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'TPL_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'USE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문HTML', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_HTML';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서보존기간코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'DOC_KEEP_PRD_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식여백값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FORM_MAGN_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계양식여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'INTG_FORM_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문외곽선여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'BODY_OLINE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'FIXD_APVR_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'자동결재선코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'AUTO_APV_LN_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서제목코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'DOC_SUBJ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'REF_VW_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_B', @level2Type = N'Column', @Level2Name = 'REF_VW_FIXD_APVR_YN';
GO

/****** Object:  Table [dbo].[AP_FORM_APV_LN_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_FORM_APV_LN_D](
	[FORM_ID] [varchar](30) NOT NULL,
	[FORM_COMB_SEQ] [int] NOT NULL,
	[APV_LN_TITL_TYP_CD] [varchar](30) NOT NULL,
	[APV_LN_DISP_TYP_CD] [varchar](30) NULL,
	[MAX_CNT] [int] NULL,
	[ALN_VA] [varchar](30) NULL,
	[BORD_USE_YN] [char](1) NULL,
	[TITL_USE_YN] [char](1) NULL,
	[LST_DUP_DISP_YN] [char](1) NULL,
 CONSTRAINT [PK_AP_FORM_APV_LN_D] PRIMARY KEY CLUSTERED 
(
	[FORM_ID] ASC,
	[FORM_COMB_SEQ] ASC,
	[APV_LN_TITL_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'FORM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식구성일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'FORM_COMB_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재라인타이틀구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'APV_LN_TITL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'결재라인표시구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'APV_LN_DISP_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'최대개수' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'MAX_CNT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'테두리선사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'BORD_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'타이틀사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'TITL_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'목록중복표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D', @level2type=N'COLUMN',@level2name=N'LST_DUP_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'양식결재라인상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_FORM_APV_LN_D'
GO

/****** Object:  Table [dbo].[AP_ERP_INTG_FILE_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_ERP_INTG_FILE_D](
	[INTG_NO] [bigint] NOT NULL,
	[SEQ] [int] NOT NULL,
	[ATT_DISP_NM] [nvarchar](240) NOT NULL,
	[FILE_EXT] [varchar](30) NOT NULL,
	[FILE_KB] [int] NULL,
	[FILE_CONT] [image] NULL,
 CONSTRAINT [PK_AP_ERP_INTG_FILE_D] PRIMARY KEY CLUSTERED 
(
	[INTG_NO] ASC,
	[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'연계번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'INTG_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'첨부표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'ATT_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일K바이트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_KB'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'파일내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'ERP연계파일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_ERP_INTG_FILE_D'
GO

--DROP TABLE [dbo].[AP_ERP_INTG_B];
CREATE TABLE [dbo].[AP_ERP_INTG_B](
    [INTG_NO]                     [bigint]           NOT NULL,
    [APV_NO]                      [bigint]           NULL,
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [RID]                         [nvarchar]   (400) NULL,
    [INTG_STAT_CD]                [varchar]     (30) NOT NULL,
    [DOC_SUBJ]                    [nvarchar]   (400) NULL,
    [BODY_HTML]                   [ntext]            NULL,
    [REG_DT]                      [datetime]         NULL,
    [INTG_URL]                    [nvarchar]  (1000) NULL,
    [INTG_TYP_CD]                 [varchar]     (30) NULL,
    [FORM_NM]                     [nvarchar]   (240) NULL,
    [REF_VA]                      [nvarchar]  (2000) NULL,
    CONSTRAINT [PK_AP_ERP_INTG_B] PRIMARY KEY CLUSTERED (
    [INTG_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP연계기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'INTG_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'INTG_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서제목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'DOC_SUBJ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'본문HTML', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'BODY_HTML';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'INTG_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'INTG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'FORM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_B', @level2Type = N'Column', @Level2Name = 'REF_VA';
GO

/****** Object:  Table [dbo].[AP_DOC_NO_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_DOC_NO_D](
	[YY] [int] NOT NULL,
	[REC_LST_ID] [varchar](30) NOT NULL,
	[ORG_ID] [varchar](30) NOT NULL,
	[ORG_RESC_ID] [varchar](30) NOT NULL,
	[DOC_SEQ] [bigint] NOT NULL,
 CONSTRAINT [PK_AP_DOC_NO_D] PRIMARY KEY CLUSTERED 
(
	[YY] ASC,
	[REC_LST_ID] ASC,
	[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'년' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'YY'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대장ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'REC_LST_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'ORG_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'문서일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'DOC_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'문서번호상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_DOC_NO_D'
GO
/****** Object:  Table [dbo].[AP_CLS_INFO_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_CLS_INFO_D](
	[ORG_ID] [varchar](30) NOT NULL,
	[CLS_INFO_ID] [varchar](30) NOT NULL,
	[CLS_INFO_PID] [varchar](30) NOT NULL,
	[RESC_ID] [varchar](30) NOT NULL,
	[CLS_INFO_NM] [nvarchar](120) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[USE_YN] [char](1) NOT NULL,
	[SYS_CLS_INFO_YN] [char](1) NOT NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_CLS_INFO_D] PRIMARY KEY CLUSTERED 
(
	[ORG_ID] ASC,
	[CLS_INFO_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'분류정보ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'CLS_INFO_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'분류정보부모ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'CLS_INFO_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'분류정보명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'CLS_INFO_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시스템분류정보여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'SYS_CLS_INFO_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'분류정보상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_CLS_INFO_D'
GO
/****** Object:  Table [dbo].[AP_APV_LN_GRP_D]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_APV_LN_GRP_D];
CREATE TABLE [dbo].[AP_APV_LN_GRP_D](
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [APV_LN_GRP_ID]               [varchar]     (30) NOT NULL,
    [APV_LN_GRP_SEQ]              [int]              NOT NULL,
    [APVR_UID]                    [varchar]     (30) NULL,
    [APV_DEPT_ID]                 [varchar]     (30) NULL,
    [APVR_DEPT_YN]                [char]         (1) NULL,
    [APVR_ROLE_CD]                [varchar]     (30) NULL,
    [DBL_APV_TYP_CD]              [varchar]     (30) NULL,
    [ABS_RSON_CD]                 [varchar]     (30) NULL,
    CONSTRAINT [PK_AP_APV_LN_GRP_D] PRIMARY KEY CLUSTERED (
    [USER_UID] ASC,
    [APV_LN_GRP_ID] ASC,
    [APV_LN_GRP_SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APVR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APV_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자부서여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APVR_DEPT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자역할코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'APVR_ROLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이중결재구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'DBL_APV_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_D', @level2Type = N'Column', @Level2Name = 'ABS_RSON_CD';
GO
/****** Object:  Table [dbo].[AP_APV_LN_GRP_B]    Script Date: 03/27/2015 18:03:31 ******/
--DROP TABLE [dbo].[AP_APV_LN_GRP_B];
CREATE TABLE [dbo].[AP_APV_LN_GRP_B](
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [APV_LN_GRP_ID]               [varchar]     (30) NOT NULL,
    [APV_LN_GRP_NM]               [nvarchar]   (120) NULL,
    [RESC_ID]                     [varchar]     (30) NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [MODR_UID]                    [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    [APV_LN_GRP_TYP_CD]           [varchar]     (30) NULL,
    CONSTRAINT [PK_AP_APV_LN_GRP_B] PRIMARY KEY CLUSTERED (
    [USER_UID] ASC,
    [APV_LN_GRP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인그룹구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_APV_LN_GRP_B', @level2Type = N'Column', @Level2Name = 'APV_LN_GRP_TYP_CD';
GO
/****** Object:  Table [dbo].[AP_AGN_APNT_R]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_AGN_APNT_R](
	[USER_UID] [varchar](30) NOT NULL,
	[AGNT_UID] [varchar](30) NOT NULL,
	[AGNT_RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_AP_AGN_APNT_R] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[AGNT_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_R', @level2type=N'COLUMN',@level2name=N'AGNT_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리인리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_R', @level2type=N'COLUMN',@level2name=N'AGNT_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리지정관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_R'
GO
/****** Object:  Table [dbo].[AP_AGN_APNT_D]    Script Date: 03/27/2015 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AP_AGN_APNT_D](
	[USER_UID] [varchar](30) NOT NULL,
	[AGNT_UID] [varchar](30) NOT NULL,
	[SEQ] [int] NOT NULL,
	[END_DT] [datetime] NULL,
	[STRT_DT] [datetime] NULL,
	[AGNT_RESC_ID] [varchar](30) NULL,
	[ABS_RSON_CD] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_AP_AGN_APNT_D] PRIMARY KEY NONCLUSTERED 
(
	[USER_UID] ASC,
	[AGNT_UID] ASC,
	[SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리인UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'AGNT_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리인리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'AGNT_RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'부재사유코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'ABS_RSON_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'대리지정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AP_AGN_APNT_D'
GO


--DROP TABLE [dbo].[PT_PUSH_MSG_D];
CREATE TABLE [dbo].[PT_PUSH_MSG_D](
    [PUSH_MSG_ID]                 [varchar]     (64) NOT NULL,
    [MD_ID]                       [varchar]     (30) NOT NULL,
    [MD_RID]                      [varchar]     (30) NOT NULL,
    [PUSH_SUBJ]                   [nvarchar]   (400) NULL,
    [WEB_URL]                     [nvarchar]  (1000) NULL,
    [WEB_AUTH_URL]                [nvarchar]  (1000) NULL,
    [MOB_URL]                     [nvarchar]  (1000) NULL,
    [MOB_AUTH_URL]                [nvarchar]  (1000) NULL,
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [LANG_TYP_CD]                 [varchar]     (30) NOT NULL,
    [ISU_DT]                      [datetime]         NULL,
    [VALD_LGIN_CNT]               [int]              NULL,
    CONSTRAINT [PK_PT_PUSH_MSG_D] PRIMARY KEY NONCLUSTERED (
    [PUSH_MSG_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'푸쉬메시지상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'푸쉬메시지ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'PUSH_MSG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'MD_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'MD_RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'푸쉬제목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'PUSH_SUBJ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'웹URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'WEB_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'웹권한URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'WEB_AUTH_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모바일URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'MOB_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모방일권한URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'MOB_AUTH_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'언어구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'LANG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발행일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'ISU_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'유효로그인수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_MSG_D', @level2Type = N'Column', @Level2Name = 'VALD_LGIN_CNT';
GO


--DROP TABLE [dbo].[CM_SRCH_B];
CREATE TABLE [dbo].[CM_SRCH_B](
    [SRCH_NO]                     [bigint]           NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [MD_RID]                      [varchar]     (30) NOT NULL,
    [MD_BX_ID]                    [varchar]     (30) NULL,
    [MD_ID]                       [varchar]     (30) NOT NULL,
    [MD_BX_RESC_ID]               [varchar]     (30) NULL,
    [TBL_ID]                      [varchar]     (30) NULL,
    [ACT_ID]                      [char]         (1) NOT NULL,
    [URL]                         [nvarchar]  (1000) NULL,
    [REG_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_CM_SRCH_B] PRIMARY KEY NONCLUSTERED (
    [SRCH_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'검색기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'검색번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'SRCH_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'MD_RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈함ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'MD_BX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'MD_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈함리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'MD_BX_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'테이블ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'TBL_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'액션ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'ACT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO

CREATE INDEX IDX_CM_SRCH_B1 ON dbo.CM_SRCH_B (MD_RID, MD_BX_ID, MD_ID);
GO



--DROP TABLE [dbo].[CM_SRCH_ERR_D];
CREATE TABLE [dbo].[CM_SRCH_ERR_D](
    [SRCH_NO]                     [bigint]           NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [MD_RID]                      [varchar]     (30) NOT NULL,
    [MD_BX_ID]                    [varchar]     (30) NULL,
    [MD_ID]                       [varchar]     (30) NOT NULL,
    [MD_BX_RESC_ID]               [varchar]     (30) NULL,
    [TBL_ID]                      [varchar]     (30) NULL,
    [ACT_ID]                      [char]         (1) NOT NULL,
    [URL]                         [nvarchar]  (1000) NULL,
    [REG_DT]                      [datetime]         NULL,
    [MSG]                         [nvarchar]  (2000) NULL,
    CONSTRAINT [PK_CM_SRCH_ERR_D] PRIMARY KEY CLUSTERED (
    [SRCH_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'검색오류상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'검색번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'SRCH_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈참조ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'MD_RID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈함ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'MD_BX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'MD_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'모듈함리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'MD_BX_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'테이블ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'TBL_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'액션ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'ACT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메시지', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SRCH_ERR_D', @level2Type = N'Column', @Level2Name = 'MSG';
GO


/****** Object:  Table [dbo].[BA_CD_GRP_B]    Script Date: 05/08/2015 15:09:25 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[BA_CD_GRP_B](
	[CD_GRP_ID] [varchar](30) NOT NULL,
	[COMP_ID] [varchar](3) NOT NULL,
	[CD_GRP_NM] [nvarchar](200) NULL,
	[RESC_ID] [varchar](30) NULL,
	[GRP_USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_CD_GRP_B] PRIMARY KEY NONCLUSTERED 
(
	[CD_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_NM'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_USE_YN'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_GRP_B'
GO

/****** Object:  Table [dbo].[BA_CD_D]    Script Date: 05/08/2015 15:09:58 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING OFF
GO

CREATE TABLE [dbo].[BA_CD_D](
	[CD_GRP_ID] [varchar](30) NOT NULL,
	[CD_ID] [varchar](30) NOT NULL,
	[CD_VA] [nvarchar](400) NULL,
	[RESC_ID] [varchar](30) NULL,
	[SORT_ORDR] [int] NOT NULL,
	[USE_YN] [char](1) NULL,
	[REGR_UID] [varchar](30) NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
 CONSTRAINT [PK_BA_CD_D] PRIMARY KEY NONCLUSTERED 
(
	[CD_GRP_ID] ASC,
	[CD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'CD_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'CD_VA'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'코드상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_CD_D'
GO

-- 2015-08-13 [결재]문서관리 보내기 추가
--DROP TABLE [dbo].[AP_ONGD_EX_D];
CREATE TABLE [dbo].[AP_ONGD_EX_D](
    [APV_NO]                      [bigint]           NOT NULL,
    [EX_ID]                       [varchar]     (30) NOT NULL,
    [EX_VA]                       [nvarchar]  (4000) NULL,
    CONSTRAINT [PK_AP_ONGD_EX_D] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [EX_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서확장상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_EX_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_EX_D', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'확장ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_EX_D', @level2Type = N'Column', @Level2Name = 'EX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'확장값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_EX_D', @level2Type = N'Column', @Level2Name = 'EX_VA';
GO



/****** Object:  Table [dbo].[DM_CAT_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_CAT_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[CAT_ID] VARCHAR(30) NOT NULL,
[CAT_NM] NVARCHAR(120),
[RESC_ID] VARCHAR(30),
[CAT_DESC] NVARCHAR(400),
[DISC_YN] CHAR(1),
[DISCR_UID] VARCHAR(30),
[REGR_DISP_YN] CHAR(1),
[OWNR_DISP_YN] CHAR(1),
[ITEM_USE_YN] CHAR(1),
[DFT_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_CAT_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[CAT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'DISC_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'DISCR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'작성자표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'REGR_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'소유자표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'OWNR_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'ITEM_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_B'
GO

/****** Object:  Table [dbo].[DM_BUMK_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_BUMK_B](
[BUMK_ID] VARCHAR(30) NOT NULL,
[BUMK_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30),
[BUMK_CAT] CHAR(1) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_BUMK_B] PRIMARY KEY CLUSTERED
(
[BUMK_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'BUMK_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'BUMK_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기유형' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'BUMK_CAT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_B'
GO

/****** Object:  Table [dbo].[DM_CD_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_CD_D](
[STOR_ID] VARCHAR(30) NOT NULL,
[CD_GRP_ID] VARCHAR(30) NOT NULL,
[CD_ID] VARCHAR(30) NOT NULL,
[CD_VA] NVARCHAR(400),
[RESC_ID] VARCHAR(30),
[SORT_ORDR] INT,
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_CD_D] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[CD_GRP_ID] ASC,
[CD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'CD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'CD_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_D'
GO

/****** Object:  Table [dbo].[DM_CD_GRP_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_CD_GRP_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[CD_GRP_ID] VARCHAR(30) NOT NULL,
[CD_GRP_NM] NVARCHAR(200),
[RESC_ID] VARCHAR(30),
[GRP_USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_CD_GRP_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[CD_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'CD_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'그룹사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'GRP_USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드그룹기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CD_GRP_B'
GO

/****** Object:  Table [dbo].[DM_COMM_FLD_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_COMM_FLD_B](
[FLD_ID] VARCHAR(30) NOT NULL,
[STOR_ID] VARCHAR(30) NOT NULL,
[CAT_ID] VARCHAR(30) NOT NULL,
[FLD_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30),
[FLD_PID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_COMM_FLD_B] PRIMARY KEY CLUSTERED
(
[FLD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공통폴더기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FLD_B'
GO

/****** Object:  Table [dbo].[DM_COMM_FILE_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_COMM_FILE_D](
[FILE_ID] INT NOT NULL,
[DM_REF_ID] INT,
[DISP_NM] NVARCHAR(240),
[DISP_ORDR] INT,
[FILE_EXT] VARCHAR(30),
[FILE_SIZE] INT,
[SAVE_PATH] VARCHAR(240),
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
CONSTRAINT [PK_DM_COMM_FILE_D] PRIMARY KEY CLUSTERED
(
[FILE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'참조ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'DM_REF_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공통파일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_FILE_D'
GO

/****** Object:  Table [dbo].[DM_ITEM_DISP_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_ITEM_DISP_D](
[ITEM_ID] VARCHAR(30) NOT NULL,
[ITEM_TYP_CD] CHAR(1) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[USE_YN] CHAR(1),
[ATRB_ID] VARCHAR(30),
[WDTH_PERC] VARCHAR(30),
[ALN_VA] VARCHAR(30),
[SORT_OPT_VA] VARCHAR(30),
[DATA_SORT_VA] VARCHAR(30),
[READ_DISP_YN] CHAR(1),
[LIST_DISP_YN] CHAR(1),
[LIST_DISP_ORDR] INT,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_ITEM_DISP_D] PRIMARY KEY CLUSTERED
(
[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'ITEM_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'READ_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목표시상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_DISP_D'
GO

/****** Object:  Table [dbo].[DM_AUTH_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_AUTH_D](
[COMP_ID] VARCHAR(3) NOT NULL,
[CLS_CD] VARCHAR(30) NOT NULL,
[CD] VARCHAR(30) NOT NULL,
[AUTH_VA] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1),
CONSTRAINT [PK_DM_AUTH_D] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[CLS_CD] ASC,
[CD] ASC,
[AUTH_VA] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'분류코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D', @level2type=N'COLUMN',@level2name=N'CLS_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D', @level2type=N'COLUMN',@level2name=N'CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'권한값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D', @level2type=N'COLUMN',@level2name=N'AUTH_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서권한상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_AUTH_D'
GO

/****** Object:  Table [dbo].[DM_BUMK_L]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_BUMK_L](
[STOR_ID] VARCHAR(30) NOT NULL,
[BUMK_ID] VARCHAR(30) NOT NULL,
[REG_CAT] VARCHAR(10) NOT NULL,
[CAT_VA] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_DM_BUMK_L] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[BUMK_ID] ASC,
[REG_CAT] ASC,
[CAT_VA] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L', @level2type=N'COLUMN',@level2name=N'BUMK_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록유형' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L', @level2type=N'COLUMN',@level2name=N'REG_CAT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L', @level2type=N'COLUMN',@level2name=N'CAT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_BUMK_L'
GO

/****** Object:  Table [dbo].[DM_TAKOVR_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_TAKOVR_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[DOC_GRP_ID] BIGINT NOT NULL,
[TAK_ORG_ID] VARCHAR(30) NOT NULL,
[TGT_ORG_ID] VARCHAR(30) NOT NULL,
[TAK_STAT_CD] CHAR(1) NOT NULL,
[TAK_REGR_UID] VARCHAR(30) NOT NULL,
[TAK_REG_DT] DATETIME,
CONSTRAINT [PK_DM_TAKOVR_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[DOC_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'DOC_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인계조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'TAK_ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대상조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'TGT_ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인계상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'TAK_STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인계등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'TAK_REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인계등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B', @level2type=N'COLUMN',@level2name=N'TAK_REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인수인계기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_TAKOVR_B'
GO

/****** Object:  Table [dbo].[DM_PUB_DOC_TGT_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_PUB_DOC_TGT_D](
[DOC_GRP_ID] BIGINT NOT NULL,
[TGT_ID] VARCHAR(30) NOT NULL,
[TGT_TYP_CD] CHAR(1) NOT NULL,
[STAT_CD] VARCHAR(30) NOT NULL,
[READ_STRT_DT] DATETIME,
[READ_END_DT] DATETIME,
[RJT_OPIN] VARCHAR(400),
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
CONSTRAINT [PK_DM_PUB_DOC_TGT_D] PRIMARY KEY CLUSTERED
(
[DOC_GRP_ID] ASC,
[TGT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'DOC_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대상ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'TGT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대상구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'TGT_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'열람시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'READ_STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'열람종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'READ_END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반려의견' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'RJT_OPIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공용문서대상상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PUB_DOC_TGT_D'
GO

/****** Object:  Table [dbo].[DM_COMM_RESC_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_COMM_RESC_B](
[RESC_ID] VARCHAR(30) NOT NULL,
[LANG_TYP_CD] VARCHAR(30) NOT NULL,
[RESC_VA] NVARCHAR(400),
CONSTRAINT [PK_DM_COMM_RESC_B] PRIMARY KEY CLUSTERED
(
[RESC_ID] ASC,
[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공통리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_COMM_RESC_B'
GO

/****** Object:  Table [dbo].[DM_CAT_DISP_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_CAT_DISP_D](
[STOR_ID] VARCHAR(30) NOT NULL,
[CAT_ID] VARCHAR(30) NOT NULL,
[ITEM_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1),
[ATRB_ID] VARCHAR(30),
[WDTH_PERC] VARCHAR(30),
[ALN_VA] VARCHAR(30),
[SORT_OPT_VA] VARCHAR(30),
[DATA_SORT_VA] VARCHAR(30),
[REG_DISP_YN] CHAR(1),
[MOD_DISP_YN] CHAR(1),
[READ_DISP_YN] CHAR(1),
[LIST_DISP_YN] CHAR(1),
[LIST_DISP_ORDR] INT,
[ADD_ITEM_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_CAT_DISP_D] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[CAT_ID] ASC,
[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이퍼센트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'WDTH_PERC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'줄맞춤값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'ALN_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬옵션값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'SORT_OPT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터정렬값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'DATA_SORT_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'REG_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'MOD_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'READ_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'LIST_DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'추가항목여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'ADD_ITEM_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형표시상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CAT_DISP_D'
GO

/****** Object:  Table [dbo].[DM_DOC_NO_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_DOC_NO_D](
[STOR_ID] VARCHAR(30) NOT NULL,
[YY] INT NOT NULL,
[ORG_ID] VARCHAR(30) NOT NULL,
[DOC_SEQ] BIGINT,
CONSTRAINT [PK_DM_DOC_NO_D] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[YY] ASC,
[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'년' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'YY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_DOC_NO_D', @level2type=N'COLUMN',@level2name=N'DOC_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서번호상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_DOC_NO_D'
GO

/****** Object:  Table [dbo].[DM_FLD_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_FLD_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[FLD_ID] VARCHAR(30) NOT NULL,
[FLD_GRP_ID] VARCHAR(30) NOT NULL,
[CAT_ID] VARCHAR(30),
[FLD_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30),
[FLD_PID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[FLD_TYP_CD] VARCHAR(30),
[USE_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_FLD_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[FLD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'유형ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_FLD_B'
GO

/****** Object:  Table [dbo].[DM_TRAN_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_TRAN_B](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [STOR_ID]                     [varchar]     (30) NOT NULL,
    [STOR_RESC_ID]                [varchar]     (30) NULL,
    [TRAN_TGT_STRT_YMD]           [datetime]         NULL,
    [TRAN_TGT_END_YMD]            [datetime]         NULL,    
    [DOC_TGT_CNT]                 [bigint]           NULL,    
    [TRAN_STRT_DT]                [datetime]         NULL,
    [TRAN_END_DT]                 [datetime]         NULL,
    [TRAN_PROC_STAT_CD]           [varchar]     (30) NULL,
    [DEFRAG_YN]                   [char]         (1) NULL,
    [USER_UID]                    [varchar]     (30) NULL,
    CONSTRAINT [PK_DM_TRAN_B] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'STOR_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'STOR_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_TGT_STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_TGT_END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서대상건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'DOC_TGT_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관시작일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_STRT_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관종료일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_END_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관진행상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_PROC_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조각모음여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'DEFRAG_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_B', @level2Type = N'Column', @Level2Name = 'USER_UID';
GO

/****** Object:  Table [dbo].[DM_TRAN_PROC_L]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_TRAN_PROC_L](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [SEQ]                         [int]              NOT NULL,
    [TRAN_CONT]                   [nvarchar]  (1000) NULL,
    [ALL_CNT]                     [bigint]           NULL,
    [PROC_CNT]                    [bigint]           NULL,
    [STRT_DT]                     [datetime]         NULL,
    [END_DT]                      [datetime]         NULL,
    [ERR_CONT]                    [nvarchar]  (1000) NULL,
    [ERR_YN]                      [char]         (1) NULL,
    CONSTRAINT [PK_DM_TRAN_PROC_L] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC,
    [SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관진행내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'순번', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'TRAN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전체건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ALL_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'PROC_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'STRT_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종류일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'END_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'오류내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ERR_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'오류여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ERR_YN';
GO

/****** Object:  Table [dbo].[DM_TRAN_TGT_T]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_TRAN_TGT_T](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [TRAN_SEQ]                    [bigint]           NOT NULL,
    [DOC_GRP_ID]                      [bigint]           NOT NULL,
    CONSTRAINT [PK_DM_TRAN_TGT_T] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC,
    [TRAN_SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상임시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_TGT_T';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'TRAN_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'DM_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'DOC_GRP_ID';
GO

/****** Object:  Table [dbo].[DM_SUBM_L]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_SUBM_L](
[COMP_ID] VARCHAR(3) NOT NULL,
[DOC_GRP_ID] BIGINT NOT NULL,
[STAT_CD] VARCHAR(30),
[RJT_OPIN] VARCHAR(400),
[DISCR_UID] VARCHAR(30),
[DISC_DT] DATETIME,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_SUBM_L] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[DOC_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'DOC_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상태코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'STAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'반려의견' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'RJT_OPIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'DISCR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'심의일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'DISC_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상신목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_SUBM_L'
GO

/****** Object:  Table [dbo].[DM_STOR_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_STOR_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[TBL_NM] VARCHAR(120) NOT NULL,
[STOR_NM] NVARCHAR(120),
[RESC_ID] VARCHAR(30),
[USE_YN] CHAR(1) NOT NULL,
[DFT_YN] CHAR(1) NOT NULL,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_STOR_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'테이블명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'TBL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'STOR_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_B'
GO

/****** Object:  Table [dbo].[DM_PSN_DOC_L]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_PSN_DOC_L](
[DOC_ID] BIGINT NOT NULL,
[COMP_ID] VARCHAR(3),
[FLD_ID] VARCHAR(30),
[SUBJ] NVARCHAR(240),
[CONT] NTEXT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
[C1] VARCHAR(120),
[C2] VARCHAR(120),
[C3] VARCHAR(2000),
[C4] VARCHAR(2000),
[C5] VARCHAR(14),
[C6] VARCHAR(14),
[C7] VARCHAR(10),
[C8] VARCHAR(10),
[C9] VARCHAR(30),
[CA] VARCHAR(30),
CONSTRAINT [PK_DM_PSN_DOC_L] PRIMARY KEY CLUSTERED
(
[DOC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'DOC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C1'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼2' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C2'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼3' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C3'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼4' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C4'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼5' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C5'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼6' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C6'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼7' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C7'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼8' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C8'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼9' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'C9'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'컬럼10' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L', @level2type=N'COLUMN',@level2name=N'CA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'개인문서기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PSN_DOC_L'
GO

/****** Object:  Table [dbo].[DM_RESC_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_RESC_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[LANG_TYP_CD] VARCHAR(30) NOT NULL,
[RESC_VA] NVARCHAR(400),
CONSTRAINT [PK_DM_RESC_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[RESC_ID] ASC,
[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_RESC_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_RESC_B'
GO

/****** Object:  Table [dbo].[DM_CLS_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_CLS_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[CLS_ID] VARCHAR(30) NOT NULL,
[CLS_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30),
[CLS_PID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[USE_YN] CHAR(1),
[HST_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_CLS_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[CLS_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'분류체계ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'CLS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'분류체계명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'CLS_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위분류체계ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'CLS_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이력여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'HST_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'분류체계기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_CLS_B'
GO

/****** Object:  Table [dbo].[DM_ITEM_B]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_ITEM_B](
[STOR_ID] VARCHAR(30) NOT NULL,
[ITEM_ID] VARCHAR(30) NOT NULL,
[ITEM_NM] VARCHAR(120),
[ITEM_DISP_NM] NVARCHAR(120),
[RESC_ID] VARCHAR(30),
[DATA_TYP] VARCHAR(10),
[COLM_LEN] INT,
[ITEM_TYP] VARCHAR(30),
[ITEM_TYP_VA] VARCHAR(30),
[REG_DISP_YN] CHAR(1),
[MOD_DISP_YN] CHAR(1),
[READ_DISP_YN] CHAR(1),
[LIST_DISP_YN] CHAR(1),
[LIST_DISP_ORDR] INT,
[USE_YN] CHAR(1),
[ADD_ITEM_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_DM_ITEM_B] PRIMARY KEY CLUSTERED
(
[STOR_ID] ASC,
[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ITEM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목표시명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ITEM_DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'데이터타입' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'DATA_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'COLM_LEN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목구분' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ITEM_TYP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목구분값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ITEM_TYP_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'REG_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'MOD_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조회표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'READ_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'LIST_DISP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'목록표시순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'LIST_DISP_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'추가항목여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'ADD_ITEM_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'항목기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_ITEM_B'
GO

/****** Object:  Table [dbo].[DM_STOR_DOC_R]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_STOR_DOC_R](
[DOC_GRP_ID] BIGINT NOT NULL,
[STOR_ID] VARCHAR(30) NOT NULL,
CONSTRAINT [PK_DM_STOR_DOC_R] PRIMARY KEY CLUSTERED
(
[DOC_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_DOC_R', @level2type=N'COLUMN',@level2name=N'STOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'문서그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_DOC_R', @level2type=N'COLUMN',@level2name=N'DOC_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장소문서관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_STOR_DOC_R'
GO

/****** Object:  Table [dbo].[DM_PLT_SETUP_D]    Script Date: 2015/10/23 15:02 ******/
CREATE TABLE [dbo].[DM_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_DM_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DM_PLT_SETUP_D'
GO

------------------------------------------------------------------------------------
-- ERP 양식 테이블 추가
------------------------------------------------------------------------------------

--DROP TABLE [dbo].[AP_ERP_FORM_B];
CREATE TABLE [dbo].[AP_ERP_FORM_B](
    [ERP_FORM_ID]                 [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [ERP_FORM_TYP_CD]             [varchar]     (30) NULL,
    [RESC_ID]                     [varchar]     (30) NULL,
    [REG_URL]                     [nvarchar]  (1000) NULL,
    [INFM_URL]                    [nvarchar]  (1000) NULL,
    CONSTRAINT [PK_AP_ERP_FORM_B] PRIMARY KEY CLUSTERED (
    [ERP_FORM_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP양식구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'ERP_FORM_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'REG_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'통보URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_FORM_B', @level2Type = N'Column', @Level2Name = 'INFM_URL';
GO


--DROP TABLE [dbo].[AP_ONGD_ERP_FORM_D];
CREATE TABLE [dbo].[AP_ONGD_ERP_FORM_D](
    [APV_NO]                      [bigint]           NOT NULL,
    [ERP_VA_TYP_CD]               [varchar]     (30) NOT NULL,
    [ERP_VA]                      [ntext]            NULL,
    CONSTRAINT [PK_AP_ONGD_ERP_FORM_D] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [ERP_VA_TYP_CD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서ERP양식상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_ERP_FORM_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_ERP_FORM_D', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP값구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_ERP_FORM_D', @level2Type = N'Column', @Level2Name = 'ERP_VA_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_ERP_FORM_D', @level2Type = N'Column', @Level2Name = 'ERP_VA';
GO

------------------------------------------------------------------------------------
-- 개인분류정보상세 - 추가
------------------------------------------------------------------------------------

--DROP TABLE [dbo].[AP_PSN_CLS_INFO_D];
CREATE TABLE [dbo].[AP_PSN_CLS_INFO_D](
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [PSN_CLS_INFO_ID]             [varchar]     (30) NOT NULL,
    [PSN_CLS_INFO_PID]            [varchar]     (30) NOT NULL,
    [PSN_CLS_INFO_NM]             [nvarchar]   (120) NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [USE_YN]                      [char]         (1) NOT NULL,
    CONSTRAINT [PK_AP_PSN_CLS_INFO_D] PRIMARY KEY CLUSTERED (
    [USER_UID] ASC,
    [PSN_CLS_INFO_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'개인분류정보상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개인분류정보ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'PSN_CLS_INFO_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개인분류정보부모ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'PSN_CLS_INFO_PID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개인분류정보명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'PSN_CLS_INFO_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_PSN_CLS_INFO_D', @level2Type = N'Column', @Level2Name = 'USE_YN';
GO

------------------------------------------------------------------------------------
-- 결재 - 저장소 이관 - 추가
------------------------------------------------------------------------------------

--DROP TABLE [dbo].[AP_STOR_B];
CREATE TABLE [dbo].[AP_STOR_B](
    [STOR_ID]                     [varchar]     (30) NOT NULL,
    [CRT_YN]                      [char]         (1) NULL,
    [RESC_ID]                     [varchar]     (30) NULL,
    [ALL_DOC_CNT]                 [bigint]           NULL,
    CONSTRAINT [PK_AP_STOR_B] PRIMARY KEY CLUSTERED (
    [STOR_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_B', @level2Type = N'Column', @Level2Name = 'STOR_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'생성여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_B', @level2Type = N'Column', @Level2Name = 'CRT_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_B', @level2Type = N'Column', @Level2Name = 'RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전체문서건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_B', @level2Type = N'Column', @Level2Name = 'ALL_DOC_CNT';
GO

--DROP TABLE [dbo].[AP_STOR_COMP_R];
CREATE TABLE [dbo].[AP_STOR_COMP_R](
    [STOR_ID]                     [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [STOR_RESC_ID]                [varchar]     (30) NULL,
    [COMP_DOC_CNT]                [bigint]           NULL,
    [USE_YN]                      [char]         (1) NULL,
    CONSTRAINT [PK_AP_STOR_COMP_R] PRIMARY KEY CLUSTERED (
    [STOR_ID] ASC,
    [COMP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소회사관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R', @level2Type = N'Column', @Level2Name = 'STOR_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R', @level2Type = N'Column', @Level2Name = 'STOR_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사문서건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R', @level2Type = N'Column', @Level2Name = 'COMP_DOC_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_STOR_COMP_R', @level2Type = N'Column', @Level2Name = 'USE_YN';
GO

--DROP TABLE [dbo].[AP_TRAN_B];
CREATE TABLE [dbo].[AP_TRAN_B](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [STOR_ID]                     [varchar]     (30) NOT NULL,
    [STOR_RESC_ID]                [varchar]     (30) NULL,
    [TRAN_TGT_STRT_YMD]           [datetime]         NULL,
    [TRAN_TGT_END_YMD]            [datetime]         NULL,
    [DOC_INCL_YN]                 [char]         (1) NULL,
    [PAP_DOC_INCL_YN]             [char]         (1) NULL,
    [DOC_TGT_CNT]                 [bigint]           NULL,
    [TRX_TGT_CNT]                 [bigint]           NULL,
    [PAP_TGT_CNT]                 [bigint]           NULL,
    [TRAN_STRT_DT]                [datetime]         NULL,
    [TRAN_END_DT]                 [datetime]         NULL,
    [TRAN_PROC_STAT_CD]           [varchar]     (30) NULL,
    [DEFRAG_YN]                   [char]         (1) NULL,
    [USER_UID]                    [varchar]     (30) NULL,
    CONSTRAINT [PK_AP_TRAN_B] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'STOR_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장소리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'STOR_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_TGT_STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_TGT_END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서포함여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'DOC_INCL_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종이문서포함여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'PAP_DOC_INCL_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'문서대상건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'DOC_TGT_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'변환대상건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRX_TGT_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종이대상건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'PAP_TGT_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관시작일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_STRT_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관종료일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_END_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관진행상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'TRAN_PROC_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조각모음여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'DEFRAG_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_B', @level2Type = N'Column', @Level2Name = 'USER_UID';
GO

--DROP TABLE [dbo].[AP_TRAN_FROM_R];
CREATE TABLE [dbo].[AP_TRAN_FROM_R](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    CONSTRAINT [PK_AP_TRAN_FROM_R] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC,
    [FORM_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관양식관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_FROM_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_FROM_R', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_FROM_R', @level2Type = N'Column', @Level2Name = 'FORM_ID';
GO

--DROP TABLE [dbo].[AP_TRAN_PROC_L];
CREATE TABLE [dbo].[AP_TRAN_PROC_L](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [SEQ]                         [int]              NOT NULL,
    [TRAN_CONT]                   [nvarchar]  (1000) NULL,
    [ALL_CNT]                     [bigint]           NULL,
    [PROC_CNT]                    [bigint]           NULL,
    [STRT_DT]                     [datetime]         NULL,
    [END_DT]                      [datetime]         NULL,
    [ERR_CONT]                    [nvarchar]  (1000) NULL,
    [ERR_YN]                      [char]         (1) NULL,
    CONSTRAINT [PK_AP_TRAN_PROC_L] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC,
    [SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관진행내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'순번', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'TRAN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전체건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ALL_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행건수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'PROC_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'STRT_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종류일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'END_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'오류내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ERR_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'오류여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_PROC_L', @level2Type = N'Column', @Level2Name = 'ERR_YN';
GO

--DROP TABLE [dbo].[AP_TRAN_TGT_T];
CREATE TABLE [dbo].[AP_TRAN_TGT_T](
    [TRAN_ID]                     [varchar]     (30) NOT NULL,
    [TRAN_DATA_TYP_CD]            [varchar]     (30) NOT NULL,
    [TRAN_SEQ]                    [bigint]           NOT NULL,
    [APV_NO]                      [bigint]           NOT NULL,
    CONSTRAINT [PK_AP_TRAN_TGT_T] PRIMARY KEY CLUSTERED (
    [TRAN_ID] ASC,
    [TRAN_DATA_TYP_CD] ASC,
    [TRAN_SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관대상임시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_TGT_T';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'TRAN_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관데이터구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'TRAN_DATA_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이관일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'TRAN_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_TRAN_TGT_T', @level2Type = N'Column', @Level2Name = 'APV_NO';
GO


/****** Object:  Table [dbo].[WC_USER_NAT_B]    Script Date: 2015/12/22 16:19 ******/
CREATE TABLE [dbo].[WC_USER_NAT_B](
[COMP_ID] VARCHAR(3) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[CD] VARCHAR(30) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
CONSTRAINT [PK_WC_USER_NAT_B] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[USER_UID] ASC
))


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_NAT_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_NAT_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_NAT_B', @level2type=N'COLUMN',@level2name=N'CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_NAT_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자국가설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_NAT_B'
GO

/****** Object:  Table [dbo].[WC_NAT_B]    Script Date: 2015/12/22 16:19 ******/
CREATE TABLE [dbo].[WC_NAT_B](
[COMP_ID] VARCHAR(3) NOT NULL,
[CD] VARCHAR(30) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[DFT_YN] CHAR(1) NULL,
CONSTRAINT [PK_WC_NAT_B] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[CD] ASC
))


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B', @level2type=N'COLUMN',@level2name=N'CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B', @level2type=N'COLUMN',@level2name=N'DFT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'국가설정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_NAT_B'
GO

/****** Object:  Table [dbo].[WC_ANNV_D]    Script Date: 2015/12/22 16:19 ******/
CREATE TABLE [dbo].[WC_ANNV_D](
[COMP_ID] VARCHAR(3) NOT NULL,
[SCHDL_ID] VARCHAR(30) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[NAT_CD] VARCHAR(30) NOT NULL,
CONSTRAINT [PK_WC_ANNV_D] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[SCHDL_ID] ASC
))


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ANNV_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ANNV_D', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ANNV_D', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'국가코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ANNV_D', @level2type=N'COLUMN',@level2name=N'NAT_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기념일상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ANNV_D'
GO


--DROP TABLE [dbo].[PT_USER_UAGNT_D];
CREATE TABLE [dbo].[PT_USER_UAGNT_D](
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [UAGNT_HASH_VA]               [int]              NOT NULL,
    [UAGNT_TKN_VA]                [varchar]     (30) NOT NULL,
    CONSTRAINT [PK_PT_USER_UAGNT_D] PRIMARY KEY CLUSTERED (
    [ODUR_UID] ASC,
    [UAGNT_HASH_VA] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자유저에이전트상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_USER_UAGNT_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_USER_UAGNT_D', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'유저에이전트해쉬값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_USER_UAGNT_D', @level2Type = N'Column', @Level2Name = 'UAGNT_HASH_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'유저에이전트토큰값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_USER_UAGNT_D', @level2Type = N'Column', @Level2Name = 'UAGNT_TKN_VA';
GO


--DROP TABLE [dbo].[OR_ODUR_SECU_D];
CREATE TABLE [dbo].[OR_ODUR_SECU_D](
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [SECU_ID]                     [varchar]     (60) NOT NULL,
    [SECU_VA]                     [nvarchar]   (200) NULL,
    CONSTRAINT [PK_OR_ODUR_SECU_D] PRIMARY KEY CLUSTERED (
    [ODUR_UID] ASC,
    [SECU_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자보안상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_SECU_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_SECU_D', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보안ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_SECU_D', @level2Type = N'Column', @Level2Name = 'SECU_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'보안값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ODUR_SECU_D', @level2Type = N'Column', @Level2Name = 'SECU_VA';
GO


/****** Object:  Table [dbo].[WC_USER_GRP_B]    Script Date: 2016/02/24 18:16 ******/
--DROP TABLE [dbo].[WC_USER_GRP_B];
CREATE TABLE [dbo].[WC_USER_GRP_B](
[USER_GRP_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(30) NOT NULL,
[USER_GRP_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[MODR_UID] VARCHAR(30) NOT NULL,
[MOD_DT] DATETIME NULL,
CONSTRAINT [PK_WC_USER_GRP_B] PRIMARY KEY CLUSTERED
(
[USER_GRP_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'USER_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자그룹명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'USER_GRP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자그룹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_B'
GO

/****** Object:  Table [dbo].[WC_USER_GRP_L]    Script Date: 2016/02/24 18:16 ******/
--DROP TABLE [dbo].[WC_USER_GRP_L];
CREATE TABLE [dbo].[WC_USER_GRP_L](
[USER_GRP_ID] VARCHAR(30) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WC_USER_GRP_L] PRIMARY KEY CLUSTERED
(
[USER_GRP_ID] ASC,
[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자그룹ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_L', @level2type=N'COLUMN',@level2name=N'USER_GRP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_L', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자그룹목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_USER_GRP_L'
GO


--DROP TABLE [dbo].[OR_DB_INTG_D];
CREATE TABLE [dbo].[OR_DB_INTG_D](
    [COMP_ID]                     [varchar]      (3) NOT NULL,
    [DB_USER_ID]                  [varchar]     (30) NULL,
    [DB_PW]                       [varchar]     (30) NULL,
    [DB_DRIVER]                   [varchar]     (60) NULL,
    [DB_URL]                      [varchar]     (60) NULL,
    [DB_TEST_SQL]                 [varchar]     (60) NULL,
    [CD_DEL_CD]                   [varchar]     (30) NULL,
    [ORG_DEL_CD]                  [varchar]     (30) NULL,
    [USER_DEL_CD]                 [varchar]     (30) NULL,
    [DEL_ORG_MOVE_ORG_ID]         [varchar]     (30) NULL,
    [DEL_USER_MOVE_ORG_ID]        [varchar]     (30) NULL,
    [NO_DEPT_ORG_ID]              [varchar]     (30) NULL,
    [ORG_SQL]                     [ntext]            NULL,
    [CD_SQL]                      [ntext]            NULL,
    [USER_SQL]                    [ntext]            NULL,
    [ADU_SQL]                     [ntext]            NULL,
    [INTG_TM]                     [varchar]     (10) NULL,
    [USE_YN]                      [char]         (1) NULL,
    CONSTRAINT [PK_OR_DB_INTG_D] PRIMARY KEY CLUSTERED (
    [COMP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'DB연계상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'데이터베이스사용자ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DB_USER_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'데이터베이스비밀번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DB_PW';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'데이터베이스드라이버', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DB_DRIVER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'데이터베이스URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DB_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'데이터베이스테스트SQL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DB_TEST_SQL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'코드삭제코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'CD_DEL_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직삭제코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'ORG_DEL_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자삭제코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'USER_DEL_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'삭제조직이동조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DEL_ORG_MOVE_ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'삭제사용자이동조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'DEL_USER_MOVE_ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부서없음조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'NO_DEPT_ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직도SQL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'ORG_SQL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'코드SQL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'CD_SQL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자SQL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'USER_SQL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'겸직SQL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'ADU_SQL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계시간', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'INTG_TM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_DB_INTG_D', @level2Type = N'Column', @Level2Name = 'USE_YN';
GO


----DROP TABLE [dbo].[OR_ORG_HST_R];
CREATE TABLE [dbo].[OR_ORG_HST_R](
    [ORG_ID]                      [varchar]     (30) NOT NULL,
    [HST_ORG_ID]                  [varchar]     (30) NOT NULL,
    [HST_RESC_ID]                 [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    CONSTRAINT [PK_OR_ORG_HST_R] PRIMARY KEY CLUSTERED (
    [ORG_ID] ASC,
    [HST_ORG_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직이력관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ORG_HST_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ORG_HST_R', @level2Type = N'Column', @Level2Name = 'ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이력조직ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ORG_HST_R', @level2Type = N'Column', @Level2Name = 'HST_ORG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이력리소스ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ORG_HST_R', @level2Type = N'Column', @Level2Name = 'HST_RESC_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_ORG_HST_R', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
GO


/****** Object:  Table [dbo].[EM_TMP_FILE_T]    Script Date: 2016/03/09 09:55 ******/
CREATE TABLE [dbo].[EM_TMP_FILE_T](
[TMP_FILE_ID] INT NOT NULL,
[DISP_NM] NVARCHAR(240) NOT NULL,
[FILE_EXT] VARCHAR(30) NOT NULL,
[FILE_SIZE] INT NOT NULL,
[SAVE_PATH] VARCHAR(240) NOT NULL,
CONSTRAINT [PK_EM_TMP_FILE_T] PRIMARY KEY CLUSTERED (
	[TMP_FILE_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임시파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T', @level2type=N'COLUMN',@level2name=N'TMP_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'임시파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_TMP_FILE_T'
GO

--DROP TABLE [dbo].[AP_ERP_INTG_CHIT_D];
CREATE TABLE [dbo].[AP_ERP_INTG_CHIT_D](
    [INTG_NO]                     [bigint]           NOT NULL,
    [CHIT_FORM_ID]                [varchar]     (30) NOT NULL,
    [FORM_NM]                     [nvarchar]   (240) NULL,
    [CHIT_BODY_HTML]              [ntext]            NULL,
    CONSTRAINT [PK_AP_ERP_INTG_CHIT_D] PRIMARY KEY CLUSTERED (
    [INTG_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'ERP연계전표상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_CHIT_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연계번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_CHIT_D', @level2Type = N'Column', @Level2Name = 'INTG_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전표양식번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_CHIT_D', @level2Type = N'Column', @Level2Name = 'CHIT_FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_CHIT_D', @level2Type = N'Column', @Level2Name = 'FORM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전표본문HTML', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ERP_INTG_CHIT_D', @level2Type = N'Column', @Level2Name = 'CHIT_BODY_HTML';
GO


/****** Object:  Table [dbo].[ST_SITE_B]    Script Date: 2016/03/14 18:08 ******/
CREATE TABLE [dbo].[ST_SITE_B](
[SITE_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CAT_ID] VARCHAR(30),
[SITE_NM] NVARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[CONT] NVARCHAR(240),
[SITE_URL] VARCHAR(240),
[SORT_ORDR] INT NOT NULL,
[REGR_UID] VARCHAR(30) NOT NULL,
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_ST_SITE_B] PRIMARY KEY CLUSTERED
(
[SITE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사이트ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'SITE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사이트명 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'SITE_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사이트URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'SITE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사이트' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_B'
GO

/****** Object:  Table [dbo].[ST_SITE_IMG_D]    Script Date: 2016/03/14 18:08 ******/
CREATE TABLE [dbo].[ST_SITE_IMG_D](
[SITE_ID] VARCHAR(30) NOT NULL,
[IMG_PATH] VARCHAR(240) NOT NULL,
[IMG_WDTH] INT NOT NULL,
[IMG_HGHT] INT NOT NULL,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_ST_SITE_IMG_D] PRIMARY KEY CLUSTERED
(
[SITE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사이트ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'SITE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'가로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'세로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_IMG_D'
GO

/****** Object:  Table [dbo].[ST_CAT_B]    Script Date: 2016/03/14 18:08 ******/
CREATE TABLE [dbo].[ST_CAT_B](
[CAT_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CAT_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_ST_CAT_B] PRIMARY KEY CLUSTERED
(
[CAT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'CAT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카테고리' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_CAT_B'
GO

/****** Object:  Table [dbo].[ST_RESC_B]    Script Date: 2016/03/14 18:08 ******/
CREATE TABLE [dbo].[ST_RESC_B](
[RESC_ID] VARCHAR(30) NOT NULL,
[LANG_TYP_CD] VARCHAR(30) NOT NULL,
[RESC_VA] NVARCHAR(400),
CONSTRAINT [PK_ST_RESC_B] PRIMARY KEY CLUSTERED
(
[RESC_ID] ASC,
[LANG_TYP_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'언어구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_RESC_B', @level2type=N'COLUMN',@level2name=N'LANG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_RESC_B', @level2type=N'COLUMN',@level2name=N'RESC_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_RESC_B'
GO

/****** Object:  Table [dbo].[ST_SITE_PLT_SETUP_D]    Script Date: 04/15/2016 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ST_SITE_PLT_SETUP_D](
	[USER_UID] [varchar](30) NOT NULL,
	[PLT_ID] [varchar](30) NOT NULL,
	[BX_ID] [varchar](30) NOT NULL,
	[SORT_ORDR] [int] NOT NULL,
 CONSTRAINT [PK_ST_SITE_PLT_SETUP_D] PRIMARY KEY CLUSTERED 
(
	[USER_UID] ASC,
	[PLT_ID] ASC,
	[BX_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'PLT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'BX_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_PLT_SETUP_D', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'Caption', @value=N'포틀릿설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'ST_SITE_PLT_SETUP_D'
GO

/****** Object:  Table [dbo].[WB_PUB_BC_B]    Script Date: 03/18/2016 18:03:31 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WB_PUB_BC_B](
	[BC_ID] [varchar](30) NOT NULL,
	[OPEN_TYP_CD] [varchar](30) NOT NULL,
	[FLD_ID] [varchar](30) NULL,
	[BC_NM] [nvarchar](30) NOT NULL,
	[COMP_NM] [nvarchar](120) NULL,
	[DEPT_NM] [nvarchar](120) NULL,
	[GRADE_NM] [nvarchar](30) NULL,
	[TICH_CONT] [nvarchar](800) NULL,
	[IPTFG_YN] [char](1) NULL,
	[FNO] [varchar](20) NULL,
	[COMP_ZIP_NO] [varchar](10) NULL,
	[COMP_ADR] [nvarchar](240) NULL,
	[COMP_DETL_ADR] [nvarchar](240) NULL,
	[HOME_ZIP_NO] [varchar](10) NULL,
	[HOME_ADR] [nvarchar](240) NULL,
	[HOME_DETL_ADR] [nvarchar](240) NULL,
	[BUMK_YN] [char](1) NULL,
	[PHOTO_FILE_ID] [varchar](30) NULL,
	[GEN_CD] [varchar](30) NULL,
	[NATY_CD] [varchar](30) NULL,
	[BIRTH] [datetime] NULL,
	[BIRTH_SCLC_CD] [varchar](30) NULL,
	[WEDD_ANNV] [datetime] NULL,
	[WEDD_ANNV_SCLC_CD] [varchar](30) NULL,
	[PSN_HPAGE_URL] [nvarchar](1000) NULL,
	[COMP_HPAGE_URL] [nvarchar](1000) NULL,
	[HOBY_CONT] [nvarchar](400) NULL,
	[SPECT_CONT] [nvarchar](400) NULL,
	[ESCHL_NM] [nvarchar](120) NULL,
	[MSCHL_NM] [nvarchar](120) NULL,
	[HSCHL_NM] [nvarchar](120) NULL,
	[UNIV_CONT] [nvarchar](240) NULL,
	[BHIST_CONT] [nvarchar](400) NULL,
	[CLNS_ID] [varchar](30) NULL,
	[NOTE_CONT] [nvarchar](400) NULL,
	[ATT_FILE_ID] [varchar](30) NULL,
	[BC_REG_TYP_CD] [varchar](30) NULL,
	[REGR_UID] [varchar](30) NOT NULL,
	[REG_DT] [datetime] NULL,
	[MODR_UID] [varchar](30) NULL,
	[MOD_DT] [datetime] NULL,
	[DEL_YN] [char](1) NULL,
	[EMAIL] [varchar](240) NULL,
	[BC_EN_NM] [nvarchar](30) NULL,
	[DFT_CNTC_TYP_CD] [varchar](30) NULL,
	[COMP_ID] [varchar](3) NULL,
	[ORIGINAL_BC_ID] [varchar](30) NULL,
	[MAIN_SETUP_YN] [char](1) NULL,
	[DUPLICATE_BC_ID] [varchar](30) NULL,
	[INITIAL_NM] [nvarchar](3) NULL,
 CONSTRAINT [PK_WB_PUB_BC_B] PRIMARY KEY CLUSTERED 
(
	[BC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공개구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'OPEN_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'직급명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'GRADE_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'담당업무내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'TICH_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주요인사여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'IPTFG_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팩스번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'FNO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사_상세주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_DETL_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택상세주소' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'HOME_DETL_ADR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'즐겨찾기여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BUMK_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사진파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'PHOTO_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'성별코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'GEN_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'국적코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'NATY_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생년월일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BIRTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생년월일월력코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BIRTH_SCLC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'결혼기념일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'WEDD_ANNV'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'결혼기념일월력코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'WEDD_ANNV_SCLC_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'개인홈페이지URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'PSN_HPAGE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사홈페이지URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_HPAGE_URL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'취미내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'HOBY_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'특기사항내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'SPECT_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'초등학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'ESCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'중학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'MSCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'고등학교명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'HSCHL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'대학교내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'UNIV_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'약력내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BHIST_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'친밀도내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'CLNS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'비고' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'NOTE_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'첨부파일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'ATT_FILE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함등록구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_REG_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'삭제여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'DEL_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'명함영문성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'BC_EN_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기본연락처구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'DFT_CNTC_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'원본명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'ORIGINAL_BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메인설정여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'MAIN_SETUP_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사본명함ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'DUPLICATE_BC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'초성명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B', @level2type=N'COLUMN',@level2name=N'INITIAL_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공유명함' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_BC_B'
GO


/****** Object:  Table [dbo].[WB_PUB_FLD_B]    Script Date: 2016/03/17 17:40 ******/
CREATE TABLE [dbo].[WB_PUB_FLD_B](
[FLD_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[FLD_NM] VARCHAR(120) NOT NULL,
[RESC_ID] VARCHAR(30) NOT NULL,
[FLD_PID] VARCHAR(30) NOT NULL,
[SORT_ORDR] INT NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WB_PUB_FLD_B] PRIMARY KEY CLUSTERED
(
[FLD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'폴더명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'리소스ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'RESC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'상위폴더ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'FLD_PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공유폴더' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WB_PUB_FLD_B'
GO



/****** Object:  Table [dbo].[BA_POP_USER_B]    Script Date: 2016/04/29 13:36 ******/
CREATE TABLE [dbo].[BA_POP_USER_B](
[COMP_ID] VARCHAR(3) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
CONSTRAINT [PK_BA_POP_USER_B] PRIMARY KEY CLUSTERED
(
[COMP_ID] ASC,
[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_POP_USER_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_POP_USER_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팝업사용자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_POP_USER_B'
GO


/****** Object:  Table [dbo].[WC_ERP_SCHDL_B]    Script Date: 2016/05/09 11:34 ******/
CREATE TABLE [dbo].[WC_ERP_SCHDL_B](
[SCHDL_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[SCHDL_TYP_CD] VARCHAR(30) NOT NULL,
[SUBJ] NVARCHAR(120) NOT NULL,
[CONT] NVARCHAR(500),
[STRT_DT] DATETIME NOT NULL,
[END_DT] DATETIME NOT NULL,
[ALLDAY_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WC_ERP_SCHDL_B] PRIMARY KEY CLUSTERED
(
[SCHDL_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정구분코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SCHDL_TYP_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'ALLDAY_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ERP일정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_ERP_SCHDL_B'
GO


/****** Object:  Table [dbo].[WC_MAIL_SCHDL_R]    Script Date: 2016/05/20 11:58 ******/
CREATE TABLE [dbo].[WC_MAIL_SCHDL_R](
[SCHDL_ID] VARCHAR(30) NOT NULL,
[USER_UID] VARCHAR(30) NOT NULL,
[ACPT_YN] CHAR(1),
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[CMPL_DT] DATETIME,
[MAIL_ID]	VARCHAR(30),
[COPY_SCHDL_ID]	VARCHAR(30),
CONSTRAINT [PK_WC_MAIL_SCHDL_R] PRIMARY KEY CLUSTERED
(
[SCHDL_ID] ASC,
[USER_UID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'USER_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수락여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'ACPT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'완료일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'CMPL_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메일ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'MAIL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'복사일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R', @level2type=N'COLUMN',@level2name=N'COPY_SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'메일일정' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_MAIL_SCHDL_R'
GO





/****** Object:  Table [dbo].[EM_SEND_B]    Script Date: 2016/05/25 14:14 ******/
CREATE TABLE [dbo].[EM_SEND_B](
[SEND_NO] BIGINT NOT NULL,
[SUBJ] NVARCHAR(240),
[CONT] NTEXT,
CONSTRAINT [PK_EM_SEND_B] PRIMARY KEY CLUSTERED
(
[SEND_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보내기번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_B', @level2type=N'COLUMN',@level2name=N'SEND_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보내기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_B'
GO

/****** Object:  Table [dbo].[EM_SEND_FILE_D]    Script Date: 2016/05/25 14:14 ******/
CREATE TABLE [dbo].[EM_SEND_FILE_D](
[SEND_NO] BIGINT NOT NULL,
[FILE_SEQ] INT NOT NULL,
[DISP_NM] NVARCHAR(240),
[FILE_EXT] VARCHAR(30),
[FILE_SIZE] INT,
[SAVE_PATH] VARCHAR(240),
CONSTRAINT [PK_EM_SEND_FILE_D] PRIMARY KEY CLUSTERED
(
[SEND_NO] ASC,
[FILE_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보내기번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'SEND_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'저장경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D', @level2type=N'COLUMN',@level2name=N'SAVE_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'보내기파일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_FILE_D'
GO



/****** Object:  Table [dbo].[EM_SEND_EMAIL_FILE_D]    Script Date: 2016/05/27 16:01 ******/
CREATE TABLE [dbo].[EM_SEND_EMAIL_FILE_D](
[EMAIL_NO] BIGINT NOT NULL,
[FILE_SEQ] INT NOT NULL,
[DISP_NM] NVARCHAR(240) NOT NULL,
[FILE_EXT] VARCHAR(30),
[FILE_SIZE] INT,
[FILE_CONT] VARBINARY NOT NULL,
CONSTRAINT [PK_EM_SEND_EMAIL_FILE_D] PRIMARY KEY CLUSTERED
(
[EMAIL_NO] ASC,
[FILE_SEQ] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일번호 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'EMAIL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일일련번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SEQ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'표시이름' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'DISP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일확장자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_EXT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일크기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D', @level2type=N'COLUMN',@level2name=N'FILE_CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일보내기첨부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_FILE_D'
GO

/****** Object:  Table [dbo].[EM_SEND_EMAIL_B]    Script Date: 2016/05/27 16:01 ******/
CREATE TABLE [dbo].[EM_SEND_EMAIL_B](
[EMAIL_NO] BIGINT NOT NULL,
[SUBJ] NVARCHAR(240),
[CONT] NTEXT,
[FILE_YN] CHAR(1),
CONSTRAINT [PK_EM_SEND_EMAIL_B] PRIMARY KEY CLUSTERED
(
[EMAIL_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일번호 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_B', @level2type=N'COLUMN',@level2name=N'EMAIL_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'제목' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_B', @level2type=N'COLUMN',@level2name=N'SUBJ'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_B', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'파일여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_B', @level2type=N'COLUMN',@level2name=N'FILE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이메일보내기' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_SEND_EMAIL_B'
GO



/****** Object:  Table [dbo].[BA_SNS_R]    Script Date: 2016/06/23 14:26 ******/
CREATE TABLE [dbo].[BA_SNS_R](
[BULL_ID] INT NOT NULL,
[BRD_ID] VARCHAR(30) NOT NULL,
[ATRB_ID] VARCHAR(30) NOT NULL,
[USE_YN] CHAR(1) NOT NULL,
[SORT_ORDR] INT NULL,
CONSTRAINT [PK_BA_SNS_R] PRIMARY KEY CLUSTERED
(
[BULL_ID] ASC,
[BRD_ID] ASC,
[ATRB_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시물ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R', @level2type=N'COLUMN',@level2name=N'BULL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'게시판ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R', @level2type=N'COLUMN',@level2name=N'BRD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'속성ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R', @level2type=N'COLUMN',@level2name=N'ATRB_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'SNS관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BA_SNS_R'
GO


--DROP TABLE [dbo].[AP_ONGD_REF_VW_D];
CREATE TABLE [dbo].[AP_ONGD_REF_VW_D](
    [APV_NO]                      [bigint]           NOT NULL,
    [REGR_UID]                    [varchar]     (30) NOT NULL,
    [REF_VWR_UID]                 [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [REF_VW_STAT_CD]              [varchar]     (30) NOT NULL,
    [REF_VWR_NM]                  [nvarchar]   (120) NULL,
    [REF_VWR_POSIT_CD]            [varchar]     (30) NULL,
    [REF_VWR_POSIT_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_TITLE_CD]            [varchar]     (30) NULL,
    [REF_VWR_TITLE_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_DEPT_ID]             [varchar]     (30) NULL,
    [REF_VWR_DEPT_NM]             [nvarchar]   (120) NULL,
    [REF_VW_DT]                   [datetime]         NULL,
    [REF_VW_OPIN_CONT]            [nvarchar]  (2000) NULL,
    [REG_DT]                      [datetime]         NULL,
    [VW_DT]                       [datetime]         NULL,
    [REF_VW_FIXD_APVR_YN]         [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_REF_VW_D] PRIMARY KEY NONCLUSTERED (
    [APV_NO] ASC,
    [REF_VWR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서참조열람상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VW_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VW_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_D', @level2Type = N'Column', @Level2Name = 'REF_VW_FIXD_APVR_YN';
GO

CREATE INDEX IDX_AP_ONGD_REF_VW_D1 ON dbo.AP_ONGD_REF_VW_D (REF_VWR_UID, REF_VW_STAT_CD, APV_NO);
GO


--DROP TABLE [dbo].[AP_ONGD_REF_VW_H];
CREATE TABLE [dbo].[AP_ONGD_REF_VW_H](
    [APV_NO]                      [bigint]           NOT NULL,
    [REGR_UID]                    [varchar]     (30) NOT NULL,
    [REF_VWR_UID]                 [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [REF_VW_STAT_CD]              [varchar]     (30) NOT NULL,
    [REF_VWR_NM]                  [nvarchar]   (120) NULL,
    [REF_VWR_POSIT_CD]            [varchar]     (30) NULL,
    [REF_VWR_POSIT_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_TITLE_CD]            [varchar]     (30) NULL,
    [REF_VWR_TITLE_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_DEPT_ID]             [varchar]     (30) NULL,
    [REF_VWR_DEPT_NM]             [nvarchar]   (120) NULL,
    [REF_VW_DT]                   [datetime]         NULL,
    [REF_VW_OPIN_CONT]            [nvarchar]  (2000) NULL,
    [REG_DT]                      [datetime]         NULL,
    [VW_DT]                       [datetime]         NULL,
    [REF_VW_FIXD_APVR_YN]         [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_REF_VW_H] PRIMARY KEY CLUSTERED (
    [APV_NO] ASC,
    [REGR_UID] ASC,
    [REF_VWR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서참조열람이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VW_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VW_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_H', @level2Type = N'Column', @Level2Name = 'REF_VW_FIXD_APVR_YN';
GO


--DROP TABLE [dbo].[AP_ONGD_REF_VW_E];
CREATE TABLE [dbo].[AP_ONGD_REF_VW_E](
    [APV_NO]                      [bigint]           NOT NULL,
    [REGR_UID]                    [varchar]     (30) NOT NULL,
    [REF_VWR_UID]                 [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    [REF_VW_STAT_CD]              [varchar]     (30) NOT NULL,
    [REF_VWR_NM]                  [nvarchar]   (120) NULL,
    [REF_VWR_POSIT_CD]            [varchar]     (30) NULL,
    [REF_VWR_POSIT_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_TITLE_CD]            [varchar]     (30) NULL,
    [REF_VWR_TITLE_NM]            [nvarchar]   (120) NULL,
    [REF_VWR_DEPT_ID]             [varchar]     (30) NULL,
    [REF_VWR_DEPT_NM]             [nvarchar]   (120) NULL,
    [REF_VW_DT]                   [datetime]         NULL,
    [REF_VW_OPIN_CONT]            [nvarchar]  (2000) NULL,
    [REG_DT]                      [datetime]         NULL,
    [VW_DT]                       [datetime]         NULL,
    [REF_VW_FIXD_APVR_YN]         [char]         (1) NULL,
    CONSTRAINT [PK_AP_ONGD_REF_VW_E] PRIMARY KEY NONCLUSTERED (
    [APV_NO] ASC,
    [REF_VWR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서참조열람이행', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VW_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직위명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_POSIT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자직책명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_TITLE_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람자부서명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VWR_DEPT_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람의견내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VW_OPIN_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'조회일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'VW_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'참조열람고정결재자여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_REF_VW_E', @level2Type = N'Column', @Level2Name = 'REF_VW_FIXD_APVR_YN';
GO

CREATE INDEX IDX_AP_ONGD_REF_VW_E1 ON dbo.AP_ONGD_REF_VW_E (REF_VWR_UID, REF_VW_STAT_CD, APV_NO);
GO


--DROP TABLE [dbo].[AP_FORM_BX_DEPT_R];
CREATE TABLE [dbo].[AP_FORM_BX_DEPT_R](
    [FORM_BX_ID]                  [varchar]     (30) NOT NULL,
    [FORM_BX_DEPT_ID]             [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NOT NULL,
    CONSTRAINT [PK_AP_FORM_BX_DEPT_R] PRIMARY KEY CLUSTERED (
    [FORM_BX_ID] ASC,
    [FORM_BX_DEPT_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식함부서관계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_BX_DEPT_R';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식함ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_BX_DEPT_R', @level2Type = N'Column', @Level2Name = 'FORM_BX_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_BX_DEPT_R', @level2Type = N'Column', @Level2Name = 'FORM_BX_DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_BX_DEPT_R', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
GO


--DROP TABLE [dbo].[OR_USER_PW_LOST_D];
CREATE TABLE [dbo].[OR_USER_PW_LOST_D](
    [LOST_ID]                     [varchar]     (40) NOT NULL,
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [LANG_TYP_CD]                 [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_OR_USER_PW_LOST_D] PRIMARY KEY CLUSTERED (
    [LOST_ID] ASC,
    [ODUR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자비밀번호분실상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_PW_LOST_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'분실ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_PW_LOST_D', @level2Type = N'Column', @Level2Name = 'LOST_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_PW_LOST_D', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'언어구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_PW_LOST_D', @level2Type = N'Column', @Level2Name = 'LANG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'OR_USER_PW_LOST_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO


/****** Object:  Table [dbo].[WA_CAR_IMG_D]    Script Date: 2017/01/04 14:21 ******/
CREATE TABLE [dbo].[WA_CAR_IMG_D](
[CAR_KND_NO] INT NOT NULL,
[IMG_PATH] VARCHAR(240) NOT NULL,
[IMG_WDTH] INT NOT NULL,
[IMG_HGHT] INT NOT NULL,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WA_CAR_IMG_D] PRIMARY KEY CLUSTERED
(
[CAR_KND_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'차종번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'CAR_KND_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'가로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'세로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_IMG_D'
GO

/****** Object:  Table [dbo].[WA_CAR_KND_L]    Script Date: 2017/01/04 14:21 ******/
CREATE TABLE [dbo].[WA_CAR_KND_L](
[CAR_KND_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CORP_NO] INT NOT NULL,
[CAR_KND_NM] NVARCHAR(120) NOT NULL,
[CAR_REG_NO] NVARCHAR(60) NOT NULL,
[CONT] NVARCHAR(300),
[SORT_ORDR] INT NOT NULL,
CONSTRAINT [PK_WA_CAR_KND_L] PRIMARY KEY CLUSTERED
(
[CAR_KND_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'차종번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'CAR_KND_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법인번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'CORP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'차종명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'CAR_KND_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자동차등록번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'CAR_REG_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'내용' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'CONT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'정렬순서' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L', @level2type=N'COLUMN',@level2name=N'SORT_ORDR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'차종목록' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CAR_KND_L'
GO

/****** Object:  Table [dbo].[WA_CORP_B]    Script Date: 2017/01/04 14:21 ******/
CREATE TABLE [dbo].[WA_CORP_B](
[CORP_NO] INT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CORP_NM] NVARCHAR(120) NOT NULL,
[CORP_REG_NO] NVARCHAR(60) NOT NULL,
CONSTRAINT [PK_WA_CORP_B] PRIMARY KEY CLUSTERED
(
[CORP_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법인번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CORP_B', @level2type=N'COLUMN',@level2name=N'CORP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CORP_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법인명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CORP_B', @level2type=N'COLUMN',@level2name=N'CORP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사업자등록번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CORP_B', @level2type=N'COLUMN',@level2name=N'CORP_REG_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법인기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_CORP_B'
GO

/****** Object:  Table [dbo].[WA_DRIV_REC_B]    Script Date: 2017/01/04 14:21 ******/
CREATE TABLE [dbo].[WA_DRIV_REC_B](
[REC_NO] BIGINT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[CORP_NO] INT NOT NULL,
[CAR_KND_NO] INT NOT NULL,
[STRT_DT] DATETIME NOT NULL,
[END_DT] DATETIME NOT NULL,
[BODY_XML] NTEXT NOT NULL,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_WA_DRIV_REC_B] PRIMARY KEY CLUSTERED
(
[REC_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기록번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'REC_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법인번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'CORP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'차종번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'CAR_KND_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시작일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'STRT_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'종료일' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'END_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'본문XML' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'BODY_XML'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'운행기록기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WA_DRIV_REC_B'
GO

/*
 *   이노비즈 운영서버만 적용
 * 
--DROP TABLE [dbo].[PT_PUSH_APP_D];
CREATE TABLE [dbo].[PT_PUSH_APP_D](
    [CUST_CD]                     [varchar]     (30) NOT NULL,
    [SEQ]                         [int]              NOT NULL,
    [CUST_NM]                     [nvarchar]   (200) NULL,
    [STRT_YMD]                    [datetime]         NULL,
    [END_YMD]                     [datetime]         NULL,
    [IPA_PATH]                    [varchar]    (240) NULL,
    [APK_PATH]                    [varchar]    (240) NULL,
    [DISP_NM]                     [nvarchar]   (200) NULL,
    [DESC_CONT]                   [nvarchar]  (2000) NULL,
    [REG_DT]                      [datetime]         NULL,
    [USE_YN]                      [char]         (1) NULL,
    CONSTRAINT [PK_PT_PUSH_APP_D] PRIMARY KEY CLUSTERED (
    [CUST_CD] ASC,
    [SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'푸쉬앱상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'CUST_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'CUST_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'IPA경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'IPA_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'APK경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'APK_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'표시명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'DISP_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'설명내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'DESC_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_PUSH_APP_D', @level2Type = N'Column', @Level2Name = 'USE_YN';
GO
*/


/****** Object:  Table [dbo].[WC_SCHDL_DEPT_R]    Script Date: 2017/06/05 11:34 ******/
CREATE TABLE [dbo].[WC_SCHDL_DEPT_R](
[SCHDL_ID] VARCHAR(30) NOT NULL,
[ORG_ID] VARCHAR(30) NOT NULL,
CONSTRAINT [PK_WC_SCHDL_DEPT_R] PRIMARY KEY CLUSTERED
(
[SCHDL_ID] ASC,
[ORG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'일정ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_DEPT_R', @level2type=N'COLUMN',@level2name=N'SCHDL_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'조직ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_DEPT_R', @level2type=N'COLUMN',@level2name=N'ORG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서일정관계' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WC_SCHDL_DEPT_R'
GO


--DROP TABLE [dbo].[AP_FORM_XML_D];
CREATE TABLE [dbo].[AP_FORM_XML_D](
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_XML_CD]                 [varchar]     (30) NOT NULL,
    [FORM_XML_CONT]               [ntext]            NULL,
    CONSTRAINT [PK_AP_FORM_XML_D] PRIMARY KEY CLUSTERED (
    [FORM_ID] ASC,
    [FORM_XML_CD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식XML상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_XML_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식XML코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_XML_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식XML내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_XML_CONT';
GO

--DROP TABLE [dbo].[AP_ONGO_FORM_XML_D];
CREATE TABLE [dbo].[AP_ONGO_FORM_XML_D](
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [FORM_XML_CD]                 [varchar]     (30) NOT NULL,
    [FORM_XML_CONT]               [ntext]            NULL,
    CONSTRAINT [PK_AP_ONGO_FORM_XML_D] PRIMARY KEY CLUSTERED (
    [FORM_ID] ASC,
    [FORM_XML_CD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행양식XML상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_XML_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식XML코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_XML_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식XML내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGO_FORM_XML_D', @level2Type = N'Column', @Level2Name = 'FORM_XML_CONT';
GO


/****** Object:  Table [dbo].[WV_SURV_POPUP_D]    Script Date: 2017/11/07 15:11 ******/
CREATE TABLE [dbo].[WV_SURV_POPUP_D](
[SURV_ID] VARCHAR(30) NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[WDTH_VA] INT,
[USE_YN] CHAR(1) NOT NULL,
[MODR_UID] VARCHAR(30) NOT NULL,
[MOD_DT] DATETIME NOT NULL,
CONSTRAINT [PK_WV_SURV_POPUP_D] PRIMARY KEY CLUSTERED
(
[SURV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'설문ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'SURV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'넓이값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'WDTH_VA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사용여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'USE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팝업설정상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WV_SURV_POPUP_D'
GO


--DROP TABLE [dbo].[PT_SSO_T];
CREATE TABLE [dbo].[PT_SSO_T](
    [SSO_ID]                      [varchar]     (30) NOT NULL,
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [LANG_TYP_CD]                 [varchar]     (30) NOT NULL,
    [TGT_URL]                     [varchar]    (100) NULL,
    [AUTH_URL]                    [varchar]    (100) NULL,
    [REG_DT]                      [datetime]         NOT NULL,
    CONSTRAINT [PK_PT_SSO_T] PRIMARY KEY NONCLUSTERED (
    [SSO_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'SSO임시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'SSO ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'SSO_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'언어구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'LANG_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대상URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'TGT_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한URL', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'AUTH_URL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_T', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO


--DROP TABLE [dbo].[PT_SSO_MSG_T];
CREATE TABLE [dbo].[PT_SSO_MSG_T](
    [MSG_ID]                      [varchar]     (30) NOT NULL,
    [MSG_VA]                      [nvarchar]   (500) NOT NULL,
    [REG_DT]                      [datetime]         NOT NULL,
    CONSTRAINT [PK_PT_SSO_MSG_T] PRIMARY KEY NONCLUSTERED (
    [MSG_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'SSO메세지임시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_MSG_T';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메세지ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_MSG_T', @level2Type = N'Column', @Level2Name = 'MSG_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'메세지값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_MSG_T', @level2Type = N'Column', @Level2Name = 'MSG_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'PT_SSO_MSG_T', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO


--DROP TABLE [dbo].[AP_ONGD_CNCL_D];
CREATE TABLE [dbo].[AP_ONGD_CNCL_D](
    [MAKR_UID]                    [varchar]     (30) NOT NULL,
    [XML_TYP_ID]                  [varchar]     (30) NOT NULL,
    [APV_NO]                      [bigint]           NOT NULL,
    CONSTRAINT [PK_AP_ONGD_CNCL_D] PRIMARY KEY CLUSTERED (
    [MAKR_UID] ASC,
    [XML_TYP_ID] ASC,
    [APV_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행문서취소상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_CNCL_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기안자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_CNCL_D', @level2Type = N'Column', @Level2Name = 'MAKR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'XML구분ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_CNCL_D', @level2Type = N'Column', @Level2Name = 'XML_TYP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_ONGD_CNCL_D', @level2Type = N'Column', @Level2Name = 'APV_NO';
GO


/****** Object:  Table [dbo].[WC_WORK_SCHDL_B]    Script Date: 2018-12-18 12:46:46 ******/
CREATE TABLE [dbo].[WC_WORK_SCHDL_B](
	[SCHDL_ID]	[VARCHAR](30)	NOT NULL,
	[COMP_ID]	[VARCHAR](3)	NOT NULL,
	[USER_UID]	[VARCHAR](30)	NOT NULL,
	[SCHDL_TYP_CD]	[VARCHAR](30)	NOT NULL,
	[SUBJ]	[NVARCHAR](120)	NOT NULL,
	[CONT]	[NTEXT],
	[STRT_DT]	[DATETIME]	NOT NULL,
	[END_DT]	[DATETIME]	NOT NULL,
	[ALLDAY_YN]	[CHAR](1),
	[REGR_UID]	[VARCHAR](30),
	[REG_DT]	[DATETIME],
	[MODR_UID]	[VARCHAR](30),
	[MOD_DT]	[DATETIME],
CONSTRAINT [PK_WC_WORK_SCHDL_B] PRIMARY KEY CLUSTERED 
(
	[SCHDL_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'근태일정', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일정ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'SCHDL_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일정구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'SCHDL_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'제목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'SUBJ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'STRT_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'END_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종일여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'ALLDAY_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'MODR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WC_WORK_SCHDL_B', @level2Type = N'Column', @Level2Name = 'MOD_DT';
GO


-- 2019-01-31_결재_양식JSP상세_추가_MSSQL.txt
--DROP TABLE [dbo].[AP_FORM_JSP_D];
CREATE TABLE [dbo].[AP_FORM_JSP_D](
    [JSP_ID]                      [varchar]     (30) NOT NULL,
    [FORM_ID]                     [varchar]     (30) NOT NULL,
    [JSP_PATH]                    [varchar]    (240) NULL,
    CONSTRAINT [PK_AP_FORM_JSP_D] PRIMARY KEY NONCLUSTERED (
    [JSP_ID] ASC,
    [FORM_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식JSP상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_JSP_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'JSP ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_JSP_D', @level2Type = N'Column', @Level2Name = 'JSP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'양식ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_JSP_D', @level2Type = N'Column', @Level2Name = 'FORM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'JSP 경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'AP_FORM_JSP_D', @level2Type = N'Column', @Level2Name = 'JSP_PATH';
GO

