<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"


%><!DOCTYPE html>
<html lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">

body, html { font:12px "dotum","arial"; color:#454545; background:#ffffff; margin:0;}

a:link    { color:#454545; text-decoration:none; }
a:visited { color:#454545; text-decoration:none; }
a:hover   { color:#454545; text-decoration:none; }
a:active  { color:#454545; text-decoration:none; }
dl, dt, dd { margin:0; padding:0; }
img { border:0; }
hr { display:none; }                               
form { margin:0; padding:0; }
img { border-top-width:0px; border-left-width:0px; border-bottom-width:0px; border-right-width:0px; }
hr { display:none; }
body { margin:0; padding:0; }
img { border:none; } 
a { selector-dummy:expression(this.hideFocus=true); } 

.approvaltable { background:#bfc8d2; color:#454545; }
.approvaltable tr { background:#ffffff; }
.approval_head { width:26px; text-align:center; padding:3px 0 0 0; background:#ebf1f6; line-height:17px; }
.approval_headw { width:95px; text-align:center; padding:3px 0 0 0; background:#ebf1f6; line-height:17px; }
.approval_body { width:95px; height:22px; text-align:center; padding:3px 3px 0 3px; line-height:17px; }
.approval_img { width:95px; height:50px; text-align:center; margin:0 auto 0 auto; padding:7px 0 7px 0; }
.approval_bodyin { width:100%; height:18px; overflow:hidden; text-align:center; padding-top:2px; }

/* list area */
.listarea  { float:left; width:100%; padding:0; margin:0; color:#454545; }
.listtable { width:100%; background:#bfc8d2; color:#454545; }
.listtable tr { background:#ffffff; }
.listtable .trover { background:#ecffd6; }
.listtable .trout { background:#ffffff; }
.listtable .folder { background:#f3f3f3; }
.listbody { background:#ffffff; border-left:1px solid #bfc8d2; border-right:1px solid #bfc8d2; border-bottom:1px solid #bfc8d2; overflow-x:hidden; overflow-y:auto; }
/* list head bg */
.listtable .head_bg { height:24px; text-align:center; background:#ebf1f6; padding:0 0 0 5px; }
.listtable .head_rd { background:#ebf1f6; padding:0 0 0 1px; }
/* list head left */
.listtable .head_lt { height:22px; background:#ebf1f6; line-height:17px; padding:2px 0 0 4px; }
.listtable .head_lt a:link    { color:#454545; }
.listtable .head_lt a:visited { color:#454545; }
.listtable .head_lt a:active  { color:#454545; text-decoration:none; }
.listtable .head_lt a:hover   { color:#454545; text-decoration:none; }
/* list head center */
.listtable .head_ct { height:22px; text-align:center; background:#ebf1f6; line-height:17px; padding:2px 2px 0 2px; }
.listtable .head_ct a:link    { color:#454545; }
.listtable .head_ct a:visited { color:#454545; }
.listtable .head_ct a:active  { color:#454545; text-decoration:none; }
.listtable .head_ct a:hover   { color:#454545; text-decoration:none; }
/* list head right */
.listtable .head_rt { height:22px; text-align:right; background:#ebf1f6; line-height:17px; padding:2px 4px 0 0; }
.listtable .head_rt a:link    { color:#454545; }
.listtable .head_rt a:visited { color:#454545; }
.listtable .head_rt a:active  { color:#454545; text-decoration:none; }
.listtable .head_rt a:hover   { color:#454545; text-decoration:none; }
/* list head essential left */
.listtable .head_ess_lt { height:22px;color:#d22681; background:#ebf1f6; line-height:17px; padding:2px 0 0 4px; }
.listtable .head_ess_lt a:link    { color:#d22681; }
.listtable .head_ess_lt a:visited { color:#d22681; }
.listtable .head_ess_lt a:active  { color:#d22681; text-decoration:none; }
.listtable .head_ess_lt a:hover   { color:#d22681; text-decoration:none; }
/* list head essential center */
.listtable .head_ess_ct { height:22px; color:#d22681; text-align:center; background:#ebf1f6; line-height:17px; padding:2px 2px 0 2px; }
.listtable .head_ess_ct a:link    { color:#d22681; }
.listtable .head_ess_ct a:visited { color:#d22681; }
.listtable .head_ess_ct a:active  { color:#d22681; text-decoration:none; }
.listtable .head_ess_ct a:hover   { color:#d22681; text-decoration:none; }
/* list body bg */
.bodybg_lt { padding:1px 0 2px 2px; }
.bodybg_ct { height:23px; text-align:center; padding:0 0 1px 4px; }
.bodybg_rt { height:23px; text-align:right; padding:0 0 1px 0; }
/* list body left */
.body_lt { height:22px; color:#454545; line-height:17px; padding:2px 3px 0 4px; }
.body_lt a:link    { color:#454545; }
.body_lt a:visited { color:#454545; }
.body_lt a:active  { color:#454545; text-decoration:none; }
.body_lt a:hover   { color:#454545; text-decoration:none; }
.bodyip_lt { color:#454545; line-height:18px; padding:3px 0 0 0; margin:0; }
/* list body new left */
.body_new_lt { font-weight:bold; height:22px; color:#4685d3; line-height:17px; padding:2px 3px 0 4px; }
.body_new_lt a:link    { color:#4685d3; }
.body_new_lt a:visited { color:#4685d3; }
.body_new_lt a:active  { color:#4685d3; text-decoration:none; }
.body_new_lt a:hover   { color:#4685d3; text-decoration:none; }
/* list body center */
.body_ct { height:22px; color:#454545; text-align:center; line-height:17px; padding:2px 3px 0 3px; }
.body_ct a:link    { color:#454545; }
.body_ct a:visited { color:#454545; }
.body_ct a:active  { color:#454545; text-decoration:none; }
.body_ct a:hover   { color:#454545; text-decoration:none; }

.bodytime { color:#454545; padding:0; }
.bodytime_red { color:#454545; border:1px solid #47aee1; background:#9ddcf3; padding:0; }
/* list body right */
.body_rt { height:22px; color:#454545; text-align:right; line-height:17px; padding:2px 4px 0 3px; }
.body_rt a:link    { color:#454545; }
.body_rt a:visited { color:#454545; }
.body_rt a:active  { color:#454545; text-decoration:none; }
.body_rt a:hover   { color:#454545; text-decoration:none; }

.titlearea { width:100%; height:16px; margin:0 0 9px 0; }
.titlearea .tit_left { float:left; margin:0; }
.titlearea .tit_left .title { float:left; height:13px; font-weight:bold; background:url("/images/blue/dot_title.png") no-repeat 0 3px; padding:3px 4px 0 10px; }
.titlearea .tit_left .title_s { float:left; height:13px; font-weight:bold; color:#454545; background:url("/images/blue/dot_title_s.png") no-repeat 0 1px; padding:3px 4px 0 9px; }
.titlearea .tit_right { float:right; padding:0; margin:0; }
.titlearea .tit_right ul { list-style:none; padding:0; margin:0; }
.titlearea .tit_right li { float:left; display:inline; padding:0; margin:0; }
.titlearea .tit_right .btn { float:left; padding:0; margin:0 0 2px 0; }
.titlearea .tit_right .ico { float:left; padding:0; margin:0 0 0 4px; }
.titlearea .tit_right .txt { float:left; color:#454545; background:url("/images/blue/dot_search.png") no-repeat 0 9px; height:13px; padding:6px 5px 0 8px; margin:0; }

.blank { clear:both; height:10px; }
</style>
</head>
<body style="height:auto; padding-bottom:0px;">

<div class="styleThese">
<tiles:insertAttribute name="body" />
</div>

</body>
</html>