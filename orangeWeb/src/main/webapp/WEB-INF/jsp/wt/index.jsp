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

<a href="./listTask.do?menuId=${menuId}">작업목록</a><br/><br/>
<a href="./setTask.do?menuId=${menuId}">작업관리 등록</a><br/><br/>
@ 작업관리 등록-조직도<br/><br/>
<a href="./viewTask.do?menuId=${menuId}">작업관리 조회</a><br/><br/>
<a href="./listMyTask.do?menuId=${menuId}">나의작업</a><br/><br/>
<a href="./listMyTaskStat.do?menuId=${menuId}">나의작업 통계</a><br/><br/>
<a href="./setTaskInOut.do?menuId=${menuId}">작업 IN/OUT</a><br/><br/>
<a href="./listData.do?menuId=${menuId}">데이터관리(사용자)</a><br/><br/>

<br/>

[관리자]
<br/><br/>

<a href="./adm/listAllTask.do?menuId=${menuId}">전체작업목록</a><br/><br/>
<a href="./adm/listData.do?menuId=${menuId}">데이터관리(관리자)</a><br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listMyTask.do?menuId=${menuId}">나의 작업</a></dd></dl>
<dl><dd><a href="./listMyTaskStat.do?menuId=${menuId}">나의 작업 통계</a></dd></dl>
<dl><dd><a href="./listTask.do?menuId=${menuId}">작업 목록</a></dd></dl>
<dl><dd><a href="./listData.do?menuId=${menuId}">작업 데이터 관리</a></dd></dl>
<dl><dd><a href="./setTaskInOut.do?menuId=${menuId}">작업 IN/OUT</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listAllTask.do?menuId=${menuId}">전체작업목록</a></dd></dl>
<dl><dd><a href="./adm/listData.do?menuId=${menuId}">작업 데이터 관리</a></dd></dl>
</div>
