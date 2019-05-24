<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="pageSuffix" test="${lstTyp eq 'L' }" value="" elseValue="Frm"/>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<script type="text/javascript">
<!--<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type=${fncMul == 'Y' ? 'checkbox' : 'radio' }]:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<%// 선택목록 리턴[docPid] %>
function selDocIds(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr, idNm);
	else return null;
};<%// 선택목록 리턴 %>
function selDocInfos(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArrs(arr, idNm);
	else return null;
};
<%// 배열에 담긴 목록 ID%>
function selRowInArr(rowArr, idNm){
	var objArr = [], $docId;
	for(var i=0;i<rowArr.length;i++){
		$docId = $(rowArr[i]).find("input[name='"+idNm+"']");
		if($docId.val()!=''){
			objArr.push($docId.val());
		}
	}
	return objArr;
};<%// 배열에 담긴 목록 배열%>
function selRowInArrs(rowArr, idNm){
	var atrbIds = ["docPid","fldId","fldNm","seculCd","docKeepPrdCd","ownrUid","ownrNm","clsList"];
	var objArr = [], $docId;
	var param = null;
	for(var i=0;i<rowArr.length;i++){
		$docId = $(rowArr[i]).find("input[name='"+idNm+"']");
		param = new ParamMap();
		for(var j=0;j<atrbIds.length;j++){
			if(atrbIds[j] == 'clsList'){//분류정보를 배열로 param에 담는다.
				var clsIds = $docId.attr('data-clsIds').split(',');
				var clsNms = $docId.attr('data-clsNms').split(',');
				if(clsIds.length == clsNms.length){//id와 name이 동일할 경우에만...
					var clsList = [];
					for(var k=0;k<clsIds.length;k++){
						clsList.push({id:clsIds[k],nm:clsNms[k]});
					}
					param.put(atrbIds[j],clsList);	
				}
			}else{
				param.put(atrbIds[j],$docId.attr('data-'+atrbIds[j]));	
			}
		}
		objArr.push(param);
	}
	return objArr;
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="5%"/><col width="25%"/><col width="15%"/><col width="*"/><col width="20%"/></colgroup>
	<tr id="headerTr">
		<td width="20px" class="head_bg">
			<c:if test="${fncMul != 'Y'}">&nbsp;</c:if>
			<c:if test="${fncMul == 'Y'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></c:if>
		</td>
		<td class="head_ct"><u:msg titleId="dm.cols.docNo" alt="문서번호"/></td>
		<td class="head_ct"><u:msg titleId="dm.cols.fld" alt="폴더"/></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일자"/></td>
	</tr>
	<c:forEach var="list" items="${dmDocLVoList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
			<td class="bodybg_ct">
				<c:set var="clsIds" />
				<c:forEach var="clsVo" items="${list.dmClsRVoList }" varStatus="status2">
					<c:set var="clsIds" value="${clsIds }${empty clsIds ? '' : ',' }${clsVo.clsId }"/>
				</c:forEach>
				<c:if test="${fncMul != 'Y'}"><u:radio id="check_${list.docId }" name="docChk" value="${list.docId }" checked="${status.first }" /><input type="hidden" name="docId" value="${list.docGrpId }" data-docPid="${list.docGrpId }" data-fldId="${list.fldId }" data-fldNm="${list.fldNm }" data-seculCd="${list.seculCd }" data-docKeepPrdCd="${list.docKeepPrdCd }" data-ownrUid="${list.ownrUid }" data-ownrNm="${list.ownrNm }" data-clsIds="${clsIds }" data-clsNms="${list.clsNm }"/></c:if>
				<c:if test="${fncMul == 'Y'}">
					<u:checkbox name="docChk" value="${list.docId }" checked="false" /><u:input type="hidden" name="docId" value="${list.docId }"/>
				</c:if>
			</td>
			<td class="body_lt" ><div class="ellipsis" title="${list.docNo }">${list.docNo }</div></td>
			<td class="body_lt" >${list.fldNm }</td>
			<td class="body_lt" ><div class="ellipsis" title="${list.subj }"><c:if test="${list.subYn eq 'Y' }"><u:icoCurr display="true" /></c:if>${list.subj }</div></td>
			<td class="body_ct" ><u:out value="${list.regDt }" type="longdate"/></td>
		</tr>
	</c:forEach>
	<c:if test="${empty dmDocLVoList }">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</table>
</div>
<u:blank />
<c:choose>
<c:when test="${frmYn eq 'Y' }"><div style="padding:10px;"><u:pagination noLeftSelect="true"/></div></c:when>
<c:otherwise><u:pagination /></c:otherwise>
</c:choose>
