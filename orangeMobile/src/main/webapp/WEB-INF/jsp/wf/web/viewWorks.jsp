<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="workNo"/>
<c:if test="${param.isWeb eq 'Y' || (!empty mdCd && !empty isOnlyMd && isOnlyMd==true && isMobile==true)}">
<style type="text/css">
.titlearea {
    width: 100%;
    height: 16px;
    margin: 0 0 9px 0;
}
.titlearea .tit_left .title_s {
    float: left;
    height: 13px;
    font-weight: bold;
    color: #454545;
}
.listarea {
    float: left;
    width: 100%;
    padding: 0;
    margin: 0;
    color: #454545;
}
.listtable {
    width: 100%;
    background: #bfc8d2;
    color: #454545;
}
.listtable tr {
    background: #ffffff;
}
.listtable .head_ct {
	height: 22px;
    text-align: center;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 2px 0 2px;
}
.listtable .head_lt {
    height: 22px;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 0 0 4px;
}
.body_ct {
    height: 22px;
    color: #454545;
    text-align: center;
    line-height: 17px;
    padding: 2px 3px 0 3px;
}
.body_lt {
    height: 22px;
    color: #454545;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}

/* tab basic */
.tab_basic { width:100%; height:25px; background:url("/images/blue/btn.png") right -292px; }
*.tab_basic { width:100%; height:25px; background:url("/images/blue/btn.png") right -292px; position:relative; z-index:1; }/* FOR IE7 */
.tab_basic .tab_left { float:left; line-height:normal; height:25px; }
.tab_basic .tab_left ul { list-style:none; padding:0; margin:0; }
.tab_basic .tab_left li { display:inline; padding:0; margin:0; }
.tab_basic .tab_left .basic a { float:left; background:url("/images/blue/btn.png") no-repeat left -188px; text-decoration:none; padding:0 0 0 11px; margin:0; }
.tab_basic .tab_left .basic a span { float:left; height:15px; display:block; background:url("/images/blue/btn.png") no-repeat right -188px; color:#ffffff; padding:7px 13px 3px 0; margin:0; }   
.tab_basic .tab_left .basic a:hover span { color:#ffffff; } 
.tab_basic .tab_left .basic a:hover { background-position:0% -214px; cursor:hand; }
.tab_basic .tab_left .basic a:hover span { background-position:100% -214px; }
.tab_basic .tab_left .basic_open a { float:left; background:url("/images/blue/btn.png") no-repeat left -240px; text-decoration:none; padding:0 0 0 11px; margin:0; }
.tab_basic .tab_left .basic_open a span { float:left; height:15px; display:block; background:url("/images/blue/btn.png") no-repeat right -240px; color:#454545; padding:7px 13px 3px 0; margin:0; }
.tab_basic .tab_left .basic_open a:hover span { color:#9a9999; } 
.tab_basic .tab_left .basic_open a:hover { background-position:0% -266px; cursor:hand; }
.tab_basic .tab_left .basic_open a:hover span { background-position:100% -266px; }
</style></c:if>
<script type="text/javascript">
<!-- 
<c:if test="${param.isWeb eq 'Y' || (!empty mdCd && !empty isOnlyMd && isOnlyMd==true)}">
var NO_SCRIPT = true;
var MOBILE_VIEW = true;
//탭변경 무시
var gIgnoreTabChange = false;
// 탭버튼 클릭
function changeTab(tabId, tabNo){
	if(gIgnoreTabChange){
		gIgnoreTabChange = false;
		return;
	}
	var areaId, showAreaId=null;
	$("#"+tabId+" li").each(function(index, obj){
		areaId = $(obj).attr("data-areaId");
		if(tabNo==index){
			$(obj).attr("class","basic_open");
			if(areaId!=null) {
				$("#"+areaId).show();
				showAreaId = areaId;
			}
		} else {
			$(obj).attr("class","basic");
			if(areaId!=null && areaId != showAreaId){
				$("#"+areaId).hide();
			}
		}
	});
}
</c:if>
<% //이미지 상세보기 - 팝업 오픈 %>
function viewImagePop(id){
	if(window.NO_SCRIPT == true) return;
	var url='./viewImagePop.do?menuId=${menuId}&formNo=${param.formNo}&workNo=${param.workNo}';
	if(id!='') url+='&imgNo='+id;	
	parent.dialog.open('viewImageDialog','<u:msg titleId="st.cols.img" alt="이미지" />', url);
}<% //사용자 상세보기 - 팝업 오픈 %>
function viewUserPopOpen(uid){
	if(window.MOBILE_VIEW == true) {
		$m.user.viewUserPop(uid);
		return;
	}
	if(window.NO_SCRIPT == true) return;
	else viewUserPop(uid);
}<% // [팝업] 파일목록 조회 %>
function viewFileListPop(workNo) {
	if(window.MOBILE_VIEW == true) {
		var url = '/wf/works/viewFileListPop.do?${paramsForList }&workNo='+workNo;
		$m.dialog.open({
			id:'viewFileListDialog',
			title:'<u:msg titleId="cols.attFile" alt="첨부파일" />',
			url:url,
		});
		return;
	}
	if(window.NO_SCRIPT == true) return;
	else {
		var url = './viewFileListPop.do?${paramsForList }&workNo='+workNo;
		parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
	}
	
}
function tabHandler(){}

//-->
</script>
<u:set var="jspInclPath" test="${param.isWeb eq 'Y' || (!empty isOnlyMd && isOnlyMd==true && isMobile==true) }" value="web" elseValue="works"/>
<c:if test="${isPop==true }"><c:set var="style" value=" style=\"width:850px;height:750px;padding:10px;\""/></c:if>
<c:if test="${isFrm==true }"><c:set var="style" value=" style=\"width:100%;padding:10px;\""/></c:if>
<c:if test="${isOpen==true }"><c:set var="style" value=" style=\"min-width:1080px;padding:10px;\""/></c:if>
<c:if test="${param.isWeb eq 'Y' || (!empty isOnlyMd && isOnlyMd==true && isMobile==true) }"><c:set var="style" value=" style=\"min-width:1080px;\""/></c:if>
<div${style }>
<c:if test="${empty param.isWeb && (empty isOnlyMd || isOnlyMd==false)}"><c:if test="${empty isPop }"><u:title title="${wfFormBVo.formNm }" alt="${wfFormBVo.formNm }" menuNameFirst="true" /></c:if></c:if>
<c:set var="idPrefix" value="view"/>
<!-- 탭 -->
<jsp:include page="/WEB-INF/jsp/wf/${jspInclPath }/inclTab.jsp" flush="false">
<jsp:param value="user" name="tabPage"/>
</jsp:include>

<!-- 컴포넌트 -->
<jsp:include page="/WEB-INF/jsp/wf/${jspInclPath }/inclViewForm.jsp" flush="false" />
<!-- 모듈별 양식 화면은 버튼 제거 [해당 모듈의 기능으로 대체] -->
<c:if test="${empty param.isWeb && (empty isOnlyMd || isOnlyMd==false)}">
<c:if test="${empty isPop && empty isFrm && empty isOpen}">
<u:buttonArea>
	<c:if test="${wfWorksLVoMap.isChk ne 'N' }"><u:button titleId="cm.btn.mod" alt="수정" onclick="setWorks();" />
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delWorks();" /></c:if>
	<u:button titleId="cm.btn.list" alt="목록" href="javascript:;" onclick="listPage();"/>		
</u:buttonArea>
</c:if>
<c:if test="${empty param.isWeb && isOpen==true }">
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" href="javascript:;" onclick="window.open('about:blank','_self').close();"/>		
</u:buttonArea>
</c:if>
</c:if>
</div>
