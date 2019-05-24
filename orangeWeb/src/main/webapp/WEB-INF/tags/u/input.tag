<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id"    required="false" rtexprvalue="true"
%><%@ attribute name="name" required="false" rtexprvalue="true"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="titlePrefix" required="false"
%><%@ attribute name="koDesc" required="false"
%><%@ attribute name="value" required="false" rtexprvalue="true"
%><%@ attribute name="className"  required="false"
%><%@ attribute name="style"  required="false"
%><%@ attribute name="type"  required="false"
%><%@ attribute name="maxLength"  required="false"
%><%@ attribute name="maxByte"  required="false"
%><%@ attribute name="maxInt"  required="false"
%><%@ attribute name="maxFloat"  required="false"
%><%@ attribute name="minLength"  required="false"
%><%@ attribute name="minByte"  required="false"
%><%@ attribute name="minInt"  required="false"
%><%@ attribute name="minFloat"  required="false"
%><%@ attribute name="onclick"  required="false"
%><%@ attribute name="onfocus"  required="false"
%><%@ attribute name="onblur"  required="false"
%><%@ attribute name="onchange"  required="false"
%><%@ attribute name="onkeydown"  required="false"
%><%@ attribute name="onkeyup"  required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="disabled"  required="false"
%><%@ attribute name="mandatory"  required="false"
%><%@ attribute name="mandatorySkipper"  required="false"
%><%@ attribute name="validator"  required="false"
%><%@ attribute name="skipper"  required="false"
%><%@ attribute name="modifier"  required="false"
%><%@ attribute name="commify"  required="false"
%><%@ attribute name="valueOption"  required="false"
%><%@ attribute name="valueAllowed"  required="false"
%><%@ attribute name="valueNotAllowed"  required="false"
%><%@ attribute name="alt"  required="false"
%><%@ attribute name="dataList"  required="false"
%><%@ attribute name="placeholder" required="false"
%><%
/*
HTML	: <input type="text" />

id		: id, name에 사용
title	: title
value	: value
name	: 없으면 id, id/name 다를경우 사용하기 위한것
readonly: 읽기전용
disabled: 비활성화

type	: text(default), hidden, view(HTML로 출력), password 

[입력타입 가능 케렉터 조절]
valueOption : number(숫자), alpha(대소문자), lower(소문자), upper(대문자), email(이메일)
valueAllowed : @_-.
valueNotAllowed : !#$

*/

if(name==null) name = id;
//<a href="javascript:mailToPop
if("view".equals(type)){
	if(value!=null && !value.isEmpty() && "email".equals(valueOption)){
		%><a href="javascript:mailToPop('<%=EscapeUtil.escapeScript(value)%>')"><%=EscapeUtil.escapeHTML(value)%></a><%
	} else {
		%><%=EscapeUtil.escapeHTML(value)%><%
	}
} else if("hidden".equals(type)) {
	if(id==null) id = name;
	%><input id="<%=id%>" name="<%=name%>" type="hidden" value="<%=EscapeUtil.escapeValue(value) %>" /><%
} else {
	Locale locale = SessionUtil.getLocale(request);
	MessageProperties properties = MessageProperties.getInstance();
	if(title==null){
		if(titleId==null || titleId.isEmpty()){
			titleId = "cols."+id;
		}
		title = properties.getMessage(titleId, locale);
	}
	
	StringBuffer buffer = new StringBuffer(128);
	String actName = properties.getMessage("cm.input.actname", locale==null ? Locale.KOREA : locale);
	if(locale.getLanguage().equals("ko")){
		if(titlePrefix!=null && !titlePrefix.isEmpty()){
			title = titlePrefix+" "+title;
		}
		if(koDesc==null || koDesc.isEmpty()){
			buffer.append(" title=\"").append(title);
			if(!actName.isEmpty() && !title.endsWith(actName)) buffer.append(' ').append(actName);
			buffer.append('\"');
		} else {
			buffer.append(" title=\"").append(koDesc).append('\"');
		}
	} else {
		buffer.append(" title=\"").append(title);
		buffer.append('\"');
	}
	
	if("password".equals(type)){
		buffer.append(" type=\"password\"");
	} else {
		buffer.append(" type=\"text\"");
	}
	
	// [[[ css 클래스 지정 ]]]
	boolean hasClassName = false;
	buffer.append(" class=\"");
	if(className!=null && !className.isEmpty()){
		buffer.append(className);
		hasClassName = true;
	}
	if( "Y".equals(readonly) || "readonly".equals(readonly) ){
		if(hasClassName) buffer.append(' ');
		buffer.append("input_disabled");
		hasClassName = true;
	}
	if( "Y".equals(disabled) || "disabled".equals(disabled) ){
		if(hasClassName) buffer.append(' ');
		buffer.append("input_disabled");
		hasClassName = true;
	}
	buffer.append("\"");
	
	if(style!=null && !style.isEmpty()) buffer.append(" style=\"").append(style).append('\"');
	if(maxLength!=null && !maxLength.isEmpty()) buffer.append(" maxlength=\"").append(maxLength).append('\"');
	if(onclick!=null && !onclick.isEmpty() ) buffer.append(" onclick=\"").append(EscapeUtil.escapeValue(onclick)).append('\"');
	if(onchange!=null && !onchange.isEmpty() ) buffer.append(" onchange=\"").append(EscapeUtil.escapeValue(onchange)).append('\"');
	if("Y".equals(commify)) {
		onblur  =  onblur==null ?   "commifyInput('"+id+"')" :   "commifyInput('"+id+"');"+onblur;
		onfocus = onfocus==null ? "uncommifyInput('"+id+"')" : "uncommifyInput('"+id+"');"+onfocus;
	}
	if(onfocus!=null && !onfocus.isEmpty() ) buffer.append(" onfocus=\"").append(EscapeUtil.escapeValue(onfocus)).append('\"');
	if(onblur!=null && !onblur.isEmpty() ) buffer.append(" onblur=\"").append(EscapeUtil.escapeValue(onblur)).append('\"');
	if(onkeydown!=null && !onkeydown.isEmpty() ) buffer.append(" onkeydown=\"").append(EscapeUtil.escapeValue(onkeydown)).append('\"');
	
	if("Y".equals(readonly) || "readonly".equals(readonly)) buffer.append(" readonly=\"readonly\"");
	if("Y".equals(disabled) || "disabled".equals(disabled)) buffer.append(" disabled=\"disabled\"");
	
	if(dataList!=null && !dataList.isEmpty()) buffer.append(" "+dataList);
	
	if(placeholder!=null && !placeholder.isEmpty()) buffer.append(" placeholder=\""+placeholder+"\"");
	
	//[[[  input-html 출력  ]]]
	%><input name="<%=name%>" id="<%=id%>" value="<%=EscapeUtil.escapeValue(value)%>"<%=buffer.toString()%> /><%
	
//	valueOption : number(숫자), alpha(대소문자), lower(소문자), upper(대문자), email(이메일)
//	valueAllow : @_.
//	valueNowAllow : !#$
	String charOption = null;
	if(valueOption!=null || valueAllowed!=null || valueNotAllowed!=null || "Y".equals(commify)){
		if(valueOption==null){
			if("Y".equals(commify)) valueOption = "number";
			else valueOption = "alpha,number";
		} else if("email".equals(valueOption)){
			valueOption = "lower,number";
			valueAllowed = "@_-.";
		}
		StringBuilder builder = new StringBuilder(64);
		
		if(valueOption.indexOf("alpha")>=0 || valueOption.indexOf("upper")>=0) builder.append(StringUtil.UPPERS);
		if(valueOption.indexOf("alpha")>=0 || valueOption.indexOf("lower")>=0) builder.append(StringUtil.LOWERS);
		if(valueOption.indexOf("number")>=0){ builder.append(StringUtil.NUMBERS); }
		if(valueOption.indexOf("float")>=0){ builder.append(StringUtil.NUMBERS).append('.'); }
		
		if(valueAllowed!=null) StringUtil.appendNotHaveChars(builder, valueAllowed);
		charOption = StringUtil.removeChars(builder.toString(), valueNotAllowed);
	}
	
	boolean needKeyUpHandler = false;
	%>
<script type="text/javascript">
$(document).ready(function() {
	var inputTitle = '<%=title%>';
	validator.addTitle('<%=id%>', inputTitle);
	validator.addHandler('<%=id%>', function(id, va, eventNm){<%
	
	// mandatorySkipper - 해당 javascript 를 실행해서 필수여부 체크를 하지 않을지 결정 - 어권별 입력의 필수가 아닐 경우 사용
	if(mandatorySkipper!=null) { %>
		var skipMandatoryVa = <%= mandatorySkipper%>;<%
	}
	
	// Validation 건너뛰기 가 있을 경우
	if(skipper!=null && !skipper.isEmpty()){
	%>
		if(eventNm == null && <%= skipper%> == true){
			return true;
		}
	<%
	}
	
	// 검증자가 있을 경우
	if(validator!=null && !validator.isEmpty()){
		needKeyUpHandler = true;
	%>
		if(eventNm == null<%if(mandatorySkipper!=null){%> && !skipMandatoryVa<%}%> && <%= validator%> == false){
			return false;
		}
	<%
	}
	
	if("Y".equals(mandatory)){
		needKeyUpHandler = true;
	%>
		if(eventNm == null && isEmptyVa(va)<%if(mandatorySkipper!=null){%> && !skipMandatoryVa<%}%>){
			alertMsg('cm.input.check.mandatory',['<%=title%>']);
			return false;
		}<%
	}
	
	// Ctrl-v 의 경우 사용 금지 char 제거
	if(!"Y".equals(readonly) && charOption != null && !charOption.isEmpty()){
		needKeyUpHandler = true;
	%>
		if(eventNm != null) {
			var currect = removeNotAllowedChars(va, '<%=charOption%>');
			if(currect!=va) $('#<%=id%>').val(currect);
		}
	<%
	}
	
	if(maxByte!=null && !maxByte.isEmpty()){
		if(minByte==null || minByte.isEmpty()) minByte = "null";
		needKeyUpHandler = true;
	%>
		var ck = isInUtf8Length(va, <%=minByte%>, <%=maxByte%>);
		if(ck!=0){
			if(ck>0) {
				alertMsg('cm.input.check.maxbyte',['<%=title%>','<%=maxByte%>']);
				$('#<%=id%>').val(cutUtf8(va, '<%=maxByte%>'));
			}<%
			if(!"null".equals(minByte)){%>
			if(eventNm == null && ck<0) {
				alertMsg('cm.input.check.minbyte',['<%=title%>','<%=minByte%>']);
			}<%}
			
			if(modifier!=null){%>
			<%=modifier%><%
			}%>
			return false;
		}
	<%
	} else if(minLength!=null && !minLength.isEmpty()){
	%>
		if(eventNm == null && va!='' && va.length < <%= minLength%>){
			alertMsg('cm.input.check.minlength',['<%=title%>','<%=minLength%>']);
			return false;
		}
	<%
		
	//minLength
	} else if(maxInt!=null && !maxInt.isEmpty()){
		if(minInt==null || minInt.isEmpty()) minInt = "null";
		needKeyUpHandler = true;
	%>
		var ck = isInInt(va, <%=minInt%>, <%=maxInt%>);
		if(ck!=0){
			if(ck>0) {
				alertMsg('cm.input.check.maxint',['<%=title%>','<%=maxInt%>']);
				$('#<%=id%>').val('<%=maxInt%>');
			}<%
			if(!"null".equals(minInt)){%>
			if(eventNm == null && ck<0) {
				alertMsg('cm.input.check.minint',['<%=title%>','<%=minInt%>']);
				$('#<%=id%>').val('<%=minInt%>');
			}<%}
			
			if(modifier!=null){%>
			<%=modifier%><%
			}%>
			return false;
		}
	<%
	} else if(maxFloat!=null && !maxFloat.isEmpty()){
		if(minFloat==null || minFloat.isEmpty()) minFloat = "null";
		needKeyUpHandler = true;
	%>
		var ck = isInFloat(va, <%=minFloat%>, <%=maxFloat%>);
		if(ck!=0){
			if(ck>0) {
				alertMsg('cm.input.check.maxfloat',['<%=title%>','<%=maxFloat%>']);
				$('#<%=id%>').val('<%=maxFloat%>');
			}<%
			if(!"null".equals(minFloat)){%>
			if(eventNm == null && ck<0) {
				alertMsg('cm.input.check.minfloat',['<%=title%>','<%=minFloat%>']);
				$('#<%=id%>').val('<%=minFloat%>');
			}<%}
			
			if(modifier!=null){%>
			<%=modifier%><%
			}%>
			return false;
		}<%
		
	}%>
	});<%
	
	if(needKeyUpHandler){%>
	$('#<%=id%>').bind('keyup',function(event) {
		var handler = validator.handlerMap.get('<%=id%>');
		if(handler!=null && !validator.escapeCodes.contains(!event.charCode ? event.which : event.charCode)){<%
			if(onkeyup!=null && !onkeyup.isEmpty()){%>
			return handler('<%=id%>', $(this).val(), 'keyup') != false && <%= onkeyup%>;<%
			} else {%>
			return handler('<%=id%>', $(this).val(), 'keyup');<%
			} %>
		};<%
		
	if(onkeyup!=null && !onkeyup.isEmpty()){%>
		return <%= onkeyup%>;<%
	} else {%>
		return true;<%
	} %>
	});
	<%
	} else {// if(needKeyUpHandler) %>
	$('#<%=id%>').bind('keyup',<%= onkeyup%>);
	<%
	}
	
	
if("Y".equals(readonly)){%>
	var $input = $('#<%=id%>');
	$input.bind('contextmenu',function () {if(e.preventDefault){e.preventDefault();} return false;});
	$input.keypress(function (e){if(e.preventDefault){e.preventDefault();} return false;});<%
	
} else if(charOption != null && !charOption.isEmpty()){%>
	var $input = $('#<%=id%>');
	$input.css('ime-mode','disabled');
	$input.bind('contextmenu',function () {if(e.preventDefault){e.preventDefault();} return false;});
	$input.keypress(function (e){
		var keyCode = (!e.charCode) ? e.which : e.charCode;
		var keyValue = String.fromCharCode(keyCode);
		if(keyCode!=0 && keyCode!=8){<%//Firefox 에서 - 0:기능키(del, home, end, 왼쪽, 오른쪽 등), 8:back-del%>
			if ('<%= charOption%>'.indexOf(keyValue) == -1){
				if(e.preventDefault){e.preventDefault();}
				return false;
			}
		}
	});<%
}
	%>
});
</script>
<%
}

%>