<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<% // 등록 %>
function setSchdl(schdlId){
	$m.dialog.close('listSchdlPop');
	var url = "/wc/work/setSchdl.do?menuId=${menuId}&${paramsForList }";
	if(schdlId != null) {
		url+= "&schdlId="+schdlId;
	}
	if(arguments.length > 1){
		url+= "&strtDt="+arguments[1];
	}else if(arguments.length==0){
		url+= "&strtDt="+moment().format('YYYYMMDD');
	}
	$m.nav.next(event, url);
};
<% // 삭제 %>
function fnDelete(flag,schdlId){
	$m.msg.confirmMsg("cm.cfrm.del", null , function(result){
		if(result){
			$m.ajax('/wc/work/transSchdlDelAjx.do?menuId=${menuId}', {schdlId:schdlId}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					goList();
				}
			});
		}
	});
}

<% // 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, '/wc/work/listCalendar.do?menuId=${menuId}&${paramsForList}');
}

$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
});

//]]>
</script>
<section>
       <!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
		       <u:secu auth="A"><dd class="btn" onclick="setSchdl('${wcWorkSchdlBVo.schdlId }');"><u:msg titleId="cm.btn.mod" alt="수정" /></dd>
		       <dd class="btn" onclick="fnDelete('del','${wcWorkSchdlBVo.schdlId }');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></u:secu>
	       		<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           		<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        	</dl>
      	 </div>
       </div>
       <!--//btnarea E-->
        
		<div id="tabViewArea">
			<!--listtablearea S-->
			<div  class="s_tablearea">
				<div class="blank30"></div>
				<table class="s_table">
					<!-- <caption>타이틀</caption> -->
					<colgroup>
					<col width="33%"/>
					<col width=""/>
					</colgroup>
					<tbody>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></th>
						<td class="shead_lt"><u:out value="${wcWorkSchdlBVo.schdlTypNm }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></th>
						<td class="shead_lt"><u:out value="${wcWorkSchdlBVo.userNm }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목"/></th>
						<td class="shead_lt"><u:out value="${wcWorkSchdlBVo.subj }" /></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="wc.cols.date" alt="일자"/></th>
						<td class="shead_lt"><u:out value="${wcWorkSchdlBVo.strtDt}" type="date"/></td>
					</tr>
					<tr>
						<th class="shead_lt"><u:msg titleId="cols.cont" alt="내용" /></th>
						<td class="shead_lt"><textarea rows="3" id="cont" name="cont" class="etr_ta"  onfocus="$('#focusLayout').show();" onblur="$('#focusLayout').hide();" style="width:95%;">${wcWorkSchdlBVo.cont}</textarea></td>
					</tr>
				</tbody>
			</table>
		</div>
       </div>
       <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />      
</section>


