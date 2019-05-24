package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcWorkSchdlBVo;

/** 고객사별 기능 서비스 - 이노비즈 */
@Service
public class ApCustEnobizSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	@Autowired
	private OrCmSvc orCmSvc;
	
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;	
	
	/** schdlID생성 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;

	public void sendErpNotiFromAp(UserVo userVo, String apvNo, XMLElement xmlElement) throws SQLException, IOException, CmException{
		// 근태일정 연계 여부
		String erpWorkSchdlYn = xmlElement.getAttr("body/leave.erpWorkSchdlYn");
		if("Y".equals(erpWorkSchdlYn)) {
			
			// 결재 정보 조회
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
			if(apOngdBVo==null) return;
			
			// 기안자UID
			String makrUid = apOngdBVo.getMakrUid();
			
			// 기안자 상세정보 조회
			Map<String, Object> userMap = orCmSvc.getUserMap(makrUid, "ko");
			
			String compId = (String)userMap.get("compId");
			if(compId==null) compId=userVo.getCompId();
			String langTypCd = (String)userMap.get("langTypCd");
			if(langTypCd==null) langTypCd=userVo.getLangTypCd();
			
			// 입력날짜
			String date = commonDao.querySysdate(null);
			
			date=date.replaceAll("[-: ]", "");
			date=date.substring(0,8); // yyyyMMdd
						
			// 시작일(월,일) - 공통 파라미터
			String erpStart = xmlElement.getAttr("body/leave.erpStart");
			if(erpStart==null || erpStart.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpStart");
			}
			
			// 종료일(월,일) - 공통 파라미터
			String erpEnd = xmlElement.getAttr("body/leave.erpEnd");
			if(erpEnd==null || erpEnd.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpEnd");
			}
			
			// 제외할 날짜 목록
			List<String> excludeList = null;
			List<String> fromToList = null;
			List<XMLElement> dates = (List<XMLElement>)xmlElement.getChildList("body/dates");
//						System.out.println("dates : "+dates);
			if(dates==null){
				boolean isInclude=false;
				String natCd = wcScdManagerSvc.getNatCd(userVo);
				String realStrtDt = wcScdManagerSvc.getDateOfDay(erpStart, "month", "s", null, 1);
				excludeList = wcScdManagerSvc.getSelectSchdlList(compId, langTypCd, natCd, realStrtDt, erpEnd, null);
				isInclude=false;
				String tmpStrtDt=erpStart.replaceAll("[-: ]", "");
				String tmpEndDt=erpEnd.replaceAll("[-: ]", "");
				fromToList = wcScdManagerSvc.getFromToDate(tmpStrtDt, tmpEndDt, excludeList, isInclude, null, false);
			}else{
				fromToList=new ArrayList<String>();
				for(XMLElement element : dates){
					fromToList.add(element.getAttr("erpDate"));
				}
			}
			
			if(fromToList==null || fromToList.size()==0){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : fromToList");
			}
						
			// 근태일정 목록
			List<WcWorkSchdlBVo> updateList = new ArrayList<WcWorkSchdlBVo>();
			
			// 휴가종류
			String erpOptions = xmlElement.getAttr("body/leave.erpOptions");
			
			// 원직자UID
			String odurUid = xmlElement.getAttr("body/leave.erpOdurUid");
			
			for(String day : fromToList){
				// 일정VO 세팅
				addWorkSchdlVoList(updateList, day, null, erpOptions, compId, odurUid, xmlElement.getAttr("body/leave.erpUserNm"));
			}
			
			// 일정 등록
			if(updateList.size()>0){
				updateWorkSchdlVoList(xmlElement, updateList, erpOptions, compId, makrUid, erpStart, erpEnd);
			}
		}
	}	
	
	/** 날짜 포맷 변경 */
    public String getFmtDateString(String date){
	    return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
    }
	   
    /** 근태 연계 일정 - 저장을 위한 VO정보 수집 */
    public void addWorkSchdlVoList(List<WcWorkSchdlBVo> updateList, String start, String end, String schdlTypCd, String compId, String userUid, String userNm){
    	//if(start.length()!=8) return;
    	 if(!(start.length()==8 || start.length()==10)) return;
    	if(start.length()==8) start=getFmtDateString(start);
    	if(end==null || end.isEmpty()) end=start;
    	// 일정 저장
    	WcWorkSchdlBVo wcWorkSchdlBVo = new WcWorkSchdlBVo();
    	wcWorkSchdlBVo.setCompId(compId);
    	wcWorkSchdlBVo.setUserUid(userUid);
    	wcWorkSchdlBVo.setSchdlTypCd(schdlTypCd);
    	wcWorkSchdlBVo.setSubj(userNm);
    	wcWorkSchdlBVo.setStrtDt(start);
    	wcWorkSchdlBVo.setEndDt(end);
    	wcWorkSchdlBVo.setAlldayYn("Y");
    	updateList.add(wcWorkSchdlBVo);
    }
	   
	
    /** 근태 연계 일정 - 목록 저장 */
    public void updateWorkSchdlVoList(XMLElement element, List<WcWorkSchdlBVo> updateList, String erpOptions, String compId, String regrUid, String erpStart, String erpEnd) throws SQLException{
    	QueryQueue queryQueue = new QueryQueue();
    	for(WcWorkSchdlBVo storedWcWorkSchdlBVo : updateList){
    		updateWorkSchdl(queryQueue, storedWcWorkSchdlBVo, getWorkSchdlCont(erpOptions, element, erpStart, erpEnd, storedWcWorkSchdlBVo.getStrtDt()), regrUid);
    	}
    	if(!queryQueue.isEmpty()) commonDao.execute(queryQueue);

    }

    /** 근태 연계 일정 - 저장 */
    public void updateWorkSchdl(QueryQueue queryQueue, WcWorkSchdlBVo wcWorkSchdlBVo, String cont, String regrUid) throws SQLException{
    	wcWorkSchdlBVo.setCont(cont);
    	wcWorkSchdlBVo.setRegrUid(regrUid);
    	wcWorkSchdlBVo.setRegDt("sysdate");
    	wcWorkSchdlBVo.setSchdlId(wcCmSvc.createId("WC_WORK_SCHDL_B"));
    	queryQueue.insert(wcWorkSchdlBVo);
    }

    /** 근태 연계 일정 - 일정 내용 생성 */
    public String getWorkSchdlCont(String erpOptions, XMLElement element, String strtDt, String endDt, String date){
    	StringBuilder sb = new StringBuilder();
    	sb.append("기간").append(" : ").append(strtDt).append(" ~ ").append(endDt);
    	
    	if(ArrayUtil.isInArray(new String[]{"31", "32", "33"}, erpOptions)){
    		String strtSi = element.getAttr("body/detail.erpStartSi");
    		String endSi = element.getAttr("body/detail.erpEndSi");
    		if(strtSi==null || endSi==null) return sb.toString();
    		sb.append("\n").append("시간").append(" : ").append(strtSi).append("시 부터 ").append(endSi).append("시 까지");
    		sb.append("(").append(element.getAttr("body/detail.erpTotalDay")).append("일 ");
    		String erpTotalTime = element.getAttr("body/detail.erpTotalTime");
    		if(erpTotalTime!=null && !erpTotalTime.isEmpty())
    		sb.append("/ ").append(element.getAttr("body/detail.erpTotalTime")).append("시간)");
    	}
    	
    	if("01".equals(erpOptions)){ // 연차 이면서 반차 일경우 오전 or 오후 반차 표시
    		// 매일반복 여부
    		String erpHalfRepet = element.getAttr("body/leave.erpHalfRepet");
    		
    		// 매일반복
    		boolean isHalfRepet = erpHalfRepet!=null && !erpHalfRepet.isEmpty();
    		
    		// 일자가 하루일 경우
    		//boolean isDateEq = strtDt.equals(endDt);
    		
    		// 반차여부 - 시작일
    		String erpHalfStart = element.getAttr("body/leave.erpHalfStart");
    		if(erpHalfStart!=null && !erpHalfStart.isEmpty() && "Y".equals(erpHalfStart) && (isHalfRepet || (!isHalfRepet && strtDt.equals(date)))) {
    			// 오전 오후 텍스트
    			String erpHalfStartAmPmTxt = "AM".equals(element.getAttr("body/leave.erpHalfStartAmPm")) ? "오전" : "오후";
    			sb.append("\n");
    			//if(!isHalfRepet) sb.append("시작일( "); 
    			sb.append(erpHalfStartAmPmTxt).append("반차");
    			//if(!isHalfRepet) sb.append(" )"); 
    		}
    		
    		// 반차여부 - 종료일
    		String erpHalfEnd = element.getAttr("body/leave.erpHalfEnd");
    		if(erpHalfEnd!=null && !erpHalfEnd.isEmpty() && "Y".equals(erpHalfEnd) && (isHalfRepet || (!isHalfRepet && endDt.equals(date)))) {
    			// 오전 오후 텍스트
    			String erpHalfEndAmPmTxt = "AM".equals(element.getAttr("body/leave.erpHalfEndAmPm")) ? "오전" : "오후";
    			sb.append("\n");
    			//if(!isHalfRepet) sb.append("종료일( "); 
    			sb.append(erpHalfEndAmPmTxt).append("반차");
    			//if(!isHalfRepet) sb.append(" )"); 
    		}
    		
    		if(((erpHalfStart!=null && !erpHalfStart.isEmpty()) || (erpHalfEnd!=null && !erpHalfEnd.isEmpty())) && isHalfRepet) {
    			sb.append(" - 매일반복");
    		}
    	}
    	// 휴가 사유
    	String erpReason = element.getAttr("body/leave.erpReason");
    	if(erpReason!=null && !erpReason.isEmpty()) {
    		sb.append("\n");
        	sb.append(erpReason);
    	}
    	
    	return sb.toString();
    }
	   
}
