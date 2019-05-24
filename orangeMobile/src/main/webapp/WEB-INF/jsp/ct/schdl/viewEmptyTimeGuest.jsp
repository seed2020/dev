<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<% //일자별 빈시간 확인 %>
function viewTable(code, value){
	$('div.timeAreaCls').hide();
	$('div #timeArea'+code).show();
};
           
<% //콤보박스 클릭 %>
function setOptionVal(id , code, value){
	$("input[name='"+id+"']").val(code);
	$("#"+id+"View span").text(value);
	$("#"+id+"Container").hide();
	viewTable(code,value);
};

$(document).ready(function() {
});           
//]]>
</script>
	<!--header S-->
    <div><a name="topgo"></a></div>
    <header class="subheader">
        <div class="back" onclick="history.back()"></div>
        <div class="subtit"><u:msg titleId="wc.btn.freeTmCnfm" alt="빈시간확인"/></div>
    </header> 
    <!--//header E-->
    
    <!--listsearch S-->
    <div class="listsearch">
        <div class="listselect">
        
            <div class="open1" id="viewDateContainer" style="display:none">
                <div class="open_in1">
                <div class="open_div">
                <dl>
                	<%
	                	String scdlStartDt = (String)request.getAttribute("scdlStartDt");
                		Integer scdGapDay = (Integer)request.getAttribute("scdGapDay");
                		int maxDays = scdGapDay+1;
                		String viewDate = "";
                		for(int i=0;i<maxDays;i++){
                			viewDate = StringUtil.addDate(scdlStartDt, i);
                	%>
	                <dd class="txt" onclick="setOptionVal('viewDate',$(this).attr('data-code'),$(this).text());" data-code="<%=i%>"><%=viewDate %></dd>
	                <%if(maxDays-1 != i){
	                %>
	                <dd class="line"></dd>
	                <%}} %>
             </dl>
                </div>
                </div>
            </div>
            
            <div class="select1">
                <div class="select_in1" onclick="$('#viewDateContainer').toggle();">
                <dl>
                <dd class="select_txt1" id="viewDateView"><span><%=scdlStartDt %></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </div>
    </div>
    <!--//listsearch E-->
    
    <div class="blank5"></div>
        
    <!--bookbody_area S-->
    <div class="bookbody_area">
    	<c:set var="strtSi" value="6" scope="page"/>
    	<c:set var="endSi" value="24" scope="page"/>
        <table class="bookbody">
            <colgroup>
            	<col />
            	<c:forEach var="si" begin="${strtSi }" end="${endSi }" step="2" varStatus="status">
            		<col width="8%"/>
            	</c:forEach>
            </colgorup>
            <tbody>
                <tr class="time_tarea">
                    <td class="time_t"></td>
                    <c:forEach var="si" begin="${strtSi }" end="${endSi }" step="2" varStatus="status">
	            		<td class="time_t">${si < 10 ? '0' : '' }${si }</td>
	            	</c:forEach>
                </tr>
            </tbody>
        </table>
    </div>
    <!--//bookbody_area E-->
        
    <!--bookbody_scroll_t S-->
    <div class="bookbody_scroll_t">
        <c:forEach var="guestUseDay" items="${guestScdlLst}" varStatus="mainStatus">
	        <!--bookbody_area S-->
	        <div class="bookbody_area timeAreaCls" id="timeArea${mainStatus.index }" <c:if test="${mainStatus.index > 0 }">style="display:none;"</c:if>>
	        <table class="bookbody">
	            <colgroup>
	                <col />
	                <c:forEach var="si" begin="${strtSi }" end="${endSi }" step="2" varStatus="status">
	            		<col width="8%"/>
	            	</c:forEach>
	            </colgorup>
	            <tbody>
	            	<c:forEach var="userList" items="${guestUidList}" varStatus="status">
	            		<tr>
		                    <td class="time_ct">${userList.guestNm}</td>
		                    <c:forEach var="si" begin="${strtSi }" end="${endSi }" step="2" varStatus="status2">
		                    	<td class="bk_body">
			                    	<c:set var="bktime_lt" value=""/>
			                    	<c:set var="bktime_rt" value=""/>
									<c:forEach var="guestUseTime" items="${guestUseDay}" varStatus="status3">
										<c:if test="${userList.guestUid == guestUseTime.userUid}">
											<c:if test="${guestUseTime.alldayYn eq 'Y' || (guestUseTime.startHour <= si && guestUseTime.endHour > si)}">
												<c:set var="bktime_lt" value="true"/>
											</c:if>
											<c:if test="${guestUseTime.alldayYn eq 'Y' || ( guestUseTime.startHour <= (si+1) && guestUseTime.endHour > (si+1))}">
												<c:set var="bktime_rt" value="true"/>
											</c:if>
										</c:if>
									</c:forEach>
									<c:if test="${!empty bktime_lt || !empty bktime_rt}">
										<div class="bk_time">
			                            <c:if test="${!empty bktime_lt }"><div class="bktime_lt"></div></c:if>
			                            <c:if test="${!empty bktime_rt }"><div class="bktime_rt"></div></c:if>
			                            </div>
									</c:if>
								</td>
		                    </c:forEach>
		                </tr>
	            	</c:forEach>
	                <tr>
	                    <td class="bk_line" colspan="11"></td>
	                </tr>
	            </tbody>
	        </table>
	        </div>
	        <!--//bookbody_area E-->
	    </c:forEach>
        
    </div>
    <!--//bookbody_scroll_t E-->