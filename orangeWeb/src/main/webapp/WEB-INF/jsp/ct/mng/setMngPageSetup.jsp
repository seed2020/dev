<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">


function preView(){
	var newWindow=window.open('', 'Preview','width=1300,height=900,top=100,left=100,scrollbars=yes');
	var $form = $('#setMngPageSetupForm');
	$form.attr('target','Preview');
	$form.attr('method','post');
	$form.attr('action','/ct/viewCm.do?menuId=${menuId}&ctId=${ctId}&preView=preView}');
	
	<c:if test="${tpl['1'] != null}">
	editor("ctItro").prepare();
	</c:if>
	$form.submit();
}

function mgnPageSave(){
	
	if(validator.validate('setMngPageSetupForm')){
		
		if(typeof $('#loc1CtFncId').val() != "undefined"){
			if($("#loc1CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc2CtFncId').val() != "undefined"){
			if($("#loc2CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc3CtFncId').val() != "undefined"){
			if($("#loc3CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc4CtFncId').val() != "undefined"){
			if($("#loc4CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc5CtFncId').val() != "undefined"){
			if($("#loc5CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc6CtFncId').val() != "undefined"){
			if($("#loc6CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc7CtFncId').val() != "undefined"){
			if($("#loc7CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc8CtFncId').val() != "undefined"){
			if($("#loc8CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc9CtFncId').val() != "undefined"){
			if($("#loc9CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc10CtFncId').val() != "undefined"){
			if($("#loc10CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc11CtFncId').val() != "undefined"){
			if($("#loc11CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		if(typeof $('#loc12CtFncId').val() != "undefined"){
			if($("#loc12CtFncId").val()==null){
				alert('<u:msg titleId="ct.msg.not.choi" alt="포틀릿 입력항목에 값이 선택되지 않았습니다.기능이 없을 경우 추가되지 앟습니다. 기능 추가 후 이용바랍니다)" />');
				return;
			}
		}
		
		
		
		
		
		
		var $form = $('#setMngPageSetupForm');
		
		$form.attr('target','');
		
	    $form.attr('method','post');
	    $form.attr('action','./transMngPageSetupSave.do?menuId=${menuId}&ctId=${ctId}&fnc=${fnc}');
	    <c:if test="${tpl['1'] != null}">
	    editor("ctItro").prepare();
	    </c:if>
	    $form.submit();
	}
}


//사진 변경 - 팝업 오픈 
function setImagePop(quesNum, examNum){
	var url='./setImagePop.do?menuId=${menuId}&quesNum='+quesNum+'&examNum='+examNum;
	dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />',url);
	
}
//사진 변경 - 후처리 
function setImage( quesNum, examNum, fileId, fileNm, filePath){
	//alert(quesNum+"  "+examNum+"  "+fileId+"  "+fileNm);
	$("#ct_img").attr("src",filePath);	
	$("#imgFileId").val(fileId);	
	dialog.close('setImageDialog');
}

$(document).ready(function() {
	
	<c:forEach var="i" begin="3" end="12" step="1">
		if(typeof $('#loc${i}CtFncId').val() != "undefined"){
			<c:if test="${fn:length(ctFncFieldList)>(i-3) }">
				<c:set var="idx"	value="${i-3}" />
				$('#loc${i}CtFncId').val("${ctFncFieldList[idx].ctFncUid}");
			</c:if>
		}
	</c:forEach>

	
	
	setUniformCSS();
});

</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />

<u:title titleId="ct.jsp.setMngPage.title" alt="초기 페이지 구성" menuNameFirst="true"/>

<form id="setMngPageSetupForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="imgFileId" value="${imgFileId}" />

<% // 폼 필드 %>
<u:listArea>

	<tr>
		<td colspan="4" class="head_ct"><u:msg titleId="cols.ptlset" alt="포틀릿 영역 설정" /></td>
	</tr>
	<tr>
		<td id="ptlSetArea" colspan="4">
			<!-- 포틀릿 영역 설정 S-->
			<div style="float:left; width:100%; padding:0 8px 8px 0;">
				<table class="listtable" border="0" cellpadding="0" cellspacing="0">
				
			
				<tr>
					<td style="padding:7px;height:178px;vertical-align:top;">
					
						<table class="listtable" border="0" cellpadding="7" cellspacing="3">
							
							<tr>

								<c:if test="${tpl['1'] != null}">
								<td class="body_ct" style="height:250px;  padding:0 0 0 0;" <c:if test="${tpl['2'] == null}">colspan='2'</c:if>>
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:250px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" height="100%" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0"  >
																<tbody>
																<tr>
																	<td class="head_bg">
																	<u:msg titleId="cols.cmItro" alt="커뮤니티 소개글" />																	
																	</td>												
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td id="cmItroArea" style="padding:2px;height:202px;vertical-align:middle;">
													<u:editor id="ctItro" width="100%" height="100%" module="ct" areaId="cmItroArea" value="${ctEstbBVo.ctItro}" namoToolbar="wcPop" />
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
								</c:if>
								<c:if test="${tpl['2'] != null}">
								<td class="body_ct" width="50%" style="height:250px; padding:0 0 0 0;" <c:if test="${tpl['1'] == null}">colspan='2'</c:if>>
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:250px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" height="100%" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg">
																	<u:msg titleId="cols.cmImg" alt="이미지" />
																	<u:buttonS id="quesImgAdd" href="javascript:setImagePop(-1,-1);" titleId="wv.btn.imgAdd" alt="이미지추가" />
																	</td>											
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td  style="padding:7px;height:100%;vertical-align:middle;">
													<img src="${imgFilePath}" id="ct_img" width="250px;" height="250px;" >
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
								</c:if>
							</tr>
							
							
							
	
	
							
							
							
							
							<c:if test="${tpl['34'] != null}">
							<tr>							
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc3CtFncId" name="loc3CtFncId" >
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>											
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg">
																		<u:msg titleId="cols.plt" alt="포틀릿" />
																	</td>
																	<td width="32%">
																		<select id="loc4CtFncId" name="loc4CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>														
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px; height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
							
							</tr>
					
							</c:if>
							
							
							
							<c:if test="${tpl['5'] != null}">
							<tr>							
								<td class="body_ct" colspan=2; style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>		
																	<td width="32%">
																		<select id="loc5CtFncId" name="loc5CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>												
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>					
							</tr>
							</c:if>
							
							<c:if test="${tpl['67'] != null}">
							<!-- 2번째 로우 -->
							<tr>
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc6CtFncId" name="loc6CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc7CtFncId" name="loc7CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px; height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
							</tr>
							</c:if>
							
							
							
							
							<c:if test="${tpl['8'] != null}">
							<tr>
								<td class="body_ct" colspan=2; style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc8CtFncId" name="loc8CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>					
							</tr>
							</c:if>
							<!-- 3번째 로우 -->
							<c:if test="${tpl['910'] != null}">
							<tr>
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc9CtFncId" name="loc9CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
								<td class="body_ct" width="50%" style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc10CtFncId" name="loc10CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px; height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>
							</tr>
							</c:if>
							
							
							
							
							<c:if test="${tpl['11'] != null}">
							<tr>
								<td class="body_ct" colspan=2; style=" height:59px; padding:0 0 0 0;">
									<!-- 섹션안에 영역 S -->
									<div style="float:left; width:100%;height:100px; padding:0 0 0 0;">
										<table class="listtable" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td class="head_rd">
													<u:checkArea>											
														<td class="head_rd">
															<table border="0" cellspacing="0" cellpadding="0">
																<tbody>
																<tr>
																	<td class="head_bg"><u:msg titleId="cols.plt" alt="포틀릿" /></td>	
																	<td width="32%">
																		<select id="loc11CtFncId" name="loc11CtFncId">
																			<c:forEach	items="${ctFncFieldList}" var="ctFncList" varStatus="status">
																				<option value="${ctFncList.ctFncUid}" >${ctFncList.ctFncNm}</option>
																			</c:forEach>
																		</select>
																	</td>													
																</tr>
																</tbody>
															</table>
														</td>
													</u:checkArea>
												</td>
											</tr>
											
											<tr>
												<td style="padding:7px;  height:59px;vertical-align:middle;">
													
												</td>
											</tr>
										</table>
									</div>
									<!-- 섹션안에 영역 E -->
								</td>					
							</tr>
							</c:if>
							
						</table>
					</td>
				</tr>
				</table>
				</div>
			<!-- 포틀릿 영역 설정 E -->			
		</td>
	</tr>
	
</u:listArea>

<c:forEach var="entry" items="${tpl}" varStatus="status">   
    <input type='hidden' id="tpl" name="tpl" value="${entry.value}">
</c:forEach>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.back" alt="뒤로" href="javascript:history.go(-1);"  />
	<u:button titleId="cm.btn.preview" alt="미리보기" href="javascript:preView('');" />
	
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:mgnPageSave('');"  />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-2);" />
</u:buttonArea>

</form>

