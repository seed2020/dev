
// common.js
if(!window.console) {
window.console = {};
window.console.log = function(str) {};
window.console.dir = function(str) {};
}
// LocalSynap
function initLocalSynap() {
	return {
		properties: {
			allowCopy : true, // 복사 방지를 하고 싶을때 false로 설정
			allowResize : true, // 슬라이드의 크기 고정하고 싶을때 false로 설정
			isRenderServer : false, // 변환서버가 있는지 여부
			thumbnailWidth : 150, // 썸네일 크기
			mobileSlideSkinViewSetting : "multi", // 모바일기기에서 슬라이드 출력방식(single : 한 페이지씩 보기, multi : 모아보기)
			useMobileTextSearch : false, // 모바일기기 텍스트 검색 (활성화 시 스킨 성능 감소)
			usePdfText : true, // PDF텍스트 출력 여부
			webAccessibility : true, // 웹접근성 향상(PDF이미지텍스트 읽기지원용)
			useLoadingSpinner : false,
			title : undefined,
			fileType : undefined,
			fileName: undefined,
			xmlObj : undefined,
			xmlUrl : undefined,
			resultDir : undefined,
			layout: "withpage",
			debug : true,
			print : true // 인쇄 방지를 하고 싶을때 false로 설정
		},

		topS: 0,
		rightS: 0,
		bottomS: 0,
		leftS: 0,
		isAllowCopy: function() {
			return this.properties.allowCopy;
		},

		isAllowResize: function() {
			return this.properties.allowResize;
		},

		getFileName: function() {
			return this.properties.fileName;
		},

		getFileType: function() {
			return this.properties.fileType;
		},

		getResultDir: function() {
			return this.properties.resultDir;
		},

		isSingleLayout: function() {
			if (localSynap.properties.layout==="single"){
				return true;
			}
			return false;
		},

		isSlide: function(){
			if (localSynap.properties.fileType==="ppt" || localSynap.properties.fileType==="pptx"){
				return true;
			}
			return false;
		},
        isImageMode: function() {
			if ( typeof localSynap.status !== "undefined" && localSynap.status.convertType === 1) {
				return true;	
			} else {
				return false;
			}
		},
		setAllowCopy: function(allowCopy){
			this.properties.allowCopy=allowCopy;
		},
		setPrint: function(print){
			this.properties.print=print;
		}
	};
}

// for view Ctrl NameSpace
function getSynapPageObject() {
	var obj = window;
	while(!(obj.localSynap) && obj != window.top) {
		try {
			if (obj.parent.localSynap) {}
		} catch(e) {
			return initLocalSynap();
		}
		obj = obj.parent;
	}
	if (obj.localSynap) {
		return obj.localSynap;
	}
	else {
		return initLocalSynap();
	}
}

var localSynap = getSynapPageObject();

// localSynap에서 설정하는 변수
// layout
// fileType

// 복사방지 스크립트 Start
function stopDefaultEvent(sEvent, el, fn) {
	if (el.addEventListener) {
		el.addEventListener(sEvent, fn);
	} else {
		if (el.attachEvent) {
			el.attachEvent("on" + sEvent, fn);
		}
	}
}

function stopEvent(e) {
	e = e || window.event;

	if (typeof e.preventDefault != "undefined") e.preventDefault();
	if (typeof e.stopPropagation != "undefined") e.stopPropagation();

	e.returnValue = false;
	return false;
}

function stopMobileTouchEvent(el) {
	if(typeof el.setAttribute != 'undefined' && el.tagName !== 'BODY') {
		el.setAttribute('style', el.getAttribute('style') + '-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;user-select:none;-webkit-tap-highlight-color:rgba(0,0,0,0);');
	}else if( typeof el.setAttribute != 'undefined' && el.tagName === 'BODY' ){
		el.setAttribute('style', el.getAttribute('style') + '-webkit-touch-callout:none;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;-webkit-tap-highlight-color:rgba(0,0,0,0);');
	}
}

function stopBrowserEvent(el) {
	stopDefaultEvent("selectstart", el, stopEvent);
	stopDefaultEvent("dragstart", el, stopEvent);
	stopDefaultEvent("contextmenu", el, stopEvent);
	stopDefaultEvent("mousedown", el, stopEvent);	
	stopMobileTouchEvent(el);
};
// 복사방지 스크립트 End

// IMG ERROR
function image_error(obj) {
	obj.alt = 'Not suppported image format.';
	obj.title = 'Not suppported image format.';
	obj.style.visibility = 'hidden';
} 

// 슬라이드, pdf
function slideKeyboardControl(){
	document.onkeydown = function(event) {
		event = event || window.event;
		if (BROWSER.PC.isIE()) {
			keyCode = event.keyCode;
		} else {
			keyCode = event.which;
		}
		switch(keyCode) {
			case 37:case 38: case 33:
				localSynap.movePrev();
				break;
			case 34: case 39: case 40:
				localSynap.moveNext();
				break;
			case 36:
				localSynap.moveSlide(0);
				break;
			case 35:
				localSynap.moveSlide(localSynap.pageSize - 1);
				break;
		}
		if (typeof localSynap.onKeyDown==="function"){
			localSynap.onKeyDown(event);
		}
	};
}

// 슬라이드외의 포맷
function docKeyboardControl(){
	document.onkeydown = function(event) {
		event = event || window.event;
		if (BROWSER.PC.isIE()) {
			keyCode = event.keyCode;
		} else {
			keyCode = event.which;
		}
		if (typeof localSynap.onKeyDown==="function"){
			localSynap.onKeyDown(event);
		}
	};
}

function loadSpinner(targetId, pageClassName){
	$('.loading_spinner').remove();
		
	var opts = {
	  lines: 11 // The number of lines to draw
	, length: 0 // The length of each line
	, width: 12 // The line thickness
	, radius: 30 // The radius of the inner circle
	, scale: 1.25 // Scales overall size of the spinner
	, corners: 0.5 // Corner roundness (0..1)
	, color: '#435c85' // #rgb or #rrggbb or array of colors
	, opacity: 0.25 // Opacity of the lines
	, rotate: 0 // The rotation offset
	, direction: 1 // 1: clockwise, -1: counterclockwise
	, speed: 5 // Rounds per second
	, trail: 65 // Afterglow percentage
	, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
	, zIndex: 2e9 // The z-index (defaults to 2000000000)
	, className: 'spinner' // The CSS class to assign to the spinner
	, top: '50%' // Top position relative to parent
	, left: '50%' // Left position relative to parent
	, shadow: false // Whether to render a shadow
	, hwaccel: false // Whether to use hardware acceleration
	, position: 'absolute' // Element positioning
	}
	var target = document.createElement('div'); target.setAttribute('id', 'div_page'); target.setAttribute('class', 'inner loading_spinner ' + pageClassName);
	document.getElementById(targetId).appendChild(target);
	var spinner = new Spinner(opts).spin(target);
}

function removeSpinner(){
	$('.loading_spinner').remove();
}
/** 인쇄방지 **/
function setPrint(fileType){
	// common
	noPrint(document);
	if (fileType=="doc" || fileType=="docx" || fileType=="hwp2k" || fileType=="hwp97" || fileType=="ndoc" || 
			fileType=="txt" || fileType=="xls" || fileType=="xlsx" || fileType=="ppt" || fileType=="pptx"){
		$('#innerWrap').on('load', function(){
			var contents = $(this).contents();
			if(contents) {
				noPrint(contents);
			}
		});
	}
}
/** 인쇄 방지 css 삽입 */
function noPrint(el){
	$('<style media="print"> body {display:none;} </style>').appendTo($(el).find('head'));
}
//SKIN READY FUNC
$(document).ready(function() {
	localSynap.setAllowCopy(false); // 복사방지
	localSynap.setPrint(false); // 인쇄방지
	if(!localSynap.properties.print) setPrint(localSynap.properties.fileType);
});