package com.innobiz.orange.web.pt.secu;

/** IP 정책 - IP가 정책에 속하는 IP 인지 검사하는 객체 */
public class IpPolc {
	
	/** 지정한 IP
	 *  - Full IP: 하나만 지정할 경우
	 *  - 앞3자리+(.) : 범위(AA-BB), * 지정(A.A.A.*) */
	private String ip = null;
	
	/** 별표(*) 로 지정 했는지 */
	private boolean asterisk = false;
	
	/** 범위 시작 IP */
	private int start = -1;
	
	/** 범위 종료 IP */
	private int end = -1;
	
	public static IpPolc create(String ipStart, String ipEnd){
		if(ipStart==null || (ipStart = ipStart.trim()).isEmpty()) return null;
		
		IpPolc ins = new IpPolc();
		if(ipEnd==null || (ipEnd = ipEnd.trim()).isEmpty()){
			if(ipStart.endsWith("*")){
				ins.ip = ipStart.substring(0, ipStart.length()-1);
				ins.asterisk = true;
			} else {
				ins.ip = ipStart;
			}
		} else {
			int p = ipStart.lastIndexOf('.')+1;
			String prefix = ipStart.substring(0, p);
			if(!ipEnd.startsWith(prefix)){
				return null;
			}
			try{
				ins.ip = prefix;
				ins.start = Integer.parseInt(ipStart.substring(p));
				ins.end = Integer.parseInt(ipEnd.substring(p));
			} catch(NumberFormatException e){
				return null;
			}
		}
		return ins;
	}
	
	/** 해당 정책 조건에 해당하는지 검사 */
	public boolean isInPolc(String ip){
		if(asterisk){
			return ip.startsWith(this.ip);
		} else if(start>-1){
			if(!ip.startsWith(this.ip)){
				return false;
			}
			try{
				int no = Integer.parseInt(ip.substring(this.ip.length()));
				if(no >= this.start && no <= this.end) return true;
				return false;
			} catch(NumberFormatException e){
				return false;
			}
		} else {
			return ip.equals(this.ip);
		}
	}
	/** toString */
	public String toString() {
		return "IpPolc [ip=" + ip + ", asterisk=" + asterisk + ", start=" + start + ", end=" + end + "]";
	}
	
}
