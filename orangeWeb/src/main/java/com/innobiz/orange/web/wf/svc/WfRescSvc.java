package com.innobiz.orange.web.wf.svc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

/**
 * 리소스 조회/저장용 서비스
 * <br/>
 * 관리자용 화면에서 어권별 리소스를 조회/저장 할 때 사용
 */
@Service
public class WfRescSvc {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;

	/** 리소스기본(WF_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WfRescBVo setWfRescBVo(HttpServletRequest request, String titleId, QueryQueue queryQueue) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		String rescId = wfCmSvc.createId("WF_RESC_B");
		
		// 첫번째 리소스 값
		WfRescBVo wfRescBVo, firstWfRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			if (titleId != null && !titleId.isEmpty()) {

				wfRescBVo = new WfRescBVo();
				wfRescBVo.setRescId(rescId);
				wfRescBVo.setLangTypCd(ptCdBVo.getCd());
				String rescVa = messageProperties.getMessage(titleId, SessionUtil.toLocale(ptCdBVo.getCd()));
				wfRescBVo.setRescVa(rescVa);

				if (firstWfRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWfRescBVo = wfRescBVo;
				}

				queryQueue.insert(wfRescBVo);
			}
		}
		
		return firstWfRescBVo;
	}

	/** 리소스기본(WF_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WfRescBVo collectWfRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException {

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
			rescId = wfCmSvc.createId("WF_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WfRescBVo wfRescBVo, firstWfRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				wfRescBVo = new WfRescBVo();
				wfRescBVo.setRescId(rescId);
				wfRescBVo.setLangTypCd(ptCdBVo.getCd());
				wfRescBVo.setRescVa(rescVa);

				if (firstWfRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWfRescBVo = wfRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(wfRescBVo);
				} else {
					queryQueue.store(wfRescBVo);
				}
			}
		}
		
		return firstWfRescBVo;
	}
	
	/** [Parameter Map]리소스기본(.._RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WfRescBVo collectWfRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue, Map<String,Object> paramMap) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// 리소스 prefix 설정
		String rescPrefix = prefix == null || prefix.isEmpty() ? "resc" : prefix + "Resc";
		// rescId 받음 : 없으면 생성
		String rescId = (String)paramMap.get(rescPrefix + "Id"), rescVa;
		if (rescId == null || rescId.isEmpty()) {
			rescId = wfCmSvc.createId("WF_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WfRescBVo wfRescBVo, firstWfRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = (String)paramMap.get(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				wfRescBVo = new WfRescBVo();
				wfRescBVo.setRescId(rescId);
				wfRescBVo.setLangTypCd(ptCdBVo.getCd());
				wfRescBVo.setRescVa(rescVa);

				if (firstWfRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWfRescBVo = wfRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(wfRescBVo);
				} else {
					queryQueue.store(wfRescBVo);
				}
			}
		}
		
		return firstWfRescBVo;
	}
	
	/** 리소스기본(WF_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectWfRescBVoList(HttpServletRequest request, QueryQueue queryQueue,
			List<? extends CommonVo> commonVoList, String validCol, String rescIdCol, String rescVaCol)
			throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		WfRescBVo wfRescBVo, firstWfRescBVo;
		String rescId, rescVa;

		// 값이 유효한지 체크할 컬럼
		String[] validColVas = request.getParameterValues(validCol);

		// 리소스ID, 없으면 생성함
		String[] rescIds = request.getParameterValues("rescId");
		boolean isNewResc = false;

		CommonVo commonVo;

		// 회사별 설정된 리소스의 어권 정보
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);

		int i, j, size = langTypCdList == null ? 0 : langTypCdList.size();
		String[] langs = new String[size];
		String[][] rescVa2s = new String[size][];
		String[] rescVas;
		for (j = 0; j < size; j++) {
			langs[j] = langTypCdList.get(j).getCd();
			rescVa2s[j] = request.getParameterValues("rescVa_" + langs[j]);
		}

		int commonIndex = 0;
		for (i = 0; i < validColVas.length; i++) {

			// 유효체크 파라미터가 없으면 - 무시
			if (validColVas[i] == null || validColVas[i].isEmpty())
				continue;

			// 리소스ID 없으면 생성
			rescId = i < rescIds.length ? rescIds[i] : null;
			if (rescId == null || rescId.isEmpty()) {
				rescId = wfCmSvc.createId("WF_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}

			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstWfRescBVo = null;

			// 어권 만큼 돌면서
			for (j = 0; j < size; j++) {

				rescVas = j < rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas != null && i < rescVas.length) ? rescVas[i] : null;
				if (rescVa == null)
					continue;

				wfRescBVo = new WfRescBVo();
				// wfRescBVo.setCompId(compId);
				wfRescBVo.setRescId(rescId);
				wfRescBVo.setLangTypCd(langs[j]);
				wfRescBVo.setRescVa(rescVa);

				if (isNewResc) {
					queryQueue.insert(wfRescBVo);
				} else {
					queryQueue.store(wfRescBVo);
				}

				if (firstWfRescBVo == null || "ko".equals(langs[j])) {
					firstWfRescBVo = wfRescBVo;
				}
			}

			if (commonVoList.size() > commonIndex) {
				commonVo = commonVoList.get(commonIndex++);

				// 리소스ID, 리소스값을
				if (firstWfRescBVo != null) {
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstWfRescBVo.getRescVa());
				}
			}
		}

	}

	/** 리소스기본(WF_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, Map<String,String> paramMap, String langTypCd) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WfRescBVo wfRescBVo = new WfRescBVo();
			wfRescBVo.setRescId(rescId);
			if(langTypCd!=null){
				wfRescBVo.setLangTypCd(langTypCd);
				wfRescBVo = (WfRescBVo)commonSvc.queryVo(wfRescBVo);
				if(wfRescBVo!=null){
					paramMap.put(wfRescBVo.getRescId() + "_" + wfRescBVo.getLangTypCd(), wfRescBVo.getRescVa());
				}
			}else{
				@SuppressWarnings("unchecked")
				List<WfRescBVo> WfRescBVoList = (List<WfRescBVo>) commonSvc.queryList(wfRescBVo);

				// JSP 출력을 위해 출력용 파라미터로 넘김
				int i, size = WfRescBVoList == null ? 0 : WfRescBVoList.size();
				for (i = 0; i < size; i++) {
					wfRescBVo = WfRescBVoList.get(i);
					paramMap.put(wfRescBVo.getRescId() + "_" + wfRescBVo.getLangTypCd(), wfRescBVo.getRescVa());
				}
			}
		}
	}
	
	/** 리소스기본(WF_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WfRescBVo wfRescBVo = new WfRescBVo();
			wfRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<WfRescBVo> WfRescBVoList = (List<WfRescBVo>) commonSvc.queryList(wfRescBVo);

			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = WfRescBVoList == null ? 0 : WfRescBVoList.size();
			for (i = 0; i < size; i++) {
				wfRescBVo = WfRescBVoList.get(i);
				model.put(wfRescBVo.getRescId() + "_" + wfRescBVo.getLangTypCd(), wfRescBVo.getRescVa());
			}
		}
	}
	
	/** 리소스기본(WF_RESC_B) 테이블 조회 후 맵에 세팅 */
	public Map<String, String> getWfRescBVoToMap(String rescId) throws SQLException {
		if(rescId==null || rescId.isEmpty()) return null;
		WfRescBVo wfRescBVo = new WfRescBVo();
		wfRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<WfRescBVo> wfRescBVoList = (List<WfRescBVo>) commonSvc.queryList(wfRescBVo);
		if(wfRescBVoList==null || wfRescBVoList.size()==0){
			return null;
		}
		Map<String, String> returnMap=new HashMap<String, String>();
		for(WfRescBVo storedWfRescBVo : wfRescBVoList){
			returnMap.put(storedWfRescBVo.getLangTypCd(), storedWfRescBVo.getRescVa());
		}
		return returnMap;
		
	}
	
	/** 리소스기본(WF_RESC_B) 테이블 조회  */
	@SuppressWarnings("unchecked")
	public List<WfRescBVo> getWfRescBVoList(String rescId) throws SQLException {
		if(rescId==null || rescId.isEmpty()) return null;
		WfRescBVo wfRescBVo = new WfRescBVo();
		wfRescBVo.setRescId(rescId);
		
		return (List<WfRescBVo>) commonSvc.queryList(wfRescBVo);
	}
}
