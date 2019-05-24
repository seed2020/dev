<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="m" tagdir="/WEB-INF/tags/m"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ attribute name="id" required="false"
%><%@ attribute name="mode" required="true"
%><%@ attribute name="snsParams" required="false"
%><%@ attribute name="text" required="false"
%><%@ attribute name="module" required="false"
%>
<c:if test="${mode eq 'set'}">
<c:forEach var="snsVo" items="${snsList }" varStatus="status">
	<u:msg var="snsTitle" titleId="bb.sns.${snsVo.atrbId }"/>
	<dd><dl><m:check type="checkbox" id="${snsVo.atrbId }" name="${snsVo.atrbId }" value="Y" checked="${snsVo.useYn eq 'Y' ? true : false }" title="${snsTitle }" /></dl></dd>	
</c:forEach>
</c:if>
<c:if test="${mode eq 'view'}">
<div id="${id }SnsContainer" style="float:right;margin:5px 5px -5px 0;">
<ul style="margin:0; padding:0; list-style:none;"></ul>
</div>
<script type="text/javascript">
$(document).ready(function() {
	$m.ajax('/${module}/getSnsInfoAjx.do?menuId=${menuId}${snsParams}', null, function(data) {
		if(data.message!=null){
			$m.dialog.alert(data.message);
		}
		if(data.isSns != null && data.isSns=='true'){
			if(data.snsList.length>0){
				var snsList=data.snsList;
				$buffer=[];
				$.each(snsList, function(index, vo){
					$title=$m.msg.callMsg('bb.sns.'+vo.atrbId);
					$buffer.append('<li style="float:left;padding:1px;" title="'+$title+'" data-sns="'+vo.atrbId+'">');
					$buffer.append('<img src="${_cxPth}/images/icon/'+vo.atrbId+'.png" alt="'+$title+'" style="max-width:35px"/></li>');
				});
				if($buffer.length>0){
					$('#${id }SnsContainer').find('ul').eq(0).html($buffer.join(''));
					$('#${id }SnsContainer').find('ul > li').on('click', function(ev){
						snsUpload($(this).attr('data-sns'), data.url, '${text}'=='' ? '' : '${text}');
					});
				}
			}
		}
	});
});
</script>
</c:if>