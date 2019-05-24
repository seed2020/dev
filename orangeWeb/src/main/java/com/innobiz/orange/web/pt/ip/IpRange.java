package com.innobiz.orange.web.pt.ip;

public class IpRange {
	
	private static short S255 = 0xFF;
	
	private byte[] from = new byte[4];
	private byte[] to   = new byte[4];
	
	public static IpRange create(String txt){
		try{
			if(txt==null) return null;
			txt = txt.trim();
			if(txt.isEmpty()) return null;
			
			int i = txt.indexOf('\t');
			if(i<0) return null;
			
			String[] from = txt.substring(0, i).split("\\.");
			String[] to   = txt.substring(i+1).split("\\.");
			
			if(from.length != 4 || to.length != 4) return null;
			
			IpRange ins = new IpRange();
			for(i=0;i<4;i++){
				ins.from[i] = (byte)(Short.parseShort(from[i]) & S255);
				ins.to  [i] = (byte)(Short.parseShort(to  [i]) & S255);
			}
			
			return ins;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static byte[] toIpv4Arr(String txt){
		if(txt==null || txt.isEmpty()) return null;
		if("0:0:0:0:0:0:0:1".equals(txt)) return new byte[]{127,0,0,1};
		String[] arr = txt.split(":");
		int p = arr.length-2, q = arr.length-1;
		if(p<0) return null;
		if(arr[p].isEmpty()) arr[p] = "0000";
		else if(arr[p].length()!=4) arr[p] = ("0000"+arr[p]).substring(arr[p].length());
		if(arr[q].isEmpty()) arr[p] = "0000";
		else if(arr[q].length()!=4) arr[q] = ("0000"+arr[q]).substring(arr[q].length());
		byte[] ipArr = new byte[4];
		ipArr[3] = (byte)(Short.parseShort(arr[p].substring(0, 2), 16) & S255);
		ipArr[2] = (byte)(Short.parseShort(arr[p].substring(2), 16) & S255);
		ipArr[1] = (byte)(Short.parseShort(arr[q].substring(0, 2), 16) & S255);
		ipArr[0] = (byte)(Short.parseShort(arr[q].substring(2), 16) & S255);
		return ipArr;
	}
	
	public static byte[] toIpArr(String txt){
		try{
			if(txt.indexOf(':')>0) return toIpv4Arr(txt);
			String[] sArr = txt.split("\\.");
			byte[] bArr = new byte[4];
			for(int i=0;i<4;i++){
				bArr[i] = (byte)(Short.parseShort(sArr[i]) & S255);
			}
			return bArr;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public int compareFrom(byte[] arr){
		int result = 0;
		for(int i=0; i<4; i++){
			result = (from[i] & S255) - (arr[i] & S255);
			if(result!=0) return result;
		}
		return result;
	}
	
	public int compareTo(byte[] arr){
		int result = 0;
		for(int i=0; i<4; i++){
			result = (to[i] & S255) - (arr[i] & S255);
			if(result!=0) return result;
		}
		return result;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder(40);
		int i;
		for(i=0;i<4;i++){
			if(i>0) builder.append('.');
			builder.append(from[i] & S255);
		}
		builder.append(" ~ ");
		for(i=0;i<4;i++){
			if(i>0) builder.append('.');
			builder.append(to[i] & S255);
		}
		return builder.toString();
	}
	
	public byte[] getFrom(){
		return from;
	}
}
