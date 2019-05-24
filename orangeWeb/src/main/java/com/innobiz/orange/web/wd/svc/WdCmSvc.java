package com.innobiz.orange.web.wd.svc;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdCnclDVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wd.vo.WdAnbBVo;
import com.innobiz.orange.web.wd.vo.WdAnbModLVo;
import com.innobiz.orange.web.wd.vo.WdAnbUseLVo;

/** 연차 공통 서비스 */
@Service
public class WdCmSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WdCmSvc.class);

	public static final String WD_SYS_PLOC = "wd.sysPloc";
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 설정 조회 */
	public Map<String, String> getConfig(String compId) throws SQLException{
		Map<String, String> sysPlocMap = ptSysSvc.getSysSetupMap(WdCmSvc.WD_SYS_PLOC+compId, true);
		if(sysPlocMap == null){
			sysPlocMap = new HashMap<String, String>();
			sysPlocMap.put("logLangTypCd", "ko");
			sysPlocMap.put("newYearDay", "1");
			sysPlocMap.put("newYearMonth", "1");
			//sysPlocMap.put("yearBaseAnbMak", "Y");
			sysPlocMap.put("manlAnbMak", "Y");
		}
		return sysPlocMap;
	}
	
	/** 연차 사용 가능수 조회 
<pre>
anb : 연차남은 갯수
anbMinus : 연차 당겨쓰기 갯수
anbOngo : 연차 결재중 갯수
nanb : 개정연차남은 갯수
nanbMinus : 개정연차 당겨쓰기 갯수
nanbUse : 개정연차 사용 갯수
nanbOngo : 개정연차 결재중 갯수
repb : 대체휴무 갯수
repbOngo : 대체휴무 결재중 갯수
-- mixedUseAllow : 연차 혼합사용(Y/N)
message : 오류 메세지 - 입사일이 설정되지 않았습니다.

newYearMonth:년도 기준일 - 월
newYearDay  :년도 기준일 - 일
</pre>
	 * */
	public void getAnbAjx(String compId, String odurUid, ModelMap model, Locale locale) throws SQLException{
		
		Map<String, String> sysPlocMap = getConfig(compId);
		
		Calendar calendar = new GregorianCalendar();
		int year  = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		// 입사일 기준 연차 생성
		boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
		
		int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));//년도 기준일 - 월
		int newYearDay   = toInt(sysPlocMap.get("newYearDay"));//년도 기준일 - 일
		if(newYearMonth==0) newYearMonth = 1;
		if(newYearDay==0) newYearDay = 1;
		
		// 입사일 기준
		if(enterBaseAnbMak){
			// 입사일
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			String entraYmd = orOdurBVo==null ? null : orOdurBVo.getEntraYmd();
			
			if(entraYmd == null || entraYmd.isEmpty()){
				// wd.msg.noEntraYmd=입사일이 설정되지 않았습니다.
				String message = messageProperties.getMessage("wd.msg.noEntraYmd", locale);
				model.put("message", message);
				return;
			}
			
			int m = Integer.parseInt(entraYmd.substring(5, 7));
			newYearMonth = m==12 ? 1 : m+1;
			newYearDay   = 1;
			
			// 년도 기준일 세팅 - : 입사일 다음달 1일
			model.put("newYearMonth", Integer.toString(newYearMonth));
			model.put("newYearDay", Integer.toString(newYearDay));
			
			// 연도 보정
			if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
				year--;
			}
			
		// 회계일 기준, 수동 생성
		} else {
			
			// 년도 기준일 세팅
			model.put("newYearMonth", sysPlocMap.get("newYearMonth"));
			model.put("newYearDay", sysPlocMap.get("newYearDay"));
			
			// 연도 보정
			if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
				year--;
			}
		}
		
//		// 개정 연차 유지 년 수
//		int nanbUseYears = toInt(sysPlocMap.get("nanbUseYears"));
		// 연차 당겨쓰기 일 수
		int anbMinusAllow = toInt(sysPlocMap.get("anbMinusAllow"));
		// 개정 연차 당겨쓰기 일 수
		int nanbMinusAllow = toInt(sysPlocMap.get("nanbMinusAllow"));
		
		// 연차 혼합사용
		String mixedUseAllow = "Y".equals(sysPlocMap.get("mixedUseAllow")) ? "Y" : "N";
		model.put("mixedUseAllow", mixedUseAllow);
		
		
		float left = 0, ongo = 0;
		WdAnbBVo wdAnbBVo;
		for(String anbTypCd : new String[]{"anb","nanb","repb"}){
			
			left = 0;
			ongo = 0;
			
			if(anbTypCd.equals("anb") || anbTypCd.equals("repb")){
				
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(Integer.toString(year));
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOdurUid(odurUid);
				wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
				
				if(wdAnbBVo != null){
					left = toFloat(wdAnbBVo.getForwCnt()) + toFloat(wdAnbBVo.getForwModCnt())
							+ toFloat(wdAnbBVo.getCreCnt()) + toFloat(wdAnbBVo.getCreModCnt())
							- toFloat(wdAnbBVo.getUseCnt()) - toFloat(wdAnbBVo.getUseModCnt());
					
					ongo = toFloat(wdAnbBVo.getOngoCnt()) + toFloat(wdAnbBVo.getOngoModCnt());
				}
				
				if(anbTypCd.equals("anb")){
					model.put("anb", Float.toString(left));
					model.put("anbMinus", Float.toString(anbMinusAllow));
					model.put("anbOngo", Float.toString(ongo));
				} else if(anbTypCd.equals("repb")){
					model.put("repb", Float.toString(left));
					model.put("repbOngo", Float.toString(ongo));
				}
				
			} else if(anbTypCd.equals("nanb")){
				
				wdAnbBVo = new WdAnbBVo();
//				wdAnbBVo.setYear(Integer.toString(year));
				wdAnbBVo.setOdurUid(odurUid);
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOrderBy("YEAR DESC");
				@SuppressWarnings("unchecked")
				List<WdAnbBVo> wdAnbBVoList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
				
				// 사용수 계산
				float sumUse = 0;
				if(wdAnbBVoList != null && !wdAnbBVoList.isEmpty()){
					
					wdAnbBVo = wdAnbBVoList.get(0);
					if(year != Integer.parseInt(wdAnbBVo.getYear())) {
						continue;
					}
					
//					// 마지막 생성된 것으로 - "개정 연차 유지 년 수" 체크해서 사용 할 수 있는지 검사함
//					wdAnbBVo = wdAnbBVoList.get(0);
//					int yearDiff = year - Integer.parseInt(wdAnbBVo.getYear());
//					if(yearDiff != 0 && yearDiff > nanbUseYears){
//						continue;
//					}
					
					// 잔여
					left = toFloat(wdAnbBVo.getForwCnt()) + toFloat(wdAnbBVo.getForwModCnt())
							+ toFloat(wdAnbBVo.getCreCnt()) + toFloat(wdAnbBVo.getCreModCnt())
							- toFloat(wdAnbBVo.getUseCnt()) - toFloat(wdAnbBVo.getUseModCnt());
					// 결재수
					ongo = toFloat(wdAnbBVo.getOngoCnt()) + toFloat(wdAnbBVo.getOngoModCnt());
					
					// 사용수 계산 - 루프 돌면서 sum
					for(WdAnbBVo nanbWdAnbBVo : wdAnbBVoList){
						sumUse += toFloat(nanbWdAnbBVo.getUseCnt()) + toFloat(nanbWdAnbBVo.getUseModCnt());
					}
					
				} else {
					continue;
				}
				
				model.put("nanb", Float.toString(left));
				model.put("nanbUse", Float.toString(sumUse));
				model.put("nanbOngo", Float.toString(ongo));
				
				// 11개 다 생성된 경우 - 당겨쓰기 없게 하기 위해서 - 개정년차는 11개 까지만 쓸수 있음
				float realMinus = 11 - (left + sumUse + ongo);
				if(realMinus < nanbMinusAllow) {
					model.put("nanbMinus", Float.toString(realMinus));
				} else {
					model.put("nanbMinus", Float.toString(nanbMinusAllow));
				}
				
			}
		}
	}
	
	/** 연차 세팅
	 * xmlTypeId : "wdLeaveReq","wdLeaveCan","wdRepbReq","wdRepbCan"
	 *  */
	public void setWdData(
			String xmlTypeId, String anbTypCd, 
			String compId, String odurUid, 
			String useYmd, float amount, 
			String apvNo, String apvSubject, String docStat, String canApvNo,
			QueryQueue queryQueue,
			Map<Integer, Boolean> dupCheckMap, List<String> canCheckList, Map<String, Float> overCheckMap) throws SQLException, CmException{
		
		// 휴가 신청서
		if("wdLeaveReq".equals(xmlTypeId)){
			if(!"mak".equals(docStat) && !"apvd".equals(docStat) && !"rejt".equals(docStat) 
					&& !"reRevw".equals(docStat) && !"retrvMak".equals(docStat)){
				return;
			}
		// 휴가 취소서, 대근 신청서, 대근 취소서
		} else {
			if(!"apvd".equals(docStat)){
				return;
			}
		}
		
		
		Map<String, String> sysPlocMap = getConfig(compId);
		
		String logLangTypCd = sysPlocMap.get("logLangTypCd");
		Locale logLocale = SessionUtil.toLocale(logLangTypCd);
		
		boolean isNanb = "nanb".equals(anbTypCd);
		int year = getAnbYear(useYmd, odurUid, sysPlocMap, logLocale);
		
		String creYear = null;
		if(isNanb) {
			// 개정년차면 - 생성년 조회
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setOdurUid(odurUid);
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOrderBy("YEAR DESC");
			@SuppressWarnings("unchecked")
			List<WdAnbBVo> voList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
			if(voList!=null && !voList.isEmpty()){
				for(WdAnbBVo vo : voList) {
					if(toFloat(vo.getCreCnt())>0) {
						creYear = vo.getYear();
						break;
					}
				}
			}
		}
		
		WdAnbBVo wdAnbBVo;
		WdAnbModLVo wdAnbModLVo;
		WdAnbUseLVo wdAnbUseLVo;
		
		Integer hash = CRC32.hash((year+anbTypCd+odurUid).getBytes());
		boolean hasData = dupCheckMap.get(hash) != null;
		if(!hasData){
			wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(Integer.toString(year));
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOdurUid(odurUid);
			hasData = commonSvc.count(wdAnbBVo) > 0;
		}
		
		OrUserBVo orUserBVo;
		// 저장된 데이터가 없으면 기본 데이터 만듬
		if(!hasData){
			wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(Integer.toString(year));
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOdurUid(odurUid);
			// compId
			orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(odurUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			if(orUserBVo != null) {
				wdAnbBVo.setCompId(orUserBVo.getCompId());
			}
			
			wdAnbBVo.setForwCnt("0");
			wdAnbBVo.setForwModCnt("0");
			wdAnbBVo.setCreCnt("0");
			wdAnbBVo.setCreModCnt("0");
			wdAnbBVo.setUseCnt("0");
			wdAnbBVo.setUseModCnt("0");
			wdAnbBVo.setOngoCnt("0");
			wdAnbBVo.setOngoModCnt("0");
			if(isNanb) {
				wdAnbBVo.setCreYear(creYear);
			}
			queryQueue.insert(wdAnbBVo);
			
			dupCheckMap.put(hash, Boolean.TRUE);
		}
		
		
		// 휴가 신청서
		if("wdLeaveReq".equals(xmlTypeId)){
			
			// 기안
			if("mak".equals(docStat)){
				
				// 연차사용내역(WD_ANB_USE_L) - 에 데이터가 없을 경우만
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				
				if(commonSvc.count(wdAnbUseLVo) == 0){
					
					// 연차기본(WD_ANB_B) - 결재중 증가
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd(anbTypCd);
					wdAnbBVo.setOdurUid(odurUid);
					wdAnbBVo.setOngoCnt(Float.toString(amount));
					wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
					queryQueue.add(wdAnbBVo);
					
					// 연차사용내역(WD_ANB_USE_L) - 사용일 등록
					wdAnbUseLVo = new WdAnbUseLVo();
					wdAnbUseLVo.setYear(Integer.toString(year));
					wdAnbUseLVo.setAnbTypCd(anbTypCd);
					wdAnbUseLVo.setOdurUid(odurUid);
					wdAnbUseLVo.setUseYmd(useYmd);
					wdAnbUseLVo.setUseCnt(Float.toString(amount));
					wdAnbUseLVo.setCmplYn("N");// 완결여부 - 진행중인 건임
					wdAnbUseLVo.setRegDt("sysdate");
					wdAnbUseLVo.setApvNo(apvNo);
					wdAnbUseLVo.setRson(apvSubject);
					queryQueue.insert(wdAnbUseLVo);
					
					// 연차차감내역(WD_ANB_MOD_L) - 결재중 증가 로그
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd(anbTypCd);
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("ongo");//ongo:결재중
					wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.mak", logLocale)+" : "+apvSubject);
					wdAnbModLVo.setModCnt(Float.toString(amount));
					wdAnbModLVo.setUseYmd(useYmd);
					wdAnbModLVo.setApvNo(apvNo);
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
				} else {
					LOGGER.warn("WD_ANB_USE_L[1] has data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
				}
				
			// 기안 회수 || 반려
			} else if("retrvMak".equals(docStat) || "rejt".equals(docStat) || "reRevw".equals(docStat)){
				
				// 연차사용내역(WD_ANB_USE_L) - 에 데이터가 있을 경우만
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				
				if(commonSvc.count(wdAnbUseLVo) > 0){
					// 연차기본(WD_ANB_B) - 결재중 감소
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd(anbTypCd);
					wdAnbBVo.setOdurUid(odurUid);
					wdAnbBVo.setOngoCnt(Float.toString(-amount));
					wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
					queryQueue.add(wdAnbBVo);
					
					// 연차사용내역(WD_ANB_USE_L) - 사용일 삭제
					wdAnbUseLVo = new WdAnbUseLVo();
					wdAnbUseLVo.setYear(Integer.toString(year));
					wdAnbUseLVo.setAnbTypCd(anbTypCd);
					wdAnbUseLVo.setOdurUid(odurUid);
					wdAnbUseLVo.setUseYmd(useYmd);
					queryQueue.delete(wdAnbUseLVo);
					
					// 연차차감내역(WD_ANB_MOD_L) - 결재중 감소 로그
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd(anbTypCd);
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("ongo");//ongo:결재중
					wdAnbModLVo.setNote(messageProperties.getMessage("ap.term."+docStat, logLocale)+" : "+apvSubject);
					wdAnbModLVo.setModCnt(Float.toString(-amount));
					wdAnbModLVo.setUseYmd(useYmd);
					wdAnbModLVo.setApvNo(apvNo);
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
				} else {
					LOGGER.warn("WD_ANB_USE_L[2] dose not have data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
				}
				
			// 승인
			} else if("apvd".equals(docStat)){
				
				// 연차사용내역(WD_ANB_USE_L) - 조회
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				wdAnbUseLVo = (WdAnbUseLVo)commonSvc.queryVo(wdAnbUseLVo);
				
				// 1인결재 - 의 경우, 연차사용내역(WD_ANB_USE_L)에 데이터가 없는데 완결됨, 이때
				if(wdAnbUseLVo == null){
					
					// 연차기본(WD_ANB_B) - 사용 증가
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd(anbTypCd);
					wdAnbBVo.setOdurUid(odurUid);
					wdAnbBVo.setUseCnt(Float.toString(amount));
					wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
					queryQueue.add(wdAnbBVo);
					
					// 연차사용내역(WD_ANB_USE_L) - 사용일 등록
					wdAnbUseLVo = new WdAnbUseLVo();
					wdAnbUseLVo.setYear(Integer.toString(year));
					wdAnbUseLVo.setAnbTypCd(anbTypCd);
					wdAnbUseLVo.setOdurUid(odurUid);
					wdAnbUseLVo.setUseYmd(useYmd);
					wdAnbUseLVo.setUseCnt(Float.toString(amount));
					wdAnbUseLVo.setCmplYn("Y");// 완결여부
					wdAnbUseLVo.setRegDt("sysdate");
					wdAnbUseLVo.setApvNo(apvNo);
					wdAnbUseLVo.setRson(apvSubject);
					queryQueue.insert(wdAnbUseLVo);
					
					// 연차차감내역(WD_ANB_MOD_L) - 사용 증가 로그
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd(anbTypCd);
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("use");//use:사용
					wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
					wdAnbModLVo.setModCnt(Float.toString(amount));
					wdAnbModLVo.setUseYmd(useYmd);
					wdAnbModLVo.setApvNo(apvNo);
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
				// 완결되지 않은 경우만 - 완결된 데이터 있으면 아무것도 하지 않음
				} else if(!"Y".equals(wdAnbUseLVo.getCmplYn())) {
					
					// 연차기본(WD_ANB_B) - 결재중 감소 & 사용 증가
					wdAnbBVo = new WdAnbBVo();
					wdAnbBVo.setYear(Integer.toString(year));
					wdAnbBVo.setAnbTypCd(anbTypCd);
					wdAnbBVo.setOdurUid(odurUid);
					wdAnbBVo.setUseCnt(Float.toString(amount));
					wdAnbBVo.setOngoCnt(Float.toString(-amount));
					wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
					queryQueue.add(wdAnbBVo);
					
					// 연차차감내역(WD_ANB_MOD_L) - 결재중 감소 로그
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd(anbTypCd);
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("ongo");//ongo:결재중
					wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
					wdAnbModLVo.setModCnt(Float.toString(-amount));
					wdAnbModLVo.setUseYmd(useYmd);
					wdAnbModLVo.setApvNo(apvNo);
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
					// 연차사용내역(WD_ANB_USE_L) - 사용일 확정
					wdAnbUseLVo = new WdAnbUseLVo();
					wdAnbUseLVo.setYear(Integer.toString(year));
					wdAnbUseLVo.setAnbTypCd(anbTypCd);
					wdAnbUseLVo.setOdurUid(odurUid);
					wdAnbUseLVo.setUseYmd(useYmd);
					wdAnbUseLVo.setUseCnt(Float.toString(amount));
					wdAnbUseLVo.setCmplYn("Y");// 완결여부
					wdAnbUseLVo.setRegDt("sysdate");
					queryQueue.update(wdAnbUseLVo);
					
					// 연차차감내역(WD_ANB_MOD_L) - 사용 증가 로그
					wdAnbModLVo = new WdAnbModLVo();
					wdAnbModLVo.setYear(Integer.toString(year));
					wdAnbModLVo.setAnbTypCd(anbTypCd);
					wdAnbModLVo.setOdurUid(odurUid);
					wdAnbModLVo.setModTypCd("use");//ongo:결재중
					wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
					wdAnbModLVo.setModCnt(Float.toString(amount));
					wdAnbModLVo.setUseYmd(useYmd);
					wdAnbModLVo.setApvNo(apvNo);
					wdAnbModLVo.setModDt("sysdate");
					queryQueue.insert(wdAnbModLVo);
					
					// 연차의 경우 - anb:연차, nanb:개정연차, repb:대휴, offb:공가 
					if("anb".equals(anbTypCd) || "nanb".equals(anbTypCd)) {
						// 현재 기준 년도
						int curYear = getAnbYear(null, odurUid, sysPlocMap, logLocale);
						// "휴가일 년도"와, "현재 기준 년도"가 다를 경우 
						if(curYear == year+1) {
							
							Float left = overCheckMap.get(anbTypCd+year);
							if(left == null) {
								wdAnbBVo = new WdAnbBVo();
								wdAnbBVo.setYear(Integer.toString(year));
								wdAnbBVo.setAnbTypCd(anbTypCd);
								wdAnbBVo.setOdurUid(odurUid);
								wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
								if(wdAnbBVo != null) {
									left = toFloat(wdAnbBVo.getForwCnt()) + toFloat(wdAnbBVo.getForwModCnt())
										 + toFloat(wdAnbBVo.getCreCnt()) + toFloat(wdAnbBVo.getCreModCnt())
										 - toFloat(wdAnbBVo.getUseCnt()) + toFloat(wdAnbBVo.getUseModCnt());
								}
							}
							
							if(left != null) {
								
								float forwCnt = 0;
								if(left>=0 && left < amount) {
									forwCnt = amount - left;
								} else if(left<0) {
									forwCnt = amount;
								}
								
								if(forwCnt > 0) {
									
									wdAnbBVo = new WdAnbBVo();
									wdAnbBVo.setYear(Integer.toString(curYear));
									wdAnbBVo.setAnbTypCd(anbTypCd);
									wdAnbBVo.setOdurUid(odurUid);
									
									// 현재 기준 년도 - 데이터가 있을 경우에
									if(commonSvc.count(wdAnbBVo) > 0) {
										
										// 연차기본(WD_ANB_B) - 이월수 감소
										wdAnbBVo = new WdAnbBVo();
										wdAnbBVo.setYear(Integer.toString(curYear));
										wdAnbBVo.setAnbTypCd(anbTypCd);
										wdAnbBVo.setOdurUid(odurUid);
										wdAnbBVo.setForwCnt(Float.toString(-forwCnt));
										wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
										queryQueue.add(wdAnbBVo);
										
										// 연차차감내역(WD_ANB_MOD_L) - 이월수 감소 로그
										wdAnbModLVo = new WdAnbModLVo();
										wdAnbModLVo.setYear(Integer.toString(curYear));
										wdAnbModLVo.setAnbTypCd(anbTypCd);
										wdAnbModLVo.setOdurUid(odurUid);
										wdAnbModLVo.setModTypCd("forw");
										wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
										wdAnbModLVo.setModCnt(Float.toString(-forwCnt));
										wdAnbModLVo.setUseYmd(useYmd);
										wdAnbModLVo.setApvNo(apvNo);
										wdAnbModLVo.setModDt("sysdate");
										queryQueue.insert(wdAnbModLVo);
									}
								}
								
								overCheckMap.put(anbTypCd+year, left - amount);
							}
							
						}
					}
					
				} else {
					LOGGER.warn("WD_ANB_USE_L[3] has completed data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
				}
			}
			
		// 휴가 취소서
		} else if("wdLeaveCan".equals(xmlTypeId)){
			
			// 연차사용내역(WD_ANB_USE_L) - 에 데이터가 있을 경우만
			wdAnbUseLVo = new WdAnbUseLVo();
			wdAnbUseLVo.setYear(Integer.toString(year));
			wdAnbUseLVo.setAnbTypCd(anbTypCd);
			wdAnbUseLVo.setOdurUid(odurUid);
			wdAnbUseLVo.setUseYmd(useYmd);
			wdAnbUseLVo = (WdAnbUseLVo)commonSvc.queryVo(wdAnbUseLVo);
			
			if(wdAnbUseLVo != null){
				
				// 연차기본(WD_ANB_B) - 사용 감소
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(Integer.toString(year));
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOdurUid(odurUid);
				wdAnbBVo.setUseCnt(Float.toString(-amount));
				wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
				queryQueue.add(wdAnbBVo);
				
				// 연차사용내역(WD_ANB_USE_L) - 사용일 제거
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				queryQueue.delete(wdAnbUseLVo);
				
				// 사용 감소 로그
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd(anbTypCd);
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("use");//use:사용
				wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
				wdAnbModLVo.setModCnt(Float.toString(-amount));
				wdAnbModLVo.setUseYmd(useYmd);
				wdAnbModLVo.setApvNo(apvNo);
				wdAnbModLVo.setModDt("sysdate");
				queryQueue.insert(wdAnbModLVo);
				
				// 연차의 경우 - anb:연차, nanb:개정연차, repb:대휴, offb:공가 
				if("anb".equals(anbTypCd) || "nanb".equals(anbTypCd)) {
					// 현재 기준 년도
					int curYear = getAnbYear(null, odurUid, sysPlocMap, logLocale);
					// "휴가일 년도"와, "현재 기준 년도"가 다를 경우 
					if(curYear == year+1) {
						
						Float left = overCheckMap.get(anbTypCd+year);
						if(left == null) {
							wdAnbBVo = new WdAnbBVo();
							wdAnbBVo.setYear(Integer.toString(year));
							wdAnbBVo.setAnbTypCd(anbTypCd);
							wdAnbBVo.setOdurUid(odurUid);
							wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
							if(wdAnbBVo != null) {
								left = toFloat(wdAnbBVo.getForwCnt()) + toFloat(wdAnbBVo.getForwModCnt())
									 + toFloat(wdAnbBVo.getCreCnt()) + toFloat(wdAnbBVo.getCreModCnt())
									 - toFloat(wdAnbBVo.getUseCnt()) + toFloat(wdAnbBVo.getUseModCnt());
							}
						}
						
						float forwCnt = 0;
						if(left<0 && left+amount > 0) {
							forwCnt = amount + left;
						} else if(left<0) {
							forwCnt = amount;
						}
						
						if(forwCnt > 0) {
							
							wdAnbBVo = new WdAnbBVo();
							wdAnbBVo.setYear(Integer.toString(curYear));
							wdAnbBVo.setAnbTypCd(anbTypCd);
							wdAnbBVo.setOdurUid(odurUid);
							
							// 현재 기준 년도 - 데이터가 있을 경우에
							if(commonSvc.count(wdAnbBVo) > 0) {
								
								// 연차기본(WD_ANB_B) - 이월수 감소
								wdAnbBVo = new WdAnbBVo();
								wdAnbBVo.setYear(Integer.toString(curYear));
								wdAnbBVo.setAnbTypCd(anbTypCd);
								wdAnbBVo.setOdurUid(odurUid);
								wdAnbBVo.setForwCnt(Float.toString(forwCnt));
								wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
								queryQueue.add(wdAnbBVo);
								
								// 연차차감내역(WD_ANB_MOD_L) - 이월수 감소 로그
								wdAnbModLVo = new WdAnbModLVo();
								wdAnbModLVo.setYear(Integer.toString(curYear));
								wdAnbModLVo.setAnbTypCd(anbTypCd);
								wdAnbModLVo.setOdurUid(odurUid);
								wdAnbModLVo.setModTypCd("forw");
								wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
								wdAnbModLVo.setModCnt(Float.toString(forwCnt));
								wdAnbModLVo.setUseYmd(useYmd);
								wdAnbModLVo.setApvNo(apvNo);
								wdAnbModLVo.setModDt("sysdate");
								queryQueue.insert(wdAnbModLVo);
							}
						}
						
						overCheckMap.put(anbTypCd+year, left + amount);
					}
				}
				
			} else {
				LOGGER.warn("WD_ANB_USE_L[4] dose not have data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
			}
			
			// 취소된 결재번호 - 진행문서취소상세(AP_ONGD_CNCL_D) 에 저장
			if(canApvNo!=null && !canApvNo.isEmpty() && !canCheckList.contains(canApvNo)){
				
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(canApvNo);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				if(apOngdBVo != null){
					
					ApOngdCnclDVo apOngdCnclDVo = new ApOngdCnclDVo();
					apOngdCnclDVo.setMakrUid(apOngdBVo.getMakrUid());
					apOngdCnclDVo.setXmlTypId("wdLeaveReq");
					apOngdCnclDVo.setApvNo(canApvNo);
					
					if(commonSvc.count(apOngdCnclDVo) == 0){
						queryQueue.insert(apOngdCnclDVo);
					}
					
					canCheckList.add(canApvNo);
				}
			}
			
		// 대근 신청서
		} else if("wdRepbReq".equals(xmlTypeId)){
			
			// 연차사용내역(WD_ANB_USE_L) - 에 데이터가 없을 경우만
			wdAnbUseLVo = new WdAnbUseLVo();
			wdAnbUseLVo.setYear(Integer.toString(year));
			wdAnbUseLVo.setAnbTypCd(anbTypCd);
			wdAnbUseLVo.setOdurUid(odurUid);
			wdAnbUseLVo.setUseYmd(useYmd);
			
			if(commonSvc.count(wdAnbUseLVo) == 0){
				
				// 연차기본(WD_ANB_B) - 생성 증가(대근)
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(Integer.toString(year));
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOdurUid(odurUid);
				wdAnbBVo.setCreCnt(Float.toString(amount));
				wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
				queryQueue.add(wdAnbBVo);
				
				// 연차사용내역(WD_ANB_USE_L) - 사용일 등록(음수로 등록)
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				wdAnbUseLVo.setUseCnt(Float.toString(-amount));
				wdAnbUseLVo.setCmplYn("Y");
				wdAnbUseLVo.setRegDt("sysdate");
				wdAnbUseLVo.setApvNo(apvNo);
				wdAnbUseLVo.setRson(apvSubject);
				queryQueue.insert(wdAnbUseLVo);
				
				// 연차차감내역(WD_ANB_MOD_L) - 생성 증가(대근) 로그
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd(anbTypCd);
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("cre");//cre:발생
				wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
				wdAnbModLVo.setModCnt(Float.toString(amount));
				wdAnbModLVo.setUseYmd(useYmd);
				wdAnbModLVo.setApvNo(apvNo);
				wdAnbModLVo.setModDt("sysdate");
				queryQueue.insert(wdAnbModLVo);
				
			} else {
				LOGGER.warn("WD_ANB_USE_L[5] has data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
			}
			
		// 대근 취소서
		} else if("wdRepbCan".equals(xmlTypeId)){
			
			// 연차사용내역(WD_ANB_USE_L) - 에 데이터가 있을 경우만
			wdAnbUseLVo = new WdAnbUseLVo();
			wdAnbUseLVo.setYear(Integer.toString(year));
			wdAnbUseLVo.setAnbTypCd(anbTypCd);
			wdAnbUseLVo.setOdurUid(odurUid);
			wdAnbUseLVo.setUseYmd(useYmd);
			wdAnbUseLVo = (WdAnbUseLVo)commonSvc.queryVo(wdAnbUseLVo);
			
			if(wdAnbUseLVo != null){
				
				// 연차기본(WD_ANB_B) - 생성 감소(대근)
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(Integer.toString(year));
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOdurUid(odurUid);
				wdAnbBVo.setCreCnt(Float.toString(-amount));
				wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
				queryQueue.add(wdAnbBVo);
				
				// 연차사용내역(WD_ANB_USE_L) - 삭제
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(Integer.toString(year));
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				queryQueue.delete(wdAnbUseLVo);
				
				// 연차차감내역(WD_ANB_MOD_L) - 생성 감소(대근) 로그
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(Integer.toString(year));
				wdAnbModLVo.setAnbTypCd(anbTypCd);
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("cre");//cre:발생
				wdAnbModLVo.setNote(messageProperties.getMessage("ap.term.apvd", logLocale)+" : "+apvSubject);
				wdAnbModLVo.setModCnt(Float.toString(-amount));
				wdAnbModLVo.setUseYmd(useYmd);
				wdAnbModLVo.setApvNo(apvNo);
				wdAnbModLVo.setModDt("sysdate");
				queryQueue.insert(wdAnbModLVo);
				
			} else {
				LOGGER.warn("WD_ANB_USE_L[6] dose not have data - typeId:"+xmlTypeId+" docStat:"+docStat+" year:"+year+" anbTypCd:"+anbTypCd+" odurUid:"+odurUid+" useYmd:"+useYmd);
			}
			
			// 취소된 결재번호 - 진행문서취소상세(AP_ONGD_CNCL_D) 에 저장
			if(canApvNo!=null && !canApvNo.isEmpty() && !canCheckList.contains(canApvNo)){
				
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(canApvNo);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				if(apOngdBVo != null){
					
					ApOngdCnclDVo apOngdCnclDVo = new ApOngdCnclDVo();
					apOngdCnclDVo.setMakrUid(apOngdBVo.getMakrUid());
					apOngdCnclDVo.setXmlTypId("wdRepbReq");
					apOngdCnclDVo.setApvNo(canApvNo);
					
					if(commonSvc.count(apOngdCnclDVo) == 0){
						queryQueue.insert(apOngdCnclDVo);
					}
					
					canCheckList.add(canApvNo);
				}
			}
		}
		
	}
	
	// 연차 년도 리턴
	public int getAnbYear(String useYmd, String odurUid, Map<String, String> sysPlocMap, Locale locale) throws SQLException, CmException {
		
		int year, month, day;
		if(useYmd == null) {
			Calendar calendar = new GregorianCalendar();
			year  = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			day = calendar.get(Calendar.DAY_OF_MONTH);
		} else {
			year = Integer.parseInt(useYmd.substring(0, 4));
			month = Integer.parseInt(useYmd.substring(5, 7));
			day = Integer.parseInt(useYmd.substring(8, 10));
		}
		
		boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
		int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));//년도 기준일 - 월
		int newYearDay   = toInt(sysPlocMap.get("newYearDay"));//년도 기준일 - 일
		
		// 입사일 기준 생성 일 경우
		if(enterBaseAnbMak){
			
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			if(orOdurBVo!=null && orOdurBVo.getEntraYmd()!=null && !orOdurBVo.getEntraYmd().isEmpty()) {
				
				newYearMonth = Integer.parseInt(orOdurBVo.getEntraYmd().substring(5, 7));
				if(month <= newYearMonth){
					year--;
				}
				return year;
			} else {
				// wd.msg.noEntraYmd=입사일이 설정되지 않았습니다.
				throw new CmException(messageProperties.getMessage("wd.msg.noEntraYmd", locale));
			}
		} else {
			if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
				year--;
			}
		}
		return year;
	}
	
	
	private float toFloat(String no){
		if(no==null || no.isEmpty()) return 0;
		return Float.parseFloat(no);
	}
	private int toInt(String no){
		if(no==null || no.isEmpty()) return 0;
		return Integer.parseInt(no);
	}
	
}
