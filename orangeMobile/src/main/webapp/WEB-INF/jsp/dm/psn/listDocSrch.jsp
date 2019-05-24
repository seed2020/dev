<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<script type="text/javascript">
<!--
<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}
function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}<%// 검색 클릭 %>
function searchList(event){
	$m.nav.curr(event, '/dm/doc/${listPage}.do?'+$('#searchForm').serialize());
}<%// 검색 조건 클릭 %>
function setListSearch(code, value){
	var $form = $("#searchForm");
	$form.find("input[name='schCat']").val(code);
	$form.find("#listsearch .listselect:first #selectView span").text(value);
	$form.find("#listsearch #searchCat").hide();
}<%// 달력 클릭 %>
function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findFldPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/dm/doc/findFldSub.do?menuId=${menuId}&lstTyp='+lstTyp+"&fldId="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	$m.nav.next(null,url);
};<%// 분류,폴더 적용%>
function setFldInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
}
$(document).ready(function() {
	$('#searchArea2').find('input[type="text"]').keyup(function(event){
		if(event.keyCode == 13) document.searchForm2.submit();
	});
	
	var unfoldUsed = false;
	$('#dtlSrchArea').find('input').each(function(){
		if($(this).attr('class') != 'notChkCls' && $(this).val() != '') {
			unfoldUsed = true;
			return true;
		}
	});
	if(unfoldUsed) fnUnfold();
});
//-->
</script>

<form id="searchForm" name="searchForm" action="${_uri}" onsubmit="searchList(event);" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="schCat" value="${empty param.schCat ? 'subj' : param.schCat}" />

<div class="listsearch" id="listsearch">
	<div class="listselect" id="schCatContainer">
		<div class="open1" id="schCatOpen" style="display:none">
			<div class="open_in1">
				<div class="open_div">
				<dl>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="subj"><u:msg titleId="cols.subj" alt="제목" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="cont"><u:msg titleId="cols.cont" alt="내용" /></dd>
				</dl>
				</div>
			</div>
		</div>
		
		<div class="select1">
			<div class="select_in1" onclick="holdHide = true; $('#schCatContainer #schCatOpen').toggle();">
			<dl>
				<dd class="select_txt1" id="selectView"><span><u:msg titleId="${empty param.schCat || param.schCat=='subj' ? 'cols.subj' : param.schCat=='cont' ? 'cols.cont' : param.schCat=='kwd' ? 'dm.cols.kwd' : param.schCat=='docNo' ? 'dm.cols.docNo' : param.schCat=='docNo' ? 'dm.cols.ownr' : 'cols.regr'}" alt="제목/내용/등록자/키워드/문서번호/소유자" /></span></dd>
				<dd class="select_btn"></dd>
			</dl>
			</div>
		</div>
		
	</div>
	<div class="listinput2">
		<div class="input1">
		<dl>
			<dd class="input_left"></dd>
			<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
			<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
		</dl>
		</div>
	</div>
	<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>
<div class="entryzone unfoldArea" id="dtlSrchArea" style="display:none;">
      <div class="entryarea">
      <dl>
     	<dd class="etr_blank"></dd>
      	<dd class="etr_select">
      		<input type="hidden" id="durCat" name="durCat" value="regDt" class="notChkCls"/>
		  	  <div class="etr_calendar_lt">
			      <div class="etr_calendar">
				  <input id="durStrtDt" name="durStrtDt" value="${param.durStrtDt}" type="hidden" />
				  <div class="etr_calendarin">
					   <dl>
					   <dd class="ctxt" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"><span id="durStrtDt">${param.durStrtDt}</span></dd>
					   <dd class="cdelete" onclick="fnTxtDelete(this,'durStrtDt');"></dd>
					   <dd class="cbtn" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"></dd>
					   </dl>
				   </div>
				</div>
			</div>
		
	        <div class="etr_calendar_rt">
	            <div class="etr_calendar">
	            	<input id="durEndDt" name="durEndDt" value="${param.durEndDt}" type="hidden" />
	            	<div class="etr_calendarin">
	                    <dl>
	                    <dd class="ctxt" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"><span id="durEndDt">${param.durEndDt}</span></dd>
	                    <dd class="cdelete" onclick="fnTxtDelete(this,'durEndDt');"></dd>
	                    <dd class="cbtn" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"></dd>
                    </dl>
                    </div>
	            </div>
	        </div>
	    </dd>
    	<dd class="etr_blank"></dd>
    	<!-- 폴더 -->
      	<dd class="etr_input">
			<div class="etr_ipmany">
			<dl>
			<dd class="etr_ip_lt">
				<div class="ip_txt" id="fldInfoArea" >
					<div id="idArea" style="display:none;"><input type="hidden" id="fldId" name="fldId" value="${param.fldId}" /></div>
					<div id="nmArea" style="display:inline;"><input type="text" id="fldNm" name="fldNm" class="etr_iplt" value="${param.fldNm}" style="width:55%;" readonly="readonly"/></div>
              	</div>
              	<div class="ip_delete" onclick="valueReset('fldInfoArea');"></div>
			</dd>
			<dd class="etr_se_rt" ><div class="etr_btn" onclick="findFldPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd>
			</dl>
			</div>
		</dd>
 		</dl>
    </div> 
</div>
</form>