
var $formBuilder = {
	isMultiLang:true, // 다중 언어 지원여부
	langTypCd:'ko', // 기본언어코드
	langList:null, // 지원 언어 목록
	gAttrAreaId:null, // 현재 선택된 속성 AreaId
	gComponentArea:null, // 현재 선택된 컴포넌트 Area
	componentIdMap:null, // 컴포넌트 ID목록맵
	attrIdMap:null, // 속성ID목록맵
	applyType:'m', // 속성 변경 적용 방법 - m : 수동 , a: 자동
	addMultiCompo:true, // 다중 컴포넌트 추가여부[체크박스 선택된 셀 기준]
	skin:'blue', // 기본 스킨
	formCdGrpList:null, // 양식 코드그룹 목록
	formCdListMap:null, // 양식 코드 맵
	historyList:null, // 히스토리 목록
	singleValueMap:null, // 단독사용 컴포넌트(라디오,체크박스) 의 value 맵
	combUseList:['text', 'date', 'datetime', 'time', 'period', 'number', 'select', 'checkbox', 'checkboxSingle', 'radio', 'radioSingle', 'label'], // 조합 사용가능 컴포넌트 목록 [컴포넌트를 여러개 붙여서 사용할 때]
	fontOptions:{
		fontFamilies:null,
		fontSizes:null,
		fontStyle:null
	}, // 폰트 옵션[글꼴, 사이즈, 스타일(굵게, 기울임, 밑줄)]
	init:function(option){
		//loadCSS('/css/upload.css');
		if(this.componentIdMap==null)
			this.componentIdMap=new Map();
		if(this.attrIdMap==null)
			this.attrIdMap=new Map();
		if(this.historyList==null)
			this.historyList=[];
		if(option!=undefined){
			// 컴포넌트 시퀀스 ID
			if(option['maxIdList']!=undefined){
				$.each(option['maxIdList'], function(key, va){
					$formBuilder.setComponentId(key, va);
				});	
			}
			// 스킨
			if(option['skin']!=undefined){
				this.skin=option['skin'];
			}
			
			// 기본 언어코드
			if(option['langTypCd']!=undefined){
				this.langTypCd=option['langTypCd'];
			}
			// 언어 목록
			if(option['langList']!=undefined && option['langList'].length>0){
				this.langList=option['langList'];
			}else{
				this.langList=['ko'];
			}
			// 글꼴
			if(option['fontFamilies']!=undefined){
				this.fontOptions['fontFamilies']=option['fontFamilies'];
			}
			// 폰트 사이즈
			if(option['fontSizes']!=undefined){
				this.fontOptions['fontSizes']=option['fontSizes'];
			}
			// 폰트 사이즈
			if(option['fontStyle']!=undefined){
				this.fontOptions['fontStyle']=option['fontStyle'];
			}
		}
	},
	addHistory:function(typ, id){ // 히스토리 추가
		
	},
	removeHistory:function(idx){ // 히스토리 삭제
		if(this.historyList!=null){
			if(idx===undefined) idx=this.historyList.length-1;
			delete this.historyList[idx];
		}
	},
	restoreHistory:function(idx){ // 히스토리 복원
		if(this.historyList!=null){
			if(idx===undefined) idx=this.historyList.length-1;
		}
	},
	createAttr:function(areaId, el, dataTyp){
		//$('#'+areaId).find('ul.table_list').closest('div').hide();
		if($(el).attr('id')==undefined) return;
		$('#'+areaId).html('');
		var component=componentList[dataTyp];
		var seqId=$(el).attr('id');
		var attrAreaId='attr_'+seqId;
		var attrArea=$('#'+attrAreaId);
		//var isNew = attrArea.length==0;
		var isNew = true;
		var title;
		if(isNew){
			buildValidator.init(); // 유효증 검증 초기화
			var fields=component.fields;
			var buffer=[];
			buffer.push('<div id="'+attrAreaId+'" style="width:98%;display:inline-block;"><ul class="table_list">');
			var isMultiLang=this.isMultiLang, fb=this;
			
			// 조합 가능여부
			if(this.isUsbComb(dataTyp)){
				title=isMultiLang ? callMsg('wf.form.attr.fields.use.comb') : '조합사용';
				buffer.push('<li class="list"><div class="list_wrap">');
				buffer.push('<ul class="attr_list"><li><input name="useCombYn" id="useCombYn" value="Y" title="" type="checkbox">');
				buffer.push('<label for="useCombYn">'+title+'</label></li></ul></div>');
				//buffer.push('<div class="list_wrap"><div class="title">컴포넌트를 조합할 수 있습니다.</div></div>');
				buffer.push('</li>');
			}
			
			var colorpickers = [];
			$.each(fields, function(key, obj){
				title=isMultiLang && obj.titleId!=undefined ? callMsg(obj.titleId) : obj.label;
				buffer.push('<li class="list">');
				style='';
				$bodyAreaId=attrAreaId+'_'+key;
				fb.createTitle($bodyAreaId, title, obj, key, buffer); // 타이틀
				buffer.push('<div class="list_wrap '+obj.type+'" id="'+$bodyAreaId+'" data-seq="1">');
				fb.createElement(key, seqId, obj, buffer); // 속성
				buffer.push('</div>');
				if(obj.explain!=undefined){ // 설명
					title=isMultiLang && obj.explain.titleId!=undefined ? callMsg(obj.explain.titleId) : obj.explain.label;
					fb.createTitle($bodyAreaId, title, obj.explain, key, buffer);
				}
				buffer.push('</li>');
				this.keyIdx++;
				if(obj.type=='btn' && obj.value=='colorpicker'){
					colorpickers.push(obj.name);
				}
			});
			
			buffer.push('</ul></div>');
			$('#'+areaId).append(buffer.join(''));
			$('#'+attrAreaId).find("input, textarea, select, button").uniform();
			
			if(colorpickers.length>0){ // 색상선택 팝업 추가
				setColorPicker(colorpickers);
			}
			
			// 유효성 검증 이벤트 삽입
			if(buildValidator.listMap!=null){
				var map=buildValidator.listMap.map;
				$.each(map, function(key, va){
					buildValidator.setValidator(key, va.maxByte, va.minByte, va.mandatory, va.valueAllowed, va.valueNotAllowed, va.valueOption, va.commify, va.format);
				});
			}
		}else{
			attrArea.show();
		}		
		this.gComponentArea=el;
		this.gAttrAreaId=attrAreaId;
		
		// 기존 속성 세팅
		this.setAttr(el, this.gAttrAreaId);
		
		if(this.applyType=='a')
			this.setChnAttrEvt($('#'+attrAreaId));
		
	},
	createTitle:function($id, title, obj, key, buffer){ // 타이틀 생성
		if(title!=null || obj.btnList!=undefined || obj.titleSelect!=undefined){
			buffer.push('<div class="list_wrap">');
			if(title!=null) {
				buffer.push('<div class="title">');
				if(obj.required!=undefined && obj.required)
					createMandatory(buffer, null, null);
				buffer.push(title+'</div>');
			}
			if(obj.btnList!=undefined){
				buffer.push('<div class="btnArea">'+createBtnList($id, obj.btnList)+'</div>');
			}
			if(obj.titleSelect!=undefined){
				buffer.push('<div class="titleSelect">'+createSelectList($id, key, obj)+'</div>');
			}
			buffer.push('</div>');
		}
		return buffer;
	},
	createElement:function(name, prefix, obj, buffer){ // create element
		var id=prefix+'_'+name;
		var title=$formBuilder.isMultiLang && obj.titleId!=undefined ? callMsg(obj.titleId) : '';
		
		if(obj.type=='input' && obj.name!=undefined){
			name=obj.name;
			id+='_'+obj.name;
		}
		
		buildValidator.setValidatorTitle(id, title);
		if(obj.type=='input'){
			var isLabel=obj.labelId!=undefined;			
			if(isLabel){
				$label=this.isMultiLang && obj.labelId!=undefined ? callMsg(obj.labelId) : obj.label;
				buffer.push('<label for="'+id+'">'+$label+'</label>');
			}
			if(obj.wdth!=undefined) $style=' style="width:'+obj.wdth+';"';
			else $style=' style="width:98%;"';
			var onFocus=obj.onFocus!=undefined ? ' onfocus="'+obj.onFocus+'(this, event);"' : '';
			var onBlur=obj.onBlur!=undefined ? ' onblur="'+obj.onBlur+'(this, event);"' : '';
			// input_disabled
			$maxLength=obj.maxLength!=undefined ? ' maxLength="'+obj.maxLength+'"' : '';
			if(this.isMultiLang && obj.langTyp!=undefined && obj.langTyp){
				buffer.push(getLangInputData(id, name, title, obj));
			}else{
				buffer.push('<input name="'+name+'" id="'+id+'" title="'+title+'"'+(obj.value!=undefined ? 'value="'+obj.value+'" ' : '')
						+'type="'+(obj.hidden!=undefined && obj.hidden ? 'hidden' : 'text')+'" '+(obj.readonly!=undefined && obj.readonly ? 'readonly="readonly" ' : '') 
						+ (obj.required!=undefined && obj.required ? ' data-required="Y"' : '')+$style+$maxLength+onFocus+onBlur+'>');
				buildValidator.set(id, obj.maxByte, obj.minByte, obj.mandatory, obj.valueAllowed, obj.valueNotAllowed, obj.valueOption, obj.commify, obj.format);
			}
			
		}else if(obj.type=='multi'){
			buffer.push('<ul class="attr_list">');
			$.each(obj.value, function(idx, va){
				//if(va.name!=undefined) name=va.name;
				//$subId=id+'_'+idx;
				buffer.push('<li>');
				$formBuilder.createElement(name, prefix, va, buffer);
				buffer.push('</li>');
			});
			buffer.push('</ul>');
		}else if(obj.type=='radio' || obj.type=='checkbox'){
			if(obj.name!=undefined) name=obj.name;
			var subId;
			buffer.push('<ul class="attr_list">');
			
			if(obj.value!=undefined){
				$.each(obj.value, function(idx, va){
					$label=$formBuilder.isMultiLang && va.labelId!=undefined ? callMsg(va.labelId) : va.label!=null ? va.label : '';
					title=$formBuilder.isMultiLang && obj.titleId!=undefined ? callMsg(obj.titleId) : '';
					subId=id+'_'+idx;
					buffer.push('<li class="attr_check">');
					buffer.push('<input name="'+name+'" id="'+subId+'" '+(va.value!=undefined ? 'value="'+va.value+'"' : '')+' title="'+title+'" type="'+obj.type+'" '+(va.checked ? 'checked="checked"' : '')+'>');
					if($label!=null) buffer.push('<label for="'+subId+'">'+$label+'</label>');
					buffer.push('</li>');
				});
			}
			
			if(obj.valueTyp=='fontOptions'){
				var valueList=this.fontOptions[name];
				$.each(valueList, function(idx, va){
					title=$formBuilder.isMultiLang && va.titleId!=undefined ? callMsg(va.titleId) : va.title;
					subId=id+'_'+idx;
					name=va.name!=undefined ? va.name : name;
					buffer.push('<li class="attr_check">');
					buffer.push('<input name="'+name+'" id="'+subId+'" '+(va.value!=undefined ? 'value="'+va.value+'"' : '')+' title="'+title+'" type="'+obj.type+'" '+(va.checked ? 'checked="checked"' : '')+'>');
					buffer.push('<label for="'+subId+'">'+title+'</label>');
					buffer.push('</li>');
				});
			}
			
			buffer.push('</ul>');
		}else if(obj.type=='select'){
			buffer.push('<select name="'+name+'" id="'+id+'">');
			
			if(obj.value!=undefined){
				$.each(obj.value, function(index, option){
					title=$formBuilder.isMultiLang && option.titleId!=undefined ? callMsg(option.titleId) : option.label;
					buffer.push('<option value="'+option.value+'" '+(option.selected ? 'selected="selected"' : '')+'>'+title+'</option>');
				});
			}
			if(obj.valueTyp!=undefined){
				if(obj.valueTyp=='fontOptions'){
					
					var emptyValue=obj.emptyValue!=undefined ? obj.emptyValue : null;
					if(emptyValue!=null){
						title=$formBuilder.isMultiLang ? callMsg('cm.select.actname') : '선택';
						buffer.push('<option value="'+emptyValue+'">'+title+'</option>');
					}
					
					var valueList=this.fontOptions[name];
					$.each(valueList, function(index, option){
						title=$formBuilder.isMultiLang && option.titleId!=undefined ? callMsg(option.titleId) : option.title;
						buffer.push('<option value="'+option.value+'" '+(option.selected ? 'selected="selected"' : '')+'>'+title+'</option>');
					});
				}
			}
			buffer.push('</select>');
		}else if(obj.type=='textarea'){
			if(obj.name!=undefined) name=obj.name;
			buffer.push('<textarea data-type="textarea" title="'+title+'" style="min-height:100px;width:98%;" name="'+name+'" id="'+id+'"'+(obj.readonly!=undefined && obj.readonly ? ' readonly="readonly" ' : '')+' ></textarea>');
		}else if(obj.type=='textarea-split'){
			$readonly=obj.readonly!=undefined ? ' readonly="'+obj.readonly+'" class="textarea_disabled"' : '';
			buffer.push('<textarea data-type="textarea-split" title="'+title+'" style="min-height:200px;width:98%;" name="'+name+'" id="'+id+'"'+$readonly+(obj.required!=undefined && obj.required ? ' data-required="Y"' : '')+'>');
			$.each(obj.value, function(index, option){
				if(index>0) buffer.push('\n');
				buffer.push(option);
			});
			buffer.push('</textarea>');
		}else if(obj.type=='btn-del'){
			buffer.push('<a href="javascript:;" onclick="attrDelRow(this, event);" class="sbutton button_small" title="삭제"><span>삭제</span></a>');
		}else if(obj.type=='btn'){
			buffer.push('<input name="'+obj.name+'" id="'+obj.name+'" type="hidden">');
			//if(obj.value=='colorpicker'){ // 색상 버튼
			//	addColorPicker(buffer, obj);
			//}
		}else{
			
		}
	}, // 조합가능 여부
	isUsbComb:function(type){
		var comb=false;
		if(type!=undefined){
			var combUseList=this.combUseList;
			for(var i in combUseList){
				if(type==combUseList[i]){
					comb=true;
					break;
				}
			}
		}
		return comb;
	},
	saveAttr:function(){ // 입력한 속성 json 으로 저장
		if($('#'+this.gAttrAreaId).css('display')=='none' || this.gComponentArea==null) return;
		var isStop=false;
		var arrs=$('#'+this.gAttrAreaId).find(':text[data-required="Y"], textarea[data-required="Y"]');
		arrs.each(function(){
			if($(this).val()==''){
				alertMsg('cm.noti.mandatory', $(this).attr('title'));
				isStop=true;
				$(this).focus();
				return false;
			}
		});
		if(isStop) return;
		//var data = $('#'+this.gAttrAreaId).find('*').not('input[type="checkbox"]').serializeArray();
		var data = $('#'+this.gAttrAreaId).find('*').serializeArray();
		/*$.each(data, function(idx, obj){
			console.log(obj.name+'='+obj.value);
		});*/
			
		data=objToJson(data, false, true);
		setJsonData(this.gComponentArea, data, this.setAttrApply);
	},
	clearAttr:function(areaId){
		$.each(json, function(name, value){
			$('#'+areaId).find(':text#'+name+', select#'+name+', textarea#'+name).val('');
			$('#'+areaId).find(':checkbox#'+name+'[value="'+json.value+'"]').prop('checked', false);
			$('#'+areaId).find('textarea#'+name).text('');
		});
	},
	setAttr:function(el, areaId){
		var jsonStr=$(el).attr('data-json');
		if(jsonStr==undefined || jsonStr=='') return;
		var json=JSON.parse(jsonStr);
		$.uniform.restore($('#'+areaId).find("input, textarea, select, button"));
		$.each(json, function(name, value){
			$('#'+areaId).find(':text[name="'+name+'"], :hidden[name="'+name+'"], select[name="'+name+'"], textarea[name="'+name+'"]').val(value);
			$('#'+areaId).find(':radio[name="'+name+'"][value="'+value+'"]').prop('checked', true);
			$('#'+areaId).find(':checkbox[name="'+name+'"]').val(value.split(','));			
			if((name!="chkTypCd" || (name=="chkTypCd" && value!='')) && $('#'+areaId).find('select[name="'+name+'"]').length>0 && $('#'+areaId).find('select[name="'+name+'"]').attr('onchange')){
				$('#'+areaId).find('select[name="'+name+'"]').trigger('change');
			}
			$('#'+areaId).find('textarea[name="'+name+'"]').text(value);
		});
		$('#'+areaId).find("input, textarea, select, button").uniform();
	}, // json 데이타를 실제 적용
	setAttrApply:function(el, jsonStr){
		if(jsonStr==undefined) return;
		//$.uniform.restore($(el).find('input, textarea, select, button'));
		var dataTyp=$(el).attr('data-colTyp');
		var json=JSON.parse(jsonStr);
		//var json=objToJson(jsonObj, false);
		
		var elId=$(el).attr('id');
		//var fileds=jsonObj['fields'];
		var arrs=null;
		var obj=null;
		if(json['id']!=undefined){
			$(el).find("input, textarea, select").attr('id', json['id']);
			$(el).find("input, textarea, select").attr('name', json['id']);
		}
		if(json['nameRescVa_'+$formBuilder.langTypCd]!=undefined){
			if(dataTyp=='radioSingle' || dataTyp=='checkboxSingle'){ // 라디오 및 체크박스 단독사용일 경우 속성을 복사한다.
				var singleId=$(el).attr('data-singleid');
				var singleList=$(el).closest('div#itemsArea').find('div.component_list[data-singleid='+singleId+']');
				if(singleList!=null){
					var elObj;
					$.each(singleList, function(){
						if(elId==$(this).attr('id')) return true;
						//elObj=$(this).closest('div.component_list');
						$(this).attr('data-name', json['nameRescVa_'+$formBuilder.langTypCd]);
						$(this).attr('data-json', jsonStr);
					});
				}
			}else{
				$(el).attr('data-name', json['nameRescVa_'+$formBuilder.langTypCd]);
			}
		}
		
		if(dataTyp=='label'){
			if($(el).find('label.header').prev())
				$(el).find('label.header').prev().remove();
			if(json['required']!=undefined && json['required']=='Y')
				$(el).find('label.header').before(createMandatory(null, null, null));
			if(json['labelRescVa_'+$formBuilder.langTypCd]!=undefined){
				$(el).attr('data-name', json['labelRescVa_'+$formBuilder.langTypCd]);
				$(el).find('label').eq(0).text(json['labelRescVa_'+$formBuilder.langTypCd]);
			}
			
			if(json['fontFamilies']!=undefined){
				$(el).css('font-family', json['fontFamilies']);
			}
			if(json['fontSizes']!=undefined){
				$(el).css('font-size', json['fontSizes']);
			}
			if(json['fontWeight']!=undefined){
				$(el).css('font-weight', json['fontWeight']);
			}else{
				$(el).css('font-weight', '');
			}
			if(json['fontStyle']!=undefined){
				$(el).css('font-style', json['fontStyle']);
			}else{
				$(el).css('font-style', '');
			}
			if(json['textDecoration']!=undefined){
				$(el).css('text-decoration', json['textDecoration']);
			}else{
				$(el).css('text-decoration', '');
			}
			if(json['fontColor']!=undefined){
				$(el).css('color', json['fontColor']);
			}
			
			/*if(json['cellHeader']!=undefined){
				$(el).closest('td').removeClass('body_ct');
				$(el).closest('td').addClass('head_ct');
			}else{
				$(el).closest('td').removeClass('head_ct');
				$(el).closest('td').addClass('body_ct');
			}*/
		}else if(dataTyp=='text' || dataTyp=='number' || dataTyp=='calculate'){
			obj=$(el).find('input').eq(0);
		}else if(dataTyp=='textarea'){
			obj=$(el).find('textarea').eq(0);
		}else if(dataTyp=='user' || dataTyp=='dept'){
			obj=$(el).find('div.title span').eq(0);
			var txtKey=dataTyp=='dept' ? 'orgNms' : 'userNms';
			if(json[txtKey]!=''){
				obj.text(json[txtKey]);
			}
		}else if(dataTyp=='select'){
			obj=$(el).find('select').eq(0);
			obj.find('option').remove();
			if(json['chkList']!=''){
				var options=json['chkList'];
				arrs=options.trim().split('\r\n');
				var buffer=[];
				$.each(arrs, function(idx, txt){
					buffer.push('<option value="'+txt+'"');
					if(idx==0) buffer.push(' selected="selected"');
					buffer.push('>'+txt+'</option>');
				});
				obj.append(buffer.join(''));
			}
		}else if(dataTyp=='radio' || dataTyp=='checkbox'){
			obj=$(el).find('input[type="'+(dataTyp=='radio' ? 'radio' : 'checkbox')+'"]').eq(0);
			$(el).find('ul.attr_list').remove();
			if(json['chkList']!=''){
				$.uniform.restore($(el).find('input, textarea, select, button'));
				var chkList=json['chkList'];
				arrs=chkList.trim().split('\r\n');
				var buffer=[];
				buffer.push('<ul class="attr_list">');
				var styleClass;
				$.each(arrs, function(idx, va){
					styleClass='attr_check';
					if(va.length>1 && va.charAt(0)==';') {
						styleClass+=' line_enter';
						va=va.substring(1);
					}
					buffer.push('<li class="'+styleClass+'">');
					buffer.push('<div style="float:left;"><input name="'+elId+'" id="'+(elId+'_'+idx)+'" value="'+va+'" type="'+dataTyp+'"></div>');
					buffer.push('<span>'+va+'</span>');
					buffer.push('</li>');
				});
				buffer.push('</ul>');
				$(el).find('div.noSelect').append(buffer.join(''));
				$(el).find("input, textarea, select, button").uniform();
			}
			if(json['lout']!=undefined && json['lout']=='height')
				$(el).find('ul.attr_list > li').css('width', '98%');
				
				
		}else if(dataTyp=='date' || dataTyp=='time'){
			if(json['startText']!=undefined)
				$(el).find('input[id^="'+dataTyp+'"]').eq(0).val(json['startText']);
		}else if(dataTyp=='period'){
			if(json['startText1']!=undefined)
				$(el).find('input[id^="'+dataTyp+'"]').eq(0).val(json['startText1']);
			if(json['startText2']!=undefined)
				$(el).find('input[id^="'+dataTyp+'"]').eq(1).val(json['startText2']);
		}else if(dataTyp=='datetime'){
			if(json['startDate']!=undefined)
				$(el).find('input[id^="'+dataTyp+'"]').eq(0).val(json['startDate']);
			if(json['startTime']!=undefined)
				$(el).find('input[id^="'+dataTyp+'"]').eq(1).val(json['startTime']);
		}else if(dataTyp=='file'){ // 이미지
			
		}else if(dataTyp=='editor'){ // 이미지
			
		}
		
		if(json['labelAlign']!=undefined)
			$(el).find('div.noSelect').eq(0).attr('style', 'text-align:'+json['labelAlign']);
		
		if(json['useCombYn']!=undefined) // 조합사용
			$(el).addClass('component_comb');
		else{
			if($(el).hasClass('component_comb')){
				$(el).removeClass('component_comb');
			}
		}
		
		if(obj!=null){
			if(json['wdth']!=undefined && json['wdth']!='')
				obj.attr('style', 'width:'+json['wdth']+json['wdthTyp']+';');
			if(json['height']!=undefined && json['height']!=''){
			    if(dataTyp=='textarea' && json['heightTyp']=='row') obj.attr('rows', json['height']);
			    else obj.attr('style', 'height:'+json['height']+(json['heightTyp']!=undefined ? json['heightTyp'] : 'px')+';');
			}
				
			if(json['placeholder']!=undefined)
				obj.attr('placeholder', json['placeholder']);
			if(json['startText']!=undefined)
				obj.val(json['startText']);
		}
		$(el).uniform.update();
		//$(el).find("input, textarea, select, button").uniform();
		
	},
	getAttrId:function(id){ // 시퀀스 ID
		var attrId = this.attrIdMap.get(id);
		if(attrId==null) attrId=1;
		this.attrIdMap.put(id, attrId+1);
		return attrId;
	},
	getComponentId:function(id){ // 컴포넌트 ID
		var attrId = this.componentIdMap.get(id);
		if(attrId==null) attrId=1;
		this.componentIdMap.put(id, attrId+1);
		return attrId;
	},
	setComponentId:function(id, va){ // 컴포넌트 ID 세팅
		var attrId = this.componentIdMap.get(id);
		if(attrId==null){
			this.componentIdMap.put(id, va);
		}
	},
	getComponentMaxId:function(){ // 컴포넌트 ID최대값
		var returnId={};//, attrId;
		if(this.componentIdMap!=null){
			var map=this.componentIdMap;
			$.each(map.keys(), function(idx, key){
				returnId[key]=map.get(key);
			});
		}
		/*
		var builder=this;
		$.each(componentList, function(id, va){
			attrId = builder.componentIdMap.get(id);
			if(attrId==null) attrId=1;
			returnId[id]=attrId;
		});*/
		
		return returnId;
	},
	chkCellWdth:function(obj, dataTyp){
		if(dataTyp=='file' && $(obj).width()<510){ // 첨부파일 가로 최소 길이 510
			alertMsg('wf.msg.limit.width.file', ['510px']); // wf.msg.limit.width.file=파일은 최소 가로 길이가 {0} 이상이어야 합니다.
			return false;
		}else if(dataTyp=='editor' && $(obj).width()<720){ // 에디터 가로 최소 길이 720
			alertMsg('wf.msg.limit.width.editor', ['720px']); // wf.msg.limit.width.file=파일은 최소 가로 길이가 {0} 이상이어야 합니다.
			return false;
		}
		return true;
	},
	chkComponentCnt:function(dataTyp){ // 컴포넌트별 갯수 제한
		var maxCnts={file:1, editor:3};
		if(maxCnts[dataTyp]===undefined) return true;
		var cnt=$('#formArea div.component_list[data-coltyp="'+dataTyp+'"]').length;
		if(maxCnts[dataTyp]==cnt){
			if(dataTyp=='file') alertMsg('wf.msg.limit.cnt.file'); // wf.msg.limit.cnt.file=파일은 최대 1개만 추가 가능합니다.
			else alertMsg('wf.msg.limit.cnt.editor', [maxCnts[dataTyp]]); // wf.msg.limit.cnt.editor=에디터는 최대 3개만 추가 가능합니다.
			return false;
		}
		return true;
	},
	getNameRescJson:function(name, isDataIncl){ // 언어별 컴포넌트 이름 설정
		var nameRescText='';
		if(name===undefined) return nameRescText;
		$.each(this.langList, function(idx, lang){
			if(idx>0) nameRescText+=',';
			nameRescText+='"nameRescVa_'+lang+'":"'+name+'"';
		});
		if(isDataIncl!=undefined && isDataIncl) nameRescText=' data-json=\'{'+nameRescText+'}\'';
		return nameRescText;
	},
	addComponent:function(obj, dataTyp, isDelBtn, vaId){
		if(obj==null) return;
		// var cellColTyp=$(obj).attr('data-coltyp');
		//if($(obj).find('div[id^=label]').length>0){
		//if(cellColTyp=='label'){
		//	alertMsg('wf.msg.not.component', ['#wf.form.items.label']); // 레이블이 추가되어있어 더이상 추가할 수 없습니다.
		//	return;
		//}
		//if($(obj).find('div[id^=button]').length>0){
		//	alertMsg('wf.msg.not.component', ['#wf.form.items.button']); // 버튼이 추가되어있어 더이상 추가할 수 없습니다.
		//	return;
		//}
		
		if(!this.chkCellWdth(obj, dataTyp)) return;
		if(!this.chkComponentCnt(dataTyp)) return;
		var component=componentList[dataTyp];
		if(component===undefined) return;
		var fields=component.fields;
		var coKey=component['id'];
		//var attrId=this.getAttrId(coId);
		coId=coKey+''+this.getComponentId(coKey);
		
		// var vaId=dataTyp+''+attrId;
		if(vaId===undefined || vaId==null)
			vaId=coId;
		var buffer=[];
		buffer.push('<div class="component_list component_area" id="'+coId+'Area" data-id="'+coId+'" data-colTyp="'+dataTyp+'"'+(fields.name!=undefined ? ' data-name="'+fields.name.value+'"' : '')+' data-tooltip-text="'+coId+'" onclick="setProp(this, event);"');
		if(dataTyp=='radioSingle' || dataTyp=='checkboxSingle'){
			buffer.push(' data-singleid="'+vaId+'"');
		}
		
		if(fields.name!=undefined){ // 컴포넌트가 생성되면 언어별 이름을 json으로 부여
			var nameRescText=this.getNameRescJson(fields.name.value, true);
			if(nameRescText!='') buffer.push(nameRescText);
		}
		buffer.push('>');
		if(dataTyp=='number')
			buffer.push('<div class="tooltip_area" style="display:none;">'+coId.toUpperCase()+'</div>');
		buffer.push('<div class="noSelect">');
		if(dataTyp=='label'){
			buffer.push('<label for="'+coId+'" class="header">&nbsp;</label>');
		}else if(dataTyp=='text' || dataTyp=='number' || dataTyp=='calculate'){
			buffer.push('<input name="'+vaId+'" id="'+vaId+'" value="" type="text" style="width:90%;" readonly="readonly">');
		}else if(dataTyp=='textarea'){
			buffer.push('<textarea id="'+vaId+'" name="'+vaId+'" style="min-height:100px;width:90%;" readonly="readonly"></textarea>');
		}else if(dataTyp=='user' || dataTyp=='dept'){
			buffer.push('<div class="title" style="float:left;"><span></span></div>');
			buffer.push('<div class="sbutton button_small" style="float:right;margin-right:15px;"><span>'+(this.isMultiLang ? callMsg('cols.'+dataTyp) : '찾기')+'</span></div>');
		}else if(dataTyp=='select'){
			buffer.push('<select id="'+vaId+'" name="'+vaId+'">');
			$.each(fields['chkList'].value, function(index, val){
				buffer.push('<option>'+val+'</option>');
			});
			buffer.push('</select>');
		}else if(dataTyp=='radio' || dataTyp=='checkbox'){
			var list=fields['chkList'].value;
			buffer.push('<ul class="attr_list">');
			$.each(list, function(idx, va){
				buffer.push('<li class="attr_check">');
				buffer.push('<div style="float:left;"><input name="'+vaId+'" id="'+(vaId+'_'+idx)+'" value="'+va+'" type="'+dataTyp+'"></div>');
				buffer.push('<span>'+va+'</span>');
				buffer.push('</li>');
			});
			buffer.push('</ul>');
			
		}else if(dataTyp=='radioSingle' || dataTyp=='checkboxSingle'){
			buffer.push('<div class="attr_check"><input name="'+vaId+'" id="'+coId+'" value="'+coId+'" type="'+dataTyp.replace(/Single/,'')+'"></div>');
		}else if(dataTyp=='date'){
			buffer.push(addDate(vaId, 'calendar'));
		}else if(dataTyp=='period'){
			buffer.push('<ul class="attr_list">');
			buffer.push('<li>');
			buffer.push(addDate(vaId+'Strt', 'calendar'));
			buffer.push('</li>');
			buffer.push('<li>~</li>');
			buffer.push('<li>');
			buffer.push(addDate(vaId+'End', 'calendar'));
			buffer.push('</li>');
			buffer.push('</ul>');
		}else if(dataTyp=='time'){
			buffer.push(addDate(vaId, 'time'));
		}else if(dataTyp=='datetime'){
			buffer.push(addDate(vaId, 'calendartime'));
		}else if(dataTyp=='file'){
			buffer.push('<div class="file2"><div class="file3"><img src="/images/etc/file1.png"/></div></div>');
		}else if(dataTyp=='editor'){
			buffer.push('<div class="editor2"><div class="editor3"><img src="/images/etc/editor1.png"/></div></div>');
		}else if(dataTyp=='image'){
			buffer.push('<div class="image_profile"><dl><dd class="photo"><img id="bcImage" src="/images/'+this.skin+'/photo_noimg.png" width="88px"/></dd>');
			buffer.push('<dd class="photo_btn">');
			buffer.push('<div class="sbutton button_small"><span>'+(this.isMultiLang ? callMsg('wf.btn.srch') : '찾기')+'</span></div>');
			buffer.push('</dd></dl></div>');
		}else if(dataTyp=='button'){
			buffer.push('<div style="margin-top:2px;">');
			buffer.push('<a href="javascript:;"><img src="/images/'+this.skin+'/ico_wadd.png" width="20" height="20" title="'+(this.isMultiLang ? callMsg('cm.btn.plus') : '행추가')+'" /></a>');
			buffer.push('<a href="javascript:;"><img src="/images/'+this.skin+'/ico_wminus.png" width="20" height="20" title="'+(this.isMultiLang ? callMsg('cm.btn.minus') : '행삭제')+'" /></a>');
			buffer.push('</div>');
		}else if(dataTyp=='addr'){
			buffer.push('<table style="width: 100%;text-align:left;" border="0" cellspacing="0" cellpadding="0">');
			buffer.push('<tbody><tr><td><table border="0" cellspacing="0" cellpadding="0"><tbody><tr><td>');
			buffer.push('<input name="compZipNo" title="Zip code" class="input_center uniform-input text" id="compZipNo" style="width: 80px; -ms-ime-mode: disabled;" maxlength="5" readonly="readonly" value="">');
			buffer.push('</td>');
			buffer.push('<td style="padding-left: 2px;"><a title="Zip Code Search - Popup" class="sbutton button_small" id="compZipBtn" href="javascript:void(0);"><span>search address</span></a></td>');
			buffer.push('<td style="padding-left: 2px;"><a title="Direct input - Popup" class="sbutton button_small" id="compZipBtn" href="javascript:void(0);"><span>Direct input</span></a></td>');
			buffer.push('</tr></tbody></table></td></tr><tr><td>');
			buffer.push('<input name="compAdr" title="Enter address" class="input_disabled uniform-input text" id="compAdr" style="width: 94%;" type="text" readonly="readonly" value="">');
			buffer.push('</td></tr></tbody></table>');
		}else if(dataTyp=='tel'){
			buffer.push('<input title="Enter phone number" class="input_center" type="text">');
		}
		buffer.push('</div>');
		if(isDelBtn===undefined || isDelBtn)
			buffer.push('<a class="close-thik" onclick="$formBuilder.delComponent(this, event);" href="#"></a>');
		buffer.push('</div>');
		$(obj).append(buffer.join(''));
		$(obj).find('div.component_list:last').find("input, textarea, select, button").uniform();
		
		return vaId;
	},
	delComponent:function(obj, event){
		var div=$(obj).closest('div.component_list');
		var id=$(div).attr('id');
		if($('#attr_'+id).length>0)
			$('#attr_'+id).remove();
		var td=$(div).closest('td');
		$(div).remove();
		$('#propBtnArea').hide();
		setProp($(td), event);
	},
	setChnAttrEvt:function(el){
		$(el).find('input, textarea, select').on('blur', function(){
			$formBuilder.saveAttr();
		});
	}
};

var StringUtil={
	UPPERS : "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
	LOWERS : "abcdefghijklmnopqrstuvwxyz",
	NUMBERS : "0123456789",
	appendNotHaveChars : function(buffer, appendString){
		if(appendString==null || appendString=='') return;
		var base = buffer.join('');
		for(var i in appendString){
			if(base.indexOf(appendString.charAt(i))<0) buffer.push(appendString.charAt(i));
		}
	},
	removeChars : function(base, removeString){
		if(removeString==null) return base;
		
		for(var k in removeString){
			for(var i=0;i<base.length;i++){
				if(base.charAt[i]==removeString.charAt(k)) base.charAt[i] = 0;
			}
		}
		var buffer=[];
		for(i=0;i<base.length;i++){
			if(base.charAt[i]!=0) buffer.push(base.charAt[i]);
		}
		return buffer.join('');
	}
};

// 유효성 검증 객체
var buildValidator={
	listMap:null,
	init:function(){
		if(this.listMap!=null){
			var map=this.listMap.map;
			$.each(map, function(key, va){
				buildValidator.removerValidator(key);
			});
			this.listMap=null;
		}
	},
	set:function(id, maxByte, minByte, mandatory, valueAllowed, valueNotAllowed, valueOption, commify, format){
		if(this.listMap==null) this.listMap=new Map();
		if(this.listMap.get(id)!=null) return;
		this.listMap.put(id, {maxByte:maxByte, minByte:minByte, mandatory:mandatory, valueAllowed:valueAllowed, valueNotAllowed:valueNotAllowed, valueOption:valueOption, commify:commify, format:format} );
	},
	remove:function(id){
		if(this.listMap==null) return;
		this.listMap.remove(id);
	},
	setValidatorTitle:function(id, title){// 속성 내에 유효성 검증을 위한 타이틀 세팅
		if(title=='') return;
		validator.addTitle(id, title);
	},
	removerValidator:function(id){
		validator.removeHandler(id);
		$('#'+id).off('keyup, contextmenu, keypress, blur');
	},
	setValidator:function(id, maxByte, minByte, mandatory, valueAllowed, valueNotAllowed, valueOption, commify, format){ // 유효성 검증 이벤트 삽입
		var title=validator.getTitle(id);
		var charOption=null;
		var isValidator=false;
		if(valueOption!=null || valueAllowed!=null || valueNotAllowed!=null || commify=='Y'){
			isValidator=true;
			if(valueOption==null){
				if(commify=='Y') valueOption = "number";
				else valueOption = "alpha,number";
			} else if(valueOption=='email'){
				valueOption = "lower,number";
				valueAllowed = "@_-.";
			}
			var buffer = [];
			
			if(valueOption.indexOf("alpha")>=0 || valueOption.indexOf("upper")>=0) buffer.push(StringUtil.UPPERS);
			if(valueOption.indexOf("alpha")>=0 || valueOption.indexOf("lower")>=0) buffer.push(StringUtil.LOWERS);
			if(valueOption.indexOf("number")>=0){ buffer.push(StringUtil.NUMBERS); }
			if(valueOption.indexOf("float")>=0){ buffer.push(StringUtil.NUMBERS).append('.'); }
			
			if(valueAllowed!=null) StringUtil.appendNotHaveChars(buffer, valueAllowed);
			charOption = StringUtil.removeChars(buffer.join(''), valueNotAllowed);
		}
		if(!isValidator && (maxByte!=null || minByte!=null || mandatory!=null || format!=null)){
			isValidator=true;
		}
		if(isValidator){
			validator.addHandler(id, function(id, va, eventNm){
				if(mandatory!=null && mandatory && eventNm == null && isEmptyVa(va)){
					alertMsg('cm.input.check.mandatory',[title]);
					return false;
				}
				if(charOption!=null && eventNm != null) {
					var currect = removeNotAllowedChars(va, charOption);
					if(currect!=va) $('#'+id).val(currect);
				}
				if(minByte!=null || maxByte!=null){
					var ck = isInUtf8Length(va, minByte, maxByte);
					if(ck!=0){
						if(ck>0) {
							alertMsg('cm.input.check.maxbyte',[title,maxByte]);
							$('#'+id).val(cutUtf8(va, maxByte));
						}
						if(eventNm == null && ck<0) {
							alertMsg('cm.input.check.minbyte',[title,minByte]);
						}
						return false;
					}
				}
				if(format!=null && format=='date'){
					chkMnalCalendar($('#'+id)[0], null);
				}
			
			});
			// console.log('validator-id : '+id);
			$('#'+id).on('keyup',function(event) {
				var handler = validator.handlerMap.get(id);
				if(handler!=null && !validator.escapeCodes.contains(!event.charCode ? event.which : event.charCode)){
					return handler(id, $(this).val(), 'keyup');
				};
				return true;
			});
			
			if(charOption!=null){
				// console.log('charOption : '+charOption);
				var $input = $('#'+id);
				$input.css('ime-mode','disabled');
				$input.on('contextmenu',function () {if(e.preventDefault){e.preventDefault();} return false;});
				$input.keypress(function (e){
					var keyCode = (!e.charCode) ? e.which : e.charCode;
					var keyValue = String.fromCharCode(keyCode);
					if(keyCode!=0 && keyCode!=8){
						if (charOption.indexOf(keyValue) == -1){
							if(e.preventDefault){e.preventDefault();}
							return false;
						}
					}
				});
			}
		}
		if(format!=null){
			$('#'+id).on('blur',function(event) {
				if(format=='date') validMnalDate(this);
				else if(format=='time'){
					var val=$(event.target).val();
					if(val!='' && !(val.length==5 && /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(val))){
						$(event.target).val('');
					}
				}
			});
			
		}
	}
};

// 컴포넌트 클릭 이벤트 추가
function addSelectEvt(obj, event){
	$('#formArea div.component_list[data-coltyp="number"] div.tooltip_area').each(function(){
		$(this).show();
		if($(this).attr('data-eventyn')===undefined){
			$(this).attr('data-eventyn', 'Y');
			$(obj).attr('data-selectyn', 'Y');
			$(this).on('click', function(e){
				$(obj).fieldSelection($(this).text().toUpperCase());
				notEvtBubble(e);
			});
		}
	});
};

//컴포넌트 클릭 이벤트 추가
function removeSelectEvt(){
	//$('#formArea div.component_list[data-coltyp="number"] div.tooltip_area').hide();
};

function hideEvent(){
	
};

// html to json
var convertHtmlToJson = function(target){
	var jsonString = [];
	//var loutFormAreaId=$(target).attr('id');
	$(target).find('div#itemsArea').each(function(idx, item){
		//jsonString.push({loutFormAreaId:loutFormAreaId});
		//obj=jsonString[loutFormAreaId];
		//console.log('');
	});
	return JSON.stringify(jsonString); 
};


// Json to 
function setJsonData(el, data, handler){
	var jsonStr = JSON.stringify(data);
	$(el).attr('data-json', jsonStr);
	if(handler!=undefined) handler(el, jsonStr);
};

//serializeArray To Object
function objToJson(data, nullValue, isArray){
	if(data==null) return null;
	var jsonObj={};
	$.each(data, function(index, obj){
		if(nullValue && this.value=='') return true;
		if(isArray && jsonObj[this.name]!=undefined) jsonObj[this.name]=jsonObj[this.name]+','+this.value;
		else jsonObj[this.name]=this.value.replace(/\\/g, '');
	});
	return jsonObj;
};
// map list To Object
function listToJson(data, nullValue, isArray){
	if(data==null) return null;
	var jsonList=[];
	var jsonObj;
	$.each(data, function(idx, obj){
		$map=obj.map;
		jsonObj={};
		$.each($map, function(key, va){
			if(nullValue && va=='') return true;
			if(isArray && jsonObj[key]!=undefined) jsonObj[key]=jsonObj[key]+','+va;
			else jsonObj[key]=va;
		});
		if(Object.keys(jsonObj).length>0)
			jsonList.push(jsonObj);
	});
	return jsonList;
};

//색상선택 팝업 - Color Picker
function setColorPicker(arr){
	$.each(arr, function(idx, id){
		$("#"+id).spectrum({
		    showPaletteOnly: true,
		    togglePaletteOnly: true,
		    color: 'blanchedalmond',
		    palette: [
		        ["#000","#444","#666","#999","#ccc","#eee","#f3f3f3","#fff"],
		        ["#f00","#f90","#ff0","#0f0","#0ff","#00f","#90f","#f0f"],
		        ["#f4cccc","#fce5cd","#fff2cc","#d9ead3","#d0e0e3","#cfe2f3","#d9d2e9","#ead1dc"],
		        ["#ea9999","#f9cb9c","#ffe599","#b6d7a8","#a2c4c9","#9fc5e8","#b4a7d6","#d5a6bd"],
		        ["#e06666","#f6b26b","#ffd966","#93c47d","#76a5af","#6fa8dc","#8e7cc3","#c27ba0"],
		        ["#c00","#e69138","#f1c232","#6aa84f","#45818e","#3d85c6","#674ea7","#a64d79"],
		        ["#900","#b45f06","#bf9000","#38761d","#134f5c","#0b5394","#351c75","#741b47"],
		        ["#600","#783f04","#7f6000","#274e13","#0c343d","#073763","#20124d","#4c1130"]
		    ]
		});
	});
	

};

// 색상선택 팝업
function addColorPicker(buffer, obj){
	var name=obj.name;
	
	var areaId=name+'PopArea';
	var title=$formBuilder.isMultiLang ? callMsg('cm.btn.colorPicker') : '색상 선택';
	
	buffer.push('<ul class="attr_list">');
	buffer.push('<li>');
	buffer.push('<input name="'+name+'" class="input_disabled text" id="'+name+'" style="width: 60px;ime-mode: disabled;" onkeydown="onlyDelWorks(event, this);" type="text" maxlength="7" value="">');
	buffer.push('</li>');
	buffer.push('<li>');
	buffer.push('<a title="'+title+'" class="sbutton button_small" onclick="showColorPicker(\''+name+'\',\'null\');" href="javascript:void(0);"><span>'+title+'</span></a>');
	buffer.push('</li>');
	buffer.push('</ul>');	
	buffer.push('<div id="'+areaId+'" style="position:absolute; z-index:2; width:425px; display:none;">');
	buffer.push('<div id="'+areaId+'Inner" style="position:relative; width:425px; "></div>');
	buffer.push('</div>');
	//buffer.push('<iframe id="'+areaId+'Frm" style="position:absolute; z-index:1; display:none;"></iframe>');
};

//색상
function showColorPicker(id ,handler){
	var $chart = $("#"+id+"PopArea");
	
	var $inner = $("#"+id+"PopAreaInner");
	var html = $chart.html();
	if($inner.html()=="" || $chart.css("display")=="none"){
		if($inner.html()==""){
			$inner.html(callHtml("/cm/util/colorPop.do"));
			$inner.find("td").bind("click",function(){
				var color = $(this).attr("bgcolor");
				$("#"+id).css('color',color).val(color);
				$("#"+id+"PopArea").hide();
				try{if(handler != null) eval(handler+"('"+id+"')");}catch(e){}
			});
		}
		$chart.show();
		
	} else {
		$chart.hide();
	}
};

// 달력(date, date+time, time) 추가
function addDate(id, type){
	var buffer=[];
	if(id==null) id='';
	buffer.push('<div id="'+id+'Area" class="datetime">');
	if(type=='calendartime' || type=='calendar'){
		buffer.push('<div class="input_date">');
		buffer.push('<div id="'+id+'DtCalArea" style="position:absolute; display:none;"></div>');
		buffer.push('<span class="input">');
		buffer.push('<input id="'+id+'Dt" name="'+id+'Dt" value="" type="text" style="width:80px; ime-mode:disabled;" class="input_center uniform-input text" title="도착시간" maxlength="10"></span>');
		buffer.push('<span class="calbtn"><a href="javascript:;" id="'+id+'DtCalBtn" class="ico_calendar" title="달력 - 팝업"><span>달력 - 팝업</span></a></span></div>');
	}
	if(type=='calendartime' || type=='time'){
		buffer.push('<div class="input_select_list" style="max-width:70px;">');
		buffer.push('<input id="'+id+'Tm" name="'+id+'Tm" value="" type="text" style="width:60px; ime-mode:disabled;" class="input_center uniform-input text" title="도착시간" maxlength="5">');
		buffer.push('<ul class="select_list" style="display: none;" id="selectTimeArea">');
		for(var i=0;i<=23;i++){
			for(var j=1;j<=60/30;j++){
				buffer.push('<li><span class="txt">');
				buffer.push((i<10 ? '0' : '')+''+i);
				buffer.push(':');
				buffer.push(j==1 ? '00' : (j-1)*30);
				buffer.push('</li>');
			}
		}
		buffer.push('</ul>');
		buffer.push('</div>');
		buffer.push('<input id="'+id+'" name="'+id+'" type="hidden" value="">');
	}
	buffer.push('</div>');
	
	return buffer.join('');
};
// input[type="text"] 추가
function addInputTxt(obj){
	var buffer=[];
	var style='';
	if(obj.wdth!=undefined) style='style="width:'+obj.wdth+';"';
	else style='style="width:98%;"';
	buffer.push('<input name="'+$id+'" id="'+$id+'" title="'+$keyTxt+'" type="text" '+style+'>');
	return buffer.join('');
};

// 버튼 생성
function createBtnList(id, btnList){
	var buffer=[];
	if(btnList!=undefined){
		buffer.push('<ul>');
		if(btnList['btn']!=undefined){
			buffer.push('<li class="btn">');
			var msgId=null, btnTitle;
			$.each(btnList['btn'], function(){
				msgId=this['titleId'];
				btnTitle=msgId!=undefined ? callMsg(msgId) : this['label']; 
				buffer.push('<a id="addRowBtn" href="javascript:void(0);"');
				if(this['click']!=undefined && this['handler']!=undefined) 
					buffer.push(' onclick="'+this['click']+'(\''+id+'\', '+this['handler']+');"');
				else buffer.push(' onclick="'+this['click']+'(\''+id+'\');"');
				buffer.push(' class="sbutton button_small" title="'+btnTitle+'"><span>'+btnTitle+'</span></a>');
			});
			
			buffer.push('</li>');
		}
		if(btnList['ico']!=undefined){
			buffer.push('<li class="ico">');
			if(btnList['move']!=undefined){
				buffer.push('<a href="javascript:void(0);" onclick="moveComponent(this,\'up\')"><img src="/images/blue/ico_wup.png" width="20" height="20" title="위로이동"></a>');
				buffer.push('<a href="javascript:void(0);" onclick="moveComponent(this,\'down\')"><img src="/images/blue/ico_wdown.png" width="20" height="20" title="아래로이동"></a>');
			}
			buffer.push('</li>');
		}
		
		buffer.push('</ul>');
	}
	return buffer.join('');

};

// 콤보박스 생성
function createSelectList(id, key, obj){
	var buffer=[];
	var titleSelect=obj.titleSelect;
	
	buffer.push('<select name="'+titleSelect.name+'" id="'+titleSelect.name+'" onchange="setCdTxt(\''+id+'\', this)">');
	if(titleSelect.value!=undefined){		
		$.each(titleSelect.value, function(index, option){
			$title=$formBuilder.isMultiLang && option.titleId!=undefined ? callMsg(option.titleId) : option.title;
			buffer.push('<option value="'+option.value+'" '+(option.selected ? 'selected="selected"' : '')+'>'+$title+'</option>');
		});
	}
	if(titleSelect.listOptCd!=undefined && titleSelect.listOptCd=='FORM_CD'){
		var emptyCdGrpList=$formBuilder.formCdGrpList==null;
		if(emptyCdGrpList){
			var cdGrpJsonString=getCdGrpListAjx(); // 코드그룹 목록 조회
			if(cdGrpJsonString==null) return;
			//cdGrpJsonString=JSON.stringify(cdGrpJsonString);
			var jsonData=JSON.parse(cdGrpJsonString);			
			var formCdGrpList=[];
			var formCdListMap = new Map();
			$.each(jsonData, function(idx, json){
				formCdGrpList.push({value:json['cdGrpId'], text:json['cdGrpRescNm']});
				formCdListMap.put(json['cdGrpId'], json['wfCdDVoList']);
			});
			$formBuilder.formCdGrpList=formCdGrpList;
			$formBuilder.formCdListMap=formCdListMap;
		}
		
		if($formBuilder.formCdGrpList!=null){
			var title, va, formCdGrpList;
			formCdGrpList=$formBuilder.formCdGrpList;
			if(formCdGrpList!=null){
				$.each($formBuilder.formCdGrpList, function(index, cdGrp){
					title=cdGrp.text;
					va=cdGrp.value;
					buffer.push('<option value="'+va+'"');
					buffer.push('>'+title+'</option>');
				});
			}
		}
	}
	buffer.push('</select>');
	return buffer.join('');
};

// json String escape
function escapeJsonString(value){
	if(value==null || value=='') return value;
	value=value.replace(/\t/g, '\\t');
	value=value.replace(/\r/g, '\\r');	
	value=value.replace(/\n/g, '\\n');
	return value;
};

// Json to String
function jsonToString(value) {
	if(value==null || value=='') return value;
	return value.replace("\\", "\\\\")
            .replace("\'", "\\\'")
            .replace("\"", "\\\"")
            .replace("\r\n", "\\n")
            .replace("\n", "\\n");
 };

// 문자열 합치기
function replaceAddText(findTxt, txt, addTxt){
	var regExp = new RegExp('('+findTxt+')', "g");	
	var findResult = txt.match(regExp);
	if(findResult!=null){
		findResult = findResult.reduce(function(a,b){if(a.indexOf(b)<0)a.push(b);return a;},[]);
		var result=null;
	 	for(var i=0;i<findResult.length;i++){
	 		result=findResult[i].charAt(0).toUpperCase();
	 		result+=findResult[i].substring(1);
	 		regExp = new RegExp(findResult[i], "g");	 
	 		txt=txt.replace(regExp, addTxt+result);
		}
	}
	return txt;
};

// 문자열 변경하기
function replaceText(findTxt, txt, replaceTxt){
	var regExp = new RegExp('('+findTxt+')', "g");	
	var findResult = txt.match(regExp);
	if(findResult!=null){
		findResult = findResult.reduce(function(a,b){if(a.indexOf(b)<0)a.push(b);return a;},[]);
	 	for(var i=0;i<findResult.length;i++){
	 		txt=txt.replace(regExp, replaceTxt);
		}
	}
	return txt;
};

// 다국어 INPUT 데이터 로드
function getLangInputData(id, name, title, obj){
	var uniform=$('#langHiddenArea').attr('data-uniform');
	if(uniform=='N'){
		$.uniform.restore($('#langHiddenArea').find('input, textarea, select, button'));
		$('#langHiddenArea').attr('data-uniform', 'Y');
	}
	//if(title!=undefined)
	//	$('#langHiddenArea input[id^="rescVa_"]').attr('title', title);
	var value=obj.value;
	if(value!=undefined){
		$('#langHiddenArea input[id^="rescVa_"]').attr('value', value);
	}
	var html=$('#langHiddenArea').html();
	html=replaceAddText('langTyp|rescVa', html, name);
	
	if(title!=undefined){
		var titleTxt=callMsg('wf.form.attr.fields.name');
		html=replaceText(titleTxt, html, title);
	}
	
	if(obj.maxByte!=undefined) html=replaceText('200', html, obj.maxByte);
	
	// 필수여부가 없으면 필수체크'N'으로 변경
	if(obj.required===undefined || !obj.required){
		html=replaceText('data-required="Y"', html, 'data-required="N"');
	}
	
	return html;
};

// 코드선택시 목록 세팅
function setCdTxt(tgtId, obj){
	var textarea=$('#'+tgtId).find('textarea');
	if(obj.value==''){
		textarea.removeAttr('readonly');
		textarea.val('');
		textarea.removeClass('textarea_disabled');
		//$('#'+tgtId).show();
	}else{
		textarea.attr('readonly', 'readonly');
		textarea.addClass('textarea_disabled');
		if($formBuilder.formCdListMap!=null && $formBuilder.formCdListMap.get(obj.value)!=null){
			var buffer=[];
			var cdList=$formBuilder.formCdListMap.get(obj.value);
			$.each(cdList, function(idx, jsonData){
				buffer.push(jsonData['cdNm']);
			});
			if(buffer.length>0){
				textarea.val(buffer.join('\n'));
			}
		}
	}
};

// 필수 아이콘 추가
function createMandatory(buffer, id, title){
	if(buffer==null) buffer=[];
	buffer.push('<img src="/images/blue/ico_asterisk.png"'+(id!=null ? ' id="'+id+'"' : '')+' width="10" height="9"'+(title!=null ? ' alt="'+title+'"' : '')+'/>');
	return buffer.join('');
};
// 속성 줄 추가
function attrAddRow(id){
	restoreUniform(id);
	var $li = $("#"+id+" ul:first");
	var html = $li[0].outerHTML;
	$li.before(html);
	$li = $li.prev();
	$li.attr('id','');
	$li.attr('style','');
	applyUniform(id);
};

//속성 줄 삭제
function attrDelRow(obj){
	var addRowList=$(obj).closest('div.addRowList');
	if(addRowList.children().length==1){
		alertMsg('wf.msg.not.minRow'); // 최소 1줄 이상 입력해야 합니다.
		return;
	}
	$(obj).closest('ul.attr_list').remove();
};

// uniform 적용하기
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

var componentList={
	label:{
		"id":"label",
		"fields":{
			"label": {
				"label": "Label Text",
				"titleId": "wf.form.attr.fields.label",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "레이블"
			  },
			  "labelAlign": {
				"label": "Label Align",
				"titleId": "pt.jsp.setLstSetup.lrAlign",
				"type": "select",
				"value": [
				   {
					"titleId": "cm.aln.center",
					"value": "center",
					"label": "Center",
					"selected": true
				  },
				  {
					"titleId": "cm.aln.left",
					"value": "left",
					"label": "Left",
					"selected": false
				  },			  
				  {
					"titleId": "cm.aln.right",
					"value": "right",
					"label": "Right",
					"selected": false
				  }			  
				]
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "fontFamilies": {
				"label": "Font Families",
				"titleId": "wf.form.attr.fields.fontFamily",
				"name": "fontFamilies",
				"type": "select",
				"valueTyp": "fontOptions",
				"emptyValue": "none"
			  },
			  "fontSizes": {
				"label": "Font Sizes",
				"titleId": "cols.size",
				"name": "fontSizes",
				"type": "select",
				"valueTyp": "fontOptions",
				"emptyValue": "inherit"
			  },
			  "fontStyle": {
				"label": "Font Style",
				"titleId": "wf.form.attr.fields.fontStyle",
				"name": "fontStyle",
				"type": "checkbox",
				"valueTyp": "fontOptions"				
			  },
			  "fontColor": {
				"label": "Font Color",
				"titleId": "wf.form.attr.fields.fontColor",
				"name": "fontColor",
				"type": "btn",
				"value": "colorpicker"
			  }
			  //,
			  /*"cellHeader": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.cell.headerYn",
					"titleId": "wf.form.attr.fields.cell.headerYn",
					"value":"Y",
					"checked": false
				 }]
			  }*/
		}
	},
	text:{
		"id":"text",
		"title": "Text Input",
		"fields": {
		  "name": {
			"label": "Name",
			"titleId": "wf.form.attr.fields.name",
			"required":true,
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": "텍스트"
		  },
		  "placeholder": {
			"label": "Placeholder",
			"titleId": "wf.form.attr.fields.placeholder",
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": ""
		  },
		  "helptext": {
			"label": "Help Text",
			"titleId": "wf.form.attr.fields.helptext",
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": ""
		  },
		  "required": {
			"label": null,
			"type": "checkbox",
			"value": [{				
				"labelId": "wf.form.attr.fields.required",
				"titleId": "wf.form.attr.fields.required",
				"value":"Y",
				"checked": false
			 }]
		  },
		  "startText": {
			"label": "Starting Text",
			"titleId": "wf.form.attr.fields.startText",
			"type": "input",
			"maxByte":"200",
			"value": ""
		  },
		  "inputLimits": {
			"label": "Input limits(Byte)",
			"titleId": "wf.form.attr.fields.inputLimits",
			"type": "multi",
			"value": [{
				"name": "min",
				"label": "min",
				"labelId": "wf.form.attr.fields.min",
				"titleId": "wf.form.attr.fields.min",
				"wdth": "50px",
				"valueOption":"number",
				"maxLength":"5",
				"type": "input"
			},{
				"name": "max",
				"label": "max",
				"labelId": "wf.form.attr.fields.max",
				"titleId": "wf.form.attr.fields.max",
				"wdth": "50px",
				"valueOption":"number",
				"maxLength":"3",
				"type": "input",
				"value": "200"
			}]
		  },
		  "wdthStyle": {
			"label": "Width",
			"titleId": "wf.form.attr.fields.wdthStyle",
			"type": "multi",
			"value": [{
				"name": "wdth",
				"label": null,
				"wdth": "30px",
				"valueOption":"number",
				"maxLength":"5",
				"type": "input"
				},{
				"name": "wdthTyp",
				"label": null,
				"type": "radio",
				"value": [{
					"label": "%",
					"value": "%",
					"checked": true
				},{
					"label": "px",
						"value": "px"
					}]
			}]
		  },
		  "allowChar": {
			"label": "Allow Text",
			"titleId": "wf.form.attr.fields.allowChar",
			"type": "multi",
			"value": [{
				"name": "allow",
				"label": "allow",
				"labelId": "wf.form.attr.fields.allow",
				"titleId": "wf.form.attr.fields.allow",
				"wdth": "50px",
				"maxByte":"30",
				"type": "input"
			},{
				"name": "notAllow",
				"label": "not Allow",
				"labelId": "wf.form.attr.fields.notAllow",
				"titleId": "wf.form.attr.fields.notAllow",
				"wdth": "50px",
				"maxByte":"30",
				"type": "input"
			}]
		  },
		  "allowList": {
			"label": "Allow List",
			"titleId": "wf.form.attr.fields.allowList",
			"type": "multi",
			"value": [{
				"label": null,
				"type": "checkbox",
				"value": [{				
					"label": "Number",
					"labelId": "wf.form.attr.fields.list.number",
					"titleId": "wf.form.attr.fields.list.number",
					"value": "number",
					"checked": false
				 },{	
					"label": "Alpha",
					"labelId": "wf.form.attr.fields.list.alpha",
					"titleId": "wf.form.attr.fields.list.alpha",
					"value": "alpha",
					"checked": false
				 },{				
					"label": "Lower",
					"labelId": "wf.form.attr.fields.list.lower",
					"titleId": "wf.form.attr.fields.list.lower",
					"value": "lower",
					"checked": false
				 },{				
					"label": "Upper",
					"labelId": "wf.form.attr.fields.list.upper",
					"titleId": "wf.form.attr.fields.list.upper",
					"value": "upper",
					"checked": false
				 },{				
					"label": "Email",
					"labelId": "wf.form.attr.fields.list.email",
					"titleId": "wf.form.attr.fields.list.email",
					"value": "email",
					"checked": false
				 }]
			}]
		  },
		  "validatorOpt": {
			"label": "Validator Option",
			"titleId": "wf.form.attr.fields.validatorList",
			"type": "select",
			"value": [
			   {
				"titleId": "cm.select.actname",
				"value": "",
				"label": "Select",
				"selected": true
			  },
			  {
				"titleId": "cols.phon",
				"value": "TEL",
				"label": "Tel"
			  },
			  {
				"titleId": "cols.email",
				"value": "EMAIL",
				"label": "Email"
			  },
			  {
				"titleId": "cols.ssn",
				"value": "SSN",
				"label": "Ssn"
			  }
			]
		  }
		}
	},	  
	textarea:{
		"id":"textarea",
		"title": "Text Area",
		"fields": {
		  "name": {
			"label": "Name",
			"titleId": "wf.form.attr.fields.name",
			"required":true,
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": "멀티 텍스트"
		  },
		  "placeholder": {
			"label": "Placeholder",
			"titleId": "wf.form.attr.fields.placeholder",
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": ""
		  },
		  "helptext": {
			"label": "Help Text",
			"titleId": "wf.form.attr.fields.helptext",
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": ""
		  },
		  "required": {
			"label": null,
			"type": "checkbox",
			"value": [{
				"labelId": "wf.form.attr.fields.required",
				"titleId": "wf.form.attr.fields.required",
				"value":"Y",
				"checked": false
			 }]
		  },
		  "startText": {
			"label": "Starting Text",
			"titleId": "wf.form.attr.fields.startText",
			"type": "input",
			"maxByte":"200",
			"value": ""
		  },
		  "inputLimits": {
			"label": "Input limits(Byte)",
			"titleId": "wf.form.attr.fields.inputLimits",
			"type": "multi",
			"value": [{
				"name": "min",
				"label": "min",
				"labelId": "wf.form.attr.fields.min",
				"titleId": "wf.form.attr.fields.min",
				"wdth": "50px",
				"valueOption":"number",
				"maxLength":"5",
				"type": "input"
			},{
				"name": "max",
				"label": "max",
				"labelId": "wf.form.attr.fields.max",
				"titleId": "wf.form.attr.fields.max",
				"wdth": "50px",
				"valueOption":"number",
				"maxLength":"3",
				"required":true,
				"type": "input",
				"value": "500"
			}]
		  },
		  "wdthStyle": {
			"label": "Width",
			"titleId": "wf.form.attr.fields.wdthStyle",
			"type": "multi",
			"value": [{
				"name": "wdth",
				"label": null,
				"wdth": "30px",
				"valueOption":"number",
				"maxLength":"5",
				"type": "input"
				},{
				"name": "wdthTyp",
				"label": null,
				"type": "radio",
				"value": [{
					"label": "%",
					"value": "%",
					"checked": true
				},{
					"label": "px",
						"value": "px"
					}]
			}]
		  },
		  "heightStyle": {
			"label": "Height",
			"titleId": "wf.form.attr.fields.heightStyle",
			"type": "multi",
			"value": [{
				"name": "height",
				"label": null,
				"wdth": "30px",
				"valueOption":"number",
				"maxLength":"5",
				"type": "input"
				},{
				"name": "heightTyp",
				"label": null,
				"type": "radio",
				"value": [{
					"labelId": "wf.cmpt.items.rows",
					"value": "row",
					"checked": true
				},{
					"label": "px",
						"value": "px"
					}]
			}]
		  },
		  "allowChar": {
			"label": "Allow Text",
			"titleId": "wf.form.attr.fields.allowChar",
			"type": "multi",
			"value": [{
				"name": "allow",
				"label": "allow",
				"labelId": "wf.form.attr.fields.allow",
				"titleId": "wf.form.attr.fields.allow",
				"wdth": "50px",
				"maxByte":"30",
				"type": "input"
			},{
				"name": "notAllow",
				"label": "not Allow",
				"labelId": "wf.form.attr.fields.notAllow",
				"titleId": "wf.form.attr.fields.notAllow",
				"wdth": "50px",
				"maxByte":"30",
				"type": "input"
			}]
		  },
		  "allowList": {
			"label": "Allow List",
			"titleId": "wf.form.attr.fields.allowList",
			"type": "multi",
			"value": [{
				"label": null,
				"type": "checkbox",
				"value": [{				
					"label": "Number",
					"labelId": "wf.form.attr.fields.list.number",
					"titleId": "wf.form.attr.fields.list.number",
					"value": "number",
					"checked": false
				 },{	
					"label": "Alpha",
					"labelId": "wf.form.attr.fields.list.alpha",
					"titleId": "wf.form.attr.fields.list.alpha",
					"value": "alpha",
					"checked": false
				 },{				
					"label": "Lower",
					"labelId": "wf.form.attr.fields.list.lower",
					"titleId": "wf.form.attr.fields.list.lower",
					"value": "lower",
					"checked": false
				 },{				
					"label": "Upper",
					"labelId": "wf.form.attr.fields.list.upper",
					"titleId": "wf.form.attr.fields.list.upper",
					"value": "upper",
					"checked": false
				 },{				
					"label": "Email",
					"labelId": "wf.form.attr.fields.list.email",
					"titleId": "wf.form.attr.fields.list.email",
					"value": "email",
					"checked": false
				 }]
			}]
		  }
		}
	  },
	  button:{
		"id":"button",
		"fields":{
			"name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "버튼"
			  },
			  "btnTyp": {
				"label": "Button Type",
				"titleId": "wf.cols.btnTyp",
				"type": "select",
				"value": [
				   {
					"titleId": "wf.option.addRow",
					"value": "add",
					"labelId": "wf.option.addRow",
					"selected": true
				  }
				]
			  },
			  "initRepetCnt": {
				"label": "Repeat Count",
				"titleId": "wf.form.attr.fields.initRepetCnt",
				"type": "select",
				"value": [{"label": "1", "value": "1", "selected": true}, {"label": "2", "value": "2"},{"label": "3", "value": "3"},
				          {"label": "4", "value": "4"}, {"label": "5", "value": "5"}, {"label": "6", "value": "6"}, {"label": "7", "value": "7"},
				          {"label": "8", "value": "8"}, {"label": "9", "value": "9"}, {"label": "10", "value": "10"}]
			  },
		}
	},
	  editor:{
		"id":"editor",
		"title": "Editor",
		"fields": {
		  "name": {
			"label": "Name",
			"titleId": "wf.form.attr.fields.name",
			"required":true,
			"type": "input",
			"maxByte":"200",
			"langTyp":true,
			"value": "에디터"
		  },
		  "required": {
			"label": null,
			"type": "checkbox",
			"value": [{
				"labelId": "wf.form.attr.fields.required",
				"titleId": "wf.form.attr.fields.required",
				"value":"Y",
				"checked": false
			 }]
		  },
		  "startText": {
			"label": "Starting Text",
			"titleId": "wf.form.attr.fields.startText",
			"type": "input",
			"maxByte":"200",
			"value": ""
		  }
		}
	},
	  date:{
			"id":"date",
			"title": "Calendar",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "날짜"
			  },
			  "placeholder": {
				"label": "Placeholder",
				"titleId": "wf.form.attr.fields.placeholder",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "startText": {
				"label": "Starting Text",
				"titleId": "wf.form.attr.fields.startText",
				"type": "input",
				"value": "",
				"wdth": "87px",
				"format": "date",
				"explain":{
					"label": "Default by registration date",
					"titleId": "wf.form.attr.fields.ex.date"
				}				
			  },
			  "defaultRegDt": {
				"type": "checkbox",
				"value": [{				
					"label": "Default by registration time",
					"labelId": "wf.form.attr.fields.defaultRegDt",
					"titleId": "wf.form.attr.fields.defaultRegDt",
					"value": "Y",
					"checked": false
				 }]
			  },
			  "allowAfterRegDt": {				
				"type": "checkbox",
				"value": [{
					"label": "Only allowed after registration time",
					"labelId": "wf.form.attr.fields.allowAfterRegDt",
					"titleId": "wf.form.attr.fields.allowAfterRegDt",
					"value": "Y",
					"checked": false
				 }]
			  }
			}
		  },
		  period:{
			"id":"period",
			"title": "Period",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "기간"
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "startText1": {
				"label": "Starting Text1",
				"titleId": "wf.form.attr.fields.startText1",
				"type": "input",
				"value": "",
				"wdth": "87px",
				"format": "date",
				"explain":{
					"label": "Default by registration start date",
					"titleId": "wf.form.attr.fields.ex.date"
				}	
			  },
			  "defaultRegDt": {
				"type": "checkbox",
				"value": [{
					"label": "Default by registration time",
					"labelId": "wf.form.attr.fields.defaultRegDt",
					"titleId": "wf.form.attr.fields.defaultRegDt",
					"value": "Y",
					"checked": false
				 }]
			  },
			  "startText2": {
				"label": "Starting Text2",
				"titleId": "wf.form.attr.fields.startText2",
				"type": "input",
				"value": "",
				"wdth": "87px",
				"format": "date",
				"explain":{
					"label": "Default by registration end date",
					"titleId": "wf.form.attr.fields.ex.date"
				}
			  }/*,
			  "dateTyp": {
				"label": "Only allowed after registration time",
				"titleId": "wf.form.attr.fields.allowAfterRegDt",
				"type": "radio",
				"value": [{
					"label": "Date",
					"labelId": "wf.form.attr.fields.date",
					"titleId": "wf.form.attr.fields.date",
					"value": "date",
					"checked": true
					},{
					"label": "Date Time",
					"labelId": "wf.form.attr.fields.datetime",
					"titleId": "wf.form.attr.fields.datetime",
					"value": "datetime",
					"checked": false
					}]					
			  }*/
			}
		  },
	  time:{
			"id":"time",
			"title": "Time",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "시간"
			  },
			  "placeholder": {
				"label": "Placeholder",
				"titleId": "wf.form.attr.fields.placeholder",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "startText": {
				"label": "Starting Text",
				"titleId": "wf.form.attr.fields.startText",
				"type": "input",
				"value": "",
				"wdth": "87px",
				"format": "time",
				"explain":{
					"label": "Default by registration time",
					"titleId": "wf.form.attr.fields.ex.time"
				}
			  },			  
			  "defaultRegDt": {
				"type": "checkbox",
				"value": [{
					"label": "Default by registration time",
					"labelId": "wf.form.attr.fields.defaultRegDt",
					"titleId": "wf.form.attr.fields.defaultRegDt",
					"value": "Y",
					"checked": false
				 }]
			  }
			}
		  },
	  datetime:{
			"id":"datetime",
			"title": "Calendar",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "날짜와 시간"
			  },
			  "placeholder": {
				"label": "Placeholder",
				"titleId": "wf.form.attr.fields.placeholder",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "startText": {
				"label": "Starting Text",
				"titleId": "wf.form.attr.fields.startText",
				"type": "multi",
				"value": [{
					"name":"startDate",
					"type": "input",
					"format": "date",
					"wdth": "87px"
				},{
					"name":"startTime",
					"type": "input",
					"format": "time",
					"wdth": "70px"
				}],
				"explain":{
					"label": "Default by registration time",
					"titleId": "wf.form.attr.fields.ex.datetime"
				}
			  },			  
			  "defaultRegDt": {
				"type": "checkbox",
				"value": [{
					"label": "Default by registration time",
					"labelId": "wf.form.attr.fields.defaultRegDt",
					"titleId": "wf.form.attr.fields.defaultRegDt",
					"value": "Y",
					"checked": false
				 }]
			  }			  
			}
		  },
		  number:{
			"id":"number",
			"title": "Text Input",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "숫자"
			  },
			  "placeholder": {
				"label": "Placeholder",
				"titleId": "wf.form.attr.fields.placeholder",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "startText": {
				"label": "Starting Text",
				"titleId": "wf.form.attr.fields.startText",
				"type": "input",
				"valueOption":"number",
				"value": ""
			  },
			  "inputLimits": {
				"label": "Input limits(Byte)",
				"titleId": "wf.form.attr.fields.inputLimits",
				"type": "multi",
				"value": [{
					"name": "min",
					"label": "min",
					"labelId": "wf.form.attr.fields.min",
					"titleId": "wf.form.attr.fields.min",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"5",
					"type": "input"
				},{
					"name": "max",
					"label": "max",
					"labelId": "wf.form.attr.fields.max",
					"titleId": "wf.form.attr.fields.max",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"3",
					"required":true,
					"type": "input",
					"value": "50"
				}]
			  },
			  "wdthStyle": {
				"label": "Width",
				"titleId": "wf.form.attr.fields.wdthStyle",
				"type": "multi",
				"value": [{
					"name": "wdth",
					"label": null,
					"wdth": "30px",
					"valueOption":"number",
					"maxLength":"5",
					"type": "input"
					},{
					"name": "wdthTyp",
					"label": null,
					"type": "radio",
					"value": [{
						"label": "%",
						"value": "%",
						"checked": true
					},{
						"label": "px",
							"value": "px"
						}]
				}]
			  },
			  "affix": {
				"label": "Affix",
				"titleId": "wf.form.attr.fields.affix",
				"type": "multi",
				"value": [{
					"name": "affix",
					"label": null,
					"wdth": "60px",
					"type": "input"
					},{
					"name": "affixTyp",
					"label": null,
					"type": "radio",
					"value": [{
						"labelId": "wf.form.attr.fields.prefix",
						"titleId": "wf.form.attr.fields.prefix",
						"value": "prefix"						
					},{
						"labelId": "wf.form.attr.fields.suffix",
						"titleId": "wf.form.attr.fields.suffix",
						"value": "suffix",
						"checked": true
						}]
				}]
			  },
			  "decimal": {
				"label": "Decimal",
				"titleId": "wf.form.attr.fields.decimal",
				"type": "input",
				"valueOption":"number",
				"maxLength":"3",
				"wdth":"60px",
				"value": ""
			  },
			  "comma": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.comma",
					"titleId": "wf.form.attr.fields.comma",
					"checked": false,
					"value":"Y"
				 }]
			  }
			}
		},
		calculate:{
			"id":"calculate",
			"title": "Calculate",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "자동계산"
			  },
			  "placeholder": {
				"label": "Placeholder",
				"titleId": "wf.form.attr.fields.placeholder",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "formula": {
				"label": "Formula",
				"titleId": "wf.form.attr.fields.formula",
				"type": "input",
				"onFocus":"addSelectEvt",
				"value": "",
				"explain":{
					"label": "ex) (NUMBER1+NUMBER2)/2",
					"titleId": "wf.form.attr.fields.ex.formula"
				}
			  },
			  "wdthStyle": {
				"label": "Width",
				"titleId": "wf.form.attr.fields.wdthStyle",
				"type": "multi",
				"value": [{
					"name": "wdth",
					"label": null,
					"wdth": "30px",
					"valueOption":"number",
					"maxLength":"5",
					"type": "input"
					},{
					"name": "wdthTyp",
					"label": null,
					"type": "radio",
					"value": [{
						"label": "%",
						"value": "%",
						"checked": true
					},{
						"label": "px",
							"value": "px"
						}]
				}]
			  },
			  "affix": {
				"label": "Affix",
				"titleId": "wf.form.attr.fields.affix",
				"type": "multi",
				"value": [{
					"name": "affix",
					"label": null,
					"wdth": "60px",
					"type": "input"
					},{
					"name": "affixTyp",
					"label": null,
					"type": "radio",
					"value": [{
						"labelId": "wf.form.attr.fields.prefix",
						"titleId": "wf.form.attr.fields.prefix",
						"value": "prefix"						
					},{
						"labelId": "wf.form.attr.fields.suffix",
						"titleId": "wf.form.attr.fields.suffix",
						"value": "suffix",
						"checked": true
						}]
				}]
			  },
			  "decimal": {
				"label": "Decimal",
				"titleId": "wf.form.attr.fields.decimal",
				"type": "input",
				"valueOption":"number",
				"maxLength":"3",
				"wdth":"60px",
				"value": ""
			  },
			  "comma": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.comma",
					"titleId": "wf.form.attr.fields.comma",
					"checked": false,
					"value":"Y"
				 }]
			  }
			}
		},
		user:{
			"id":"user",
			"title": "Users",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "사용자"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "data": {
				"label": "Default Data",
				"titleId": "wf.form.attr.fields.startText",
				"type": "multi",
				"btnList":{
					"btn":[{
						"titleId":"cm.btn.add",
						"click":"formUserSrch"
					}]	
				},
				"value": [{
					"type": "input",
					"hidden": true,
					"name":"userUids"
				},{
					"type": "textarea",
					"readonly":"readonly",
					"name":"userNms"
				}]
			  },
			  "defaultRegrUid": {
				"type": "checkbox",
				"value": [{				
					"label": "Default by registration user",
					"labelId": "wf.form.attr.fields.defaultRegrUid",
					"titleId": "wf.form.attr.fields.defaultRegrUid",
					"checked": false,
					"value": "Y"
				 }]
			  }
			}
		},
		dept:{
			"id":"dept",
			"title": "Users",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "부서"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "data": {
				"label": "Default Data",
				"titleId": "wf.form.attr.fields.startText",
				"type": "multi",
				"btnList":{
					"btn":[{
						"titleId": "cm.btn.add",
						"click":"formOrgSrch"
					}]	
				},
				"value": [{
					"type": "input",
					"hidden": true,
					"name":"orgIds"
				},{
					"type": "textarea",
					"readonly":"readonly",
					"name":"orgNms"
				}]
			  },
			  "defaultRegrUid": {
				"type": "checkbox",
				"value": [{				
					"label": "Default by registration user",
					"labelId": "wf.form.attr.fields.defaultRegOrgUid",
					"titleId": "wf.form.attr.fields.defaultRegOrgUid",
					"checked": false,
					"value": "Y"
				 }]
			  }
			}
		},
		  file:{
			"id":"file",
			"title": "File / Image",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "파일"
			  }/*,
			  "notExtFile": {
				"label": "Extension Restrictions",
				"titleId": "wf.form.attr.fields.notExt",
				"type": "input",
				"value": ""
			  }*/
			}
		  },
		  radio:{
			"id":"radio",
			"title": "Multiple Radios",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "단일선택"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "lout": {
				"label": "Layout",
				"titleId": "wf.form.attr.fields.lout",
				"type": "radio",
				"value": [{
					"labelId": "wf.form.attr.fields.width",
					"titleId": "wf.form.attr.fields.width",
					"checked": true,
					"value":"width"
				 },{
					"labelId": "wf.form.attr.fields.height",
					"titleId": "wf.form.attr.fields.height",
					"checked": false,
					"value":"height"
				 }]
			  }, 
			  "chkList": {
				"label": "Items",
				"titleId": "wf.form.attr.fields.items",
				"titleSelect":{
					"name":"chkTypCd",
					"value": [{"titleId":"wf.option.directInput", "title": "직접입력", "value":"", "selected": true}], // 직접입력
					"listOptCd":"FORM_CD" // 양식코드
				},
				"type": "textarea-split",
				"name":"chkList",
				"required":true,
				"value": [
				  "Option one",
				  "Option two"
				]
			  }
			}
		  },	
		  radioSingle:{
			"id":"radioSingle",
			"title": "Single Radio Box",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "단일선택(레이블없음)"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "text": {
				"label": "Text to display when selected",
				"titleId": "wf.form.attr.fields.text.disp",
				"name":"dispName",
				"required":true,
				"type": "input",
				"maxByte":"30",
				"langTyp":true,
				"value": "Y"
			  }
			}
		  },	
		  checkboxSingle:{
			"id":"checkboxSingle",
			"title": "Single Check Box",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "다중선택(레이블없음)"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "text": {
				"label": "Text to display when selected",
				"titleId": "wf.form.attr.fields.text.disp",
				"name":"dispName",
				"required":true,
				"type": "input",
				"maxByte":"30",
				"langTyp":true,
				"value": "Y"
			  }		  
			}
		  },	
		  checkbox:{
			"id":"check",
			"title": "Multiple Checkboxes",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "다중선택"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "lout": {
				"label": "Layout",
				"titleId": "wf.form.attr.fields.lout",
				"type": "radio",
				"value": [{
					"labelId": "wf.form.attr.fields.width",
					"titleId": "wf.form.attr.fields.width",
					"checked": true,
					"value":"width"
				 },{
					"labelId": "wf.form.attr.fields.height",
					"titleId": "wf.form.attr.fields.height",
					"checked": false,
					"value":"height"
				 }]
			  }, 
			  "chkList": {
				"label": "Checkboxes",
				"titleId": "wf.form.attr.fields.items",
				"titleSelect":{
					"name":"chkTypCd",
					"value": [{"titleId":"wf.option.directInput", "title": "직접입력", "value":"", "selected": true}], // 직접입력
					"listOptCd":"FORM_CD" // 양식코드
				},
				"type": "textarea-split",
				"name":"chkList",
				"required":true,
				"value": [
				  "Option one",
				  "Option two"
				]
			  }
			}
		  },
		  select:{
			"id":"select",
			"title": "Select Basic",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "드롭박스"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  },
			  "chkList": {
				"label": "Options",
				"titleId": "wf.form.attr.fields.items",
				"titleSelect":{
					"name":"chkTypCd",
					"value": [{"titleId":"wf.option.directInput", "title": "직접입력", "value":"", "selected": true}], // 직접입력
					"listOptCd":"FORM_CD" // 양식코드
				},
				"type": "textarea-split",
				"name":"chkList",
				"required":true,
				"value": [
				  "Option one",
				  "Option two"
				]
			  }
			}
		  },
		  addr:{
			"id":"addr",
			"title": "Address Input",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "주소"
			  }
			}
		  },		  
		  tel:{
			"id":"tel",
			"title": "Tel Input",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "전화번호"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "required": {
				"label": null,
				"type": "checkbox",
				"value": [{
					"labelId": "wf.form.attr.fields.required",
					"titleId": "wf.form.attr.fields.required",
					"value":"Y",
					"checked": false
				 }]
			  }
			}
		  },	
		  image:{
			"id":"image",
			"title": "Image",
			"fields": {
			  "name": {
				"label": "Name",
				"titleId": "wf.form.attr.fields.name",
				"required":true,
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": "이미지"
			  },
			  "helptext": {
				"label": "Help Text",
				"titleId": "wf.form.attr.fields.helptext",
				"type": "input",
				"maxByte":"200",
				"langTyp":true,
				"value": ""
			  },
			  "allowFile": {
				"label": "Extension Allow",
				"titleId": "wf.form.attr.fields.allow",
				"type": "input",
				"explain":{
					"label": "ex) jpg,png",
					"titleId": "wf.form.attr.fields.ex.image"
				},
				"value": ""
			  },
			  "viewSize": {
				"label": "View Image Size",
				"titleId": "wf.form.attr.fields.viewImgSize",
				"type": "multi",
				"required":true,
				"value": [{
					"name": "viewWdth",
					"label": "width",
					"labelId": "wf.form.attr.fields.wdthStyle",
					"titleId": "wf.form.attr.fields.wdthStyle",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"5",
					"required":true,
					"type": "input",
					"value":"88"
				},{
					"name": "viewHeight",
					"label": "height",
					"labelId": "wf.form.attr.fields.heightStyle",
					"titleId": "wf.form.attr.fields.heightStyle",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"5",
					"required":true,
					"type": "input",
					"value":"110"
				},{
					"name": "viewSizeTyp",
					"label": null,
					"type": "radio",
					"value": [{
							"label": "%",
							"value": "%"
						},{
							"label": "px",
							"value": "px",
							"checked": true
							}]
					}],
				"explain":{
					"label": "ex) 100px",
					"titleId": "wf.form.attr.fields.ex.imageSize"
				}
			  },
			  "reSizeText": {
				"label": "Image Recommend Help Text",
				"titleId": "wf.form.attr.fields.help.reSizeText",
				"type": "input",
				"maxByte":"50",				
				"value": ""
			  },
			  "listSize": {
				"label": "List Image Size",
				"titleId": "wf.form.attr.fields.listImgSize",
				"type": "multi",
				"value": [{
					"name": "listWdth",
					"label": "width",
					"labelId": "wf.form.attr.fields.wdthStyle",
					"titleId": "wf.form.attr.fields.wdthStyle",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"5",
					"type": "input",
					"value":"88"
				},{
					"name": "listHeight",
					"label": "height",
					"labelId": "wf.form.attr.fields.heightStyle",
					"titleId": "wf.form.attr.fields.heightStyle",
					"wdth": "50px",
					"valueOption":"number",
					"maxLength":"5",
					"type": "input",
					"value":"110"
				},{
					"name": "listSizeTyp",
					"label": null,
					"type": "radio",
					"value": [{
							"label": "%",
							"value": "%"
						},{
							"label": "px",
							"value": "px",
							"checked": true
							}]
					}],
				"explain":{
					"label": "ex) 100px",
					"titleId": "wf.form.attr.fields.ex.imageSize"
				}
			  }
			}
		  }
};

// 지원 언어 목록
var $langs = {
	
};

// input 데이터 가져오기
function getHiddenData(areaId, name){
	var hiddenData=$('#'+areaId).find('input[name="'+name+'"]').val();
	var data=[];
	if(hiddenData!=null){
		var arrs=hiddenData.trim().split(',');
		arrs.each(function(idx, va){
			data.push(va);
		});
	}
	return data;
};

// 사용자 조회
function formUserSrch(areaId, handler){
	var data=[];
	var hiddenArrs=getHiddenData(areaId, 'userUids');
	if(hiddenArrs!=null && hiddenArrs.length>0){
		hiddenArrs.each(function(idx, va){
			data.push({userUid:va});
		});
	}
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			//$('#'+areaId).html('');
			var txt='',va='';
			arr.each(function(index, vo){
				//if(hiddenArrs!=null && hiddenArrs.contains(vo.userUid)) return true;
				if(index>0){
					txt+=','; 
					va+=','; 
				}
				txt+=vo.rescNm; 
				va+=vo.userUid;
			});
			$('#'+areaId).find("input[name='userUids']").val(va);
			$('#'+areaId).find("textarea[name='userNms']").val(txt);
		}
	});
};

// 조직도 조회
function formOrgSrch(areaId, handler){
	var data=[];
	var hiddenArrs=getHiddenData(areaId, 'orgIds');
	if(hiddenArrs!=null && hiddenArrs.length>0){
		hiddenArrs.each(function(idx, va){
			data.push({orgId:va});
		});
	}
	searchOrgPop({data:data, multi:true, withSub:false, mode:'search'}, function(arr){
		if(arr!=null){
			//$('#'+areaId).html('');
			var txt='',va='';
			arr.each(function(index, vo){
				if(index>0){
					txt+=','; 
					va+=','; 
				}
				txt+=vo.rescNm; 
				va+=vo.orgId;
			});
			$('#'+areaId).find("input[name='orgIds']").val(va);
			$('#'+areaId).find("textarea[name='orgNms']").val(txt);
			//$('#'+areaId).append(buffer.join(''));
		}
	});
};

// 사용자,부서 생성[text + hidden]
function setTxtRows(id, arr){
	arr.each(function(index, vo){
		alert(vo.rescNm);
	});
};

// cell checkbox init 
function cellChkInit(td){
	var len=$(td).find('div.check').length;
	if(len>1){
		$.each($(td).find('div.check'), function(idx, check){
			if(idx>0) $(check).remove();
		});
	}else{
		$(td).addClass('body_ct cell_fix');
		$(td).attr('onclick', 'setProp(this, event);');
		$(td).attr('data-coltyp', 'cell');
		$(td).attr('data-drag', 'Y');
		$(td).prepend('<div class="check"><input type="checkbox" onclick="cellChk(this, event);"/></div>');
		setJsUniform(td);
	}
};

//cell checkbox init 
function cellChkHidden(td){
	$.each($(td).find('div.check'), function(idx, check){
		if(idx>0) $(check).hide();
	});
};

/** cell checkbox check event */
function cellChk(obj, event){
	var td=$(obj).closest('td');
	if (obj.checked === true) {
		$(td).addClass('cell-selected');
		$tables.mark(true, td);
	}
	else {
		$(td).removeClass('cell-selected');
		$tables.mark(false, td);
	}
	notEvtBubble(event);
};

//이벤트 버블 방지
function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
};

/** ------------------------------------------------------------------- Table ------------------------------------------------------------------ */
var $tables = {
		show_index:true,				// (boolean) show cell index
		init:function(option){

		},
		getTables:function (el) {
			var arr = [],	// result array
				nodes,		// node collection
				i;			// loop variable
			// collect table nodes				
			if(el===undefined) {
				var element = event.srcElement ? event.srcElement : event.target;
				el=$(element).closest('div#itemsArea');
			}
			nodes = $(el).find('table');
			// open node loop and push to array
			for (i = 0; i < nodes.length; i++) {
				arr.push(nodes[i]);
			}
			// return result array
			return arr;
		},
		removeCellChk:function(){
			$('#itemsArea div.check input[type="checkbox"]:checked').trigger('click');
		},
		mergeAll:function(){
			// first merge cells horizontally and leave cells marked
			this.merge('h', false);
			// and then merge cells vertically and clear cells (second parameter is true by default)
			this.merge('v');
			
			this.removeCellChk();
		},

		merge:function (mode, clear, table) {
			var	 tbl,		// table array (loaded from tables array or from table input parameter)
				tr,			// row reference in table
				c,			// current cell
				rc1,		// row/column maximum value for first loop
				rc2,		// row/column maximum value for second loop
				marked,		// (boolean) marked flag of current cell
				span,		// (integer) rowspan/colspan value
				id,			// cell id in format "1-2", "1-4" ...
				cl,			// cell list with new coordinates
				t,			// table reference
				i, j,		// loop variables
				first = {index : -1,	// index of first cell in sequence
						span : -1};		// span value (colspan / rowspan) of first cell in sequence
			// remove text selection
			
			// if table input parameter is undefined then use "tables" private property (table array) or set table reference from get_table method
			tbl = this.getTables();
			// open loop for each table inside container
			for (t = 0; t < tbl.length; t++) {
				// define cell list with new coordinates
				cl = this.cell_list($(tbl[t]));
				// define row number in current table
				tr = $(tbl[t]).find('tr').not('#hiddenTr, #headerTr');
				// define maximum value for first loop (depending on mode)
				rc1 = (mode === 'v') ? this.max_cols(tbl[t]) : tr.length;
				// define maximum value for second loop (depending on mode)
				rc2 = (mode === 'v') ? tr.length : this.max_cols(tbl[t]);
				// first loop
				for (i = 0; i < rc1; i++) {
					// reset marked cell index and span value
					first.index = first.span = -1;
					// second loop
					for (j = 0; j <= rc2; j++) {
						// set cell id (depending on horizontal/verical merging)
						id = (mode === 'v') ? (j + '-' + i) : (i + '-' + j);
						// if cell with given coordinates (in form like "1-2") exists, then process this cell
						if (cl[id]) {
							// set current cell
							c = cl[id];
							// if custom property "redips" doesn't exist then create custom property
							c.redips = c.redips || {};
							// set marked flag for current cell
							marked = c ? $(c).hasClass('cell-selected') : false;
							// set opposite span value
							span = (mode === 'v') ? c.colSpan : c.rowSpan;
						}
						else {
							marked = false;
						}
						// if first marked cell in sequence is found then remember index of first marked cell and span value
						if (marked === true && first.index === -1) {
							first.index = j;
							first.span = span;
						}
						// sequence of marked cells is finished (naturally or next cell has different span value)
						else if ((marked !== true && first.index > -1) || (first.span > -1 && first.span !== span)) {
							// merge cells in a sequence (cell list, row/column, sequence start, sequence end, horizontal/vertical mode)
							this.merge_cells(cl, i, first.index, j, mode, clear);
							// reset marked cell index and span value
							first.index = first.span = -1;
							// if cell is selected then unmark and reset marked flag
							// reseting marked flag is needed in case for last cell in column/row (so merge_cells () outside for loop will not execute)
							if (marked === true) {
								// if clear flag is set to true (or undefined) then clear marked cell after merging
								if (clear === true || clear === undefined) {
									this.mark(false, c);
								}
								marked = false;
							}
						}
						// increase "j" counter for span value (needed for merging spanned cell and cell after when index is not in sequence)
						if (cl[id]) {
							j += (mode === 'v') ? c.rowSpan - 1: c.colSpan - 1;
						}
					}
					// if loop is finished and last cell is marked (needed in case when TD sequence include last cell in table row)
					if (marked === true) {
						this.merge_cells(cl, i, first.index, j, mode, clear);
					}
				}
			}
			// show cell index (if show_index public property is set to true)
			this.cellIndex();
		},

		merge_cells:function (cl, idx, pos1, pos2, mode, clear) {
			var span = 0,	// set initial span value to 0
				id,			// cell id in format "1-2", "1-4" ...
				fc,			// reference of first cell in sequence
				c,			// reference of current cell 
				i;			// loop variable
			// set reference of first cell in sequence
			fc = (mode === 'v') ? cl[pos1 + '-' + idx] : cl[idx + '-' + pos1];
			// delete table cells and sum their colspans
			for (i = pos1 + 1; i < pos2; i++) {
				// set cell id (depending on horizontal/verical merging)
				id = (mode === 'v') ? (i + '-' + idx) : (idx + '-' + i);
				// if cell with given coordinates (in form like "1-2") exists, then process this cell
				if (cl[id]) {
					// define next cell in column/row
					c = cl[id];
					// add colSpan/rowSpan value
					span += (mode === 'v') ? c.rowSpan : c.colSpan;
					// relocate content before deleting cell in merging process
					this.relocate(c, fc);
					// delete cell
					c.parentNode.deleteCell(c.cellIndex);
				}
			}
			// if cell exists
			if (fc !== undefined) {		
				// vertical merging
				if (mode === 'v') {
					fc.rowSpan += span;			// set new rowspan value
				}
				// horizontal merging
				else {
					fc.colSpan += span;			// set new rowspan value
				}
				// if clear flag is set to true (or undefined) then set original background color and reset selected flag
				if (clear === true || clear === undefined) {
					this.mark(false, $(fc));
					cellChkInit(fc);
				}
			}
		},
		max_cols:function (table) {
			var tr = $(table).find('tr').not('#hiddenTr, #headerTr'),	// define number of rows in current table
				span,				// sum of colSpan values
				max = 0,			// maximum number of columns
				i, j;				// loop variable
			// if input parameter is string then overwrite it with table reference
			if (typeof(table) === 'string') {
				table = document.getElementById(table);
			}
			// open loop for each TR within table
			for (i = 0; i < tr.length; i++) {
				// reset span value
				span = 0;
				// sum colspan value for each table cell
				for (j = 0; j < tr[i].cells.length; j++) {
					span += tr[i].cells[j].colSpan || 1;
				}
				// set maximum value
				if (span > max) {
					max = span;
				}
			}
			// return maximum value
			return max;
		},	
		split:function (mode, table) {
			var tbl,	// table array (loaded from tables array or from table input parameter)
				tr,		// row reference in table
				c,		// current table cell
				cl,		// cell list with new coordinates
				rs,		// rowspan cells before
				n,		// reference of inserted table cell
				cols,	// number of columns (used in TD loop)
				max,	// maximum number of columns
				t,		// table reference
				i, j,	// loop variables
				get_rowspan;

			get_rowspan = function (c, row, col) {
				var rs,
					last,
					i;
				// set rs
				rs = 0;
				// set row index of bottom row for the current cell with rowspan value
				last = row + c.rowSpan - 1;
				// go through every cell before current cell in a row
				for (i = col - 1; i >= 0; i--) {
					// if cell doesn't exist then rowspan cell exists before
					if (cl[last + '-' + i] === undefined) {
						rs++;
					}
				}
				return rs;
			};
			// method returns number of rowspan cells before current cell (in a row)
			
			// if table input parameter is undefined then use "tables" private property (table array) or set table reference from get_table method
			tbl = (table !== undefined) ? table : this.getTables();
			// loop TABLE
			for (t = 0; t < tbl.length; t++) {
				// define cell list with new coordinates
				cl = this.cell_list($(tbl[t]));
				// define maximum number of columns in table
				max = this.max_cols($(tbl[t]));
				// define row number in current table
				tr = $(tbl[t]).find('tr').not('#hiddenTr, #headerTr');
				// loop TR
				for (i = 0; i < tr.length; i++) {
					// define column number (depending on mode)
					cols = (mode === 'v') ? max : tr[i].cells.length;
					// loop TD
					for (j = 0; j < cols; j++) {
						// split vertically
						if (mode === 'v') {
							// define current table cell
							c = cl[i + '-' + j];
							// if custom property "redips" doesn't exist then create custom property
							if (c !== undefined) {
								c.redips = c.redips || {};
							}
							// if marked cell is found and rowspan property is greater then 1
							if (c !== undefined && $(c).hasClass('cell-selected') === true && c.rowSpan > 1) {
								// get rowspaned cells before current cell (in a row)
								rs = get_rowspan(c, i, j);
								// insert new cell at last position of rowspan (consider rowspan cells before)
								n = tr[i + c.rowSpan - 1].insertCell(j - rs);
								// set the same colspan value as it has current cell
								n.colSpan = c.colSpan;
								// decrease rowspan of marked cell
								c.rowSpan -= 1;
								// recreate cell list after vertical split (new cell is inserted)
								cl = this.cell_list(tbl[t]);
								cellChkInit($(n));
							}
						}
						// split horizontally
						else {
							// define current table cell
							c = tr[i].cells[j];
							// if custom property "redips" doesn't exist then create custom property
							c.redips = c.redips || {};
							// if marked cell is found and cell has colspan property greater then 1
							if ($(c).hasClass('cell-selected') === true && c.colSpan > 1) {
								// increase cols (because new cell is inserted)
								cols++;
								// insert cell after current cell
								n = tr[i].insertCell(j + 1);
								// set the same rowspan value as it has current cell
								n.rowSpan = c.rowSpan;
								// decrease colspan of marked cell
								c.colSpan -= 1;
								cellChkInit($(n));
							}
						}
						// return original background color and reset selected flag (if cell exists)
						if (c !== undefined) {
							this.mark(false, c);
						}
					}
				}
			}
			// show cell index (if show_index public property is set to true)
			this.cellIndex();
			
			this.removeCellChk();
		},
		row:function (mode, index) {
			var	nc,			// new cell
				nr = null,	// new row
				fr,			// reference of first row
				c,			// current cell reference
				cl,			// cell list
				cols = 0,	// number of columns
				i, j, k;	// loop variables
			// remove text selection
			if($('td.cell-selected').length==0) return;
			var table=$('td.cell-selected:eq(0)').closest("table").attr('id');

			// if table is not object then input parameter is id and table parameter will be overwritten with table reference
			if (typeof(table) !== 'object') {
				table = document.getElementById(table);
			}
			// if index is not defined then index of the last row
			if (index === undefined) {
				index = -1;
			}
			// insert table row
			if (mode === 'insert') {
				// set reference of first row
				fr = table.rows[0];
				// define number of columns (it is colspan sum)
				for (i = 0; i < fr.cells.length; i++) {
					cols += fr.cells[i].colSpan;
				}
				// insert table row (insertRow returns reference to the newly created row)
				nr = table.insertRow(index);
				this.setRowAttr(nr);
				// insert table cells to the new row
				for (i = 0; i < cols; i++) {
					nc = nr.insertCell(i);
					this.setCellAttr(nc);
					// add "redips" property to the table cell and optionally event listener
					//this.cell_init(nc);
				}
				// show cell index (if show_index public property is set to true)
				this.cellIndex();
				setJsUniform($(nr));
			}
			// delete table row and update rowspan for cells in upper rows if needed
			else {
				// last row should not be deleted
				if (table.rows.length === 1) {
					return;
				}
				// delete last row
				table.deleteRow(index);
				// prepare cell list
				cl = this.cell_list(table);
				// set new index for last row
				index = table.rows.length - 1;
				// set maximum number of columns that table has
				cols = this.max_cols(table);
				// open loop for each cell in last row
				for (i = 0; i < cols; i++) {
					// try to find cell in last row
					c = cl[index + '-' + i];
					// if cell doesn't exist then update colspan in upper cells
					if (c === undefined) {
						// open loop for cells up in column
						for (j = index, k = 1; j >= 0; j--, k++) {
							// try to find cell upper cell with rowspan value
							c = cl[j + '-' + i];
							// if cell is found then update rowspan value
							if (c !== undefined) {
								c.rowSpan = k;
								break;
							}
						}
					}
					// if cell in last row has rowspan greater then 1
					else if (c.rowSpan > 1) {
						c.rowSpan -= 1;
					}
					// increase loop variable "i" for colspan value
					i += c.colSpan - 1;
				}
			}
			// in case of inserting new table row method will return TR reference (otherwise it will return NULL)
			return nr;
		},
		setRowAttr:function(row){
			$(row).attr("id", "row");
		},
		setCellAttr:function(cell){
			$(cell).addClass("body_ct cell_fix");
			$(cell).attr("data-colTyp", "cell");
			$(cell).attr("onclick", "setProp(this, event);");
			$(cell).attr('data-drag', 'Y');
			$(cell).append('<div class="check"><input type="checkbox" onclick="cellChk(this, event);"/></div>');
			setEvtDragComporent($(cell));
		},
		addColgroup:function(table){
			$(table).find('colgroup').eq(0).append('<col width=""/>');
		},
		delColgroup:function(table){
			$(table).find('colgroup col:last').remove();
		},
		column:function (mode, index) {
			var	c,		// current cell
				idx,	// cell index needed when column is deleted
				nc,		// new cell
				i;		// loop variable
			// remove text selection
			if($('td.cell-selected').length==0) return;
			var table=$('td.cell-selected:eq(0)').closest("table").attr('id');
			// if table is not object then input parameter is id and table parameter will be overwritten with table reference
			if (typeof(table) !== 'object') {
				table = document.getElementById(table);
			}

			// if index is not defined then index will be set to special value -1 (means to remove the very last column of a table or add column to the table end)
			if (index === undefined) {
				index = -1;
			}

			// insert table column
			if (mode === 'insert') {
				// loop iterates through each table row
				for (i = 0; i < table.rows.length; i++) {
					// insert cell
					nc = table.rows[i].insertCell(-1);
					this.setCellAttr(nc);
					setJsUniform($(nc));
					// add "redips" property to the table cell and optionally event listener
					//this.cell_init(nc);
				}
				// show cell index (if show_index public property is set to true)
				this.addColgroup(table);
				this.cellIndex();
			}
			// delete table column
			else {
				// set reference to the first row
				c = table.rows[0].cells;
				// test column number and prevent deleting last column
				if (c.length === 1 && (c[0].colSpan === 1 || c[0].colSpan === undefined)) {
					return;
				}
				// row loop
				for (i = 0; i < table.rows.length; i++) {
					// define cell index for last column
					if (index === -1) {
						idx = table.rows[i].cells.length - 1;
					}
					// if index is defined then use "index" value
					else {
						idx = index;
					}
					// define current cell (it can't use special value -1)
					c = table.rows[i].cells[idx];
					// if cell has colspan value then decrease colspan value
					if (c.colSpan > 1) {
						c.colSpan -= 1;
					}
					// else delete cell
					else {
						table.rows[i].deleteCell(index);
					}
					// increase loop variable "i" for rowspan value
					i += c.rowSpan - 1;
				}
				this.delColgroup(table);
			}
		},
		tableIndex:function(table){
			var c,			// current cell reference
			cl,			// cell list
			cols = 0,	// number of columns
			i, j, k;	// loop variables
			if(table===undefined){
				var element = event.srcElement ? event.srcElement : event.target;
				var el=$(element).closest('div#itemsArea');
				table=$(el).find('table:first');
			}else if(typeof(table) !== 'object'){
				table = $('#'+table);
			}
			// prepare cell list
			cl = this.cell_list(table);
			// set new index for last row
			var index = (table).find("tbody:first").children().not('#hiddenTr, #headerTr').length - 1;
			// set maximum number of columns that table has
			cols = this.max_cols(table);
			// open loop for each cell in last row
			for (i = 0; i < cols; i++) {
				// try to find cell in last row
				c = cl[index + '-' + i];
				// if cell doesn't exist then update colspan in upper cells
				if (c === undefined) {
					// open loop for cells up in column
					for (j = index, k = 1; j >= 0; j--, k++) {
						// try to find cell upper cell with rowspan value
						c = cl[j + '-' + i];
						// if cell is found then update rowspan value
						if (c !== undefined) {
							c.rowSpan = k;
							break;
						}
					}
				}
				// if cell in last row has rowspan greater then 1
				else if (c.rowSpan > 1) {
					c.rowSpan -= 1;
				}
				// increase loop variable "i" for colspan value
				i += c.colSpan - 1;
			}
			
		},
		mark:function (flag, el, row, col) {
			var cl;
			if (typeof(flag) !== 'boolean') {
				return;
			}
			if (typeof(el) === 'string') {
				el = $(el);
			}
			else if (typeof(el) !== 'object') {
				return;
			}
			if ($(el).prop('tagName') === 'TABLE') {
				cl = cell_list(el);
				el = cl[row + '-' + col];
			}
			if (!el || $(el).prop('tagName') !== 'TD') {
				return;
			}
			event.stopPropagation();
		},
		cell_list:function (table) {
			var matrix = [],
				matrixrow,
				lookup = {},
				c,			// current cell
				ri,			// row index
				rowspan,
				colspan,
				firstAvailCol,
				tr,			// TR collection
				i, j, k, l;	// loop variables
			// set HTML collection of table rows
			tr = $(table).find('tr').not('#hiddenTr, #headerTr');
			// open loop for each TR element
			for (i = 0; i < tr.length; i++) {
				// open loop for each cell within current row
				for (j = 0; j < tr[i].cells.length; j++) {
					// define current cell
					c = tr[i].cells[j];
					// set row index
					ri = c.parentNode.rowIndex;
					// define cell rowspan and colspan values
					rowspan = c.rowSpan || 1;
					colspan = c.colSpan || 1;
					// if matrix for row index is not defined then initialize array
					matrix[ri] = matrix[ri] || [];
					// find first available column in the first row
					for (k = 0; k < matrix[ri].length + 1; k++) {
						if (typeof(matrix[ri][k]) === 'undefined') {
							firstAvailCol = k;
							break;
						}
					}
					// set cell coordinates and reference to the table cell
					lookup[ri + '-' + firstAvailCol] = c;
					for (k = ri; k < ri + rowspan; k++) {
						matrix[k] = matrix[k] || [];
						matrixrow = matrix[k];
						for (l = firstAvailCol; l < firstAvailCol + colspan; l++) {
							matrixrow[l] = 'x';
						}
					}
				}
			}
			return lookup;
		},

		relocate:function (from, to) {
			var cn,		// number of child nodes
				i, j;	// loop variables
			// test if "from" cell is equal to "to" cell then do nothing
			if (from === to) {
				return;
			}
			// define childnodes length before loop
			cn = from.childNodes.length;
			// loop through all child nodes in table cell
			// 'j', not 'i' because NodeList objects in the DOM are live
			for (i = 0, j = 0; i < cn; i++) {
				// relocate only element nodes
				if (from.childNodes[j].nodeType === 1) {
					to.appendChild(from.childNodes[j]);
				}
				// skip text nodes, attribute nodes ...
				else {
					j++;
				}
			}	
		},
		cellIndex:function (flag, tables) {
			// if input parameter isn't set and show_index private property is'nt true, then return
			// input parameter "flag" can be undefined in case of internal calls 
			if (flag === undefined && this.show_index !== true) {
				return;
			}
			if(tables===undefined) tables=this.getTables();
			
			// if input parameter is set, then save parameter to the private property show_index
			if (flag !== undefined) {
				// save flag to the show_index private parameter
				this.show_index = flag;
			}
			// variable declaration
			var tr,			// number of rows in a table
				c,			// current cell
				cl,			// cell list
				cols,		// maximum number of columns that table contains
				i, j, t;	// loop variables
			// open loop for each table inside container
			for (t = 0; t < tables.length; t++) {
				// define row number in current table
				tr = tables[t].rows;
				// define maximum number of columns (table row may contain merged table cells)
				cols = this.max_cols(tables[t]);
				// define cell list
				cl = this.cell_list($(tables[t]));
				// open loop for each row
				for (i = 0; i < tr.length; i++) {
					// open loop for every TD element in current row
					for (j = 0; j < cols; j++) {
						// if cell exists then display cell index
						if (cl[i + '-' + j]) {
							// set reference to the current cell
							c = cl[i + '-' + j];
							// set innerHTML with cellIndex property
							if(this.show_index) c.setAttribute('data-index', i + '-' + j);
						}
					}
				}
			}
		}
	};



Map = function(){
 this.map = new Object();
};   
Map.prototype = {   
    put : function(key, value){   
        this.map[key] = value;
    },   
    get : function(key){   
        return this.map[key];
    },
    containsKey : function(key){    
     return key in this.map;
    },
    containsValue : function(value){    
     for(var prop in this.map){
      if(this.map[prop] == value) return true;
     }
     return false;
    },
    isEmpty : function(key){    
     return (this.size() == 0);
    },
    clear : function(){   
     for(var prop in this.map){
      delete this.map[prop];
     }
    },
    remove : function(key){    
     delete this.map[key];
    },
    keys : function(){   
        var keys = new Array();   
        for(var prop in this.map){   
            keys.push(prop);
        }   
        return keys;
    },
    values : function(){   
     var values = new Array();   
        for(var prop in this.map){   
         values.push(this.map[prop]);
        }   
        return values;
    },
    size : function(){
      var count = 0;
      for (var prop in this.map) {
        count++;
      }
      return count;
    }
};
	
$(function() {
	$(document).mousedown(function(event) {
		if((!$(event.target).closest('div.tooltip_area').length &&
			       !$(event.target).is('div.tooltip_area')) &&
			       $(event.target).attr('data-selectyn')===undefined) {
			$('#formArea div.component_list[data-coltyp="number"] div.tooltip_area').hide();
		}
	});
	//$.extend(formBuilder, componentList);
});