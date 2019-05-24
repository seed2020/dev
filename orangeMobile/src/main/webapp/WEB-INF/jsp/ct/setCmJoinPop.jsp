<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[

function fnConfirm(){
		$m.ajax('/ct/setCmntJoin.do?menuId=${param.menuId}', {ctId:'${joinCtEstbBVo.ctId}'}, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
			}
			if (data.result == 'ok') {
				var win = $m.nav.getWin();
				if(win==null) return;
				win.searchList();
				$m.dialog.close('setCmJoin');
			}
		});
}
//]]>
</script>

<div class="s_tablearea">
	<div class="blank15"></div>


	<table class="s_table">
	<%-- <caption><u:msg titleId="or.jsp.setUserPop.ref" alt="참조정보"/></caption> --%>
	<colgroup>
		<col width="33%"/>
		<col width=""/>
	</colgroup>
	<tbody>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.cmNm" alt="커큐니티명" /></th>
			<td class="shead_lt">${joinCtEstbBVo.ctNm}</td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.mast" alt="마스터" /></th>
			<td class="sbody_lt"><a href="javascript:;" onclick="$m.user.viewUserPop('${joinCtEstbBVo.mastUid}');">${joinCtEstbBVo.mastNm}</a></td>
		</tr>
		<tr>
			<th class="shead_lt"><u:msg titleId="cols.joinMet" alt="가입방법" /></th>
			<td class="shead_lt">
				<c:choose>
					<c:when test="${joinCtEstbBVo.joinMet == '1'}">
						<u:msg titleId="ct.jsp.setCmJoin.joinMet02" alt="즉시 가입됩니다." />
					</c:when>
					<c:when test="${joinCtEstbBVo.joinMet == '2'}">
						<u:msg titleId="ct.jsp.setCmJoin.joinMet01" alt="커뮤니티 마스터의 승인 후 가입됩니다."/>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>


	</tbody>
	</table>
	
</div>

<%-- <div class="entryarea">
	<dl>
		<dd class="etr_tit"><u:msg titleId="cols.cmNm" alt="커큐니티명" /></dd>
		<dd class="etr_blank"></dd>
		<dd class="etr_input">${joinCtEstbBVo.ctNm}</dd>
	</dl>
	<dl>
		<dd class="etr_tit"><u:msg titleId="cols.mast" alt="마스터" /></dd>
		<dd class="etr_blank"></dd>
		<dd class="etr_input">${joinCtEstbBVo.mastNm}</dd>
	</dl>
	<dl>
		<dd class="etr_tit"><u:msg titleId="cols.joinMet" alt="가입방법" /></dd>
		<dd class="etr_blank"></dd>
		<dd class="etr_input">
		<c:choose>
			<c:when test="${joinCtEstbBVo.joinMet == '1'}">
				<u:msg titleId="ct.jsp.setCmJoin.joinMet02" alt="즉시 가입됩니다." />
			</c:when>
			<c:when test="${joinCtEstbBVo.joinMet == '2'}">
				<u:msg titleId="ct.jsp.setCmJoin.joinMet01" alt="커뮤니티 마스터의 승인 후 가입됩니다."/>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		</dd>
	</dl>
</div> --%>

		<div class="blank20"></div>
        <div class="btnarea">
            <div class="size">
            <dl>
            <dd class="btn" onclick="fnConfirm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
            <dd class="btn" onclick="$m.dialog.close('setCmJoin')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>
