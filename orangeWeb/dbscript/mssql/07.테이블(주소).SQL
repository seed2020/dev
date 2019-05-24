
-- [2] 
/****** Object:  Table [dbo].[EM_ADR_BLD_INFO_B_ALL]    Script Date: 2015/08/24 11:48 - 주소전체 : 가공을 위한 전체 데이터 ******/
CREATE TABLE [dbo].[EM_ADR_BLD_INFO_B_ALL](
[LOW_TOWN_CD] VARCHAR(10) NOT NULL,
[SIDO] VARCHAR(40) NOT NULL,
[GUGUN] VARCHAR(40),
[DONG] VARCHAR(40) NOT NULL,
[RI] VARCHAR(40),
[MT_YN] CHAR(1),
[MAIN_LOT_NO] INT,
[SUB_LOT_NO] INT,
[ROAD_NM_CD] VARCHAR(12),
[ROAD_NM] VARCHAR(80),
[BASEMENT_YN] CHAR(1),
[MAIN_BLD_NO] INT,
[SUB_BLD_NO] INT,
[BLD_NM] VARCHAR(40),
[BLD_DETL_NM] VARCHAR(100),
[BLD_MNG_NO] VARCHAR(25) NOT NULL,
[DONG_SEQ] VARCHAR(2) NOT NULL,
[GOV_TOWN_CD] VARCHAR(10),
[GOV_TOWN_NM] VARCHAR(20),
[ZIP_NO] VARCHAR(10),
[ZIP_SEQ] VARCHAR(3),
[BULK_DELIVERY_NM] VARCHAR(40),
[MOVE_REASON_CD] VARCHAR(2),
[NOTICE_YMD] VARCHAR(8),
[BEFORE_ROAD_ADR] VARCHAR(25),
[CITY_PUBLIC_BLD_NM] VARCHAR(200),
[APT_HOURSE_YN] CHAR(1),
[DFT_ZONE_NO] VARCHAR(5),
[DETL_ADR_YN] CHAR(1),
[NOTE1] VARCHAR(15),
[NOTE2] VARCHAR(15),
CONSTRAINT [PK_EM_ADR_BLD_INFO_B_ALL] PRIMARY KEY CLUSTERED
(
[BLD_MNG_NO] ASC
));

/****** Object:  Table [dbo].[EM_ADR_BLD_INFO_B]    Script Date: 2015/08/24 11:48 실제 주소 테이블 : 직접 만들지 않고 Create Table as ... 로 구성한다. ******/
/*
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[EM_ADR_BLD_INFO_B](
[LOW_TOWN_CD] VARCHAR(10) NOT NULL,
[MT_YN] CHAR(1),
[MAIN_LOT_NO] INT,
[SUB_LOT_NO] INT,
[ROAD_NM_CD] VARCHAR(12),
[BASEMENT_YN] CHAR(1),
[MAIN_BLD_NO] INT,
[SUB_BLD_NO] INT,
[BLD_MNG_NO] VARCHAR(25) NOT NULL,
[ZIP_NO] VARCHAR(10),
[CITY_PUBLIC_BLD_NM] VARCHAR(200),
[APT_HOURSE_YN] CHAR(1),
[DFT_ZONE_NO] VARCHAR(5),
CONSTRAINT [PK_EM_ADR_BLD_INFO_B] PRIMARY KEY CLUSTERED
(
[BLD_MNG_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법정동코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'LOW_TOWN_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'산여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'MT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지번본번(번지)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'MAIN_LOT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지번부번(호)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'SUB_LOT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'도로명코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'ROAD_NM_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'지하여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'BASEMENT_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'건물본번' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'MAIN_BLD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'건물부번' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'SUB_BLD_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'건물관리번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'BLD_MNG_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'우편번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'ZIP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시군구용 건물명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'CITY_PUBLIC_BLD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'공동주택여부' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'APT_HOURSE_YN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'기초구역번호' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B', @level2type=N'COLUMN',@level2name=N'DFT_ZONE_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'건물정보기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_BLD_INFO_B'
GO
*/

-- [4]
-- 임시테이블에 중복제거 주소데이터 저장 - 6190564 건 생성(7월분까지)
--DROP TABLE EM_ADR_BLD_INFO_B_GROUP
SELECT * INTO EM_ADR_BLD_INFO_B_GROUP  FROM  EM_ADR_BLD_INFO_B_ALL 
WHERE BLD_MNG_NO IN(
	SELECT MIN(BLD_MNG_NO) BLD_MNG_NO
	FROM EM_ADR_BLD_INFO_B_ALL
	GROUP BY MT_YN, MAIN_LOT_NO, SUB_LOT_NO, ROAD_NM_CD, BASEMENT_YN, MAIN_BLD_NO, SUB_BLD_NO, CITY_PUBLIC_BLD_NM
);
--DELETE FROM EM_ADR_BLD_INFO_B_ALL

-- [5]
/****** Object:  Table [dbo].[EM_ADR_GUGUN_B]    Script Date: 2015/08/26 15:26 ******/
CREATE TABLE [dbo].[EM_ADR_GUGUN_B](
[GUGUN_CD] VARCHAR(5) NOT NULL,
[SIDO] VARCHAR(40) NOT NULL,
[GUGUN] VARCHAR(40),
CONSTRAINT [PK_EM_ADR_GUGUN_B] PRIMARY KEY CLUSTERED
(
[GUGUN_CD] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시군구코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_GUGUN_B', @level2type=N'COLUMN',@level2name=N'GUGUN_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시도명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_GUGUN_B', @level2type=N'COLUMN',@level2name=N'SIDO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시군구명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_GUGUN_B', @level2type=N'COLUMN',@level2name=N'GUGUN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'시군구기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_GUGUN_B'
GO

-- [6]
-- 데이터 삽입
--DELETE FROM EM_ADR_GUGUN_B
INSERT INTO EM_ADR_GUGUN_B
SELECT SUBSTRING(ROAD_NM_CD,1,5) AS GUGUN_CD, SIDO, GUGUN
FROM EM_ADR_BLD_INFO_B_GROUP
GROUP BY SUBSTRING(ROAD_NM_CD,1,5), SIDO, GUGUN;

-- [7]
/****** Object:  Table [dbo].[EM_ADR_ROAD_B]    Script Date: 2015/08/26 17:10 ******/
CREATE TABLE [dbo].[EM_ADR_ROAD_B](
[ROAD_NM_CD] VARCHAR(12) NOT NULL,
[ROAD_NM] VARCHAR(80) NOT NULL,
CONSTRAINT [PK_EM_ADR_ROAD_B] PRIMARY KEY CLUSTERED
(
[ROAD_NM_CD] ASC
))
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'도로명코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_ROAD_B', @level2type=N'COLUMN',@level2name=N'ROAD_NM_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'도로명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_ROAD_B', @level2type=N'COLUMN',@level2name=N'ROAD_NM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'도로기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_ROAD_B'
GO

-- [8]
--DELETE FROM EM_ADR_ROAD_B
INSERT INTO EM_ADR_ROAD_B
SELECT ROAD_NM_CD, ROAD_NM
FROM EM_ADR_BLD_INFO_B_GROUP
GROUP BY ROAD_NM_CD, ROAD_NM;

/****** Object:  Table [dbo].[EM_ADR_DONG_B]    Script Date: 2015/08/26 17:10 ******/
CREATE TABLE [dbo].[EM_ADR_DONG_B](
[LOW_TOWN_CD] VARCHAR(10) NOT NULL,
[DONG] VARCHAR(40) NOT NULL,
[RI] VARCHAR(40),
CONSTRAINT [PK_EM_ADR_DONG_B] PRIMARY KEY CLUSTERED
(
[LOW_TOWN_CD] ASC
))
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법정동코드' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_DONG_B', @level2type=N'COLUMN',@level2name=N'LOW_TOWN_CD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법정읍면동명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_DONG_B', @level2type=N'COLUMN',@level2name=N'DONG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'법정리명' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_DONG_B', @level2type=N'COLUMN',@level2name=N'RI'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'동기본' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'EM_ADR_DONG_B'
GO

-- [9]
--DELETE FROM EM_ADR_DONG_B
INSERT INTO EM_ADR_DONG_B
SELECT LOW_TOWN_CD, DONG, RI
FROM EM_ADR_BLD_INFO_B_GROUP
GROUP BY LOW_TOWN_CD, DONG, RI;

-- [10]
-- 컬럼 삭제
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN SIDO;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN GUGUN;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN DONG;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN RI;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN ROAD_NM;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN BLD_NM;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN BLD_DETL_NM;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN DONG_SEQ;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN GOV_TOWN_CD;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN GOV_TOWN_NM;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN ZIP_SEQ;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN BULK_DELIVERY_NM;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN MOVE_REASON_CD;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN NOTICE_YMD;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN BEFORE_ROAD_ADR;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN DETL_ADR_YN;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN NOTE1;
ALTER TABLE EM_ADR_BLD_INFO_B_GROUP DROP COLUMN NOTE2;
GO

-- 데이터 최종 복사
Select * into EM_ADR_BLD_INFO_B  from  EM_ADR_BLD_INFO_B_GROUP;


-- PK
ALTER TABLE EM_ADR_BLD_INFO_B ADD CONSTRAINT PK_EM_ADR_BLD_INFO_B PRIMARY KEY CLUSTERED (BLD_MNG_NO ASC);

--INDEX
--DROP INDEX EM_ADR_BLD_INFO_B.IDX_EM_ADR_BLD_INFO_B;
CREATE INDEX IDX_EM_ADR_BLD_INFO_B_1 ON EM_ADR_BLD_INFO_B(ROAD_NM_CD,MAIN_BLD_NO);
CREATE INDEX IDX_EM_ADR_BLD_INFO_B_2 ON EM_ADR_BLD_INFO_B(LOW_TOWN_CD);

-- 변환전 데이터 : 1991632 KB - 전체(ALL) EM_ADR_BLD_INFO_B_ALL
-- 변환전 데이터 : 1147992KB - 건물상세제외(GROUP) EM_ADR_BLD_INFO_B_GROUP 
-- 변환후 데이터 : 623632 KB - 불필요한 컬럼 제외(최종) EM_ADR_BLD_INFO_B

-- 기존 우편번호 테이블 삭제
--DROP TABLE PT_ZIPCD_B
--DROP TABLE PT_ZIPCD_DFT_B

-- [11]
-- 주소그룹 테이블 삭제
DROP TABLE EM_ADR_BLD_INFO_B_ALL;
DROP TABLE EM_ADR_BLD_INFO_B_GROUP;
GO

