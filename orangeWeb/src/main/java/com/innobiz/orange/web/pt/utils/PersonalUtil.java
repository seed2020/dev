package com.innobiz.orange.web.pt.utils;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;

/** 개인화 정보 조회 */
public class PersonalUtil {

	/** 디폴트 페이지별 줄 수 */
	private static final Integer DEFAULT_PAGE_ROW_COUNT = Integer.valueOf(20);
	
	/** 페이지별 줄 수 리턴 */
	public static Integer getPageRowCnt(HttpServletRequest request) throws SQLException {
		
		String uri = (String)request.getAttribute("_uri");
		if(uri==null) uri = "";
		
		try{
			Integer rowCnt = (Integer)request.getAttribute("pageRowCnt");
			if(rowCnt!=null) return rowCnt;
		} catch(Exception ignore){}
		
		if(!ServerConfig.IS_MOBILE){
			// 팝업이면 10으로 고정
			if(uri.endsWith("Pop.do")) return 10;
			
			// 파라미터값 있으면 파라미터 값 리턴
			String rowCnt = request.getParameter("pageRowCnt");
			if(rowCnt!=null && !rowCnt.isEmpty()){
				try{
					return Integer.valueOf(rowCnt, 10);
				} catch(Exception ignore){}
			}
		}
		
		// 모듈별 레코드 수 설정 맵 리턴
		Map<String, Integer> pageRecCntMap = PtPsnSvc.ins.getPageRecCntMap(request);
		if(pageRecCntMap==null) return DEFAULT_PAGE_ROW_COUNT;
		
		String uriPrefix = uri.length()==0?"":uri.substring(1, 3);
		if("or".equals(uriPrefix)) uriPrefix = "pt";
		Integer no;
		if((no = pageRecCntMap.get(uriPrefix)) != null) return no;
		return DEFAULT_PAGE_ROW_COUNT;
	}
	
	/** 페이지별 줄 수 리턴 */
	public static Integer getPageRowCnt(String uriPrefix, HttpSession session) throws SQLException{
		Map<String, Integer> pageRecCntMap = PtPsnSvc.ins.getPageRecCntMap(session);
		if(pageRecCntMap==null) return DEFAULT_PAGE_ROW_COUNT;
		
		Integer no;
		if((no = pageRecCntMap.get(uriPrefix)) != null) return no;
		return DEFAULT_PAGE_ROW_COUNT;
	}
	
	/** 현재 페이지, 페이지별 레코드수 세팅
	 * - recodeCount(DB의 데이터 건수) 를 넘겨 주면 pageNo가 잘못 되었을 때 수정해줌 */
	public static void setPaging(HttpServletRequest request, CommonVo commonVo, Integer recodeCount) throws SQLException{
		Integer pageRowCnt = getPageRowCnt(request);
		commonVo.setPageNo(1);
		commonVo.setPageRowCnt(pageRowCnt);
		String pageNo = request.getParameter("pageNo");
		if(pageNo!=null && !pageNo.isEmpty()){
			try{
				commonVo.setPageNo(Math.max(Integer.parseInt(pageNo), 1));
			} catch(Exception ignore){}
		}
		if(recodeCount!=null){
			int currPage = (recodeCount / pageRowCnt)+1;
			if(commonVo.getPageNo() > currPage) commonVo.setPageNo(currPage);
		}
	}
	
	/** 현재 페이지, 페이지별 레코드수를 FixedpageRowCnt 로 세팅
	 * - recodeCount(DB의 데이터 건수) 를 넘겨 주면 pageNo가 잘못 되었을 때 수정해줌
	 * */
	public static void setFixedPaging(HttpServletRequest request, CommonVo commonVo, Integer FixedpageRowCnt, Integer recodeCount){
		Integer pageRowCnt = FixedpageRowCnt;
		commonVo.setPageNo(1);
		commonVo.setPageRowCnt(pageRowCnt);
		String pageNo = request.getParameter("pageNo");
		if(pageNo!=null && !pageNo.isEmpty()){
			try{
				commonVo.setPageNo(Math.max(Integer.parseInt(pageNo), 1));
			} catch(Exception ignore){}
		}
		if(recodeCount!=null){
			int currPage = (recodeCount / pageRowCnt)+1;
			if(commonVo.getPageNo() > currPage) commonVo.setPageNo(currPage);
		}
	}
	
	/** 디폴트 페이지의 레코드 수 */
	public static Integer getDefaultPageRowCount(){
		return DEFAULT_PAGE_ROW_COUNT;
	}
}
