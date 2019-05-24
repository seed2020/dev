package com.innobiz.orange.web.pt.ip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.pt.secu.IpChecker;

public class ForeignIpChecker extends RangeIpChecker {
	
	@Autowired
	private IpChecker ipChecker;
	
	public ForeignIpChecker(){
		load("cn.ip.txt");
	}
	
	public ForeignIpChecker(String countryCode){
		load(countryCode+".ip.txt");
	}
	
	public String getInRangeIp(HttpServletRequest request){
		
		String ipValue = request.getRemoteAddr();
		if("0:0:0:0:0:0:0:1".equals(ipValue)) ipValue = "127.0.0.1";
		byte[] ipArr = IpRange.toIpArr(ipValue);
		if(ipArr==null) return ipValue;
		
		if(checkIpInRange(ipArr)){
			return ipValue;
		}
		
		
		for(String header : ipChecker.getProxyHeaders()){
			ipValue = request.getHeader(header);
			if(ipValue==null || ipValue.isEmpty()) continue;
			
			ipArr = IpRange.toIpArr(ipValue);
			if(ipArr==null) continue;
			
			if(checkIpInRange(ipArr)) return ipValue;
		}
		
		return null;
	}
}
