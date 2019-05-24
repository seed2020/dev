<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"	
%><%
// 코드1 목록
String[][] cd1List = {
		{"1", "1팀"},
		{"2", "2팀"},
		{"3", "3팀"},
		{"4", "4팀"},
		{"5", "5팀"},
		{"6", "6팀"},
		{"7", "7팀"},
		{"99", "기타"}
	};
request.setAttribute("cd1List", cd1List);

//코드2 목록
String[][] cd2List = {
		{"1", "1팀"},
		{"2", "2팀"},
		{"3", "3팀"},
		{"4", "4팀"},
		{"5", "5팀"},
		{"6", "6팀"},
		{"7", "7팀"}
	};
request.setAttribute("cd2List", cd2List);

//코드3 목록
String[][] cd3List = {
		{"1", "전일지침"},
		{"2", "금일지침"},
		{"3", "사용량"}
	};
request.setAttribute("cd3List", cd3List);

//코드4 목록
String[][] cd4List = {
		{"1", "전일지침"},
		{"2", "금일지침"},
		{"3", "사용량"}
	};
request.setAttribute("cd4List", cd4List);

//코드5 목록
String[][] cd5List = {
		{"1", "전일지침"},
		{"2", "금일지침"},
		{"3", "사용량"}
	};
request.setAttribute("cd5List", cd5List);

//코드6 목록
String[][] cd6List = {
		{"1", "소화전주펌프"},
		{"2", "소화전예비펌프"},
		{"3", "S/P주펌프"},
		{"4", "S/P예비펌프"},
		{"99", "기타"}
	};
request.setAttribute("cd6List", cd6List);

%>