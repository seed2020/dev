
1. 우편번호 데이터 받기
  URL : http://www.juso.go.kr/addrlink/addressBuildDevNew.do?menu=rdnm
  1.1 받을파일 : 전체주소 ~ 최종안

2.EM_ADR_BLD_INFO_B_ALL 테이블 생성(dbscript참조)

3. CTRL 폴더의 loadData.txt SQL 실행 - sql 실행 툴에서
 - 한글이 깨질경우 주소파일의 인코딩 확인(utf-8로 변환)

4.EM_ADR_BLD_INFO_B_GROUP 테이블로 중복데이터를 제외하고 복사(dbscript참조)

5.EM_ADR_GUGUN_B 생성 및 복사(dbscript참조)

6.EM_ADR_ROAD_B 생성 및 복사(dbscript참조)

7.EM_ADR_DONG_B 생성 및 복사(dbscript참조)

8.EM_ADR_BLD_INFO_B_GROUP 테이블 불필요한 컬럼 삭제(dbscript참조)

9.데이터 최종 복사 EM_ADR_BLD_INFO_B_GROUP --> EM_ADR_BLD_INFO_B(dbscript참조)

10.PK 및 인덱스 생성(dbscript참조)

11.EM_ADR_BLD_INFO_B_GROUP 삭제(dbscript참조)
