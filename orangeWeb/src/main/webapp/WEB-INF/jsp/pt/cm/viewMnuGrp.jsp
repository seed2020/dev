<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>



	<!--Left S-->
	<div style="float:left; width:24%;">

		<!--portlet S-->
		<div class="portlet">
			<div class="ptltit">
				<dl>
				<dd class="title">공지사항</dd>
				<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
				<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
				</dl>
			</div>
			<div class="ptlbody">
				<div class="ptlbody_ct"> 
					<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr>
						  <td class="head_ct"><a href="javascript:">제목</a></td>
						  <td width="30%" class="head_ct"><a href="javascript:">작성일</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
						  <td class="body_lt"><a href="javascript:">소치 동계 올림필 승리 기원 행사</a></td>
						  <td class="body_rt"><a href="javascript:">2014-01-23</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
						  <td class="body_lt"><a href="javascript:">연말정산 담당자 교육 참석자 명단</a></td>
						  <td class="body_rt"><a href="javascript:">2014-01-15</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
						  <td class="body_lt"><a href="javascript:">2014년 신년 경영계획 간담회 실시</a></td>
						  <td class="body_rt"><a href="javascript:">2014-01-05</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
						  <td class="body_lt"><a href="javascript:">성탄절 이웃돕기 바자회 개최</a></td>
						  <td class="body_rt"><a href="javascript:">2013-12-17</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
						<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
						  <td class="body_lt"><a href="javascript:">2013년 종합건강진단 실시</a></td>
						  <td class="body_rt"><a href="javascript:">2013-12-12</a></td>
						</tr>
						<tr>
						  <td colspan="2" class="line"></td>
						</tr>
					</table>
				</div>
				
				<!--Blank S-->
				<div class="blank">
				</div><!--//Blank E-->
				
				<!--PageNavigation-->
				<div class="paging">
					<div class="paging_navi">						
						<a class="num_box" href="#">1</a>
						<strong class="cur_num">2</strong>	
						<a class="num_box" href="#">3</a>					
						<a class="num_box" href="#">4</a>	
						<a class="num_box" href="#">5</a>		
					</div>
				</div><!--//PageNavigation-->
				<div class="btnarea">
				<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
				</div>
			</div>
        </div><!--//portlet E-->
        
        <!--Blank S-->
        <div class="blank"> </div><!--//Blank E-->
              
        <!--portlet S-->
        <div class="portlet">
			<div class="ptltit">
			<dl>
			<dd class="title">커뮤니티</dd>
			<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
			</dl>
			</div>
			<div class="ptlbody">
				<div>
					<dl>
					<dd class="ptltxt_head"><img src="/images/${_skin}/tree_folder_close.gif"><strong>기술/지식</strong></dd>
					<dd class="ptltxt_body"><a href="javascript:">인터넷</a>, <a href="javascript:">하드웨어</a>, <a href="javascript:">자격증</a>, <a href="javascript:">보안</a>, <a href="javascript:">멀티미디어</a>, <a href="javascript:">그래픽</a>, <a href="javascript:">프로그래밍</a>, <a href="javascript:">통신/네트워크</a></dd>
					</dl>
					
					<dl>
					<dd class="ptltxt_line"></dd>
					</dl>
					
					<dl>
					<dd class="ptltxt_head"><img src="/images/${_skin}/tree_folder_close.gif"><strong>동호회</strong></dd>
					<dd class="ptltxt_body"><a href="javascript:">스크린 골프</a>, <a href="javascript:">바다 낙시</a>, <a href="javascript:">기차 여행</a>, <a href="javascript:">한식 요리</a>, <a href="javascript:">사진</a>, <a href="javascript:">외국어</a></dd>
					</dl>
				</div>
				<div class="btnarea">
				<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
				</div>
			</div>
        </div><!--//portlet E-->
        
        <!--Blank S-->
        <div class="blank">
        </div><!--//Blank E-->
        
        <!--portlet S-->
        <div class="portlet">
			<div class="ptltit">
				<dl>
				<dd class="title">나의 일정</dd>
				<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
				</dl>
			</div>
			<div class="ptlbody">
				<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
					<tr>
					  <td class="head_ct" style="padding:2px 0 2px 0;"><table class="center" border="0" cellpadding="0" cellspacing="0">       
						  <tr>
						  <td class="frontico"><a href="javascript:"><img src="/images/${_skin}/ico_left.png" /></a></td>
						  <td class="head_ct" style="width:75px;">02/25 화요일</td>
						  <td class="frontico"><a href="javascript:"><img src="/images/${_skin}/ico_right.png" /></a></td>
						  </tr>
						  </table></td>
					</tr>					
					
					<tr>
					  <td class="line"></td>
					</tr>
					<tr>
					  <td class="height5"></td>
					</tr>					
					
					<tr>
					  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong>개인일정</strong></td>
					</tr>
					<tr>
					  <td><span class="scd_promise">09:00 프로젝트 지원 방안 회의</span></td>
					</tr>
					<tr>
					  <td><span class="scd_work">12:00 1분기 실적 자료 취합</span></td>
					</tr>
					<tr>
					  <td><span class="scd_event">15:00 멀티미디어 발전방향 세미나</span></td>
					</tr>
					<tr>
					  <td><span class="scd_anniversary">결혼 기념일</span></td>
					</tr>					
					
					<tr>
					  <td class="height5"></td>
					</tr>
					<tr>
					  <td class="line"></td>
					</tr>
					<tr>
					  <td class="height5"></td>
					</tr>					
					
					<tr>
					  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong>부서일정</strong></td>
					</tr>
					<tr>
					   <td><span class="scd_event">14:00 복지 정책 변경 설명회</span></td>
					</tr>
					<tr>
					   <td><span class="scd_event">16:00 주간업무 회의</span></td>
					</tr>	
					
					<tr>
					  <td class="height5"></td>
					</tr>
					<tr>
					  <td class="line"></td>
					</tr>
					<tr>
					  <td class="height5"></td>
					</tr>					
					
					<tr>
					  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong>회사일정</strong></td>
					</tr>
					<tr>
					  <td><span class="scd_event">11:00 기술 협력 조인식</span></td>
					</tr>
					<tr>
					  <td><span class="scd_promise">14:00 외부인사 초청 간담회</span></td>
					</tr>
					<tr>
					  <td><span class="scd_anniversary">회사 창립 기념일</span></td>
					</tr>
					
				</table>
				<div class="btnarea">
				<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
				</div>
			</div>
        </div><!--//portlet E-->
            
		</div><!--//Left E-->

		<!--Right S-->
		<div style="float:right; width:74.5%;">
			<!--Right(left) S-->
			<div style="float:left; width:66%;">
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">받은 편지</dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
						</dl>
					</div>
					<div class="ptlbody">
                        <div class="ptlbody_ct"> 
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr>
								  <td class="head_ct"><a href="javascript:">제목</a></td>
								  <td width="13%" class="head_ct"><a href="javascript:">보낸사람</a></td>
								  <td width="16%" class="head_ct"><a href="javascript:">받은시간</a></td>
								  <td width="10%" class="head_ct"><a href="javascript:">크기</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">[인사뉴스레터] 2014년 직원교육 연 4만원으로 해결!</a></td>
								  <td class="body_ct"><a href="javascript:">잡코리아</a></td>
								  <td class="body_ct"><a href="javascript:">14-02-12 10:23</a></td>
								  <td class="body_rt"><a href="javascript:">25.7K</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">스마트 금융 & 정보보호 페어(SFIS) 2014</a></td>
								  <td class="body_ct"><a href="javascript:">홍길동</a></td>
								  <td class="body_ct"><a href="javascript:">14-02-07 11:36</a></td>
								  <td class="body_rt"><a href="javascript:">5.1K</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">판매 제품 가격표 요청의 건</a></td>
								  <td class="body_ct"><a href="javascript:">김태희</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_rt"><a href="javascript:">250.9K</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">프로젝트 투입인력 프로파일입니다</a></td>
								  <td class="body_ct"><a href="javascript:">한효주</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-21 15:11</a></td>
								  <td class="body_rt"><a href="javascript:">16.3K</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">추가 기능 요청 사항 보배 드립니다</a></td>
								  <td class="body_ct"><a href="javascript:">박은빈</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-13 12:45</a></td>
								  <td class="body_rt"><a href="javascript:">7K</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">데이터 연계 표준가이드 작성 방법</a></td>
								  <td class="body_ct"><a href="javascript:">이상화</a></td>
								  <td class="body_ct"><a href="javascript:">13-12-18 17:41</a></td>
								  <td class="body_rt"><a href="javascript:">158.7K</a></td>
								</tr>
							</table>
						</div>
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->

				<!--Blank S-->
				<div class="blank">
				</div><!--//Blank E-->
				
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">전자결재</dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
						</dl>
					</div>
					<div class="ptlbody">
						<div class="ptlbody_ct"> 
							<div class="tab_basic">
								<div class="tab_left">
								<ul>
								<li class="basic_open"><a href="javascript:"><span>결재 대기 문서</span></a></li>
								<li class="basic"><a href="javascript:"><span>접수 문서</span></a></li>
								</ul>
								</div>
								
							</div>
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr>
								  <td class="head_ct"><a href="javascript:">문서제목</a></td>
								  <td width="13%" class="head_ct"><a href="javascript:">기안자</a></td> 
								  <td width="16%" class="head_ct"><a href="javascript:">상신일자</a></td>
								  <td width="10%" class="head_ct"><a href="javascript:">문서상태</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">신규 도입 설비 규격 및 가격 검토결과 보고서</a></td>
								  <td class="body_ct"><a href="javascript:">홍길동</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">합의중</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">설비 증설에 따른 추가 경비 및 상세 내역</a></td>
								  <td class="body_ct"><a href="javascript:">김태희</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">진행</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">육아 휴직 신청(2014.03 ~ 2014.08)</a></td>
								  <td class="body_ct"><a href="javascript:">한효주</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">진행</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">2014년 해외(북미지역) 판매 전략 수립</a></td>
								  <td class="body_ct"><a href="javascript:">박은빈</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">합의중</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">2013년 경영 실적 보고</a></td>
								  <td class="body_ct"><a href="javascript:">이상화</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">진행</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
							</table>
						 </div> 
				 
						<!--Blank S-->
						<div class="blank"> </div><!--//Blank E-->
				
						<!--PageNavigation-->
						<div class="paging">
							<div class="paging_navi">
								<strong class="cur_num">1</strong>
								<a class="num_box" href="#">2</a>
								<a class="num_box" href="#">3</a>								
								<a class="num_box" href="#">4</a>	
								<a class="num_box" href="#">5</a>		
							</div>
						</div><!--//PageNavigation-->			
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank"> </div><!--//Blank E-->
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
					<dl>
					<dd class="title">게시판</dd>					
					<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
					<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
					</dl>
					</div>
					<div class="ptlbody">
						<div class="ptlbody_ct">
							<div class="tab_basic">
								<div class="tab_left">
									<ul>
									<li class="basic_open"><a href="javascript:"><span>최신게시물</span></a></li>
									<li class="basic"><a href="javascript:"><span>나의게시물</span></a></li>
									</ul>
								</div>
								
							</div>
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr>
								  <td class="head_ct"><a href="javascript:">제목</a></td>
								  <td width="13%" class="head_ct"><a href="javascript:">작성자</a></td>
								  <td width="16%" class="head_ct"><a href="javascript:">작성일</a></td>
								  <td width="10%" class="head_ct"><a href="javascript:">조회수</a></td>
								 </tr>
								 <tr>
								   <td colspan="4" class="line"></td>
								 </tr>
								 <tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">2013년 연말정산 공지</a></td>
								  <td class="body_ct"><a href="javascript:">홍길동</a></td>
								  <td class="body_ct"><a href="javascript:">14-02-12 10:23</a></td>
								  <td class="body_ct"><a href="javascript:">120</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">Q&A - 궁금한게 있으시면 이 글을 클릭하세요~!</a></td>
								  <td class="body_ct"><a href="javascript:">이상화</a></td>
								  <td class="body_ct"><a href="javascript:">14-02-07 11:36</a></td>
								  <td class="body_ct"><a href="javascript:">85</a></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">원산지관리시스템(UNIFTA) 관련 공지가 올라 왔습니다.</a></td>
								  <td class="body_ct"><a href="javascript:">김태희</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-22 11:29</a></td>
								  <td class="body_ct"><a href="javascript:">234</a></td>
								</tr>
								<tr>
								  <td colspan="4" class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">[OZ] 클라이언트 PC에 오즈 뷰어가 설치 안될 때 조치 방법</a></td>
								  <td class="body_ct"><a href="javascript:">한효주</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-21 15:11</a></td>
								  <td class="body_ct"><a href="javascript:">327</a></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">아는 자가 아닌 배우는 자가 되어라.</a></td>
								  <td class="body_ct"><a href="javascript:">박은빈</a></td>
								  <td class="body_ct"><a href="javascript:">14-01-13 12:45</a></td>
								  <td class="body_ct"><a href="javascript:">98</a></td>
								</tr>
								 <tr>
								   <td colspan="4" class="line"></td>
								 </tr>
							 </table>
						</div>

						<!--Blank S-->
						<div class="blank"> </div><!--//Blank E-->

						<!--PageNavigation-->
						<div class="paging">
							<div class="paging_navi">
								<a class="num_box" href="#">1</a>
								<a class="num_box" href="#">2</a>	
								<strong class="cur_num">3</strong>	
								<a class="num_box" href="#">4</a>	
								<a class="num_box" href="#">5</a>		
							</div>
						</div><!--//PageNavigation-->		

						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank"> </div><!--//Blank E-->				
				    
			</div><!--//Right(left) E-->

			<!--Right(right) S-->
			<div style="float:right; width:32%;">
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">개인 명함</dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_setting.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
						</dl>
					</div>
					<div class="ptlbody">
						<div class="ptlbody_ct"> 
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
                                   <td class="body_lt"><a href="javascript:">김태희 (010-1234-1234)</a></td>
                                 </tr>
                                 <tr>
                                   <td class="line"></td>
                                 </tr>
                                 <tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
                                   <td class="body_lt"><a href="javascript:">한효주 (010-1234-1234)</a></td>
                                 </tr>
                                 <tr>
                                   <td class="line"></td>
                                 </tr>
                                 <tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
                                   <td class="body_lt"><a href="javascript:">박은빈 (010-1234-1234)</a></td>
                                 </tr>
                                 <tr>
                                   <td class="line"></td>
                                 </tr>
                                 <tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
                                   <td class="body_lt"><a href="javascript:">심은경 (010-1234-1234)</a></td>
                                 </tr>
                                 <tr>
                                   <td class="line"></td>
                                 </tr>
                                 <tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
                                   <td class="body_lt"><a href="javascript:">이상화 (010-1234-1234)</a></td>
                                 </tr>
                                 <tr>
                                   <td class="line"></td>
                                 </tr>
                            </table>
                        
                            <div class="blank_s"> </div>  
        
							
                        </div>
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				 </div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank"> </div><!--//Blank E-->
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">자원현황</dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
						</dl>
					</div>
					<div class="ptlbody">
						<div class="ptlbody_ct"> 
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">빔 프로젝트 사용 요청</a></td>
								</tr>
								<tr>
								  <td class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">3층 대회의실 사용</a></td>
								</tr>
								<tr>
								  <td class="line"> </td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:">개발장비(서버) 반출 요청 건</a></td>
								</tr>
								<tr>
								  <td class="line"></td>
								</tr>
								<tr onmouseover='this.className=&quot;trover&quot;' onmouseout='this.className=&quot;trout&quot;'>
								  <td class="body_lt"><a href="javascript:"></a></td>
								</tr>
								<tr>
								  <td class="line"></td>
								</tr>								
							</table>
						</div>
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank">	</div><!--//Blank E-->
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">일정관리</dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd>
						<dd class="btn"><a href="javascript:"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd>
						</dl>
					</div>
					<div class="ptlbody">
                        <div class="ptlbody_ct"> 
							<!--calendar S-->
							<div class="calendar" style="width:100%;border:0px solid #ffffff;padding:5px 0 0 0;">
                                <table class="month" border="0" cellpadding="0" cellspacing="0">
									<tr>
									  <td class="monthbtn"><a href="javascript:"><img src="/images/${_skin}/ico_allleft.png" width="20" height="20" /></a></td>
									  <td class="monthbtn"><a href="javascript:"><img src="/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
									  <td class="monthtxt"><a href="javascript:">2014</a>/<a href="javascript:">02</a></td>
									  <td class="monthbtn"><a href="javascript:"><img src="/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
									  <td class="monthbtn"><a href="javascript:"><img src="/images/${_skin}/ico_allright.png" width="20" height="20" /></a></td>
									</tr>
                                </table>
                                <table class="week_pd" border="0" cellpadding="0" cellspacing="0">
									<tr>
									  <td width="23" class="week_red">S</td>
									  <td width="23" class="week_gray">M</td>
									  <td width="23" class="week_gray">T</td>
									  <td width="23" class="week_gray">W</td>
									  <td width="23" class="week_gray">T</td>
									  <td width="23" class="week_gray">F</td>
									  <td width="23" class="week_blue">S</td>
									</tr>
									
									<tr>
									  <td><a href="javascript:" class="day_red_off">26</a></td>
									  <td><a href="javascript:" class="day_gray_off">27</a></td>
									  <td><a href="javascript:" class="day_gray_off">28</a></td>
									  <td><a href="javascript:" class="day_gray_off">29</a></td>
									  <td><a href="javascript:" class="day_red_off">30</a></td>
									  <td><a href="javascript:" class="day_red_off">31</a></td>
									  <td><a href="javascript:" class="day_red">1</a></td>
									</tr>
									<tr>
									  <td><a href="javascript:" class="day_red">2</a></td>
									  <td><a href="javascript:" class="day_gray">3</a></td>
									  <td><a href="javascript:" class="day_gray">4</a></td>
									  <td><a href="javascript:" class="day_gray">5</a></td>
									  <td><a href="javascript:" class="day_gray">6</a></td>
									  <td><a href="javascript:" class="day_gray">7</a></td>
									  <td><a href="javascript:" class="day_blue">8</a></td>
									</tr>
									<tr>
									  <td><a href="javascript:" class="day_red">9</a></td>
									  <td><a href="javascript:" class="day_gray">10</a></td>
									  <td><a href="javascript:" class="day_gray">11</a></td>
									  <td><a href="javascript:" class="day_gray">12</a></td>
									  <td><a href="javascript:" class="day_gray">13</a></td>
									  <td><a href="javascript:" class="day_gray">14</a></td>
									  <td><a href="javascript:" class="day_blue">15</a></td>
									</tr>
									<tr>
									  <td><a href="javascript:" class="day_red">16</a></td>
									  <td><a href="javascript:" class="day_gray">17</a></td>
									  <td><a href="javascript:" class="day_gray">18</a></td>
									  <td><a href="javascript:" class="day_gray">19</a></td>
									  <td><a href="javascript:" class="day_gray">20</a></td>
									  <td><a href="javascript:" class="day_gray">21</a></td>
									  <td><a href="javascript:" class="day_blue">22</a></td>
									</tr>
									<tr>
									  <td><a href="javascript:" class="day_red">23</a></td>
									  <td><a href="javascript:" class="day_gray">24</a></td>
									  <td><a href="javascript:" class="day_gray">25</a></td>
									  <td><a href="javascript:" class="today">26</a></td>
									  <td><a href="javascript:" class="day_gray">27</a></td>
									  <td><a href="javascript:" class="day_gray">28</a></td>
									  <td><a href="javascript:" class="day_blue_off">1</a></td>
									</tr>
									<tr>
									  <td><a href="javascript:" class="day_red_off">1</a></td>
									  <td><a href="javascript:" class="day_gray_off">2</a></td>
									  <td><a href="javascript:" class="day_gray_off">3</a></td>
									  <td><a href="javascript:" class="day_gray_off">4</a></td>
									  <td><a href="javascript:" class="day_gray_off">5</a></td>
									  <td><a href="javascript:" class="day_gray_off">6</a></td>
									  <td><a href="javascript:" class="day_blue_off">7</a></td>
									</tr>
                                </table>
							</div><!--//calendar E-->
                        </div>
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank"> </div><!--//Blank E-->
        
				<!--portlet S-->
				<div class="portlet">
					<div class="ptltit">
						<dl>
						<dd class="title">명함등록</dd>
						</dl>
					</div>
					<div class="ptlbody">
						<div class="ptlbody_ct"> 
							<table class="ptltable" border="0" cellpadding="0" cellspacing="0">       
                                <tr>
                                  <td colspan="2" class="line"></td>
                                </tr>
                                <tr>
                                  <td width="25%" class="head_lt">이름</td>
                                  <td class="left"><input type="text" style="width:200px;" /></td>
                                </tr>
                                <tr>
                                  <td colspan="2" class="line"></td>
                                </tr>
                                <tr>
                                  <td class="head_lt">전자우편</td>
                                  <td class="left"><input type="text" style="width:200px;" /></td>
                                </tr>
                                <tr>
                                  <td colspan="2" class="line"></td>
                                </tr>
                                <tr>
                                  <td class="head_lt">전화번호</td>
                                  <td class="left">
									<table border="0" cellpadding="0" cellspacing="0">
									   <tr>
									   <td><input type="text" style="width:108px;" /></td>
									   <td><select>
									   <option>휴대폰</option>
									   <option>회사</option>
									   <option>집</option>
									   </select></td>
									   </tr>
                                    </table>
								  </td>
                                </tr>
                                <tr>
                                  <td colspan="2" class="line"></td>
                                </tr>
                            </table>
                                
                            <div class="blank_s"> </div>  
        
							<div class="front">
								<div class="front_right">
									<table border="0" cellpadding="0" cellspacing="0">       
										<tr>
										<td class="frontbtn"><a href="javascript:" class="sbutton button_small"><span>빠른추가</span></a></td>
										</tr>
									</table>
								</div>          
							</div>
                        </div>                        
						<div class="btnarea">
						<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
						</div>
					</div>
				</div><!--//portlet E-->
        
				<!--Blank S-->
				<div class="blank">	</div><!--//Blank E-->
        
				
				
			</div><!--//Right(right) E-->            
		</div><!--//Right E-->	