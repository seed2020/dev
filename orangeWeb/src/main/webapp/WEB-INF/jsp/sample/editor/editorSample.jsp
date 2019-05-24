<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%

%>
<strong>
SampleEditorCtrl.java<br/>
editorSample.jsp<br/><br/>
</strong>

<u:editor id="editor1" width="800" height="400" module="pt" value="${htmlValue}"
	/>
<br/>
<br/>
1. editor의 html을 히든 textarea에 세팅 : <a href="javascript:jellyEditor('editor1').prepare()">jellyEditor('editerID').prepare()</a> - submit 전 호출해야함<br/><br/>
2. 히든 textarea 값 <a href="javascript:alert(document.getElementById('editor1').value)">alert()</a><br/><br/>
3. editor에 html 세팅 : <a href="javascript:jellyEditor('editor1').setHtml('<p>hello ~</p>')">jellyEditor('editerID').setHtml(htmlText)</a><br/><br/>
4. editor의 html 가져오기 : <a href="javascript:alert(jellyEditor('editor1').getHtml())">jellyEditor('editerID').getHtml()</a><br/><br/>
5. 내용 입력 여부 체크 : <a href="javascript:alert(jellyEditor('editor1').empty())">jellyEditor('editerID').empty()</a><br/><br/>
5. 내용 변경 여부 체크 : <a href="javascript:alert(jellyEditor('editor1').isChanged())">jellyEditor('editerID').isChanged()</a> - prepare() 뒤에 초기화 됨<br/><br/>
<br/><br/><br/>