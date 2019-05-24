package com.innobiz.orange.web.wr.svc;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wr.vo.WrRescImgDVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;

@Service
public class WrRescMngSvc {
	
	/** 자원관리 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WrRescFileSvc wrRescFileSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 자원관리 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WrRescMngBVo> getWrRescMngList(WrRescMngBVo wrRescMngBVo ) throws Exception {
		return (List<WrRescMngBVo>)commonDao.queryList(wrRescMngBVo);
	}
	
	/** 자원관리 목록 조회 */
	public Integer getWrRescMngListCnt(WrRescMngBVo wrRescMngBVo ) throws Exception {
		return commonDao.count(wrRescMngBVo);
	}
	
	/** 자원관리 이미지 조회 */
	public WrRescImgDVo getWrRescMngImg(WrRescImgDVo wrRescImgDVo ) throws Exception {
		wrRescImgDVo = (WrRescImgDVo)commonDao.queryVo(wrRescImgDVo);
		if( wrRescImgDVo == null ) return null;
		return wrRescImgDVo;
	}
	
	/** 자원관리 이미지 세팅 */
	public void setWrRescMngImg(List<WrRescMngBVo> wrRescMngBVoList ) throws Exception {
		if(wrRescMngBVoList.size() > 0){
			WrRescImgDVo wrRescImgDVo = null;
			for(WrRescMngBVo wrRescMngBVo : wrRescMngBVoList){
				wrRescImgDVo = new WrRescImgDVo();
				wrRescImgDVo.setRescMngId(wrRescMngBVo.getRescMngId());
				wrRescMngBVo.setWrRescImgDVo(this.getWrRescMngImg(wrRescImgDVo));
			}
		}
	}
	
	/** 자원관리 상세보기 */
	public WrRescMngBVo getWbBcInfo(WrRescMngBVo wrRescMngBVo ) throws Exception {
		wrRescMngBVo = (WrRescMngBVo)commonDao.queryVo(wrRescMngBVo);
		
		if(wrRescMngBVo != null){
			/** 자원관리이미지상세 */
			WrRescImgDVo wrRescImgDVo = new WrRescImgDVo();
			wrRescImgDVo.setRescMngId(wrRescMngBVo.getRescMngId());
			wrRescMngBVo.setWrRescImgDVo(this.getWrRescMngImg(wrRescImgDVo));
		}
		
		return wrRescMngBVo;
	}
	
	
	/** 자원관리 저장 [부가 정보 포함]*/
	public void saveRescMng(HttpServletRequest request, QueryQueue queryQueue , WrRescMngBVo wrRescMngBVo , UserVo userVo) throws Exception{
		String rescMngId = wrRescMngBVo.getRescMngId();
		if (rescMngId == null || rescMngId.isEmpty()) {
			
			// ID 생성
			rescMngId = wrCmSvc.createId("WR_RESC_MNG_B");
			wrRescMngBVo.setRescMngId(rescMngId);
			// 등록자, 등록일시
			wrRescMngBVo.setRegrUid(userVo.getUserUid());
			wrRescMngBVo.setRegDt("sysdate");
			
			// 순서 구하기
			WrRescMngBVo sortVo = new WrRescMngBVo();
			sortVo.setCompId(userVo.getCompId());
			sortVo.setRescKndId(wrRescMngBVo.getRescKndId());
			sortVo.setInstanceQueryId("com.innobiz.orange.web.wr.dao.WrRescMngBDao.maxWrRescMngB");
			
			Integer sortOrdr = commonDao.queryInt(sortVo);
			if(sortOrdr==null) sortOrdr = 1;
			wrRescMngBVo.setSortOrdr(sortOrdr.intValue());
			
			// 자원관리기본(WR_RESC_MNG_B) 테이블 - INSERT
			queryQueue.insert(wrRescMngBVo);
			
		} else {
			// 수정자, 수정일시
			wrRescMngBVo.setModrUid(userVo.getUserUid());
			wrRescMngBVo.setModDt("sysdate");
			// 자원관리기본(WR_RESC_MNG_B) 테이블 - UPDATE
			queryQueue.update(wrRescMngBVo);
		}
		
		//이미지 저장
		wrRescFileSvc.savePhoto(request, rescMngId, "photo", new WrRescImgDVo(), queryQueue);
	}
	
}
