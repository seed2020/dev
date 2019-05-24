package com.innobiz.orange.mobile.pt.secu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innobiz.orange.mobile.pt.svc.MoSecuSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.em.preview.PreviewAuthSvc;
import com.innobiz.orange.web.pt.secu.SecuCookieChecker;
import com.innobiz.orange.web.pt.sso.SsoClientSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Component
public class MoBridge {
	
	/** MobileBridge static(스테틱) 인스턴스 */
	protected static MoBridge ins = null;
	
	/** 포털 공통 서비스 */
	@Autowired
	protected PtCmSvc ptCmSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	protected PtSecuSvc ptSecuSvc;
	
	/** 모바일 보안 서비스 */
	@Autowired
	protected MoSecuSvc moSecuSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	protected PtSysSvc ptSysSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	protected PtPsnSvc PtPsnSvc;
	
	/** 미리보기 권한 서비스 */
	@Autowired
	protected PreviewAuthSvc previewAuthSvc;
	
	/** 메세지 */
	@Autowired
	protected MessageProperties messageProperties;
	
	/** SSO 클라이언트 서비스 */
	@Autowired
	protected SsoClientSvc ssoClientSvc;
	
	/** 보안쿠키 체크용 객체 */
	@Autowired
	protected SecuCookieChecker secuCookieChecker; 
	
	/** 생성자 */
	public MoBridge(){
		ins = this;
	}

}
