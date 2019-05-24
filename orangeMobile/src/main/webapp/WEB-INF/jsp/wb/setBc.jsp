<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ownerUid="${listPage eq 'listBc' ? wbBcBVo.regrUid : ''}"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:set test="${!empty wbBcBVo.publTypCd}" var="publTypCd" value="${wbBcBVo.publTypCd}" elseValue="priv" />
<u:set var="urlPrefix" test="${listPage eq 'listPubBc'}" value="/wb/pub" elseValue="/wb"/>
<script type="text/javascript">
//<![CDATA[

<%//지정인 목록 리턴 %>
function getApntrData(){
	var $view = $("#apntrList"), arr = [];
	$view.find("div.apntrArea").each(function(){
		arr.push({userUid:$(this).find("input[id='userUid']").val(),rescNm:$(this).find("input[id='rescNm']").val()});
	});
	return arr.length==0 ? null : arr;
}

//여러명의 사용자 선택
function openMuiltiUser(){
	var oldApntrData = getApntrData();
	var param = {selected:oldApntrData};
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) {
			$("#apntrList").empty();
			$('#apntrUserMsg').html('');
			$('#apntrUserMsg').hide();
		}else{
			var $area = $("#setRegForm #apntrList");
			$area.html("");
			var buffer = [];
			arr.each(function(index, data){
				buffer.push('<div class="apntrArea">');
				buffer.push('<input type="hidden" id="userUid" name="bcApntrUid" value="'+data.userUid+'"/>');
				buffer.push('<input type="hidden" name="userNm" id="rescNm" value="'+data.rescNm+'" />');
				buffer.push('<input type="hidden" id="updateYn" name="updateYn" value="'+fnUserCheck(data.userUid)+'"/>');
				buffer.push('</div>');
			});
			$area.html(buffer.join(''));
			var userNms = $area.find("input[id='rescNm']");
			var msg = '<u:msg titleId="wb.jsp.viewBc.apntr" arguments="'+userNms.eq(0).val()+','+(userNms.length-1)+'"/>'
			$('#apntrUserMsg').html(msg);
			$('#apntrUserMsg').show();
		}
	});
};

//수정시 지정인목록을 비교하여 수정여부 체크
function fnUserCheck( userUid ){
	<c:forEach var="list" items="${wbBcBVo.wbBcApntrRVoList }" varStatus="status">
		if("${list.bcApntrUid }" == userUid){
			return 'Y';
		}
	</c:forEach>
	return 'N';
};

function fnSetScre(cd)
{
	$('#publTypCd').val(cd);
	$('dd.etr_se_lt span').text($("div.etr_open2 dd[data-schCd='"+cd+"']").text());
	$("div.etr_open2").hide();
	
	$('dd.apntPubl').hide();
	if(cd == 'apntPubl'){
		$('dd.apntPubl').show();
	}else{
		$('#apntrUserMsg').hide();
	}
}

var holdHide = false;
$(document).ready(function() {
	fnSetScre('${publTypCd}');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("div.etr_open2").hide();
	});
});


<% //저장 %>
function save(){
	if($("#bcNm").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [이름]
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.nm"], function(){
			$("#setRegForm input[name='bcNm']").focus();
		});
		return;
	}
	if($("#setRegForm input[name='publTypCd']").val() == 'apntPubl'){
		if($("input[id='userUid']").length == 0){
			$m.dialog.alert("<u:msg titleId='wb.cfrm.apntr'/>");
			return;	
		}
	}else{
		$("#apntrList").empty();
		$('#apntrUserMsg').html('');
	}
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#setRegForm');
			$m.nav.post($form);
		}
	});
}


//동명이인 조회 팝업
function fnPwsmSearchPop(bcNm){
	$m.nav.next(event, '/wb/findBcPwsmSub.do?menuId=${menuId}&setPage=${setPage}&typ=${param.typ}&bcId=${param.bcId}&schCat=pwsmName${agntParam}&schWord='+ encodeURIComponent(bcNm));
};

//동명이인 중복체크
function fnPwsmCheck(){
	if($('#bcNm').val() == ''){
		$m.msg.alertMsg("wb.input.check.require.bcNm",'<u:msg titleId="cols.nm" alt="이름" />');
		return;
	}
	$m.ajax('/wb/findPwsmCheck.do?menuId=${menuId}&bcId=${param.bcId}', {bcNm:$('#bcNm').val(),schBcRegrUid:'${schBcRegrUid}'}, function(data){
		if(data.message !=null){
			$m.dialog.alert(data.message);
		}
		if(data.result == 'popup'){
			fnPwsmSearchPop($("input[name='bcNm']").val());
		}
	});
};

//file-tag 에서 사용
var gFileTagMap = {};
function resetFileTag(id){
	var html = gFileTagMap[id];
	if(html!=null){
		var $file = $("#"+id+"File");
		$file.before(html);
		$file.remove();
		$("#"+id+"FileView").val('');
	}
};
function setFileTag(id, value, handler, exts){
	var viewId = id+'FileView';
	if(value==null) value = "";
	else {
		var p = value.lastIndexOf('\\');
		if(p>0) value = value.substring(p+1);
	}
	var $view = $("#"+viewId);
	var oldValue = $view.val();
	$view.val(value);
	
	if(exts!=null && exts!="" && value!=""){
		if(oldValue!=value){//IE에서 클릭했을때 이벤트 타는 버그 고침
			var va = value.toLowerCase();
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				$m.msg.alertMsg("cm.msg.attach.not.support.ext",[exts]);
				resetFileTag(viewId.substring(0, viewId.length-8));
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), null);
			} else {
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
			}
		}
	} else {
		if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
	}
};
function setFileInfo(id , va){
	if(va == null ) {
		resetFileTag(id);
		return;
	};
	var $form = $('#photoImageForm');
	$form.find("input[name='photo']").remove();
	var $file = $("#"+id+"File");
	var html = $file[0].outerHTML;
	$file.before(html);
	$form.append($file);
	$form.find("input[id='"+id+"File']").attr('id','photo');
	//resetFileTag(id);
	$m.nav.post($form);
	$form.find("input[name='photo']").remove();
};
<%//사진 변경 - 팝업 오픈 %>
function setImagePop(){
	$m.dialog.open({
		id:'setImageDialog',
		title:'<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />',
		url:'/wb/setImagePop.do?menuId=${menuId}&bcId=${param.bcId}',
	});
}
//사진 미리보기
function setImageReview(filePath){
	$('#bcImage').attr("src","./viewImage.do?menuId=${menuId}&fileDir="+filePath);
	$('#photoTempPath').val(filePath);
	$('#photoDelBtn').show();
}
function delImage(){
	$('#bcImage').attr("src","${_cxPth}/images/blue/noimg2.png");
	$('#photoTempPath').val('');
	
	<c:if test="${!empty wbBcBVo && !empty wbBcBVo.wbBcImgDVo.imgPath}">
	$m.ajax('${_cxPth}${urlPrefix}/transImageDelAjx.do?menuId=${menuId}', {bcId:'${wbBcBVo.wbBcImgDVo.bcId}'}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
	});
    </c:if>
}
function findFldPop(){
	$m.nav.next(event, "${urlPrefix}/findBcFldSub.do?menuId=${menuId}${agntParam }");
}

//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<c:if test="${writeAuth == 'Y'}">
            <dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setRegForm" name="setRegForm" enctype="multipart/form-data" action="/wb/${transPage}Post.do?menuId=${menuId}">
	<input type="hidden" id="typ" name="typ" value="${param.typ}" />
	<c:if test="${!empty param.bcId}">
		<input type="hidden" id="bcId" name="bcId" value="${param.bcId}" />
	</c:if>
	<c:if test="${!empty param.toBcId}">
		<input type="hidden" id="toBcId" name="toBcId" value="${param.toBcId}" />
	</c:if>
	<c:if test="${!empty schBcRegrUid}">
		<input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	<input type="hidden" id="listPage"  name="listPage" value="${urlPrefix }/${listPage}.do?menuId=${menuId}&${nonPageParams}${agntParam}" />
	<input type="hidden" id="viewPage"  name="viewPage" value="${urlPrefix }/${viewPage}.do?${params}" />
	
    <input type="hidden" id="dftCntcTypCd" name="dftCntcTypCd" value="compPhon"/>
    <input type="hidden" name="publTypCd" id="publTypCd" value="${publTypCd}"/>
    
    <input type="hidden" id="photoTempPath" name="tempDir" value="${!empty param.toBcId ? wbBcBVo.wbBcImgDVo.imgPath : ''}"/>
    
	<div id="apntrList">
		<c:if test="${wbBcBVo.publTypCd eq 'apntPubl' }">
			<c:forEach var="list" items="${wbBcBVo.wbBcApntrRVoList }" varStatus="status">
				<div class="apntrArea">
					<input type="hidden" id="userUid" name="bcApntrUid" value="${list.bcApntrUid }"/>
					<input type="hidden" name="userNm" id="rescNm" value="${list.userNm }" />
					<input type="hidden" id="updateYn" name="updateYn" value="Y"/>
				</div>
			</c:forEach>
		</c:if>
	</div>
    
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_bodytit">
            <div class="icotit_dot"><u:msg titleId="or.txt.photo" alt="사진" /></div>
            <u:set var="photoMsgId" test="${empty wbBcBVo.wbBcImgDVo.imgWdth }" value="cm.btn.add" elseValue="cm.btn.chg"/>
            <m:fileBtn id="photo" titleId="${photoMsgId }" alt="변경" exts="gif,jpg,jpeg,png,tif" onchange="setFileInfo" delBtn="delImage"/>
        </dd>
        <dd class="etr_body" >
        	<div style="width:98px;height:120px;padding:3px;float:left;overflow:hidden;">
        	<c:choose>
				<c:when test="${empty wbBcBVo.wbBcImgDVo.imgPath}"><img id="bcImage" src="${_cxPth}/images/blue/noimg2.png" width="88px" height="110px"/></c:when>
				<c:otherwise>
					<fmt:parseNumber var="imgWdth" type="number" value="${wbBcBVo.wbBcImgDVo.imgWdth}" />
					<a href="${_ctx}${wbBcBVo.wbBcImgDVo.imgPath}" target="viewPhotoWin" style="border:0px;"><img id="bcImage" src="${_cxPth}${wbBcBVo.wbBcImgDVo.imgPath}" width="88px" height="110px" onerror='this.src="${_cxPth}/images/blue/noimg2.png"'/></a>
				</c:otherwise>
			</c:choose>
			</div>
			<div style="float:left;">88px 110px</div>
        </dd>
        </dl>
        </div>
    </div>
                    
    <div class="entryzone">
        <div class="entryarea" id="entryId">
        <dl>
        <dd class="etr_tit"><u:msg titleId="wb.jsp.setBc.${!empty param.bcId ? 'mod' : 'reg'}.title" alt="명함 등록" /></dd>
        <dd class="etr_bodytit"><u:msg titleId="wb.cols.fldNm" alt="폴더" /></dd>
         <dd class="etr_input">
            <div class="etr_ipmany">
            <dl>
            <dd class="etr_ip_lt">
            	<input type="hidden" id="schFldId" name="fldId" value="${wbBcBVo.fldId}" />
            	<input type="text" id="schFldNm" name="fldNm" class="etr_iplt" value="${wbBcBVo.fldNm}" readonly="readonly"/></dd>
            <dd class="etr_se_rt" onclick="findFldPop();"><div class="etr_btn"><u:msg titleId="wb.btn.fldChoi" alt="폴더선택" /></div></dd>
            </dl>
            </div>
        </dd>
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.nm" alt="이름" /></dd>
         <dd class="etr_input">
            <div class="etr_ipmany">
            <dl>
            <dd class="etr_ip_lt"><input type="text" id="bcNm" name="bcNm" class="etr_iplt" value="${wbBcBVo.bcNm}" /></dd>
            <dd class="etr_se_rt" onclick="fnPwsmCheck();"><div class="etr_btn"><u:msg titleId="cm.btn.pwsmRead" alt="동명이인조회" /></div></dd>
            </dl>
            </div>
        </dd>

       <dd class="etr_bodytit"><u:msg titleId="cols.comp" alt="회사" /><u:msg titleId="cols.phon" alt="전화번호" /></dd>
		<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
			<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'compPhon' }">
				<input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'compPhon' : list.cntcTypCd}" />
				<input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
				<input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />
       			<dd class="etr_input"><div class="etr_inputin"><input type="text" id="compPhon" name="cntcCont" class="etr_iplt" value="${list.cntcCont}" /></div></dd>
			</c:if>
		</c:forEach>
	
	
       <dd class="etr_bodytit"><u:msg titleId="cols.home" alt="자택"/><u:msg titleId="cols.phon" alt="전화번호" /></dd>
		<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
			<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'homePhon' }">
				<input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'homePhon' : list.cntcTypCd}" />
				<input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
				<input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />
      			 <dd class="etr_input"><div class="etr_inputin"><input type="text" id="homePhon" name="cntcCont" class="etr_iplt" value="${list.cntcCont}" /></div></dd>
			</c:if>
		</c:forEach>
								
	
       <dd class="etr_bodytit"><u:msg titleId="cols.mob" alt="휴대전화" /><u:msg titleId="cols.phon" alt="전화번호" /></dd>
		<c:forEach var="list" items="${wbBcBVo.wbBcCntcDVo }" varStatus="status">
			<c:if test="${list.cntcClsCd eq 'CNTC' && list.cntcTypCd eq 'mbno' }">
				<input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'mbno' : list.cntcTypCd}" />
				<input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
				<input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'CNTC' : list.cntcClsCd}" />			
       			<dd class="etr_input"><div class="etr_inputin"><input type="text" id="mobPhon" name="cntcCont"  class="etr_iplt" value="${list.cntcCont}" /></div></dd>
			</c:if>
		</c:forEach>
	
        <dd class="etr_bodytit"><u:msg titleId="cols.email" alt="이메일" /></dd>
		<c:forEach var="list" items="${wbBcBVo.wbBcEmailDVo }" varStatus="status">
			<c:if test="${list.cntcClsCd eq 'EMAIL' && list.cntcTypCd eq 'email' }">
				<input type="hidden" id="cntcTypCd" name="cntcTypCd" value="${empty list.cntcTypCd ? 'email' : list.cntcTypCd}" />
				<input type="hidden" id="bcCntcSeq" name="bcCntcSeq" value="${list.bcCntcSeq }" />
				<input type="hidden" id="cntcClsCd" name="cntcClsCd" value="${empty list.cntcClsCd ? 'EMAIL' : list.cntcClsCd}" />
        		<dd class="etr_input"><div class="etr_inputin"><input type="text" id="email${status.index }" name="cntcCont" value="${list.cntcCont}" class="etr_iplt" /></div></dd>
			</c:if>
		</c:forEach>
	
        <dd class="etr_bodytit"><u:msg titleId="cols.comp" alt="회사" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="compNm" name="compNm" class="etr_iplt" value="${wbBcBVo.compNm}" /></div></dd>
		
		<dd class="etr_bodytit"><u:msg titleId="cols.dept" alt="부서" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="deptNm" name="deptNm" class="etr_iplt" value="${wbBcBVo.deptNm}" /></div></dd>
		
		<dd class="etr_bodytit"><u:msg titleId="cols.tich" alt="담당업무" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="tichCont" name="tichCont" class="etr_iplt" value="${wbBcBVo.tichCont}" /></div></dd>
		
		<dd class="etr_bodytit"><u:msg titleId="cols.publYn" alt="공개여부" /></dd>
        <dd class="etr_input">
            <div class="etr_ipmany">
            <dl>
            <dd class="etr_se_lt">
            
                <div class="select_in1" onclick="holdHide = true;$('.etr_open2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>

                <div class="etr_open2" style="display:none">
                    <div class="open_in1">
                        <div class="open_div">
                        <dl>
			                <dd class="txt" onclick="javascript:fnSetScre('priv');" data-schCd="priv"><u:msg titleId="cm.option.priv"/></dd>
			                <dd class="line"></dd>
			                <dd class="txt" onclick="javascript:fnSetScre('allPubl');" data-schCd="allPubl"><u:msg titleId="cm.option.allPubl"/></dd>
			                <dd class="line"></dd>
			                <dd class="txt" onclick="javascript:fnSetScre('deptPubl');" data-schCd="deptPubl"><u:msg titleId="cm.option.deptPubl"/></dd>
			                <dd class="line"></dd>
			                <dd class="txt" onclick="javascript:fnSetScre('apntPubl');" data-schCd="apntPubl"><u:msg titleId="cm.option.apntPubl" /></dd>
			                <dd class="line"></dd>
                     </dl>
                        </div>
                    </div>
                </div>

            </dd>
            <dd class="etr_se_rt apntPubl" style="display:none" >
            	<div class="etr_btn" onclick="openMuiltiUser();"><u:msg titleId="cm.option.apntPubl" /></div>
            </dd>
            </dl>
            </div>
          </dd>
		   <dd class="etr_bodytit" id="apntrUserMsg" style="display:${!empty wbBcBVo.wbBcApntrRVoList ? '' : 'none' }"><u:msg titleId="wb.jsp.viewBc.apntr" arguments="${wbBcBVo.wbBcApntrRVoList[0].userNm },${fn:length(wbBcBVo.wbBcApntrRVoList) -1}"/></dd>
           <dd class="etr_bodytit"><u:msg titleId="cols.note" alt="비고" /></dd>
           <dd class="etr_input" ><div class="etr_textareain"><textarea rows="3" id="noteCont" name="noteCont" class="etr_ta"  onfocus="$('#focusLayout').show();" onblur="$('#focusLayout').hide();">${wbBcBVo.noteCont}</textarea></div></dd>
		</dl>
		</div>
	</div>
    <!--entryzone S-->
    <div class="entryzone">
        <div class="blank20"></div> 
        <div class="entryarea">
        <dl>
        <dd class="etr_bodytit">
            <div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="addFile('${filesId}');"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
            </dl>
            </div>
        </dd>
        </dl>
        </div>
    </div>
    <!--//entryzone E-->
    <m:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <c:if test="${writeAuth == 'Y'}">
            <dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
         </dl>
        </div>
    </div>
	
	</form>
	<form id="photoImageForm" method="post" enctype="multipart/form-data" action="./transImagePost.do?menuId=${menuId }"></form>
	<div id="focusLayout" style="height:100px;width:100%;float:left;display:none;"></div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>