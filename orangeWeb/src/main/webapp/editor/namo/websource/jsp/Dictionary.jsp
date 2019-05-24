<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>

<%
String source = request.getParameter("source");
String target = request.getParameter("target");
String input = request.getParameter("input");
BufferedReader in = null;
InputStreamReader isr = null;
//System.out.println(source +" : "+target+" : "+input);
try {
            
	try{
		//URL url = new URL("http://ses8demo2.systran-seoul.co.kr:8903/translation/text/translate?key=93fc0ceb-c7a6-48e2-8886-e151fc2cec30&source=ko&target=en&input=%EA%B0%95%EC%95%84%EC%A7%80");
		String tUrl = "http://platform.systran-saas.co.kr:8903/resources/dictionary/lookup?key=2bf64954-825f-4f2e-afcb-577e6ddbaa96&source="+source+"&target="+target+"&input="+input;
		URL url = new URL(tUrl);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");

		isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
		in = new BufferedReader(isr);
		String inputLine;
		StringBuffer sb =  new StringBuffer();
		while((inputLine = in.readLine()) != null) {
			//System.out.println(inputLine);
			sb.append(inputLine);
		}   
		conn.disconnect();
		response.getWriter().println(sb.toString());
	}catch(java.io.IOException e) {
		//System.out.println("E 1="+e.getMessage()+", E 2="+e.toString());
		//e.printStackTrace();
		String errMsg = "{'outputs':[{'error':'internal server error.'}]}";
		response.getWriter().println(errMsg);
	}finally{
		try{
			if( in != null){
				in.close();
				in = null;
			}
			if( isr != null){
				isr.close();
				isr = null;
			}
		}catch(java.io.IOException err1){
			//System.out.println("An internal exception occured!!");
		}
	}
                 

}catch(java.net.MalformedURLException e) {
	//System.out.println("E 1="+e.getMessage()+", E 2="+e.toString());
	//e.printStackTrace();
	String errMsg = "{'outputs':[{'error':'internal server error.'}]}";
	response.getWriter().println(errMsg);
}
%>
