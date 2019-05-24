package com.innobiz.orange.web.ap.cust;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wd.svc.WdCmSvc;

/** 결재 - 연차관리 서비스 */
@Service
public class ApWdSvc {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 연차 공통 서비스 */
	@Autowired
	private WdCmSvc wdCmSvc;
	
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 일정관리 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	public void sendWdNotiFromAp(String apvNo, String apvSubject, String docStat,
			UserVo userVo, String xmlString) throws SQLException {
		
		try {
			
			XMLElement xmlElement = SAXHandler.parse(xmlString);
			String xmlTypeId = xmlElement.getAttr("head.typId");
			
			// 휴가 신청서
			if("wdLeaveReq".equals(xmlTypeId)){
				if(!"mak".equals(docStat) && !"apvd".equals(docStat) && !"rejt".equals(docStat) 
						&& !"reRevw".equals(docStat) && !"retrvMak".equals(docStat)){
					return;
				}
			// 휴가 취소서, 대근 신청서, 대근 취소서
			} else {
				if(!"apvd".equals(docStat)){
					return;
				}
			}
			
			// anbTypCd - anb:연차, nanb:개정연차, repb:대휴, offb:공가
			String anbTypCd = null, compId = null, odurUid = null, useYmd = null, canApvNo = null;
			float amount = 0; 
			Map<Integer, Boolean> dupCheckMap = new HashMap<Integer, Boolean>();
			Map<String, Float> overCheckMap = new HashMap<String, Float>();
			List<String> delCheckList = new ArrayList<String>();
			
			QueryQueue queryQueue = new QueryQueue();
			
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
			// 기간
			List<String> fromToList = null;
			// 수동입력한 날짜 목록
			List<XMLElement> dates = (List<XMLElement>)xmlElement.getChildList("body/dates");
			
			// 원직자UID
			odurUid=xmlElement.getAttr("body/leave.erpOdurUid");
			if(odurUid==null || odurUid.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : odurUid");
			}
			// 원직자 상세정보 조회
			Map<String, Object> userMap = orCmSvc.getUserMap(odurUid, "ko");
			
			// 회사ID
			compId=(String)userMap.get("compId");
			if(compId==null) compId=userVo.getCompId();
			String langTypCd = (String)userMap.get("langTypCd");
			if(langTypCd==null) langTypCd=userVo.getLangTypCd();
			
			// 대체근무여부
			boolean isRepb=ArrayUtil.isInArray(new String[]{"wdRepbReq", "wdRepbCan"}, xmlTypeId);
			
			// 휴가 여부
			//boolean isVacation=ArrayUtil.isInArray(new String[]{"wdLeaveReq", "wdLeaveCan"}, xmlTypeId);
			
			if(dates==null){
				// 휴일만 포함여부[대체근무,대체근무취소] - 휴일만 적용
				String natCd = wcScdManagerSvc.getNatCd(userVo);
				String realStrtDt = wcScdManagerSvc.getDateOfDay(erpStart, "month", "s", null, 1);
				excludeList = wcScdManagerSvc.getSelectSchdlList(compId, langTypCd, natCd, realStrtDt, erpEnd, null);
				boolean isInclude= excludeList==null ? true : false;
				String tmpStrtDt=erpStart.replaceAll("[-: ]", "");
				String tmpEndDt=erpEnd.replaceAll("[-: ]", "");
				fromToList = wcScdManagerSvc.getFromToDate(tmpStrtDt, tmpEndDt, excludeList, isInclude, "yyyy-MM-dd", isRepb);
			}else{
				fromToList=new ArrayList<String>();
				for(XMLElement element : dates){
					fromToList.add(element.getAttr("erpDate"));
				}
			}
			
			if(fromToList==null || fromToList.size()==0){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : fromToList");
			}
			
			anbTypCd = xmlElement.getAttr("body/leave.erpOptions");
			if(anbTypCd==null || anbTypCd.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : anbTypCd");
			}
			// 시작일 반차여부
			String erpHalfStart = xmlElement.getAttr("body/leave.erpHalfStart");
			
			// 종료일 반차여부
			String erpHalfEnd = xmlElement.getAttr("body/leave.erpHalfEnd");
			
			// 결재번호(취소)
			canApvNo = xmlElement.getAttr("body/leave.erpCanApvNo");
			
			int idx=0;
			// 기간
			for(String day : fromToList){
				amount=1;
				// 시작일 또는 종료일 반차여부에 따라 0.5로 변경
				if((idx==0 && erpHalfStart!=null && "Y".equals(erpHalfStart)) || (fromToList.size()-1==idx && erpHalfEnd!=null && "Y".equals(erpHalfEnd))) 
					amount=(float) 0.5;
				//if(isVacation){ // 반차가 있는경우(휴가 신청,취소)				
				//}
				useYmd=day;
//				System.out.println("xmlTypeId : "+xmlTypeId+"\tanbTypCd : "+anbTypCd+"\tuseYmd : "+useYmd+"\tamount : "+amount);
				// 연차 세팅
				wdCmSvc.setWdData(xmlTypeId, anbTypCd, 
						compId, odurUid, useYmd, amount, 
						apvNo, apvSubject, docStat, 
						canApvNo, queryQueue, dupCheckMap, delCheckList, overCheckMap);
				idx++;
			}
			
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
