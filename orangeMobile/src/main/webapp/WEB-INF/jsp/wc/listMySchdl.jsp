<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${!empty param.schSchdlKndCd || !empty param.schSchdlTypCd || !empty param.schdlStartDt || !empty param.schdlEndDt}" var="unfoldareaUsed" value="Y" elseValue="N" />
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, './${listPage }.do?'+$('#searchForm').serialize());
};

<% //콤보박스 클릭 %>
function setOptionVal(id , code, value){
	var $form = $("#searchForm");
	$form.find("input[name='"+id+"']").val(code);
	$form.find("#"+id+"View span").text(value);
	$form.find("#"+id+"Container").hide();
};
	
function openChkSelect( list , title , id , handler){
	var html = [];
	html.push('<div class="entryzone" >');
	html.push('	<div class="entryarea">');
	html.push('		<dl>');
	html.push('		<dd class="etr_input">');
	html.push('			<div class="etr_ipmany">');
	html.push('				<dl>');
	var i, arr = list, size = list.length;
	for(i=0;i<size;i++){
		html.push('					<dd class="check" id="'+id+'" onclick="$ui.toggle(this, \'checkArea\');" data-cd="'+arr[i].cd+'" data-nm="'+arr[i].nm+'"><input name="'+id+'" type="checkbox" style="display:none" /></dd>');
		html.push('					<dd class="etr_body" onclick="$ui.toggle($(this).prev()[0], \'checkArea\');">'+arr[i].nm+'</dd><dd class="line"></dd>');
	}
	html.push('				</dl>');
	html.push('			</div>');
	html.push('		</dd>');
	html.push('		</dl>');
	html.push('	</div>');
	html.push('	<div class="blank5"></div>');
	html.push('</div>');	
	if(handler != null){
		html.push('<div class="btnarea">');
		html.push('	<div class="size">');
		html.push('	<dl>');
		html.push('	<dd class="btn" onclick="$m.nav.getWin().setCheckSelect('+handler+');"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>');
		html.push('	</dl>');
		html.push('	</div>');
		html.push('</div>');
	}
	$m.dialog.open({
		id:id+'SelectPop',
		title:title,
		html:html.join('\n')
	});	
};

function setCheckSelect(handler){
	var area = $('#checkArea');
	var checks = [];
	area.find('dd.check_on').each(function(){
		checks.push({cd:$(this).attr('data-cd'),nm:$(this).attr('data-nm')});
	});
	if(checks.length > 0){
		handler(checks);	
	}
};
<% //일정대상 %>
function schSchdlKndSelect(cd){
	var arrs = [];
	<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
		arrs.push({cd:'${list[0]}',nm:'${list[1]}'});
	</c:forEach>
	if(arrs.length > 0) openChkSelect(arrs,'<u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/>','schSchdlKnd', function(data){
		alert(2);
		//setSchdlKndSelect(data);
	});
};

<% //일정종류 %>
function schSchdlTypCdSelect(cd){
	var arrs = [];
	<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
		arrs.push({cd:'${list.catId}',nm:'${list.catNm}'});
	</c:forEach>
	if(arrs.length > 0) openChkSelect(arrs,'<u:msg titleId="cols.schdlKnd" alt="일정종류"/>','schSchdlKnd' , function(data){
		//setSchdlTypSelect(data);
	});
};

function fnUnfold(obj){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('#unfoldArea').toggle();
}

<% // 상세보기 %>
function viewSchdl(schdlId){
	var url = "/wc/viewMySchdl.do?schdlId="+schdlId+"&${params}";
	$m.nav.next(event, url);
};

var holdHide = false, holdHide2 = false, holdHide3 = false;
$(document).ready(function() {
	if('${unfoldareaUsed}' == 'Y')
		fnUnfold();
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

function fnCalendar(id,opt){
	$m.dialog.open({
	id:id,
	noPopbody:true,
	cld:true,
	url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
};

//]]>
</script>
<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />

<!--listsearch S-->
<div class="listsearch">
    <div class="listselect">
        <div class="select1">
            <div class="select_in1" >
            <dl>
            <dd class="select_txt1"><span><u:msg titleId="cols.subj" alt="제목" /></span></dd>
            <dd class="select_btn"></dd>
            </dl>
            </div>
        </div>
    </div>
    <div class="listinput2">
        <div class="input1">
        <dl>
        <dd class="input_left"></dd>
        <dd class="input_input"><input type="text" name="schWord" class="input_ip" value="${param.schWord}"/></dd>
        <dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
        </dl>
        </div>
    </div>
    <div class="unfoldbtn" id="unfold" onclick="fnUnfold();" ></div>
    <!--<div class="unfoldbtn_on"></div>-->
</div>
<!--entryzone S-->
<div class="entryzone" id="unfoldArea" style="display:none;">
    <div class="entryarea">
    <dl>
    	<dd class="etr_blank"></dd>
    	<dd class="etr_input">
            <div class="etr_ipmany">
            <dl>
            <dd class="etr_se_lt">
            	<div class="etr_open2" id="schSchdlKndCdContainer" style="display:none">
                    <div class="open_in1">
						<div class="open_div">
						<dl>
							<dd class="txt" onclick="setOptionVal('schSchdlKndCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
							<c:set var="cdNo" value="0"/>
                            <c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
								<c:if test="${cdNo > 0 }"><dd class="line"></dd></c:if>
								<c:if test="${empty schdlKndNm || !empty param.schSchdlKndCd && param.schSchdlKndCd == list[0]}">
									<c:set var="schdlKndNm" value="${list[1]}"/><c:set var="schSchdlKndCd" value="${list[0]}"/>
								</c:if>
								<dd class="txt" onclick="setOptionVal('schSchdlKndCd',$(this).attr('data-code'),$(this).text());" data-code="${list[0] }">${list[1] }</dd>
								<c:set var="cdNo" value="${cdNo+1 }"/>
							</c:forEach>
						</dl>
						</div>
					</div>
				</div>
				<div class="select_in1" onclick="$('#searchForm #schSchdlKndCdContainer').toggle();">
				<dl>
					<dd class="select_txt" id="schSchdlKndCdView" ><span><c:choose><c:when test="${!empty param.schSchdlKndCd }">${schdlKndNm }</c:when><c:otherwise><u:msg titleId="cm.option.all" alt="전체선택"/></c:otherwise></c:choose></span></dd>
					<dd class="select_btn"><input type="hidden" id="schSchdlKndCd" name="schSchdlKndCd"  value="${param.schSchdlKndCd }"/></dd>
				</dl>
				</div>
            </dd>
            <dd class="etr_se_rt">
            	<div class="etr_open3" id="schSchdlTypCdContainer" style="display:none">
				    <div class="open_in2">
						<div class="open_div">
						<dl>
							<dd class="txt" onclick="setOptionVal('schSchdlTypCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
							<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
                           		<c:if test="${status.index > 0 && fn:length(wcCatClsBVoList) > status.count }"><dd class="line"></dd></c:if>
                           		<c:if test="${!empty param.schSchdlTypCd && param.schSchdlTypCd == list.catId}"><c:set var="schdlTypNm" value="${list.catNm}"/></c:if>
								<dd class="txt" onclick="setOptionVal('schSchdlTypCd',$(this).attr('data-code'),$(this).text());" data-code="${list.catId }">${list.catNm }</dd>
							</c:forEach>
						</dl>
						</div>
					</div>
				</div>
				<div class="select_in2" onclick="$('#searchForm #schSchdlTypCdContainer').toggle();">
					<dl>
						<dd class="select_txt" id="schSchdlTypCdView"><span><c:choose><c:when test="${!empty param.schSchdlTypCd }">${schdlTypNm }</c:when><c:otherwise><u:msg titleId="cm.option.all" alt="전체선택"/></c:otherwise></c:choose></span></dd>
						<dd class="select_btn"><input type="hidden" id="schSchdlTypCd" name="schSchdlTypCd"  value="${param.schSchdlTypCd }"/></dd>
				   </dl>
				</div>
            </dd>
            </dl>
            </div>
        </dd>
        <dd class="etr_blank"></dd>
        <dd class="etr_select">
            <div class="etr_calendar_lt">
                <div class="etr_calendar">
                	<input id="schdlStartDt" name="schdlStartDt" value="${param.schdlStartDt}" type="hidden" />
                    <div class="etr_calendarin">                    	
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('schdlStartDt','{end:\'schdlEndDt\'}');"><span id="schdlStartDt">${param.schdlStartDt}</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'schdlStartDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('schdlStartDt','{end:\'schdlEndDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
            <div class="etr_calendar_rt">
                <div class="etr_calendar" >
                	<input id="schdlEndDt" name="schdlEndDt" value="${param.schdlEndDt}" type="hidden" />
                    <div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('schdlEndDt','{start:\'schdlStartDt\'}');"><span id="schdlEndDt">${param.schdlEndDt}</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'schdlEndDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('schdlEndDt','{start:\'schdlStartDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
        </dd>
    </dl>
    </div>
</div>
                
<!--//listsearch E-->
</form>


<!--section S-->
<section>
<div class="listarea">
<article>
	<c:choose>
		<c:when test="${!empty wcSchdlBVoList}">
			<c:forEach  var="list" items="${wcSchdlBVoList}"  varStatus="status">
			
          <div class="listdiv" onclick="javascript:viewSchdl('${list.schdlId }');">
              <div class="list">
              <dl>
              <dd class="tit">${list.subj }</dd>
              <dd class="body">
              	<c:choose>
					<c:when test="${list.schdlKndCd == 1}"><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"/></c:when>
					<c:when test="${list.schdlKndCd == 2}"><u:msg titleId="wc.jsp.listPsnSchdl.grp.title" alt="그룹일정"/></c:when>
					<c:when test="${list.schdlKndCd == 3}"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"/></c:when>
					<c:when test="${list.schdlKndCd == 4}"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"/></c:when>
				</c:choose> ㅣ
				<c:choose>
					<c:when test="${list.schdlTypCd == '1'}"><u:msg titleId="wc.cols.prom" alt="약속"/></c:when>
					<c:when test="${list.schdlTypCd == '2'}"><u:msg titleId="wc.cols.work" alt="할일"/></c:when>
					<c:when test="${list.schdlTypCd == '3'}"><u:msg titleId="wc.cols.evnt" alt="행사"/></c:when>
					<c:when test="${list.schdlTypCd == '4'}"><u:msg titleId="wc.cols.annv" alt="기념일"/></c:when>
					<c:otherwise>${list.schdlTypNm }</c:otherwise>
				</c:choose> 
				</dd>
              <dd class="name">
				  	<fmt:parseDate var="strtDate" value="${list.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="strtYmd" value="${strtDate}" pattern="yyyy-MM-dd"/> 
					<fmt:formatDate var="strtTime" value="${strtDate}" pattern="HH:mm"/>
					<fmt:parseDate var="endDate" value="${list.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="endYmd" value="${endDate}" pattern="yyyy-MM-dd"/> 
					<fmt:formatDate var="endTime" value="${endDate}" pattern="HH:mm"/>
					<c:choose>
						<c:when test="${list.alldayYn eq 'Y'}">${strtYmd }~${endYmd }</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${strtYmd eq endYmd}">${strtYmd }</c:when>
								<c:otherwise>${strtYmd }~${endYmd }</c:otherwise>
							</c:choose>
						(${strtTime} ~ ${endTime})
						</c:otherwise>
					</c:choose>
              </dd>
           </dl>
              </div>
          </div>
			
			 </c:forEach>   
		</c:when>
		<c:otherwise>
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:otherwise>
	</c:choose>
	</article>
    
</div>
<m:pagination />
	
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<!--//section E-->
