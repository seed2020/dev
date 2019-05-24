<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${param.schCat != null}" var="schCat" value="${param.schCat}" elseValue="bcNm" />
<u:set test="${param.detlViewType != null}" var="detlViewType" value="${param.detlViewType}" elseValue="bcList" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/wb/findBcSub.do?'+$('#searchForm').serialize());
}

function fnSetSchCd(cd)
{
	$('#schCat').val(cd);
	$('.schTxt1 span').text($(".schOpnLayer1 dd[data-schCd="+cd+"]").text());
	$('.schOpnLayer1').hide();
}

var holdHide = false;
$(document).ready(function() {
	fnSetSchCd('${schCat}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
	});
});

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
}

var selectbcId = null;
function fnSetChk(bcId){
	selectbcId = bcId;
}

function fnSetBc(){
	if(selectbcId == null) {
		$m.msg.alertMsg("cm.msg.noSelect");
		return;
	}
	bcId = $('#'+selectbcId).attr("data-bcId");
	bcNm = $('#'+selectbcId).attr("data-bcNm");
	compNm = $('#'+selectbcId).attr("data-compNm");
	
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	win.$('#bcNm').val(bcNm);
	win.$('#bcId').val(bcId);
	win.$('#compNm').val(compNm);
	history.back();
}
//]]>
</script>
<section>
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="fnSetBc();"><u:msg titleId="cm.btn.choice" alt="선택" /></dd>
     </dl>
    </div>
</div>

<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="" />
<div class="entryzone unfoldArea">
      <div class="entryarea">
      <dl>
		<dd class="dd_blank5"></dd>

                <dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    
					
					<u:set var="checked" test="${detlViewType == 'bcList'}" value="true" elseValue="false"/>
					<m:check type="radio" id="radioDDdetlViewType" name="detlViewType" inputId="radiodetlViewType" value="bcList" checked="${checked }" onclick="fnChecked(this, 'radio','detlViewType', 1);" />
					<dd class="etr_body"><u:msg titleId="wb.jsp.findBcPop.tab.psnBc" alt="개인명함" /></dd>
					                    
					<u:set var="checked" test="${detlViewType == 'bcOpenList'}" value="true" elseValue="false"/>
					<m:check type="radio" id="radioDDdetlViewType" name="detlViewType" inputId="radiodetlViewType" value="bcOpenList" checked="${checked }" onclick="fnChecked(this, 'radio','detlViewType', 2);" />
					<dd class="etr_body"><u:msg titleId="wb.jsp.findBcPop.tab.publBc" alt="공개명함" /></dd>           
		
                    	
                    </dl>
                    </div>
                </dd>
    	<dd class="dd_blank6"></dd>
 		</dl>
    </div> 
</div>
<div class="listsearch">
		<div class="listselect">
		    <div class="open1 schOpnLayer1" style="display:none;">
		        <div class="open_in1">
		        	<div class="open_div">
				        <dl>       
					        
					        <dd class="txt" onclick="javascript:fnSetSchCd('bcNm');" data-schCd="bcNm"><u:msg titleId="cols.nm" alt="이름" /></dd>
					        <dd class="line"></dd>
					        <dd class="txt" onclick="javascript:fnSetSchCd('compNm');" data-schCd="compNm"><u:msg titleId="cols.compNm" alt="회사" /></dd>
					        <dd class="line"></dd>
					        
				    	</dl>
				    </div>	
		        </div>
		    </div>
			<div class="select1 schTxt1">
			<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
			<dl>
				<dd class="select_txt1"><span></span></dd>
				<dd class="select_btn"></dd>
			</dl>
			</div>
			</div>
		</div>
    	<div class="listinput">
			<div class="input1">
			<dl>
				<dd class="input_left"></dd>
				<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
				<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
			</dl>
			</div>
		</div>
		
</div>
</form>
		
    <div class="listarea">
    <article>
             
	<c:if test="${fn:length(wbBcBMapList) == 0}">
            <div class="listdiv_nodata" >
            <dl>
            <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
            </dl>
            </div>
	</c:if>
	<c:if test="${fn:length(wbBcBMapList) > 0}">
		<c:forEach var="wbBcBMap" items="${wbBcBMapList}" varStatus="status">
		<input type="hidden" id="${wbBcBMap.bcId }" data-bcId="${wbBcBMap.bcId }" data-bcNm="${wbBcBMap.bcNm }" data-compNm="${wbBcBMap.compNm }"/>
                  <div class="listdiv_fixed">
                        <div class="listcheck_fixed">
                        <dl>
                        <m:check type="radio" id="radioDDchkSelect" name="chkSelect" inputId="radiochkSelect" 
                        value="" checked="" onclick="fnSetChk('${wbBcBMap.bcId }');fnChecked(this, 'radio','chkSelect', ${status.index });" />
	                    </dl>
                        </div>

                      <div class="list_fixed">
                      <dl>
                      <dd class="tit">
                      	${wbBcBMap.bcNm } / ${wbBcBMap.compNm }
                      </dd>
                      <dd class="name">
                      	${wbBcBMap.dftCntc }
                      	/
						<c:choose>
							<c:when test="${detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'allPubl'}"><u:msg titleId="cm.option.allPubl"/></c:when>
							<c:when test="${detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'deptPubl'}"><u:msg titleId="cm.option.deptPubl"/></c:when>
							<c:when test="${detlViewType eq 'bcOpenList' && wbBcBMap.publTypCd eq 'apntPubl'}"><u:msg titleId="cm.option.apntPubl"/></c:when>
							<c:otherwise>${wbBcBMap.fldId eq 'ROOT' ? '' : wbBcBMap.fldNm}</c:otherwise>
						</c:choose>	
					  </dd>
                   </dl>
                      </div>
                  </div>
		</c:forEach>
	</c:if>

    </article>
	<m:pagination />
    
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</div>



</section>


