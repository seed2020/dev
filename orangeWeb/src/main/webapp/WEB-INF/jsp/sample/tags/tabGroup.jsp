<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%

%>
<strong>
SampleTagJsCtrl.java<br/>
sample/tags/tabGroup.jsp<br/><br/>
</strong>

<u:tabGroup>
	<u:tab titleId="cm.btn.addReg" areaId="tabView1" on="true" alt="추가등록" />
	<u:tab titleId="cm.btn.selDel" areaId="tabView2" alt="선택삭제" />
</u:tabGroup>
<br/><br/><br/>

<u:tabGroup>
	<u:tab titleId="cm.btn.addReg" areaId="tabView1" on="true" alt="추가등록" />
	<u:tab titleId="cm.btn.selDel" areaId="tabView2" alt="선택삭제" />
	<u:tabText title="표시텍스트" onclick="alertMsg('cm.msg.file.fileNotFound')" />
</u:tabGroup>
<br/><br/><br/>

<u:tabGroup>
	<u:tab titleId="cm.btn.addReg" areaId="tabView1" on="true" alt="추가등록" />
	<u:tab titleId="cm.btn.selDel" areaId="tabView2" alt="선택삭제" />
	<u:tabText title="표시텍스트" onclick="alertMsg('cm.msg.file.fileNotFound')" />
	<u:tabButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:tabButton titleId="cm.btn.selDel" onclick="selDel()"
	/>
</u:tabGroup>
<br/><br/><br/>
<u:tabGroup>
	<u:tab titleId="cm.btn.addReg" areaId="tabView1" on="true" alt="추가등록" />
	<u:tab titleId="cm.btn.selDel" areaId="tabView2" alt="선택삭제" />
	<u:tabText title="표시텍스트" onclick="alertMsg('cm.msg.file.fileNotFound')" />
	<u:tabButton titleId="cm.btn.addReg" onclick="addReg()"
	/><u:tabButton titleId="cm.btn.selDel" onclick="selDel()"
	/><u:tabIcon type="up" onclick="moveUp()" />
	<u:tabIcon type="down" onclick="moveDown()" />
</u:tabGroup>
<br/><br/><br/>
