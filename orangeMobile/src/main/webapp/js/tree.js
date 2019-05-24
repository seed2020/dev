
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
	img: { tree:"mtree", folder:"mtree_folder", 
		root:"mtree_org", app:"mtree_application", word:"mtree_document", 
		comp:"mtree_company", org:"mtree_organization", dept:"mtree_department", part:"mtree_part"},
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
		//if(type=='R'||type=='T'||type=='F'){
		//	path.push(".gif");
		//} else {
			path.push(".png");
		//}
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
		if($ul.children().length == 0) return;
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
	this.selected = null;
	this.onclick = 'clickTree';
	this.openLvl = 2;
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
		htmls.push('<nobr id="ROOTLI" data-exts=\'{"id":"ROOT"}\'><img src="'+TREE.getImage('R')+'" width="25" height="24"> <a href="javascript:;" onclick="TREE.select(\''+this.id+'\',\'ROOT\'); '+this.onclick+'(\'ROOT\',\''+escapeValue(root.name)+'\')" style="position:relative; top:-13px;">'+root.name+'</a></nobr>');
		this.drawUL(root, htmls, 1);
		this.treeDiv.html(htmls.join('\n'));
	};
	this.drawUL = function(node, htmls, openLvl){
		var i, size = node.children==null ? 0 : node.children.length;
		var ulStyle = (size>0 && openLvl!=null && openLvl<=this.openLvl) ? 'class="tree_menu"' : 'style="display:none"';
		htmls.push('<ul id="'+node.id+'UL" '+ulStyle+'>');
		for(i=0;i<size;i++){
			this.drawLI(node.children[i], htmls, (size-i)==1, openLvl);
		}
		htmls.push('</ul>');
	};
	this.drawLI = function(node, htmls, isLast, openLvl){
		var aTitle = (node.exts==null || node.exts.title==null) ? '' : ' title=\"'+node.exts.title+'\"';
		var treeOpen = (node.children==null || node.children.length==0) || (openLvl!=null && openLvl<this.openLvl);
		htmls.push('<li id="'+node.id+'LI"'+this.toDataExt('exts',node.exts)+this.toDataExt('rescs',node.rescs)+(isLast?' class="end"':'')+'>');
		htmls.push('<nobr>');
		htmls.push('<a href="javascript:;" class="control" onclick="TREE.toggle(\''+this.id+'\',\''+node.id+'\')"><img id="'+node.id+'T" src="'+TREE.getImage('T', treeOpen)+'" width="25" height="24" /></a>');
		htmls.push('<a href="javascript:;" onclick="TREE.toggle(\''+this.id+'\',\''+node.id+'\')"><img id="'+node.id+'F" data-type="'+node.type+'" src="'+TREE.getImage(node.type, false)+'" title="'+TREE.getIconTitle(node.type)+'" width="25" height="24" /></a>');
		htmls.push('<a href="javascript:;" onclick="TREE.select(\''+this.id+'\',\''+node.id+'\'); '+this.onclick+'(\''+node.id+'\',\''+escapeValue(node.name)+'\',\''+node.rescId+'\');"'+aTitle+' style="position:relative; top:-6px;">'+node.name+'</a>');
		if(node.exts!=null && node.exts.useYn=='N') htmls.push('<img data-type="'+node.type+'" src="/images/cm/del.gif" title="Not Used" />');
		htmls.push('</nobr>');
		this.drawUL(node, htmls, (openLvl==null ? null : openLvl+1));
		htmls.push('</li>');
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