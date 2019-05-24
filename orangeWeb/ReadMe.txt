
1. 암호화 프로퍼티
	- context-mssql.properties, context-oracle.properties
	- 변경 후 암호화를 위해 SecuPropertyUtil.main 실행해야함.
	- context-mssql.properties 의 주석 참조

2. URL 규칙
	- /pt/adm/mnu/listMnu.do?catCd=TEST
	
	- 첫째 경로 : 해당 업무구분
	  cm:공통, pt:포털, bb:게시, or:조직도, ap:결재, ct:커뮤니티 ..
	
	- 둘째 경로 : 업무 대구분, 관리자 경로는 (adm), 나머지는 업무 특성에 맞춰서 구별
	
	- URL의 경로는 메뉴에 붙이는 단위로 폴더가 생기도록 함
	
	- menuId 파라미터를 넘길 것이며 각 페이지마다 달고 다녀야함
	
	- URL 폴더 경로 + 파라미터 : Unique 해야함 (권한 체크 목적)
	  /pt/adm/mnu + catCd=TEST
	
	- 메뉴에 등록된 파라미터는 연관 페이지에서 항상 같이 넘겨 줘야함
	- 연관 페이지들
	  /pt/adm/mnu/setMnu.do?catCd=TEST
	  /pt/adm/mnu/transMnu.do?catCd=TEST
	  /pt/adm/mnu/viewMnu.do?catCd=TEST
	
3. URL, 함수 Prefix(Ctrl) 정의
    -- 원칙 --
    url, ctrl 내의 함수명, jsp 파일명 동일하게 가져감 - listMnu(메뉴목록)
    
    -- 화면용 prefix --
	list	목록 (화면용)
	set		등록 / 수정 (화면용)
	view	조회 (화면용)
	tree	조회 (트리화면용)
	index.do
	-- 비화면 용 --
	get		조회(단건,다건), AJAX용, 화면 없음
	trans	DB 저장용, 등록/수정/삭제 용 Submit url 또는 AJAX용, 화면없음
	create	생성
	process	진행 - 배치작업의 시작,
	-- 팝업용 용 --
	search	검색 팝업
	select	선택 팝업
	...
	
4. URL, 함수 suffix 정의
	pop 화면은 ~Pop.do
	frame, iframe 용 화면은 ~Frm.do
	포틀릿 ~Plt.do
	포틀릿내의 프레임 : ~PltFrm.do
	Ajax 용 url : Ajx.do

5. DATABASE
	MSSQL
	- jdbc:sqlserver://192.168.0.144:1433;DatabaseName=innogw
	- gwuser / gw000
	- DATABASE
		INNOGW : 포털, 공통, 조직도, 결재, 협업, 게시, 커뮤니티, 일정, 명함 등
	ORACLE
	- jdbc:oracle:thin:@192.168.0.145:1521:ORC45
	-- gwuser / gw000
	- TABLESPACE
		TS_GW_POTL_DAT : 포털, 공통, 조직도 - 테이블
		TS_GW_POTL_IND : 포털, 공통, 조직도 - 인덱스
		TS_GW_APPR_DAT : 결재 - 테이블
		TS_GW_APPR_IND : 결재 - 인덱스
		TS_GW_WORK_DAT : 협업, 게시, 커뮤니티, 일정, 명함 등 - 테이블
		TS_GW_WORK_IND : 협업, 게시, 커뮤니티, 일정, 명함 등 - 인덱스

	- 개발서버(145) : sa / innodb12345!@#$%

[로컬 서버 세팅]
	Tomcat Server - Overview - Open launch confiuration > Arguments - 맨뒤에 [ -Drun_mode=LOC] 추가
	Tomcat Server - Overview - Server Options - Serve modules without publishing 체크

[hosts 세팅]
192.168.0.145  mail.innogw.com    oracle.gworange.com
192.168.0.144  ep.innogw.com
192.168.0.144  db.gworange.com    mssql.gworange.com

[버튼 의미]
	저장 : 데이터 파일등이 서버에 저장 된다는 의미 - 데이터를 추후에 일반적인 사용자가 조회 가능하도록 저장함
	수정 : 데이터가 변경되어 저장 된다는 의미 - 일반적으로 [저장] 사용
	변경 : 데이터가 변경되지만 화면상에서만 변경된다는 의미 - 추후 저장 버튼을 클릭해야 저장됨
	확인 : 
		1 .데이터가 서버에 저장되기는 하지만 볼 수 없는 형태 또는 삭제, 폐기 등의 상태값 저장
			- 예) 데이터 삭제, 파일 삭제, 데이터 폐기, 폐기 취소(복원), 삭제 취소,
		2. 팝업창에서 부모창으로 데이터를 넘길때
	닫기 : 팝업창의 경우
	취소 : 이전 화면으로 - history.go(-1), 경우에 따라 이동 위치가 달라 질 수 있음

[META TITLE]
	- 메뉴에 붙은 페이지 - 프레임웍 제공
	- 메뉴에 붙지 않은 페이지
		- request.setAttribute("META_TITLE", "페이지명"+" | "+request.getAttribute("META_RESC"));
	- 프레임 / 팝업 - 제외함

[권한 정리]
	권한 - SYS:시스템 관리자, S:관리자(회사 관리자), 
	     - A:관리(남의글 수정, 삭제 가능), M:수정(남의글 수정 가능), W:쓰기, R:읽기

	  > 같은 권한 또는 상위 권한을 가진 경우 해당 기능 활성화
	  > W:쓰기 의 경우 ownerUid(작성자)와 세션의 사용자 정보를 비교하여 같은 경우 활성화함
	      - 따라서 W의 경우 아직 작성되지 않은 곳은 세션의 사용자ID를 넣어줌
	  > 기타 글을 읽을 수 있으면 할 수 있는 행위(복사, 인쇄, 저장, 메일보내기 등)은 읽기 권한과 같음
	
	ownerUid 의미 : W:쓰기 이상의 권한이 있고, 작성자가 세션과 같으면 권한 있음
	
	[목록 화면 - 여러 사용자가 보는 목록]
	<u:button title="등록" auth="W" />
	<u:button title="수정" auth="A" />
	<u:button title="삭제" auth="A" />
	<u:button title="취소/닫기/목록/이전" />
	
	[목록 화면 - 자기가 작성한 것만 보는 목록]
	<u:button title="등록" auth="W" />
	<u:button title="수정" auth="W" />
	<u:button title="삭제" auth="W" />
	<u:button title="취소/닫기/목록/이전" />
	
	[조회/수정 화면]
	<u:button title="수정/저장" auth="M" ownerUid="${someVo.regrUid}" />
	<u:button title="삭제" auth="A" ownerUid="${someVo.regrUid}" />
	<u:button title="취소/닫기/목록/이전" />
	
	[등록 화면]
	<u:button title="저장" auth="W" />
	<u:button title="취소/닫기/목록/이전" />
	
	