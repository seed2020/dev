package com.innobiz.orange.web.cu.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.or.sync.SimpleIterator;
import com.innobiz.orange.web.or.sync.SimpleQuery;

/** 게시파일 서비스 */
@Service
public class CuPlakorSvc {

	/** 외부 DB 서비스*/
	@Autowired
	private CuFncSvc cuFncSvc;
	
	/** 마감구분 목록 조회 */
	public List<String[]> getMajorCdList() throws SQLException, IOException{
		List<String[]> returnList=new ArrayList<String[]>();
		String[] majorCd=new String[]{"ZU001", "물류마감"};
		returnList.add(majorCd); // 물류마감
		
		return returnList;
	}
	
	public List<Map<String,String>> getList(Map<String,Object> params) throws SQLException, IOException{
		List<Map<String,String>> returnList=null;
		SimpleQuery query=cuFncSvc.createQuery(null);
		
		if(query!=null){
			// 마감구분
			List<String> closeKndList=new ArrayList<String>();
			if(params.containsKey("closeKnd") && !"".equals(params.get("closeKnd"))){
				closeKndList.add((String)params.get("closeKnd"));
			}else{
				closeKndList.add("ZU001");
			}
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT T.YYYYMM,");
			sql.append(" T.CLOSE_KIND,");
			sql.append(" T.CLOSE_TYPE,");
			sql.append(" T.CLOSE_FLAG,");
			sql.append(" T.CLOSE_DT,");
			sql.append(" T.CLOSE_PLAN_DT,");
			sql.append(" T.CLOSE_CONFIRM_DT,");
			sql.append(" T.CLOSE_DEPT,");
			sql.append(" (SELECT MAJOR_NM FROM B_MAJOR M WHERE T.CLOSE_KIND = M.MAJOR_CD) AS MAJOR_NM,");
			sql.append(" (SELECT MINOR_NM FROM B_MINOR I WHERE T.CLOSE_KIND = I.MAJOR_CD AND T.CLOSE_TYPE = I.MINOR_CD) AS MINOR_NM").append("\n");
			sql.append(" FROM Z_CLOSE_STATUS_KO501 T").append("\n");
			sql.append(" WHERE T.CLOSE_KIND IN (");
			for(int i=0;i<closeKndList.size();i++){
				if(i>0) sql.append(",");
				sql.append("'"+closeKndList.get(i)+"'");
			}
			sql.append(")").append("\n");
			sql.append(" AND T.CLOSE_PLAN_DT>=").append("CONVERT(DATETIME,'").append(params.get("start")).append("', 120)\n");
			sql.append(" AND T.CLOSE_PLAN_DT<=").append("CONVERT(DATETIME,'").append(params.get("end")).append("', 120)\n");
			try{
				SimpleIterator iterator = query.queryIterator(sql.toString(), null);			
				
				Map<String, String> listData;
				
				if(iterator!=null){
					returnList=new ArrayList<Map<String,String>>();
					while((listData = iterator.next()) != null){
						returnList.add(listData);
					}
				}
			}finally{
				query.close();
			}
		}
		return returnList;
	}
	
	/** 마감 구분 조회*/
	public List<Map<String,String>> getCloseKndList(Map<String,Object> params) throws SQLException, IOException{
		List<Map<String,String>> returnList=null;
		SimpleQuery query=cuFncSvc.createQuery(null);
		
		if(query!=null){
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT  S.CLOSE_KIND , \n");
			sql.append("B.MINOR_NM , \n");
			sql.append("CONVERT(INT, CONVERT(NVARCHAR(2), S.CLOSE_PLAN_DT, 105)) AS CLOSE_PLAN_DT , \n");
			sql.append("( SELECT CONVERT(INT, CONVERT(NVARCHAR(2), MAX(D.CLOSE_CONFIRM_DT), 105)) \n");
			sql.append("FROM dbo.B_CLOSE_STATUS_DEPT_KO501 D ( NOLOCK ) \n");
			sql.append("WHERE D.YYYYMM = S.YYYYMM \n");
			sql.append("AND D.CLOSE_KIND = S.CLOSE_KIND \n");
			sql.append("AND D.CLOSE_CONFIRM_FLAG = 'Y' \n");
			sql.append(") DEPT_CLOSE_DT \n");
			sql.append("FROM    dbo.B_CLOSE_STATUS_KO501 S ( NOLOCK ) \n");
			sql.append("INNER JOIN dbo.B_MINOR B ( NOLOCK ) ON B.MAJOR_CD = N'UZB01' \n");
			sql.append("AND S.CLOSE_KIND = B.MINOR_CD \n");
			sql.append("WHERE 1=1\n");
			if(params.containsKey("yyyymm") && params.get("yyyymm")!=null && !((String)params.get("yyyymm")).isEmpty()){
				sql.append(" AND S.YYYYMM=").append("N'").append((String)params.get("yyyymm")).append("'\n");
			}
			if(params.containsKey("start") && params.get("start")!=null && !((String)params.get("start")).isEmpty()){
				sql.append(" AND CONVERT(int, S.YYYYMM)>=").append((String)params.get("start")).append("\n");
			}
			if(params.containsKey("end") && params.get("end")!=null && !((String)params.get("end")).isEmpty()){
				sql.append(" AND CONVERT(int, S.YYYYMM)<=").append((String)params.get("end")).append("\n");
			}
			try{
				SimpleIterator iterator = query.queryIterator(sql.toString(), null);			
				
				Map<String, String> listData;
				
				if(iterator!=null){
					returnList=new ArrayList<Map<String,String>>();
					while((listData = iterator.next()) != null){
						returnList.add(listData);
					}
				}
			}finally{
				query.close();
			}
		}
		return returnList;
	}
	
	/** 마감 목록 조회 */
	public List<Map<String,String>> getDeadlineList(Map<String,Object> params) throws SQLException, IOException{
		List<Map<String,String>> returnList=null;
		SimpleQuery query=cuFncSvc.createQuery(null);
		
		if(query!=null){
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT C.CLOSE_KIND, B.MINOR_NM, D.DEPT_NM , \n");
			sql.append("CASE WHEN C.CLOSE_FLAG = 'Y' \n");
			sql.append("THEN CONVERT(NVARCHAR(10), C.CLOSE_DT, 120) \n");
			sql.append("ELSE NULL \n");
			sql.append("END AS CLOSE_DT, \n");
			sql.append("CASE WHEN C.CLOSE_CONFIRM_FLAG = 'Y' \n");
			sql.append("THEN CONVERT(NVARCHAR(10), C.CLOSE_CONFIRM_DT, 120) \n");
			sql.append("ELSE NULL \n");
			sql.append("END AS CLOSE_CONFIRM_DT, \n");
			sql.append("A.CLOSE_PLAN_DT \n");
			sql.append("FROM dbo.B_CLOSE_STATUS_DEPT_KO501 C ( NOLOCK ) \n");
			sql.append("INNER JOIN dbo.B_MINOR B ( NOLOCK ) ON B.MAJOR_CD = N'UZB01' AND C.CLOSE_KIND = B.MINOR_CD \n");
			sql.append("INNER JOIN B_CLOSE_STATUS_KO501 A(NOLOCK) ON C.YYYYMM = A.YYYYMM AND C.CLOSE_KIND = A.CLOSE_KIND \n");
			sql.append("INNER JOIN dbo.B_ACCT_DEPT D ( NOLOCK ) ON C.DEPT_CD = D.DEPT_CD \n");
			sql.append("AND D.ORG_CHANGE_ID = ( SELECT \n");
			sql.append("MAX(ORG_CHANGE_ID) \n");
			sql.append("FROM \n");
			sql.append("dbo.B_ACCT_DEPT (NOLOCK) \n");
			sql.append("WHERE \n");
			sql.append("ORG_CHANGE_DT <= GETDATE() \n");
			sql.append(") \n");
			sql.append("WHERE 1=1 \n");
			if(params.containsKey("closeKindList") && params.get("closeKindList")!=null){
				// 마감구분
				@SuppressWarnings("unchecked")
				List<String> closeKindList=(List<String>)params.get("closeKindList");
				if(closeKindList.size()>0){
					sql.append(" AND C.CLOSE_KIND IN (");
					for(int i=0;i<closeKindList.size();i++){
						if(i>0) sql.append(",");
						sql.append("'"+closeKindList.get(i)+"'");
					}
					sql.append(")").append("\n");
				}
			}
			if(!params.containsKey("closeKindList") && params.containsKey("closeKind") && params.get("closeKind")!=null && !((String)params.get("closeKind")).isEmpty()){
				sql.append(" AND C.CLOSE_KIND=").append("'").append((String)params.get("closeKind")).append("'\n");
			}
			if(params.containsKey("yyyymm") && params.get("yyyymm")!=null && !((String)params.get("yyyymm")).isEmpty()){
				sql.append(" AND C.YYYYMM=").append("N'").append((String)params.get("yyyymm")).append("'\n");
			}
			if(params.containsKey("start") && params.get("start")!=null && !((String)params.get("start")).isEmpty()){
				sql.append(" AND CONVERT(int, C.YYYYMM)>=").append((String)params.get("start")).append("\n");
			}
			if(params.containsKey("end") && params.get("end")!=null && !((String)params.get("end")).isEmpty()){
				sql.append(" AND CONVERT(int, C.YYYYMM)<=").append((String)params.get("end")).append("\n");
			}
			if(params.containsKey("closeKindList")){
				sql.append("ORDER BY C.CLOSE_KIND\n");
			}
			try{
				SimpleIterator iterator = query.queryIterator(sql.toString(), null);			
				
				Map<String, String> listData;
				
				if(iterator!=null){
					returnList=new ArrayList<Map<String,String>>();
					while((listData = iterator.next()) != null){
						returnList.add(listData);
					}
				}
			}finally{
				query.close();
			}
		}
		return returnList;
	}
	
	public String getAttrColumns(String attr){
		String returnAttr=null;
		return returnAttr;
	}
}
