
1. 우편번호 데이터 받기
  URL : http://www.juso.go.kr/addrlink/addressBuildDevNew.do?menu=rdnm
  1.1 받을파일 : 최근 전체 주소

2.EM_ADR_BLD_INFO_B_ALL 테이블 생성(dbscript참조)

3. [건물정보] INSERT
BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_강원도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_경기도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_경상남도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_경상북도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_광주광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_대구광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_대전광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_부산광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_서울특별시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_세종특별자치시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_울산광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_인천광역시.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_전라남도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_전라북도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_제주특별자치도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_충청남도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');

BULK INSERT EM_ADR_BLD_INFO_B_ALL
FROM 'D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\2015.08.01\건물정보_충청북도.txt'
WITH ( FIELDTERMINATOR ='|', FIRSTROW = 1, FORMATFILE ='D:\[개발하자]\[우편번호]\일괄등록_2015.08\ZIP_MSSQL\EM_ADR_BLD_INFO_B_ALL.Fmt');


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
