var calendar = {
			skin : 'blue',
			area : null,
			myId : null,
			checkOpt : null,
			handler : null,
			weekLang : null,
			open : function(myId, checkOpt, handler){
				this.myId = myId;
				this.checkOpt = checkOpt;
				this.handler = handler;
				this.getWeekLang();
				this.draw(this.getMyDt());
			},
			draw : function(myDt){
				var $area = $('#'+this.myId+'CalArea');
				if($area.length>0){
					this.area = $area[0];
					var buffer = [];
					this.appendHead(myDt, buffer);
					this.appendBody(myDt, buffer);
					$area.html(buffer.join(''));
				}
			},
			getMyDt : function(){
				var myDt = $('#'+this.myId).val();
				return myDt==null || myDt=='' || myDt.length != 10 ? new Date() : new Date(parseInt(myDt.substring(0,4)), parseInt(myDt.substring(5,7),10)-1, parseInt(myDt.substring(8),10));
			},
			appendHead : function(myDt, buffer){
				buffer.push('<div class="calendar_nav"><div class="nav"><dl>\n');
				var y = myDt.getFullYear(), m = myDt.getMonth()+1;
				buffer.push('<dd class="cld_first" onclick="calendar.moveTo('+(y-1)+','+m+');"></dd>\n');
				buffer.push('<dd class="cld_prev" onclick="calendar.moveTo('+(m==1 ? y-1 : y)+','+(m==1 ? 12 : m-1)+');"></dd>\n');
				buffer.push('<dd class="cld_txt" onclick="calendar.getCalendarSelectorPop();"><span  id="yearSelector">'+y+'</span> / <span id="monthSelector">'+this.fm(m)+'</span></dd>');
				buffer.push('<dd class="cld_next" onclick="calendar.moveTo('+(m==12 ? y+1 : y)+','+(m==12 ? 1 : m+1)+');"></dd>\n');
				buffer.push('<dd class="cld_last" onclick="calendar.moveTo('+(y+1)+','+m+');"></dd>\n');
				buffer.push('</dl></div></div>\n');
			},
			getWeekLang : function(){
				var weekLangMsg = [], strWeekLang ="";
				weekLangMsg.push("cal.sun");
				weekLangMsg.push("cal.mon");
				weekLangMsg.push("cal.tue");
				weekLangMsg.push("cal.wed");
				weekLangMsg.push("cal.thu");
				weekLangMsg.push("cal.fri");
				weekLangMsg.push("cal.sat");
				for(var i=0; i<weekLangMsg.length; i++){
					var message = $m.msg.callMsg(weekLangMsg[i]);
					if(message!=null) strWeekLang += message;
					else strWeekLang += '.';
				}
				this.weekLang = strWeekLang;
			},
			appendBody : function(myDt, buffer){
				buffer.push('<div class="calendar_body"><div class="bodyin">\n');
				buffer.push('<dl class="weekdl">');
				this.weekLang.split('').each(function(idx, w){
					buffer.push('<dd class="'+(idx==0 ? 'week_sunday' : (idx==6 ?'week_saturday':'week'))+'">'+w+'</dd>');
				});
				buffer.push('</dl>\n');

				var step = 0, suffix = '_g';
				var dt = new Date(myDt.getFullYear(), myDt.getMonth(), 0);
				var i, end = dt.getDate();
				var y = dt.getFullYear(), m = this.fm(dt.getMonth()+1), d = end - dt.getDay();
				var curr = new Date();
				var today = curr.getFullYear()==y && curr.getMonth()==myDt.getMonth() ? curr.getDate() : '';
				var holi = 'off';
				var holiday = '';

				$m.ajax('/cm/transGetCalendarHolidayAjax.do', {year:y+'',month:m+'',holyYn:'Y'}, function(data){
					if(data.message!=null){
						holiday = data.message;
					} 
				});
				var index = 0;//주차
				while(step<3){
					buffer.push('<dl class="daydl">');
					for(i=0;i<7;i++){
						if(holiday.indexOf(y+''+m+''+((d<10)?'0'+d:d))>-1) holi = 'on';
						else holi = 'off';
						buffer.push('<dd onclick="calendar.setDate(\''+y+'-'+m+'-'+this.fm(d)+'\', this)" class="'+(i==0 ? 'sunday'+suffix : i==6 ? 'saturday'+suffix : holi=='on' ? 'sunday'+suffix : 'day'+suffix)+'">');
						
						if(step==1 && today==d) buffer.push('<div class="today">');
						else buffer.push('<div>');
						buffer.push(d);
						buffer.push('</div>');
						buffer.push('</dd>');
						
						d++;
						if(d>end){
							dt = step==0 ? myDt : new Date(myDt.getFullYear(), myDt.getMonth()+1, 1);
							y = dt.getFullYear();
							m = this.fm(dt.getMonth()+1);
							d = 1;
							end = this.getLastDate(y,m);
							suffix = step==0 ? '' : '_g';
							today = step!=0 ? '' : curr.getFullYear()==y && curr.getMonth()==dt.getMonth() ? curr.getDate() : '';
							step++;
						}
					}
					buffer.push('</dl>\n');
					if(d>end) break;					
					index++;
					if(index == 6) break;
				}
				buffer.push('</div></div>\n');
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
			setDate : function(date, obj){
				$('#'+this.myId).val(date);
				$('.today').attr("class","");
				$(obj).find('div').attr("class","today");
			},
			moveTo : function(year, month){
				this.draw(new Date(year, month-1, 1));
			},
			getCalendarSelectorPop : function(){
				myDt = this.getMyDt();
				var y = myDt.getFullYear(), m = myDt.getMonth()+1;
				$m.dialog.open({
					id:'getCalendarSelectorPop',
					url:'/cm/util/getCalendarSelectorPop.do?y='+y+'&m='+this.fm(m),
				});
			},
			fm : function(no){ return no>9 ? no : '0'+no; }
		};