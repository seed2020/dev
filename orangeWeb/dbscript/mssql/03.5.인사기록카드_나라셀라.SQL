/****** Object:  Table [dbo].[CU_PSN_INFO_D]    Script Date: 2017/02/03 12:54 ******/
CREATE TABLE [dbo].[CU_PSN_INFO_D](
[CARD_NO] BIGINT NOT NULL,
[SSN_ENC] VARCHAR(40),
[BIRTH_ENC] VARCHAR(40),
[HOME_ZIP_NO_ENC] VARCHAR(80),
[HOME_ADR_ENC] VARCHAR(400),
[HOME_DETL_ADR_ENC] VARCHAR(400),
[MBNO_ENC] VARCHAR(120),
CONSTRAINT [PK_CU_PSN_INFO_D] PRIMARY KEY CLUSTERED
(
[CARD_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카드번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'CARD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'주민등록번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'SSN_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'생년월일암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'BIRTH_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택우편번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'HOME_ZIP_NO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'HOME_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'자택상세주소암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'HOME_DETL_ADR_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'휴대전화번호암호값' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D', @level2type=N'COLUMN',@level2name=N'MBNO_ENC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인사정보상세' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_INFO_D'
GO

/****** Object:  Table [dbo].[CU_PSN_CARD_B]    Script Date: 2017/02/03 12:54 ******/
CREATE TABLE [dbo].[CU_PSN_CARD_B](
[CARD_NO] BIGINT NOT NULL,
[COMP_ID] VARCHAR(3) NOT NULL,
[EIN] VARCHAR(30),
[COMP_NM] NVARCHAR(120),
[DEPT_NM] NVARCHAR(120),
[JOIN_DT] DATETIME,
[TEAM_NM] NVARCHAR(120),
[POSIT_NM] NVARCHAR(60),
[KO_NM] NVARCHAR(30),
[ZN_NM] NVARCHAR(30),
[EN_NM] NVARCHAR(60),
[BODY_XML] NTEXT,
[REGR_UID] VARCHAR(30),
[REG_DT] DATETIME,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_CU_PSN_CARD_B] PRIMARY KEY CLUSTERED
(
[CARD_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카드번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'CARD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'회사ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'COMP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'사번' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'EIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'소속법인' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'COMP_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'부서명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'DEPT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'입사일자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'JOIN_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'팀명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'TEAM_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'직위명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'POSIT_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'한글명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'KO_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'한문명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'ZN_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'영문명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'EN_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'본문XML' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'BODY_XML'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'REGR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'등록일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'REG_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자UID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'인사기록카드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_CARD_B'
GO

/****** Object:  Table [dbo].[CU_PSN_IMG_D]    Script Date: 2017/02/03 12:54 ******/
CREATE TABLE [dbo].[CU_PSN_IMG_D](
[CARD_NO] BIGINT NOT NULL,
[IMG_PATH] VARCHAR(240) NOT NULL,
[IMG_WDTH] INT NOT NULL,
[IMG_HGHT] INT NOT NULL,
[MODR_UID] VARCHAR(30),
[MOD_DT] DATETIME,
CONSTRAINT [PK_CU_PSN_IMG_D] PRIMARY KEY CLUSTERED
(
[CARD_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'카드번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'CARD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지경로' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_PATH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'가로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_WDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'세로길이' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'IMG_HGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정자' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'MODR_UID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'수정일시' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D', @level2type=N'COLUMN',@level2name=N'MOD_DT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'이미지' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'CU_PSN_IMG_D'
GO

INSERT INTO CM_SEQ_B VALUES ('CU_PSN_CARD_B', '1');
