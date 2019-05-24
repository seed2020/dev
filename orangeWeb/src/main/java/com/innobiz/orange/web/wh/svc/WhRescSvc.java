package com.innobiz.orange.web.wh.svc;

import java.sql.SQLException;
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
import com.innobiz.orange.web.wh.vo.WhRescBVo;

/**
 * 리소스 조회/저장용 서비스
 * <br/>
 * 관리자용 화면에서 어권별 리소스를 조회/저장 할 때 사용
 */
@Service
public class WhRescSvc {

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
	private WhCmSvc whCmSvc;

	/** 리소스기본(WH_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WhRescBVo setWhRescBVo(HttpServletRequest request, String titleId, QueryQueue queryQueue) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		String rescId = whCmSvc.createId("WH_RESC_B");
		
		// 첫번째 리소스 값
		WhRescBVo whRescBVo, firstWhRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			if (titleId != null && !titleId.isEmpty()) {

				whRescBVo = new WhRescBVo();
				whRescBVo.setRescId(rescId);
				whRescBVo.setLangTypCd(ptCdBVo.getCd());
				String rescVa = messageProperties.getMessage(titleId, SessionUtil.toLocale(ptCdBVo.getCd()));
				whRescBVo.setRescVa(rescVa);

				if (firstWhRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWhRescBVo = whRescBVo;
				}

				queryQueue.insert(whRescBVo);
			}
		}
		
		return firstWhRescBVo;
	}

	/** 리소스기본(WH_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WhRescBVo collectWhRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException {

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
			rescId = whCmSvc.createId("WH_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WhRescBVo whRescBVo, firstWhRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				whRescBVo = new WhRescBVo();
				whRescBVo.setRescId(rescId);
				whRescBVo.setLangTypCd(ptCdBVo.getCd());
				whRescBVo.setRescVa(rescVa);

				if (firstWhRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWhRescBVo = whRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(whRescBVo);
				} else {
					queryQueue.store(whRescBVo);
				}
			}
		}
		
		return firstWhRescBVo;
	}
	
	/** [Parameter Map]리소스기본(DM_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WhRescBVo collectWhRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue, Map<String,Object> paramMap) throws SQLException {

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
			rescId = whCmSvc.createId("WH_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WhRescBVo whRescBVo, firstWhRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = (String)paramMap.get(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				whRescBVo = new WhRescBVo();
				whRescBVo.setRescId(rescId);
				whRescBVo.setLangTypCd(ptCdBVo.getCd());
				whRescBVo.setRescVa(rescVa);

				if (firstWhRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWhRescBVo = whRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(whRescBVo);
				} else {
					queryQueue.store(whRescBVo);
				}
			}
		}
		
		return firstWhRescBVo;
	}
	
	/** 리소스기본(WH_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectWhRescBVoList(HttpServletRequest request, QueryQueue queryQueue,
			List<? extends CommonVo> commonVoList, String validCol, String rescIdCol, String rescVaCol)
			throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		WhRescBVo whRescBVo, firstWhRescBVo;
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
				rescId = whCmSvc.createId("WH_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}

			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstWhRescBVo = null;

			// 어권 만큼 돌면서
			for (j = 0; j < size; j++) {

				rescVas = j < rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas != null && i < rescVas.length) ? rescVas[i] : null;
				if (rescVa == null)
					continue;

				whRescBVo = new WhRescBVo();
				// whRescBVo.setCompId(compId);
				whRescBVo.setRescId(rescId);
				whRescBVo.setLangTypCd(langs[j]);
				whRescBVo.setRescVa(rescVa);

				if (isNewResc) {
					queryQueue.insert(whRescBVo);
				} else {
					queryQueue.store(whRescBVo);
				}

				if (firstWhRescBVo == null || "ko".equals(langs[j])) {
					firstWhRescBVo = whRescBVo;
				}
			}

			if (commonVoList.size() > commonIndex) {
				commonVo = commonVoList.get(commonIndex++);

				// 리소스ID, 리소스값을
				if (firstWhRescBVo != null) {
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstWhRescBVo.getRescVa());
				}
			}
		}

	}

	/** 리소스기본(WH_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, Map<String,String> paramMap, String langTypCd) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WhRescBVo whRescBVo = new WhRescBVo();
			whRescBVo.setRescId(rescId);
			if(langTypCd!=null){
				whRescBVo.setLangTypCd(langTypCd);
				whRescBVo = (WhRescBVo)commonSvc.queryVo(whRescBVo);
				if(whRescBVo!=null){
					paramMap.put(whRescBVo.getRescId() + "_" + whRescBVo.getLangTypCd(), whRescBVo.getRescVa());
				}
			}else{
				@SuppressWarnings("unchecked")
				List<WhRescBVo> WhRescBVoList = (List<WhRescBVo>) commonSvc.queryList(whRescBVo);

				// JSP 출력을 위해 출력용 파라미터로 넘김
				int i, size = WhRescBVoList == null ? 0 : WhRescBVoList.size();
				for (i = 0; i < size; i++) {
					whRescBVo = WhRescBVoList.get(i);
					paramMap.put(whRescBVo.getRescId() + "_" + whRescBVo.getLangTypCd(), whRescBVo.getRescVa());
				}
			}
		}
	}
	
	/** 리소스기본(WH_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WhRescBVo whRescBVo = new WhRescBVo();
			whRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<WhRescBVo> WhRescBVoList = (List<WhRescBVo>) commonSvc.queryList(whRescBVo);

			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = WhRescBVoList == null ? 0 : WhRescBVoList.size();
			for (i = 0; i < size; i++) {
				whRescBVo = WhRescBVoList.get(i);
				model.put(whRescBVo.getRescId() + "_" + whRescBVo.getLangTypCd(), whRescBVo.getRescVa());
			}
		}
	}
}
