
INSERT INTO CM_SEQ_B VALUES ('WO_ONEC_B', '1');


--DROP TABLE [dbo].[WO_ONEC_B];
CREATE TABLE [dbo].[WO_ONEC_B](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [VER]                         [int]              NOT NULL,
    [ONEC_TYP_CD]                 [varchar]     (30) NULL,
    [FREQ_CD]                     [varchar]     (30) NULL,
    [ITEM_NM]                     [varchar]    (200) NOT NULL,
    [ISS_DT]                      [datetime]         NULL,
    [REVS_DT]                     [datetime]         NULL,
    [DUE_DT]                      [datetime]         NULL,
    [HOLDR_UID]                   [varchar]     (30) NULL,
    [BRND_NM]                     [varchar]    (200) NULL,
    [CAT_CD]                      [varchar]     (30) NULL,
    [ORGN_CD]                     [varchar]     (30) NULL,
    [MKET_SITU]                   [varchar]    (600) NULL,
    [CUST_ANAL]                   [varchar]    (600) NULL,
    [CMPTR_ANAL]                  [varchar]    (600) NULL,
    [BM_ITEM]                     [varchar]    (200) NULL,
    [BM_RET_PRC]                  [int]              NULL,
    [BM_SALS_PRC]                 [int]              NULL,
    [BM_SP]                       [numeric]    (5,1) NULL,
    [BM_SPEC_SIZE]                [varchar]    (600) NULL,
    [DIFF]                        [varchar]    (600) NULL,
    [SPEC]                        [varchar]    (600) NULL,
    [TGT_CUST]                    [varchar]    (600) NULL,
    [PACK]                        [varchar]    (200) NULL,
    [STORG_CD]                    [varchar]     (30) NULL,
    [POS_MY_X]                    [int]              NULL,
    [POS_MY_Y]                    [int]              NULL,
    [POS_OTHR_X]                  [int]              NULL,
    [POS_OTHR_Y]                  [int]              NULL,
    [POS_TGT_PRC]                 [int]              NULL,
    [POS_COST]                    [int]              NULL,
    [POS_SP]                      [numeric]    (5,1) NULL,
    [POS_REMK]                    [varchar]    (600) NULL,
    [TA_APV_YN]                   [char]         (1) NULL,
    [SF_3M_VO]                    [int]              NULL,
    [SF_6M_VO]                    [int]              NULL,
    [SF_1Y_VO]                    [int]              NULL,
    [SF_2Y_VO]                    [int]              NULL,
    [SF_3Y_VO]                    [int]              NULL,
    [SF_3M_SAIL]                  [int]              NULL,
    [SF_6M_SAIL]                  [int]              NULL,
    [SF_1Y_SAIL]                  [int]              NULL,
    [SF_2Y_SAIL]                  [int]              NULL,
    [SF_3Y_SAIL]                  [int]              NULL,
    [SF_REMK]                     [varchar]    (600) NULL,
    [PROM_PLAN]                   [varchar]    (600) NULL,
    [SAIL_PERFM]                  [varchar]    (600) NULL,
    [NOTE]                        [varchar]   (1200) NULL,
    [DEPT_ID]                     [varchar]     (30) NULL,
    [APVD_HIS_NO]                 [int]              NULL,
    [CUR_VER]                     [int]              NULL,
    [STAT_CD]                     [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [APV_NO]                      [bigint]           NULL,
    [APV_ASK_DT]                  [datetime]         NULL,
    [APV_CMPL_DT]                 [datetime]         NULL,
    CONSTRAINT [PK_WO_ONEC_B] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'ONEC_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'주기코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'FREQ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'품목명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'ITEM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'최초작성일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'ISS_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개정일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'REVS_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개발완료일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'DUE_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'홀더UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'HOLDR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'브랜드명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BRND_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'카테고리코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'CAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'출처코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'ORGN_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시장상황', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'MKET_SITU';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객분석', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'CUST_ANAL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'경쟁사분석', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'CMPTR_ANAL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크품목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BM_ITEM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크소비자가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BM_RET_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크판매가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BM_SALS_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크SP', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BM_SP';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'베치마크스펙사이즈', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'BM_SPEC_SIZE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차이점', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'DIFF';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SPEC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대상고객', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'TGT_CUST';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포장', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'PACK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장형태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'STORG_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션자사X', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_MY_X';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션자사Y', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_MY_Y';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션타사X', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_OTHR_X';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션타사Y', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_OTHR_Y';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션목표가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_TGT_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션원가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_COST';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션SP', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_SP';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'POS_REMK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'TA승인여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'TA_APV_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3개월볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_3M_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측6개월볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_6M_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측1년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_1Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측2년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_2Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_3Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3개월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_3M_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측6개월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_6M_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측1년월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_1Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측2년판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_2Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3년판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_3Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SF_REMK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로모션계획', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'PROM_PLAN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매활동', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'SAIL_PERFM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'NOTE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'승인된이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'APVD_HIS_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'현재버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'CUR_VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재요청일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'APV_ASK_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재완료일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_B', @level2Type = N'Column', @Level2Name = 'APV_CMPL_DT';
GO

CREATE INDEX IDX_WO_ONEC_B1 ON dbo.WO_ONEC_B (ONEC_TYP_CD);
CREATE INDEX IDX_WO_ONEC_B2 ON dbo.WO_ONEC_B (CAT_CD);
CREATE INDEX IDX_WO_ONEC_B3 ON dbo.WO_ONEC_B (ORGN_CD);
CREATE INDEX IDX_WO_ONEC_B4 ON dbo.WO_ONEC_B (STORG_CD);
CREATE INDEX IDX_WO_ONEC_B5 ON dbo.WO_ONEC_B (HOLDR_UID);
GO

--DROP TABLE [dbo].[WO_ONEC_BH];
CREATE TABLE [dbo].[WO_ONEC_BH](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [VER]                         [int]              NOT NULL,
    [ONEC_TYP_CD]                 [varchar]     (30) NULL,
    [FREQ_CD]                     [varchar]     (30) NULL,
    [ITEM_NM]                     [varchar]    (200) NOT NULL,
    [ISS_DT]                      [datetime]         NULL,
    [REVS_DT]                     [datetime]         NULL,
    [DUE_DT]                      [datetime]         NULL,
    [HOLDR_UID]                   [varchar]     (30) NULL,
    [BRND_NM]                     [varchar]    (200) NULL,
    [CAT_CD]                      [varchar]     (30) NULL,
    [ORGN_CD]                     [varchar]     (30) NULL,
    [MKET_SITU]                   [varchar]    (600) NULL,
    [CUST_ANAL]                   [varchar]    (600) NULL,
    [CMPTR_ANAL]                  [varchar]    (600) NULL,
    [BM_ITEM]                     [varchar]    (200) NULL,
    [BM_RET_PRC]                  [int]              NULL,
    [BM_SALS_PRC]                 [int]              NULL,
    [BM_SP]                       [numeric]    (5,1) NULL,
    [BM_SPEC_SIZE]                [varchar]    (600) NULL,
    [DIFF]                        [varchar]    (600) NULL,
    [SPEC]                        [varchar]    (600) NULL,
    [TGT_CUST]                    [varchar]    (600) NULL,
    [PACK]                        [varchar]    (200) NULL,
    [STORG_CD]                    [varchar]     (30) NULL,
    [POS_MY_X]                    [int]              NULL,
    [POS_MY_Y]                    [int]              NULL,
    [POS_OTHR_X]                  [int]              NULL,
    [POS_OTHR_Y]                  [int]              NULL,
    [POS_TGT_PRC]                 [int]              NULL,
    [POS_COST]                    [int]              NULL,
    [POS_SP]                      [numeric]    (5,1) NULL,
    [POS_REMK]                    [varchar]    (600) NULL,
    [TA_APV_YN]                   [char]         (1) NULL,
    [SF_3M_VO]                    [int]              NULL,
    [SF_6M_VO]                    [int]              NULL,
    [SF_1Y_VO]                    [int]              NULL,
    [SF_2Y_VO]                    [int]              NULL,
    [SF_3Y_VO]                    [int]              NULL,
    [SF_3M_SAIL]                  [int]              NULL,
    [SF_6M_SAIL]                  [int]              NULL,
    [SF_1Y_SAIL]                  [int]              NULL,
    [SF_2Y_SAIL]                  [int]              NULL,
    [SF_3Y_SAIL]                  [int]              NULL,
    [SF_REMK]                     [varchar]    (600) NULL,
    [PROM_PLAN]                   [varchar]    (600) NULL,
    [SAIL_PERFM]                  [varchar]    (600) NULL,
    [NOTE]                        [varchar]   (1200) NULL,
    [DEPT_ID]                     [varchar]     (30) NULL,
    [APVD_HIS_NO]                 [int]              NULL,
    [CUR_VER]                     [int]              NULL,
    [STAT_CD]                     [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    [APV_NO]                      [bigint]           NULL,
    [APV_ASK_DT]                  [datetime]         NULL,
    [APV_CMPL_DT]                 [datetime]         NULL,
    CONSTRAINT [PK_WO_ONEC_BH] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC,
    [VER] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드기본이력', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'ONEC_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'주기코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'FREQ_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'품목명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'ITEM_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'최초작성일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'ISS_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개정일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'REVS_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'개발완료일', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'DUE_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'홀더UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'HOLDR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'브랜드명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BRND_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'카테고리코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'CAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'출처코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'ORGN_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'시장상황', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'MKET_SITU';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'고객분석', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'CUST_ANAL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'경쟁사분석', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'CMPTR_ANAL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크품목', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BM_ITEM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크소비자가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BM_RET_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크판매가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BM_SALS_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'벤치마크SP', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BM_SP';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'베치마크스펙사이즈', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'BM_SPEC_SIZE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'차이점', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'DIFF';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SPEC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대상고객', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'TGT_CUST';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포장', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'PACK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'저장형태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'STORG_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션자사X', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_MY_X';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션자사Y', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_MY_Y';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션타사X', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_OTHR_X';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션타사Y', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_OTHR_Y';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션목표가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_TGT_PRC';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션원가', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_COST';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션SP', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_SP';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'포지션비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'POS_REMK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'TA승인여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'TA_APV_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3개월볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_3M_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측6개월볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_6M_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측1년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_1Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측2년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_2Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3년볼륨', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_3Y_VO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3개월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_3M_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측6개월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_6M_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측1년월판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_1Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측2년판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_2Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측3년판매', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_3Y_SAIL';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매예측비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SF_REMK';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'프로모션계획', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'PROM_PLAN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'판매활동', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'SAIL_PERFM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'NOTE';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부서ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'DEPT_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'승인된이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'APVD_HIS_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'현재버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'CUR_VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'APV_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재요청일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'APV_ASK_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재완료일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_BH', @level2Type = N'Column', @Level2Name = 'APV_CMPL_DT';
GO

CREATE INDEX IDX_WO_ONEC_BH1 ON dbo.WO_ONEC_BH (ONEC_TYP_CD);
CREATE INDEX IDX_WO_ONEC_BH2 ON dbo.WO_ONEC_BH (CAT_CD);
CREATE INDEX IDX_WO_ONEC_BH3 ON dbo.WO_ONEC_BH (ORGN_CD);
CREATE INDEX IDX_WO_ONEC_BH4 ON dbo.WO_ONEC_BH (STORG_CD);
CREATE INDEX IDX_WO_ONEC_BH5 ON dbo.WO_ONEC_BH (HOLDR_UID);
GO

--DROP TABLE [dbo].[WO_ONEC_HIS_L];
CREATE TABLE [dbo].[WO_ONEC_HIS_L](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [HIS_NO]                      [int]              NOT NULL,
    [HIS_REG_DT]                  [datetime]         NULL,
    [HIS_CONT]                    [varchar]   (1200) NULL,
    CONSTRAINT [PK_WO_ONEC_HIS_L] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC,
    [HIS_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드이력내역', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_HIS_L';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_HIS_L', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이력번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_HIS_L', @level2Type = N'Column', @Level2Name = 'HIS_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이력등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_HIS_L', @level2Type = N'Column', @Level2Name = 'HIS_REG_DT';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'이력내용', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_HIS_L', @level2Type = N'Column', @Level2Name = 'HIS_CONT';
GO

--DROP TABLE [dbo].[WO_ONEC_AUTH_D];
CREATE TABLE [dbo].[WO_ONEC_AUTH_D](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [AUTH_TYP_CD]                 [varchar]     (30) NOT NULL,
    [REGR_UID]                    [varchar]     (30) NULL,
    [REG_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_WO_ONEC_AUTH_D] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC,
    [USER_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드권한상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'권한구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D', @level2Type = N'Column', @Level2Name = 'AUTH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D', @level2Type = N'Column', @Level2Name = 'REGR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'등록일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_AUTH_D', @level2Type = N'Column', @Level2Name = 'REG_DT';
GO

--DROP TABLE [dbo].[WO_ONEC_CD_D];
CREATE TABLE [dbo].[WO_ONEC_CD_D](
    [CLS_CD]                      [varchar]     (30) NOT NULL,
    [CD]                          [varchar]     (30) NOT NULL,
    [CD_VA]                       [varchar]    (200) NULL,
    [SORT_ORDR]                   [int]              NULL,
    [USE_YN]                      [char]         (1) NULL,
    [NOTE]                        [varchar]    (400) NULL,
    CONSTRAINT [PK_WO_ONEC_CD_D] PRIMARY KEY CLUSTERED (
    [CLS_CD] ASC,
    [CD] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드코드상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'분류코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'CLS_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'코드값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'CD_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'USE_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'비고', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_CD_D', @level2Type = N'Column', @Level2Name = 'NOTE';
GO

--DROP TABLE [dbo].[WO_ONEC_PICH_D];
CREATE TABLE [dbo].[WO_ONEC_PICH_D](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [PICH_TYP_CD]                 [varchar]     (30) NOT NULL,
    [USER_UID]                    [varchar]     (30) NOT NULL,
    [SORT_ORDR]                   [int]              NULL,
    CONSTRAINT [PK_WO_ONEC_PICH_D] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC,
    [PICH_TYP_CD] ASC,
    [USER_UID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드담당자상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_PICH_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_PICH_D', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'담당자구분코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_PICH_D', @level2Type = N'Column', @Level2Name = 'PICH_TYP_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'사용자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_PICH_D', @level2Type = N'Column', @Level2Name = 'USER_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'정렬순서', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_PICH_D', @level2Type = N'Column', @Level2Name = 'SORT_ORDR';
GO

--DROP TABLE [dbo].[WO_ONEC_APV_LN_D];
CREATE TABLE [dbo].[WO_ONEC_APV_LN_D](
    [ONEC_NO]                     [bigint]           NOT NULL,
    [VER]                         [int]              NOT NULL,
    [APV_LN_PNO]                  [int]              NOT NULL,
    [APV_LN_NO]                   [int]              NOT NULL,
    [APVR_ROLE_CD]                [varchar]     (30) NOT NULL,
    [APV_STAT_CD]                 [varchar]     (30) NOT NULL,
    [ABS_RSON_CD]                 [varchar]     (30) NULL,
    [ABS_RSON_NM]                 [nvarchar]   (120) NULL,
    [PRED_YN]                     [char]         (1) NULL,
    [AGNT_UID]                    [varchar]     (30) NULL,
    [APVR_UID]                    [varchar]     (30) NULL,
    [APVR_NM]                     [nvarchar]   (120) NULL,
    [POSIT_DISP_VA]               [nvarchar]   (120) NULL,
    [SIGN_DISP_VA]                [nvarchar]   (120) NULL,
    [DT_DISP_VA]                  [nvarchar]   (120) NULL,
    [SIGN_IMG_PATH]               [varchar]    (240) NULL,
    [APV_DT]                      [datetime]         NULL,
    CONSTRAINT [PK_WO_ONEC_APV_LN_D] PRIMARY KEY CLUSTERED (
    [ONEC_NO] ASC,
    [VER] ASC,
    [APV_LN_PNO] ASC,
    [APV_LN_NO] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드결재라인상세', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'원카드번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'ONEC_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'버전', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'VER';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인부모번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_LN_PNO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재라인번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_LN_NO';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자역할코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_ROLE_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재상태코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_STAT_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유코드', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'ABS_RSON_CD';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'부재사유명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'ABS_RSON_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'전결여부', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'PRED_YN';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'대결자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'AGNT_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자UID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_UID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재자명', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APVR_NM';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'직위표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'POSIT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'SIGN_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일시표시값', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'DT_DISP_VA';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'서명이미지경로', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'SIGN_IMG_PATH';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'결재일시', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'WO_ONEC_APV_LN_D', @level2Type = N'Column', @Level2Name = 'APV_DT';
GO

