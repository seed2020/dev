package com.innobiz.orange.web.pt.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RangeIpChecker {
	
	IpRange[] ipRangeps = null;
	
	protected void load(String fileName){
		try {
			// load blocking ips from file
			ArrayList<IpRange> list = new ArrayList<IpRange>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ForeignIpChecker.class.getResourceAsStream("cn.ip.txt")));
			String line;
			IpRange range;
			while((line = reader.readLine()) != null){
				range = IpRange.create(line);
				if(range!=null) list.add(range);
			}
			reader.close();
			
			// sort and make array
			int i, p, size = list.size();
			IpRange[] ranges = new IpRange[size];
			
			if(size>0) ranges[0] = list.get(0);
			for(i=1;i<size;i++){
				range = list.get(i);
				if(ranges[i-1].compareFrom(range.getFrom())>0){
					ranges[i] = ranges[i-1];
					for(p=i-1;p>=0;p--){
						if(p==0){
							ranges[p] = range;
						} else if(ranges[p-1].compareFrom(range.getFrom())>0){
							ranges[p] = ranges[p-1];
						} else {
							ranges[p] = range;
							break;
						}
					}
				} else {
					ranges[i] = range;
				}
			}
			
			ipRangeps = ranges;
		} catch(IOException ignore){
		}
	}
	
	protected boolean checkIpInRange(byte[] ipArr){
		try{
			IpRange ipRange = getIpRange(ipArr);
			if(ipRange==null) return false;
			
			return ipRange.compareTo(ipArr) >= 0;
		} catch(Exception ignore){}
		return true;
	}
	
	protected IpRange getIpRange(byte[] ipArr){
		int low = 0, high = ipRangeps.length-1, mid, diff;
		while(low <= high){
			mid = (low + high) / 2;
			 if((diff = ipRangeps[mid].compareFrom(ipArr)) == 0){
				 return ipRangeps[mid];
			 } else if(diff < 0){
				 low = mid + 1;
			 } else {
				 high = mid - 1;
			 }
		}
		if(high==-1) return null;
		return ipRangeps[high];
	}
}
