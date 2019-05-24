<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">

// 1 부서 선택
function openSingOrg(){
	var $view = $("#sample01");
	var data = {orgId:$view.find("#orgId").val()};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$view.find("#orgId").val(orgVo.orgId);
			$view.find("#rescNm").val(orgVo.rescNm);
			$view.find("#rescId").val(orgVo.rescId);
		}
		//return false;// 창이 안닫힘
	});
}
// 1 부서 선택 - 하위조직 포함여부
function openSingOrgWithSub(){
	var $view = $("#sample02");
	var data = {orgId:$view.find("#orgId").val(), withSub:$view.find("#withSub").val()};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, withSub:true}, function(orgVo){
		if(orgVo!=null){
			$view.find("#orgId").val(orgVo.orgId);
			$view.find("#rescNm").val(orgVo.rescNm);
			$view.find("#rescId").val(orgVo.rescId);
			$view.find("#withSub").val(orgVo.withSub);
		}
		//return false;// 창이 안닫힘
	});
}
// 다중 부서 선택
function openMuiltiOrg(mode){
	var $view = $("#sample03"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$view.find("#orgId").each(function(){
		data.push({orgId:$(this).val()});
	});
	<%// option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input id='orgId' style='width:80px' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input id='rescNm' style='width:80px' value='"+orgVo.rescNm+"' />\n");
				buffer.push("<input id='rescId' style='width:80px' value='"+orgVo.rescId+"' />\n");
				buffer.push("<br/>\n");
			});
			$view.html(buffer.join(''));
			setUniformCSS($view[0]);
		} else {
			alert('하나 이상 부서 선택 ~~~');
			return false;// 창이 안닫힘
		}
	});
}
//다중 부서 선택 - 하위부서 여부 포함
function openMuiltiOrgWithSub(mode){
	var $view = $("#sample04"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $subs = $view.find("#withSub");
	$view.find("#orgId").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	<%// option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input id='orgId' style='width:80px' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input id='rescNm' style='width:80px' value='"+orgVo.rescNm+"' />\n");
				buffer.push("<input id='rescId' style='width:80px' value='"+orgVo.rescId+"' />\n");
				buffer.push("<input id='withSub' style='width:80px' value='"+orgVo.withSub+"' />\n");
				buffer.push("<br/>\n");
			});
			$view.html(buffer.join(''));
			setUniformCSS($view[0]);
		}
		//return false;// 창이 안닫힘
	});
}

//1명의 사용자 선택
function openSingUser(){
	var $view = $("#sample11");
	var data = {userUid:$view.find("#userUid").val()};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#userUid").val(userVo.userUid);
			$view.find("#rescNm").val(userVo.rescNm);
			$view.find("#rescId").val(userVo.rescId);
			alert(new ParamMap(userVo));
		}
	});
	
}
//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#sample12"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$view.find("#userUid").each(function(){
		data.push({userUid:$(this).val()});
	});
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input id='userUid' style='width:80px' value='"+userVo.userUid+"' />\n");
				buffer.push("<input id='rescNm' style='width:80px' value='"+userVo.rescNm+"' />\n");
				buffer.push("<input id='rescId' style='width:80px' value='"+userVo.rescId+"' />\n");
				buffer.push("<br/>\n");
			});
			$view.html(buffer.join(''));
			setUniformCSS($view[0]);
		}
	});
}

// 하위부서 선택
function openDownOrg(){
	var $view = $("#sample21");
	var startId = $view.find("#startId").val();
	if(startId==null){
		alert('기준부서 없음');
		return;
	}
	var data = {orgId:$view.find("#orgId").val()};
	searchOrgPop({data:data, downward:startId }, function(orgVo){<%// downward - 하위부서 시작 조직ID %>
		if(orgVo!=null){
			$view.find("#orgId").val(orgVo.orgId);
			$view.find("#rescNm").val(orgVo.rescNm);
			$view.find("#rescId").val(orgVo.rescId);
		}
	});
}
// 상위부서 선택
function openUpOrg(){
	var $view = $("#sample22");
	var startId = $view.find("#startId").val();
	if(startId==null){
		alert('기준부서 없음');
		return;
	}
	var data = {orgId:$view.find("#orgId").val()};
	searchOrgPop({data:data, upward:startId}, function(orgVo){<%// upward - 상위부서 시작 조직ID %>
		if(orgVo!=null){
			$view.find("#orgId").val(orgVo.orgId);
			$view.find("#rescNm").val(orgVo.rescNm);
			$view.find("#rescId").val(orgVo.rescId);
		}
	});
}


$(document).ready(function() {
	setUniformCSS();
});
</script>

</head>
<body>


<br/><br/><br/>
<a href="javascript:viewUserPop('U0000003')">[사용자 상세 조회]</a><br/>
<br/><br/>


[부서 선택]<br/>
<table border="1">
	<tr>
		<td width="300"><a href="javascript:openSingOrg()">1 부서 선택</a></td>
		<td width="400" id="sample01">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td width="300"><a href="javascript:openSingOrgWithSub()">1 부서 선택 - 하위조직 포함여부</a></td>
		<td width="400" id="sample02">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
			<input id="withSub" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td width="300">
			<a href="javascript:openMuiltiOrg()">여러 부서 선택 ~ 없으면 창안닫힘 예제</a><br/><br/>
			<a href="javascript:openMuiltiOrg('view')">선택된 부서 보기</a>
		</td>
		<td width="400" id="sample03">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td width="300">
			<a href="javascript:openMuiltiOrgWithSub()">여러 부서 선택 - 하위조직 포함여부</a><br/><br/>
			<a href="javascript:openMuiltiOrgWithSub('view')">선택된 부서 보기</a>
		</td>
		<td width="400" id="sample04">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
			<input id="withSub" style="width:80px" />
		</td>
	</tr>
</table>
<br/><br/>

[사용자 선택]<br/>
<table border="1">
	<tr>
		<td width="300"><a href="javascript:openSingUser()">1 사용자 선택</a></td>
		<td width="400" id="sample11">
			<input id="userUid" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
	<tr>
		<td width="300">
			<a href="javascript:openMuiltiUser()">여러 사용자 선택</a><br/><br/>
			<a href="javascript:openMuiltiUser('view')">선택된 사용자 보기</a>
		</td>
		<td width="400" id="sample12">
			<input id="userUid" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
</table><br/>
<br/><br/>

[결재용]<br/>
<table border="1">
	<tr id="sample21">
		<td width="300"><a href="javascript:openDownOrg()">하위 부서 선택</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 기준 부서 : <input id="startId" value="O0000004" style="width:80px" /></td>
		<td width="400">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
	<tr id="sample22">
		<td width="300">
			<a href="javascript:openUpOrg()">상위 부서 선택</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 기준 부서 : <input id="startId" value="O0000007" style="width:80px" /><br/>
		</td>
		<td width="400">
			<input id="orgId" style="width:80px" />
			<input id="rescNm" style="width:80px" />
			<input id="rescId" style="width:80px" />
		</td>
	</tr>
</table><br/>