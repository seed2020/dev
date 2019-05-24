package com.innobiz.orange.web.wd.svc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wd.vo.WdAnbBVo;
import com.innobiz.orange.web.wd.vo.WdAnbModLVo;

/** 연차 생성 서비스 */
@Service
public class WdCreSvc {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 연차 공통 서비스 */
	@Autowired
	private WdCmSvc wdCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	@Scheduled(cron = "0 5 0 * * *")
	public void create(){
		
		try {
			Calendar calendar = new GregorianCalendar();
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			Map<String, String> sysPlocMap;
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", "ko");
			
			boolean enterBaseAnbMak, repbNextYear;
			int newYearMonth, newYearDay;
			
			if(ptCompBVoList != null){
				
				for(PtCompBVo ptCompBVo : ptCompBVoList){
					
					// 정책
					sysPlocMap = wdCmSvc.getConfig(ptCompBVo.getCompId());
					
					// 수동 생성 옵션
					if("Y".equals(sysPlocMap.get("manlAnbMak"))) {
						continue;
					}
					
					
					///////////// 연차
					
					// 입사일 기준 연차 생성
					enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
					
					// 대체 근무 이월
					repbNextYear = "Y".equals(sysPlocMap.get("repbNextYear"));
					
					// 년도 기준일  - 월,일
					newYearMonth = toInt(sysPlocMap.get("newYearMonth"));
					newYearDay   = toInt(sysPlocMap.get("newYearDay"));
					if(newYearMonth==0) newYearMonth = 1;
					if(newYearDay==0) newYearDay = 1;
					
					// 입사일 기준 연차 생성 - 사용안함
					if(enterBaseAnbMak){
						
						// 메월 1일에 돌아가며,
						// 각 함수에서 - 입사월이 (현제월-1)인 사용자를 조회하여 - 해당 사용자만 생성 또는 이월 함
						
						// 매월 1일 이면
						if(day == 1){
							
							// 연차 생성 및 이월
							createAnb(ptCompBVo.getCompId());
							
							// 대체 근무 이월
							if(repbNextYear){
								// 대체근무 이월
								fowardWd(ptCompBVo.getCompId(), "repb");
							}
							
							// 개정년차 이월
							fowardWd(ptCompBVo.getCompId(), "nanb");
							
							// 개정년차 생성
							createNanb(ptCompBVo.getCompId());
							
						}
						
					// 회계일 기준 연차 생성
					} else {
						
						// 기준일 이면 - 생성
						if(month==newYearMonth && day==newYearDay){
							
							// 연차 생성 및 이월
							createAnb(ptCompBVo.getCompId());
							
							// 대체 근무 이월
							if(repbNextYear){
								// 대체근무 이월
								fowardWd(ptCompBVo.getCompId(), "repb");
							}
							
							// 개정년차 이월
							fowardWd(ptCompBVo.getCompId(), "nanb");
						}
						
						// 매월 1일
						if(day == 1){
							// 개정년차 생성
							createNanb(ptCompBVo.getCompId());
						}
					}
					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 연차 생성 */
	public int createAnb(String compId){
		
		int createCnt = 0;
		try {
			
			// 정책 조회
			Map<String, String> sysPlocMap = wdCmSvc.getConfig(compId);
			
			String logLangTypCd = sysPlocMap.get("logLangTypCd");
			Locale logLocale = SessionUtil.toLocale(logLangTypCd);
			
			Calendar calendar = new GregorianCalendar();
			int year  = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			// 입사일 기준 연차 생성
			boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
			
			int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));//년도 기준일 - 월
			int newYearDay   = toInt(sysPlocMap.get("newYearDay"));//년도 기준일 - 일
			if(newYearMonth==0) newYearMonth = 1;
			if(newYearDay==0) newYearDay = 1;
			
			if(!enterBaseAnbMak){
				if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
					year--;
				}
			}
			
			// 1년 미만 잔여 연차 생성 안함
			boolean notMakeLessThen1Y = "Y".equals(sysPlocMap.get("notMakeLessThen1Y"));
			
			// 연차 등록할 후보자
			List<OrUserBVo> candiList = null;
			
			// 입사일 기준 연차 생성
			if(enterBaseAnbMak){
				
				// 이전월
				Integer EntraMonth = month==1 ? 12 : month-1;
				String maxEntraYmd = getMonthlyAddedDate(-12);
				// 입사 1년 이상 자 중에, 이전월 입사자 사용자 조회
				candiList = getUserByEntraYmd(compId, EntraMonth, null, maxEntraYmd);
				
			// 회계일 기준 연차 생성
			} else {
				//입사월 체크 안함
				Integer EntraMonth = null;
				// notMakeLessThen1Y:1년 미만 잔여 연차 생성 안함
				String maxEntraYmd = (notMakeLessThen1Y ? year-1 : year)
						+"-"
						+(newYearMonth<10?"0":"")+newYearMonth
						+"-"
						+(newYearDay<10?"0":"")+newYearDay;
				
				candiList = getUserByEntraYmd(compId, EntraMonth, null, maxEntraYmd);
			}
			
			if(candiList!=null && !candiList.isEmpty()){
				
				// 이미 등록한 연차 - 다시 한번 돌렸을때 보정용
				List<String> alreadyList = null;
				
				WdAnbModLVo wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd("anb");// anb:연차
				wdAnbModLVo.setModTypCd("cre");// cre:발생
				
				@SuppressWarnings("unchecked")
				List<WdAnbModLVo> wdAnbModLVoList = (List<WdAnbModLVo>)commonSvc.queryList(wdAnbModLVo);
				if(wdAnbModLVoList != null){
					alreadyList = new ArrayList<String>();
					for(WdAnbModLVo oldVo : wdAnbModLVoList){
						alreadyList.add(oldVo.getOdurUid());
					}
				}
				
				String odurUid, year1PrevCompareDt = null, compareMonthDay = null;
				WdAnbBVo wdAnbBVo, prevWdAnbBVo;
				
				float forwCnt, forwModCnt, creCnt, creModCnt, useCnt, useModCnt;
				int yearDiff, dayDiff, additional;
				long compareDayDiffTime=0, timeDiff;
				float creCntVa, creCntLeft;
				
				float lessThen49=0, lessThen99=0;
				
				if(!enterBaseAnbMak){
					compareMonthDay = (newYearMonth<10?"0":"") + newYearMonth + "-" + (newYearDay<10?"0":"") + newYearDay;
					if(!notMakeLessThen1Y){
						year1PrevCompareDt = (year-1) + "-" + compareMonthDay;
						compareDayDiffTime = java.sql.Date.valueOf(year + "-" + compareMonthDay).getTime();
					}
					if(sysPlocMap.get("lessThen49") != null){
						lessThen49 = toFloat(sysPlocMap.get("lessThen49"));
					}
					if(sysPlocMap.get("lessThen99") != null){
						lessThen99 = toFloat(sysPlocMap.get("lessThen99"));
					}
				}
				
				QueryQueue queryQueue = new QueryQueue();
				
				for(OrUserBVo candiVo : candiList){
					
					odurUid = candiVo.getOdurUid();
					// 계정연차가 등록된 사용자
					if(alreadyList!=null && alreadyList.contains(odurUid)) continue;
					
					if(candiVo.getEntraYmd()==null || candiVo.getEntraYmd().isEmpty()) continue;
					
					
					// 이월수, 진행수(결재중)
					forwCnt = 0;
					
					// 전년 데이터 - 연차기본(WD_ANB_B) 조회
					prevWdAnbBVo = new WdAnbBVo();
					prevWdAnbBVo.setYear(Integer.toString(year-1));
					prevWdAnbBVo.setAnbTypCd("anb");//anb:연차
					prevWdAnbBVo.setOdurUid(odurUid);
					prevWdAnbBVo = (WdAnbBVo)commonSvc.queryVo(prevWdAnbBVo);
					
					if(prevWdAnbBVo != null){
						forwCnt = toFloat(prevWdAnbBVo.getForwCnt());
						forwModCnt = toFloat(prevWdAnbBVo.getForwModCnt());
						creCnt = toFloat(prevWdAnbBVo.getCreCnt());
						creModCnt = toFloat(prevWdAnbBVo.getCreModCnt());
						useCnt = toFloat(prevWdAnbBVo.getUseCnt());
						useModCnt = toFloat(prevWdAnbBVo.getUseModCnt());
						
						forwCnt = forwCnt + forwModCnt + creCnt + creModCnt - useCnt - useModCnt;
						
						// 당겨 쓴 경우만 - 이월
						if(forwCnt > 0) forwCnt = 0;
					}
					
					// 발생수
					creCnt = 0;
					
					// 입사일 기준 연차 생성 - 발생수 계산
					if(enterBaseAnbMak){
						
						yearDiff = year - Integer.parseInt(candiVo.getEntraYmd().substring(0, 4));
						// 1월의 경우 - 전달은 해가 넘어 가므로 1을 빼서 보정
						if(month == 1) yearDiff--;
						
						additional = Math.min(10, (int)(yearDiff/2));
						creCnt = 15 + additional;
						
					// 회계일 기준 연차 생성 - 발생수 계산
					} else {
						
						// 1년 미만자면
						if(year1PrevCompareDt!=null && year1PrevCompareDt.compareTo(candiVo.getEntraYmd().substring(0, 10)) < 0){
							
							// 1년 미만 잔여 연차 생성 안함
							if(notMakeLessThen1Y) continue;
							
							timeDiff = compareDayDiffTime - java.sql.Date.valueOf(candiVo.getEntraYmd().substring(0, 10)).getTime();
							dayDiff = (int)(timeDiff / (1000*60*60*24));
							creCntVa = 15 * ((float)dayDiff / 365);
							
							additional = (int)creCntVa;
							creCntLeft = creCntVa - additional;
							
							if(creCntLeft>0 && creCntLeft<0.5){
								creCnt = additional + lessThen49;
							} else if(creCntLeft>=0.5 && creCntLeft<1){
								creCnt = additional + lessThen99;
							} else {
								creCnt = additional;
							}
							
						} else {
							
							yearDiff = year - Integer.parseInt(candiVo.getEntraYmd().substring(0, 4));
							
							// 기준일 또는 이후 입사자면 - 근속년 1년 감소
							if(compareMonthDay.compareTo(candiVo.getEntraYmd().substring(5, 10)) < 0){
								yearDiff--;
							}
							
							additional = Math.min(10, (int)(yearDiff/2));
							creCnt = 15 + additional;
						}
					}
					
					// 데이터가 있는지 체크
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd("anb");// anb:연차
					wdAnbBVo.setOdurUid(odurUid);
					
					if(commonSvc.count(wdAnbBVo) == 1){
						
						wdAnbBVo = new WdAnbBVo();
						wdAnbBVo.setYear(Integer.toString(year));
						wdAnbBVo.setAnbTypCd("anb");// anb:연차
						wdAnbBVo.setOdurUid(odurUid);
						wdAnbBVo.setCompId(compId);
						if(forwCnt != 0){
							wdAnbBVo.setForwCnt(Float.toString(forwCnt));
						}
						wdAnbBVo.setCreCnt(Float.toString(creCnt));
						queryQueue.update(wdAnbBVo);
						
					} else {
						
						wdAnbBVo = new WdAnbBVo();
						wdAnbBVo.setYear(Integer.toString(year));
						wdAnbBVo.setAnbTypCd("anb");// anb:연차
						wdAnbBVo.setOdurUid(odurUid);
						wdAnbBVo.setCompId(compId);
						wdAnbBVo.setForwCnt(Float.toString(forwCnt));
						wdAnbBVo.setForwModCnt("0");
						wdAnbBVo.setCreCnt(Float.toString(creCnt));
						wdAnbBVo.setCreModCnt("0");
						wdAnbBVo.setUseCnt("0");
						wdAnbBVo.setUseModCnt("0");
						wdAnbBVo.setOngoCnt("0");
						wdAnbBVo.setOngoModCnt("0");
						
						queryQueue.insert(wdAnbBVo);
					}
					
					if(forwCnt != 0){
						wdAnbModLVo = new WdAnbModLVo();
						wdAnbModLVo.setYear(Integer.toString(year));
						wdAnbModLVo.setAnbTypCd("anb");// anb:연차
						wdAnbModLVo.setOdurUid(odurUid);
						wdAnbModLVo.setModTypCd("forw");// forw:이월
						wdAnbModLVo.setModCnt(Float.toString(forwCnt));
						// wd.perdTypCd.yearly=연배치
						wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.yearly", logLocale));
						wdAnbModLVo.setModDt("sysdate");
						queryQueue.insert(wdAnbModLVo);
					}
					
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd("anb");// anb:연차
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("cre");// cre:발생
					wdAnbModLVo.setModCnt(Float.toString(creCnt));
					// wd.perdTypCd.yearly=연배치
					wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.yearly", logLocale));
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
					commonSvc.execute(queryQueue);
					queryQueue.removeAll();
					createCnt++;
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return createCnt;
	}
	
	/** 입사월에 해당하는 사용자 목록 조회 */
	private List<OrUserBVo> getUserByEntraYmd(String compId, Integer entraMonth,
			String minEntraYmd, String maxEntraYmd) throws SQLException{

		// 입사 1년 이상자
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBByEntraYmd");
		orUserBVo.setCompId(compId);
		orUserBVo.setUserStatCd("02");// 02:근무중
		if(minEntraYmd != null) {
			orUserBVo.setMinEntraYmd(minEntraYmd);
		}
		if(maxEntraYmd != null) {
			orUserBVo.setMaxEntraYmd(maxEntraYmd);
		}
		@SuppressWarnings("unchecked")
		List<OrUserBVo> allList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		if(entraMonth == null) return allList;
		
		// 등록할 후보자
		List<OrUserBVo> candiList = new ArrayList<OrUserBVo>();
		String strMonth = entraMonth<10 ? "0"+entraMonth : Integer.toString(entraMonth);
		
		if(allList != null){
			for(OrUserBVo vo : allList){
				// 입사월 과 이전월 이 같은 사람
				if(strMonth.equals(vo.getEntraYmd().substring(5,7))){
					candiList.add(vo);
				}
			}
		}
		return candiList;
	}
	
	
	/** 이월 - 입사일 기준(년차), 대체근무 */
	public void fowardWd(String compId, String anbTypCd){
		
		try {
			// 정책 조회
			Map<String, String> sysPlocMap = wdCmSvc.getConfig(compId);
			
			String logLangTypCd = sysPlocMap.get("logLangTypCd");
			Locale logLocale = SessionUtil.toLocale(logLangTypCd);
			
			Calendar calendar = new GregorianCalendar();
			int year  = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			boolean isNanb = "nanb".equals(anbTypCd);
			boolean isRepb = "repb".equals(anbTypCd);
			
			// 입사일 기준 연차 생성
			boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
			if(!enterBaseAnbMak){
				int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));//년도 기준일 - 월
				int newYearDay   = toInt(sysPlocMap.get("newYearDay"));//년도 기준일 - 일
				if(newYearMonth==0) newYearMonth = 1;
				if(newYearDay==0) newYearDay = 1;
				
				if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
					year--;
				}
			}
			
			// 작년 데이터 조회
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(Integer.toString(year-1));
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setCompId(compId);
			@SuppressWarnings("unchecked")
			List<WdAnbBVo> candiList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
			
			boolean hasData;
			int nanbUseYears = toInt(sysPlocMap.get("nanbUseYears")), creYear=0;
			String odurUid;
			float forwCnt, forwModCnt, creCnt, creModCnt, useCnt, useModCnt;
			if(candiList != null){
				
				// 이미 등록한 연차 - 다시 한번 돌렸을때 보정용
				List<String> alreadyList = null;
				
				WdAnbModLVo wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd(anbTypCd);
				wdAnbModLVo.setModTypCd("forw");// forw:이월
				
				@SuppressWarnings("unchecked")
				List<WdAnbModLVo> wdAnbModLVoList = (List<WdAnbModLVo>)commonSvc.queryList(wdAnbModLVo);
				if(wdAnbModLVoList != null){
					alreadyList = new ArrayList<String>();
					for(WdAnbModLVo oldVo : wdAnbModLVoList){
						alreadyList.add(oldVo.getOdurUid());
					}
				}
				
				// 이월할 후보자 - 입사일 기준 일 경우
				List<OrUserBVo> fowrCandiList = null;
				// 입사일 기준 연차 생성
				if(enterBaseAnbMak){
					// 이전월
					Integer EntraMonth = month==1 ? 12 : month-1;
					String maxEntraYmd = getMonthlyAddedDate(-12);
					// 입사 1년 이상 자 중에, 이전월 입사자 사용자 조회
					fowrCandiList = getUserByEntraYmd(compId, EntraMonth, null, maxEntraYmd);
				}
				
				
				QueryQueue queryQueue = new QueryQueue();
				
				boolean hasUser;
				for(WdAnbBVo candiVo : candiList){
					
					odurUid = candiVo.getOdurUid();
					// 연차가 등록된 사용자
					if(alreadyList!=null && alreadyList.contains(odurUid)) continue;
					
					// 입사일 기준 생성 이고, 개정년차 또는 대체휴무 이월 일 경우
					if(enterBaseAnbMak && (isNanb || isRepb)) {
						hasUser = false;
						if(fowrCandiList != null) {
							for(OrUserBVo vo : fowrCandiList) {
								if(odurUid.equals(vo.getOdurUid())) {
									hasUser = true;
									break;
								}
							}
						}
						// 대상이 아니면 이월 안함
						if(!hasUser) {
							continue;
						}
					}
					
					forwCnt = toFloat(candiVo.getForwCnt());
					forwModCnt = toFloat(candiVo.getForwModCnt());
					creCnt = toFloat(candiVo.getCreCnt());
					creModCnt = toFloat(candiVo.getCreModCnt());
					useCnt = toFloat(candiVo.getUseCnt());
					useModCnt = toFloat(candiVo.getUseModCnt());
					
					forwCnt = forwCnt + forwModCnt + creCnt + creModCnt - useCnt - useModCnt;
					
					// 개정년차 일 경우
					if(isNanb) {
						creYear = toInt(candiVo.getCreYear());
						// 생성년도가 없거나, 개정년차 사용 년수가 지난 경우
						if(creYear==0 || year - creYear > nanbUseYears) {
							// 이월 안함
							forwCnt = 0;
						}
					}
					
					if(forwCnt != 0){
						
						// 입력할 데이터가 있는지 확인
						wdAnbBVo = new WdAnbBVo();
						wdAnbBVo.setYear(Integer.toString(year));
						wdAnbBVo.setAnbTypCd(anbTypCd);
						wdAnbBVo.setOdurUid(odurUid);
						hasData = commonSvc.count(wdAnbBVo) > 0;
						
						// 데이터 있으면 - update
						if(hasData){
							
							wdAnbBVo = new WdAnbBVo();
							wdAnbBVo.setYear(Integer.toString(year));
							wdAnbBVo.setAnbTypCd(anbTypCd);
							wdAnbBVo.setOdurUid(odurUid);
							wdAnbBVo.setForwCnt(Float.toString(forwCnt));
							if(isNanb) {
								wdAnbBVo.setCreYear(Integer.toString(creYear));
							}
							queryQueue.update(wdAnbBVo);
							
						// 데이터 없으면 - insert
						} else {
							
							wdAnbBVo = new WdAnbBVo();
							wdAnbBVo.setYear(Integer.toString(year));
							wdAnbBVo.setAnbTypCd(anbTypCd);
							wdAnbBVo.setOdurUid(odurUid);
							
							wdAnbBVo.setCompId(compId);
							wdAnbBVo.setForwCnt(Float.toString(forwCnt));
							wdAnbBVo.setForwModCnt("0");
							wdAnbBVo.setCreCnt("0");
							wdAnbBVo.setCreModCnt("0");
							wdAnbBVo.setUseCnt("0");
							wdAnbBVo.setUseModCnt("0");
							wdAnbBVo.setOngoCnt("0");
							wdAnbBVo.setOngoModCnt("0");
							if(isNanb) {
								wdAnbBVo.setCreYear(Integer.toString(creYear));
							}
							queryQueue.insert(wdAnbBVo);
						}
						
						// 당년 - 이월수 - 로그
						wdAnbModLVo = new WdAnbModLVo();
						wdAnbModLVo.setYear(Integer.toString(year));
						wdAnbModLVo.setAnbTypCd(anbTypCd);
						wdAnbModLVo.setOdurUid(odurUid);
						wdAnbModLVo.setModTypCd("forw");
						wdAnbModLVo.setModCnt(Float.toString(forwCnt));
						if(enterBaseAnbMak) {
							// wd.perdTypCd.monthly=월배치
							wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.monthly", logLocale));
						} else {
							// wd.perdTypCd.yearly=연배치
							wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.yearly", logLocale));
						}
						wdAnbModLVo.setModDt("sysdate");
						queryQueue.insert(wdAnbModLVo);
						
						commonSvc.execute(queryQueue);
						queryQueue.removeAll();
					}
				}
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/** 개정연차 생성 */
	public int createNanb(String compId){
		
		int createCnt = 0;
		try {
			
			// 정책 조회
			Map<String, String> sysPlocMap = wdCmSvc.getConfig(compId);
			
			String logLangTypCd = sysPlocMap.get("logLangTypCd");
			Locale logLocale = SessionUtil.toLocale(logLangTypCd);
			
			// 개정연차 - 입사 익월 생성
			boolean enterNextMonth = sysPlocMap==null ? false : "Y".equals(sysPlocMap.get("enterNextMonth"));
			
			// 현제 - 년,월
			Calendar calendar = new GregorianCalendar();
			int year  = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			
			WdAnbBVo wdAnbBVo;
			// 개정연차 - 대상자 조회
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBByEntraYmd");
			orUserBVo.setCompId(compId);
			orUserBVo.setMinEntraYmd(getMonthlyAddedDate(-13));
			// 개정연차 - 입사 익월 생성 - 이 아닐 경우
			if(!enterNextMonth){
				orUserBVo.setMaxEntraYmd(getMonthlyAddedDate(-1));
			} else {
				orUserBVo.setMaxEntraYmd(getMonthlyAddedDate(0));
			}
			
			// 개정연차 등록 후보자
			@SuppressWarnings("unchecked")
			List<OrUserBVo> candiList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
			
			if(candiList != null){
				
				// 동월에 이미 등록된 계정연차 - 조회
				List<String> alreadyList = null;
				
				WdAnbModLVo wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd("nanb");//nanb:개정연차
				wdAnbModLVo.setModTypCd("cre");
				wdAnbModLVo.setMonth(Integer.toString(month));// 개정연차 월을 입력 관리함
				
				@SuppressWarnings("unchecked")
				List<WdAnbModLVo> wdAnbModLVoList = (List<WdAnbModLVo>)commonSvc.queryList(wdAnbModLVo);
				if(wdAnbModLVoList != null){
					alreadyList = new ArrayList<String>();
					for(WdAnbModLVo alreadyVo : wdAnbModLVoList){
						alreadyList.add(alreadyVo.getOdurUid());
					}
				}
				
				
				QueryQueue queryQueue = new QueryQueue();
				
				String odurUid;
				WdAnbBVo prevWdAnbBVo, prevWdAnbBVo2;
				float forwCnt, forwModCnt, creCnt, creModCnt, useCnt, useModCnt, creSum;
				
				for(OrUserBVo candiVo : candiList){
					
					odurUid = candiVo.getOdurUid();
					// 이미 등록된 사용자
					if(alreadyList!=null && alreadyList.contains(odurUid)) continue;
					
					// 연차기본(WD_ANB_B) 조회 - 데이터 있는지 확인
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd("nanb");//nanb:개정연차
					wdAnbBVo.setOdurUid(odurUid);
					wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
					
					prevWdAnbBVo = new WdAnbBVo();
					prevWdAnbBVo.setYear(Integer.toString(year-1));
					prevWdAnbBVo.setAnbTypCd("nanb");//nanb:개정연차
					prevWdAnbBVo.setOdurUid(odurUid);
					prevWdAnbBVo = (WdAnbBVo)commonSvc.queryVo(prevWdAnbBVo);
					
					prevWdAnbBVo2 = new WdAnbBVo();
					prevWdAnbBVo2.setYear(Integer.toString(year-2));
					prevWdAnbBVo2.setAnbTypCd("nanb");//nanb:개정연차
					prevWdAnbBVo2.setOdurUid(odurUid);
					prevWdAnbBVo2 = (WdAnbBVo)commonSvc.queryVo(prevWdAnbBVo2);
					
					// 발생수
					creSum = (wdAnbBVo==null ? 0 : toFloat(wdAnbBVo.getCreCnt()))
							+ (prevWdAnbBVo==null ? 0 : toFloat(prevWdAnbBVo.getCreCnt()))
							+ (prevWdAnbBVo2==null ? 0 : toFloat(prevWdAnbBVo2.getCreCnt()));
					
					// 11개 발행 되었으면 안함
					if(creSum>=11) continue;
					
					// 올해 데이터가 없을 경우
					if(wdAnbBVo==null){
						
						// 이월연차 계산
						forwCnt = 0;
						// 이전년 데이터가 있으면 - 이월 계산
						if(prevWdAnbBVo != null){
							forwCnt = toFloat(prevWdAnbBVo.getForwCnt());
							forwModCnt = toFloat(prevWdAnbBVo.getForwModCnt());
							creCnt = toFloat(prevWdAnbBVo.getCreCnt());
							creModCnt = toFloat(prevWdAnbBVo.getCreModCnt());
							useCnt = toFloat(prevWdAnbBVo.getUseCnt());
							useModCnt = toFloat(prevWdAnbBVo.getUseModCnt());
							
							forwCnt = forwCnt + forwModCnt + creCnt + creModCnt - useCnt - useModCnt;
						}
						
						// 이달의 개정년차 생성 - 연차기본(WD_ANB_B)
						wdAnbBVo = new WdAnbBVo();
						wdAnbBVo.setYear(Integer.toString(year));
						wdAnbBVo.setAnbTypCd("nanb");//nanb:개정연차
						wdAnbBVo.setOdurUid(odurUid);
						wdAnbBVo.setCompId(compId);
						wdAnbBVo.setForwCnt(Float.toString(forwCnt));
						wdAnbBVo.setForwModCnt("0");
						wdAnbBVo.setCreCnt("1");
						wdAnbBVo.setCreModCnt("0");
						wdAnbBVo.setUseCnt("0");
						wdAnbBVo.setUseModCnt("0");
						wdAnbBVo.setOngoCnt("0");
						wdAnbBVo.setOngoModCnt("0");
						wdAnbBVo.setCreYear(Integer.toString(year));
						
						queryQueue.insert(wdAnbBVo);
						
						// 개정년차 - 이월 내역 로그
						if(forwCnt != 0){
							wdAnbModLVo = new WdAnbModLVo();
							wdAnbModLVo.setYear(Integer.toString(year));
							wdAnbModLVo.setAnbTypCd("nanb");//nanb:개정연차
							wdAnbModLVo.setOdurUid(odurUid);
							wdAnbModLVo.setModTypCd("forw");// forw:이월
							// wd.perdTypCd.monthly=월배치
							wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.monthly", logLocale));
							wdAnbModLVo.setModCnt(Float.toString(forwCnt));
							wdAnbModLVo.setModDt("sysdate");
							queryQueue.insert(wdAnbModLVo);
						}
						
						// 개정년차 - 생성 내역 로그
						wdAnbModLVo = new WdAnbModLVo();
						wdAnbModLVo.setYear(Integer.toString(year));
						wdAnbModLVo.setAnbTypCd("nanb");//nanb:개정연차
						wdAnbModLVo.setOdurUid(odurUid);
						wdAnbModLVo.setModTypCd("cre");// cre:발생
						// wd.perdTypCd.monthly=월배치
						wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.monthly", logLocale));
						wdAnbModLVo.setModCnt("1");
						wdAnbModLVo.setMonth(Integer.toString(month));
						wdAnbModLVo.setModDt("sysdate");
						queryQueue.insert(wdAnbModLVo);
						
						
					} else {
						
						// 올해의 데이터에 더하기- 연차기본(WD_ANB_B)
						wdAnbBVo = new WdAnbBVo();
						wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
						wdAnbBVo.setYear(Integer.toString(year));
						wdAnbBVo.setAnbTypCd("nanb");//nanb:개정연차
						wdAnbBVo.setOdurUid(odurUid);
						wdAnbBVo.setCreCnt("1");//발생갯수
						wdAnbBVo.setCreYear(Integer.toString(year));
						queryQueue.add(wdAnbBVo);
						
						// 개정년차 - 생성 내역 로그
						wdAnbModLVo = new WdAnbModLVo();
						wdAnbModLVo.setYear(Integer.toString(year));
						wdAnbModLVo.setAnbTypCd("nanb");//nanb:개정연차
						wdAnbModLVo.setOdurUid(odurUid);
						wdAnbModLVo.setModTypCd("cre");// cre:발생
						// wd.perdTypCd.monthly=월배치
						wdAnbModLVo.setNote(messageProperties.getMessage("wd.perdTypCd.monthly", logLocale));
						wdAnbModLVo.setModCnt("1");
						wdAnbModLVo.setMonth(Integer.toString(month));
						wdAnbModLVo.setModDt("sysdate");
						queryQueue.insert(wdAnbModLVo);
					}
					
					commonSvc.execute(queryQueue);
					queryQueue.removeAll();
					createCnt++;
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return createCnt;
	}
	
	private static SimpleDateFormat SIMPlE_DATE = new SimpleDateFormat("yyyy-MM-dd");
	private String getMonthlyAddedDate(int month){
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return SIMPlE_DATE.format(calendar.getTime());
	}
	
	
	private float toFloat(String no){
		if(no==null || no.isEmpty()) return 0;
		return Float.parseFloat(no);
	}
	private int toInt(String no){
		if(no==null || no.isEmpty()) return 0;
		return Integer.parseInt(no);
	}
}
