package com.innobiz.orange.web.wc.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcRescBVo;

@Service
public class WcRescSvc {
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("WC_RESC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'R', 8);
		} 
		return null;
	}
	
	/** 리소스기본(OR_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectWcRescBVoList(HttpServletRequest request, QueryQueue queryQueue, List<? extends CommonVo> commonVoList
			, String validCol, String rescIdCol, String rescVaCol) throws SQLException{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		WcRescBVo wcRescBVo, firstWcRescBVo;
		String rescId, rescVa;
		
		// 값이 유효한지 체크할 컬럼
		String[] validColVas = request.getParameterValues(validCol);
		
		// 리소스ID, 없으면 생성함
		String[] rescIds = request.getParameterValues("rescId");
		boolean isNewResc = false;
		
		CommonVo commonVo;
		
		// 회사별 설정된 리소스의 어권 정보
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		
		int i, j, size = langTypCdList==null ? 0 : langTypCdList.size();
		String[] langs = new String[size];
		String[][] rescVa2s = new String[size][];
		String[] rescVas;
		for(j=0;j<size;j++){
			langs[j] = langTypCdList.get(j).getCd();
			rescVa2s[j] = request.getParameterValues("rescVa_"+langs[j]);
		}
		
		int commonIndex = 0;
		for(i=0;i<validColVas.length;i++){
			
			// 유효체크 파라미터가 없으면 - 무시
			if(validColVas[i]==null || validColVas[i].isEmpty()) continue;
			
			// 리소스ID 없으면 생성
			rescId = i<rescIds.length ? rescIds[i] : null;
			if(rescId==null || rescId.isEmpty()){
				rescId = createId("WC_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}
			
			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstWcRescBVo = null;
			
			// 어권 만큼 돌면서
			for(j=0;j<size;j++){
				
				rescVas = j<rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas!=null && i<rescVas.length) ? rescVas[i] : null;
				if(rescVa==null) continue;
				
				wcRescBVo = new WcRescBVo();
				wcRescBVo.setRescId(rescId);
				wcRescBVo.setLangTypCd(langs[j]);
				wcRescBVo.setRescVa(rescVa);
				
				if(isNewResc){
					wcRescBVo.setCompId(userVo.getCompId());
					queryQueue.insert(wcRescBVo);
				} else {
					queryQueue.store(wcRescBVo);
				}
				
				if(firstWcRescBVo==null || "ko".equals(langs[j])){
					firstWcRescBVo = wcRescBVo;
				}
			}
			
			
			if(commonVoList.size()>commonIndex){
				commonVo = commonVoList.get(commonIndex++);
				
				// 리소스ID, 리소스값을 
				if(firstWcRescBVo!=null){
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstWcRescBVo.getRescVa());
				}
				
				/*if(isNewResc){
					VoUtil.setValue(commonVo, "regDt", "sysdate");
					VoUtil.setValue(commonVo, "regrUid", userVo.getUserUid());
					queryQueue.insert(commonVo);
				} else {
					queryQueue.store(commonVo);
				}*/
				
			}
		}
	}
	
	/** 카테고리 삭제 */
	public void deleteCatCls(QueryQueue queryQueue, WcCatClsBVo delWcCatClsBVo) throws SQLException{
		WcCatClsBVo wcCatClsBVo = (WcCatClsBVo)commonDao.queryVo(delWcCatClsBVo);
		if(wcCatClsBVo != null ){
			WcRescBVo wcRescBVo = new WcRescBVo();
			wcRescBVo.setCompId(wcCatClsBVo.getCompId());
			wcRescBVo.setRescId(wcCatClsBVo.getRescId());
			//리소스 삭제
			queryQueue.delete(wcRescBVo);
			
			//카테고리 삭제
			queryQueue.delete(delWcCatClsBVo);
		}
	}
	
	/** 일정대상 목록 */
	public String[][] getSchdlTypList(){
		String[][] list = {{"psn","개인일정"},{"grp","그룹일정"},{"dept","부서일정"},{"comp","회사일정"}};
		return list;
	}
	
	/** 리소스기본(BA_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WcRescBVo collectBaRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException {

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
			rescId = createId("WC_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WcRescBVo wcRescBVo, firstBaRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				wcRescBVo = new WcRescBVo();
				wcRescBVo.setCompId(userVo.getCompId());
				wcRescBVo.setRescId(rescId);
				wcRescBVo.setLangTypCd(ptCdBVo.getCd());
				wcRescBVo.setRescVa(rescVa);

				if (firstBaRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstBaRescBVo = wcRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(wcRescBVo);
				} else {
					queryQueue.store(wcRescBVo);
				}
			}
		}
		
		return firstBaRescBVo;
	}
	
	/** 리소스기본(WC_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WcRescBVo wcRescBVo = new WcRescBVo();
			wcRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<WcRescBVo> wcRescBVoList = (List<WcRescBVo>) commonDao.queryList(wcRescBVo);

			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = wcRescBVoList == null ? 0 : wcRescBVoList.size();
			for (i = 0; i < size; i++) {
				wcRescBVo = wcRescBVoList.get(i);
				model.put(wcRescBVo.getRescId() + "_" + wcRescBVo.getLangTypCd(), wcRescBVo.getRescVa());
			}
		}
	}
}
