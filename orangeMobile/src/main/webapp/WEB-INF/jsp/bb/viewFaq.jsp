<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<!--section S-->
<section>
	<div id="tabViewArea">
		<div class="bodyzone_scroll view contCls">
            <div class="bodyarea">
            <dl>
            <dd class="bodytxt_scroll"><div class="scroll editor" id="bodyHtmlArea">
            		<div id="zoom">
			${bbBullLVo.cont }
			</div>
            </div>
            </dd>
         </dl>
            </div>
        </div>
           ${fileVoList } 
		<c:if test="${!empty fileVoList }">
		<div class="attachzone view attchCls" >
		<div class="blank30"></div>
			<div class="attacharea">
				<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
					<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
				</c:forEach>
			</div>
         </div>  
         </c:if>
	 
		<c:if test="${baBrdBVo.screUseYn == 'Y'}">
           <!--entryzone S-->
           <div class="entryzone view etcCls" style="display:none;">
               <div class="entryarea">
               <dl>
                <dd class="etr_tit"><u:msg titleId="bb.cols.saveScre" alt="점수주기" /></dd>
                <dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    <dd class="etr_se_lt">
                    
                        <div class="select_in1" onclick="holdHide = true;fnScreSelect();">
                        <dl>
                        <dd class="select_txt_o"><span></span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>

                        <input type="hidden" name="scre" id="scre" value=""/>
                        <div class="etr_open2" style="display:none">
                            <div class="open_in1">
                                <div class="open_div">
                                <dl>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('');" data-schCd=""><u:msg titleId="bb.msg.scre.notChecked" alt="점수를 선택하세요."/></dd>
				                    <dd class="line"></dd>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('1');" data-schCd="1">★ ☆ ☆ ☆ ☆</dd>
				                    <dd class="line"></dd>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('2');" data-schCd="2">★ ★ ☆ ☆ ☆</dd>
				                    <dd class="line"></dd>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('3');" data-schCd="3">★ ★ ★ ☆ ☆</dd>
				                    <dd class="line"></dd>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('4');" data-schCd="4">★ ★ ★ ★ ☆</dd>
				                    <dd class="line"></dd>
				                    <dd class="txt_o" onclick="javascript:fnSetScre('5');" data-schCd="5">★ ★ ★ ★ ★</dd>
				                    <dd class="line"></dd>
	                            </dl>
                                </div>
                            </div>
                        </div>

                    </dd>
                    <dd class="etr_se_rt">
                    	<c:if test="${screHstExist == false}">
                    	<div class="etr_btn" onclick="saveScre();"><u:msg titleId="cm.btn.save" alt="저장" /></div>
                    	</c:if>
                    	<div class="etr_btn" onclick="viewScre();"><u:msg titleId="bb.btn.viewScre" alt="점수내역" /></div>
                    </dd>
                    </dl>
                    </div>
                  </dd>
            </dl>
               </div>
		</div>
	</c:if>
	</div>
</section>
<!--//section E-->

