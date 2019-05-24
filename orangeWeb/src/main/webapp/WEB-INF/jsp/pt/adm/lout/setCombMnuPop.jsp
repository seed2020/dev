<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 우측 프레임 - 메뉴 클릭시 실행되는 함수로 - 여기서 사용안하며 메뉴관리에서만 사용됨, 스크립트 에러 방지용 %>
function openMnu(id, fldYn){
}
<%// [SELECT] - change 우측 상단 구분 - 변경시 실행 %>
function changeMnuGrp(mnuGrpId){
	getIframeContent('mnuGrpFrm').reload('./treeMnuFrm.do?menuId=${menuId}&mnuGrpId='+mnuGrpId);
}
<%// [아이콘 <, << ] %>
function addMnu(mode){
	var arr = getIframeContent('mnuGrpFrm').getSelectedNodeTrees(mode);
	getIframeContent('mnuCombFrm').addMnu(arr);
}
<%// [아이콘 >, >> ] %>
function removeMnu(mode){
	getIframeContent('mnuCombFrm').removeMnu(mode);
}
<%// [아이콘 △,▽ ] - 좌상단 %>
function moveComb(direction){
	getIframeContent('mnuCombFrm').moveComb(direction);
}
<%// [소버튼 좌하단] - 폴더등록/폴더수정/폴더삭제 %>
function mngFld(mode){
	var frame = getIframeContent('mnuCombFrm');
	if(mode=='del'){
		frame.mngFld(mode);
	} else {
		var extObj = frame.getTreeData(mode, "exts");
		if(extObj!=null){
			if(mode=='add'){
				dialog.open('setFldPopDialog', $("#btnAddFld").text(), './setFldPop.do?menuId=${menuId}&compId=${compId}&mode=add&type=pop');
			} else {
				var langObj = frame.getTreeData(mode, "rescs"), param="";
				if(langObj==null){
					if(extObj.rescId!=null && extObj.rescId!=''){
						param = "&mnuYn=N&rescId="+extObj.rescId;
					} else {
						param = "&mnuYn=Y&rescId="+extObj.mnuRescId;
					}
				}
				dialog.open('setFldPopDialog', $("#btnModFld").text(), './setFldPop.do?menuId=${menuId}&compId=${compId}&mode=mod&type=pop'+param);
				if(langObj!=null){
					var nm, va;
					$("#setFldPop input[name^='rescVa']").each(function(){
						nm = $(this).attr('name');
						va = langObj[nm.substring(nm.length-2)];
						if(va!=null && va!='') $(this).val(va);
					});
				}
			}
		}
		
	}
}
<%// [확인버튼 - 폴더등록/폴더수정 팝업] %>
function setFldPop(mode){
	if(validator.validate('setFldPop')){
		var $pop = $("#setFldPop"), values={};
		gLangs.each(function(index, lang){
			values[lang] = $pop.find("input[name='rescVa_"+lang+"']").val();
			if(lang=='${_lang}') values['cur'] = values[lang];
		});
		getIframeContent('mnuCombFrm').setFldPop(mode, values);
		dialog.close('setFldPopDialog');
	}
}
<%// [팝업 - 저장] : 메뉴조합구성 %>
function saveComb(){
	var dataString = getIframeContent('mnuCombFrm').getCombData();
	var $form = $("#combForm");
	$form.attr("action","./transComb.do");
	$form.attr("target","dataframe");
	$form.find("[name='dataString']").val(JSON.stringify(dataString));
	$form.submit();
}
<%// 저장 후 처리 %>
function doAfterSave(){
	dialog.close('setCombMnuPopDialog');
}
//-->
</script>
<div style="width:700px; height:650px; padding 10px;">

<form id="combForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="compId" value="${compId}" />
<input type="hidden" name="loutCatId" value="${param.loutCatId}" />
<input type="hidden" name="mnuLoutId" value="${param.mnuLoutId}" />
<input type="hidden" name="dataString" value="" />
</form>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:left; width:47%;"
	outerStyle="height:600px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">
	
	<u:title titleId="pt.jsp.setIconLout.mnuComb" alt="메뉴 조합" type="small">
		<u:titleIcon type="up" href="javascript:moveComb('up')" auth="A" />
		<u:titleIcon type="down" href="javascript:moveComb('down')" auth="A" />
	</u:title>
	
	<iframe id="mnuCombFrm" name="mnuCombFrm" src="./treeCombFrm.do?menuId=${menuId}&compId=${compId}&loutCatId=${param.loutCatId}&mnuLoutId=${param.mnuLoutId}" class="iframe_border" style="width:100%; height:525px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
	<u:buttonArea topBlank="true">
		<u:buttonS alt="폴더등록" href="javascript:mngFld('add');" titleId="pt.jsp.setCd.regFld" id="btnAddFld" auth="A" />
		<u:buttonS alt="폴더수정" href="javascript:mngFld('mod');" titleId="pt.jsp.setCd.modFld" id="btnModFld" auth="A" />
		<u:buttonS alt="폴더삭제" href="javascript:mngFld('del');" titleId="pt.jsp.setCd.delFld" id="btnDelFld" auth="A" />
	</u:buttonArea>
	
</u:boxArea>

<div style="float:left; width:6%; text-align:center; margin:250px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addMnu('all');"<u:elemTitle titleId="cm.btn.allAdd" alt="전체추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_allleft.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:addMnu('selected');"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeMnu('selected');"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeMnu('all');"<u:elemTitle titleId="cm.btn.allDel" alt="전체삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_allright.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<u:boxArea className="wbox" noBottomBlank="true"
	style="float:right; width:47%;"
	outerStyle="height:600px;overflow:hidden;"
	innerStyle="width:96%; margin:0 auto 0 auto; padding:10px 0 0 0;">
	
	<u:title titleId="pt.jsp.setIconLout.mnuGrp.title" alt="메뉴 그룹" type="small">
		<li class="txt"><u:msg titleId="cm.gubun" alt="구분" /></li>
		<li><select id="mnuGrpId" onchange="changeMnuGrp(this.value)"<u:elemTitle titleId="pt.jsp.setIconLout.mnuGrp.title" />>
		<c:forEach items="${ptMnuGrpBVoList}" var="ptMnuGrpBVo" varStatus="status">
		<u:option value="${ptMnuGrpBVo.mnuGrpId}" title="${ptMnuGrpBVo.rescNm}" /><u:set
			test="${status.first}" var="mnuGrpId" value="${ptMnuGrpBVo.mnuGrpId}" elseValue="${mnuGrpId}" />
		</c:forEach>
		</select></li>
	</u:title>
	
	<iframe id="mnuGrpFrm" name="mnuGrpFrm" src="./treeMnuFrm.do?menuId=${menuId}&mnuGrpId=${mnuGrpId}" class="iframe_border" style="width:100%; height:560px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	
</u:boxArea>

<u:buttonArea topBlank="true">
	<u:button alt="저장" href="javascript:saveComb();" titleId="cm.btn.save" auth="A" />
	<u:button alt="취소" onclick="dialog.close(this);" titleId="cm.btn.cancel" />
</u:buttonArea>

</div>