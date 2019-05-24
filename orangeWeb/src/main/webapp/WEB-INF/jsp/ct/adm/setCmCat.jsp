<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
<script type="text/javascript">
<!--

<%// [버튼] 폴더등록 %>
function regFld() {
	var tree = getIframeContent('cdTree').getTreeData('reg');
	if(tree!=null){<%//pt.jsp.setCd.regFld=폴더등록%>
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.regFld"/>','./setFldPop.do?menuId=${menuId}&mode=reg&catId='+tree.id);
	}
};
<%// [버튼] 폴더수정 %>
function modFld() {
	var tree = getIframeContent('cdTree').getTreeData('mod');
	if(tree.id != 'ROOT'){<%//pt.jsp.setCd.modFld=폴더수정%>
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.modFld"/>','./setFldPop.do?menuId=${menuId}&mode=mod&catId='+tree.id);
	}
};

<%// [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldForm')){
		var $frm = $('#setFldForm');
		$frm.attr('method','post');
		$frm.attr('action','./transFldSave.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
};

<%// [팝업:분류등록, 폴더수정] - 저장 버튼 %>
function saveCls(){
	if(validator.validate('setClsForm')){
		var $frm = $('#setClsForm');
		$frm.attr('method','post');
		$frm.attr('action','./transClsSave.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
};

<%// [버튼] 폴더삭제 %>
function delFld(){
	getIframeContent('cdTree').delFld();
};

<%// [버튼] 분류삭제 %>
function delCls() {
	getIframeContent('cdList').delCls();
};


<%// [메뉴팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(ctFldId){
	getIframeContent('cdTree').reload('./listCtFldFrm.do?menuId=${menuId}&ctFldId='+ctFldId);
	dialog.close('setFldDialog');
};

<%// [분류팝업:저장 후처리] - 분류 리로드 %>
function reloadClass(ctClsId){
	getIframeContent('cdList').reload('./listCtClsFrm.do?menuId=${menuId}&typ=C&catId='+ctClsId);
	dialog.close('setClsDialog');
};

<%// [버튼] 분류등록 %>
function regCls() {
	var tree = getIframeContent('cdTree').getTreeData('reg','cls');
	if(tree!=null && tree.id != 'ROOT'){<%//ct.jsp.setCd.regCls=분류등록%>
		dialog.open('setClsDialog','<u:msg titleId="ct.jsp.setCd.regCls"/>','./setClsPop.do?menuId=${menuId}&mode=reg&clsId='+tree.id);
	}
	
}

<%// [버튼] 분류수정 %>
function modCls() {
	var clsId = getIframeContent('cdList').clsSelId();
	if(clsId!=null){<%//ct.jsp.setCd.modCls=분류수정%>
		dialog.open('setClsDialog','<u:msg titleId="ct.jsp.setCd.modCls"/>','./setClsPop.do?menuId=${menuId}&mode=mod&clsId='+ clsId);
	}
}


<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(id){
	/* var src = './listBcFrm.do?menuId=${menuId}';
	if(id !='ROOT'){
		src+= '&schFldTypYn=F&schFldId='+id;
	}
	$("#cdList").attr('src', src);
	return; */
	$("#cdList").attr('src', './listCtClsFrm.do?menuId=${menuId}&typ=C&catId='+id);
	//gEmptyRight = false;
};



$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.setCmCat.title" alt="카테고리 관리" menuNameFirst="true"/>

<u:boxArea className="gbox" outerStyle="height:400px;padding:9px 12px 0 10px;" innerStyle="NO_INNER_IDV">

<!-- LEFT -->
<div class="left" style="width:49%; height: 400px;">

<u:title titleId="ct.jsp.setCmCat.subtitle01" type="small" alt="폴더">
	<u:titleButton titleId="cm.btn.reg" alt="등록" onclick="regFld();"
	/><u:titleButton titleId="cm.btn.mod" alt="수정" onclick="modFld();"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delFld();" />
</u:title>

 <u:titleArea frameId="cdTree" frameSrc="./listCtFldFrm.do?menuId=${menuId}"
	outerStyle="height: 360px;" 
	innerStyle="NO_INNER_IDV;"
	frameStyle="width:100%; height:360px; overflow:auto;" />
 
</div>

<!-- RIGHT -->
<div class="right" style="width:49.8%; height: 400px;">

<u:title titleId="ct.jsp.setCmCat.subtitle02" type="small" alt="분류">
	<u:titleButton titleId="cm.btn.reg" alt="등록" onclick="regCls();"
	/><u:titleButton titleId="cm.btn.mod" alt="수정" onclick="modCls();"
	/><u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delCls();" />
</u:title>


 <u:titleArea frameId="cdList" frameSrc="./listCtClsFrm.do?menuId=${menuId}"
	outerStyle="height: 360px;" 
	innerStyle="NO_INNER_IDV;"
	frameStyle="width:100%; height:360px; overflow:auto;" />


</div>

</u:boxArea>

