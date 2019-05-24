<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//상세보기
function viewBc(bcId) {
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewBcPop.do?menuId=${menuId}&bcId='+bcId);
};

<%// [버튼] 폴더등록 %>
function regFld(){
	var tree = getIframeContent('cdTree').getTreeData('reg');
	if(tree!=null){<%//pt.jsp.setCd.regFld=폴더등록%>
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.regFld"/>','./setFldPop.do?menuId=${menuId}&mode=reg&bcFldId='+tree.id);
	}
};

<%// [버튼] 폴더수정 %>
function modFld(){<%//pt.jsp.setCd.modFld=폴더수정%>
	var tree = getIframeContent('cdTree').getTreeData('mod');
	if(tree.id != 'ROOT'){
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.modFld"/>','./setFldPop.do?menuId=${menuId}&mode=mod&bcFldId='+tree.id);
	}
};

var selArrs;
//폴더선택
fnFldSelect = function(){
	var tree = getIframeContent('findBcFldFrm').getTreeData();
	if(tree != null && selArrs != null){
		if(tree.id == selArrs.bcFldId){
			alertMsg("wb.msg.copy.noDupFldNm");
			return;
		}
		if(selArrs.mode == 'copy' || selArrs.mode == 'move'){
			if(confirmMsg("wb.cfrm."+selArrs.mode+".fld")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
				callAjax('./transBcFldCopyAjx.do?menuId=${menuId}', {mode:selArrs.mode, abvFldId:tree.id, bcFldId:selArrs.bcFldId}, function(data){
					if(data.message!=null){
						alert(data.message);
					}
					if(data.result=='ok'){
						getIframeContent('cdTree').reload('./listBcFldFrm.do?menuId=${menuId}&bcFldId='+data.bcFldId);
					}
				});
			}
		}else if(selArrs.mode == 'copyBc' || selArrs.mode == 'moveBc'){
			if(confirmMsg("wb.cfrm."+selArrs.mode)) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
				callAjax('./transBcCopyAjx.do?menuId=${menuId}', {mode:selArrs.mode, bcFldId:tree.id , toBcIds:selArrs.toBcIds}, function(data){
					if(data.message!=null){
						alert(data.message);
					}
					if(data.result=='ok'){
						getIframeContent('cdList').reload('./listBcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId='+tree.id+'&listType=list');
						getIframeContent('cdTree').onSelectFld(tree.id);
						//var tree = TREE.getTree('cdTree');
						//tree.selectTree(tree.id);
					}
				});
			}
		}
	}
	
	dialog.close('findBcFldPop');
};

// 폴더 복사
function copyFld() {
	var tree = getIframeContent('cdTree').getTreeData('copy');
	if(tree.id != 'ROOT') selArrs= {mode:'copy',bcFldId : tree.id};
	else return;
	dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
};

// 폴더 이동
function moveFld() {
	var tree = getIframeContent('cdTree').getTreeData('move');
	if(tree.id != 'ROOT') selArrs= {mode:'move',bcFldId : tree.id};
	else return;
	dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
};

// 명함 선택[명함 등록]
function fnBcSelect(detlViewType){
	var objArr = getIframeContent(detlViewType+'Frm').fnSelBc();
	if(objArr == null ) return;
	var tree = getIframeContent('cdTree').getTreeData('reg');
	if(tree != null ){
		if(true/*confirmMsg("cm.cfrm.save")*/ ) {<%//cm.cfrm.save=저장하시겠습니까 ?%>
			callAjax('./transBcCopyAjx.do?menuId=${menuId}', {mode:'copyBc', bcFldId:tree.id , toBcIds:objArr}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				if(data.result=='ok'){
					getIframeContent('cdList').reload('./listBcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId='+tree.id+'&listType=list');
				}
			});
		}
		dialog.close('findBcPop');
	}
	
};

// 명함 등록
function regBc() {
	dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&fncMul=Y');
};

//명함삭제
function delBc(){
	var objArr = getIframeContent('cdList').fnSelBc();
	if(objArr != null){
		var tree = getIframeContent('cdTree').getTreeData('reg');
		if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
			callAjax('./transBcDelAjx.do?menuId=${menuId}', {toBcIds:objArr}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				if(data.result=='ok'){
					getIframeContent('cdList').reload('./listBcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId='+tree.id+'&listType=list');
				}
			});
		}
	}
};

// 명함 복사
function copyBc() {
	var objArr = getIframeContent('cdList').fnSelBc();
	if(objArr != null){
		selArrs= {mode:'copyBc',toBcIds : objArr};
		dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
	}
};

// 명함 이동
function moveBc() {
	var objArr = getIframeContent('cdList').fnSelBc();
	if(objArr != null){
		selArrs= {mode:'moveBc',toBcIds : objArr};
		dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}');
	}
};

<%// [버튼] 폴더삭제 %>
function delFld(){
	getIframeContent('cdTree').delFld();
};

/* function viewBc() {
	location.href="./viewBc.do?menuId=${menuId}";
} */

<%// [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldForm')){
		var $frm = $('#setFldForm');
		$frm.attr('action','./transFld.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
};

<%// [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(bcFldId){
	getIframeContent('cdTree').reload('./listBcFldFrm.do?menuId=${menuId}&bcFldId='+bcFldId);
	dialog.close('setFldDialog');
};

<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(id){
	/* var src = './listBcFrm.do?menuId=${menuId}';
	if(id !='ROOT'){
		src+= '&schFldTypYn=F&schFldId='+id;
	}
	$("#cdList").attr('src', src);
	return; */
	$("#cdList").attr('src', './listBcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId='+id+'&listType=list');
	//gEmptyRight = false;
};

//-->
</script>

<u:title titleId="wb.jsp.listBcFld.title" alt="명함폴더" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />

<u:title titleId="wb.jsp.listBcFld.fldTree" type="small" alt="폴더트리">
</u:title>

<u:titleArea frameId="cdTree" frameSrc="./listBcFldFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
<div class="titlearea">
	<div class="tit_right">
		<u:titleButton titleId="cm.btn.reg" alt="등록" onclick="regFld();" auth="W"/>
		<u:titleButton titleId="cm.btn.mod" alt="수정" onclick="modFld();" auth="W"/>
		<u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delFld();" auth="W"/>
		<u:titleButton titleId="cm.btn.copy" alt="복사" onclick="copyFld();" auth="W"/>
		<u:titleButton titleId="cm.btn.move" alt="이동" onclick="moveFld();" auth="W"/>
	</div>
</div>
	
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:title titleId="wb.jsp.listBcFld.bcList" type="small" alt="명함목록" />

<u:titleArea frameId="cdList" frameSrc="./listBcFrm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId=ROOT&listType=list&popYn=Y"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button titleId="wb.btn.bcReg" alt="명함등록" onclick="regBc();" auth="R" />
		<u:button titleId="wb.btn.bcDel" alt="명함삭제" onclick="delBc();" auth="R" />
		<u:button titleId="wb.btn.bcCopy" alt="명함복사" onclick="copyBc();" auth="R" />
		<u:button titleId="wb.btn.bcMove" alt="명함이동" onclick="moveBc();" auth="R" />
	</u:buttonArea>
	
</div>

<u:blank />

