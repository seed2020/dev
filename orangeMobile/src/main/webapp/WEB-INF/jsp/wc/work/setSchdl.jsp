<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="viewPageParams" excludes="menuId,noCache"/>
<script type="text/javascript">
//<![CDATA[
<% // 달력 선택 %>
function fnCalendar(id){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}

<% //콤보박스 클릭 %>
function setOptionVal(id, code, value){
	var $form = $("#setRegForm");
	$form.find("input[name='"+id+"']").val(code);
	$form.find("#"+id+"View span").text(value);
	$form.find("#"+id+"Container").hide();
};

function save(){
	if(!validator.validate('setRegForm')){
		return;
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#setRegForm');
			$form.attr('action','/wc/work/transSchdlPost.do?menuId=${menuId}');
			$m.nav.post($form);
		}
	});
};

<% //사용자 조회%>
function openSingleUser(areaNm){
	$m.user.selectOneUser({userUid:'${wcWorkSchdlBVo.userUid}'}, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
			return false;
		} else {
			$('#setRegForm').find("input[id='"+areaNm+"Uid']").val(userVo.userUid);
			$('#setRegForm').find("input[id='"+areaNm+"Nm']").val(userVo.rescNm);
			$('#setRegForm').find("input[id='subj']").val(userVo.rescNm);
			return true;
		}
	});
}
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
});
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
            <u:secu auth="A"><dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></u:secu>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setRegForm" name="setRegForm" >
		<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
		<c:if test="${!empty wcWorkSchdlBVo.schdlId }">
			<input id="schdlId" name="schdlId" type="hidden" value="${wcWorkSchdlBVo.schdlId }"/>
		</c:if>
    	<input type="hidden" id="listPage" name="listPage" value="/wc/work/listCalendar.do?menuId=${menuId}&${paramsForList}" />
    	<input type="hidden" id="viewPage" name="viewPage" value="/wc/work/viewSchdl.do?menuId=${menuId}&${viewPageParams}" />
    	
    	<!-- 일정종류 -->
    	<u:set var="schdlTypCd" test="${empty wcWorkSchdlBVo.schdlId && !empty wcCatClsBVoList }" value="${wcCatClsBVoList[0].cd }" elseValue="${wcWorkSchdlBVo.schdlTypCd }"/>
    	<input type="hidden" id="schdlTypCd" name="schdlTypCd"  value="${schdlTypCd }"/>
    	
		<u:set var="tabNo" test="${empty param.tabNo }" value="0" elseValue="${param.tabNo }"/>
			
	    <div class="entryzone">
	        <div class="entryarea">
	        <dl>
		        <dd class="etr_tit"><u:msg titleId="wc.btn.schdl${!empty wcWorkSchdlBVo.schdlId ? 'Mod' : 'Reg'}" alt="일정 등록/수정" /></dd>
		        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></dd>
		        <dd class="etr_select">
                     <div class="etr_open1" id="schdlTypCdContainer" style="display:none">
                        <div class="open_in1">
                            <div class="open_div">
                            <dl>
                            	<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
                            		<c:if test="${status.index > 0 && fn:length(wcCatClsBVoList) > status.count }"><dd class="line"></dd></c:if>
                            		<c:if test="${!empty wcWorkSchdlBVo.schdlId && wcWorkSchdlBVo.schdlTypCd == list.cd}"><c:set var="schdlTypNm" value="${list.rescNm}"/></c:if>
									<dd class="txt" onclick="setOptionVal('schdlTypCd',$(this).attr('data-code'),$(this).text());" data-code="${list.cd }">${list.rescNm }</dd>									
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                    </div>
                    <c:if test="${ empty wcWorkSchdlBVo.schdlId && !empty wcCatClsBVoList}"><c:set var="schdlTypNm" value="${wcCatClsBVoList[0].rescNm}"/></c:if>
                    <div class="etr_ipmany">
                        <div class="select_in1" onclick="$('#setRegForm #schdlTypCdContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="schdlTypCdView"><span>${schdlTypNm }</span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </div>
                </dd>
                <dd class="etr_bodytit_asterisk"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></dd>
                <dd class="etr_input">
					<div class="etr_ipmany">
					<dl>
					<dd class="etr_ip_lt">
						<div id="ownrInfoArea" >
							<div id="idArea" style="display:none;"><input type="hidden" id="userUid" name="userUid" value="${wcWorkSchdlBVo.userUid }"/></div>
							<div id="nmArea" style="display:inline;"><m:input type="text" id="userNm" name="userNm" className="etr_iplt" titleId="wc.cols.schdlKndCd" value="${wcWorkSchdlBVo.userNm }" style="width:55%;" readonly="Y" mandatory="Y"/></div>
		              	</div>
					</dd>
					<dd class="etr_se_rt" ><div class="etr_btn" onclick="openSingleUser('user');"><u:msg titleId="cm.btn.choice" alt="선택" /></div></dd>
					</dl>
					</div>
				</dd>
                <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
                <dd class="etr_input"><div class="etr_inputin"><input type="text" class="etr_iplt" name="subj" id="subj" value="${wcWorkSchdlBVo.subj }"/></div></dd>
                <dd class="etr_bodytit_asterisk"><u:msg titleId="wc.cols.date" alt="일자"/></dd>
		        <dd class="etr_select"><fmt:parseDate var="dateStartDt" value="${wcWorkSchdlBVo.strtDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
		            <div class="etr_calendar">
		            	<input id="startYmd" name="startYmd" type="hidden" value="${convStartDt}"/>
		                <div class="etr_calendarin">
                        <dl>
                        <dd class="ctxt" onclick="fnCalendar('startYmd');"><span id="startYmd">${convStartDt}</span></dd>
                        <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                        <dd class="cbtn" onclick="fnCalendar('startYmd');"></dd>
                        </dl>
                        </div>
		             </div>
		        </dd>
		        <dd class="etr_bodytit"><u:msg titleId="cols.cont" alt="내용" /></dd>
           		<dd class="etr_input" ><div class="etr_textareain"><textarea rows="3" id="cont" name="cont" class="etr_ta"  onfocus="$('#focusLayout').show();" onblur="$('#focusLayout').hide();">${wcWorkSchdlBVo.cont}</textarea></div></dd>
        	</dl>
    	</div>
 	</div>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <u:secu auth="A"><dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></u:secu>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>