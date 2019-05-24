
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
};
String.prototype.ltrim = function() {
	return this.replace(/^\s+/,"");
};
String.prototype.rtrim = function() {
	return this.replace(/\s+$/,"");
};
String.prototype.startsWith = function (str){
	return this.slice(0, str.length) == str;
};
String.prototype.endsWith = function (str){
	return this.slice(-str.length) == str;
};
String.prototype.replaceAll = function (){ 
    var arg = arguments, length = arg.length;
    if (length == 0) return this;
    var regExp = new RegExp( arg[0], "g");
    return (length == 1) ? this.replace(regExp, "") : this.replace(regExp, arg[1]);
};

Array.prototype.contains = function(va) {
	for(var i=0;i<this.length;i++){
		if(va==this[i]) return true;
	}
	return false;
};
Array.prototype.each = function(handler, reverse) {
	if(reverse==true){
		for(var i=this.length-1;i>=0;i--){
			if(handler(i, this[i]) == false) break;
		}
	} else {
		for(var i=0;i<this.length;i++){
			if(handler(i, this[i]) == false) break;
		}
	}
};
if(Array.prototype.indexOf == null){//for IE 7
	Array.prototype.indexOf = function(item, start){
		for(var i=(start==null ? 0 : start);i<this.length;i++){
			if(this[i]==item) return i;
		}
		return -1;
	};
}
Array.prototype.removeValues = function() {
    var what, arg = arguments, size = arg.length, index;
    while (size && this.length) {
        what = arg[--size];
        while ((index = this.indexOf(what)) !== -1) {
            this.splice(index, 1);
        }
    }
    return this;
};
Array.prototype.append = function(obj) {
	this.push(obj);
	return this;
};
Array.prototype.swap = function(idx1, idx2) {
	var va = this[idx1];
	this[idx1] = this[idx2];
	this[idx2] = va;
};
function StringBuffer(){
	this.buffer = [];
	this.append = function(a) { this.buffer.push(a); return this; };
	this.join = function(j){ return this.buffer.join(j); };
	this.toString = function(){ return this.buffer.join(""); };
}
function ParamMap(initObj){
	this.map = initObj==null ? {} : initObj;
	this.put = function(k,va){this.map[k]=va; return this;};
	this.get = function(k,nullva){return this.map[k]==null ? nullva : this.map[k];};
	this.remove = function(k){delete this.map[k]; return this;};
	this.clear = function(){this.map={}; return this;};
	this.each = function(handler, arg) {
		var keepGoing = true;
		for(var k in this.map) {
			if(keepGoing!=false) keepGoing = handler(k, this.map[k], arg);
		}
		return keepGoing!=false;
	};
	this.keys = function(){
		var k=null,ks = [];
		for(k in this.map){ks.push(k);}
		return ks;
	};
	this.toObject = function() {
		var k=null, rv={};
		for(k in this.map) rv[k] = this.map[k];
		return rv;
	};
	this.extend = function(obj){
		var k=null, rv={};
		for(k in this.map) rv[k] = this.map[k];
		if(obj==null) return rv;
		return new ParamMap(rv).readObject(obj).map;
	};
	this.readObject = function(obj){
		if(obj==null) return this;
		var tp;
		for(var prop in obj){
			tp = this.toTypeString(obj[prop]);
			if(tp=='function'){}
			else if(tp=='data'){ this.map[prop] = obj[prop]; }
			else if(tp=='array'){ this.map[prop] = this.readArray(obj[prop]); }
			else { this.map[prop] = new ParamMap().readObject(obj[prop]); }
		}
		return this;
	};
	this.readArray = function(arr){
		var i, tp;
		for(i=0;i<arr.length;i++){
			tp = this.toTypeString(arr[i]);
			if(tp=='array'){ arr[i] = this.readArray(arr[i]); }
			else if(tp=='object'){ arr[i] = new ParamMap().readObject(arr[i]); }
		}
		return arr;
	};
	this.toTypeString = function(obj){
		var tp = Object.prototype.toString.call(obj);
		if(tp.indexOf("String")>0||tp.indexOf("Number")>0||tp.indexOf("Boolean")>0||tp.indexOf("Date")>0) return "data";
		if(tp.indexOf("Array")>0) return "array";
		if(tp.indexOf("Function")>0) return "function";
		return "object";
	};
	this.toQueryString = function(){return this.str('=','&',null, encodeURIComponent);};
	this.toJSON = function(){ return this.map; };
	this.toString = function(){return this.str('=',',\n',['{','}'], null);};
	this.str = function(keySp, itemSp, bracket, cvtr){
		var k=null, rv=[];
		if(bracket!=null) rv.push(bracket[0]);
		for(k in this.map){
			if(typeof this.map[k] == 'function') continue;
			if(rv.length>1) rv.push(itemSp);
			rv.push(cvtr==null ? k : cvtr(k));
			rv.push(keySp);
			rv.push(cvtr==null ? this.map[k] : cvtr(this.map[k]));
		}
		if(bracket!=null) rv.push(bracket[1]);
		return rv.join('');
	};
	this.getData = function(areaId, prefix, keyName){
		if(typeof areaId == "string"){
			return this.getDataFromObj($("#"+areaId), prefix, keyName);
		} else {
			return this.getDataFromObj($(areaId), prefix, keyName);
		}
	};
	this.getDataFromObj = function($areaObj, prefix, keyName){
		var prefixLen = prefix==null ? 0 : prefix.length==null ? 0 : prefix.length;
		var obj, tp=null, key, tg, va, oldVa, me = this;
		if(keyName==null) keyName = "name";
		$areaObj.find(":input").each(function(index){
			obj = $(this);
			if(obj.attr('disabled')==true) return;
			key = obj.attr(keyName)==null ? "" : obj.attr(keyName).substring(prefixLen);
			if(key!=""){
				if((tg=obj.tagName())=='input'){
					tp = obj.attr('type');
					tp = tp==null ? '' : tp.toLowerCase();
					va = null;
					if(tp=='radio' || tp=='checkbox') {
						if(obj[0].checked) va = obj.val();
					} else if(tp=='' || tp=='text' || tp=='hidden' || tp=='password' || tp=='file') {
						va = obj.val();
					}
				} else if(tg=='select'){
					va = obj.find(":selected").val();
					if(va==null) va = "";
				} else {
					va = obj.val();
				}
				if(va!=null){
					oldVa = me.get(key);
					if(oldVa==null) me.put(key, va);
					else if(va!='') me.put(key, oldVa+", "+va);
				}
			}
		});
		return me;
	};
	this.setData = function(areaId, prefix, withEvnt){
		if(typeof areaId == "string"){
			return this.setDataToObj($("#"+areaId), prefix, withEvnt);
		} else {
			return this.setDataToObj($(areaId), prefix, withEvnt);
		}
	};
	this.setDataToObj = function($areaObj, prefix, withEvnt){
		if(prefix==null) prefix = "";
		var setValFunc = function($input, va){
			var tp = $input.attr('type')==null ? $input.tagName() : $input.attr('type').toLowerCase();
			if(withEvnt==true){
				if(tp=='radio'){
					if($input.val()==va && !$input[0].checked){
						$input.trigger('click');
					}
				} else if(tp=='checkbox'){
					if($input.val()==va && !$input[0].checked){
						$input.trigger('click');
					} else if($input.val()!=va && $input[0].checked){
						$input.trigger('click');
					}
				} else if(tp=='select'){
					$input.find("option[value='"+va+"']").attr("selected", true);
				} else {
					$input.val(va);
				}
			} else {
				if(tp=='checkbox' || tp=='radio'){
					$input.attr('checked',$input.val()==va);
				} else {
					$input.val(va);
				}
			}
		};
		this.each(function(k,v){
			var $input = $areaObj.find("[name="+prefix+k+"]");
			if($input.length==1){
				setValFunc($input, v);
			} else if($input.length>1){
				$input.each(function(){setValFunc($(this), v);});
			}
		});
	};
	this.getNotHaveAll = function(keys){
		var i, va, firstNotHave=null, allNotHave=true;
		for(i=0;i<keys.length;i++){
			va = this.get(keys[i]);
			if(va==null || va.trim()==""){
				if(firstNotHave==null) firstNotHave = keys[i];
			} else {
				allNotHave = false;
			}
		}
		return allNotHave ? null : firstNotHave;
	};
}

(function($){
	$.fn.tagName = function() {
		return (this.get(0)!=null) ? this.get(0).tagName.toLowerCase() : null;
	};
	$.fn.parentTag = function(name){
		var p = $(this), tn;
		while((p = p.parent()).length > 0){
			tn = p[0].tagName.toLowerCase();
			if(tn==name) return p;
			if(tn=='body') return null;
		}
		return null;
	};
	$.fn.parentClass = function(name){
		var p = $(this), tn;
		while((p = p.parent()).length > 0){
			if(p.hasClass(name)) return p;
			tn = p[0].tagName.toLowerCase();
			if(tn=='body') return null;
		}
		return null;
	};
	$.fn.appendHidden = function(attrs) {
		var $hidden = $('<input type="hidden"/>');
		for(var key in attrs) {
			$hidden.attr(key, attrs[key]);
		}
		this.append($hidden);
		return this;
	};
})(jQuery);

//스크립트의 따옴표("" or '') 내에서 깨지지 않도록 치환
function escapeScript(value){
	if(value==null || value=='') return value;
	value = value.replace(/\'/g,"\\\'");
	value = value.replace(/\"/g,"\\\"");
	value = value.replace(/\r/g,"\\\r");
	value = value.replace(/\n/g,"\\\n");
	return value;
}
//element 의 value 따옴표(value="" or value='') 내에서 깨지지 않도록 치환
function escapeValue(value){
	if(value==null || value=='') return value;
	value = value.replace(/\'/g,"&apos;");
	value = value.replace(/\"/g,"&quot;");
	value = value.replace(/\r/g,"&#13;");
	value = value.replace(/\n/g,"&#10;");
	return value;
}
function escapeHtml(value){
	if(value==null || value=='') return value;
	value = value.replace(/</g,"&lt;");
	value = value.replace(/>"/g,"&gt;");
	value = value.replace(/\r/g,"");
	value = value.replace(/\n/g,"<br/>");
	return value;
}
function fnTxtDelete(obj){
	if(arguments.length == 0) return;
	var $parent = $(obj).parents('div.etr_calendar');
	if(arguments.length == 1){
		$parent.find('span, input[type="hidden"]').each(function(){
			if($(this).prop('tagName') == 'SPAN') $(this).text('');
			else $(this).val('');
		});
	}else{
		var i;
		for(i=1;i<arguments.length;i++){
			$parent.find('span#'+arguments[i]).text('');
			$parent.find('input[id="'+arguments[i]+'"]').val('');
		}
	}
}
//form 초기화[id:컨테이너,chkIds:초기화 제외할 id]
function valueReset(id, chkIds){
	if(id == undefined) return;
	var $area = $('#'+id);
	if($area == undefined) return;
	$area.find("input,select").each(function(){
		if(chkIds != null && $(this).attr('id') != undefined && chkIds.contains($(this).attr('id'))) return true;
		if($(this).prop("tagName") == 'SELECT'){
			if($(this).find('option') != undefined) $(this).val($(this).find('option').eq(0).val());
			$(this).uniform.update();
		}else $(this).val('');
	});
}
var $ui = {
	apply:function(obj, on){
		if(obj==null) return;
		if(obj.tagName.toLowerCase()=='input'){
			var objType = obj.type;
			if(objType!=null){
				objType = objType.toLowerCase();
				if(objType=='radio' || objType=='checkbox') obj = obj.parentElement;
			}
		}
		
		var clsNm = $(obj).attr('class');
		if(clsNm==null || clsNm.indexOf('disabled')>0) return;
		var onNow = clsNm.endsWith('_on');
		if(on){
			if(!onNow) $(obj).attr('class', clsNm+'_on');
			$(obj).children()[0].checked = true;
		} else {
			if(onNow) $(obj).attr('class', clsNm.substring(0, clsNm.length-3));
			$(obj).children()[0].checked = false;
		}
	},
	toggle:function(obj, areaId){
		if(obj==null) return;
		var me = $(obj);
		var clsNm = me.attr('class');
		if(clsNm.indexOf('disabled')>0) return;
		if(clsNm.startsWith('radio')){
			if(areaId==null){
				me.parent().children().each(function(){
					clsNm = $(this).attr('class');
					if(clsNm!=null && clsNm.startsWith('radio')){
						$ui.apply(this, obj==this);
					}
				});
			} else {
				$('#'+areaId).find('dd.radio, dd.radio_on, dd.radio_disabled, dd.radio_disabled_on, div.radio, div.radio_on').each(function(){
					$ui.apply(this, obj==this);
				});
			}
		} else if(clsNm.startsWith('check')){
			var on = clsNm.endsWith('_on');
			$ui.apply(obj, !on);
		}
	},
	disable:function(obj, on){
		if(obj==null) return;
		if(on!=false) on = true;
		var clsNm = $(obj).attr('class');
		var objDisabled = clsNm.indexOf('disabled')>0;
		if(objDisabled && !on){
			clsNm = clsNm.replace('_disabled', '');
			$(obj).attr('class', clsNm);
		} else if(!objDisabled && on){
			clsNm = (clsNm.endsWith('_on')) ? clsNm.replace('_on', '_disabled_on') : clsNm+'_disabled';
			$(obj).attr('class', clsNm);
		}
	}
};

var browser = { ie:false, chrome:false, firefox:false, safari:false, opera:false, etc:false, ver:0,
	init:function(){
		var ag = navigator.userAgent, p;
		if((p=ag.indexOf('MSIE'))>0){
			this.ie = true;
			this.ver = parseInt(ag.substring(p+5, ag.indexOf(';',p+5)));
		} else if((p=ag.indexOf('Trident'))>0){
			this.ie = true;
			p = ag.indexOf("rv:",p+8);
			this.ver = parseInt(ag.substring(p+3, ag.indexOf('.',p+3)));
		} else if((p=ag.indexOf('OPR/'))>0){
			this.opera = true;
		} else if((p=ag.indexOf('Chrome'))>0){
			this.chrome = true;
		} else if((p=ag.indexOf('Firefox'))>0){
			this.firefox = true;
		} else if((p=ag.indexOf('Safari'))>0){
			this.safari = true;
		} else if((p=ag.indexOf('Opera'))>0){
			this.opera = true;
		} else {
			this.etc = true;
		}
		return this;
	}
}.init();
function returnTrue(){ return true; }
function returnFalse(){ return false; }
function copyEnable(){ toggleCopy(true); }
function copyDisable(){ toggleCopy(false); }
function toggleCopy(flag){
	if(flag){
		document.oncontextmenu = returnTrue;
		if(browser.ie && browser.ver < 11){
			$(document).unbind("keydown keyup keypress");
		} else {
			$(document).unbind("copy cut");
		}
	} else {
		document.oncontextmenu = returnFalse;
		if(browser.ie && browser.ver < 11){
			$('body').bind("keydown keyup keypress",function(e) {
				var evt = e||window.event;
				var cKey = 67, xKey = 88;
				var ctrlDown = evt.ctrlKey||evt.metaKey;
				if (ctrlDown && (evt.keyCode==cKey || evt.keyCode==xKey)){
					if(evt.preventDefault) evt.preventDefault();
					return false;
				}
				return true;
			});
		} else {
			$(document).bind("copy cut",function(evt) {
				evt = evt||window.event;
				if(evt.preventDefault) evt.preventDefault();
				return false;
			});
		}
	}
}
/**
 * 브라우저 관계 없이 iframe 내 content 가져오기.
 *
 * @param {String}	id
 *
 * return : 해당 윈도우 객체.                                         
 **/
function getIframeContent(id) {
	var ifrm = document.getElementById(id);
	return ifrm.contentWindow || ifrm.contentDocument;
}
/** 문서뷰어 */
function openViewAttchFile(url){
	if(url.indexOf('Sub')>-1) url=url.replace('Sub','Ajx');
	//loading(document.body, true);
	$m.ajax(url, null, function(data){
		//loading(document.body, false);
		if(data.message!=null) $m.dialog.alert(data.message);
		if(data.fn!=null && data.rs!=null) {			
			var url = '/viewer/skin/doc.html?fn='+data.fn+'&rs=/'+data.rs;
			if($m.browser.mobile && $m.browser.safari){
				var a = document.createElement('a');
			    a.setAttribute("href", url);
			    a.setAttribute("target", "_blank");
			    var dispatch = document.createEvent("HTMLEvents");
			    // initEvent( "커스텀 이벤트 이름:String", "버블링:Boolean", "취소가능:Boolean" )
			    dispatch.initEvent("click", true, true); // Events : e.initEvent,  MouseEvents : e.initMouseEvent, UIEvents : e.initUIEvent
			    a.dispatchEvent(dispatch);
			}else{
				window.open(url, '_blank');
			}
		}
	}, {async:false});
}
// SNS 올리기
function snsUpload(sns, url, text){
	var encodeUrl = encodeURIComponent(url);
	if(sns=='twitter'){
		window.open('https://twitter.com/intent/tweet?url='+encodeUrl+(text!='' ? '&text='+text : ''), '_blank');
	}else if(sns=='facebook'){		
		window.open('http://www.facebook.com/sharer/sharer.php?u='+encodeUrl, '_blank');
	}else if(sns=='naverband'){
		window.open('http://band.us/plugin/share?body='+text+'&route='+encodeUrl, '_blank');
	}else if(sns=='kakaostory'){
		window.open('https://story.kakao.com/share?url='+encodeUrl, '_blank');
	}else if(sns=='instargram'){
		window.open('https://www.instagram.com/', '_blank');
	}
}
function elResize(el){
	var height,scroll;
	$(el).each(function(){
		height=$(this).height();
		scroll=$(this).prop('scrollHeight');
		if(parseInt(height)<parseInt(scroll)) $(this).css('height', scroll+'px');
	});
}
//로딩 - 인디케이터
function loading(obj, display){
	var target=typeof obj =='string' ? $('#'+obj) : $(obj);
	if(display) target.addClass('loading');
	else target.removeClass('loading');
}

//시간선택 레이어 이벤트 처리
function setTimeSelectEvt(id){
	$('#'+id+'Area ul#selectTimeArea li').on({
		click: function(e){	
			$('#'+id+'Area div.input_select_list input[type="text"]').val($(e.target).text());	
			$(e.target).closest('ul.select_list').hide();
			$('#'+id+'Area div.input_select_list input[type="text"]').focus();
		},		
		keydown: function(e){
			var keyCode=e.keyCode;
			if(keyCode==38 || keyCode==40){
				var index=$('#'+id+'Area ul#selectTimeArea li').index($(e.target));
				if(keyCode==38 && index==0){
					$('#'+id+'Area div.input_select_list input[type="text"]').focus();
					$('#'+id+'Area ul#selectTimeArea').hide();
					return false;
				}
				var lastIndex=$('#'+id+'Area ul#selectTimeArea li').length;
				if(keyCode==40 && index==lastIndex-1)
					return false;
				if(keyCode==38) $(e.target).prev().focus();
				else if(keyCode==40) $(e.target).next().focus();
				else return false;				
				if(index<6) return false;
			}else if(keyCode==13) $(e.target).trigger('click');			
		},
		focus: function(e){
			$('#'+id+'Area ul#selectTimeArea li').css('background-color','');
			$(e.target).css('background-color','#f2f2f2');
		},
		mouseover: function(){
			$(this).css('background-color','#f2f2f2');
		},
		mouseout: function(){
			$(this).css('background-color','transparent');
		}
	});
	
	$('#'+id+'Area div.input_select_list input[type="text"]').on({		
		click: function(){
			$('ul#selectTimeArea').hide();
			$('#'+id+'Area ul#selectTimeArea').show();
		},
		keyup: function(e){
			if(e.keyCode==40){
				$('#'+id+'Area ul#selectTimeArea').show();
				$('#'+id+'Area ul#selectTimeArea li:first').focus();
			}			
		},
		blur: function(e){
			var val=$(e.target).val();
			if(val!='' && !(val.length==5 && /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(val))){
				$(e.target).val('');
				//return false;
			}
			if($(e.target).attr('data-onchange')!=''){
				eval($(e.target).attr('data-onchange'));
			}
		}
	});
	
	// 화면 클릭시 팝업 닫기
	$(document).click(function(event){
		if(!$(event.target).closest('div.input_select_list').length &&
		   !$(event.target).is('div.input_select_list')) {
			   if($('#'+id+'Area ul#selectTimeArea').is(":visible")) {
				$('#'+id+'Area ul#selectTimeArea').hide();
			}
		}
	});
	// 윈도우 리사이즈시 팝업 닫기
	$(window).resize(function(event){
		if(!$(event.target).closest('div.input_select_list').length &&
		   !$(event.target).is('div.input_select_list')) {
			   if($('#'+id+'Area ul#selectTimeArea').is(":visible")) {
				$('#'+id+'Area ul#selectTimeArea').hide();
			}
		}
	});
}