<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
	function addOptions(arr){
		var $select = $('#sendEmailPop select[id="recvNmSelect"]');
		arr.each(function(index, optionVo){
			$select.append("<option value='"+escapeHtml(escapeValue(optionVo.va))+"'\>" + escapeHtml(escapeValue(optionVo.nm)) +"\</option>");
		});	
	};
	
	$(document).ready(function() {
		if('${cmEmailBVo.recvNm }' == '11'){
			var splitVa  = '${cmEmailBVo.recvNm }'.split(';');
			var arr = [];
			for(var i=0;i<splitVa.length-1;i++){
				arr.push({va:splitVa[i],nm:splitVa[i]});
			}
			if(arr.length > 0 ){
				addOptions(arr);
			}
		}
	});
</script>


<div style="width:700px">
	<form id="sendEmailPop">
	<u:listArea noBottomBlank="true">
	<tr>
		<td width="27%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="73%"><u:input id="subj" value="${cmEmailBVo.subj}" titleId="cols.subj" style="width: 493px;" /></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.sendr" alt="보낸사람" /></td>
		<td><u:input id="sendNm" value="${cmEmailBVo.sendNm}" titleId="cols.sendr" style="width: 493px;" /></td>
	</tr>

	<tr>
		<td rowspan="2" class="head_lt"><u:msg titleId="cols.recvr" alt="받는사람" /></td>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="230">
						<u:input id="recvr" value="" titleId="cols.recvr" style="width: 456px;" />
					</td>
					<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');" /></td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>

	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="230">
						<select id="recvNmSelect" size="3" style="width: 465px;">
							<c:forTokens var="recvNm" items="${cmEmailBVo.recvNm }" delims="|">
								<option value="${recvNm }">${recvNm }</option>
							</c:forTokens>
						</select>
					</td>
					<td style="padding-top: 2px; vertical-align: top;"><u:buttonS href="" titleId="cm.btn.del" alt="삭제" auth="M" /></td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2"><u:textarea id="cont" style="width:98%;height:200px;" value="${cmEmailBVo.cont }"/></td>
	</tr>
	</u:listArea>
	
	<%-- <u:listArea noBottomBlank="true">
	<tr>
		<td id="editor1Area" colspan="2">
			<u:editor id="editor1" width="697" height="300"
			module="bb" areaId="editor1Area" value="${cont}" /></td>
		</tr>
		</u:listArea> --%>
		<u:listArea>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
			<td colspan="3">
				<div class="attachbtn">
					<dl>
					<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
					<dd><a href="javascript:" class="sbutton button_small"><span>파일첨부</span></a><a href="javascript:" class="sbutton button_small"><span><img src="${_cxPth}/images/${_skin}/ico_delete.png"/>삭제</span></a></dd>
					</dl>
				</div>
				<c:forEach	var="file" items="${cmEmailFileDVoList }" varStatus="status">
				<div class="attacharea">
					<dl>
						<dd class="attach_check"><input type="checkbox" id="contactBack"/></dd>
						<dd class="attach_img"><img src="${_cxPth}/images/cm/ico_${file.fileExt }.png" onerror='this.src="${_cxPth}/images/cm/ico_file.png"'/></dd>
						<dd class="attach"><a href="javascript:">${file.dispNm }</a></dd>
					</dl>
				</div>
				</c:forEach>
			</td>
		</tr>
		</u:listArea>

		<u:buttonArea>
		<u:msg titleId="bb.msg.send.success" var="msg" alt="성공적으로 발송하였습니다." />
		<u:button titleId="cm.btn.send" onclick="alert('${msg}'); dialog.close(this);" alt="보내기" auth="R" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
		</u:buttonArea>

		</form>
	</div>
