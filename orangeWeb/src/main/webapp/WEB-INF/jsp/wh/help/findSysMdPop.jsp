<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setSysMd";
%><style type="text/css">
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
<script type="text/javascript">
<!--<% // 시스템 모듈 선택 %>
function selectSysMdList(obj){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	if(obj.value=='') return;
	callAjax('./getSysMdListAjx.do?menuId=${menuId}', {mdPid:obj.value}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addSysMdList(target, data.whMdBVoList);
		}
	});
}<% // 시스템 모듈 추가 %>
function addSysMdList(target, whMdBVoList){
	if(whMdBVoList==null || whMdBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectSysMdList(this);" style="min-width:100px;">');
	var whMdBVo;
	buffer.push('<option value="">'+callMsg('cm.option.all')+'</option>');
	$.each(whMdBVoList, function(index, item){
		whMdBVo=item.map;
		buffer.push('<option value="'+whMdBVo.mdId+'">'+whMdBVo.mdNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('sysMdContainer');
		$(target).after(parent);		
		//setJsUniform(parent);
		var container=$('#sysMdContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('sysMdContainer');
		dialog.resize('findSysMdDialog');	
	}
}<% // [하단우측버튼] - 확인 %>
function setConfirm(){
	var arr=[];
	$('#sysMdArea option:selected').each(function(){
		if($(this).val()=='') return false;
		arr.push({id:$(this).val(), nm:$(this).text()});
	});
	parent.<%= callback%>(arr.length==0 ? null : arr[arr.length-1]);
	dialog.close('findSysMdDialog');
}
//-->
</script>
<div style="width:350px;">
<div id="sysMdContainer" style="width:100%;overflow-y:auto;"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><select onchange="selectSysMdList(this);" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? param.mdId : paramMdIds[paramStatus.index]}" /></c:forEach></select></li></c:forEach></ul>
</div>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.confirm" onclick="setConfirm();" alt="확인" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
