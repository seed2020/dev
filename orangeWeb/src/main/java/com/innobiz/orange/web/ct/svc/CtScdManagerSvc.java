package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.calender.CtScdCalDay;
import com.innobiz.orange.web.ct.calender.CtScdCalMonth;
import com.innobiz.orange.web.ct.calender.CtScdCalWeek;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlCatClsBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlPromGuestDVo;
import com.innobiz.orange.web.ct.vo.CtSchdlRepetSetupDVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.util.LunalCalenderUtil;

/** 일정을 조회 저장 수정 하는 서비스 */
@Service
public class CtScdManagerSvc {
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "ctfiles";

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** schdlID생성 서비스*/
	@Autowired
	private CtCmSvc ctCmSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 게시파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	@Autowired
	private LunalCalenderUtil lunalCalenderUtil;
	
	/** 공통 기념일 조회 */
	public Map<String,Object> getCommAnnv(HttpServletRequest request, CtSchdlBVo wcSchdlBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
//		Integer recodeCount = this.getWcSchdlBListCnt(wcSchdlBVo);
		
		CtSchdlBVo conditionScd = new CtSchdlBVo();
		conditionScd.setInstanceQueryId("countCommonAnnv");
		conditionScd.setSchdlTypCd("5");
		Integer recodeCount = this.getWcSchdlBListCnt(conditionScd);
	
		PersonalUtil.setPaging(request, wcSchdlBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtSchdlBVo> commAnnvList = (List<CtSchdlBVo>) commonDao.queryList(wcSchdlBVo);
		
		rsltMap.put("commAnnvList", commAnnvList);
		rsltMap.put("recodeCount", recodeCount);
		return rsltMap;
	}
	
	/** 공통 기념일수 조회 */
	public Integer getWcSchdlBListCnt(CtSchdlBVo wcSchdlBVo) throws Exception{
		return commonDao.count(wcSchdlBVo);
	}
	

	

	/** 한자리수 월 , 일에 0 조합 */
    public String getMixDt(int d){
    	return d < 10 ? "0"+ d : ""+d;
    }
	
	public CtScdCalMonth getScdMonth(
			 int year
			,int month			
			,String timeZone
			,String ctId 
			,String catId
			,boolean withSchdl) {
		Calendar cal = Calendar.getInstance();		
		int start_day, end_day, beforeLastDay = 0;
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.YEAR, year);
		// 월은 0부터 11로 리턴되기 때문에 항상 1을 증가해야 한다. 7을 입력하면 8월이 된다.
		// 알고자 하는 달을 입력했을 경우에는 -1
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, 1);
		
		// 1~7까지 일~토
		start_day = cal.get(Calendar.DAY_OF_WEEK);
		end_day = cal.getActualMaximum(Calendar.DATE);
		
		List<CtScdCalWeek> weekObj = new ArrayList<CtScdCalWeek>();
		List<CtScdCalDay> dayObj = new ArrayList<CtScdCalDay>();
		
		//이전달 구하는 달력 생성
		//beforeLastDay : 전달 28,29,30,31 계산.
		Calendar beforeCal = Calendar.getInstance();
		int processMonth =month;
		int processYear = year;
		//month가 1월로 오면 이전 달은 12월로 셋팅 하면서  year -1 
		if(processMonth == 1){
			processYear -= 1;
			processMonth = 12;
		}else{
			processMonth -= 1;
		}
		beforeCal.set(Calendar.YEAR, processYear);
		beforeCal.set(Calendar.MONTH, processMonth-1); 
		beforeCal.set(Calendar.DATE, 1);
		beforeLastDay = beforeCal.getActualMaximum(Calendar.DATE);
		beforeLastDay = (beforeLastDay + 2) - start_day;
		
		//Month객체 생성
		CtScdCalMonth monthObj=new CtScdCalMonth();
		monthObj.setMonth(month);
		monthObj.setYear(year);
		//음,양력 
		monthObj.setSolaYN("Y");
		//스케쥴 입력
		
		
		int processWeekDay =1;
//		int sixDay = end_day + start_day ;
		int startweek=1;
		/*int endweek=end_day/7;
		//시작날짜가 1번째로 시작되지 않거나, 마지막날의 7로 나눈 몫이 0과 같지 않다면 주차를 1주차 추가한다.
		if(end_day%7!=0||start_day>1){
			//시작일과 마지막일의 합이 36이상 넘어 가면 6주이다.
			if(sixDay > 36){
				endweek=endweek+2;
			}else{
				endweek=endweek+1;
			}
		}*/
		int endweek=6;
		//temp값 까지 계산
		int tempDay;
		//1~마지막일까지.. day
		int day=0;
		
		//1부터 정해진 주차 까지 계산
		for(int i=startweek; i<=endweek; i++){
			for(int j=1; j<=7; j++){
				tempDay=j+((i-1)*7);
				CtScdCalDay days = new CtScdCalDay();
				days.setMonth(month);
				days.setYear(year);
				days.setSolaYN("Y");
				
				if(start_day>tempDay){
					//시작일 그 이전 날짜 채우는 구간
					//이전월 연한 빨간색 set
					if(j == 1) days.setHoliFlag("scddate_red_prev");
					else days.setHoliFlag("scddate_prev");
					days.setDay(beforeLastDay);
					days.setToDayFlag("scdtd");
					
					
					//전달 년 월 set
					days.setYear(processYear);
					days.setMonth(processMonth);
					
					days.setDays(processYear+""+getMixDt(processMonth)+""+getMixDt(beforeLastDay));
					
					//전달 ~ 전달마지막일 add
					dayObj.add(days);
					beforeLastDay++;
				}else{
					day++;
					//1일부터 end_day 구하는 구간
					if(day<=end_day){
						if(j == 1) days.setHoliFlag("scddate_red");
						else days.setHoliFlag("scddate");
						
						//today 설정
						Calendar defaultCal = Calendar.getInstance();

						int defYear = defaultCal.get(Calendar.YEAR);
						int defMonth = defaultCal.get(Calendar.MONTH) +1;
						int defDay = defaultCal.get(Calendar.DAY_OF_MONTH);
						
						//today flag 표시
						if(defYear == year && defMonth ==month &&defDay == day ) days.setToDayFlag("scd_today");
						else days.setToDayFlag("scdtd");
						
						//이번달 년 월 set
						days.setYear(year);
						days.setMonth(month);
						days.setDays(year+""+getMixDt(month)+""+getMixDt(day));
						//1~마지막일자 입력
						days.setDay(day);
						dayObj.add(days);
						
					//종료일 그 이후 채우는 구간
					}else{ 
						if(j == 1) days.setHoliFlag("scddate_red_prev");
						else days.setHoliFlag("scddate_prev");
						days.setDay(processWeekDay);
						days.setToDayFlag("scdtd");
						
						
						//다음달 년 월 set
						int nextYear =year;
						int nextMonth =month;
						if(12 >= (nextMonth + 1)) nextMonth+= 1;
						else{
							nextMonth = 1;
							nextYear += 1;
						}
						days.setYear(nextYear);
						days.setMonth(nextMonth);
						days.setDays(nextYear+""+getMixDt(nextMonth)+""+getMixDt(processWeekDay));
						//마지막일 ~ 다음달 첫주 add
						dayObj.add(days);
						processWeekDay++;
					}
				}	
				
			}
			CtScdCalWeek weeks = new CtScdCalWeek();
			weeks.setMonth(month);
			weeks.setYear(year);
			weeks.setWeek(i);
			weeks.setSolaYN("Y");
			
			weeks.setDays(dayObj);
			weekObj.add(weeks);
			dayObj = new ArrayList<CtScdCalDay>();			
		}
		
		monthObj.setWeeks(weekObj);
		
		try {
			if(withSchdl)
				getMonthSchdlList(year, month,timeZone,ctId,catId, monthObj);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return monthObj;
	}

	
	public CtScdCalWeek getScdWeek(int year, int month, int week,String timeZone,String ctId,String catId) {
		CtScdCalMonth calMonth=getScdMonth(year,month,timeZone,ctId,catId,true);
		CtScdCalWeek calWeek=calMonth.getWeeks().get(week-1);
		
		return calWeek;
	}	
	
	public CtScdCalWeek getScdWeek(int year, int month, int week, CtScdCalMonth calMonth) {		
		CtScdCalWeek calWeek=calMonth.getWeeks().get(week-1);		
		return calWeek;
	}

	public CtScdCalDay getScdDay(int year, int month, int day,String timeZone,String ctId,String catId) {
		CtScdCalMonth calMonth=getScdMonth(year,month,timeZone,ctId,catId,true);
		for(CtScdCalWeek calWeek:calMonth.getWeeks()){
			for(CtScdCalDay calDay:calWeek.getDays()){
				if(calDay.getMonth()==month && calDay.getDay()==day){
					return calDay;
				}
			}
		}		
		return null;
	}
	
	public CtScdCalDay getScdDay(int year, int month, int day,CtScdCalMonth calMonth) {		
		for(CtScdCalWeek calWeek:calMonth.getWeeks()){
			for(CtScdCalDay calDay:calWeek.getDays()){
				if(calDay.getMonth()==month && calDay.getDay()==day){
					return calDay;
				}
			}
		}		
		return null;
	}
	
	
	/**
	 * 해당 월/주/일 의 스케쥴을 리턴 함.
	 */
	public CtScdCalMonth getMonthSchdlList(
			int year
			,int month
			,String timeZone
			,String ctId
			,String catId
			,CtScdCalMonth monthObj
			) throws Exception
	{
		//1일 이전 30또는 31일 까지 스케쥴 생성 및 삽입
		CtSchdlBVo conditionScd = new CtSchdlBVo();
		conditionScd.setInstanceQueryId("ctselectStartToEndDate");
		conditionScd.setOrderBy("SCHDL_START_DT");
		conditionScd.setSchdlTypCd(catId);
		String monthStr=(month<10)? ("0" + month):month+"";
		conditionScd.setSchdlYm(year+ "-" +monthStr);		
		conditionScd.setCtId(ctId);
		
		//이전달 년 월 set
		int prevYear =year;
		int prevMonth = month;
		if(1 <= (prevMonth - 1)) prevMonth -= 1;
		else{
			prevMonth = 12;
			prevYear -= 1;
		}
		
		String convPrevMonth = prevMonth+"";
		if(prevMonth<10)
			convPrevMonth = "0" + prevMonth;
		if(convPrevMonth.length()==1) convPrevMonth = "0" + convPrevMonth;
		conditionScd.setSchdlPrevYm(prevYear + "-" + convPrevMonth);
		//다음달 년 월 set
		int nextYear =year;
		int nextMonth = month;
		if(12 >= (nextMonth + 1)) nextMonth+= 1;
		else{
			nextMonth = 1;
			nextYear += 1;
		}
		
		String convNextMonth = "0" + nextMonth;
		if(convNextMonth.length()==1) convNextMonth = "0" + convNextMonth;
		conditionScd.setSchdlNextYm(nextYear + "-" + convNextMonth);
		
		
		//모든 스케쥴을 가져온다.
		@SuppressWarnings("unchecked")
		List<CtSchdlBVo> schdl = (List<CtSchdlBVo>) commonSvc.queryList(conditionScd);
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//공통기념일 체크
		List<CtSchdlBVo> delList = new ArrayList<CtSchdlBVo>();
		for(CtSchdlBVo wcs : schdl){
			if(wcs.getSchdlTypCd().equals("5") && wcs.getCompId() != null && !wcs.getCompId().isEmpty() && 
					!userVo.getCompId().equals(wcs.getCompId())){
				delList.add(wcs);				
			}
		}
		if(delList.size() > 0){
			for(CtSchdlBVo removeVo : delList){
				schdl.remove(removeVo);
			}
		}
				
		boolean sortCheck=false;
		for(CtSchdlBVo wcs : schdl){
			if(timeZone!=null&&!timeZone.equals("")&&!timeZone.equals(TimeZone.getDefault().getID())){
				DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
				dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
				Calendar startCal=stringConvertCal(wcs.getSchdlStartDt(), false);
				wcs.setSchdlStartDt(dateFormat.format(startCal.getTime()));
				Calendar endCal=stringConvertCal(wcs.getSchdlEndDt(), false);				
				wcs.setSchdlEndDt(dateFormat.format(endCal.getTime()));
				sortCheck=true;
			}
			
			if(wcs.getSolaLunaYn()!=null&&wcs.getSolaLunaYn().equals("N")){
				String startTime=wcs.getSchdlStartDt().substring(11,wcs.getSchdlStartDt().length() );
				//String startDate=lunalCalenderUtil.fromLunar(wcs.getSchdlStartDt());
				String startDate=lunalCalenderUtil.toSolar(wcs.getSchdlStartDt());
				wcs.setSchdlLunaStartDt(wcs.getSchdlStartDt());
				wcs.setSchdlLunaEndDt(wcs.getSchdlEndDt());
				
				wcs.setSchdlStartDt(startDate+" "+startTime);				
				String endTime=wcs.getSchdlEndDt().substring(11,wcs.getSchdlEndDt().length() );
				//String endDate=lunalCalenderUtil.fromLunar(wcs.getSchdlEndDt());
				String endDate=lunalCalenderUtil.toSolar(wcs.getSchdlEndDt());
				
				wcs.setSchdlEndDt(endDate+" "+endTime);
				sortCheck=true;
			}
		}
		if(sortCheck)
			Collections.sort(schdl, new IndexSchdlStartDateAscCompare());
				
		for(int i=0; i < monthObj.getWeeks().size(); i++){
			for(int j=0; j < 7; j++){
				
				CtScdCalDay culentDay=monthObj.getWeeks().get(i).getDays().get(j);
				culentDay.setDayOfTheWeek(String.valueOf(j));
				int calenderYear=culentDay.getYear();
				int calenderMonth= culentDay.getMonth();
				int calenderDay=culentDay.getDay();
				
				Calendar currentCal = Calendar.getInstance ( );
				currentCal.set ( calenderYear, calenderMonth - 1, calenderDay );
				currentCal.set( Calendar.HOUR_OF_DAY, 0 );
				currentCal.set( Calendar.MINUTE, 0 );
				currentCal.set( Calendar.SECOND, 0 );
				currentCal.set( Calendar.MILLISECOND, 0 );
				
				for(CtSchdlBVo wcs : schdl){
					
					if(!wcs.getSchdlStartDt().substring(0,10).equalsIgnoreCase(wcs.getSchdlEndDt().substring(0,10))){
						wcs.setSchdlRepetState("scd_repeat");
					}
					
					int sYear = Integer.parseInt(wcs.getSchdlStartDt().substring(0,4));
					int sMonth =Integer.parseInt(wcs.getSchdlStartDt().substring(5,7));
					int sDate = Integer.parseInt(wcs.getSchdlStartDt().substring(8,10));
					
					
					
					int eYear = Integer.parseInt(wcs.getSchdlEndDt().substring(0,4));
					int eMonth = Integer.parseInt(wcs.getSchdlEndDt().substring(5,7));
					int eDate = Integer.parseInt(wcs.getSchdlEndDt().substring(8,10));
					
					if(calenderMonth==5&&calenderDay==31){
						System.out.println();
					}
					
					if(wcs.getSchdlId().equals("XO100001")){
						System.out.println();
					}
					Calendar startCal = Calendar.getInstance ( );
					startCal.set ( sYear, sMonth - 1, sDate );
					startCal.set( Calendar.HOUR_OF_DAY, 0 );
					startCal.set( Calendar.MINUTE, 0 );
					startCal.set( Calendar.SECOND, 0 );
					startCal.set( Calendar.MILLISECOND, 0 );
									
					Calendar endCal = Calendar.getInstance ( );
					endCal.set ( eYear, eMonth - 1, eDate );
					endCal.set( Calendar.HOUR_OF_DAY, 0 );
					endCal.set( Calendar.MINUTE, 0 );
					endCal.set( Calendar.SECOND, 0 );
					endCal.set( Calendar.MILLISECOND, 0 );
			
					//시작날짜
					if(startCal.equals(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else if(culentDay.getScds().size()>0){
							//Collections.sort(culentDay.getScds(), new IndexAscCompare());
							int maxIndex=-1;
							 
							for(CtSchdlBVo vo:culentDay.getScds()){
								if(vo.getSchdIndex()>maxIndex){
									maxIndex=vo.getSchdIndex();
								}
							}
							
							
							boolean isEmpty=false;
							for(int k=1; k<maxIndex; k++){
								boolean isExist=false;
								for(CtSchdlBVo vo:culentDay.getScds()){										
									if(vo.getSchdIndex()==k){
										isExist=true;
										break;
									}
								}
								if(isExist==false){
									wcs.setSchdIndex(k);
									culentDay.getScds().add(wcs);
									isEmpty=true;
									break;
								}
							}
							if(isEmpty==false){
								wcs.setSchdIndex(maxIndex+1);
								culentDay.getScds().add(wcs);
								Collections.sort(culentDay.getScds(), new IndexAscCompare());
								if(culentDay.getScds().size()>0)
									culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							}
						}else{
							wcs.setSchdIndex(1);
							culentDay.getScds().add(wcs);
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
						}
						continue;
					}						
					//중간날짜
					if(startCal.before(currentCal)&&endCal.after(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else{
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							
							culentDay.getScds().add(wcs);
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0){
								int maxIndex=-1;
								 
								for(CtSchdlBVo vo:culentDay.getScds()){
									if(vo.getSchdIndex()>maxIndex){
										maxIndex=vo.getSchdIndex();
									}
								}
								if(maxIndex==-1){
								
									maxIndex=1;
									wcs.setSchdIndex(1);
								}
								
								culentDay.setScdMaxIndex(maxIndex);
							}
							continue;
						}
					}
					
					//종료날짜
					if(endCal.equals(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else{
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							
							if(!startCal.equals(endCal)){
								//종일일정이 아니면서 종료시간이 12시 이상일경우에만 일정을 매핑한다.
								if((wcs.getAlldayYn() == null || "N".equals(wcs.getAlldayYn())) && "00".equals(wcs.getSchdlEndDt().substring(11,13)) ){
									continue;
								}
								
								culentDay.getScds().add(wcs);	
								Collections.sort(culentDay.getScds(), new IndexAscCompare());
								if(culentDay.getScds().size()>0){
									int maxIndex=-1;
									 
									for(CtSchdlBVo vo:culentDay.getScds()){
										if(vo.getSchdIndex()>maxIndex){
											maxIndex=vo.getSchdIndex();
										}
									}
									culentDay.setScdMaxIndex(maxIndex);
								}
									
							}								
							continue;
						}
					}
					
					
//						if(calenderYear == sYear &&calenderMonth  == sMonth && calenderDay == sDate){
//							
//						}

				}
			}
			
		}
		
		for(int i=0; i < monthObj.getWeeks().size(); i++){
			for(int j=0; j < 7; j++){
				
				CtScdCalDay culentDay=monthObj.getWeeks().get(i).getDays().get(j);
				
				int calenderYear=culentDay.getYear();
				int calenderMonth= culentDay.getMonth();
				int calenderDay=culentDay.getDay();
				
				Calendar currentCal = Calendar.getInstance ( );
				currentCal.set ( calenderYear, calenderMonth - 1, calenderDay );
				currentCal.set( Calendar.HOUR_OF_DAY, 0 );
				currentCal.set( Calendar.MINUTE, 0 );
				currentCal.set( Calendar.SECOND, 0 );
				currentCal.set( Calendar.MILLISECOND, 0 );
				
				int maxScdIndex=culentDay.getScdMaxIndex();
				for(int idx=1; idx<maxScdIndex; idx++){
					boolean emptyflag=false;
					for(CtSchdlBVo scd:culentDay.getScds()){
						if(idx==scd.getSchdIndex()){
							emptyflag=true;
							break;
						}
					}
					if(!emptyflag){
						CtSchdlBVo emptyScd=new CtSchdlBVo();
						emptyScd.setSchdIndex(idx);
						String scdTypeCd="";
						if(j==0){
							for(CtSchdlBVo beforScd:monthObj.getWeeks().get(i-1).getDays().get(6).getScds()){
								if(beforScd.getSchdIndex()==idx){
									scdTypeCd=beforScd.getSchdlTypCd();
									break;
								}
							}
						}else{
							for(CtSchdlBVo beforScd:monthObj.getWeeks().get(i).getDays().get(j-1).getScds()){
								if(beforScd.getSchdIndex()==idx){
									scdTypeCd=beforScd.getSchdlTypCd();
									break;
								}
							}
						}						
						emptyScd.setSchdlTypCd(scdTypeCd);
						culentDay.getScds().add(emptyScd);
						Collections.sort(culentDay.getScds(), new IndexAscCompare());
					}
				}
				
			}
		}
		
		return monthObj;	
		  
	}	
	

	public static class IndexAscCompare implements Comparator<CtSchdlBVo> {
		 
		/**
		 * 오름차순(ASC)
		 */	
		@Override
		public int compare(CtSchdlBVo fisrtSchd, CtSchdlBVo secondSchd) {			
			return fisrtSchd.getSchdIndex()-secondSchd.getSchdIndex();
		}

	}
	
	public static class IndexSchdlStartDateAscCompare implements Comparator<CtSchdlBVo> {
		 
		/**
		 * 오름차순(ASC)
		 */	
		@Override
		public int compare(CtSchdlBVo fisrtSchd, CtSchdlBVo secondSchd) {	
			Calendar firstStartCal = stringConvertCal(fisrtSchd.getSchdlStartDt(),false);
			Calendar secondStartCal = stringConvertCal(secondSchd.getSchdlStartDt(),false);
			return ((firstStartCal.getTimeInMillis()-secondStartCal.getTimeInMillis())>0?1:-1);
		}

	}
	
	
	
	public void repetSetup(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
			List<CtSchdlPromGuestDVo> wcGuestVo, QueryQueue queryQueue, HttpServletRequest request) {
					
		if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY")){
			repetDay(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK")){
			repetWeek(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null);
		}
	}
	
	private void repetYely(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
			List<CtSchdlPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList) {
		
		try{
			
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			
			int firYelySelect =  Integer.parseInt(wcRepetVo.getRepetMm());
		
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}
			
			
			int strtYear = repetStartCal.get(Calendar.YEAR); 
			int strtMonth = repetStartCal.get(Calendar.MONTH); 
			int endYear = repetEndCal.get(Calendar.YEAR); 
			int endMonth = repetEndCal.get(Calendar.MONTH); 
			int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
			
			int startYear=0;
			int startMonth=0;
			//int startWeek=0;
			int startWeekDay=0;
			Calendar startCal = (Calendar) scdStartCal.clone();
			
			if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
				int firYelyDaySelect=Integer.parseInt(wcRepetVo.getApntDd());
				for(int i=0; i<=(monthGap/12)+1; i++){
					
					startYear=repetStartCal.get(Calendar.YEAR)+i;
					startMonth=firYelySelect;
					startWeekDay=firYelyDaySelect;
					startCal.set(startYear,startMonth-1,startWeekDay);
					
					if(startCal.get(Calendar.DATE)!=startWeekDay)
						continue;
					
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){						
						
						
						CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
						scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));

						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
						
							
							cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							//나의일정cd가 -1 이면 공통기념일.
							if(!cloneWcsVo.getSchdlKndCd().equalsIgnoreCase("-1")){
								// 게시파일 저장
								//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
								ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							}
							
							
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
									CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
					
					}
				}
			
			}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
				int secYelyWeekSelect=Integer.parseInt(wcRepetVo.getApntWk());
				int secYelyWeekOfDaySelect=Integer.parseInt(wcRepetVo.getApntDy());
				for(int i=0; i<=(monthGap/12)+1; i++){
					
					startYear=repetStartCal.get(Calendar.YEAR)+i;
					startMonth=firYelySelect;
					startWeekDay=secYelyWeekOfDaySelect;
					CtScdCalMonth calMonth=getScdMonth(startYear,startMonth,null,null,null,false);
					CtScdCalWeek wcWeek=calMonth.getWeeks().get(secYelyWeekSelect-1);
					CtScdCalDay wcDay=wcWeek.getDays().get(secYelyWeekOfDaySelect-1);
					
					if(wcDay.getMonth()!=startMonth)
						continue;
					
					startCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						
						CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
						//scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdEndCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
																	
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
						
						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							
							cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							// 게시파일 저장
							//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue , "indiv");
							ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
									CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
						
						
						
					}
				
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	private void repetMoly(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
			List<CtSchdlPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList) {
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			
			int firMolySelect =  Integer.parseInt(wcRepetVo.getRepetMm());
		
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}
			
			
			int strtYear = repetStartCal.get(Calendar.YEAR); 
			int strtMonth = repetStartCal.get(Calendar.MONTH); 
			int endYear = repetEndCal.get(Calendar.YEAR); 
			int endMonth = repetEndCal.get(Calendar.MONTH); 
			int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
			
			int startYear=0;
			int startMonth=0;
			//int startWeek=0;
			int startWeekDay=0;
			Calendar startCal = (Calendar) scdStartCal.clone();
			
			if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
				for(int i=0; i<=(monthGap/firMolySelect)+1; i++){
					int firMolyDaySelect =  Integer.parseInt(wcRepetVo.getApntDd());
					
					startYear=repetStartCal.get(Calendar.YEAR);
					startMonth=(repetStartCal.get(Calendar.MONTH)+1)+(i*firMolySelect);
					startWeekDay=firMolyDaySelect;
					startCal.set(startYear,startMonth-1,startWeekDay);
					
					if(startCal.get(Calendar.DATE)!=startWeekDay)
						continue;
					
					
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
						scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
						
						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							
							cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
							cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
							// 게시파일 저장
							//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
							ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
						
						
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
									CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
					}
					
				}
				
			}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
				for(int i=0; i<=(monthGap/firMolySelect)+1; i++){								
					startYear=repetStartCal.get(Calendar.YEAR);
					startMonth=(repetStartCal.get(Calendar.MONTH)+1)+(i*firMolySelect);
					CtScdCalMonth calMonth=getScdMonth(startYear,startMonth,null,null,null,false);
					int secMolyWeekSelect = Integer.parseInt(wcRepetVo.getApntWk());		
					int secMolyWeekOfDaySelect =  Integer.parseInt(wcRepetVo.getApntDy());
					
//					WcScdCalWeek wcWeek=calMonth.getWeeks().get(secMolyWeekSelect-1);
//					WcScdCalDay wcDay=wcWeek.getDays().get(secMolyWeekOfDaySelect-1);
					//CtScdCalWeek wcWeek=null;
					CtScdCalDay wcDay=null;
					
					int choiceWeekSelect=0;
					for(int t=0; t<calMonth.getWeeks().size(); t++){
						CtScdCalWeek wcWeekTmp=calMonth.getWeeks().get(t);
						for(int p=0; p<wcWeekTmp.getDays().size(); p++){
							CtScdCalDay wcDayTmp=wcWeekTmp.getDays().get(p);
							if(wcDayTmp.getMonth()!=startMonth)
								continue;
							
							if((secMolyWeekOfDaySelect-1)==p){
								choiceWeekSelect++;
								if(choiceWeekSelect==secMolyWeekSelect){
									if(wcDayTmp.getMonth()!=startMonth)
										continue;
									//wcWeek=wcWeekTmp;
									wcDay=wcDayTmp;
									break;
								}
							}
						}
					}
					
					if(wcDay==null)
						continue;
					
					startCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
	
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
						scdEndCal.set(wcDay.getYear(),wcDay.getMonth()-1, wcDay.getDay());
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
						
						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							// 게시파일 저장
							//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
							ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
									CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	private void repetWeek(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
			List<CtSchdlPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList) {
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			int repetWele = Integer.parseInt(wcRepetVo.getRepetWk());
			String dow =  wcRepetVo.getApntDy();
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}else{
				
				int strtYear = repetStartCal.get(Calendar.YEAR); 
				int strtMonth = repetStartCal.get(Calendar.MONTH); 
				int endYear = repetEndCal.get(Calendar.YEAR); 
				int endMonth = repetEndCal.get(Calendar.MONTH); 
				int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
				//Map<String, CtScdCalMonth> calenderMap=new HashMap<String, CtScdCalMonth>();
				
				
				int startYear=0;
				int startMonth=0;
				int startWeek=0;
				int startWeekDay=0;
				Calendar startCal = Calendar.getInstance();
				
				//boolean choiceWeek=false;
				for(int i=0; i<=monthGap; i++){
					if(i>0)
						repetStartCal.add(Calendar.MONTH, 1);
					CtScdCalMonth calMonth=getScdMonth(repetStartCal.get(Calendar.YEAR),repetStartCal.get(Calendar.MONTH)+1,null,null,null,false);
					
					for(int weekIdx=0; weekIdx < calMonth.getWeeks().size(); weekIdx++){
						CtScdCalWeek  week=calMonth.getWeeks().get(weekIdx);
						for(int j=0; j<7; j++){
							CtScdCalDay day=week.getDays().get(j);
							Calendar currentCal = Calendar.getInstance();
							currentCal.set(day.getYear(),day.getMonth()-1,day.getDay());
							currentCal.set( Calendar.HOUR_OF_DAY, 0 );
							currentCal.set( Calendar.MINUTE, 0 );
							currentCal.set( Calendar.SECOND, 0 );
							currentCal.set( Calendar.MILLISECOND, 0 );
							
							
							if(i==0&&repetStartCal.after(currentCal)){
								//choiceWeek=false;
								continue;
							}else{
								if(startMonth==0 && startWeekDay==0){
									//choiceWeek=true;
									startYear=day.getYear();
									startMonth=week.getDays().get(0).getMonth();
									startWeek=weekIdx;//현재것을 셋팅
									startWeekDay=week.getDays().get(0).getDay();
									startCal = Calendar.getInstance();
									startCal.set(startYear,startMonth-1,startWeekDay);
									startCal.set( Calendar.HOUR_OF_DAY, 0 );
									startCal.set( Calendar.MINUTE, 0 );
									startCal.set( Calendar.SECOND, 0 );
									startCal.set( Calendar.MILLISECOND, 0 );
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										//if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
										if(stringConvertCal(repetDateFormat.format(scdStartCal.getTime()),true).after(repetEndCal)){
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
											
									
										
											cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											
											// 게시파일 저장
											//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											queryQueue.insert(cloneWcsVo);
											if(wcGuestVo.size() != 0){					
												for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
													CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
										
									}
									if(j==6){
										//다음 주차 첫 날 입력..
										startCal.add(Calendar.DATE, repetWele*7);
										startYear=startCal.get(Calendar.YEAR);
										startMonth=startCal.get(Calendar.MONTH)+1;
										startWeek=-1;//현재것을 셋팅
										startWeekDay=startCal.get(Calendar.DATE);
									}
									
									
								}else if(startWeek==weekIdx){
									//월화수목금토일 검사후 일정 입력
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										//if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
										if(stringConvertCal(repetDateFormat.format(scdStartCal.getTime()),true).after(repetEndCal)){										
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
										
											cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											// 게시파일 저장
											//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											
											queryQueue.insert(cloneWcsVo);
											if(wcGuestVo.size() != 0){					
												for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
													CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
									
									}
									
									
									
									if(j==6){
										//다음 주차 첫 날 입력..
										startCal.add(Calendar.DATE, repetWele*7);
										startYear=startCal.get(Calendar.YEAR);
										startMonth=startCal.get(Calendar.MONTH)+1;
										startWeek=-1;//현재것을 셋팅
										startWeekDay=startCal.get(Calendar.DATE);
									}
									
									
								}else if(startYear==day.getYear()
										&&startMonth==day.getMonth()
										&&startWeekDay==day.getDay()){									
									startWeek=weekIdx;
									
									//월화수목금토일 검사후 일정 입력
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
										
											cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											// 게시파일 저장
											//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											queryQueue.insert(cloneWcsVo);
											if(wcGuestVo.size() != 0){					
												for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
													CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
									
									}
									
									
								}
							}
								
						}
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	//시간 초기화
	public static Calendar getConvertYmd(Calendar dt){
		Calendar convertCal = Calendar.getInstance();
		convertCal.set(dt.get(Calendar.YEAR),dt.get(Calendar.MONTH),dt.get(Calendar.DATE));
		convertCal.set( Calendar.HOUR_OF_DAY, 0 );
		convertCal.set( Calendar.MINUTE, 0 );
		convertCal.set( Calendar.SECOND, 0 );
		convertCal.set( Calendar.MILLISECOND, 0 );
		return convertCal;
	}

	public void repetDay(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
		List<CtSchdlPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList){
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			String repetDay =  wcRepetVo.getRepetDd();
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			long difference;
			int gapday = 0;
			int scdGapDay = 0;
			int repetCount =0;
			String schdlPid = wcsVo.getSchdlId();
			if(repetStartCal.before(getConvertYmd(scdEndCal))){
				repetStartCal=scdEndCal;
			}
			if(getConvertYmd(scdEndCal).after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}else{
	
				//반복 시작 끝날짜 차이계산
				difference = (repetEndCal.getTimeInMillis() - repetStartCal.getTimeInMillis())/1000;
				gapday = (int) (difference/(24*60*60));
				
				//일정 시작일정 과 끝일의 차이를 구한다.
				difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
				scdGapDay = (int) (difference/(24*60*60));
				
				repetCount  = gapday/Integer.parseInt(repetDay);
				
				int hour = Integer.parseInt(scdEndDay.substring(11,13));
				int minuit = Integer.parseInt(scdEndDay.substring(14,16));
				repetStartCal.set( Calendar.HOUR_OF_DAY, hour );
				repetStartCal.set( Calendar.MINUTE, minuit );
				scdEndCal = (Calendar)repetStartCal.clone();
				
				for(int i=0; i <= repetCount; i++){					
					
					CtSchdlBVo cloneWcsVo = (CtSchdlBVo) wcsVo.clone();				
					if(getConvertYmd(scdStartCal).equals(getConvertYmd(repetStartCal)) || i > 0 )
						scdEndCal.add(Calendar.DATE, Integer.parseInt(repetDay));
					scdStartCal=(Calendar) scdEndCal.clone();
					
					hour = Integer.parseInt(scdStartDay.substring(11,13));
					minuit = Integer.parseInt(scdStartDay.substring(14,16));
					scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
					scdStartCal.set( Calendar.MINUTE, minuit );					
					scdEndCal.add(Calendar.DATE, scdGapDay);
					
					cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
					cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
					
					if(getConvertYmd(scdStartCal).after(repetEndCal) ){//||getConvertYmd(scdStartCal).equals(repetEndCal)
						break;
					}
					//행사 일 경우
					if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
						int afterDay = Integer.parseInt(request.getParameter("afterDay"));
						String endTm = request.getParameter("endTm");
						String strtTm = request.getParameter("strtTm");
						
						for(int j=0; j<afterDay; j++){
							CtSchdlBVo cloneAvntWcsVo=(CtSchdlBVo) cloneWcsVo.clone();
							try {
								cloneAvntWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));								
							} catch (SQLException e1) {
								
								e1.printStackTrace();
							}
							
							// 게시파일 저장
							ctFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
							int fileSaveSize=queryQueue.size();
							cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
							
							Calendar schdlStartCal=Calendar.getInstance();
							schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
							schdlStartCal.add(Calendar.DATE, j);
							Calendar schdlEndCal=Calendar.getInstance();
							schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
							schdlEndCal.add(Calendar.DATE, j);
							DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
							
							//시작 일 +시 간
							cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
							//종료 일 +시간
							cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
							queryQueue.insert(cloneAvntWcsVo);
							// 게시파일 저장
							
						}
					}else{	
					
						
						
						cloneWcsVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));					
						cloneWcsVo.setSchdlPid(schdlPid);
						
						// 게시파일 저장
						//ctFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
						ctFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
						
						queryQueue.insert(cloneWcsVo);
						if(wcGuestVo.size() != 0){					
							for(CtSchdlPromGuestDVo wcguest:  wcGuestVo){	
								CtSchdlPromGuestDVo cloneWcGuest=(CtSchdlPromGuestDVo) wcguest.clone();
								cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
								queryQueue.insert(cloneWcGuest);							
							}
						}	
					}
						
					
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	

//	public static void main(String[] arg){
//		
//	}
	public static Calendar stringConvertCal(String date,boolean resetHHmm){
		//date ex)2014-05-30
		int Year = Integer.parseInt(date.substring(0,4));
		int Month =Integer.parseInt(date.substring(5,7));
		int Date = Integer.parseInt(date.substring(8,10));
		
		Calendar convertCal = Calendar.getInstance();
		convertCal.set(Year,Month-1,Date);
		if(resetHHmm){
			convertCal.set( Calendar.HOUR_OF_DAY, 0 );
			convertCal.set( Calendar.MINUTE, 0 );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}else{
			int hour = Integer.parseInt(date.substring(11,13));
			int minuit = Integer.parseInt(date.substring(14,16));
			convertCal.set( Calendar.HOUR_OF_DAY, hour );
			convertCal.set( Calendar.MINUTE, minuit );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}
		
		return convertCal;
	}
	
	public static Calendar stringConvertCal(String date,boolean resetHHmm,String timeZone){
		//date ex)2014-05-30
		int Year = Integer.parseInt(date.substring(0,4));
		int Month =Integer.parseInt(date.substring(5,7));
		int Date = Integer.parseInt(date.substring(8,10));
		
		Calendar convertCal = Calendar.getInstance();
		convertCal.setTimeZone(TimeZone.getTimeZone(timeZone));
		convertCal.set(Year,Month-1,Date);
		if(resetHHmm){
			convertCal.set( Calendar.HOUR_OF_DAY, 0 );
			convertCal.set( Calendar.MINUTE, 0 );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}else{
			int hour = Integer.parseInt(date.substring(11,13));
			int minuit = Integer.parseInt(date.substring(14,16));
			convertCal.set( Calendar.HOUR_OF_DAY, hour );
			convertCal.set( Calendar.MINUTE, minuit );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}
		
		return convertCal;
	}
	

	//날짜 시작일과 종료일 사이 계산
	public String betweenDay(String schdlStartDt, String schdlEndDt) {
		long startDay,endDay; 
		
		// Cal객체와 MilliSecond 로 변환 
		startDay = stringConvertCal(schdlStartDt, true).getTime().getTime(); 
		endDay = stringConvertCal(schdlEndDt, true).getTime().getTime();  
		
		// 계산 
	    int days =(int)((endDay-startDay)/(1000*60*60*24)); 
	    //ex) (2014-05-20) - (2014-05-15) = 5일이지만 당일 포함 6일간 이니 +1 해줌
		return String.valueOf(days+1);
	}
	@SuppressWarnings("unchecked")
	public List<CtSchdlPromGuestDVo> getPromGuestLst(CtSchdlPromGuestDVo wcPromGuest) throws SQLException {
		return (List<CtSchdlPromGuestDVo>)commonDao.queryList(wcPromGuest);
	}
	
	
	//일정보내기 스케쥴 조회
	public List<CtSchdlBVo> getSchdlSendMail(String startDay, String option, HttpServletRequest request, CmEmailBVo cmEmailBVo ){
		//참석자 조회용 compId 
		UserVo userVo = LoginSession.getUser(request);
		
		List<CtSchdlBVo> wcsList = new ArrayList<CtSchdlBVo>();
		
		int year = Integer.parseInt(startDay.substring(0,4));
		int month =Integer.parseInt(startDay.substring(5,7));
		int day = Integer.parseInt(startDay.substring(8,10));
		int startWeek =0;
		
		CtScdCalMonth wcsMonth = getScdMonth(year, month, null,request.getParameter("ctId"),null, true);
		//옵션 : M:월간/W:주간/D:일일/
		if(option.equalsIgnoreCase("M")){
			for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					for(int x=0; x < wcsMonth.getWeeks().get(i).getDays().get(j).getScds().size(); x++){
						if(wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x) != null
						&& wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x).getSchdlId() != null){
							wcsList.add( wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x));
						}
					}
				}
			}
			cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월 일정");
			
		}else if(option.equalsIgnoreCase("W")){
	//해당 일의 주차 를 찾는다.
	findWeek :for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					if(wcsMonth.getWeeks().get(i).getDays().get(j).getDay() == day){
						startWeek = wcsMonth.getWeeks().get(i).getWeek() -1;
						break findWeek;
					}
				}
			}
			 //해당 주차 의 schld를 구한다.
			 for(int i=0; i < 7; i++){
				for(int j=0; j < wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().size(); j++){
					if(wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j) != null
					&& wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j).getSchdlId() != null){
						wcsList.add( wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j));
					}
				}
			}
			 
			 cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월" + wcsMonth.getWeeks().get(startWeek).getWeek() +" 째주 일정");
			
		}else if(option.equalsIgnoreCase("D")){
			findDay : for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					//day 비교 후 같은 day schdl만 구해온다.
					if(wcsMonth.getWeeks().get(i).getDays().get(j).getDay() == day){
						for(int x=0; x < wcsMonth.getWeeks().get(i).getDays().get(j).getScds().size(); x++){
							if(wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x) != null 
									&& wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x).getSchdlId() != null){
								wcsList.add( wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x));
							}
						}
						break findDay;
					}
					
				}
			}
			
			cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월" + day +" 일 일정");
		}
		
		return wcsList;
		
	}
	
	/** 참석자 정보 저장 */
	public List<CtSchdlPromGuestDVo> saveGuest(HttpServletRequest request,QueryQueue queryQueue , CtSchdlBVo wcsVo) throws Exception{
		CtSchdlPromGuestDVo deleteGuestVo = new CtSchdlPromGuestDVo();
		deleteGuestVo.setSchdlId(wcsVo.getSchdlId());
		queryQueue.delete(deleteGuestVo);
		
		@SuppressWarnings("unchecked")
		List<CtSchdlPromGuestDVo> wcPromGuestDVoList = (List<CtSchdlPromGuestDVo>)VoUtil.bindList(request, CtSchdlPromGuestDVo.class, new String[]{"guestNm"});
		
		// 등록할 목록이 있을경우 등록처리한다.
		if(wcPromGuestDVoList.size() > 0 ){
			for(CtSchdlPromGuestDVo insertGuestVo : wcPromGuestDVoList){
				insertGuestVo.setStatCd("T");
				insertGuestVo.setSchdlId(wcsVo.getSchdlId());
				insertGuestVo.setEmailSendYn(wcsVo.getEmailSendYn());
				queryQueue.insert(insertGuestVo);
			}
		}
		
		return wcPromGuestDVoList;
	}
	
	
	/** 카테고리 삭제 */
	public void deleteCatCls(QueryQueue queryQueue, CtSchdlCatClsBVo delCatClsBVo) throws Exception{
		CtSchdlCatClsBVo ctCatClsBVo = (CtSchdlCatClsBVo)commonDao.queryVo(delCatClsBVo);
		if(ctCatClsBVo != null ){
			CtRescBVo catVo = new CtRescBVo();			
			catVo.setRescId(ctCatClsBVo.getRescId());
			//리소스 삭제
			queryQueue.delete(catVo);
			
			//카테고리 삭제
			queryQueue.delete(delCatClsBVo);
		}
	}
	
	//일정 삭제및 참석자 삭제
	public void deleteSchdl(String schdlId, String ctId ,QueryQueue queryQueue, HttpServletRequest request) throws Exception{
		if(schdlId == null || schdlId.isEmpty() || ctId == null || ctId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
		ctSchdlBVo.setCtId(ctId);
		ctSchdlBVo.setSchdlId(schdlId);
		queryQueue.delete(ctSchdlBVo);
		
		CtSchdlPromGuestDVo ctSchdlPromGuestDVo = new CtSchdlPromGuestDVo();
		ctSchdlPromGuestDVo.setSchdlId(ctSchdlBVo.getSchdlId());
		queryQueue.delete(ctSchdlPromGuestDVo);
		
	}
	
	//일정 관리자 삭제및 참석자 삭제
		public void deleteSchdl(String schdlId, QueryQueue queryQueue, HttpServletRequest request) throws Exception{
			if(schdlId == null || schdlId.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
			ctSchdlBVo.setSchdlId(schdlId);
			queryQueue.delete(ctSchdlBVo);
			
			CtSchdlPromGuestDVo ctSchdlPromGuestDVo = new CtSchdlPromGuestDVo();
			ctSchdlPromGuestDVo.setSchdlId(ctSchdlBVo.getSchdlId());
			queryQueue.delete(ctSchdlPromGuestDVo);
			
		}
		
	/** 언어코드 변환 */
	public String getCalLangTypCd(String langTypCd){
		if(langTypCd.startsWith("zh")) return "zh-cn";
		return langTypCd;
	}
	
	public void repetSetup(CtSchdlRepetSetupDVo wcRepetVo, CtSchdlBVo wcsVo,
			List<CtSchdlPromGuestDVo> wcGuestVo, QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList) {
					
		if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY")){
			repetDay(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK")){
			repetWeek(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList);
		}
	}
		
}
