<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
		import="java.util.ArrayList"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	
	// 담당 - 접수문서 담당의 경우
	if("makVw".equals(request.getParameter("makrRoleCd"))){
		request.setAttribute("makRole", "makVw");
		request.setAttribute("byOneRole", "byOne");
		
	// 합의부서 기안의 경우
	} else if(request.getParameter("apvLnPno") != null && !"0".equals(request.getParameter("apvLnPno"))){
		request.setAttribute("makRole", "makAgr");
		request.setAttribute("byOneRole", "byOneAgr");
		
	// 일반 기안의 경우
	} else {
		request.setAttribute("makRole", "mak");
		request.setAttribute("byOneRole", "byOne");
	}

%>
<script type="text/javascript">
<!--<%
// 결재 옵션 - json %>
var gOptConfig = ${optConfigJson};<%
// 사용자 정보 관리 항목 %>
var gAttrs = ["apvrRoleCd","dblApvTypCd","apvrDeptYn","apvrUid","apvrNm","apvrPositNm","apvrTitleNm","apvDeptId","apvDeptNm","apvDeptAbbrNm","absRsonCd","absRsonNm","apvStatCd","apvrDeptYn","fixdApvrYn"];<%
// 결재라인번호 %>
var gApvLnNo = "${apOngdApvLnDVoList[0].apvLnNo}";<%
// 양식결재라인구분코드 %>
var gFormApvLnTypCd = "${param.formApvLnTypCd}";<%
//용어설정 목록 %>
var apTermResc = {};<c:forEach items="${apTermList}" var="apTerm">
apTermResc["${apTerm[0]}"] = "<u:out value="${apTerm[1]}" type="script" />";</c:forEach>
apTermResc["byOneAgr"] = apTermResc["byOne"];
<%// 사용자/부서 - 추가,
/*	opt
		type:user/dept, 
		apvrRoleCd:결재자역할코드,
		dblApvTypCd:(이중결재-신청:reqDept,처리:prcDept),
		absRsonCd:부재사유코드,
		absRsonNm:부재사유명
*/%>
function addSelected(arr, opt){
	opt['dblApvTypCd'] = opt.apvLnSetupTypCd=='reqDept' || opt.apvLnSetupTypCd=='prcDept' ? opt.apvLnSetupTypCd : '';
	if(opt.type=='user'){
		var addCnt=0, msgs=[], msg;
		arr.each(function(index, obj){
			data = {};
			data["apvrRoleCd"] = opt.apvrRoleCd;<%// 결재구분 - 결재자역할코드 %>
			data["dblApvTypCd"] = opt.dblApvTypCd;<%// 이중결재 : 신청부서 / 주관부서%>
			data["apvrUid"] = obj.userUid;<%// 사용자UID%>
			data["apvrNm"] = obj.rescNm;<%// 사용자명%>
			data["apvrPositNm"] = obj.positNm==null ? "" : obj.positNm;<%// 직위%>
			data["apvrTitleNm"] = obj.titleNm==null ? "" : obj.titleNm;<%// 직책%>
			data["apvDeptId"] = obj.deptId;<%//부서ID%>
			data["apvDeptNm"] = obj.deptRescNm==null ? "" : obj.deptRescNm;<%//부서명%>
			data["apvDeptAbbrNm"] = "";<%//부서약어명%>
			data["absRsonCd"] = opt.absRsonCd==null ? "" : opt.absRsonCd;<%//부재사유코드%>
			data["absRsonNm"] = opt.absRsonNm==null ? "" : opt.absRsonNm;<%//부재사유명%>
			data["apvrDeptYn"] = "N";<%//결재자부서여부%>
			if((msg = checkAddValidation(data)) == null){
				addNewTr(data);
				addCnt++;
			} else {
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		});
		if(msgs.length>0){
			alert(msgs.join('\n'));
		}
		return (addCnt>0);
	} else if(opt.type=='dept'){
		var addCnt=0, msgs=[], msg;
		arr.each(function(index, obj){
			data = {};
			data["apvrRoleCd"] = opt.apvrRoleCd;<%// 결재구분 - 결재자역할코드 %>
			data["dblApvTypCd"] = opt.dblApvTypCd;<%// 이중결재 : 신청부서 / 주관부서%>
			data["apvrUid"] = "";<%// 사용자UID%>
			data["apvrNm"] = "";<%// 사용자명%>
			data["apvrPositNm"] = "";<%// 직위%>
			data["apvrTitleNm"] = "";<%// 직책%>
			data["apvDeptId"] = obj.orgId;<%//부서ID%>
			data["apvDeptNm"] = obj.rescNm;<%//부서명%>
			data["apvDeptAbbrNm"] = opt.orgAbbrRescNm==null ? "" : opt.orgAbbrRescNm;<%//부서약어명%>
			data["absRsonCd"] = "";<%//부재사유코드%>
			data["absRsonNm"] = "";<%//부재사유명%>
			data["apvrDeptYn"] = "Y";<%//결재자부서여부%>
			if((msg = checkAddValidation(data)) == null){
				addNewTr(data);
				addCnt++;
			} else {
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		});
		if(msgs.length>0){
			alert(msgs.join('\n'));
		}
		return (addCnt>0);
	}
}<%
// 경로그룹 - 선택한 것 추가 %>
function addApvLnGrp(arr){
	if(arr==null) return;
	var addCnt=0, msgs=[], msg;
	var isDbl = ("${param.formApvLnTypCd=='apvLnDbl' or param.formApvLnTypCd=='apvLnDblList' ? 'Y' : ''}"=="Y");<%// 이중결재 여부 %>
	var checkDeptId = "${param.apvLnPno!='0' ? sessionScope.userVo.deptId : ''}";
	var makRole = "${makRole}";
	arr.each(function(index, data){<%// psnInfm:개인통보, deptInfm:부서통보 %>
		if(isDbl && data['dblApvTypCd']=='' && !(data['apvrRoleCd']=='psnInfm' || data['apvrRoleCd']=='deptInfm')){
			msg = '<u:msg titleId="ap.msg.onlyDblApvLn" alt="이중결재 결재경로만 지정 할 수 있습니다." /> : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if(makRole == 'makVw' && !(data['apvrRoleCd']=='fstVw' || data['apvrRoleCd']=='pubVw' || data['apvrRoleCd']=='paralPubVw')){<%
			// ap.msg.onlyCan3="{0}", "{1}", "{2}" 만 지정 할 수 있습니다. %>
			msg = '${onlyCan3} : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if(!isDbl && data['dblApvTypCd']!=''){
			msg = '<u:msg titleId="ap.msg.notDblApvLn" alt="이중결재 결재경로는 지정 할 수 없습니다." /> : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if(checkDeptId != '' && data['apvrUid']==''){
			msg = '<u:msg titleId="ap.msg.deptAgrOnlyUser" alt="부서합의는 사용자만 지정 가능 합니다." /> : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);<%
		// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
		} else if(checkDeptId != '' && (data['apvrRoleCd']=='psnOrdrdAgr' || data['apvrRoleCd']=='psnParalAgr'
				|| data['apvrRoleCd']=='deptOrdrdAgr' || data['apvrRoleCd']=='deptParalAgr')){
			msg = '<u:msg titleId="ap.msg.deptAgrNoAgr" alt="부서합의에 합의를 지정 할 수 없습니다." /> : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);<%
		// postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보 %>
		} else if(checkDeptId != '' && (data['apvrRoleCd']=='postApvd' || data['apvrRoleCd']=='psnInfm'
			|| data['apvrRoleCd']=='deptInfm')){<%
			// ap.msg.deptAgrNotInfm=부서합의에 "{0}", "{1}" 은(는) 지정 할 수 없습니다. %>
			msg = '${deptAgrNotInfm} : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if(checkDeptId != '' && data['apvDeptId']!=checkDeptId){
			msg = '<u:msg titleId="ap.msg.deptAgrDeptId" alt="부서합의는 동일 부서 사용자만 가능 합니다." /> : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if(makRole != 'makVw' && (data['apvrRoleCd']=='fstVw' || data['apvrRoleCd']=='pubVw' || data['apvrRoleCd']=='paralPubVw')){<%
			// ap.msg.onlyCanNot3="{0}", "{1}", "{2}" 은(는) 지정 할 수 없습니다. %>
			msg = '${onlyCanNot3} : '+(data['apvrNm']=='' ? data['apvDeptNm'] : data['apvrNm']);
			if(!msgs.contains(msg)) msgs.push(msg);
		} else if((msg = checkAddValidation(data)) == null){
			addNewTr(data);
			addCnt++;
		} else {
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	});
	if(msgs.length>0){
		alert(msgs.join('\n'));
	}
	return (addCnt>0);
}
<%
// 추가용 - 신규 TR 생성 %>
function addNewTr(data){
	var param = new ParamMap(data);
	var $tr = addEmptyTr();
	$check = $tr.find("input[type='checkbox']");
	$check.val(data["apvrUid"]!='' ? data["apvrUid"] : data["apvDeptId"]);
	param.each(function(key, va){<%// checkbox 의 추가 attribute 에 데이터 저장 %>
		$check.attr("data-"+key, va);
	});
	if(param.get("fixdApvrYn")=='Y'){
		$check[0].disabled = true;
	} else {
		$check.bind("keydown", function(event){
			if(event.which==13) $(this).trigger("click");
		});
	}
	$check.uniform();<%
	
	// 화면 각 TD에 표시%>
	$tds = $tr.children();
	$($tds[1]).text(data.apvDeptNm);<%// 부서명%>
	if(data.apvrUid!="" && data.apvrDeptYn != "Y"){
		$($tds[2]).find("a").attr("href", "javascript:parent.viewUserPop('"+data.apvrUid+"');").text(data.apvrNm);<%// 사용자명%>
		$($tds[3]).text(data.apvrPositNm);<%// 직위명%>
		$($tds[4]).text(data.apvrTitleNm);<%// 직책명%>
	} else {
		$($tds[2]).find("a").text("");<%// 사용자명%>
		$($tds[3]).text("");<%// 직위명%>
		$($tds[4]).text("");<%// 직책명%>
	}
	setApvTypeTdTxt($check);
	$tr.show();
	adjustInsertLoc($tr);
	return true;
}<%
// 결재자 추가 위치 조절 - 추가한 사용자/부서 를 해당 위치까지 위로 올림 %>
function adjustInsertLoc($tr){
	var apvrRoleCd = $tr.find("input[type='checkbox']").attr('data-apvrRoleCd');
	if(apvrRoleCd=='psnInfm' || apvrRoleCd=='deptInfm'){
		return;
	}
	var myUid = parent.getMyTurnUid();
	var $prevTr, $prevCk, prevApvrRoleCd, prevUid;
	if(!(gFormApvLnTypCd == 'apvLnDbl' || gFormApvLnTypCd == 'apvLnDblList')){
		while(true){
			$prevTr = $tr.prev();
			$prevCk = $prevTr.find("input[type='checkbox']");
			prevUid = $prevCk.val();
			prevApvrRoleCd = $prevCk.attr('data-apvrRoleCd');
			
			if(prevApvrRoleCd=='psnInfm' || prevApvrRoleCd=='deptInfm'){
				$prevTr.before($tr);
			} else if(apvrRoleCd=='postApvd'){
				break;
			} else if(prevUid == myUid){
				break;
			} else if(apvrRoleCd=='entu'){
				if(prevApvrRoleCd=='postApvd'){
					$prevTr.before($tr);
				} else {
					break;
				}
			} else if(apvrRoleCd=='apv' || apvrRoleCd=='pred'){
				if(prevApvrRoleCd=='entu' || prevApvrRoleCd=='postApvd'){
					$prevTr.before($tr);
				} else {
					break;
				}
			} else {
				if(prevApvrRoleCd=='apv' || prevApvrRoleCd=='pred' || prevApvrRoleCd=='entu' || prevApvrRoleCd=='postApvd'){
					$prevTr.before($tr);
				} else {
					break;
				}
			}
		}
	} else {
		var dblApvTypCd = $tr.find("input[type='checkbox']").attr('data-dblApvTypCd');
		var prevAblApvTypCd;
		while(true){
			$prevTr = $tr.prev();
			$prevCk = $prevTr.find("input[type='checkbox']");
			prevUid = $prevCk.val();
			prevApvrRoleCd = $prevCk.attr('data-apvrRoleCd');
			prevAblApvTypCd = $prevCk.attr('data-dblApvTypCd');
			
			if(prevApvrRoleCd=='psnInfm' || prevApvrRoleCd=='deptInfm'){
				$prevTr.before($tr);
			} else if(dblApvTypCd=='reqDept'){
				if(prevUid == myUid){
					break;
				} else if(prevAblApvTypCd=='prcDept'){
					$prevTr.before($tr);
				} else {
					break;
				}
			} else if(dblApvTypCd=='prcDept'){
				
				if(apvrRoleCd=='postApvd'){
					break;
				} else if(prevUid == myUid){
					break;
				} else if(apvrRoleCd=='entu'){
					if(prevApvrRoleCd=='postApvd'){
						$prevTr.before($tr);
					} else {
						break;
					}
				} else if(apvrRoleCd=='apv' || apvrRoleCd=='pred'){
					if(prevApvrRoleCd=='entu' || prevApvrRoleCd=='postApvd'){
						$prevTr.before($tr);
					} else {
						break;
					}
				} else {
					if(prevApvrRoleCd=='apv' || prevApvrRoleCd=='pred' || prevApvrRoleCd=='entu' || prevApvrRoleCd=='postApvd'){
						$prevTr.before($tr);
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
	}
}<%
// 결재구분 TD - text 세팅 %>
function setApvTypeTdTxt($check){
	var dblApvTypCd = $check.attr('data-dblApvTypCd');
	var apvrRoleCd = $check.attr('data-apvrRoleCd');
	var $td = $(getParentTag($check[0],'tr')).children('td:last');<%
	// 이중결재가 아니거나, 개인합의, 부서합의  일 경우
	//     >> 역할 + [공석사유] 출력 %>
	if(dblApvTypCd=='' || apvrRoleCd=='psnInfm' || apvrRoleCd=='deptInfm' || apvrRoleCd=='prcDept'){
		var role = apTermResc[$check.attr('data-apvrRoleCd')];<%// 결재구분 - 결재자역할코드 %>
		if(role==null) role = "";
		if($check.attr('data-absRsonNm')!='') role = role + " ["+$check.attr('data-absRsonNm')+"]";<%// 부재사유 %>
		$td.text(role);<%// 결재구분 - 결재자역할코드 %>
	} else {<%// 이중결재의 경우 %>
		var dblApvTypNm = apTermResc[dblApvTypCd];<%// 이중결재 : 신청부서 / 주관부서 %>
		if(dblApvTypNm==null) dblApvTypNm = "";
		var role = apTermResc[apvrRoleCd];<%// 결재구분 - 결재자역할코드 %>
		if(role==null) role = "";
		if(dblApvTypNm == 'prcDept'){<%// 처리부서의 - 부서추가 의 경우%>
			$td.text(dblApvTypNm);<%// 처리부서 출력 %>
		} else if(dblApvTypNm == 'psnInfm' || dblApvTypNm == 'deptInfm'){<%// 통보(개인통보, 부서통보) - 통보만 출력 %>
			$td.text(role);<%// 개인통보, 부서통보 %>
		} else {
			if($check.attr('data-absRsonNm')!='') role = role + " ["+$check.attr('data-absRsonNm')+"]";<%// 부재사유 %>
			$td.text(dblApvTypNm + " / " + role);
		}
	}
}<%
// 결재자/결재부서를 추가할 때 추가 할 수 있는지 체크 %>
function checkAddValidation(data, opt){
	var $inputs = $("input[type='checkbox'][id!='checkHeader']:visible");
	var $parent = $inputs.parent();
	var $users = $parent.find("input[data-apvrUid!='']");<%// 추가된 사용자 사용자 %>
	var roleCd = data.apvrRoleCd;
	
	if(data.apvrDeptYn != "Y"){<%// 사용자 추가의 경우 %>
		var $user = $parent.find("input[data-apvrUid='"+data.apvrUid+"']");<%// 해당 userUid의 사용자%><%
		// 중복 사용자 확인 - 결재 라인
		// byOne:1인결재, (makRole- mak:기안, makAgr:합의기안), revw:검토, abs:공석, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의,  apv:결재, pred:전결,  entu:결재안함(위임)
		// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 %>
		if(roleCd=="${byOneRole}" || roleCd=="${makRole}" || roleCd=="revw" || roleCd=="revw2" || roleCd=="revw3" || roleCd=="abs" || roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr" || roleCd=="apv" || roleCd=="pred" || roleCd=="entu" || roleCd=="fstVw" || roleCd=="pubVw" || roleCd=="paralPubVw"){
			if($user.is("[data-apvrRoleCd='${byOneRole}'], [data-apvrRoleCd='${makRole}'], [data-apvrRoleCd='revw'], [data-apvrRoleCd='revw2'], [data-apvrRoleCd='revw3'], [data-apvrRoleCd='abs'], [data-apvrRoleCd='psnOrdrdAgr'], [data-apvrRoleCd='psnParalAgr'], [data-apvrRoleCd='apv'], [data-apvrRoleCd='pred'], [data-apvrRoleCd='entu'], [data-apvrRoleCd='fstVw'], [data-apvrRoleCd='pubVw'], [data-apvrRoleCd='paralPubVw']")){<%
				//ap.apvLn.alrdyUser=이미 사용자가 결재라인에 지정되어 있습니다.%>
				return callMsg('ap.apvLn.alrdyUser')+" : "+data.apvrNm;
			}
		}<%
		// 사후보고(후열), 개인통보 - 중복 확인 %>
		if(roleCd == 'postApvd' || roleCd=="psnInfm"){<%// postApvd:사후보고(후열), psnInfm:개인통보 %>
			if($user.is("[data-apvrRoleCd='postApvd'], [data-apvrRoleCd='psnInfm']")){<%
				//ap.apvLn.alrdyUser2=이미 사용자가 "{0}" 또는 "{1}"에 지정되어 있습니다. [사후보고(후열)][개인통보]%>
				return callMsg('ap.apvLn.alrdyUser2',['#ap.term.postApvd','#ap.term.psnInfm'])+" : "+data.apvrNm;
			}
		}<%
		// apv:결재, pred:전결 - 둘중 하나만 추가 가능, 1인만 추가 가능 %>
		if(roleCd == 'apv' || roleCd == 'pred'){
			if($users.is("[data-apvrRoleCd='apv'], [data-apvrRoleCd='pred']")){<%
				//ap.apvLn.alrdyRole2=이미 "{0}자" 또는 "{1}자"가 지정되어 있습니다.%>
				return callMsg('ap.apvLn.alrdyRole2',['#ap.term.apv','#ap.term.pred']);
			}<%
		// entu:결재안함(위임) - 1인만 추가 가능 %>
		} else if(roleCd == 'entu'){
			if($users.is("[data-apvrRoleCd='entu']")){<%
				//ap.apvLn.alrdyRole=이미 "{0}자"가 지정되어 있습니다.%>
				return callMsg('ap.apvLn.alrdyRole',['#ap.term.entu'])+" : "+data.apvrNm;
			}<%
		// fstVw:선람 - 1인만 추가 가능 %>
		} else if(roleCd == 'fstVw'){
			if($users.is("[data-apvrRoleCd='fstVw']")){<%
				//ap.apvLn.alrdyRole=이미 "{0}자"가 지정되어 있습니다.%>
				return callMsg('ap.apvLn.alrdyRole',['#ap.term.fstVw'])+" : "+data.apvrNm;
			}
		}<%
		// 처리부서가 지정되었을 때 - 처리부서의 사용자 추가 방지 %>
		if(data.dblApvTypCd == 'prcDept'){<%// 이중결재 - 처리부서 사용자 추가 %>
			if($inputs.is("[data-apvrRoleCd='prcDept']")){<%// 처리부서가 추가되어 있는지 체크 %><%
				//ap.apvLn.alrdyPrcDeptNotUser=이미 "{0}"가 지정되어 "{0}의 사용자"를 추가 할 수 없습니다. - [처리부서]%>
				return callMsg('ap.apvLn.alrdyPrcDeptNotUser', ['#ap.term.prcDept']);
			}
		}
	} else {<%// 부서 추가의 경우 %>
		var makDeptId = $($users[0]).attr("data-apvDeptId");<%// 기안부서 %>
		var $dept = $parent.find("input[data-apvDeptId='"+data.apvDeptId+"'][data-apvrUid='']");<%
		// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
		if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr"){<%
			// 기안부서를 합의부서로 추가여부 확인 %>
			if(data.apvDeptId == makDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser="{0}자"가 속한 부서를 "{1} 부서"로 지정 할 수 없습니다. - [기안][부서순차합의, 부서병렬합의]%>
				return callMsg('ap.apvLn.notRoleDeptWithRoleUser',['#ap.term.mak', '#ap.term.'+roleCd])+" : "+data.apvrNm;
			}
			var msg = null;<%
			// 중복 부서 추가 방지 - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
			['deptOrdrdAgr','deptParalAgr'].each(function(index, va){<%// 부서순차합의, 부서병렬합의 %>
				if($dept.is("[data-apvrRoleCd='"+va+"']")){<%
					//ap.apvLn.alrdyDept=이미 부서가 "{0}"에 추가되어 있습니다. - [부서순차합의 or 부서병렬합의]%>
					msg = callMsg('ap.apvLn.alrdyDept', ['#ap.term.'+va])+" : "+data.apvDeptNm;
					return false;
				}
			});
			if(msg!=null) return msg;<%
		// 중복 부서 추가 방지 - deptInfm:부서통보 %>
		} else if(roleCd=="deptInfm"){<%// 부서통보 추가 %>
			if($dept.is("[data-apvrRoleCd='deptInfm']")){<%
				//ap.apvLn.alrdyDept=이미 부서가 "{0}"에 추가되어 있습니다. - [부서통보]%>
				return callMsg('ap.apvLn.alrdyDept',['#ap.term.deptInfm'])+" : "+data.apvDeptNm;
			}
		} else if(roleCd=="prcDept"){<%// 이중결재 - 처리부서 추가 %><%
			// 기안부서를 처리부서로 추가여부 확인 %>
			if(data.apvDeptId == makDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser2="{0}자"가 속한 부서를 "{1}"로 지정 할 수 없습니다. - [기안자][처리부서]%>
				return callMsg('ap.apvLn.notRoleDeptWithRoleUser2',['#ap.term.mak', '#ap.term.prcDept'])+" : "+data.apvrNm;
			}<%
			// 1부서 지정 - prcDept:처리부서 %>
			if($inputs.is("[data-apvrRoleCd='prcDept']")){<%// 처리부서가 추가 되어 있는지 체크 %><%
				//ap.apvLn.alrdyPrcDept=이미 "{0}"가 지정되어 있습니다. - [처리부서]%>
				return callMsg('ap.apvLn.alrdyPrcDept', ['#ap.term.prcDept'])+" : "+$parent.find("[data-apvrRoleCd='prcDept']").attr("data-apvDeptNm");
			}<%
			// 처리부서 사용자 있을 경우 - 처리부서 추가 방지 %>
			if($inputs.is("[data-dblApvTypCd='prcDept'][data-apvrUid!='']")){<%// 처리부서의 사용자가 추가되어 있는지 체크 %><%
				//ap.apvLn.alrdyUserNotPrcDept=이미 "{0}의 사용자"가 지정되어 "{0}"를 추가 할 수 없습니다. - [처리부서]%>
				return callMsg('ap.apvLn.alrdyUserNotPrcDept', ['#ap.term.prcDept']);
			}
		}
	}
	return null;
}<%
// 추가용 - 신규 빈 TR HTML 생성 %>
function addEmptyTr(){
	var $tr = $("#hiddenTr:last");
	var html = $tr[0].outerHTML;
	$(html).insertBefore($tr);
	$tr = $tr.prev();
	$tr.attr("id", "");
	return $tr;
}<%
// 삭제 %>
function delSelected(){
	var arr = [], tr;
	$("input:checked").each(function(){
		if($(this).is("[data-fixed!='Y']:visible") == true){
			tr = getParentTag(this, 'tr');
			if($(tr).attr('id')!='hiddenTr' && $(tr).attr('id')!='titleTr'){
				arr.push(tr);
			}
		}
	});
	if(arr.length==0){
		//alertMsg("cm.msg.noSelectedItem",["#ap.cols.item"]);<%//선택된 항목이 없습니다.%>
	} else {
		arr.each(function(index, obj){
			$(obj).remove();
		});
	}
}<%
// 상하이동 %>
function moveLine(direction){
	var i, arr = [];<%
	// 체크된 목록 모으기 %>
	$("input[type='checkbox'][id!='checkHeader'][data-fixed!='Y']:checked:visible").each(function(){
		arr.push(getParentTag(this,'tr'));
	});
	if(arr.length==0) return;
	
	var $node, $prev, $next, $std;
	if(direction=='up'){
		$std = $(getParentTag($("input[type='checkbox'][data-fixed='Y']:last")[0],'tr'));
		for(i=0;i<arr.length;i++){
			$node = $(arr[i]);
			$prev = $node.prev();
			if($prev[0]!=$std[0]){
				$prev.before($node);
			}
			$std = $node;
		}
	} else if(direction=='down'){
		$std = $('#hiddenTr');
		for(i=arr.length-1;i>=0;i--){
			$node = $(arr[i]);
			$next = $node.next();
			if($next[0]!=$std[0]){
				$next.after($node);
			}
			$std = $node;
		}
	}
}<%
// 결재구분 클릭 %>
function setApvLnRoleCd(setupCd, roleCd, absRsonCd, absRsonNm){
	if(gFormApvLnTypCd != 'apvLnDbl' && gFormApvLnTypCd != 'apvLnDblList'){<%// 이중결재가 아니면 - 기안 / 1인결재 변경 %>
		var $check = $("tr:eq(1)").find("input[type='checkbox']");
		var firstRoleCd = setupCd=='nomlApv' ? '${makRole}' : setupCd=='byOne' ? '${byOneRole}' : setupCd;
		if(firstRoleCd != $check.attr("data-apvrRoleCd")){
			$check.attr("data-apvrRoleCd", firstRoleCd);
			setApvTypeTdTxt($check);
		}
	}
	var apvrUid, $me, message = null;
	$("input[type='checkbox'][id!='checkHeader']:checked:visible").each(function(){
		$me = $(this);
		apvrUid = $me.attr('data-apvrUid');<%// 결재자UID - 부서일 경우 공백 %>
		
		if(apvrUid==''){<%// 부서의 경우 - ordrdAgr:순차합의, paralAgr:병렬합의, infm:통보 %>
			if(roleCd=='ordrdAgr'){<%// ordrdAgr:순차합의 %>
				if(gOptConfig['deptAgrEnab']=='Y'){<%//[부서합의] 사용 가능 - 옵션%>
					$me.attr('data-apvrRoleCd','deptOrdrdAgr');
					setApvTypeTdTxt($me);
				}
			} else if(roleCd=='paralAgr'){<%// paralAgr:병렬합의 %>
				if(gOptConfig['deptAgrEnab']=='Y'){<%//[부서합의] 사용 가능 - 옵션%>
					$me.attr('data-apvrRoleCd','deptParalAgr');
					setApvTypeTdTxt($me);
				}
			} else if(roleCd=='infm'){<%// infm:통보 %>
				if(gOptConfig['deptInfmEnab']=='Y'){<%//[부서통보] 사용 가능 - 옵션%>
					$me.attr('data-apvrRoleCd','deptInfm');
					setApvTypeTdTxt($me);
				}
			} else if(setupCd=='prcDept'){<%// 이중결재 - 결재구분 상단 - 처리부서 클릭 %>
				if(gOptConfig['prcDeptEnab']=='Y'){<%//[처리부서 지정] 사용 가능 - 옵션%>
					$me.attr('data-dblApvTypCd',setupCd);
					$me.attr('data-apvrRoleCd',setupCd);
					setApvTypeTdTxt($me);
				}
			} else if(setupCd=='infm'){<%// 이중결재 - 결재구분 상단 - 통보 클릭 %>
				if(gOptConfig['deptInfmEnab']=='Y'){<%//[부서통보] 사용 가능 - 옵션%>
					$me.attr('data-dblApvTypCd','');
					$me.attr('data-apvrRoleCd','deptInfm');
					setApvTypeTdTxt($me);
				}
			}
		} else {<%// 사용자의 경우 %>
			if(apvrUid=='${sessionScope.userVo.userUid}'){<%
				// revw:검토, apv:결재, pred:전결 - 자신을 검토,결재,전결 외 다른것으로 바꾸면
				// - gOptConfig.chgApvrAtPsnOrdrdAgr : 개인순차합의 결재선 변경
				// %>
				if(roleCd!='revw' && roleCd!='revw2' && roleCd!='revw3' && roleCd!='apv' && roleCd!='pred'){
					var msgArr = ["#ap.term.revw","#ap.term.apv","#ap.term.pred"];
					var msgNo = 1;
					
					if(gOptConfig.revw2Enable=='Y'){
						msgNo++;
						msgArr.push("#ap.term.revw2");
					}
					if(gOptConfig.revw3Enable=='Y'){
						msgNo++;
						msgArr.push("#ap.term.revw3");
					}<%
					// ap.apvLn.notChangeMeTo=자기 자신은 "{0}", "{1}", "{2}"로 만 변경 가능 합니다.
					// ap.apvLn.notChangeMeTo2=자기 자신은 "{0}", "{1}", "{2}", "{3}"로 만 변경 가능 합니다. %>
					message = callMsg("ap.apvLn.notChangeMeTo"+(msgNo==1 ? '' : msgNo),msgArr);
					return;
				}
			}
			if(roleCd=='abs'){<%// 공석 사유 세팅 %>
				$me.attr('data-absRsonCd', absRsonCd);
				$me.attr('data-absRsonNm', absRsonNm);
			} else {
				$me.attr('data-absRsonCd', '');
				$me.attr('data-absRsonNm', '');
			}
			<%// ordrdAgr:순차합의, paralAgr:병렬합의, infm:통보 %>
			if(roleCd=='ordrdAgr'){
				$me.attr('data-apvrRoleCd','psnOrdrdAgr');
				setApvTypeTdTxt($me);
			} else if(roleCd=='paralAgr'){
				$me.attr('data-apvrRoleCd','psnParalAgr');
				setApvTypeTdTxt($me);
			} else if(roleCd=='infm'){
				$me.attr('data-apvrRoleCd','psnInfm');
				setApvTypeTdTxt($me);
			} else if(setupCd=='infm'){<%// 이중결재 - 결재구분 상단 - 통보 클릭 %>
				$me.attr('data-dblApvTypCd','');
				$me.attr('data-apvrRoleCd','psnInfm');
				setApvTypeTdTxt($me);
			} else {
				if(gFormApvLnTypCd == 'apvLnDbl' || gFormApvLnTypCd == 'apvLnDblList'){<%// 이중결재 면 %>
					$me.attr('data-dblApvTypCd', (setupCd=='reqDept' || setupCd=='prcDept') ? setupCd : '');<%// 신청부서/주관부서%>
				}
				$me.attr('data-apvrRoleCd', roleCd);
				setApvTypeTdTxt($me);
			}
		}
	});
	if(message!=null) alert(message);
}<%
// 선택 해제 - 조직도클릭 or 사용자 클릭하면 하단의 사용자/부서 선택된것 선택 해제함 - parent에서 호출 %>
function deselectAll(){
	$("input:checked").each(function(){
		$(this).checkInput(false);
	});
}<%
// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelected(uncheck){
	var arr = [], $me, obj;
	$("#listApvLnForm input[name='apvLnCheck']:checked").each(function(){
		obj = {};
		$me = $(this);
		if(uncheck){ $(this).checkInput(false); }
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}<%
// [확인 - 버튼] - 결재라인 체크 %>
function validateConfirm($inputs, initAuto){<%
	// 사용자, 부서별로 나누어 담음 %>
	var users=[], depts=[];
	$inputs.each(function(){
		if($(this).attr('data-apvrDeptYn')=='Y') depts.push(this);
		else users.push(this);
	});<%
	
	// 동일결재자/부서 체크용 배열
	//    apvrs:결재(검토~사후보고), infms:통보, agrs:합의 %>
	var apvrs=[], infms=[], agrs=[], msgs=[], msg;<%
	// 각 결재자 항목 별 임시 변수
	//    roleCd:역할코드, apvrUid:결재자UID, apvDeptId:결재자부서ID, 
	//    dblApvTypCd:이중결재구분코드(reqDept:신청부서, prcDept:처리부서) %>
	var roleCd, apvrUid, apvDeptId, dblApvTypCd;<%//
	// 결재역할 수 - 1명만 세팅 체크용
	//    makByOneCnt:(기안,1인결재)자 수, - 한명만(필수)
	//    apvPredCnt:(결재,전결)자 수, - [옵션]최종 결재자 필수 여부 에 따라서 필수
	//    predCnt:전결자 수, entuCnt:결재안함(위임)자 수
	//        - [결재안함(위임)]은 [전결] 뒤에만 올 수 있으면 1명만 가능
	//    fstVwCnt:선람자 수
	%>
	var makByOneCnt=0, apvPredCnt=0, predCnt=0, entuCnt=0, fstVwCnt=0;<%
	// prcDeptCnt:처리부서 수, 
	// prcDeptApvrCnt:처리부서의 사용자 수, 
	// validPrcDeptApvrCnt:처리부서의 사용자 수 - 공석 제외, 
	//    - 처리부서, 처리부서 사용장 - 동시에 지정 불가능, 처리부서 또는 처리부서 사용자 둘 중 하나 필수
	//
	// validNomlApvrCnt:유효한 일반결재 결재자 수(검토, 합의(순차/병렬, 개인/부서), 결재, 전결) - 공석 제외 %>
	var prcDeptCnt=0, prcDeptApvrCnt=0, validPrcDeptApvrCnt=0, validNomlApvrCnt=0;<%
	
	// 결재라인 최대수 체크용 유효 수
	//    apvCnt:결재, agrCnt:합의, reqCnt:신청, prcCnt:처리 %>
	var apvCnt=0, agrCnt=0, reqCnt=0, prcCnt=0;<%
	// makrDeptId:기안자부서ID, makrRoleCd:기안자역할코드(mak:기안,byOne:1인결재)  %>
	var makrDeptId = $(users[0]).attr('data-apvDeptId');
	var makrRoleCd = $(users[0]).attr('data-apvrRoleCd');<%
	
	// 상위 결재라인 사용자UID %>
	var apvNo = "${param.apvNo}";
	var apvLnPno = "${param.apvLnPno}";
	var otherLnApvrs = [];//parentApvrs
	if(apvNo!=""){
		callAjax("./getOtherApvLnUidAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvNo:apvNo, apvLnPno:apvLnPno}, function(data){
			if(data.message != null) alert(data.message);
			if(data.apvrUids != null && data.apvrUids != ""){
				otherLnApvrs = data.apvrUids.split(",");
			}
		});
	}<%
	
	// 사용자 중복 체크 %>
	users.each(function(index, input){
		roleCd = $(input).attr('data-apvrRoleCd');
		apvrUid = $(input).attr('data-apvrUid');
		dblApvTypCd = $(input).attr('data-dblApvTypCd');<%
		// 1명 지정 체크 - byOne:1인결재, mak:기안 %>
		if(roleCd=="${byOneRole}" || roleCd=="${makRole}"){
			makByOneCnt++;
			if(makByOneCnt>1){<%
				//ap.apvLn.needOneUserNot2="{0}자", "{1}자" 중 한명을 지정해야 합니다.(둘 지정 불가) - [1인결재][기안]%>
				msg = callMsg('ap.apvLn.needOneUserNot2', ['#ap.term.byOne','#ap.term.${makRole}']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 1명 지정 체크 - apv:결재, pred:전결 %>
		if(roleCd=="apv" || roleCd=="pred"){
			apvPredCnt++;
			if(apvPredCnt>1){<%
				//ap.apvLn.needOneUserNot2="{0}자", "{1}자" 중 한명을 지정해야 합니다.(둘 지정 불가) - [결재][전결]%>
				msg = callMsg('ap.apvLn.needOneUserNot2', ['#ap.term.apv','#ap.term.pred']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// pred:전결 - 지정 수 확인 %>
			if(roleCd=="pred"){
				predCnt++;
			}
		}<%
		// 1명 지정 체크 - entu:결재안함(위임) %>
		if(roleCd=="entu"){
			entuCnt++;
			if(entuCnt>1){<%
				//ap.apvLn.needOneUser=한명의 "{0}자"를 지정해야 합니다. - [결재안함(위임)]%>
				msg = callMsg('ap.apvLn.needOneUser', ['#ap.term.entu']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 1명 지정 체크 - fstVw:선람 %>
		if(roleCd=="fstVw"){
			fstVwCnt++;
			if(fstVwCnt>1){<%
				//ap.apvLn.needOneUser=한명의 "{0}자"를 지정해야 합니다. - [선람]%>
				msg = callMsg('ap.apvLn.needOneUser', ['#ap.term.fstVw']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 동일 사용자 체크 - 결재 라인
		// byOne:1인결재, mak:기안, revw:검토, abs:공석, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, apv:결재, pred:전결,  entu:결재안함(위임)
		// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 %>
		if(roleCd=="${byOneRole}" || roleCd=="${makRole}" || roleCd=="revw" || roleCd=="revw2" || roleCd=="revw3" || roleCd=="abs" || roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr" || roleCd=="apv" || roleCd=="pred" || roleCd=="entu" || roleCd=="fstVw" || roleCd=="pubVw" || roleCd=="paralPubVw"){
			if(apvrs.contains(apvrUid)){<%
				//ap.apvLn.checkDupApvLn=결재라인에 중복된 {0}가 있습니다. [사용자]%>
				msg = callMsg('ap.apvLn.checkDupApvLn', ['#cols.user']) + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				apvrs.push(apvrUid);
			}<%
			// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의 - 합의수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			if(roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr"){
				agrCnt++;<%
			// entu:결재안함(위임) 이 아니면 - 결재수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			} else if(roleCd!="entu"){
				apvCnt++;
			}
		}<%
		// 동일 사용자 체크 - psnInfm:개인통보, postApvd:사후보고(후열) %>
		if(roleCd=="psnInfm" || roleCd=="postApvd"){
			if(infms.contains(apvrUid)){<%
				//ap.apvLn.checkDupRole2="{0}"와(과) "{1}"에 중복된 {2}가 있습니다. [개인통보][사후보고(후열)][사용자]%>
				msg = callMsg('ap.apvLn.checkDupRole2', ['#ap.term.psnInfm','#ap.term.postApvd','#cols.user']) + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				infms.push(apvrUid);
			}
		}<%
		// 일반결재(이중결재, 1인결재 아님)에서 결재라인 추가 여부 체크 %>
		if(gFormApvLnTypCd!='apvLnDbl' && gFormApvLnTypCd!='apvLnDblList' && makrRoleCd=='${makRole}'){<%
			// 유효 결재 라인 - 카운트
			//    revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//    deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, apv:결재, pred:전결
			//    fstVw:선람, pubVw:공람, paralPubVw:동시공람 - 문서유통 %>
			if(roleCd=="revw" || roleCd=="revw2" || roleCd=="revw3" || roleCd=="psnOrdrdAgr" || roleCd=="psnParalAgr" || roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr" || roleCd=="apv" || roleCd=="pred" || roleCd=="fstVw" || roleCd=="pubVw" || roleCd=="paralPubVw"){
				validNomlApvrCnt++;
			}
		}<%
		// "1인결재"에서 사용 못하는 기능 체크 %>
		if(makrRoleCd == '${byOneRole}'){<%
			// byOne:1인결재, postApvd:사후보고(후열), psnInfm:개인통보 - 가 아닌 경우 %>
			if(roleCd!="${byOneRole}" && roleCd!="postApvd" && roleCd!="psnInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
				msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.byOne','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 이중결재 "신청부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'reqDept'){<%
			// mak:기안, revw:검토, abs:공석 - 가 아닌 경우 %>
			if(roleCd!="mak" && roleCd!="revw" && roleCd!="revw2" && roleCd!="revw3" && roleCd!="abs"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [신청부서][..]%>
				msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.reqDept','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 신청부서 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			reqCnt++;
		}<%
		// 이중결재 "처리부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'prcDept'){<%
			// revw:검토, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열) - 가 아닌경우 %>
			if(roleCd!="revw" && roleCd!="revw2" && roleCd!="revw3" && roleCd!="abs" && roleCd!="apv" && roleCd!="pred" && roleCd!="entu" && roleCd!="postApvd"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [처리부서][..]%>
				msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.prcDept','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 처리부서 사용자 수 %>
		if(dblApvTypCd=='prcDept'){<%
			// 처리부서 사용자 카운트 - 처리부서 와 처리부서의 사용자가 같이 지정되는 것 방지 용%>
			prcDeptApvrCnt++;<%
			// 유효한 처리부서 사용자 카운트(공석, 결재안함, 사후보고 제외) - 처리부서의 결재자가 세팅 되었는지 체크용 %>
			if(roleCd!="abs" && roleCd!="entu" && roleCd!="postApvd"){
				validPrcDeptApvrCnt++;
			}<%
			// 신청부서 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			if(roleCd!="entu" && roleCd!="postApvd"){
				prcCnt++;
			}
		}<%
		// 상위 결재라인의 사용자 중복 체크 %>
		if(otherLnApvrs.length>0){
			if(otherLnApvrs.contains(apvrUid)){<%
				// ap.apvLn.checkDupOtherApvLn="전체경로" 결재라인에 중복된 사용자가 있습니다.%>
				msg = callMsg('ap.apvLn.checkDupOtherApvLn') + " : " + $(input).attr('data-apvrNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	});
	infms=[];<%// 개인통보 초기화 - 부서통보 체크용 %><%
	// 부서 중복 체크 %>
	depts.each(function(index, input){
		roleCd = $(input).attr('data-apvrRoleCd');
		apvDeptId = $(input).attr('data-apvDeptId');
		dblApvTypCd = $(input).attr('data-dblApvTypCd');<%
		// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
		if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr"){<%
			// 기안부서를 합의부서로 지정했는지 체크 %>
			if(makrDeptId == apvDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser="{0}자"가 속한 부서를 "{1} 부서"로 지정 할 수 없습니다. - [기안][부서순차합의, 부서병렬합의]%>
				msg = callMsg('ap.apvLn.notRoleDeptWithRoleUser', ['#ap.term.mak','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 동일 합의부서 체크 - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
			if(agrs.contains(apvDeptId)){<%
				//ap.apvLn.checkDupRole2="{0}"와(과) "{1}"에 중복된 {2}가 있습니다. [부서순차합의][부서병렬합의][부서]%>
				msg = callMsg('ap.apvLn.checkDupRole2', ['#ap.term.deptOrdrdAgr','#ap.term.deptParalAgr','#cols.dept']) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				agrs.push(apvDeptId);
			}<%
			// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 - 합의수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			agrCnt++;
		}<%
		// prcDept:처리부서 %>
		if(roleCd=="prcDept"){<%
			// 1부서 지정 체크 - prcDept:처리부서 %>
			prcDeptCnt++;
			if(prcDeptCnt>1){<%
				//ap.apvLn.needOneDept=하나의 "{0}"를 지정해야 합니다. - [처리부서]%>
				msg = callMsg('ap.apvLn.needOneDept', ['#ap.term.prcDept']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// 기안부서를 처리부서로 지정했는지 체크 %>
			if(makrDeptId == apvDeptId){<%
				//ap.apvLn.notRoleDeptWithRoleUser="{0}자"가 속한 부서를 "{1} 부서"로 지정 할 수 없습니다. - [기안][부서순차합의, 부서병렬합의]%>
				msg = callMsg('ap.apvLn.notRoleDeptWithRoleUser', ['#ap.term.mak','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}<%
			// prcDept:처리부서 - 처리부서수 더하기 - 도장방 갯수보다 많을 경우 제어용 %>
			prcCnt++;
		}<%
		// deptInfm:부서통보 %>
		if(roleCd=="deptInfm"){<%
			// 동일 통보부서 체크 - deptInfm:부서통보 %>
			if(infms.contains(apvDeptId)){<%
				//ap.apvLn.checkDupRole="{0}"에 중복된 {1}가 있습니다. [부서통보][부서]%>
				msg = callMsg('ap.apvLn.checkDupRole', ['#ap.term.deptInfm','#cols.dept']) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				infms.push(apvDeptId);
			}
		}<%
		// "1인결재"에서 사용 못하는 기능 체크 %>
		if(makrRoleCd == '${byOneRole}'){<%
			// deptInfm:부서통보 - 가 아닌 경우 %>
			if(roleCd!="deptInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
				msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.byOne','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}<%
		// 이중결재 "신청부서"에서 사용 못하는 기능 체크 - 신청부서는 부서 지정 기능 없음 %>
		if(dblApvTypCd == 'reqDept'){<%
			//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [1인결재][..]%>
			msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.reqDept','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvDeptNm');
			if(!msgs.contains(msg)) msgs.push(msg);
		}<%
		// 이중결재 "처리부서"에서 사용 못하는 기능 체크 %>
		if(dblApvTypCd == 'prcDept'){<%
			// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, deptInfm:부서통보 - 인 경우 %>
			if(roleCd=="deptOrdrdAgr" || roleCd=="deptParalAgr" || roleCd=="deptInfm"){<%
				//ap.apvLn.notRoleAt="{0}"에서는 "{1}"을(를) 사용 할 수 없습니다. - [처리부서][..]%>
				msg = callMsg('ap.apvLn.notRoleAt', ['#ap.term.prcDept','#ap.term.'+roleCd]) + " : " + $(input).attr('data-apvDeptNm');
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	});<%
	// 기안자/1인결재자 지정 체크 %>
	if(makByOneCnt==0){<%
		//ap.apvLn.needOneUserOf2="{0}자", "{1}자" 중 한명을 지정해야 합니다. - [1인결재][기안]%>
		msg = callMsg('ap.apvLn.needOneUserOf2', ['#ap.term.byOne','#ap.term.${makRole}']);
		if(!msgs.contains(msg)) msgs.push(msg);
	}<%
	// 이중결재 일때 %>
	if(gFormApvLnTypCd == 'apvLnDbl' || gFormApvLnTypCd == 'apvLnDblList'){<%
		// "처리부서" 와 "처리부서의 사용자(공석제외)"가 지정되지 않은 경우 %>
		if(prcDeptCnt==0 && validPrcDeptApvrCnt==0){<%
			// 공석만 지정된 경우 %>
			if(validPrcDeptApvrCnt != prcDeptApvrCnt){<%
				//ap.apvLn.empty2="{0}"에 유효한 결재자가 지정되지 않았습니다. [처리부서]%>
				msg = callMsg('ap.apvLn.empty2',['#ap.term.prcDept']);
				if(!msgs.contains(msg) && !initAuto) msgs.push(msg);<%
			// [옵션]처리부서 지정 - 가능 %>
			} else if(gOptConfig['prcDeptEnab']=='Y'){<%
				//ap.apvLn.needPrcDeptOrPrcDeptUser="{0}" 또는 "{0}의 사용자"를 지정 해야 합니다. [처리부서]%>
				msg = callMsg('ap.apvLn.needPrcDeptOrPrcDeptUser',['#ap.term.prcDept']);
				if(!msgs.contains(msg) && !initAuto) msgs.push(msg);<%
			// [옵션]처리부서 지정 - 불가능 %>
			} else {<%
				//ap.apvLn.needPrcDeptUser="{0}의 사용자"를 지정 해야 합니다. [처리부서]%>
				msg = callMsg('ap.apvLn.needPrcDeptUser',['#ap.term.prcDept']);
				if(!msgs.contains(msg) && !initAuto) msgs.push(msg);
			}<%
		// "처리부서"와 "처리부서의 사용자"를 같이 넣은 경우 %>
		} else if(prcDeptCnt>0 && prcDeptApvrCnt>0){<%
			//ap.apvLn.needOneOfPrc="{0}" 또는 "{0} 사용자"를 같이 지정할 수 없습니다. [처리부서]%>
			msg = callMsg('ap.apvLn.needOneOfPrc',['#ap.term.prcDept']);
			if(!msgs.contains(msg)) msgs.push(msg);
		}
	}<%
	// [옵션]최종 결재자(apv:결재, pred:전결) - 필수 %>
	if(gOptConfig['needLastApvr']=='Y' && makrRoleCd != '${byOneRole}' && makrRoleCd != 'makVw'){<%
		// 최종결재자 없고, 처리부서(이중결재의)가 없으며 - 처리부서 지정되면 처리부서에서 최종결재 지정 %>
		if(apvPredCnt==0 && prcDeptCnt==0){<%
			//ap.apvLn.needLastApvr=최종 결재자({0} 또는 {1})를 지정 해야 합니다. [결재][전결]%>
			msg = callMsg('ap.apvLn.needLastApvr',['#ap.term.apv','#ap.term.pred']);
			if(!msgs.contains(msg) && !initAuto) msgs.push(msg);
		}<%
	// [옵션]최종 결재자(apv:결재, pred:전결) - 필수 아님 %>
	} else {<%
		// 일반결재(이중결재, 1인결재 아님)에서 결재라인 추가 여부 체크 %>
		if(gFormApvLnTypCd!='apvLnDbl' && gFormApvLnTypCd!='apvLnDblList' && makrRoleCd=='${makRole}'){<%
			// 유효한결재자수 체크%>
			if(validNomlApvrCnt==0){<%
				//ap.apvLn.empty=유효한 결재자가 지정되지 않았습니다.%>
				msg = callMsg('ap.apvLn.empty');
				if(!msgs.contains(msg) && !initAuto) msgs.push(msg);
			}
		}
	}
	if(msgs.length>0){
		if(!initAuto){
			alert(msgs.join('\n\n'));
		}
		return false;
	}<%
	///////////////////////////
	//       순서 체크 
	%>
	var curOrder, maxOrder=-1, firstPrcDeptIndex=-1, orders=[], roles=[];<%
	// byOne:1인결재, mak:기안, makAgr:합의기안, makVw:담당 %>
	orders.push(["${byOneRole}","${makRole}"]);<%
	// revw:검토, abs:공석, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 %>
	orders.push(["revw","revw2","revw3","abs","psnOrdrdAgr","psnParalAgr","deptOrdrdAgr","deptParalAgr","prcDept"]);<%
	// apv:결재, pred:전결 %>
	orders.push(["apv","pred"]);<%
	// entu:결재안함(위임) %>
	orders.push(["entu"]);<%
	// postApvd:사후보고(후열) %>
	orders.push(["postApvd"]);<%
	// psnInfm:개인통보, deptInfm:부서통보  %>
	orders.push(["psnInfm","deptInfm"]);<%//fstVw:선람, pubVw:공람, paralPubVw:동시공람
	// fstVw:선람 %>
	orders.push(["fstVw"]);<%
	// pubVw:공람, paralPubVw:동시공람 %>
	orders.push(["pubVw","paralPubVw"]);
	orders.each(function(){ roles.push(''); });
	$inputs.each(function(inputIndex){
		order = -1;
		roleCd = $(this).attr('data-apvrRoleCd');
		apvrUid = $(this).attr('data-apvrUid');
		dblApvTypCd = $(this).attr('data-dblApvTypCd');<%
		// orders 의 몇 번째에 있는 roleCd 인지 찾기 - 결과저장 : order %>
		orders.each(function(index, arr){
			if(arr.contains(roleCd)){
				order = index;
				return false;
			}
		});
		<%
		// 이중결재 일때 - 신청부서가 처리부서 뒤에 오는지 체크 %>
		if((gFormApvLnTypCd == 'apvLnDbl' || gFormApvLnTypCd == 'apvLnDblList') && firstPrcDeptIndex != 9999){
			if(dblApvTypCd=='prcDept'){<%
				// 처리부서면 처음의 처리부서 index를 firstPrcDeptIndex 에 저장 %>
				if(firstPrcDeptIndex==-1){
					firstPrcDeptIndex = inputIndex;
				}
			} else if(dblApvTypCd=='reqDept'){<%
				// 신청부서면 처리부서가 있으면 - 순서가 바뀐것
				// 다시 체크 안하도록 9999 세팅 %>
				if(firstPrcDeptIndex!=-1){
					firstPrcDeptIndex = 9999;<%
					//ap.apvLn.mustRoleAft="{0}"은(는) "{1}" 뒤에 사용 해야 합니다. [처리부서][신청부서]%>
					msg = callMsg('ap.apvLn.mustRoleAft',['#ap.term.prcDept','#ap.term.reqDept']);
					if(!msgs.contains(msg)) msgs.push(msg);
				}
			}
		}
		if(order>=0){<%
			// 순서가 뒤바뀐 경우 %>
			if(maxOrder>order){<%
				// ap.apvLn.mustNotRoleAft="{0}"은(는) "{1}" 뒤에 사용 할 수 없습니다. %>
				msg = callMsg('ap.apvLn.mustNotRoleAft',['#ap.term.'+roleCd,'#ap.term.'+roles[maxOrder]]) + " : " +(apvrUid=='' ? $(this).attr('data-apvDeptNm') : $(this).attr('data-apvrNm'));
				if(!msgs.contains(msg)) msgs.push(msg);
			} else {
				if(maxOrder<order){
					maxOrder = order;<%// 현재까지 가장 높은 순위 저장 %>
				}
				if(roles[order]==''){roles[order] = roleCd;}<%// 현재 결재 역할을 해당 순위 순번에 저장 %>
			}<%
			// entu:결재안함(위임)을 사용 할 때 - pred:전결 이 없을 경우 %>
			if(roleCd=='entu'){
				if(predCnt==0){<%
					//ap.apvLn.mustRoleAft="{0}"은(는) "{1}" 뒤에 사용 해야 합니다. - [결재안함(위임)][전결]%>
					msg = callMsg('ap.apvLn.mustRoleAft', ['#ap.term.entu','#ap.term.pred']);
					if(!msgs.contains(msg)) msgs.push(msg);
				}
			}
		}
	});
	if(msgs.length>0){
		if(!initAuto){
			alert(msgs.join('\n\n'));
		}
		return false;
	}
	var cntSkip = ("${param.makrRoleCd eq 'makVw' ? 'Y' : ''}" == "Y");
	if(!cntSkip){<%
		// 결재라인 최대수 체크용 유효 수 체크
		//    parent.gApvLnMaxCnt : apv:결재, agr:합의, req:신청, prc:처리 - 양식 설정 내용
		//    apvCnt:결재, agrCnt:합의, reqCnt:신청, prcCnt:처리 - 스크립트에서 설정 한 값
		//    gFormApvLnTypCd : 결재선 구분코드
		//        apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합)
		//        apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
		//        apvLnDbl:이중결재 %>
		if(gFormApvLnTypCd=='apvLn'){<%// apvLn:결재(합의표시안함) %>
			if(apvCnt > parent.gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea=양식에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) %>
				msg = callMsg('ap.apvLn.exceedSignArea', [''+parent.gApvLnMaxCnt.apv,''+apvCnt]);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLnMixd'){<%// apvLnMixd:결재(결재합의혼합) %>
			if((apvCnt + agrCnt) > parent.gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea=양식에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) %>
				msg = callMsg('ap.apvLn.exceedSignArea', [''+parent.gApvLnMaxCnt.apv,''+(apvCnt + agrCnt)]);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLn1LnAgr' || gFormApvLnTypCd=='apvLn2LnAgr'){<%// apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄) %>
			if(apvCnt > parent.gApvLnMaxCnt.apv){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][결재] %>
				msg = callMsg('ap.apvLn.exceedSignArea2', [''+parent.gApvLnMaxCnt.apv,''+apvCnt, '#ap.term.apv']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
			if(agrCnt > parent.gApvLnMaxCnt.agr){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][합의] %>
				msg = callMsg('ap.apvLn.exceedSignArea2', [''+parent.gApvLnMaxCnt.agr,''+agrCnt, '#ap.term.agr']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLnDbl'){<%// apvLnDbl:이중결재 %>
			if(reqCnt > parent.gApvLnMaxCnt.req){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][신청] %>
				msg = callMsg('ap.apvLn.exceedSignArea2', [''+parent.gApvLnMaxCnt.req,''+reqCnt, '#ap.term.req']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
			if(prcCnt > parent.gApvLnMaxCnt.prc){<%
				//ap.apvLn.exceedSignArea2=양식({2})에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) - [][][합의] %>
				msg = callMsg('ap.apvLn.exceedSignArea2', [''+parent.gApvLnMaxCnt.prc,''+prcCnt, '#ap.term.prc']);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		} else if(gFormApvLnTypCd=='apvLnWrtn'){<%// apvLnWrtn:서면결재 %>
			if((apvCnt + agrCnt) > parent.gApvLnMaxCnt.req){<%
				//ap.apvLn.exceedSignArea=양식에 표시할 수 있는 결재 수를 초과 하였습니다.(양식:{0}, 설정:{1}) %>
				msg = callMsg('ap.apvLn.exceedSignArea', [''+parent.gApvLnMaxCnt.req,''+(apvCnt + agrCnt)]);
				if(!msgs.contains(msg)) msgs.push(msg);
			}
		}
	}
	<%
	// 공석으로 결재라인 완결 금지 체크 
	// [히든옵션] lastAbsEnab=마지막 결재자 공석 허용 %>
	if(gOptConfig['lastAbsEnab']!='Y'){<%
		// abs:공석 뒤에 와야 하는 것
		//   revw:검토, apv:결재, pred:전결, prcDept:처리부서
		//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
		%>
		var lastAbs = null;
		$inputs.each(function(inputIndex){
			roleCd = $(this).attr('data-apvrRoleCd');
			if(roleCd=='abs'){
				lastAbs = this;
			} else if(roleCd=='revw'||roleCd=='revw2'||roleCd=='revw3'||roleCd=='apv'||roleCd=='pred'||roleCd=='prcDept'||roleCd=='psnOrdrdAgr'||roleCd=='psnParalAgr'||roleCd=='deptOrdrdAgr'||roleCd=='deptParalAgr'){
				lastAbs = null;
			}
		});
		if(lastAbs != null){
			var msgArr = null;
			if(gFormApvLnTypCd != 'apvLnDbl' && gFormApvLnTypCd != 'apvLnDblList'){<%
				// ap.apvLn.notEndWithAbs1="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}", "{3}", "{4}" 중 하나가 와야 합니다.
				// %>
				msgArr = ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred', '#ap.term.agr'];
				//msg = callMsg('ap.apvLn.notEndWithAbs1', ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred', '#ap.term.agr']);
				//msgs.push(msg);
			} else if($(lastAbs).attr('data-dblApvTypCd')=='reqDept'){<%
				// ap.apvLn.notEndWithAbs2="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}" 중 하나가 와야 합니다.
				// %>
				msgArr = ['#ap.term.abs', '#ap.term.revw', '#ap.term.prcDept'];
				//msg = callMsg('ap.apvLn.notEndWithAbs2', ['#ap.term.abs', '#ap.term.revw', '#ap.term.prcDept']);
				//msgs.push(msg);
			} else if($(lastAbs).attr('data-dblApvTypCd')=='prcDept'){<%
				// ap.apvLn.notEndWithAbs3="{0}"으로 결재라인을 완결 할 수 없습니다.
				//   \n공석 뒤에는 "{1}", "{2}", "{3}" 중 하나가 와야 합니다.
				// %>
				msgArr = ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred'];
				//msg = callMsg('ap.apvLn.notEndWithAbs3', ['#ap.term.abs', '#ap.term.revw', '#ap.term.apv', '#ap.term.pred']);
				//msgs.push(msg);
			}
			if(msgArr != null){
				var msgNo = msgArr.length - 1;
				if(gOptConfig.revw2Enable == 'Y'){
					msgNo++;
					msgArr.push('#ap.term.revw2');
				}
				if(gOptConfig.revw3Enable == 'Y'){
					msgNo++;
					msgArr.push('#ap.term.revw3');
				}
				msg = callMsg('ap.apvLn.notEndWithAbs'+msgNo, msgArr);
				msgs.push(msg);
			}
			
		}
	}
	
	if(msgs.length>0){
		if(!initAuto){
			alert(msgs.join('\n\n'));
		}
		return false;
	}
	return true;
}<%
// [확인 - 버튼] - parent 에서 호출 %>
function getConfirmList(initAuto){
	var $inputs = $("input[type='checkbox'][id!='checkHeader']:visible");
	if(!validateConfirm($inputs, initAuto)){
		if(initAuto!=true) return null;
		else {
			parent.showApvLnPop();
			return null;
		}
	}
	var apvLns = [], data, $me, prevParal=null, hasMultiParal=false;
	$inputs.each(function(){
		data = {};
		$me = $(this);<%
		// input의 데이터를 data 에 담음 %>
		gAttrs.each(function(index, va){
			data[va] = $me.attr("data-"+va);
		});<%
		// 하나만 병렬인것 - 순차로 변경 작업 %>
		if(data['apvrRoleCd']=='psnParalAgr' || data['apvrRoleCd']=='deptParalAgr'){
			if(prevParal==null){
				hasMultiParal = false;
			} else {
				hasMultiParal = true;
			}
			prevParal = data;
		} else {
			if(prevParal!=null){
				if(hasMultiParal) prevParal = null;
				else {
					if(prevParal['apvrRoleCd']=='psnParalAgr'){<%
						// 1개의 개인병렬합의 - 개인순차합의 로 변경 %>
						prevParal['apvrRoleCd']='psnOrdrdAgr';
						prevParal['apvrRoleNm']=apTermResc['psnParalAgr'];
					} else if(prevParal['apvrRoleCd']=='deptParalAgr'){<%
						// 1개의 부서병렬합의 - 부서순차합의 로 변경 %>
						prevParal['apvrRoleCd']='deptOrdrdAgr';
						prevParal['apvrRoleNm']=apTermResc['deptOrdrdAgr'];
					}
					prevParal = null;
				}
			}
		}
		apvLns.push(data);
	});
	return apvLns;
}<%

// onload %>
$(document).ready(function() {
	var noInit = '${param.noInit}';
	if(noInit!='Y'){
		var arr = parent.getApvLnData();
		if(arr!=null){<%
			// 기존에 그려진 영역 삭제 %>
			$("input[type='checkbox'][id!='checkHeader']:visible").not(":first").each(function(){
				$(getParentTag(this, 'tr')).remove();
			});<%
			// 부모창의 데이터로 목록 구성하기 %>
			for(var i=1;i<arr.length;i++){
				addNewTr(arr[i]);
			}
			parent.setApvLnSetTypCdByRole(arr[0].apvrRoleCd);
		}
	}
	$("input[type='checkbox']").not(":last").uniform();
	parent.readyListApvLnFrm = true;
});
//-->
</script>
<form id="listApvLnForm" style="padding:8px 10px 10px 10px;">

<u:listArea noBottomBlank="true">
	<tr id="titleTr"><td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvLnForm', this.checked);" value=""/></td>
		<td width="25%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td width="15%" class="head_ct"><u:msg titleId="cols.user" alt="사용자" /></td>
		<td width="15%" class="head_ct"><u:term termId="or.term.posit" alt="직위" /></td>
		<td width="15%" class="head_ct"><u:term termId="or.term.title" alt="직책" /></td>
		<td width="25%" class="head_ct"><u:msg titleId="ap.jsp.apvTyp" alt="결재구분" /></td>
	</tr>
<c:forEach items="${apOngdApvLnDVoList}" var="apOngdApvLnDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<td class="bodybg_ct"><input type="checkbox" name="apvLnCheck"<c:if
				test="${status.last}"> class="skipThese"</c:if><c:if
				test="${status.first
					or apOngdApvLnDVo.apvStatCd == 'byOne' or apOngdApvLnDVo.apvStatCd == 'byOneAgr'
					or apOngdApvLnDVo.apvStatCd == 'mak' or apOngdApvLnDVo.apvStatCd == 'makAgr'
					or apOngdApvLnDVo.apvStatCd == 'apvd' or apOngdApvLnDVo.apvStatCd == 'rejt'
					or apOngdApvLnDVo.apvStatCd == 'pros' or apOngdApvLnDVo.apvStatCd == 'cons'
					or apOngdApvLnDVo.apvStatCd == 'inVw' or apOngdApvLnDVo.apvStatCd == 'cmplVw'
					or (apOngdApvLnDVo.apvrRoleCd == 'psnOrdrdAgr' and apOngdApvLnDVo.apvStatCd != 'befoAgr')
					or apOngdApvLnDVo.fixdApvrYn == 'Y'
			}"> disabled="disabled"</c:if>
			value="${apOngdApvLnDVo.apvrDeptYn != 'Y' ? apOngdApvLnDVo.apvrUid : apOngdApvLnDVo.apvDeptId}"<c:if
				test="${status.first or (not empty apOngdApvLnDVo.apvStatCd and (
				apOngdApvLnDVo.apvStatCd != 'befoApv' and apOngdApvLnDVo.apvStatCd != 'befoAgr' and apOngdApvLnDVo.apvStatCd != 'befoVw' and apOngdApvLnDVo.apvStatCd != 'befoInfm'))}">
			data-fixed="Y"</c:if>
			data-apvrRoleCd="${apOngdApvLnDVo.apvrRoleCd}"
			data-dblApvTypCd="${apOngdApvLnDVo.dblApvTypCd}"
			data-apvrDeptYn="${apOngdApvLnDVo.apvrDeptYn}"
			data-apvrUid="${apOngdApvLnDVo.apvrUid}"
			data-apvrNm="<u:out value="${apOngdApvLnDVo.apvrNm}" type="value" />"
			data-apvrPositNm="<u:out value="${apOngdApvLnDVo.apvrPositNm}" type="value" />"
			data-apvrTitleNm="<u:out value="${apOngdApvLnDVo.apvrTitleNm}" type="value" />"
			data-apvDeptId="${apOngdApvLnDVo.apvDeptId}"
			data-apvDeptNm="<u:out value="${apOngdApvLnDVo.apvDeptNm}" type="value" />"
			data-apvDeptAbbrNm="<u:out value="${apOngdApvLnDVo.apvDeptAbbrNm}" type="value" />"
			data-absRsonCd="<u:out value="${apOngdApvLnDVo.absRsonCd}" type="value" />"
			data-absRsonNm="<u:out value="${apOngdApvLnDVo.absRsonNm}" type="value" />"
			data-apvStatCd="<u:out value="${apOngdApvLnDVo.apvStatCd}" type="value" />"
			/></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /></td>
		<td class="body_ct"><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
				><a href="javascript:parent.viewUserPop('${apOngdApvLnDVo.apvrUid}');"><u:out value="${apOngdApvLnDVo.apvrNm}" /></a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}" ><c:if
				test="${not empty apOngdApvLnDVo.apvrNm}"
					><u:out value="${apOngdApvLnDVo.apvrNm}" /></c:if><c:if
				test="${empty apOngdApvLnDVo.apvrNm}"
					><a></a></c:if></c:if></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrPositNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdApvLnDVo.apvrTitleNm}" /></td>
		<td class="body_ct" id="view"><c:if
			test="${(param.formApvLnTypCd == 'apvLnDbl' or param.formApvLnTypCd == 'apvLnDblList')
				and not empty apOngdApvLnDVo.dblApvTypCd}"><u:term
			termId="ap.term.${apOngdApvLnDVo.dblApvTypCd}" /> / </c:if><c:if
			test="${not empty apOngdApvLnDVo.apvrRoleCd}"><u:term
			termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}"
			/></c:if></td>
	</tr>
</c:forEach>
</u:listArea>
</form>