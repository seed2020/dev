<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%

%>
<script type="text/javascript">
<!--
function test(dialogId){
	
	// 파라미터 - id, title, url, param, width, height
	// param - ajax 호출시 JSON 으로 묶어서 data 라는 파라미터로 넘김
	// width, height 없으면 자동계산
	// - width 값이 없을 경우 컨텐츠 html을 div 테그로 싸고 style-width 값 줄것 (파폭에서 width에 문제가 있음)
	// 닫을때 dialog.close(id);
	dialog.open(dialogId, '테스트 다이얼로그('+dialogId+')','/sample/js/contentPop.do');
	
}

//-->
</script>

<strong>
SampleTagJsCtrl.java<br/>
sample/js/pop.jsp<br/>
sample/js/contentPop.jsp<br/><br/>
</strong>

<br/><br/>
<strong>
[팝업 Sample]
</strong>
<br/><br/>

<a href="javascript:test('dialogId_1')">팝업 열기 1</a><br/><br/>

<a href="javascript:test('dialogId_2')">팝업 열기 2</a>


