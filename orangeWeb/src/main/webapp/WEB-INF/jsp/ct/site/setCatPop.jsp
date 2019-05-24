<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:msg titleId="ct.msg.noSelectCat" alt="카테고리를 선택해주시기 바랍니다." var="catSelect" />
<u:msg titleId="ct.msg.notInsertCatNm" alt="이름 수정란에 카테고리명을 입력해주시기 바랍니다." var="modCatNm" />
<script type="text/javascript">
<!--
// 카테고리 추가 시 유일값 생성
var serialNo = 0;
var catArr = [];
var delCatArr = [];
var $catDiv = $("#catDiv");

<%// [버튼] 카테고리 저장 %>
function saveCat(){
	var insertValArr = []; //언어별 코드의 input에 값이 입력되면 배열로 담는다.
	var $insertDiv = $("#insertDiv"); //[추가]버튼 클릭 시 값을 담는 DIV
	
	var inputValCount = 0; //입력된 input개수
	var $langTypLength = $("#langTypArea_Add").find("input[name^='rescVa_']").length; //입력해야하는 input개수
	
	//사용자 언어
	var langTypCd = $("#langTypCd").val();
	$("#langTypArea_Add").find("input[name^='rescVa_']").each(function(){
		if($(this).val() == "" || $(this).val() == null){
			alert($(this).attr("title")+"<u:msg titleId="ct.msg.site.catReq" alt="바랍니다."/>");
			return false;
		}else{
			inputValCount++;
			insertValArr.push('<span id="'+$(this).attr("id")+'"><input id="cat'+serialNo+'" name="cat'+serialNo+'" type="hidden" value="'+$(this).val()+'"/></span>');
			
			if(inputValCount == $langTypLength){
				//insertDiv에 html 삽입
				$insertDiv.html(insertValArr);
				//사용자 언어코드에 따른 입력값
				var langTypVal=$("#insertDiv").find("#"+langTypCd).find("input").val();
				//리스트에 추가
				$("#catSlt").append('<option value="cat'+serialNo+'">'+langTypVal+'</option>');
				//insertDiv 초기화
				$insertDiv.html("");
				//catArr에 값 추가 및 카테고리 입력란 초기화
				$("#langTypArea_Add").find("input[name^='rescVa_']").each(function(){
					catArr.push('<input id="'+$(this).attr("id")+'" name="cat'+serialNo+'" type="hidden" value="'+$(this).val()+'"/>');
					$(this).val("");
				});
				$catDiv.html(catArr);
				//언어타입코드 SelectBox 초기화
				$("#langTypeCd_Add").find("option[value="+langTypCd+"]").attr("selected","selected");
				$("#langTypeCd_Add").find("#langSelector").trigger("click");
				
			}
			
		}
	});
	serialNo++;
	
}

<%// [버튼] 카테고리 삭제 %>
function delSiteCat(){
	var $selectedCat = $("#catSlt").val();
	if($selectedCat == null || $selectedCat == ""){
		alert('${catSelect}');
	}else{
		$("#catSlt").find("option").each(function(){
			if($(this).val() == $selectedCat){
				$(this).remove();
			}
		});
		$("#catDiv").find("input[name="+$selectedCat+"]").each(function(){
			$(this).remove();
		});
		//삭제된 카테고리ID
		delCatArr.push('<input id="'+$selectedCat+'" name="'+$selectedCat+'" type="hidden"/>');
	}
}

<%// [이벤트] 카테고리 목록 클릭 시 해당 카테고리 정보 '이름 수정'란으로 보내기 %>
function sendCatInfo(){
	var $selectedCat = $("#catSlt").val();
	
	$("#langTypArea_Mod").find("input[name^='modRescVa_']").each(function(){
		var modValue; //수정input 변수
		var langType = $(this).attr("id");
		$("#catDiv").find("input[name="+$selectedCat+"]").each(function(){
			if($(this).attr("id") == langType){
				modValue = $(this).val();
			}
		});
		$(this).val(modValue);
	});
}

<%// [버튼] 카테고리 수정 %>
function modCat(){
	var $selectedCat = $("#catSlt").val();
	var insertValArr = []; //언어별 코드의 input에 값이 입력되면 배열로 담는다.
	var $insertDiv = $("#insertDiv"); //[수정]버튼 클릭 시 값을 담는 DIV
	var inputValCount = 0; //입력된 input개수
	var $langTypLength = $("#langTypArea_Mod").find("input[name^='modRescVa_']").length; //입력해야하는 input개수
	//사용자 언어
	var langTypCd = $("#langTypCd").val();
	
	
	if($selectedCat == null || $selectedCat == ""){
		alert('${catSelect}');
	}else{
		$("#langTypArea_Mod").find("input[name^='modRescVa_']").each(function(){
			
			if($(this).val() == "" || $(this).val() == null){
				alert($(this).attr("title")+"<u:msg titleId="ct.msg.site.catReq" alt="바랍니다."/>");
				return false;
			}else{
				inputValCount++;
				insertValArr.push('<span id="'+$(this).attr("id")+'"><input id="cat'+serialNo+'" name="cat'+serialNo+'" type="hidden" value="'+$(this).val()+'"/></span>');
				if(inputValCount == $langTypLength){
					//insertDiv에 html 삽입
					$insertDiv.html(insertValArr);
					//사용자 언어코드에 따른 입력값
					var langTypVal=$("#insertDiv").find("#"+langTypCd).find("input").val();
					
					//catArr에 값 추가 및 카테고리 입력란 초기화
					$("#langTypArea_Mod").find("input[name^='modRescVa_']").each(function(){
						var modValue = $(this).val(); //수정input 변수
						var langType = $(this).attr("id");
						$("#catDiv").find("input[name="+$selectedCat+"]").each(function(){
							if($(this).attr("id") == langType){
								$(this).val(modValue);
							}
						});
						$("#catSlt").find("option").each(function(){
							if($(this).val() == $selectedCat){
								$(this).text(langTypVal);
								
							}
						});
						//insertDiv 초기화
						$insertDiv.html("");
						$(this).val("");
						//언어타입코드 SelectBox 초기화
						$("#langTypeCd_Mod").find("option[value="+langTypCd+"]").attr("selected","selected");
						$("#langTypeCd_Mod").find("#langSelector").trigger("click");
					});
				}
			}
		});
		
	}
	
}

<%// [버튼] 카테고리 저장 %>
function catSave(){
	var sendCatArr = [];
	var $sendCatDiv = $("#sendCatDiv");
	var $delCatDiv =$("#delCatDiv");
	if(true/*confirmMsg("cm.cfrm.save")*/ ){
		$delCatDiv.html(delCatArr);
		for(var i=0; i<serialNo; i++){
			$("#catDiv").find("input[name^=cat"+i+"]").each(function(){
				var delCat = $delCatDiv.find($(this).attr("name")).length;
				if(delCat == 0){
					sendCatArr.push('<input id="'+$(this).attr("name")+$(this).attr("id")+'" name="'+$(this).attr("name")+$(this).attr("id")+'" value="'+$(this).val()+'" type="hidden"/>');
				}
			});
		}
		$sendCatDiv.html(sendCatArr);
		
		$form = $("#setCatForm");
		$form.attr("method", "POST");
		$form.attr("action", "./transCatSave.do?menuId=${menuId}&ctId=${ctId}&serialNo="+serialNo);
		$form.submit();
	}
	
}

$(document).ready(function() {
	setUniformCSS();
	
	//사용자 언어
	var langTypCd = $("#langTypCd").val();
	
	//사용자 언어코드에 따른 입력값 SelectBox Append
	$("#storedCatListDiv").find("input").each(function(){
		if($(this).attr("id") == langTypCd){
			//리스트에 추가
			$("#catSlt").append('<option value="'+$(this).attr("name")+'">'+$(this).val()+'</option>');
		}
	});
	
	//해당 커뮤니티의 CT_SITE_CAT_D에 담겨있는 카테고리 리스트 Array에 담기
	$("#storedCatListDiv").find("input[name^='cat']").each(function(){
		catArr.push('<input id="'+$(this).attr("id")+'" name="'+$(this).attr("name")+'" type="hidden" value="'+$(this).val()+'"/>');
	});
	
	$("#catDiv").html(catArr);
	
	//추가된 카테고리 리스트 수 만큼 시리얼넘버를 증가
	for(var i=0; i< '${fn:length(siteCatList)}';i++){
		serialNo++;
	}
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />

<div style="width:400px">
<form id="setCatForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="catId" value="${ctCtFldBVo.catId}" />
<u:input type="hidden" id="langTypCd" value="${langTypCd}" />

<!--  저장 시 form으로 보낼 값-->
<div id="sendCatDiv">
</div>

<!--  저장 전 삭제할 catId-->
<div id="delCatDiv">
</div>


<!-- 모든 카테고리 값을 담는 DIV 
	 정상적으로 추가가 될 때마다 값을 담으며, 최종적으로 [저장]버튼 클릭 시 필요-->
<div id="catDiv">
</div>


<!-- [추가]버튼 클릭 시 값을 담는 DIV -->
<div id="insertDiv">
</div>

<!-- 해당 커뮤니티의 CT_SITE_CAT_D에 담겨있는 카테고리 리스트를 담는 DIV -->
<div id="storedCatListDiv">
	<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
			<c:forEach var="ctSiteRescVo" items="${ctSiteRescList}" varStatus="stat">
				<c:if test="${siteCatVo.catNmRescId == ctSiteRescVo.rescId}">
					<input id="${ctSiteRescVo.langTypCd}" name="cat${status.index}" value="${ctSiteRescVo.rescVa}" type="hidden"/>
				</c:if>
			</c:forEach>
	</c:forEach>
</div>

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="95" class="head_lt"><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td id="catSubjRescArea">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea_Add">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<c:set var="rescId" value="rescVa_" />
							<c:set var="rescNm" value="rescVa_" />
							<u:input id="${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.catNm" 
								value="${rescVa}" style="${style}" maxByte="200"/>
							<input id="langTypCnt" name="langTypCnt" value="${langTypCdVo.cd}" type="hidden"/>
						</c:forEach>
					</td>
					<td id="langTypeCd_Add">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('catSubjRescArea','langTypArea_Add',this.value)" <u:elemTitle titleId="cols.langTyp" />>
								<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
									<option value="${langTypCdVo.cd}">${langTypCdVo.rescNm}</option>
								</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId" value="${ctSiteCatDVo.catNmRescId}" />
					</td>
				</tr>
			</table>
		</td>
<%-- 	<td width="40"><u:buttonS titleId="cm.btn.add" alt="추가" onclick="javascript:saveCat('reg')" /></td> --%>
	<td width="40"><u:buttonS titleId="cm.btn.add" alt="추가" onclick="javascript:saveCat();" /></td>
	</tr>
	
		
	<tr>
	<td colspan="2"><select id="catSlt" name="catSlt" size="5" style="width: 350px;" onclick="javascript:sendCatInfo();">
		</select></td>
	<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="javascript:delSiteCat()" /></td>
	</tr>
	<tr>
		<td width="95" class="head_lt"><u:msg titleId="ct.cols.nmMod" alt="이름수정" /></td>
		<td id="catSubjRescModArea">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td id="langTypArea_Mod">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<c:set var="rescId" value="modRescVa_" />
							<c:set var="rescNm" value="modRescVa_" />
							<u:input id="${langTypCdVo.cd}" name="${rescNm}${langTypCdVo.cd}"  titlePrefix="${titlePrefix}" titleId="cols.catNm" 
								value="${rescVa}" style="${style}" maxByte="200"/>
						</c:forEach>
					</td>
					<td id="langTypeCd_Mod">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('catSubjRescModArea','langTypArea_Mod',this.value)" <u:elemTitle titleId="cols.langTyp" />>
								<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
									<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
								</c:forEach>
							</select>
						</c:if>
						<u:input type="hidden" id="rescId" value="${ctSiteCatDVo.catNmRescId}" />
					</td>
				</tr>
			</table>
		</td>
<%-- 		<td><u:buttonS titleId="cm.btn.mod" alt="수정" onclick="javascript:saveCat('mod');" /></td> --%>
		<td><u:buttonS titleId="cm.btn.mod" alt="수정" onclick="javascript:modCat();" /></td>
		</tr>
	
</u:listArea>
		
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:catSave();" alt="저장"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
