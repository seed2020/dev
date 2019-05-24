package com.innobiz.orange.web.wr.svc;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wr.vo.WrRescBVo;
import com.innobiz.orange.web.wr.vo.WrRescKndBVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;
import com.innobiz.orange.web.wr.vo.WrRezvBVo;

@Service
public class WrRescSvc {
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 자원관리 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 파일 서비스 */
	@Autowired
	private WrRescFileSvc wrRescFileSvc;
	
	/** 자원종류 및 자원목록 세팅 */
	public void setRescInfo(HttpServletRequest request, ModelMap model , String rescKndCd , String compId , boolean initStatus , WrRezvBVo searchVO , String useYn) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		/** 종류코드 조회 */
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		wrRescKndBVo.setQueryLang(langTypCd);
		if(compId != null)  wrRescKndBVo.setCompId(compId);
		wrRescKndBVo.setUseYn(useYn);//사용여부
		@SuppressWarnings("unchecked")
		List<WrRescKndBVo> wrRescKndBVoList = (List<WrRescKndBVo>)commonDao.queryList(wrRescKndBVo);
		model.put("wrRescKndBVoList", wrRescKndBVoList);
		// 자원종류가 있으면 해당 코드로 자원정보를 조회한다.
		if(wrRescKndBVoList.size() > 0 ){
			/** 자원코드 조회 */
			WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
			wrRescMngBVo.setQueryLang(langTypCd);
			if(compId != null) wrRescMngBVo.setCompId(compId);
			if(rescKndCd != null && !rescKndCd.isEmpty())	wrRescMngBVo.setRescKndId(rescKndCd);
			if(initStatus && (rescKndCd == null || rescKndCd.isEmpty()) ) wrRescMngBVo.setRescKndId(wrRescKndBVoList.get(0).getRescKndId());
			if(wrRescMngBVo.getRescKndId() != null && !wrRescMngBVo.getRescKndId().isEmpty()){
				if(initStatus && searchVO != null && ( searchVO.getRescKndId() == null || searchVO.getRescKndId().isEmpty()) ) searchVO.setRescKndId(wrRescMngBVo.getRescKndId());//조회조건 초기 세팅
				//목록 조회
				wrRescMngBVo.setUseYn(useYn);//사용여부
				@SuppressWarnings("unchecked")
				List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>)commonDao.queryList(wrRescMngBVo);
				model.put("wrRescMngBVoList", wrRescMngBVoList);
				
				if(wrRescMngBVoList.size() > 0 && initStatus && searchVO != null && ( searchVO.getRescMngId() == null || searchVO.getRescMngId().isEmpty())) searchVO.setRescMngId(wrRescMngBVoList.get(0).getRescMngId());//조회조건 초기 세팅
				
			}
		}
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("wr") * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 일시 초기화 */
	public String initBullRezvDt(String d) {
		Calendar cal = Calendar.getInstance();
		if (d == null) {
			//cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}else{
			d = d.replaceAll("[-: ]", "");//-:공백 제거
        	int year = Integer.parseInt(d.substring(0, 4)); 
            int month = Integer.parseInt(d.substring(4, 6)); 
            int day = Integer.parseInt(d.substring(6,8));
            int hour = Integer.parseInt(d.substring(8,10));
            int min = Integer.parseInt(d.substring(10,12));
            hour = min >= 30 ? hour+1 : hour;
            min = min >= 30 ? 0 : 30;
            if(hour == 24 && min == 0) day-=1;
            cal.set(Calendar.YEAR, year); 
            cal.set(Calendar.MONTH, month-1); // 0 이 1월, 1 은 2월, .... 
            cal.set(Calendar.DAY_OF_MONTH, day); 
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
		}
		return new Timestamp(cal.getTimeInMillis()).toString();
	}
	
	
	/** 리소스기본(WR_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WrRescBVo collectBaRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// 리소스 prefix 설정
		String rescPrefix = prefix == null || prefix.isEmpty() ? "resc" : prefix + "Resc";
		// rescId 받음 : 없으면 생성
		String rescId = request.getParameter(rescPrefix + "Id"), rescVa;
		if (rescId == null || rescId.isEmpty()) {
			rescId = wrCmSvc.createId("WR_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WrRescBVo wrRescBVo, firstBaRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				wrRescBVo = new WrRescBVo();
				wrRescBVo.setRescId(rescId);
				wrRescBVo.setLangTypCd(ptCdBVo.getCd());
				wrRescBVo.setRescVa(rescVa);

				if (firstBaRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstBaRescBVo = wrRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(wrRescBVo);
				} else {
					queryQueue.store(wrRescBVo);
				}
			}
		}
		
		return firstBaRescBVo;
	}
	
	/** 리소스기본(WM_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws Exception {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WrRescBVo wrRescBVo = new WrRescBVo();
			wrRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<WrRescBVo> wrRescBVoList = (List<WrRescBVo>) commonDao.queryList(wrRescBVo);

			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = wrRescBVoList == null ? 0 : wrRescBVoList.size();
			for (i = 0; i < size; i++) {
				wrRescBVo = wrRescBVoList.get(i);
				model.put(wrRescBVo.getRescId() + "_" + wrRescBVo.getLangTypCd(), wrRescBVo.getRescVa());
			}
		}
	}
	
	/** 자원종류 삭제 */
	public void deleteRescKnd(QueryQueue queryQueue , String rescKndId) throws SQLException {
		//자원관리 조회
		WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
		wrRescMngBVo.setRescKndId(rescKndId);
		@SuppressWarnings("unchecked")
		List<WrRescMngBVo> wrRescMngBVoList = (List<WrRescMngBVo>) commonDao.queryList(wrRescMngBVo);
		//자원관리 삭제
		for(WrRescMngBVo storedWrRescMngBVo : wrRescMngBVoList){
			deleteRescMng(queryQueue, storedWrRescMngBVo.getRescMngId());
		}
		
		//자원종류 삭제
		WrRescKndBVo wrRescKndBVo = new WrRescKndBVo();
		wrRescKndBVo.setRescKndId(rescKndId);
		queryQueue.delete(wrRescKndBVo);
	}
	
	/** 자원관리 삭제 */
	public void deleteRescMng(QueryQueue queryQueue , String rescMngId) throws SQLException {
		//자원예약 조회
		WrRezvBVo wrRezvBVo = new WrRezvBVo();
		wrRezvBVo.setRescMngId(rescMngId);
		@SuppressWarnings("unchecked")
		List<WrRezvBVo> wrRezvBVoList = (List<WrRezvBVo>) commonDao.queryList(wrRezvBVo);
		//자원예약 삭제
		for(WrRezvBVo storedWrRezvBVo : wrRezvBVoList){
			deleteRezv(queryQueue, storedWrRezvBVo.getRezvId());
		}
		
		//자원관리 삭제
		WrRescMngBVo wrRescMngBVo = new WrRescMngBVo();
		wrRescMngBVo.setRescMngId(rescMngId);
		queryQueue.delete(wrRescMngBVo);
		
		//사진 삭제
		wrRescFileSvc.delPhoto(queryQueue, rescMngId);
	}
	
	/** 자원예약 삭제 */
	public void deleteRezv(QueryQueue queryQueue , String rezvId) throws SQLException {
		//일정 삭제
		WrRezvBVo storedWrRezvBVo = new WrRezvBVo();
		storedWrRezvBVo.setRezvId(rezvId);
		storedWrRezvBVo = (WrRezvBVo)commonDao.queryVo(storedWrRezvBVo);
		if(storedWrRezvBVo.getSchdlId() != null && !"".equals(storedWrRezvBVo.getSchdlId())){
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setSchdlId(storedWrRezvBVo.getSchdlId());
			queryQueue.delete(wcSchdlBVo);
		}
		
		//자원예약 삭제
		WrRezvBVo wrRezvBVo = new WrRezvBVo();
		wrRezvBVo.setRezvId(rezvId);
		queryQueue.delete(wrRezvBVo);
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("resc"))
			return "/wr/listResc.do";
		else if(bxId.equals("my"))
			return "/wr/listRezv.do";
		else if(bxId.equals("all"))
			return "/wr/listRezv.do";
		else if(bxId.equals("disc"))
			return "/wr/listRezvDisc.do";
		else
			return "";
	}
}
