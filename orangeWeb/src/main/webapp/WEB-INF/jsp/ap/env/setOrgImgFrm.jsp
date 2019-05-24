<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gOrgId = null;
var gSeq = null;
<%// 이미지 상세 조회 %>
function viewOrgImg(orgId, seq){
	callAjax('./getOrgImgDetlAjx.do?menuId=${menuId}', {orgId:orgId, seq:seq}, function(data){
		var vo = data.vo;
		var $imgDetlArea = $("#imgDetlArea");
		if(vo != null){
			gOrgId = orgId;
			gSeq = seq;
			
			//var imgHght = Math.min(parseInt(vo.get("imgHght"), 10), 170);
			var imgHght = 80;
			var $stamp = $imgDetlArea.find("#stampImg");
			$stamp.attr("src", "${_ctx}"+vo.get("imgPath"));
			$stamp.attr("height", imgHght+"px");
			$stamp.css("margin-top", (Math.ceil((130 - imgHght)/2)+10)+"px");
			$stamp.show();
			
			$imgDetlArea.find("#imgSize").text(vo.get("imgWdth") + "(px) x " + vo.get("imgHght")+"(px)");
			$imgDetlArea.find("#regDt").text(vo.get("regDt"));
			$imgDetlArea.find("#disuDt").text(vo.get("disuDt"));
			$imgDetlArea.find("#chnRson").html(escapeHtml(vo.get("chnRson")));
			
			if(vo.get("disuYn")=="Y"){<%//폐기면 %>
				$imgDetlArea.find("#disuMark").show();<%// - 붉은색 폐기 마크 %>
				$imgDetlArea.find("#disuseImgBtn").hide();<%// 폐기 버튼 %>
				$imgDetlArea.find("#cancelDisuBtn").show();<%// 폐기 취소 버튼 %>
				$imgDetlArea.find("#modifyImgBtn").hide();<%// 수정 버튼 %>
				$imgDetlArea.find("#setDftImgBtn").hide();<%// 기본 이미지로 설정 버튼 %>
			} else {
				$imgDetlArea.find("#disuMark").hide();<%// 왼쪽 상단 - 붉은색 폐기 마크 %>
				$imgDetlArea.find("#disuseImgBtn").show();<%// 폐기 버튼 %>
				$imgDetlArea.find("#cancelDisuBtn").hide();<%// 폐기 취소 버튼 %>
				$imgDetlArea.find("#modifyImgBtn").show();<%// 수정 버튼 %>
				if(vo.get("dftOfseYn")=="Y"){<%// - 기본 이미지 이면 %>
					$imgDetlArea.find("#setDftImgBtn").hide();<%// 기본 이미지로 설정 버튼 %>
				} else {
					$imgDetlArea.find("#setDftImgBtn").show();<%// 기본 이미지로 설정 버튼 %>
				}
			}
		} else {
			$imgDetlArea.find("#disuMark").hide();
			$imgDetlArea.find("#stampImg").hide();
			$imgDetlArea.find("#imgSize").text("");
			$imgDetlArea.find("#chnRson").text("");
			
			$imgDetlArea.find("#modifyImgBtn").hide();
			$imgDetlArea.find("#disuseImgBtn").hide();
			$imgDetlArea.find("#cancelDisuBtn").hide();
			$imgDetlArea.find("#setDftImgBtn").hide();
			gOrgId = null;
			gSeq = null;
		}
		<%// 선택 하일라이트 %>
		$("#orOfseDVoListArea td").each(function(){
			if($(this).attr('data-seq')==seq) $(this).addClass("text_on");
			else if($(this).hasClass("text_on")) $(this).removeClass("text_on");
		});
	});
}
<%// 수정 - 소버튼 - 이미지 변경 %>
function modifyImg(){
	if(gOrgId != null && gSeq != null){
		parent.dialog.open('setOrgImgDialog','<u:msg titleId="ap.jsp.setOrgEnv.modImg" alt="관인 / 서명인 수정"/>','./setOrgImgPop.do?menuId=${menuId}&orgId='+gOrgId+'&seq='+gSeq);
	}
}
<%// 폐기 - 소버튼 - 이미지 폐기 %>
function disuseImg(){
	if(gOrgId != null && gSeq != null){
		parent.dialog.open('disuseOrgImgDialog','<u:msg titleId="ap.jsp.setOrgEnv.disuImg" alt="관인 / 서명인 폐기"/>','./setOrgImgDisuPop.do?menuId=${menuId}&orgId='+gOrgId+'&seq='+gSeq);
	}
}
<%// 폐기 취소 - 소버튼 - 이미지 폐기 취소 %>
function cancelDisu(){
	<%//ap.jsp.setOrgEnv.cfm.cancelDisu=폐기된 이미지를 복원하시겠습니까 ?%>
	if(gOrgId != null && gSeq != null && confirmMsg("ap.jsp.setOrgEnv.cfm.cancelDisu")){
		callAjax('./transOrgImgCancelDisuAjx.do?menuId=${menuId}', {orgId:gOrgId, seq:gSeq}, function(data){
			if(data.message != null){
				alert(data.message);
			}
			if(data.result == 'ok') parent.setDetl('Img', '&orgId='+gOrgId+'&selectedSeq='+gSeq);
			if(data.result == 'okAdmin') parent.setDetl('Img', '&selectedSeq='+gSeq);
		});
	}
}
<%// 기본 이미지로 설정  %>
function setDftImg(){
	<%//ap.jsp.setOrgEnv.cfm.setAsDft=기본 이미지로 설정하시겠습니까 ?%>
	if(gOrgId != null && gSeq != null && confirmMsg("ap.jsp.setOrgEnv.cfm.setAsDft")){
		callAjax('./transOrgImgSetAsDftAjx.do?menuId=${menuId}', {orgId:gOrgId, seq:gSeq}, function(data){
			if(data.message != null){
				alert(data.message);
			}
			if(data.result == 'ok') parent.setDetl('Img', '&orgId='+gOrgId+'&selectedSeq='+gSeq);
			if(data.result == 'okAdmin') parent.setDetl('Img', '&selectedSeq='+gSeq);
		});
	}
}
<%// [버튼] 추가 - 부서이미지[탭] %>
function addOrgImg(){
	var data = new ParamMap().getData("searchForm").map;
	parent.dialog.open('setOrgImgDialog','<u:msg titleId="ap.jsp.setOrgEnv.addImg" alt="관인 / 서명인 추가"/>','./setOrgImgPop.do?menuId=${menuId}&orgId='+data.orgId+'&ofseTypCd='+data.ofseTypCd);
}
$(document).ready(function() {
	setUniformCSS();
	<c:if test="${not empty param.selectedSeq}">viewOrgImg('${param.orgId != null ? param.orgId : sessionScope.userVo.deptId}', '${param.selectedSeq == "last" ? fn:length(orOfseDVoList) : param.selectedSeq}');</c:if>
});
//-->
</script>

<div style="padding:10px;">
	<u:searchArea>
		<form id="searchForm" name="searchForm" action="./setOrgImgFrm.do">
		<input type="hidden" name="menuId" value="${menuId}" />
		<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="search_tit"><u:msg titleId="cols.dept" alt="부서" /></td>
			<td><select id="orgId" name="orgId"<u:elemTitle titleId="cols.dept" alt="부서" />><c:forEach
				items="${orgTreeVoList}" var="orgTreeVo" varStatus="status">
				<u:option title="${orgTreeVo.rescNm}" value="${orgTreeVo.orgId}" selected="${param.orgId == orgTreeVo.orgId or (empty param.orgId and status.last)}" /></c:forEach>
				</select></td>
			<td class="width20"></td>
			<td class="bodyip_lt"><u:msg titleId="or.cols.imgTyp" alt="이미지 구분" /></td>
			<td><select id="ofseTypCd" name="ofseTypCd"<u:elemTitle titleId="or.cols.imgTyp" alt="이미지 구분" />>
				<u:option titleId="cm.option.all" value="" alt="전체" selected="${param.ofseTypCd == ''}" /><c:forEach
				items="${ofseTypCdList}" var="ofseTypCd" varStatus="status">
				<u:option title="${ofseTypCd.rescNm}" value="${ofseTypCd.cd}" selected="${param.ofseTypCd == ofseTypCd.cd}" /></c:forEach>
				</select></td>
			<td class="width20"></td>
			<td class="bodyip_lt"><u:msg titleId="or.cols.disuYn" alt="폐기 여부" /></td>
			<td><select id="disuYn" name="disuYn"<u:elemTitle titleId="or.cols.disuYn" alt="폐기 여부" />>
				<u:option titleId="cm.option.all" value="" alt="전체" selected="${empty param.disuYn}" />
				<u:option titleId="cm.option.use" value="N" alt="사용" selected="${param.disuYn == 'N'}" />
				<u:option titleId="cm.option.disu" value="Y" alt="폐기" selected="${param.disuYn == 'Y'}" />
				</select></td>
			</tr>
			</table></td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
		</table>
		</form>
	</u:searchArea>
	
	<div style="float:left; width:59.5%; overflow-y:auto;">
	<u:listArea id="orOfseDVoListArea" tbodyClass="group_smu" >
	
		<tr>
		<td width="25%" class="head_ct"><u:msg titleId="or.cols.imgTyp" alt="이미지 구분" /></td>
		<td width="45%" class="head_ct"><u:msg titleId="or.cols.imgNm" alt="이미지 명" /></td>
		<td width="15%" class="head_ct"><u:msg titleId="cm.option.dft" alt="기본" /></td>
		<td width="15%" class="head_ct"><u:msg titleId="cm.option.disu" alt="폐기" /></td>
		</tr>
		<c:if test="${fn:length(orOfseDVoList)==0}" >
		<tr>
			<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
		</c:if>
		<c:forEach items="${orOfseDVoList}" var="orOfseDVo" varStatus="status">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_ct" data-seq="${orOfseDVo.seq}" style="padding-left:0px"><a href="javascript:viewOrgImg('${orOfseDVo.orgId}','${orOfseDVo.seq}')">${orOfseDVo.ofseTypNm}</a></td>
		<td class="body_ct" data-seq="${orOfseDVo.seq}" style="padding-left:0px"><a href="javascript:viewOrgImg('${orOfseDVo.orgId}','${orOfseDVo.seq}')"><u:out value="${orOfseDVo.rescNm}" /></a></td>
		<td class="body_ct"><c:if test="${orOfseDVo.dftOfseYn == 'Y'}">${orOfseDVo.dftOfseYn}</c:if></td>
		<td class="body_ct"><c:if test="${orOfseDVo.disuYn == 'Y'}">${orOfseDVo.disuYn}</c:if></td>
		</tr>
		</c:forEach>
		
	</u:listArea>
	</div>
	
	<div style="float:right; width:39.5%;" id="imgDetlArea">
		<div class="stamparea" style="height:153px; position:relative;">
			<div style="position:relative; border:1px; height:130px;">
			<img id="stampImg" style="display:none"/>
			</div><u:set test="${browser.ie or browser.firefox}" var="padding" value="5px 5px 3px 5px" elseValue="5px 5px 5px 5px"></u:set>
			<div id="disuMark" style="position:absolute; top:4px; left:4px; border:3px; border-style:double; border-color:red; padding:${padding}; font-size:12pt; color:red; display:none"><strong><u:msg titleId="cm.option.disu" alt="폐기" /></strong></div>
			<div style="margin-right:3px; position:relative; float:right; ">
				<c:if test="${not empty adminPage or optConfigMap.alwChgOfcSeal == 'Y'}">
				<u:buttonS id="modifyImgBtn" titleId="cm.btn.mod" alt="수정" href="javascript:modifyImg()" style="display:none" auth="A"
				/><u:buttonS id="disuseImgBtn" titleId="cm.option.disu" alt="폐기" href="javascript:disuseImg()" style="display:none" auth="A"
				/><u:buttonS id="cancelDisuBtn" titleId="cm.btn.cancelDisu" alt="폐기 취소" href="javascript:cancelDisu()" style="display:none" auth="A"
				/><u:buttonS id="setDftImgBtn" titleId="or.btn.setDftImg" alt="기본 이미지로 설정" href="javascript:setDftImg()" style="display:none" auth="A"
				/>
				</c:if>
			</div>
		</div>
	
		<div class="blank">
		</div>
	
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
			<tr>
			<td width="20%" class="head_lt"><u:msg titleId="cols.size" alt="사이즈" /></td>
			<td class="body_lt" id="imgSize"></td>
			</tr>
			
			<tr>
			<td width="20%" class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
			<td class="body_lt" id="regDt"></td>
			</tr>
			<tr>
			<td width="20%" class="head_lt"><u:msg titleId="cols.disuDt" alt="폐기일시" /></td>
			<td class="body_lt" id="disuDt"></td>
			</tr>
	
			<tr>
			<td class="head_lt"><u:msg titleId="or.cols.disuRson" alt="폐기 사유" /></td>
			<td class="body_lt"><div id="chnRson" style="width:100%; height:80px; overflow-y:auto"></div></td>
			</tr>
	
			</table>
		</div>
	</div>
</div>
