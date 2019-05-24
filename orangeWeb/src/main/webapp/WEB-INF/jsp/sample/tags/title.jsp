<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%

%>
<strong>
SampleTagJsCtrl.java<br/>
sample/tags/title.jsp<br/><br/>
</strong>


<u:title title="기본 큰 타이틀" />
<br/><br/>

<u:title title="기본 큰 타이틀 + 아이콘">
	<u:titleIcon type="up" onclick="moveUp()" />
	<u:titleIcon type="down" onclick="moveDown()" />
</u:title>
<br/><br/>

<u:title title="기본 큰 타이틀 + 버튼">
	<u:titleButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/>
</u:title>
<br/><br/>

<u:title title="기본 큰 타이틀 + 버튼 + 아이콘">
	<u:titleButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleIcon type="up" onclick="moveUp()" />
	<u:titleIcon type="down" onclick="moveDown()" />
</u:title>
<br/><br/>

<u:title title="작은 타이틀" type="small" />
<br/><br/>

<u:title title="작은 타이틀 + 아이콘" type="small" >
	<u:titleIcon type="up" onclick="moveUp()" />
	<u:titleIcon type="down" onclick="moveDown()" />
</u:title>
<br/><br/>

<u:title title="작은 타이틀 + 버튼" type="small" >
	<u:titleButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/>
</u:title>
<br/><br/>

<u:title title="작은 타이틀 + 버튼 + 아이콘" type="small" >
	<u:titleButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:titleIcon type="up" onclick="moveUp()" />
	<u:titleIcon type="down" onclick="moveDown()" />
</u:title>
<br/><br/>

