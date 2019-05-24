<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.config.CustConfig,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil,
			com.innobiz.orange.web.pt.utils.SysSetupUtil,
			com.innobiz.orange.web.pt.secu.LoginSession,
			com.innobiz.orange.web.pt.secu.Browser"
%><%@ attribute name="id"    required="true" rtexprvalue="true"
%><%@ attribute name="value" required="false" rtexprvalue="true"
%><%@ attribute name="width"  required="false"
%><%@ attribute name="height"  required="false"
%><%@ attribute name="module"  required="false"
%><%@ attribute name="areaId"  required="false"
%><%@ attribute name="padding"  required="false"
%><%@ attribute name="init"  required="false"
%><%@ attribute name="noFocus"  required="false" type="java.lang.Boolean"
%><%@ attribute name="namoInitFnc"  required="false"
%><%@ attribute name="namoToolbar"  required="false"
%><%
/*

사용법
  1. submit 전 jellyEditor("editerID").prepare() 호출할것.
  2. 값 유무 체크는 jellyEditor("editerID").empty() 로 할것.

  
cm.editor.file=파일
cm.editor.desc=설명
cm.editor.edit=편집
cm.editor.viewSrc=소스보기
cm.editor.notMerge=셀이 합해질 수 없습니다.
cm.editor.imgExt=이미지 확장자는 "gif", "jpg", "png", "tiff" 만 지원 합니다.

[변경사항]
	width:100% 추가
	padding - 추가


*/
	if(width==null || width.isEmpty()) width = "800";
	if(height==null || height.isEmpty()) height = "500";

	String paddingOpen="", paddingClose="";
	if(padding!=null && !padding.isEmpty()){
		int intHeight = Integer.parseInt(height.endsWith("px") ? height.substring(0, height.length()-2).trim() : height);
		intHeight = Integer.parseInt(padding)*2 + intHeight;
		paddingOpen = "<div style=\"padding-top:"+padding+"px; padding-bottom:"+padding+"px; width:"+width+"; min-height:"+intHeight+"px; float:left;\">";
		paddingClose = "</div>";
	}

%><%= paddingOpen%><%

	boolean namoEditorEnable = request.getAttribute("namoEditorEnable") != null;

	// 나모 에디터
	if(namoEditorEnable){
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// API
		// http://comp.namo.co.kr/ce3/help/ko/dev/body_api.htm
		// 툴바 참조
		// http://comp.namo.co.kr/ce3/help/dev/properties/CreateToolbar2.htm
		
		// 팝업의 경우 - 툴바 제어
		if("wcPop".equals(namoToolbar)){
			namoToolbar = "fontname|fontsize|lineheight|spacebar|word_style|space|word_color|space|word_indentset|spacebar|tabledraginsert|image|insertfile|specialchars|emoticon|spacebar|undo|redo|saveas";
		} else {
			namoToolbar = null;
		}
		
		// 나모 4.0 툴바 제어
		String namoToolbar40 = "newdoc|fopen|saveas|print|pagebreak|spacebar|undo|redo|cut|copy|paste|pastetext|search|replace|selectall|spacebar|image|backgroundimage|flash|insertchart|insertfile|spacebar|hyperlink|remove_hyperlink|bookmark|inserthorizontalrule|specialchars|emoticon|spacebar|layout|spacebar|word_layer|enter|word_style|word_color|cancelattribute|spacebar|word_justify|word_indentset|txtmargin|word_listset|spacebar|tableinsert|tabledraginsert|spacebar|tablerowinsert|tablerowdelete|tablecolumninsert|tablecolumndelete|spacebar|tablecellmerge|tablecellsplit|spacebar|tablecellattribute|spacebar|spellchecker|enter|template|fontname|fontsize|lineheight|spacebar|blockquote|word_script|spacebar|formatcopy|spacebar|word_dir|spacebar|fullscreen|help|information";
		
	%>
<textarea id="<%= id%>" name="<%= id%>" title="editor init text" style="display:none;"><%= EscapeUtil.escapeTextarea(value)%></textarea>
<script type="text/javascript">
	var <%= id%>Namo = new NamoSE("<%= id%>");
	setNamoEditor("<%= id%>", <%= id%>Namo);
	
	<%= id%>Namo.params.Width = "<%= width%>";
	<%= id%>Namo.params.Height = parseInt("<%= height%>");<%
	if(Boolean.TRUE.equals(noFocus)){%>
	<%= id%>Namo.params.SetFocus = false;
	<%}%>
	<%= id%>Namo.params.AjaxCacheSetup = false;<%
		if(namoToolbar!=null){%>
	<%= id%>Namo.params.Menu = false;
	<%= id%>Namo.params.CreateToolbar = "<%= namoToolbar%>";
	<%= id%>Namo.params.ResizeBar = false; <%  // 에디터 Resize 비활성화
		} else {%>
	if(<%= id%>Namo.IsProhibitProfanity != null){<%// 4.0 버전에서 추가된 함수 - 욕설방지 %>
		<%= id%>Namo.params.Menu = false;
		<%= id%>Namo.params.CreateToolbar = "<%= namoToolbar40%>";
	}<%}

	// 언어 설정	
	// 한국어 : kor
	// 영어   : enu
	// 일본어 : jpn
	// 중국어 간체 : chs
	// 중국어 번체 : cht
	// 자동설정 : auto(브라우저 언어에 해당하는 언어 리소스가 없을 경우 기본 언어로 설정됨)
		if(!"ko".equals(langTypCd)){%>
	<%= id%>Namo.params.UserLang = "<%= (
				"en".equals(langTypCd) ? "enu" : 
					"ja".equals(langTypCd) ? "jpn" : 
						"zh".equals(langTypCd) ? "chs" : "auto")%>";<%}%><%
		if(areaId != null && !areaId.isEmpty()){%>
	<%= id%>Namo.params.ParentEditor = document.getElementById("<%= areaId%>");
	//$(<%= id%>Namo.params.ParentEditor).css('height','<%= height%>');
	<%}
	
	// 폰트 설정
	
	// 디폴트
	// \"돋움\":\"돋움\",\"굴림\":\"굴림\",\"바탕\":\"바탕\",\"궁서\":\"궁서\",\"David\":\"David\",\"MS PGothic\":\"MS PGothic\",\"New MingLiu\":\"New MingLiu\",\"Simplified Arabic\":\"Simplified Arabic\",\"simsun\":\"simsun\",\"Arial\":\"Arial\",\"Courier New\":\"Courier New\",\"Tahoma\":\"Tahoma\",\"Times New Roman\":\"Times New Roman\",\"Verdana\":\"Verdana\"
	
	// 언어가 한글 일때
	if("ko".equals(langTypCd)){
		String fonts = null;
		// 프라코 - FB7F63, 개발서버 - AD8227
		if(CustConfig.CUST_PLAKOR || CustConfig.DEV_SVR){
			fonts = "\"돋움\":\"돋움\",\"돋움체\":\"돋움체\",\"굴림\":\"굴림\",\"굴림체\":\"굴림체\",\"바탕\":\"바탕\",\"바탕체\":\"바탕체\",\"궁서\":\"궁서\",\"궁서체\":\"궁서체\""
					+ ",\"현대하모니 B\":\"현대하모니 B\",\"현대하모니 L\":\"현대하모니 L\",\"현대하모니 M\":\"현대하모니 M\""
					+ ",\"맑은 고딕\":\"맑은 고딕\""
					+ ",\"David\":\"David\",\"MS PGothic\":\"MS PGothic\",\"New MingLiu\":\"New MingLiu\",\"Simplified Arabic\":\"Simplified Arabic\",\"simsun\":\"simsun\",\"Arial\":\"Arial\",\"Courier New\":\"Courier New\",\"Tahoma\":\"Tahoma\",\"Times New Roman\":\"Times New Roman\",\"Verdana\":\"Verdana\"";
		} else {
			// 디폴트에 - 돋움체,굴림체,바탕체,궁서체 - 추가
			fonts = "\"돋움\":\"돋움\",\"돋움체\":\"돋움체\",\"굴림\":\"굴림\",\"굴림체\":\"굴림체\",\"바탕\":\"바탕\",\"바탕체\":\"바탕체\",\"궁서\":\"궁서\",\"궁서체\":\"궁서체\""
					+ ",\"맑은 고딕\":\"맑은 고딕\""
					+ ",\"David\":\"David\",\"MS PGothic\":\"MS PGothic\",\"New MingLiu\":\"New MingLiu\",\"Simplified Arabic\":\"Simplified Arabic\",\"simsun\":\"simsun\",\"Arial\":\"Arial\",\"Courier New\":\"Courier New\",\"Tahoma\":\"Tahoma\",\"Times New Roman\":\"Times New Roman\",\"Verdana\":\"Verdana\"";
		}
		%>
	<%= id%>Namo.params.Font = {<%= fonts%>};<%
	}
	
	%>
	<%= id%>Namo.EditorStart();
	//<%= id%>Namo.SetDirty();
	function OnInitCompleted(e){<%
		if(namoInitFnc==null || namoInitFnc.isEmpty()){%>
		e.editorTarget.SetBodyValue(document.getElementById(e.editorName).value);
		window.setTimeout(e.editorName+'Namo.SetDirty();', 1000);<%
		} else {%>
		<%=namoInitFnc%>("<%= id%>");<%
		}%>
		unloadEvent.addEditor("<%= id%>", "namo");
	}
</script><%
		


	// jelly 에디터
	} else {

		// 어권별 폰트 조회
		String langTypCd = LoginSession.getLangTypCd(request);
		String fonts = EscapeUtil.escapeScript(SysSetupUtil.getFonts(langTypCd));// 어권별 설정된 폰트

		// 에디터에서 사용하는 리소스
		boolean first = true;
		Locale locale = SessionUtil.getLocale(request);
		MessageProperties properties = MessageProperties.getInstance();
		StringBuilder builder = new StringBuilder(128);
		String[] rescs = { "file","desc","edit","cfrm","cncl","viewSrc","msgNotMerge","msgImgExt","msgImgFile" };
		for(String resc : rescs){
			if(first) first = false;
			else builder.append(',');
			builder.append(resc).append(':').append('"');
			builder.append(EscapeUtil.escapeScript(properties.getMessage("cm.editor."+resc, null, locale))).append('"');
		}
		
		if(module==null || module.isEmpty()) module = "etc";//pt, bb, ct, ap, or
		if(areaId==null || areaId.isEmpty()) areaId = "";
		
		String noFocusOption = Boolean.TRUE.equals(noFocus) ? ", noFocus:true" : "";

%>
<script type="text/javascript">
jellyEditor('<%= id%>').setConfig({width:'<%= width%>', height:'<%= height
	%>', module:'<%= module%>', lang:'<%= langTypCd%>', fonts:'<%= fonts
	%>', uploadUrl:'/cm/jelly/transImage.do'<%= noFocusOption%>, resc:{<%= builder.toString()%>}});<%

	
if(init==null||value!=null||init.equals("true")){
%>
jellyEditor('<%= id%>').setValue('<%= EscapeUtil.escapeScript(value)%>');
jellyEditor('<%= id%>').writeEditor('<%= areaId%>');
unloadEvent.addEditor('<%= id%>');<%
}

%>
</script><%
		
	}
%><%= paddingClose%>