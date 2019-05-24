package com.innobiz.orange.web.wc.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.util.ChineseCalendar;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;

/** 일정 공통 서비스(일정ID) */
@Service
public class WcCmSvc {
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("WC_SCHDL_B".equals(tableName)) { // 일정
			return commonSvc.createId(tableName, 'S', 8);
		}else if ("WC_BUMK_D".equals(tableName)) { // 즐겨찾기
			return commonSvc.createId(tableName, 'B', 8);
		}else if ("WC_SCHDL_GRP_B".equals(tableName)) { // 일정그룹
			return commonSvc.createId(tableName, 'G', 8);
		}else if ("WC_CAT_CLS_B".equals(tableName)) { // 일정 카테고리
			return commonSvc.createId(tableName, 'C', 8);
		}else if ("WC_USER_GRP_B".equals(tableName)) { // 사용자그룹
			return commonSvc.createId(tableName, 'U', 8);
		}else if ("WC_ERP_SCHDL_B".equals(tableName)) { // ERP 일정(한화제약)
			return commonSvc.createId(tableName, 'E', 8);
		}else if ("WC_MAIL_SCHDL_R".equals(tableName)) {	// 메일일정관계
			return commonSvc.createId(tableName, 'M', 8);
		}else if ("WC_WORK_SCHDL_B".equals(tableName)) { // 근태 일정
			return commonSvc.createId(tableName, 'W', 8);
		}
		
		return null;
	}
	
	/** NO 통합 생성 */
	public Long createNo(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName);
		return null;
	}
	
	/** 게시물첨부파일 ID 생성 */
	public Integer createFileId(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 첨부파일 목록 조회 */
	public ModelAndView getFileList(HttpServletRequest request,String fileIds , String actionParam) throws SQLException, IOException  {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}

		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		// fileId
		String[] fileIdArray = fileIds.split(",");
		for (String fileId : fileIdArray) {
			// 첨부파일
			CommonFileVo fileVo = null; 
			if(actionParam != null && "metng".equals(actionParam)){
				fileVo = wcFileSvc.getFileVo(Integer.valueOf(fileId));
			}else{
				fileVo = wcFileSvc.getFileVo(Integer.valueOf(fileId));
			}
			
			if (fileVo != null) {
				fileVo.setSavePath(wasCopyBaseDir + fileVo.getSavePath());
				File file = new File(fileVo.getSavePath());
				if (file.isFile()) {
					fileVoList.add(fileVo);
				}
			}
		}
		
		// 파일이 몇개인가
		if (fileVoList.size() == 0) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			Locale locale = SessionUtil.getLocale(request);
			// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
			mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
			return mv;
			
		} else if (fileVoList.size() == 1) {
			CommonFileVo fileVoVo = fileVoList.get(0);
			String savePath = fileVoVo.getSavePath();
			File file = new File(savePath);
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", fileVoVo.getDispNm());
			return mv;
			
		} else {
			File zipFile = zipUtil.makeZipFile(fileVoList, "files.zip");
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", zipFile);
			mv.addObject("realFileName", zipFile.getName());
			return mv;
		}
					
	}
	
	/** 한자리수 월 , 일에 0 조합 */
    public String getMixDt(int d){
    	return d < 10 ? "0"+ d : ""+d;
    }
	
	/** 시작일과 종료일을 세팅한다. */
    public void setSchdlDt(String d , WcSchdlBVo wcsVo , String listType ) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	if(d == null || "".equals(d)){
    		Calendar cal2 = Calendar.getInstance();
    		d = sdf.format(cal2.getTime());
    	}
    	if("daly".equals(listType)){
    		wcsVo.setSchdlStartDt(d);
			wcsVo.setSchdlEndDt(d);
    	}else{
    		Calendar cal = Calendar.getInstance();
    		d = d.replaceAll("[-: ]", "");//-:공백 제거
        	int year = Integer.parseInt(d.substring(0, 4)); 
            int month = Integer.parseInt(d.substring(4, 6)); 
            int day = Integer.parseInt(d.substring(6));
    		cal.set(Calendar.YEAR, year);
    		cal.set(Calendar.MONTH, month-1);
    		
    		String dtFmt = "-";
    		if("moly".equals(listType)){
    			cal.set(Calendar.DATE, 1);
    			int strtDay = 1;
    			int endDay = cal.getActualMaximum(Calendar.DATE);
    			wcsVo.setSchdlStartDt(year+dtFmt+getMixDt(month)+dtFmt+getMixDt(strtDay));
    			wcsVo.setSchdlEndDt(year+dtFmt+getMixDt(month)+dtFmt+getMixDt(endDay));
    		}else{
    			cal.set(Calendar.DATE, day);
    			//cal.set(Calendar.YEAR, Integer.valueOf(cal.get(Calendar.YEAR)));
    			cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR));
    			//일요일
    			cal.set(Calendar.DAY_OF_WEEK, 1);
    			wcsVo.setSchdlStartDt(sdf.format(cal.getTime()));
    			
    			//토요일
    			cal.set(Calendar.DAY_OF_WEEK, 7);
    			wcsVo.setSchdlEndDt(sdf.format(cal.getTime()));
    		}
    	}
	}
    
    /** 일정대상 조회 - HashMap */
    public Map<String,String> getSchdlKndNmMap(HttpServletRequest request){
    	Map<String,String> rsltMap = new HashMap<String,String>();
    	String[][] schdlKndCdList = new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"},{"2",messageProperties.getMessage("wc.option.grpSchdl", request),"Y"},{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}};
    	for(String[] schdlKndCds : schdlKndCdList){
    		rsltMap.put(schdlKndCds[0],schdlKndCds[1]);
    	}
    	return rsltMap;
    }
    
    /** 이메일 정보 조회 및 세팅 [기존 - 일일,주간,월간 메세지]*/
    public CmEmailBVo getEmailInfoTemp(HttpServletRequest request , String listType , WcSchdlBVo wcsVo, List<WcSchdlBVo> wcsList){
    	//제목
		String subj = "";
		if("daly".equals(listType)) subj += wcsVo.getSchdlStartDt();
		else if("moly".equals(listType)) subj += wcsVo.getSchdlStartDt().substring(0,6);
		else subj += wcsVo.getSchdlStartDt() + "~" + wcsVo.getSchdlEndDt();
		subj += " ["+messageProperties.getMessage("wc.jsp.listPsnSchdl.tab."+listType+"Schdl", request)+"]";//일일,주간,월간
		
		//일정대상 조회
		Map<String,String> schdlKndCdMap = getSchdlKndNmMap(request);
		//메일 내용정리 - <[일정대상][일정종류][기간] - 제목>
		StringBuilder sb = new StringBuilder();
		String ltg = "[";
		String rtg = "]";
		String schdlPeriod = "";
		String strtYmd,strtTime,endYmd,endTime;
		String strtTag = "<p>";//시작태그
		String endTag = "</p>";//종료태그
		for(WcSchdlBVo storedWcsVo : wcsList){
			strtYmd = storedWcsVo.getSchdlStartDt().substring(0,10);
			strtTime = storedWcsVo.getSchdlStartDt().substring(11,16);
			endYmd = storedWcsVo.getSchdlEndDt().substring(0,10);
			endTime = storedWcsVo.getSchdlEndDt().substring(11,16);
			if("Y".equals(storedWcsVo.getAlldayYn()))//종일일정여부
				schdlPeriod = strtYmd + "~" + endYmd;
			else{//종일일정이 아닐경우 시간을 표시
				if(strtYmd.equals(endYmd))	schdlPeriod = strtYmd;
				else schdlPeriod = strtYmd + "~" + endYmd;					
				schdlPeriod+="("+strtTime+"~"+endTime+")";
			}
			sb.append(strtTag)
			.append(ltg).append(schdlKndCdMap.get(storedWcsVo.getSchdlKndCd())).append(rtg)//일정대상
			.append(ltg).append(storedWcsVo.getSchdlTypNm()).append(rtg)//일정종류
			.append(ltg).append(schdlPeriod).append(rtg)//기간
			.append("-").append(storedWcsVo.getSubj())//제목
			.append(endTag);
		}
		
		//이메일 Vo[업무별 정보 세팅-제목,내용]
		CmEmailBVo cmEmailBVo = new CmEmailBVo();
		cmEmailBVo.setSubj(subj);
		cmEmailBVo.setCont(sb.toString());
		
		return cmEmailBVo;
    }
    
    
    /** 이메일 정보 조회 및 세팅 */
    public CmEmailBVo getEmailInfo(HttpServletRequest request , WcSchdlBVo wcsVo, List<WcSchdlBVo> wcsList){
    	//제목<홍길동 - 일정보내기 기간>
		String subj = "";
		if(wcsVo.getRegrNm() != null && !"".equals(wcsVo.getRegrNm())){
			subj += wcsVo.getRegrNm() + " - ";
		}
		subj += messageProperties.getMessage("wc.jsp.setSchdlSend.title", request);//일정 보내기
		
		if(wcsVo.getSchdlStartDt() != null && !"".equals(wcsVo.getSchdlStartDt())){
			subj += " "+wcsVo.getSchdlStartDt();
		}
		
		if(wcsVo.getSchdlEndDt() != null && !"".equals(wcsVo.getSchdlEndDt()) && !wcsVo.getSchdlEndDt().equals(wcsVo.getSchdlStartDt())){
			subj += " ~ " + wcsVo.getSchdlEndDt();
		}
		
		//일정대상 조회
		Map<String,String> schdlKndCdMap = getSchdlKndNmMap(request);
		//메일 내용정리 - <[일정대상][일정종류][기간] - 제목>
		StringBuilder sb = new StringBuilder();
		String ltg = "[";
		String rtg = "]";
		String schdlPeriod = "";
		String strtYmd,strtTime,endYmd,endTime;
		String strtTag = "<p>";//시작태그
		String endTag = "</p>";//종료태그
		for(WcSchdlBVo storedWcsVo : wcsList){
			strtYmd = storedWcsVo.getSchdlStartDt().substring(0,10);
			strtTime = storedWcsVo.getSchdlStartDt().substring(11,16);
			endYmd = storedWcsVo.getSchdlEndDt().substring(0,10);
			endTime = storedWcsVo.getSchdlEndDt().substring(11,16);
			if("Y".equals(storedWcsVo.getAlldayYn()))//종일일정여부
				schdlPeriod = strtYmd + "~" + endYmd;
			else{//종일일정이 아닐경우 시간을 표시
				if(strtYmd.equals(endYmd))	schdlPeriod = strtYmd;
				else schdlPeriod = strtYmd + "~" + endYmd;					
				schdlPeriod+="("+strtTime+"~"+endTime+")";
			}
			sb.append(strtTag)
			.append(ltg).append(schdlKndCdMap.get(storedWcsVo.getSchdlKndCd())).append(rtg)//일정대상
			.append(ltg).append(storedWcsVo.getSchdlTypNm()).append(rtg)//일정종류
			.append(ltg).append(schdlPeriod).append(rtg)//기간
			.append("-").append(storedWcsVo.getSubj())//제목
			.append(endTag);
		}
		
		//이메일 Vo[업무별 정보 세팅-제목,내용]
		CmEmailBVo cmEmailBVo = new CmEmailBVo();
		cmEmailBVo.setSubj(subj);
		cmEmailBVo.setCont(sb.toString());
		
		return cmEmailBVo;
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
	
	/** 조회 일자에 대한 일정목록 조회 */
    public List<WcSchdlBVo> getSchdlList(String durStrtDt , String durEndDt , List<WcSchdlBVo> wcsList){
    	int durStrtYmd = Integer.parseInt(durStrtDt.replaceAll("[-: ]", ""));
		int durEndYmd = Integer.parseInt(durEndDt.replaceAll("[-: ]", ""));
    	String strtYmd = "";
		String endYmd = "";
		List<WcSchdlBVo> rsltList = new ArrayList<WcSchdlBVo>();
		
		for(WcSchdlBVo wcSchdlBVo : wcsList){
			strtYmd = wcSchdlBVo.getSchdlStartDt().replaceAll("[-: ]", "").substring(0,8);
			endYmd = wcSchdlBVo.getSchdlEndDt().replaceAll("[-: ]", "").substring(0,8);
			//음력일정일 경우 일자와의 비교를 위해 양력일정으로 변경한다.
			strtYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? fromLunar(strtYmd) : strtYmd;
			endYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? fromLunar(endYmd) : endYmd;
			if(Integer.parseInt(strtYmd) <= durEndYmd && Integer.parseInt(endYmd) >= durStrtYmd){
				rsltList.add(wcSchdlBVo);
			}
		}
		return rsltList;
    }
    
    /** 일정대상 조회 */
    public String[][] getSchdlKndList(HttpServletRequest request){
    	return new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"},{"2",messageProperties.getMessage("wc.option.grpSchdl", request),"Y"},{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}};
    }
}
