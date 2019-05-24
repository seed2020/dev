package com.innobiz.orange.web.wb.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wb.vo.WbBcMetngAtndRVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;

@Service
public class WbMetngSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WbBcMetngDVo> getWbBcList(WbBcMetngDVo wbBcMetngDVo ) throws SQLException {
		return (List<WbBcMetngDVo>)commonDao.queryList(wbBcMetngDVo);
	}
	
	/** 목록 조회 건수 */
	public Integer getWbBcListCnt(WbBcMetngDVo wbBcMetngDVo ) throws SQLException {
		return commonDao.count(wbBcMetngDVo);
	}
	
	/** 목록 조회[MAP] */
	public Map<String,Object> getWbBcMetngMapList(HttpServletRequest request , WbBcMetngDVo wbBcMetngDVo ) throws SQLException {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		/*
		String schWord = wbBcMetngDVo.getSchWord();//검색어
		if(schWord!=null && !schWord.isEmpty()){
			wbBcMetngDVo.setSchCat(wbBcMetngDVo.getSchCat());
			wbBcMetngDVo.setSchWord(schWord);
		}*/
		
		Integer recodeCount = this.getWbBcListCnt(wbBcMetngDVo);
		PersonalUtil.setPaging(request, wbBcMetngDVo, recodeCount);
		
		//목록 조회
		List<WbBcMetngDVo> wbBcMetngDVoList = this.getWbBcList(wbBcMetngDVo);
	
		rsltMap.put("wbBcMetngDVoList", wbBcMetngDVoList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	/** 상세보기 [미팅정보]*/
	public WbBcMetngDVo getWbMetngInfo(WbBcMetngDVo wbBcMetngDVo ) throws SQLException {
		return (WbBcMetngDVo)commonDao.queryVo(wbBcMetngDVo);
	}
	
	/** 상세보기 [미팅,참석자 정보 포함]*/
	public WbBcMetngDVo getWbMetngInfoVo(WbBcMetngDVo wbBcMetngDVo, String langTypCd) throws SQLException {
		wbBcMetngDVo = (WbBcMetngDVo)commonDao.queryVo(wbBcMetngDVo);
		
		if(wbBcMetngDVo == null){
			return null;		
		}
		
		// 미팅 참석자 정보
		WbBcMetngAtndRVo wbBcMetngAtndRVo = new WbBcMetngAtndRVo();
		wbBcMetngAtndRVo.setQueryLang(langTypCd);
		wbBcMetngAtndRVo.setCompId(wbBcMetngDVo.getCompId());
		wbBcMetngAtndRVo.setRegrUid(wbBcMetngDVo.getRegrUid());
		wbBcMetngAtndRVo.setBcMetngDetlId(wbBcMetngDVo.getBcMetngDetlId());
		/** 참석자 조회 */
		@SuppressWarnings("unchecked")
		List<WbBcMetngAtndRVo> wbBcMetngAtndRVoList = (List<WbBcMetngAtndRVo>)commonDao.queryList(wbBcMetngAtndRVo);
		
		wbBcMetngDVo.setWbBcMetngAtndRVo(wbBcMetngAtndRVoList);
		//model.put("wbBcMetngAtndRVoList", wbBcMetngAtndRVoList);
				
		return wbBcMetngDVo;
	}
	
	
	/** 저장 */
	public void saveMetng(HttpServletRequest request, QueryQueue queryQueue , WbBcMetngDVo wbBcMetngDVo , UserVo userVo) throws SQLException{
		String bcMetngDetlId = wbBcMetngDVo.getBcMetngDetlId();
		String regrUid = wbBcMetngDVo.getCopyRegrUid() !=null && !"".equals(wbBcMetngDVo.getCopyRegrUid()) ? wbBcMetngDVo.getCopyRegrUid() : userVo.getUserUid();
		//wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
		if (bcMetngDetlId == null || bcMetngDetlId.isEmpty()) {
			
			// ID 생성
			bcMetngDetlId = wbCmSvc.createId("WB_BC_METNG_D");
			wbBcMetngDVo.setBcMetngDetlId(bcMetngDetlId);
			// 등록자, 등록일시
			wbBcMetngDVo.setRegrUid(regrUid);
			wbBcMetngDVo.setRegDt("sysdate");
			
			// 명함미팅상세(WB_BC_METNG_D) 테이블 - INSERT
			queryQueue.insert(wbBcMetngDVo);
			
		} else {
			// 수정자, 수정일시
			wbBcMetngDVo.setModrUid(regrUid);
			wbBcMetngDVo.setModDt("sysdate");
			// 게시판관리(BA_BRD_B) 테이블 - UPDATE
			queryQueue.update(wbBcMetngDVo);
		}
		
		// 참석자
		@SuppressWarnings("unchecked")
		List<WbBcMetngAtndRVo> wbBcMetngAtndRVoList = (List<WbBcMetngAtndRVo>)VoUtil.bindList(request, WbBcMetngAtndRVo.class, new String[]{"emplNm"});
		
		if(wbBcMetngAtndRVoList.size() > 0 ){
			List<WbBcMetngAtndRVo> atndInsertList = new ArrayList<WbBcMetngAtndRVo>();//신규항목
			List<WbBcMetngAtndRVo> atndUpdateList = new ArrayList<WbBcMetngAtndRVo>();//수정항목(delete하기 위해서)
			for(WbBcMetngAtndRVo wbBcMetngAtndRVo : wbBcMetngAtndRVoList){
				wbBcMetngAtndRVo.setCompId(wbBcMetngDVo.getCompId());
				wbBcMetngAtndRVo.setRegrUid(regrUid);
				wbBcMetngAtndRVo.setBcMetngDetlId(wbBcMetngDVo.getBcMetngDetlId());
				
				if(wbBcMetngAtndRVo.getBcMetngAtndDetlId() != null && !wbBcMetngAtndRVo.getBcMetngAtndDetlId().isEmpty()){
					atndUpdateList.add(wbBcMetngAtndRVo);
				}else{
					atndInsertList.add(wbBcMetngAtndRVo);
				}
			}
			
			//수정할 목록이 있을경우 부분삭제 또는 전체삭제 한다.
			if(atndUpdateList.size() > 0 || atndUpdateList.size() == 0){
				WbBcMetngAtndRVo deleteAtndVo = new WbBcMetngAtndRVo();
				deleteAtndVo.setCompId(wbBcMetngDVo.getCompId());
				deleteAtndVo.setRegrUid(wbBcMetngDVo.getRegrUid());
				deleteAtndVo.setBcMetngDetlId(wbBcMetngDVo.getBcMetngDetlId());
				String[] deleteAtndIds = new String[atndUpdateList.size()];
				if(atndUpdateList.size() > 0){//수정항목이 있으면 해당 UID를 배열에 담아 삭제 처리한다.
					for(int i=0;i<atndUpdateList.size();i++){
						deleteAtndIds[i] = atndUpdateList.get(i).getBcMetngAtndDetlId();
					}
					deleteAtndVo.setDeleteAtndIds(deleteAtndIds);
				}
				queryQueue.delete(deleteAtndVo);
			}
			
			// 등록할 목록이 있을경우 등록처리한다.
			if(atndInsertList.size() > 0 ){
				for(WbBcMetngAtndRVo insertAtndVo : atndInsertList){
					insertAtndVo.setBcMetngAtndDetlId(wbCmSvc.createId("WB_BC_METNG_ATND_R"));
					queryQueue.insert(insertAtndVo);
				}
			}
		}else{
			WbBcMetngAtndRVo deleteAtndVo = new WbBcMetngAtndRVo();
			deleteAtndVo.setCompId(wbBcMetngDVo.getCompId());
			deleteAtndVo.setRegrUid(wbBcMetngDVo.getRegrUid());
			deleteAtndVo.setBcMetngDetlId(wbBcMetngDVo.getBcMetngDetlId());
			queryQueue.delete(deleteAtndVo);
		}
		
	}
	
	/** 전체 삭제 */
	public void deleteMetng(QueryQueue queryQueue , String ids , WbBcMetngDVo searchVO) throws SQLException {
		String[] idArray = ids.split(",");
		
		//미팅
		WbBcMetngDVo wbBcMetngDVo = null;
		
		//참석자
		WbBcMetngAtndRVo wbBcMetngAtndRVo = null;
		
		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		for (String id : idArray) {
			// 미팅
			wbBcMetngDVo = new WbBcMetngDVo();
						
			//참석자 삭제
			wbBcMetngAtndRVo = new WbBcMetngAtndRVo();
			wbBcMetngAtndRVo.setCompId(searchVO.getCompId());//회사ID
			if(searchVO.getModrUid() != null && !searchVO.getModrUid().isEmpty()){
				wbBcMetngDVo.setRegrUid(searchVO.getModrUid());
				wbBcMetngAtndRVo.setRegrUid(searchVO.getModrUid());
			}
			wbBcMetngAtndRVo.setBcMetngDetlId(id);
			
			queryQueue.delete(wbBcMetngAtndRVo);
			
			// 미팅 삭제
			wbBcMetngDVo.setCompId(searchVO.getCompId());//회사ID
			wbBcMetngDVo.setBcMetngDetlId(id);
			queryQueue.delete(wbBcMetngDVo);
			
			// 첨부파일
			deletedFileList.addAll(wbBcMetngFileSvc.deleteBcFile(id, queryQueue));
		}
		
		//첨부파일 삭제
		if(deletedFileList.size() > 0 ){
			// 파일 삭제
			wbBcMetngFileSvc.deleteDiskFiles(deletedFileList);
		}		
		
		
	}
	
}
