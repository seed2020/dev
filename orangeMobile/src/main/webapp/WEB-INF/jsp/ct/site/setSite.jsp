<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function saveSite(){
	if($("#siteNm").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [사이트명]
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.siteNm"], function(){
			$("#setRezv input[name='siteNm']").focus();
		});
		return;
	}
	
	if($("#catId").val().trim()==''){
		$m.dialog.alert('<u:msg titleId="ct.msg.noSelectCat" alt="카테고리를 선택해주시기 바랍니다."  />');
		return;
	}
	
	if($("#siteUrl").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [Site Url]
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.siteUrl"], function(){
			$("#setRezv input[name='siteUrl']").focus();
		});
		return;
	}
	
	//var regExDomain=/^((?:(?:(?:\w[\.\-\+]?)*)\w)+)((?:(?:(?:\w[\.\-\+]?){0,62})\w)+)\.(\w{2,6})$/;
	//if(!regExDomain.test($("#siteUrl").val().trim())){
	//	// ct.msg.notMatch.domain=도메인 형식에 맞지 않습니다.
	//	$m.msg.alertMsg("ct.msg.notMatch.domain");
	//	return;
	//}

	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			$m.nav.curr(event, '/ct/site/transSaveSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteId}&'+$('#setRezv').serialize());
		}
	});

}
           
function fnSetRescKndId(cd)
{
	$('#catId').val(cd);
	$('.schTxt1 span').text($(".schSelect1 dd[data-rescKndId='"+cd+"']").text());
	$('.schSelect1').hide();
}

function getEditHtml(){
	return $('#bodyHtmlArea').html();
}
function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
}

var holdHide = false;
$(document).ready(function() {
	fnSetRescKndId('${catId}');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schSelect1").hide();
	});
});

//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<dd class="btn" onclick="saveSite();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setRezv">
	<input type="hidden" id="catId"  name="catId" value="" />
    
    <textarea id="cont" name="cont" style="display:none">${ctSiteBVo.cont}</textarea>
    
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="ct.jsp.listSite.title" alt="Cool Site"/></dd>
        
        <dd class="etr_bodytit_asterisk"><span><u:msg titleId="cols.siteNm" alt="사이트명" /></span></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="siteNm" name="siteNm" class="etr_iplt" value="${ctSiteBVo.subj}" /></div></dd>
        
        <dd class="etr_bodytit_asterisk"><span><u:msg titleId="cols.cat" alt="카테고리" /></span></dd>
        <dd class="etr_select">
            <div class="etr_ipmany">
                <div class="select_in1 schTxt1" onclick="holdHide = true;$('.schSelect1').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
             <div class="etr_open1 schSelect1" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
		            <dd class="txt" onclick="fnSetRescKndId('');" data-rescKndId=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
		            <dd class="line"></dd>
					<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
			            <dd class="txt" onclick="fnSetRescKndId('${siteCatVo.catId}');" data-rescKndId="${siteCatVo.catId}">${siteCatVo.catNm}</dd>
			            <dd class="line"></dd>
					</c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
		</dd>
		
		
        <dd class="etr_bodytit_asterisk"><span><u:msg titleId="cols.siteUrl" alt="Site URL" /></span></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="siteUrl" name="siteUrl" class="etr_iplt" value="${ctSiteBVo.url}" /></div></dd>
	
		
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
         	<div class="icotit_dot"><u:msg titleId="cols.recmdRson" alt="추천사유" /></div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="$m.openEditor();"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
            </dl>
            </div>
         </dd>
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><u:out value="${ctSiteBVo.cont}" type="noscript" /></div></dd>

		</dl>
		</div>
	</div>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <dd class="btn" onclick="saveSite();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
         </dl>
        </div>
    </div>

	
	</form>
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
 </section>