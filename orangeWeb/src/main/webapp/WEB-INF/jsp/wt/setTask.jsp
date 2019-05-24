<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="작업관리 화면 뷰 작업?" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="작업관리 화면의 뷰 작성 및 링크 작업" elseValue="" />
<u:set test="${fnc == 'mod'}" var="regr" value="홍길동" elseValue="" />
<u:set test="${fnc == 'mod'}" var="regDt" value="2014-01-13" elseValue="" />
<u:set test="${fnc == 'mod'}" var="ddln" value="2014-01-16" elseValue="" />
<u:set test="${fnc == 'mod'}" var="strtDt" value="2014-01-14" elseValue="" />
<u:set test="${fnc == 'mod'}" var="taskr" value="홍길동(담당), 김건모(담당), 신승훈(담당)" elseValue="" />

<u:msg titleId="wt.jsp.setTask.${fnc}.title" var="title" alt="작업등록/작업수정" />
<u:title title="${title}" alt="작업등록/작업수정" menuNameFirst="true"/>

<form id="setTask">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3"><u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 98%" /></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="32%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="ddln" value="${ddln}" titleId="cols.ddln" style="width:80px;" className="input_center" /></td>
		<td><u:calendar id="ddln" /></td>
		</tr>
		</tbody></table></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
	<td width="32%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="strtDt" value="${strtDt}" titleId="cols.strtDt" style="width:80px;" className="input_center" /></td>
		<td><u:calendar id="strtDt" /></td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
	<td><select>
		<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />
		<option>시작안함</option>
		<option${selected}>진행</option>
		<option>완료</option>
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
	<td><select>
		<option${selected}>낮음</option>
		<option>중간</option>
		<option>높음</option>
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td colspan="3"><select>
		<option>0%</option>
		<option>10%</option>
		<option>20%</option>
		<option>30%</option>
		<option>40%</option>
		<option>50%</option>
		<option>60%</option>
		<option${selected}>70%</option>
		<option>80%</option>
		<option>90%</option>
		<option>100%</option>
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.taskr" alt="작업자" />
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<u:radio name="role" value="R" titleId="wt.option.roleR" alt="R(담당)" inputClass="bodybg_lt" checked="true" />
		<u:radio name="role" value="A" titleId="wt.option.roleA" alt="A(책임)" inputClass="bodybg_lt" />
		<u:radio name="role" value="C" titleId="wt.option.roleC" alt="C(협의)" inputClass="bodybg_lt" />
		<u:radio name="role" value="I" titleId="wt.option.roleI" alt="I(참조)" inputClass="bodybg_lt" />
		</tr>
		</tbody></table>
		
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td style="padding-left: 10px;" class="body_lt">성명 | 부서 | 직급 | 역할</td>
		</tr>
		</tbody></table>
		
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select size="5" style="width:360px; height:80px;">
			<option>홍길동 | 총무팀 | 부장 | 담당</option>
			<option>심수봉 | 총무팀 | 과장 | 담당</option>
			<option>김윤아 | 개발팀 | 대리 | 담당</option>
			</select></td>
		<td style="vertical-align: top; padding-top: 2px;">
			<div style="margin-bottom: 2px;"><u:buttonS href="" titleId="cm.btn.add" alt="추가" /></div>
			<div style="margin-bottom: 2px;"><u:buttonS href="" titleId="cm.btn.del" alt="삭제" /></div>
			</td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td colspan="4"><u:editor id="editor1" width="990" height="300" module="wc" value="${cont}" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3"><div class="attachbtn">
		<dl>
		<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
		<dd><a href="javascript:" class="sbutton button_small"><span>파일첨부</span></a><a href="javascript:" class="sbutton button_small"><span><img src="${_cxPth}/images/${_skin}/ico_delete.png"/>삭제</span></a></dd>
		</dl>
		</div></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:history.go(-1);" alt="저장" auth="R" />
	<u:button titleId="cm.btn.cancel" href="javascript:history.go(-1);" alt="취소" />
</u:buttonArea>

</form>

