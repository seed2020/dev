<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[
function searchList(event){
	$m.nav.curr(event, '/wb/findBcPwsmSub.do?'+$('#searchForm').serialize());
}

//동명이인 > 원본 수정
function fnPwsmUpdate(bcId){
	$m.nav.next(event, '/wb/${param.setPage}.do?menuId=${menuId}&typ=${param.typ}${agntParam}&bcId='+bcId);
};

//동명이인 선택
function fnPwsmSelect(toBcId){
	$m.nav.next(event, '/wb/${param.setPage}.do?menuId=${menuId}&typ=${param.typ}${agntParam}&toBcId='+toBcId);
};


$(document).ready(function() {
});

<% // [팝업:선택] 선택된 값 조회 %>
<%-- function selChecked() {
	var $checked = $('#findBcPwsmFrm').contents().find('input[name="bcNmCheck"]:checked');
	if ($checked.length == 0 || $checked.val() == '') {
		alertMsg('cm.msg.noSelect'); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return null;
	}
	return $checked.val();
}; --%>

<% // 동명이인 정보 선택 %>
function setChecked(value) {
	//var value = selChecked();
	if( value != null) {
		fnPwsmSelect(value);
	}
};

<% // 동명이인 원본정보 수정 %>
function setOriginalUpdate(value) {
	//var value = selChecked();
	if( value != null) {
		fnPwsmUpdate(value);
	}
};

//]]>
</script>
<section>
<form id="searchForm" name="searchForm" action="./${listPage }.do" onsubmit="searchList(event);">
<input type="hidden" name="menuId" value="${param.menuId}" />
<input type="hidden" name="schCat" id="schCat" value="bcNm" />
<div class="listsearch">
		<div class="listselect">
			<div class="select1 schTxt1">
			<div class="select_in1" >
			<dl>
				<dd class="select_txt1"><span><u:msg titleId="cols.nm" alt="이름" /></span></dd>
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
             
	<c:if test="${fn:length(wbBcBVoList) == 0}">
            <div class="listdiv_nodata" >
            <dl>
            <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
            </dl>
            </div>
	</c:if>
	<c:if test="${fn:length(wbBcBVoList) > 0}">
		<c:forEach var="list" items="${wbBcBVoList}" varStatus="status">
                  <div class="listdiv" onclick="">
                      <div class="list">
                      <dl>
                      <dd class="tit">
                      	${list.bcNm } ${!empty list.compNm ? '/' : ''} ${list.compNm }
                      </dd>
                      <dd class="name">
                      	${list.dftCntc }
                      	${!empty list.dftCntc && !empty list.deptNm ? '/' : ''}
						${list.deptNm }
					  </dd>
					  <c:if test="${writeAuth == 'Y' }">
                        <dd><div class="list_btnarea">
                            <div class="size">
                            <dl>
                            <dd class="btn" onclick="setOriginalUpdate('${list.bcId }');"><u:msg titleId="wb.btn.set.original" alt="원본수정"/></dd>
                            <dd class="btn" onclick="setChecked('${list.bcId }');"><u:msg titleId="cm.btn.copy" alt="복사"/></dd>
	                        </dl>
                            </div>
                            </div>
                        </dd>
                      </c:if>  
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


