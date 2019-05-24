<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<script type="text/javascript">
<!--<%// 복합식 value 삭제%>
function delCidVa(id){
	$('#'+id+'Id').val('');
	$('#'+id+'Nm').val('');
}<%// 사용자,부서 선택 - 복합식 %>
function openCidPop(atrb){
	var catVa = $("select[name='"+atrb+"Cat'] > option:selected").eq(0).val();
	if(catVa == 'user') openSingUser(atrb,"Id");
	else openSingOrg(atrb);
}<%// 부서 선택 %>
function openSingOrg(id){
	var data = [];
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#'+id+'Id').val(orgVo.orgId);
			$('#'+id+'Nm').val(orgVo.rescNm);
		}
	});
}<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findSelPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/dm/doc/findSelSub.do?menuId=${menuId}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	$m.nav.next(null,url);
	//var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	//dialog.open('findSelPop', msgTitle, url);
};<%// 분류,폴더 적용%>
function setSelInfos(arr, lstTyp){
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
	//setPageChk(prefix);
}<%// 1명의 사용자 선택 %>
function openSingUser(id, suffix){
	if(suffix == undefined) suffix = "Uid";
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#'+id+suffix).val() != '') data.push({userUid:$('#'+id+suffix).val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#'+id+suffix).val(userVo.userUid);
			$('#'+id+'Nm').val(userVo.rescNm);
		}
	});
};<%// 저장소 선택 %>
function srchStor(storId){
	var $form = $('#searchForm');
	$form.find("input[name='paramStorId']").remove();
	$form.appendHidden({name:'paramStorId',value:storId});
	$form.submit();
	
};<%
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
<c:if test="${!empty storList }">
<u:set var="paramStorId" test="${!empty paramStorId }" value="${paramStorId }" elseValue="${param.paramStorId }"/>
<div class="entryzone" >
<div class="entryarea">
  <dl>
	<dd class="etr_blank"></dd>
	<dd class="etr_input">
	   <div class="etr_ipmany" id="paramStorContainer">
	   <dl>
	   <dd class="etr_se_rt">
	   	   <c:set var="storNm" value="${storVo.storNm}"/>
		   <div class="etr_open2" id="paramStorOpen" style="display:none">
		   	   <u:msg var="storDftTitle" titleId="cm.option.dft" alt="기본" />
			   <div class="open_in1">
				   <div class="open_div">
				   <dl>
				   <c:forEach items="${storList}" var="storVo" varStatus="status">
				   <u:set var="storSubTitle" test="${storVo.dftYn eq 'Y' }" value="(${storDftTitle })" elseValue=""/>
					<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
					<c:if test="${storVo.storId eq paramStorId }"><c:set var="storNm" value="${storVo.storNm}${storSubTitle }"/></c:if>
					<dd class="txt" onclick="srchStor('${storVo.storId}')" >${storVo.storNm}${storSubTitle }</dd>
				</c:forEach>
				</dl>
				   </div>
			   </div>
		   </div>
		   
		   <div class="select_in1" onclick="$('#paramStorContainer #paramStorOpen').toggle();">
		   <dl>
			<dd class="select_txt" id="selectView"><span>${storNm }</span></dd>
		   <dd class="select_btn"></dd>
		   </dl>
		   </div>
	   </dd>
	   </dl>
	   </div>
	  </dd>
  </dl>
  </div>
</div>
</c:if>

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
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="regrNm"><u:msg titleId="cols.regr" alt="등록자" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="kwd"><u:msg titleId="dm.cols.kwd" alt="키워드" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="docNo"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></dd>
					<dd class="line"></dd>
					<dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="ownrNm"><u:msg titleId="dm.cols.ownr" alt="소유자" /></dd>
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
<c:set var="paramMap" value="${param }" scope="request"/><!-- 전체 param -->
<c:set var="initSrchMap" value="${initSrchMap }" scope="request"/><!-- 초기화할 paramMap -->
<c:set var="srchYmdMap" value="${srchYmdMap }" scope="request"/><!-- 일자 검색 paramMap -->

<c:if test="${!empty paramStorId }"><input type="hidden" id="paramStorId" value="${paramStorId}" /></c:if>
<c:if test="${!empty param.fromStorId }"><input type="hidden" id="fromStorId" value="${param.fromStorId}" /></c:if>
<c:if test="${!empty param.tgtStorId }"><input type="hidden" id="tgtStorId" value="${param.tgtStorId}" /></c:if>
<div class="entryzone unfoldArea" id="dtlSrchArea" style="display:none;">
      <div class="entryarea">
      <dl>
      	<c:if test="${!empty srchDispMap.regDt }"><!-- 등록일자 -->
      		<dd class="etr_blank"></dd>
	      	<dd class="etr_select">
	      		<c:set var="atrbId" value="regDt"/>
	      		<u:convertMap var="srchYmdMap" srcId="srchYmdMap" attId="${atrbId }" />
	      		<u:convertMap var="strtValue" srcId="srchYmdMap" attId="durStrtDt" />
				<u:convertMap var="endValue" srcId="srchYmdMap" attId="durEndDt" />
	      		<input type="hidden" id="durCat" name="durCat" value="regDt" class="notChkCls"/>
			  	  <div class="etr_calendar_lt">
				      <div class="etr_calendar">
					  <input id="durStrtDt${atrbId }" name="durStrtDt" value="${param.durStrtDt}" type="hidden" />
					  <div class="etr_calendarin">
						   <dl>
						   <dd class="ctxt" onclick="fnCalendar('durStrtDt${atrbId }','{end:\'durEndDt${atrbId }\'}');"><span id="durStrtDt${atrbId }">${strtValue}</span></dd>
						   <dd class="cdelete" onclick="fnTxtDelete(this,'durStrtDt${atrbId }');"></dd>
						   <dd class="cbtn" onclick="fnCalendar('durStrtDt${atrbId }','{end:\'durEndDt${atrbId }\'}');"></dd>
						   </dl>
					   </div>
					</div>
				</div>
			
		        <div class="etr_calendar_rt">
		            <div class="etr_calendar">
		            	<input id="durEndDt${atrbId }" name="durEndDt" value="${param.durEndDt}" type="hidden" />
		            	<div class="etr_calendarin">
		                    <dl>
		                    <dd class="ctxt" onclick="fnCalendar('durEndDt${atrbId }','{start:\'durStrtDt${atrbId }\'}');"><span id="durEndDt${atrbId }">${endValue}</span></dd>
		                    <dd class="cdelete" onclick="fnTxtDelete(this,'durEndDt${atrbId }');"></dd>
		                    <dd class="cbtn" onclick="fnCalendar('durEndDt${atrbId }','{start:\'durStrtDt${atrbId }\'}');"></dd>
	                    </dl>
	                    </div>
		            </div>
		        </div>
		    </dd>
      	</c:if>
      	<c:if test="${!empty srchDispMap.viewReqStatNm }"><!-- 요청상태(열람요청) -->
			<dd class="etr_blank"></dd>
			<u:convertMap var="value" srcId="paramMap" attId="viewReqStatCd" type="html" />
			<c:if test="${empty value }"><u:convertMap var="value" srcId="initSrchMap" attId="viewReqStatCd" type="html" /></c:if>
			<dd class="etr_select" id="viewReqStatCdContainer">
	        	<c:set var="viewReqStatNm" value=""/>
	        	<c:set var="viewReqStatCd" value=""/>
	            <div class="etr_open1" id="viewReqStatCdOpen" style="display:none">
	                <div class="open_in1">
	                    <div class="open_div">
	                    <dl>
	                    <c:if test="${isAdmin == false}"><dd class="txt" onclick="setSelOptions('viewReqStatCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd></c:if>
	                    <c:if test="${empty param.viewReqStatCd}"><c:set var="viewReqStatNm" value="${noSelect }"/></c:if>
	                    <c:forEach items="${srchDispMap.viewReqStatNm.colmVo.cdList}" var="cd" varStatus="status">
	                    <c:if test="${cd.cdId == value}"><c:set var="viewReqStatNm" value="${cd.rescNm }"/><c:set var="viewReqStatCd" value="${cd.cdId }"/></c:if>
						<c:if test="${(isAdmin == true && status.count>1) || (isAdmin == false && status.count>0) }"><dd class="line"></dd></c:if>
	                    <dd class="txt" onclick="setSelOptions('viewReqStatCd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
	                    </c:forEach>
	                 </dl>
	                    </div>
	                </div>
	            </div>
	            <input type="hidden" name="viewReqStatCd" value="${viewReqStatCd }" class="notChkCls"/>
	            <div class="etr_ipmany">
	                <div class="select_in1" onclick="holdHide = true; $('#viewReqStatCdContainer #viewReqStatCdOpen').toggle();">
	                <dl>
	                <dd class="select_txt" id="selectView"><span>${viewReqStatNm }</span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </div>
	        </dd>
		</c:if>
		<c:if test="${!empty srchDispMap.statNm }"><!-- 문서상태 -->
			<dd class="etr_blank"></dd>
			<u:convertMap var="value" srcId="paramMap" attId="statCd" type="html" />
			<c:if test="${empty value }"><u:convertMap var="value" srcId="initSrchMap" attId="statCd" type="html" /></c:if>
			<dd class="etr_select" id="statCdContainer">
	        	<c:set var="statNm" value=""/>
	        	<c:set var="statCd" value=""/>
	            <div class="etr_open1" id="statCdOpen" style="display:none">
	                <div class="open_in1">
	                    <div class="open_div">
	                    <dl>
	                    <dd class="txt" onclick="setSelOptions('statCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd>
	                    <c:if test="${empty param.statCd && empty value }"><c:set var="statNm" value="${noSelect }"/></c:if>
	                    <c:forEach items="${srchDispMap.statNm.colmVo.cdList}" var="cd" varStatus="status">
	                    <c:if test="${cd.cdId == value}"><c:set var="statNm" value="${cd.rescNm }"/><c:set var="statCd" value="${cd.cdId }"/></c:if>
						<dd class="line"></dd>
	                    <dd class="txt" onclick="setSelOptions('statCd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
	                    </c:forEach>
	                 </dl>
	                    </div>
	                </div>
	            </div>
	            <input type="hidden" name="statCd" value="${statCd }" class="notChkCls"/>
	            <div class="etr_ipmany">
	                <div class="select_in1" onclick="holdHide = true; $('#statCdContainer #statCdOpen').toggle();">
	                <dl>
	                <dd class="select_txt" id="selectView"><span>${statNm }</span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </div>
	        </dd>
		</c:if>
		<c:if test="${!empty srchDispMap.discStatNm }"><!-- 심의상태 -->
			<dd class="etr_blank"></dd>
			<u:convertMap var="value" srcId="paramMap" attId="discStatCd" type="html" />
			<c:if test="${empty value }"><u:convertMap var="value" srcId="initSrchMap" attId="discStatCd" type="html" /></c:if>
			<dd class="etr_select" id="discStatCdContainer">
	        	<c:set var="discStatNm" value=""/>
	        	<c:set var="discStatCd" value=""/>
	            <div class="etr_open1" id="discStatCdOpen" style="display:none">
	                <div class="open_in1">
	                    <div class="open_div">
	                    <dl>
	                    <dd class="txt" onclick="setSelOptions('discStatCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체선택"/>${noSelect }</dd>
	                    <c:if test="${empty param.discStatCd && empty value }"><c:set var="discStatNm" value="${noSelect }"/></c:if>
	                    <c:forEach items="${srchDispMap.discStatNm.colmVo.cdList}" var="cd" varStatus="status">
	                    <c:if test="${cd.cdId == value}"><c:set var="discStatNm" value="${cd.rescNm }"/><c:set var="discStatCd" value="${cd.cdId }"/></c:if>
						<dd class="line"></dd>
	                    <dd class="txt" onclick="setSelOptions('discStatCd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
	                    </c:forEach>
	                 </dl>
	                    </div>
	                </div>
	            </div>
	            <input type="hidden" name="discStatCd" value="${discStatCd }" class="notChkCls"/>
	            <div class="etr_ipmany">
	                <div class="select_in1" onclick="holdHide = true; $('#discStatCdContainer #discStatCdOpen').toggle();">
	                <dl>
	                <dd class="select_txt" id="selectView"><span>${discStatNm }</span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </div>
	        </dd>
		</c:if>
		<c:if test="${!empty dmBumkBVoList }"><!-- 즐겨찾기 -->
			<dd class="etr_blank"></dd>
			<dd class="etr_select" id="bumkIdContainer">
	        	<c:set var="bumkNm" value=""/>
	        	<c:set var="bumkId" value=""/>
	            <div class="etr_open1" id="bumkIdOpen" style="display:none">
	                <div class="open_in1">
	                    <div class="open_div">
	                    <dl>
	                    <c:forEach items="${dmBumkBVoList}" var="bumkVo" varStatus="status">
	                    <c:if test="${(empty param.bumkId && status.count == 1) || (!empty param.bumkId && bumkVo.bumkId == param.bumkId)}"><c:set var="bumkNm" value="${bumkVo.bumkNm }"/><c:set var="bumkId" value="${bumkVo.bumkId }"/></c:if>
						<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
	                    <dd class="txt" onclick="setSelOptions('bumkId',$(this).attr('data-code'),$(this).text());" data-code="${bumkVo.bumkId}">${bumkVo.bumkNm }</dd>
	                    </c:forEach>
	                 </dl>
	                    </div>
	                </div>
	            </div>
	            <input type="hidden" name="bumkId" value="${bumkId }" class="notChkCls"/>
	            <div class="etr_ipmany">
	                <div class="select_in1" onclick="holdHide = true; $('#bumkIdContainer #bumkIdOpen').toggle();">
	                <dl>
	                <dd class="select_txt" id="selectView"><span>${bumkNm }</span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </div>
	        </dd>
		</c:if>
      	<c:if test="${!empty srchDispMap.fldNm }"><!-- 폴더 -->
	    <dd class="etr_blank"></dd>
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
			<dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd>
			</dl>
			</div>
		</dd>
		</c:if>
		<c:if test="${!empty srchDispMap.clsNm }"><!-- 분류 -->
		<dd class="etr_blank"></dd>
		<dd class="etr_input">
			<div class="etr_ipmany">
			<dl>
			<dd class="etr_ip_lt">
				<div class="ip_txt" id="clsInfoArea" >
					<div id="idArea" style="display:none;">
						<c:set var="clsNmTmp" />
						<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
							<input type="hidden" id="clsId" name="clsId" value="${clsVo.clsId}" />
							<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
						</c:forEach>
					</div>
					<div id="nmArea" style="display:inline;"><input type="text" name="clsNm" id="clsNm" class="etr_iplt" value="${clsNmTmp}" style="width:55%;" readonly="readonly"/></div>
              	</div>
              	<div class="ip_delete" onclick="valueReset('clsInfoArea');"></div>
			</dd>
			<dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('C');"><u:msg titleId="dm.btn.clsSel" alt="분류선택"/></div></dd>
			</dl>
			</div>
		</dd>
		</c:if>
    	<dd class="etr_blank"></dd>
 		</dl>
    </div> 
</div>
</form>