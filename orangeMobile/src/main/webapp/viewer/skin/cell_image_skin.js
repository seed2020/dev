// slide_skin.js
console.log( 'cell_image_skin.js' );

/*
이미지변환기 변수 리스트
 localSynap.jobId
 localSynap.properties.fileExtention
 localSynap.properties.postJobUri
 member.convertServerUri
*/
// TODO: 이미지변환서버 주소 설정
// ex) var convertServerAddr = "http://210.126.1.38:44090/test";
var convertServerAddr = "";
var defaultDPI = "M";

var enableScrollEvent = true;
var enableZoomEvent = true;

var enableMockDebug = false;

$.extend(localSynap, (function() {
	var member = {
		sheetViewFrame: undefined,
		refreshTimer : null,
		convertServerUri : localSynap.properties.contextPath,
		cellPieceWidth : 500,   // 셀의 부분 조각으로 렌더링시 기본 너비(데스크탑과 모바일의 기본값은 다르다.)
		cellPieceHeight : 500,  // 셀의 부분 조각으로 렌더링시 기본 높이

        parseCellImgData : function(key, dpi) {
            dpi = (typeof dpi !== "undefined") ? dpi : defaultDPI;
			var pageIdx = localSynap.curPage - 1;
			member.getImageSize(key, pageIdx, dpi);

			obj = localSynap.objList[pageIdx];
			// 전체 페이지를 자른상태에서의 index 범위를 계산합니다.
			colSize = Math.ceil(obj.w/member.cellPieceWidth);
			rowSize = Math.ceil(obj.h/member.cellPieceHeight);
			lastColWidth = obj.w%member.cellPieceWidth;
			lastRowHeight = obj.h%member.cellPieceHeight;

			cell.countColumn = colSize;
			cell.countRow = rowSize;
			cell.lastColWidth = lastColWidth;
			cell.lastRowHeight = lastRowHeight;

			// 필요한 CSS를 미리 만들어 놓는다.
			member.createCellCSSElement(obj.w, member.cellPieceWidth, lastColWidth, member.cellPieceHeight, lastRowHeight);

			for (var y=0; y<rowSize; y++) {
				var currHeight = member.cellPieceHeight;
				if(y==rowSize-1) {
					currHeight = lastRowHeight;
				}
				$('#sheet').append( member.createCellRowElement(obj.w, member.cellPieceWidth, lastColWidth, currHeight, colSize, y, 0) );
			}
		},

		redrawSheet : function(key, dpi) {
            dpi = (typeof dpi !== "undefined") ? dpi : defaultDPI;
        },
        
		isDynamicLoading: function(){
			if( cell.INIT_IMAGE_LIMIT == 99999 || localSynap.pageSize <cell.INIT_IMAGE_LIMIT) {
				return false;
			}else{
				return true;
			}
		},

		getImagePath: function(pageNum){
            if (localSynap.jobId != undefined) {
                return member.convertServerUri + localSynap.objList[pageNum].path + '?dpi=M';
            } else {
                return localSynap.getResultDir()+encodeURI(localSynap.objList[pageNum].path);
            }
		},

        getImageSize : function (key, pageIdx, dpi) {
			pageIdx = (typeof pageIdx !== "undefined") ? pageIdx : 0;
            dpi = (typeof dpi !== "undefined") ? dpi : defaultDPI;
            $.ajax({
                type: "GET",
                url: (member.convertServerUri + '/dimension/' + key + '/' + pageIdx),
                async: false,
                dataType: "json",
                error: function(data){
                    //alert('error')
                },
                complete : function(data){
                    //alert('complate')
                },
                success:function(data) {
                    // TODO: 변환서버의 형식이 정해지면 맞춰주어야 함.
                    // Debug
					$.each(data, function (idx, elem) {
						if (typeof localSynap.objList[elem.p] == "undefined") {
							obj = {
								index: elem.p, // sheet index
								path: '/thumbnail/'+key+'/'+ elem.p,
								w: elem.w,
								h: elem.h
							};
							localSynap.objList[elem.p] = obj;
						}
					});
                }
			});
            //return obj;
        },
		
		drawCell: function() {
			if (enableScrollEvent) {
				var pageChangeBaseHeight = member.sheetViewFrame.height()/2;

				var startX = $(document).scrollLeft();
				var startY = $(document).scrollTop();
				var endX = $(window).width() + startX;
				var endY = $(window).height() + startY;
				member.displayCellImage(startX, startY, endX, endY);
			}
			enableScrollEvent = true;
		},

		displayCellImage: function(startX, startY, endX, endY) {
			var startCol = Math.ceil(startX/member.cellPieceWidth);
			var startRow = Math.ceil(startY/member.cellPieceHeight);
			var endCol = Math.ceil(endX/member.cellPieceWidth);
			var endRow = Math.ceil(endY/member.cellPieceHeight);

			if(endCol >= cell.countColumn) {
				endCol = cell.countColumn - 1;
			}
			if(endRow >= cell.countRow) {
				endRow = cell.countRow - 1;
			}
			
			for(var row=startRow; row<=endRow; row++) {
				for(var col=startCol; col<=endCol; col++) {
					var width = member.cellPieceWidth;
					var height = member.cellPieceHeight;
					
					if(row==(cell.countRow-1)) {
						height = cell.lastRowHeight;
					}

					if(col==(cell.countColumn-1)) {
						width = cell.lastColWidth;
					}

					member.setCellImage(row, col, width, height, cell.countColumn);
				}
			}

		},

		setCellImage: function(row, col, width, height, colSize) {
			if($('#piece' + row + '_'+ col).attr("src")=="") {
				// 실제 서버의 경우
				var imgpath = member.convertServerUri + '/thumbnail/' + localSynap.jobId + '/' + (localSynap.curPage-1) + '?dpi=' + defaultDPI + '&x=' + (col*member.cellPieceWidth+1) + '&y=' + (row*member.cellPieceHeight+1)+ '&w=' + width + '&h=' + height;
				// mock 서버의 경우
				if(enableMockDebug) {
					imgpath = member.convertServerUri + '/thumbnail/' + localSynap.jobId + '/' + (localSynap.curPage-1) + '/' + row + '_' + col;
				}
				$('#piece' + row + '_'+ col).attr("src", imgpath);
			}
		},

		createCellImgElement: function (row, col, width, height, colSize) {
			// Element만 만든다. 실제 그리는 것은 drawCell에서 한다.
			var html = '<img id="piece' + row + '_' + col +'" name="piece' + row + '_' + col + '" style="unselectable:on;width:' + width + 'px;height:'+ height + 'px;" src="" unselectable="on" onerror="image_error(this,' + row + ',' + col + ')" onload="onLoadImg('+ row + ',' + col + ',' + width + ',' + height +',' + colSize + ')" title="cell'+ row + '_' + col +'" alt="cell'+ row + '_' + col +'" title="cell'+ row + '_' + col +'"/>';

			return html;
		},

		createCellCSSElement: function (totalWidth, itemWidth, lastItemWidth, height, lastItemHeight) {
			var cellDivArea = $("#cellCommonDiv");
			cellDivArea.css('width', totalWidth);
			cellDivArea.css('height', height);
			cellDivArea.css('clear', 'both');
			cellDivArea.css('overflow', 'hidden');
			cellDivArea.css('background-color', '#FF0000');
		},

		// 한줄단위 이미지를 감쌀 Div를 생성
		createCellRowElement: function (totalWidth, itemWidth, lastItemWidth, height, colSize, rowIdx, colIdx) {
			var imgpath = '';

			var html = '<div class="CellCommonDiv"';
			html+= ' style="width:' + totalWidth + 'px;height:' + height + 'px;clear:both; overflow:hidden;"';
			html+= '>';
			for (var i=0; i<colSize; i++) {
				var currWidth = itemWidth;
				if(i==colSize-1) {
					currWidth = lastItemWidth;
				}
				html+= '<div id="cellpage' + rowIdx +'_' + i + '" name="cellpage'+rowIdx +'_' + i +'" style="float:left; width:' + currWidth + 'px;height:' + height + 'px;">';
				html+= member.createCellImgElement(rowIdx, i, currWidth, height, colSize);
				html+= '</div>';
			}
			html+= '</div>';

			return html;
		},

		initEvent: function() {
			if ((typeof member.sheetViewFrame)!=="undefined"){
				if( BROWSER.PC.isIE() && BROWSER.VERSION.IE() <= 9){
					$(window).scroll(function(e) {
						member.drawCell();
					});
				}else{
					$(document).scroll(function(e) {
						member.drawCell();
					});
				}
			}
		}
	};

	var cell = {
		FILENAME: encodeURI(localSynap.getFileName()),
		ratio: 1,
		scrollTopValue: 0,
		loadedImgCount: 0,
		objList: [],
		INIT_IMAGE_LIMIT: 10, // 동적로딩 갯수. 99999로 설정하면 동적로딩을 하지 않는다.
		INIT_MAKE_LIMIT: 10, // 초기화시 로딩 이미지 갯수
		searchMeta: {},
		countColumn: 0,
		countRow: 0,
		lastColWidth: 0,
		lastRowHeight: 0,
		// 셀 이미지 위에 생성할 텍스트를 가져온다.
		parseTextXmlWithKey: function (rowIdx, colIdx, width, height, pageIdx) {
			key = localSynap.jobId;
			var xmlUrl = member.convertServerUri +'/thumbnailxml/'+key+'/'+(localSynap.curPage-1)+'?dpi='+defaultDPI+'&x=' + (colIdx*member.cellPieceWidth+1) + '&y=' + (rowIdx*member.cellPieceHeight+1)+ '&w=' + width + '&h=' + height;

			// mock 서버의 경우
			if(enableMockDebug) {
				xmlUrl = member.convertServerUri +'/thumbnailxml/'+key+'/'+ (localSynap.curPage-1)+'/' + rowIdx + '_' + colIdx;
			}
			localSynap.searchMeta[pageIdx] = pdfSearch.loadTextXml(xmlUrl, colIdx*member.cellPieceWidth+1, rowIdx*member.cellPieceHeight+1, colIdx*member.cellPieceWidth+1+width, rowIdx*member.cellPieceHeight+1+height, true);
		},
		
		// 실제 텍스트를 렌더링한다.
		getText: function (pageNum) {
			return pdfSearch.getData(pageNum, localSynap.searchMeta, 8, 8); // 8 is mean : iframe margin 8pixel
		},
		
		getImagePath: function (pageIdx) {
			return member.getImagePath(pageIdx);
		},
		
		resize: function() {
			if (BROWSER.isMobile()){
				localSynap.resizeMobile();
			}else{
				localSynap.resizeDesktop();
			}
		},
		
		resizeDesktop: function() {

		},

		resizeMobile: function() {
			var pageElem = $('#page');
			var originalWidth = localSynap.objList[localSynap.curPage - 1].w;
			var originalHeight = localSynap.objList[localSynap.curPage - 1].h;
			var slideRatio = originalHeight / originalWidth;
			var deviceWidth = $(window).width();
			deviceWidth -= 20;
			pageElem.width(deviceWidth);
			pageElem.height(deviceWidth * slideRatio);
		},

		skinReadyFunc: function() {
			window.onresize = localSynap.resize;

			if( BROWSER.isMobile() ){
				cell.skinReadyMobileFunc();
			}else{
				cell.skinReadyDesktopFunc();
			}

			if (typeof localSynap.updateAfterMove == "function") {
				localSynap.updateAfterMove();
			}
		},

		skinReadyDesktopFunc: function() {
			member.sheetViewFrame = $("#sheet");
			member.initEvent();
			member.parseCellImgData(localSynap.jobId);
			member.redrawSheet(localSynap.jobId);
			member.drawCell();
            setDownloadButton(localSynap.downloadUrl);
		},

		skinReadyMobileFunc: function() {
			member.sheetViewFrame = $("#sheet");
			member.initEvent();
			member.parseCellImgData(localSynap.jobId);
			var loadingCnt = localSynap.pageSize;
			if(member.isDynamicLoading() && localSynap.pageSize>cell.INIT_MAKE_LIMIT){
				loadingCnt = cell.INIT_MAKE_LIMIT;
			}
			member.drawCell();
		},

		image_retry: function(obj, row, col){
			var width = member.cellPieceWidth;
			var height = member.cellPieceHeight;
			
			if(row==(cell.countRow-1)) {
				height = cell.lastRowHeight;
			}

			if(col==(cell.countColumn-1)) {
				width = cell.lastColWidth;
			}
			member.setCellImage(row, col, width, height, cell.countColumn);
			$(obj).attr('onclick', '');
			$(obj).attr('alt', 'cell'+(row*1+1));
			$(obj).attr('title', 'cell'+(row*1+1));
		}
	}
	return cell;
})());

function image_error(obj, row,col)
{
	// TODO: 에러 이미지를 넣거나 색을 지정해줍시다.
	if (obj.src!=''){
		var str_script = 'localSynap.image_retry(this,'+row+','+col+')';
		$(obj).attr('onclick', str_script);
		$(obj).attr('alt', 'Please, Click here!');
		$(obj).attr('title', 'Please, Click here!');
	}
}

var onLoadImg = function(row, col, width, height, colSize){
	// 서버이미지변환기/PDF는 페이지별로 텍스트XML을 파싱하고, 처리한다.
	var index = row*colSize + col;
	if (typeof localSynap.jobId !== "undefined") {
		localSynap.parseTextXmlWithKey(row, col, width, height, index);
	}

	var pageArea = $("#cellpage" + row + "_" + col);
	pageArea.append(localSynap.getText(index));
}

// SKIN READY FUNC
$(document).ready(function() {
	if (typeof localSynap.skinReadyFunc == "function") {
		localSynap.skinReadyFunc();
	}
});

