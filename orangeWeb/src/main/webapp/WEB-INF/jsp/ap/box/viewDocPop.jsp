<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:960px; height:750px;">
<iframe id="viewDocFrm" name="viewDocFrm" src="./view${not empty dmUriBase ? 'Ap' : ''}DocFrm.do?menuId=${menuId
	}&bxId=${param.bxId}${strMnuParam
	}${not empty param.apvNo ? '&apvNo='.concat(param.apvNo) : ''
	}${not empty param.vwMode ? '&vwMode='.concat(param.vwMode) : ''
	}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''
	}${not empty param.secuId ? '&secuId='.concat(param.secuId) : ''
	}${not empty param.intgNo ? '&intgNo='.concat(param.intgNo) : ''
	}${not empty param.intgTypCd ? '&intgTypCd='.concat(param.intgTypCd) : ''
	}" style="width:960px; height:750px; overflow-y:auto;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>