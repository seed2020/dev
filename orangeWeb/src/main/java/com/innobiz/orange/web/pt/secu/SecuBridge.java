package com.innobiz.orange.web.pt.secu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.em.preview.PreviewAuthSvc;
import com.innobiz.orange.web.pt.sso.SsoClientSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoginSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** Filter 에서 Spring 영역에 있는 서비스를 사용하기 위한 브릿지.
 *  - 스프링 기능에 의해 서비스를 직접 꽂아 넣고 쓸 경우 context-root 와 나머지 부분이 다른 메모리 영역에 할당되어
 *    캐쉬등의 데이터가 동기화 되지 않는 문제가 생겨, static 링크 형태로 전환하기 위한 객체
 **/
@Component
public class SecuBridge {
	
	/** SecuBridge static(스테틱) 인스턴스 */
	protected static SecuBridge ins = null;
	
	/** 포털 공통 서비스 */
	@Autowired
	protected PtCmSvc ptCmSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	protected PtSecuSvc ptSecuSvc;
	
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
	
	/** 로그인 관련 서비스 */
	@Autowired
	protected PtLoginSvc ptLoginSvc;
	
	/** 생성자 */
	public SecuBridge(){
		ins = this;
	}
	
}
