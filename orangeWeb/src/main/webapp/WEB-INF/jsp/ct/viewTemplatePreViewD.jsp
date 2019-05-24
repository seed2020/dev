<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<style>
.itrobox { float: left; width: 99%; background: #ebf1f6; border: 1px solid #bfc8d2; padding: 5px 7px 3px 10px; }
.itrobox_tit { float: left; width: 47%; background:url("/images/blue/dot_search.png") no-repeat 0 6px; padding:3px 2px 5px 9px; }
.itrobox_body { width: 99%; line-height:18px; padding:0px 0 0px 0; }
</style>
<% // 커뮤니티 정보 %>

<c:set var="title" value="${ctEstbBVo.ctNm}" />
<div style="float: left; width: 49%; height: 198px;">
<u:title title="${title}" alt="커뮤니티명" menuNameFirst="true" />
	<div class="itrobox">
		<dl>
			<dd class="itrobox_tit"><u:msg titleId="cols.mast" alt="마스터" />: <a href="javascript:viewUserPop('${ctEstbBVo.mastUid}');">${ctEstbBVo.mastNm}</a></dd>
			<dd class="itrobox_tit"><u:msg titleId="ct.jsp.setCmCat.subtitle02" alt="분류" />: ${ctEstbBVo.catPnm}&gt; ${ctEstbBVo.catNm}</dd>
				<fmt:parseDate var="dateTempParse" value="${ctEstbBVo.ctApvdDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<dd class="itrobox_tit"><u:msg titleId="ct.cols.setup.day" alt="설립일" />: <fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></dd>
				<u:set test="${ctEstbBVo.joinMet == '1'}" var="joins" value="즉시가입" elseValue="마스터 승인 후 가입"></u:set>
			<dd class="itrobox_tit"><u:msg titleId="cols.joinMet" alt="가입방법" />: ${joins}</dd>
			<dd class="itrobox_tit"><u:msg titleId="ct.cols.statusOfMembers" alt="회원현황" />: <u:msg titleId="ct.cols.all" alt="전체" /> ${allPeople}, <u:msg titleId="ct.cols.today" alt="오늘" /> ${todayPeople}</dd>
				<u:set test="${ctEstbBVo.mngTgtYn == 'Y'}" var="mngTgt" value="Yes" elseValue="No"></u:set>
			<dd class="itrobox_tit"><u:msg titleId="ct.cols.mngTgtYn" alt="관리대상 여부" />: ${mngTgt}</dd>
		</dl>
	</div>
</div>


<% // 영역1 %>
<div style="float: right; width: 49%; height: 198px;">

<div class="titlearea">
		<div class="tit_left">
			<dl>
				<dd class="title_s">
					<u:msg titleId="ct.cols.preView" alt="미리보기" />
				</dd>
			</dl>
		</div>
	</div>
	
	<u:listArea>
		<tr>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="20%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		</tr>
	
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
			<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
			<td class="body_ct">2014-01-23</td>
		</tr>
		
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
			<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
			<td class="body_ct">2013-11-12</td>
		</tr>
		
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt">
				<u:msg titleId="ct.cols.preView" alt="미리보기" />
			</td>
			<td class="body_ct">
				<u:msg titleId="ct.cols.preView" alt="미리보기" />
			</td>
			<td class="body_ct">2013-11-10</td>
		</tr>
	</u:listArea>

</div>


<% // 커뮤니티 소개  %>
<div style="float: left; width: 49%; height: 198px;">
	<div class="titlearea">
		<div class="tit_left">
			<dl>
				<dd class="title_s"><u:msg titleId="ct.jsp.main.title01" alt="커뮤니티 소개" /></dd>
			</dl>
		</div>
	</div>
	
	<div class="itrobox">
		<dl>
			<dd class="itrobox_body"><u:msg titleId="ct.cols.preView" alt="미리보기" />.<br/>
				<u:msg titleId="ct.cols.preView" alt="미리보기" />
			</dd>
		</dl>
	</div>
</div>



<% // 영역2 %>
<div style="float: right; width: 49%; height: 198px;">

<div class="titlearea">
		<div class="tit_left">
			<dl>
				<dd class="title_s">
					<u:msg titleId="ct.cols.preView" alt="미리보기" />
				</dd>
			</dl>
		</div>
	</div>

<u:listArea>
	<tr>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:icon type="new" /><u:msg titleId="ct.cols.preView" alt="미리보기" /> 8</td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2014-01-17</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /> 7</td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-11-10</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /> 6</td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-11-10</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /> 5</td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-11-09</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /> 4</td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-11-08</td>
	</tr>
</u:listArea>

</div>

<% // 영역3 %>
<div style="float: left; width: 49%; height: 198px;">

<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="title_s"><u:msg titleId="ct.cols.preView" alt="미리보기" /></dd>
	</dl>
	</div>
</div>

<u:listArea id="listArea">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2014-01-17</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-11-10</td>
	</tr>
</u:listArea>

</div>

<% // 영역4 %>
<div style="float: right; width: 49%; height: 198px;">

<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="title_s"><u:msg titleId="ct.cols.preView" alt="미리보기" /></dd>
	</dl>
	</div>
</div>

<u:listArea>
	<tr>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-12-09</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct"><u:msg titleId="ct.cols.preView" alt="미리보기" /></td>
	<td class="body_ct">2013-12-09</td>
	</tr>
</u:listArea>

</div>
