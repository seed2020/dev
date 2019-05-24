<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${!empty ctPrBVo.ctId}" var="ctId" value="${ctPrBVo.ctId}" elseValue="" />
<script type="text/javascript">
//<![CDATA[

function fnSetCatId(cd)
{
	$('#selectCtPr').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}
           
var holdHide2 = false;     
$(document).ready(function() {
	fnSetCatId('${ctId}');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea');
	$('body:first').on('click', function(){
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
	});
});

function saveBull() {
	if($("#subj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목]
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#viewPrForm input[name='subj']").focus();
		});
		return;
	}
	
	var $selectCtId = $("#selectCtPr").val();
	if($selectCtId == null || $selectCtId == ""){
		//ct.msg.noSelectCt = 커뮤니티를 선택해주시기 바랍니다.
		$m.msg.alertMsg("ct.msg.noSelectCt"); 
		return;
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#viewPrForm');
			$form.attr('action','/ct/pr/transSetCtPrSavePost.do?menuId=${menuId}');
			$m.nav.post($form);
		}
	});
	
}


function getEditHtml(){
	return $('#bodyHtmlArea').html();
}
function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
}
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
            <u:secu auth="W"><dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></u:secu>
     </dl>
    </div>
</div>

<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="viewPrForm" name="viewPrForm" enctype="multipart/form-data">
	<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
	<input type="hidden" id="bullId" name="bullId" value="${param.bullId}" />
	<input type="hidden" id="bullPid" name="bullPid" value="${param.bullPid}" />
	<input type="hidden" id="bullStatCd" name="bullStatCd" value="B" />
	<input type="hidden" id="bullRezvDt" name="bullRezvDt" value="${ctPrBVo.bullRezvDt}" />
	<input type="hidden" id="tgtDeptYn" name="tgtDeptYn" value="${ctPrBVo.tgtDeptYn}" />
	<input type="hidden" id="tgtUserYn" name="tgtUserYn" value="${ctPrBVo.tgtUserYn}" />
	
	<textarea id="cont" name="cont" style="display:none">${ctPrBVo.cont}</textarea>

	<input type="hidden" id="selectCtPr" name="selectCtPr" value="" />
	
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit">
			<u:msg titleId="ct.jsp.setPr.${fnc}.title" alt="홍보마당 등록/홍보마당 수정" />
        </dd>

        <dd class="etr_bodytit_asterisk"><span><u:msg titleId="cols.subj" alt="제목" /></span></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="subj" name="subj" class="etr_iplt" value="${ctPrBVo.subj}" /></div></dd>

		<dd class="etr_bodytit_asterisk"><span><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></span></dd>
		<dd class="etr_select">
			<div class="etr_open1 schOpnLayer2" style="display:none;">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    	<dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg titleId="ct.jsp.setPr.tx01" alt="해당 커뮤니티를 선택하십시오." /></dd>
					    <dd class="line"></dd>
                    	<c:forEach var="ctUserMastVo" items="${ctUserMastList}" varStatus="status">
                    		<c:if test="${status.count > 1 }"><dd class="line"></dd></c:if>
		                    <dd class="txt" onclick="javascript:fnSetCatId('${ctUserMastVo.ctId}');" data-schCd="${ctUserMastVo.ctId}">${ctUserMastVo.ctNm}</dd>		                    
                    	</c:forEach>
                 	</dl>
                    </div>
                </div>
            </div>
            <div class="etr_ipmany">
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
		</dl>
		</div>
	</div>

    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>


    <div class="entryzone">
        <div class="entryarea">
        <dl>

         <dd class="etr_bodytit">
         	<div class="icotit_dot"><u:msg titleId="cols.cont" alt="내용" /></div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="$m.openEditor();"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
            </dl>
            </div>
         </dd>
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><u:out value="${ctPrBVo.cont}" type="noscript" /></div></dd>

		</dl>
		</div>
	</div>
	
	
<c:set var="id" value="ctfiles"/>
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
            	<u:secu auth="W"><dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></u:secu>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>