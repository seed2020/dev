package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApRescBVo;
import com.innobiz.orange.web.ap.vo.ApRescDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 결재 리소스 처리 서비스 */
@Service
public class ApRescSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 리소스기본(AP_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public ApRescBVo collectApRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException{
		
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
			rescId = apCmSvc.createId("AP_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		ApRescBVo apRescBVo, firstApRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
		for(i=0;i<size;i++){
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix+"Va_"+ptCdBVo.getCd());
			if(rescVa!=null && !rescVa.isEmpty()){
				
				apRescBVo = new ApRescBVo();
				apRescBVo.setRescId(rescId);
				apRescBVo.setLangTypCd(ptCdBVo.getCd());
				apRescBVo.setRescVa(rescVa);
				
				if(firstApRescBVo==null || "ko".equals(ptCdBVo.getCd())){
					firstApRescBVo = apRescBVo;
				}
				
				if(emptyId){
					queryQueue.insert(apRescBVo);
				} else {
					queryQueue.store(apRescBVo);
				}
			}
		}
		
		return firstApRescBVo;
	}
	
	/** 리소스기본(AP_RESC_D) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public ApRescDVo collectApRescDVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException{
		
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
			rescId = apCmSvc.createId("AP_RESC_D");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		ApRescDVo apRescDVo, firstApRescDVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
		for(i=0;i<size;i++){
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix+"Va_"+ptCdBVo.getCd());
			if(rescVa!=null && !rescVa.isEmpty()){
				
				apRescDVo = new ApRescDVo();
				apRescDVo.setCompId(userVo.getCompId());
				apRescDVo.setRescId(rescId);
				apRescDVo.setLangTypCd(ptCdBVo.getCd());
				apRescDVo.setRescVa(rescVa);
				
				if(firstApRescDVo==null || "ko".equals(ptCdBVo.getCd())){
					firstApRescDVo = apRescDVo;
				}
				
				if(emptyId){
					queryQueue.insert(apRescDVo);
				} else {
					queryQueue.store(apRescDVo);
				}
			}
		}
		
		return firstApRescDVo;
	}
	
	/** 사용자 정보조회 */
	public OrUserBVo getOrUserBVo(String userUid, String langTypCd, Map<Integer, OrUserBVo> userCacheMap) throws SQLException{
		Integer hash = null;
		if(userCacheMap != null){
			hash = Hash.hashUid(userUid);
			OrUserBVo orUserBVo = userCacheMap.get(hash);
			if(orUserBVo!=null) return orUserBVo;
		}
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
		if(orUserBVo!=null){
			if(userCacheMap != null) userCacheMap.put(hash, orUserBVo);
			return orUserBVo;
		}
		return null;
	}
	/** 부서 정보조회 */
	public OrOrgBVo getOrOrgBVo(String orgId, String langTypCd, Map<Integer, OrOrgBVo> orgCacheMap) throws SQLException{
		Integer hash = null;
		if(orgCacheMap != null){
			hash = Hash.hashId(orgId);
			OrOrgBVo orOrgBVo = orgCacheMap.get(hash);
			if(orOrgBVo!=null) return orOrgBVo;
		}
		
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgId(orgId);
		orOrgBVo.setQueryLang(langTypCd);
		orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
		if(orOrgBVo!=null){
			if(orgCacheMap != null) orgCacheMap.put(hash, orOrgBVo);
			return orOrgBVo;
		}
		return null;
	}
	/** 조직의 리소스 리턴 */
	public String getOrRescVa(String rescId, String langTypCd) throws SQLException{
		OrRescBVo orRescBVo = new OrRescBVo();
		orRescBVo.setRescId(rescId);
		orRescBVo.setLangTypCd(langTypCd);
		orRescBVo = (OrRescBVo)commonDao.queryVo(orRescBVo);
		if(orRescBVo!=null) return orRescBVo.getRescVa();
		return null;
	}
}
