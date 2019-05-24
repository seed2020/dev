package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.sync.SimpleQuery;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcErpSchdlBVo;

/** 고객사별 기능 서비스 - 한화제약 */
@Service
public class ApCustHanwhaSvc{
	
	@Autowired
	private OrCmSvc orCmSvc;
	
	@Autowired
	private ApCustFncSvc apCustFncSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;	
	
	/** [한화제약] ERP UPDATE */
	public void updateHanwhaErp(Map<String, String> exMap) throws SQLException, IOException{
		
		String intgTypCd = exMap.get("intgTypCd");
		if("ERP_HANWHA".equals(intgTypCd)){
			
			String intgStatCd = exMap.get("intgStatCd");
			if("temp".equals(intgStatCd)){
				return;
			}
			
			String gubun = exMap.get("gubun");
			String mid = exMap.get("mid")==null ? exMap.get("fid") : exMap.get("mid");
			String oid = exMap.get("oid");
			String status
					= "cncl".equals(intgStatCd) ? "9"
					: "apvd".equals(intgStatCd) ? "3"
					: "rejt".equals(intgStatCd) ? "4"
					: "2";
			String apvNo = exMap.get("apvNo");
			if(oid==null || gubun==null) return;
			
			ArrayList<String> param = new ArrayList<String>();
			param.add(status);
			param.add(apvNo);
			param.add(gubun);
			param.add(mid);
			param.add(oid);
			
			String updateSQL = "UPDATE ERPAPP SET STATUS=?, GWKEY=? WHERE GUBUN=? AND MODULEID=? AND OBJECTID=?";
			String insertSQL = "INSERT INTO ERPAPP(STATUS, GWKEY, GUBUN, MODULEID, OBJECTID) VALUES ( ?,?,?,?,? )";
			
			// 한화제약 - E78CEB
			if(CustConfig.CUST_HANWHA){
				SimpleQuery query = apCustFncSvc.createQuery(null);
				try{
					int result = query.executeUpdate(updateSQL, param);
					if(result==0){
						query.executeUpdate(insertSQL, param);
					}
					query.commit();
				} finally {
					if(query!=null) query.close();
				}
			} else {
				System.out.println(updateSQL+"  : "+param);
			}
		}
	}
	
	/** [한화제약] 결재 시작 - ERP 테이블 INSERT */
	public void sendErpNotiFromAp(UserVo userVo, String apvNo, XMLElement xmlElement) throws SQLException, IOException, CmException{
//		if(!"E78CEB".equals(custCode) && !ServerConfig.IS_LOC) return;
		//if("E78CEB".equals(custCode)) return;
		String typId = xmlElement.getAttr("head.typId");
		if(!ArrayUtil.isInArray(new String[]{"training", "leave", "bizTrip"}, typId) ) return;
		//if("trainingResults".equals(typId) || typId.startsWith("notErp")) return;
		
		if("leave".equals(typId)){
			String erpOptions = xmlElement.getAttr("body/leave.erpOptions");
			if(erpOptions==null || erpOptions.isEmpty()) return;
		}
		
		SimpleQuery query = null;
		try{
//			System.out.println("================setHanwhaErpNotiFromAp Start========================");
			
			// 결재 정보 조회
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
			if(apOngdBVo==null) return;
			
			// 기안자UID
			String makrUid = apOngdBVo.getMakrUid();
			
			// 기안자 상세정보 조회
			Map<String, Object> userMap = orCmSvc.getUserMap(makrUid, "ko");
			
			String compId = (String)userMap.get("compId");
			if(compId==null) compId=userVo.getCompId();
			String langTypCd = (String)userMap.get("langTypCd");
			if(langTypCd==null) langTypCd=userVo.getLangTypCd();
			
			// 입력날짜
			String date = commonDao.querySysdate(null);
			
			date=date.replaceAll("[-: ]", "");
			date=date.substring(0,8); // yyyyMMdd
			// Sql Parameter
			List<String> param = null;
			
			String strtDtNm = getStrtDtNm(typId);
			if(strtDtNm==null){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : strtDtNm");
			}
			
			// 시작일(월,일) - 공통 파라미터
			String erpStart = xmlElement.getAttr(strtDtNm+".erpStart");
			if(erpStart==null || erpStart.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpStart");
			}
			
			// 종료일(월,일) - 공통 파라미터
			String erpEnd = xmlElement.getAttr(strtDtNm+".erpEnd");
			if(erpEnd==null || erpEnd.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpEnd");
			}
			
			// 제외할 날짜 목록
			List<String> excludeList = null;
			List<String> fromToList = null;
			List<XMLElement> dates = (List<XMLElement>)xmlElement.getChildList("body/dates");
//			System.out.println("dates : "+dates);
			if(dates==null){
				boolean isInclude=true;
				if("leave".equals(typId)){
					String natCd = wcScdManagerSvc.getNatCd(userVo);
					String realStrtDt = wcScdManagerSvc.getDateOfDay(erpStart, "month", "s", null, 1);
					excludeList = wcScdManagerSvc.getSelectSchdlList(compId, langTypCd, natCd, realStrtDt, erpEnd, null);
					isInclude=false;
				}
				String tmpStrtDt=erpStart.replaceAll("[-: ]", "");
				String tmpEndDt=erpEnd.replaceAll("[-: ]", "");
				fromToList = wcScdManagerSvc.getFromToDate(tmpStrtDt, tmpEndDt, excludeList, isInclude, null, false);
			}else{
				fromToList=new ArrayList<String>();
				for(XMLElement element : dates){
					fromToList.add(element.getAttr("erpDate"));
				}
			}
			
			if(fromToList==null || fromToList.size()==0){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : fromToList");
			}
			
			query = apCustFncSvc.createQuery(null);
			
			// 입력자 사번[기안자]
			String userEin = (String)userMap.get("ein");
						
			String pgMonth,pgDay;
			
			List<WcErpSchdlBVo> updateList = new ArrayList<WcErpSchdlBVo>();
			
			// ERP오류여부
			boolean isErpError=false;
			
			Exception exception = null;
			for(String day : fromToList){
				pgMonth = day.substring(0,6); // 연월
				pgDay = day.substring(6,8); // 일
				
				if("bizTrip".equals(typId)){ // 출장신청서
					List<XMLElement> users = (List<XMLElement>)xmlElement.getChildList("body/users");
					String pgDateColNm = getPgDateColNm("02"); // 출장
					if(pgDateColNm==null){
						throw new CmException("[ERROR] - 필수값이 없습니다.\tname : pgDateColNm");
					}
					
					for(XMLElement user : users){ // 사용자 별 저장
						
						// 일정VO 세팅
						wcScdManagerSvc.addErpSchdlVoList(updateList, day, null, "02", compId, user.getAttr("erpUserUid"), user.getAttr("erpName"));
						
						if(isErpError) continue; // ERP 오류시 continue
						
						param = new ArrayList<String>();
						setErpParamList(user, param, true, "erpGubun","erpEin"); // 회사구분, 신청자 사번
						setErpParam(param, true, "pgMonth", pgMonth); // 시작월
						setErpParam(param, true, "pgDay", pgDay); // 시작일
						// 출장일수 1
						param.add("1");
						
						// 출장사유
						setErpParamList(xmlElement, param, false, "body/period.erpReason");
						setErpParam(param, true, "userEin", userEin); // 입력자 사번 
						setErpParam(param, true, "todate", date); // 입력날짜(yyyyMMdd)
						if(query!=null){
							try{
								query.executeUpdate("INSERT INTO H_PR_GUNDAY (PG_GUBUN, PG_SNO, PG_MONTH, PG_DAY, "+pgDateColNm+", PG_REMARK, PG_INUSER, PG_INDATE) VALUES(?,?,?,?,?,?,?,?)",
									param);
							}catch(Exception e){
								exception=e;
								isErpError=true;
							}
						}
						
						//printParams(param); // 로그 출력
					}
					
				} else if("leave".equals(typId)){ // 휴가 | 조퇴원
					String erpOptions = xmlElement.getAttr("body/leave.erpOptions");
					if(erpOptions==null || erpOptions.isEmpty()){
						throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpOptions");
					}
					
					// 일정VO 세팅
					wcScdManagerSvc.addErpSchdlVoList(updateList, day, null, erpOptions, compId, xmlElement.getAttr("body/leave.erpUserUid"), xmlElement.getAttr("body/leave.erpUserNm"));
					
					if(isErpError) continue; // ERP 오류시 continue
					
					String pgDateColNm = getPgDateColNm(erpOptions); // 컬럼명
					if(pgDateColNm==null){
						throw new CmException("[ERROR] - 필수값이 없습니다.\tname : pgDateColNm");
					}
					param = new ArrayList<String>();
					
					setErpParamList(xmlElement, param, true, "body/leave.erpGubun","body/leave.erpEin"); // 회사구분, 신청자 사번
					setErpParam(param, true, "pgMonth", pgMonth); // 시작월
					setErpParam(param, true, "pgDay", pgDay); // 시작일
					
					// 휴가/조퇴일수
					String erpTotalDay = xmlElement.getAttr("body/leave.erpTotalDay");
					if(erpTotalDay==null || erpTotalDay.isEmpty()){
						throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpTotalDay");
					}
					if("04".equals(erpOptions) || "14".equals(erpOptions)){ // 휴가|조퇴 연차1, 반차0.5
						setErpParam(param, true, "pgYedate", "0.5"); // 0.5
					}else{
						setErpParam(param, true, "pgYedate", "1"); // 일수
					}
					
					// 사유
					setErpParamList(xmlElement, param, false, "body/leave.erpCont"); 
					setErpParam(param, true, "userEin", userEin); // 입력자 사번 
					setErpParam(param, true, "todate", date); // 입력날짜(yyyyMMdd)
					
					if(query!=null){
						try{
						query.executeUpdate("INSERT INTO H_PR_GUNDAY (PG_GUBUN, PG_SNO, PG_MONTH, PG_DAY, "+pgDateColNm+", PG_REMARK, PG_INUSER, PG_INDATE) VALUES(?,?,?,?,?,?,?,?)",
								param);
						}catch(Exception e){
							exception=e;
							isErpError=true;
						}
					}
					
					//printParams(param); // 로그 출력
					
				} else if("training".equals(typId)){ // 교육수강신청서
					List<XMLElement> users = (List<XMLElement>)xmlElement.getChildList("body/users");
					String pgDateColNm = getPgDateColNm("01"); // 교육
					if(pgDateColNm==null){
						throw new CmException("[ERROR] - 필수값이 없습니다.\tname : pgDateColNm");
					}
					for(XMLElement user : users){
						// 일정VO 세팅
						wcScdManagerSvc.addErpSchdlVoList(updateList, day, null, "01", compId, user.getAttr("erpUserUid"), user.getAttr("erpName"));
						
						if(isErpError) continue;
						param = new ArrayList<String>();
						setErpParamList(user, param, true, "erpGubun","erpEin"); // 회사구분, 신청자 사번
						setErpParam(param, true, "pgMonth", pgMonth); // 시작월
						setErpParam(param, true, "pgDay", pgDay); // 시작일
						// 교육일수 1 
						param.add("1");
						
						// 사유
						setErpParamList(xmlElement, param, false, "body/detail.erpPurpose"); 
						setErpParam(param, true, "userEin", userEin); // 입력자 사번 
						setErpParam(param, true, "todate", date); // 입력날짜(yyyyMMdd)
						if(query!=null){
							try{
							query.executeUpdate("INSERT INTO H_PR_GUNDAY (PG_GUBUN, PG_SNO, PG_MONTH, PG_DAY, "+pgDateColNm+", PG_REMARK, PG_INUSER, PG_INDATE) VALUES(?,?,?,?,?,?,?,?)",
									param);
							}catch(Exception e){
								exception=e;
								isErpError=true;
							}
						}
						
					}
				} else{
					return;
				}/*else if("trainingResults".equals(typId)){
					
				}*/
				
			}
			if(query==null) {
				System.out.println("[ERROR] - sendErpNotiFromAp ==> query is null!!");
			}
			
			// 일정 등록
			if(updateList.size()>0){
				wcScdManagerSvc.updateErpSchdlVoList(xmlElement, updateList, typId, compId, makrUid, erpStart, erpEnd);
			}
			
			// ERP 오류시 exception 발생
			if(isErpError && exception!=null){
				throw exception;
			}
			
			if(query!=null){
				query.commit();
				
			}
		} catch(Exception e){
			if(query!=null) query.rollback();
			e.printStackTrace();
			//throw new CmException(e.getMessage());
		}finally {
			if(query!=null) query.close();
		}
	}
	
	/** SQL 파라미터 목록 세팅*/
	public void setErpParamList(XMLElement element, List<String> param, boolean isValid, String... paramNames) throws CmException{
		for(String name : paramNames){
			setErpParam(param, isValid, name, element.getAttr(name));
		}
	}
	
	/** SQL 파라미터 세팅*/
	public void setErpParam(List<String> param, boolean isValid, String name, String value) throws CmException{
		if(isValid && value==null){
			throw new CmException("[ERROR] - 필수값이 없습니다.\tname : "+name);
		}
//		System.out.println(name+" : "+value);
		param.add(value==null ? "" : value);
	}
	
	/** ERP 연계 컬럼명 조회*/
	public String getPgDateColNm(String param){
		if("01".equals(param)) return "PG_EDDATE"; // 교육
		else if("02".equals(param)) return "PG_TODATE"; // 출장
		else if("03".equals(param)) return "PG_AB_ODATE"; // 결근
		else if("04".equals(param) || "05".equals(param) || "14".equals(param)) return "PG_YEDATE"; // 연차휴가(연차1, 반차0.5)
		else if("06".equals(param)) return "PG_TRDATE"; // 훈련
		else if("07".equals(param)) return "PG_SADATE"; // 생리휴가
		else if("08".equals(param)) return "PG_FUDATE"; // 경조휴가
		else if("09".equals(param)) return "PG_REPLACEMENT"; // 대체휴무
		return null;
	}
	
	/** ERP 연계 데이터(시작연월일 element명) 조회 */
	public String getStrtDtNm(String typId){
		if("bizTrip".equals(typId)){
			return "body/period";
		}else if("leave".equals(typId)){
			return "body/leave";
		}else if("training".equals(typId)){
			return "body/detail";
		}
		
		return null;
	}
	
	/** 파라미터 로그 - 테스트용 */
	public void printParams(List<String> params){
		int cnt=1;
		System.out.println("=================ERP Params Start======================");
		for(String param : params){
			System.out.println(cnt+".\t"+param);			
			cnt++;
		}
		System.out.println("=================ERP Params End======================");
		
	}
	
}
