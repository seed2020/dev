var browser = { ie:false, chrome:false, firefox:false, safari:false, opera:false, etc:false, ver:0, ieCompatibility:false, secure:location.protocol === 'https:',
	init:function(){
		var ag = navigator.userAgent, p;
		if((p=ag.indexOf('MSIE'))>0){
			this.ie = true;
			this.ver = parseInt(ag.substring(p+5, ag.indexOf(';',p+5)));
		} else if((p=ag.indexOf('Trident'))>0){
			this.ie = true;
			p = ag.indexOf("rv:",p+8);
			this.ver = parseInt(ag.substring(p+3, ag.indexOf('.',p+3)));
		} else if((p=ag.indexOf('OPR/'))>0){
			this.opera = true;
		} else if((p=ag.indexOf('Chrome'))>0){
			this.chrome = true;
		} else if((p=ag.indexOf('Firefox'))>0){
			this.firefox = true;
		} else if((p=ag.indexOf('Safari'))>0){
			this.safari = true;
		} else if((p=ag.indexOf('Opera'))>0){
			this.opera = true;
		} else {
			this.etc = true;
		}
		this.ieCompatibility = /MSIE 7.*Trident/.test(ag);
		return this;
	}
}.init();

// 탭변경 무시
var gIgnoreTabChange = false;
// 탭버튼 클릭
function changeTab(tabId, tabNo){
	if(gIgnoreTabChange){
		gIgnoreTabChange = false;
		return;
	}
	var areaId, showAreaId=null;
	$("#"+tabId+" li").each(function(index, obj){
		areaId = $(obj).attr("data-areaId");
		if(tabNo==index){
			$(obj).attr("class","basic_open");
			if(areaId!=null) {
				$("#"+areaId).show();
				showAreaId = areaId;
			}
		} else {
			$(obj).attr("class","basic");
			if(areaId!=null && areaId != showAreaId){
				$("#"+areaId).hide();
			}
		}
	});
}
// 탭숨김, 보이기
function displayTab(tabId, areaId, on){
	var $tab = $("#"+tabId+" li[data-areaId="+areaId+"]");
	if($tab.length>0){
		if(on==true){
			$tab.show();
		} else {
			if($tab.attr("class") == "basic_open"){
				var $altTab = $tab.prev();
				if($altTab.length==0) $altTab = $tab.next();
				if($altTab.length>0){
					var $a = $altTab.find("a:first");
					var href = $a.attr("href");
					if(href.startsWith("javascript:")){
						eval(href.substring(11));
					} else {
						$a.trigger("click");
					}
				}
			}
			$tab.hide();
		}
	}
}
// 스크립트의 따옴표("" or '') 내에서 깨지지 않도록 치환
function escapeScript(value){
	if(value==null || value=='') return value;
	value = value.replace(/\'/g,"\\\'");
	value = value.replace(/\"/g,"\\\"");
	value = value.replace(/\r/g,"\\\r");
	value = value.replace(/\n/g,"\\\n");
	return value;
}
// element 의 value 따옴표(value="" or value='') 내에서 깨지지 않도록 치환
function escapeValue(value){
	if(value==null || value=='') return value;
	value = value.replace(/\'/g,"&apos;");
	value = value.replace(/\"/g,"&quot;");
	value = value.replace(/\r/g,"&#13;");
	value = value.replace(/\n/g,"&#10;");
	return value;
}
function escapeHtml(value){
	if(value==null || value=='') return value;
	value = value.replace(/</g,"&lt;");
	value = value.replace(/>"/g,"&gt;");
	value = value.replace(/\r/g,"");
	value = value.replace(/\n/g,"<br/>");
	return value;
}
// DIV 팝업
var dialog = {
	data:{ clientX:null, clientY:null, objectTop:null, objectLeft:null, documentHeight:null, documentWidth:null, instance:null, zindex:1000 },
	noFocus:false,
	openYn:{},
	closeHandler:{},
	onClose:function(id, handler){ this.closeHandler[id] = handler; },
	local:false,
	open2:function(id, title, url, option){
		if(option==null){
			this.open(id, title, url);
		} else {
			var param = option==null ? null : option['param'];
			var width = option==null ? null : option['width'];
			var height = option==null ? null : option['height'];
			var focusId = option==null ? null : option['focusId'];
			var failHandler = option==null ? null : option['failHandler'];
			this.open(id, title, url, param, width, height, focusId, failHandler, option);
		}
	},
	open:function(id, title, url, param, width, height, focusId, failHandler, option){
		var $dialog = $('#'+id);
		if($dialog.length==0){
			var offsetTop = option!=null && option.offsetTop!=null ? ' data-offsetTop="'+option.offsetTop+'"' : '';
			var offsetLeft = option!=null && option.offsetLeft!=null ? ' data-offsetLeft="'+option.offsetLeft+'"' : '';
			$(".styleThese").first().append('<div class="layerpoparea" id="'+id+'" style="position:absolute; z-index:'+(++dialog.data.zindex)+'; visibility:visible; display:none;"'+offsetTop+offsetLeft+' ></div>');
			$dialog = $('#'+id);
			
			var buff=[];
			buff.push('<div id="dialogTitle" class="lypoptit" style="cursor:move;">');
			buff.push('	<dl>');
			buff.push('	<dd id="dialogTitleTxt" class="title">'+title+'</dd>');
			buff.push('	<dd class="btn"><a href="javascript:void(0);" id="cloasBtn" onclick="dialog.close(this);" class="close"><span>close</span></a></dd>');
			buff.push('	</dl>');
			buff.push('</div>');
			buff.push('<div id="dialogBody" class="bodyarea">');
			buff.push('</div>');
			$dialog.append(buff.join('\n'));
			
			if(option!=null && option['padding']!=null){
				var $dialogBody = $dialog.find("#dialogBody");
				var padding = option['padding'];
				if(padding['right']!=null){
					$dialogBody.css("padding-right", padding['right']);
				}
				if(padding['left']!=null){
					$dialogBody.css("padding-left", padding['left']);
				}
				if(padding['top']!=null){
					$dialogBody.css("padding-top", padding['top']);
				}
				if(padding['bottom']!=null){
					$dialogBody.css("padding-bottom", padding['bottom']);
				}
			}
			
			$dialog.find('#dialogTitle').bind("mousedown", function(event){
				var outer = this.parentNode;
				dialog.data.instance = outer;
				dialog.data.objectTop = parseInt($(outer).css('top'));
				dialog.data.objectLeft = parseInt($(outer).css('left'));
				dialog.data.documentHeight = $(document).height();
				dialog.data.documentWidth = $(document).width();
				dialog.data.clientX = event.clientX;
				dialog.data.clientY = event.clientY;
				
				if($(outer).css('z-index')!=dialog.data.zindex){
					$(outer).css('z-index', ++dialog.data.zindex);
				}
				
				if(event.preventDefault) event.preventDefault();
				return false;
			});
			var windowObj = (browser.ie && browser.ver<9) ? document : window;
			$(windowObj).bind("mouseup", function(event){
				dialog.data.instance = null;
			});
			$(windowObj).bind("mousemove", function(event){
				if(dialog.data.instance != null){
					var diff;
					var topPos = event.clientY - dialog.data.clientY + dialog.data.objectTop;
					topPos = Math.max(-10, topPos);
					diff = (browser.ie && browser.ver<8) || browser.firefox ? 25 : 40;
					topPos = Math.min(dialog.data.documentHeight-diff, topPos);
					var leftPos = event.clientX - dialog.data.clientX + dialog.data.objectLeft;
					leftPos = Math.max(30 - $(dialog.data.instance).width(), leftPos);
					diff = browser.ie && browser.ver>7 ? 50 : 30;
					leftPos = Math.min(dialog.data.documentWidth-diff, leftPos);
					$(dialog.data.instance).css('top', topPos);
					$(dialog.data.instance).css('left', leftPos);
					if(event.preventDefault) event.preventDefault();
					return false;
				}
			});
			
		} else {
			$dialog.find('#dialogTitleTxt').text(title);
			$dialog.css('z-index', ++dialog.data.zindex);
		}
		var popHtml = (option!=null && option['popHtml']!=null) ? option['popHtml'] : callHtml(url, param, failHandler);
		if(this.local && popHtml!=null && popHtml.trim().startsWith('<!DOCTYPE')){
			alert('check jsp tiles type !');
		}
		if(popHtml!=null){
			var $body = $dialog.find('#dialogBody');
			var html = popHtml;
			
			$body.html(html);
			var left = $dialog.css("left");
			var opened = left!='auto' && left!='0px';
			
			left = !opened ? -2000 : parseInt(left) - 2000;
			$dialog.css("left", left+"px");
			$dialog.show();
			
			$body.find("input, textarea, select, button").not(".skipThese").uniform();
			$body.find("input:checkbox,input:radio").bind("keydown", function(event){
				if(event.which==13) $(this).trigger("click");
			});
			this.resize(id, width, height, option!=null && option['hidden']==true ? null : opened);
			if(this.noFocus){
				this.noFocus = false;
			} else {
				if(focusId==null){
					var $focus = $dialog.find("input:visible, select:visible, textarea:visible").not("[readonly='readonly']").first();
					if($focus.length>0) $focus.focus();
					else {
						$dialog.find("#cloasBtn:first").focus();
					}
				} else {
					$dialog.find("#"+focusId).focus();
				}
			}
		}
		this.openYn[id] = 'Y';
	},
	cancelHide:function(id){
		this.resize(id, null, null, false);
	},
	resize:function(id, width, height, opened){
		var $dialog = $('#'+id);
		var $dialogBody = $dialog.find('#dialogBody');
		var $sizeRefer = $dialogBody.find("div:first");
		
		var padW = parseInt($dialogBody.css("padding-left")) + parseInt($dialogBody.css("padding-right"));
		var padH = parseInt($dialogBody.css("padding-top")) + parseInt($dialogBody.css("padding-bottom"));
		var dialogWidth = width==null ? Math.max($sizeRefer.width()+padW, 200) : width;
		var dialogHeight = height==null ? Math.max($sizeRefer.height()+30+padH, 120) : height;
		$dialog.css('width',dialogWidth+'px');
		$dialog.css('height',dialogHeight+'px');
		
		if(opened==null){}
		else if(opened==true){
			var left = parseInt($dialog.css("left")) + 2000;
			$dialog.css("left", left+"px");
		} else {
			var dialogTop = Math.max(parseInt(($(window).height() - dialogHeight)/2 + $(window).scrollTop()),$(window).scrollTop());
			var dialogLeft = Math.max(parseInt(($(window).width() - dialogWidth)/2 + $(window).scrollLeft()),$(window).scrollLeft());
			var offsetTop = $dialog.attr("data-offsetTop");
			var offsetLeft = $dialog.attr("data-offsetLeft");
			$dialog.css("top", offsetTop!=null && offsetTop!='' ? parseInt(offsetTop) : dialogTop);
			$dialog.css("left",offsetLeft!=null && offsetLeft!='' ? parseInt(offsetLeft) : dialogLeft);
		}
	},
	close:function(obj){
		var $pop=null;
		if (typeof obj === 'string') {
			$pop = $('#'+obj);
		} else {
			var p = obj.parentNode, $p;
			while(p!=null){
				if(p.tagName!=null && p.tagName.toLowerCase()=='body'){
					break;
				}
				if(($p = $(p)).attr('class')=='layerpoparea'){
					$pop = $p;
					break;
				}
				p = p.parentNode;
			}
		}
		if($pop!=null){
			if(!this.checkEditor($pop.find('.bodyarea'))) return;
			this.runCloseHandler($pop.attr('id'));
			
			$pop.find("iframe").each(function(){
				if(this.contentDocument){
					$(this.contentDocument.activeElement).blur();
					$(this.contentDocument).find('a:first').focus();
				}
			});
			
			$pop.find('#cloasBtn').focus();
			$pop.hide();
			$pop.find('.bodyarea').empty();
			$pop.css('left','0px');
		}
	},
	runCloseHandler:function(id){
		this.openYn[id] = 'N';
		if((handler = dialog.closeHandler[id]) != null){
			dialog.closeHandler[id] = null;
			handler(id);
		}
	},
	forward:function(id){
		var $dialog = $('#'+id);
		if($dialog.length>0) $dialog.css('z-index', ++dialog.data.zindex);
	},
	isOpen:function(id){
		return 'Y' == this.openYn[id];
	},
	closeAll:function(){
		for(var id in this.openYn) {
			if(this.openYn[id] == 'Y'){
				dialog.close(id);
			}
		}
	},
	checkEditor:function($bodyarea){
		var $editFrm = $bodyarea.find("iframe[title='Editor Area']");
		if($editFrm.length>0){
			var changed = false;
			$editFrm.each(function(){
				if(!changed){
					var editId = $(this).attr('id');
					editId = editId.substring(0, editId.length-4);
					if(jellyEditor(editId).isChanged()){
						changed = true;
					}
				}
			});
			if(changed){
				var result = confirmMsg('cm.cfrm.closeChange');
				// for ie bug under ver. 11
				if(!result && browser.ie && browser.ver<11){
					window.onbeforeunload = null;
					window.setTimeout('window.onbeforeunload = onUnloadEvent;',300);
				}
				return result;
			}
		}
		return true;
	}
};
// TREE
var TREE = {
	ins: {},
	cpath: null,
	skin: "blue",
	selectOption:1,
	iconTitle: null,
	getIconTitle: function(iconType){
		if(this.iconTitle==null) return '';
		var va = this.iconTitle[iconType];
		return va==null ? '' : va;
	},
	img: { tree:"tree", folder:"tree_folder", 
		root:"tree_org", app:"tree_application", word:"tree_document", 
		comp:"ico_company", org:"ico_organization", dept:"ico_department", part:"ico_part"},
	getImage: function(type, open){
		var path = [];
		if(this.cpath!=null) path.push(this.cpath);
		path.push("/images/");
		path.push(this.skin);
		path.push("/");
		path.push(type=='T' ? this.img.tree : type=='F' ? this.img.folder : 
			type=='R' ? this.img.root : type=='A' ? this.img.app : type=='W' ? this.img.word : 
			type=='C' ? this.img.comp : type=='G' ? this.img.org : type=='D' ? this.img.dept : type=='P' ? this.img.part : "");
		if(type=='T'||type=='F'){
			path.push(open ? "_open" : "_close");
		}
		if(type=='R'||type=='T'||type=='F'){
			path.push(".gif");
		} else {
			path.push(".png");
		}
		return path.join('');
	},
	create: function(id, builder){
		if(builder==null) builder = new TreeBuilder(id);
		this.ins[id] = builder;
		return builder;
	},
	getTree: function(id){
		return this.ins[id];
	},
	toggle:function(treeId, nodeId, open){
		var $li = $('#'+treeId+' #'+nodeId+"LI"), $ul = $li.find("ul:first");
		if($ul.children().length == 0){
			if($ul.attr('data-secondary')=='Y'){
				$ul.attr('data-secondary', '');
				this.getTree(treeId).drawSecondary(nodeId, $ul);
				if($ul.children().length == 0) return;
			} else {
				return;
			}
		}
		if(open==null) open = ($ul.css("display") == "none");
		$ul.css("display", open ? "block" : "none");
		$li.find("nobr img:first").attr("src",TREE.getImage('T', open));
	},
	select:function(treeId, nodeId){
		var tree = TREE.getTree(treeId), $li;
		if(this.selectOption==1){
			if(tree.selected!=null){
				$li =  $(tree.selected);
				var $a = $li.attr('id')=='ROOTLI' ? $li.find('a:last') : $li.find('nobr:first a:last');
				$a.removeClass('menu_open');
				tree.selected = null;
			}
			$li = $('#'+treeId+' #'+nodeId+'LI');
			if($li.length==1){
				var $a = $li.attr('id')=='ROOTLI' ? $li.find('a:last') : $li.find('nobr:first a:last');
				$a.addClass('menu_open');
				tree.selected = $li[0];
			}
		} else {
			$li = $('#'+treeId+' #'+nodeId+'LI');
			if($li.length==1){
				var $a = $li.attr('id')=='ROOTLI' ? $li.find('a:last') : $li.find('nobr:first a:last');
				if($a.hasClass('menu_open')){
					$a.removeClass('menu_open');
				} else {
					$a.addClass('menu_open');
				}
			}
		}
	},
	getExtData:function(id, extId){
		if(this.ins[id]==null || this.ins[id].selected==null) return null;
		var exts = $(this.ins[id].selected).attr('data-'+extId);
		if(exts==null || exts=='') return null;
		return JSON.parse(exts);
	}
};
//TREE 생성
function TreeBuilder(id){
	this.id = id;
	this.treeDiv = $("#"+id);
	this.rootId = null;
	this.nodes = {};
	this.nodeSize = 0;
	this.secondaryDraw = false;
	this.secondaryIds = [];
	this.selected = null;
	this.onclick = 'clickTree';
	this.openLvl = 2;
	this.isTwoStepDraw = function(){
		return this.nodeSize > 500;
	};
	this.setRoot = function(rootId, rootName){
		this.rootId = rootId;
		this.add('_', rootId, rootName, 'R', '1');
	};
	this.setSkin = function(skin){
		TREE.skin = skin;
	};
	this.setIconTitle = function(iconTitle){
		TREE.iconTitle = JSON.parse(iconTitle);
	};
	this.add = function(pid, id, name, type, sortOrdr, rescId, exts){
		var node = {pid:pid,id:id,name:name,type:type,sortOrdr:parseInt(sortOrdr,10),rescId:rescId,exts:exts,children:null};
		this.nodes[id] = node;
		this.nodeSize++;
	};
	this.prepare = function(){
		var node, pNode, nodes = this.nodes;
		for(var id in nodes){
			node = nodes[id];
			pNode = nodes[node.pid];
			if(pNode!=null){
				if(pNode.children==null) pNode.children = [];
				pNode.children.push(node);
			}
		}
		var root = nodes[this.rootId];
		if(root==null || root.children==null) return root;
		this.sortNode(root);
		return root;
	};
	this.sortNode = function(node){
		var child, children = node.children;
		children.sort(function(a,b){ return a.sortOrdr - b.sortOrdr; });
		for(var i=0;i<children.length;i++){
			child = children[i];
			if(child.children!=null) this.sortNode(child);
		}
	};
	this.draw = function(){
		var root = this.prepare(), htmls=[];
		root.id = 'ROOT';
		htmls.push('<nobr id="ROOTLI" data-exts=\'{"id":"ROOT"}\'><img src="'+TREE.getImage('R')+'"> <a href="javascript:void(0);" onclick="TREE.select(\''+this.id+'\',\'ROOT\'); '+this.onclick+'(\'ROOT\',\''+escapeValue(root.name)+'\')">'+root.name+'</a></nobr>');
		this.drawUL(root, htmls, 1);
		this.treeDiv.html(htmls.join('\n'));
		this.secondaryDraw = true;
	};
	this.drawUL = function(node, htmls, openLvl){
		var i, size = node.children==null ? 0 : node.children.length;
		var disp = (size>0 && openLvl!=null && openLvl<=this.openLvl);
		var skipChild = !(!this.isTwoStepDraw() || disp || this.secondaryDraw);
		htmls.push('<ul id="'+node.id+'UL" '+(disp ? 'class="tree_menu"' : 'style="display:none"')+(skipChild && size>0 ? ' data-secondary="Y"' : '')+'>');
		if(!skipChild){
			for(i=0;i<size;i++){
				this.drawLI(node.children[i], htmls, (size-i)==1, openLvl);
			}
		} else {
			this.secondaryIds.push(node.id);
		}
		htmls.push('</ul>');
	};
	this.drawLI = function(node, htmls, isLast, openLvl){
		var aTitle = (node.exts==null || node.exts.title==null) ? '' : ' title=\"'+node.exts.title+'\"';
		var treeOpen = (node.children==null || node.children.length==0) || (openLvl!=null && openLvl<this.openLvl);
		htmls.push('<li id="'+node.id+'LI"'+this.toDataExt('exts',node.exts)+this.toDataExt('rescs',node.rescs)+(isLast?' class="end"':'')+'>');
		htmls.push('<nobr>');
		htmls.push('<a href="javascript:void(0);" class="control" onclick="TREE.toggle(\''+this.id+'\',\''+node.id+'\')"><img id="'+node.id+'T" src="'+TREE.getImage('T', treeOpen)+'" /></a>');
		htmls.push('<a href="javascript:void(0);" onclick="TREE.toggle(\''+this.id+'\',\''+node.id+'\')"><img id="'+node.id+'F" data-type="'+node.type+'" src="'+TREE.getImage(node.type, false)+'" title="'+TREE.getIconTitle(node.type)+'" /></a>');
		htmls.push('<a href="javascript:void(0);" onclick="TREE.select(\''+this.id+'\',\''+node.id+'\'); '+this.onclick+'(\''+node.id+'\',\''+escapeValue(node.name)+'\',\''+(node.rescId!=null ? node.rescId : node.exts.rescId !=null ? node.exts.rescId : '')+'\');"'+aTitle+'>'+node.name+'</a>');
		if(node.exts!=null && node.exts.useYn=='N') htmls.push('<img data-type="'+node.type+'" src="/images/cm/del.gif" title="Not Used" />');
		htmls.push('</nobr>');
		this.drawUL(node, htmls, (openLvl==null ? null : openLvl+1));
		htmls.push('</li>');
	};
	this.drawSecondary = function(nodeId, $ul){
		var htmls = [];
		this.drawUL(this.nodes[nodeId], htmls, this.openLvl);
		if(htmls.length > 0) $ul.html(htmls.join('\n'));
	};
	this.toDataExt = function(extNm, data){
		if(data==null || data=='') return '';
		var str = JSON.stringify(data);
		str = str.replace(/\'/g,'&apos;');
		str = str.replace(/\"/g,'&quot;');
		str = str.replace(/\r/g,'&#13;');
		str = str.replace(/\n/g,'&#10;');
		return ' data-'+extNm+'="'+str+'"';
	};
	this.selectTree = function(nodeId){
		if(nodeId=='') return;
		var $node = this.treeDiv.find('#'+nodeId+'LI'), $p;
		if($node.length==0 && this.isTwoStepDraw()){
			var nodeObj = this.nodes[nodeId];
			if(nodeObj != null){
				while(true){
					if(this.secondaryIds.contains(nodeObj.id)){
						var $ul = this.treeDiv.find('#'+nodeObj.id+'UL');
						this.drawSecondary(nodeObj.id, $ul);
						$node = $ul.find('#'+nodeId+'LI');
						$ul.show();
						break;
					}
					if((nodeObj = this.nodes[nodeObj.pid]) == null) break;
				}
			}
		}
		if($node.length>0){
			$p = $node.find("UL:first");
			while(true){
				if($p.children().length>0) $p.show();
				if($p.parent().find("nobr:first").attr("id")=='ROOTLI') break;
				$p.parent().find("nobr:first img:first").attr("src",TREE.getImage('T', open));
				$p = $p.parent().parent();
				if($p.length==0) break;
				if($p[0].tagName==null || $p[0].tagName.toLowerCase()!='ul') break;
			}
		}
		var $a = (nodeId=='ROOT') ? $node.find('a:last') : $node.find('nobr:first a:last');
		$a.addClass('menu_open');
		this.selected = $node[0];
	};
	this.isRootSelected = function(){
		return this.treeDiv.find("a.menu_open:first").parent().attr('id')=='ROOTLI';
	};
	this.selection = function(handler, option){
		if(handler!=null){
			this.treeDiv.find("a.menu_open"+(option==null ? "" : option)).each(handler);
		}
	};
	this.seletedUL = function(handler, option){
		var div = this.treeDiv;
		div.find("a.menu_open"+(option==null ? "" : option)).parent().each(function(){
			if($(this).attr('id')=='ROOTLI'){
				div.find("#ROOTUL").each(handler);
			} else {
				$(this).parent().children("ul").each(handler);
			}
		});
	};
	this.seletedLI = function(handler, option){
		var div = this.treeDiv;
		div.find("a.menu_open"+(option==null ? "" : option)).parent().each(function(){
			if($(this).attr('id')=='ROOTLI'){
				$(this).each(handler);
			} else {
				$(this).parent().each(handler);
			}
		});
	};
	this.removeSelected = function(){
		var $li, $prev;
		this.treeDiv.find("a.menu_open").each(function(){
			$li = $(this).parent().parent();
			if($li.hasClass("end")){
				$prev = $li.prev();
				if($prev.length>0){
					$prev.addClass("end");
				} else {
					if($li.parent().attr('id')!='ROOTUL'){
						$li.parent().parent().addClass("end");
					}
				}
			}
			$li.remove();
		});
		this.selected = null;
	};
	this.removeAll = function(){
		this.treeDiv.find("#ROOTUL").children().remove();
		this.selected = null;
	};
	this.move = function(direction){
		var $p = this.treeDiv.find("a.menu_open:first").parent();
		if($p.attr('id')!='ROOTLI'){
			if(direction=='up'){
				var $li = $p.parent();
				var $prev = $li.prev();
				if($prev.length>0){
					$prev.before($li);
				}
				if($li.hasClass("end")){
					$li.removeClass("end");
					$prev.addClass("end");
				}
			} else if(direction=='down'){
				var $li = $p.parent();
				var $next = $li.next();
				if($next.length>0){
					$next.after($li);
				}
				if($next.hasClass("end")){
					$next.removeClass("end");
					$li.addClass("end");
				}
			}
		}
	};
	this.getPidForAppend = function(){
		var $p = this.treeDiv.find("a.menu_open:first").parent();
		if($p.length==0 || $p.attr('id')=='ROOTLI'){
			return 'ROOT';
		} else {
			$p = $p.parent();
			var exts = $p.attr("data-exts");
			var obj = $.parseJSON(exts);
			if(obj.fldYn=='N') return null;
			var pid = $p.attr('id');
			if(pid.length<2) return null;
			return pid.substring(0, pid.length-2);
		}
	};
	this.appendNodes = function(pid, nodes){
		if(nodes.length>0){
			var htmls=[], $ul = this.treeDiv.find("#"+pid+"UL"), me = this;
			if(pid=='ROOT' && !$ul.hasClass("tree_menu")){
				$ul.addClass("tree_menu");
			}
			$ul.children(":last").removeClass("end");
			nodes.each(function(index, node){
				me.drawLI(node, htmls, index+1 == nodes.length, null);
			});
			$ul.append(htmls.join('\n'));
			$ul.show();
		}
	};
}
var docCookies = {
	getItem: function (sKey) {
		return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
	},
	setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
		if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return false; }
		var sExpires = "";
		if (vEnd) {
			switch (vEnd.constructor) {
				case Number:
					sExpires = vEnd === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age=" + vEnd;
					break;
				case String:
					sExpires = "; expires=" + vEnd;
					break;
				case Date:
					sExpires = "; expires=" + vEnd.toUTCString();
					break;
			}
		}
		document.cookie = encodeURIComponent(sKey) + "=" + encodeURIComponent(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
		return true;
	},
	removeItem: function (sKey, sPath, sDomain) {
		if (!sKey || !this.hasItem(sKey)) { return false; }
		document.cookie = encodeURIComponent(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + ( sDomain ? "; domain=" + sDomain : "") + ( sPath ? "; path=" + sPath : "");
		return true;
	}
};
// 전체 선택/선택해제
function checkAllCheckbox(areaId, checked, checkId){
	var arrs=checkId!=undefined ? $("#"+areaId).find("input:checkbox:visible[id='"+checkId+"']") : $("#"+areaId).find("input:checkbox:visible");
	arrs.each(function(){
		if(checked != this.checked){
			$(this).trigger('click');
		}
	});
}
// 체크된 체크박스의 value 배열 구하기
function getCheckedValue(areaId, noSelectMsgId){
	var arr = [], va;
	$("#"+areaId+" input:checked").each(function(){
		va = $(this).val();
		if(va!='') arr.push(va);
	});
	if(arr.length==0 && noSelectMsgId!=null){
		alertMsg(noSelectMsgId);
		return null;
	}
	return arr;
}
// 관리자용 입력에서 언어 선택 변경
function changeLangTypCd(areaId, langAreaId, langCd){
	$('#'+areaId+' #'+langAreaId+' input').each(function(){
		var rescNm = $(this).attr('name');
		if(rescNm.endsWith(langCd)){
			$(this).show(); $(this).focus();
		} else { $(this).hide(); }
	});
}
// input:text 에서 readonly 설정, 설정해제
function setReadonly($object, readonly){
	setUiDisable($object, "readonly", readonly);
}
// 비활성화 처리 (checkbox, radio, select 용)
function setDisabled($object, disabled){
	setUiDisable($object, "disabled", disabled);
}
// readonly, disable 처리
function setUiDisable($object, type, disabled){
	var uiType = getUiType($object);
	if(disabled){
		$object.attr(type,type);
		if(uiType=='text') $object.addClass("input_disabled");
		else if(uiType=='textarea') $object.addClass("textarea_disabled");
		else $object.uniform.update();
	} else {
		$object.removeAttr(type);
		if(uiType=='text') $object.removeClass("input_disabled");
		else if(uiType=='textarea') $object.removeClass("textarea_disabled");
		else $object.uniform.update();
	}
}
// input이면 type 아니면 tagName 리턴 - setUiDisable에서 사용
function getUiType($object){
	if($object==null || $object.length==0 || $object[0].tagName==null) return null;
	var tn = $object[0].tagName.toLowerCase();
	if(tn!='input') return tn;
	var tp = $object.attr('type');
	return tp==null ? 'text' : tp.toLowerCase();
}
// disabled 해제하기 - submit시 전송하기 위한것
function releaseDisable($object){
	var arr = [];
	$object.each(function(){
		if($(this).attr('disabled')=='disabled'){
			$(this).removeAttr('disabled');
			arr.push(this);
		}
	});
	return arr;
}
//disabled 해제 원복하기
function unreleaseDisable(arr){
	for(var i=0;i<arr.length;i++){
		$(arr[i]).attr('disabled','disabled');
	}
}
// 해당 테그명에 해당하는 부모테그 구하기, 테그명 소문자 기준
function getParentTag(obj, tagNm){
	var curTagNm;
	while(true){
		obj = obj.parentNode;
		if(obj.tagName!=null){
			curTagNm = obj.tagName.toLowerCase();
			if(curTagNm==tagNm) return obj;
			if(curTagNm=='body') return null;
		}
	}
}
// 리소스 입력시 해당 어권 보이게
function changeLangSelector(areaId, id, va){
	if(va==''){
		var $langSelector = $('#'+areaId+' #langSelector');
		$langSelector.val(id.substring(id.length-2));
		$langSelector.trigger('click');
	}
}
// 어권별 입력 필수가 이닐 경우 모든 어권 입력이 없으면 필수 해제함
function skipMandatory(areaId, idStart){
	var hasVa = false;
	$('#'+areaId+' [id^='+idStart+']').each(function(){
		if(!hasVa && $(this).val()!='') hasVa = true;
	});
	return !hasVa;
}
/////////////////////////////////////////
//
//          TOP 메뉴 관련

// Top 영역
var $gMenuTop = {$topArea:null, popMnuObj:null, popMnuTimeout:null, extraMnuObj:null, extraMnuTimeout:null};
// 메뉴에 등록된 외부팝업
function openMnuPop(grpId, url, opts, event){
	if(url!=null && url.startsWith('javascript:')){
		if(event!=null && event.preventDefault) event.preventDefault();
		var func = url.substring(11).trim();
		if(func.startsWith('openErpSso')){
			openErpSso(grpId, opts);
		} else {
			eval(func);
		}
	} else {
		window.open(url, grpId, opts);
		if(event!=null && event.preventDefault) event.preventDefault();
	}
}
// 서브메뉴(메인서브,관리자,겸직 팝업) 보이기
function showMnuPop(event, evtObj, tgtObj){
	$(tgtObj).show();
	var tgtId = $(tgtObj).attr("id");
	var evtId = $(evtObj).attr("id");
	if(evtId=='AdurMnu'){
		if(gLoutType=='B') $(tgtObj).css("left", "5px");
		else {
			var left = $(evtObj).offset().left - 15;
			$(tgtObj).css("left", left+"px");
		}
	} else if(tgtId=='topExtraMnuArea' || !tgtId.startsWith("topSubMain")){
		if($(evtObj).attr("id").startsWith("Icon")){
			var tgtW = $(tgtObj).width();
			var left = $(evtObj).offset().left - ((tgtW - 70) / 2);
			$(tgtObj).css("left", left+"px");
		} else {
			var left = $(evtObj).offset().left - 15;
			$(tgtObj).css("left", left+"px");
		}
	}
}
// 서브메뉴(메인서브,관리자,겸직 팝업) 감추기
function hideSubPopMnu(){
	if($gMenuTop.popMnuTimeout != null){
		window.clearTimeout($gMenuTop.popMnuTimeout);
		$gMenuTop.popMnuTimeout = null;
	}
	if($gMenuTop.popMnuObj != null){
		$($gMenuTop.popMnuObj).hide();
		$gMenuTop.popMnuObj = null;
	}
}
// 메인메뉴 초과분의 팝업 메뉴 감추기
function hideExtraPopMnu(){
	if($gMenuTop.extraMnuTimeout != null){
		window.clearTimeout($gMenuTop.extraMnuTimeout);
		$gMenuTop.extraMnuTimeout = null;
	}
	if($gMenuTop.extraMnuObj != null){
		$($gMenuTop.extraMnuObj).hide();
		$gMenuTop.extraMnuObj = null;
	}
}
// [메뉴]메뉴위로 마우스 들어갈때- 팝업메뉴 보이기
function enterMnuMouse(event){
	hideSubPopMnu();
	hideExtraPopMnu();
	if($gMenuTop.$topArea==null) $gMenuTop.$topArea = $("#header_"+gSkin);
	// 서브메뉴 보이기 - 서브(관리자,겸직)
	var $sub = $gMenuTop.$topArea.find("#topSub"+$(this).attr("id"));
	if($sub.length>0){
		$gMenuTop.popMnuObj = $sub[0];
		showMnuPop(event, this, $sub[0]);
	}
	// 서브메뉴 보이기 - 메인(메인메뉴 표시 갯수 초과분 팝업)
	var cls = $(this).attr("class");
	if(cls!=null && cls.startsWith("bmenuarrow")){
		var $subMain = $gMenuTop.$topArea.find("#topExtraMnuArea");
		if($subMain.length>0){
			$gMenuTop.extraMnuObj = $subMain[0];
			showMnuPop(event, this, $subMain[0]);
		}
	}
}
// [팝업]메인메뉴 초과분의 팝업 메뉴 - 메뉴위로 마우스 들어갈때- 팝업메뉴 보이기
function enterExtraMouse(event){
	hideSubPopMnu();
	// 서브메뉴 보이기
	var $sub = $gMenuTop.$topArea.find("#topSub"+$(this).attr("id"));
	if($sub.length>0){
		$gMenuTop.popMnuObj = $sub[0];
		showMnuPop(event, this, $sub[0]);
	}
}
// [메뉴]메뉴에서 마우스 나갈때 - 팝업메뉴 숨기기 - timeout
function leaveMnuMouse(event){
	if($gMenuTop.popMnuObj != null){
		$gMenuTop.popMnuTimeout = window.setTimeout("hideSubPopMnu()", 600);
	}
	if($gMenuTop.extraMnuObj != null){
		$gMenuTop.extraMnuTimeout = window.setTimeout("hideExtraPopMnu()", 600);
	}
}
// [팝업]팝업메뉴 - 팝업위로 마우스 올라갈때 - 이벤트 함수, 타임아웃 클리어
function enterSubPopMouse(event){
	if($gMenuTop.popMnuTimeout != null){
		window.clearTimeout($gMenuTop.popMnuTimeout);
		$gMenuTop.popMnuTimeout = null;
	}
}
// [팝업]메인메뉴 초과분의 팝업 메뉴 - 팝업위로 마우스 올라갈때 - 이벤트 함수, 타임아웃 클리어
function enterExtraPopMouse(event){
	hideSubPopMnu();
	if($gMenuTop.extraMnuTimeout != null){
		window.clearTimeout($gMenuTop.extraMnuTimeout);
		$gMenuTop.extraMnuTimeout = null;
	}
	// 서브메뉴 보이기 - 서브(관리자,겸직)
	var $sub = $gMenuTop.$topArea.find("#topSub"+$(this).attr("id"));
	if($sub.length>0){
		$gMenuTop.popMnuObj = $sub[0];
		showMnuPop(event, this, $sub[0]);
	}
}
// [팝업]팝업메뉴 - 팝업에서 벗어날때
function leaveSubPopMouse(event){
	$gMenuTop.popMnuTimeout = window.setTimeout("hideSubPopMnu()", 600);
}
// [팝업]메인메뉴 초과분의 팝업 메뉴 - 팝업에서 벗어날때
function leaveExtraPopMouse(event){
	$gMenuTop.extraMnuTimeout = window.setTimeout("hideExtraPopMnu()", 600);
	if($gMenuTop.popMnuObj != null){
		$gMenuTop.popMnuTimeout = window.setTimeout("hideSubPopMnu()", 600);
	}
}
function openErpSso(grpId, opts){
	callAjax('/cm/sso/getErpTkUrlAjx.do', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.lginUrl != null){
			location.href = data.lginUrl;
		} else if(data.url != null){
			window.open(data.url, grpId, opts);
		}
	});
}
//          TOP 메뉴 관련
//
/////////////////////////////////////////

// 로고클릭 - 홈으로 이동
function goHome(){
	callAjax('/cm/getHomeUrlAjx.do', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.url != null){
			location.href = data.url;
		}
	});
}
// 상단메뉴 클릭
function goLout(event, mnuLoutId){
	callAjax('/cm/getLoutUrlAjx.do', {mnuLoutId:mnuLoutId}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.url != null){
			if(event.preventDefault) event.preventDefault();
			location.href = data.url;
		}
	});
}
// 상단 마이메뉴 클릭
function goMyLout(event){
	callAjax('/cm/getMyMnuUrlAjx.do', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.url != null){
			if(event.preventDefault) event.preventDefault();
			location.href = data.url;
		}
	});
}
// 메뉴에서 팝업 오픈
function openMnuDialog(popTitle, popUrl, event){
	dialog.open('menuPopDialog', popTitle, popUrl);
	if(event.preventDefault) event.preventDefault();
}

// file-tag 에서 사용
var gFileTagMap = {};
function resetFileTag(id){
	var html = gFileTagMap[id];
	if(html!=null){
		var $file = $("#"+id+"File");
		$file.before(html);
		$file.remove();
		$("#"+id+"FileView").val('');
	}
}
function setFileTag(viewId, value, handler, exts){
	if(value==null) value = "";
	else {
		var p = value.lastIndexOf('\\');
		if(p>0) value = value.substring(p+1);
	}
	var $view = $("#"+viewId);
	var oldValue = $view.val();
	$view.val(value);
	
	if(exts!=null && exts!="" && value!=""){
		if(!browser.ie || oldValue!=value){//IE에서 클릭했을때 이벤트 타는 버그 고침
			var va = value.toLowerCase();
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				alertMsg('cm.msg.attach.not.support.ext',[exts]);
				resetFileTag(viewId.substring(0, viewId.length-8));
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), null);
			} else {
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
			}
		}
	} else {
		if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
	}
}

// 달력
var calendar = {
	skin : 'blue',
	area : null,
	myId : null,
	checkOpt : null,
	handler : null,
	open : function(myId, checkOpt, handler){
		if(this.myId == myId){
			this.close();
		} else {
			this.close();
			this.myId = myId;
			this.checkOpt = checkOpt;
			this.handler = handler;
			this.draw(this.getMyDt());
		}
	},
	close : function(){
		if(this.area != null){
			var $area = $(this.area);
			$area.find('#yearArea').hide();
			$area.find('#monthArea').hide();
			$area.hide();
			this.area = null;
			this.myId = null;
		}
	},
	draw : function(myDt){
		var $area = this.checkOpt!=null && this.checkOpt['iframeId']!=null ? $('#'+this.checkOpt['iframeId']).contents().find('#'+this.myId+'CalArea') : $('#'+this.myId+'CalArea');
		if($area.length>0){
			var $cal = $area.find('#calendar');
			if($cal.length==0){
				$area.append('<div id="calendar" class="calendar" style="position:absolute; z-index:990;"></div>');
				$cal = $area.find('#calendar');
			}
			
			var buffer = [];
			this.appendHead(myDt, buffer);
			this.appendBody(myDt, buffer);
			$cal.html(buffer.join(''));
			var offset = null;
			
			if(this.checkOpt!=null && this.checkOpt['iframeId']!=null){
				$("body").find('#'+this.myId+'CalArea').remove();
				var iframe = $('#'+this.checkOpt['iframeId']);
				offset = iframe.contents().find('#'+this.myId+'CalBtn').position();
				var top = offset.top+21;
				var left = offset.left-89;
				left = iframe.offset().left + left;
				top = iframe.offset().top + top;
				$area.css('top' , top+'px');
				$area.css('left', left+'px');
				var html = $area[0].outerHTML;
				$("body").append(html);
				$("body").find('#'+this.myId+'CalArea').show();
				this.area = $("body").find('#'+this.myId+'CalArea');
				return;
			}
			offset = $('#'+this.myId+'CalBtn').position();
			this.area = $area[0];
			$area.css('top' , (offset.top+21)+'px');
			$area.css('left', (offset.left-89)+'px');
			$area.show();
		}
	},
	getMyDt : function(){
		var myDt = $('#'+this.myId).val();
		return myDt==null || myDt=='' || myDt.length != 10 ? new Date() : new Date(parseInt(myDt.substring(0,4)), parseInt(myDt.substring(5,7),10)-1, parseInt(myDt.substring(8),10));
	},
	appendHead : function(myDt, buffer){
		buffer.push('<div class="closearea"><a href="javascript:calendar.close();" class="close"><span>close</span></a></div>\n');
		buffer.push('<table class="month" border="0" cellpadding="0" cellspacing="0"><tbody><tr>\n');
		var y = myDt.getFullYear(), m = myDt.getMonth()+1;
		buffer.push('<td class="monthbtn"><a href="javascript:calendar.moveTo('+(y-1)+','+m+');"><img src="/images/'+this.skin+'/ico_allleft.png" width="20" height="20" /></a></td>\n');
		buffer.push('<td class="monthbtn"><a href="javascript:calendar.moveTo('+(m==1 ? y-1 : y)+','+(m==1 ? 12 : m-1)+');"><img src="/images/'+this.skin+'/ico_left.png" width="20" height="20" /></a></td>\n');
		buffer.push('<td class="monthtxt"><a href="javascript:calendar.chooseYear('+y+','+m+')" id="yearSelector">'+y+'</a>/<a href="javascript:calendar.chooseMonth('+y+','+m+')" id="monthSelector">'+this.fm(m)+'</a></td>');
		buffer.push('<td class="monthbtn"><a href="javascript:calendar.moveTo('+(m==12 ? y+1 : y)+','+(m==12 ? 1 : m+1)+');"><img src="/images/'+this.skin+'/ico_right.png" width="20" height="20" /></a></td>\n');
		buffer.push('<td class="monthbtn"><a href="javascript:calendar.moveTo('+(y+1)+','+m+');"><img src="/images/'+this.skin+'/ico_allright.png" width="20" height="20" /></a></td>\n');
		buffer.push('</tr></tbody></table>\n');
	},
	appendBody : function(myDt, buffer){
		buffer.push('<table class="week_pd" border="0" cellpadding="0" cellspacing="0"><tbody>\n');
		buffer.push('<tr>');
		'SMTWTFS'.split('').each(function(idx, w){
			buffer.push('<td width="23" class="'+(idx==0 ? 'week_red' : 'week_gray')+'">'+w+'</td>');
		});
		buffer.push('</tr>\n');
		
		var step = 0, suffix = '_off';
		var dt = new Date(myDt.getFullYear(), myDt.getMonth(), 0);
		var i, end = dt.getDate();
		var y = dt.getFullYear(), m = this.fm(dt.getMonth()+1), d = end - dt.getDay();
		var curr = new Date();
		var today = curr.getFullYear()==y && curr.getMonth()==myDt.getMonth() ? curr.getDate() : '';
		var holi = 'off';
		var holiday = '';
		
		callAjax('/cm/transGetCalendarHolidayAjax.do', {year:y+'',month:m+'',holyYn:'Y'}, function(data){
			if(data.message!=null){
				holiday = data.message;
			} 
		});
		
		while(step<2){
			buffer.push('<tr>');
			for(i=0;i<7;i++){
				if(holiday.indexOf(y+''+m+''+((d<10)?'0'+d:d))>-1) holi = 'on';
				else holi = 'off';
				buffer.push('<td><a href="javascript:calendar.setDate(\''+y+'-'+m+'-'+this.fm(d)+'\')" class="'+(step==1 && today==d ? 'today' : i==0 ? 'day_red'+suffix : holi=='on' ? 'day_red'+suffix : 'day_gray'+suffix)+'">'+d+'</a></td>');
				d++;
				if(d>end){
					dt = step==0 ? myDt : new Date(myDt.getFullYear(), myDt.getMonth()+1, 1);
					y = dt.getFullYear();
					m = this.fm(dt.getMonth()+1);
					d = 1;
					end = this.getLastDate(y,m);
					suffix = step==0 ? '' : '_off';
					today = step!=0 ? '' : curr.getFullYear()==y && curr.getMonth()==dt.getMonth() ? curr.getDate() : '';
					step++;
				}
			}
			buffer.push('</tr>\n');
			if(d>end) break;
		}
		buffer.push('</tbody></table>\n');
	},
	getLastDate : function(year, month){
		while(month<1){ year--; month+=12; }
		while(month>12){ year++; month-=12; }
		if(month==4 || month==6 || month==9 || month==11) return 30;
		if(month==2){
			return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0) ? 29 : 28;
		}
		return 31;
	},
	setDate : function(date){
		if(this.handler==null || this.handler(date, this.checkOpt)){
			if(this.checkOpt!=null){
				if(this.checkOpt['end']!=null){
					var $endCal = $('#'+this.checkOpt['end']);
					var endDt = $endCal.val();
					if(endDt!=null && endDt!='' && date>endDt){ $endCal.val(date); }
				}
				if(this.checkOpt['start']!=null){
					var $startCal = $('#'+this.checkOpt['start']);
					var startDt = $startCal.val();
					if(startDt!=null && startDt!='' && date<startDt){ $startCal.val(date); }
				}
				if(this.checkOpt['checkHandler']!=null){
					if(this.checkOpt['checkHandler'](date)) {return;};
				}
				if(this.checkOpt['iframeId']!=null){
					$('#'+this.checkOpt['iframeId']).contents().find('#'+this.myId).val(date);
					this.close();
					return;
				}
			}
			$('#'+this.myId).val(date);
			if(this.checkOpt!=null && this.checkOpt['onchange']!=null){
				this.checkOpt['onchange'](this);
			}
			this.close();
		}
	},
	moveTo : function(year, month){
		this.draw(new Date(year, month-1, 1));
		$(this.area).find('#yearArea').hide();
		$(this.area).find('#monthArea').hide();
	},
	chooseYear : function(year, month){
		var $area = $(this.area), $yearArea = $area.find('#yearArea');
		$area.find('#monthArea').hide();
		if($yearArea.length>0 && $yearArea.css('display')!='none'){
			$yearArea.hide();
			return;
		}
		
		if($yearArea.length==0){
			$area.append('<div id="yearArea" class="calendar_year" style="position:absolute; z-index:999;"></div>');
			$yearArea = $area.find("#yearArea");
		}
		
		var buffer = [];
		buffer.push('<div class="calendar_years">\n');
		buffer.push('<table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>\n');
		buffer.push('<tr><td height="3"></td></tr>\n');
		
		var i, y = year - 60;
		for(i=0;i<91;i++){
			buffer.push('<tr onmouseover=\'this.className="mouseover_color"\' onmouseout=\'this.className=""\'>');
			buffer.push('<td class="menu"><a href="javascript:calendar.moveTo('+(y+i)+','+month+');">'+(y+i)+'</a></td>');
			buffer.push('</tr>\n');
		}
		buffer.push('<tr><td height="3"></td></tr></tbody></table></div>');
		$yearArea.html(buffer.join(''));
		
		var offset = $(this.area).find('#yearSelector').position();
		$yearArea.css('top' , (offset.top+16)+'px');
		$yearArea.css('left', (offset.left-6)+'px');
		$yearArea.show();
		$yearArea.children().scrollTop(488);
	},
	chooseMonth : function(year, month){
		var $area = $(this.area), $monthArea = $area.find('#monthArea');
		$area.find('#yearArea').hide();
		if($monthArea.length>0 && $monthArea.css('display')!='none'){
			$monthArea.hide();
			return;
		}
		
		if($monthArea.length==0){
			$area.append('<div id="monthArea" class="calendar_month" style="position:absolute; z-index:999;"></div>');
			$monthArea = $area.find("#monthArea");
		}
		
		var buffer = [];
		buffer.push('<div class="calendar_months">\n');
		buffer.push('<table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>\n');
		buffer.push('<tr><td height="3"></td></tr>\n');
		
		for(var m=1;m<=12;m++){
			buffer.push('<tr onmouseover=\'this.className="mouseover_color"\' onmouseout=\'this.className=""\'>');
			buffer.push('<td class="menu"><a href="javascript:calendar.moveTo('+year+','+m+');">'+this.fm(m)+'</a></td>');
			buffer.push('</tr>\n');
		}
		buffer.push('<tr><td height="3"></td></tr></tbody></table></div>');
		$monthArea.html(buffer.join(''));
		
		var offset = $(this.area).find('#monthSelector').position();
		$monthArea.css('top' , (offset.top+16)+'px');
		$monthArea.css('left', (offset.left-8)+'px');
		$monthArea.show();
	},
	keydown : function(event){
		if(event.which==46) this.value = '';
		if(event.preventDefault) event.preventDefault();
		return false;
	},
	fm : function(no){ return no>9 ? no : '0'+no; }
};

function onlyDelWorks(event, object, isMnal){
	var k = event.which;
	if(((k>=37 && k<=40) || k==9 || (event.ctrlKey && k==67)) || 
			((isMnal!=undefined && isMnal=='Y') && ((k>=48 && k<=57) || (k>=96 && k<=105) || k==109 || k==189 || k==8 ))){ // 수동입력 + 숫자 + 백스페이스 + '-'
	} else {
		if(event.which==46 && $(object).attr('readonly')==null){
			object.value = '';
		}
		if(event.preventDefault) event.preventDefault();
		return false;
	}
}

// 조직도 부서 선택
var gOrgHandler = null;
var gOrgSelectedObj = null;
function searchOrgPop(option, handler){//option : {data, titleId, multi(true/false), withSub(true/false), mode(search/view), section(upward,downward)}
	var urlParam = '', title = callMsg(option!=null && option.titleId!=null ? option.titleId : option!=null && option.mode=="view" ? "cm.selectedList" : "or.jsp.searchOrgPop.title");
	gOrgHandler = handler;
	if(option!=null){
		gOrgSelectedObj = option.data;
		if(option.multi==true) urlParam = "?multi=Y";
		if(option.withSub==true) urlParam = (urlParam=='' ? '?' : urlParam+'&') + "withSub=Y";
		if(option.upward!=null){
			urlParam = (urlParam=='' ? '?' : urlParam+'&') + "upward="+option.upward;
		} else if(option.downward!=null){
			urlParam = (urlParam=='' ? '?' : urlParam+'&') + "downward="+option.downward;
		}
		if(option.data!=null && option.data.orgId!=null && option.data.orgId!=''){
			if(option.multi!=true || option.upward!=null || option.downward!=null){
				urlParam = (urlParam=='' ? '?' : urlParam+'&') + "orgId="+option.data.orgId;
			}
		}
		if(option.global!=null && option.global!=''){
			urlParam = (urlParam=='' ? '?' : urlParam+'&') + "global="+option.global;
		}
		if(option.withDel==true){
			urlParam = (urlParam=='' ? '?' : urlParam+'&') + "withDel=Y";
		}
		if(option.compId!=null){
			urlParam = (urlParam=='' ? '?' : urlParam+'&') + "compId="+option.compId;
		}
	}
	if(option!=null && option.mode=='view'){
		dialog.open('searchOrgDialog', title, '/or/org/viewOrgPop.do'+urlParam);
	} else {
		dialog.open('searchOrgDialog', title, '/or/org/searchOrgPop.do'+urlParam);
	}
}
// 사용자 선택
var gUserHandler = null;
var gUserSelectedObj = null;
function searchUserPop(option, handler){//option : {data, multi, mode, global, titleId, downward, userStatCd}
	var urlParam = [], title = (option!=null && option.title!=null) ? option.title : callMsg(option!=null && option.titleId!=null ? option.titleId : option!=null && option.mode=="view" ? "cm.selectedList" : "or.jsp.searchUserPop.title");
	gUserHandler = handler;
	if(option!=null){
		gUserSelectedObj = option.data;
		urlParam.push((option.multi==true) ? "?opt=multi" : "?opt=single");
		if(option.compId!=null){ urlParam.push("&compId="+option.compId); }
		if(option.global!=null){ urlParam.push("&global="+option.global); }
		if(option.foreign!=null){ urlParam.push("&foreign="+option.foreign); }
		if(option.orgId!=null) { urlParam.push("&orgId="+option.orgId); }
		if(option.downward!=null) { urlParam.push("&downward="+option.downward); }
		if(option.oneDeptId!=null) { urlParam.push("&oneDeptId="+option.oneDeptId); }
		if(option.userStatCd!=null) { urlParam.push("&userStatCd="+option.userStatCd); }
		if(option.apvrRoleCds!=null) { urlParam.push("&apvrRoleCds="+option.apvrRoleCds); }
		if(option.multi!=true && option.data!=null && option.data.userUid!=null && option.data.userUid!=''){
			urlParam.push("&userUid="+option.data.userUid);
		}
		if(option.mode=='search' && option.userNm!=null && option.userNm!=''){
			urlParam.push("&userNm="+option.userNm);
		}
	}
	if(option!=null && option.mode=='view'){
		dialog.open('searchUserDialog', title, '/or/user/listUserPop.do'+urlParam.join(''));
	} else {
		dialog.open('searchUserDialog', title, '/or/user/searchUserPop.do'+urlParam.join(''));
	}
}

/**
 * openWindow(url, name, width, height)
 * : 주어진 값에 따라 새창을 오픈한다. (화면 중앙에 위치함)
 *
 * @PARAM URL    WINDOW의 URL
 * @PARAM NAME   WINDOW의 명
 * @PARAM WIDHT  WINDOW폭 (픽셀)
 * @PARAM HEIGHT WINDOW높이 (픽셀)
 * @PARAM OPT WINDOW높이 (픽셀)
 *
 * examples  :
 * var win = openWindow("http://localhost/", localhost, 300, 300);
 *   
 * return : 해당 윈도우 객체.                                         
 **/
function openWindow(url, name, width, height, opt)   {
	var top  = screen.height / 2 - height / 2 - 50;
	var left = screen.width / 2 - width / 2 ;
	
	var option = 'resizable=no,status=yes,toolbar=no,menubar=no';
	if (opt != null && opt != undefined) option = opt;
	//alert(opt);

	var win = open(url, name, 'width=' + width + ', height=' + height + ', top=' + top + ', left=' + left + ', ' + option);
	win.focus();


	return win;
}

/**
 * 브라우저 관계 없이 iframe 내 content 가져오기.
 *
 * @param {String}	id
 *
 * return : 해당 윈도우 객체.                                         
 **/
function getIframeContent(id) {
	var ifrm = document.getElementById(id);
	if(ifrm==null) return null;
	return ifrm.contentWindow || ifrm.contentDocument;
}

/**
 * iframe 리사이징
 * 
 * @param {String}	id
 * 
 **/
function resizeIframe(id){
	var tgt = id == undefined || id == null ? 'iframe' : '#'+id;
	$(tgt).load(function() {
        $(this).css("height", $(this).contents().find("body").height() + "px");
    });
}

/** 페이지 로딩후 uniform 스타일 적용
 *
 * [참조] setUniformCSS()
 **/
//$(document).ready(function() {
//	setUniformCSS();
//});

/** 페이지를 재로딩 하지 않고 화면을 수정하는 경우 호출. (페이지 재로딩 없이 데이터를 표현할 경우, 새로운 데이터에 대해서 uniform 스타일이 적용되지 않음)
 *
 * [참조] $(document).ready()
 **/
function setUniformCSS(areaObj) {
	var styles = $(areaObj==null ? ".styleThese" : areaObj);
	styles.find("input, textarea, select, button, a.uniformTest").not(".skipThese").uniform();
	styles.find("input:checkbox,input:radio").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
// 오늘 날짜 리턴
function getToday(){
	var d = new Date();
	return d.getFullYear() +"-"+ ((d.getMonth() > 8) ? d.getMonth()+1 : "0"+(d.getMonth()+1)) +"-"+ ((d.getDate() > 9) ? d.getDate() : "0"+d.getDate());
}
// 엘리먼트에 uniform 적용
function applyUniform(id){
	$('#'+id).find("input, textarea, select, button").uniform();
}
// 엘리먼트에 적용된 uniform 복원
function restoreUniform(id) {
	$.uniform.restore($('#'+id).find('input, textarea, select, button'));
}

//상단 임직원 검색
var gTopUserSearchObj = null;
var gTopUserSearchTxt = null;
function searchUserTop(event, eventOpt, formId){
	if(gTopUserSearchObj==null){
		gTopUserSearchObj = $("#"+formId+" #topUserName").first();
		gTopUserSearchTxt = gTopUserSearchObj.attr("title");
	}
	if(eventOpt=='focus'){
		if(gTopUserSearchObj.val()==gTopUserSearchTxt){
			gTopUserSearchObj.val('');
		}
	} else if(eventOpt=='blur') {
		if(gTopUserSearchObj.val()==''){
			gTopUserSearchObj.val(gTopUserSearchTxt);
		}
	} else if(eventOpt=='keydown' || eventOpt=='click') {
		if(eventOpt=='keydown' && event!=null){
			if(event.keyCode!=13) return;
			if(event.preventDefault) event.preventDefault();
			if(browser.firefox){
				window.setTimeout("searchUserTop(null, 'click', 'topSearchUserForm')",5);
				return;
			}
		}
		var param = new ParamMap().getData(formId), count=null;
		if(gTopUserSearchTxt==param.get('userNm')) param.remove('userNm');
		if(formId=='topSearchUserForm' && param.get('userNm')!=null){
			callAjax('/or/user/getUserCountByNmAjx.do',param.toJSON(), function(data){
				count = data.count;
			});
		}
		if(count==1){
			if(browser.ieCompatibility){
				window.setTimeout('viewUserPop(null, "'+param.get('userNm').replace('"', '\"')+'")',10);
			} else {
				viewUserPop(null, param.get('userNm'));
			}
		} else {
			if(browser.ieCompatibility){
				window.setTimeout('dialog.open("searchUserDialog", "'+gTopUserSearchTxt.replace('"', '\"')+'", "/or/user/searchUserPop.do?'+param.toQueryString()+'")', 10);
			} else {
				dialog.open('searchUserDialog', gTopUserSearchTxt, "/or/user/searchUserPop.do?"+param.toQueryString());
			}
		}
		if(formId=='topSearchUserForm') gTopUserSearchObj.val(gTopUserSearchTxt);
	}
}//상단 통합검색
var gTopIntgSearchObj = null;
var gTopIntgSearchTxt = null;
function searchIntgTop(event, eventOpt, formId){
	if(gTopIntgSearchObj==null){
		gTopIntgSearchObj = $("#"+formId+" #topIntgName").first();
		gTopIntgSearchTxt = gTopIntgSearchObj.attr("title");
	}
	if(eventOpt=='focus'){
		if(gTopIntgSearchObj.val()==gTopIntgSearchTxt){
			gTopIntgSearchObj.val('');
		}
	} else if(eventOpt=='blur') {
		if(gTopIntgSearchObj.val()==''){
			gTopIntgSearchObj.val(gTopIntgSearchTxt);
		}
	} else if(eventOpt=='keydown' || eventOpt=='click') {
		if(eventOpt=='keydown' && event!=null){
			if(event.keyCode!=13) return;
			if(event.preventDefault) event.preventDefault();
		}
		var $form = $('#'+formId);
		$form.attr('action', '/sh/index.do');
		$('#'+formId).submit();
	}
}//form - submit 방지
var gDoNotSubmitHandler = null;
function doNotSubmit(event, inputHandler){
	if(inputHandler==null){
		if(event.preventDefault) event.preventDefault();
		return false;
	} else {
		if(event.keyCode!=13) return;
		if(event.preventDefault) event.preventDefault();
		if(browser.firefox){
			gDoNotSubmitHandler = inputHandler;
			window.setTimeout("doNotSubmitHandler()",5);
			return;
		} else {
			inputHandler();
		}
	}
	return false;
}
function doNotSubmitHandler(){
	if(gDoNotSubmitHandler!=null) gDoNotSubmitHandler();
	gDoNotSubmitHandler = null;
}
//사진보기
function viewImageDetl(userUid, userImgTypCd){
	var popTitle = callMsg('or.jsp.setOrg.viewImageTitle');
	dialog.open('viewImageDialog', popTitle, '/or/user/viewImagePop.do?userUid='+userUid+'&userImgTypCd='+userImgTypCd);
}//사진수정
function setMyImagePop(userImgTypCd){
	var popTitle = callMsg('or.jsp.setOrg.'+(userImgTypCd=='01' ? 'stampTitle' : userImgTypCd=='02' ? 'signTitle' : 'photoTitle'));
	dialog.open('setUserImageDialog', popTitle, '/or/user/setMyImagePop.do?userImgTypCd='+userImgTypCd);
}//사진수정 후 처리
function setUserImage(userUid, userImgTypCd, path, width, height){
	$("#"+userUid+" #orImage"+userImgTypCd).each(function(){
		var $img = $(this);
		$img.attr("src", path);
		$img.parent().attr("href","javascript:viewImageDetl('"+userUid+"','"+userImgTypCd+"');");
		
		if($img.attr("data-maxWidth")!=null){
			$img.attr("width", Math.min(parseInt($img.attr("data-maxWidth"),10), width)+"px");
		}
		if($img.attr("data-maxHeight")!=null){
			$img.attr("height", Math.min(parseInt($img.attr("data-maxHeight"),10), height)+"px");
		}
	});
	dialog.close('setUserImageDialog');
}//사용자조회
function viewUserPop(userUid, userNm){
	var popTitle = callMsg('or.jsp.viewUserPop.title');
	var param = userUid!=null ? 'userUid='+userUid : 'userNm='+encodeURIComponent(userNm);
	dialog.open('viewUserDialog', popTitle, '/or/user/viewUserPop.do?'+param);
}//메일 보내기
function mailToPop(email){
	var arrs = []; arrs.push(email);
	emailSendPop({recvVal:arrs},null , "/cm");
}//페이지 리로드
function reload(href){
	if(href==null) href = location.href;
	location.replace(href);
}//해당 프레임 리로드
function reloadFrame(id, url){
	try { getIframeContent(id).reload(url); }
	catch(e){ $("#"+id).attr('src', url); }
}
//색상
function showColorPop(id ,handler){
	var $chart = $("#"+id+"ColorPopArea");
	var html = $chart.html();
	if($chart.html()=="" || $chart.css("display")=="none"){
		if($chart.html()==""){
			$chart.html(callHtml("/cm/util/colorPop.do"));
			$chart.find("td").bind("click",function(){
				var color = $(this).attr("bgcolor");
				$("#"+id).css('color',color).val(color);
				$("#"+id+"ColorPopArea").hide();
				try{if(handler != null) eval(handler+"('"+id+"')");}catch(e){}
			});
		}
		$chart.show();
	} else {
		$chart.hide();
	}
}

//주소검색
function findZipCodePopup( id , menuId, frameId){
	var url = '/cm/findZipCodePop.do?menuId='+menuId+'&adrId='+id.replace('ZipBtn','');
	if(frameId!=undefined && frameId!=null && frameId != '') url+= '&frameId='+frameId;
	parent.dialog.open('zipCodePopup', callMsg('cm.btn.zipNo'), url);
};

//주소 직접입력
function setZipCodePopup(id , menuId, frameId){
	var url = '/cm/setZipCodePop.do?menuId='+menuId+'&adrId='+id.replace('ZipBtn','');
	if(frameId!=undefined && frameId!=null && frameId != '') url+= '&frameId='+frameId;
	parent.dialog.open('zipCodePopup', callMsg('cm.btn.zipNoMng'), url);
};
//프린트 + 미리보기
function printWeb(){
	if(browser.ie && top==window){ // IE 일경우
		var previewPrint = false;
		callAjax('/cm/getPreviewPrintAjx.do', null, function(data){ // 인쇄미리보기 여부 체크
			message = data.message;
			if(data.previewEnable != null){
				previewPrint = data.previewEnable=='Y'; 
			}
		}, null, function(){}, false);
		if(previewPrint){
			try{
		        //웹 브라우저 컨트롤 생성 - IE 5.5 이상에서만 동작
				var ocxTag = '<OBJECT ID="previewOcx" WIDTH="0" HEIGHT="0" CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
		        //웹 페이지에 객체 삽입
				document.body.insertAdjacentHTML('beforeEnd', ocxTag);
				var previewObj = document.getElementById('previewOcx');
		        //ExexWB 메쏘드 실행 (7 : 미리보기 , 8 : 페이지 설정 , 6 : 인쇄하기(대화상자))
				previewObj.ExecWB(7, 1);
				previewObj.outerHTML = "";
			}catch (e) {
				window.print();
			    //alert("- 도구 > 인터넷 옵션 > 보안 탭 > 신뢰할 수 있는 사이트 선택\n   1. 사이트 버튼 클릭 > 사이트 추가\n   2. 사용자 지정 수준 클릭 > 스크립팅하기 안전하지 않은 것으로 표시된 ActiveX 컨트롤 (사용)으로 체크\n\n※ 위 설정은 프린트 기능을 사용하기 위함임");
			}
		} else{
			window.print();
		}
	} else {
		window.print();
	}
}

//이메일 사전정보 저장 및 팝업
function emailSendPop(param , menuId , callAjxPrefix){

	var message = '';
	var mailEnable = '';
	callAjax('/cm/getVirtualUserAjx.do', null, function(data){
		message = data.message;
		if(data.sysPlocMap != null){
			var sysPlocMap = $.parseJSON(data.sysPlocMap);
			if(sysPlocMap != null && sysPlocMap.mailEnable != null) mailEnable = sysPlocMap.mailEnable;
		}
	});

	if(mailEnable == null || mailEnable == '' || mailEnable != 'Y' ){
		//이메일 서비스를 제공하지 않습니다.
		alertMsg('em.msg.not.email.service');
		return;
	}
	
	if(message!=''){
		alert(message);
		return;
	}
	
	var isLoc = false;
	if(param!=null)
		param = $.parseJSON(JSON.stringify(param));
	
	if(callAjxPrefix == null || callAjxPrefix =='') callAjxPrefix = ".";
	var url = callAjxPrefix+'/transEmailAjx.do';
	
	if(menuId != null && menuId != '') url+= '?menuId='+menuId;
	if(callAjxPrefix == "."){
		var urlParam = JSON.stringify(param);
		url+='&'+urlParam.replace('{','').replace('}','').replaceAll(',', '&').replaceAll(':','=' ).replaceAll('"','' );
	}
	
	callAjax(url, param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		
		if (data.result == 'ok') {
			if(!isLoc){
				//window.open('/cm/zmailPop?emailId='+data.emailId);
				openWindow('/cm/zmailPop.do?emailId='+data.emailId,'_blank',900,700,'resizable=yes,status=yes,toolbar=no,menubar=no,scrollbars=yes');	
			}else{
				dialog.open('sendEmailPop',callMsg('cm.btn.emailSend'),'/cm/sendEmailPop.do?menuId='+menuId+'&emailId='+data.emailId);
			}
		}
	});
}


//이메일 사전정보 저장 및 팝업(a link) 임시
function emailSendLink(param , menuId ){
	var arrs = []; arrs.push(param);
	emailSendPop({recvVal:arrs},menuId , "/cm");
};

var mainCntBlink = {
	ids:null, orginColor:null, blinkColor:null, index:0, count:12, duration:650,
	startBlink:function(){
		var $blinkObj = $("#"+this.ids[0]);
		this.orginColor = $blinkObj.css('color');
		this.index = 0;
		this.blinkColor = gLoutType=="B" ? "#bbff43" : "#e6b2a0";
		this.blink();
	},
	blink:function(){
		if(this.index % 2 == 0){
			$("#"+this.ids.join(", #")).css("color", this.blinkColor);
		} else {
			$("#"+this.ids.join(", #")).css("color", this.orginColor);
		}
		this.index++;
		if(this.index < this.count){
			window.setTimeout("mainCntBlink.blink()", this.duration);
		}
	}
};
//메인 건수 새로고침
function mainRefreshCnt(opt){
	
	var bxIds=null, apMenuMap=null, applyCachedCnt=false, uidMap={};
	if(opt!=null && opt['needApBxCnt']==true){
		bxIds = getApCountBxIds();
		apMenuMap = getApCountMenuMap(bxIds);
		if(apMenuMap != null){
			applyCachedCnt = displayApCachedBxCount(bxIds, apMenuMap, uidMap);
		}
	}
	var cntIds = ['topMailCnt','topAppCnt','topSchdlCnt','topApvMailCnt','topAdditionalCnt'];
	callAjax('/cm/getMainCntAjx.do', {hasApMenu:(apMenuMap==null?'N':'Y'), applyCachedCnt:(applyCachedCnt?'Y':'N'), uid:(uidMap['uid']==null?'':uidMap['uid'])}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		mainCntBlink.ids = [];
		var $cntObj = null, cntTxt;
		if(data.result=='ok' && data.rsltMap != null){
			var rsltMap = JSON.parse(JSON.stringify(data.rsltMap));
			for(var i=0;i<cntIds.length;i++){
				if(rsltMap[cntIds[i]]==null){
					$('.'+cntIds[i]+'Cls').hide();
					continue;
				}
				$cntObj = $('#'+cntIds[i]);
				cntTxt = $cntObj.text();
				if(cntTxt!=null) cntTxt = cntTxt.trim();
				if(rsltMap[cntIds[i]]!='0' && cntTxt!=null && cntTxt!='' && cntTxt!=rsltMap[cntIds[i]]){
					mainCntBlink.ids.push(cntIds[i]);
				}
				$cntObj.text(rsltMap[cntIds[i]]);
				if(cntIds[i]=='topApvMailCnt'){
					$('.topAdditionalCntCls').hide(); // 겸직 건수 hidden
					$('.'+cntIds[i]+'Cls').show();
					break;
				}
			}
			if(rsltMap['addtionalUrl']!=null){
				$('#topAdditionalCnt').parent().attr('href', rsltMap['addtionalUrl']);
			}
			if(rsltMap['uid'] != null){
				displayApBxCount(bxIds, apMenuMap, rsltMap);
				setApCachedBxCountMap(bxIds, rsltMap);
			} else {
				if(apMenuMap != null){
					setApCachedBxCount("waitBx", rsltMap["topAppCnt"]);
				}
			}
			
			if(mainCntBlink.ids.length>0){
				mainCntBlink.startBlink();
			}
		} else {
			for(var i=0;i<cntIds.length;i++){
				$('#'+cntIds[i]).text("0");
			}
		}
	}, function(){}, function(){}, true);
};

function getApCountBxIds(){
	var bxIds = ['waitBx','ongoBx','apvdBx','myBx','rejtBx','postApvdBx','drftBx','deptBx','refVwBx','recvBx'];
	return bxIds;
}
function displayApCachedBxCount(bxIds, apMenuMap, uidMap){
	if(apMenuMap != null){
		var cachedCountMap = getApCachedCountMap(bxIds, 5, uidMap);
		if(cachedCountMap != null){
			var menu, text, va;
			bxIds.each(function(index, bxId){
				menu = apMenuMap[bxId];
				if(menu!=null){
					text = $(menu).text();
					if(text.indexOf(' (')>0) text = text.substring(0, text.indexOf(' ('));
					va = cachedCountMap[bxId];
					if(va==null || va=='') $(menu).text(text);
					else $(menu).text(text+" ("+va+")");
				}
			});
			return true;
		}
	}
	return false;
}
function setApCachedBxCountMap(bxIds, countMap){
	if(window.sessionStorage){
		window.sessionStorage["apCnt_lastTime"] = new Date().getTime();
		var va;
		bxIds.each(function(index, bxId){
			va = countMap[bxId];
			window.sessionStorage["apCnt_"+bxId] = va==null ? (bxId=='recvBx' ? '' : 0) : va;
		});
		if(countMap['uid']!=null && countMap['uid']!=''){
			window.sessionStorage["apCnt_uid"] = countMap['uid'];
		}
	}
}
function setApCachedBxCount(bxId, count){
	if(window.sessionStorage){
		window.sessionStorage["apCnt_"+bxId] = (count==null ? '' : count);
		var bxIds = getApCountBxIds();
		var apMenuMap = getApCountMenuMap(bxIds);
		if(apMenuMap!=null){
			var menu = apMenuMap[bxId];
			if(menu!=null){
				var text = $(menu).text();
				if(text.indexOf(' (')>0) text = text.substring(0, text.indexOf(' ('));
				if(count==null || (count!=0 && count=='')) $(menu).text(text);
				else $(menu).text(text+" ("+count+")");
			}
		}
	}
}
function displayApBxCount(bxIds, apMenuMap, countMap){
	if(countMap != null){
		var menu, va;
		bxIds.each(function(index, bxId){
			menu = apMenuMap[bxId];
			if(menu!=null){
				text = $(menu).text();
				if(text.indexOf(' (')>0) text = text.substring(0, text.indexOf(' ('));
				va = countMap[bxId];
				if((va==null || va=='') && bxId=='recvBx'){
					$(menu).text(text);
				} else {
					$(menu).text(text+" ("+(va==null ? 0 : va)+")");
				}
			}
		});
	}
}
function getApCountMenuMap(bxIds){
	var apMenuMap = {}, href, p, q, bxId, hasBx = false;
	$("#leftMenuArea a").each(function(){
		href = $(this).attr("href");
		if(href!=null && href.startsWith("/ap/box/listApvBx.do")){
			p = href.indexOf('bxId=');
			q = href.indexOf('&', p);
			bxId = q>0 ? href.substring(p+5, q) : href.substring(p+5);
			if(bxIds.contains(bxId)){
				apMenuMap[bxId] = this;
				hasBx = true;
			}
		}
	});
	return hasBx ? apMenuMap : null;
}
function getApCachedCountMap(bxIds, validTimeGap, uidMap){
	if(window.sessionStorage){
		var apCntLastTime = window.sessionStorage["apCnt_lastTime"];
		if(validTimeGap==null || apCntLastTime==null) return null;
		var timeGap = parseInt(new Date().getTime()) - parseInt(apCntLastTime);
		if(timeGap < (validTimeGap * 60 * 1000)){
			var va, cachedMap = {};
			bxIds.each(function(index, bxId){
				va = window.sessionStorage["apCnt_"+bxId];
				if(!((va==null || va=='') && bxId=='recvBx')){
					cachedMap[bxId] = (va==null) ? 0 : va;
				}
			});
			va = window.sessionStorage["apCnt_uid"];
			if(va!=null && va!='') uidMap['uid'] = va;
			return cachedMap;
		}
	}
	return null;
}
function removeApCachedCountMap(){
	if(window.sessionStorage){
		window.sessionStorage.removeItem("apCnt_lastTime");
	}
}
// 겸직 결재 건수 팝업
function openAdditionalApWaitBx(){
	var popTitle = callMsg('pt.top.selAddiUser');
	dialog.open("selAddiUserDialog", popTitle, "/cm/ap/setApAddiUserPop.do");
}
// for editor unload event
var unloadEvent = {
	editorIds:[],
	timeout:null,
	editorType:"jelly",
	addEditor:function(editorId, editorType){
		if(browser.ie && browser.ver<11){
			//window.setTimeout('unloadEvent.add("'+editorId+'");', 30);
		} else {
			unloadEvent.add(editorId, editorType);
		}
	},
	add:function(editorId, editorType){
		var i, idx = -1;
		for(i=0; i<unloadEvent.editorIds.length; i++){
			if(unloadEvent.editorIds[i]==editorId) idx = i;
		}
		if(idx==-1){
			unloadEvent.editorIds.push(editorId);
		}
		if(editorType!=null) this.editorType = editorType;
		if(this.editorType=='namo'){
			window.setTimeout('unloadEvent.enable();', 1000);
		} else {
			if(window.onbeforeunload == null){
				window.onbeforeunload = unloadEvent.unload;
			}
		}
	},
	removeEditor:function(editorId){
		var i, idx = -1;
		for(i=0; i<unloadEvent.editorIds.length; i++){
			if(unloadEvent.editorIds[i]==editorId) idx = i;
		}
		if(idx>=0){
			unloadEvent.editorIds = unloadEvent.editorIds.splice(idx,1);
			if(unloadEvent.editorIds.length==0) window.onbeforeunload = null;
		}
	},
	unload:function(){
		for(var i=0;i<unloadEvent.editorIds.length;i++){
			if(unloadEvent.editorIds[i] != null){
				if(editor(unloadEvent.editorIds[i])!=null && editor(unloadEvent.editorIds[i]).isChanged()){
					return callMsg('cm.cfrm.leaveChange');
				}
			}
		}
		window.onbeforeunload = null;
	},
	enable:function(){
		window.onbeforeunload = unloadEvent.unload;
	},
	disable:function(time){
		window.onbeforeunload = null;
		if(time!=null){
			if(unloadEvent.timeout!=null){
				window.clearTimeout(unloadEvent.timeout);
			}
			unloadEvent.timeout = window.setTimeout('unloadEvent.enable();', time);
		}
	}
};
// DM 사용여부 또는 문서보내기 사용여부
function isSendEnable(){
	var message = '';
	var docSendEnable = 'Y';//시스템설정 완료되면 '' 로 변경
	/*callAjax('/cm/getVirtualUserAjx.do', null, function(data){
		message = data.message;
		if(data.sysPlocMap != null){
			var sysPlocMap = $.parseJSON(data.sysPlocMap);
			if(sysPlocMap != null && sysPlocMap.docSendEnable != null) docSendEnable = sysPlocMap.docSendEnable;
		}
	});*/

	if(docSendEnable == null || docSendEnable == '' || docSendEnable != 'Y' ){
		//문서보내기 서비스를 제공하지 않습니다.
		alertMsg('dm.msg.not.send.service');
		return false;
	}
	
	if(message!=''){
		alert(message);
		return false;
	}
	return true;
}
//문서로 보내기 - 옵션 [fldId[폴더ID],clsId[분류체계ID-콤마구분],seculCd[보안등급코드],docKeepPrdCd[보존연한코드],ownrUid[소유자UID]]
function sendDocOptPop(callback, param){
	var url = '/cm/doc/setSendOptPop.do';
	if(param != null && param != undefined) url+= '?'+param.toQueryString();
	url+= (url.indexOf('?') > -1 ? "&" : "?")+"callback="+callback;
	dialog.open('setSendOptDialog',callMsg('dm.jsp.sendDoc.title'),url);
}
// 문서로 보내기 - 등록팝업
function sendDocWritePop(param){
	var url = '/cm/doc/setSendWritePop.do';
	if(param != null && param != undefined) url+= '?'+param.toQueryString();
	dialog.open('setSendWritePop', callMsg('dm.jsp.sendWrite.title'), url);
};
// form 초기화[id:컨테이너,chkIds:초기화 제외할 id]
function valueReset(id, chkIds){
	if(id == undefined) return;
	var $area = $('#'+id);
	if($area == undefined) return;
	$area.find("input,select").each(function(){
		if(chkIds != null && $(this).attr('id') != undefined && chkIds.contains($(this).attr('id'))) return true;
		if($(this).prop("tagName") == 'SELECT'){
			if($(this).find('option') != undefined) $(this).val($(this).find('option').eq(0).val());
			$(this).uniform.update();
		}else $(this).val('');
	});
}
// jellyEditor - namoEditor 호환 적용
function editor(id){
	if(unloadEvent.editorType == "namo"){
		return namoEditor[id];
	} else {
		return jellyEditor(id);
	}
}
var namoEditor = {};
function setNamoEditor(id, object){
	namoEditor[id] = new NamoDelegator(id, object);
}
function NamoDelegator(id, crossEditor){
	this.id = id;
	this.crossEditor = crossEditor;
	this.isChanged = function(){
		return this.crossEditor.IsDirty();
	};
	this.getHtml = function(){
		return this.crossEditor.GetBodyValue();
	};
	this.setHtml = function(html){
		return this.crossEditor.SetBodyValue(html);
	};
	this.prepare = function(){
		$('#'+this.id).val(this.crossEditor.GetBodyValue());
		this.crossEditor.SetDirty();
	};
	this.setInitHtml = function(html){
		this.crossEditor.SetBodyValue(html);
		this.crossEditor.SetDirty();
	};
	this.clean = function(){
		namoEditor[this.id] = null;
	};
	this.empty = function(){
		var text = this.crossEditor.GetTextValue().trim();
		return text == "";
	};
	this.setConfig = function(){
		//alert('not support here !');
	};
	this.writeEditor = function(areaId){
		alert('not support here !');
	};
	this.resize = function(){
	};
}

function returnTrue(){ return true; }
function returnFalse(){ return false; }
function copyEnable(){ toggleCopy(true); }
function copyDisable(){ toggleCopy(false); }
function toggleCopy(flag){
	if(flag){
		document.oncontextmenu = returnTrue;
		if(browser.ie && browser.ver < 11){
			$(document).unbind("keydown keyup keypress");
		} else {
			$(document).unbind("copy cut");
		}
	} else {
		document.oncontextmenu = returnFalse;
		if(browser.ie && browser.ver < 11){
			$('body').bind("keydown keyup 	",function(e) {
				var evt = e||window.event;
				var cKey = 67, xKey = 88;
				var ctrlDown = evt.ctrlKey||evt.metaKey;
				if (ctrlDown && (evt.keyCode==cKey || evt.keyCode==xKey)){
					if(evt.preventDefault) evt.preventDefault();
					return false;
				}
				return true;
			});
		} else {
			$(document).bind("copy cut",function(evt) {
				evt = evt||window.event;
				if(evt.preventDefault) evt.preventDefault();
				return false;
			});
		}
	}
}

//파일업로드 후 콜백(팝업)
function saveFileToPop(url, param){
	if(param != null && param != undefined) url+= (url.indexOf('?') > -1 ? "&" : "?")+param;
	dialog.open('setUploadProgressDialog',callMsg('cm.title.upload'),url); // cm.title.upload=파일 업로드
}

// 업로더맵
var uploaderMap=new ParamMap();

function fileUploader(){
	this.id = null;
	this.fileId = null;
	this.uploadArea = 'uploadArea';
	this.fileListArea = 'fileListArea';
	this.progressArea = 'fileListArea';
	this.isFileUpload = false; // 업로드 진행여부
	this.maxSize = null; // 전체 파일 사이즈[설정에서 가져옴] 
	this.fileSize = 0; // 현재파일사이즈
	this.fileListMap = null; // 목록맵
	this.fGrpNo = 0; // 그룹번호
	this.fMaxNo = 0; // 파일번호
	this.fileSizeMap = null; // 첨부사이즈맵
	this.delListMap = null; // 삭제목록맵
	this.xhr = null; //xhr 배열(파일요청건별생성)
	this.xhrLen = 0; // xhr 진행건수
	this.async=false; // 파일 업로드 비동기여부[false:순차저장, true:비동기저장]
	this.maxSize = null; // 첨부제한 사이즈(전체)
	this.maxSizeUrl=null; // 첨부제한 사이즈 URL
	this.module=null; // 모듈
	this.isUniform=false;	 // 유니폼 적용여부
	this.isPop=false; // 팝업전송여부
	this.callback=null; // 콜백
	this.form=null; // 폼
	this.isHidden=false; // 파일목록 숨김여부
	this.uploadUrl=null;
	this.paramQueryString=null;
	this.isProcessLoading=false; // 파일업로드 처리 중일때 전체화면 인디케이터 작동여부
	this.statusMsgs=null; // 상태메세지 목록
	this.init = function(options) {
		//this.fileSize = 0; // 현재파일사이즈
		//this.fGrpNo = 0; // 그룹번호
		//this.fMaxNo = 0; // 파일번호
		//this.fileSizeMap = null;
		
		var uploader=this;
		var fileId=this.fileId;
		if(options==undefined) return;
		if(options['id']!=undefined) this.id=options['id'];
		if(fileId==null && options['fileId']!=undefined){
			fileId=options['fileId'];
			this.fileId=options['fileId'];
		}
		if(options['drag']!=undefined && options['drag']){
			$('.dropZone').on("dragover", this.handleDragOver); 
			$('.dropZone').on("drop", function(event){
				uploader.handleFileSelect(event, fileId);
			});
		}
		if(options['module']!=undefined) this.module=options['module'];
		if(options['maxSizeUrl']!=undefined) {
			this.maxSizeUrl=options['maxSizeUrl'];
			this.setMaxSize();
		}
		if(options['isUniform']!=undefined) this.isUniform=options['isUniform'];
		if(this.isUniform) this.setUniform();
		if(options['isPop']!=undefined) this.isPop=options['isPop'];
		if(options['isHidden']!=undefined) this.isHidden=options['isHidden'];
		if(options['fileListArea']!=undefined) this.fileListArea=options['fileListArea'];
		if(options['uploadUrl']!=undefined) this.uploadUrl=options['uploadUrl'];
		if(options['paramQueryString']!=undefined) this.paramQueryString=options['paramQueryString'];
		if(options['isProcessLoading']!=undefined) this.isProcessLoading=options['isProcessLoading'];
		if(options['async']!=undefined) this.async=options['async'];
		
		// 파일 업로드 창 메세지 다국어 로드
		if(this.statusMsgs==null){
			var success=callMsg('cm.btn.save'); // 저장
			var error=callMsg('cm.msg.error'); // 오류
			this.statusMsgs={success:success, error:error};
		}
	};	
	this.setMultiFileDisp = function(va, file, aLink, isOld, inputHtml, isHidden){ // 멀티 파일 이름 생성 [extsTyp='A':허용,extsTyp='L':제한]
		if(va==null) va = file.name;
		if (va==undefined || va=='') return;	
		if(isOld==undefined || isOld==null) isOld = false;
		if(inputHtml==undefined || inputHtml==null) inputHtml = null;
		if(isHidden==undefined || isHidden==null) isHidden = false;
		var $last = $('#'+this.progressArea).find('.fileItem:last');
		var $clone = $last.clone();
		
		var p = va.lastIndexOf('\\');
		if (p > 0) va = va.substring(p + 1);
		if(aLink!=null)	$last.find('.title').html(aLink);
		else $last.find('.title').text(va);
		$last.find('input[name="'+this.id+'_valid"]').val('Y');
		p = va.lastIndexOf('.');
		if (p > 0) {
			var ext = va.substring(p + 1);
			if (extArr.contains(ext)) {
				var ico = $last.find('.file_icon');
				ico.addClass(ext);
			}
		}
		// 파일사이즈
		if(file.size!=undefined){
			$last.find('.file_size').text(bytesToSize(file.size));
			// 파일사이즈맵 추가
			if(this.fileSizeMap==null) this.fileSizeMap = new ParamMap();
			// 파일 개별단위로 추가
			if(!isOld) this.fileSizeMap.put('fileList_'+this.fGrpNo+'-item'+this.fMaxNo, file.size);
			//$last.find('.fileKb').val(file.size);
		}
		if(isOld && file.grpNo!=undefined && file.seqNo!=undefined){
			$last.find('input[name="'+this.id+'_fGrpNo"]').val(file.grpNo);
			$last.find('input[name="'+this.id+'_fSeqNo"]').val(file.seqNo);
			$last.attr('id', $last.attr('id').replace('fileList', 'fileList_'+file.grpNo));
		}else{
			$last.find('input[name="'+this.id+'_fGrpNo"]').val(this.fGrpNo);
			$last.find('input[name="'+this.id+'_fSeqNo"]').val(this.fMaxNo);
			$last.attr('id', $last.attr('id').replace('fileList', 'fileList_'+this.fGrpNo));
		}
		if(inputHtml!=null){
			$last.append(inputHtml);
		}
		$clone.insertAfter($last);
		if(!isHidden) $last.removeClass('dispNone');//$last.show();
		if(this.isUniform) setUniformCSS($last);
	};
	this.chkDelFiles = function(key, index){ // 삭제된 파일 체크
		if(this.delListMap==null || this.delListMap.get(key+'-item'+index)==null) return false;
		return true;
	};
	this.uploadAbort = function(cnt, popId){ // 업로드 취소[cnt:업로드인덱스]
		if(this.xhr==null) return;
		if(cnt!=null) this.xhr[cnt].abort();
		else{
			$.each(this.xhr, function(index, xhr){
				xhr.abort();
			});	
		}
		this.isFileUpload=false;
		if(popId!=undefined) dialog.close(popId);
	};
	this.formSubmit = function(cnt){ // 폼 실행
		if(!this.isFileUpload) return;
		if(this.form!=null && this.xhrLen==cnt) {
			//this.fileListInit();
			//this.isFileUpload=false;
			this.form.submit();
			dialog.close('setUploadProgressDialog');
			this.uploaderRemove();
			return;
		}
		if(this.callback!=null && this.xhrLen==cnt){
			//this.fileListInit();
			//this.isFileUpload=false;
			eval(this.callback)();
			this.uploaderRemove();
		}
	};
	this.uploaderRemove = function(){
		uploaderMap.remove('uploader'+this.id);
	};
	this.fileListInit = function(){ // 파일 정보 초기화
		this.fileListMap=null;
		this.delListMap=null;
	};
	this.fileSizeInit = function(){ // 파일 사이즈 초기화
		this.fileSize = 0; // 현재파일사이즈
		//this.fGrpNo = 0; // 그룹번호
		//this.fMaxNo = 0; // 파일번호
		//this.fileSizeMap = null;
	};
	this.getUploadList = function(){ // 업로드할 목록 조회
		var uploader=this;
		var uploadList = [];
		if(this.fileListMap==null) return null;
		this.fileListMap.each(function(key, va){
			$.each(va, function(index, file){
				if(uploader.chkDelFiles(key, index)) return true;
				uploadList.push({key:key,index:index,file:file});
			});
		});
		return uploadList;
	};
	this.handleFileSelect = function(evt, id) {//드랍 이벤트
	    evt.stopPropagation();
	    evt.preventDefault();
	    setMultiFileList(evt.originalEvent.dataTransfer);
	    //if(browser.chrome || browser.opera) $('#'+id).prop('files', evt.originalEvent.dataTransfer.files);
		//else setMultiFile(evt.originalEvent.dataTransfer);
		
	};
	this.handleDragOver = function(evt) {// 드래그 이벤트
		evt.stopPropagation();
	    evt.preventDefault();
	    //evt.originalEvent.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
	};
	this.setMaxSize = function(url){ // 첨부 사이즈 조회
		var uploader=this;
		callAjax(this.maxSizeUrl, {module:this.module}, function(data) {
			if (data.result == 'ok' && data.maxMegaBytes != null) {
				var maxMegaBytes=data.maxMegaBytes;
				uploader.maxSize=maxMegaBytes*1024*1024;
				$('#maxSizeMsg').text(bytesToSize(uploader.maxSize));
			}
		});
	};
	this.updateSize = function(){ // 파일 사이즈 업데이트
		if(this.maxSize!=null && this.maxSize<this.fileSize) this.fileSize = this.maxSize;
		$('#fileSizeMsg').text(bytesToSize(this.fileSize));
	};
	this.setUniform = function(){
		$("#fileList, .upload_header").children().each(function(){
			if($(this).css('display') != 'none'){
				setUniformCSS(this);
			}
		});
	};
	this.fileUpload = function(){ // 파일 업로드
		var uploadList = this.getUploadList();
		if(uploadList==null || uploadList.length==0) {
			//if(form!=undefined) form.submit();	
			return;
		}
		this.processLoading(true); // 인디케이터
		this.isFileUpload=true;
		this.xhr = [];
		// 업로드목록, xhr 배열, 시작카운트, 비동기여부, 폼
		this.upload(uploadList, this.xhr, 0, this.async);
	};
	this.getFileListHtml = function(){
		return $('#'+this.fileListArea).html();
		
	};
	this.processLoading = function(isLoad, obj){
		if(this.isProcessLoading){
			if(obj===undefined) obj=$(document.body);
			loading(obj, isLoad);
		}
	};
	this.upload = function(uploadList, xhr, cnt, async){ // 파일 업로드[파일목록, request배열, 배열번호, 비동기화여부]
		var uploader = this;
		var isNext = uploadList.length>cnt+1;
		var listObj = $('#'+uploader.fileListArea).find('.fileItem#'+uploadList[cnt].key).eq(uploadList[cnt].index);
		var progressObj = $('#'+uploader.progressArea).find('.fileItem#'+uploadList[cnt].key).eq(uploadList[cnt].index).find('.status');
		var newXhr=xhr[cnt]=new XMLHttpRequest();
		//upload the file
		newXhr.open("post", uploader.uploadUrl, true);
		newXhr.upload.addEventListener("progress", function (event) {
			if (event.lengthComputable) {
				$(progressObj).find('img').eq(0).css("width",(event.loaded / event.total) * 100 + "%");
			}else {
				alertMsg('cm.msg.upload.processError', ['Failed to compute file upload length']);// cm.msg.upload.processError=업로드 도중 오류가 발생 하였습니다.({0})
				uploader.processLoading(false); // 인디케이터
			}
		}, false);
		
		newXhr.onreadystatechange = function(e) {
			if (newXhr.readyState === 4){
				if(e.target.responseText==undefined || e.target.responseText=='') {
					uploader.isFileUpload=false;
					return;
				}
				var data = $.parseJSON(e.target.responseText);
				if (newXhr.status === 200){
		            if (data.model.message!=null) {
		            	alert(data.model.message);
		            	newXhr.abort();
		            	uploader.isFileUpload=false;
		            	uploader.processLoading(false); // 인디케이터
		            	return;
		            }
		            $(progressObj).find('img').eq(0).hide();
					$(progressObj).removeClass('progress');
					$(progressObj).text(uploader.statusMsgs['success']);
					// 임시파일ID를 폼에 append
					listObj.find('input[name="tmpFileId"]').val(data.model.tmpFileId);
					uploader.xhrLen++;
					if(data.model.message==null && isNext && !async){
						cnt++;
						uploader.upload(uploadList, xhr, cnt, async);
					}
					uploader.formSubmit(uploadList.length);
				}else{
					if (data.model.message!=null) {
						alert(data.model.message);
						uploader.isFileUpload=false;
						uploader.processLoading(false); // 인디케이터
					}
				}
			}
			//$('#messageTest').append('<div>'+newXhr.readyState+'</div>');
	    };
	    newXhr.addEventListener("error", function(e){
	    	alertMsg('cm.msg.upload.processError', ['Failed to file upload']);// cm.msg.upload.processError=업로드 도중 오류가 발생 하였습니다.({0})
	    	uploader.processLoading(false); // 인디케이터
	    });
		var uploaderForm = new FormData(); // Create new FormData
		uploaderForm.append(uploader.fileId, uploadList[cnt].file); // append the next file for upload
		if(uploadList[cnt].file.fileId!=undefined)
			uploaderForm.append('fileId', uploadList[cnt].file.fileId);
		// Send the file (doh)
		newXhr.send(uploaderForm);
		// 비동기
		if(isNext && async){
			cnt++;
			uploader.upload(uploadList, xhr, cnt, async);
		}
	};
	
	this.renderView = function(id){
		
	};
}

// 업로더 가져오기
function getUploader(id){
	return uploaderMap.get('uploader'+id);
}
// 업로더 맵에 담음
function setUploader(id){	
	uploaderMap.put('uploader'+id, new fileUploader());
}
// 업로더 생성및 조회
function newUploader(id, isNew){
	if(isNew==undefined) isNew = false;
	if(isNew || getUploader(id)==null){
		setUploader(id);
	}
	return getUploader(id);
}

//파일업로드 후 폼 저장
function saveFileToForm(id, form, callback, noSubmit){
	var uploader=getUploader(id);
	if(uploader != null){
		if(!uploader.isFileUpload) {
			var list=uploader.getUploadList(); // 업로드할 파일목록
			if(list!=null && list.length>0){
				if(form!=undefined && form!=null) uploader.form=form;
				if(callback!=undefined && callback!=null) uploader.callback=callback;
				if(uploader.isPop){
					uploader.progressArea='progressArea';
					saveFileToPop('/cm/setUploadProgressPop.do?'+uploader.paramQueryString, 'uploaderId='+id);
				}else uploader.fileUpload(); 
			}else{
				if(callback!=undefined && callback!=null){
					eval(callback)();
					return;
				}
				if(form!=undefined && form!=null && noSubmit!=true) form.submit();
			}
		}else alertMsg("cm.msg.upload.progress"); // cm.msg.upload.progress=파일 업로드가 진행중입니다.
	}else{
		if(callback!=undefined && callback!=null){
			eval(callback)();
			return;
		}
		if(form!=undefined && form!=null && noSubmit!=true) form.submit();
	}
}

function sizeToBytes(size){ // 파일 사이즈를 바이트로 변환
	if(size==undefined) size = '1 KB';
	var sizes=size.split(' ');
	if(sizes[0]==0) return 0;
	return sizes[1]=='MB' ? sizes[0]*1024*1024 : sizes[0]*1024;
};
function bytesToSize(bytes, isUnit){ // 파일 사이즈 체크
	if(bytes==undefined) bytes=1024;
	if(isUnit==undefined) isUnit=true;
	var fileSizeKB=bytes/1024;
	var fileSizeMB=bytes/(1024*1024);
	var fileSizeTemp=fileSizeKB+(1-(fileSizeKB%1))%1;
	var fileSize=fileSizeTemp > 999 ? fileSizeMB+(1-(fileSizeMB%1))%1 : fileSizeTemp;
	var fileSizeUnit=fileSizeTemp > 999 ? 'MB' : 'KB';
	return fileSize+(isUnit ? ' '+fileSizeUnit : '');
};
// 멀티파일 지원여부
function isMultiFile(){
	return window.FileReader && window.File && window.FileList && window.Blob ? true : false;
}
// css 로드
function loadCSS(url) {
    if (!$('link[href="' + url + '"]').length)
        $('head').append('<link rel="stylesheet" type="text/css" href="' + url + '">');
}
//메뉴ID 구하기
function getMenuIdByUrl(url){
	var menuId = '';
	callAjax('/cm/secu/getMenuIdAjx.do', {url:url}, function(data) {
		menuId = data.menuId;
	});
	return menuId;
}
//SNS 올리기
function snsUpload(sns, url, text){
	var width = sns=='twitter' ? 640 : 555;
    var height = sns=='twitter' ? 440 : 520;
    var top = (screen.availHeight / 2) - (height / 2);
    var left = (screen.availWidth / 2) - (width / 2);
    var opt ="location=0, status=0, scrollbars=0,resizeable=0, width="+ width +", height="+ height +", top=" + top + ", left=" + left;
	var encodeUrl = encodeURIComponent(url);
	if(sns=='twitter'){
		window.open('https://twitter.com/intent/tweet?url='+encodeUrl+(text!='' ? '&text='+encodeURIComponent(text) : ''), 'twitter', opt);
	}else if(sns=='facebook'){
		window.open('http://www.facebook.com/sharer/sharer.php?u='+encodeUrl, 'facebook', opt);
	}else if(sns=='naverband'){
		window.open('http://band.us/plugin/share?body='+text+' '+encodeUrl, 'naverband', opt);
	}else if(sns=='kakaostory'){
		window.open('https://story.kakao.com/share?url='+encodeUrl, 'kakaostory', opt);
	}else if(sns=='instargram'){
		window.open('https://www.instagram.com/', 'instargram', opt);
	}	
}
var countRefreshUtil = {
		on:false, extTime:(20*60*1000), needApBxCnt:false, mailDomain:null,
		setRefresh:function(apBxCnt, durationSec){
			this.needApBxCnt = apBxCnt;
			this.extTime = durationSec * 1000;
			if(!this.on){
				this.on = true;
				this.refresh(false);
			}
		},
		refresh:function(runNow){
			if(!this.on) return;
			if(runNow==true){
				mainRefreshCnt({needApBxCnt:this.needApBxCnt});
			}
			if(this.mailDomain!=null){
				$.ajax({
					url: this.mailDomain+"/zmail/empty.jsp",
					dataType: 'jsonp',
					jsonpCallback: "doNothing",
					success: function(data) {},
					error: function(xhr) {}
				});
			}
			window.setTimeout('countRefreshUtil.refresh(true)', this.extTime);
		}
	};
function doNothing(){}
/*
var pcNotiUtil = {
		cycleMiliSec: 20 * 60 * 1000,
		initAttempts: 0,
		timeoutId: null,
		uid:null,
		debug: window.sessionStorage!=null && sessionStorage.getItem('pcNotiDebug')=='Y',
		init: function(){
			if (window['Notification'] === undefined) {
				if (pcNotiUtil.initAttempts < 18) {
					pcNotiUtil.initAttempts++;
					setTimeout('pcNotiUtil.init()', 333);
				} else {
					pcNotiUtil.log('not installed');
					pcNotiUtil.timeoutId = window.setTimeout('pcNotiUtil.refresh()', 60 * 1000);
				}
			} else {
				Notification.requestPermission(function(permission){});
				if(pcNotiUtil.debug && !pcNotiUtil.isGranted()) pcNotiUtil.log('permission denied');
				pcNotiUtil.timeoutId = window.setTimeout('pcNotiUtil.refresh()', 100);
			}
		},
		setRefresh: function(sec){
			pcNotiUtil.cycleMiliSec = sec * 1000;
			if(browser.ie){
				$(document).ready(function() {
					pcNotiUtil.init();
				});
			} else {
				pcNotiUtil.init();
			}
		},
		refresh: function(){
			
			if(pcNotiUtil.timeoutId != null){
				window.clearTimeout(pcNotiUtil.timeoutId);
				pcNotiUtil.timeoutId = null;
			}
			
			var timeObj = pcNotiUtil.storage();
			if(timeObj!=null){
				if(timeObj.pcNotiSkip){
					pcNotiUtil.timeoutId = window.setTimeout(pcNotiUtil.refresh, !pcNotiUtil.isGranted() ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec);
					pcNotiUtil.log(null, 'skip', null, (!pcNotiUtil.isGranted() ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec));
					return;
				}
				if(timeObj.pcNotiLocalTm != ''){
					var timeGap = new Date().getTime() - parseInt(timeObj.pcNotiLocalTm);
					var minInterval = Math.min(pcNotiUtil.cycleMiliSec, 60000);
					if(timeGap < minInterval){
						pcNotiUtil.timeoutId = window.setTimeout(pcNotiUtil.refresh, Math.min(minInterval, (minInterval-timeGap) + 1000));
						pcNotiUtil.log(null, 'time gap', null, (Math.min(minInterval, (minInterval-timeGap) + 1000)));
						return;
					}
				}
			}
			
			var param = {pcNotiServerTm: timeObj==null ? '' : timeObj.pcNotiServerTm};
			if(!pcNotiUtil.isGranted() || timeObj==null){
				param['permission'] = 'denied';
			} else {
				param['host'] = location.protocol+"//"+location.hostname+(location.port=="" ? "" : ":"+location.port);
			}
			if(pcNotiUtil.debug == true) param['debug'] = 'Y';
			
			callAjax('/cm/getPcNoti.do', param, function(data){
				
				if(data.list != null){
					data.list.each(function(index, obj){
						pcNotiUtil.alertNoti(obj.get('title'), obj.get('body'), obj.get('url'));
					});
				}
				
				pcNotiUtil.storage({pcNotiLocalTm: new Date().getTime(), pcNotiServerTm: data.pcNotiServerTm, pcNotiUid: data.pcNotiUid});
				pcNotiUtil.timeoutId = window.setTimeout(pcNotiUtil.refresh, !pcNotiUtil.isGranted() || data.pcNotiServerTm=='' ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec);
				pcNotiUtil.log(null, data.pcNotiMode, data.pcNotiServerTm, (!pcNotiUtil.isGranted() || data.pcNotiServerTm=='' ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec));
			}, null, function(){
				pcNotiUtil.timeoutId = window.setTimeout(pcNotiUtil.refresh, !pcNotiUtil.isGranted() ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec);
				pcNotiUtil.log(null, 'error', null, (!pcNotiUtil.isGranted() ? 20 * 60 * 1000 : pcNotiUtil.cycleMiliSec));
			}, true);
		},
		log:function(msg, mode, serverTm, next){
			if(!pcNotiUtil.debug) return;
			var d = new Date();
			var h = d.getHours();
			var m = d.getMinutes();
			var s = d.getSeconds();
			var tm = (h<10?"0":"")+h+":"+(m<10?"0":"")+m+":"+(s<10?"0":"")+s;
			var logMsg = (mode!=null ? ' ['+mode+']' : '') + (serverTm!=null ? ' serverTm: '+serverTm : '') + (next!=null ? ' next: '+next : '') + (msg!=null ? ' - '+msg : '');
			console.log(tm+' pcNotiUtil'+logMsg);
		},
		setDebug:function(mode){
			if(mode==null || mode==true){
				sessionStorage.setItem('pcNotiDebug', 'Y');
				pcNotiUtil.debug = true;
			} else if(mode==false){
				sessionStorage.removeItem('pcNotiDebug');
				pcNotiUtil.debug = false;
			}
		},
		storage: function(data){
			if(window['sessionStorage']  === undefined){
				return null;
			}
			if(data == null){
				var pcNotiLocalTm = sessionStorage.getItem('pcNotiLocalTm');
				var pcNotiServerTm = sessionStorage.getItem('pcNotiServerTm');
				var pcNotiUid = localStorage.getItem('pcNotiUid');
				var pcNotiLastTm = localStorage.getItem('pcNotiLastTm');
				if(pcNotiLocalTm == null){
					return {pcNotiLocalTm: '', pcNotiServerTm: ''};
				}
				if(pcNotiUtil.uid!=null && pcNotiUtil.uid==pcNotiUid && pcNotiServerTm!='' && pcNotiServerTm!=pcNotiLastTm){
					sessionStorage.setItem('pcNotiServerTm', pcNotiLastTm);
					return {pcNotiSkip: true};
				}
				return {pcNotiLocalTm: pcNotiLocalTm, pcNotiServerTm: pcNotiServerTm};
			} else {
				sessionStorage.setItem('pcNotiLocalTm', data.pcNotiLocalTm);
				sessionStorage.setItem('pcNotiServerTm', data.pcNotiServerTm);
				if(data.pcNotiUid != null){
					pcNotiUtil.uid = data.pcNotiUid;
					localStorage.setItem('pcNotiUid', data.pcNotiUid);
					localStorage.setItem('pcNotiLastTm', data.pcNotiServerTm);
				}
				return null;
			}
		},
		isGranted: function (){
			return window['Notification'] !=null && window['Notification'].permission == 'granted';
		},
		alertNoti: function(title, body, url){
			if(browser.ie){
				if(!pcNotiUtil.isGranted()) return;
				if(title==null || body==null || url==null) return;
				var notification = new Notification(title, {
					body: body,
					icon: location.protocol+"//"+location.hostname+(location.port=="" ? "" : ":"+location.port)+"/images/cm/fcmBell.png",
					data: url
				});
				notification.addEventListener('click', function(event) {
					window.focus();
					window.open(url, '_blank');
				});
			} else {
				var notification = new Notification(title, {
					body: body,
					icon: "/images/cm/fcmBell.png",
					data: url
				});
				notification.addEventListener('click', function(event) {
					event.preventDefault();
					notification.close();
					window.open(url, '_blank');
				});
			}
		}
	};
*/
var webPushUtil = {
		initAttempts: 0,
		timeoutId: null,
		runningId: null,
		runningMarkTm: 3900,
		runningCheckTm: 4000,
		runningOn: false,
		abortOn: false,
		uid: null,
		oid: null,
		port: null,
		xhr: null,
		msgIds: null,
		errCount: 0,
		debug: window.sessionStorage!=null && sessionStorage.getItem('webPushDebug')=='Y',
		startWebPush: function(uid, oid, port){
			webPushUtil.uid = uid;
			webPushUtil.oid = oid;
			if(port!='') webPushUtil.port = port;
			webPushUtil.init();
		},
		init: function(){
			if (window['Notification'] === undefined) {
				if (webPushUtil.initAttempts < 24) {
					webPushUtil.initAttempts++;
					setTimeout('webPushUtil.init()', 250);
				} else {
					webPushUtil.log('error', 'not installed');
				}
			} else {
				Notification.requestPermission(function(permission){});
				if(!webPushUtil.isGranted()){
					webPushUtil.log('error', 'permission denied');
				} else {
					webPushUtil.timeoutId = window.setTimeout('webPushUtil.prepare()', 10);
				}
			}
		},
		doStop: function(){
			webPushUtil.abortOn = true;
			if(window['localStorage']  !== undefined){
				localStorage.setItem('abortTm', new Date().getTime());
			}
		},
		prepare: function(){
			webPushUtil.timeoutId = null;
			var obj = webPushUtil.storage();
			if(obj==null){
				webPushUtil.log('error', 'not supported');
				return;
			}
			if(obj.localUid==null || parseInt(obj.localUid)!=parseInt(webPushUtil.uid) || obj.localTm==null || (new Date().getTime() - parseInt(obj.localTm))>webPushUtil.runningCheckTm ){
				if(webPushUtil.debug){
					var msg = obj.localUid==null ? 'localUid null' :
						parseInt(obj.localUid)!=parseInt(webPushUtil.uid) ? 'localUid != uid'
							: obj.localTm==null ? 'localTm null'
								: (new Date().getTime() - parseInt(obj.localTm))>webPushUtil.runningCheckTm ? 'timeGap > checkTm'
									: null;
					if(msg!=null) webPushUtil.log('prepare', msg);
				}
				webPushUtil.setRunnigData();
				webPushUtil.runningId = window.setTimeout('webPushUtil.markRunning()', webPushUtil.runningMarkTm);
				webPushUtil.runningOn = true;
				webPushUtil.queryWebPush();
			} else {
				webPushUtil.log('prepare', 'waiting');
				webPushUtil.runningId = window.setTimeout('webPushUtil.checkRunning()', webPushUtil.runningCheckTm);
			}
		},
		setRunnigData:function(){
			var tm = new Date().getTime();
			webPushUtil.storage({sessionTm:tm, localTm:tm, localUid:webPushUtil.uid});
		},
		markRunning:function(){
			webPushUtil.log('check', 'running');
			webPushUtil.runningId = null;
			var obj = webPushUtil.storage();
			if(webPushUtil.abortOn){
				webPushUtil.log('check', 'mark abort-on');
				webPushUtil.abort();
			} else if(parseInt(obj.localUid)==parseInt(webPushUtil.uid) && parseInt(obj.sessionTm)==parseInt(obj.localTm)){
				webPushUtil.setRunnigData();
				webPushUtil.runningId = window.setTimeout('webPushUtil.markRunning()', webPushUtil.runningMarkTm);
			} else {
				webPushUtil.uid = obj.localUid;
				webPushUtil.storage({sessionTm:obj.localTm});
				webPushUtil.runningOn = false;
				webPushUtil.abort();
				webPushUtil.runningId = window.setTimeout('webPushUtil.checkRunning()', webPushUtil.runningCheckTm);
			}
		},
		checkRunning:function(){
			webPushUtil.log('check', 'waiting');
			webPushUtil.runningId = null;
			var obj = webPushUtil.storage();
			if(webPushUtil.abortOn){
				webPushUtil.log('check', 'check abort-on');
			} else if(obj.sessionTm==null || parseInt(obj.sessionTm)!=parseInt(obj.localTm) || parseInt(obj.localUid)!=parseInt(webPushUtil.uid)){
				webPushUtil.uid = obj.localUid;
				webPushUtil.storage({sessionTm:obj.localTm});
				webPushUtil.runningId = window.setTimeout('webPushUtil.checkRunning()', webPushUtil.runningCheckTm);
			} else {
				webPushUtil.setRunnigData();
				webPushUtil.runningId = window.setTimeout('webPushUtil.markRunning()', webPushUtil.runningMarkTm);
				webPushUtil.runningOn = true;
				webPushUtil.queryWebPush();
			}
		},
		abort:function(){
			webPushUtil.log('query', 'abort');
			if(webPushUtil.xhr != null && webPushUtil.xhr.readyState != 4){
				webPushUtil.xhr.abort();
				webPushUtil.xhr = null;
	        }
		},
		queryWebPush: function(){
			webPushUtil.log('query', 'query push');
			if(webPushUtil.timeoutId != null){
				window.clearTimeout(webPushUtil.timeoutId);
				webPushUtil.timeoutId = null;
			}
			
			var needJSONP = !browser.ie && browser.secure;
			
			webPushUtil.xhr = $.ajax({
				url:webPushUtil.getHost()+'/Apps/webPush/queryWebPush.do',
				async:true,
				cache:false,
			    dataType: needJSONP ? 'jsonp' : 'json',
				type:"GET",
				data:"data="+encodeURIComponent(JSON.stringify({uid: webPushUtil.uid, oid:webPushUtil.oid, debug:webPushUtil.debug ? 'Y' : ''})),
				timeout:900000,
				success:function(data){
					webPushUtil.xhr = null;
					
					if(webPushUtil.abortOn){
						webPushUtil.abortOn = true;
						webPushUtil.log('query', 'abort-on');
						return;
					}
					
					if(data.list != null){
						data.list.each(function(index, obj){
							if(obj!=null) webPushUtil.log('result', 'title:'+obj.title+', body:'+obj.body+', url:'+host+obj.url);
							if(browser.ie && obj.url!=null && !obj.url.startsWith('http')){
								var host = location.protocol+"//"+location.hostname+(location.port=="" ? "" : ":"+location.port);
								webPushUtil.alertNoti(obj.title, obj.body, host+obj.url);
							} else {
								webPushUtil.alertNoti(obj.title, obj.body, obj.url);
							}
						});
					}
					
					if(webPushUtil.abortOn){
						webPushUtil.log('query', 'next abort-on');
					} else if(webPushUtil.runningOn){
						var nextTime = 500 + parseInt(Math.random() * 2000);
						if(webPushUtil.debug){
							webPushUtil.log('query', 'next:'+nextTime);
						}
						webPushUtil.timeoutId = window.setTimeout(webPushUtil.queryWebPush, nextTime);
					} else {
						webPushUtil.log('query', 'end query');
					}
					webPushUtil.errCount = 0;
				},
				error:function(xhr){
					webPushUtil.xhr = null;
					
					if(webPushUtil.abortOn){
						webPushUtil.log('query', 'fail(abort) and abort-on');
					} else if(webPushUtil.errCount>5){
						webPushUtil.log('query', 'fail(abort) and errCount over');
						webPushUtil.abortOn = true;
					} else if(webPushUtil.runningOn){
						webPushUtil.errCount++;
						webPushUtil.log('query', 'fail(abort) and retry : '+webPushUtil.errCount);
						webPushUtil.timeoutId = window.setTimeout(webPushUtil.queryWebPush, 5000);
					} else {
						webPushUtil.errCount++;
						webPushUtil.log('query', 'fail(abort) and check');
					}
				}
			});
		},
		getHost: function(){
			var host = "";
			if(webPushUtil.port != null){
				host = location.protocol+"//"+location.hostname+":"+webPushUtil.port;
			}
			return host;
		},
		log:function(mode, msg){
			if(!(webPushUtil.debug || mode=='error')) return;
			var d = new Date();
			var h = d.getHours();
			var m = d.getMinutes();
			var s = d.getSeconds();
			var tm = (h<10?"0":"")+h+":"+(m<10?"0":"")+m+":"+(s<10?"0":"")+s;
			var logMsg = (mode!=null ? ' ['+mode+']' : '') + (msg!=null ? ' - '+msg : '');
			console.log(tm+' webPushUtil'+logMsg);
		},
		setDebug:function(mode){
			if(mode==null || mode==true){
				sessionStorage.setItem('webPushDebug', 'Y');
				webPushUtil.debug = true;
			} else if(mode==false){
				sessionStorage.removeItem('webPushDebug');
				webPushUtil.debug = false;
			}
		},
		storage: function(data){
			if(window['sessionStorage']  === undefined){
				return null;
			}
			var abortTm = localStorage.getItem('abortTm');
			if(abortTm != null){
				var gapTm = new Date().getTime() - parseInt(abortTm);
				if(gapTm < webPushUtil.runningCheckTm * 2){
					webPushUtil.abortOn = true;
					return;
				} else {
					localStorage.removeItem('abortTm');
				}
			}
			if(data == null){
				var sessionTm  = sessionStorage.getItem('sessionTm');
				var localTm    = localStorage.getItem('localTm');
				var localUid   = localStorage.getItem('localUid');
				return {sessionTm:sessionTm, localTm:localTm, localUid:localUid};
			} else {
				if(data.sessionTm != null) sessionStorage.setItem('sessionTm', data.sessionTm);
				if(data.localTm   != null) localStorage.setItem('localTm', data.localTm);
				if(data.localUid  != null) localStorage.setItem('localUid', data.localUid);
				return null;
			}
		},
		isGranted: function (){
			return window['Notification'] !=null && window['Notification'].permission == 'granted';
		},
		alertNoti: function(title, body, url){
			if(!webPushUtil.checkValidMsgId(url)) return;
			
			if(browser.ie){
				if(!webPushUtil.isGranted()) return;
				if(title==null || body==null || url==null) return;
				var host = location.protocol+"//"+location.hostname+(location.port=="" ? "" : ":"+location.port);
				var notification = new Notification(title, {
					body: body,
					icon: host+"/images/cm/fcmBell.png",
					data: url
				});
				notification.addEventListener('click', function(event) {
					window.focus();
					window.open(url, '_blank');
				});
			} else {
				var notification = new Notification(title, {
					body: body,
					icon: "/images/cm/fcmBell.png",
					data: url
				});
				notification.addEventListener('click', function(event) {
					event.preventDefault();
					notification.close();
					window.open(url, '_blank');
				});
			}
		},
		checkValidMsgId:function(url){
			
			var p = url.indexOf('?msgId=');
			if(p<0) return true;
			var msgId = url.substring(p+7);
			
			if(webPushUtil.msgIds==null || webPushUtil.msgIds==''){
				webPushUtil.msgIds = msgId;
				return true;
			}
			
			var hasMsgId = false;
			var arr = webPushUtil.msgIds.split(',');
			arr.each(function(index, va){
				if(va == msgId){
					hasMsgId = true;
					return false;
				}
			});
			if(hasMsgId){
				webPushUtil.log('dup msgId', webPushUtil.msgIds+" / "+msgId);
				return false;
			}
			
			if(arr.length>3){
				webPushUtil.msgIds = webPushUtil.msgIds.substring(webPushUtil.msgIds.indexOf(',')+1)+','+msgId;
			} else {
				webPushUtil.msgIds = webPushUtil.msgIds+','+msgId;
			}
			
			return true;
		}
	};
// 로딩 - 인디케이터
function loading(obj, display, isSize, cls, handler){
	var target=typeof obj =='string' ? $('#'+obj) : $(obj);
	if(cls===undefined) cls='loading';
	if(display){
		var inner=$('<div class="'+cls+'"></div>');
		inner.prependTo(target);
		if(isSize){
			inner.css({width:target.width()+'px', height:target.height()+'px'});
			$( window ).resize(function() {
				inner.css({width:target.width()+'px', height:target.height()+'px'});
			});
		}
		if(handler) handler();
	}else target.find('.'+cls).eq(0).remove();
}
function enterSubmit(event) {
    event = event || window.event;
    if (event.keyCode === 13) {
    	var element = event.srcElement ? event.srcElement : event.target;
    	var form = getParentTag(element, 'form');
    	if(form!=null) form.submit();
    }
}//textarea height auto
function textareaAuto(obj){
	var target=typeof obj =='string' ? $('#'+obj) : $(obj);
	$(target).on('keyup', 'textarea', function (e){
		$(this).css({'height':'auto', 'resize': 'none', 'overflow-y': 'hidden', 'padding-bottom': '0.2em', 'line-height': '1.6'});
		if(this.scrollHeight>0) $(this).height( this.scrollHeight );
	});
	$(target).find('textarea').keyup();
}// [날짜] : 수동입력 onblur 시 날짜 형식 검증
function validMnalDate(obj, val){
	if(val===undefined) val=obj.value;
	if(val=='') return false;
	if(!((val.length==8 && /^(19\d{2}|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/.test(val)) || 
			(val.length==10 && /^(19\d{2}|20\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/.test(val)))){
		obj.value = "";
		return false;
	}
	return true;
}// [날짜] : 수동입력 유효성 검증 
function chkMnalCalendar(obj, handler, checkOpt){
	var val=obj.value;
	if(!(/^[0-9\-]*$/.test(val))){
		obj.value = "";
		return;
	}
	var isValid=(val.length==8 && /^[0-9]*$/.test(val)) || (val.length==10 && /^[0-9\-]*$/.test(val));
	if(!isValid) return;
	
	if(!validMnalDate(obj, val)) return;
	
	var isInput=false;
	if(/^(19\d{2}|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/.test(val)){
		val = RegExp.$1 +'-'+RegExp.$2+'-'+RegExp.$3;
		isInput=true;
	}
	
	//날짜 유효성검사
    if (!isValidDate(val)){
    	obj.value = "";
    	return;
    }
    if(handler==null || handler(val, checkOpt)){
		if(checkOpt!=undefined && checkOpt!=null){
			if(checkOpt['end']!=null){
				var $endCal = $('#'+checkOpt['end']);
				var endDt = $endCal.val();
				if(endDt!=null && endDt!='' && val>endDt){ $endCal.val(val); }
			}
			if(checkOpt['start']!=null){
				var $startCal = $('#'+checkOpt['start']);
				var startDt = $startCal.val();
				if(startDt!=null && startDt!='' && val<startDt){ $startCal.val(val); }
			}
			if(checkOpt['checkHandler']!=null){
				if(checkOpt['checkHandler'](val)) {obj.value=''; return;};
			}			
		}
		if(isInput) obj.value = val;
		if(checkOpt!=undefined && checkOpt!=null && checkOpt['onchange']!=null){
			checkOpt['onchange'](obj);
		}
	}    
    
}//날짜 유효성검사
function isValidDate(param) {
    try{
        param = param.replace(/-/g,'');
        // 자리수가 맞지않을때
        if( isNaN(param) || param.length!=8 ) {
            return false;
        }
        var year = Number(param.substring(0, 4));
        var month = Number(param.substring(4, 6));
        var day = Number(param.substring(6, 8));
        if( month<1 || month>12 ) {
            return false;
        }
        var maxDaysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        var maxDay = maxDaysInMonth[month-1];
         
        // 윤년 체크
        if( month==2 && ( year%4==0 && year%100!=0 || year%400==0 ) ) {
            maxDay = 29;
        }
         
        if( day<=0 || day>maxDay ) {
            return false;
        }
        return true;

    } catch (err) {
        return false;
    }                       
}
// 날짜 및 시간 변경시 히든값 변경
function chnDateTime(obj){
	if(obj===undefined) return;
	var id=obj.myId===undefined ? $(obj).attr('id') : obj.myId;
	if(id===undefined) return;
	id=id.replace(/Dt|Tm/g, '');
	if($('#'+id+'Dt').val()!='' && $('#'+id+'Tm').val()!='')
		$('#'+id).val($('#'+id+'Dt').val() + ' ' + $('#'+id+'Tm').val() + ':00');
}

// 시간선택 레이어 이벤트 처리
function setTimeSelectEvt(id){
	$('#'+id+'Area ul#selectTimeArea li').on({
		click: function(e){	
			$('#'+id+'Area div.input_select_list input[type="text"]').val($(e.target).text());	
			$(e.target).closest('ul.select_list').hide();
			$('#'+id+'Area div.input_select_list input[type="text"]').focus();
		},		
		keydown: function(e){
			var keyCode=e.keyCode;
			if(keyCode==38 || keyCode==40){
				var index=$('#'+id+'Area ul#selectTimeArea li').index($(e.target));
				if(keyCode==38 && index==0){
					$('#'+id+'Area div.input_select_list input[type="text"]').focus();
					$('#'+id+'Area ul#selectTimeArea').hide();
					return false;
				}
				var lastIndex=$('#'+id+'Area ul#selectTimeArea li').length;
				if(keyCode==40 && index==lastIndex-1)
					return false;
				if(keyCode==38) $(e.target).prev().focus();
				else if(keyCode==40) $(e.target).next().focus();
				else return false;				
				if(index<6) return false;
			}else if(keyCode==13) $(e.target).trigger('click');			
		},
		focus: function(e){
			$('#'+id+'Area ul#selectTimeArea li').css('background-color','');
			$(e.target).css('background-color','#f2f2f2');
		},
		mouseover: function(){
			$(this).css('background-color','#f2f2f2');
		},
		mouseout: function(){
			$(this).css('background-color','transparent');
		}
	});
	
	$('#'+id+'Area div.input_select_list input[type="text"]').on({		
		click: function(){
			$('ul#selectTimeArea').hide();
			$('#'+id+'Area ul#selectTimeArea').show();
		},
		keyup: function(e){
			if(e.keyCode==40){
				$('#'+id+'Area ul#selectTimeArea').show();
				$('#'+id+'Area ul#selectTimeArea li:first').focus();
			}			
		},
		blur: function(e){
			var val=$(e.target).val();
			if(val!='' && !(val.length==5 && /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(val))){
				$(e.target).val('');
				//return false;
			}
			if($(e.target).attr('data-onchange')!=''){
				eval($(e.target).attr('data-onchange'));
			}
		}
	});
	
	// 화면 클릭시 팝업 닫기
	$(document).click(function(event){
		if(!$(event.target).closest('div.input_select_list').length &&
		   !$(event.target).is('div.input_select_list')) {
			   if($('#'+id+'Area ul#selectTimeArea').is(":visible")) {
				$('#'+id+'Area ul#selectTimeArea').hide();
			}
		}
	});
	// 윈도우 리사이즈시 팝업 닫기
	$(window).resize(function(event){
		if(!$(event.target).closest('div.input_select_list').length &&
		   !$(event.target).is('div.input_select_list')) {
			   if($('#'+id+'Area ul#selectTimeArea').is(":visible")) {
				$('#'+id+'Area ul#selectTimeArea').hide();
			}
		}
	});
}

// 랜덤 문자열 만들기
function randomString(len){
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
	if(len===undefined) len=15; // 문자열 길이 기본값 15자리
	var random = '';
	for (var i=0;i<len; i++) {
		var rnum = Math.floor(Math.random() * chars.length);
		random += chars.substring(rnum,rnum+1);
	}
	return random;
}

// [나라셀라] - bizplay 사이트 팝업
function callBizplay(){
	callAjax('/cm/nara/getBizplayParamAjx.do', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.jsonData != null){
			var jsonData=data.jsonData;
			
			var url="https://www.bizplay.co.kr/BpCpldGateLogin";
			var $form = $('<form>', {
				'id':'bizplayWindow',
				'method':'post',
				'action':url,
				'target':'bizplay'
			}).append($('<input>', {
				'name':'JSONData',
				'value':jsonData,
				'type':'hidden'
			}));
			$(document.body).append($form);
			
			window.open('', "bizplay");
			$('#bizplayWindow').submit();
			$('#bizplayWindow').remove();		
			
		}
	});
	
};

//[나라셀라] - bizplay 사이트 팝업 - submit 방식
function popBizplay(){
	callAjax('/cm/nara/getBizplayRandomAjx.do', null, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.returnJson != null){
			var jsonData=data.returnJson;
			
			var url="https://www.bizplay.co.kr/BpCpldGate";
			
			var $form = $('<form>', {
				'id':'bizplayWindow',
				'method':'post',
				'action':url,
				'target':'bizplay'
			}).append($('<input>', {
				'name':'JSONData',
				'value':jsonData,
				'type':'hidden'
			}));
			$(document.body).append($form);
			
			window.open('', "bizplay");
			$('#bizplayWindow').submit();
			$('#bizplayWindow').remove();
			
		}
	});
	
};

function noChangeSelect(obj){
	if($(obj).attr('data-sIndex')==null){
		$(obj).attr('data-sIndex', obj.selectedIndex);
	} else {
		obj.selectedIndex = $(obj).attr('data-sIndex');
	}
}

// html 을 이미지로 다운( HTML => Canvas )
function htmlToCanvas(obj, options, fileName, beforeCallback, imgHdlCallBack){
	// Support Browser
	// Firefox 3.5+, Google Chrome, Opera 12+, IE9+, Edge, Safari 6+
	if(!(browser.chrome || browser.firefox || browser.safari || browser.opera || (browser.ie && browser.ver>=9))){ 
		alert('Not Browser Support!!');
		return;
	}
	if(options===undefined || options==null) options={};
	options['logging']=false; // 로깅 여부(브라우저 콘솔창)
	options['timeout']=2000; // 이미지 로드 제한시간(밀리초)
	//options['width']=500;
	//options['height']=200;
	//options['scale']=3; // 확대, 축소
	//options['backgroundColor']='#ffffff'; // 이미지의 background color
	var target=typeof obj =='string' ? $('#'+obj) : $(obj);
	
	if(beforeCallback!=null) beforeCallback(); // 이미지 다운로드 전 핸들러 호출
	
	options['onclone']=function(doc){ // background cellspacing 으로 border 를 표현할 경우 세로줄을 강제로 그려줌
		var tables=$(doc).find('table.listtable');
		if(tables.length>0){
			var backgroundColr='${_skin}'=='blue' ? '#bfc8d2' : '#c6c6c6';
			tables.css({'background-color' : backgroundColr, 'border-collapse':'collapse'});
			tables.find('td').css('border', '1px solid #bfc8d2');
		}
		// notImage 클래스를 가진 엘리먼트 숨김 처리
		$(doc).find('.notImage').css('display', 'none');
	};
		
	html2canvas(target[0], options).then(function(canvas) {
		var dataUrl=canvas.toDataURL("image/png");
		if(imgHdlCallBack!=null) imgHdlCallBack(dataUrl); // 이미지 핸들러[이미지 데이터로 후작업(미리보기) 할 경우]
        var $form = $('<form>', {
        	'id':'imgSaveForm',
    		'method':'post',
    		'action':'/cm/htmlToImg.do',
    		'target':'dataframe'
    		}).append($('<input>', {
    		'name':'data',
    		'value':dataUrl,
    		'type':'hidden'
    	}));
    	if(fileName != undefined) $form.append($('<input>', {'name':'fileName', 'value':fileName,'type':'hidden'}));
    	$('form[action="/cm/htmlToImg.do"]').remove();
    	$(document.body).append($form);
    	$form.submit();
	});
};

//이미지 마우스 이벤트 초기화
function initImgMouseEvt(selectContainer, tags, options, fileName, imgHdlCallBack){
	if(selectContainer==null) selectContainer='selectContainer'; 
	var evtArea=$('#'+selectContainer); // 전체 영역
	// 영역선택 다운로드 기능 활성화시 이벤트 부여
	$(document.body).on({
		mouseover:function(evt){ // 마우스 오버 시에 해당영역 경계선 표시
			evtArea.find('.selectArea').removeClass('selectArea'); // 선택한 영역 초기화
			if($(evt.target).closest('div#'+selectContainer).length) {
				// 선택할 수 있는 태그 목록[이름]
				if(tags==null) tags = [/* {name:'div', cls:'imgSelect'},  */{name:'table'}]; // 태그명, 아이디, 클래스{name:'table', id:'아이디', cls:'클래스명'}
				var findObj=null;
				var parentNm='';
				for(var key in tags){
					// 태그명
					if(tags[key].name!=undefined) parentNm=tags[key].name;
					// 태그 아이디, 클래스명
					if(tags[key].id!=undefined) parentNm+='#'+tags[key].id;
					else if(tags[key].cls!=undefined) parentNm+='.'+tags[key].cls;
					findObj=$(evt.target).closest(parentNm);
					if(findObj!=undefined){
						findObj.addClass('selectArea');
						break;
					}
				}
			}
			//if($(evt.target).is('div#'+selectContainer)){
			//	$(evt.target).addClass('selectArea'); // 영역 css class 추가
			//}
		},
		mouseout:function(evt){
			if(!($(evt.target).closest('div#'+selectContainer).length ||
				       $(evt.target).is('div#'+selectContainer))) {
				evtArea.find('.selectArea').removeClass('selectArea');
			}
		},
		click: function(evt){
			if($(evt.target).closest('a#selectAreaBtn').length || $(evt.target).is('a#selectAreaBtn')) // 버튼 영역 클릭 이벤트 제외 
				return;
			if($(evt.target).closest('div#'+selectContainer).length) { 
				htmlToCanvas(evtArea.find('.selectArea').eq(0), options, fileName, function(){ // 이미지 다운로드
					evtArea.find('.selectArea').removeClass('selectArea');
				}, imgHdlCallBack);
			}else{ // 이벤트 초기화
				$(document.body).off('mouseover mouseout click');
				evtArea.find('.selectArea').removeClass('selectArea');
				$(window).off('resize');
			}
			
		}
	});
	$(window).resize(function() {
		$(document.body).off('mouseover mouseout click');
		evtArea.find('.selectArea').removeClass('selectArea');
		$(window).off('resize');
	});
	
};

// 텍스트 공백제거
function txtTrim(obj){
	var target=typeof obj =='string' ? $('#'+obj) : $(obj);
	var va=target.val();
	if(va!='' && /\s/.test(va)){
		va=va.replace(/\s/gi, "");
		target.val(va);
	}
};

