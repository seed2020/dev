<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><!DOCTYPE html>
<html><script type="text/javascript">
window.onload = function(){
	var a = document.createElement('a');
    a.setAttribute("href", "/?device=${param.device ne device ? device : 'ios'}");
    a.setAttribute("target", "_self");
    var dispatch = document.createEvent("HTMLEvents");
    dispatch.initEvent("click", true, true);
    a.dispatchEvent(dispatch);
};
</script><body></body></html>