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
<a href="./setBc.do?menuId=${menuId}">BS-001. 명함등록-기본정보</a><br/><br/>
! BS-002. 명함등록-추가정보<br/><br/>
<a href="javascript:dialog.open('findAdrBookPop','주소록 선택','./findAdrBookPop.do?menuId=${menuId}');">BS-003. 명함등록-주소록 선택</a><br/><br/>
<a href="javascript:dialog.open('findBcFldPop','폴더 선택','./findBcFldPop.do?menuId=${menuId}');">BS-004. 명함등록-폴더선택</a><br/><br/>
<a href="javascript:dialog.open('setClnsPop','친밀도 추가','./setClnsPop.do?menuId=${menuId}');">BS-005. 명함등록-친밀도추가</a><br/><br/>
<a href="./listBc.do?typ=L&menuId=${menuId}">BS-006. 개인명함-목록조회(LIST)</a><br/><br/>
<a href="javascript:dialog.open('copyBcPop','명함복사','./copyBcPop.do?menuId=${menuId}');">BS-007. 개인명함-명함복사</a><br/><br/>
<a href="./listBc.do?typ=C&menuId=${menuId}">BS-008. 개인명함-목록조회(명함)</a><br/><br/>
<a href="./viewBc.do?menuId=${menuId}">BS-009. 개인명함-명함조회-기본정보</a><br/><br/>
! BS-010. 개인명함-명함조회-추가정보<br/><br/>
<a href="./setBc.do?menuId=${menuId}&fnc=mod">BS-011. 개인명함-명함수정-기본정보</a><br/><br/>
! BS-012. 개인명함-명함수정-추가정보<br/><br/>
<a href="./listAdrBook.do?menuId=${menuId}">BS-013. 주소록-목록조회</a><br/><br/>
<a href="javascript:dialog.open('setAdrBookPop','개인주소록 등록','./setAdrBookPop.do?menuId=${menuId}');">BS-014. 주소록-등록</a><br/><br/>
<a href="javascript:dialog.open('findBcPop','명함선택','./findBcPop.do?menuId=${menuId}');">BS-015. 주소록-명함선택</a><br/><br/>
<a href="javascript:dialog.open('viewAdrBookPop','주소록 조회','./viewAdrBookPop.do?menuId=${menuId}');">BS-016. 주소록-조회</a><br/><br/>
<a href="./listPublBc.do?menuId=${menuId}">BS-017. 공개명함-목록조회</a><br/><br/>
<a href="javascript:dialog.open('sendEmailPop','이메일 발송','./sendEmailPop.do?menuId=${menuId}');">BS-018. 공개명함-관계자 메일발송</a><br/><br/>
<a href="./listBc.do?typ=L&menuId=${menuId}&fncAgnt=Y">BS-019. 대리명함-목록조회(LIST)</a><br/><br/>
<a href="./setAgntAdm.do?menuId=${menuId}">BS-020. 대리관리자 지정</a><br/><br/>
<a href="javascript:dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');">BS-021. 대리관리자 지정-조직도</a><br/><br/>
<a href="./listBc.do?typ=C&menuId=${menuId}&fncAgnt=Y">BS-022. 대리명함-목록조회(명함)</a><br/><br/>
<a href="./listBcFld.do?menuId=${menuId}">BS-023. 명함폴더</a><br/><br/>
<a href="javascript:dialog.open('setFldPop','폴더 등록','./setFldPop.do?menuId=${menuId}');">BS-024. 명함폴더-폴더추가</a><br/><br/>
<a href="javascript:dialog.open('setFldPop','폴더 수정','./setFldPop.do?menuId=${menuId}&fnc=mod');">BS-025. 명함폴더-폴더Rename</a><br/><br/>
<a href="./setBcScrn.do?menuId=${menuId}">BS-026. 화면설정</a><br/><br/>
<a href="./setBcBumk.do?menuId=${menuId}">BS-027. 자주찾는 명함관리</a><br/><br/>
<a href="./listBcBumk.do?menuId=${menuId}">BS-028. 자주찾는 명함조회</a><br/><br/>
<a href="./setBcInOut.do?menuId=${menuId}">BS-029. 명함 IN/OUT</a><br/><br/>
<a href="./listMetng.do?typ=C&menuId=${menuId}">BS-030. 관련미팅-목록</a><br/><br/>
<a href="./setMetng.do?typ=C&menuId=${menuId}">BS-031. 관련미팅-등록</a><br/><br/>
<a href="javascript:dialog.open('findBcPop','명함선택','./findBcPop.do?menuId=${menuId}');">BS-032. 관련미팅-명함지정</a><br/><br/>
<a href="./viewMetng.do?typ=C&menuId=${menuId}">BS-033. 관련미팅-조회</a><br/><br/>
<a href="./viewMetng.do?typ=C&menuId=${menuId}&fncAgnt=Y">BS-034. 대리관련미팅-조회</a><br/><br/>
! BS-035. 명함인쇄<br/><br/>
<strong>- BS-036. 명함인쇄-인쇄화면</strong><br/><br/>
<strong>- BS-037. 포틀릿-개인명함목록</strong><br/><br/>
<strong>- BS-038. 포틀릿-공개명함목록</strong><br/><br/>
<strong>- BS-039. 포틀릿-자주찾기</strong><br/><br/>
<strong>- BS-040. 포틀릿-명함검색</strong><br/><br/>
<strong>- BS-041. 포틀릿-명함검색-검색결과</strong><br/><br/>
<strong>- BS-042. 포틀릿-빠른추가</strong><br/><br/>

[관리자]
<br/><br/>
<a href="./adm/listAllBc.do?typ=C&menuId=${menuId}">BS-043. 전체명함 조회</a><br/><br/>
<a href="./adm/listAllMetng.do?typ=C&menuId=${menuId}">BS-044. 전체관련미팅 조회</a><br/><br/>
<a href="./adm/setMainBc.do?menuId=${menuId}">BS-045. 메인명함 설정</a><br/><br/>
<a href="./adm/setDupFrnd.do?menuId=${menuId}">BS-046. 중복지인 선택</a><br/><br/>
<a href="./adm/setSrchCond.do?menuId=${menuId}">BS-047. 검색조건 설정</a><br/><br/>
<a href="./adm/listMetingCls.do?menuId=${menuId}">BS-048. 관련미팅 분류 목록</a><br/><br/>
<a href="javascript:dialog.open('setMetngClsPop','관련미팅분류 등록','./adm/setMetngClsPop.do?menuId=${menuId}');">BS-049. 관련미팅 분류 등록</a><br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listPublBc.do?menuId=${menuId}">공개명함</a></dd></dl>
<dl><dd><a href="./listBc.do?typ=C&menuId=${menuId}">개인명함</a></dd></dl>
<dl><dd><a href="./listBc.do?typ=C&menuId=${menuId}&fncAgnt=Y">대리명함</a></dd></dl>
<dl><dd><a href="./setBcBumk.do?menuId=${menuId}">자주찾는명함</a></dd></dl>
<dl><dd><a href="./setBcScrn.do?menuId=${menuId}">화면설정</a></dd></dl>
<dl><dd><a href="./listMetng.do?typ=C&menuId=${menuId}">관련미팅목록</a></dd></dl>
<dl><dd><a href="./listMetng.do?typ=C&menuId=${menuId}&fncAgnt=Y">대리관련미팅</a></dd></dl>
<dl><dd><a href="./setAgntAdm.do?menuId=${menuId}">대리관리자 설정</a></dd></dl>
<dl><dd>명함인쇄</dd></dl>
<dl><dd><a href="./listBcFld.do?menuId=${menuId}">명함폴더</a></dd></dl>
<dl><dd><a href="./setBcInOut.do?menuId=${menuId}">명함(in/out)</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listAllBc.do?typ=C&menuId=${menuId}">전체명함조회</a></dd></dl>
<dl><dd><a href="./adm/listAllMetng.do?typ=C&menuId=${menuId}">전체관련미팅조회</a></dd></dl>
<dl><dd><a href="./adm/setMainBc.do?menuId=${menuId}">메인명함설정</a></dd></dl>
<dl><dd><a href="./adm/setDupFrnd.do?menuId=${menuId}">중복지인선택</a></dd></dl>
<dl><dd><a href="./adm/setSrchCond.do?menuId=${menuId}">명함검색조건설정</a></dd></dl>
<dl><dd><a href="./adm/listMetingCls.do?menuId=${menuId}">관련미팅분류</a></dd></dl>
</div>
