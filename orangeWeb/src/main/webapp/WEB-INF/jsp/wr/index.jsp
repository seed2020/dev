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

<a href="./listResc.do?typ=I&menuId=${menuId}">RS-001. 자원현황(이미지)</a><br/><br/>
<a href="./listResc.do?typ=L&menuId=${menuId}">RS-002. 자원현황(목록)</a><br/><br/>
<a href="./listRezv.do?menuId=${menuId}">RS-003. 예약목록</a><br/><br/>
<a href="javascript:dialog.open('viewRescPop','자원상세조회','./viewRescPop.do?menuId=${menuId}');">RS-004. 자원상세조회</a><br/><br/>
<a href="./viewRezv.do?menuId=${menuId}">RS-005. 자원예약 상세조회</a><br/><br/>
<a href="./listRezv.do?menuId=${menuId}&fncMy=Y">RS-006. 본인예약목록</a><br/><br/>
<a href="./setRezv.do?menuId=${menuId}">RS-007. 자원예약</a><br/><br/>
<a href="javascript:dialog.open('listRezvStatPop','예약현황','./listRezvStatPop.do?menuId=${menuId}');">RS-008. 예약현황</a><br/><br/>
<a href="./listRezvStat.do?menuId=${menuId}">RS-009. 전체예약현황</a><br/><br/>
<a href="./listRezvSrch.do?menuId=${menuId}">RS-010. 예약자원검색</a><br/><br/>

<br/>

[관리자]
<br/><br/>

<a href="./adm/listResc.do?typ=I&menuId=${menuId}">RS-011. 자원현황</a><br/><br/>
<a href="javascript:dialog.open('listRescKndPop','자원종류','./adm/listRescKndPop.do?menuId=${menuId}');">RS-012. 자원종류</a><br/><br/>
<a href="javascript:dialog.open('setRescPop','자원등록','./adm/setRescPop.do?menuId=${menuId}');">RS-013. 자원등록</a><br/><br/>
<a href="javascript:dialog.open('setRescAdmPop','관리자선택','./adm/setRescAdmPop.do?menuId=${menuId}');">RS-014. 자원등록 – 관리자 선택</a><br/><br/>
<a href="javascript:dialog.open('viewRescPop','자원상세조회','./adm/viewRescPop.do?menuId=${menuId}');">RS-015. 자원상세조회</a><br/><br/>
<a href="./listRezvDisc.do?menuId=${menuId}">RS-016. 예약심의목록</a><br/><br/>
<a href="./viewRezvDisc.do?menuId=${menuId}">RS-017. 자원예약심의</a><br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listResc.do?typ=I&menuId=${menuId}">자원현황</a></dd></dl>
<dl><dd><a href="./listRezv.do?menuId=${menuId}">예약목록</a></dd></dl>
<dl><dd><a href="./listRezvStat.do?menuId=${menuId}">예약현황</a></dd></dl>
<dl><dd><a href="./listRezvSrch.do?menuId=${menuId}">예약자원검색</a></dd></dl>
<dl><dd><a href="./listRezvDisc.do?typ=I&menuId=${menuId}">예약심의</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listResc.do?typ=I&menuId=${menuId}">자원현황</a></dd></dl>
</div>
