package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 결재 문서 보안 서비스 */
@Service
public class ApDocSecuSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocSecuSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 결재 문서에 대한 조회 권한이 있는지 조회 */
	public boolean hasOngdAuth(UserVo userVo, String apvNo, String storage, String refdApvNo) throws SQLException{
		
		String userUid = userVo.getUserUid();
		String deptId = userVo.getDeptId();
		
		// 결재 라인에 속해 있는지 조회
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		if(apvNo!=null && !apvNo.isEmpty()){
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setOrderBy("APV_NO, APV_LN_PNO, APV_LN_NO");
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
			if(apOngdApvLnDVoList!=null){
				for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
					// 결재라인에 있는지
					if(userUid.equals(storedApOngdApvLnDVo.getApvrUid())) return true;
					// 기안자와 같은 부서에 있는지
					if("mak".equals(storedApOngdApvLnDVo.getApvrRoleCd()) || "byOne".equals(storedApOngdApvLnDVo.getApvrRoleCd())){
						if(deptId.equals(storedApOngdApvLnDVo.getApvDeptId()))  return true;
					}
				}
			}
		}
		
		// 참조 결재번호 가 있을 경우 - 참조되는 문서의 결재자인지 조회
		if(refdApvNo!=null && !refdApvNo.isEmpty()){
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(refdApvNo);
			apOngdApvLnDVo.setOrderBy("APV_NO, APV_LN_PNO, APV_LN_NO");
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
			if(apOngdApvLnDVoList!=null){
				for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
					// 결재라인에 있는지
					if(userUid.equals(storedApOngdApvLnDVo.getApvrUid())) return true;
//					// 기안자와 같은 부서에 있는지
//					if("mak".equals(storedApOngdApvLnDVo.getApvrRoleCd()) || "byOne".equals(storedApOngdApvLnDVo.getApvrRoleCd())){
//						if(deptId.equals(storedApOngdApvLnDVo.getApvDeptId()))  return true;
//					}
				}
			}
		}
		
		return false;
	}
	
	/** [결재비밀번호 암호화] 세션에 결재 보안번호 생성하고 해당 보안번호 리턴함 */
	public String createSecuId(HttpSession session){
		Long secuNo = Math.abs(StringUtil.getNextLong());// 보안
		Long current = System.currentTimeMillis();// 현재시간
		
		// list의 마지막에 현재 시간을 저장하며 3분이 경과하면 유효하지 않은 것으로 간주함
		@SuppressWarnings("unchecked")
		LinkedList<Long> apSecuList = (LinkedList<Long>)session.getAttribute("apSecuList");
		// 저장된 apSecuList 가 없거나, 유효하지 않으면 - 새 List를 만들어 세팅함
		if(apSecuList==null || expired(apSecuList, current)){
			apSecuList = new LinkedList<Long>();
			session.setAttribute("apSecuList", apSecuList);
		// 유효한 것이면 - 마지막의 시간을 현재 시간으로 하기위해 제거함
		} else if(apSecuList!=null){
			apSecuList.removeLast();
		}
		apSecuList.add(secuNo);// 보안번호 세팅
		apSecuList.add(current);// 현재시간 세팅
		return Long.toHexString(secuNo);// 핵사코드로 변환 리턴
	}
	
	/** [결재비밀번호 암호화] 세션의 결재 보안번호와 비교하여 같은 값인지 확인함 */
	public boolean confirmSecuId(HttpSession session, String secuId){
		// 저장된 apSecuList 가 없으면 해당 - 확인 못함
		@SuppressWarnings("unchecked")
		LinkedList<Long> apSecuList = (LinkedList<Long>)session.getAttribute("apSecuList");
		if(apSecuList==null){
			return false;
		}
		
		// List 가 만료 되었으면 - 확인 못함
		Long current = System.currentTimeMillis();
		if(expired(apSecuList, current)){
			UserVo userVo = (UserVo)session.getAttribute("userVo");
			LOGGER.error("AP - apv / doc password confirmation [EXPIRED] ! - "+userVo.getUserUid()+":"+userVo.getUserNm());
			session.removeAttribute("apSecuList");
			return false;
		}
		
		// 핵사코드 숫자 전환 - Exception 발생의 경우 - 확인 못함
		Long secuNo = null, storedSecuNo;
		try{ secuNo = Long.parseLong(secuId, 16);}
		catch(Exception e){ return false; }
		
		// 같은 번호가 있는지 비교
		int i, size = apSecuList.size()-1;
		for(i=0;i<size;i++){
			storedSecuNo = apSecuList.get(i);
			if(storedSecuNo!=null && storedSecuNo.equals(secuNo)){// sucuNo 가 같을때
				if(size==1){
					session.removeAttribute("apSecuList");
				} else {
					apSecuList.remove(i);
				}
				return true;
			}
		}
		return false;
	}
	
	/** [결재비밀번호 암호화] 만료된 보안번호인지 확인함 */
	private boolean expired(LinkedList<Long> apSecuList, Long current){
		return (current - apSecuList.getLast()) > (1000 * 60 * 3);
	}
}
