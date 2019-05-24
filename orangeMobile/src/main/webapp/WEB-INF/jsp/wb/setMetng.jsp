<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ownerUid="${listPage eq 'listMetng' ? wbBcMetngDVo.regrUid : ''}"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:set test="${wbBcMetngDVo.metngClsCd != null}" var="metngClsCd" value="${wbBcMetngDVo.metngClsCd}" elseValue="${empty wbMetngClsCdBVoList ? '' : wbMetngClsCdBVoList[0].rescId }" />
<script type="text/javascript">
//<![CDATA[
<%// 명함 정보 추가 %>
function atndAdd(objArr){
	addCols(objArr,"FRND");
};
<%// 선택추가 %>
function addCols(arr , emplTypCd){	
	if(arr==null) return;
	var area = $('#guestTypContainer').find('#selectedListArea');
	var html,emplId;//empty = area.find('dd:first').length==0;
	var vas = getAllVas(emplTypCd);
	var len = area.children().length == 0 ? false : true; 
	arr.each(function(index, data){
		emplId = emplTypCd == 'EMPL' ? data.userUid : (emplTypCd == 'ETC' ? data.etcId : data.bcId );
		if(vas==null || !vas.contains(emplId)){
			html = "";
			if(len) html +='<dd class="line"></dd>';
			html += '<dd class="txt_dd" data-guestUid="'+emplId+'" data-emplTypCd="'+emplTypCd+'">';
			html += '<div class="txt_lt">';
			html += data.bcNm+'/'+'<u:msg titleId="wc.option.frnd" alt="지인"/>';
			if(data.compNm != '') html+='/'+data.compNm;
			if(data.email != '') html+='/'+data.email;
			html +='<input type="hidden" name="bcMetngAtndDetlId" />';
			html +='<input type="hidden" name="emplNm" value="'+data.bcNm+'"/>';
			html +='<input type="hidden" name="emplTypCd" value="'+emplTypCd+'"/>';
			html +='<input type="hidden" name="emplPhon" value="'+data.mbno+'"/>';
			html +='<input type="hidden" name="emplId" value="'+emplId+'"/>';
			html +='<input type="hidden" name="compNm" value="'+data.compNm+'"/>';
			html +='<input type="hidden" name="compPhon" value="'+data.compPhon+'"/>';
			html +='<input type="hidden" name="email" value="'+data.email+'"/>';
			html += '</div>';
			html += '<div class="txt_delete" onclick="delSelects(this);"></div>';
			html +='</dd>';
			area.append(html);
			len = true;
		}
	});
};

<%//콤보박스 삭제 %>
function delSelects(obj){
	var $parent = $(obj).parent();
	var $next = $parent.next().length > 0 ? $parent.next() : null;
	if($next != null && $next.attr('class') == 'line') $next.remove();
	$parent.remove();
};

<%//현재 등록된 id 목록 리턴 %>
function getAllVas(emplTypCd){
	var arr=[];
	var area = $('#guestTypContainer').find('#selectedListArea');
	area.children("dd[class!='line']").each(function(){
		if($(this).attr("data-emplTypCd") == emplTypCd){
			arr.push($(this).attr('data-guestUid'));
		}
	});
	
	if(arr.length==0){
		return null;
	}
	return arr;
};

//여러명의 사용자 선택
function openMuiltiUser(){
	var param = {};
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return true;
		var area = $('#guestTypContainer').find('#selectedListArea');
		var html,emplId;//empty = area.find('dd:first').length==0;
		var emplTypCd = 'EMPL';
		var vas = getAllVas(emplTypCd);
		var len = area.children().length == 0 ? false : true; 
		arr.each(function(index, data){
			emplId = emplTypCd == 'EMPL' ? data.userUid : (emplTypCd == 'ETC' ? data.etcId : data.bcId );
			if(vas==null || !vas.contains(emplId)){
				html = "";
				if(len) html +='<dd class="line"></dd>';
				html += '<dd class="txt_dd" data-guestUid="'+emplId+'" data-emplTypCd="'+emplTypCd+'">';
				html += '<div class="txt_lt">';
				html += data.rescNm+'/'+'<u:msg titleId="cm.option.empl" alt="임직원"/>';
				if(data.deptRescNm != '') html+='/'+data.deptRescNm;
				if(data.email != '') html+='/'+data.email;
				html +='<input type="hidden" name="bcMetngAtndDetlId" />';
				html +='<input type="hidden" name="emplNm" value="'+data.rescNm+'"/>';
				html +='<input type="hidden" name="emplTypCd" value="'+emplTypCd+'"/>';
				html +='<input type="hidden" name="emplPhon" value="'+data.mbno+'"/>';
				html +='<input type="hidden" name="emplId" value="'+emplId+'"/>';
				html +='<input type="hidden" name="compNm" value="'+data.deptRescNm+'"/>';
				html +='<input type="hidden" name="compPhon" value="'+data.compPhon+'"/>';
				html +='<input type="hidden" name="email" value="'+data.email+'"/>';
				html += '</div>';
				html += '<div class="txt_delete" onclick="delSelects(this);"></div>';
				html +='</dd>';
				area.append(html);
				len = true;
			}
		});
	});
};

function guestAdd(){
	var guestTyp = $('#setRegForm input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'FRND'){
		$m.nav.next(null, '/wc/findBc.do?menuId=${menuId}${agntParam}&selection=multi');
		//dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
	}else{
		openMuiltiUser();
	}
};

function fnScreSelect(){
	$("div.etr_open2").show();
}

function fnSetScre(cd)
{
	$('#metngClsCd').val(cd);
	$('dd.etr_se_lt span').text($("div.etr_open2 dd[data-schCd='"+cd+"']").text());
	$("div.etr_open2").hide();
}

var holdHide = false;
$(document).ready(function() {
	fnSetScre('${metngClsCd}');
	$("input[type='text'], textarea").each(function(){
	$space.apply(this, {relative:30});
	});
	$('body:first').on('click', function(){
	if(holdHide) holdHide = false;
	else $("div.etr_open2").hide();
	});
});



function save(){
	if($("#metngSubj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
		$("#setRegForm input[name='metngSubj']").focus();
		});
		return;
	}

	if($("#metngYmd").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [관련미팅일시] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.metngDt"], function(){
		$("#setRegForm input[name='metngDt']").focus();
		});
		return;
	}

	if($("#metngCont").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [내용] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.cont"], function(){
		$("#setRegForm textarea[name='metngCont']").focus();
		});
		return;
	}

	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
	if(result){
	var $form = $('#setRegForm');
	$m.nav.post($form);
	}
	});
}


function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}

function findBcPop(){
	/* $m.dialog.open({
		id:'findBcPop',
		title:'<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />',
		url:'/wb/findBcSub.do?menuId=${menuId}${agntParam}',
	}); */
	$m.nav.next(event, '/wb/findBcSub.do?menuId=${menuId}${agntParam}');
};

function fnChecked(dd, type, quesId, examOrdr){
	var obj = $('input:'+(type=='check'?type+'box':type)+'[id="'+type+quesId+'"]:eq('+(examOrdr-1)+')');

	if(type == 'radio'){
		$('dd#radioDD'+quesId).each(function(){
		$(this).attr("class", type);
		});
	}

	if(type == 'radio')
		$(dd).attr("class", type+'_on');

	if(obj.is(":checked") == false){
		obj.prop('checked',true);
	}
	else{
		if(type == 'check'){
			obj.prop('checked',false);
		}
	}
};
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

<div id="docApvLnArea"></div>

<div class="blankzone">
	<div class="blank20"></div>
</div>

<form id="setRegForm" name="setRegForm" enctype="multipart/form-data" action="/wb/${transPage}Post.do?menuId=${menuId}">
<c:if test="${!empty wbBcMetngDVo.bcMetngDetlId }">
<input type="hidden" id="bcMetngDetlId" name="bcMetngDetlId" value="${wbBcMetngDVo.bcMetngDetlId}" />
</c:if>
<c:if test="${!empty schBcRegrUid}">
<input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/>
</c:if>

<input type="hidden" id="listPage" name="listPage" value="/wb/${listPage}.do?menuId=${menuId}&${nonPageParams}${agntParam}" />
<input type="hidden" id="viewPage" name="viewPage" value="/wb/${viewPage}.do?${params}" />
<input type="hidden" id="bcId" name="bcId" value="${wbBcMetngDVo.bcId}" />

<input type="hidden" id="metngClsCd" name="metngClsCd" />


<div class="entryzone">
	<div class="entryarea">
		<dl>
		<dd class="etr_tit"><u:msg titleId="wb.jsp.setMetng.reg.title" alt="관련미팅정보 등록/관련미팅정보 수정" /></dd>

		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
		<dd class="etr_input"><div class="etr_inputin"><input type="text" id="metngSubj" name="metngSubj" class="etr_iplt" value="${wbBcMetngDVo.metngSubj}" /></div></dd>


		<dd class="etr_bodytit"><u:msg titleId="wb.cols.bc" alt="명함" /></dd>
		<dd class="etr_input">
		<div class="etr_ipmany">
			<dl>
			<dd class="etr_ip_lt"><input type="text" id="bcNm" name="bcNm" class="etr_iplt_disabled" value="${wbBcMetngDVo.bcNm}" readonly="readonly"/></dd>
			<dd class="etr_se_rt" onclick="javascript:findBcPop();"><div class="etr_btn"><u:msg titleId="cm.btn.search" alt="검색"  /></div></dd>
			</dl>
		</div>
		</dd>


		<dd class="etr_bodytit"><u:msg titleId="cols.comp" alt="회사" /></dd>
		<dd class="etr_input"><div class="etr_inputin_disabled"><input type="text" id="compNm" name="bcCompNm" class="etr_iplt_disabled" value="${wbBcMetngDVo.compNm}" readonly="readonly"/></div></dd>

		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.metngDt" alt="관련미팅일시" /></dd>
		<dd class="etr_select">
		<div class="etr_calendar_lt">
			<div class="etr_calendar">
				<input id="metngYmd" name="metngYmd" value="${wbBcMetngDVo.metngYmd}" type="hidden" />
				<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('metngYmd','');"><span id="metngYmd">${wbBcMetngDVo.metngYmd}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this,'metngYmd');"></dd>
                <dd class="cbtn" onclick="fnCalendar('metngYmd','');"></dd>
                </dl>
                </div>
			</div>
		</div>
		</dd>

		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.secul" alt="보안등급" /></dd>
		<dd class="etr_input">
		<div class="etr_ipmany" id="openYnContainer">
			<dl>
			<u:set var="checked" test="${empty wbBcMetngDVo.openYn || wbBcMetngDVo.openYn == 'Y'}" value="true" elseValue="false"/>
			<m:check type="radio" id="openYnY" name="openYn" inputId="openYnY" value="Y" areaId="openYnContainer" titleId="cm.option.publ" checked="${checked }" />
			<u:set var="checked" test="${wbBcMetngDVo.openYn == 'N'}" value="true" elseValue="false"/>
	        <m:check type="radio" id="openYnN" name="openYn" inputId="openYnN" value="N" areaId="openYnContainer" titleId="cm.option.priv" checked="${checked }"/>
			</dl>
		</div>
		</dd>

		<dd class="etr_bodytit"><u:msg titleId="wb.cols.cls" alt="분류" /></dd>
		<dd class="etr_input">
		<div class="etr_ipmany">
			<dl>
			<dd class="etr_se_lt">

			<div class="select_in1" onclick="holdHide = true;$('div.etr_open2').toggle();">
				<dl>
				<dd class="select_txt"><span></span></dd>
				<dd class="select_btn"></dd>
				</dl>
			</div>

			<div class="etr_open2" style="display:none">
				<div class="open_in1">
					<div class="open_div">
						<dl>
						<c:forEach items="${wbMetngClsCdBVoList}" var="list" varStatus="status">
							<c:if test="${status.count > 1 }"><dd class="line"></dd></c:if>
							<dd class="txt" onclick="fnSetScre('${list.rescId}');" data-schCd="${list.rescId}">${list.rescNm}</dd>
						</c:forEach>
						</dl>
					</div>
				</div>
			</div>

			</dd>

			</dl>
		</div>
		</dd>
		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.cont" alt="내용" /></dd>
		<dd class="etr_input"><div class="etr_textareain"><textarea rows="5" id="metngCont" name="metngCont" class="etr_ta">${wbBcMetngDVo.metngCont}</textarea></div></dd>
		</dl>
	</div>
</div>
<div class="blank10"></div>

<!--entryzone S-->
<div class="entryzone">
	<div class="blank20"></div>
    <div class="entryarea">
    <dl>
    	<dd class="etr_bodytit"><div class="icotit_dot"><u:msg titleId="cols.guestApnt" alt="참석자지정" /></div></dd>
		<dd class="etr_input">
			<div class="etr_ipmany" id="guestTypChkContainer">
	            <dl>
	            	<m:check type="radio" id="guestTypEMPL" name="guestTyp" inputId="guestTypEMPL" value="EMPL" areaId="guestTypChkContainer" titleId="cm.option.empl" checked="true" />
	            	<m:check type="radio" id="guestTypFRND" name="guestTyp" inputId="guestTypFRND" value="FRND" areaId="guestTypChkContainer" titleId="wc.option.frnd" checked="false"/>
	            	<dd class="etr_btn" onclick="guestAdd();"><u:msg titleId="wc.btn.guestAdd" alt="참석자추가" /></dd>
	            </dl>
	        </div>
		</dd>
		<dd class="etr_blank"></dd>
		<dd class="etr_select">
                 <div class="etr_open1" id="guestTypContainer" style="display:none">
                    <div class="open_in1">
                        <div class="open_div">
                        <dl id="selectedListArea">
                        	<c:forEach  var="list" items="${wbBcMetngAtndRVoList}" varStatus="status">
                        		<dd class="txt_dd" data-guestUid="${list.emplId }" data-emplTypCd="${list.emplTypCd }" >
	                                <div class="txt_lt" >
									${list.emplNm}/<u:msg titleId="${list.emplTypCd eq 'FRND' ? 'wc.option.frnd' : (list.emplTypCd eq 'EMPL' ? 'cm.option.empl' : 'ct.option.etc')}" alt="지인"/>/${list.compNm}/${list.email}
									<input type="hidden" name="bcMetngAtndDetlId" value="${list.bcMetngAtndDetlId}" />
									<input type="hidden" name="emplNm" value="${list.emplNm}"/>
									<input type="hidden" name="emplTypCd" value="${list.emplTypCd}"/>
									<input type="hidden" name="emplPhon" value="${list.emplPhon}"/>
									<input type="hidden" name="emplId" value="${list.emplId}"/>
									<input type="hidden" name="compNm" value="${list.compNm}"/>
									<input type="hidden" name="compPhon" value="${list.compPhon}"/>
									<input type="hidden" name="email" value="${list.email}"/>
									</div>
	                                <div class="txt_delete" onclick="delSelects(this);"></div>
								</dd>
								<c:if test="${fn:length(wbBcMetngAtndRVoList) > status.count }"><dd class="line"></dd></c:if>
							</c:forEach>
                     	</dl>
                        </div>
                    </div>
                </div>
                <c:if test="${ !empty wcSchdlBVo.schdlId && !empty wbBcMetngAtndRVoList}"><c:set var="guestNm" value="${wbBcMetngAtndRVoList[0].emplNm}"/></c:if>
                <div class="etr_ipmany">
                    <div class="select_in1" onclick="$('#setRegForm #guestTypContainer').toggle();">
                    <dl>
                    <dd class="select_txt" id="guestNmView"><span><u:msg titleId="cols.guestApnt" alt="참석자지정" /></span></dd>
                    <dd class="select_btn"></dd>
                    </dl>
                    </div>
                </div>
            </dd>
        </dl>
    </div>
</div>
<c:set var="id" value="mtfiles"/>
<script type="text/javascript">
//<![CDATA[
function addFile() {
	$('#${id}_file').trigger('click');
}

function setFile(obj) {
	var va = $(obj).val();
	if (va != '') {
		var $last = $('.filearea:last');
		var $clone = $last.clone();
		$last.removeClass('tmp');
		$last.show();
		var p = va.lastIndexOf('\\');
		if (p > 0) va = va.substring(p + 1);
		$last.find('#${id}_fileView').text(va).removeAttr('id');
		$last.find('input[name="${id}_valid"]').val('Y');

		$area = $(obj).parents('.${id}attachBtnArea');
		var $cloneArea = $('#${id}attachBtnAreaParent').clone();
		$cloneArea.replaceWith( $cloneArea.clone(true) );
		$cloneArea.insertBefore($area);

		var id = $area.attr('id');
		var fileSeq = id.substring(id.lastIndexOf('_')+1);
		$last.find('input[name="${id}_fileSeq"]').val(fileSeq);
		fileSeq++;
		$cloneArea.attr('id','${id}attachBtnArea_'+fileSeq);
		$clone.insertAfter($last);
		}
		}

		function delFile(checkedObj) {
		$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
		$area = $(checkedObj).parents('.filearea');
		if ($area.hasClass('tmp') == false) {
		$area.find('input[name="${id}_useYn"]').val('N');
		var fileSeq = $area.find('input[name="${id}_fileSeq"]').val();
		if(fileSeq != undefined){
		$('#${id}attachBtnArea_'+fileSeq).remove();
		}
		$area.hide();
		}
		}
		});
		}
		//]]>
		</script>

		<!--entryzone S-->
<div class="entryzone">
	<div class="blank20"></div>
		<div class="entryarea">
		<dl>
			<dd class="etr_bodytit">
				<div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
				<div class="icoarea">
					<dl>
						<dd class="btn" onclick="addFile();"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
					</dl>
				</div>
			</dd>
		</dl>
	</div>
</div>
		<!--//entryzone E-->

		<!--attachzone S-->
<div class="attachzone">
	<div class="attacharea">
		<div id="${id}attachBtnArea_0" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this);" /></div>
		<div id="${id}attachBtnAreaParent" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this);" /></div>
		<c:if test="${fn:length(fileVoList) > 0}">
		<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
		<c:set var="fileSizeKB" value="${fileVo.fileSize/1024 }"/>
		<c:set var="fileSizeMB" value="${fileVo.fileSize/(1024*1024) }"/>
		<c:set var="fileSizeTemp" value="${fileSizeKB+(1-(fileSizeKB%1))%1}"/>
		<u:set var="fileSize" test="${fileSizeTemp > 999 }" value="${fileSizeMB+(1-(fileSizeMB%1))%1 }" elseValue="${fileSizeTemp }"/>
		<u:set var="fileSizeUnit" test="${fileSizeTemp > 999 }" value="MB" elseValue="KB"/>
		<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
		<div class="attachin filearea">
		<div class="attach" onclick="javascript:;">
		<div class="btn"></div>
		<div class="txt">${fileVo.dispNm}(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</div>
		</div>
		<div class="delete" onclick="javascript:delFile(this);"></div>
		<input type="hidden" name="${id}_fileId" value="${fileVo.fileId}" />
		<input type="hidden" name="${id}_valid" value="${ctSendYn}" />
		<input type="hidden" name="${id}_useYn" value="${fileVo.useYn}" />
		<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />
		</div>
		</c:forEach>
		</c:if>

		<div class="attachin filearea tmp" style="display:none">
		<div class="attach" >
		<div class="btn"></div>
		<div class="txt"><span id="${id}_fileView"></span></div>
		</div>
		<div class="delete" onclick="javascript:delFile(this);"></div>
		<input type="hidden" name="${id}_fileSeq" value="" />
		<input type="hidden" name="${id}_fileId" value="" />
		<input type="hidden" name="${id}_valid" value="N" />
		<input type="hidden" name="${id}_useYn" value="Y" />
		<input type="hidden" name="${id}_ctSendYn" value="" />
		</div>

	</div>
</div>
		<!--//attachzone E-->

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

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
