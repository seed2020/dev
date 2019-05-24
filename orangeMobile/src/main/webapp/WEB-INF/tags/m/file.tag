<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.HashSet"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%@ attribute name="id"			required="true" rtexprvalue="true"
%><%@ attribute name="module"		required="true"
%><%@ attribute name="mode"			required="true"
%>
<!--attachzone S-->
<div class="attachzone" id="${id}Area">
<div class="attacharea">

	<div class="attachin filearea tmp" style="display:none">
	<div class="attach" >
		<div class="btn"></div>
		<div class="txt"><span id="${id}_fileView"></span></div>
	</div>
	<div class="delete" onclick="javascript:delFileInfo(this,'${id}');"></div>
	</div>

</div>
</div>
<!--//attachzone E-->