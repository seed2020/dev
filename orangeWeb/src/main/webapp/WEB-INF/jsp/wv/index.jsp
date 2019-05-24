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
<a href="./listSurv.do?menuId=${menuId}">SV-001. 설문목록</a><br/><br/>
<a href="./viewSurv.do?menuId=${menuId}">SV-002. 설문참여</a><br/><br/>
<a href="./viewSurvRes.do?menuId=${menuId}">SV-003. 결과보기</a><br/><br/>
<a href="javascript:dialog.open('listDeptStacsPop','부서별통계','./listDeptStacsPop.do?menuId=${menuId}');">SV-004. 결과보기-부서별통계</a><br/><br/>
<a href="javascript:dialog.open('listOendAnsPop','주관식 답변 리스트','./listOendAnsPop.do?menuId=${menuId}');">SV-005. 결과보기-주관식 답변 리스트</a><br/><br/>
<strong>- SV-006. 포틀릿-설문목록</strong><br/><br/>
<strong>- SV-007. 포틀릿-진행중인 설문</strong><br/><br/>

[관리자]
<br/><br/>
<a href="./adm/listSurv.do?menuId=${menuId}">SV-008. 설문목록</a><br/><br/>
<a href="./adm/setSurv.do?menuId=${menuId}">SV-009. 설문등록</a><br/><br/>
@ SV-010. 설문등록-권한설정<br/><br/>
<a href="javascript:dialog.open('setMulcQuesPop','객관식 질문 추가','./adm/setMulcQuesPop.do?menuId=${menuId}');">SV-011. 설문등록-객관식 질문추가</a><br/><br/>
<a href="javascript:dialog.open('setOendQuesPop','주관식 질문 추가','./adm/setOendQuesPop.do?menuId=${menuId}');">SV-012. 설문등록-주관식 질문추가</a><br/><br/>
<a href="./adm/setSurvQues.do?menuId=${menuId}">SV-013. 설문등록-설문질문 등록</a><br/><br/>
! SV-014. 설문등록-설문질문 등록-설문설명<br/><br/>
<a href="./adm/setSurv.do?menuId=${menuId}&fnc=mod">SV-015. 설문수정</a><br/><br/>
<a href="./adm/setSurvQues.do?menuId=${menuId}&fnc=mod">SV-016. 설문수정-설문질문 등록</a><br/><br/>
<a href="./adm/viewSurvRes.do?menuId=${menuId}">SV-017. 설문결과</a><br/><br/>
<a href="./adm/setSurvPolc.do?menuId=${menuId}">SV-018. 설문정책</a><br/><br/>
<a href="./adm/listSurvApvd.do?menuId=${menuId}">SV-019. 설문승인-목록</a><br/><br/>
<a href="./adm/viewSurvApvd.do?menuId=${menuId}">SV-020. 설문승인</a><br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listSurv.do?menuId=${menuId}">설문목록</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listSurv.do?menuId=${menuId}">설문조사</a></dd></dl>
<dl><dd><a href="./adm/listSurvApvd.do?menuId=${menuId}">신청중인 설문목록</a></dd></dl>
<dl><dd><a href="./adm/setSurvPolc.do?menuId=${menuId}">설문정책</a></dd></dl>
</div>
