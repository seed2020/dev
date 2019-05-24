<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

function changeLeftArea(sel) {
	if (sel.value == '부서') {
		$('#leftArea1').show();
		$('#leftArea2').hide();
	} else {
		$('#leftArea1').hide();
		$('#leftArea2').show();
	}
}

function deptTreeClick(id, name) {
}

<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('deptTree');
	tree.onclick = 'deptTreeClick';
	tree.setRoot('ROOT', '이노비즈(주)');
	tree.setSkin("${_skin}");
	tree.add('ROOT', 'node1', '총무팀', 'F', '1');
	tree.add('ROOT', 'node2', '개발1팀', 'F', '2');
	tree.add('node2', 'node21', 'UI파트', 'F', '3');
	tree.add('node2', 'node22', '서버파트', 'F', '4');
	tree.add('ROOT', 'node3', '개발2팀', 'F', '5');
	tree.add('node3', 'node31', 'UI파트', 'F', '6');
	tree.add('node3', 'node32', '서버파트', 'F', '7');
	tree.add('ROOT', 'node4', '홍보1팀', 'F', '8');
	tree.add('ROOT', 'node5', '홍보2팀', 'F', '9');
	tree.add('ROOT', 'node6', '기술지원1팀', 'F', '10');
	tree.add('ROOT', 'node7', '기술지원2팀', 'F', '11');
	tree.add('ROOT', 'node8', '기술지원3팀', 'F', '12');
	tree.draw();
	tree.selectTree("node1");
	
	changeTab('pathTab','${tabNo}');
});
//-->
</script>

<div style="width:700px">
<form id="setBbAuthPop">

<u:titleArea outerStyle="height: 340px; overflow: hidden;" innerStyle="NO_INNER_IDV">
	<!-- LEFT -->
	<div style="float: left; width: 47%">
		<div style="padding:10px 0 0 10px;">
			<!-- FRONT -->
			<div class="front">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td class="frontinput"><select onchange="changeLeftArea(this);">
					<option>부서</option>
					<option>사용자그룹</option>
					</select></td>
				</tr>
				</tbody></table>
			</div>
			</div>
			<!-- TREE -->
			<u:titleArea id="leftArea1" outerStyle="width: 100%; height: 261px; overflow: auto; padding: 10px 0 0 0;" innerStyle="NO_INNER_IDV" noBottomBlank="true">
				<div id="deptTree" class="tree"></div>
			</u:titleArea>
			<!-- LIST -->
			<div id="leftArea2" class="listarea" style="display: none;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="34">
					<col>
				<tbody>
				<tr>
				<td class="head_ct">&nbsp;</td>
				<td class="head_ct">사용자그룹</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:249px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="33">
					<col>
				<tbody>
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk1" value="1" checked="false" /></td>
				<td class="body_ct">사용자그룹1</td>
				</tr>
				
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk1" value="2" checked="false" /></td>
				<td class="body_ct">사용자그룹2</td>
				</tr>
				
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk1" value="3" checked="false" /></td>
				<td class="body_ct">사용자그룹3</td>
				</tr>
				
				</tbody></table>
				</div>
			</div>
		</div>
	</div>
	
	<!-- CENTER -->
	<div class="left" style="float:left; width:6%; height: 312px;">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
		<td style="vertical-align: middle">
			<table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:buttonIcon href="javascript:" image="ico_wadd.png" titleId="cm.btn.add" alt="선택추가" /></td>
			</tr>
	
			<tr><td class="height5"></td></tr>
	
			<tr>
			<td><u:buttonIcon href="javascript:" image="ico_wminus.png" titleId="cm.btn.del" alt="선택삭제" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		</tbody></table>
	</div>
	
	<!-- RIGHT -->
	<div style="float: right; width: 47%">
		<div style="margin:0 auto 0 auto; padding:10px 10px 0 0;">
			<!-- FRONT -->
			<div class="front">
			<div class="front_left">
				<u:checkArea>
				<u:checkbox name="readAuth" value="Y" titleId="wv.cols.read" alt="조회" checked="true" inputClass="bodybg_lt" disabled="Y" />
				<u:checkbox name="survAuth" value="Y" titleId="wv.cols.surv" alt="투표" checked="false" inputClass="bodybg_lt" />
				</u:checkArea>
			</div>
			<div class="front_right">
				<table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td class="frontico"><u:buttonIcon titleId="cm.btn.up" alt="위로이동" onclick="" /></td>
				<td class="frontico"><u:buttonIcon titleId="cm.btn.down" alt="아래로이동" onclick="" /></td>
				</tr>
				</tbody></table>
			</div>
			</div>
			
			<!-- LIST -->
			<div class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="34">
					<col>
				<tbody>
				<tr>
				<td class="head_ct">&nbsp;</td>
				<td class="head_ct">선택된 대상</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:249px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="33">
					<col>
				<tbody>
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk2" value="1" checked="false" /></td>
				<td class="body_ct">개발1팀</td>
				</tr>
				
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk2" value="2" checked="false" /></td>
				<td class="body_ct">개발2팀</td>
				</tr>
				
				<tr>
				<td class="bodybg_ct"><u:checkbox name="chk2" value="3" checked="false" /></td>
				<td class="body_ct">사용자그룹1</td>
				</tr>
				
				</tbody></table>
				</div>
			</div>
		</div>
	</div>
	
	<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="red_stxt">- 권한은 위에서부터 순서대로 적용됩니다.</td>
		</tr>
		</tbody></table>
	</div>
	</div>
</u:titleArea>

<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.save" alt="저장" onclick="alert('${msg}');" auth="R" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>
