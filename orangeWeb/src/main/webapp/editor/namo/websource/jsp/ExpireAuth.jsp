<%@page contentType="application/json"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.net.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.security.Security"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="Util.jsp"%>
<%@include file="SecurityTool.jsp"%>
<%@include file="expireInfo.jsp"%>
<%!
	static public String getEncMD5(String str) {
	   String MD5 = ""; 
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			//e.printStackTrace(); 
			MD5 = null; 
		}
		return MD5;
	}
	static public String getKey(String strExpire, String strSerial) {
		return strExpire + "|" + strSerial;
	}
%>
<%

	String strDictKey = getEncMD5 (getKey (ce_dict_expire_date, ce_serial));
	String strDictExpire = "";
	if (strDictKey.equalsIgnoreCase(ce_dict_key)) {
		strDictExpire = ce_dict_expire_date;
	}
	String strTransKey = getEncMD5 (getKey (ce_trans_expire_date, ce_serial));
	String strTransExpire = "";
	if (strTransKey.equalsIgnoreCase(ce_trans_key)) {
		strTransExpire = ce_trans_expire_date;
	}
	String strVaccineKey = getEncMD5 (getKey (ce_vaccine_expire_date, ce_serial));
	String strVaccineExpire = "";
	if (strVaccineKey.equalsIgnoreCase(ce_vaccine_key)) {
		strVaccineExpire = ce_vaccine_expire_date;
	}

	Date d1 = new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate = df.format(d1);
%>
{"dict":"<%=strDictExpire%>", "trans":"<%=strTransExpire%>", "vaccine":"<%=strVaccineExpire%>", "cur_date":"<%=formattedDate%>"}