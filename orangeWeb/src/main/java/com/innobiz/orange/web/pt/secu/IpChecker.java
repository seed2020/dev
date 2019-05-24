package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.innobiz.orange.web.cm.exception.InternetAccessException;
import com.innobiz.orange.web.cm.exception.SessionIpException;

/** IP 체크용 객체 - IP 조회 및 정책 적용 */
@Repository
public class IpChecker {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(IpChecker.class);
	
	protected static IpChecker ins;
	
	/** IP 해더 */
	private String[] ipHeaders = null;
	
	/** 로그인 IP 정책 사용 여부 */
	private boolean useLginIpPloc = false;
	
	/** 로그인 IP 정책 데이터 */
	private List<IpPolc> lginIpPlocList = null;
	
	/** 내부망만 허용 여부 */
	private boolean internalIpOnlyPloc = false;
	
	/** 외부망 별도 권한 여부 */
	private boolean authByIpRangePloc = false;
	
	/** 세션 IP 정책 */
	private boolean checkSesnIp = false;
	
	/** 세션 IP 정책 데이터 */
	private List<List<IpPolc>> sesnIpPlocList = null;
	
	/** 로그인 IP 정책 데이터가 내부망 인지 여부 */
	private boolean inIpRange = false;
	
	public IpChecker(){
		ins = this;
	}
	
	/** IP 해더를 가져옴, 설정이 없으면 디폴트값 */
	public String[] getHeader(){
		if(ipHeaders==null) return new String[]{};
		else return ipHeaders;
	}
	
	/** 프록시 해더 조회 */
	public String[] getProxyHeaders(){
		return new String[] {
				"X-Forwarded-For",
				"Proxy-Client-IP",
				"WL-Proxy-Client-IP",
				"HTTP_X_FORWARDED_FOR",
				"HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP",
				"HTTP_CLIENT_IP",
				"HTTP_FORWARDED_FOR",
				"HTTP_FORWARDED",
				"HTTP_VIA",
				"REMOTE_ADDR"
		};
	}
	

	/** 로그인 정책 설정 */
	public void setLginPolc(String lginIpPolc, String ipRange, List<String> lginIpList){
		
		this.useLginIpPloc = !"N".equals(lginIpPolc);
		if(useLginIpPloc){
			
			this.inIpRange          = !"OUT".equals(ipRange);
			this.internalIpOnlyPloc = "internalIpOnlyPloc".equals(lginIpPolc);
			this.authByIpRangePloc  = "authByIpRangePloc".equals(lginIpPolc);
			
			List<IpPolc> ipList = new ArrayList<IpPolc>();
			
			String[] ips;
			IpPolc ipPolc;
			if(lginIpList!=null){
				for(String lginIps : lginIpList){
					for(String range : lginIps.split(",")){
						ips = range.split("-");
						if(ips.length==1){
							ipPolc = IpPolc.create(ips[0], null);
						} else {
							ipPolc = IpPolc.create(ips[0], ips[1]);
						}
						if(ipPolc != null) ipList.add(ipPolc);
					}
				}
			}
			lginIpPlocList = ipList;
			
		} else {
			lginIpList = null;
			this.inIpRange          = true;
			this.internalIpOnlyPloc = false;
			this.authByIpRangePloc  = false;
			
		}
	}
	/** 세션 IP 체크 설정 세팅 */
	public void setSesnIp(String chkSesnIp, List<String> sessionIpExcpList){
		
		this.checkSesnIp = !"N".equals(chkSesnIp);
		
		if(checkSesnIp && sessionIpExcpList!=null && !sessionIpExcpList.isEmpty()){
			
			sesnIpPlocList = new ArrayList<List<IpPolc>>();
			
			List<IpPolc> ipList;
			String[] ips;
			IpPolc ipPolc;
			
			for(String excpIps : sessionIpExcpList){
				
				ipList = new ArrayList<IpPolc>();
				
				for(String range : excpIps.split(",")){
					ips = range.split("-");
					if(ips.length==1){
						ipPolc = IpPolc.create(ips[0], null);
					} else {
						ipPolc = IpPolc.create(ips[0], ips[1]);
					}
					if(ipPolc != null) ipList.add(ipPolc);
				}
				
				if(!ipList.isEmpty()) sesnIpPlocList.add(ipList);
			}
		} else {
			sesnIpPlocList = null;
		}
	}
	
	/** 내부망만 사용 정책 여부 */
	public boolean isInternalIpOnlyPloc(){
		return internalIpOnlyPloc;
	}
	
	/** 외부망 별도 권한 정책 여부 */
	public boolean isAuthByIpRangePloc(){
		return authByIpRangePloc;
	}
	
	/** 내부망 IP 여부 */
	public boolean isInternalIpRange(String ip){
		if(!useLginIpPloc) return true;
		if("127.0.0.1".equals(ip)) return true;
		
		if(lginIpPlocList != null){
			for(IpPolc ipPolc : lginIpPlocList){
				if(ipPolc.isInPolc(ip)){
					return inIpRange;
				}
			}
		}
		return !inIpRange;
	}
	
	/** 세션 IP 체크 */
	@SuppressWarnings("unlikely-arg-type")
	public void checkSessionIp(HttpServletRequest request) throws SessionIpException, InternetAccessException{
		// 세션 IP 체크 정책 - 사용 안하면 통과
		if(!checkSesnIp) return;
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null) return;
		
		// 관리자 페이지를 인터넷(외부) 에서 접속 할때
		if( (userVo.getExcliLginYn() || !userVo.isInternalIp())
				&& request.getRequestURL().indexOf("/adm/")>0){
			// pt.logout.InternetAdmPolc=외부망에서 관리자 모듈에 접근 할 수 없습니다.
			throw new InternetAccessException("pt.logout.InternetAdmPolc", request).setIp(getIp(request));
		}
		String ip = getIp(request);
		// IP 같으면 통과
		if(userVo.getLginIp()!=null && userVo.getLginIp().equals(ip)){
			return;
		// 세션 IP 체크 - 예외 처리 사용자면 통과
		} else if(userVo.getSesnIpExcliYn()){
			return;
		}
		
		// 변경 가능한 IP 인지 - 이미 체크 된 곳
		List<String> changeableIpList = userVo.getChangeableIpList();
		if(changeableIpList!=null && changeableIpList.contains(request)) return;
		
		// 변경 가능한 IP 인지 - 체크 함
		if(userVo.getLginIp()!=null && isInChangeableSesnIp(userVo.getLginIp(), ip)){
			if(changeableIpList==null){
				changeableIpList = new ArrayList<String>();
				userVo.setChangeableIpList(changeableIpList);
			}
			changeableIpList.add(ip);
			return;
		}
		
		// IP 가 다를 경우 보안 로그 - IP 추출 HTTP Header 별 IP 나열함(차후 HTTP Header 수정 목적)
		String clientIp;
		StringBuilder builder = new StringBuilder(64);
		builder.append("[USER IP] - ").append(userVo.getUserUid()).append('/').append(userVo.getUserNm())
			.append("  Login IP : ").append(userVo.getLginIp()).append('\n');
		for(String header : getProxyHeaders()){
			if(header==null || header.isEmpty()) continue;
			clientIp = request.getHeader(header);
			if(clientIp!=null && !clientIp.isEmpty() && !clientIp.toLowerCase().equals("unknown")){
				builder.append("  ").append(header).append(" : ").append(clientIp).append('\n');
			}
		}
		builder.append("  RemoteAddr : ").append(request.getRemoteAddr());
		LOGGER.warn(builder.toString());
		
		// pt.logout.SesnIpPolc=IP 변경으로 인하여 로그인이 차단 됩니다.
		throw new SessionIpException("pt.logout.SesnIpPolc", request).setIp(ip);
	}
	
	/** 변경 가능한 세션IP 인지 검사 */
	private boolean isInChangeableSesnIp(String ip1, String ip2){
		if(sesnIpPlocList==null) return false;
		
		boolean hasIp1;
		for(List<IpPolc> ipList : sesnIpPlocList){
			if(ipList!=null){
				
				hasIp1 = false;
				for(IpPolc ipPolc : ipList){
					if(ipPolc.isInPolc(ip1)){
						hasIp1 = true;
						break;
					}
				}
				
				if(hasIp1){
					for(IpPolc ipPolc : ipList){
						if(ipPolc.isInPolc(ip2)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/** 클라이언트 IP 리턴 */
	public String getIp(HttpServletRequest request){
		String clientIp;
		for(String header : getHeader()){
			if(header==null || header.isEmpty()) continue;
			clientIp = request.getHeader(header);
			if(clientIp!=null && !clientIp.isEmpty() && !clientIp.toLowerCase().equals("unknown")){
				return toIPv4Stack(clientIp);
			}
		}
		return toIPv4Stack(request.getRemoteAddr());
	}
	
	private String toIPv4Stack(String ip){
		if("0:0:0:0:0:0:0:1".equals(ip)) return "127.0.0.1";
		return ip;
	}
	
	/** IP를 가져오는 HTTP HEADER 값 - null 이면 디폴트 방법으로 구함 */
	public String getIpHeader(){
		StringBuilder builder = new StringBuilder(64);
		boolean first = true;
		for(String header : getHeader()){
			if(first) first = false;
			else builder.append(", ");
			builder.append(header);
		}
		return builder.toString();
	}
	
	/** IP를 가져오는 HTTP HEADER 값 세팅 - 콤마(,) 구별 */
	public void setIpHeader(String header){
		if(header==null || header.isEmpty()) ipHeaders = null;
		else {
			header = header.replace(" ", "");
			header = header.replace(",,", ",");
			if(header.isEmpty()) ipHeaders = null;
			else {
				ipHeaders = header.split(",");
			}
		}
	}
//	/** 세션 IP 체크 설정 세팅 */
//	public void setSesnIp(boolean checkSesnIp, List<String> sessionIpExcpList){
//		this.checkSesnIp = checkSesnIp;
//		
//		//sesnIpPlocList
//		
//	}
	
	/** 외부망 권한 적용 여부 */
	public boolean isExAuth(UserVo userVo){
		return authByIpRangePloc && userVo.isInternalIp()!=null && !userVo.isInternalIp().booleanValue();
	}
}
