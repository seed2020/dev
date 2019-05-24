package com.innobiz.orange.web.ct.svc;

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
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

@Service
public class CtRescSvc {
	
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CtRescSvc.class);
	
	/** 커뮤니티 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 리소스기본(CT_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws Exception{
		
		// 회사명 언어별 리소스 조회
		if(rescId!=null && !rescId.isEmpty()){
			CtRescBVo ctRescBVo = new CtRescBVo();
			ctRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<CtRescBVo> ctRescBVoList = (List<CtRescBVo>)commonSvc.queryList(ctRescBVo);
			
			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = ctRescBVoList==null ? 0 : ctRescBVoList.size();
			for(i=0;i<size;i++){
				ctRescBVo = ctRescBVoList.get(i);
				model.put(ctRescBVo.getRescId()+"_"+ctRescBVo.getLangTypCd(), ctRescBVo.getRescVa());
			}
		}
	}
	
	
	/** 리소스기본(CT_RESC_B)에 저장할 리소스 데이터를 QueryQueue 에 저장 */
	public CtRescBVo collectCtRescBVo(HttpServletRequest request, QueryQueue queryQueue, String prefix, List<PtCdBVo> langTypCdList) throws SQLException{
		
		CtRescBVo ctRescBVo, returnOrRescBVo = null;
		boolean emptyPrefix = prefix==null || prefix.isEmpty(), isNewResc = false;
		String rescId = request.getParameter(emptyPrefix ? "rescId" : prefix+"RescId"), rescVa;
		if(rescId==null || rescId.isEmpty()){
			rescId = ctCmSvc.createId("CT_RESC_B");
			isNewResc = true;
		}
		
		for(PtCdBVo ptCdBVo : langTypCdList){
			rescVa = request.getParameter(emptyPrefix ? "rescVa_"+ptCdBVo.getCd() : prefix+"RescVa_"+ptCdBVo.getCd());
			if(rescVa==null || rescVa.isEmpty()) continue;
			
			ctRescBVo = new CtRescBVo();
			ctRescBVo.setRescId(rescId);
			ctRescBVo.setLangTypCd(ptCdBVo.getCd());
			ctRescBVo.setRescVa(rescVa);
			if(isNewResc){
				queryQueue.insert(ctRescBVo);
			} else {
				queryQueue.store(ctRescBVo);
			}
			if(returnOrRescBVo==null || "ko".equals(ptCdBVo.getCd())){
				returnOrRescBVo = ctRescBVo;
			}
		}
		return returnOrRescBVo;
	}
	
	/** 리소스기본(CT_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectCtRescBVoList(HttpServletRequest request, QueryQueue queryQueue, List<? extends CommonVo> commonVoList
			, String validCol, String rescIdCol, String rescVaCol) throws SQLException{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		CtRescBVo ctRescBVo, firstOrRescBVo;
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
				rescId = ctCmSvc.createId("CT_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}
			
			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstOrRescBVo = null;
			
			// 어권 만큼 돌면서
			for(j=0;j<size;j++){
				
				rescVas = j<rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas!=null && i<rescVas.length) ? rescVas[i] : null;
				if(rescVa==null) continue;
				
				ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(rescId);
				ctRescBVo.setLangTypCd(langs[j]);
				ctRescBVo.setRescVa(rescVa);
				
				if(isNewResc){
					queryQueue.insert(ctRescBVo);
				} else {
					queryQueue.store(ctRescBVo);
				}
				
				if(firstOrRescBVo==null || "ko".equals(langs[j])){
					firstOrRescBVo = ctRescBVo;
				}
			}
			
			
			if(commonVoList.size()>commonIndex){
				commonVo = commonVoList.get(commonIndex++);
				
				// 리소스ID, 리소스값을 
				if(firstOrRescBVo!=null){
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstOrRescBVo.getRescVa());
				}
				
				if(isNewResc){
					VoUtil.setValue(commonVo, "regDt", "sysdate");
					VoUtil.setValue(commonVo, "regrUid", userVo.getUserUid());
					queryQueue.insert(commonVo);
				} else {
					queryQueue.store(commonVo);
				}
				
			}
		}
	}

	/** 리소스기본(CT_RESC_B)에 저장할 리소스  복사하여 새로운 RESCID 생성*/
	public String collectCtRescCopy(HttpServletRequest request, QueryQueue queryQueue, String rescId) throws SQLException{
		//CtRescBVo ctRescBVo, returnOrRescBVo = null;
		
		CtRescBVo ctRescFindVo = new CtRescBVo();
		ctRescFindVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<CtRescBVo> ctRescFindList = (List<CtRescBVo>) commonDao.queryList(ctRescFindVo);
		
		String ctRescID = ctCmSvc.createId("CT_RESC_B");
		
		for(CtRescBVo ctrescVo : ctRescFindList){
			CtRescBVo ctRescCloneVo = new CtRescBVo();
			ctRescCloneVo.setRescId(ctRescID);
			ctRescCloneVo.setRescVa(ctrescVo.getRescVa());
			ctRescCloneVo.setLangTypCd(ctrescVo.getLangTypCd());
			queryQueue.insert(ctRescCloneVo);
		}
		return ctRescID;
	}
	
}
