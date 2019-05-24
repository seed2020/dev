
// 해당 ID value에 comma 추가
function commifyInput(id){
	var obj = $("#"+id);
	obj.val(addComma(obj.val()));
}
//해당 ID value에 comma 제거
function uncommifyInput(id){
	var obj = $("#"+id);
	obj.val(removeComma(obj.val()));
}
//comma(,) 제거
function removeComma(no){
	if(no==null) return "";
	return no.replace(/,/g, '');
}
// comma(,) 추가
function addComma(no){
	if(no==null) return "";
	no = no.replace(/,/g, '');
	var regExp = /(-?[0-9]+)([0-9]{3})/;
	while(regExp.test(no)) no = no.replace(regExp, '$1,$2');
	return no;
}

//utf-8 길이를 체크해서 해당 길이 만큼 자름
function cutUtf8(txt, size){
	if(txt==null || txt=='') return txt;
	try{len = parseInt(len,10);}
	catch(e){ return ""; }
	var i, c, sum=0, len=txt.length, buffer=[];
	for(i=0;i<len;i++){
		c = txt.charCodeAt(i);
		if(c<128) sum++;
		else if(c<2048) sum += 2;
		else if(c<65536) sum += 3;
		else if(c<2097152) sum += 4;
		else if(c<67108864) sum += 5;
		else sum += 6;
		if(sum<=size) buffer.push(txt.charAt(i));
		else return buffer.join("");
	}
	return buffer.join("");
}

//utf-8 길이
function getUtf8Length(txt){
	if(txt==null) return 0;
	var i, c, sum=0, len=txt.length;
	for(i=0;i<len;i++){
		c = txt.charCodeAt(i);
		if(c<128) sum++;
		else if(c<2048) sum += 2;
		else if(c<65536) sum += 3;
		else if(c<2097152) sum += 4;
		else if(c<67108864) sum += 5;
		else sum += 6;
	}
	return sum;
}

// 범위내의 utf-8 길이인지 검사
function isInUtf8Length(txt, min, max) {
	if(txt==null) return -1;
    var size = getUtf8Length(txt);
    if(max!=null && size > max) return 1;
    if(min!=null && size < min) return -1;
    return 0;
}

// 
function isEmptyVa(va){
	return va!=null && va.trim().length==0;
}

// 범위내의 길이인지 검사
function isInLength(txt, min, max) {
	var len = txt.length;
    if(max!=null && len > max) return 1;
	if(min!=null && len < min) return -1;
    return 0;
}

// 범위내의 정수인지 검사
function isInInt(txt, min, max) {
	var no = parseInt(txt, 10);
    if(max!=null && no > max) return 1;
    if(min!=null && no < min) return -1;
    return 0;
}

// 범위내의 소수인지 검사
function isInFloat(txt, min, max) {
	var no = parseFloat(txt, 10);
    if(max!=null && no > max) return 1;
    if(min!=null && no < min) return -1;
    return 0;
}

// allowedChars 이외의 것 제거
function removeNotAllowedChars(va, allowedChars){
	var buffer = new StringBuffer(), i, len = va.length;
	for(i=0;i<len;i++){
		if(allowedChars.indexOf(va.charAt(i))!=-1) buffer.append(va.charAt(i));
	}
	return buffer.toString();
}
// 메일 체크용
function checkMail(inputTitle, value){
	if(value==null || value=='') return true;
	if(!isEmail(value)){
		alertMsg("cm.input.check.notValid", inputTitle);
		return false;
	}
	return true;
}
// 메일 형식 체크
function isEmail(email){
	if(email==null || email=='') return false;
	var regExp = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	return regExp.test(email);
}
// 주민등록번호체크
function checkSSN(ssn){
	if(ssn==null||ssn.length!=13||parseInt(ssn)==NaN) return false;
	var i, sum=0;
	for(i=0;i<12;i++){ sum += parseInt(ssn.charAt(i)) * ((i%8)+2);	}
	return ((11 - (sum % 11)) % 10) == parseInt(ssn.charAt(12));
}
// 일괄 체크
var validator = {
	handlerMap:new ParamMap(),
	titleMap:new ParamMap(),
	validaterMap:new ParamMap(),
	focusId:null,
	addHandler:function(id, handler){ this.handlerMap.put(id, handler); },
	getHandler:function(id){ return this.handlerMap.get(id); },
	removeHandler:function(id){ this.handlerMap.remove(id); },
	addValidator:function(id, handler){ this.validaterMap.put(id, handler); },
	getValidator:function(id){ return this.validaterMap.get(id); },
	addTitle:function(id, title){ this.titleMap.put(id, title); },
	getTitle:function(id){ return this.titleMap.get(id); },
	validate:function(areaId){
		var areaMap = new ParamMap().getData(areaId, null, "id");
		var handlerMap = this.handlerMap;
		return areaMap.each(function(id, va){
			var handler = handlerMap.get(id);
			if(handler!=null && handler(id, va)==false){
				if(validator.focusId!=null){
					$("#"+validator.focusId).focus();
					validator.focusId = null;
				} else {
					if(typeof areaId == 'string'){
						$("#"+areaId+" #"+id).focus();
					} else {
						$("#"+id).focus();
					}
				}
				return false;
			}
		});
	},
	escapeCodes:[8, 9, 16, 17, 18, 27, 35, 36, 37, 38, 39, 40, 46, 229]
};

//휴대전화 체크용
function checkPhone(inputTitle, value){
	if(value==null || value=='') return true;
	if(!isPhone(value)){
		alertMsg("cm.input.check.notValid", inputTitle);
		return false;
	}
	return true;
};

// 전화번호 체크
function isPhone(no){
	if(no==null || no=='') return false;	
	var regExp = /[0-9]{2,3}[0-9]{3,4}[0-9]{4}|[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$/;
	return regExp.test(no);
};

// 전화번호 형식 체크후 '-'를 붙여준다
function fnPhoneInput(obj , format){
	var regExp = /(^0[3456][0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	if( obj.value.length > 7 ){
		if(obj.value.substring(0,2) == '02'){ regExp = /(^0[0-9]{1})([0-9]{3,4})([0-9]{4})$/;}
		if(obj.value.substring(0,2) == '01'){ regExp =  /(^01[016789])([0-9]{3,4})([0-9]{4})$/;}
		if(obj.value.substring(0,3) == '070'){ regExp =  /(^070)([0-9]{3,4})([0-9]{4})$/;}
		if(obj.value.substring(0,2) == '15'){ regExp =  /(^15[0-9]{2})([0-9]{4})$/;}
		if(regExp.test(obj.value)){
			format = format || '-';
			obj.value = RegExp.$1 +format+RegExp.$2+(obj.value.substring(0,2) == '15' ? '' : format+RegExp.$3 );
		}else{ obj.value = "";}
	}else {obj.value = "";}
};

// 전화번호 '-' 제거
function fnPhoneUnInput(obj){
	var regExp = /(^0[0-9]{1,2})-([0-9]{3,4})-([0-9]{4})$/;
	if (regExp.test(obj.value)){
		obj.value = RegExp.$1 +""+RegExp.$2+""+RegExp.$3;
	}
};

// 소수점 체크
function isChkFloat(obj){
	if(obj.value != ''){
		var regExp = /^(\d{1,3})([.])(\d{1})$/;
		if (!regExp.test(obj.value)){
			obj.value = '';
		}
	}
	
} 

// 유효성 검증 
function checkValidator(typ, inputTitle, va){
	if(typ==null || typ=='') return true;
	if(typ=='TEL'){
		return checkPhone(inputTitle, va);
	}else if(typ=='EMAIL'){
		return checkMail(inputTitle, va);
	}else if(typ=='SSN'){
		 if(!checkSSN(va)){
			 alertMsg("cm.input.check.notValid", inputTitle);
			return false;
		 }
		return true;
	}
	return true;
}
