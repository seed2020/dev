
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
function trim(str)  { return str==null ? null : str.trim(); }
function ltrim(str) { return str==null ? null : str.ltrim(); }
function rtrim(str) { return str==null ? null : str.rtrim(); }

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
var gContextPath = null;
var gLogoutCheck = false;

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
	this.clear = function(){this.map={}; return this;};
	this.remove = function(k){delete this.map[k]; return this;};
	this.removeEmpty = function(){
		for(var k in this.map) {
			if(this.map[k]==null || this.map[k]=='') delete this.map[k];
		}
		return this;
	};
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
					if(va==null) va="";
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
				} else if(tp=='select' || tp.startsWith('select-')){
					$input.find("option[value='"+va+"']").attr("selected", true);
					$input.uniform.update();
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
	this.isEmpty = function(){
		return Object.keys(this.map).length == 0;
	};
}

function callAjax(url, param, handler, failHandler, errHandler, async, htmlMode, timeout){
	var sending = "data="+encodeURIComponent(JSON.stringify(param==null ? "" : param.toJSON==null ? param : param.toJSON()));
	var xhr = $.ajax({
		url:toContextURL(url),
		async:(async==true),
		cache:false,
		type:sending.length<1024 ? "GET" : "POST",
		data:sending,
		timeout:timeout,
		success:function(strJson){
			if (typeof strJson == "object") {
				handler(strJson);
			} else if (typeof strJson == "string") {
				if(htmlMode==true){
					if(strJson.startsWith('{"message":') && strJson.endsWith('}')){
						var jsonObject = JSON.parse(strJson);
						if(jsonObject.logout!=null){
							if(top.gLogoutCheck != true){
								if(jsonObject.message!="") alert(jsonObject.message);
								top.gLogoutCheck = true;
							}
							top.location.href = "/cm/login/viewLogin.do";
						} else {
							if(failHandler!=null){
								var model = new ParamMap().readObject(jsonObject.model);
								failHandler(model.toObject(), false);
							} else {
								alert(jsonObject.message);
							}
						}
					} else {
						handler(strJson, true);
					}
				} else {
					var jsonObject = JSON.parse(strJson);
					if(jsonObject.message!=null && jsonObject.message!=""){
						if(jsonObject.logout!=null){
							if(top.gLogoutCheck != true){
								alert(jsonObject.message);
								top.gLogoutCheck = true;
							}
							top.location.href = "/cm/login/viewLogin.do";
						} else {
							if(failHandler!=null){
								var model = new ParamMap().readObject(jsonObject.model);
								failHandler(model.toObject(), false);
							} else {
								alert(jsonObject.message);
							}
						}
					} else {
						var model = new ParamMap().readObject(jsonObject.model);
						handler(model.toObject(), true);
					}
				}
			}
		},
		error: function (xhr, ajaxOptions, thrownError) {
			if(errHandler==null){
				if(url=='/cm/msg/getMessageAjx.do') return;
				var errCd = xhr.status;
				if(errCd=='400') errCd = '403';
				var msgCd = (errCd=='403' || errCd=='404') ? 'cm.msg.errors.'+errCd : 'cm.msg.errors.500';
				if(errCd=='403' || errCd=='404' || (param!=null && param.msgId!="cm.msg.errors.noMessage")){
					alertMsg(msgCd, [errCd, url]);
				}
			} else {
				errHandler(xhr, param, thrownError);
			}
		}
	});
	return xhr;
}

function callMsgAjax(msgId, args, lang, url){
	var message = null;
	if(msgId=='cm.msg.errors.noMessage' && args!=null && args.length>0 && args[0]=='cm.msg.errors.500') return null;
	callAjax(url, {"msgId":msgId, "lang":lang, "args":args}, function(data){
		message = data.message;
	});
	if(message==null){
		callAjax("/cm/msg/getMessageAjx.do", {"msgId":"cm.msg.errors.noMessage", "lang":lang, "args":msgId}, function(data){
			if(data.message!=null) alert(data.message);
		});
	}
	return message;
}
function callMsg(msgId, args, lang){
	return callMsgAjax(msgId, args, lang, "/cm/msg/getMessageAjx.do");
}
function callTerm(msgId, args){
	return callMsgAjax(msgId, args, null, "/cm/msg/getTermAjx.do");
}
var noAlertMsg = false;
function alertMsg(msgId, args, lang){
	var message = callMsg(msgId, args, lang);
	if(message!=null && !noAlertMsg){
		alert(message);
	}
}
var noConfirmMsg = false;
function confirmMsg(msgId, args, lang){
	var message = callMsg(msgId, args, lang);
	if(message!=null && !noConfirmMsg){
		return confirm(message);
	}
	return false;
}

function callHtml(url, param, failHandler, errHandler){
	var txtHtml = null;
	callAjax(url, param, function(html){
		txtHtml = html;
	}, failHandler, errHandler, false, true);
	return txtHtml;
}

function toContextURL(url){
	if(gContextPath==null) return url;
	return gContextPath+url;
}

(function($){
	$.fn.tagName = function() {
		return (this.get(0)!=null) ? this.get(0).tagName.toLowerCase() : null;
	};
	$.fn.checkInput = function(va) {
		this.each(function(index, obj){
			obj.checked = (va!=false);
			$(obj).uniform.update();
		});
		return this;
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
