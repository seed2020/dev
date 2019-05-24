package com.innobiz.orange.web.wp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.wp.vo.WpPrjMpPlanLVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpRsltDVo;

public class UsrMpDetails {

	private List<String> prjNoList = new ArrayList<String>();
	
	private Map<String, Map<String, Double>> planLMap = new Crc32Map<String, Map<String, Double>>();
	private Map<String, Map<String, Double>> rsltDMap = new Crc32Map<String, Map<String, Double>>();
	
	private String minYearMonth = null;
	private String maxYearMonth = null;
	
	public List<String> getPrjNoList(){
		return prjNoList;
	}
	
	public List<String> getMonthList(){
		if(minYearMonth==null || maxYearMonth==null) return null;
		int minY = Integer.parseInt(minYearMonth.substring(0, 2));
		int minM = Integer.parseInt(minYearMonth.substring(3));
		int maxY = Integer.parseInt(maxYearMonth.substring(0, 2));
		int maxM = Integer.parseInt(maxYearMonth.substring(3));
		
		List<String> monthList = new ArrayList<String>();
		while(minY <= maxY){
			
			monthList.add(
					(minY<10 ? "0" : "")+minY+"-"+(minM<10 ? "0" : "")+minM );
			
			if(minY == maxY && minM == maxM) break;
			if(minM == 12){
				minM = 1;
				minY++;
			} else {
				minM++;
			}
		}
		return monthList;
	}
	
	public void setPlanLList(List<WpPrjMpPlanLVo> wpPrjMpPlanLVoList){
		if(wpPrjMpPlanLVoList==null || wpPrjMpPlanLVoList.isEmpty()) return;
		
		String prjNo, oldPrjNo = null, yearMonth=null;
		Map<String, Double> mmMap = null;
		for(WpPrjMpPlanLVo vo : wpPrjMpPlanLVoList){
			prjNo = vo.getPrjNo();
			if(oldPrjNo==null || !oldPrjNo.equals(prjNo)){
				if(!prjNoList.contains(prjNo)){
					prjNoList.add(prjNo);
				}
				oldPrjNo = prjNo;
				
				mmMap = new HashMap<String, Double>();
				planLMap.put(prjNo, mmMap);
			}
			yearMonth = vo.getPlanYm().substring(2, 7);
			if(minYearMonth == null || minYearMonth.compareTo(yearMonth) > 0) minYearMonth = yearMonth;
			if(maxYearMonth == null || maxYearMonth.compareTo(yearMonth) < 0) maxYearMonth = yearMonth;
			mmMap.put(yearMonth, Double.valueOf(vo.getPlanMm()));
		}
	}
	
	public void setRsltDList(List<WpPrjMpRsltDVo> WpPrjMpRsltDVoList){
		if(WpPrjMpRsltDVoList==null || WpPrjMpRsltDVoList.isEmpty()) return;
		
		String prjNo, oldPrjNo = null, yearMonth=null;
		Map<String, Double> mmMap = null;
		for(WpPrjMpRsltDVo vo : WpPrjMpRsltDVoList){
			prjNo = vo.getPrjNo();
			if(oldPrjNo==null || !oldPrjNo.equals(prjNo)){
				if(!prjNoList.contains(prjNo)){
					prjNoList.add(prjNo);
				}
				oldPrjNo = prjNo;
				
				mmMap = new HashMap<String, Double>();
				rsltDMap.put(prjNo, mmMap);
			}
			yearMonth = vo.getRsltYm().substring(2, 7);
			if(minYearMonth == null || minYearMonth.compareTo(yearMonth) > 0) minYearMonth = yearMonth;
			if(maxYearMonth == null || maxYearMonth.compareTo(yearMonth) < 0) maxYearMonth = yearMonth;
			mmMap.put(yearMonth, Double.valueOf(vo.getRsltMm()));
		}
	}
	
	public Map<String, Double> getPlanMap(String prjNo){
		return planLMap.get(prjNo);
	}
	public Map<String, Double> getRsltMap(String prjNo){
		return rsltDMap.get(prjNo);
	}
}
