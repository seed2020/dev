package com.innobiz.orange.web.wr.svc;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.util.ChineseCalendar;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.util.LunalCalenderUtil;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wr.vo.WrDayVo;
import com.innobiz.orange.web.wr.vo.WrMonthVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;
import com.innobiz.orange.web.wr.vo.WrRezvBVo;
import com.innobiz.orange.web.wr.vo.WrWeekVo;

@Service
public class WrCmSvc {
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** schdlID생성 서비스*/
	@Resource(name = "wcCmSvc")
	private WcCmSvc wcCmSvc;
	
	/** 관리자 서비스 */
	@Autowired
	private WrAdmSvc wrAdmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	@Autowired
	private LunalCalenderUtil lunalCalenderUtil;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("WR_RESC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'R', 8);
		} else if ("WR_RESC_KND_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'K', 8);
		} else if ("WR_RESC_MNG_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'M', 8);
		} else if ("WR_REZV_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'E', 8);
		} 
		
		return null;
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	/**
     * 'yyyy-MM-dd HH24:mm' 형태의 String형을 현재 시간으로 세팅[Null 일경우 현재시간으로 세팅]
     * @param d
     * @return
     */
    public void setStringDateTime( String d ) {
    	Calendar cal = Calendar.getInstance();
        if(d == null){
        	cal.add(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
        }else{
        	d = d.replaceAll("[-: ]", "");//-:공백 제거
        	int year = Integer.parseInt(d.substring(0, 4)); 
            int month = Integer.parseInt(d.substring(4, 6)); 
            int day = Integer.parseInt(d.substring(6,8));
            int hour = Integer.parseInt(d.substring(8,10));
            int min = Integer.parseInt(d.substring(10,12));
            cal.set(Calendar.YEAR, year); 
            cal.set(Calendar.MONTH, month-1); // 0 이 1월, 1 은 2월, .... 
            cal.set(Calendar.DAY_OF_MONTH, day); 
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
        }
    }
    
	//이전 다음 일정 세팅
    public String getDateOfDay(  String startDay , String tabType , String schedulePmValue , String clicknps , int stepValue){
 		if(startDay != null && ( schedulePmValue == null || "".equals(schedulePmValue) )){
 			return startDay;
 		}
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		GregorianCalendar cal = new GregorianCalendar();
 		String daysValue = "";
 		if(startDay != null && !"".equals(startDay)){
 			cal.set(Integer.parseInt(startDay.split("-")[0]) , Integer.parseInt(startDay.split("-")[1])-1 , Integer.parseInt(startDay.split("-")[2]));
 	 		
 	 		if("month".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.MONTH, 1);
 	 			else
 	 				cal.add(Calendar.MONTH, -1);
 	 		}else if("week".equals(tabType) || "weeks".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 7);
 	 			else
 	 				cal.add(Calendar.DATE, -7);
 	 		}else if("day".equals(tabType) ){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 1);
 	 			else
 	 				cal.add(Calendar.DATE, -1);
 	 		}else if( "year".equals(tabType) ){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.YEAR, 1);
 	 			else
 	 				cal.add(Calendar.YEAR, -1);
 	 		}else if("allSchedule".equals(tabType)){
 	 			if("".equals(clicknps)){
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, 1);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -1);
 	 			}else{
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, stepValue);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -stepValue);
 	 			}
 	 		}
 	 		Date days = cal.getTime();
 	 		daysValue = sdf.format(days);
 		}else{
 			daysValue = sdf.format((Date)(cal.getTime()));
 		}
        
 		return daysValue;
 	}
    
    //일간 일정 WrDayVo
    public WrDayVo getDay(  String startDay ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar ();
		cal.set(Integer.parseInt(startDay.split("-")[0]) , Integer.parseInt(startDay.split("-")[1])-1 , Integer.parseInt(startDay.split("-")[2]));
    	String today = sdf.format((Date)(cal.getTime()));//오늘 일자
        
        WrDayVo wrDayVo = new WrDayVo();//일간정보 초기화
        String days = "";
        String year,month,day;
        String dayFmt = "-";
        
        year = String.valueOf(cal.get(Calendar.YEAR ));
        month = cal.get(Calendar.MONTH)+1 < 10 ? "0"+ (cal.get(Calendar.MONTH)+1) : String.valueOf(cal.get(Calendar.MONTH)+1);
        day = cal.get(Calendar.DATE ) < 10 ? "0" + cal.get(Calendar.DATE ) : String.valueOf(cal.get(Calendar.DATE ));
        days = year + dayFmt + month + dayFmt + day;
        wrDayVo.setYear(cal.get(Calendar.YEAR ));
        wrDayVo.setMonth(cal.get(Calendar.MONTH )+1);
        wrDayVo.setDay(cal.get(Calendar.DATE ));
        wrDayVo.setDays(days);//yyyy-MM-dd 세팅
        
        //요일
        Integer dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        wrDayVo.setDayOfWeek(dayOfWeek);
        if(dayOfWeek == 1 ) {//일요일이면
        	wrDayVo.setIsHoliDay("holiday");
        }
        wrDayVo.setSolaYn("Y");
        //오늘이면
        if(today.equals(days)) wrDayVo.setTodayYn("Y");
        
		return wrDayVo;
	}
    
    //주간 일정 WrWeekVo
    public WrWeekVo getWeek(String startDay){
    	String today = StringUtil.getCurrYmd();
    	if(startDay==null || startDay.isEmpty()) startDay=today;
    	if(startDay.length()!=8) startDay=startDay.replaceAll("[-: ]", "");
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, Integer.parseInt(startDay.substring(0,4))) ;
        cal.set(Calendar.MONTH, Integer.parseInt(startDay.substring(4,6))-1) ;
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDay.substring(6,8))) ;
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek!=1) cal.add(Calendar.DATE, -(dayOfWeek-1));
        WrWeekVo wrWeekVo = new WrWeekVo();
        List<WrDayVo> dayList = new ArrayList<WrDayVo>();
        WrDayVo wrDayVo = null;
        String days = "";
        String year,month,day;
        String dayFmt = "-";
        for(int i=1;i<=7; ++i) {
        	//if(i>1) cal.add(Calendar.DATE, 1);
            cal.set(Calendar.DAY_OF_WEEK, i);
            year = String.valueOf(cal.get(Calendar.YEAR ));
            month = cal.get(Calendar.MONTH)+1 < 10 ? "0"+ (cal.get(Calendar.MONTH)+1) : String.valueOf(cal.get(Calendar.MONTH)+1);
            day = cal.get(Calendar.DATE ) < 10 ? "0" + cal.get(Calendar.DATE ) : String.valueOf(cal.get(Calendar.DATE ));
            days = year + dayFmt + month + dayFmt + day;
            wrDayVo = new WrDayVo();//일간정보 초기화
            wrDayVo.setYear(cal.get(Calendar.YEAR ));
            wrDayVo.setMonth(cal.get(Calendar.MONTH )+1);
            wrDayVo.setDay(cal.get(Calendar.DATE ));
            wrDayVo.setDays(days);//yyyy-MM-dd 세팅
            if(i == 1 ) {//일요일이면
            	wrDayVo.setIsHoliDay("holiday");
            	wrWeekVo.setStrtDt(days);//시작일
            }
            if(i == 7 ) wrWeekVo.setEndDt(days);//종료일
            wrDayVo.setSolaYn("Y");
            //오늘이면
            if(today.equals(days)) wrDayVo.setTodayYn("Y");
            dayList.add(wrDayVo);
        }
        wrWeekVo.setWrDayVo(dayList);
		return wrWeekVo;
	}
    
    /** 양력을 음력으로 변환 */
    public String toLunar( String yyyymmdd ) {
        if( yyyymmdd == null ) return "" ;

        Calendar cal ;
        ChineseCalendar cc ;

        // default TimeZone, Locale 을 사용..
        cal = Calendar.getInstance() ;
        cc = new ChineseCalendar();

        String date = yyyymmdd.trim() ;
        if( date.length() != 8 ) {
                if( date.length() == 4 )
                        date = date + "0101" ;
                else if( date.length() == 6 )
                        date = date + "01" ;
                else if( date.length() > 8 )
                        date = date.substring(0,8) ;
                else
                        return "" ;
        }

        cal.set( Calendar.YEAR, Integer.parseInt(date.substring(0,4)) ) ;
        cal.set( Calendar.MONTH, Integer.parseInt(date.substring(4,6))-1 ) ;
        cal.set( Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)) ) ;

        cc.setTimeInMillis( cal.getTimeInMillis() ) ;

        // ChinessCalendar.YEAR 는 1~60 까지의 값만 가지고 ,

        // ChinessCalendar.EXTENDED_YEAR 는 Calendar.YEAR 값과 2637 만큼의 차이를 가집니다.
        int y = cc.get(ChineseCalendar.EXTENDED_YEAR)-2637 ;
        int m = cc.get(ChineseCalendar.MONTH)+1 ;
        int d = cc.get(ChineseCalendar.DAY_OF_MONTH) ;

        StringBuffer ret = new StringBuffer() ;
        if( y < 1000 )          ret.append( "0" ) ;
        else if( y < 100 )      ret.append( "00" ) ;
        else if( y < 10 )       ret.append( "000" ) ;
        ret.append( y ) ;

        if( m < 10 ) ret.append( "0" ) ;
        ret.append( m ) ;

        if( d < 10 ) ret.append( "0" ) ;
        ret.append( d ) ;
        return ret.toString() ;
	}
	
    /** 음력을 양력으로 변환 */
	public String fromLunar( String yyyymmdd ){
        Calendar cal ;
        ChineseCalendar cc ;

        // default TimeZone, Locale 을 사용..
        cal = Calendar.getInstance() ;
        cc = new ChineseCalendar();
        
        if (yyyymmdd == null ) return "" ;

        String date = yyyymmdd.trim() ;

        if( date.length() != 8 ) {
                if( date.length() == 4 )
                        date = date + "0101" ;
                else if( date.length() == 6 )
                        date = date + "01" ;
                else if( date.length() > 8 )
                        date = date.substring(0,8) ;
                else
                        return "" ;
        }

        cc.set( ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0,4))+2637 ) ;
        cc.set( ChineseCalendar.MONTH, Integer.parseInt(date.substring(4,6))-1 ) ;
        cc.set( ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)) ) ;

        int leap_yn = 0;
        
        cc.set(ChineseCalendar.IS_LEAP_MONTH, leap_yn);
        
        cal.setTimeInMillis( cc.getTimeInMillis() ) ;

        int y = cal.get(Calendar.YEAR) ;
        int m = cal.get(Calendar.MONTH)+1 ;
        int d = cal.get(Calendar.DAY_OF_MONTH) ;

        StringBuffer ret = new StringBuffer() ;
        if( y < 1000 )          ret.append( "0" ) ;
        else if( y < 100 )      ret.append( "00" ) ;
        else if( y < 10 )       ret.append( "000" ) ;
        ret.append( y ) ;

        if( m < 10 ) ret.append( "0" );
        ret.append( m ) ;

        if( d < 10 ) ret.append( "0" );
        ret.append( d ) ;

        return ret.toString() ;
	}
    
    /** 공휴일 및 기념일 */
    public void setSpclDt( WrDayVo wrDayVo , String d , String suffix){
    	//일자,기념일명,양력여부,휴일여부
    	String[][] chkList = {{"0101","신정","Y","Y"},{"0301","3.1절","Y","Y"},{"0505","어린이 날","Y","Y"},{"0606","현충일","Y","Y"},{"0815","광복절","Y","Y"},{"1003","개천절","Y","Y"},{"1009","한글날","Y","Y"},{"1225","크리스마스","Y","Y"}
    	,{"1230","구정","N","Y"},{"0101","구정","N","Y"},{"0102","구정","N","Y"},{"0408","석가탄신일","N","Y"},{"0814","추석","N","Y"},{"0815","추석","N","Y"},{"0816","추석","N","Y"}
    	};
    	d = d.replaceAll("[-: ]", "");//-:공백 제거
    	String day = d.substring(4);
    	
    	List<String[]> spclDtList = new ArrayList<String[]>(); 
    	//양력,음력에 대한 공휴일 정보 초기화
    	String[] spclInfos = null;
    	String lunarStr = this.toLunar(d);
    	for(String[] chkDt : chkList){
    		if("Y".equals(chkDt[2]) && chkDt[0].equals(day)){//양력체크
    			spclInfos = chkDt;
    			if("Y".equals(chkDt[3])) wrDayVo.setIsHoliDay("scddate_red"+suffix);
    			spclDtList.add(spclInfos);
    		}else if("N".equals(chkDt[2]) && !"".equals(lunarStr) && lunarStr.substring(4).equals(chkDt[0])){    			
				spclInfos = chkDt;
				if("Y".equals(chkDt[3])) wrDayVo.setIsHoliDay("scddate_red"+suffix);
				spclDtList.add(spclInfos);
    		}
    	}
    	
    	/** 기념일 목록이 있을경우 */
    	if(spclDtList.size() > 0 ){
    		wrDayVo.setSpclDtList(spclDtList);
    		wrDayVo.setLunarDays(lunarStr);
    	}
    }
    
    /** 기념일 세팅 */
    public void setSpclDtList(String days , String[] spclInfos, WrDayVo storedWrDayVo , List<WcSchdlBVo> wcsList){
    	spclInfos = null;
    	//해당 일자에 기념일 목록이 없으면 생성한다.
    	if(storedWrDayVo.getSpclDtList() == null ) {
    		storedWrDayVo.setSpclDtList(new ArrayList<String[]>());
    	}    	
    	for(WcSchdlBVo storedWcSchdlBVo : wcsList){
			days = storedWrDayVo.getDays().replaceAll("[-: ]", "");
			//기념일 목록과 일자를 비교한다.(해당 일자가 기념일 기간에 있으면 기념일을 세팅한다.
			if(Integer.parseInt(days) <= Integer.parseInt(storedWcSchdlBVo.getSchdlEndYmd()) && Integer.parseInt(days) >= Integer.parseInt(storedWcSchdlBVo.getSchdlStrtYmd())){
				//일자,제목,양력여부,휴일여부
				spclInfos = new String[]{days.substring(4),storedWcSchdlBVo.getSubj(),storedWcSchdlBVo.getSolaLunaYn(),storedWcSchdlBVo.getHoliYn()};
				//휴일여부가 'Y'인 경우 해당 일자를 휴일 css 를 적용하기 위해 css class 명을 변경한다.
				if("Y".equals(spclInfos[3])) storedWrDayVo.setIsHoliDay("scddate_red"+( storedWrDayVo.getIsHoliDay() != null && storedWrDayVo.getIsHoliDay().indexOf("prev") > -1 ? "_"+storedWrDayVo.getIsHoliDay().substring(storedWrDayVo.getIsHoliDay().indexOf("prev")) : ""));
				storedWrDayVo.getSpclDtList().add(spclInfos);//세팅된 기념일 정보를 일자에 저장한다.
			}
		}
    }
    
    /** 공휴일 및 기념일 세팅 [일정관리 데이터]*/
    public void setSpclDays( Object dayVo , String durStrtDt , String durEndDt) throws Exception{
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
    	WcSchdlBVo searchVo = new WcSchdlBVo();
    	// 시작 일자와 종료일자 세팅
    	searchVo.setSchdlStartDt(getDateOfDay(getDateOfDay(durStrtDt, "month", "s", null, 1), "month", "s", null, 1));//음력일정을 구하기 위해 두달전 일정까지 조회한다.
    	searchVo.setSchdlEndDt(durEndDt);
    	
    	searchVo.setSchdlTypCd("5");//기념일
    	searchVo.setInstanceQueryId("selectWcSchdlBPlt");
    	searchVo.setCompId(userVo.getCompId());

    	//공통 기념일 조회
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> wcsList = (List<WcSchdlBVo>)commonDao.queryList(searchVo);
		
		//음력일정일 경우 일자와의 비교를 위해 양력일정으로 변경한다.
    	String strtYmd , endYmd;
		for(WcSchdlBVo wcSchdlBVo : wcsList){
			strtYmd = wcSchdlBVo.getSchdlStartDt().replaceAll("[-: ]", "").substring(0,8);
			endYmd = wcSchdlBVo.getSchdlEndDt().replaceAll("[-: ]", "").substring(0,8);
			//strtYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? fromLunar(strtYmd) : strtYmd;
			//endYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? fromLunar(endYmd) : endYmd;
			strtYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? lunalCalenderUtil.toSolar(strtYmd) : strtYmd;
			endYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? lunalCalenderUtil.toSolar(endYmd) : endYmd;
			wcSchdlBVo.setSchdlStrtYmd(strtYmd.replaceAll("[-: ]", ""));
			wcSchdlBVo.setSchdlEndYmd(endYmd.replaceAll("[-: ]", ""));
		}
		
		String days = "";
		String[] spclInfos = null;//기념일 객체 초기화
    	if(dayVo instanceof WrMonthVo){//월간일정
    		List<WrWeekVo> weekList = ((WrMonthVo)dayVo).getWrWeekVo();
    		for(WrWeekVo storedWrWeekVo : weekList){
    			for(WrDayVo storedWrDayVo : storedWrWeekVo.getWrDayVo()){
    				setSpclDtList(days, spclInfos, storedWrDayVo, wcsList);
    			}
    		}
    	}else if(dayVo instanceof WrWeekVo){//주간일정
    		WrWeekVo storedWrWeekVo = ((WrWeekVo)dayVo);
			for(WrDayVo storedWrDayVo : storedWrWeekVo.getWrDayVo()){
				setSpclDtList(days, spclInfos, storedWrDayVo, wcsList);
			}
    	}else{//일간일정
    		WrDayVo storedWrDayVo = ((WrDayVo)dayVo);
    		setSpclDtList(days, spclInfos, storedWrDayVo, wcsList);
    	}
    }
    
    /** 한자리수 월 , 일에 0 조합 */
    public String getMixDt(int d){
    	return d < 10 ? "0"+ d : ""+d;
    }
    
    /** 월간 일자 정보를 조회한다. */
    public WrMonthVo getMonth(String d) {
		Calendar cal = Calendar.getInstance();
		d = d.replaceAll("[-: ]", "");//-:공백 제거
    	int year = Integer.parseInt(d.substring(0, 4)); 
        int month = Integer.parseInt(d.substring(4, 6)); 
        
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
		
		//이전달 구하는 달력 생성
		//beforeLastDay : 전달 28,29,30,31 계산.
		Calendar beforeCal = Calendar.getInstance();
		
		//month가 1월로 오면 이전 달은 12월로 셋팅 하면서  year -1 
		int beforeYear = month == 1 ? year -1 : year;
		int beforeMonth = month == 1 ? 12 : month-1;
				
		beforeCal.set(Calendar.YEAR, beforeYear);
		beforeCal.set(Calendar.MONTH, beforeMonth-1); 
		beforeCal.set(Calendar.DATE, 1);
		beforeLastDay = beforeCal.getActualMaximum(Calendar.DATE);//전월의 일수
		beforeLastDay = (beforeLastDay + 2) - start_day;//시작일을 기준으로 달력에 보여줘야할 전월의 시작일
		
		int afterStartDay = 1;//익월의 시작일
		int startweek = 1;//시작주차
		int endweek = 6;//월마다 주차가 달라서 보여지는 화면의 이질감을 없애기 위해 6주로 통일(화면변화없이 날짜 변경)
		//temp값 까지 계산
		int tempDay;
		//1~마지막일까지.. day
		int day=0;
		
		//월간정보
		WrMonthVo wrMonthVo = new WrMonthVo();
		//주간정보 목록 초기화
		List<WrWeekVo> weekList = new ArrayList<WrWeekVo>();
		//일간정보 목록 초기화
		List<WrDayVo> dayList = null;
		//주간정보
		WrWeekVo wrWeekVo = null;
		//일간정보
		WrDayVo wrDayVo = null;
		//today 설정
		Calendar defaultCal = Calendar.getInstance();

		int defYear = defaultCal.get(Calendar.YEAR);
		int defMonth = defaultCal.get(Calendar.MONTH) +1;
		int defDay = defaultCal.get(Calendar.DAY_OF_MONTH);
		
		String dtFmt = "-";
		
		//1부터 정해진 주차 까지 계산
		for(int i=startweek; i<=endweek; i++){
			dayList = new ArrayList<WrDayVo>();
			for(int j=1; j<=7; j++){//주의 요일만큼 계산
				tempDay=j+((i-1)*7); 
				wrDayVo = new WrDayVo();
				wrDayVo.setSolaYn("Y");
				
				if(start_day > tempDay){
					//시작일 그 이전 날짜 채우는 구간
					//이전월 연한 빨간색 set
					if(j == 1) wrDayVo.setIsHoliDay("scddate_red_prev");
					else if(j == 7) wrDayVo.setIsHoliDay("scddate_prev");
					else wrDayVo.setIsHoliDay("scddate_prev");
					wrDayVo.setDay(beforeLastDay);
					wrDayVo.setTodayYn("N");
					
					//전달 년 월 set
					wrDayVo.setYear(beforeYear);
					wrDayVo.setMonth(beforeMonth);
					
					//전월포함 시작일
					if(i == 1 && j == 1) wrMonthVo.setBeforeStrtDt(beforeYear+dtFmt+getMixDt(beforeMonth)+dtFmt+getMixDt(beforeLastDay));
					//setSpclDt(wrDayVo , beforeYear+""+getMixDt(beforeMonth)+""+getMixDt(beforeLastDay),"_prev");
					wrDayVo.setDays(beforeYear+""+getMixDt(beforeMonth)+""+getMixDt(beforeLastDay));
					//전달 ~ 전달마지막일 add
					dayList.add(wrDayVo);
					beforeLastDay++;
				}else{
					day++;
					//1일부터 end_day까지 구하는 구간
					if(day<=end_day){
						//이번달 년 월 set
						wrDayVo.setMonth(month);
						wrDayVo.setYear(year);
						if(j == 1) wrDayVo.setIsHoliDay("scddate_red");
						else if(j == 7) wrDayVo.setIsHoliDay("scddate");
						else wrDayVo.setIsHoliDay("scddate");
						
						//today yn 표시
						if(defYear == year && defMonth == month && defDay == day ) wrDayVo.setTodayYn("Y");
						else wrDayVo.setTodayYn("N");
						
						//시작일 (전월없이 시작될경우)
						if(i == 1 && j == 1) wrMonthVo.setBeforeStrtDt(year+dtFmt+getMixDt(month)+dtFmt+getMixDt(day));
						//setSpclDt(wrDayVo , year+""+getMixDt(month)+""+getMixDt(day),"");
						wrDayVo.setDays(year+""+getMixDt(month)+""+getMixDt(day));
						//1~마지막일자 입력
						wrDayVo.setDay(day);						
						dayList.add(wrDayVo);
						
					//종료일 그 이후 채우는 구간
					}else{ 
						if(j == 1) wrDayVo.setIsHoliDay("scddate_red_prev");
						else if(j == 7) wrDayVo.setIsHoliDay("scddate_prev");
						else wrDayVo.setIsHoliDay("scddate_prev");
						
						wrDayVo.setDay(afterStartDay);
						wrDayVo.setTodayYn("N");
						
						//다음달 년 월 set
						int nextYear =year;
						int nextMonth =month;
						if(12 >= (nextMonth + 1)) nextMonth+= 1;
						else{
							nextMonth = 1;
							nextYear += 1;
						}
						wrDayVo.setYear(nextYear);
						wrDayVo.setMonth(nextMonth);
						
						//익월포함 종료일
						if(i == endweek && j == 7) wrMonthVo.setAfterEndDt(nextYear+dtFmt+getMixDt(nextMonth)+dtFmt+getMixDt(afterStartDay));
						//setSpclDt(wrDayVo , nextYear+""+getMixDt(nextMonth)+""+getMixDt(afterStartDay),"_prev");
						wrDayVo.setDays(nextYear+""+getMixDt(nextMonth)+""+getMixDt(afterStartDay));
						//마지막일 ~ 다음달 첫주 add
						dayList.add(wrDayVo);
						afterStartDay++;
					}
				}	
				
			}
			wrWeekVo = new WrWeekVo();;
			wrWeekVo.setMonth(month);
			wrWeekVo.setYear(year);
			wrWeekVo.setWeek(i);
			
			wrWeekVo.setWrDayVo(dayList);
			weekList.add(wrWeekVo);
		}
		//월간정보에 주간정보를 담는다.
		wrMonthVo.setWrWeekVo(weekList);
		
		//월의 시작일과 종료일 세팅
		wrMonthVo.setStrtDt(year+dtFmt+(month < 10 ? "0"+ month : month)+dtFmt+"01");
		wrMonthVo.setEndDt(year+dtFmt+(month < 10 ? "0"+ month : month)+dtFmt+end_day);
		
		return wrMonthVo;
	}
    
    /** 일정 등록(자원예약시 심의없음 또는 승인된 정보를 일정에 등록 ) */
    public void saveSchdl(HttpServletRequest request , QueryQueue queryQueue , WrRezvBVo wrRezvBVo , WrRescMngBVo wrRescMngBVo) throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
    	
		// 사용자 정보맵
		Map<String, Object> userMap = orCmSvc.getUserMap(wrRezvBVo.getRegrUid(), "ko");
		
		String compId = (String)userMap.get("compId");
		if(compId==null) compId=userVo.getCompId();
    	//일정
    	WcSchdlBVo wcsVo = new WcSchdlBVo();
    	
    	String schdlId = wrRezvBVo.getSchdlId() == null ||  wrRezvBVo.getSchdlId().isEmpty() ? wcCmSvc.createId("WC_SCHDL_B") : wrRezvBVo.getSchdlId();
    	//schdlId 생성
		wcsVo.setSchdlId(schdlId);
		wcsVo.setSchdlPid(schdlId);
		
		//개인,회사일정등(카테고리)
		//Integer[] schdlTyps = {1,2,3,4};
		
		wcsVo.setCompId(compId);
		wcsVo.setUserUid(wrRezvBVo.getRegrUid());
		
		wcsVo.setDeptId(userVo.getDeptId());
		wcsVo.setRegrUid(wrRezvBVo.getRegrUid());
		wcsVo.setRegrNm(wrRezvBVo.getRegrNm());
		
		//제목
		wcsVo.setSubj(wrRezvBVo.getSubj());
		//장소명
		wcsVo.setLocNm(wrRescMngBVo.getRescLoc());
		//시작 일 +시 간
		wcsVo.setSchdlStartDt(wrRezvBVo.getRezvStrtDt());
		//종료 일 +시간
		wcsVo.setSchdlEndDt(wrRezvBVo.getRezvEndDt());
		//공개여부 1.공개, 2.지정인공개, 3.비공개
		wcsVo.setOpenGradCd("1");
		//내용
		wcsVo.setCont(wrRezvBVo.getCont());
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(compId);
		wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		if(wcCatClsBVoList.size() > 0 ){//일정구분목록이 있을경우 첫번째 구분코드를 일정에 삽입한다.			
			wcsVo.setSchdlTypCd(wcCatClsBVoList.get(0).getCatId());
		}else{
			wcsVo.setSchdlTypCd("1");
		}
		String schdlKndCd=wrRezvBVo.getSchdlKndCd(); // 일정대상코드
		if(schdlKndCd==null || schdlKndCd.isEmpty()){
			// 설정 조회
			Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			
			if(envConfigMap!=null && envConfigMap.containsKey("schdlKndCd"))
				schdlKndCd=envConfigMap.get("schdlKndCd");
			else
				schdlKndCd="1";
		}
		//일정종류코드 1.나의일정, 2.그룹, 3.부서 4.회사
		wcsVo.setSchdlKndCd(schdlKndCd);
		//양음력 값
		wcsVo.setSolaLunaYn("Y");
		//휴일여부 NULL체크
		wcsVo.setHoliYn("N");
		//일정상태코드 T보류/C참석/N불참/D약속취소
		wcsVo.setSchdlStatCd("T");
		//일정반복여부
		wcsVo.setRepetYn("N");
		
		queryQueue.store(wcsVo);
		
    }
    
    /** rezvDt:예약일 별 rowIndex:정렬순서, rowCnt:중복수 설정 */
    public void setRowIndex(List<WrRezvBVo> wrRezvBVoList){
    	String rezvDt = null;
    	List<WrRezvBVo> tempList = new ArrayList<WrRezvBVo>();
    	int rowIndex=0;
    	if(wrRezvBVoList != null && !wrRezvBVoList.isEmpty()){
    		
    		if(wrRezvBVoList.get(0).getRezvDt()==null) return;
    		
    		for(WrRezvBVo wrRezvBVo : wrRezvBVoList){
    			
    			if(rezvDt==null || !rezvDt.equals(wrRezvBVo.getRezvDt())){
    				if(!tempList.isEmpty()){
    					for(WrRezvBVo vo : tempList){
    						vo.setRowCnt(rowIndex);
    					}
    					tempList.clear();
    				}
    				rowIndex=0;
    				rezvDt = wrRezvBVo.getRezvDt();
    			}
    			rowIndex++;
    			wrRezvBVo.setRowIndex(rowIndex);
    			tempList.add(wrRezvBVo);
    		}
    		
    		if(!tempList.isEmpty()){
				for(WrRezvBVo vo : tempList){
					vo.setRowCnt(rowIndex);
				}
			}
    	}
    }
    
    /** 일정에 대한 순서 세팅 */
    public void setRowIndex(Object dayVo , List<WrRezvBVo> wrRezvBVoList ,ModelMap model){
    	List<WrRezvBVo> list = new ArrayList<WrRezvBVo>();
    	if(dayVo instanceof WrMonthVo){//월간일정
    		List<WrWeekVo> weekList = ((WrMonthVo)dayVo).getWrWeekVo();
    		for(WrWeekVo storedWrWeekVo : weekList){
    			list.addAll(getRezvList(storedWrWeekVo, wrRezvBVoList));
    		}
    	}else if(dayVo instanceof WrWeekVo){//주간일정
    		WrWeekVo storedWrWeekVo = ((WrWeekVo)dayVo);
    		list.addAll(getRezvList(storedWrWeekVo, wrRezvBVoList));
    	}else{//일간일정
    		WrDayVo storedWrDayVo = ((WrDayVo)dayVo);
    		list.addAll(getSortRezvList(storedWrDayVo, wrRezvBVoList));
    	}
    	
    	model.put("wrRezvBVoList", list);
    }
    
    /** 예약일정을 시간대별로 임의 생성한다. */
    public List<WrRezvBVo> getSortRezvList(WrDayVo storedWrDayVo , List<WrRezvBVo> wrRezvBVoList){
    	List<WrRezvBVo> list = new ArrayList<WrRezvBVo>();
    	if(wrRezvBVoList.size() > 0){
    		int days = Integer.parseInt(storedWrDayVo.getDays().replaceAll("[-: ]", ""));
    		int strtSi=7,endSi=24;
    		
    		String strtTime = "0"+strtSi+"00"; 
    		String endTime = endSi+"00";
			int strtYmd,endYmd;
			WrRezvBVo wrRezvBVo = null;
			for(WrRezvBVo storedWrRezvBVo : wrRezvBVoList){
				strtYmd = Integer.parseInt(storedWrRezvBVo.getRezvStrtYmd().replaceAll("[-: ]", ""));
	    		endYmd = Integer.parseInt(storedWrRezvBVo.getRezvEndYmd().replaceAll("[-: ]", ""));
	    		if(strtYmd <= days && endYmd >= days){
	    			wrRezvBVo = new WrRezvBVo();
	    			BeanUtils.copyProperties(storedWrRezvBVo, wrRezvBVo);
	    			wrRezvBVo.setRezvStrtYmd(String.valueOf(days));
    				wrRezvBVo.setRezvEndYmd(String.valueOf(days));
	    			//일정의 시작일과 종료일이 해당 일의 범위 밖이면 일의 시작시간과 종료시간을 세팅한다.
	    			if(strtYmd < days && endYmd > days){
	    				wrRezvBVo.setRezvStrtTime(strtTime);
	    				wrRezvBVo.setRezvEndTime(endTime);
	    				wrRezvBVo.setPrevRezvYn("Y");//이전 일정 여부
	    				wrRezvBVo.setNextRezvYn("Y");//이후 일정 여부
    				//일정의 시작일이 해당일 이전이고 종료일이 해당 일 안이면 일의 시작시간 , 일정의 종료시간을 세팅한다.
	    			}else if(strtYmd < days && endYmd == days){
	    				wrRezvBVo.setRezvStrtTime(strtTime);
	    				wrRezvBVo.setRezvEndTime(storedWrRezvBVo.getRezvEndTime().replaceAll("[-: ]",""));
	    				wrRezvBVo.setPrevRezvYn("Y");//이전 일정 여부
	    			//일정의 종료일이 주의 종료일 이후이고 시작일이 해당 주 범위 안이면 일정의 시작일 , 주의 종료일을 세팅한다.
	    			}else if(endYmd > days && strtYmd == days){
	    				wrRezvBVo.setRezvStrtTime(storedWrRezvBVo.getRezvStrtTime().replaceAll("[-: ]",""));
	    				wrRezvBVo.setRezvEndTime(endTime);
	    				wrRezvBVo.setNextRezvYn("Y");//이후 일정 여부
	    			}else{
	    				wrRezvBVo.setRezvStrtTime(wrRezvBVo.getRezvStrtTime().replaceAll("[-: ]",""));
	    				wrRezvBVo.setRezvEndTime(wrRezvBVo.getRezvEndTime().replaceAll("[-: ]",""));
	    			}
	    			if(wrRezvBVo.getTimeCnt() == null || wrRezvBVo.getTimeCnt().isEmpty())
	    				wrRezvBVo.setTimeCnt(String.valueOf(getDateDiff(String.valueOf(days)+wrRezvBVo.getRezvEndTime(),String.valueOf(days)+wrRezvBVo.getRezvStrtTime(),"min")/30));
	    			list.add(wrRezvBVo);
	    			
	    		}
			}
			if(list.size() > 0){
				Comparator<WrRezvBVo> comparator = new Comparator<WrRezvBVo>(){
					int strtTime,tmpStrtTime;
					@Override
					public int compare(WrRezvBVo o1, WrRezvBVo o2) {
						strtTime = Integer.parseInt(o1.getRezvStrtTime().replaceAll("[-: ]", ""));
			    		tmpStrtTime = Integer.parseInt(o2.getRezvStrtTime().replaceAll("[-: ]", ""));
						return (strtTime < tmpStrtTime ? -1 : ( strtTime == tmpStrtTime ? 0 : 1))+( Integer.parseInt(o1.getTimeCnt()) > Integer.parseInt(o2.getTimeCnt()) ? -1 : 0);
					}
				};
				// 시간순 정렬
				Collections.sort(list , comparator);
				
				int tempStrtYmd,tempEndYmd;
		    	Map<String,List<WrRezvBVo>> tempMap = new HashMap<String,List<WrRezvBVo>>();
		    	List<WrRezvBVo> indexList = null;
		    	for(WrRezvBVo storedWrRezvBVo : list){
		    		strtYmd = Integer.parseInt(storedWrRezvBVo.getRezvStrtTime().replaceAll("[-: ]", ""));
		    		endYmd = Integer.parseInt(storedWrRezvBVo.getRezvEndTime().replaceAll("[-: ]", ""));
		    		indexList = new ArrayList<WrRezvBVo>();
	    			for(WrRezvBVo tempVo : list){
	    				if( storedWrRezvBVo.getRezvId().equals(tempVo.getRezvId())) continue;
	        			tempStrtYmd = Integer.parseInt(tempVo.getRezvStrtTime().replaceAll("[-: ]", ""));
	        			tempEndYmd = Integer.parseInt(tempVo.getRezvEndTime().replaceAll("[-: ]", ""));
	        			if( ( strtYmd >= tempStrtYmd && strtYmd < tempEndYmd ) || (
	        					tempStrtYmd>=strtYmd && tempStrtYmd<endYmd )){
	        				if(!tempMap.containsKey("rezvId_"+tempVo.getRezvId()+tempVo.getRezvStrtYmd())) {	        					
	        					tempVo.setRowIndex(storedWrRezvBVo.getRowIndex()+1);
	        				}
	        				indexList.add(tempVo);
	        			}
	        		}
	    			storedWrRezvBVo.setRowCnt(indexList.size());
	    			tempMap.put("rezvId_"+storedWrRezvBVo.getRezvId()+storedWrRezvBVo.getRezvStrtYmd(), indexList);
		    	}
		    	setSortIndex(list, tempMap);//비어 있는 일정을 위한 인덱스 재설정
			}
			
    	}
    	return list;
    }
    
    /** 예약일정을 주간별로 임의 생성한다. */
    public List<WrRezvBVo> getRezvList(WrWeekVo storedWrWeekVo , List<WrRezvBVo> wrRezvBVoList){
    	List<WrRezvBVo> list = new ArrayList<WrRezvBVo>();
    	if(wrRezvBVoList.size() > 0){
    		//주의 시작일
	    	int weekStrtDt = Integer.parseInt(storedWrWeekVo.getWrDayVo().get(0).getDays().replaceAll("[-: ]", ""));
	    	//주의 종료일
			int weekEndDt = Integer.parseInt(storedWrWeekVo.getWrDayVo().get(6).getDays().replaceAll("[-: ]", ""));
			int strtYmd,endYmd;
			WrRezvBVo wrRezvBVo = null;
			for(WrRezvBVo storedWrRezvBVo : wrRezvBVoList){
				strtYmd = Integer.parseInt(storedWrRezvBVo.getRezvStrtYmd().replaceAll("[-: ]", ""));
	    		endYmd = Integer.parseInt(storedWrRezvBVo.getRezvEndYmd().replaceAll("[-: ]", ""));
	    		if(strtYmd <= weekEndDt && endYmd >= weekStrtDt){
	    			wrRezvBVo = new WrRezvBVo();
	    			BeanUtils.copyProperties(storedWrRezvBVo, wrRezvBVo);
	    			//일정의 시작일과 종료일이 해당 주의 범위 밖이면 주의 시작일과 종료일을 세팅한다.
	    			if(strtYmd < weekStrtDt && endYmd > weekEndDt){
	    				wrRezvBVo.setRezvStrtYmd(String.valueOf(weekStrtDt));
	    				wrRezvBVo.setRezvEndYmd(String.valueOf(weekEndDt));
	    				wrRezvBVo.setPrevRezvYn("Y");//이전 일정 여부
	    				wrRezvBVo.setNextRezvYn("Y");//이후 일정 여부
    				//일정의 시작일이 주의 시작일 이전이고 종료일이 해당 주 범위 안이면 주의 시작일 , 일정의 종료일을 세팅한다.
	    			}else if(strtYmd < weekStrtDt && endYmd >= weekStrtDt && endYmd <= weekEndDt){
	    				wrRezvBVo.setRezvStrtYmd(String.valueOf(weekStrtDt));
	    				wrRezvBVo.setRezvEndYmd(String.valueOf(endYmd));
	    				wrRezvBVo.setPrevRezvYn("Y");//이전 일정 여부
	    			//일정의 종료일이 주의 종료일 이후이고 시작일이 해당 주 범위 안이면 일정의 시작일 , 주의 종료일을 세팅한다.
	    			}else if(endYmd > weekEndDt && strtYmd >= weekStrtDt && strtYmd <= weekEndDt){
	    				wrRezvBVo.setRezvStrtYmd(String.valueOf(strtYmd));
	    				wrRezvBVo.setRezvEndYmd(String.valueOf(weekEndDt));
	    				wrRezvBVo.setNextRezvYn("Y");//이후 일정 여부
	    			}else{
	    				wrRezvBVo.setRezvStrtYmd(String.valueOf(strtYmd));
	    				wrRezvBVo.setRezvEndYmd(String.valueOf(endYmd));
	    			}
	    			wrRezvBVo.setRowCnt(getDateDiff(wrRezvBVo.getRezvEndYmd(),wrRezvBVo.getRezvStrtYmd(),"date"));
	    			list.add(wrRezvBVo);
	    			
	    		}
			}
			if(list.size() > 0){
				Comparator<WrRezvBVo> comparator = new Comparator<WrRezvBVo>(){
					int strtYmd,tmpStrtYmd;
					@Override
					public int compare(WrRezvBVo o1, WrRezvBVo o2) {
						strtYmd = Integer.parseInt(o1.getRezvStrtYmd().replaceAll("[-: ]", ""));
			    		tmpStrtYmd = Integer.parseInt(o2.getRezvStrtYmd().replaceAll("[-: ]", ""));
						return (strtYmd < tmpStrtYmd ? -1 : ( strtYmd == tmpStrtYmd ? 0 : 1))+( o1.getRowCnt() > o2.getRowCnt() ? -1 : 0);
					}
				};
				// 날짜순 정렬
				Collections.sort(list , comparator);
				
				int tempStrtYmd,tempEndYmd;
		    	Map<String,List<WrRezvBVo>> tempMap = new HashMap<String,List<WrRezvBVo>>();
		    	List<WrRezvBVo> indexList = null;
		    	for(WrRezvBVo storedWrRezvBVo : list){
		    		strtYmd = Integer.parseInt(storedWrRezvBVo.getRezvStrtYmd().replaceAll("[-: ]", ""));
		    		endYmd = Integer.parseInt(storedWrRezvBVo.getRezvEndYmd().replaceAll("[-: ]", ""));
		    		indexList = new ArrayList<WrRezvBVo>();
	    			for(WrRezvBVo tempVo : list){
	    				if( storedWrRezvBVo.getRezvId().equals(tempVo.getRezvId())) continue;
	        			tempStrtYmd = Integer.parseInt(tempVo.getRezvStrtYmd().replaceAll("[-: ]", ""));
	        			tempEndYmd = Integer.parseInt(tempVo.getRezvEndYmd().replaceAll("[-: ]", ""));
	        			if( ( strtYmd >= tempStrtYmd && strtYmd <= tempEndYmd ) || (
	        					tempStrtYmd>=strtYmd && tempStrtYmd<=endYmd )){
	        				if(!tempMap.containsKey("rezvId_"+tempVo.getRezvId()+tempVo.getRezvStrtYmd())) {
	        					tempVo.setRowIndex(storedWrRezvBVo.getRowIndex()+1);
	        				}
	        				indexList.add(tempVo);
	        			}
	        		}
	    			storedWrRezvBVo.setRowCnt(indexList.size());
	    			tempMap.put("rezvId_"+storedWrRezvBVo.getRezvId()+storedWrRezvBVo.getRezvStrtYmd(), indexList);
		    	}
		    	setSortIndex(list, tempMap);//비어 있는 일정을 위한 인덱스 재설정
			}
			
    	}
    	return list;
    }
    
    /** 비어 있는 일정을 위한 인덱스 재설정 */
    public void setSortIndex(List<WrRezvBVo> list , Map<String,List<WrRezvBVo>> tempMap){
    	List<String[]> saveList = new ArrayList<String[]>();
    	String[] saveArrs = null;
    	for(WrRezvBVo storedWrRezvBVo : list){
	    	if(tempMap.containsKey("rezvId_"+storedWrRezvBVo.getRezvId()+storedWrRezvBVo.getRezvStrtYmd())) {
	    		List<WrRezvBVo> iList = tempMap.get("rezvId_"+storedWrRezvBVo.getRezvId()+storedWrRezvBVo.getRezvStrtYmd());
	    		int size=iList.size();
	    		if(size>0){
	    			int[] indexs = getIndexs();
	    			int[] indexArrs = new int[iList.size()];
	    			for(int i=0;i<size;i++){
	    				if(saveList.size() > 0){
	    					for(String[] save : saveList){
	    						if(save[0].equals(iList.get(i).getRezvId())){
	    							iList.get(i).setRowIndex(Integer.parseInt(save[1]));
	    						}
	    					}
	    				}
	    				indexArrs[i] = iList.get(i).getRowIndex();
	    			}
	    			Arrays.sort(indexArrs);
	    			for(int no : indexs){
	    				if(Arrays.binarySearch(indexArrs, no) < 0 && no < storedWrRezvBVo.getRowIndex()){
	    				
	    					saveArrs = new String[3];
	    					saveArrs[0] = storedWrRezvBVo.getRezvId();
	    					saveArrs[1] = String.valueOf(no);
	    					saveArrs[2] = String.valueOf(storedWrRezvBVo.getRowIndex());
	    					storedWrRezvBVo.setRowIndex(no);
	    					saveList.add(saveArrs);
	    					
	    					break;
	    				}
	    			}
	    		}
	    	}
    	}
    }
    
    /** 전체 순서 배열 */
    public int[] getIndexs(){    	    	
    	int i,maxInt=100;
    	int[] indexs = new int[maxInt];
    	for(i=1;i<=maxInt;i++){
    		indexs[i-1] = i;
    	}
    	return indexs;
    }
    
    /** 날짜의 차이 계산 */
    public Integer getDateDiff(String date1 , String date2 , String type) {
    	try{
    		String fmt = "date".equals(type) ? "yyyyMMdd" : "yyyyMMddHHmm";
    		Date dToday = new SimpleDateFormat(fmt).parse(date1);
        	Calendar todayCal = new GregorianCalendar();
        	todayCal.setTime(dToday);

        	Date dSaleStrDm = new SimpleDateFormat(fmt).parse(date2);
        	Calendar saleStrDmCal = new GregorianCalendar();
        	saleStrDmCal.setTime(dSaleStrDm);
        	     
        	long diffMillis = todayCal.getTimeInMillis() - saleStrDmCal.getTimeInMillis();
        	long diff = 0;
        	if("min".equals(type)){
	        	// 분
	        	diff = diffMillis / (60 * 1000);
        		return (int)diff;
        	}
        	if("hour".equals(type)){
	        	// 시
        		diff = diffMillis / (60 * 60 * 1000);
        		return (int)diff;
        	}
        	if("date".equals(type)){
            	// 일
            	diff = diffMillis/ (24 * 60 * 60 * 1000);
            	return (int)diff;
        	}
    	}catch(ParseException pe){
    		pe.printStackTrace();
    	}
    	
    	return null;
    }
    
    /** 회사ID 세팅 및 회사목록 추가 */
    public String setCompIdList(HttpServletRequest request, ModelMap model, String compId, String langTypCd, CommonVo commonVo, boolean isList) throws SQLException, CmException{
    	// 설정 조회
		Map<String, String> envConfigMap = wrAdmSvc.getEnvConfigMap(model, compId);
		String paramCompId = ParamUtil.getRequestParam(request, "paramCompId", false);
		
		if(envConfigMap==null || !envConfigMap.containsKey("allCompUseYn") || !"Y".equals(envConfigMap.get("allCompUseYn"))){
			VoUtil.setValue(commonVo, "compId", compId);
			return compId;
		}
		
		// 목록이면 회사목록(계열사) 모델에 추가
    	if(isList) wrAdmSvc.setCompAffiliateVoList(model, compId, langTypCd);
    	
		if(paramCompId==null || paramCompId.isEmpty()){
			VoUtil.setValue(commonVo, "compId", compId);
			return compId;
		}
    	// 계열사 회사ID 목록 조회
    	List<String> compIdList = wrAdmSvc.getCompAffiliateIdList(paramCompId, langTypCd, false);
    	String[] compIds = ArrayUtil.toArray(compIdList);
    	
    	// 회사ID 목록에 조회할 회사가 없을 경우 내 회사ID 만 세팅
    	if(!ArrayUtil.isInArray(compIds, paramCompId)){
    		VoUtil.setValue(commonVo, "compId", compId);
			return compId;
    	}
    	
    	VoUtil.setValue(commonVo, "compId", paramCompId);
    	
    	return paramCompId;
    }
    
}
