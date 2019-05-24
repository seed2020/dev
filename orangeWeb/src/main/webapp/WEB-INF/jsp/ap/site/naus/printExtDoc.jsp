<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.List, java.util.ArrayList, com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)request.getAttribute("apOngdRecvDeptLVoList");

	List<ApOngdRecvDeptLVo> recvList = new ArrayList<ApOngdRecvDeptLVo>();
	List<ApOngdRecvDeptLVo> refList = new ArrayList<ApOngdRecvDeptLVo>();
	int i, size = apOngdRecvDeptLVoList==null ? 0 : apOngdRecvDeptLVoList.size();
	ApOngdRecvDeptLVo apOngdRecvDeptLVo;
	for(i=0; i<size; i++){
		apOngdRecvDeptLVo = apOngdRecvDeptLVoList.get(i);
		if("Y".equals(apOngdRecvDeptLVo.getAddSendYn())) continue;
		
		if(apOngdRecvDeptLVo.getRecvDeptNm()==null || apOngdRecvDeptLVo.getRecvDeptNm().isEmpty()){
			continue;
		}
		recvList.add(apOngdRecvDeptLVo);
		if(apOngdRecvDeptLVo.getRefDeptNm()==null || apOngdRecvDeptLVo.getRefDeptNm().isEmpty()){
			continue;
		}
		refList.add(apOngdRecvDeptLVo);
	}
	request.setAttribute("recvList", recvList);
	request.setAttribute("refList", refList);

%>

<div>
<%-- 로고 이미지 --%>
<img alt="logo" src="/images/site/NAUS_LOG.jpg" style="width:230px; filter: alpha(opacity=40); opacity: 0.4;" />
<div style="width:100%; height:1px;"></div>
<%-- 상단 회사명 --%>
<div style="width:100%; padding:6px; 0 6px 0; text-align:center; font-size:3em">주식회사 나우스</div>
<%-- 수신/참조/제목 --%>
<div style="width:100%; font-size:1.3em">
<div style="padding-top:12px"><span style=""><strong>수신 :</strong></span><span style="padding-left:10px"><c:if

	test="${fn:length(recvList) == 1}"><u:out value="${recvList[0].recvDeptNm}" /></c:if><c:if

	test="${fn:length(recvList) > 1}"><c:forEach
			items="${recvList}" var="apOngdRecvDeptLVo" varStatus="recvDeptStatus"><c:if
				test="${not recvDeptStatus.first}"
				>, </c:if><nobr><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /></nobr></c:forEach></c:if></span></div>
<div style="padding-top:12px"><span style=""><strong>참조 :</strong></span><span style="padding-left:10px"><c:if

	test="${fn:length(recvList) == 1}"><u:out value="${refList[0].refDeptNm}" /></c:if><c:if

	test="${fn:length(recvList) > 1}"><c:forEach
			items="${refList}" var="apOngdRecvDeptLVo" varStatus="recvDeptStatus"><c:if
				test="${not recvDeptStatus.first}"
				>, </c:if><nobr><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" />(<u:out value="${apOngdRecvDeptLVo.refDeptNm}" />)</nobr></c:forEach></c:if></span></div>
<div style="padding-top:12px"><span style=""><strong>제목 :</strong></span><span style="padding-left:10px"><u:out value="${apvData.docSubj}" /></span></div>
<div style="width:100%; height:12px;"></div>
<%-- 회색 가로바 --%>
<div style="width:100%; height:6px; background-color:#A7A5A5"></div>
</div>

<%-- 본문 --%>
<div style="width:100%; height:12px;"></div>
<div style="width:100%; font-size:1.2em">
<div style="padding-top:12px">1. 귀사의 협조와 노고에 감사드립니다.</div>
<div style="padding-top:12px">2. 내용</div>
<div class="editor" style="min-height:480px">${apvData.bodyHtml}</div>
</div>

<%-- 발신명의 / 직인 --%>
<div style="width:100%; height:12px;"></div>
<div style="position:relative;">
<div style="width:100%; padding:6px; 0 6px 0; text-align:center; font-size:3em">주식회사 나우스</div>
<div style="width:100%; padding:6px; 0 6px 0; text-align:center; font-size:3em"><c:if
		test="${empty apvData.sendrNmRescNm}"><u:out value="${docSender.txtCont}" /></c:if><c:if
		test="${not empty apvData.sendrNmRescNm}"><u:out value="${apvData.sendrNmRescNm}" /></c:if></div>
	<div id="stampArea" style="text-align:center; position:relative; float:left; left:50%; width:115px; margin-left:${empty apvData.ofseHghtPx or apvData.ofseHghtPx=='80'
		 ? 120 : 120}px; margin-top:${empty apvData.ofseHghtPx or apvData.ofseHghtPx=='80' ? '-80' : -40}px; z-index:1;"><c:if
				test="${not empty apvData.ofsePath}"><img height="${apvData.ofseHghtPx}px" src="${_cxPth}${apvData.ofsePath}" /></c:if></div>
<%-- 회색 가로바 --%>
<div style="width:100%; height:6px; background-color:#A7A5A5"></div>
</div>


<div style="width:100%; height:30px;"></div>
<%-- 담당자 / 결재선 --%>
<div style="width:100%; font-size:1.2em">
<div style="width:100%;"><c:forEach items="${rootApOngdApvLnDVoList}" var="apOngdApvLnDVo" varStatus="status"><c:if
	test="${status.index==0}"><span>담당자 : </span></c:if><c:if
	test="${status.index==1}"><span style="padding-left:30px;">승인 : </span></c:if><c:if
	test="${status.index>1}">, </c:if>${apOngdApvLnDVo.apvrNm} ${apOngdApvLnDVo.apvrPositNm}
</c:forEach></div>
<%-- 기안일 / 발송일 --%>
<div style="width:100%; padding-top:8px; color:red">
<span>기안 : </span><span><u:out value="${apvData.makDt}" type="date" /></span><span style="padding-left:30px;">발송 : </span><span><u:out value="${apvData.enfcDt}" type="date" /></span>
</div>
<%-- 발송부서 / 담당자 / 전화 --%>
<div style="width:100%; padding-top:8px; color:red">
<span>발송부서 : </span><span><u:out value="${sessionScope.userVo.deptNm}" /></span><span style="padding-left:30px;">담당자 : </span><span><u:out value="${sessionScope.userVo.userNm}" /> <u:out value="${orUserBVo.positNm}" /></span><span style="padding-left:30px;">전화 : </span><span><u:out value="${orOrgCntcDVo.phon}" /></span>
</div>
<%-- footer / 회사주소,전화,펙스,홈페이지 --%>
<c:if test="${not empty docFooter.txtCont}">
<div style="width:100%; padding-top:8px;">
<u:out value="${empty apvData.footerVa ? docFooter.txtCont : apvData.footerVa}" />
</div>
</c:if>
</div>

</div>

