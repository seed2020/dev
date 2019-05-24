package com.innobiz.orange.web.pt.ip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.pt.secu.IpChecker;

public class DomesticIpChecker extends RangeIpChecker {
	
	@Autowired
	private IpChecker ipChecker;
	
	public DomesticIpChecker(){
		load("kr.ip.txt");
	}
	
	public DomesticIpChecker(String countryCode){
		load(countryCode+".ip.txt");
	}
	
	public String getInRangeIp(HttpServletRequest request){
		
		byte[] ipArr;
		String ipValue;
		for(String header : ipChecker.getHeader()){
			ipValue = request.getHeader(header);
			if(ipValue==null || ipValue.isEmpty()) continue;
			
			ipArr = IpRange.toIpArr(ipValue);
			if(ipArr==null) continue;
			
			if(checkIpInRange(ipArr)) return ipValue;
			else return null;
		}
		
		ipValue = request.getRemoteAddr();
		if("0:0:0:0:0:0:0:1".equals(ipValue)) ipValue = "127.0.0.1";
		ipArr = IpRange.toIpArr(ipValue);
		if(ipArr==null) return null;
		
		if(checkIpInRange(ipArr)){
			return ipValue;
		}
		return null;
	}
	
	public String getOutOfRangeIp(HttpServletRequest request){
		
		byte[] ipArr;
		String ipValue;
		for(String header : ipChecker.getHeader()){
			ipValue = request.getHeader(header);
			if(ipValue==null || ipValue.isEmpty()) continue;
			
			ipArr = IpRange.toIpArr(ipValue);
			if(ipArr==null) continue;
			
			if(checkIpInRange(ipArr)) return null;
			else return ipValue;
		}
		
		ipValue = request.getRemoteAddr();
		if("0:0:0:0:0:0:0:1".equals(ipValue)) ipValue = "127.0.0.1";
		ipArr = IpRange.toIpArr(ipValue);
		if(ipArr==null) return ipValue;
		
		if(checkIpInRange(ipArr)){
			return null;
		}
		return ipValue;
	}
}
