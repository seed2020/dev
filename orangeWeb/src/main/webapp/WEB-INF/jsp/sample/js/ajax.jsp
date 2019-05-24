<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%

%>
<script type="text/javascript">
<!--
function test(){
	var param = new ParamMap().put("compId","test comp id").put("rescId","test resc id");
	callAjax("/sample/js/getData.do", param, function(data){
		
		alert("map data -----\n"+data.map);
		alert("'a1' from map  -----\n"+data.map.get("a1"));
		
		alert("vo data -----\n"+data.vo);
		alert("'compId' from vo  -----\n"+data.vo.get("compId"));
		
		var list = data.list;
		for(var i=0;i<list.length;i++){
			alert("list data index["+i+"] ---\n"+list[i]+"\nget durCat from list["+i+"] : "+list[i].get("durCat"));
		}
		
	});
	//vo.setCompId("11111");
	//vo.setDurCat("startDt");
}
//-->
</script>

<strong>
SampleTagJsCtrl.java<br/>
sample/js/ajax.jsp<br/><br/>
</strong>

<br/><br/>
<strong>
[AJAX Sample]
</strong>
<br/><br/>

<a href="javascript:test()">test</a>

<br/><br/><br/>
