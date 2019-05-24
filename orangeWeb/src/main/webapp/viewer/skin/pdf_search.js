/*
PDF 검색모듈
텍스트정보XML을 읽고, 각 페이지 별 span태그 목록을 반환한다.

예제 코드
var aaa = new pdfSearch("test.xml");
$(target).append(aaa.getData(2));
*/
console.log("pdfSearch on");

var pdfSearch = (function() {
	var Search = {
		textObj : {
			l: 0,
			t: 0,
			w: 0,
			h: 0,
			text: "",
			link: null
		},
		pageFullText: undefined,
		xmlData: undefined,
		makeAttrObj: function (data) {
			var obj = {};
			if (typeof data === "undefined") { return obj; }
			obj.t = data.getAttribute('t');
			obj.l = data.getAttribute('l');
			obj.w = data.getAttribute('w');
			obj.h = data.getAttribute('h');

			if(BROWSER.PC.isIE()) {
				if(typeof data.nodeTypedValue!=="undefined") {
					obj.text = data.nodeTypedValue;
				} else {
					if(typeof data.textContent!=="undefined") {
						obj.text = data.textContent;
					}
				}
			} else {
				obj.text = data.textContent; // IE의 경우는 IE9에서만 지원된다.
			}
			return obj;
		},
		callAjaxXmlData: function (xmlPath) {
			var xmlData;
			$.ajax(
				{
					type: "GET",
					url: xmlPath,
					async: false,
					dataType: (BROWSER.PC.isIE()) ? "text" : "xml",
					error: function(data){
						console.log('Error occurred loading Text XML : ', xmlPath, data);
					},
					success:function(data) {

						if (typeof data === "string") {
							xml = new ActiveXObject("Microsoft.XMLDOM");
							xml.async = false;
							xml.loadXML(data);
						} else {
							xml = data;
						}

						xmlData = $(xml).find("page");
					}
				}
			);
			return xmlData	  
		},
		makeXmlDataObj: function (xmlObj, textList) {
			var page = $(xmlObj).attr('page');
			var target = $(xmlObj).attr('target');
			if ( (typeof page !== "undefined") || (typeof target !== "undefined") ) {
				$(xmlObj).children().each(function (lidx, lxmlObj) {
					obj = Search.makeAttrObj(lxmlObj);
					if(typeof page !== "undefined") {
						obj.link = parseInt(page); // 페이지 번호로 이동하는 link는 숫자형으로 기록한다.
					} else {
						obj.link = target;
					}

					// check boundary
					if(typeof isRelative==="boolean" && isRelative==true) {
						//셀의 경우 잘라진 조각 이미지를 기준으로 좌표이기 때문에 좌표 조정이 필요하다.
						obj.l = parseFloat(obj.l) + (startX-1);
						obj.t = parseFloat(obj.t) + (startY-1);
					}			   
					if(typeof startX!=="undefined") {
						var objLeft = parseFloat(obj.l);
						var objTop = parseFloat(obj.t);
						if(objLeft>=startX && objLeft<=endX && objTop>=startY && objTop<=endY) {
							textList.push(obj);
						}
					} else {
						textList.push(obj);
					}
				});
			} else {
				obj = Search.makeAttrObj(xmlObj);
			}
			if ( (typeof page === "undefined") && (typeof target === "undefined") ) {
				// check boundary
				if(typeof isRelative==="boolean" && isRelative==true) {
					//셀의 경우 잘라진 조각 이미지를 기준으로 좌표이기 때문에 좌표 조정이 필요하다.
					obj.l = parseFloat(obj.l) + (startX-1);
					obj.t = parseFloat(obj.t) + (startY-1);
				}			   
				if(typeof startX!=="undefined") {
					var objLeft = parseFloat(obj.l);
					var objTop = parseFloat(obj.t);
					if(objLeft>=startX && objLeft<=endX && objTop>=startY && objTop<=endY) {
						textList.push(obj);
					}
				} else {
					textList.push(obj);
				}
			}

			return [obj, textList];
		},
		// input : 텍스트정보가 있는 xml파일
		// rect영역내의 텍스트만 읽어들이도록 boundary정보를 받을 수 있다.
		loadTextXml: function(xmlFilePath, startX, startY, endX, endY, isRelative) {
			var textList = [];
			var xmlData = pdfSearch.callAjaxXmlData(xmlFilePath);   
			var obj = {};
			$.each($(xmlData).children(), function (idx, elem) {
				var retObj = pdfSearch.makeXmlDataObj(elem, textList);
				obj = retObj[0];
				textList = retObj[1];
			});
			var pageSizeObj = {
				width: xmlData.attr('w'),
				height: xmlData.attr('h')
			};
			// TODO: 이거 함수로 꼭 빼내야됨. 급해서 이렇게 해놓은거
			localSynap.pageInfo = pageSizeObj;
			return textList;
		},
		loadTextXmlAsync: function(xmlFilePath, pageIdx, startX, startY, endX, endY, isRelative) {
			var textList = [];
			var xmlData = pdfSearch.callAjaxXmlData(xmlFilePath);   
			var obj = {};
			var copy = $.map($(xmlData).children(), function(elem) {return elem;});
			setTimeout(function () {
				var elem = copy.shift();
				var retObj = pdfSearch.makeXmlDataObj(elem, textList);
				obj = retObj[0];
				textList = retObj[1];

				if (copy.length > 0) {
					// console.log('calling');
					setTimeout(arguments.callee, 0);
				} else {
					var pageSizeObj = {
						width: xmlData.attr('w'),
						height: xmlData.attr('h')
					};
					// TODO: 이거 함수로 꼭 빼내야됨. 급해서 이렇게 해놓은거
					localSynap.pageInfo = pageSizeObj;
					var xmlObj = {
						index: pageIdx,
					   content: textList	
					};
					localSynap.textXmlQueue.push(xmlObj);   
					localSynap.appendTextInterval.push(setInterval(localSynap.appendTextIntervalFunc, 500));
				}
			}, 0);
		},
		/*
		인덱스 페이지의 텍스트 정보를 담은 Span태그들을 반환한다.
		input : pageIdx 페이지 인덱스
				offsetX, offsetY : X좌표와 Y좌표를 보정해야 하는경우 좌표 수치
		output : ex. <span class="pageText" style="top:10px;left:40px">.....</span>...</span>
		*/
		getData: function(pageIdx, parsedData, offsetX, offsetY) {
			var textNodes = parsedData[pageIdx];
			var html = '';

			$.each(textNodes, function (idx, elem) {
				var elemTop = elem.t;
				var elemLeft = elem.l;
				if(typeof offsetX !== "undefined") {
					elemLeft = parseFloat(elem.l)+offsetX;
				}
				if(typeof offsetY !== "undefined") {
					elemTop = parseFloat(elem.t)+offsetY;
				}



				//if (BROWSER.isMobile() && BROWSER.VERSION.IOS() && typeof elem.link === "undefined") {
				if (localSynap.properties.useMobileTextSearch == false && BROWSER.isMobile() && typeof elem.link === "undefined") {
					// iOS 단말기의 경우 브라우저에서 검색기능을 제공하지않으므로 하이퍼링크를 제외하고는 표현하지 않는다.
				} else {
					var htmlHead = '';
					var htmlTail = '';
					if (typeof elem.link === "string") {
						htmlHead = '<a href="' + elem.link + '" target="_blank" title="' + elem.link + '">';
						htmlTail = '</a>';
					}

					html += htmlHead;
					html += "<span class=\"pageText\" style='" +
							"top:" + elemTop + "px; " +
							"left:" + elemLeft + "px; " +
							"width:" + elem.w + "px; " +
							"height:" + elem.h + "px; " +
							"font-size:" + elem.h + "px; " +
							"position:absolute; ";
					// 투명도 브라우저별
					if (BROWSER.PC.isIE()) {
						if (BROWSER.VERSION.IE() <= 7) {
							html += "filter: alpha(opacity=0); ";
						} else if (BROWSER.VERSION.IE() == 8) {
							html += "-ms-filter:\"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)\"; ";
						} else {
							html += "color:transparent;";
						}
					} else {
						html += "color:transparent;";
					}
					html += "' ";
					
					// 링크태그라면 처리해준다.
					if (typeof elem.link === "number") {
						html += "onclick='localSynap.movePage("+elem.link+");'";
					} 
					// class명은 Image와 레이아웃을 맞추기 위해 동일하게 해주었다.
					if(typeof elem.text === "undefined" || elem.text=="") {
						html += ">&nbsp;</span>";
					} else {
						html += ">" + elem.text + "</span>";
					}
					html += htmlTail;
				}
			});
			
			return html;
		}
	};
	return Search; 
})();
