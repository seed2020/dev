
/*
 * 이노비즈에만 적용 할 것
 * 
 * */

INSERT INTO CM_SEQ_B VALUES ('WP_PRJ_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WP_MP_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('WP_PRJ_GRP_B', '1296');


--DROP TABLE [dbo].[WP_PRJ_B];
CREATE TABLE [dbo].[WP_PRJ_B](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [PRJ_CD]                      [varchar]     (30) NOT NULL,
    [PRJ_NM]                      [nvarchar]   (200) NULL,
    [GRP_ID]                      [varchar]     (30) NULL,
    [STRT_YMD]                    [datetime]         NULL,
    [END_YMD]                     [datetime]         NULL,
    [CMPL_YMD]                    [datetime]         NULL,
    [PRJ_MM]                      [numeric]    (5,1) NULL,
    [PRJ_AMT]                     [bigint]           NULL,
    [PM_ID]                       [varchar]     (30) NULL,
    [PM_MP_TYP_CD]                [varchar]     (30) NULL,
    [SMRY]                        [varchar]    (800) NULL,
    [CUST_NM]                     [varchar]     (60) NULL,
    [CUST_ZIP_NO]                 [varchar]     (30) NULL,
    [CUST_ADR]                    [varchar]    (400) NULL,
    [PRJ_STAT_CD]                 [varchar]     (30) NULL,
    [MOD_STAT_CD]                 [varchar]     (30) NULL,
    [MOD_CONT]                    [varchar]    (800) NULL,
    [REJT_RSON]                   [varchar]    (800) NULL,
    [REG_DT]                      [datetime]         NULL,
    [REG_UID]                     [varchar]     (30) NULL,
    CONSTRAINT [PK_WP_PRJ_B] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'CMPL_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트맨먼스', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_MM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트금액', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_AMT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'피엠ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'피엠인력구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PM_MP_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개요', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'SMRY';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'CUST_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객우편번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'CUST_ZIP_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객주소', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'CUST_ADR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'PRJ_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'변경상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'MOD_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'MOD_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'반려사유', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'REJT_RSON';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_B', @level2Type = N'Column', @Level2Name = 'REG_UID';
GO

CREATE INDEX IDX_WP_PRJ_B1 ON dbo.WP_PRJ_B (PRJ_CD);
GO

--DROP TABLE [dbo].[WP_PRJ_BH];
CREATE TABLE [dbo].[WP_PRJ_BH](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [PRJ_CD]                      [varchar]     (30) NOT NULL,
    [PRJ_NM]                      [nvarchar]   (200) NULL,
    [GRP_ID]                      [varchar]     (30) NULL,
    [STRT_YMD]                    [datetime]         NULL,
    [END_YMD]                     [datetime]         NULL,
    [CMPL_YMD]                    [datetime]         NULL,
    [PRJ_MM]                      [numeric]    (5,1) NULL,
    [PRJ_AMT]                     [bigint]           NULL,
    [PM_ID]                       [varchar]     (30) NULL,
    [PM_MP_TYP_CD]                [varchar]     (30) NULL,
    [SMRY]                        [varchar]    (800) NULL,
    [CUST_NM]                     [varchar]     (60) NULL,
    [CUST_ZIP_NO]                 [varchar]     (30) NULL,
    [CUST_ADR]                    [varchar]    (400) NULL,
    [PRJ_STAT_CD]                 [varchar]     (30) NULL,
    [MOD_STAT_CD]                 [varchar]     (30) NULL,
    [MOD_CONT]                    [varchar]    (800) NULL,
    [REJT_RSON]                   [varchar]    (800) NULL,
    [REG_DT]                      [datetime]         NULL,
    [REG_UID]                     [varchar]     (30) NULL,
    CONSTRAINT [PK_WP_PRJ_BH] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [VER] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트기본이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시작년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'STRT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'종료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'END_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'완료년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'CMPL_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트맨먼스', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_MM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트금액', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_AMT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'피엠ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PM_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'피엠인력구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PM_MP_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개요', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'SMRY';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'CUST_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객우편번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'CUST_ZIP_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객주소', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'CUST_ADR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'PRJ_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'변경상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'MOD_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'수정내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'MOD_CONT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'반려사유', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'REJT_RSON';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_BH', @level2Type = N'Column', @Level2Name = 'REG_UID';
GO

--DROP TABLE [dbo].[WP_PRJ_GRP_B];
CREATE TABLE [dbo].[WP_PRJ_GRP_B](
    [GRP_ID]                      [varchar]     (30) NOT NULL,
    [GRP_NM]                      [nvarchar]   (120) NOT NULL,
    CONSTRAINT [PK_WP_PRJ_GRP_B] PRIMARY KEY CLUSTERED (
    [GRP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'그룹기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_GRP_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'그룹ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_GRP_B', @level2Type = N'Column', @Level2Name = 'GRP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'그룹명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_GRP_B', @level2Type = N'Column', @Level2Name = 'GRP_NM';
GO

--DROP TABLE [dbo].[WP_PRJ_PICH_B];
CREATE TABLE [dbo].[WP_PRJ_PICH_B](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [SEQ]                         [int]              NOT NULL,
    [PICH_NM]                     [varchar]     (30) NULL,
    [PICH_GRADE]                  [varchar]     (60) NULL,
    [PICH_PHON]                   [varchar]     (50) NULL,
    [PICH_EMAIL]                  [varchar]     (50) NULL,
    [NOTE]                        [varchar]    (400) NULL,
    CONSTRAINT [PK_WP_PRJ_PICH_B] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트담당자상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'PICH_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자직급', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'PICH_GRADE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자전화번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'PICH_PHON';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자이메일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'PICH_EMAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_PICH_B', @level2Type = N'Column', @Level2Name = 'NOTE';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_PLAN_D];
CREATE TABLE [dbo].[WP_PRJ_MP_PLAN_D](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [MP_TYP_CD]                   [varchar]     (30) NULL,
    [PRJ_ROLE1_CD]                [varchar]     (30) NULL,
    [PRJ_ROLE2_CD]                [varchar]     (30) NULL,
    [SORT_ORDR]                   [varchar]     (30) NULL,
    [MP_MM_SUM]                   [numeric]    (5,1) NULL,
    CONSTRAINT [PK_WP_PRJ_MP_PLAN_D] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [MP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력계획상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'MP_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트역할1코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'PRJ_ROLE1_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트역할2코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'PRJ_ROLE2_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력멘먼스합계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_D', @level2Type = N'Column', @Level2Name = 'MP_MM_SUM';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_PLAN_DH];
CREATE TABLE [dbo].[WP_PRJ_MP_PLAN_DH](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [MP_TYP_CD]                   [varchar]     (30) NULL,
    [PRJ_ROLE1_CD]                [varchar]     (30) NULL,
    [PRJ_ROLE2_CD]                [varchar]     (30) NULL,
    [SORT_ORDR]                   [varchar]     (30) NULL,
    [MP_MM_SUM]                   [numeric]    (5,1) NULL,
    CONSTRAINT [PK_WP_PRJ_MP_PLAN_DH] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [VER] ASC,
    [MP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력계획상세이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'MP_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트역할1코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'PRJ_ROLE1_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트역할2코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'PRJ_ROLE2_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력멘먼스합계', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_DH', @level2Type = N'Column', @Level2Name = 'MP_MM_SUM';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_PLAN_L];
CREATE TABLE [dbo].[WP_PRJ_MP_PLAN_L](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [M_NO]                        [int]              NOT NULL,
    [PLAN_MM]                     [numeric]    (5,1) NULL,
    [PLAN_YM]                     [datetime]         NULL,
    CONSTRAINT [PK_WP_PRJ_MP_PLAN_L] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [MP_ID] ASC,
    [M_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력계획내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'월번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'M_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'계획멘먼스', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'PLAN_MM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'계획년월', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_L', @level2Type = N'Column', @Level2Name = 'PLAN_YM';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_PLAN_LH];
CREATE TABLE [dbo].[WP_PRJ_MP_PLAN_LH](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [VER]                         [numeric]    (5,1) NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [M_NO]                        [int]              NOT NULL,
    [PLAN_MM]                     [numeric]    (5,1) NULL,
    [PLAN_YM]                     [datetime]         NULL,
    CONSTRAINT [PK_WP_PRJ_MP_PLAN_LH] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [VER] ASC,
    [MP_ID] ASC,
    [M_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력계획내역이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'월번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'M_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'계획멘먼스', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'PLAN_MM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'계획년월', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_PLAN_LH', @level2Type = N'Column', @Level2Name = 'PLAN_YM';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_RSLT_D];
CREATE TABLE [dbo].[WP_PRJ_MP_RSLT_D](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [RSLT_YM]                     [datetime]         NOT NULL,
    [RSLT_MM]                     [numeric]    (5,1) NULL,
    [M_MD]                        [int]              NULL,
    CONSTRAINT [PK_WP_PRJ_MP_RSLT_D] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [MP_ID] ASC,
    [RSLT_YM] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력결과상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결과년월', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D', @level2Type = N'Column', @Level2Name = 'RSLT_YM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결과맨먼스', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D', @level2Type = N'Column', @Level2Name = 'RSLT_MM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'월멘데이', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_D', @level2Type = N'Column', @Level2Name = 'M_MD';
GO

--DROP TABLE [dbo].[WP_PRJ_MP_RSLT_L];
CREATE TABLE [dbo].[WP_PRJ_MP_RSLT_L](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [RSLT_YMD]                    [datetime]         NOT NULL,
    [RSLT_MD]                     [numeric]    (5,1) NULL,
    [NOTE]                        [varchar]    (200) NULL,
    CONSTRAINT [PK_WP_PRJ_MP_RSLT_L] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [MP_ID] ASC,
    [RSLT_YMD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트인력결과내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결과년월일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L', @level2Type = N'Column', @Level2Name = 'RSLT_YMD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결과맨데이', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L', @level2Type = N'Column', @Level2Name = 'RSLT_MD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_MP_RSLT_L', @level2Type = N'Column', @Level2Name = 'NOTE';
GO

--DROP TABLE [dbo].[WP_PRJ_ATT_FILE_D];
CREATE TABLE [dbo].[WP_PRJ_ATT_FILE_D](
    [PRJ_NO]                      [bigint]           NOT NULL,
    [ATT_SEQ]                     [int]              NOT NULL,
    [DISP_NM]                     [nvarchar]   (240) NOT NULL,
    [FILE_EXT]                    [varchar]     (30) NULL,
    [FILE_SIZE]                   [int]              NULL,
    [SAVE_PATH]                   [varchar]    (240) NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_WP_PRJ_ATT_FILE_D] PRIMARY KEY CLUSTERED (
    [PRJ_NO] ASC,
    [ATT_SEQ] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트첨부파일상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로잭트번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'PRJ_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'첨부일련번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'ATT_SEQ';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'첨부표시명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'DISP_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'파일확장자', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'FILE_EXT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'파일크기', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'FILE_SIZE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'SAVE_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_PRJ_ATT_FILE_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO

--DROP TABLE [dbo].[WP_MP_B];
CREATE TABLE [dbo].[WP_MP_B](
    [MP_ID]                       [varchar]     (30) NOT NULL,
    [MP_NM]                       [varchar]     (30) NOT NULL,
    [MP_GRADE]                    [varchar]     (60) NULL,
    [MP_PHONE]                    [varchar]     (50) NOT NULL,
    [MP_EMAIL]                    [varchar]     (50) NOT NULL,
    [REG_DT]                      [datetime]         NULL,
    [REG_UID]                     [varchar]     (30) NULL,
    CONSTRAINT [PK_WP_MP_B] PRIMARY KEY CLUSTERED (
    [MP_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'MP_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'MP_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력직급', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'MP_GRADE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력전화번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'MP_PHONE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'인력이메일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'MP_EMAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WP_MP_B', @level2Type = N'Column', @Level2Name = 'REG_UID';
GO




