package com.innobiz.orange.web.em.svc;

import java.sql.SQLException;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.vo.EmSsoTVo;
import com.innobiz.orange.web.pt.secu.UserVo;

/** SSO 서비스 */
@Service
public class EmSsoSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** Sso Key 리턴 */
	public String getSsoKey(QueryQueue queryQueue, UserVo userVo, String ssoKey, Integer keyLen) throws SQLException {
		String ssoId=null;
		
		if(ssoKey==null || ssoKey.isEmpty()){
			if(keyLen==null || keyLen.intValue()>40) keyLen=40;
			ssoId=createRandomKey(keyLen);
		}else{
			ssoId=ssoKey;
		}
		
		EmSsoTVo emSsoTVo = new EmSsoTVo();
		emSsoTVo.setSsoId(ssoId);
		emSsoTVo.setUserUid(userVo.getUserUid());
		emSsoTVo.setRegDt("sysdate");
		
		if(queryQueue==null) commonDao.insert(emSsoTVo);
		else queryQueue.insert(emSsoTVo);
		
		return ssoId;
	}
	
	
	/** Sso Key 검증 */
	public boolean chkSsoKey(QueryQueue queryQueue, String ssoId, String userUid) throws SQLException {
		EmSsoTVo emSsoTVo = new EmSsoTVo();
		emSsoTVo.setSsoId(ssoId);
		if(userUid!=null) emSsoTVo.setUserUid(userUid);
		
		if(commonDao.count(emSsoTVo)>0){
			if(queryQueue==null) commonDao.delete(emSsoTVo);
			else queryQueue.delete(emSsoTVo);
			
			return true;
		}
		
		return false;
	}
	
	
	
	/** Sso Key 삭제 */
	public void deleteSsoKey(QueryQueue queryQueue, String ssoId, String userUid) throws SQLException {
		EmSsoTVo emSsoTVo = new EmSsoTVo();
		emSsoTVo.setSsoId(ssoId);
		if(userUid!=null) emSsoTVo.setUserUid(userUid);
		
		if(queryQueue==null) commonDao.delete(emSsoTVo);
		else queryQueue.delete(emSsoTVo);
	}
	
	/** 랜덤 문자+숫자 리턴 */
	public String createRandomKey(Integer len){
		if(len==null) len=15;
		Random rnd =new Random();
		
		StringBuilder sb =new StringBuilder();

		for(int i=0;i<len.intValue();i++){
		    // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
		    if(rnd.nextBoolean()){
		    	sb.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		    	sb.append((rnd.nextInt(10)));
		    }
		}
		
		return sb.toString();
	}
	
	
	
}
