<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
 function selectCdList(code){
	$('#selectCdList > dd').each(function(index){
		if(index>0) $(this).remove();
	});
	if(code==''){
		setSelOptions('schCd', '', '<u:msg titleId="cm.option.all" alt="전체선택"/>');
		return;	
	}
	if(code.indexOf('_')>-1)
		code=code.substring(0,code.indexOf('_'));
	$m.ajax("${_cxPth}/bb/getSelectCdListAjx.do?menuId=${menuId}&brdId=${param.brdId}", {cdGrpId:code}, function(data){
		if(data.message != null){
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			setSelOptions('schCd', '', '<u:msg titleId="cm.option.all" alt="전체선택"/>', false);
			var buffer=[];
			$.each(data.cdList, function(index, cd){
				buffer.push('<dd class="line"></dd>');
				buffer.push('<dd class="txt" onclick="setSelOptions(\'schCd\',$(this).attr(\'data-code\'),$(this).text());" data-code="'+cd.cdId+'">'+cd.rescNm+'</dd>');
			});
			if(buffer.length>0)
				$('#selectCdList').append(buffer.join(''));
		}
	});
 }
function executeHandler(codeNm, code, isSubmit){
	if(codeNm=='cdCat')
		selectCdList(code);
	
	<c:if test="${empty baseList.textOptList }">
		if(isSubmit===undefined && (codeNm=='schCd' || codeNm=='catId')){
			searchList(event);//$('#searchForm')[0].submit();
		}
	</c:if>
}
<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value, isSubmit){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
	executeHandler(codeNm, code, isSubmit);
}
function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}
$(document).ready(function() {
	<c:if test="${!empty baseList.textOptList }">
	var unfoldUsed = false;
	$('#dtlSrchArea').find('input').each(function(){
		if($(this).attr('class') != 'notChkCls' && $(this).val() !='') {
			unfoldUsed = true;
			return true;
		}
	});
	if(unfoldUsed) fnUnfold();
	</c:if>
});
//]]>
</script>
<form id="searchForm" name="searchForm" action="./listBull.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="brdId" value="${param.brdId}" />
<c:if test="${!empty baseList.textOptList }">
<div class="listsearch">
	<div class="listselect schTxt1" id="schCatContainer">
		<c:set var="schCatNm" value=""/>
	    <c:set var="schCat" value="${param.schCat }"/>
	    <div class="open1 schOpnLayer1" id="schCatOpen" style="display:none;">
	        <div class="open_in1">
	        	<div class="open_div">
			        <dl>
                        <c:forEach var="col" items="${baseList.textOptList }" varStatus="status">
                        <c:if test="${empty param.schCat && status.first}"><c:set var="schCatNm" value="${col[1] }"/><c:set var="schCat" value="${col[0] }"/></c:if>
			        	<c:if test="${!empty param.schCat && col[0] == param.schCat}"><c:set var="schCatNm" value="${col[1] }"/><c:set var="schCat" value="${col[0] }"/></c:if>			        	
			        	<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="${col[0]}">${col[1] }</dd>
			        	<c:if test="${!status.last}"><dd class="line"></dd></c:if>
		        	</c:forEach>
			    	</dl>
			    </div>	
	        </div>
	    </div><input type="hidden" name="schCat" value="${schCat }" class="notChkCls"/>
		<div class="select1">
		<div class="select_in1" onclick="holdHide = true;$('.schOpnLayer1').toggle();">
		<dl>
			<dd class="select_txt1" id="selectView"><span>${schCatNm }</span></dd>
			<dd class="select_btn"></dd>
		</dl>
		</div>
		</div>
	</div>
   	<div class="listinput2">
		<div class="input1">
		<dl><dd class="input_left"></dd><dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd><dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd></dl>
		</div>
	</div>
	<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>
</c:if>

<div class="entryzone unfoldArea" id="dtlSrchArea" <c:if test="${!empty baseList.textOptList }">style="display:none;"</c:if>>
      <div class="entryarea">
      <dl>
          <c:if test="${!empty baseList.codeOptList }">
		  <dd class="etr_blank"></dd>
	      <dd class="etr_input">
               <div class="etr_ipmany">
               <dl>
               <dd class="etr_se_lt" id="cdCatContainer">
               	   <c:set var="cdCatNm" value=""/>
	        	   <c:set var="cdCat" value=""/>
                   <div class="etr_open2" id="cdCatOpen" style="display:none;">
                       <div class="open_in1">
                           <div class="open_div">
                           <dl>
                           <dd class="txt" onclick="setSelOptions('cdCat',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd>
                           <c:if test="${empty param.cdCat}"><c:set var="cdCatNm" value="${noSelect }"/></c:if>
                           <c:forEach var="col" items="${baseList.codeOptList }" varStatus="status">
                           		<c:set var="paramCd" value="${col[0]}_${col[2]}"/>
					        	<c:if test="${paramCd == param.cdCat}"><c:set var="cdCatNm" value="${col[1] }"/><c:set var="cdCat" value="${paramCd }"/><c:set var="cdCatNm" value="${col[1] }"/></c:if>
					        	<c:if test="${status.count>0}"><dd class="line"></dd></c:if>
					        	<dd class="txt" onclick="setSelOptions('cdCat',$(this).attr('data-code'),$(this).text());" data-code="${paramCd }">${col[1] }</dd>
					        </c:forEach>
                        </dl>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="cdCat" value="${cdCat }" class="notChkCls"/>
               <div class="select_in1" onclick="holdHide = true; $('#cdCatContainer #cdCatOpen').toggle();">
               <dl>
               <dd class="select_txt" id="selectView"><span>${cdCatNm }</span></dd>
               <dd class="select_btn"></dd>
               </dl>
               </div>
           </dd>
           <dd class="etr_se_rt" id="schCdContainer">
           	   <c:set var="schCdNm" value=""/>
	           <c:set var="schCd" value="${param.schCd }"/>
               <div class="etr_open3" id="schCdOpen" style="display:none;">
                   <div class="open_in2">
                       <div class="open_div">
                       <dl id="selectCdList">
                       <dd class="txt" onclick="setSelOptions('schCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd>
                           <c:if test="${empty param.schCd}"><c:set var="schCdNm" value="${noSelect }"/></c:if>
                           <c:forEach var="col" items="${baseList.codeSelectList }" varStatus="status">
					        	<c:if test="${col.cdId == param.schCd}"><c:set var="schCdNm" value="${col.rescNm }"/><c:set var="schCd" value="${col.cdId }"/></c:if>
					        	<c:if test="${status.count>0}"><dd class="line"></dd></c:if>
					        	<dd class="txt" onclick="setSelOptions('schCd',$(this).attr('data-code'),$(this).text());" data-code="${col.cdId}">${col.rescNm }</dd>
					        </c:forEach>
                    </dl>
                       </div>
                   </div>
               </div>
               <input type="hidden" name="schCd" value="${schCd }"/>
               <div class="select_in2" onclick="holdHide = true; $('#schCdContainer #schCdOpen').toggle();">
               <dl>
               <dd class="select_txt" id="selectView"><span>${schCdNm }</span></dd>
               <dd class="select_btn"></dd>
               </dl>
               </div>
           </dd>
           
           </dl>
           </div>
       </dd></c:if>
    	<c:if test="${baBrdBVo.catYn == 'Y'}">
      	<dd class="dd_blank5"></dd>
		<dd class="etr_input">
              <div class="etr_ipmany">
              <dl>              
              <dd class="etr_se_lt" id="catIdContainer">
              	   <c:set var="catIdNm" value=""/>
	               <c:set var="catId" value="${param.catId }"/>
                  <div class="etr_open2" id="catIdOpen" style="display:none;">
                    <div class="open_in1">
                        <div class="open_div">
					        <dl>
					        <dd class="txt" onclick="setSelOptions('catId',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="bb.btn.catChoi" alt="카테고리선택" />${noSelect }</dd>
					        <c:if test="${empty param.catId}"><c:set var="catIdNm" value="${noSelect }"/></c:if>
					        <c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
					        	<c:if test="${catVo.catId == param.catId}"><c:set var="catIdNm" value="${catVo.rescNm }"/><c:set var="catId" value="${catVo.catId }"/></c:if>
					        	<c:if test="${status.count>0}"><dd class="line"></dd></c:if>
					        	<dd class="txt" onclick="setSelOptions('catId',$(this).attr('data-code'),$(this).text());" data-code="${catVo.catId}">${catVo.rescNm }</dd>
							</c:forEach>
					    	</dl>
                        </div>
                    </div>
                </div>   
                <input type="hidden" name="catId" value="${catId }"/>
                <div class="select_in1" onclick="holdHide = true; $('#catIdContainer #catIdOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${catIdNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </dd>
            </dl>
            </div>
        </dd></c:if><dd class="etr_blank1"></dd>
 		</dl>
    </div> 
</div>
</form>