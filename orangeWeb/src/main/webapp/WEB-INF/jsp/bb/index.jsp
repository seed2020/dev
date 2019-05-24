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

<a href="./listBull.do?menuId=${menuId}">BB-001. 게시목록</a><br/><br/>
<a href="./setBull.do?menuId=${menuId}">BB-002. 게시등록</a><br/><br/>
<a href="javascript:dialog.open('findBbPop','게시판 선택','./findBbPop.do?menuId=${menuId}');">BB-003. 게시등록-게시판선택 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setBbAuthPop', '권한 설정', './setBbAuthPop.do?menuId=${menuId}');">BB-004. 게시등록-게시대상설정 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setFileBullPop','게시물 등록','./setFileBullPop.do?menuId=${menuId}');">BB-005. 파일게시등록 (팝업)</a><br/><br/>
<a href="./viewBull.do?menuId=${menuId}">BB-006. 게시물 조회</a><br/><br/>
<a href="./viewBull.do?menuId=${menuId}&fncFile=Y">BB-007. 파일게시물 조회</a><br/><br/>
<a href="javascript:dialog.open('sendEmailPop','메일보내기','/em/sendEmailPop.do?menuId=${menuId}');">BB-008. 게시물 조회-메일보내기 (팝업)</a><br/><br/>
<a href="./setReply.do?menuId=${menuId}">BB-009. 게시물 조회-답변작성</a><br/><br/>
<a href="javascript:dialog.open('setFileReplyPop','게시물 답변','./setFileReplyPop.do?menuId=${menuId}');">BB-010. 게시물 조회-답변-파일게시</a><br/><br/>
! BB-011. 게시물 조회-인쇄<br/><br/>
<a href="./listTmpBull.do?menuId=${menuId}">BB-012. 임시저장함-목록조회</a><br/><br/>
<a href="./setTmpBull.do?menuId=${menuId}">BB-013. 임시저장함-게시물수정</a><br/><br/>
<a href="./listSubmBull.do?menuId=${menuId}">BB-014. 게시상신함-목록조회</a><br/><br/>
<a href="./viewSubmBull.do?menuId=${menuId}">BB-015. 게시상신함-게시물조회</a><br/><br/>
<a href="./listDiscBull.do?menuId=${menuId}">BB-016. 심의함-목록조회</a><br/><br/>
<a href="./viewDiscBull.do?menuId=${menuId}">BB-017. 심의함-게시물조회</a><br/><br/>
<a href="./listMyBull.do?menuId=${menuId}">BB-018. 나의게시물-목록조회</a><br/><br/>
<a href="javascript:dialog.open('setMyBullPop','나의 게시물 설정','./setMyBullPop.do?menuId=${menuId}');">BB-019. 나의게시물-설정 (팝업)</a><br/><br/>
<a href="./listNewBull.do?menuId=${menuId}">BB-020. 최신게시물</a><br/><br/>
 
<br/>

[관리자]
<br/><br/>

<a href="./adm/listTbl.do?menuId=${menuId}">BB-021. 테이블관리-테이블 목록</a><br/><br/>
<a href="javascript:dialog.open('listTblBbPop','테이블 상세조회','./adm/listTblBbPop.do?menuId=${menuId}');">BB-022. 테이블관리-테이블 목록-테이블 상세조회 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setTblPop','테이블 등록','./adm/setTblPop.do?menuId=${menuId}&fnc=reg');">BB-023. 테이블관리-테이블 등록 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setTblPop','확장 테이블 등록','./adm/setTblPop.do?menuId=${menuId}&fnc=reg&fncEx=Y');">BB-024. 테이블관리-확장 테이블 등록 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setTblPop','확장 테이블 수정','./adm/setTblPop.do?menuId=${menuId}&fnc=mod&fncEx=Y');">BB-025. 테이블관리-확장 테이블 수정 (팝업)</a><br/><br/>
<a href="./adm/listBb.do?menuId=${menuId}">BB-026. 게시판관리-게시판목록</a><br/><br/>
<a href="./adm/setBb.do?menuId=${menuId}&fnc=reg">BB-027. 게시판관리-게시판등록</a><br/><br/>
<a href="./adm/setBb.do?menuId=${menuId}&fnc=mod">BB-028. 게시판관리-게시판수정</a><br/><br/>
<a href="./adm/setBb.do?menuId=${menuId}&fnc=reg&fncEx=Y">BB-029. 게시판관리-확장 게시판등록</a><br/><br/>
<a href="./adm/viewBb.do?menuId=${menuId}">BB-030. 게시판관리-확장 게시판조회</a><br/><br/>
<a href="javascript:dialog.open('setColmMngPop','컬럼관리','./adm/setColmMngPop.do?menuId=${menuId}');">BB-031. 게시판관리-확장 게시판조회-컬럼관리 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('setListOrdrPop','목록순서','./adm/setListOrdrPop.do?menuId=${menuId}');">BB-032. 게시판관리-확장 게시판조회-컬럼순서조정 (팝업)</a><br/><br/>
<a href="./adm/listBullCatMng.do?menuId=${menuId}">BB-033. 카테고리관리-목록조회</a><br/><br/>
<a href="javascript:dialog.open('setBullCatMngPop','게시물 카테고리 등록','./adm/setBullCatMngPop.do?menuId=${menuId}');">BB-034. 카테고리관리-카테고리 등록</a><br/><br/>
<a href="./adm/listBullMng.do?menuId=${menuId}">BB-035. 게시물관리-목록조회</a><br/><br/>
<a href="./adm/listDeptBb.do?menuId=${menuId}">BB-036. 부서함설정-목록조회</a><br/><br/>
<a href="javascript:dialog.open('setDeptBbPop','부서함 등록','./adm/setDeptBbPop.do?menuId=${menuId}');">BB-037. 부서함설정-등록 (팝업)</a><br/><br/>
<a href="./adm/setNewBull.do?menuId=${menuId}">BB-038. 최신게시물 설정</a><br/><br/>
<a href="javascript:dialog.open('findBullCatPop','게시물 카테고리그룹 선택','./adm/findBullCatPop.do?menuId=${menuId}');">게시물 카테고리그룹 선택</a><br/><br/>
<a href="javascript:dialog.open('findOrgcPop','조직도','./findOrgcPop.do?menuId=${menuId}');">조직도 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('findDeptPop','부서선택','./findDeptPop.do?menuId=${menuId}');">부서선택 (팝업)</a><br/><br/>
<a href="javascript:dialog.open('viewUserInfoPop','사용자정보','/bb/viewUserInfoPop.do?menuId=${menuId}');">사용자정보 (팝업)</a><br/><br/>

<div class="headbox">
<dl><dd>[사용자 메뉴]</dd></dl>
<dl><dd><a href="./listMyBull.do?menuId=${menuId}">나의게시물</a></dd></dl>
<dl><dd><a href="./listNewBull.do?menuId=${menuId}">최신게시물</a></dd></dl>
<dl><dd><a href="./listTmpBull.do?menuId=${menuId}">임시저장함</a></dd></dl>
<dl><dd><a href="./listSubmBull.do?menuId=${menuId}">게시상신함</a></dd></dl>
<dl><dd><a href="./listDiscBull.do?menuId=${menuId}">심의함</a></dd></dl>
<dl><dd><a href="./listBull.do?menuId=${menuId}">자유게시판</a></dd></dl>
<dl><dd><a href="./listBull.do?menuId=${menuId}&fncBb=2">공지사항</a></dd></dl>
<dl><dd><a href="./listBull.do?menuId=${menuId}&fncBb=3">경조사</a></dd></dl>
<dl><dd><a href="./listBull.do?menuId=${menuId}&fncBb=4">테스트게시판</a></dd></dl>
</div>

<br/>

<div class="headbox">
<dl><dd>[관리자 메뉴]</dd></dl>
<dl><dd><a href="./adm/listTbl.do?menuId=${menuId}">테이블관리</a></dd></dl>
<dl><dd><a href="./adm/listBb.do?menuId=${menuId}">일반게시판관리</a></dd></dl>
<dl><dd><a href="./adm/listBb.do?menuId=${menuId}&fncEx=Y">확장게시판관리</a></dd></dl>
<dl><dd><a href="./adm/listBullCatMng.do?menuId=${menuId}">카테고리관리</a></dd></dl>
<dl><dd><a href="./adm/listBullMng.do?menuId=${menuId}">게시물관리</a></dd></dl>
<dl><dd><a href="./listDiscBull.do?menuId=${menuId}">심의함</a></dd></dl>
<dl><dd><a href="./adm/listDeptBb.do?menuId=${menuId}">부서함설정</a></dd></dl>
<dl><dd><a href="./adm/setNewBull.do?menuId=${menuId}">최신게시물 설정</a></dd></dl>
</div>
