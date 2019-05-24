<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ attribute name="id" required="false"
%><%@ attribute name="mode" required="true"
%><%@ attribute name="snsParams" required="false"
%><%@ attribute name="urlPrefix" required="false"
%><%@ attribute name="text" required="false"
%>
<%
if(urlPrefix==null) urlPrefix=".";
%>
<c:if test="${mode eq 'set'}">
<u:checkArea>
	<c:forEach var="snsVo" items="${snsList }" varStatus="status">
	<u:msg var="snsTitle" titleId="bb.sns.${snsVo.atrbId }"/>
	<u:checkbox name="${snsVo.atrbId }" value="Y" title="${snsTitle }" alt="${snsTitle }" checkValue="${snsVo.useYn }" inputClass="bodybg_lt"/>
	</c:forEach>
</u:checkArea>
</c:if>
<c:if test="${mode eq 'view'}">
<div id="${id }SnsContainer" style="float:left;">
<ul style="margin:0; padding:0; list-style:none;"></ul>
</div>
<script type="text/javascript">
$(document).ready(function() {
	callAjax('<%=urlPrefix%>/getSnsInfoAjx.do?menuId=${menuId}${snsParams}', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.isSns != null && data.isSns=='true'){
			if(data.snsList.length>0){
				var snsList=data.snsList;
				$buffer=[];
				$.each(snsList, function(index, vo){
					vo=vo.map;
					$title=callMsg('bb.sns.'+vo.atrbId);
					$buffer.append('<li style="float:left;padding:2px;" title="'+$title+'"><a href="javascript:;" id="'+vo.atrbId+'Lnk" title="'+$title+'" data-sns="'+vo.atrbId+'">');
					$buffer.append('<img src="${_cxPth}/images/icon/'+vo.atrbId+'.png" alt="'+$title+'" style="max-width:25px"/></a></li>');
				});
				if($buffer.length>0){
					$('#${id }SnsContainer').find('ul').eq(0).html($buffer.join(''));
					$('#${id }SnsContainer').find('ul > li a').on('click', function(ev){
						snsUpload($(this).attr('data-sns'), data.url, '${text}'=='' ? '' : '${text}');
					});
				}
			}
		}
	});
});
</script>
</c:if>