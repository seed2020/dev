<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
};

<% //콤보박스 클릭 %>
function setOptionVal(id , code, value){
	var $form = $("#setRepetContainer");
	$form.find("input[name='"+id+"']").val(code);
	$form.find("#"+id+"View span").text(value);
	$form.find("#"+id+"Container").hide();
};

<% //반복설정 저장 %>
function setRepetSelect(){
	var repetKnd = $('input[name="repetKnd"]').val();
	if(repetKnd == 'WELY'){
		var dowChk='';
		$('input[name="dow"]:checkbox:checked').each(function(index){
			dowChk += $(this).val() + "/";});
		if(dowChk==''){
			$m.dialog.alert("주간에 요일을 선택하여 주세요.");
			return;
		}
	}
	
	if( $("#repetchoiDisDt").val() == ''||$("#repetcmltDisDt").val()==''){
		$m.dialog.alert("반복기간에 시작일과 종료일을 입력 하여 주세요.");
		return;
	}
	
	var arr = [];
	var result = null;

	obj={};
	$this = $(this);
	var repetSetup = 'Y';
	if(repetKnd == 'DALY'){
		arr = {repetSetup:repetSetup, 
				  repetKnd:repetKnd, 
				  dalySelect: $('input[name="dalySelect"]').val(),
				  repetchoiDt: $("#repetchoiDisDt").val(),
				  repetcmltDt: $("#repetcmltDisDt").val()};
	}else if(repetKnd == 'WELY'){
		//체크박스 
		var dowChk='';
		$('input[name="dow"]:checkbox:checked').each(function(index){
			dowChk += $(this).val() + "/";});
		arr = {repetSetup:repetSetup,
				  repetKnd:repetKnd,
				  welySelect: $('input[name="welySelect"]').val(),
				  dow:dowChk,
				  repetchoiDt: $("#repetchoiDisDt").val(),
				  repetcmltDt: $("#repetcmltDisDt").val()};
		
	}else if(repetKnd == 'MOLY'){
		if($(':radio[name="molyKnd"]:checked').val() == '1'){
			arr = {repetSetup:repetSetup,
					  repetKnd:repetKnd,
					  molyKnd: $(':radio[name="molyKnd"]:checked').val(),
					  firMolySelect: $('input[name="firMolySelect"]').val(),
					  firMolyDaySelect: $('input[name="firMolyDaySelect"]').val(),
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()};
		}else{
			arr = {repetSetup:repetSetup,
					  repetKnd:repetKnd,
					  molyKnd: $(':radio[name="molyKnd"]:checked').val(),
					  secMolySelect: $('input[name="secMolySelect"]').val(),
					  secMolyWeekSelect: $('input[name="secMolyWeekSelect"]').val(),
					  secMolyWeekOfDaySelect: $('input[name="secMolyWeekOfDaySelect"]').val(),
					  repetchoiDt: $("#repetchoiDisDt").val(),
					  repetcmltDt: $("#repetcmltDisDt").val()};
		}

	}else if(repetKnd == 'YELY'){
		if($(':radio[name="yelyKnd"]:checked').val() == '1'){
			arr = {repetSetup:repetSetup,
				  repetKnd:repetKnd,
				  yelyKnd: $(':radio[name="yelyKnd"]:checked').val(),
				  firYelySelect: $('input[name="firYelySelect"]').val(),
				  firYelyDaySelect: $('input[name="firYelyDaySelect"]').val(),
				  repetchoiDt: $("#repetchoiDisDt").val(),
				  repetcmltDt: $("#repetcmltDisDt").val()};
		
		}else{
			arr = {repetSetup:repetSetup,
				  repetKnd:repetKnd,
				  yelyKnd: $(':radio[name="yelyKnd"]:checked').val(),
				  secYelySelect: $('input[name="secYelySelect"]').val(),
				  secYelyWeekSelect: $('input[name="secYelyWeekSelect"]').val(),
				  secYelyWeekOfDaySelect: $('input[name="secYelyWeekOfDaySelect"]').val(),
				  repetchoiDt: $("#repetchoiDisDt").val(),
				  repetcmltDt: $("#repetcmltDisDt").val()};
		}
	}
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	win.setRepetSetup(arr);
	history.back();
};

<% // 취소 %>
function setRepetPopClose(){
	<c:if test="${!empty param.schdlId }">
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	win.chkFileSetting();
	</c:if>
	history.back();
}

//]]>
</script>
<c:choose>
	<c:when test="${param.repetKnd eq 'WELY' }"><u:msg var="repetMsg" titleId="wc.option.wely" alt="주간"/></c:when>
	<c:when test="${param.repetKnd eq 'MOLY' }"><u:msg var="repetMsg" titleId="wc.option.moly" alt="월간"/></c:when>
	<c:when test="${param.repetKnd eq 'YELY' }"><u:msg var="repetMsg" titleId="wc.option.yely" alt="연간"/></c:when>
	<c:otherwise><u:msg var="repetMsg" titleId="wc.option.daly" alt="일일"/></c:otherwise>
</c:choose>
<!--header S-->
<header>
    <div class="back" onclick="history.back();"></div>
    <div class="subtit2"><u:msg titleId="wc.btn.repetSetup" alt="반복설정"/>-${repetMsg }</div>
    <div class="save" onclick="setRepetSelect();"></div>
</header> 
<!--//header E-->
 <!--section S-->
<section>
    
<!--blankzone S-->
<div class="blankzone">
    <div class="blank20"></div>
</div>
<!--//blankzone E-->
<input type="hidden" name="repetKnd" value="${param.repetKnd }"/>
<div class="entryzone" id="setRepetContainer">
	<div class="entryarea">
		<dl>
		<dd class="etr_bodytit">${repetMsg }</dd>		
		<c:choose>
			<c:when test="${param.repetKnd eq 'WELY' }">
				<input type="hidden" name="welySelect" value="1"/>
				<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    <dd>
                        <div class="open_divfr" id="welySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="8" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('welySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #welySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="welySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" /></dd>
                    </dl>
                    </div>
                </dd>
                <dd class="etr_line1"></dd>
               
               <dd class="etr_select2">
                    <div class="bookbody_area">
                    <table class="bookbody">
                    <colgroup>
                        <col />
                        <col width="14%"/>
                        <col width="14%"/>
                        <col width="14%"/>
                        <col width="14%"/>
                        <col width="14%"/>
                        <col width="14%"/>
                        </colgorup>
                    <tr class="time_tarea">
                        <td class="time_t2"><u:msg titleId="wc.option.sun" alt="일"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.mon" alt="월"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.tue" alt="화"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.wed" alt="수"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.thu" alt="목"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.fri" alt="금"/></td>
                        <td class="time_t2"><u:msg titleId="wc.option.sat" alt="토"/></td>
                    </tr>
                    <tr>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="SUN" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="MON" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="TUE" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="WED" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="THU" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="FRI" /></div></div></td>
                        <td class="bk_body2"><div class="ct"><div class="check" onclick="$ui.toggle(this, '');"><input name="dow" type="checkbox" style="display:none" value="SAT" /></div></div></td>
                    </tr>
                    </table>
                    </div>
                </dd>
                <dd class="etr_line1"></dd>
			</c:when>
			<c:when test="${param.repetKnd eq 'MOLY' }">
				<input type="hidden" name="firMolySelect" value="1"/>
				<input type="hidden" name="firMolyDaySelect" value="1"/>
				
				<input type="hidden" name="secMolySelect" value="1"/>
				<input type="hidden" name="secMolyWeekSelect" value="1"/>
				<input type="hidden" name="secMolyWeekOfDaySelect" value="1"/>
				<dd class="etr_input">
                    <div class="etr_ipmany" >
                    <dl>
                    	<m:check type="radio" id="molyKnd" name="molyKnd" inputId="molyKnd" value="1" areaId="setRepetContainer" checked="true" />
                    	<dd>
                        <div class="open_divfr" id="firMolySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="60" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('firMolySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        <div class="select_in1" onclick="$('#setRepetContainer #firMolySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="firMolySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /></dd>
                    <dd class="wblank5"></dd>
                    
                    
                    <dd>
                        <div class="open_divfr" id="firMolyDaySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="31" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('firMolyDaySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #firMolyDaySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="firMolyDaySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" /></dd>
                    </dl>
                    </div>
                </dd>
                <dd class="etr_blank6"></dd>
				
				<dd class="etr_input">
                    <div class="etr_ipmany" >
                    <dl>
                    	<m:check type="radio" id="molyKnd" name="molyKnd" inputId="molyKnd" value="2" areaId="setRepetContainer" checked="false" />
                    	<dd>
                        <div class="open_divfr" id="secMolySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="60" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('secMolySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #secMolySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="secMolySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /></dd>
                    	
                    </dl>
                    </div>
                </dd>
				<dd class="etr_blank6"></dd>
				
				<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    <dd class="etr_se_lt">
                        <div class="etr_open2" id="secMolyWeekSelectContainer" style="display:none">
                            <div class="open_in1">
                                <div class="open_div">
                                <dl>
                                	<c:forEach begin="1" end="5" step="1" var="no">
										<dd class="txt" onclick="setOptionVal('secMolyWeekSelect',$(this).attr('data-code'),$(this).text());" data-code="${no }"><u:msg titleId="wc.cols.week${no }" alt="${no }주차" /></dd>
										<dd class="line"></dd>
									</c:forEach>
	                            </dl>
                                </div>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #secMolyWeekSelectContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="secMolyWeekSelectView"><span><u:msg titleId="wc.cols.week1" alt="1주차" /></span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="etr_se_rt">
                        <div class="etr_open3" id="secMolyWeekOfDaySelectContainer" style="display:none">
                            <div class="open_in2">
                                <div class="open_div">
                                <dl>
                                	<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="1"><u:msg titleId="wc.option.sun" alt="일요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="2"><u:msg titleId="wc.option.mon" alt="월요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="3"><u:msg titleId="wc.option.tue" alt="화요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="4"><u:msg titleId="wc.option.wed" alt="수요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="5"><u:msg titleId="wc.option.thu" alt="목요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="6"><u:msg titleId="wc.option.fri" alt="금요일" /></dd>
									<dd class="txt" onclick="setOptionVal('secMolyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="7"><u:msg titleId="wc.option.sat" alt="토요일" /></dd>                                
	                            </dl>
                                </div>
                            </div>
                        </div>
                        
                        <div class="select_in2" onclick="$('#setRepetContainer #secMolyWeekOfDaySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="secMolyWeekOfDaySelectView"><span><u:msg titleId="wc.option.sun" alt="일요일" /></span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </dd>
                    
                    </dl>
                    </div>
                </dd>
				
			</c:when>
			<c:when test="${param.repetKnd eq 'YELY' }">
				<input type="hidden" name="firYelySelect" value="1"/>
				<input type="hidden" name="firYelyDaySelect" value="1"/>
				
				<input type="hidden" name="secYelySelect" value="1"/>
				<input type="hidden" name="secYelyWeekSelect" value="1"/>
				<input type="hidden" name="secYelyWeekOfDaySelect" value="1"/>
				<dd class="etr_input">
                    <div class="etr_ipmany" >
                    <dl>
                    	<m:check type="radio" id="yelyKnd" name="yelyKnd" inputId="yelyKnd" value="1" areaId="setRepetContainer" checked="true" />
                    	<dd>
                        <div class="open_divfr" id="firYelySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="12" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('firYelySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #firYelySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="firYelySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" /></dd>
                    <dd class="wblank5"></dd>
                    
                    <dd>
                        <div class="open_divfr" id="firYelyDaySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="31" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('firYelyDaySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #firYelyDaySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="firYelyDaySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" /></dd>
                    
                    </dl>
                    </div>
                </dd>
				<dd class="etr_blank6"></dd>
				<dd class="etr_input">
                    <div class="etr_ipmany" >
                    <dl>
                    	<m:check type="radio" id="yelyKnd" name="yelyKnd" inputId="yelyKnd" value="2" areaId="setRepetContainer" checked="false" />
                    	<dd>
                        <div class="open_divfr" id="secYelySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="12" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('secYelySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #secYelySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="secYelySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" /></dd>
                    
                    </dl>
                    </div>
                </dd>
				<dd class="etr_blank6"></dd>
				
				<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    <dd class="etr_se_lt">
                        <div class="etr_open2" id="secYelyWeekSelectContainer" style="display:none">
                            <div class="open_in1">
                                <div class="open_div">
                                <dl>
                                <c:forEach begin="1" end="5" step="1" var="no">
									<dd class="txt" onclick="setOptionVal('secYelyWeekSelect',$(this).attr('data-code'),$(this).text());" data-code="${no }"><u:msg titleId="wc.cols.week${no }" alt="${no }주차" /></dd>
								</c:forEach>
	                            </dl>
                                </div>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #secYelyWeekSelectContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="secYelyWeekSelectView"><span><u:msg titleId="wc.cols.week1" alt="1주차" /></span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="etr_se_rt">
                        <div class="etr_open3" id="secYelyWeekOfDaySelectContainer" style="display:none">
                            <div class="open_in2">
                                <div class="open_div">
                                <dl>
                                <dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="1"><u:msg titleId="wc.option.sun" alt="일요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="2"><u:msg titleId="wc.option.mon" alt="월요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="3"><u:msg titleId="wc.option.tue" alt="화요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="4"><u:msg titleId="wc.option.wed" alt="수요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="5"><u:msg titleId="wc.option.thu" alt="목요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="6"><u:msg titleId="wc.option.fri" alt="금요일" /></dd>
								<dd class="txt" onclick="setOptionVal('secYelyWeekOfDaySelect',$(this).attr('data-code'),$(this).text());" data-code="7"><u:msg titleId="wc.option.sat" alt="토요일" /></dd>
	                            </dl>
                                </div>
                            </div>
                        </div>
                        
                        <div class="select_in2" onclick="$('#setRepetContainer #secYelyWeekOfDaySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="secYelyWeekOfDaySelectView"><span><u:msg titleId="wc.option.sun" alt="일요일" /></span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </dd>
                    
                    </dl>
                    </div>
                </dd>
				
			</c:when>
			<c:otherwise>
				<input type="hidden" name="dalySelect" value="1"/>
				<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    <dd>
                        <div class="open_divfr" id="dalySelectContainer" style="display:none">
                            <div class="open_div">
                            <dl>
                            	<c:forEach begin="1" end="60" step="1" var="no">
                            		<c:if test="${no > 1 }"><dd class="line"></dd></c:if>
									<dd class="txt" onclick="setOptionVal('dalySelect',$(this).attr('data-code'),$(this).text());" data-code="${no }">${no }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                        
                        <div class="select_in1" onclick="$('#setRepetContainer #dalySelectContainer').toggle();">
                        <dl>
                        <dd class="select_txtfr" id="dalySelectView"><span>1</span></dd>
                        <dd class="select_btnfr"></dd>
                        </dl>
                        </div>
                    </dd>
                    <dd class="wblank5"></dd>
                    <dd class="etr_body"><u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" /></dd>
                    </dl>
                    </div>
                </dd>
                
                <dd class="etr_line1"></dd>
			</c:otherwise>
		</c:choose>
		<dd class="etr_blank6"></dd>
		<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.repetPrd" alt="반복기간" /></dd>
        <dd class="etr_select">
        	<div class="etr_calendar_lt">
                <div class="etr_calendar">
                	<input id="repetchoiDisDt" name="repetchoiDisDt" value="${param.repetchoiDisDt}" type="hidden" />
                    <div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('repetchoiDisDt','{end:\'repetcmltDisDt\'}');"><span id="repetchoiDisDt">${param.repetchoiDisDt}</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'repetchoiDisDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('repetchoiDisDt','{end:\'repetcmltDisDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
            <div class="etr_calendar_rt">
                <div class="etr_calendar">
                	<input id="repetcmltDisDt" name="repetcmltDisDt" value="" type="hidden" />
                    <div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('repetcmltDisDt','{start:\'repetchoiDisDt\'}');"><span id="repetcmltDisDt"></span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'repetcmltDisDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('repetcmltDisDt','{start:\'repetchoiDisDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
        </dd>
        <dd class="etr_line1"></dd>    
		</dl>
	</div>
</div>

<div class="blank20"></div>
<div class="btnarea">
	<div class="size">
		<dl>
		<dd class="btn" onclick="setRepetSelect();"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
		<dd class="btn" onclick="setRepetPopClose();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
		</dl>
	</div>
</div>
</section>
<!--//section E-->

