package com.innobiz.orange.web.wp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.wp.vo.WpPrjMpPlanDVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpPlanLVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpRsltDVo;

public class PrjMpDetails {

	private int startYear = 0;
	private int startMonth = 0;
	
	private boolean advance = false;
	
	private int maxMno = 0;
	
	private List<String> conMpIdList = new ArrayList<String>();
	
	private Crc32Map<String, Map<Integer, Double>> planLMap = new Crc32Map<String, Map<Integer, Double>>();
	private Crc32Map<String, Map<Integer, Double>> rsltDMap = new Crc32Map<String, Map<Integer, Double>>();
	
	private Crc32Map<String, Map<Integer, Double>> roleSumMap = new Crc32Map<String, Map<Integer, Double>>();
	
	private double planMmSum = 0;
	private double rsltMmSum = 0;
	
	public PrjMpDetails(String startYmd){
		startYear = Integer.parseInt(startYmd.substring(0, 4));
		startMonth = Integer.parseInt(startYmd.substring(5, 7));
		
		for(String role1 : new String[]{ "con", "dev", "all" }){
			for(String gubun : new String[]{ "plan", "rslt" }){
				roleSumMap.put(role1+gubun, new HashMap<Integer, Double>());
			}
		}
	}
	
	public boolean isAdvance(){
		return advance;
	}
	public int getMaxMno(){
		return maxMno;
	}
	public double getPlanSum(){
		return Math.round(planMmSum * 10.0) / 10.0;
	}
	public double getRsltSum(){
		return Math.round(rsltMmSum * 10.0) / 10.0;
	}
	
	public void setPlanDList(List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList){
		if(wpPrjMpPlanDVoList ==null || wpPrjMpPlanDVoList.isEmpty()){
			return;
		}
		for(WpPrjMpPlanDVo vo : wpPrjMpPlanDVoList){
			if("con".equals(vo.getPrjRole1Cd())){
				conMpIdList.add(vo.getMpId());
			}
		}
	}
	
	public void setPlanLList(List<WpPrjMpPlanLVo> wpPrjMpPlanLVoList){
		if(wpPrjMpPlanLVoList ==null || wpPrjMpPlanLVoList.isEmpty()){
			return;
		}
		
		String planMm;
		double mm, eachSum=0;
		int mNo;
		String mpId, oldMpId = null;
		boolean isCon = false;
		Map<Integer, Double> mmMap = null;
		Map<Integer, Double> sumMap;
		for(WpPrjMpPlanLVo vo : wpPrjMpPlanLVoList){
			mpId = vo.getMpId();
			if(oldMpId == null || !oldMpId.equals(mpId)){
				if(oldMpId!=null && eachSum>0){
					eachSum = Math.round(eachSum * 10.0) / 10.0;
					mmMap.put(-100, Double.valueOf(eachSum));
					eachSum = 0;
				}
				mmMap = new HashMap<Integer, Double>();
				planLMap.put(mpId, mmMap);
				oldMpId = mpId;
				isCon = conMpIdList.contains(mpId);
			}
			mNo = Integer.parseInt(vo.getMNo());
			if(maxMno < mNo) maxMno = mNo;
			
			planMm = vo.getPlanMm();
			if(planMm!=null && !planMm.isEmpty()){
				mm =  Double.parseDouble(planMm);
				mmMap.put(mNo, Double.valueOf(mm));
				planMmSum += mm;
				eachSum += mm;
				
				sumMap = roleSumMap.get((isCon ? "con" : "dev")+"plan");
				if(sumMap.get(mNo)==null){
					sumMap.put(mNo, mm);
				} else {
					sumMap.put(mNo, mm + sumMap.get(mNo));
				}
				sumMap = roleSumMap.get("allplan");
				if(sumMap.get(mNo)==null){
					sumMap.put(mNo, mm);
				} else {
					sumMap.put(mNo, mm + sumMap.get(mNo));
				}
			}
		}
		if(oldMpId!=null && eachSum>0){
			mmMap.put(-100, Double.valueOf(eachSum));
			eachSum = 0;
		}
	}
	
	public void setRsltDList(List<WpPrjMpRsltDVo> wpPrjMpRsltDVoList){
		if(wpPrjMpRsltDVoList ==null || wpPrjMpRsltDVoList.isEmpty()){
			return;
		}
		
		String rsltMm;
		double mm, eachSum=0;
		int mNo;
		String mpId, oldMpId = null;
		boolean isCon = false;
		Map<Integer, Double> mmMap = null;
		Map<Integer, Double> sumMap;
		for(WpPrjMpRsltDVo vo : wpPrjMpRsltDVoList){
			if(oldMpId!=null && eachSum>0){
				if(mmMap.get(-100)!=null){
					eachSum += mmMap.get(-100);
				}
				eachSum = Math.round(eachSum * 10.0) / 10.0;
				mmMap.put(-100, Double.valueOf(eachSum));
				eachSum = 0;
			}
			mpId = vo.getMpId();
			if(oldMpId == null || !oldMpId.equals(mpId)){
				mmMap = new HashMap<Integer, Double>();
				rsltDMap.put(mpId, mmMap);
				oldMpId = mpId;
				isCon = conMpIdList.contains(mpId);
			}
			mNo = toMNo(vo.getRsltYm());
			if(maxMno < mNo) maxMno = mNo;
			if(mNo==0) advance = true;
			
			rsltMm = vo.getRsltMm();
			if(rsltMm!=null && !rsltMm.isEmpty()){
				mm = Double.parseDouble(rsltMm);
				if(mmMap.get(mNo) == null){
					mmMap.put(mNo, Double.valueOf(mm));
				} else {
					mmMap.put(mNo, Double.valueOf(mm) + mmMap.get(mNo));
				}
				rsltMmSum += mm;
				eachSum += mm;
				
				sumMap = roleSumMap.get((isCon ? "con" : "dev")+"rslt");
				if(sumMap.get(mNo)==null){
					sumMap.put(mNo, mm);
				} else {
					sumMap.put(mNo, mm + sumMap.get(mNo));
				}
				sumMap = roleSumMap.get("allrslt");
				if(sumMap.get(mNo)==null){
					sumMap.put(mNo, mm);
				} else {
					sumMap.put(mNo, mm + sumMap.get(mNo));
				}
			}
		}
		if(oldMpId!=null && eachSum>0){
			if(mmMap.get(-100)!=null){
				eachSum += mmMap.get(-100);
			}
			eachSum = Math.round(eachSum * 10.0) / 10.0;
			mmMap.put(-100, Double.valueOf(eachSum));
			eachSum = 0;
		}
		
		calcSumOfSum();
	}
	
	private void calcSumOfSum(){
		
		double sum;
		Map<Integer, Double> sumMap;
		for(String role : new String[]{ "con", "dev", "all" }){
			for(String gubun : new String[]{ "plan", "rslt" }){
				sumMap = roleSumMap.get(role+gubun);
				
				sum = 0;
				Iterator<Double> iterator = sumMap.values().iterator();
				while(iterator.hasNext()) sum += iterator.next();
				
				sum = Math.round(sum * 10.0) / 10.0;
				if(sum>0){
					sumMap.put(-100, sum);
				}
			}
		}
	}
	
	public Map<Integer, Double> getPlanMap(String mpId){
		return planLMap.get(mpId);
	}
	
	public Map<Integer, Double> getRsltMap(String mpId){
		return rsltDMap.get(mpId);
	}
	
	public Map<Integer, Double> getSumMap(String role1, String gubun){
		return roleSumMap.get(role1+gubun);
	}
	
	private int toMNo(String ymd){
		int year = Integer.parseInt(ymd.substring(0, 4));
		int month = Integer.parseInt(ymd.substring(5, 7));
		if(year<startYear || (year==startYear && month<startMonth)){
			return 0;
		}
		return ((year - startYear) * 12) + (month - startMonth) + 1;
	}
	
	public String toYearMonth(int mNo){
		
		int year = startYear;
		int month = startMonth + mNo -1;
		while(month>12){
			year++;
			month -= 12;
		}
		return (year % 100) + "-" + (month>9 ? "" : "0") + month;
		
		
	}
}
