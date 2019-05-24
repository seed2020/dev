<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		java.util.ArrayList,
		java.util.List,
		com.innobiz.orange.web.pt.utils.PersonalUtil,
		com.innobiz.orange.web.cm.utils.ArrayUtil,
		com.innobiz.orange.web.cm.utils.EscapeUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties"

%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"

%><%@ attribute name="formId" required="false"
%><%@ attribute name="javascriptFunction" required="false"
%><%@ attribute name="onPageChange" required="false"
%><%@ attribute name="pageNo" required="false" type="java.lang.Integer"
%><%@ attribute name="pageRowCnt" required="false" type="java.lang.Integer"
%><%@ attribute name="recodeCount" required="false" type="java.lang.Integer"
%><%@ attribute name="excludedParams" required="false"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%@ attribute name="noTotalCount" required="false" type="java.lang.Boolean"
%><%@ attribute name="pltBlock" required="false" type="java.lang.Boolean"
%><%@ attribute name="noLeftSelect" required="false" type="java.lang.Boolean"
%><%

	//String[] pageRecCnts = { "10","20","30","40","50","60","70","80","90","100" };
	String[] pageRecCnts = { "5","10","15","20","25","30","35","40","45","50" };
	request.setAttribute("pageRecCnts", pageRecCnts);

	// configuration
	// <<,>> : first-page, last-page
	// <,> : first-page of previous block, first-page of next block

	// javascript function
	if(javascriptFunction==null || javascriptFunction.isEmpty()) javascriptFunction = "goPage";
	
	// form id
	if(formId==null || formId.isEmpty()) formId = "paginationForm";
	
	// option - view "Total"
	boolean viewTotle = noTotalCount==null || !noTotalCount;
	if(noLeftSelect == null) noLeftSelect = Boolean.FALSE;
	
	// parameter init
	String no;
	if(pageNo==null){
		no = request.getParameter("pageNo");
		if(no==null || no.isEmpty()) pageNo = 1;
		else {
			try{ pageNo = Integer.parseInt(no); }
			catch(Exception ignore){}
		}
	}
	if(pageRowCnt==null){
		pageRowCnt = PersonalUtil.getPageRowCnt(request);
	}
	request.setAttribute("pageRowCnt", pageRowCnt);
	
	if(recodeCount==null){
		recodeCount = (Integer)request.getAttribute("recodeCount");
	}
	if(recodeCount==null) recodeCount = 0;
	
	// 페이지 수
	int pageCount = (int)Math.ceil((double)recodeCount / pageRowCnt);
	
	if(Boolean.TRUE.equals(pltBlock) && pageCount == 1)
		return;
	
	if(pageNo<0) pageNo = 1;
	if(pageNo>pageCount) pageNo = pageCount;
	
	// 블럭 카운트
	Integer nBlock = 10;
	if(Boolean.TRUE.equals(pltBlock)) nBlock = 5;
	int blockCount = (int)Math.ceil((double)pageCount / nBlock);
	int blockNo = (int)Math.ceil((double)pageNo / nBlock);
	
	String[] naviScript = new String[4];
	if(pageNo==1){
		naviScript[0] = "void(0);";
	} else {
		naviScript[0] = javascriptFunction+"(1);";
	}
	if(blockNo>1){
		naviScript[1] = javascriptFunction+"("+((blockNo-2) * nBlock + 1)+");";
	} else {
		naviScript[1] = "void(0);";
	}
	
	if(blockNo<blockCount){
		naviScript[2] = javascriptFunction+"("+(blockNo * nBlock + 1)+");";
	} else {
		naviScript[2] = "void(0);";
	}
	if(pageNo==pageCount){
		naviScript[3] = "void(0);";
	} else {
		naviScript[3] = javascriptFunction+"("+pageCount+");";
	}
	
	List<String> excludedParamList = ArrayUtil.toList(excludedParams, ",", false);
	excludedParamList.add("pageNo");
	excludedParamList.add("extValues");
	excludedParamList.add("pageRowCnt");
	
	MessageProperties messageProperties = MessageProperties.getInstance();
	
%>
<script type="text/javascript">
<!--
	function <%=javascriptFunction%>(pageNo){
		var formId = '<%=formId%>';
		var form = document.getElementById(formId);
		if(pageNo!=null) form.pageNo.value = pageNo;<%
		if(onPageChange!=null && !onPageChange.isEmpty()){
			%><%="\r\n"%>		if(<%=onPageChange%> == false){ return; }<%
		}
		%>
		form.submit();
	}
//-->
</script>
<form method="get" action="${_uri}" id="<%=formId%>" name="<%=formId%>">
	<input type="hidden" name="pageNo" value="<%=pageNo%>" />
	<input type="hidden" name="extValues" value="${param.extValues}" /><%
	
	java.util.Enumeration reqParams = request.getParameterNames();
	while (reqParams.hasMoreElements()) {
		String key = (String)reqParams.nextElement();
		if(excludedParamList.contains(key)) continue;
		String value = (String)request.getParameter(key);
		
		%>
	<input type="hidden" name="<%= key%>" value="<%= EscapeUtil.escapeValue(value)%>" /><%
	}
	%>
	<div class="paging"><%
	
	if(recodeCount>0){
		
		// 왼쪽 - 목록 갯수 :
		if(viewTotle && !noLeftSelect){
		%>
		<div class="paging_left">
			<dl>
				<dd class="paging_left_text"><u:msg titleId="cm.page.pagePerRecord" alt="목록개수" /> : </dd>
				<dd style="float:left; margin:0 3px 0 0;"><select onchange="<%=javascriptFunction%>('1');" id="pageRowCnt" name="pageRowCnt"<u:elemTitle titleId="cm.page.pagePerRecord" type="change" /> >
				<c:forEach items="${pageRecCnts}" var="pageRecCnt" varStatus="status">
				<u:option value="${pageRecCnt}" title="${pageRecCnt}" checkValue="${pageRowCnt}" />
				</c:forEach>
				</select></dd>
				<dd style="float:left; background:url('${_cxPth}/images/${_skin}/ico_setting.png'); width:23px; height:24px; cursor:pointer;" title="<u:msg titleId='cm.btn.setup'/>" onclick="top.location.href='<u:authUrl url="/pt/psn/cnt/setPageRecCnt.do" />'" onmouseover="this.style.backgroundPosition='-23px 0px'" onmouseout="this.style.backgroundPosition='0px 0px'"></dd>
			</dl>
		</div>
		<%
		} else {
			if(request.getAttribute("pageRowCnt") != null){
				%><input type="hidden" name="pageRowCnt" value="<%=request.getAttribute("pageRowCnt")%>" /><%
			}
		}
		
		// 왼쪽(목록갯수),오른쪽(Total) 있을 때만 div.paging_center 로 감쌈
		if(viewTotle){
		%>
		<div class="paging_center"><%
		}
		%>
			<div class="paging_navi">
				<a href="javascript:<%= naviScript[0]%>" title="<%= messageProperties.getMessage("cm.page.first", request) %>" class="paging_front"></a>
				<a href="javascript:<%= naviScript[1]%>" title="<%= messageProperties.getMessage("cm.page.prevBlock", request) %>" class="paging_prev"<c:if
					test="${browser.ie && browser.ver=='7'}"> style="margin-right:3px;"</c:if>></a><%
				
			Integer currPage, startPage = (blockNo-1) * nBlock + 1;
			for(int i=0;i<nBlock;i++){
				
				currPage = startPage + i;
				if(currPage<=pageCount){
					if(pageNo==currPage){
						%>
				<strong class="cur_num"><%= currPage%></strong><%
					} else {
						%>
				<a class="num_box" href="javascript:<%= javascriptFunction%>(<%= currPage%>)" title="<%= messageProperties.getMessage("cm.page.moveTo", new String[]{currPage.toString()}, request) %>"><%= currPage%></a><%
					}
				}
			}
				%>
				<a href="javascript:<%= naviScript[2]%>" title="<%= messageProperties.getMessage("cm.page.nextBlock", request) %>" class="paging_next"></a>
				<a href="javascript:<%= naviScript[3]%>" title="<%= messageProperties.getMessage("cm.page.last", request) %>" class="paging_back"></a>
			</div><%
		
		// 왼쪽(목록갯수),오른쪽(Total) 있을 때만 div.paging_center 로 감쌈
		if(viewTotle){
		%>
		</div><%
		}
		
		// 오른쪽 - Total :
		if(viewTotle){
		%>
		<div class="paging_right">
			Total : <strong><%= recodeCount%></strong>
		</div><%
		}
	
	}//if(recodeCount>0){
		%>
	</div>
</form><%
	
if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>
