console.log( 'image_skin.js' );

var enableZoomEvent = true;
var zoomRatio = 1;

localSynap.getCurrentPage = function() {
	return 1;
}
localSynap.gotoHtmlPage = function(pageno) {

}

localSynap = (function() {

	var member = {
	
	}

	var image = {

		sheetViewFrame : null,

		skinReadyFunc: function() {
			
			var imageDir = $(localSynap.properties.xmlObj).find('path_image').text();
			var max_width = $(localSynap.properties.xmlObj).find('width').text() + 'px';
			$('#contents').attr('src', localSynap.getResultDir() + imageDir);
			
			if (!localSynap.isAllowCopy()) {
				$('#contents').get(0).oncontextmenu = function(){
					return false;
				}
			}
			
			if( BROWSER.isMobile() ){
				image.skinReadyMobileFunc();
			}else{
				image.skinReadyDesktopFunc();
			}
			containerSizeAdjust();
			setFullScreenClick();
			
			image.resize();
			window.onresize = image.resize;
			
			$(document).delegate('#documentScale', 'click', function(e){
				if (enableZoomEvent){
					enableZoomEvent = false;
					var btn_top = $('#documentScale').offset().top;  // bar 상대top
					var btn_height = $('#documentScale').height(); // bar Height
					var mouse_y = window.event.y - btn_top; // bar 영역내 클릭 위치
					var tab_range = parseInt(btn_height / 3);
					
					var bar3_top = btn_top;
					if (mouse_y<tab_range){
						zoomRatio = 2;
					} else if (mouse_y<(tab_range*2)) {
						zoomRatio = 1.5;
						bar3_top += parseInt(btn_height/2);
					}else{
						zoomRatio = 1;
						bar3_top += btn_height;
					}
					bar3_top = bar3_top - parseInt($('.bar3').height()/2);
					$('.state').text(zoomRatio+'X');
	
					$('.bar3').offset({top:bar3_top});
					enableZoomEvent = true;
					
					image.resize();
				}
			});
			window.onscroll = function(e){
				localSynap.onScroll && localSynap.onScroll(e);
			}
			localSynap.onLoadedBody && localSynap.onLoadedBody();
		},

		skinReadyDesktopFunc: function() {
			$('#container').attr('style', 'text-align:center; overflow:auto;');
			$('#innerWrap').attr('style', 'display:inline-block; vertical-align:middle; height:100%; left: 5%; right: 5%;');
			docKeyboardControl();
            if (localSynap.properties.isRenderServer == true) {
                setDownloadButton(localSynap.downloadUrl);
            } else {
                setDownloadButton(localSynap.properties.xmlObj);
            }
			setPrintButton(localSynap.properties.isRenderServer);
		},

		skinReadyMobileFunc: function() {
        	initMobile();
			$('#container').attr('style', "text-align:center; -webkit-touch-scroll: touch");
			$('#innerWrap').attr('style', "display:inline-block; vertical-align:middle; height:100%;");
			$("#contents").attr('style', " margin:auto;  position: absolute; top:0; left:0; bottom: 0; right:0; width:90%;" );
		},

		resize: function(){
			setResizeHeaderTitle();
			var originalWidth = $(localSynap.properties.xmlObj).find('width').text();
			var originalHeight = $(localSynap.properties.xmlObj).find('height').text();
			var ww = $('#container').width() * 0.9;
			var wh = $('#container').height() * 0.9;
			
			var fixWidth = originalWidth < ww ? originalWidth : ww;
			var fixHeight = originalHeight < wh ? originalHeight : wh;
			if( ww / wh < originalWidth / originalHeight ){		// 800 / 600 > 1000 / 600
				$('#innerWrap').css('width', fixWidth * zoomRatio + 5).css('min-height', Math.ceil(originalHeight * (fixWidth / originalWidth) * zoomRatio)).css('min-width', '');
				$('#contents').css('width', fixWidth * zoomRatio).css('height', '');
			}
			else{
				$('#innerWrap').css('min-height', fixHeight * zoomRatio).css('min-width', Math.ceil(originalWidth * (fixHeight / originalHeight)) * zoomRatio + 5);
				$('#contents').css('height', fixHeight * zoomRatio).css('width', '');
			}
		}
	}
	return $.extend(localSynap, image);
})();

// SKIN READY FUNC
$(document).ready(function() {
	if (typeof localSynap.skinReadyFunc == "function") {
		localSynap.skinReadyFunc();
	}
	else{
		alert('Error!');
	}
});

