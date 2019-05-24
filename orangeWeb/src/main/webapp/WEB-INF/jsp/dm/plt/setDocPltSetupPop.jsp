<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 저장 버튼 클릭 %>
function saveDocPltSetup(){
	var arr = [];
	$("#docPltSetupArea input[name='docPltSetupCheck']:checked").each(function(){
		arr.push($(this).val());
	});
	if(arr.length>5){<%
		// ap.plt.canSetUnder={0}개 까지 설정 가능 합니다.%>
		alertMsg('ap.plt.canSetUnder',['5']);
	} else if(arr.length>0){
		var result = false;
		callAjax("${_cxPth}/dm/plt/transDocPltSetupAjx.do?menuId=${menuId}", {pltId:'${param.pltId}', bxIds:arr.join(',')}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result){
			dialog.close('setupPltDialog');
		}
	}
}<%
// [순서조절:위로,아래로] %>
function moveDocBx(direction){
	var i, arr = getCheckedBxTrs("cm.msg.noSelect");
	if(arr==null) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $('#titleTr');
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
}<%
//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedBxTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#docPltSetupArea input[name='docPltSetupCheck']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
//-->
</script>
<div style="width:400px; padding-top:-10px;">
<div style="text-align:right;">
	<u:buttonIcon titleId="cm.btn.up" alt="위로이동" href="javascript:moveDocBx('up');" />
	<u:buttonIcon titleId="cm.btn.down" alt="아래로이동" href="javascript:moveDocBx('down');" />
</div>

<u:listArea id="docPltSetupArea" colgroup="7%,93%">
	<tr id="titleTr"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('docPltSetupArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="dm.jsp.search.doc.title" alt="문서조회" /></td>
	</tr>
<c:forEach items="${dmPltSetupDVoList}" var="dmPltSetupDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="bodybg_ct"><input type="checkbox" name="docPltSetupCheck" id="${dmPltSetupDVo.bxId}DocPltSetup"
			value="${dmPltSetupDVo.bxId}" ${dmPltSetupDVo.useYn=='Y' ? 'checked="checked"' : ''} /></td>
		<td class="body_lt"><label for="${dmPltSetupDVo.bxId}DocPltSetup"><u:out value="${dmPltSetupDVo.bxNm}" /></label></td>
	</tr>
</c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveDocPltSetup();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>