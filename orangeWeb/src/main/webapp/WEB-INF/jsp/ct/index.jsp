<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<style>
	a:link { color: blue; }
	a:visited { color: purple; }
	a:active { color: red; }
</style>

- (작업필요)
! (작업보류)
@ (공통화면)
<br/><br/>

[사용자]
<br/><br/>

<a href="./listMyCm.do?menuId=${menuId}">CT-001. 나의 커뮤니티</a><br/><br/>
<a href="./listCmReqs.do?menuId=${menuId}">CT-002. 신청중인 커뮤니티</a><br/><br/>
<a href="./setCm.do?menuId=${menuId}&fnc=mod">CT-003. 커뮤니티 개설신청 수정</a><br/><br/>
<a href="./listAllCm.do?menuId=${menuId}">CT-004. 전체커뮤니티</a><br/><br/>
<a href="./listCm.do?menuId=${menuId}">CT-005. 커뮤니티목록</a><br/><br/>
<a href="./setCmJoin.do?menuId=${menuId}">CT-006. 커뮤니티 가입</a><br/><br/>
<a href="./listMyBull.do?menuId=${menuId}">CT-007. 커뮤니티게시목록</a><br/><br/>
<a href="javascript:dialog.open('findCmPop','커뮤니티 선택','./findCmPop.do?menuId=${menuId}');">CT-008. 커뮤니티 선택</a><br/><br/>
<a href="./listCmMngTgt.do?menuId=${menuId}">CT-009. 관리대상목록</a><br/><br/>
<a href="./notc/listNotc.do?menuId=${menuId}">CT-010. 관리자 공지사항</a><br/><br/>
<a href="./pr/listPr.do?menuId=${menuId}">CT-011. 커뮤니티 홍보마당</a><br/><br/>
<a href="./pr/setPr.do?menuId=${menuId}">CT-012. 홍보마당 등록</a><br/><br/>
<a href="./pr/viewPr.do?menuId=${menuId}">CT-013. 홍보마당 조회</a><br/><br/>
<a href="./setCm.do?menuId=${menuId}">CT-014. 커뮤니티개설신청(개설 후 승인)</a><br/><br/>
<a href="./setCmFnc.do?menuId=${menuId}">CT-015. 기능선택</a><br/><br/>
<a href="javascript:dialog.open('setFldPop','이름 변경','./setFldPop.do?menuId=${menuId}&fnc=mod');">CT-016. 이름변경</a><br/><br/>
<a href="javascript:dialog.open('setFldPop','폴더 생성','./setFldPop.do?menuId=${menuId}');">CT-017. 폴더생성</a><br/><br/>
! CT-018. 커뮤니티개설신청(개설 전 승인)<br/><br/>
<strong>- CT-019. 커뮤니티 메뉴</strong><br/><br/>
<a href="./board/listBoard.do?menuId=${menuId}">CT-020. 커뮤니티 기능-게시판-게시물 목록</a><br/><br/>
<a href="./board/setBoard.do?menuId=${menuId}">CT-021. 커뮤니티 기능-게시판-게시물 등록</a><br/><br/>
<a href="./board/viewBoard.do?menuId=${menuId}">CT-022. 커뮤니티 기능-게시판-게시물 조회</a><br/><br/>
<a href="./pds/listPds.do?menuId=${menuId}">CT-023. 커뮤니티 기능-자료실-자료 목록</a><br/><br/>
<a href="./pds/setPds.do?menuId=${menuId}">CT-024. 커뮤니티 기능-자료실-자료 등록</a><br/><br/>
<a href="./pds/viewPds.do?menuId=${menuId}">CT-025. 커뮤니티 기능-자료실-자료 조회</a><br/><br/>
<a href="javascript:dialog.open('sendToPop','보내기','./sendToPop.do?menuId=${menuId}');">CT-026. 커뮤니티 기능-자료실-자료 조회-보내기</a><br/><br/>
<a href="./debr/listDebr.do?menuId=${menuId}">CT-027. 커뮤니티 기능-토론실-토론목록</a><br/><br/>
<a href="./debr/listOpin.do?menuId=${menuId}">CT-028. 커뮤니티 기능-토론실-의견목록</a><br/><br/>
<a href="javascript:dialog.open('setOpinPop','의견 등록','./debr/setOpinPop.do?menuId=${menuId}');">CT-029. 커뮤니티 기능-토론실-의견등록</a><br/><br/>
<a href="javascript:dialog.open('setDebrPop','토론실 등록','./debr/setDebrPop.do?menuId=${menuId}');">CT-030. 커뮤니티 기능-토론실-토론방 등록</a><br/><br/>
<a href="./site/listSite.do?menuId=${menuId}">CT-031. 커뮤니티 기능-Cool Site-목록조회</a><br/><br/>
<a href="./site/setSite.do?menuId=${menuId}">CT-032. 커뮤니티 기능-Cool Site-사이트등록</a><br/><br/>
<a href="javascript:dialog.open('setCatPop','카테고리 관리','./site/setCatPop.do?menuId=${menuId}');">CT-033. 커뮤니티 기능-Cool Site-카테고리 관리</a><br/><br/>
<a href="./site/viewSite.do?menuId=${menuId}">CT-034. 커뮤니티 기능-Cool Site-사이트조회</a><br/><br/>
<a href="./schdl/listSchdl.do?menuId=${menuId}">CT-035. 커뮤니티 기능-일정관리-월간일정</a><br/><br/>
<a href="./schdl/listSchdl.do?menuId=${menuId}&tabNo=1">CT-036. 커뮤니티 기능-일정관리-주간일정</a><br/><br/>
<a href="./schdl/listSchdl.do?menuId=${menuId}&tabNo=2">CT-037. 커뮤니티 기능-일정관리-일간일정</a><br/><br/>
<a href="javascript:dialog.open('setEvntPop','행사등록','./schdl/setEvntPop.do?menuId=${menuId}');">CT-038. 커뮤니티 기능-일정관리-행사등록</a><br/><br/>
<a href="javascript:dialog.open('setAnnvPop','기념일등록','./schdl/setAnnvPop.do?menuId=${menuId}');">CT-039. 커뮤니티 기능-일정관리-기념일등록</a><br/><br/>
<a href="javascript:dialog.open('viewEvntPop','행사조회','./schdl/viewEvntPop.do?menuId=${menuId}');">CT-040. 커뮤니티 기능-일정관리-행사조회</a><br/><br/>
<a href="javascript:dialog.open('viewAnnvPop','기념일조회','./schdl/viewAnnvPop.do?menuId=${menuId}');">CT-041. 커뮤니티 기능-일정관리-기념일조회</a><br/><br/>
<a href="./surv/listSurv.do?menuId=${menuId}">CT-042. 커뮤니티 기능-투표-투표목록</a><br/><br/>
<a href="./surv/setSurv.do?menuId=${menuId}">CT-043. 커뮤니티 기능-투표-투표등록</a><br/><br/>
<a href="./surv/setSurvQues.do?menuId=${menuId}">CT-044. 커뮤니티 기능-투표-질문추가</a><br/><br/>
<a href="javascript:dialog.open('setMulcQuesPop','객관식 질문 추가','./surv/setMulcQuesPop.do?menuId=${menuId}');">CT-045. 커뮤니티 기능-투표-질문등록</a><br/><br/>
<a href="./surv/viewSurv.do?menuId=${menuId}">CT-046. 커뮤니티 기능-투표-투표참여</a><br/><br/>
<a href="./surv/viewSurvRes.do?menuId=${menuId}">CT-047. 커뮤니티 기능-투표-투표결과</a><br/><br/>
<a href="./listMbsh.do?menuId=${menuId}">CT-048. 커뮤니티 기능-회원정보 검색</a><br/><br/>
<a href="./mng/listMngMain.do?menuId=${menuId}">CT-049. 커뮤니티 관리-메인</a><br/><br/>
<a href="./setCm.do?menuId=${menuId}&fnc=mod">CT-050. 커뮤니티 관리-기본사항</a><br/><br/>
<a href="./mng/setMngAuth.do?menuId=${menuId}">CT-051. 커뮤니티 관리-기능별 권한관리</a><br/><br/>
<a href="./mng/setMngPage.do?menuId=${menuId}">CT-052. 커뮤니티 관리-초기페이지 구성</a><br/><br/>
<a href="./mng/setMngPageSetup.do?menuId=${menuId}">CT-053. 커뮤니티 관리-초기페이지 설정</a><br/><br/>
<a href="./mng/listMngData.do?menuId=${menuId}">CT-054. 커뮤니티 관리-자료삭제</a><br/><br/>
<a href="./mng/setMngClose.do?menuId=${menuId}">CT-055. 커뮤니티 관리-커뮤니티 폐쇄</a><br/><br/>
<a href="./mng/listMngJoinReqs.do?menuId=${menuId}">CT-056. 커뮤니티 관리-가입신청자 관리-목록조회 </a><br/><br/>
<a href="javascript:dialog.open('viewMngJoinReqsPop','가입신청 승인','./mng/viewMngJoinReqsPop.do?menuId=${menuId}');">CT-057. 커뮤니티 관리-가입 신청자 관리-가입승인</a><br/><br/>
<a href="./mng/listMngMbsh.do?menuId=${menuId}">CT-058. 커뮤니티 관리-회원정보 변경-회원목록</a><br/><br/>
<a href="javascript:dialog.open('setMngMbshPop','회원정보 변경','./mng/setMngMbshPop.do?menuId=${menuId}');">CT-059. 커뮤니티 관리-회원정보 변경-회원정보 변경</a><br/><br/>
! - CT-060. 커뮤니티 관리-회원정보 변경-회원전체 목록<br/><br/>
<a href="./mng/setMngMast.do?menuId=${menuId}">CT-061. 커뮤니티 관리-마스터 변경</a><br/><br/>
<a href="./mng/listMngVistrStat.do?menuId=${menuId}">CT-062. 커뮤니티 관리-방문자 현황</a><br/><br/>
<strong>- CT-063. 포틀릿-전체 커뮤니티</strong><br/><br/>
<strong>- CT-064. 포틀릿-나의 커뮤니티</strong><br/><br/>
<strong>- CT-065. 포틀릿-신설 커뮤니티</strong><br/><br/>
<strong>- CT-066. 포틀릿-기간설정</strong><br/><br/>
<strong>- CT-067. 포틀릿-인기 커뮤니티</strong><br/><br/>
<strong>- CT-068. 포틀릿-인기 커뮤니티-순위지정</strong><br/><br/>
<strong>- CT-069. 포틀릿-추천 커뮤니티</strong><br/><br/>
<strong>- CT-070. 포틀릿-관리대상</strong><br/><br/>
<strong>- CT-071. 포틀릿-홍보마당</strong><br/><br/>
<strong>- CT-072. 포틀릿-관리자 공지사항</strong><br/><br/>
<strong>- CT-073. 포틀릿-커뮤니티 게시판</strong><br/><br/>
<strong>- CT-074. 포틀릿-환경설정</strong><br/><br/>
<strong>- CT-075. 포틀릿-커뮤니티 자료실</strong><br/><br/>
<strong>- CT-076. 포틀릿-커뮤니티 토론실</strong><br/><br/>
<strong>- CT-077. 포틀릿-커뮤니티 Cool Site</strong><br/><br/>
<strong>- CT-078. 포틀릿-커뮤니티 일정</strong><br/><br/>
<strong>- CT-079. 포틀릿-커뮤니티 투표</strong><br/><br/>

<br/>

[관리자]
<br/><br/>

<a href="./adm/setEstbPolc.do?menuId=${menuId}">CT-080. 개설정책</a><br/><br/>
<a href="./adm/listEstbReqs.do?menuId=${menuId}">CT-081. 개설승인-신청중인 목록</a><br/><br/>
! CT-082. 개설승인-미승인 목록<br/><br/>
<a href="./adm/setCmCat.do?menuId=${menuId}">CT-083. 카테고리 관리</a><br/><br/>
<a href="javascript:dialog.open('setFldPop','폴더 등록','./adm/setFldPop.do?menuId=${menuId}');">CT-084. 카테고리 관리-폴더추가</a><br/><br/>
<a href="javascript:dialog.open('setClsPop','분류 등록','./adm/setClsPop.do?menuId=${menuId}');">CT-085. 카테고리 관리-분류추가</a><br/><br/>
<a href="./adm/listMngTgtApvd.do?menuId=${menuId}">CT-086. 관리대상 커뮤니티</a><br/><br/>
<a href="./adm/listCmInfo.do?menuId=${menuId}">CT-087. 커뮤니티 정보변경</a><br/><br/>
<a href="javascript:dialog.open('setMastPop','커뮤니티 마스터 변경','./adm/setMastPop.do?menuId=${menuId}');">CT-088. 커뮤니티 정보변경-마스터 변경</a><br/><br/>
<a href="./adm/listFnc.do?menuId=${menuId}">CT-089. 커뮤니티 기능관리</a><br/><br/>
<a href="javascript:dialog.open('setFncPop','커뮤니티 기능 등록','./adm/setFncPop.do?menuId=${menuId}');">CT-090. 커뮤니티 기능추가</a><br/><br/>
<a href="javascript:dialog.open('setFncPop','커뮤니티 기능 수정','./adm/setFncPop.do?menuId=${menuId}&fnc=mod');">CT-091. 커뮤니티 기능수정</a><br/><br/>
<a href="./adm/listNotc.do?menuId=${menuId}">CT-092. 커뮤니티 공지사항-목록조회</a><br/><br/>
<a href="./adm/setNotc.do?menuId=${menuId}">CT-093. 커뮤니티 공지사항-등록</a><br/><br/>
<a href="./adm/viewNotc.do?menuId=${menuId}">CT-094. 커뮤니티 공지사항-내용조회</a><br/><br/>
<a href="./adm/listCloseReqs.do?menuId=${menuId}">CT-095. 커뮤니티 폐쇄관리</a><br/><br/>
<a href="./adm/listMastEmail.do?menuId=${menuId}">CT-096. 마스터에게 메일보내기</a><br/><br/>
<a href="./adm/listUseStat.do?menuId=${menuId}">CT-097. 커뮤니티 정보조회</a><br/><br/>
<a href="./adm/listExntCm.do?menuId=${menuId}">CT-098. 우수 커뮤니티 관리</a><br/><br/>
<a href="./adm/listData.do?menuId=${menuId}">CT-099. 커뮤니티 자료삭제</a><br/><br/>

<div class="headbox">
<dl><dd>[커뮤니티 메뉴]</dd></dl>
<dl><dd><a>탈퇴</a></dd></dl>
<dl><dd><a href="./viewCm.do?menuId=${menuId}">커뮤니티홈</a></dd></dl>
<dl><dd><a href="./listMbsh.do?menuId=${menuId}">회원목록</a></dd></dl>
<dl><dd><a href="./mng/listMngMain.do?menuId=${menuId}">커뮤니티 관리</a></dd></dl>
<dl><dd><a href="./board/listBoard.do?menuId=${menuId}">게시판</a></dd></dl>
<dl><dd><a href="./pds/listPds.do?menuId=${menuId}">자료실</a></dd></dl>
<dl><dd><a href="./debr/listDebr.do?menuId=${menuId}">토론실</a></dd></dl>
<dl><dd><a href="./schdl/listSchdl.do?menuId=${menuId}">일정관리</a></dd></dl>
<dl><dd><a href="./site/listSite.do?menuId=${menuId}">Cool Site</a></dd></dl>
<dl><dd><a href="./surv/listSurv.do?menuId=${menuId}">투표</a></dd></dl>
<dl><dd><a href="./forum/listForum.do?menuId=${menuId}">포럼</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listMyCm.do?menuId=${menuId}">나의 커뮤니티</a></dd></dl>
<dl><dd><a href="./listAllCm.do?menuId=${menuId}">전체 커뮤니티</a></dd></dl>
<dl><dd><a href="./listMyBull.do?menuId=${menuId}">커뮤니티 게시물</a></dd></dl>
<dl><dd><a href="./listCmReqs.do?menuId=${menuId}">신청중인 커뮤니티</a></dd></dl>
<dl><dd><a href="./setCm.do?menuId=${menuId}">커뮤니티 만들기</a></dd></dl>
<dl><dd><a href="./notc/listNotc.do?menuId=${menuId}">커뮤니티관리자공지사항</a></dd></dl>
<dl><dd><a href="./pr/listPr.do?menuId=${menuId}">커뮤니티 홍보마당</a></dd></dl>
<dl><dd><a href="./listCmMngTgt.do?menuId=${menuId}">관리대상 목록</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/setEstbPolc.do?menuId=${menuId}">개설정책</a></dd></dl>
<dl><dd><a href="./adm/listEstbReqs.do?menuId=${menuId}">개설승인</a></dd></dl>
<dl><dd><a href="./adm/setCmCat.do?menuId=${menuId}">카테고리 관리</a></dd></dl>
<dl><dd><a href="./adm/listFnc.do?menuId=${menuId}">커뮤니티 기능관리</a></dd></dl>
<dl><dd><a href="./adm/listNotc.do?menuId=${menuId}">커뮤니티 공지사항</a></dd></dl>
<dl><dd><a href="./adm/listMngTgtApvd.do?menuId=${menuId}">관리대상 승인</a></dd></dl>
<dl><dd><a href="./adm/listMastEmail.do?menuId=${menuId}">마스터에게 메일보내기</a></dd></dl>
<dl><dd><a href="./adm/listCmInfo.do?menuId=${menuId}">커뮤니티 정보변경</a></dd></dl>
<dl><dd><a href="./adm/listUseStat.do?menuId=${menuId}">커뮤니티 정보조회</a></dd></dl>
<dl><dd><a href="./adm/listData.do?menuId=${menuId}">커뮤니티 자료삭제</a></dd></dl>
<dl><dd><a href="./adm/listCloseReqs.do?menuId=${menuId}">커뮤니티 폐쇄신청</a></dd></dl>
<dl><dd><a>타모듈연계 환경설정</a></dd></dl>
<dl><dd><a href="./adm/listExntCm.do?menuId=${menuId}">우수커뮤니티 관리</a></dd></dl>
</div>
