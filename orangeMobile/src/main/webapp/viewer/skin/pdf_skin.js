// pdf_skin.js
console.log( 'pdf_skin.js' );

var defaultDPI = "M";
var enableScrollEvent = true;
var enableZoomEvent = true;
var LIMIT_NUMBER = 999999;

$.extend(localSynap, (function() {
	var member = {
		$idxFrame: undefined,
		$pdfFrame: undefined,
		arrThumbShowList: [],
		arrPageShowList: [],
		arrVisitPageIdx: [],
		rawObjList: [],
		refreshTimer : null,
		convertServerUri : localSynap.properties.contextPath,
		offsetGetImageSize : 30,
		
		parsePdfXML: function(xml) {
			localSynap.infoXml = xml;
			localSynap.pageSize = parseInt($(xml).find('pdf_cnt').text());
			$(xml).find('pdf').each(function () {
				obj = {
					index: (parseInt($(this).find('id').text())),
					title: $(this).find('title').text(),
					path: encodeURI( $(this).find('path_html').text()),
					w: (parseInt($(this).find('w').text())),
					h: (parseInt($(this).find('h').text()))
				};
				member.rawObjList.push(obj);
				localSynap.objList.push(obj);
			});
		},
		
		parseImgData : function(key, index, dpi) {
			dpi = dpi || defaultDPI;
			
			// 이미지 사이즈는 초기에만 호출하고, 차후에 동적로딩에서 재보정 하든지 한다.
			var i = index;
			while(i < localSynap.pageSize) {
				member.getImageSize(key, i, dpi);
				i = i + member.offsetGetImageSize;
			}
		},
				
		isDynamicLoading: function(){
			if( pdf.INIT_IMAGE_LIMIT == LIMIT_NUMBER || localSynap.pageSize <pdf.INIT_IMAGE_LIMIT) {
				return false;
			}else{
				return true;
			}
		},
		
		hasObjInfo: function(index) {
			if (typeof localSynap.objList[index] != "undefined") {
				return true;
			} else {
				return false;
			}
		},

		getHighQualPath: function(pageNum){
			if (localSynap.properties.isRenderServer && localSynap.isImageMode()) {
				return member.convertServerUri + localSynap.objList[pageNum].path + '?dpi=H';
			} else {
				return localSynap.getResultDir()+localSynap.objList[pageNum].path;
			}
		},
		
		getImagePath: function(pageNum){
			if (localSynap.properties.isRenderServer && localSynap.isImageMode()) {
				return member.convertServerUri + localSynap.objList[pageNum].path + '?dpi=M';
			} else {
				return localSynap.getResultDir()+localSynap.objList[pageNum].path;
			}
		},
		
		getThumbPath: function(pageNum){
			if (localSynap.properties.isRenderServer && localSynap.isImageMode()) {
				return member.convertServerUri + localSynap.objList[pageNum].path + '?dpi=L';
			} else {
				return localSynap.getResultDir()+localSynap.objList[pageNum].path;
			}
		},
		
		getImageSize : function (key, pageIdx, dpi) {
			pageIdx = (typeof pageIdx !== "undefined") ? pageIdx : 0;
			dpi = (typeof dpi !== "undefined") ? dpi : defaultDPI;
			var obj = {
				index: 0,
				path: "",
				w: 0,
				h: 0
			};
			var url = member.convertServerUri + '/dimension/' + key + '/' + pageIdx +'-'+ member.offsetGetImageSize;
			$.ajax({
				type: "GET",
				url: url,
				async: false,
				dataType: "json",
				error: function(data){
					//alert('error')
				},
				complete : function(data){
					//alert('complate')
				},
				success:function(data) {
					// 썸네일 영역을 뺀 부분을 화면표시 너비로 잡아준다.
					var thumbWidth = 0;
					if ( $('thumbnail').length > 0 ) {
						thumbWidth = parseInt($('#thumbnail').css('width'));
					}
					var winWidth = $(window).width() - thumbWidth - 20;
					// 헤더영역보다 조금 더 여유있게 화면표시 높이를 잡아준다.
					var headerHeight = 0;
					if ( $('#header').length > 0 ) {
						headerHeight = parseInt($('#header').css('height'));
					}
					var winHeight = $(window).height() - headerHeight * 2;
					$.each(data, function (idx, elem) {
						if (typeof member.rawObjList[elem.p] == "undefined") {
							rawObj = {
								index: elem.p,
								path: '/thumbnail/'+key+'/'+ elem.p,
								w: elem.w,
								h: elem.h
							};
							member.rawObjList[elem.p] = rawObj;
							// 가로가 긴 문서는 화면폭에 맞춰줌
							var objHeight = elem.h;
							var objWidth = elem.w;
							var heightPerWidth = objHeight / objWidth;
							var width = winWidth > objWidth ? objWidth : winWidth;
							var height = width == elem.w ? objHeight : width * heightPerWidth;
							if (height > winHeight) {
								height = winHeight;
								width = height / heightPerWidth;
							}
							
							obj = {
								index: elem.p,
								path: '/thumbnail/'+key+'/'+ elem.p,
								w: width,
								h: height
							};
							member.rawObjList[elem.p] = obj;
							localSynap.objList[elem.p] = obj;
						}
					});
				}
			});
		},
		
		// 이미지 태그를 생성한다. src는 빈칸으로 생성하고, 동적로딩에서 path를 넣어준다.
		createPageElement: function (idNum) {
			var imgpath = '';
			// 초기에는 기준갯수만 이미지 로딩
			if( !member.isDynamicLoading() || pdf.INIT_IMAGE_LIMIT>idNum) { // 동적로딩 설정이 되어 있지않거나 동적로딩 갯수보다 낮은 순번일때
				//imgpath = member.getImagePath(idNum);
				member.arrPageShowList.push(idNum);
			}
			
			//var html = '<img id="page' + idNum +'" name="'+idNum+'" src="" unselectable="on" onerror="image_error(this)" onload="onLoadImg('+ idNum +')" title="page'+ (idNum + 1) +'" alt="page'+ (idNum + 1) +'" />';
			var html = "";
			var failImagePath = 'image/common/img_loading.png';
			html = '<img id="page' + idNum +'" name="'+idNum+'" src="" style="background-position: 50% 50%; background-repeat: no-repeat; background-image: url(' + failImagePath  + ');" unselectable="on"  onload="onLoadImg('+ idNum +')" title="page'+ (idNum + 1) +'" onerror="image_error(this)" alt="page'+ (idNum + 1) +'" />';
			
			return html;
		},
		
		createThumbElement: function (idNum) {
			var pageNum = idNum + 1;
			var margin='50%';
			var curWidth = localSynap.properties.thumbnailWidth;
			var thumbHeight = parseInt((curWidth/localSynap.objList[idNum].w) * localSynap.objList[idNum].h);
		
			var imgpath = '';
			// 초기에는 기준갯수만 이미지 로딩
			if( pdf.INIT_IMAGE_LIMIT == LIMIT_NUMBER || pdf.INIT_IMAGE_LIMIT>idNum) { // 동적로딩 설정이 되어 있지않거나 동적로딩 갯수보다 낮은 순번일때
				//imgpath = member.getThumbPath(idNum);
				member.arrThumbShowList.push(idNum);
			}
			
			//html += '<img id="thumb' + idNum +'" name="'+idNum+'" src="'+imgpath+'" style="height:' + thumbHeight + 'px;width:100%" unselectable="on" onerror="image_thum_error(this)" title="page'+ (idNum + 1) +'" alt="page'+ (idNum + 1) +'"/>'
			var html = "";
			var failImagePath = 'image/common/thumb_loading.png';
			html = '<img id="thumb' + idNum +'" name="'+idNum+'" src="" style="height:' + thumbHeight + 'px;width:' + curWidth + '; background-position: 50% 50%; background-repeat: no-repeat; background-image: url(' + failImagePath + ')" unselectable="on" onerror="image_thum_error(this)" title="page'+ (idNum + 1) +'" alt="page'+ (idNum + 1) +'" onload="console.log(' + idNum + ')"/>';
			
			return html;
		},

		initEvent: function() {
			if (BROWSER.isMobile()){
				member.initEventMobile();
			} else {
				member.initEventDesktop();
			}
			
			if (localSynap.hasPdfFrame()){
				if ( BROWSER.isMobile() ) {
					localSynap.lastScrollTop = LIMIT_NUMBER * 1000;
					if ( BROWSER.MOBILE.isIOS() ) {
						$(document).scroll(function(e) {
							if (enableScrollEvent) {
									// 페이지 번호는 실시간으로 업데이트
								var pageChangeBaseHeight = $(window).scrollTop() + $(window).height()/2;
									var pageIdx = localSynap.getCurrentPage() - 1;	// 스크롤 이전의 현재 페이지인덱스
								if (pageIdx > localSynap.objList.length - 1) {
									pageIdx = localSynap.objList.length - 1;
								}
								var pageObj = $("#page-area" + pageIdx);
									var pageBottom = pageObj.position().top + pageObj.height();	// 40은 헤더영역
								var winScrollTop = $(window).scrollTop();
									// 스크롤 성능 개선
								if ( localSynap.lastScrollTop < winScrollTop ) {
										// 아래로 스크롤 할 때
									var winHeight = $(window).height();
									while( winScrollTop + winHeight> pageObj.position().top + pageObj.height()) {
										++pageIdx;
										if (pageIdx + 1 > localSynap.objList.length) {
											pageIdx = localSynap.objList.length;
											break;
										}
										pageObj = $("#page-area" + pageIdx);
									}
									--pageIdx;
								} else {
										// 위로 스크롤 할 때
									while( winScrollTop  < pageObj.position().top  ) {
										--pageIdx;
										if ( pageIdx < 0 ) {
											break;
										}
										pageObj = $("#page-area" + pageIdx);
									}
									++pageIdx;
								}
								// 스크롤 시 첫페이지는 예외처리 해준다.
								if ( winScrollTop < 20 || pageIdx < 0 ) {	  // 빈틈 20
									pageIdx = 0;
								}
								pdf.changePaperPageNumber(pageIdx + 1);
								pdf.imageReload(pageIdx + 1, 'page', member.arrPageShowList);
							}
							enableScrollEvent = true;
							localSynap.lastScrollTop = $(window).scrollTop();
						});
					} else {
						member.$pdfFrame.scroll(function(e) {
							if (enableScrollEvent) {
								var pageChangeBaseHeight = member.$pdfFrame.height()/2;
								var pageIdx = localSynap.getCurrentPage() - 1;	// 스크롤 이전의 현재 페이지인덱스
								var pageObj = $("#page-area" + pageIdx);
								var pageBottom = pageObj.position().top + pageObj.height();
								
								if ( localSynap.lastScrollTop < member.$pdfFrame.scrollTop() ) {
									// 아래로 스크롤 할 때
									// 스크롤 마지막은 끝페이지 넘버를 준다.
									if (document.getElementById("contents").scrollHeight == (member.$pdfFrame.scrollTop() + member.$pdfFrame.height())) {
										pageIdx = localSynap.pageSize - 1;
										
									} else {
										while ( pageBottom < pageChangeBaseHeight && pageIdx >= 0) {
										++pageIdx;
										pageObj = $("#page-area" + pageIdx);
										pageBottom = pageObj.position().top + pageObj.height();
										}
									}
								} else {
									// 위로 스크롤 할 때
									if (member.$pdfFrame.scrollTop() == 0) {
										pageIdx = 0;
									} else {
										while ( pageBottom > pageChangeBaseHeight && pageIdx >= 0) {
											--pageIdx;
											if (pageIdx < 0) {
												break;
											}
											pageObj = $("#page-area" + pageIdx);
											pageBottom = pageObj.position().top + pageObj.height();
											
										}
										++pageIdx;
									}
								}
								pdf.changePaperPageNumber(pageIdx + 1);
								pdf.imageReload(pageIdx + 1, 'page', member.arrPageShowList);
							}
							enableScrollEvent = true;
							localSynap.lastScrollTop = member.$pdfFrame.scrollTop();
							localSynap.onScroll && localSynap.onScroll(e);
						});
					}
				} else {
					member.$pdfFrame.scroll(function(e) {
					if (enableScrollEvent) {
						var pageChangeBaseHeight = member.$pdfFrame.height()/2;
						for (var i=0; i<localSynap.pageSize; i++) {
							var pageBottom = $("#page-area" + i).position().top + $("#page-area" + i).height();
							if ( pageBottom > pageChangeBaseHeight) {
								if ((i+1)!=localSynap.getCurrentPage()){
									pdf.changePaperPageNumber(i + 1);
									pdf.imageReload(i + 1, 'page', member.arrPageShowList);
									pdf.moveThumbPage(i + 1);
								}
								break;
							}
						}
					}
					enableScrollEvent = true;
					localSynap.onScroll && localSynap.onScroll(e);
					});
				}
			}
		},
		
		initEventDesktop: function() {
			$(document).delegate('#slideJumpButton', 'click', function(){
				try{
					var pageNum = parseInt($('#inputPageNumber').val());
					if( pageNum > localSynap.pageSize || pageNum < 0 || isNaN(pageNum) )
						throw new Error("wrong Number");
					pdf.movePage(pageNum);
				}catch(e){
					$('#inputPageNumber').val( localSynap.getCurrentPage() );
				}
			});

			$(document).delegate('#inputPageNumber', 'keypress', function(e){
				if (e.which==13){
					$('#slideJumpButton').click();
				}
			});

			$('a.closeBtn').on('click', function(e) {
				e.preventDefault();
				$('#thumbnail').animate({ left: -$('#thumbnail').width() },
				  function() {
						$(window).resize();
						// $('#leftPanel_hidden').toggle(true);
					});
				localSynap.leftPanelShow = !localSynap.leftPanelShow;
			});
			$('a.openThumbnail').on('click', function(e){
				e.preventDefault();
				
				$('#thumbnail').animate({ left: 0 },
				  function() {
						$(window).resize();
						// $('#leftPanel_hidden').toggle(false);
					});
				localSynap.leftPanelShow = !localSynap.leftPanelShow;
			});

			$(document).delegate('#documentScale', 'click', function(e){
				if (enableZoomEvent){
					enableZoomEvent = false;
					var cur_ratio = localSynap.ratio;
					var btn_top = $('#documentScale').offset().top;  // bar 상대top
					var btn_height = $('#documentScale').height(); // bar Height
					var evt = e.clientY || window.event.y;
					var mouse_y = evt - btn_top; // bar 영역내 클릭 위치
					var tab_range = parseInt(btn_height / 3);
					
					var new_ratio = 1;
					var bar3_top = btn_top;
					if (mouse_y<tab_range){
						new_ratio = 2;
					} else if (mouse_y<(tab_range*2)) {
						new_ratio = 1.5;
						bar3_top += parseInt(btn_height/2);
					}else{
						bar3_top += btn_height;
					}
					bar3_top = bar3_top - parseInt($('.bar3').height()/2);
					$('.state').text(new_ratio+'X');
	
					
					while (cur_ratio != new_ratio) 
					{
						if (cur_ratio<new_ratio){
							member.zoomIn();
							cur_ratio += 0.5;
						}else if (cur_ratio>new_ratio){
							member.zoomOut();
							cur_ratio -= 0.5;
						}
					}
					
					$('.bar3').offset({top:bar3_top});
					enableZoomEvent = true;
				}
			});
			
			$(document).keydown(function(e) {
				if (e.which == 38 || e.which == 40) {
					e.preventDefault();
				} else if (e.which == 37) {
					e.preventDefault();
				} else if (e.which == 39) {
					e.preventDefault();
				} else if (e.which == 33 || e.which == 34) {
					e.preventDefault();
				}
			});
			// 썸네일 영역이 있으면 변수값을 true로 초기화 한다.
			if (localSynap.hasIndexFrame()){
				localSynap.leftPanelShow = true;
			}else{
				localSynap.leftPanelShow = false;
			}

			if (localSynap.hasIndexFrame()){
				member.$idxFrame.parent().scroll(function(e) {
					if (enableScrollEvent) {
						var idxScrollFrame = member.$idxFrame.parent();
						var pageChangeBaseHeight = idxScrollFrame.height()/2;
						for (var i=0; i<localSynap.pageSize; i++) {
							var pageBottom = $("#thumb-area" + i).offset().top + $("#thumb-area" + i).height();
							if ( pageBottom > pageChangeBaseHeight) {
								// 동적로딩을 그린다.
								pdf.imageReload(i, 'thumb', member.arrThumbShowList);
								break;
							}
						}
					}
					enableScrollEvent = true;
				});
			}
		},
		
		initEventMobile: function() {
			/*
			$('.navPrev').click(function(e){
					e.preventDefault();
					localSynap.movePrev();
				}
			);
			$('.navNext').click(function(e){
					e.preventDefault();
					localSynap.moveNext();
				}
			);
			*/
			$('.paging').click(function(e) {
				enableScrollEvent = false;
			});
			
			$(window).on("orientationchange",function(){
				if ( BROWSER.MOBILE.isGalaxyNote3() ) {
					$('#wrap').css('height', $(window).height() - $('#header').height());
				}
				// 안드로이드 ICS는 landscape에만 변환을 해준다.(사실 portrait로 돌아갈때 objList가 왜 크게 변형되는지 모르겠음..)
				if (BROWSER.VERSION.isAndroidICS()) {
					if(window.orientation == 0) // Portrait
					{
					}
					else // Landscape
					{
						// 썸네일 영역을 뺀 부분을 화면표시 너비로 잡아준다.
						var thumbWidth = 0;
						if ( $('thumbnail').length > 0 ) {
							thumbWidth = parseInt($('#thumbnail').css('width'));
						}
						var winWidth = $(window).width() - thumbWidth - 20;
						// 헤더영역보다 조금 더 여유있게 화면표시 높이를 잡아준다.
						var headerHeight = 0;
						if ( $('#header').length > 0 ) {
							headerHeight = parseInt($('#header').css('height'));
						}
						var winHeight = $(window).height() - headerHeight * 2;
						$.each(member.rawObjList, function (idx, elem) {
							// 가로가 긴 문서는 화면폭에 맞춰줌
							var objHeight = elem.h;
							var objWidth = elem.w;
							var heightPerWidth = objHeight / objWidth;
							var width = winWidth > objWidth ? objWidth : winWidth;
							var height = width == elem.w ? objHeight : width * heightPerWidth;
							if (height > winHeight) {
								height = winHeight;
								width = height / heightPerWidth;
							}
							
							obj = {
								index: elem.index,
								path: '/thumbnail/'+localSynap.jobId+'/'+ elem.index,
								w: width,
								h: height
							};
							localSynap.objList[elem.index] = obj;
							localSynap.resizePage(idx);
						});
					}
				}
			});
		},
					
		zoomOut: function() {
			// horizontal scroll
			/******************************************************************/
			var contentWidth = member.$pdfFrame.width();
		
			var prevScrollW = member.$pdfFrame.get(0).scrollWidth;
			var prevScrollL = member.$pdfFrame.scrollLeft();
			var scrollLeftRatio = (prevScrollL / (prevScrollW - contentWidth));
			/******************************************************************/
			
			var oldRatio = localSynap.ratio;
			localSynap.ratio = RATIO_NUMBERS.reduceRatio();
			var newRatio = localSynap.ratio;
			var offsetTop = $('#page-area' + (localSynap.getCurrentPage() - 1)).position().top * newRatio / oldRatio;
			member.resizePageAll();
		
			// vertical scroll
			/******************************************************************/
			localSynap.movePage(localSynap.getCurrentPage());
			member.$pdfFrame.scrollTop(member.$pdfFrame.scrollTop() - offsetTop + 20);		// 20은 자동 마진영역
			
			/******************************************************************/
		
			// horizontal scroll
			/******************************************************************/
			var newScrollW = member.$pdfFrame.get(0).scrollWidth;
			var newScrollLeft = (newScrollW - contentWidth) * scrollLeftRatio;
			member.$pdfFrame.scrollLeft(newScrollLeft);
			/******************************************************************/
		},
		
		zoomIn: function() {
			// horizontal scroll
			/******************************************************************/
			var contentWidth = member.$pdfFrame.width();
		
			var prevScrollW = member.$pdfFrame.get(0).scrollWidth;
			var prevScrollL = member.$pdfFrame.scrollLeft();
			var scrollLeftRatio = (prevScrollL / (prevScrollW - contentWidth));
			/******************************************************************/
		
			var oldRatio = localSynap.ratio;
			localSynap.ratio = RATIO_NUMBERS.increaseRatio();
			var newRatio = localSynap.ratio;
			var offsetTop = $('#page-area' + (localSynap.curPage - 1)).position().top * newRatio / oldRatio;
			member.resizePageAll();
		
			// vertical scroll
			/******************************************************************/
			localSynap.movePage(localSynap.getCurrentPage());
			member.$pdfFrame.scrollTop(member.$pdfFrame.scrollTop() - offsetTop + 20);		// 20은 마진영역
			
			/******************************************************************/
		
			// horizontal scroll
			/******************************************************************/
			var newScrollW = member.$pdfFrame.get(0).scrollWidth;
			var newScrollLeft = (newScrollW - contentWidth) * scrollLeftRatio;
			member.$pdfFrame.scrollLeft(newScrollLeft);
			/******************************************************************/
		},
		getThumbnailWidth: function() {
			if (localSynap.hasIndexFrame() && $('#thumbnail').length>0){
				return $('#thumbnail').get(0).offsetWidth;
			}else{
				return 0;
			}
		},
		resizeOnClosingSelect: function() {
			var $contentWrap = $('#contents');
			// landscape / portrait 회전 과정에서, contents 자체 높이로 계산하니, 높이가 변경되지 않아, 이 함수가 호출될때 마다 새로 계산함.
			var height = $(window).height() - $('#header').height();
			$contentWrap.height(height-1);
			$contentWrap.height(height);
		},
		// 이미지의 resize 함수(확대/축소에 따른)
		resizePageAll: function() {
			$.each(localSynap.objList, function(pageIndex, pageElem) {
				var width = pageElem.w * localSynap.ratio;
				var height = pageElem.h * localSynap.ratio;
				var imgElem = $("#page" + pageIndex);
				
				// 이미지 태그가 존재하는지 체크
				if ( imgElem.length == 0 ) {
					return;
				}
				
				// 이미지사이즈가 새 배율 사이즈와 다르다면, 새 배율 사이즈를 적용
				if ( imgElem.width() != width && imgElem.height() != height ) {
					imgElem.width(width).height(height);
				} else {
					return;
				}
				
				// 이미지태그에 src가 존재할 때에만 고품질이미지로 교체
				if (imgElem.attr('src').length > 0) {
					if (localSynap.ratio > 1) {
						hPath = member.getHighQualPath(pageIndex);
						if (hPath != imgElem.attr('src')) { 
							imgElem.attr('src', hPath);
						}
					} else {
						mPath = member.getImagePath(pageIndex);
						if (mPath != imgElem.attr('src')) {
							imgElem.attr('src', mPath);
						}
					}
				}
				
				
				var pageArea = $("#page-area" + pageIndex);
				if( BROWSER.PC.isIE() && BROWSER.VERSION.IE() === "6") {
					pageArea.width(width);
				}else{
					pageArea.css('min-width', width);
					pageArea.css('height', height);
				}

				if (localSynap.properties.usePdfText) {
					// 텍스트XML 위치 resize
					var adjustRatio = localSynap.pxToPtRatio * localSynap.ratio;
					if (localSynap.isImageMode()) {
						adjustRatio = RATIO_NUMBERS.getRatio();
					}
					pageArea.find('span').each(function (idx, elem) {
						if (elem.getAttribute('class') == 'pageText') {
							obj = localSynap.searchMeta[pageIndex][idx];
							elem.style.top = (obj.t * adjustRatio) + "px";
							elem.style.left = (obj.l * adjustRatio) + "px";
							elem.style.width = (obj.w * adjustRatio) + "px";
							elem.style.height = (obj.h * adjustRatio) + "px";
							elem.style.fontSize = (obj.h * adjustRatio) + "px";
						}
					});
				}
			});
		},

		resize: function() {
			if (BROWSER.isMobile()){
				member.resizeMobile();
			}else{
				member.resizeDesktop();
			}
		},
		
		resizeDesktop: function() {
			setResizeHeaderTitle(); // #DEFECT-2580 title resize를 위해 호출
			
			if( localSynap.leftPanelShow === true )
				member.$pdfFrame.css('left', member.getThumbnailWidth()); 
			else
				member.$pdfFrame.css('left', 0);
			
			member.resizePageAll();
		},

		resizeMobile: function() {
			var pageElem = $('#page');
			var originalWidth = localSynap.objList[localSynap.getCurrentPage() - 1].w;
			var originalHeight = localSynap.objList[localSynap.getCurrentPage() - 1].h;
			var slideRatio = originalHeight / originalWidth;
			var deviceWidth = $(window).width();
			deviceWidth -= 20;
			pageElem.width(deviceWidth);
			pageElem.height(deviceWidth * slideRatio);
			
			// 안드로이드 기본브라우저에서 필요한 리사이즈함수. 조건을 뺄까?
			if (BROWSER.MOBILE.isAndroid()) {
				member.resizeOnClosingSelect();
			}
		}

		
	};

	var pdf = {
		curPage: 1,
		FILENAME: encodeURI(localSynap.getFileName()),
		pageSize: 0,
		ratio: 1,
		pxToPtRatio: undefined,
		scrollTopValue: 0,
		loadedImgCount: 0,
		objList: [],
		INIT_IMAGE_LIMIT: 10, // 동적로딩 갯수. LIMIT_NUMBER로 설정하면 동적로딩을 하지 않는다.
		INIT_MAKE_LIMIT: 10, // 초기화시 로딩 이미지 갯수
		searchMeta: {},
		fullText: {},
		textXmlQueue: [],

		hasIndexFrame: function() {
			if (typeof member.$idxFrame != "undefined") {
				return member.$idxFrame.length>0; 
			} else {
				return false;
			}
		},

		hasPdfFrame: function() {
			if (typeof member.$pdfFrame != "undefined") {
				return member.$pdfFrame.length>0; 
			} else {
				return false;
			}
		},

		parseImgXml: function(xml) {
			localSynap.infoXml = xml;
			localSynap.pageSize = parseInt($(xml).find('image_cnt').text());
			$(xml).find('image').each(function () {
				obj = {
					index: (parseInt($(this).find('id').text())),
					path: encodeURI( $(this).find('path_image').text()),
					w: (parseInt($(this).find('width').text())),
					h: (parseInt($(this).find('height').text()))
				};
				member.rawObjList.push(obj);
				localSynap.objList.push(obj);
			});
		},
		prepareFullText: function (index) {
			var textObj = localSynap.searchMeta[index];
			$(textObj).each(function (textIndex, elem) {
				if (typeof localSynap.fullText[index] === "undefined") {
					localSynap.fullText[index] = elem.text;
				} else {
					localSynap.fullText[index] += elem.text;
				}
			});
		},	
		parseTextXmlWithKey: function (pageIdx) {
			var key = localSynap.jobId;
			var targetUrl = member.convertServerUri +'/thumbnailxml/'+key+'/'+pageIdx+'?dpi='+defaultDPI;
			pdfSearch.loadTextXmlAsync(targetUrl, pageIdx);
		},

		parseTextXmlFromFile: function (fileName, index) {
			if (localSynap.properties.fileType == 'pdf') {
				var pageNum = parseInt(index) + 1;
				var xmlFile = fileName + '_' + pageNum + '.xml';
				var targetUrl = localSynap.getResultDir() + fileName + '.files/' + xmlFile;
				pdfSearch.loadTextXmlAsync(targetUrl, index);
			} else {
				var padding = '0000';
				// 이미지변환파일은 개별폴더가 아닌 대상폴더에 모두 생성된다.
				var pageNum = parseInt(index)+1;
				var padNo = padding.substring(0, padding.length -(pageNum+"").length) + pageNum;
				file = localSynap.getResultDir() + fileName + '_' + padNo + '.xml';
				pdfSearch.loadTextXmlAsync(file, index);
			}
		},

		getText: function (pageNum) {
			return pdfSearch.getData(pageNum, localSynap.searchMeta);
			// RenderServer에서 text를 image보다 먼저 줄 수 없을 때를 방지하기 위한 코드. 추후에 개선되면 삭제하자.
			if (localSynap.properties.isRenderServer && localSynap.isImageMode()) {
				if (pageIndex in member.arrVisitPageIdx == false) {
					return "";
				}
			}
		},
		// appendTextIntervalFunc를 setInterval로 담을 객체
		appendTextInterval: [],
		// 큐에 담긴 textXmlObject를 계속 붙여준다.
		appendTextIntervalFunc: function () {
			// console.log('appending ...')
			if (localSynap.textXmlQueue.length > 0) {
				var xmlObj = localSynap.textXmlQueue.shift();
				localSynap.searchMeta[xmlObj.index] = xmlObj.content;
				append_text_async(xmlObj.index);
			} else {
				clearInterval(localSynap.appendTextInterval.shift());
				// console.log('complete');
			}
		},

		getImagePath: function (pageIdx) {
			if (pageIdx in member.arrVisitPageIdx == false) {
				member.arrVisitPageIdx.push(pageIdx);
			}
			return member.getImagePath(pageIdx);
		},
		getThumbPath: function (pageIdx) {
			return member.getThumbPath(pageIdx);
		},
		
		// 썸네일 전체에 대한 div를 생성한다.
		createThumbDiv: function () {
			if (typeof member.$idxFrame != "undefined") {
				for (var i = 0; i < localSynap.pageSize; ++i) {
					var pageNum = 1+i;
					var curWidth = localSynap.properties.thumbnailWidth;
					var thumbHeight = parseInt((curWidth/localSynap.objList[i].w) * localSynap.objList[i].h);
					var html = '<div class="imgBox"  style="height:'+thumbHeight+'px; width:'+curWidth+'px;"><em class="thum_num">' + pageNum + '</em><div id="thumb-area' + i + '" onclick="enableScrollEvent=false; localSynap.movePage(' + pageNum + ');">';
					html += '</div>';
					
					member.$idxFrame.append(html);
				}
			}
		},
	
		// 페이지 전체에 대한 div를 생성한다.
		createContentDiv: function () {
			var winWidth = $(window).width();
			for (var i = 0; i < localSynap.pageSize; ++i) {
				var height = localSynap.objList[i].h;
				var width = localSynap.objList[i].w;
				var html = '<div id="page-area' + i + '" class="page-element" ';
				html += 'style="width:'+width+'px;height:'+height+'px;';
				//html += 'style="';
				html += 'margin-left:auto; margin-right:auto; position:relative;"';
				html += '></div>';
				member.$pdfFrame.append(html);
			}
		},
		
		// img를 INIT_IMAGE_LIMIT 갯수만큼만 만든다.
		loadImage: function (startPageNum, endPageNum) {
			for (var i=startPageNum; i<endPageNum; i++) {
				if (localSynap.objList[i] == null) {
					member.parseImgData(localSynap.jobId, i);
				}
				area = $('#page-area'+i);
				area.append(member.createPageElement(i));
				// 초기화면에 해당하는 페이지는 src를 삽입해준다.
				page = $('#page'+i);
				if (i < pdf.INIT_MAKE_LIMIT) {
					page.attr('src', member.getImagePath(i));
				}
				page.css('width', localSynap.objList[i].w);
				page.css('height', localSynap.objList[i].h);
			}
			//pdf.resizePageAll();
			if (endPageNum < localSynap.pageSize){ // 아직 읽을게 남았으면
				var s = endPageNum;
				var e = s + pdf.INIT_MAKE_LIMIT;
				if (e>localSynap.pageSize){
					e = localSynap.pageSize;
				}
				refreshTimer = setTimeout('localSynap.loadImage('+s+','+e+');', 0);
			}
		},

		// img를 INIT_IMAGE_LIMIT 갯수만큼만 만든다.
		loadThumbImage: function (startPageNum, endPageNum) {
			for (var i=startPageNum; i<endPageNum; i++) {
				//member.$idxFrame.append(member.createThumbElement(i));
				$('#thumb-area'+i).append(member.createThumbElement(i));
				if (i < pdf.INIT_MAKE_LIMIT) {
					$('#thumb'+i).attr('src', member.getThumbPath(i));
				}
			}
			if (endPageNum < localSynap.pageSize){ // 아직 읽을게 남았으면
				var s = endPageNum;
				var e = s + pdf.INIT_MAKE_LIMIT;
				if (e>localSynap.pageSize){
					e = localSynap.pageSize;
				}
				setTimeout('localSynap.loadThumbImage('+s+','+e+');', 0);
			}
		},
		
		changePaperPageNumber: function (pageNum) {
			if (BROWSER.isMobile()){
				$('.paging option').eq((pageNum-1)).prop('selected', true);
			}else{
				$('#inputPageNumber').val(pageNum);
				$("#totalPageNumber").text(localSynap.pageSize);
				
				$('#documentList > *').removeClass('thumbnailSel');
				$('#documentList > *:nth-child(' + (pageNum) + ')').addClass('thumbnailSel');
			}
			// 자신을 기준으로 앞/뒤 로딩을 다시 설정
			/*
			if( pdf.INIT_IMAGE_LIMIT != 99999 && pdf.INIT_IMAGE_LIMIT < localSynap.pageSize && localSynap.curPage != pageNum) { // 동적로딩
				pdf.imageReload(pageNum, 'page', member.arrPageShowList);
				//pdf.imageReload(pageNum, 'thumb', member.arrThumbShowList);
			}
			*/
			localSynap.curPage = pageNum;
		},

		// 동적로딩
		// + img태그의 src를 삽입한다.
		imageReload: function (pageIdx, objName, arrShowList) {
			var start = parseInt(pageIdx-(pdf.INIT_IMAGE_LIMIT/2)); // 기준의 절반 앞부터
			start = start < 0 ? 0: start;

			var end = start+pdf.INIT_IMAGE_LIMIT;
			end = end > localSynap.pageSize ? localSynap.pageSize : end;

			//for( var i = 0; i < arrShowList.length; i++ ){
			var i = 0;
			while(arrShowList.length > 0) {
				var idx = arrShowList.shift();
				
				if (idx >= start && idx <=end){ // 범위 내는 로딩
					// 이미 보여지고 있으니 삭제하지 않고 배열에서만 뺀다.
				}else{
					var elImg = document.getElementById(objName+(idx));
					elImg.src = '';
				}
				++i;
			}
			var listIndex = 0;
			for( var i = start; i < end; ++i ){
				var elImg = document.getElementById(objName+(i));

				
				// 재변환 적용
				var imgpath = "";
				if (objName == "thumb") {
					imgpath = member.getThumbPath(i);
				} else {
					if (localSynap.ratio > 1) {
						imgpath = member.getHighQualPath(i);
					} else {
						imgpath = member.getImagePath(i);
					}
				};
				if (elImg!=null){
					if (elImg.src.indexOf(imgpath) < 0) {
						elImg.src = imgpath;
					}
					//중앙정렬이 안되어서 주석처리 elImg.style.display = 'block';
					arrShowList[listIndex] = i;
					++listIndex;
				}
				localSynap.resizePage(i);
			}
		},

		// 1에서 시작
		movePrev: function() {
			if (localSynap.getCurrentPage() < 1) {
				return;
			} else {
				try{
					pdf.movePage(localSynap.getCurrentPage() - 1);
				}catch( e ){
					
				}
			}
		},

		moveNext: function() {
			if (localSynap.getCurrentPage() >= localSynap.pageSize) {
				return;
			} else {
				try{
					pdf.movePage(localSynap.curPage + 1);
				}catch( e ){
					
				}
			}
		},

		// 본문 화면이 해당 페이지로 이동한다.
		// 1부터 시작
		movePage: function (pageNum) {
			if (!(pageNum > 0 && pageNum <= localSynap.pageSize)){
				throw new Error("wrong number");
			}
			//pdf.layoutButtonUpdate();
			var contPos = $("#page0").parent().position();
			var prevPos =  $("#page" + (pageNum - 1)).parent().position();
			enableScrollEvent = false;
			pdf.scrollTopValue = parseInt( prevPos.top) - contPos.top;
			
			if ( BROWSER.MOBILE.isIOS() ) {
				$(window).scrollTop(pdf.scrollTopValue);
			} else {
				member.$pdfFrame.scrollTop(pdf.scrollTopValue);
			}
			
			pdf.changePaperPageNumber(pageNum);
			pdf.imageReload(pageNum, 'page', member.arrPageShowList);
			pdf.moveThumbPage(pageNum);
			localSynap.onMovePage && localSynap.onMovePage();
		},
		
		// pageIdx : 페이지 인덱스 (0base)
		moveSlide: function (pageIdx) {
			pdf.movePage(pageIdx + 1);
		},
		
		thumbnailScrollPositionFix: function () {
			// 전체화면 해제 시 썸네일 스크롤을 본문과 동기화한다.
			pdf.moveThumbPage(localSynap.curPage);
		},
		
		
		// 썸네일을 해당 페이지로 이동한다.
		moveThumbPage: function (pageNum) {
			if (localSynap.hasIndexFrame()){
				var contPos = $("#thumb-area0").parent().position();
				var prevPos = $("#thumb-area" + (pageNum - 1)).parent().position();
				
				var idxScrollTopValue = parseInt( prevPos.top) - contPos.top;
				if( !($('#thumbnail').scrollTop() <= idxScrollTopValue 
					&& idxScrollTopValue + $('.imgBox').height() <= $('#thumbnail').scrollTop() + $('#thumbnail').height()) ) {
					member.$idxFrame.parent().scrollTop(idxScrollTopValue);
				}
				pdf.thumbImageEmphasized(pageNum);
			}
		},
		
		// 현재 페이지에 해당하는 썸네일 이미지에 테두리를 친다.
		thumbImageEmphasized: function (pageNum) {
			$(".imgBox.thumbnailSel").removeClass("thumbnailSel");
			$("#thumb-area" + (pageNum - 1)).parent().addClass("thumbnailSel");
		},
		
		resizePage: function (pageIndex) {
			var pageElem = localSynap.objList[pageIndex];
			var width = pageElem.w * localSynap.ratio;
			var height = pageElem.h * localSynap.ratio;
			var id = "#page" + pageIndex;
			var imgElem = $(id);
			if ( imgElem.length == 0 
				|| imgElem.attr('src').length == 0
				|| (imgElem.width() == width && imgElem.height() == height)) {
				return;
			}
			
			// 재변환 적용
			imgElem.width(width).height(height);
			if (localSynap.ratio > 1) {
				if (imgElem.attr('src').indexOf('dpi=M') > -1) {
					imgElem.attr('src', member.getHighQualPath(pageIndex));
				}
			} else {
				if (imgElem.attr('src').indexOf('dpi=H') > -1) {
					imgElem.attr('src', member.getImagePath(pageIndex));
				}
			}
			
			var pageArea = $("#page-area" + pageIndex);
			if( BROWSER.PC.isIE() && BROWSER.VERSION.IE() === "6") {
				pageArea.width(width);
			}else{
				pageArea.css('min-width', width);
				pageArea.css('height', height);
			}
		
			if (localSynap.properties.usePdfText) {	
				// 텍스트XML 위치 resize
				setTimeout(function () {
					pageArea.find('span').each(function (idx, elem) {
						var elemStyle = elem.style;
						var adjustRatio = localSynap.pxToPtRatio * localSynap.ratio;
						if (elem.getAttribute('class') == 'pageText') {
							obj = localSynap.searchMeta[pageIndex][idx];
							if (typeof obj.text == "undefined") {
								return;
							}
							elemStyle.top = (obj.t * adjustRatio) + "px";
							elemStyle.left = (obj.l * adjustRatio) + "px";
							elemStyle.width = (obj.w * adjustRatio) + "px";
							elemStyle.height = (obj.h * adjustRatio) + "px";
							elemStyle.fontSize = (obj.h * adjustRatio) + "px";
						}
					});
				}, 50);
			}
		},
		
		
		skinReadyFunc: function() {
			window.onresize = member.resize;
			
			if (localSynap.properties.isRenderServer == true) {
				member.fileInfo = localSynap.status;
				localSynap.pageSize = localSynap.status.pageNum;
			}
			if( BROWSER.isMobile() ){
				pdf.skinReadyMobileFunc();
			}else{
				pdf.skinReadyDesktopFunc();
				setResizeHeaderTitle();
			}
			containerSizeAdjust();
			setFullScreenClick();
			localSynap.onLoadedBody && localSynap.onLoadedBody();
			if (!localSynap.isAllowCopy()) {
				localSynap.killCopyHtml();
			}
		},

		skinReadyDesktopFunc: function() {
			slideKeyboardControl();
			
			member.$pdfFrame = $('#contents');
			member.$idxFrame = $('#indexWrap');
			member.$idxFrame.css('margin-top', '30px');
			
			if ($('.leftPanel').length!=0){
				if (localSynap.isSingleLayout()){
					$('.leftPanel').hide();
					member.$idxFrame = undefined;
					localSynap.leftPanelShow = false;
					$('#leftPanel_hidden').css('display', 'none');
				}
				else{
					localSynap.leftPanelShow = true;
				}
			}
			
			// 헤더 아이콘 설정
			var icon = $('#iconImage');
			if (localSynap.properties.isRenderServer && localSynap.isImageMode()) {
				var fileExt = member.fileInfo.format.toLowerCase();
				// 서버에서 처리하는 루틴
				if ( fileExt == "pptx" || fileExt == "ppt") {
					icon.attr('src' , "image/header/icon_format_PPT.png");
				} else if ( fileExt == "docx" || fileExt == "doc" ) {
					icon.attr('src' , "image/header/icon_format_DOC.png");
				} else if ( fileExt == "pdf" ) {
					icon.attr('src' , "image/header/icon_format_PDF.png");
				} else if ( fileExt == "hwp" || fileExt == "hml" || fileExt == "hwp2k" || fileExt == "hwp3" ) {
					icon.attr('src' , "image/header/icon_format_HWP.png");
				} else if ( fileExt == "txt" ) {
					icon.attr('src' , "image/header/icon_format_TXT.png");
				} else if ( fileExt == "gif" || fileExt == "png" || fileExt == "jpg" || fileExt == "tiff" || fileExt == "jpeg" ) {
					icon.attr('src' , "image/header/icon_format_IMG.png");
				}
				// 초기 한정된 페이지만 정보를 받아온다.
				// 텍스트는 모두 동일하다고 생각한다.
				if (fileExt == "txt") {
					member.getImageSize(localSynap.jobId, 0);
					obj = localSynap.objList[0];
					for (var i = 0; i < localSynap.pageSize; ++i) {
						localSynap.objList[i].w = obj.w;
						localSynap.objList[i].h = obj.h;
						localSynap.objList[i].path = obj.path;
						localSynap.objList[i].index = obj.index;
					}
				} else {
					member.parseImgData(localSynap.jobId, 0);
				}
			} else {
				// 단일제품 루틴
				var fileExt = localSynap.properties.fileType || member.fileInfo.format.toLowerCase();
				if ( fileExt == "pdf" ) {
					icon.attr('src' , "image/header/icon_format_PDF.png");
					member.parsePdfXML(localSynap.properties.xmlObj);
				}
				else{
					if ( fileExt == "pptx" || fileExt == "ppt") {
						icon.attr('src' , "image/header/icon_format_PPT.png");
					} else if ( fileExt == "docx" || fileExt == "doc" ) {
						icon.attr('src' , "image/header/icon_format_DOC.png");
					} else if ( fileExt == "hwp" || fileExt == "hml" || fileExt == "hwp2k" ) {
						icon.attr('src' , "image/header/icon_format_HWP.png");
					}
					
					pdf.parseImgXml(localSynap.properties.xmlObj);
				}
			}
			
			if (localSynap.properties.isRenderServer === true) {
				setDownloadButton(localSynap.downloadUrl);
			} else {
				setDownloadButton(localSynap.properties.xmlObj);
			}
			setPrintButton(localSynap.properties.isRenderServer);
			// ratio를 구하기위해 #contentWrap width가 결정되어 있어야 한다.
			member.$pdfFrame.css('left', localSynap.leftPanelShow ? member.getThumbnailWidth() : 0);
			member.initEvent();

			localSynap.createContentDiv();
			localSynap.createThumbDiv();
			
			var loadingCnt = localSynap.pageSize;
			if(member.isDynamicLoading() && localSynap.pageSize>pdf.INIT_MAKE_LIMIT){
				loadingCnt = pdf.INIT_MAKE_LIMIT;
			}
			
			RATIO_NUMBERS.setRatioIndex(1);
			
			localSynap.loadImage(0, loadingCnt);
			if (!localSynap.isSingleLayout()){
				localSynap.loadThumbImage(0, loadingCnt);
			}
			member.resizePageAll();
			pdf.movePage(localSynap.getCurrentPage());
			
			// 텍스트XML 파싱처리 : HTML변환기는 한번에 텍스트를 파싱한다.
			if (localSynap.properties.isRenderServer === false) {
				//pdf.parseTextXml(pdf.FILENAME);
			}
		},

		skinReadyMobileFunc: function() {
			initMobile();
			member.$pdfFrame = $('#contents');

			if (localSynap.isImageMode()) {
				member.parseImgData(localSynap.jobId, 0);
			} else {
				member.parsePdfXML(localSynap.properties.xmlObj);
			}
			
			for ( var i = 1; i <= localSynap.pageSize; ++i ) {
				$('.paging>select').append('<option>' + i + '</option>');	
			}
			
			$('.paging').append(' / ' + localSynap.pageSize);
			$('.paging>select').change(function(){
				$("select option:selected").each(function(){
					pdf.movePage($(this).index()+1);
				});
			});
			
			member.initEvent();
			
			// div를 먼저 생성한다.
			
			localSynap.createContentDiv();
			localSynap.createThumbDiv();
			
			// 안드로이드 기본브라우저에서 resize시 사용함
			member.contentsHeight = $('#contents').height();
			// 갤노트3 기본브라우저에서는 wrap 높이를 지정해주어야 헤더가 스크롤 시 빨려올라가지 않는다.
			if ( BROWSER.MOBILE.isGalaxyNote3() ) {
				$('#wrap').css('height', $(window).height() - $('#header').height());
			}
			var loadingCnt = localSynap.pageSize;
			if(member.isDynamicLoading() && localSynap.pageSize>pdf.INIT_MAKE_LIMIT && !BROWSER.VERSION.isAndroidICS() ){
				loadingCnt = pdf.INIT_MAKE_LIMIT;
			} else {
				// ICS는 동적로딩을 하지 않는다.
				pdf.INIT_MAKE_LIMIT = localSynap.pageSize;
			}
			
			// iOS계열은 height를 주어야한다.
			if ( BROWSER.MOBILE.isIOS() ) {
				member.$pdfFrame.css('height', document.getElementById('contents').scrollHeight);
			}
			
			RATIO_NUMBERS.setRatioIndex(1);
			localSynap.loadImage(0, loadingCnt);
			pdf.movePage(localSynap.getCurrentPage());
		}
	}
	return pdf;
})());

localSynap.getPageSize = function() {
	return localSynap.pageSize;
}

localSynap.getCurrentPage = function() {
	if (localSynap.curPage < 1) {
		return 1;
	} else {
		return localSynap.curPage;
	}
}

localSynap.killCopyHtml = function() {
	// common
	var index = document.getElementsByName('index')[0];
	if(index) {
		stopBrowserEvent(index.contentWindow.document);
		index.onload=function(){
			stopBrowserEvent(index.contentWindow.document);
		};
	}
}


var RATIO_NUMBERS = {
	curIndex : 0,
	adjustRatio: 1,
	numbers : [1, 1.5, 2],
	increaseRatio : function(){
		if (0 <= this.curIndex && this.curIndex < (this.numbers.length-1)) {
			++this.curIndex;
		}
		return this.numbers[this.curIndex];
	},
	reduceRatio : function() {
		if (0 < this.curIndex && this.curIndex <= (this.numbers.length-1)) {
			--this.curIndex;
		}
		return this.numbers[this.curIndex];
	},
	setRatioIndex : function(_ratio) {
		this.curIndex = this.numbers.length - 1;
		if (_ratio > this.numbers[this.numbers.length - 1]) {
			localSynap.ratio = this.numbers[this.numbers.length - 1];
			return;
		}
		for (var i=0; i<this.numbers.length; i++) {
			if (this.numbers[i] == _ratio) {
				this.curIndex = i;
				break;
			}
		}
	},
	setStandardRatio : function (value) {
		this.adjustRatio = value;
	},
	getRatio : function () {
		return this.numbers[this.curIndex] * this.adjustRatio;
	}
};

function image_thum_error(obj)
{
	$(obj).attr('onclick', 'thumb_retry('+obj.name+')');
}

function image_error(obj)
{
	$(obj).attr('onclick', 'image_retry('+obj.name+')');
}

function thumb_retry(idx)
{
	$('#thumb'+idx).attr('src' , localSynap.getImagePath(idx)).attr('onclick', '').attr('alt', 'thumb'+(idx*1+1));
}

function image_retry(idx)
{
	$('#page'+idx).attr('src' , localSynap.getImagePath(idx)).attr('onclick', '').attr('alt', 'page'+(idx*1+1));
	localSynap.resizePage(idx);
}

function append_text_async(index) {
	var $page = $("#page" + index);
	if (localSynap.properties.usePdfText === false) { return; }	
	var $pageArea = $("#page-area" + index);
	if ($pageArea.find("span").length == 0 && localSynap.searchMeta[index] ) {
		$pageArea.append(localSynap.getText(index));

		localSynap.pxToPtRatio = parseInt($pageArea.css('width')) / localSynap.pageInfo.width;
		var adjustRatio = localSynap.pxToPtRatio * localSynap.ratio;	
		// 이미지변환기용 스킨은 원본이미지와 크기가 다른 경우가 있다.
		// 텍스트를 배치할 때 그 비율이 필요하며, 아래의 코드가 사용된다. 다소 손볼곳이 많으나, 구조가 안정화 된 후에 정리하자
		if (localSynap.pxToPtRatio == undefined) {
			localSynap.pxToPtRatio = parseInt(localSynap.$pageAreas[0].css('width')) / localSynap.pageInfo.width;
			RATIO_NUMBERS.setStandardRatio(localSynap.pxToPtRatio);
		}
		if (localSynap.isImageMode()) {
			adjustRatio = RATIO_NUMBERS.getRatio();
		}

		// 텍스트XML 위치 resize
		$pageArea.find('span').each(function (idx, elem) {
			if (elem.getAttribute('class') == 'pageText') {
				obj = localSynap.searchMeta[index][idx];
				elem.style.top = (obj.t * adjustRatio) + "px";
				elem.style.left = (obj.l * adjustRatio) + "px";
				elem.style.width = (obj.w * adjustRatio) + "px";
				elem.style.height = (obj.h * adjustRatio) + "px";
				elem.style.fontSize = (obj.h * adjustRatio) + "px";
			}
		});
	}
	if (localSynap.properties.webAccessibility) {
		localSynap.prepareFullText(index);
		$page.attr('alt', localSynap.fullText[index]);
	}
}

var onLoadImg = function(index){
	if( BROWSER.isMobile() ){
		localSynap.resizePage(index);
	}else{
		if (++localSynap.loadedImgCount === localSynap.pageSize) {
			localSynap.resizePage(index);
		}
	}
	// 서버이미지변환기/PDF는 페이지별로 텍스트XML을 파싱하고, 처리한다.
	if (localSynap.properties.usePdfText) {
		if ( localSynap.isImageMode() ) {
			localSynap.parseTextXmlWithKey(index);
		} else {
			localSynap.parseTextXmlFromFile(localSynap.properties.fileName, index);
		}
	}
}

// SKIN READY FUNC
$(document).ready(function() {
	if (typeof localSynap.skinReadyFunc == "function") {
		localSynap.skinReadyFunc();
	}
});

