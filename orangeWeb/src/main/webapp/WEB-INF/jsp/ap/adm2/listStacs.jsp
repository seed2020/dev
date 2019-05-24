<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${!empty param.tabNo}" var="tabNo" value="${param.tabNo}" elseValue="0" />
<script type="text/javascript">
<!--

function toggleDiv(div, id) {
	if (div.attr('id') == id) div.show();
	else div.hide();
}

function showDiv(div) {
	toggleDiv($('#stacsRead01'), div);
	toggleDiv($('#stacsRead02'), div);
	toggleDiv($('#stacsRead03'), div);
	toggleDiv($('#stacsRead04'), div);
}

<% // TREE %>
function pathGrpTreeClick() {
}

$(document).ready(function() {
	var tree = TREE.create('pathGrpTree');
	tree.onclick = 'pathGrpTreeClick';
	tree.setRoot('ROOT', '부서');
	tree.setSkin("${_skin}");
	tree.add('ROOT', 'node1', '총무팀', 'F', '1');
	tree.add('ROOT', 'node2', '개발1팀', 'F', '2');
	tree.add('node2', 'node21', 'UI파트', 'F', '3');
	tree.add('node2', 'node22', '서버파트', 'F', '4');
	tree.add('ROOT', 'node3', '개발2팀', 'F', '5');
	tree.add('node3', 'node31', 'UI파트', 'F', '6');
	tree.add('node3', 'node32', '서버파트', 'F', '7');
	tree.add('ROOT', 'node4', '홍보부', 'F', '8');
	tree.add('node4', 'node41', '홍보1팀', 'F', '8');
	tree.add('node4', 'node42', '홍보2팀', 'F', '9');
	tree.add('ROOT', 'node5', '기술지원1팀', 'F', '10');
	tree.add('ROOT', 'node6', '기술지원2팀', 'F', '11');
	tree.add('ROOT', 'node7', '기술지원3팀', 'F', '12');
	tree.draw();
	tree.selectTree("ROOT");
	
	setUniformCSS();
	changeTab('apvStacsTab','${tabNo}');
});
//-->
</script>

<u:title title="전자결재 통계" menuNameFirst="true" />

<u:tabGroup id="apvStacsTab" noBottomBlank="true">
	<u:tab id="apvStacsTab" areaId="apvStacsTab1Area" title="통계설정" on="true" />
	<u:tab id="apvStacsTab" areaId="apvStacsTab2Area" title="통계조회" />
</u:tabGroup>

<% // 통계설정 %>
<div id="apvStacsTab1Area" style="height: 400px; display: ;">
	<u:titleArea outerStyle="height: 400px; overflow:hidden;" innerStyle="padding:7px 10px 0 10px;" blueTop="true">
		<!-- LEFT -->
		<div style="float:left; width:43.5%;">
			<div style="margin:0 auto 0 auto; padding:10px 0 0 10px;">
				<u:listArea>
					<tr>
					<td><u:checkArea>
						<u:radio name="subDept" value="1" title="하위부서 포함" inputClass="bodybg_lt" checked="true" />
						<u:radio name="subDept" value="2" title="부서별 소계" inputClass="bodybg_lt" />
						</u:checkArea></td>
					</tr>
				</u:listArea>
				
				<u:titleArea outerStyle="height: 331px; overflow: auto;" innerStyle="NO_INNER_IDV">
					<div id="pathGrpTree" class="tree"></div>
				</u:titleArea>
			</div>
		</div>
		
		<!-- CENTER -->
		<div class="left" style="float:left; width:6%; height: 250px;">
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td style="vertical-align: middle">
				<table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td><u:buttonIcon href="javascript:" image="ico_right.png" titleId="cm.btn.selAdd" alt="선택추가" /></td>
				</tr>
	
				<tr><td class="height5"></td></tr>
	
				<tr>
				<td><u:buttonIcon href="javascript:" image="ico_left.png" titleId="cm.btn.selDel" alt="선택삭제" /></td>
				</tr>
				</tbody></table></td>
			</tr>
			</tbody></table>
		</div>
	
		<!-- RIGHT -->
		<div style="float:right; width:49.5%; height:274px;">
			<div style="margin:0 auto 0 auto; padding:10px 0 0 0;">
				<div class="listarea">
					<table class="listtable" border="0" cellpadding="0" cellspacing="1">
					<colgroup>
						<col width="33">
						<col>
					</colgroup>
					<tbody>
					<tr>
					<td class="head_ct">&nbsp;</td>
					<td class="head_ct">통계대상부서</td>
					</tr>
					</tbody></table>
	
					<div class="listbody" style="height:236px;">
					<table class="listtable" style="border-right:0; border-bottom:0;" border="0" cellpadding="0" cellspacing="1">
					<colgroup>
						<col width="32">
						<col>
					</colgroup>
					<tbody>
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="bodybg_ct"><u:checkbox name="chk1" value="1" checked="false" /></td>
					<td class="body_lt">홍보1팀</td>
					</tr>
					
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="bodybg_ct"><u:checkbox name="chk1" value="2" checked="false" /></td>
					<td class="body_lt">홍보2팀</td>
					</tr>
					</tbody></table>
					</div>
				</div>
	
			</div>
		</div>
		
		<div style="float:right; width:49.5%; height:250px; margin-top: 10px;">
			<u:listArea colgroup="27%,73%">
				<tr>
				<td class="head_lt">검색할 기간 입력</td>
				</tr>
				
				<tr>
				<td style="padding: 10px;">
					<table border="0" cellpadding="0" cellspacing="0"><tbody>
					<tr>
					<u:radio name="srchPrd" value="1" title="연도별검색" inputClass="bodybg_lt" />
					<td><select>
						<option>2014</option>
						<option>2013</option>
						<option>2012</option>
						<option>2011</option>
						<option>2010</option>
						</select></td>
					</tr>
					
					<tr>
					<u:radio name="srchPrd" value="2" title="기간별검색" inputClass="bodybg_lt" checked="true" />
					<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
						<tr>
						<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
							<tr>
							<td><u:calendar id="strtDt" /></td>
							</tr>
							</tbody></table></td>
						<td class="body_lt">~</td>
						<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
							<tr>
							<td><u:calendar id="endDt" /></td>
							</tr>
							</tbody></table></td>
						</tr>
						</tbody></table></td>
					</tr>
					</tbody></table></td>
				</tr>
			</u:listArea>
		</div>
	</u:titleArea>
</div>

<% // 통계조회 %>
<div id="apvStacsTab2Area" style="height: 290px; display: none;">
	<u:titleArea outerStyle="height: 400px; overflow:hidden;" innerStyle="padding:7px 10px 0 10px;" blueTop="true">
		<u:searchArea>
			<form name="searchForm" action="./listStacs.do" >
			<u:input type="hidden" id="menuId" value="${menuId}" />
			<u:input type="hidden" id="tabNo" value="${tabNo}" />
		
			<table class="search_table" cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td><table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="search_tit">통계 건수 종류</td>
				<td><u:checkArea>
					<u:radio name="stacsItem1" value="1" title="결재건수" inputClass="bodybg_lt" onclick="showDiv('stacsRead01');" checked="true" />
					<u:radio name="stacsItem1" value="2" title="발송건수" inputClass="bodybg_lt" onclick="showDiv('stacsRead02');" />
					<u:radio name="stacsItem1" value="3" title="대장건수" inputClass="bodybg_lt" onclick="showDiv('stacsRead03');" />
					</u:checkArea></td>
				<td class="width20"></td>
				<td class="search_tit">표시 형식</td>
				<td><u:checkArea>
					<u:radio name="stacsItem2" value="1" title="건수조회" inputClass="bodybg_lt" checked="true" />
					<u:radio name="stacsItem2" value="2" title="기간조회" inputClass="bodybg_lt" onclick="showDiv('stacsRead04');" />
					</u:checkArea></td>
				</tr>
				</table></td>
			<td><div class="button_search"><ul><li class="search"><a href="javascript:"><span>검색</span></a></li></ul></div></td>
			</tr>
			</table>
			</form>
		</u:searchArea>
		
		<!-- 결재건수 -->
		<div id="stacsRead01" style="display: ;">
			<div class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="251">
					<col width="250">
					<col width="250">
					<col>
				</colgroup>
				<tbody>
				<tr>
				<td class="head_ct">상위부서</td>
				<td class="head_ct">부서명</td>
				<td class="head_ct">진행중인 문서</td>
				<td class="head_ct">완료된 문서</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:#px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="250">
					<col width="250">
					<col width="250">
					<col>
				</colgroup>
				<tbody>
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct">홍보부</td>
				<td class="body_ct">홍보1팀</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				</tr>
				
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct">홍보부</td>
				<td class="body_ct">홍보2팀</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				</tr>
				</tbody></table>
				</div>
			</div>
		</div>
		
		<!-- 발송건수 -->
		<div id="stacsRead02" style="display: none;">
			<div class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="201">
					<col width="200">
					<col width="200">
					<col width="200">
					<col>
				</colgroup>
				<tbody>
				<tr>
				<td class="head_ct">상위부서</td>
				<td class="head_ct">부서명</td>
				<td class="head_ct">대내발송</td>
				<td class="head_ct">대외발송</td>
				<td class="head_ct">대내외발송</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:#px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="200">
					<col width="200">
					<col width="200">
					<col width="200">
					<col>
				</colgroup>
				<tbody>
				<tr>
			<td class="body_ct">홍보부</td>
			<td class="body_ct">홍보1팀</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			</tr>
			
			<tr>
			<td class="body_ct">홍보부</td>
			<td class="body_ct">홍보2팀</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			</tr>
				</tbody></table>
				</div>
			</div>
		</div>
		
		<!-- 대장건수 -->
		<div id="stacsRead03" style="display: none;">
			<div class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="201">
					<col width="200">
					<col width="200">
					<col width="200">
					<col>
				</colgroup>
				<tbody>
				<tr>
				<td class="head_ct">상위부서</td>
				<td class="head_ct">부서명</td>
				<td class="head_ct">등록대장</td>
				<td class="head_ct">접수대장</td>
				<td class="head_ct">배부대장</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:#px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="200">
					<col width="200">
					<col width="200">
					<col width="200">
					<col>
				</colgroup>
				<tbody>
				<tr>
			<td class="body_ct">홍보부</td>
			<td class="body_ct">홍보1팀</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			</tr>
			
			<tr>
			<td class="body_ct">홍보부</td>
			<td class="body_ct">홍보2팀</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			<td class="body_ct">0</td>
			</tr>
				</tbody></table>
				</div>
			</div>
		</div>
		
		<!-- 기간조회 -->
		<div id="stacsRead04" style="display: none;">
			<div class="listarea">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="201">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col>
				</colgroup>
				<tbody>
				<tr>
				<td class="head_ct">부서명</td>
				<td class="head_ct">1월</td>
				<td class="head_ct">2월</td>
				<td class="head_ct">3월</td>
				<td class="head_ct">4월</td>
				<td class="head_ct">5월</td>
				<td class="head_ct">6월</td>
				<td class="head_ct">7월</td>
				<td class="head_ct">8월</td>
				<td class="head_ct">9월</td>
				<td class="head_ct">10월</td>
				<td class="head_ct">11월</td>
				<td class="head_ct">12월</td>
				</tr>
				</tbody></table>
			
				<div class="listbody" style="height:#px;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<colgroup>
					<col width="200">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col width="66">
					<col>
				</colgroup>
				<tbody>
				<tr>
				<td class="body_ct">홍보1팀</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				</tr>
				
				<tr>
				<td class="body_ct">홍보2팀</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				<td class="body_ct">0</td>
				</tr>
				</tbody></table>
				</div>
			</div>
		</div>
	</u:titleArea>
</div>

