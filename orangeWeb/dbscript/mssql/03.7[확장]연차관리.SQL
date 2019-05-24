

--DROP TABLE [dbo].[WD_ANB_B];
CREATE TABLE [dbo].[WD_ANB_B](
    [YEAR]                        [int]              NOT NULL,
    [ANB_TYP_CD]                  [varchar]     (30) NOT NULL,
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [COMP_ID]                     [varchar]      (3) NULL,
    [FORW_CNT]                    [numeric]    (5,1) NULL,
    [FORW_MOD_CNT]                [numeric]    (5,1) NULL,
    [CRE_CNT]                     [numeric]    (5,1) NULL,
    [CRE_MOD_CNT]                 [numeric]    (5,1) NULL,
    [USE_CNT]                     [numeric]    (5,1) NULL,
    [USE_MOD_CNT]                 [numeric]    (5,1) NULL,
    [ONGO_CNT]                    [numeric]    (5,1) NULL,
    [ONGO_MOD_CNT]                [numeric]    (5,1) NULL,
    [CRE_YEAR]                    [int]              NULL,
    CONSTRAINT [PK_WD_ANB_B] PRIMARY KEY CLUSTERED (
    [YEAR] ASC,
    [ANB_TYP_CD] ASC,
    [ODUR_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기준년도', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'YEAR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'ANB_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'회사ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'COMP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이월수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'FORW_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이월차감수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'FORW_MOD_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발생수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'CRE_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발생차감수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'CRE_MOD_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'USE_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용차감수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'USE_MOD_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'ONGO_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'진행차감수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'ONGO_MOD_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'발생년도', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_B', @level2Type = N'Column', @Level2Name = 'CRE_YEAR';
GO

CREATE INDEX IDX_WD_ANB_B1 ON dbo.WD_ANB_B (ODUR_UID, ANB_TYP_CD);
GO


--DROP TABLE [dbo].[WD_ANB_USE_L];
CREATE TABLE [dbo].[WD_ANB_USE_L](
    [YEAR]                        [int]              NOT NULL,
    [ANB_TYP_CD]                  [varchar]     (30) NOT NULL,
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [USE_YMD]                     [datetime]         NOT NULL,
    [CMPL_YN]                     [char]         (1) NULL,
    [USE_CNT]                     [numeric]    (5,1) NULL,
    [REG_DT]                      [datetime]         NULL,
    [APV_NO]                      [bigint]           NULL,
    [RSON]                        [varchar]    (400) NULL,
    CONSTRAINT [PK_WD_ANB_USE_L] PRIMARY KEY CLUSTERED (
    [YEAR] ASC,
    [ANB_TYP_CD] ASC,
    [ODUR_UID] ASC,
    [USE_YMD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차사용내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기준년도', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'YEAR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'ANB_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'USE_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완결여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'CMPL_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'USE_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'근거', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_USE_L', @level2Type = N'Column', @Level2Name = 'RSON';
GO

CREATE INDEX IDX_WD_ANB_USE_L1 ON dbo.WD_ANB_USE_L (ODUR_UID, USE_YMD);
GO


--DROP TABLE [dbo].[WD_ANB_MOD_L];
CREATE TABLE [dbo].[WD_ANB_MOD_L](
    [YEAR]                        [int]              NOT NULL,
    [ANB_TYP_CD]                  [varchar]     (30) NOT NULL,
    [ODUR_UID]                    [varchar]     (30) NOT NULL,
    [SEQ]                         [int]              NOT NULL,
    [MOD_TYP_CD]                  [varchar]     (30) NULL,
    [MOD_CNT]                     [numeric]    (5,1) NULL,
    [USE_YMD]                     [datetime]         NULL,
    [MONTH]                       [int]              NULL,
    [APV_NO]                      [bigint]           NULL,
    [RSON]                        [varchar]    (400) NULL,
    [MOD_UID]                     [varchar]     (30) NULL,
    [MOD_DT]                      [datetime]         NULL,
    [NOTE]                        [varchar]    (400) NULL,
    CONSTRAINT [PK_WD_ANB_MOD_L] PRIMARY KEY CLUSTERED (
    [YEAR] ASC,
    [ANB_TYP_CD] ASC,
    [ODUR_UID] ASC,
    [SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차차감내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'기준년도', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'YEAR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'연차구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'ANB_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원직자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'ODUR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차감구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'MOD_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차감수', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'MOD_CNT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'USE_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'월', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'MONTH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'근거', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'RSON';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차감자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'MOD_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차감일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'MOD_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WD_ANB_MOD_L', @level2Type = N'Column', @Level2Name = 'NOTE';
GO


