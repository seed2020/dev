<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

/*
ap.cmpt.header=머리글
ap.cmpt.apvLine=결재라인
	ap.cmpt.apvLine.apvLnMixd=결재(합의1칸)
	ap.cmpt.apvLine.apvLn=결재(합의표시안함)
	ap.cmpt.apvLine.apvLn1LnAgr=결재+합의(1줄)
	ap.cmpt.apvLine.apvLn2LnAgr=결재+합의(2줄)
	ap.cmpt.apvLine.apvLnDbl=신청+처리(이중결재)
	ap.cmpt.apvLine.apvLnWrtn=신청+처리(서면결재)
	ap.cmpt.apvLine.apvLnList=리스트
	ap.cmpt.apvLine.apvLnDblList=리스트(이중결재)
	ap.cmpt.apvLine.apvLnOneTopList=최종결재 리스트
	ap.cmpt.apvLine.apvLnMultiTopList=도장방 리스트
ap.cmpt.items=항목지정
ap.cmpt.body=본문
	ap.cmpt.editorBody=본문 (편집기)
	ap.cmpt.formBody=본문 (폼양식)
	ap.cmpt.formEditBody=본문 (편집양식)
	ap.cmpt.wfFormBody=본문 (업무양식)
ap.cmpt.sender=발신명의
ap.cmpt.footer=바닥글
ap.cmpt.refDocBdoy=참조문서 본문
ap.cmpt.refVw=참조열람
ap.cmpt.infm=통보
ap.cmpt.printer=인쇄설정
*/

//폼양식 사용
boolean formBodyEnable = request.getAttribute("isAdmin")!=null;
java.util.Map<String, String> sysPlocMap = (java.util.Map<String, String>)request.getAttribute("sysPlocMap");
//폼양식과 에디터 같이 사용 못함
//boolean noBothBody = true;
boolean noBothBody = !"Y".equals(sysPlocMap.get("apEditorAndForm"));

java.util.Map<String, String> optConfigMap = (java.util.Map<String, String>)request.getAttribute("optConfigMap");

String[] components = {"header","apvLine","items",
		(formBodyEnable ? "body" : null),
		("Y".equals(optConfigMap.get("refVwEnable")) ? "refVw" : null),
		"infm",
		"refDocBdoy","sender","footer","printer"};
request.setAttribute("components", components);

//TODO - onecard 추가 - apvLnOnecard
String[] apvLns = {"apvLnMixd","apvLn","apvLn1LnAgr","apvLn2LnAgr","apvLnDbl","apvLnWrtn",
		"apvLnList","apvLnDblList","apvLnOneTopList","apvLnMultiTopList"};//, "apvLnOnecard"};
request.setAttribute("apvLns", apvLns);

if(formBodyEnable){
	String[] bodies = {"editorBody","formBody", "formEditBody", ("Y".equals(sysPlocMap.get("wfEnable")) ? "wfFormBody" : null)};
	request.setAttribute("bodies", bodies);
}

if(noBothBody){
	request.setAttribute("noBothBody", Boolean.TRUE);
}

%>
<script type="text/javascript">
<!--<%
// 컴포넌트 구성 트리 클릭 %>
function applyComponent(id, name){
	popComponent(id);
}<%
// 컴포넌트 위 아래로 이동 %>
function moveComponent(obj, direction){
	var $area = $(obj).parents(".titlearea").parent();
	var isEditor = $area.attr('id')=='bodyHtmlArea' || ($area.find("iframe[id^='Namo']").length == 1);
	if(isEditor){
		if(direction=='up'){
			var $prev = $area.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0){
				$prev.insertAfter($area);
			}
		} else if(direction=='down'){
			var $next = $area.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(':visible')){
				$next.insertBefore($area);
			}
		}
	} else {
		if(direction=='up'){
			var $prev = $area.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0 && $prev.is(":visible") != true) $prev = $prev.prev();
			if($prev.length>0){
				$area.insertBefore($prev);
			}
		} else if(direction=='down'){
			var $next = $area.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(":visible") != true) $next = $next.next();
			if($next.length>0 && $next.is(':visible')){
				$area.insertAfter($next);
			}
		}
	}
}<%
// 스타일 - 스타일 지정 버튼 %>
function openStylePop(formTxtTypCd, titleId){
	var title = callMsg(titleId);
	dialog.open("setDocStyleDialog", title+' <u:msg titleId="ap.cmpt.style" alt="스타일" />', "./setDocStylePop.do?menuId=${menuId}&formTxtTypCd="+formTxtTypCd);
}<%
// 인쇄모드/편집모드 전환 %>
function setViewMode(mode){
	var bodyHtmlArea = $("#docArea #bodyHtmlArea");
	if(mode=='print'){
		$("#formFuncDiv").hide();
		var $editDiv = $("#formEditDiv").css("width","100%");
		
		if(bodyHtmlArea.length>0 && bodyHtmlArea.is(":visible")){
			var $bodyHtmlViewArea = $editDiv.find("#bodyHtmlViewArea");
			if($bodyHtmlViewArea.length>0){
				$bodyHtmlViewArea.show().find(".editor").html(editor("bodyHtml").getHtml());
				$editDiv.find("#bodyHtmlArea").hide();
			}
		}
	} else {
		$("#formFuncDiv").show();
		var $editDiv = $("#formEditDiv").css("width","82%");
		
		if(bodyHtmlArea.length>0 && !bodyHtmlArea.is(":visible")){
			var $bodyHtmlViewArea = $editDiv.find("#bodyHtmlViewArea");
			if($bodyHtmlViewArea.length>0){
				$bodyHtmlViewArea.hide();
				$editDiv.find("#bodyHtmlArea").show();
				if(unloadEvent.editorType!='namo'){
					editor("bodyHtml").resize();<%// 2번 필요 %>
					editor("bodyHtml").resize();
				}
			}
		}
	}
	if(window.setXmlEditViewMode){
		setXmlEditViewMode(mode);<%// 편집양식 인쇄모드 변경 %>
	}
}<%
// 컴포넌트 더할 위치 %>
function getInsertLoc(){
	var bodyTypes = getBodyTypes(), $area;
	for(var i=0; i<bodyTypes.length; i++){
		$area = $("#"+bodyTypes[i]+"Area");
		if($area.is(":visible")){
			return $area;
		}
	}
	return null;
}<%
// 본문 종류 리턴 %>
function getBodyTypes(){
	return ["bodyHtml", "formBody", "formEditBody", "wfFormBody"];
}<%
////////////////////////////////
// 구성요소
  
// 구성요소 팝업 열기 - 트리클리 or 설정 버튼 %>
var noBothBody = '${noBothBody}'=='true';
function popComponent(id, obj){
	<%//"header","apvLine","items","sender","footer"%>
	if(id=='header'){
		dialog.open("setDocHeaderDialog", '<u:msg titleId="ap.cmpt.header" alt="머리글" />', "./setDocHeaderPop.do?menuId=${menuId}");
		dialog.onClose("setDocHeaderDialog", function(){ dialog.close("setDocStyleDialog"); });
	} else if(id.startsWith('apvLn')){
		if(id=='apvLnList' || id=='apvLnDblList'){
			setApvLnData(null, id);
		} else {
			var title = callMsg('ap.cmpt.apvLine.'+id), tabNo=1;
			if(obj!=null && $(obj).parents(".titlearea").parent().attr("data-seq")=='2') tabNo=2;
			dialog.open("setDocApvLnDialog", '<u:msg titleId="ap.cmpt.apvLine" alt="결재라인" /> - '+title, "./setDocApvLnPop.do?menuId=${menuId}&formApvLnTypCd="+id+(tabNo==2 ? '&tabNo=2' : ''));
		}
	} else if(id=='items'){
		var seq = null;
		if(obj!=null) seq = $(obj).parents(".titlearea").parent().attr("data-seq");
		dialog.open("setDocItemsDialog", '<u:msg titleId="ap.cmpt.items" alt="항목지정" />', "./setDocItemsPop.do?menuId=${menuId}"+(seq!=null ? '&seq='+seq : ''));
	} else if(id=='sender'){
		dialog.open("setDocSenderDialog", '<u:msg titleId="ap.cmpt.sender" alt="발신명의" />', "./setDocSenderPop.do?menuId=${menuId}");
		dialog.onClose("setDocSenderDialog", function(){ dialog.close("setDocStyleDialog"); });
	} else if(id=='footer'){
		dialog.open("setDocFooterDialog", '<u:msg titleId="ap.cmpt.footer" alt="바닥글" />', "./setDocFooterPop.do?menuId=${menuId}");
		dialog.onClose("setDocFooterDialog", function(){ dialog.close("setDocStyleDialog"); });
	} else if(id=='printer'){
		dialog.open("setDocPrinterDialog", '<u:msg titleId="ap.cmpt.printer" alt="인쇄설정" />', "./setDocPrinterPop.do?menuId=${menuId}");
	} else if(id=='editorBody'){
		$("#bodyHtmlArea").show();
		if(unloadEvent.editorType!='namo'){
			editor("bodyHtml").resize();<%// 2번 필요%>
			editor("bodyHtml").resize();
		}
		if(noBothBody){
			toggleBodyArea('bodyHtml');
		}
	} else if(id=='formBody'){
		var erpFormId = $("#formBodyArea #formBodyDataArea input[name='erpFormId']").val();
		dialog.open("listErpFormDialog", '<u:msg titleId="ap.cmpt.formBody" alt="본문 (폼양식)" />', "./listErpFormPop.do?menuId=${menuId}&erpFormTypCd=xmlFromAp"+(erpFormId!=null && erpFormId!='' ? '&erpFormId='+erpFormId : ''));
	} else if(id=='formEditBody'){
		var erpFormId = $("#formEditBodyArea #formEditBodyDataArea input[name='erpFormId']").val();
		dialog.open("listErpFormDialog", '<u:msg titleId="ap.cmpt.formEditBody" alt="본문 (편집양식)" />', "./listErpFormPop.do?menuId=${menuId}&erpFormTypCd=xmlEditFromAp"+(erpFormId!=null && erpFormId!='' ? '&erpFormId='+erpFormId : ''));
	} else if(id=='xmlBody'){
		var erpFormId = $("#bodyHtmlArea #erpFormId").val();
		dialog.open("listErpFormDialog", '<u:msg titleId="ap.btn.xmlToHtml" alt="XML to HTML" />', "./listErpFormPop.do?menuId=${menuId}&erpFormTypCd=xmlFromErp"+(erpFormId!=null && erpFormId!='' ? '&erpFormId='+erpFormId : ''));
	} else if(id=='wfFormBody'){
		openWfFormList();
	} else if(id=='refDocBdoy' || id=='refVw' || id=='infm'){
		appendArea(id);
	}
}<%
// 해당 body 보이기/숨기기 %>
function toggleBodyArea(bodyId){
	var bodyIds = getBodyTypes(), $area;
	for(var i=1; i<bodyIds.length; i++){
		$area = $("#"+bodyIds[i]+"Area");
		if($area.length>0 && $area.parent().attr('id')!='docHiddenArea'){
			if(bodyId != bodyIds[i]){
				delComponent(bodyIds[i]);
			}
		} else {
			if(bodyId == bodyIds[i]){
				$("#docArea").append($area);
			}
		}
	}
}
<%
// 해당 컴포넌트 지우기 %>
function checkAndDel(ids){
	for(var i=0; i<ids.length; i++){
		var $area = $("#"+ids[i]+"Area");
		if($area.length>0 && $area.parent().attr('id')!='docHiddenArea'){
			delComponent(ids[i]);
		}
	}
}<%
// 해당 영역 보이기 %>
function appendArea(areaId){
	var $area = $("#"+areaId+"Area");
	if($area.parent().attr('id')=='docHiddenArea'){
		$("#docArea").append($area);
	}
}<%
// 폼 양식 세팅 - 결재 생성, ERP 통보 XML 생성용 양식 %>
function setXmlFromApData(erpFormId, erpFormTypCd){
	dialog.close("listErpFormDialog");
	var isEdit = erpFormTypCd!=null && erpFormTypCd.indexOf('Edit')>0;
	var html = callHtml("./getErpFormHtmlAjx.do?menuId=${menuId}", {erpFormId:erpFormId, formBodyMode:isEdit ? 'edit' : 'reg'});
	if(html!=null && html!=''){
		var areaId = isEdit ? "formEditBody" : "formBody";
		toggleBodyArea(areaId);
		var $area = $("#"+areaId+"Area"), $dataArea = $area.find("#"+areaId+"DataArea");
		$dataArea.html('');
		$dataArea.append("<input type='hidden' name='erpFormId' value='"+erpFormId+"'/>");
		$dataArea.append("<input type='hidden' name='erpFormTypCd' value='"+erpFormTypCd+"'/>");
		
		var $formBodyHtmlArea = $area.find('#'+areaId+'HtmlArea');
		$formBodyHtmlArea.html(html);
		if(isEdit){
			if($formBodyHtmlArea.find("#hiddenTr").length>0){
				$formBodyHtmlArea.find("tr").not("#hiddenTr").find("input, textarea, select, button").uniform();
			} else {
				setUniformCSS($formBodyHtmlArea);
			}
		} else {
			setUniformCSS($formBodyHtmlArea);
		}
		if(noBothBody){
			var $bodyHtmlArea = $("#bodyHtmlArea");
			if($bodyHtmlArea.length>0 && $bodyHtmlArea.is(":visible")){
				delComponent('bodyHtml','0');
			}
		}
		
		$("#bodyHtmlArea #bodyHtmlDataArea").html('');
	}
}<%
//폼 양식 세팅 -ERP XML to HTML 변환용 양식 %>
function setXmlFromErpData(erpFormId){
	var $dataArea = $("#bodyHtmlArea #bodyHtmlDataArea");
	$dataArea.html('');
	$dataArea.append("<input type='hidden' name='erpFormId' value='"+erpFormId+"'/>");
	$dataArea.append("<input type='hidden' name='erpFormTypCd' value='xmlFromErp'/>");
	dialog.close("listErpFormDialog");
}<%
// [업무관리] 양식 열기 %>
var wfFormNo="${wfFormNo}";
function openWfFormList(){
	var url='./findFormPop.do?menuId=${menuId}&mdTypCd=AP&callback=setWfForm'+(wfFormNo!='' ? '&formNo='+wfFormNo : '');
	parent.dialog.open('findFormDialog','<u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" />', url);
}<%
// [업무관리] 양식 열기 - callback %>
function setWfForm(data){
	
	var html = callHtml("./getWfFormHtmlAjx.do?menuId=${menuId}", {formId:"${param.formId}", wfFormNo:data.formNo, wfGenId:data.genId, wfMode:"edit"});
 	if(html!=null && html!=''){
		
		var areaId = "wfFormBody";
		toggleBodyArea(areaId);
		
		var $area = $("#"+areaId+"Area"), $dataArea = $area.find("#"+areaId+"DataArea");
		['setFormEdit','erpFormTypCd','wfFormNo','wfGenId','wfRescId'].each(function(idx, obj){
			$dataArea.find("[name='"+obj+"']").remove();
		});
		$dataArea.append("<input type='hidden' name='erpFormId' value='${apFormBVo.erpFormId}'/>");
		$dataArea.append("<input type='hidden' name='erpFormTypCd' value='wfForm'/>");
		$dataArea.append("<input type='hidden' name='wfFormNo' value='"+data.formNo+"'/>");
		$dataArea.append("<input type='hidden' name='wfGenId' value='"+data.genId+"'/>");
		$dataArea.append("<input type='hidden' name='wfRescId' value='"+data.rescId+"'/>");
		wfFormNo = data.formNo;
		
		var $formBodyHtmlArea = $area.find('#'+areaId+'HtmlArea');
		$formBodyHtmlArea.html(html);
		$formBodyHtmlArea.find("input, textarea, select, button").uniform();
		if(noBothBody){
			var $bodyHtmlArea = $("#bodyHtmlArea");
			if($bodyHtmlArea.length>0 && $bodyHtmlArea.is(":visible")){
				delComponent('bodyHtml','0');
			}
		}
		$("#bodyHtmlArea #bodyHtmlDataArea").html('');
	}
	
}<%
// 구성요소 삭제 열기 - 삭제 버튼 %>
function delComponent(id, seq){
	if(id == 'bodyHtml'){
		$("#"+id+"Area").hide();
		editor("bodyHtml").setInitHtml("<p><br /></p>");
	} else if(seq==null || seq==''){<%// docHiddenArea 로 해당 컴포넌트를 옮김 %>
		var $area = $("#"+id+"Area");
		$area.find("#"+id+"DataArea").html("");
		$("#docHiddenArea").append($area);
		if(id == 'wfFormBody') wfFormNo = '';
	} else if(seq=='0' && id!='items'){<%// 해당 컴포넌트 제거 - 결재 라인의 경우 %>
		$("#"+id+"Area").remove();<%// 2번 필요 - 결재선 %>
		$("#"+id+"Area").remove();
	} else {<%// 특정 시퀀스를 지움 - 항목지정의 경우 %>
		$("#"+id+"Area[data-seq='"+seq+"']").remove();
	}
}<%
// 머리글 적용 - setDocHeaderPop.jsp 에서 호출 %>
function setHeaderData(param){
	var areaId = "header";
	var $area = $("#"+areaId+"Area"), $dataArea = $area.find("#"+areaId+"DataArea");
	var oldParam = new ParamMap().getData($dataArea[0]);
	$dataArea.html("");
	setTxtFormData(param, areaId, ["docHeader","docName"], $area, $dataArea, oldParam);
	setImgFormData(param, areaId, ["docHeaderImg","docLogo","docSymbol"], $area, $dataArea, oldParam);
	if($area.parent().attr('id')=='docHiddenArea'){
		var insLoc = getInsertLoc();
		if(insLoc != null){
			$area.insertBefore(insLoc);
		} else {
			$("#docArea").append($area);
		}
	}
	if($area.find("#docLogoViewArea").html()=='' && $area.find("#docNameViewArea").html()=='' && $area.find("#docSymbolViewArea").html()==''){
		$area.find("#docLogoViewArea").hide();
		$area.find("#docNameViewArea").hide();
		$area.find("#docSymbolViewArea").hide();
	} else {
		$area.find("#docLogoViewArea").show();
		$area.find("#docNameViewArea").show();
		$area.find("#docSymbolViewArea").show();
	}
}<%
// 발신명의 적용 - setDocSenderPop.jsp 에서 호출 %>
function setSenderData(param){
	var areaId = "sender";
	var $area = $("#"+areaId+"Area"), $dataArea = $area.find("#"+areaId+"DataArea");
	$dataArea.html("");
	setTxtFormData(param, areaId, ["docSender","docReceiver"], $area, $dataArea, null);
	if($area.parent().attr('id')=='docHiddenArea'){
		$("#docArea").append($area);
	}
}<%
// 바닥글 적용 - setDocFooterPop.jsp 에서 호출 %>
function setFooterData(param){
	var areaId = "footer";
	var $area = $("#"+areaId+"Area"), $dataArea = $area.find("#"+areaId+"DataArea");
	$dataArea.html("");
	setTxtFormData(param, areaId, ["docFooter"], $area, $dataArea, null);
	if($area.parent().attr('id')=='docHiddenArea'){
		$("#docArea").append($area);
	}
}<%
// 이미지 기반 폼 설정을 UI 적용하고 hidden 테그를 생성함 %>
function setImgFormData(param, areaId, imgArray, $area, $dataArea, oldParam){
	var hiddens = ["imgPath","imgWdth","imgHght"];
	var imgPath, attrNm, $viewArea, dataParam=null, hasImage=false;
	imgArray.each(function(index, name){
		$viewArea = $area.find("#"+name+"ViewArea");
		if(param.get(name+"-useYn")=='Y'){
			imgPath = param.get(name+"-imgPath");
			if(imgPath!=null && imgPath!=''){
				dataParam = param;
			} else {
				imgPath = oldParam.get(name+"-imgPath");
				if(imgPath!=null && imgPath!=''){
					dataParam = oldParam;
				}
			}
			if(imgPath!=null && imgPath!=''){
				hasImage = true;
				if(name=='docHeaderImg'){
					$viewArea.html("<img width='"+dataParam.get(name+"-imgWdth")+"' height='"+dataParam.get(name+"-imgHght")+"' src='"+dataParam.get(name+"-imgPath")+"'/>");
				} else {
					$viewArea.html("<img height='75' src='"+dataParam.get(name+"-imgPath")+"'/>");
				}
				$dataArea.append("<input type='hidden' name='"+name+"-useYn' value='Y'/>");
				hiddens.each(function(index, hidden){
					attrNm = name+"-"+hidden;
					$dataArea.append("<input type='hidden' name='"+attrNm+"' value='"+dataParam.get(attrNm)+"'/>");
				});
			}
		} else {
			$viewArea.html('');
			$dataArea.append("<input type='hidden' name='"+name+"-useYn' value='N'/>");
		}
	});
	return hasImage;
}<%
// 텍스트 기반 폼 설정을 UI 적용하고 hidden 테그를 생성함 %>
function setTxtFormData(param, areaId, txtArray, $area, $dataArea, oldParam){
	var hiddens = ["txtCont","txtFontVa","txtStylVa","txtSize","txtColrVa"];
	var txtStylVa, attrNm, $viewArea, txtVa, hasText = false;
	txtArray.each(function(index, name){
		$viewArea = $area.find("#"+name+"ViewArea");
		
		txtVa = param.get(name+"-txtCont");
		if(txtVa!=null) txtVa = txtVa.trim();
		if(param.get(name+"-useYn")=='Y' && txtVa!=''){
			hasText = true;
			
			$viewArea.html(txtVa);
			$viewArea.css("font-family", param.get(name+"-txtFontVa"));
			$viewArea.css("font-size", param.get(name+"-txtSize"));
			$viewArea.css("color", param.get(name+"-txtColrVa"));
			txtStylVa = param.get(name+"-txtStylVa");
			$viewArea.css("font-weight", txtStylVa.indexOf('bold')>=0 ? 'bold' : '');
			$viewArea.css("font-style", txtStylVa.indexOf('italic')>=0 ? 'italic' : '');
			$viewArea.css("text-decoration", txtStylVa.indexOf('underline')>=0 ? 'underline' : '');
			$dataArea.append("<input type='hidden' name='"+name+"-useYn' value='Y'/>");
			hiddens.each(function(index, hidden){
				attrNm = name+"-"+hidden;
				$dataArea.append("<input type='hidden' name='"+attrNm+"' value='"+param.get(attrNm)+"'/>");
			});
		} else {
			$viewArea.html('');
			$dataArea.append("<input type='hidden' name='"+name+"-useYn' value='N'/>");
		}
	});
	return hasText;
}<%
// 결재라인 적용 - setDocApvLnPop.jsp, setDocApvLn1LnAgrPop.jsp, setDocApvLn2LnAgrPop.jsp 에서 호출
// formApvLnTypCd : 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재
// formApvLnTypCd - 추가 : apvLnList:리스트, apvLnOneTopList:최종결재+리스트, apvLnMultiTopList:서명+리스트
// apvLnTitlTypCd : 결재라인타이틀구분코드 - apv:결재, agr:합의, req:신청, prc:처리
%>
function setApvLnData(param, formApvLnTypCd){<%
	// 양식결재선 - 설정 데이터 %>
	var apvLnGrpData = getFromApvLnData();<%
	// apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄), apvLnDbl:이중결재
	// - 1줄에 표시 %>
	if(formApvLnTypCd=='apvLn' || formApvLnTypCd=='apvLnMixd' || formApvLnTypCd=='apvLn1LnAgr' || formApvLnTypCd=='apvLnDbl' || formApvLnTypCd=='apvLnWrtn'){
		var $apvLnArea = setApvLineArea();
		if(formApvLnTypCd=='apvLn' || formApvLnTypCd=='apvLnMixd'){<%// apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸) %>
			applyApvLn(param, $apvLnArea, formApvLnTypCd, ["apv"]);<%// 결재라인타이틀구분코드 - apv:결재 %>
		} else if(formApvLnTypCd=='apvLn1LnAgr'){<%// apvLn1LnAgr:결재+합의(1줄) %>
			applyApvLn(param, $apvLnArea, formApvLnTypCd, ["apv", "agr"]);
		} else if(formApvLnTypCd=='apvLnDbl' || formApvLnTypCd=='apvLnWrtn'){<%// apvLnDbl:이중결재 %>
			applyApvLn(param, $apvLnArea, formApvLnTypCd, ["req", "prc"]);
		}<%
	// apvLn2LnAgr:결재+합의(2줄)
	// - 2줄에 표시 %>
	} else if(formApvLnTypCd=='apvLn2LnAgr'){<%// apvLn2LnAgr:결재+합의(2줄)%>
		var $doc = $("#docArea"), $bodyHtml = $("#bodyHtmlArea");
		var $apvLnForDel = $doc.children("#apvLnArea");<%// 기존영역 - 기존영역 삭제용 %>
		var html = $("#apvLnHiddenArea #apvLineArea")[0].outerHTML;<%// 신규 삽입 html %>
		
		var $apvLnArea1 = $doc.children("#apvLnArea[data-seq='1']");<%// 기존영역 %>
		if($apvLnArea1.length==0) $apvLnArea1 = $doc.children("#apvLnArea:first");

		var $apvLnArea2 = $doc.children("#apvLnArea[data-seq='2']");<%// 기존영역 %>
		if($apvLnArea2.length==0) $apvLnArea2 = $doc.children("#apvLnArea:first");
		<%// 신규1 insert %>
		if($apvLnArea1.length==0){
			$(html).insertBefore($bodyHtml);
			$apvLnArea1 = $bodyHtml.prev();
		} else {
			$(html).insertBefore($apvLnArea1);
			$apvLnArea1 = $apvLnArea1.prev();
		}
		$apvLnArea1.attr("data-seq", "1");
		<%// 신규2 insert %>
		if($apvLnArea2.length==0){
			$(html).insertBefore($bodyHtml);
			$apvLnArea2 = $bodyHtml.prev();
		} else {
			$(html).insertBefore($apvLnArea2);
			$apvLnArea2 = $apvLnArea2.prev();
		}
		$apvLnArea2.attr("data-seq", "2");
		<%// 기존 영역 삭제 %>
		if($apvLnForDel.length>0) $apvLnForDel.remove();
		
		applyApvLn(param, $apvLnArea1, formApvLnTypCd, ["apv"], ["apv","agr"]);<%// 결재라인타이틀구분코드 - apv:결재 %>
		applyApvLn(param, $apvLnArea2, formApvLnTypCd, ["agr"], ["apv","agr"]);<%// 결재라인타이틀구분코드 - agr:합의 %>
	} else if(formApvLnTypCd=='apvLnList'){<%// apvLnList:리스트 %>
		var $apvLnArea = setApvLineArea();
		$apvLnArea.attr("id", "apvLnArea");
		$apvLnArea.find('#setupBtn').remove();
		var html = $("#apvLnHiddenArea #apvLnListArea")[0].outerHTML;<%// 결재자 목록 신규 삽입 html %>
		var $blank = $apvLnArea.find('#bottomBlank');
		$(html).insertBefore($blank);
	} else if(formApvLnTypCd=='apvLnDblList'){<%// apvLnDblList=리스트(이중결재) %>
		var $apvLnArea = setApvLineArea();
		$apvLnArea.attr("id", "apvLnArea");
		$apvLnArea.find('#setupBtn').remove();
		var html = $("#apvLnHiddenArea #apvLnListArea")[0].outerHTML;<%// 결재자 목록 신규 삽입 html %>
		var $blank = $apvLnArea.find('#bottomBlank');
		$(html).insertBefore($blank);
		
		$('<div class="blank"></div>').insertBefore($blank);
		$(html).insertBefore($blank);
		
	} else if(formApvLnTypCd=='apvLnMultiTopList'){<%// apvLnMultiTopList:서명+리스트 %>
		var $apvLnArea = setApvLineArea();
		$apvLnArea.attr("id", "apvLnArea");
		applyApvLn(param, $apvLnArea, formApvLnTypCd, ["apv"]);<%// 결재라인타이틀구분코드 - apv:결재 %>
		var html = $("#apvLnHiddenArea #apvLnListArea")[0].outerHTML;<%// 결재자 목록 신규 삽입 html %>
		var $blank = $apvLnArea.find('#bottomBlank');
		$('<div class="blank"></div>').insertBefore($blank);
		$(html).insertBefore($blank);
	} else if(formApvLnTypCd=='apvLnOneTopList'){<%// apvLnOneTopList:최종결재+리스트 %>
		var $apvLnArea = setApvLineArea();
		$apvLnArea.attr("id", "apvLnArea");
		<%// 양식 삽입 : 항목지정 - 최종결재자 - 지시사항 %>
		var $blank = $apvLnArea.find('#bottomBlank');
		var html = $("#apvLnHiddenArea #oneTopArea")[0].outerHTML;
		$(html).insertBefore($blank);
		$('<div class="blank"></div>').insertBefore($blank);
		<%// 양식의 항목명 체우기 %>
		$apvLnArea.find("#oneTopArea table:first tr").find("td:first").each(function(index){
			var va = param.get((index+1)+"-1-nm");
			$(this).text(va==null ? '' : va);
		});
		<%// 결재자 목록 - html 삽입 %>
		var html = $("#apvLnHiddenArea #apvLnListArea")[0].outerHTML;
		$(html).insertBefore($apvLnArea.find('#bottomBlank'));
		<%// hidden 삽입 %>
		var $dataArea = $apvLnArea.find("#apvLnDataArea");
		$dataArea.html("");
		param.each(function(key, value){
			if(key.endsWith("-nm")){
				<%//do nothing%>
			} else if(["rowCnt","colCnt"].contains(key) || key.endsWith("-1")){
				$dataArea.append("<input type='hidden' name='items-99999-"+key+"' value='"+escapeValue(value)+"' />");
			} else {
				$dataArea.append("<input type='hidden' name='"+key+"' value='"+escapeValue(value)+"' />");
			}
		});
	} else if(formApvLnTypCd=='apvLnOnecard'){<%// apvLnOnecard: One Card %>
		var $apvLnArea = setApvLineArea();
		applyApvLn(param, $apvLnArea, formApvLnTypCd, ["apv"]);<%// 결재라인타이틀구분코드 - apv:결재 %>
	}
	setFromApvLnData(apvLnGrpData);
	$("#formApvLnTypCd").val(formApvLnTypCd);
}<%
// 결재자 도장방 영역 HTML 삽입 %>
function setApvLineArea(){
	var $apvLnArea = $("#docArea").children("#apvLnArea");<%// 기존영역 %>
	var html = $("#apvLnHiddenArea").children("#apvLineArea")[0].outerHTML;<%// 신규 삽입 html %>
	if($apvLnArea.length>0){
		$(html).insertBefore($apvLnArea[0]);<%// 신규 영역 insert %>
		$apvLnArea.remove();<%// 기존영역 삭제 %>
	} else {<%// 신규 영역 insert %>
		var insLoc = getInsertLoc();
		if(insLoc != null){
			$(html).insertBefore(insLoc);
		} else {
			$("#docArea").append(html);
		}
	}
	return $("#docArea #apvLineArea");
}<%
// 결재라인 적용 - 결재방 타이틀 및 칸수에 맞게 결재방 표시, hidden tag 생성
// formApvLnTypCd : 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸),
//		apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
//		apvLnDbl=신청+처리(이중결재), apvLnWrtn=신청+처리(서면결재)
// apvLnTitlTypCd : 결재라인타이틀구분코드 - apv:결재, agr:합의, req:신청, prc:처리
%>
function applyApvLn(param, $area, formApvLnTypCd, apvLnTitlTypCds, apvLnTitlTypCdsFor2Lines){
	if(formApvLnTypCd.startsWith("apvLn")){
		$area.attr("id", "apvLnArea");<%// id-attr 변경 %>
	} else {
		$area.attr("id", formApvLnTypCd+"Area");<%// id-attr 변경 %>
	}
	$area.find(".title_s").text(callMsg("ap.cmpt.apvLine."+formApvLnTypCd));<%// 표시 title 변경 %>
	
	var maxCntForWdth = 0, maxCnt;
	if(apvLnTitlTypCdsFor2Lines != null){
		apvLnTitlTypCdsFor2Lines.each(function(index, apvLnTitlTypCd){
			maxCnt = param.get(apvLnTitlTypCd+"-maxCnt");
			maxCnt = (maxCnt==null || maxCnt=='') ? 1 : parseInt(maxCnt, 10);
			if(maxCntForWdth < maxCnt) maxCntForWdth = maxCnt;
		});
	} else {
		apvLnTitlTypCds.each(function(index, apvLnTitlTypCd){
			maxCnt = param.get(apvLnTitlTypCd+"-maxCnt");
			maxCnt = (maxCnt==null || maxCnt=='') ? 1 : parseInt(maxCnt, 10);
			maxCntForWdth += maxCnt;
		});
	}
	
	if(formApvLnTypCd=='apvLnOnecard'){
		var $blank = $area.find("#bottomBlank");
		var img = $('<div><img src="/images/etc/DYNE.png" style="float:left; width:216px; height:72px" /></div>');
		img.insertBefore($blank);
	}
	
	apvLnTitlTypCds.each(function(index, apvLnTitlTypCd){
		var apvLnDispTypCd = param.get(apvLnTitlTypCd+"-apvLnDispTypCd");<%// 3row, 2row, 1row %>
		var $blank = $area.find("#bottomBlank");
		$($("#apvLnHiddenArea #"+apvLnDispTypCd)[0].outerHTML).insertBefore($blank);<%// 해당 타입에 맞는 html 삽입 %>
		var i, $signArea = $blank.prev();<%// $signArea : 결재방 %>
		$signArea.find("table").css("float", param.get(apvLnTitlTypCd+"-alnVa"));<%// 기준위치 - 좌,우 정렬 %>
		if("N" == param.get(apvLnTitlTypCd+"-bordUseYn")){<%// 테두리선 - 사용안함 처리 %>
			$signArea.find("table").css("background", "#ffffff");
		}
		if("N" == param.get(apvLnTitlTypCd+"-titlUseYn")){<%// 타이틀 - 사용안함 처리 %>
			$signArea.find("tr:first td:first").remove();
		} else if(apvLnTitlTypCd != 'apv'){
			var titleHtml = callTerm("ap.term."+apvLnTitlTypCd);
			if(apvLnDispTypCd!='1row') titleHtml = titleHtml.split("").join("<br/>");
			$signArea.find("tr:first td:first").html(titleHtml);<%// 결재 / 합의 or 신청 / 처리 %>
		}
		<%// 결재방 갯수 늘리기 %>
		var html, maxCnt = param.get(apvLnTitlTypCd+"-maxCnt");
		var addCnt = (maxCnt==null || maxCnt=='') ? 0 : parseInt(param.get(apvLnTitlTypCd+"-maxCnt"), 10) - 1;
		if(addCnt>0 || apvLnDispTypCd=='3row'){
			if(apvLnDispTypCd=='3row'){
				var wdthCnt = apvLnTitlTypCds.length>1 ? maxCntForWdth+1 : maxCntForWdth;
				var signAreaWith = wdthCnt>12 ? 53 : wdthCnt>10 ? 63 : wdthCnt>8 ? 77 : 95;
				var dtString = maxCntForWdth>12 ? '<u:msg titleId="ap.cmpt.signArea.sampleShortDate" alt="12-25" />' : maxCntForWdth>8 ? '<u:msg titleId="ap.cmpt.signArea.sampleMediumDate" alt="10-12-25" />' : '<u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" />';
				
				$signArea.find(".approval_body, .approval_img").each(function(index){
					$(this).css('width', signAreaWith+'px');
					<%// 서면결재 - 처리 - 빈칸만 표시(내용지움) %>
					if(formApvLnTypCd=='apvLnWrtn' && apvLnTitlTypCd=='prc'){
						$(this).html('');
					} else {
						if(index==2){
							$(this).text(dtString);
						}
					}
					html = this.outerHTML;
					for(i=0;i<addCnt;i++){
						$(html).insertAfter(this);
					}
				});
			} else if(apvLnDispTypCd=='2row'){
				var htmls = [];
				$signArea.find(".approval_body").each(function(index){
					if(index==0) htmls.push(this.outerHTML);
					else if(index==1) htmls[0] = htmls[0]+this.outerHTML;
					else if(index==2) htmls.push(this.outerHTML);
				});
				$signArea.find("tr").each(function(index){
					for(i=0;i<addCnt;i++){
						$(this).append(htmls[index]);
					}
				});
			} else if(apvLnDispTypCd=='1row'){
				var htmls = [];
				$signArea.find(".approval_body").each(function(index){
					htmls.push(this.outerHTML);
				});
				html = htmls.join("");
				$signArea.find("tr").each(function(index){
					for(i=0;i<addCnt;i++){
						$(this).append(html);
					}
				});
			}
		}
	});
	<%// hidden 삽입 %>
	var $dataArea = $area.find("#apvLnDataArea");
	$dataArea.html("");
	apvLnTitlTypCds.each(function(index, apvLnTitlTypCd){
		param.each(function(key, value){
			if(key.startsWith(apvLnTitlTypCd)){
				$dataArea.append("<input type='hidden' name='"+key+"' value='"+escapeValue(value)+"' />");
			}
		});
	});
}<%

// 항목지정 - 새로운 항목 삽입용 시퀀스 %>
var gMaxItemSeq = "${maxItemSeq}" == "" ? 0 : parseInt("${maxItemSeq}", 10);<%

// 항목지정 적용 - setDocItemsPop.jsp 에서 호출 %>
function setItemsData(param, seq){
	if(seq==null || seq=='') seq = ++gMaxItemSeq;
	var $itemsArea = $("#docArea").children("#itemsArea[data-seq='"+seq+"']");<%// 기존영역 %>
	var html = $("#itemsHiddenArea #itemsArea")[0].outerHTML;<%// 신규 삽입 html %>
	if($itemsArea.length>0){
		$(html).insertBefore($itemsArea[0]);<%// 신규 영역 insert %>
		var $prev = $itemsArea.prev();
		$itemsArea.remove();<%// 기존영역 삭제 %>
		$itemsArea = $prev;<%// $itemsArea 에 신규 삽입된 영역 할당 %>
	} else {
		var insLoc = getInsertLoc();
		if(insLoc != null){
			$(html).insertBefore(insLoc);
			$itemsArea = insLoc.prev();<%// $itemsArea 에 신규 삽입된 영역 할당 %>
		} else {
			$("#docArea").append(html);
			$itemsArea = $("#docArea").children("[id='itemsArea']:last");
		}
	}
	$itemsArea.attr("data-seq", seq);<%// seq attr 설정 %>
	$itemsArea.find("a#delBtn").attr("onclick","delComponent('items', '"+seq+"')");<%// 삭제 버튼의 seq 적용 %>
	
	var $itemTables = $itemsArea.find("#itemsViewArea table:first");
	$itemTables.css('table-layout', '');
	<%// colgroup 설정 %>
	var colCnt = parseInt(param.get("colCnt"), 10), $colgroup = $itemsArea.find("#itemsViewArea colgroup");
	if(colCnt==1) $colgroup.html('<col width="13%" /><col width="87%" />');
	if(colCnt==2) $colgroup.html('<col width="13%" /><col width="37%" /><col width="13%" /><col width="35%" />');
	if(colCnt==3) $colgroup.html('<col width="13%" /><col width="20%" /><col width="13%" /><col width="21%" /><col width="13%" /><col width="20%" />');
	<%// view 설정 %>
	var $tbody = $itemsArea.find("#itemsViewArea tbody");
	var row = '', keys, vals, $tr=null, arr;
	param.each(function(key, va){
		if(key.endsWith("-nm")){
			keys = key.split('-');
			vals = va.split('-');
			vals[1] = vals[1]=='1' ? '' : ' colspan="'+((parseInt(vals[1])*2)-1)+'"';
			if(row!=keys[0]){
				row = keys[0];
				$tbody.append("<tr></tr>");
				$tr = $tbody.find("tr:last");
			}
			arr = [];
			arr.append('<td class="head_lt">').append(vals[0]).append('</td>');
			arr.append('<td class="body_lt"').append(vals[1]).append('>&nbsp;</td>');
			$tr.append(arr.join(''));
		}
	});
	$itemTables.css('table-layout', 'fixed');
	<%
	// hidden 설정%>
	var $dataArea = $itemsArea.find("#itemsDataArea");
	param.each(function(key, va){
		if(!key.endsWith("-nm")){
			$dataArea.append("<input type='hidden' name='items-"+seq+"-"+key+"' value='"+escapeValue(va)+"' />");
		}
	});
}<%
// 저장버튼 클릭 %>
function saveForm(){
	var $form = $("#docEditForm");
	setFormCombIds($form);<%--// 구성요소 순서 세팅 --%>
	$form.find("#maxItemSeq").val(gMaxItemSeq);<%--// 항목지정 시퀀스 최대값 --%>
	editor('bodyHtml').prepare();<%--// editor 준비 --%>
	if($("#wfFormBodyArea").is(':visible')){
		if(setSaveData($form) == false) return;<%--// 업무관리 - 양식 데이터 말기 --%>
	}
	$form.attr("method", "post");
	$form.attr("action", "./transForm.do");
	$form.attr('target','dataframe');
	$form.submit();
}<%
// submit을 위한 - 구성요소 순서 세팅 - 머리말 - 결재방 .... 등의 %>
function setFormCombIds($form){
	var id, seq, ids = [];
	$form.find("#docArea").children("div").each(function(){
		id = $(this).attr('id');
		if(id.endsWith("Area")){
			id = id.substring(0, id.length-4);
			if(id=='apvLn' || id=='items'){
				seq = $(this).attr('data-seq');
				if(seq==null || seq=='') ids.push(id);
				else ids.push(id+":"+seq);
			} else if(id!='bodyHtmlView'){
				if(id=='bodyHtml'){
					if($(this).is(':visible')){
						ids.push(id);
					}
				} else {
					ids.push(id);
				}
			}
			if(id=='formEditBody'){
				if(window.checkFormBodyXML) checkFormBodyXML();
				setFormEditBodyXML();
			}
		}
	});
	$form.find("#formCombIds").val(ids.join(","));
}<%
// [버튼] 양식 결재선 %>
function setFromApvLn(){
	dialog.open("setFromApvLnDialog", '<u:msg titleId="ap.cfg.fixdApvLn" alt="양식 결재선" />', "./listApvLnGrpPop.do?menuId=${menuId}");
}<%
// [버튼] 양식 참조열람 %>
function setFromRefVw(){
	dialog.open("setFromRefVwDialog", '<u:msg titleId="ap.cfg.fixdRefVw" alt="양식 참조열람" />', "./listRefVwGrpPop.do?menuId=${menuId}");
}<%
// 양식 결재선 - 결과 세팅 %>
function setFromApvLnData(apvLnGrpData){
	$("#docArea").children("[id=apvLnArea]").each(function(){
		var seq = $(this).attr('data-seq');
		if(seq==null || seq==1){
			var $area = $(this).find("#apvLnDataArea");
			if(apvLnGrpData.apvLnGrpId != null){
				$area.find("input[name='apvLnGrpId'], input[name='fixdApvrYn'], input[name='autoApvLnCd']").remove();
				if(apvLnGrpData.apvLnGrpId!=''){
					$area.append("<input type='hidden' name='apvLnGrpId' value='"+apvLnGrpData.apvLnGrpId+"' />");
					$area.append("<input type='hidden' name='fixdApvrYn' value='"+apvLnGrpData.fixdApvrYn+"' />");
				} else {
					$area.append("<input type='hidden' name='apvLnGrpId' value='' />");
					$area.append("<input type='hidden' name='fixdApvrYn' value='' />");
				}
				if(apvLnGrpData.autoApvLnCd!=null && apvLnGrpData.autoApvLnCd!=''){
					$area.append("<input type='hidden' name='autoApvLnCd' value='"+apvLnGrpData.autoApvLnCd+"' />");
				} else {
					$area.append("<input type='hidden' name='autoApvLnCd' value='' />");
				}
			} else if(apvLnGrpData.refVwGrpId != null){
				$area.find("input[name='refVwGrpId'], input[name='refVwFixdApvrYn']").remove();
				if(apvLnGrpData.refVwGrpId!=''){
					$area.append("<input type='hidden' name='refVwGrpId' value='"+apvLnGrpData.refVwGrpId+"' />");
					$area.append("<input type='hidden' name='refVwFixdApvrYn' value='"+apvLnGrpData.refVwFixdApvrYn+"' />");
				} else {
					$area.append("<input type='hidden' name='refVwGrpId' value='' />");
					$area.append("<input type='hidden' name='refVwFixdApvrYn' value='' />");
				}
			}
		}
	});
}<%
// 양식 결재선 - 세팅된 데이터 조회 %>
function getFromApvLnData(){
	var apvLnGrpData = {apvLnGrpId:null, fixdApvrYn:null, autoApvLnCd:null};
	$("#docArea").children("[id=apvLnArea]").each(function(){
		var seq = $(this).attr('data-seq');
		if(seq==null || seq==1){
			var $area = $(this).find("#apvLnDataArea");
			apvLnGrpData.apvLnGrpId = $area.find("input[name='apvLnGrpId']").val();
			apvLnGrpData.fixdApvrYn = $area.find("input[name='fixdApvrYn']").val();
			apvLnGrpData.autoApvLnCd = $area.find("input[name='autoApvLnCd']").val();
			apvLnGrpData.refVwGrpId = $area.find("input[name='refVwGrpId']").val();
			apvLnGrpData.refVwFixdApvrYn = $area.find("input[name='refVwFixdApvrYn']").val();
		}
	});
	return apvLnGrpData;
}<%
// 본문 (편집양식) - xml 저장 %>
function setFormEditBodyXML(){
	var $area = $("#formEditBodyDataArea");
	$area.find("input[name='formEditBodyXML']").remove();
	$area.append("<input type='hidden' name='formEditBodyXML' value=''/>");
	var $input = $area.find("input[name='formEditBodyXML']");
	$input.val(getFormBodyXML());
}<%
// 양식결재(formBodyXML) 관련 %>
function getFormBodyXML(){
	var xmlDoc = createXMLParser("<formBodyXML></formBodyXML>");
	var root = xmlDoc.getElementsByTagName("formBodyXML")[0];
	
	var head = xmlDoc.createElement("head");
	root.appendChild(head);
	
	var body = xmlDoc.createElement("body");
	root.appendChild(body);
	
	var $area = $("#xmlArea");
	$area.find("[id='xml-head'] :input").each(function(){
		$(head).attr($(this).attr('name'), $(this).val());
	});
	
	var ieVer8 = (browser.ie==true && browser.ver<9);
	
	var elemId, loopId=null, p;
	$area.find("[id='xml-body'] [id^='xml-']").each(function(){
		elemId = $(this).attr('id').substring(4);
		p = elemId.indexOf('/');
		if(p>0){
			loopId = elemId.substring(p+1);
			elemId = elemId.substring(0,p);
			
			var pElem = xmlDoc.createElement(elemId), elem;
			body.appendChild(pElem);
			
			$(this).find("[id='"+loopId+"']").each(function(){
				var elem = xmlDoc.createElement(loopId), tp, attNm, attVa;
				var valid = true;
				$(this).find(":input").each(function(){
					if(valid){
						tp = $(this).attr('type');
						tp = tp==null ? '' : tp.toLowerCase();
						if(tp=='radio'||tp=='checkbox'){
							if(!this.checked) return;
						}
						attNm = $(this).attr('name');
						attVa = $(this).val();
						if(attNm!=null && attNm.startsWith('erp')){
							if($(this).attr('data-validation')=='Y' && attVa==''){
								valid = false;
							} else {
								$(elem).attr($(this).attr('name'), attVa);
								if(this.tagName.toLowerCase()=='select'){
									$(elem).attr(attNm+'Nm', $(this).text());
								}
							}
						}
					}
				});
				if(valid){
					pElem.appendChild(elem);
				}
			});
		} else {
			var elem = xmlDoc.createElement(elemId);
			body.appendChild(elem);
			var tp, attNm, attVa;
			$(this).find(":input").each(function(){
				tp = $(this).attr('type');
				tp = tp==null ? '' : tp.toLowerCase();
				if(tp=='radio'||tp=='checkbox'){
					if(!this.checked) return;
				}
				attNm = $(this).attr('name');
				attVa = $(this).val();
				if(attNm!=null && attNm.startsWith('erp')){
					
					$(elem).attr($(this).attr('name'), attVa);
					if($(this).attr('data-validation')=='Y' && attVa==''){
						valid = false;
					} else {
						$(elem).attr($(this).attr('name'), attVa);
						if(this.tagName.toLowerCase()=='select'){
							$(elem).attr(attNm+'Nm', $(this).text());
						}
					}
				}
			});
		}
	});
	if(ieVer8){
		var xml = serializeXML(xmlDoc).trim();
		xml = xml.replaceAll("\r\n", "&#10;");
		xml = xml.replaceAll("'",    "&apos;");
		return xml;
	} else {
		return serializeXML(xmlDoc);
	}
}
function createXMLParser(xmlString){
	if(window.DOMParser){
		var parser = new DOMParser();
		return parser.parseFromString(xmlString, "text/xml");
	} else {
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(xmlString);
        return xmlDoc;
	}
}
function serializeXML(xmlDoc){
	if(window.XMLSerializer){
		var serializer = new XMLSerializer();
		return serializer.serializeToString(xmlDoc);
	} else {
		return xmlDoc.xml;
	}
}
$(document).ready(function() {
	setUniformCSS();
	<%// 양식 구성 요서 - 트리 그리기 %>
	var tree = TREE.create('componentTree');
	tree.onclick = 'applyComponent';
	tree.setRoot('ROOT', '<u:msg titleId="ap.jsp.setFormEdit.component" alt="양식 구성 요소" />');
	tree.setSkin("${_skin}");
	tree.openLvl = 2;
	<c:forEach items="${components}" var="item" varStatus="status"><c:if test="${not empty item}">
	tree.add("ROOT","${item}","<u:msg titleId='ap.cmpt.${item}' />","${item=='apvLine' or item=='body' ? 'F' : 'A'}","1","",{pid:"ROOT",id:"${item}",title:"${item}"});</c:if></c:forEach>
	<c:forEach items="${apvLns}" var="item" varStatus="status"><c:if test="${not empty item}">
	tree.add("apvLine","${item}","<u:msg titleId='ap.cmpt.apvLine.${item}' />","A","1","",{pid:"ROOT",id:"${item}",title:"${item}"});</c:if></c:forEach>
	<c:forEach items="${bodies}" var="item" varStatus="status"><c:if test="${not empty item}">
	tree.add("body","${item}","<u:msg titleId='ap.cmpt.${item}' />","A","1","",{pid:"ROOT",id:"${item}",title:"${item}"});</c:if></c:forEach>
	
	tree.draw();
});
//-->
</script>
<c:if test="${empty param.formId}"><u:title titleId="ap.jsp.setApvForm.regForm" alt="양식 등록" titleSuffix="${apFormBVo.rescNm}" /></c:if>
<c:if test="${not empty param.formId}"><u:title titleId="ap.jsp.setApvForm.modForm" alt="양식 수정" titleSuffix="${apFormBVo.rescNm}" /></c:if>

<u:buttonArea>
	<u:button href="javascript:saveForm()" titleId="cm.btn.save" alt="저장" auth="A" />
	<u:button titleId="ap.btn.print" alt="인쇄" onclick="setViewMode('print');printWeb();setViewMode('edit');" /><c:if
		test="${not browser.ie and not browser.chrome}">
	<u:button titleId="ap.btn.printView" alt="인쇄전환" onclick="setViewMode('print')" />
	<u:button titleId="ap.btn.editView" alt="편집전환" onclick="setViewMode('edit')" /></c:if>
	<u:button href="./setApvForm.do?menuId=${menuId}&formBxId=${param.formBxId}" titleId="cm.btn.cancel" alt="취소" />
</u:buttonArea>

<!-- LEFT -->
<div id="formFuncDiv" class="left" style="float:left; width:17%;">

<u:title titleId="ap.jsp.setFormEdit.component" type="small" alt="양식 구성 요소" />

<u:titleArea 
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
><div id="componentTree" class="tree"></div></u:titleArea>

</div>

<!-- RIGHT -->
<div id="formEditDiv" class="right" style="float:right; width:82%;">

<form id="docEditForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="formId" type="hidden" value="${param.formId}" />
<u:input id="formCombIds" type="hidden" value="" />
<u:input id="maxItemSeq" type="hidden" value="" />
<u:input id="formApvLnTypCd" type="hidden" value="${apFormBVo.formApvLnTypCd}" />
<div id="docArea" style="min-height:610px">

<u:cmt cmt="위아래 순서 조절된 순서대로 출력하기 위한 루프" />
<c:forEach items="${apFormCombDVoList}" var="apFormCombDVo"><c:if

	test="${apFormCombDVo.formCombId == 'header'}"

><c:if test="${not empty docName.txtCont or not empty docHeaderImg.imgPath or not empty docLogo.imgPath or not empty docSymbol.imgPath or not empty docHeader.txtCont}"><c:set var="headerArea" value="Y" />
<div id="headerArea">
<u:title titleId="ap.cmpt.header" type="small" alt="머리글" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('header')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('header')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title><u:set test="${empty docLogo.imgPath and empty docSymbol.imgPath and empty docName.txtCont}"
	var="heightDispStyle" value=" display:none;" />
<div>
	<div id="docHeaderViewArea" style="width:100%; text-align:center; color:${docHeader.txtColrVa}; font-family:${docHeader.txtFontVa}; font-size:${docHeader.txtSize}; ${docHeader.txtStylVa}"><u:out value="${docHeader.txtCont}" nullValue="" /></div>
	<div id="docHeaderImgViewArea" style="width:100%; text-align:center;"><c:if test="${not empty docHeaderImg.imgPath}"><img width="${docHeaderImg.imgWdth}" height="${docHeaderImg.imgHght}" src="${docHeaderImg.imgPath}"></c:if></div>
	<div id="docLogoViewArea" style="float:left; width:20%; height:75px;${heightDispStyle}"><c:if test="${not empty docLogo.imgPath}"><img height="75" src="${docLogo.imgPath}"></c:if></div>
	<div id="docNameViewArea" style="float:left; width:60%; height:75px;${heightDispStyle} text-align:center; margin-top:15px; color:${docName.txtColrVa}; font-family:${docName.txtFontVa}; font-size:${docName.txtSize}; ${docName.txtStylVa}"><u:out value="${docName.txtCont}" /></div>
	<div id="docSymbolViewArea" style="float:right; width:20%; height:75px;${heightDispStyle} text-align: right;"><c:if test="${not empty docSymbol.imgPath}"><img height="75" src="${docSymbol.imgPath}"></c:if></div>
</div>
<div class="blank"></div>
<div style="display:none" id="headerDataArea"><c:forEach
	items="${headerTxts}" var="item"><u:convert
	srcId="${item}" var="apFormTxtDVo"/>
	<input type="hidden" name="${item}-useYn" value="${not empty apFormTxtDVo.txtCont ? 'Y' : 'N'}" />
	<input type="hidden" name="${item}-txtCont" value="<u:out value="${apFormTxtDVo.txtCont}" type="value" />" />
	<input type="hidden" name="${item}-txtFontVa" value="<u:out value="${apFormTxtDVo.txtFontVa}" type="value" />" />
	<input type="hidden" name="${item}-txtStylVa" value="<u:out value="${apFormTxtDVo.txtStylVa}" type="value" />" />
	<input type="hidden" name="${item}-txtSize" value="<u:out value="${apFormTxtDVo.txtSize}" type="value" />" />
	<input type="hidden" name="${item}-txtColrVa" value="<u:out value="${apFormTxtDVo.txtColrVa}" type="value" />" /></c:forEach><c:forEach
	items="${headerImgs}" var="item"><u:convert
	srcId="${item}" var="apFormImgDVo"/>
	<input type="hidden" name="${item}-useYn" value="${not empty apFormImgDVo.imgPath ? 'Y' : 'N'}" />
	<input type="hidden" name="${item}-imgPath" value="<u:out value="${apFormImgDVo.imgPath}" type="value" />" />
	<input type="hidden" name="${item}-imgWdth" value="<u:out value="${apFormImgDVo.imgWdth}" type="value" />" />
	<input type="hidden" name="${item}-imgHght" value="<u:out value="${apFormImgDVo.imgHght}" type="value" />" /></c:forEach>
</div>
</div>
</c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'apvLn'}"

>
<div id="apvLnArea" data-seq="${apFormCombDVo.formCombSeq}">
<u:title titleId="ap.cmpt.apvLine.${apFormBVo.formApvLnTypCd}" type="small" alt="결재라인" notPrint="true" ><c:if
		test="${optConfigMap.fixdApvLn eq 'Y'}">
	<u:titleButton titleId="ap.cfg.fixdApvLn" id="fixdApvLnBtn" alt="양식 결재선" onclick="setFromApvLn()" auth="A"
	/></c:if><c:if
		test="${optConfigMap.fixdRefVw eq 'Y'}">
	<u:titleButton titleId="ap.cfg.fixdRefVw" id="fixdRefVwBtn" alt="양식 참조열람" onclick="setFromRefVw()" auth="A"
	/></c:if><c:if
		test="${apFormBVo.formApvLnTypCd != 'apvLnList' and apFormBVo.formApvLnTypCd != 'apvLnDblList'}"
	><u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent($('#formApvLnTypCd').val(), this)" auth="A"
	/></c:if><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('apvLn', '0')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title><c:if
		test="${apFormBVo.formApvLnTypCd == 'apvLnOnecard'}">
<div><img src="/images/etc/DYNE.png" style="float:left; width:216px; height:72px" /></div></c:if>
<u:convert
	srcId="apvLn${apFormCombDVo.formCombSeq}" var="apvLns"/><c:forEach
	items="${apvLns}" var="apvLnTitlTypCd"><u:convert
	srcId="${apvLnTitlTypCd}" var="apFormApvLnDVo"/><c:if
	
	test="${apFormApvLnDVo.apvLnDispTypCd == '3row'}"
><div id="3row"><c:set var="stmpWdthStyle" value="${
				apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
					? 'width:53px' : 
				apFormApvLnDVo.maxSum>10 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl')and apFormApvLnDVo.maxSum>9)
					? 'width:63px' : 
				apFormApvLnDVo.maxSum> 8 ? 'width:77px' : ''}" />
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_head" rowspan="3"><u:term termId="ap.term.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></td></c:if>
		<c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_body" style="${stmpWdthStyle}"><c:if test="${not (apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and apvLnTitlTypCd eq 'prc')}"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></c:if></td></c:forEach></tr>
	<tr><c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_img" style="${stmpWdthStyle}"><c:if test="${not (apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and apvLnTitlTypCd eq 'prc')}"><img src="${_ctx}/images/etc/etc_s.png"></c:if></td></c:forEach></tr>
	<tr><c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_body" style="${stmpWdthStyle}"><c:if test="${not (apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and apvLnTitlTypCd eq 'prc')}"><u:msg titleId="ap.cmpt.signArea.${
			apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
				? 'sampleShortDate' : 
			apFormApvLnDVo.maxSum>8 ? 'sampleMediumDate' : 'sampleDate'}" alt="2010-12-25 / 10-12-25 / 12-25" /></c:if></td></c:forEach></tr>
	</tbody></table>
</div></c:if><c:if

	test="${apFormApvLnDVo.apvLnDispTypCd == '2row'}"
><div id="2row">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_head" rowspan="2"><u:term termId="ap.term.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></td></c:if>
		<c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_body" rowspan="2"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td></c:forEach></tr>
	<tr><c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_body"><img height="20" src="${_ctx}/images/etc/etc_s.png"></td></c:forEach></tr>
	</tbody></table>
</div></c:if><c:if

	test="${apFormApvLnDVo.apvLnDispTypCd == '1row'}"
><div id="1row">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_headw"><u:term termId="ap.term.${apvLnTitlTypCd}" alt="결재" /></td></c:if>
		<c:forEach begin="1" end="${apFormApvLnDVo.maxCnt}" step="1"
		><td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td>
		<td class="approval_body"><img height="20" src="${_ctx}/images/etc/etc_s.png"></td></c:forEach></tr>
	</tbody></table>
</div></c:if>
</c:forEach>
<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnList'
	or apFormBVo.formApvLnTypCd == 'apvLnDblList'
	or apFormBVo.formApvLnTypCd == 'apvLnOneTopList'
	or apFormBVo.formApvLnTypCd == 'apvLnMultiTopList'}">
<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnOneTopList'}">
<u:listArea id="oneTopArea" colgroup="13%,20%,3%,31%,33%" noBottomBlank="true">
	<tr>
		<td class="head_lt"><c:if test="${not empty oneTopItems[0].itemId}"><u:msg titleId="ap.doc.${oneTopItems[0].itemId}" /></c:if></td>
		<td class="body_lt"></td>
		<td class="head_ct" rowspan="5"><u:msg titleId="ap.signArea.apv" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="head_ct"><u:msg titleId="ap.cmpt.signArea.sampleCEO" alt="대표이사" /></td>
		<td class="head_ct"><u:msg titleId="ap.cmpt.order" alt="지시사항" /></td>
	</tr>
	<tr>
		<td class="head_lt"><c:if test="${not empty oneTopItems[1].itemId}"><u:msg titleId="ap.doc.${oneTopItems[1].itemId}" /></c:if></td>
		<td class="body_lt"></td>
		<td class="body_ct" rowspan="3"><img src="${_ctx}/images/etc/etc_s.png"></td>
		<td class="body_ct" rowspan="5"></td>
	</tr>
	<tr>
		<td class="head_lt"><c:if test="${not empty oneTopItems[2].itemId}"><u:msg titleId="ap.doc.${oneTopItems[2].itemId}" /></c:if></td>
		<td class="body_lt"></td>
	</tr>
	<tr>
		<td class="head_lt"><c:if test="${not empty oneTopItems[3].itemId}"><u:msg titleId="ap.doc.${oneTopItems[3].itemId}" /></c:if></td>
		<td class="body_lt"></td>
	</tr>
	<tr>
		<td class="head_lt"><c:if test="${not empty oneTopItems[4].itemId}"><u:msg titleId="ap.doc.${oneTopItems[4].itemId}" /></c:if></td>
		<td class="body_lt"></td>
		<td class="body_ct"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td>
	</tr>
</u:listArea>
</c:if>

<u:set test="true" var="apvLnListInDoc" value="Y" /><u:set test="true" var="apvLnListForEdit" value="Y" />
<jsp:include page="../../box/viewApvLnInc.jsp" flush="false" />
<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnDblList'}">
<div class="blank"></div>
<jsp:include page="../../box/viewApvLnInc.jsp" flush="false" />
</c:if>
</c:if>

<div class="blank"></div>
<div style="display:none" id="apvLnDataArea"><u:convert
	srcId="apvLn${apFormCombDVo.formCombSeq}" var="apvLns"/><c:forEach
	items="${apvLns}" var="apvLnTitlTypCd"><u:convert
	srcId="${apvLnTitlTypCd}" var="apFormApvLnDVo"/><c:if
		test="${not empty apFormApvLnDVo.apvLnDispTypCd}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-apvLnDispTypCd" value="<u:out value="${apFormApvLnDVo.apvLnDispTypCd}" type="value" />" /></c:if><c:if
		test="${not empty apFormApvLnDVo.maxCnt}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-maxCnt" value="<u:out value="${apFormApvLnDVo.maxCnt}" type="value" />" /></c:if><c:if
		test="${not empty apFormApvLnDVo.alnVa}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-alnVa" value="<u:out value="${apFormApvLnDVo.alnVa}" type="value" />" /></c:if><c:if
		test="${not empty apFormApvLnDVo.bordUseYn}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-bordUseYn" value="<u:out value="${apFormApvLnDVo.bordUseYn}" type="value" />" /></c:if><c:if
		test="${not empty apFormApvLnDVo.titlUseYn}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-titlUseYn" value="<u:out value="${apFormApvLnDVo.titlUseYn}" type="value" />" /></c:if><c:if
		test="${not empty apFormApvLnDVo.lstDupDispYn}">
	<input type="hidden" name="${apFormApvLnDVo.apvLnTitlTypCd}-lstDupDispYn" value="<u:out value="${apFormApvLnDVo.lstDupDispYn}" type="value" />" /></c:if>
	
	<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnOneTopList'}">
	<input name="items-99999-rowCnt" type="hidden" value="5"/>
	<input name="items-99999-colCnt" type="hidden" value="1"/><c:if
		test="${not empty oneTopItems[0]}">
	<input name="items-99999-${oneTopItems[0].rowNo}-1" type="hidden" value="${oneTopItems[0].itemId}-1"></c:if><c:if
		test="${not empty oneTopItems[1]}">
	<input name="items-99999-${oneTopItems[1].rowNo}-1" type="hidden" value="${oneTopItems[1].itemId}-1"></c:if><c:if
		test="${not empty oneTopItems[2]}">
	<input name="items-99999-${oneTopItems[2].rowNo}-1" type="hidden" value="${oneTopItems[2].itemId}-1"></c:if><c:if
		test="${not empty oneTopItems[3]}">
	<input name="items-99999-${oneTopItems[3].rowNo}-1" type="hidden" value="${oneTopItems[3].itemId}-1"></c:if><c:if
		test="${not empty oneTopItems[4]}">
	<input name="items-99999-${oneTopItems[4].rowNo}-1" type="hidden" value="${oneTopItems[4].itemId}-1"></c:if>
	</c:if>
	</c:forEach><c:if
		
		test="${not empty apFormBVo.apvLnGrpId}">
	<input type="hidden" name="apvLnGrpId" value="<u:out value="${apFormBVo.apvLnGrpId}" type="value" />" /></c:if><c:if
		test="${not empty apFormBVo.fixdApvrYn}">
	<input type="hidden" name="fixdApvrYn" value="<u:out value="${apFormBVo.fixdApvrYn}" type="value" />" /></c:if><c:if
		test="${not empty apFormBVo.autoApvLnCd}">
	<input type="hidden" name="autoApvLnCd" value="<u:out value="${apFormBVo.autoApvLnCd}" type="value" />" /></c:if><c:if
		test="${not empty apFormBVo.refVwGrpId}">
	<input type="hidden" name="refVwGrpId" value="<u:out value="${apFormBVo.refVwGrpId}" type="value" />" /></c:if><c:if
		test="${not empty apFormBVo.refVwFixdApvrYn}">
	<input type="hidden" name="refVwFixdApvrYn" value="<u:out value="${apFormBVo.refVwFixdApvrYn}" type="value" />" /></c:if>
</div>
<div style="display:none" id="apvLnDataArea">
</div>
</div>
</c:if><c:if

	test="${apFormCombDVo.formCombId == 'items'}"

><u:convert srcId="items${apFormCombDVo.formCombSeq}" var="apFormItemDVo"
/><c:if test="${not empty apFormItemDVo}">
<div id="itemsArea" data-seq="${apFormCombDVo.formCombSeq}">
<u:title titleId="ap.cmpt.items" type="small" alt="항목지정" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('items', this)" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('items', '${apFormCombDVo.formCombSeq}')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:listArea id="itemsViewArea" colgroup="${ apFormItemDVo.colCnt=='1' ? '13%,87%' : apFormItemDVo.colCnt=='2' ? '13%,37%,13%,37%' : '13%,20%,13%,21%,13%,20%' }" tableStyle="table-layout:fixed;" noBottomBlank="true"
><u:set test="${true}" var="rowIndex" value="1" />
<c:forEach items="${apFormItemDVo.childList}" var="apFormItemLVo" varStatus="status"><u:set
	test="${status.first}" value="<tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" value="
</tr><tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" var="rowIndex" value="${apFormItemLVo.rowNo}" elseValue="${rowIndex}" />
	<td class="head_lt"><c:if
		test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td><td class="body_lt"<c:if
		test="${not empty apFormItemLVo.cspnVa and apFormItemLVo.cspnVa != '1'}"> colspan="${(apFormItemLVo.cspnVa * 2) - 1}"</c:if>>&nbsp;</td><u:set
	test="${status.last}" value="
</tr>" elseValue=""
	/></c:forEach>
</u:listArea>
<div class="blank"></div>
<div style="display:none" id="itemsDataArea">
	<input type="hidden" name="items-${apFormCombDVo.formCombSeq}-rowCnt" value="${apFormItemDVo.rowCnt}" />
	<input type="hidden" name="items-${apFormCombDVo.formCombSeq}-colCnt" value="${apFormItemDVo.colCnt}" /><c:forEach
		items="${apFormItemDVo.childList}" var="apFormItemLVo">
	<input type="hidden" name="items-${apFormCombDVo.formCombSeq}-${apFormItemLVo.rowNo}-${apFormItemLVo.colNo}" value="${apFormItemLVo.itemId}-${apFormItemLVo.cspnVa}" /></c:forEach>
</div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'bodyHtml'}"

><c:set var="bodyHtmlArea" value="Y" />
<div id="bodyHtmlArea">
<u:title titleId="ap.cmpt.editorBody" type="small" alt="본문(편집기)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="ap.btn.xmlToHtml" id="xmlToHtmlBtn" alt="XML to HTML" onclick="popComponent('xmlBody')" />
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('bodyHtml', '0')" auth="A" /></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title><%
	com.innobiz.orange.web.ap.vo.ApFormBVo apFormBVo1 = (com.innobiz.orange.web.ap.vo.ApFormBVo)request.getAttribute("apFormBVo");
	if(apFormBVo1 != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(apFormBVo1.getBodyHtml());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", apFormBVo1.getBodyHtml());
		}
	}
%>
<u:editor id="bodyHtml" width="100%" height="400px" module="ap.form" value="${_bodyHtml}" />
<div id="bodyHtmlDataArea" style="display:none"><c:if test="${not empty apFormBVo.erpFormId}">
<input type="hidden" name="erpFormId" id="erpFormId" value="${apFormBVo.erpFormId}" />
<input type="hidden" name="erpFormTypCd" id="erpFormTypCd" value="${apFormBVo.erpFormTypCd}" /></c:if>
</div>
<div class="blank"></div>
</div>
<div id="bodyHtmlViewArea" style="display:none;">
<u:listArea noBottomBlank="true">
<tr><td class="editor" valign="top"<c:if test="${not empty apFormBVo.bodyHghtPx}"> style="height:${apFormBVo.bodyHghtPx}px"</c:if>></td></tr>
</u:listArea>
<div class="blank"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'formBody' and not empty apErpFormBVo}"

><c:set var="formBodyArea" value="Y" />
<div id="formBodyArea">
<u:title titleId="ap.cmpt.formBody" type="small" alt="본문 (폼양식)" notPrint="true" >
	<c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('formBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('formBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="formBodyHtmlArea">
<jsp:include page="/WEB-INF/jsp/${apErpFormBVo.regUrl}" />
</div>
<div class="blank"></div>
<div style="display:none" id="formBodyDataArea">
<input name="erpFormId" type="hidden" value="${apErpFormBVo.erpFormId}">
<input name="erpFormTypCd" type="hidden" value="${apErpFormBVo.erpFormTypCd}"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'formEditBody' and not empty apErpFormBVo}"

><c:set var="formEditBodyArea" value="Y" />
<div id="formEditBodyArea">
<u:title titleId="ap.cmpt.formEditBody" type="small" alt="본문 (편집양식)" notPrint="true" >
	<c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('formEditBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('formEditBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="formEditBodyHtmlArea"><u:set test="${true}" var="formBodyMode" value="edit" />
<jsp:include page="/WEB-INF/jsp/${apErpFormBVo.regUrl}" />
</div>
<div class="blank"></div>
<div style="display:none" id="formEditBodyDataArea">
<input name="erpFormId" type="hidden" value="${apErpFormBVo.erpFormId}">
<input name="erpFormTypCd" type="hidden" value="${apErpFormBVo.erpFormTypCd}"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'wfFormBody' and not empty apErpFormBVo}"

><c:set var="wfFormBodyArea" value="Y" />
<div id="wfFormBodyArea">
<u:title titleId="ap.cmpt.wfFormBody" type="small" alt="본문 (업무양식)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('wfFormBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('wfFormBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="wfFormBodyHtmlArea"><u:set test="${true}" var="formBodyMode" value="edit" />
<jsp:include page="/WEB-INF/jsp/${apErpFormBVo.regUrl}" />
</div>
<div class="blank"></div>
<div style="display:none" id="wfFormBodyDataArea">
<input name="erpFormId" type="hidden" value="${apErpFormBVo.erpFormId}">
<input name="erpFormTypCd" type="hidden" value="${apErpFormBVo.erpFormTypCd}"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'refDocBdoy'}"

><c:set var="refDocBdoyArea" value="Y" />
<div id="refDocBdoyArea" class="notPrint">
<u:title titleId="ap.cmpt.refDocBdoy" type="small" alt="참조문서 본문" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('refDocBdoy')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div class="blank notPrint"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'refVw'}"

><c:set var="refVwArea" value="Y" />
<div id="refVwArea" class="notPrint">
<u:title titleId="ap.term.refVw" type="small" alt="참조열람" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('refVw')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:set test="true" var="refVwListInDoc" value="Y" />
<jsp:include page="../../box/viewRefVwInc.jsp" flush="false" />
<div class="blank notPrint"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'infm'}"

><c:set var="infmArea" value="Y" />
<div id="infmArea">
<u:title titleId="ap.cmpt.infm" type="small" alt="통보" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('infm')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:listArea colgroup="13%,87%" noBottomBlank="true">
	<tr><td class="head_ct"><u:term termId="ap.term.infm" alt="통보" /></td>
		<td class="body_lt"></td>
	</tr>
</u:listArea>
<div class="blank notPrint"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'sender'}"

><c:if test="${not empty docSender.txtCont}"><c:set var="senderArea" value="Y" />
<div id="senderArea">
<u:title titleId="ap.cmpt.sender" type="small" alt="발신명의" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('sender')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('sender')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div>
	<div style="width:100%; height:95px;">
		<dl style="position:relative; float:right; left:-50%; z-index:1; margin-top:30px;">
			<dd style="position:relative; float:left; left:50%; width:80px; z-index:2;">&nbsp;</dd>
			<dd style="position:relative; float:left; left:50%; text-align:center; z-index:2;">
				<div id="docSenderViewArea" style="color:${docSender.txtColrVa}; font-family:${docSender.txtFontVa}; font-size:${docSender.txtSize}; ${docSender.txtStylVa}"><u:out value="${docSender.txtCont}" /></div>
			</dd>
			<dd style="text-align:center; position:relative; float:left; left:50%; width:115px; margin-left:-35px; margin-top:-20px; z-index:1;"><img height="80px" src="${_cxPth}/images/etc/seal.png"></dd>
		</dl>
	</div>
	<div style="width:100%; text-align:center;">
		<div id="docReceiverViewArea" style="color:${docReceiver.txtColrVa}; font-family:${docReceiver.txtFontVa}; font-size:${docReceiver.txtSize}; ${docReceiver.txtStylVa}"><u:out value="${docReceiver.txtCont}" /></div>
	</div>
</div>
<div class="blank"></div>
<div style="display:none" id="senderDataArea"><c:forEach
	items="${senderTxts}" var="item"><u:convert
	srcId="${item}" var="apFormTxtDVo"/>
	<c:if test="${not empty apFormTxtDVo}"><input type="hidden" name="${item}-useYn" value="Y" /></c:if>
	<input type="hidden" name="${item}-txtCont" value="<u:out value="${apFormTxtDVo.txtCont}" type="value" />" />
	<input type="hidden" name="${item}-txtFontVa" value="<u:out value="${apFormTxtDVo.txtFontVa}" type="value" />" />
	<input type="hidden" name="${item}-txtStylVa" value="<u:out value="${apFormTxtDVo.txtStylVa}" type="value" />" />
	<input type="hidden" name="${item}-txtSize" value="<u:out value="${apFormTxtDVo.txtSize}" type="value" />" />
	<input type="hidden" name="${item}-txtColrVa" value="<u:out value="${apFormTxtDVo.txtColrVa}" type="value" />" /></c:forEach>
</div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'footer'}"

><c:if test="${not empty docFooter.txtCont}"><c:set var="footerArea" value="Y" />
<div id="footerArea">
<u:title titleId="ap.cmpt.footer" type="small" alt="바닥글" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('footer')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('footer')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div>
	<div id="docFooterViewArea" style="width:100%; text-align:center; color:${docFooter.txtColrVa}; font-family:${docFooter.txtFontVa}; font-size:${docFooter.txtSize}; ${docFooter.txtStylVa}"><u:out value="${docFooter.txtCont}" /></div>
</div>
<div class="blank"></div>
<div style="display:none" id="footerDataArea"><c:forEach
	items="${footerTxts}" var="item"><u:convert
	srcId="${item}" var="apFormTxtDVo"/>
	<c:if test="${not empty apFormTxtDVo.txtCont}"><input type="hidden" name="${item}-useYn" value="Y" /></c:if>
	<input type="hidden" name="${item}-txtCont" value="<u:out value="${apFormTxtDVo.txtCont}" type="value" />" />
	<input type="hidden" name="${item}-txtFontVa" value="<u:out value="${apFormTxtDVo.txtFontVa}" type="value" />" />
	<input type="hidden" name="${item}-txtStylVa" value="<u:out value="${apFormTxtDVo.txtStylVa}" type="value" />" />
	<input type="hidden" name="${item}-txtSize" value="<u:out value="${apFormTxtDVo.txtSize}" type="value" />" />
	<input type="hidden" name="${item}-txtColrVa" value="<u:out value="${apFormTxtDVo.txtColrVa}" type="value" />" /></c:forEach></div>
</div></c:if></c:if>
</c:forEach>

<c:if test="${empty bodyHtmlArea}">
<div id="bodyHtmlArea" style="display:none">
<u:title titleId="ap.cmpt.editorBody" type="small" alt="본문(편집기)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('bodyHtml', '0')" auth="A" /></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title><%
	com.innobiz.orange.web.ap.vo.ApFormBVo apFormBVo2 = (com.innobiz.orange.web.ap.vo.ApFormBVo)request.getAttribute("apFormBVo");
	if(apFormBVo2 != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(apFormBVo2.getBodyHtml());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", apFormBVo2.getBodyHtml());
		}
	}
%>
<u:editor id="bodyHtml" width="100%" height="400px" module="ap.form" value="${_bodyHtml}" />
<div id="bodyHtmlDataArea" style="display:none"><c:if test="${not empty apFormBVo.erpFormId}">
<input type="hidden" name="erpFormId" id="erpFormId" value="${apFormBVo.erpFormId}" />
<input type="hidden" name="erpFormTypCd" id="erpFormTypCd" value="${apFormBVo.erpFormTypCd}" /></c:if>
</div>
<div class="blank"></div>
</div>
<div id="bodyHtmlViewArea" style="display:none;">
<u:listArea noBottomBlank="true">
<tr><td class="editor" valign="top"<c:if test="${not empty apFormBVo.bodyHghtPx}"> style="height:${apFormBVo.bodyHghtPx}px"</c:if>></td></tr>
</u:listArea>
<div class="blank"></div>
</div>
</c:if>

<div style="display:none" id="printerDataArea">
<input type="hidden" name="bodyHghtPx" value="<u:out value="${apFormBVo.bodyHghtPx}" type="value" />" />
<input type="hidden" name="formWdthTypCd" value="<u:out value="${apFormBVo.formWdthTypCd}" type="value" />" />
<input type="hidden" name="formMagnVa" value="<u:out value="${apFormBVo.formMagnVa}" type="value" />" />
</div>

</div>
</form>

<div id="docHiddenArea" style="display:none;"><c:if

	test="${empty headerArea}">
<div id="headerArea">
<u:title titleId="ap.cmpt.header" type="small" alt="머리글" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('header')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('header')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div>
	<div id="docHeaderViewArea" style="width:100%; text-align:center; color:${docHeader.txtColrVa}; font-family:${docHeader.txtFontVa}; font-size:${docHeader.txtSize}; ${docHeader.txtStylVa}"><u:out value="${docHeader.txtCont}" /></div>
	<div id="docLogoViewArea" style="float:left; width:20%; height:75px;"><c:if test="${not empty docLogo.imgPath}"><img height="75" src="${docLogo.imgPath}"></c:if></div>
	<div id="docNameViewArea" style="float:left; width:60%; height:75px; text-align:center; margin-top:15px; color:${docName.txtColrVa}; font-family:${docName.txtFontVa}; font-size:${docName.txtSize}; ${docName.txtStylVa}"><u:out value="${apFormTxtDVo.txtCont}" /></div>
	<div id="docSymbolViewArea" style="float:right; width:20%; height:75px; text-align: right;"><c:if test="${not empty docSymbol.imgPath}"><img height="75" src="${docSymbol.imgPath}"></c:if></div>
</div>
<div class="blank"></div>
<div style="display:none" id="headerDataArea"></div>
</div>
</c:if><c:if

		test="${empty footerArea}">
<div id="footerArea">
<u:title titleId="ap.cmpt.footer" type="small" alt="바닥글" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('footer')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('footer')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div>
	<div id="docFooterViewArea" style="width:100%; text-align:center; color:${docFooter.txtColrVa}; font-family:${docFooter.txtFontVa}; font-size:${docFooter.txtSize}; ${docFooter.txtStylVa}"><u:out value="${docFooter.txtCont}" /></div>
</div>
<div class="blank"></div>
<div style="display:none" id="footerDataArea"></div>
</div>
</c:if><c:if

		test="${empty refDocBdoyArea}" >
<div id="refDocBdoyArea" class="notPrint">
<u:title titleId="ap.cmpt.refDocBdoy" type="small" alt="참조문서 본문" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('refDocBdoy')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div class="blank notPrint"></div>
</div>
</c:if><c:if


	test="${empty refVwArea}" >
<div id="refVwArea">
<u:title titleId="ap.term.refVw" type="small" alt="참조열람" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('refVw')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:set test="true" var="refVwListInDoc" value="Y" />
<jsp:include page="../../box/viewRefVwInc.jsp" flush="false" />
<div class="blank notPrint"></div>
</div></c:if><c:if


	test="${empty infmArea}" >
<div id="infmArea">
<u:title titleId="ap.cmpt.infm" type="small" alt="통보" notPrint="true" >
	<u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('infm')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:listArea colgroup="13%,87%" noBottomBlank="true">
	<tr><td class="head_ct"><u:term termId="ap.term.infm" alt="통보" /></td>
		<td class="body_lt"></td>
	</tr>
</u:listArea>
<div class="blank notPrint"></div>
</div></c:if><c:if


	test="${empty senderArea}" >
<div id="senderArea">
<u:title titleId="ap.cmpt.sender" type="small" alt="발신명의" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('sender')" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('sender')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div>
	<div style="width:100%; height:95px;">
		<dl style="position:relative; float:right; left:-50%; z-index:1; margin-top:30px;">
			<dd style="position:relative; float:left; left:50%; width:80px; z-index:2;">&nbsp;</dd>
			<dd style="position:relative; float:left; left:50%; text-align:center; z-index:2;">
				<div id="docSenderViewArea" style="color:${docSender.txtColrVa}; font-family:${docSender.txtFontVa}; font-size:${docSender.txtSize}; ${docSender.txtStylVa}"><u:out value="${docSender.txtCont}" /></div>
			</dd>
			<dd style="text-align:center; position:relative; float:left; left:50%; width:115px; margin-left:-35px; margin-top:-20px; z-index:1;"><img height="80px" src="${_cxPth}/images/etc/seal.png"></dd>
		</dl>
	</div>
	<div style="width:100%; text-align:center;">
		<div id="docReceiverViewArea" style="color:${docReceiver.txtColrVa}; font-family:${docReceiver.txtFontVa}; font-size:${docReceiver.txtSize}; ${docReceiver.txtStylVa}"><u:out value="${docReceiver.txtCont}" /></div>
	</div>
</div>
<div class="blank"></div>
<div style="display:none" id="senderDataArea"></div>
</div>
</c:if><c:if

	test="${empty formBodyArea}" >
<div id="formBodyArea">
<u:title titleId="ap.cmpt.formBody" type="small" alt="본문 (폼양식)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('formBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('formBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="formBodyHtmlArea"></div>
<div class="blank"></div>
<div style="display:none" id="formBodyDataArea"></div>
</div>
</c:if><c:if

	test="${empty formEditBodyArea}" >
<div id="formEditBodyArea">
<u:title titleId="ap.cmpt.formEditBody" type="small" alt="본문 (편집양식)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('formEditBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('formEditBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="formEditBodyHtmlArea"></div>
<div class="blank"></div>
<div style="display:none" id="formEditBodyDataArea"></div>
</div>
</c:if><c:if

	test="${empty wfFormBodyArea}" >
<div id="wfFormBodyArea">
<u:title titleId="ap.cmpt.wfFormBody" type="small" alt="본문 (업무양식)" notPrint="true" ><c:if
		test="${not empty bodies}">
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('wfFormBody')" auth="A"
	/><c:if
		test="${empty noBothBody}"><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('wfFormBody')" auth="A" /></c:if></c:if>
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="wfFormBodyHtmlArea"></div>
<div class="blank"></div>
<div style="display:none" id="wfFormBodyDataArea"></div>
</div>
</c:if>

</div>

<div id="apvLnHiddenArea" style="display:none;">

<div id="apvLineArea">
<u:title titleId="ap.cmpt.apvLine" type="small" alt="결재라인" notPrint="true" ><c:if
		test="${optConfigMap.fixdApvLn eq 'Y'}">
	<u:titleButton titleId="ap.cfg.fixdApvLn" id="fixdApvLnBtn" alt="양식 결재선" onclick="setFromApvLn()" auth="A"
	/></c:if><c:if
		test="${optConfigMap.fixdRefVw eq 'Y'}">
	<u:titleButton titleId="ap.cfg.fixdRefVw" id="fixdRefVwBtn" alt="양식 참조열람" onclick="setFromRefVw()" auth="A"
	/></c:if><u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent($('#formApvLnTypCd').val(), this)" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('apvLn', '0')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<div id="bottomBlank" class="blank"></div>
<div style="display:none" id="apvLnDataArea"></div>
<div style="display:none" id="refVwDataArea"></div>
</div>

<div id="3row">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:right;"><tbody>
	<tr><td class="approval_head" rowspan="3"><u:term termId="ap.term.apv" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td></tr>
	<tr><td class="approval_img"><img src="${_ctx}/images/etc/etc_s.png"></td></tr>
	<tr><td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td></tr>
	</tbody></table>
</div>

<div id="2row">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:right;"><tbody>
	<tr><td class="approval_head" rowspan="2"><u:term termId="ap.term.apv" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="approval_body" rowspan="2"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td></tr>
	<tr><td class="approval_body"><img height="20" src="${_ctx}/images/etc/etc_s.png"></td></tr>
	</tbody></table>
</div>

<div id="1row">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:right;"><tbody>
	<tr><td class="approval_headw"><u:term termId="ap.term.apv" alt="결재" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleTitle" alt="과장" /></td>
		<td class="approval_body"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td>
		<td class="approval_body"><img height="20" src="${_ctx}/images/etc/etc_s.png"></td></tr>
	</tbody></table>
</div>

<u:listArea id="oneTopArea" colgroup="13%,20%,3%,31%,33%" noBottomBlank="true">
	<tr>
		<td class="head_lt"></td>
		<td class="body_lt"></td>
		<td class="head_ct" rowspan="5"><u:msg titleId="ap.signArea.apv" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="head_ct"><u:msg titleId="ap.cmpt.signArea.sampleCEO" alt="대표이사" /></td>
		<td class="head_ct"><u:msg titleId="ap.cmpt.order" alt="지시사항" /></td>
	</tr>
	<tr>
		<td class="head_lt"></td>
		<td class="body_lt"></td>
		<td class="body_ct" rowspan="3"><img src="${_ctx}/images/etc/etc_s.png"></td>
		<td class="body_ct" rowspan="5"></td>
	</tr>
	<tr>
		<td class="head_lt"></td>
		<td class="body_lt"></td>
	</tr>
	<tr>
		<td class="head_lt"></td>
		<td class="body_lt"></td>
	</tr>
	<tr>
		<td class="head_lt"></td>
		<td class="body_lt"></td>
		<td class="body_ct"><u:msg titleId="ap.cmpt.signArea.sampleDate" alt="2010-12-25" /></td>
	</tr>
</u:listArea>

<u:set test="true" var="apvLnListInDoc" value="Y" /><u:set test="true" var="apvLnListForEdit" value="Y" />
<jsp:include page="../../box/viewApvLnInc.jsp" flush="false" />

</div>

<div id="itemsHiddenArea" style="display:none;">

<div id="itemsArea">
<u:title titleId="ap.cmpt.items" type="small" alt="항목지정" notPrint="true" >
	<u:titleButton titleId="cm.btn.setup" id="setupBtn" alt="설정" onclick="popComponent('items', this)" auth="A"
	/><u:titleButton titleId="cm.btn.del" id="delBtn" alt="삭제" onclick="delComponent('items', '0')" auth="A" />
	<u:titleIcon type="up" onclick="moveComponent(this,'up')" auth="A" />
	<u:titleIcon type="down" onclick="moveComponent(this,'down')" auth="A" />
</u:title>
<u:listArea id="itemsViewArea" colgroup="15%" noBottomBlank="true"></u:listArea>
<div class="blank"></div>
<div style="display:none" id="itemsDataArea"></div>
</div>

</div>

<u:buttonArea>
	<u:button href="javascript:saveForm()" titleId="cm.btn.save" alt="저장" auth="A" />
	<u:button titleId="ap.btn.print" alt="인쇄" onclick="setViewMode('print');printWeb();setViewMode('edit');" /><c:if
		test="${not browser.ie and not browser.chrome}">
	<u:button titleId="ap.btn.printView" alt="인쇄전환" onclick="setViewMode('print')" />
	<u:button titleId="ap.btn.editView" alt="편집전환" onclick="setViewMode('edit')" /></c:if>
	<u:button href="./setApvForm.do?menuId=${menuId}&formBxId=${param.formBxId}" titleId="cm.btn.cancel" alt="취소" />
</u:buttonArea>

</div>