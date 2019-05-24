// 포틀릿 다이얼로그
var pltDialog = {
	data:{ clientX:null, clientY:null, zindex:100, posIndex:1, moveObject:null, moveTop:null, moveLeft:null, resizeObject:null, resizeWidth:null, resizeHeight:null, resizeDirection:null },
	openYn:{},
	closeHandler:{},
	movePx:4,
	skin:'blue',
	areaText:null,
	toMovePx:function(value){
		var no = parseInt(value / this.movePx);
		return no * this.movePx;
	},
	onClose:function(id, handler){ this.closeHandler[id] = handler; },
	open:function(areaId, id, title, top, left, width, height, rescId){
		var $pltDialog = $('#'+id);
		if(this.areaText==null){
			this.areaText = callMsg("pt.jsp.setPltStep2.pltContArea");
		}
		if($pltDialog.length==0){
			var buff=[];
			buff.push('<div class="portlet" id="'+id+'" data-rescId='+rescId+' style="position:absolute; z-index:'+(++pltDialog.data.zindex));
			if(top==null && left==null){
				buff.push('; top:'+pltDialog.toMovePx(pltDialog.data.posIndex*24)+'px; left:'+pltDialog.toMovePx(pltDialog.data.posIndex*36)+'px; width:'+pltDialog.toMovePx(width)+'px; height:'+pltDialog.toMovePx(height)+'px; visibility:visible; display:none;">');
				pltDialog.data.posIndex++;
			} else {
				buff.push('; top:'+top+'px; left:'+left+'px; width:'+width+'px; height:'+height+'px; visibility:visible; display:none;">');
			}
			buff.push('<div id="pltDialogTitle" class="ptltit" style="cursor:move;">');
			buff.push('	<dl>');
			buff.push('	<dd id="pltDialogTitleTxt" class="title">'+title+'</dd>');
			buff.push('	<dd class="btn"><a href="javascript:;" id="cloasBtn" onclick="pltDialog.close(this);"><img src="/images/'+pltDialog.skin+'/icoptl_delete.png" width="19" height="21" /></a></dd>');
			buff.push('	</dl>');
			buff.push('</div>');
			buff.push('<div id="pltDialogBody" class="ptlbody">');
			buff.push('<div class="ptlpage" style="margin:4px; padding:0px;"><div id="pltContTxt">'+this.areaText+'</div></div>');
			buff.push('</div>');
			buff.push('</div>');
			$("#"+areaId).first().append(buff.join('\n'));
			$pltDialog = $('#'+id);
			
			$pltDialog.find('#pltDialogTitle').bind("mousedown", function(event){
				var outer = this.parentNode;
				pltDialog.data.moveObject = outer;
				pltDialog.data.moveTop = parseInt($(outer).css('top'));
				pltDialog.data.moveLeft = parseInt($(outer).css('left'));
				pltDialog.data.clientX = event.clientX;
				pltDialog.data.clientY = event.clientY;
				
				if($(outer).css('z-index')!=pltDialog.data.zindex){
					$(outer).css('z-index', ++pltDialog.data.zindex);
				}
				
				if(event.preventDefault) event.preventDefault();
				return false;
			});
			
			$pltDialog.bind("mousedown", function(event){
				var $me = $('#'+id), data = pltDialog.data;
				var fromLeft = parseInt($(this).css('width')) - parseInt(event.pageX-$me.offset().left) + 1;
				var fromBottom = parseInt($(this).css('height')) - parseInt(event.pageY-$('#'+id).offset().top) + 1;
				if(fromLeft<8){
					if(fromBottom<8) data.resizeDirection = 'BOTH';
					else data.resizeDirection = 'WIDTH';
				} else if(fromBottom<8){
					data.resizeDirection = 'HEIGHT';
				}
				if(data.resizeDirection != null){
					data.resizeObject = $me[0];
					data.resizeWidth = parseInt($me.css('width'));
					data.resizeHeight = parseInt($me.css('height'));
					data.clientX = event.clientX;
					data.clientY = event.clientY;
				}
				if($me.css('z-index')!=pltDialog.data.zindex){
					$me.css('z-index', ++pltDialog.data.zindex);
				}
				if(event.preventDefault) event.preventDefault();
				return false;
			});
			var windowObj = (browser.ie && browser.ver<9) ? document : window;
			$(windowObj).bind("mouseup", function(event){
				data = pltDialog.data;
				data.moveObject = null;
				data.resizeObject = null;
				data.resizeDirection = null;
			});
			$(windowObj).bind("mousemove", function(event){
				var data = pltDialog.data;
				if(data.moveObject != null){
					var topPos  = Math.max(0, event.clientY - data.clientY + data.moveTop);
					var leftPos = Math.max(0, event.clientX - data.clientX + data.moveLeft);
					$(data.moveObject).css('top', pltDialog.toMovePx(topPos));
					$(data.moveObject).css('left', pltDialog.toMovePx(leftPos));
					guideLine.resize(areaId);
					if(event.preventDefault) event.preventDefault();
					return false;
				}
				if(data.resizeObject!=null){
					if(data.resizeDirection=='BOTH' || data.resizeDirection=='WIDTH'){
						var width  = Math.max(50, event.clientX - data.clientX + data.resizeWidth);
						$(data.resizeObject).css('width', pltDialog.toMovePx(width));
						guideLine.resize();
						guideLine.resize(areaId, 'width');
					}
					if(data.resizeDirection=='BOTH' || data.resizeDirection=='HEIGHT'){
						var height = pltDialog.toMovePx(Math.max(55, event.clientY - data.clientY + data.resizeHeight));
						var $pltpage = $(data.resizeObject).find('.ptlpage');
						$pltpage.css('height',(height-38)+'px');
						$(data.resizeObject).find('#pltContTxt').css('padding-top',(height-48)/2);
						$(data.resizeObject).css('height',height);
						guideLine.resize(areaId, 'height');
					}
					if(event.preventDefault) event.preventDefault();
					return false;
				}
			});
			
		} else {
			$pltDialog.find('#pltDialogTitleTxt').text(title);
			$pltDialog.css('z-index', ++pltDialog.data.zindex);
		}
		$pltDialog.css('width',width);
		var $pltpage = $pltDialog.find('.ptlpage');
		$pltpage.css('height',(height-38)+'px');
		$pltDialog.find('#pltContTxt').css('padding-top',(height-48)/2);
		$pltDialog.css('height',height);
		$pltDialog.show();
		
		this.openYn[id] = 'Y';
	},
	close:function(obj){
		var id=null, handler;
		var p = obj.parentNode;
		while(p!=null){
			if(p.tagName!=null && p.tagName.toLowerCase()=='body'){
				break;
			}
			if($(p).attr('class')=='portlet'){
				$(p).find('#cloasBtn').focus();
				$(p).remove();
				break;
			}
			p = p.parentNode;
		}

		if(id!=null){
			this.openYn[id] = 'N';
			if((handler = pltDialog.closeHandler[id]) != null){
				pltDialog.closeHandler[id] = null;
				handler(id);
			}
		}
	},
	isOpen:function(id){
		return 'Y' == this.openYn[id];
	}
};
// 가이드 라인
var guideLine = {
	vIndex : 1,
	hIndex : 1,
	clientX:null,
	clientY:null,
	posiTop:null,
	posiLeft:null,
	moveLine:null,
	vTitle:null,
	hTitle:null,
	areaId:null,
	draw:function(areaId, type, px){
		if(this.areaId==null) {
			this.areaId = areaId;
			var windowObj = (browser.ie && browser.ver<9) ? document : window;
			$(windowObj).bind("resize", function(event){
				guideLine.resize(areaId, null);
			});
		}
		var html = null, id = null;
		if(type=='H'){//가로줄
			html = '<div id="hLine'+guideLine.hIndex+'" class="pageline_w" onmouseover="this.style.cursor=\'hand\';" onmouseout="this.style.cursor=\'default\';" style="cursor:hand; position:absolute; width:100%; top:'+px+'px; left:0px; z-index:200; visibility:visible;"><a href="javascript:guideLine.close(\'hLine'+guideLine.hIndex+'\');"><img src="/images/'+pltDialog.skin+'/pageline_x.png"/></a></div>';
			id = 'hLine'+guideLine.hIndex;
			guideLine.hIndex++;
		} else if(type=='V'){//세로줄
			html = '<div id="vLine'+guideLine.vIndex+'" class="pageline_h" onmouseover="this.style.cursor=\'hand\';" onmouseout="this.style.cursor=\'default\';" style="cursor:hand; position:absolute; height:100%; top:0px; left:'+px+'px; bottom:0; z-index:200; visibility:visible;"><a href="javascript:guideLine.close(\'vLine'+guideLine.vIndex+'\');"><img src="/images/'+pltDialog.skin+'/pageline_x.png"/></a></div>';
			id = 'vLine'+guideLine.vIndex;
			guideLine.vIndex++;
		}
		
		if(html!=null){
			var $area = $("#"+areaId);
			$area.append(html);

			$('#'+id).bind("mousedown", function(event){
				$me = $('#'+id);
				guideLine.clientX = event.clientX;
				guideLine.clientY = event.clientY;
				guideLine.posiTop = parseInt($me.css('top'));
				guideLine.posiLeft = parseInt($me.css('left'));
				guideLine.moveLine = $me[0];
				if(event.preventDefault) event.preventDefault();
				return false;
			});
			var windowObj = (browser.ie && browser.ver<9) ? document : window;
			$(windowObj).bind("mouseup", function(event){
				if(guideLine.moveLine!=null) guideLine.moveLine = null;
			});
			$(windowObj).bind("mousemove", function(event){
				if(guideLine.moveLine!=null){
					if($(guideLine.moveLine).attr('class')=='pageline_w'){
						var moveTop  = Math.max(0, event.clientY - guideLine.clientY + guideLine.posiTop);
						$(guideLine.moveLine).css("top", pltDialog.toMovePx(moveTop));
					} else if($(guideLine.moveLine).attr('class')=='pageline_h'){
						var moveLeft = Math.max(0, event.clientX - guideLine.clientX + guideLine.posiLeft);
						$(guideLine.moveLine).css("left", pltDialog.toMovePx(moveLeft));
					}
				}
				if(event.preventDefault) event.preventDefault();
				return false;
			});
		}
		this.resize(areaId, null);
	},
	close:function(id){//라인 삭제
		$("#"+id).remove();
	},
	resize:function(areaId, direction){// 리사이즈
		var $area = $("#"+areaId);
		if(direction==null || direction=='height'){
			$area.find("div.pageline_h").css("height", $area.prop("scrollHeight"));
		}
		if(direction==null || direction=='width' ){
			$area.find("div.pageline_w").css("width" , $area.prop("scrollWidth" ));
		}
	},
	openPop:function(type, menuId){// 가로/세로 줄 추가 팝업 열기
		if(type=='H'){//가로줄
			if(this.hTitle==null){
				this.hTitle = callMsg("pt.jsp.setPltStep2.add.hline");
			}
			dialog.open("selectLineDialog", this.hTitle, "./setLinePop.do?menuId="+menuId+"&type="+type);
			$('#setLinePop #px').val(guideLine.hIndex*20);
			$('#setLinePop #btnSaveLine').focus();
		} else if(type=='V'){//세로줄
			if(this.vTitle==null){
				this.vTitle = callMsg("pt.jsp.setPltStep2.add.vline");
			}
			dialog.open("selectLineDialog", this.vTitle, "./setLinePop.do?menuId="+menuId+"&type="+type);
			$('#setLinePop #px').val(guideLine.vIndex*20);
			$('#setLinePop #btnSaveLine').focus();
		}
	}
};