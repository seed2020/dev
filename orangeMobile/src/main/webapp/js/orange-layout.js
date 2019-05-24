
var $layout = {
	adjust:function(){
		this.adjustNav();
		this.adjustBtn();
		this.adjustTab();
		if(window.adjustPaging) adjustPaging();
	},
	adjustNav:function(){
		var nav = $('#navArea .nav');
		if(nav.length==0) return;
		var sumW = 10, ul = nav.find('ul'), areaW = nav.width();
		ul.find('li').each(function(idx){
			sumW += $(this).outerWidth();
		});
		if(sumW<areaW){
			ul.width(areaW+'px');
		} else {
			ul.width(sumW+'px');
			nav.css('overflow-x','scroll');
		}
		
		var handler = $layout.scrollNav;
		if(sumW > areaW){
			if(ul.find('li:last').attr('class')=='navtit_on'){
				nav[0].scrollLeft = nav[0].scrollWidth;
			} else {
				var on = ul.find('.navtit_on');
				var onR = on.position().left + on.width() + 30;
				var scrL = onR - areaW;
				if(scrL>0){
					nav[0].scrollLeft = scrL;
				}
			}
		} else {
			handler = null;
		}
		this.scrollNav();
		nav.scroll(handler);
		
		if($m.browser.lg){
			$layout.touch.initScrollX(nav[0]);
		}
	},
	scrollNav:function(){
		var navArea = $('#navArea');
		var scrL = navArea.find('.nav').scrollLeft();
		var toLeft = navArea.find('#toLeft');
		if(scrL>0){
			if(!toLeft.hasClass('more-fadeIn')){
				toLeft.show().removeClass('more-fadeOut').addClass('more-fadeIn');
			}
		} else {
			if(toLeft.hasClass('more-fadeIn')){
				toLeft.removeClass('more-fadeIn').addClass('more-fadeOut');
			}
		}
		
		var nav = navArea.find('.nav');
		var toRight = navArea.find('#toRight');
		if(nav.scrollLeft() + nav.parent().width() < nav[0].scrollWidth){
			if(!toRight.hasClass('more-fadeIn')){
				toRight.show().removeClass('more-fadeOut').addClass('more-fadeIn');
			}
		} else {
			if(toRight.hasClass('more-fadeIn')){
				toRight.removeClass('more-fadeIn').addClass('more-fadeOut');
			}
		}
	},
	adjustBtn:function(){
		var btnArea = $('#btnArea'), area = btnArea.find('dl');
		if(btnArea.length==0) return;
		
		var btnLine = btnArea.find('.size:first dl'), btnLineIndex = 0, me = this;
		var btnLines = [], p = btnLine.parent();
		var areaW = btnArea.innerWidth() - (parseInt(p.css('margin-left')) + parseInt(p.css('margin-right')));
		var sum = 0, width, moved = false, more = area.find('#moreBtn');
		var dds = area.children("dd[id!='moreBtn']"), ddLast = dds.length - 1;
		
		if(dds.length==0){
			btnArea.hide();
			return;
		}
		
		btnArea.children('.size').show();
		more.show();
		var moreWidth = more.outerWidth() + parseInt(more.css('margin-left')) + parseInt(more.css('margin-right'));
		
		dds.each(function(index){
			width = $(this).outerWidth() + parseInt($(this).css('margin-left')) + parseInt($(this).css('margin-right'));
			if(!moved){
				sum += width;
				if((index<ddLast && sum+moreWidth>areaW) || (index==ddLast && sum>areaW)){
					moved = true;
					more.appendTo(btnLine);
					
					btnLineIndex++;
					btnLine = me._getNextBtnLn(btnArea, btnLineIndex);
					btnLines.push(btnLine);
					sum = width;
					$(this).appendTo(btnLine);
				} else {
					$(this).appendTo(btnLine);
				}
			} else {
				sum += width;
				if(sum<areaW){
					$(this).appendTo(btnLine);
				} else {
					btnLineIndex++;
					btnLine = me._getNextBtnLn(btnArea, btnLineIndex);
					btnLines.push(btnLine);
					$(this).appendTo(btnLine);
					sum = width;
				}
			}
		});
		
		btnLines.each(function(index, object){ $(object).parent().hide(); });
		more.attr('class', 'close');
		if(moved) more.show();
		else more.hide();
	},
	_getNextBtnLn:function(btnArea, idx){
		var btnLine = btnArea.find('.size:eq('+idx+') dl');
		if(btnLine.length!=0){
			return btnLine;
		}
		$('<div class="size"><dl></dl></div>').appendTo(btnArea);
		return btnArea.find('.size:eq('+idx+') dl');
	},
	toggleBtns:function(){
		var btnArea = $('#btnArea');
		var more = btnArea.find('#moreBtn'), showAll = more.attr('class') == 'close';
		btnArea.find('.size').each(function(index){
			if(index!=0){
				if(showAll){
					$(this).show();
				} else {
					$(this).hide();
				}
			}
		});
		if(showAll){
			more.attr('class', 'open');
		} else {
			more.attr('class', 'close');
		}
	},
	adjustTab:function(){
		var tabBtnArea = $('#tabBtnArea');
		if(tabBtnArea.length==0) return;
		
		var areaW = tabBtnArea.width(), tabSz = tabBtnArea.children('.tabsize'), dl = tabBtnArea.find('dl'), sumW = 3;
		dl.children('dd').each(function(){
			sumW += $(this).outerWidth();
		});
		dl.width((sumW<areaW ? areaW : sumW)+'px');
		
		var handler = $layout.scrollTab;
		if(sumW > areaW){
			if(dl.find('dd:last').attr('class')=='tab_on'){
				tabSz.scrollLeft(tabSz[0].scrollWidth);
			} else {
				var on = dl.find('.tab_on');
				var onR = on.position().left + on.width();
				var scrL = onR - areaW;
				if(scrL>0){
					tabSz.scrollLeft(scrL);
				}
			}
		} else {
			handler = null;
		}
		this.scrollTab();
		tabSz.scroll(handler);
	},
	scrollTab:function(){
		var tabBtnArea = $('#tabBtnArea'), tabSz = tabBtnArea.children('.tabsize');
		var scrL = tabSz.scrollLeft();
		var toLeft = tabBtnArea.find('#toLeft');
		if(scrL>0){
			if(!toLeft.hasClass('more-fadeIn')){
				toLeft.show().removeClass('more-fadeOut').addClass('more-fadeIn');
			}
		} else {
			if(toLeft.hasClass('more-fadeIn')){
				toLeft.removeClass('more-fadeIn').addClass('more-fadeOut');
			}
		}
		
		var toRight = tabBtnArea.find('#toRight');
		if(tabSz.scrollLeft() + tabSz.parent().width() < tabSz[0].scrollWidth){
			if(!toRight.hasClass('more-fadeIn')){
				toRight.show().removeClass('more-fadeOut').addClass('more-fadeIn');
			}
		} else {
			if(toRight.hasClass('more-fadeIn')){
				toRight.removeClass('more-fadeIn').addClass('more-fadeOut');
			}
		}
	},
	adjustBodyHtml: function(id){
		var bodyHtml = $('#'+id);
		if(bodyHtml.length==0) return;
		bodyHtml.css('padding-bottom', '20px');
		
		var tab = bodyHtml.find('table:first');
		if(tab.length==0){
			var p = bodyHtml.parent();
			var w = Math.floor((p.width()-15) * 0.8333);
			bodyHtml.children('div:first').width(w);
			bodyHtml.width(p.width()-10);
			p.css('overflow-x', 'auto');
		} else {
			var height = bodyHtml.height();
			bodyHtml.width('auto');
			if(height == bodyHtml.height()){
				bodyHtml.parent().css('overflow-x', 'auto');
				var sw = bodyHtml.parent()[0].scrollWidth;
				if(sw > bodyHtml.width()+20){
					bodyHtml.width(sw-10);
				}
				bodyHtml.parent().css('overflow-x', 'visible');
			} else {
				var checkWith = 1024;
				while(true){
					checkWith -= 32;
					bodyHtml.width(checkWith+'px');
					if(height != bodyHtml.height()){
						bodyHtml.width((checkWith+32)+'px');
						break;
					}
				}
			}
		}
		if($layout.touch.isTouchDevice() && $m.browser.iosVer != 0){
			$layout.touch.initScroll(bodyHtml.parent()[0]);
		}
	},
	tab:{
		tabBtnArea:null,
		tabViewArea:null,
		tabClsNm:'tab',
		init:function(btnArea, viewArea, tabCls){
			if(this.tabBtnArea == null){
				if(btnArea!=null && viewArea!=null){
					this.tabBtnArea = btnArea;
					this.tabViewArea = viewArea;
					this.tabClsNm = tabCls;
				} else {
					this.tabBtnArea = $('#tabBtnArea .tabsize dl');
					this.tabViewArea = $('#tabViewArea');
				}
			}
		},
		on:function(id){
			this.init();
			if(this.tabBtnArea.find('#'+id).attr('class')==this.tabClsNm){
				var onId = this.tabBtnArea.find('.'+this.tabClsNm+'_on').attr('class', this.tabClsNm).attr('id');
				this.tabViewArea.children('#'+onId).hide();
				
				this.tabBtnArea.find('#'+id).attr('class', this.tabClsNm+'_on');
				this.tabViewArea.children('#'+id).show();
			}
		}
	},
	touch:{
		posX:0,
		posY:0,
		posArr:[],
		scrollObjX:null,
		scrollObjY:null,
		isTouchDevice:function(){
			try{
				document.createEvent("TouchEvent");
				return true;
			}catch(e){
				return false;
			}
		},
		initScroll:function(obj){
			if($(obj).attr('data-has-touchevent')!=null){
				return;
			} else {
				$(obj).attr('data-has-touchevent', 'Y');
			}
			var $wrapscroll = $(".wrapscroll");
			this.scrollObjY = $wrapscroll.length > 0 ? $wrapscroll[0] : null;
			if(this.scrollObjY!=null && obj!=null){
				obj.addEventListener("touchstart", function(event) {
					var touch = $layout.touch;
					touch.endMoveStarted = false;
					
					touch.posArr=[];
					touch.scrollObjX = this;
					touch.posX = this.scrollLeft + event.touches[0].pageX;
					touch.posY =touch.scrollObjY.scrollTop + event.touches[0].pageY;
					//event.preventDefault();
				},false);
				obj.addEventListener("touchmove", function(event) {
					var touch = $layout.touch;
					this.scrollLeft = touch.posX - event.touches[0].pageX;
					touch.scrollObjY.scrollTop = touch.posY - event.touches[0].pageY;
					var posArr = touch.posArr;
					if(posArr.length<2){
						posArr.push([event.touches[0].pageX, event.touches[0].pageY]);
					} else {
						posArr[0] = posArr[1];
						posArr[1] = [event.touches[0].pageX, event.touches[0].pageY];
					}
					// iphone magnification - E78CEB : hanwha
					if($m.custCode!="E78CEB"){
						event.preventDefault();
					}
				},false);
				/*
				obj.addEventListener("touchend", function(event) {
					if($m.browser.iosVer != 0){
						var touch = $layout.touch;
						var posArr = touch.posArr;
						if(posArr.length>1){
							var x = posArr[1][0] - posArr[0][0];
							var y = posArr[1][1] - posArr[0][1];
							
							touch.endMoveStarted = true;
							var leftCnt = Math.round(Math.max(Math.abs(x),Math.abs(y)));
							window.setTimeout('$layout.touch.endMoveI('+x+','+y+',0,'+leftCnt+')', 20);
						}
					}
				},false);
				// */
			}
		},
		initScrollX:function(obj){
			if(this.isTouchDevice() && obj!=null){
				if($(obj).attr('data-has-touchevent')!=null){
					return;
				} else {
					$(obj).attr('data-has-touchevent', 'Y');
				}
				obj.addEventListener("touchstart", function(event) {
					var touch = $layout.touch;
					touch.endMoveStarted = false;
					
					touch.posArr=[];
					touch.scrollObjX = this;
					touch.posX = this.scrollLeft + event.touches[0].pageX;
					//event.preventDefault();
				},false);
				obj.addEventListener("touchmove", function(event) {
					var touch = $layout.touch;
					this.scrollLeft = touch.posX - event.touches[0].pageX;
					var posArr = touch.posArr;
					if(posArr.length<2){
						posArr.push([event.touches[0].pageX, event.touches[0].pageY]);
					} else {
						posArr[0] = posArr[1];
						posArr[1] = [event.touches[0].pageX, event.touches[0].pageY];
					}
					//event.preventDefault();
				},false);
				/*
				obj.addEventListener("touchend", function(event) {
					var touch = $layout.touch;
					var posArr = touch.posArr;
					if(posArr.length>1){
						var x = posArr[1][0] - posArr[0][0];
						var y = 0;//posArr[1][1] - posArr[0][1];
						touch.endMoveStarted = true;
						var leftCnt = Math.round(Math.max(Math.abs(x),Math.abs(y)));
						window.setTimeout('$layout.touch.endMoveI('+x+','+y+',0,'+leftCnt+')', 20);
					}
				},false);
				// */
			}
		},
		endMoveStarted:false,
		endMoveI:function(x, y, index, leftCnt){
			if(!this.endMoveStarted) return;
			
			var timeGap = 20;
			var recursive = 30;
			var slowMoveWhen = 8;
			var leftTimes = leftCnt-index;
			
			var objX = this.scrollObjX;
			var objY = this.scrollObjY;
			
			if(leftTimes >= recursive){
				var moveX = parseInt(objX.scrollLeft) - x;
				objX.scrollLeft = moveX>0 ? moveX : 0;
				if(objY!=null && y!=0){
					var moveY = parseInt(objY.scrollTop)  - y;
					objY.scrollTop  = moveY>0 ? moveY : 0;
				}
				index++;
				window.setTimeout('$layout.touch.endMoveI('+x+','+y+','+index+','+leftCnt+')', timeGap);
				return;
			}
			
			var signX = x<0 ? -1 : 1;
			var signY = y<0 ? -1 : 1;
			var absX = x * signX;
			var absY = y * signY;
			var isXBigger = absX > absY;
			
			var maxVa = Math.max(absX, absY);
			var minVa = Math.min(absX, absY);
			var ratio = (1 - Math.cos(leftTimes/recursive))*2.2;
			
			var moveMax = Math.round(maxVa * ratio);
			var moveMin = Math.round(minVa * ratio);
			
			var moveX = ( isXBigger ? moveMax : moveMin) * signX;
			var moveY = (!isXBigger ? moveMax : moveMin) * signY;
			
			var posMoveX = parseInt(objX.scrollLeft) - moveX;
			objX.scrollLeft = posMoveX>0 ? posMoveX : 0;
			if(objY!=null && y!=0){
				var posMoveY = parseInt(objY.scrollTop)  - moveY;
				objY.scrollTop  = posMoveY>0 ? posMoveY : 0;
			}
			
			index++;
			if(index<leftCnt && moveMax>0){
				if(leftTimes <= slowMoveWhen ){
					timeGap = 20 + ((slowMoveWhen-leftTimes)*5);
				}
				window.setTimeout('$layout.touch.endMoveI('+x+','+y+','+index+','+leftCnt+')', timeGap);
			} else {
				this.endMoveStarted = false;
			}
		}
	}
};

$space = {
	hasSpace:true,
	spaceObj:null,
	scrollObj:null,
	spaceHideHold:false,
	on:function(obj, relative, absolute, areaId){
		if(!$m.browser.mobile || obj==null) return;
		if($m.browser.safari) return;
		this.scrollSpace(true);
		
		var scroll = areaId==null ? $('.wrapscroll:first') : $('#'+areaId);
		if(absolute==null){
			var $obj = $(obj), $p = $obj.parentClass('etr_input');
			scroll.scrollTop(($p || $obj).position().top + scroll.scrollTop() - relative);
		} else {
			scroll.scrollTop(absolute);
		}
	},
	off:function(){
		if(!$m.browser.mobile || !this.hasSpace) return;
		this.spaceHideHold = false;
		window.setTimeout('$space.doOff()', 200);
	},
	doOff:function(){
		if(!this.spaceHideHold){
			this.spaceObj.hide();
		}
	},
	scrollSpace:function(on){
		if(this.hasSpace){
			if(this.spaceObj == null){
				this.spaceObj = $('#scrollSpace');
				if(this.spaceObj.length==0){
					this.hasSpace = false;
					this.spaceObj = null;
				}
				if(this.spaceObj != null){
					this.spaceHideHold = true;
					if($m.nav.screenSize().h<=640){
						this.spaceObj.height('350');
					} else {
						this.spaceObj.height('450');
					}
					this.spaceObj.show();
				}
			} else {
				this.spaceHideHold = true;
				this.spaceObj.show();
			}
		}
	},
	apply:function(obj, param){
		if(!$m.browser.mobile) return;
		var $obj = $(obj), name = $obj.attr('name');
		var data = param[name]==null ? param : param[name];
		if(data.relative) $obj.attr('data-sp-relative', data.relative);
		if(data.absolute) $obj.attr('data-sp-absolute', data.absolute);
		$obj.focus(function(){
			var rt = $(this).attr('data-sp-relative');
			var at = $(this).attr('data-sp-absolute');
			if(rt!=null) rt = parseInt(rt);
			if(at!=null) at = parseInt(at);
			$space.on(this, rt, at);
		}).blur(function(){
			$space.off();
		});
	},
	placeFooter:function(which){
		if(which=='list'){
			var sectionTop = $('section:first').position().top;
			var footerHeight = $('#footer').height()+30, screenHeight = $m.nav.screenSize().h;
			var gap = screenHeight - sectionTop - footerHeight;
			$('#footerSpace').height(gap).show();
		} else if(which=='tabview'){
			var tabview = $("#tabViewArea");
			var tabviewTop = tabview.position().top;
			var footerHeight = $('#footer').height()+30;
			var screenHeight = $m.nav.screenSize().h;
			var minHeight = screenHeight - tabviewTop - footerHeight;
			tabview.children().css('min-height', minHeight);
		}
	}
};

function reload(href){
	if(href == null) href = location.href;
	if(!href.startsWith('/cm')) $m.secu.set();
	location.replace(href==null ? location.href : href);
}
