
1. 우편번호 데이터 받기
  URL : http://www.juso.go.kr/support/AddressBuild.do
  1.1 받을파일 : 전체주소 ~ 최종안

2.EM_ADR_BLD_INFO_B_ALL 테이블 생성(dbscript참조)

3. [건물정보] INSERT
BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_gangwon.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_gyunggi.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_gyeongnam.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_gyeongbuk.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_gwangju.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_daegu.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_daejeon.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_busan.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_seoul.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_sejong.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_ulsan.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_incheon.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_jeonnam.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_jeonbuk.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_jeju.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_chungnam.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\zipData\201604\build_chungbuk.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\zipData\EM_ADR_BLD_INFO_B_ALL.Fmt');


4.EM_ADR_BLD_INFO_B_GROUP 테이블로 중복데이터를 제외하고 복사(dbscript참조)

5.EM_ADR_GUGUN_B 생성 및 복사(dbscript참조)

6.EM_ADR_ROAD_B 생성 및 복사(dbscript참조)

7.EM_ADR_DONG_B 생성 및 복사(dbscript참조)

8.EM_ADR_BLD_INFO_B_GROUP 테이블 불필요한 컬럼 삭제(dbscript참조)

9.데이터 최종 복사 EM_ADR_BLD_INFO_B_GROUP --> EM_ADR_BLD_INFO_B(dbscript참조)

10.PK 및 인덱스 생성(dbscript참조)

11.EM_ADR_BLD_INFO_B_GROUP 삭제(dbscript참조)



참조) 포멧파일 만들기
  - 명령 프롬프트에서 실행
    bcp innogw..EM_ZIPCD_B format nul -c -t, -f EM_ZIPCD_B.Fmt -T
    bcp innogw..EM_ZIPCD_DFT_B format nul -c -t, -f EM_ZIPCD_DFT_B.Fmt -T
