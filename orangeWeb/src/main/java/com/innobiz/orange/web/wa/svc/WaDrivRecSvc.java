package com.innobiz.orange.web.wa.svc;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;

/** 자동차 운행기록 서비스 */
@Service
public class WaDrivRecSvc {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuNaraInsaSvc.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
//	/** 암호화 서비스 */
//	@Autowired
//	private CryptoSvc cryptoSvc;
	
	
}
