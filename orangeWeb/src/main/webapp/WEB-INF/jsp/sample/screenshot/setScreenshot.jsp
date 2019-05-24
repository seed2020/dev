<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:set var="preview" value="N"/><!-- 미리보기 -->
<link rel="stylesheet" href="/css/container.blue.css" type="text/css">
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print">
<style type="text/css">
.selectArea{border:2px solid #0080FF;}
</style>
<script type="text/javascript" src="/js/jquery-1.10.2.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/commonEx.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/screenshot/html2canvas.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/screenshot/es6-promise.auto.min.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--
var imgHdlCallBack=null;
<% // [버튼:다운] - 이미지 다운로드 %>
function downImg(typ){
	<c:if test="${preview eq 'Y'}">
	imgHdlCallBack=function(dataUrl){
		$('#previewImg img').attr('src', dataUrl);// 미리보기
	}
	</c:if>
	
	var objId='selectContainer';
	if(typ===undefined) // 기본
		htmlToCanvas(objId, null, null, imgHdlCallBack);
	else if(typ=='size') // 사이즈 조절
		htmlToCanvas(objId, {width:500, height:200}, null, imgHdlCallBack);
	else if(typ=='name') // 파일명 변경
		htmlToCanvas(objId, null, '이미지', imgHdlCallBack);
	else if(typ=='select'){ // 영역 선택
		initImgMouseEvt('selectContainer', [{name:'table'}], null, null, imgHdlCallBack);
	}
};

$(document.body).ready(function(){
	//initImgMouseEvt(); //이미지 마우스 이벤트 초기화
});
//-->
</script>
<p><br /></p>
<div style="padding:5px;">
<div id="selectContainer" >
<div class="imgSelect">
<u:listArea id="listArea" colgroup="3%,,13%,15%,5%,5%">
<tr >
<td class="head_ct">번호</td>
<td class="head_ct">제목</td>
<td class="head_ct">등록자</td>
<td class="head_ct">등록일시</td>
<td class="head_ct">조회수</td>
<td class="head_ct">첨부</td>
</tr>
<tr class="trout" >
<td class="body_ct">10</td>
<td class="body_lt">
<div class="ellipsis" title="한화 - 양식 결재 - 양식 참조문서 - 본문 조회" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">한화 - 양식 결재 - 양식 참조문서 - 본문 조회</div></td>
<td class="body_ct">김하나</td>
<td class="body_ct">2018-10-30 16:21:57</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">9</td>
<td class="body_lt">
<div class="ellipsis" title="부서대기함, 접수함" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">부서대기함, 접수함</div></td>
<td class="body_ct">김하나</td>
<td class="body_ct">2018-10-15 13:53:26</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">8</td>
<td class="body_lt">
<div class="ellipsis" title="결재 - Custom JSP - 연결문서 /대진 /안전사고 보고서" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">결재 - Custom JSP - 연결문서 /대진 /안전사고 보고서</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-09-10 10:44:44</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">7</td>
<td class="body_lt">
<div class="ellipsis" title="개정년차 - 노동부 문서" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-weight: bold;">개정년차 - 노동부 문서</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-08-21 16:44:22</td>
<td class="body_ct">0</td>
<td class="body_ct"><a><img src="http://127.0.0.1:8080/images/blue/ico_file.png" alt="첨부파일" title="첨부파일" width="10" height="10" style="border: none;" /></a></td>
</tr>
<tr class="trout" >
<td class="body_ct">6</td>
<td class="body_lt">
<div class="ellipsis" title="한화 - 기간 연계 - 교육신청서" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">한화 - 기간 연계 - 교육신청서</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-08-13 14:34:05</td>
<td class="body_ct">1</td>
<td class="body_ct"><a><img src="http://127.0.0.1:8080/images/blue/ico_file.png" alt="첨부파일" title="첨부파일" width="10" height="10" style="border: none;" /></a></td>
</tr>
<tr class="trout" >
<td class="body_ct">5</td>
<td class="body_lt">
<div class="ellipsis" title="팝업창에서 대상 윈도우 찾기" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">팝업창에서 대상 윈도우 찾기</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-08-01 15:31:33</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">4</td>
<td class="body_lt">
<div class="ellipsis" title="Mobile > PC sso 처리" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">Mobile &gt; PC sso 처리</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-07-30 17:31:44</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">3</td>
<td class="body_lt">
<div class="ellipsis" title="업무 저장" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; font-weight: bold;">업무 저장</div></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">2018-07-23 19:16:08</td>
<td class="body_ct">0</td>
<td class="body_ct"></td>
</tr>
<tr class="trout" >
<td class="body_ct">2</td>
<td class="body_lt">
<div class="ellipsis" title="fffffff" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">fffffff</div></td>
<td class="body_ct">김하나</td>
<td class="body_ct">2018-07-23 17:31:17</td>
<td class="body_ct">1</td>
<td class="body_ct"><a><img src="http://127.0.0.1:8080/images/blue/ico_file.png" alt="첨부파일" title="첨부파일" width="10" height="10" style="border: none;" /></a></td>
</tr>
<tr class="trout" >
<td class="body_ct">1</td>
<td class="body_lt">
<div class="ellipsis" title="1111111111111" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">1111111111111</div></td>
<td class="body_ct">김하나</td>
<td class="body_ct">2018-07-18 17:45:42</td>
<td class="body_ct">1</td>
<td class="body_ct"><a><img src="http://127.0.0.1:8080/images/blue/ico_file.png" alt="첨부파일" title="첨부파일" width="10" height="10" style="border: none;" /></a></td>
</tr>
</u:listArea>

<div class="blank"></div>
<u:listArea id="listArea2" colgroup="3%,,13%,15%,5%,5%">
<tr >
<td class="head_ct">번호</td>
<td class="head_ct">제목</td>
<td class="head_ct">등록자</td>
<td class="head_ct">등록일시</td>
<td class="head_ct">조회수</td>
<td class="head_ct">첨부</td>
</tr>
<tr class="trout" >
<td class="body_ct">10</td>
<td class="body_lt">
<div class="ellipsis" title="한화 - 양식 결재 - 양식 참조문서 - 본문 조회" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">한화 - 양식 결재 - 양식 참조문서 - 본문 조회</div></td>
<td class="body_ct">김하나</td>
<td class="body_ct">2018-10-30 16:21:57</td>
<td class="body_ct">1</td>
<td class="body_ct"></td>
</tr>
</u:listArea>

</div></div>
<div class="blank"></div>
<% // 하단 버튼 %>
<div class="button_basic notPrint">
	<ul>
	<li class="basic notImage"><a href="javascript:void(0);" onclick="downImg();" title="다운로드"><span>이미지로 다운로드</span></a></li>
	<li class="basic"><a href="javascript:void(0);" onclick="downImg('size');" title="사이즈지정"><span>이미지로 다운로드(사이즈지정)</span></a></li>
	<li class="basic notImage"><a href="javascript:void(0);" onclick="downImg('name');" title="파일명변경"><span>이미지로 다운로드(파일명변경)</span></a></li>
	<li class="basic"><a href="javascript:void(0);" id="selectAreaBtn" onclick="downImg('select');" title="영역선택"><span>이미지로 다운로드(영역선택)</span></a></li>
	</ul>
</div><iframe id="dataframe" name="dataframe" src="about:blank" style="border:0px;width:0px;height:0px;"></iframe>

</div>
<c:if test="${preview eq 'Y'}">
<div class="blank"></div>
<div id="previewImg"><div><strong>* 미리보기</strong></div><img src=""/></div>
</c:if>

