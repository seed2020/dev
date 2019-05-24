
1. 우편번호 데이터 받기
  URL : http://www.juso.go.kr/support/AddressBuild.do
  1.1 받을파일 : 전체주소 ~ 최종안

2.EM_ADR_BLD_INFO_B_ALL 테이블 생성(dbscript참조)

2.1 주소파일,control 파일 서버에 복사(orange 계정으로 복사 후 폴더 및 파일 소유자 변경)

3. [건물정보] INSERT
SQL_LOADER.SQL 내용 복사 후 db계정에 접속해서 실행

4.EM_ADR_BLD_INFO_B_GROUP 테이블로 중복데이터를 제외하고 복사(dbscript참조)

5.EM_ADR_GUGUN_B 생성 및 복사(dbscript참조)

6.EM_ADR_ROAD_B 생성 및 복사(dbscript참조)

7.EM_ADR_DONG_B 생성 및 복사(dbscript참조)

8.EM_ADR_BLD_INFO_B 생성

9.데이터 최종 복사 EM_ADR_BLD_INFO_B_GROUP --> EM_ADR_BLD_INFO_B(dbscript참조)

10.EM_ADR_BLD_INFO_B_GROUP 삭제(dbscript참조)

참조) 포멧파일 만들기
  - 명령 프롬프트에서 실행
    bcp innogw..EM_ZIPCD_B format nul -c -t, -f EM_ZIPCD_B.Fmt -T
    bcp innogw..EM_ZIPCD_DFT_B format nul -c -t, -f EM_ZIPCD_DFT_B.Fmt -T
