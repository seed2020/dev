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
<a href="javascript:dialog.open('setPromPop','약속등록','./setPromPop.do?menuId=${menuId}');">CS-001. 약속등록</a><br/><br/>
<a href="javascript:dialog.open('setWorkPop','할일등록','./setWorkPop.do?menuId=${menuId}');">CS-002. 할일등록</a><br/><br/>
<a href="javascript:dialog.open('setEvntPop','행사등록','./setEvntPop.do?menuId=${menuId}');">CS-003. 행사등록</a><br/><br/>
<a href="javascript:dialog.open('setAnnvPop','기념일등록','./setAnnvPop.do?menuId=${menuId}');">CS-004. 기념일등록</a><br/><br/>
<a href="javascript:dialog.open('setRepetPop','반복설정','./setRepetPop.do?menuId=${menuId}');">CS-005. 일정등록-반복설정</a><br/><br/>
<a href="./listPsnSchdl.do?tabNo=2&menuId=${menuId}">CS-006. 개인일정-일일일정 조회</a><br/><br/>
<a href="./listPsnSchdl.do?tabNo=1&menuId=${menuId}">CS-007. 개인일정-주간일정 조회</a><br/><br/>
<a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=psn">CS-008. 개인일정-월간일정 조회</a><br/><br/>
<a href="javascript:dialog.open('viewPromPop','약속조회','./viewPromPop.do?menuId=${menuId}');">CS-009. 약속조회</a><br/><br/>
<a href="javascript:dialog.open('viewWorkPop','할일조회','./viewWorkPop.do?menuId=${menuId}');">CS-010. 할일조회</a><br/><br/>
<a href="javascript:dialog.open('viewEvntPop','행사조회','./viewEvntPop.do?menuId=${menuId}');">CS-011. 행사조회</a><br/><br/>
<a href="javascript:dialog.open('viewAnnvPop','기념일조회','./viewAnnvPop.do?menuId=${menuId}');">CS-012. 기념일조회</a><br/><br/>
<a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=dept">CS-013. 부서일정조회</a><br/><br/>
! CS-014. 즐겨찾기<br/><br/>
<a href="./grp/listGrp.do?menuId=${menuId}">CS-015. 그룹목록</a><br/><br/>
<a href="javascript:dialog.open('setGrpPop','그룹등록','./grp/setGrpPop.do?menuId=${menuId}');">CS-016. 그룹등록</a><br/><br/>
<a href="./grp/listGrpMng.do?menuId=${menuId}">CS-017. 그룹관리</a><br/><br/>
<a href="javascript:dialog.open('setAuthPop','권한설정','./grp/setAuthPop.do?menuId=${menuId}');">CS-018. 그룹관리-권한변경</a><br/><br/>
<a href="javascript:dialog.open('setGrpPop','그룹수정','./grp/setGrpPop.do?menuId=${menuId}&fnc=mod');">CS-019. 그룹관리-그룹명변경</a><br/><br/>
! CS-020. 그룹조회<br/><br/>
<a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=grp">CS-021. 그룹일정조회</a><br/><br/>
! CS-022. 그룹일정조회-등록그룹선택<br/><br/>
<a href="javascript:dialog.open('setGrpChoiPop','그룹선택','./grp/setGrpChoiPop.do?menuId=${menuId}');">CS-023. 그룹일정조회-그룹선택</a><br/><br/>
<a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=my">CS-024. 나의일정</a><br/><br/>
<a href="javascript:dialog.open('setMySetupPop','설정','./setMySetupPop.do?menuId=${menuId}');">CS-025. 나의일정-옵션설정</a><br/><br/>
<a href="./listSchdl.do?menuId=${menuId}">CS-026. 일정검색</a><br/><br/>
<a href="./setSchdlSend.do?menuId=${menuId}">CS-027. 일정보내기</a><br/><br/>
@ CS-028. 메일발송<br/><br/>
<a href="./setSchdlEnv.do?menuId=${menuId}">CS-029. 환경설정</a><br/><br/>
@ CS-030. 사용자선택<br/><br/>
<strong>- CS-031. 포틀릿-나의일정</strong><br/><br/>
<strong>- CS-032. 포틀릿-타인일정</strong><br/><br/>
<strong>- CS-033. 포틀릿-타인일정-조회대상설정</strong><br/><br/>
<strong>- CS-034. 포틀릿-달력</strong><br/><br/>

[관리자]
<br/><br/>
<a href="./adm/listCommAnnv.do?menuId=${menuId}">CS-035. 공통기념일-목록조회</a><br/><br/>
! CS-036. 일정조회<br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=psn">개인일정</a></dd></dl>
<dl><dd><a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=my">나의일정</a></dd></dl>
<dl><dd><a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=dept">부서일정</a></dd></dl>
<dl><dd><a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=comp">회사일정</a></dd></dl>
<dl><dd><a href="./setSchdlEnv.do?menuId=${menuId}">환경설정</a></dd></dl>
<dl><dd><a href="./setSchdlSend.do?menuId=${menuId}">일정보내기</a></dd></dl>
<dl><dd><a href="./grp/listGrp.do?menuId=${menuId}">그룹관리</a></dd></dl>
<dl><dd><a href="./listPsnSchdl.do?menuId=${menuId}&fncCal=grp">그룹일정</a></dd></dl>
<dl><dd><a href="./listSchdl.do?menuId=${menuId}">나의일정 검색</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listCommAnnv.do?menuId=${menuId}">공통기념일</a></dd></dl>
</div>
