<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="pltLoutCd" required="true"
%><%@ attribute name="outer" required="true" type="java.lang.Boolean"
%><%@ attribute name="no" required="true" type="java.lang.Integer"
%><%
/*
 [[ 포틀릿 설정에서 사용하는 Layout용 - 1단, 2단, 3단 나누는 테그 ]]

pltLoutCd : 포틀릿레이아웃코드
	D2R37:2단 (3:7), D2R46:2단 (4:6), D2R55:2단 (5:5), D2R64:2단 (6:4), D2R73:2단 (7:3)
	D3R111:3단 (1:1:1), D3R112:3단 (1:1:2), D3R121:3단 (1:2:1), 
	D3R211:3단 (2:1:1), D3R221:3단 (2:2:1), D3R212:3단 (2:1:2)

2단 레이아웃
	- 좌우 비율과 float:left, float:right 으로 조절
	- outer:true 일 경우만 테그 추가
	
3단 레이아웃 - 좌우 대칭
	- 좌중우 비율과 좌우의  float:left, float:right 으로 조절, 중앙은 margin 설정만
	- outer:true 일 경우만 테그 추가
	
	D3R111:3단 (1:1:1),
	D3R121:3단 (1:2:1),
	D3R212:3단 (2:1:2)

3단 레이아웃 - 좌우 [비]대칭
	- 중첩 DIV 사용 - 동일 비율의 것을 중첩 DIV
	- float:left, float:right 으로 조절

*/

	boolean nestedLoopStart = false;
	boolean nestedLoopEnd = false;
	String width = "";
	String outerStyle = "";
	String innerStyle = "";
	
	// 3단 비대칭 의 경우 - 앞쪽 중첩 DIV
	if("D3R112".equals(pltLoutCd)){
		if(outer){
			if(no.intValue()==1){
				nestedLoopStart = true;//중첩 루프 OPEN
				width = "49.5%";
				outerStyle = "float:left;";
			} else if(no.intValue()==2){
				nestedLoopEnd = true;//중첩 루프 CLOSE
				width = "49%";
				outerStyle = "float:right;";
				innerStyle = "float:right;";
			} else {
				width = "49.5%";
				outerStyle = "float:right;";
				innerStyle = "float:right;";
			}
		} else {
			if(no.intValue()==1){
				width = "49%";
				outerStyle = "float:left;";
				innerStyle = "float:left;";
				outer = true;// outer 에서 중첩 DIV 만들고 실제 DIV 만들지 않아서 실제 DIV 만듬
			}
		}
	} else if("D3R221".equals(pltLoutCd)){
		if(outer){
			if(no.intValue()==1){
				nestedLoopStart = true;//중첩 루프 OPEN
				width = "79%";
				outerStyle = "float:left;";
			} else if(no.intValue()==2){
				nestedLoopEnd = true;//중첩 루프 CLOSE
				width = "49.4%";
				outerStyle = "float:right;";
				innerStyle = "float:right;";
			} else {
				width = "20%";
				outerStyle = "float:right;";
				innerStyle = "float:right;";
			}
		} else {
			if(no.intValue()==1){
				width = "49.3%";
				outerStyle = "float:left;";
				innerStyle = "float:left;";
				outer = true;// outer 에서 중첩 DIV 만들고 실제 DIV 만들지 않아서 실제 DIV 만듬
			}
		}
	// 3단 비대칭 의 경우 - 뒤쪽 중첩 DIV
	} else if("D3R211".equals(pltLoutCd)){
		if(outer){
			if(no.intValue()==1){
				width = "49.5%";
				outerStyle = "float:left;";
				innerStyle = "float:left;";
			} else if(no.intValue()==2){
				nestedLoopStart = true;//중첩 루프 OPEN
				width = "49.5%";
				outerStyle = "float:right;";
			} else {
				nestedLoopEnd = true;//중첩 루프 CLOSE
				width = "49%";
				outerStyle = "float:right;";
				innerStyle = "float:right;";
			}
		} else {
			if(no.intValue()==2){
				width = "49%";
				outerStyle = "float:left;";
				innerStyle = "float:left;";
				outer = true;// outer 에서 중첩 DIV 만들고 실제 DIV 만들지 않아서 실제 DIV 만듬
			}
		}
	// 2단 또는 3단 대칭
	} else {
		if(outer){
			if(pltLoutCd.startsWith("D2") && pltLoutCd.length()==5){
				int leftRatio = (pltLoutCd.charAt(3) - '0') * 10;
				if(no.intValue()==1){
					width = (leftRatio-1)+"%";
					outerStyle = "float:left;";
					innerStyle = "float:left;";
				} else {
					width = (100-leftRatio)+"%";
					outerStyle = "float:right;";
					innerStyle = "float:right;";
				}
			} else {
				if("D3R111".equals(pltLoutCd)){
					width = (no.intValue()==2) ? "32%" : "33%";
				} else if("D3R121".equals(pltLoutCd)){
					width = (no.intValue()==2) ? "49%" : "25%";
				} else if("D3R212".equals(pltLoutCd)){
					width = (no.intValue()==2) ? "19%" : "40%";
				}
				if(no.intValue()==1){
					outerStyle = "float:left;";
					innerStyle = "float:left;";
				} else if(no.intValue()==2){
					outerStyle = "margin:0 auto 0 auto;";
					innerStyle = "float:left;";
				} else {
					outerStyle = "float:right;";
					innerStyle = "float:right;";
				}
			}
		}
	}
	
	// 중첩 DIV - OPEN
	if(nestedLoopStart){
		out.write("	<div style=\""+outerStyle+" width:"+width+";\">\r\n");

		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
	
	// 2단 또는 3단 대칭의 경우
	// 3단 비대칭에서 중첩되지 않은 경우
	// 3단 비대칭에서 중첩되는 경우의  inner 테그의 경우
	// >> 일반 단락 나누는 DIV 출력
	} else if(outer){
		
%>	<div style="<%=outerStyle%> width:<%= width%>;">
		<div style="<%=innerStyle%> width:100%;">
<%
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
%>
		</div>
		</div>
<%
	
	// 2단 또는 3단 대칭 경우의 inner 테그의 경우
	// 3단 비대칭에서 중첩되지 않은 경우의 inner 테그의 경우
	} else {
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
	}
	
	// 중첩 DIV - CLOSE
	if(nestedLoopEnd){
		out.write("\r\n		</div>\r\n");
	}
%>