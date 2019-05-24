<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(function() {
	
	var $ctTplFileSubj = "${tpl}";
	
	

});

function nextSubmit(){
	var tpl =  $(':radio[name=tpl]:checked').val();
	$("#template").val(tpl);
	
	if(validator.validate('setMngPageForm')){
		var $form = $('#setMngPageForm');
	    $form.attr('method','post');
	    $form.attr('action','./setMngPageSetup.do?menuId=${menuId}&ctId=${ctId}');
	    $form.submit();
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }"  alt="초기 페이지 구성" menuNameFirst="true"/>

<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="red_stxt">* <u:msg titleId="ct.jsp.setMngPage.tx01" alt="선택된 초기 페이지 템플릿이 없습니다." /></td>
		</tr>
		</table>
	</div>
</div>
<form id="setMngPageForm">
<input type="hidden" id="template" name="template" />
<input type="hidden" id="fnc" name="fnc" />

<% // 표 %>
<div class="listarea">

	<div style="float:left; width:100%; padding:0 8px 8px 0;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="padding:7px;height:178px;vertical-align:top;">
		
			<table class="listtable" border="0" cellpadding="7" cellspacing="3">
				<tr>
					<td class="body_ct" style="height:50px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<c:if test="${tpl['1'] != null}">
												<u:checkbox name="tpl" value="1" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true"/>
											</c:if>
											<c:if test="${tpl['1'] == null}">
												<u:checkbox name="tpl" value="1" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false"/>
											</c:if>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px;height:178px;vertical-align:middle;">
										<u:msg titleId="cols.cmItro" alt="커뮤니티 소개글" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
					<td class="body_ct" width="50%" style="height:50px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<c:if test="${tpl['2'] != null}">
												<u:checkbox name="tpl" value="2" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true" />
											</c:if>
											<c:if test="${tpl['2'] == null}">
												<u:checkbox name="tpl" value="2" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false" />
											</c:if>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px;height:178px;vertical-align:middle;">
										<u:msg titleId="cols.cmImg" alt="이미지" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
				</tr>
				<tr>
					<td class="body_ct" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>	
											<c:if test="${tpl['34'] != null}">										
												<u:checkbox name="tpl" value="34" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true" />
											</c:if>
											<c:if test="${tpl['34'] == null}">										
												<u:checkbox name="tpl" value="34" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false" />
											</c:if>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
					<td class="body_ct" width="15px" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<td class="head_rd">
												<table border="0" cellspacing="0" cellpadding="0">
													<tbody>
													<tr>
														<td class="head_bg"></td>												
													</tr>
													</tbody>
												</table>
											</td>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px;height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
				</tr>
				<tr>
					<td class="body_ct" colspan=2; style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>	
											<c:if test="${tpl['5'] != null}">											
											<u:checkbox name="tpl" value="5" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true"  checked="true"/>
											</c:if>
											<c:if test="${tpl['5'] == null}">											
											<u:checkbox name="tpl" value="5" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true"  checked="false"/>
											</c:if>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>					
				</tr>
				<!-- 2번째 로우 -->
				<tr>
					<td class="body_ct" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<c:if test="${tpl['67'] != null}">												
											<u:checkbox name="tpl" value="67" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true" />
											</c:if>
											<c:if test="${tpl['67'] == null}">												
											<u:checkbox name="tpl" value="67" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false"/>
											</c:if>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
					<td class="body_ct" width="15px" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<td class="head_rd">
												<table border="0" cellspacing="0" cellpadding="0">
													<tbody>
													<tr>
														<td class="head_bg"></td>												
													</tr>
													</tbody>
												</table>
											</td>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px;height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
				</tr>
				<tr>
					<td class="body_ct" colspan=2; style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>	
											<c:if test="${tpl['8'] != null}">																					
												<u:checkbox name="tpl" value="8" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true"/>
											</c:if>
											<c:if test="${tpl['8'] == null}">																					
												<u:checkbox name="tpl" value="8" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false"/>
											</c:if>	
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>					
				</tr>
				<!-- 3번째 로우 -->
				<tr>
					<td class="body_ct" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>	
											<c:if test="${tpl['910'] != null}">											
											<u:checkbox name="tpl" value="910" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true"/>
											</c:if>
											<c:if test="${tpl['910'] == null}">
											<u:checkbox name="tpl" value="910" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false"/>
											</c:if>	
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
					<td class="body_ct" width="15px" style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>
											<td class="head_rd">
												<table border="0" cellspacing="0" cellpadding="0">
													<tbody>
													<tr>
														<td class="head_bg"></td>												
													</tr>
													</tbody>
												</table>
											</td>
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px;height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>
				</tr>
				<tr>
					<td class="body_ct" colspan=2; style="height:35px; padding:0 0 0 0;">
						<!-- 섹션안에 영역 S -->
							<table class="listtable" border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td class="head_rd">
										<u:checkArea>	
											<c:if test="${tpl['11'] != null}">										
											<u:checkbox name="tpl" value="11" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="true"/>
											</c:if>
											<c:if test="${tpl['11'] == null}">										
											<u:checkbox name="tpl" value="11" titleId="cm.btn.sel" alt="선택" inputClass="head_bg" textClass="head_lt" noSpaceTd="true" checked="false"/>
											</c:if>
											
										</u:checkArea>
									</td>
								</tr>
								<tr>
									<td style="padding:7px; height:35px;vertical-align:middle;">
										<u:msg titleId="cols.plt" alt="포틀릿" />
									</td>
								</tr>
							</table>
						<!-- 섹션안에 영역 E -->
					</td>					
				</tr>
			</table>
		</td>
	</tr>
	</table>
	</div>

	

</div>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.next" alt="다음" href="javascript:nextSubmit();" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
</form>
<u:blank />

