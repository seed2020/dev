
var $m = {
	custCode:null,
	browser:{
		chrome:false, custChrome:false, safari:false, firefox:false, opera:false, 
		ie:false, ie11:false, 
		naver:false, daum:false, 
		samsung:false, lg:false, lgChrome:false, unknown:false, 
		mobile:false,
		iosVer:0, iphoneShortcut:false,
		androidVer:0,
		tabletList:"SHW".split(','),
		mobile:false,
		tablet:false,
		isTouch:true,
		init:function(){
				var uagnt = navigator.userAgent;
				if(uagnt.indexOf('Opera')>=0 || uagnt.indexOf('OPR/')>=0){
					this.opera = true;
				} else if((uagnt.indexOf('iPhone') >=0 || uagnt.indexOf('iPad') >=0 || uagnt.indexOf('iPod') >=0) && uagnt.indexOf('AppleWebKit') >=0 && uagnt.indexOf('CriOS')<0){
					this.safari = true;
					var p = uagnt.indexOf(' OS ');
					var q = uagnt.indexOf(' ',p+4);
					if(p>0 && q>p){
						var s = uagnt.substring(p+4, q).trim().replace(/\_/g, '.');
						p = s.indexOf('.');
						q = s.indexOf('.',p+1);
						this.iosVer = q>0 ? parseFloat(s.substring(0,q)) : parseFloat(s);
					} else {
						p = uagnt.indexOf('Version/');
						q = uagnt.indexOf(' ',p+9);
						if(p>0 && q>p){
							this.iosVer = parseFloat(uagnt.substring(p+8, q).trim());
						}
					}
					p = uagnt.indexOf("Mobile/");
					q = uagnt.length - p;
					if(p>0 && q<16){
						this.iphoneShortcut = true;
					}
				} else if(uagnt.indexOf('Safari') >=0){
					if(uagnt.indexOf('LG')>=0){
						this.lg = true;
					} else if(uagnt.indexOf('SAMSUNG')>=0 || uagnt.indexOf('SHW')>=0){
						this.samsung = true;
					}
					
					if(uagnt.indexOf('Chrome')>=0 || uagnt.indexOf('Android')>0){
						var lower = uagnt.toLowerCase();
						if(lower.indexOf('naver')>=0){
							this.naver = true;
							this.custChrome = true;
						} else if(lower.indexOf('daum')>=0){
							this.daum = true;
							this.custChrome = true;
						} else if(uagnt.indexOf('SAMSUNG')>=0 || uagnt.indexOf('SHW')>=0){
							this.samsung = true;
							this.custChrome = true;
						} else if(this.lg){
							if(uagnt.indexOf(' U;')>0){
								//this.lg = true;
							} else {
								this.lgChrome = true;
							}
							this.custChrome = true;
						} else if(uagnt.indexOf('HUAWEI')>=0 || uagnt.indexOf('HTC')>=0 || uagnt.indexOf('ASUS')>=0 || uagnt.indexOf('Dell')>=0
									|| uagnt.indexOf('BlackBerry')>=0 || uagnt.indexOf('Nokia')>=0|| uagnt.indexOf('Fennec')>=0|| uagnt.indexOf('Haier')>=0){
							this.custChrome = true;
						} else {
							var p, q, s;
							p = uagnt.indexOf('Android');
							if(p>0){
								p = uagnt.indexOf(';',p);
								q = uagnt.indexOf('Build');
								s = uagnt.substring(p+1, q).trim();
								if(s.indexOf(';')>0 || s.indexOf(' ')>0){
									this.custChrome = true;
								}
							}
							if(!this.custChrome){
								p = uagnt.indexOf('Safari');
								s = uagnt.indexOf(' ', p);
								if(s>0){
									this.custChrome = true;
								}
							}
							if(!this.custChrome){
								this.chrome = true;
							}
						}
					} else if(uagnt.indexOf('CriOS')>=0){
						this.chrome = true;
					} else {
						this.safari = true;
						var p = uagnt.indexOf(' OS ');
						var q = uagnt.indexOf(' ',p+4);
						if(p>0 && q>p){
							var s = uagnt.substring(p+4, q).trim().replace(/\_/g, '.');
							p = s.indexOf('.');
							q = s.indexOf('.',p+1);
							this.iosVer = q>0 ? parseFloat(s.substring(0,q)) : parseFloat(s);
						} else {
							p = uagnt.indexOf('Version/');
							q = uagnt.indexOf(' ',p+9);
							if(p>0 && q>p){
								this.iosVer = parseFloat(uagnt.substring(p+8, q).trim());
							}
						}
					}
					
					var p = uagnt.indexOf('Android');
					var q = uagnt.indexOf(';', p+7);
					if(p>0 && q>p){
						var s = uagnt.substring(p+7, q).trim();
						p = s.indexOf('.');
						q = s.indexOf('.', p+1);
						this.androidVer = q>0 ? parseFloat(s.substring(0,q)) : parseFloat(s);
					}
					
				} else if(uagnt.indexOf('Firefox')>=0){
					this.firefox = true;
				} else if(uagnt.indexOf('Trident')>=0){
					this.ie = true;
					this.ie11 = true;
				} else if(uagnt.indexOf('MSIE')>=0){
					this.ie = true;
				} else {
					this.unknown = true;
				}
				$m.browser.mobile = (function(){if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) return true; return false;})();
				$m.browser.tablet = (function(){
					if(!$m.browser.mobile) return false;
					var i, arr = $m.browser.tabletList, ua = navigator.userAgent;
					for(i=0;i<arr.length;i++){
						if(ua.indexOf(arr[i])>0){
							return true;
						}
					}
					return false;
				})();
				$m.browser.isTouch = (function(){
					try{
						document.createEvent("TouchEvent");
						return true;
					}catch(e){
						return false;
					}
				})();
			}
		},
	nav:{
		cPath:null,
		index:0,
		hisInit:history.state && history.state.hisInit ? Math.min(history.state.hisInit, history.length) : history.length,
		hisCount:history.state && history.state.hisCount ? history.state.hisCount : 0,
		frameList:[], frameIdIndex:0,
		beforLogin:true,
		noInitPush:false,
		area:null,
		moveData:null,
		goUrl:null,
		direction:null,// 0:current, -1:back, 1:next, 2:forward, 9:exit,
		ani:{
			pageInIndex:null, pageIn:null, pageOut:null, effect:null, isInit:false, isReplace:false,
			hideOut:function(){
				if(this.pageOut!=null){
					$(this.pageOut).css('left',- ($m.nav.screenSize().w * 2));
				}
			},
			menuReplace:false
		},
		
		prepare:function(frmIndex, url, effect){
			if(this.area==null) this.area = $('#orangeMobile');
			if(url!=null) url = this.toNoCacheUrl(url);
			
			if(frmIndex<0){
				alert('$m.nav error !');
				this.ani.pageOut = null;
				this.ani.pageIn = this.frameList[0];
				this.ani.effect = effect;
				return;
			}
			
			if(!this.noInitPush){
				var curIdx = this.relativeIndex(0);
				if(effect=='next'){
					this.ani.pageOut = this.frameList[curIdx];
					this.ani.pageIn = null;
					this.ani.pageInIndex = frmIndex;
					this.ani.effect = effect;
					this.ani.isReplace = false;
				} else if(effect=='replace'){
					if(this.frameList.length>frmIndex+1){
						this.ani.pageOut = this.frameList[curIdx];
						this.ani.pageIn = this.frameList[frmIndex];
						this.ani.pageInIndex = null;
						this.ani.effect = 'next';
						this.ani.isReplace = true;
					} else {
						this.ani.pageOut = this.frameList[curIdx];
						this.ani.pageIn = null;
						this.ani.pageInIndex = frmIndex;
						this.ani.effect = 'next';
						this.ani.isReplace = true;
					}
				} else if(effect=='back'){
					this.ani.pageOut = this.frameList[curIdx];
					this.ani.pageIn = this.frameList[frmIndex];
					this.ani.effect = effect;
				} else if(effect=='current'){
					if(curIdx<0){
						this.ani.pageOut = null;
						this.ani.pageInIndex = 0;
						this.ani.pageIn = null;
						this.ani.effect = effect;
						this.ani.isInit = true;
					} else {
						this.ani.pageOut = null;
						this.ani.pageIn = null;
						this.ani.effect = effect;
					}
				}
			}
			if(url != null){
				if(!url.startsWith('/cm/')){ $m.secu.set(); }
				this.ani.menuReplace = false;
				if(this.frameList.length<frmIndex+1){
					this.addFrame(url);
				} else {
					if(effect=='replace' && $m.browser.androidVer>=5){
						this.ani.menuReplace = true;
					}
					this.frameList[frmIndex][0].contentWindow.location.replace(url);
				}
			}
		},
		addFrame:function(url){
			if(this.area==null) this.area = $('#orangeMobile');
			var scr = this.screenSize();
			var id = 'M'+this.frameIdIndex;
			this.frameIdIndex++;
			this.area.append('<iframe id="'+id+'" src="'+url+'" style="position:absolute; border:0px; margin:0px; width:'+scr.w+'px; height:'+scr.h+'px; top:0; left:'+(-scr.w)+'px; overflow:hidden; backface-visibility:hidden; transform:translate3d(0,0,0);"></iframe>');
			this.frameList.push(this.area.find('#'+id));
		},
		ready:function(win){
			
			var newFrameForIe = false;
			if(this.ani.pageIn == null && this.ani.pageInIndex != null){
				newFrameForIe = true;
				this.ani.pageIn = this.frameList[this.ani.pageInIndex];
				this.ani.pageInIndex = null;
			}
			
			if(this.moveData != null){
				if($m.browser.ie){
					history.go($m.nav.moveData.history - 1);
					window.setTimeout('$m.nav.onPopState();', 1);
				} else {
					//TODO androidVer 5
					if($m.nav.ani.menuReplace){
						$m.nav.pushHistory();
					}
					window.setTimeout('history.go($m.nav.moveData.history - 1);', 1);
				}
			} else if(this.ani.pageIn != null){
				if(this.ani.isInit){
					this.ani.isInit = false;
					
					var $in = $(this.ani.pageIn);
					$in.css('left','0px');
					this.index = 1;
					if(this.noInitPush){
						this.noInitPush = false;
						this.replaceHistory();
					} else {
						this.pushHistory();
					}
				} else if(this.ani.effect == 'current'){
					// do nothing here
				} else {
					
					var $in = $(this.ani.pageIn);
					var $out = $(this.ani.pageOut);
					
					if(this.ani.effect == 'next'){
						if($m.browser.ie && newFrameForIe){
							window.setTimeout('$m.nav.nextForIe()', 1);
						} else {
							$in.css('left','0px').attr('class', 'pg-moveFromRightFade');
							$out.css('left','0px').attr('class', 'pg-moveToLeftFade');
							
							if(history.pushState && this.direction == 'next'){
								this.index++;
								this.pushHistory();
								if(this.ani.isReplace){
									this.frameList.swap(this.index, this.index-1);
									this.ani.replaceMode = null;
								}
							}
						}
					} else if(this.ani.effect == 'back'){
						this.index--;
						$in.css('left','0px').attr('class', 'pg-moveFromLeftFade');
						$out.css('left','0px').attr('class', 'pg-moveToRightFade');
					}
					window.setTimeout('$m.nav.ani.hideOut()', 550);
				}
				if(this.moveData==null && !($m.browser.ie && newFrameForIe)){
					this.ani.pageIn = null;
				}
				if($m.browser.safari && this.ani.effect == 'back'){
					this.replaceHistory();
				}
			}
			if($m.nav.screenSizeHolder == null){
				$m.handler.onOrientationChange();
			}
			
			if($m.nav.index == $m.menu.menuIndex){
				if(win==null) win = $m.nav.getWin(0);
				var header = win.$('header:first');
				header.find('dd.back').hide();
				header.find('#space').show();
			}
		},
		noLaoutReady:function(win, noHisAdd){
			if(noHisAdd!='true') this.hisCount++;
			this.replaceHistory();
		},
		nextForIe:function(){
			this.index++;
			$($m.nav.ani.pageIn).css('left','0px');
			$($m.nav.ani.pageIn).attr('class', 'pg-moveFromRightFade');
			$($m.nav.ani.pageOut).attr('class', 'pg-moveToLeftFade');
			this.pushHistory();
			this.ani.pageIn = null;
		},
		onPopState:function(event){
			var state = history.state || (event.originalEvent!=null ? event.originalEvent.state : null);
			if(state==null){
				return;
			}
			if(window.sessionStorage && window.sessionStorage['HIS_FIRST'] == 'Y'){
				window.sessionStorage['HIS_FIRST'] = '';
				$m.nav.index = 0;
				$m.nav.hisCount = 0;
				$m.nav.replaceHistory();
				location.replace('/');
			} else if(window.sessionStorage && window.sessionStorage['SAFARI_EXIT'] == 'Y'){
				window.sessionStorage['SAFARI_EXIT'] = '';
				$m.nav.direction = 'current';
				if(this.hisCount<1){
					$m.nav.replaceHistory();
					$m.nav.index = 1;
					$m.nav.pushHistory();
				}
			} else if($m.nav.moveData != null){
				if($m.nav.moveData.direction == 'back'){
					$m.nav.index += $m.nav.moveData.history;
					$m.nav.hisCount += $m.nav.moveData.history;
					$m.nav.pushHistory();
					$m.nav.index++;
					$m.nav.moveData = null;
					$m.nav.ready();
				} else if($m.nav.moveData.direction == 'replace'){
					$m.nav.index--;
					$m.nav.hisCount--;
					$m.nav.moveData = null;
					$m.nav.replaceHistory();
					$m.nav.direction = 'next';
					$m.nav.ready();
				}
			} else if($m.nav.direction == 'backExit'){
				$m.nav.replaceHistory();
				window.setTimeout('history.back()', 1);
			} else if($m.nav.direction == 'exit'){
				$m.nav.direction = 'current';
				if($m.browser.ie){
					$($m.nav.frameList[0]).hide();
				} else {
					$m.nav.hisCount = 0;
					$m.nav.replaceHistory();
					if($m.browser.safari){
						window.sessionStorage['SAFARI_EXIT'] = 'Y';
					}
					window.setTimeout('history.back()', 1);
				}
			} else if(($m.nav.index == 1 && state.index == 0)) {
				$m.nav.index = 0;
				if($m.browser.ie){
					//window.setTimeout('history.back()', 1);
					$m.nav.index = 0;
					$m.nav.hisCount = 0;
					$m.nav.direction = 'backExit';
					window.setTimeout('history.back()', 1);
				} else if($m.browser.safari){
					$m.nav.direction = 'exit';
					var mv = $m.nav.hisCount - 1;
					if(mv < 1){
						mv = 1;
						$m.nav.replaceHistory();
						window.sessionStorage['SAFARI_EXIT'] = 'Y';
					}
					$m.nav.hisCount = 0;
					window.setTimeout('history.go(-'+mv+')', 1);
				} else {
					$m.nav.index = 0;
					$m.nav.hisCount = 0;
					$m.nav.direction = 'backExit';
					window.setTimeout('history.back()', 1);
				}
			} else if($m.nav.index == 0 && state.index != 0 && !$m.browser.ie) {
				//TODO - 임시 막기
				//var mv = 0 - state.index - 1;
				//window.setTimeout('history.go('+mv+')', 1);
			} else if($m.nav.index == 0 && state.index == 1) {
				$m.nav.direction = 'forward';
				$m.nav.index = 1;
			} else if($m.nav.index == state.index+1) {
				if($m.menu.on) $m.menu.close();
				$m.dialog.closeAll();
				
				$m.nav.hisCount--;
				$m.nav.direction = 'back';
				$m.nav.prepare($m.nav.absoluteIndex(state.index), null, 'back');
				$m.nav.ready();
			} else if($m.nav.index == state.index-1){
				$m.nav.hisCount++;
				$m.nav.direction = 'forward';
				$m.nav.prepare($m.nav.absoluteIndex(state.index), null, 'next');
				$m.nav.ready();
			} else {
			}
		},
		toFirst:function(addHis){
			if(window.sessionStorage){
				if($m.browser.ie){
					window.sessionStorage['NO_INIT_PUSH'] = 'Y';
					$m.nav.hisCount++;
					if(addHis) $m.nav.hisCount++;
					$m.nav.replaceHistory();
					location.reload('/');
				} else {
					this.pushHistory();
					var mv = this.hisInit - history.length;
					sessionStorage['HIS_FIRST'] = 'Y';
					history.go(mv);
				}
			}
		},
		screenSizeHolder:null,
		heightGap:0,
		getOrientation:function(){
			return !$m.browser.firefox ? window.orientation : screen.mozOrientation==null || screen.mozOrientation.indexOf('landscape')<0 ? 0 : 90;
		},
		screenSize:function(){
			if(!$m.browser.mobile){
				return {w:360,h:580};
			}
			
			return {w:document.documentElement.clientWidth, h:document.documentElement.clientHeight};
		},
		getInnerWinSize:function(or){
			var w = window.innerWidth;
			var h = window.innerHeight;
			if(!(or==90 || or==-90)){
				this.screenSizeHolder.vertical = {w:w, h:h};
				return this.screenSizeHolder.vertical;
			} else {
				this.screenSizeHolder.horizontal = {w:w, h:h};
				return this.screenSizeHolder.horizontal;
			}
		},
		getRelativeFrame:function(index){
			return this.frameList[this.relativeIndex(index)];
		},
		getAbsoluteFrame:function(index){
			return this.frameList[this.absoluteIndex(index)];
		},
		relativeIndex:function(index){
			return this.index - 1 + (index==null ? 0 : index);
		},
		absoluteIndex:function(index){
			return index - 1;
		},

		next:function(event, url){
			if(event!=null && event.preventDefault) event.preventDefault();
			if($m.menu.on) $m.menu.close();
			this.direction = 'next';
			this.prepare(this.relativeIndex(1), url, this.direction);
		},
		replaceCurr:function(event, url){
			if(event!=null && event.preventDefault) event.preventDefault();
			if($m.browser.ie){
				this.curr(null, url);
			} else if(url!=null){
				var index = this.absoluteIndex(this.index);
				this.moveData = { from:index, to:index+1, history:0, direction:'replace' };
				this.prepare(index+1, url, 'replace');
			}
		},
		prev:function(event, url){
			if(event!=null && event.preventDefault) event.preventDefault();
			if(this.relativeIndex(-1)<0){
				this.getWin(0).$('#navArea li.navtit_on').trigger('click');
			} else {
				this.direction = 'back';
				if(url==true){
					var win = this.getWin(-1);
					if(win != null) url = win.location.href;
					else url = null;
				}
				this.prepare(this.relativeIndex(-1), url, this.direction);
				if(url==null) this.ready();
			}
		},
		curr:function(event, url){
			if(event!=null && event.preventDefault) event.preventDefault();
			this.ani.pageIn = null;
			this.ani.pageInIndex = null;
			var win = this.getWin(0);
			if(win!=null) {
				if(!url.startsWith('/cm/')) $m.secu.set();
				win.location.replace(this.toNoCacheUrl(url));
			}
		},
		move:function(event, url, index){
			if(event!=null && event.preventDefault) event.preventDefault();
			if(url!=null){
				var to = this.absoluteIndex(index);
				var from = this.absoluteIndex(this.index);
				var his = to - from;
				this.moveData = { from:from, to:to, history:his, direction:'back' };
				this.prepare(to, url, 'back');
			}
		},
		toList:function(reloadable){
			var to = this.absoluteIndex($m.menu.menuIndex);
			var from = this.absoluteIndex(this.index);
			var his = to - from;
			var win = this.getWin(his);
			if(win!=null){
				var url = win.location.href;
				this.moveData = { from:from, to:to, history:his, direction:'back' };
				this.prepare(to, url, 'back');
			}
		},
		init:function(event, url, loginUrl){
			if(event!=null && event.preventDefault) event.preventDefault();
			if(url==null){
				$m.msgmap['btn-close'] = $m.msg.callMsg("mcm.btn.close");
				$m.msg.alertMsg("mpt.mobile.noSetup");
				return;
			}
			this.direction = 'next';
			if($m.secu.hasKey() && url.indexOf('?msgId=')<0 && url.indexOf('?secu=')<0){
				if(url.startsWith('/cm/login')){
					var initPg = sessionStorage['INIT_PAGE'];
					if(initPg!=null && initPg!=''){
						url = initPg;
					}
				} else {
					this.replaceHistory();
					sessionStorage['INIT_PAGE'] = url;
				}
			}
			if(!url.startsWith('/cm/')) $m.secu.set();
			this.prepare(this.index, url, 'current');
		},
		reload:function(url, checkWinResize){
			if(checkWinResize==true) $m.nav.screenSizeHolder = null;
			var win = this.getWin(0);
			if(win!=null) {
				if(url==null) url = win.location.href;
				if(!url.startsWith('/cm/')) $m.secu.set();
				win.location.replace(this.toNoCacheUrl(url));
			}
		},
		getWin:function(index){
			var frm = this.getRelativeFrame(index);
			return frm==null || frm.length==0 ? null : (frm[0].contentWindow || frm[0].contentDocument);
		},
		pushHistory:function(){
			this.hisCount++;
			history.pushState({index: this.index, hisInit:this.hisInit, hisCount:this.hisCount}, 'M'+this.index, null);
		},
		replaceHistory:function(){
			history.replaceState({index: this.index, hisInit:this.hisInit, hisCount:this.hisCount}, 'M'+this.index, null);
		},
		toNoCacheUrl:function(url){
			if(url != null && (($m.browser.chrome && !$m.browser.custChrome) || $m.browser.ie)){
				var i, p, arr = [], possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
				for(i=0;i<8;i++){
					arr.push(possible.charAt(Math.floor(Math.random() * possible.length)));
				}
				if((p = url.indexOf('&noCache='))>0){
					url = url.substring(0,p+9) + arr.join('');
				} else if((p = url.indexOf('?noCache='))>0){
					url = url.substring(0,p+9) + arr.join('');
				} else if(url.indexOf('?') > 0){
					url = url + '&noCache=' + arr.join('');
				} else {
					url = url + '?noCache=' + arr.join('');
				}
				return url;
			} else {
				return url;
			}
		},
		post:function(form){
			if(form==null || form.attr == null || form.length==0) return;
			form.attr("method", "post");
			form.attr('target','dataframe');
			var url = form.attr('action');
			var p = (url==null) ? -1 : url.indexOf('.do?');
			var q = p>0 ? url.lastIndexOf('/',p) : -1;
			var valid = false;
			if(p>0 && q>0){
				var pg = url.substring(q+1, p);
				if(pg.startsWith('trans') && pg.endsWith("Post")){
					valid = true;
				} else {
					$m.dialog.alert('[Invalid POST URL]\nURL must start with \'trans\' and end with \'Post.do\' !');
				}
			} else {
				$m.dialog.alert('[Invalid POST URL] - '+url);
			}
			if(valid){
				$m.secu.set();
				form.submit();
			}
		}
	},
	
	initOrange:function(){
		$m.browser.init();
		var body = $('body:first');
		if(body.hasClass('noInit')) return;
		var scr = $m.nav.screenSize();
		body.css('margin','0px').css('padding','0px').append('<div id="orangeMobile" style="position:absolute; overflow:hidden; width:'+scr.w+'px; height:'+scr.h+'px; z-index:1; perspective:1200px; transform-style:preserve-3d; background-color: #ffffff; background-color: #ffffff;"><div>');
		$(window).on({
			'orientationchange':$m.handler.onOrientationChange,
			'popstate':$m.handler.onPopState,
			'pageshow':$m.handler.onPageShow,
			'pagehide':$m.handler.onPageHide});
		
		$(window).resize(function () {
			$m.dialog.resizeAll();
			$m.nav.frameList.each(function(index, frm){
				if(frm != null){
					var win = frm[0].contentWindow || frm[0].contentDocument;
					if(win){
						if(win.$layout) win.$layout.adjust();
						if(win.onResize){
							win.onResize();
						}
					}
				}
			});
		});
		
		if($m.browser.firefox){
			var mqOrientation = window.matchMedia("(orientation: portrait)");
			mqOrientation.addListener($m.handler.onOrientationChange);
		}
		
		$m.nav.replaceHistory('initOrange');
		$m.menu.setSize(scr.w, scr.h);
		if($m.browser.mobile){
			window.scrollTo(0,1);
			window.setTimeout('$m.resizeWins()', 300);
		}
	},
	
	resizeWins:function(){
		var scr = $m.nav.screenSize();
		var area = $('#orangeMobile');
		if(scr.h != area.height()){
			area.height(scr.h);
			area.width(scr.w);
			$m.menu.setSize(scr.w, scr.h);
		}
	},
	
	handler:{
		onPageShow:function(event, data){
			if(window.sessionStorage){
				if($m.browser.ie){
					if(sessionStorage['NO_INIT_PUSH'] == 'Y'){
						sessionStorage['NO_INIT_PUSH'] = '';
						$m.nav.noInitPush = true;
						return;
					}
				} else if($m.browser.safari){
					var hisCnt = sessionStorage['HIS_CNT'];
					if(hisCnt!=null && hisCnt!=''){
						sessionStorage['HIS_CNT'] = '';
						$m.nav.hisCount = parseInt(hisCnt) + 1;
					}
				}
			}
		},
		onPageHide:function(event, data){
			if($m.nav.direction == 'backExit'){
				$m.nav.direction = null;
			}
			if(window.sessionStorage){
				if($m.browser.ie){
					if(history.state && history.state.index==0 && $m.nav.index==0){
						return;
					}

					if(history.state==null) $m.nav.replaceHistory();
					$m.nav.direction = 'exit';
					var mv = -$m.nav.index;
					$m.nav.index = 0;
					if(mv<0) history.go(mv);
				} else if($m.browser.safari){
					this.pushHistory();
					var mv = history.length - this.hisInit;
					sessionStorage['HIS_CNT'] = mv;
				}
			}/*
			if($m.browser.ie){
				if(history.state && history.state.index==0 && $m.nav.index==0){
					return;
				}
				if(history.state==null) $m.nav.replaceHistory();
//alert($m.nav.index+":"+(history.state ? history.state.index : null));
				$m.nav.direction = 'exit';
				//var mv = -$m.nav.index -1;
				var mv = -$m.nav.index;
				//var mv = $m.nav.index == 1 ? -1 : -1 -$m.nav.index;
				$m.nav.index = 0;
				if(mv<0) history.go(mv);
			} else if($m.browser.safari){
				if($m.nav.hisCount==0){
					sessionStorage['HIS_CNT'] = '';
				} else {
					sessionStorage['HIS_CNT'] = $m.nav.hisCount;
				}
			}*/
		},
		onOrientationChange:function(event){
			window.setTimeout("$m.handler.onOrientationChangeAction(null);", 300);
		},
		onOrientationChangeAction:function(event){
			if($m.menu.on && !$m.menu.afterLoginOn) $m.menu.close();
			var scr = $m.nav.screenSize();
			$m.nav.area.css('width',scr.w).css('height',scr.h);
			var left = - Math.max(scr.w, scr.h) * 2, frmIndex = $m.nav.relativeIndex(0);
			$m.nav.frameList.each(function(index, frm){
				if(frm != null){
					frm.css('width',scr.w).css('height',scr.h);
					if(frmIndex != index){
						frm.css('left', left).removeClass('pg-moveToRightFade').removeClass('pg-moveToLeftFade');
					}
					var win = frm[0].contentWindow || frm[0].contentDocument;
					if(win){
						if(win.$layout) win.$layout.adjust();
						if(win.onOrientationChange){
							win.onOrientationChange({orientation:$m.nav.getOrientation()});
						}
					}
				}
			});
			$m.dialog.resizeAll();
			$m.menu.setSize(scr.w, scr.h);
		},
		onPopState:function(event){
			$m.nav.onPopState(event);
		}
	},
	
	rsa:{
		getKey:function(e,m){
			return new RSAPublicKey(e, m);
		},
		encrypt:function(key, data){
			return encrypt(key, data);
		}
	},
	
	secu:{
		storage:window['localStorage'],
		session:window['sessionStorage'],
		supported:this.storage !== 'undefined',
		setKey:function(e,m,x){
			if(!this.supported){ return false; }
			if(e=='' || m=='' || x=='') return false;
			this.storage.setItem('secu-e', e);
			this.storage.setItem('secu-m', m);
			this.storage.setItem('secu-x', x);
			this.session.setItem('secu-x', x);
			return true;
		},
		clearKey:function(){
			if(!this.supported){ return false; }
			this.storage.setItem('secu-e', '');
			this.storage.setItem('secu-m', '');
			this.storage.setItem('secu-x', '');
			this.session.setItem('secu-x', '');
		},
		hasKey:function(){
			var x = this.session==null ? null : this.session.getItem('secu-x');
			return x != null && x != '';
		},
		set:function(){
			if(!this.supported){ return false; }
			var time, code = this.storage.getItem('secu-code');
			if(code !== null){
				if(this.storage.getItem('secu-time') !== null){
					var diff = new Date().getTime() - parseInt(this.storage.getItem('secu-time'));
					if(diff > 3000){
						code = null;
					}
				}
			}
			if(code === null){
				var x = this.storage.getItem('secu-x');
				if(x==null || x==''){
					$m.cookie.removeItem('secuCode');
				} else {
					var key = new RSAPublicKey(this.storage.getItem('secu-e'), this.storage.getItem('secu-m'));
					time = new Date().getTime()+'';
					code = encrypt(key, this.storage.getItem('secu-x')+':'+time);
					$m.cookie.setItem('secuCode', code, '/');
					this.storage.setItem('secu-time', time);
					this.storage.setItem('secu-code', code);
				}
			} else {
				$m.cookie.setItem('secuCode', code, '/');
			}
			return true;
		},
		remove:function(){
			$m.cookie.removeItem('secuCode', '/');
		}
	},
	
	cookie:{
		getItem:function (sKey) {
			return decodeURIComponent(document.cookie.replace(new RegExp('(?:(?:^|.*;)\\s*' + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, '\\$&') + '\\s*\\=\\s*([^;]*).*$)|^.*$'), '$1')) || null;
		},
		setItem:function (sKey, sValue, sPath, vEnd, sDomain, bSecure) {
			if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return false; }
			var sExpires = '';
			if (vEnd) {
				switch (vEnd.constructor) {
					case Number:
						sExpires = vEnd === Infinity ? '; expires=Fri, 31 Dec 9999 23:59:59 GMT' : '; max-age=' + vEnd;
						break;
					case String:
						sExpires = '; expires=' + vEnd;
						break;
					case Date:
						sExpires = '; expires=' + vEnd.toUTCString();
						break;
				}
			}
			document.cookie = encodeURIComponent(sKey) + '=' + encodeURIComponent(sValue) + sExpires + (sDomain ? '; domain=' + sDomain : '') + (sPath ? '; path=' + sPath : '') + (bSecure ? '; secure' : '');
			return true;
		},
		hasItem:function (sKey) {
			return (new RegExp('(?:^|;\\s*)' + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, '\\$&') + '\\s*\\=')).test(document.cookie);
	    },
		removeItem:function (sKey, sPath, sDomain) {
			if (!sKey || !this.hasItem(sKey)) { return false; }
			document.cookie = encodeURIComponent(sKey) + '=; expires=Thu, 01 Jan 1970 00:00:00 GMT' + ( sDomain ? '; domain=' + sDomain : '') + ( sPath ? '; path=' + sPath : '');
			return true;
		},
		keys:function () {
	        var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, '').split(/\s*(?:\=[^;]*)?;\s*/);
	        for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = decodeURIComponent(aKeys[nIdx]); }
	        return aKeys;
	    }
	},
	
	ajax:function(url, param, handler, extension){
		var sending = null;
		if(param==null || extension==null || extension.paramAsIs != true){
			sending = "data="+encodeURIComponent(JSON.stringify(param==null ? {} : param.toJSON==null ? param : param.toJSON()));
		} else {
			sending = (param.toJSON==null ? new ParamMap(param) : param).toQueryString();
		}
		
		if(extension==null || extension.secuSession != true){
			if(!url.startsWith('/cm/')) $m.secu.set();
		}
		$.ajax({
			url: $m.nav.cPath==null ? url : $m.nav.cPath + url,
			async:extension!=null && extension.async==true,
			cache:extension!=null && extension.cache==true,
			type:(extension!=null && extension.method!=null && extension.method.toLowerCase()=='post') ? 'POST' : 'GET',
			timeout: (extension!=null && extension.timeout!=null) ? extension.timeout : 10000,
			data:sending,
			success:function(strJson){
				if (typeof strJson == "object") {
					handler(strJson);
				} else if (typeof strJson == "string") {
					var isJson = strJson.startsWith('{"message":') && strJson.endsWith('}');
					var jsonObject = isJson ? JSON.parse(strJson) : null;
					if(jsonObject != null && jsonObject.message != ''){
						if($m.nav.beforLogin != true){
							$m.dialog.alert(jsonObject.message, function(){
								top.location.replace('/');
							});
						} else {
							if(extension!=null && extension.fail!=null){
								//var model = new ParamMap().readObject(jsonObject.model);
								//extension.fail(model.toObject(), false);
								extension.fail(jsonObject.model, false);
							} else if(jsonObject.logout=='true'){
								$m.dialog.alert(jsonObject.message, function(){
									top.location.replace('/');
								});
							} else {
								$m.dialog.alert(jsonObject.message);
							}
						}
					} else {
						if(extension!=null && extension.mode == 'HTML'){
							handler(strJson, true);
						} else {
							var jsonObject = JSON.parse(strJson);
							//var model = new ParamMap().readObject(jsonObject.model);
							//handler(model.toObject(), true);
							handler(jsonObject.model, true);
						}
					}
				}
			},
			error: function (xhr, ajaxOptions, thrownError) {
				if(extension!=null && extension.error!=null){
					extension.error(xhr, param, thrownError);
				} else {
					var errCd = xhr.status;
					if(errCd=='0'){
						alert($m.msgmap['msg-timeout']);
					} else {
						if(errCd=='400') errCd = '403';
						var msgCd = (errCd=='403' || errCd=='404') ? 'cm.msg.errors.'+errCd : 'cm.msg.errors.500';
						if(errCd=='403' || errCd=='404' || (param!=null && param.msgId!="cm.msg.errors.noMessage")){
							$m.msg.alertMsg(msgCd, [errCd, url]);
						}
					}
				}
			}
		});
	},
	
	dialog:{
		backgroud:null,
		index:-1,
		data:[],
		focused:null,
		holdCloseFlag:false,
		alert:function(msg, handler, isHtml){
			$m.dialog.index++;
			$m.dialog.data[$m.dialog.index] = (handler == null) ? null : {handler:handler};
			$m.dialog.openMessage({
				index:$m.dialog.index,
				msg:msg,
				isHtml:(isHtml==true),
				btns:[[$m.msgmap['btn-close'], '$m.dialog.closeByIndex('+$m.dialog.index+');']]
			});
		},
		confirm:function(msg, handler, isHtml){
			$m.dialog.index++;
			$m.dialog.data[$m.dialog.index] = (handler == null) ? null : {handler:handler};
			this.openMessage({
				index:$m.dialog.index,
				msg:msg,
				isHtml:(isHtml==true),
				btns:[[$m.msgmap['btn-ok'], '$m.dialog.closeByIndex('+$m.dialog.index+', true);']
					,[$m.msgmap['btn-cancel'], '$m.dialog.closeByIndex('+$m.dialog.index+', false);']]
			});
		},
		showBackground:function(show){
			if(this.backgroud == null){
				$('body:first').append('<div class="popbackground" id="popbackground" style="z-index:30;"></div>');
				this.backgroud = $('#popbackground');
			}
			if(show) this.backgroud.show();
			else this.backgroud.hide();
		},
		close:function(id){
			var dlgIdx = null;
			this.data.each(function(idx, obj){
				if(obj!=null && obj.id==id){
					dlgIdx = idx;
					return false;
				}
			});
			if(dlgIdx != null){
				$m.dialog.closeByIndex(dlgIdx);
			}
		},
		closeByIndex:function(idx, result, event, force){
			if(event!=null && event.preventDefault) event.preventDefault();
			if($m.dialog.holdCloseFlag) return;
			if($m.dialog.data[idx] != null && $m.dialog.data[idx].handler != null){
				if($m.dialog.data[idx].handler(result) == false && force != true){
					return;
				}
			}
			$('#POP_'+idx).remove();
			$('#BCK_'+idx).remove();
			if($m.dialog.data.length>idx){
				$m.dialog.data[idx] = null;
			}
			if(idx == $m.dialog.index) $m.dialog.index--;
		},
		openSelect:function(obj, handler){
			
			if(obj==null || obj.cdList==null || obj.cdList.length==null || obj.cdList.length==0){
				$m.dialog.alert('No data for select-pop !');
				return;
			}
			
			$m.dialog.index++;
			$m.dialog.data[$m.dialog.index] = { id:obj.id, handler:handler };
			obj['index'] = $m.dialog.index;
			
			var html = [];
			html.push('<div class="popbackground" id="BCK_'+obj.index+'" style="z-index:'+(50+obj.index)+'" onclick="$m.dialog.closeByIndex('+obj.index+');"></div>');
			
			html.push('<div class="popuparea_s2" id="POP_'+obj.index+'" style="z-index:'+(50+obj.index)+'">');
			html.push('	<div class="btn" onclick="$m.dialog.closeByIndex('+obj.index+', null, event);"></div>');
			html.push('	<div class="popup">');
			html.push('	<div class="inner">');
			
			
			html.push('	<div class="pop_radio" data-idx="'+obj.index+'">');
			var i, arr = obj.cdList, size = arr.length, on, hasOn=false;
			for(i=0;i<size;i++){
				if(i!=0) html.push('	<dl><dd class="line"></dd></dl>');
				on = arr[i].cd==obj.selected;
				if(on) hasOn = true;
				html.push('		<dl onclick="$m.dialog.clickSelect(this);" data-cd="'+arr[i].cd+'"><dd class="sradio'+(on ? '_on' : '')+'"><span>'+arr[i].nm+'</span></dd></dl>');
			}
			html.push('	</div>');
			
			
			html.push('	</div>');
			html.push('	</div>');
			html.push('</div>');
			var wrapper = $('.wrapper:first');
			if(wrapper.length==0) wrapper = $('body:first');
			wrapper.append(html.join('\n'));
			
			if(hasOn){
				var onObj = wrapper.children('#POP_'+obj.index).find('.sradio_on').parent();
				var pObj = onObj.parent().parent();
				pObj.scrollTop(onObj.position().top+pObj.scrollTop() - 128);
			}
		},
		clickSelect:function(obj){
			var area = $(obj).parentClass('pop_radio');
			area.find('dd.sradio_on').attr('class', 'sradio');
			var txt = $(obj).find('dd').attr('class', 'sradio_on').text();
			var idx = parseInt(area.attr('data-idx'));
			var data = {cd:$(obj).attr('data-cd'), nm:txt};
			$m.dialog.closeByIndex(idx, data);
		},
		openMessage:function(obj){
			var html = [];
			html.push('<div class="popbackground" id="BCK_'+obj.index+'" style="z-index:'+(50+obj.index)+'" onclick="$m.dialog.closeByIndex('+obj.index+');"></div>');
			
			html.push('<div class="popuparea_s" id="POP_'+obj.index+'" style="z-index:'+(50+obj.index)+'">');
			html.push('	<div class="btn" onclick="$m.dialog.closeByIndex('+obj.index+', null, event);"></div>');
			html.push('	<div class="popup">');
			html.push('	<div class="inner">');

			if(obj.isHtml){
				html.push('	<div class="bodyzone">');
				html.push('	<div class="bodyarea">');
				html.push(obj.msg);
				html.push('	</div>');
				html.push('	</div>');
			} else {
				html.push('	<div class="bodyzone">');
				html.push('	<div class="bodyarea">');
				html.push('		<dl>');
				html.push('		<dd class="bodytxt">'+escapeHtml(obj.msg)+'</dd>');
				html.push('		</dl>');
				html.push('	</div>');
				html.push('	</div>');
			}

			if(obj.btns != null){
				html.push('	<div class="popbtnarea">');
				html.push('	<div class="btnarea">');
				html.push('	<div class="size">');
				html.push('		<dl>');
				for(var i=0;i<obj.btns.length;i++){
					html.push('		<dd class="btn" onclick="'+obj.btns[i][1]+'">'+obj.btns[i][0]+'</dd>');
				}
				html.push('		</dl>');
				html.push('	</div>');
				html.push('	</div>');
				html.push('	</div>');
			}

			html.push('	</div>');
			html.push('	</div>');
			html.push('</div>');
			$('.wrapper:first').append(html.join('\n'));
			
			var pop = $('#POP_'+obj.index);
			var popTop = parseInt(pop.css('margin-top'));
			var inner = pop.find('.inner:first');
			inner.css('height','auto');
			var innerHeight = inner[0].scrollHeight;
			if(innerHeight<200){
				var gap = parseInt((200 - innerHeight)/2);
				pop.css('margin-top', (popTop + gap)+'px');
			} else {
				var max = Math.min(240, innerHeight);
				pop.find('.popup').css('height', max+'px');
				inner.css('height','100%');
				var gap = parseInt((200 - max)/2);
				pop.css('margin-top', (popTop + gap -3)+'px');
			}
		},
		open:function(obj, handler){
			
			if(obj.titleId != null && obj.titleId != ''){
				obj.title = $m.msg.callMsg(obj.titleId);
			}
			if(obj.url != null && obj.url != ''){
				$m.ajax(obj.url, obj.param, function(html){
					obj.html = html;
				}, {mode:'HTML'});
			}
			
			if(obj.html == null) return;
			obj.index = ++this.index;
			
			var html = [];
			var btnX = (obj.noMove==true) ? '<div class="btn"></div>' : '';
			html.push('<div class="popbackground" id="BCK_'+obj.index+'" style="z-index:'+(50+obj.index)+';" onclick="$m.dialog.closeByIndex('+obj.index+');">'+btnX+'</div>');
			
			html.push('<div id="POP_'+obj.index+'" style="position:absolute; top:100%; width:100%; height:100%; z-index:'+(50+obj.index)+';" onclick="$m.dialog.closeByIndex('+obj.index+');">');
			
			if(obj.cld == true){
				html.push('<div class="popbackground_cld">');
				html.push('<div class="popuparea_cld" onclick="$m.dialog.holdClose(event);">');
			}else{
				html.push('<div class="popuparea" onclick="$m.dialog.holdClose(event);">');
			}
			if(obj.title!=null && obj.title!=''){
				html.push('	<div class="poptit">');
				html.push('		<div class="tit">'+obj.title+'</div>');
				html.push('		<div class="close" onclick="$m.dialog.closeByIndex('+obj.index+');"></div>');
				html.push('	</div>');
			}
			
			if(obj.noPopbody!=true){
				html.push('	<div class="popbody">');
			}
			
			html.push(obj.html);
			
			if(obj.btns != null){
				html.push('	<div class="blank20"></div>');
				html.push('	<div class="btnarea">');
				html.push('		<div class="size">');
				html.push('		<dl>');
				for(var i=0;i<obj.btns.length;i++){
					html.push('		<dd class="btn" onclick="'+obj.btns[i][1]+'">'+obj.btns[i][0]+'</dd>');
				}
				html.push('		</dl>');
				html.push('		</div>');
				html.push('	</div>');
				html.push('	<div class="blank20"></div>');
			}
			
			if(obj.noPopbody!=true){
				html.push('	</div>');
			}
			
			html.push('</div>');
			html.push('</div>');
			if(obj.cld == true){
				html.push('</div>');
			}
			var wrapper = $('.wrapper:first');
			if(wrapper.length==0) wrapper = $('body:first');
			wrapper.append(html.join('\n'));
			
			if(obj.noResize){
				this.data[obj.index] = { id:obj.id, handler:handler };
			} else {
				this.data[obj.index] = { id:obj.id, handler:handler, resizable:true };
				this.resizeByIndex(obj.index, true);
			}
			
			$('#POP_'+obj.index).find('input[type!=\'hidden\'], textarea').on("focus", function(){
				$m.dialog.focused = this;
				window.setTimeout('$m.dialog.scrollToTitle()', 100);
			});
			if(obj.noMove!=true){
				window.setTimeout("$('#POP_"+obj.index+"').removeClass('popup-moveFromBottom').css('top',0).addClass('popup-moveFromBottom')", 650);
			} else {
				return $('#POP_'+obj.index);
			}
		},
		resizeByIndex:function(idx, initResize, scr){
			if(scr==null) scr = $m.nav.screenSize();
			$('#BCK_'+idx).width(scr.w).height(scr.h).css('left','').css('bottom','');
			
			var pop = $('#POP_'+idx);
			var btn = pop.find('.popbtnarea:first');
			if(pop.length>0 && btn.length>0){
				var or = $m.nav.getOrientation();
				var w = $(window).height();
				var gapId = (!(or==90||or==-90) ? 'V':'H')+w;
				var gap = this.data[idx][gapId];
				if(gap==null){
					gap = pop.find('.popbody:first').height() - btn.position().top - 63;
					this.data[idx][gapId] = gap;
				}
				
				if(gap>0){
					var poparea = pop.find('.popuparea:first');
					var gapTop = Math.floor(gap/2);
					var gapBottom = gap - gapTop;
					poparea.css('top', 20 + gapTop);
					poparea.css('bottom', 20 + gapBottom);
				} else {
					pop.find('.popuparea:first').css('top', 20).css('bottom', 20);
					if(initResize){
						this.data[idx].resizable = false;
					}
				}
			}
		},
		resizeAll:function(){
			var scr = $m.nav.screenSize();
			for(var i=0; i<this.data.length; i++){
				if(this.data[i] != null && this.data[i].resizable){
					this.resizeByIndex(i, false, scr);
				}
			}
			this.scrollToTitle();
		},
		closeAll:function(){
			while(this.index>=0){
				this.closeByIndex(this.index, null, null, true);
			}
		},
		holdClose:function(event){
			if(event!=null && event.preventDefault) event.preventDefault();
			this.holdCloseFlag = true;
			window.setTimeout('$m.dialog.holdCloseFlag = false;', 100);
		},
		scrollToTitle:function(){
			if($m.dialog.focused == null) return;
			var input = $($m.dialog.focused), p = input.parentClass('popbody');
			p.scrollTop(input.position().top + p.scrollTop() - 30);
		},
		getDialog:function(id){
			var dlgIdx = null;
			this.data.each(function(idx, obj){
				if(obj!=null && obj.id==id){
					dlgIdx = idx;
					return false;
				}
			});
			if(dlgIdx == null) return null;
			return $('#POP_'+dlgIdx);
		}
	},
	
	user:{
		callback:null,
		selected:null,
		search:function(){
			$m.nav.next(null, '/or/user/searchUser.do');
		},
		viewUserPop:function(userUid){
			$m.dialog.open({
				titleId:'or.jsp.viewUserPop.title',
				url:'/or/user/viewUserPop.do?userUid='+userUid,
				btns:[[$m.msgmap['btn-close'], '$m.dialog.closeByIndex('+($m.dialog.index+1)+');']]
			});
		},
		selectOneUser:function(obj, callback){
			var param = new ParamMap(obj).put('selection', 'single');
			this.openSelect(param, callback);
		},
		selectUsers:function(obj, callback){
			var param = new ParamMap(obj).put('selection', 'multi');
			this.openSelect(param, callback);
		},
		openSelect:function(param, callback){
			if(callback==null){
				alert('No callback-func was defined !');
				return;
			}
			this.callback = callback;
			this.selected = param.get('selected');
			param.remove('selected');
			$m.nav.next(null, '/or/user/selectUser.do?'+param.toQueryString());
		}
	},
	org:{
		callback:null,
		selected:null,
		selectOneOrg:function(obj, callback){
			var param = new ParamMap(obj).put('selection', 'single');
			this.openSelect(param, callback);
		},
		selectOrgs:function(obj, callback){
			var param = new ParamMap(obj).put('selection', 'multi');
			this.openSelect(param, callback);
		},
		openSelect:function(param, callback){
			if(callback==null){
				alert('No callback-func was defined !');
				return;
			}
			this.callback = callback;
			this.selected = param.get('selected');
			param.remove('selected');
			$m.nav.next(null, '/or/org/selectOrg.do?'+param.toQueryString());
		}
	},
	msgmap:{},
	
	msg:{
		callMsg:function(msgId, args, lang){
			return this.callMsgAjax(msgId, args, lang, '/cm/msg/getMessageAjx.do');
		},
		callTerm:function(msgId, args){
			return this.callMsgAjax(msgId, args, null, '/cm/msg/getTermAjx.do');
		},
		alertMsg:function(msgId, args, handler){
			var lang = null; 
			if(handler!=null && typeof handler === 'string'){
				lang = handler;
				handler = null;
			}
			var message = this.callMsg(msgId, args, lang);
			if(message!=null){
				$m.dialog.alert(message, handler);
			}
		},
		confirmMsg:function(msgId, args, handler){
			var lang = null; 
			if(handler!=null && typeof handler === 'string'){
				lang = handler;
				handler = null;
			}
			var message = this.callMsg(msgId, args, lang);
			if(message!=null){
				return $m.dialog.confirm(message, handler);
			}
			return false;
		},
		callMsgAjax:function(msgId, args, lang, url){
			var message = null;
			$m.ajax(url, {'msgId':msgId, 'lang':lang, 'args':args}, function(data){
				message = data.message;
			}, {error:function(){}});
			return message;
		}
	},
	
	menu:{
		afterLoginOn:false,
		on:false,
		mainMenu:null,
		onLoutId:null,
		subHeight:0,
		menuIndex:1,
		open:function(loutId, menuId){
			this.mainMenu.find('#adursSelector').hide();
			this.mainMenu.removeClass('menu-moveToLeft');
			
			var iconArea = this.mainMenu.find('#iconArea');
			iconArea.children('.ico_on').attr('class', 'ico');
			iconArea.find('#'+loutId).attr('class','ico_on');
			
			var subAniArea = this.mainMenu.find('#subAniArea'), subAniHeight, dnoHeight;
			subAniArea.children().each(function(){
				$(this).find('.sub_on').removeClass('sub_on');
				
				if($(this).attr('id')==loutId){
					$(this).css('left', '0').removeClass('menu-moveToLeft');
					if(menuId==null){
						$(this).find('ul li:first').addClass('sub_on');
						$m.menu.afterLoginOn = true;
					} else {
						$m.menu.afterLoginOn = false;
						$(this).find('#'+menuId).addClass('sub_on');
					}
					
					subAniHeight = $(this).attr('data-subAniHeight');
					dnoHeight = $(this).attr('data-dnoHeight');
					if(subAniHeight!=null && subAniHeight!='' && dnoHeight!=null && dnoHeight!=''){
						subAniHeight = parseInt(subAniHeight);
						dnoHeight = parseInt(dnoHeight);
						
						if(dnoHeight<=0){
							subAniArea.height(subAniHeight -dnoHeight -1);
						} else {
							subAniArea.height(subAniHeight);
						}
					}
				} else {
					$(this).css('left', '-400%').removeClass('menu-moveToLeft');
				}
			});
			
			this.mainMenu.find('.background').show();
			this.mainMenu.css('left','0');
			this.mainMenu.addClass('menu-moveFromLeft');
			
			this.on = true;
			this.onLoutId = loutId;
			
			this.updateCount();
		},
		close:function(){
			this.mainMenu.find('.background').hide();
			this.mainMenu.removeClass('menu-moveFromLeft');
			this.mainMenu.addClass('menu-moveToLeft');
			this.on = false;
			window.setTimeout("$m.menu.mainMenu.css('left','-200%').removeClass('menu-moveToLeft')", 600);
			window.setTimeout("$m.menu.mainMenu.find('.background').show()", 650);
		},
		iconClick:function(event, loutId, mnuGrpId){
			if(event!=null && event.preventDefault) event.preventDefault();
			if(this.onLoutId==loutId) return;
			
			var iconArea = this.mainMenu.find('#iconArea');
			iconArea.find('#'+this.onLoutId).attr('class','ico');
			var loutDiv = iconArea.find('#'+loutId), isOnLout = false;
			if(loutDiv.length>0){
				loutDiv.attr('class','ico_on');
				isOnLout = true;
			}
			
			var subAniArea = this.mainMenu.find('#subAniArea');
			var outDiv = subAniArea.find('#'+this.onLoutId).css('z-index','2');
			var inDiv = subAniArea.find('#'+loutId).css('z-index','1').css('left','0').removeClass('menu-moveToLeft');
			
			var newSubHeight = inDiv.find('.sub').height();
			subAniArea.height( newSubHeight >= this.subHeight ? newSubHeight-(isOnLout ? 2 : 1) : this.subHeight );
			this.onLoutId = loutId;
			
			outDiv.addClass('menu-moveToLeft');
			if(mnuGrpId!=''){
				$m.ajax("/cm/getOuterUrlAjx.do", {mnuGrpId:mnuGrpId}, function(data){
					if(data.message!=null) $m.dialog.alert(data.message);
					if(data.url != null) {
						if($m.browser.mobile && $m.browser.safari){
							var a = document.createElement('a');
						    a.setAttribute("href", data.url);
						    a.setAttribute("target", "_blank");
						    var dispatch = document.createEvent("HTMLEvents");
						    // initEvent( "커스텀 이벤트 이름:String", "버블링:Boolean", "취소가능:Boolean" )
						    dispatch.initEvent("click", true, true); // Events : e.initEvent,  MouseEvents : e.initMouseEvent, UIEvents : e.initUIEvent
						    a.dispatchEvent(dispatch);
						}else{
							window.open(data.url, 'GW_SSO_POP');
							$m.menu.close();
						}
					}
				});
			}
		},
		openSso:function(event, param){
			$m.ajax("/cm/getOuterUrlAjx.do", param, function(data){
				if(data.message!=null) $m.dialog.alert(data.message);
				if(data.url != null) {
					window.open(data.url, 'GW_SSO_POP');
					$m.menu.close();
				}
			});
		},
		menuClick:function(event, mnuTypCd, mnuUrl){
			if(event!=null && event.preventDefault) event.preventDefault();
			if(this.on) this.close();
			if($m.nav.index == $m.menu.menuIndex){
				$m.nav.replaceCurr(null, mnuUrl);
			} else {
				$m.nav.move(null, mnuUrl, $m.menu.menuIndex);
			}
		},
		fixedMenuClick:function(){
			var win = $m.nav.getWin(0);
			if(win!=null) win.$('header:first div.menu').trigger('click');
		},
		getFirstUrl:function(){
			this.mainMenu = $('#mainMenu');
			return this.mainMenu.find("#subArea .sub li[data-url!='']:first").attr('data-url');
		},
		setSize:function(width, height){
			this.mainMenu = $('#mainMenu');
			var subArea = this.mainMenu.find('#subArea');
			if(subArea.length==0) return;
			
			this.mainMenu.width(width).height(height);
			if(height<460){
				this.mainMenu.find('.aside_header').hide();
				subArea.css('top','118px');
			} else {
				this.mainMenu.find('.aside_header').show();
				subArea.css('top','187px');
			}
			var subAniArea = subArea.find('#subAniArea');
			var subAniHeight = height - subArea.offset().top - subArea.find('#footer').height(), dnoHeight;
			subAniArea.css('height', subAniHeight);
			this.subHeight = subAniHeight;
			
			subAniArea.children().each(function(){
				dnoHeight = subAniHeight - $(this).find('.sub').outerHeight();
				$(this).find('.dno').height(dnoHeight>0 ? dnoHeight : 0);
				$(this).attr('data-subAniHeight', subAniHeight);
				$(this).attr('data-dnoHeight', dnoHeight);
			});
		},
		logout:function(event){
			$('#dataframe').attr('src', '/cm/login/processLogout.do');
		},
		switchUser:function(event, uid, destination){
			$('#dataframe').attr('src', '/cm/login/processAdurSwitch.do?userUid='+uid+(destination==null ? '' : '&destination='+destination));
		},
		updateCount:function(){
			var cntIds = ['topMailCnt','topAppCnt','topSchdlCnt','topApvMailCnt','topAdditionalCnt'];
			$m.ajax('/cm/getMainCntAjx.do', {}, function(data){
				if(data.message!=null){
					alert(data.message);
				}
				var area = $("#countArea");
				for(var i=0;i<cntIds.length;i++){
					if(data.result=='ok'){
						if(data.rsltMap[cntIds[i]]==null){
							$('.'+cntIds[i]+'Cls').hide();
							continue;
						}
						area.find('#'+cntIds[i]).text(data.rsltMap[cntIds[i]]);
						if(cntIds[i]=='topApvMailCnt'){
							$('.topAdditionalCnt').hide(); // 겸직 건수 hidden
							$('.topSchdlCntCls').hide(); // 일정 건수 hidden
							$('.'+cntIds[i]+'Cls').show();
							break;
						}
					} else {
						area.find('#'+cntIds[i]).text('0');
					}
				}
				if(data.rsltMap && data.rsltMap['addtionalFnc']!=null){
					$('#topAdditionalCnt').parent().attr('onclick', data.rsltMap['addtionalFnc']);
				}
			}, {async:true});
		}
	},
	
	openEditor:function(){
		$m.secu.set();
		window.open("/cm/util/editor.do", "editorWin");
	}
};

$(document).ready(function() {
	$m.initOrange();
});
