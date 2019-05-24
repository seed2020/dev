<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><u:set test="${myTurnApOngdApvLnDVo.apvLnNo == apOngdApvLnDVo.apvLnNo and myTurnApOngdApvLnDVo.apvLnPno == apOngdApvLnDVo.apvLnPno}"
	var="isMine" value="Y" />
				<div class="${isMine=='Y' ? 'listdivline' : (isSub=='Y' ? 'listdiv_fixed' : 'listdiv')
					}" onclick="javascript:viewUser('${apOngdApvLnDVo.apvrUid}');" data-apvLnNo="${apOngdApvLnDVo.apvLnNo
						}" data-apvLnPno="${apOngdApvLnDVo.apvLnPno}" data-cmpl="${
						(	apOngdApvLnDVo.apvStatCd == 'apvd'
						or	apOngdApvLnDVo.apvStatCd == 'rejt'
						or	apOngdApvLnDVo.apvStatCd == 'cons'
						or	apOngdApvLnDVo.apvStatCd == 'pros'
						or	apOngdApvLnDVo.apvStatCd == 'cmplVw') ? 'Y' : 'N'}" data-befo="${
						(	apOngdApvLnDVo.apvStatCd == 'befoApv'
						or	apOngdApvLnDVo.apvStatCd == 'befoAgr'
						or	apOngdApvLnDVo.apvStatCd == 'befoVw') ? 'Y' : 'N'}" data-in="${
						(	apOngdApvLnDVo.apvStatCd == 'inApv'
						or	apOngdApvLnDVo.apvStatCd == 'inAgr'
						or	apOngdApvLnDVo.apvStatCd == 'hold'
						or	apOngdApvLnDVo.apvStatCd == 'reRevw'
						or	apOngdApvLnDVo.apvStatCd == 'inInfm'
						or	apOngdApvLnDVo.apvStatCd == 'inVw') ? 'Y' : 'N'}"><!-- 부서합의 이외의 것 --><c:if
					test="${isSub=='Y'}">
				<div class="${isMine=='Y' ? 'listcheck_comment_line' : 'listcheck_comment'}"><dl><dd class="comment"></dd></dl></div>
				</c:if>
				<div class="${isSub=='Y' ? 'list_comment' : 'list'}">
				<dl>
					<dd class="tit"><u:out value="${apOngdApvLnDVo.apvrNm
						}" /> / <u:out value="${apOngdApvLnDVo.apvDeptNm}" /> / <u:out value="${apOngdApvLnDVo.apvrPositNm}" /></dd>
					<dd class="body"><c:if test="${apFormBVo.formApvLnTypCd=='apvLnDbl'}">[<u:term
						termId="ap.term.${empty apOngdApvLnDVo.dblApvTypCd ? 'prcDept' : apOngdApvLnDVo.dblApvTypCd}" langTypCd="${apvData.docLangTypCd}"
						/>] </c:if><u:term
						termId="ap.term.${apOngdApvLnDVo.apvrRoleCd=='byOneAgr' ? 'byOne' : apOngdApvLnDVo.apvrRoleCd}" langTypCd="${apvData.docLangTypCd}"
						/><c:if
		
						test="${apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
							or apOngdApvLnDVo.apvStatCd == 'cons'}"> (<u:term termId="ap.term.${apOngdApvLnDVo.apvStatCd}" /><c:if
		
						test="${not empty apOngdApvLnDVo.agntUid}">, <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a></c:if>)</c:if><c:if
						
						test="${not empty apOngdApvLnDVo.agntUid and not (
							apOngdApvLnDVo.apvStatCd == 'rejt' or apOngdApvLnDVo.apvStatCd == 'reRevw'
							or apOngdApvLnDVo.apvStatCd == 'cons')}"> (<a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term termId="ap.term.agnt" /></a>)</c:if><c:if
						
						test="${not empty apOngdApvLnDVo.apvDt}"> / <u:out value="${apOngdApvLnDVo.apvDt}" /></c:if></dd><c:if
						
						
						test="${isHis!='Y' and isSub!='Y' and (not empty apOngdApvLnDVo.apvDt and not empty apOngdApvLnDVo.apvOpinCont)}">
					<dd class="reply"><div class="replyico"></div><div class="replyin"><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></div></dd></c:if><c:if
						
						test="${isHis!='Y'
							and (not empty apOngdApvLnDVo.prevBodyHstNo or not empty apOngdApvLnDVo.prevApvLnHstNo or not empty apOngdApvLnDVo.prevAttHstNo)
							and (not (not empty apOngdApvLnDVo.apvDt and not empty apOngdApvLnDVo.apvOpinCont)
								or isSub!='Y')}">
					<dd>
						<div class="list_btnarea">
						<div class="size">
						<dl><c:if
							
							test="${not empty apOngdApvLnDVo.prevBodyHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','body');"
							/><u:msg titleId="cols.body" alt="본문" /></dd></c:if><c:if
							
							test="${not empty apOngdApvLnDVo.prevApvLnHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','apvLn');"
							/><u:msg titleId="ap.btn.ln" alt="경로" /></dd></c:if><c:if
							
							test="${not empty apOngdApvLnDVo.prevAttHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','attch');"
							/><u:msg titleId="ap.btn.attShort" alt="첨부" /></dd></c:if>
						</dl>
						</div>
						</div>
					</dd></c:if>
				</dl>
				</div><c:if
						
						
						test="${isHis!='Y' and isSub=='Y' and not empty apOngdApvLnDVo.apvDt and not empty apOngdApvLnDVo.apvOpinCont}">
				<div class="reply_commentarea">
				<div class="reply_commentareain">
				<dl>
					<dd class="reply_comment"><div class="replyico"></div><div class="replyin"><u:out value="${apOngdApvLnDVo.apvOpinCont}" /></div></dd><c:if
						
						test="${isHis!='Y'
							and (not empty apOngdApvLnDVo.prevBodyHstNo or not empty apOngdApvLnDVo.prevApvLnHstNo or not empty apOngdApvLnDVo.prevAttHstNo)
							and not (not empty apOngdApvLnDVo.apvDt and not empty apOngdApvLnDVo.apvOpinCont)
							and isSub=='Y'}">
					<dd>
						<div class="list_btnarea">
						<div class="size">
						<dl><c:if
							
							test="${not empty apOngdApvLnDVo.prevBodyHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','body');"
							/><u:msg titleId="cols.body" alt="본문" /></dd></c:if><c:if
							
							test="${not empty apOngdApvLnDVo.prevApvLnHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','apvLn');"
							/><u:msg titleId="ap.btn.ln" alt="경로" /></dd></c:if><c:if
							
							test="${not empty apOngdApvLnDVo.prevAttHstNo}">
							<dd class="btn" onclick="viewDocHis('${apOngdApvLnDVo.apvLnPno}','${apOngdApvLnDVo.apvLnNo}','attch');"
							/><u:msg titleId="ap.btn.attShort" alt="첨부" /></dd></c:if>
						</dl>
						</div>
						</div>
					</dd></c:if>
				</dl>
				</div>
				</div>
					</c:if>
				</div>