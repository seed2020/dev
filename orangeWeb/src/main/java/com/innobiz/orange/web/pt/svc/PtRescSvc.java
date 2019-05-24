package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;

/** 리소스 조회 저장용 서비스
 * <br/>
 * 관리자용 화면에서 어권별 리소스를 조회/저장 할 때 사용 */
@Service
public class PtRescSvc {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 리소스상세(PT_RESC_D)에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public PtRescDVo collectPtRescDVo(HttpServletRequest request, String prefix, String[] langTypCds, QueryQueue queryQueue) throws SQLException{
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// COMP_ID - 없으면 세션에서 가져옴
		String compId = request.getParameter("compId");
		if(compId==null || compId.isEmpty()){
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		
		// 리소스 prefix 설정
		String rescPrefix = prefix==null || prefix.isEmpty() ? "resc" : prefix+"Resc";
		
		// rescId 받음 : 없으면 생성
		String rescId = request.getParameter(rescPrefix+"Id"), rescVa;
		if(rescId==null || rescId.isEmpty()){
			rescId = ptCmSvc.createId("PT_RESC_D");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		PtRescDVo firstPtRescDVo = null;
		
		if(langTypCds==null){
			List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
			int i, size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
			langTypCds = new String[size];
			for(i=0;i<size;i++){
				langTypCds[i] = ptCdBVoList.get(i).getCd();
			}
		}
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtRescDVo ptRescDVo;
		int i, size = langTypCds.length;
		for(i=0;i<size;i++){
			rescVa = request.getParameter(rescPrefix+"Va_"+langTypCds[i]);
			if(rescVa!=null && !rescVa.isEmpty()){
				
				ptRescDVo = new PtRescDVo();
				ptRescDVo.setCompId(compId);
				ptRescDVo.setRescId(rescId);
				ptRescDVo.setLangTypCd(langTypCds[i]);
				ptRescDVo.setRescVa(rescVa);
				
				if(firstPtRescDVo==null || "ko".equals(langTypCds[i])){
					firstPtRescDVo = ptRescDVo;
				}
				
				if(emptyId){
					queryQueue.insert(ptRescDVo);
				} else {
					queryQueue.store(ptRescDVo);
				}
			}
		}
		
		return firstPtRescDVo;
	}
	
	/** 리소스기본(PT_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public PtRescBVo collectPtRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// 리소스 prefix 설정
		String rescPrefix = prefix==null || prefix.isEmpty() ? "resc" : prefix+"Resc";
		// rescId 받음 : 없으면 생성
		String rescId = request.getParameter(rescPrefix+"Id"), rescVa;
		if(rescId==null || rescId.isEmpty()){
			rescId = ptCmSvc.createId("PT_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		PtRescBVo ptRescBVo, firstPtRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
		for(i=0;i<size;i++){
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix+"Va_"+ptCdBVo.getCd());
			if(rescVa!=null && !rescVa.isEmpty()){
				
				ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(rescId);
				ptRescBVo.setLangTypCd(ptCdBVo.getCd());
				ptRescBVo.setRescVa(rescVa);
				
				if(firstPtRescBVo==null || "ko".equals(ptCdBVo.getCd())){
					firstPtRescBVo = ptRescBVo;
				}
				
				if(emptyId){
					queryQueue.insert(ptRescBVo);
				} else {
					queryQueue.store(ptRescBVo);
				}
			}
		}
		
		return firstPtRescBVo;
	}
	
	/** 리소스기본(PT_RESC_B) 테이블에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectPtRescBVoList(HttpServletRequest request, List<PtCdBVo> ptCdBVoList, 
			QueryQueue queryQueue) throws SQLException{
		
		PtRescBVo ptRescBVo, firstPtRescBVo;
		String rescId, rescVa;
		
		// 값이 유효한지 체크할 컬럼
		String[] cds = request.getParameterValues("cd");
		// 리소스ID, 없으면 생성함
		String[] rescIds = request.getParameterValues("rescId");
		boolean isNewResc = false;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		PtCdBVo ptCdBVo;
		
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
		
		for(i=0;i<cds.length;i++){
			
			// 코드값 없으면 불완전 데이터 - 무시
			if(cds[i]==null || cds[i].isEmpty()) continue;
			
			// 리소스ID 없으면 생성
			rescId = i<rescIds.length ? rescIds[i] : null;
			if(rescId==null || rescId.isEmpty()){
				rescId = ptCmSvc.createId("PT_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}
			
			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstPtRescBVo = null;
			
			// 어권 만큼 돌면서
			for(j=0;j<size;j++){
				
				rescVas = j<rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas!=null && i<rescVas.length) ? rescVas[i] : null;
				if(rescVa==null) continue;
				
				ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(rescId);
				ptRescBVo.setLangTypCd(langs[j]);
				ptRescBVo.setRescVa(rescVa);
				
				if(isNewResc){
					queryQueue.insert(ptRescBVo);
				} else {
					queryQueue.store(ptRescBVo);
				}
				
				if(firstPtRescBVo==null || "ko".equals(langs[j])){
					firstPtRescBVo = ptRescBVo;
				}
			}
			
			if(ptCdBVoList.size()>i){
				ptCdBVo = ptCdBVoList.get(i);
				
				// 리소스ID, 리소스값을 코드기본(PT_CD_B) 테이블에 세팅
				if(firstPtRescBVo!=null){
					ptCdBVoList.get(i).setRescId(rescId);
					ptCdBVoList.get(i).setCdVa(firstPtRescBVo.getRescVa());
				}
				
				// 코드기본(PT_CD_B) 테이블 저장
				ptCdBVo.setModDt("sysdate");
				ptCdBVo.setModrUid(userVo.getUserUid());
				if(isNewResc){
					ptCdBVo.setFldYn("N");//폴더여부
					ptCdBVo.setSysCdYn("N");
					ptCdBVo.setRegDt("sysdate");
					ptCdBVo.setRegrUid(userVo.getUserUid());
					queryQueue.insert(ptCdBVo);
				} else {
					queryQueue.store(ptCdBVo);
				}
			}
		}
	}
	
	/** 리소스기본(PT_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectPtRescBVoList(HttpServletRequest request, 
			QueryQueue queryQueue, List<? extends CommonVo> commonVoList,
			String validCol, String rescIdCol, String rescVaCol) throws SQLException{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		PtRescBVo ptRescBVo, firstPtRescBVo;
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
				rescId = ptCmSvc.createId("PT_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}
			
			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstPtRescBVo = null;
			
			// 어권 만큼 돌면서
			for(j=0;j<size;j++){
				
				rescVas = j<rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas!=null && i<rescVas.length) ? rescVas[i] : null;
				if(rescVa==null) continue;
				
				ptRescBVo = new PtRescBVo();
//				ptRescBVo.setCompId(compId);
				ptRescBVo.setRescId(rescId);
				ptRescBVo.setLangTypCd(langs[j]);
				ptRescBVo.setRescVa(rescVa);
				
				if(isNewResc){
					queryQueue.insert(ptRescBVo);
				} else {
					queryQueue.store(ptRescBVo);
				}
				
				if(firstPtRescBVo==null || "ko".equals(langs[j])){
					firstPtRescBVo = ptRescBVo;
				}
			}
			
			
			if(commonVoList.size()>commonIndex){
				commonVo = commonVoList.get(commonIndex++);
				
				// 리소스ID, 리소스값을 
				if(firstPtRescBVo!=null){
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstPtRescBVo.getRescVa());
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
	
	/** 리소스기본(PT_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException{
		
		// 회사명 언어별 리소스 조회
		if(rescId!=null && !rescId.isEmpty()){
			PtRescBVo ptRescBVo = new PtRescBVo();
			ptRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);
			
			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = ptRescBVoList==null ? 0 : ptRescBVoList.size();
			for(i=0;i<size;i++){
				ptRescBVo = ptRescBVoList.get(i);
				model.put(ptRescBVo.getRescId()+"_"+ptRescBVo.getLangTypCd(), ptRescBVo.getRescVa());
			}
		}
	}
	
	/** 리소스상세(PT_RESC_D) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescDVo(String compId, String rescId, ModelMap model) throws SQLException{
		
		// 회사명 언어별 리소스 조회
		if(rescId!=null && !rescId.isEmpty()){
			PtRescDVo ptRescDVo = new PtRescDVo();
			ptRescDVo.setCompId(compId);
			ptRescDVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
			
			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = ptRescDVoList==null ? 0 : ptRescDVoList.size();
			for(i=0;i<size;i++){
				ptRescDVo = ptRescDVoList.get(i);
				model.put(ptRescDVo.getRescId()+"_"+ptRescDVo.getLangTypCd(), ptRescDVo.getRescVa());
			}
		}
	}
	
}
