<%@ page contentType="text/html; charset=UTF-8"
		import="
			java.lang.management.ManagementFactory,
			java.lang.management.MemoryMXBean,
			java.lang.management.MemoryUsage,
			java.lang.management.MemoryPoolMXBean,
			java.text.DecimalFormat,
			java.text.NumberFormat,
			java.util.List"
%><%!
	private static String kbytes(long l) {
		NumberFormat nf = new DecimalFormat("#,###");
		return nf.format(l/1000);
	}
	
	private static String percent(double l) {
		NumberFormat nf = new DecimalFormat("#.#");
		return nf.format(l * 100.0D);
	}
%><%
	// Memory Info
	MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
	MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
	MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
	
%><!DOCTYPE html>
<html>
<head>
<title>Memory state</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Pragma" content="no-cache" />
<style type="text/css">
<!--
body {
	font: 12px sans-serif;
}

table {
	border: 0px solid #DEDEDE;
}

td, th {
	border: 1px solid #DEDEDE;
	padding: 1px 10px;
}

.right {
	text-align: right;
}
-->
</style>
</head>
<body>

<table>
<tr>
<th>Area</th>
<th>Init (KB)</th>
<th>Max (KB)</th>
<th>Used (KB)</th>
<th>Used(%)</th>
</tr>

<tr>
<td>Heap</td>
<td class="right"><%=kbytes(heapMemoryUsage.getInit())%></td>
<td class="right"><%=kbytes(heapMemoryUsage.getMax())%></td>
<td class="right"><%=kbytes(heapMemoryUsage.getUsed())%></td>
<td class="right"><%=percent((double)heapMemoryUsage.getUsed()/(double)heapMemoryUsage.getMax())%></td>
</tr>

<tr>
<td>Non Heap</td>
<td class="right"><%=kbytes(nonHeapMemoryUsage.getInit())%></td>
<td class="right"><%=kbytes(nonHeapMemoryUsage.getMax())%></td>
<td class="right"><%=kbytes(nonHeapMemoryUsage.getUsed())%></td>
<td class="right"><%=percent((double)nonHeapMemoryUsage.getUsed()/(double)nonHeapMemoryUsage.getMax())%></td>
</tr>
</table>


<table>
<tr>
<th>Area</th>
<th>Init (KB)</th>
<th>Max (KB)</th>
<th>Used (KB)</th>
<th>Used(%)</th>
</tr>

<%

MemoryUsage memoryUsage;
List<MemoryPoolMXBean> memoryPoolMXBeanList = ManagementFactory.getMemoryPoolMXBeans();
for(MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeanList){
	
	memoryUsage = memoryPoolMXBean.getUsage();
%>
<tr>
<td><%= memoryPoolMXBean.getName()%></td>
<td class="right"><%=kbytes(memoryUsage.getInit())%></td>
<td class="right"><%=kbytes(memoryUsage.getMax())%></td>
<td class="right"><%=kbytes(memoryUsage.getUsed())%></td>
<td class="right"><%=percent((double)memoryUsage.getUsed()/(double)memoryUsage.getMax())%></td>
</tr>
<%
}
%>
</table>


</body>
</html>