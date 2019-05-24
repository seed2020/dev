package com.innobiz.orange.web.cu.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.vo.CuNoteRecvBVo;
import com.innobiz.orange.web.cu.vo.CuNoteRecvLVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 공통 서비스 */
@Service
public class CuNoteSvc {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuNoteSvc.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNoteFileSvc")
	private CuNoteFileSvc cuNoteFileSvc;
	
	/** 시퀀스ID 생성 */
	public String createId(boolean isRecvNote) throws SQLException {
		if(!isRecvNote)
			return String.valueOf(commonSvc.nextVal("CU_NOTE_SEND_B").intValue());
		return String.valueOf(commonSvc.nextVal("CU_NOTE_RECV_B").intValue());
	}
	
	/** 첨부파일 ID 생성 */
	public Integer createFileId() throws SQLException {
		return commonSvc.nextVal("CU_NOTE_FILE_D").intValue();
	}
	
	/** 저장 */
	public CuNoteRecvBVo saveNote(HttpServletRequest request, QueryQueue queryQueue) throws CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 조회조건 매핑
		CuNoteRecvBVo cuNoteRecvBVo = new CuNoteRecvBVo();
		VoUtil.bind(request, cuNoteRecvBVo);
		cuNoteRecvBVo.setRecvNote(false);
		
		String sendNo=createId(false);
		// ID 생성
		cuNoteRecvBVo.setSendNo(sendNo);				
		// 등록자, 등록일시
		cuNoteRecvBVo.setRegrUid(userVo.getUserUid());
		cuNoteRecvBVo.setRegDt("sysdate");
		
		// INSERT
		queryQueue.insert(cuNoteRecvBVo);
					
		return cuNoteRecvBVo;
	}
	
	/** 보내기 */
	public Integer sendNote(HttpServletRequest request, QueryQueue queryQueue, CuNoteRecvBVo cuNoteRecvBVo) throws CmException, SQLException, ParseException {
		String recvList = ParamUtil.getRequestParam(request, "recvList", false);
		if(recvList==null || recvList.isEmpty()) return null;
		
		Integer returnCnt=0;
		recvList=recvList.replaceAll("\\\\","");
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray)parser.parse(recvList);
		if(jsonArray!=null && !jsonArray.isEmpty()){
			CuNoteRecvBVo recvVo=null;
			CuNoteRecvLVo cuNoteRecvLVo = null;
			JSONObject json;
			String recvNo, recvrUid=null, value=null;
			String[] attrs = new String[]{"sendNo", "subj", "cont", "regrUid"};
			for(int i=0;i<jsonArray.size();i++){
				json=(JSONObject)jsonArray.get(i);
				recvrUid=(String)json.get("userUid");
				if(recvrUid==null) continue;
				recvVo = new CuNoteRecvBVo();
				recvVo.setRecvrUid(recvrUid);
				// ID 생성
				recvNo=createId(true);		
				recvVo.setRecvNo(recvNo);	
				
				for(String attNm : attrs){
					value=(String)VoUtil.getValue(cuNoteRecvBVo, attNm);
					if(value!=null) VoUtil.setValue(recvVo, attNm, value);
				}
				// 등록일시
				recvVo.setRegDt("sysdate");
				recvVo.setReadYn("N");
				recvVo.setDelYn("N");
				
				// INSERT
				queryQueue.insert(recvVo);
				
				cuNoteRecvLVo = new CuNoteRecvLVo();
				cuNoteRecvLVo.setRecvNo(recvNo);
				cuNoteRecvLVo.setSendNo(cuNoteRecvBVo.getSendNo());
				cuNoteRecvLVo.setRecvrUid(recvrUid);
				
				// INSERT
				queryQueue.insert(cuNoteRecvLVo);
				
				returnCnt++;
			}
		}
		
		return returnCnt;
		
	}
	
	
	/** 상세보기 */
	public void viewNote(HttpServletRequest request, ModelMap model, boolean isRecvNote, boolean isAdmin) throws SQLException, CmException{
		String seqNo = ParamUtil.getRequestParam(request, "seqNo", false);
		if(seqNo==null || seqNo.isEmpty()) return;
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);		
		
		CuNoteRecvBVo cuNoteRecvBVo = new CuNoteRecvBVo();		
		cuNoteRecvBVo.setQueryLang(langTypCd);
		
		if(!isRecvNote){ // 보낸쪽지
			if(!isAdmin) cuNoteRecvBVo.setRegrUid(userVo.getUserUid());
			cuNoteRecvBVo.setRecvNote(isRecvNote);
			cuNoteRecvBVo.setSendNo(seqNo);
		}else{
			if(!isAdmin) cuNoteRecvBVo.setRecvrUid(userVo.getUserUid());
			cuNoteRecvBVo.setRecvNo(seqNo);
		}
		
		CuNoteRecvBVo returnVo = (CuNoteRecvBVo)commonSvc.queryVo(cuNoteRecvBVo);
		if(returnVo!=null){
			model.put("cuNoteRecvBVo", returnVo);
			
			// 첨부파일 리스트 model에 추가
			cuNoteFileSvc.putFileListToModel(returnVo.getSendNo(), model, userVo.getCompId());
			
			// 읽은시간 저장
			if(isRecvNote && !isAdmin && (returnVo.getReadYn()==null || "N".equals(returnVo.getReadYn()))){
				cuNoteRecvBVo.setReadYn("Y"); // 읽음여부 변경
				QueryQueue queryQueue = new QueryQueue();
				queryQueue.update(cuNoteRecvBVo);
				
				// 수신확인 저장
				CuNoteRecvLVo cuNoteRecvLVo = new CuNoteRecvLVo();
				cuNoteRecvLVo.setRecvNo(returnVo.getRecvNo());
				cuNoteRecvLVo.setRecvrNm(userVo.getUserNm());
				cuNoteRecvLVo.setRecvDt("sysdate");
				queryQueue.update(cuNoteRecvLVo);
				
				commonSvc.execute(queryQueue);			
			}
		}
	}
	
	/** 수신목록 세팅 */
	public void setCuNoteRecvLVoList(CuNoteRecvBVo cuNoteRecvBVo, String langTypCd, String sendNo) throws SQLException{
		// 수신확인 조회
		CuNoteRecvLVo cuNoteRecvLVo = new CuNoteRecvLVo();
		cuNoteRecvLVo.setSendNo(sendNo);
		cuNoteRecvLVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<CuNoteRecvLVo> cuNoteRecvLVoList = (List<CuNoteRecvLVo>) commonSvc.queryList(cuNoteRecvLVo);
		cuNoteRecvBVo.setCuNoteRecvLVoList(cuNoteRecvLVoList);
	}
	
	/** 삭제 */
	public void deleteNote(boolean isRecvNote, QueryQueue queryQueue, UserVo userVo, 
			String seqNo, boolean isAdmin) throws SQLException, CmException{
		
		// 쪽지 삭제
		CuNoteRecvBVo cuNoteRecvBVo = new CuNoteRecvBVo();
		if(!isRecvNote){ // 보낸쪽지
			if(!isAdmin) cuNoteRecvBVo.setRegrUid(userVo.getUserUid());
			cuNoteRecvBVo.setRecvNote(isRecvNote);
			cuNoteRecvBVo.setSendNo(seqNo);
			
			// 수신이력 삭제
			CuNoteRecvLVo cuNoteRecvLVo = new CuNoteRecvLVo();
			cuNoteRecvLVo.setSendNo(seqNo);
			queryQueue.delete(cuNoteRecvLVo);
			
		}else{
			if(!isAdmin) cuNoteRecvBVo.setRecvrUid(userVo.getUserUid());
			cuNoteRecvBVo.setRecvNo(seqNo);
		}
		queryQueue.delete(cuNoteRecvBVo);
	}
	
	/** 파일 삭제 */
	public void deleteFileList(QueryQueue queryQueue, String seqNo, JSONArray seqNos, boolean isRecvNote, List<CommonFileVo> deletedFileList) throws SQLException{
		
		List<String> seqNoList=new ArrayList<String>();
		
		if(seqNo!=null && !seqNo.isEmpty()) seqNoList.add(seqNo);
		if(seqNos!=null && !seqNos.isEmpty()){
			for(int i=0;i<seqNos.size();i++){
				seqNo = (String)seqNos.get(i);
				seqNoList.add(seqNo);
			}
		}
		CuNoteRecvBVo cuNoteRecvBVo = null;
		if(!isRecvNote){ // 보낸쪽지 삭제
			for(String no : seqNoList){
				cuNoteRecvBVo = new CuNoteRecvBVo();
				// 받은쪽지 조회
				cuNoteRecvBVo.setSendNo(no);
				if(commonSvc.count(cuNoteRecvBVo)==0){ // 받은쪽지가 없으면 파일 삭제
					deleteFile(queryQueue, deletedFileList, no);
				}
			}
			
		}else{ // 받은쪽지 삭제
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			List<String> recvIdList=null;
			String sendNo;
			for(String no : seqNoList){
				cuNoteRecvBVo = new CuNoteRecvBVo();
				// 받은쪽지 조회
				cuNoteRecvBVo.setRecvNo(no);
				cuNoteRecvBVo=(CuNoteRecvBVo)commonSvc.queryVo(cuNoteRecvBVo);
				if(cuNoteRecvBVo==null) continue;
				sendNo=cuNoteRecvBVo.getSendNo();
				if(!map.containsKey(sendNo)){
					recvIdList=new ArrayList<String>();
					map.put(sendNo, recvIdList);
				}
				recvIdList=map.get(sendNo);
				recvIdList.add(cuNoteRecvBVo.getRecvNo());
			}
			if(map.size()>0){
				Entry<String, List<String>> entry;
				Iterator<Entry<String, List<String>>> iterator = map.entrySet().iterator();
				int len=0;
				while(iterator.hasNext()){
					entry = iterator.next();
					sendNo=entry.getKey();
					// 보낸쪽지 조회
					cuNoteRecvBVo = new CuNoteRecvBVo();
					cuNoteRecvBVo.setRecvNote(false); // 받은쪽지 여부					
					cuNoteRecvBVo.setSendNo(sendNo);
					if(commonSvc.count(cuNoteRecvBVo)>0) // 보낸쪽지가 있으면 삭제 보류
						continue;
					
					len=entry.getValue().size();
					
					// 보낸쪽지가 없으면 받은쪽지 전체 건수 조회
					cuNoteRecvBVo = new CuNoteRecvBVo();
					cuNoteRecvBVo.setSendNo(sendNo);
					if(commonSvc.count(cuNoteRecvBVo)==len){ // 받은쪽지 와 삭제할 건수가 같으면 삭제
						deleteFile(queryQueue, deletedFileList, sendNo);
					}
				}
			}
		}
	}
	
	/** 파일 삭제 */
	public void deleteFile(QueryQueue queryQueue, List<CommonFileVo> deletedFileList, String sendNo) throws SQLException{
		List<CommonFileVo> list=null;
		if(deletedFileList!=null){
			list=cuNoteFileSvc.deleteNoteFile(sendNo, queryQueue);
			if(list!=null && list.size()>0) deletedFileList.addAll(list);
		}else{
			// 파일 삭제
			cuNoteFileSvc.deleteNoteFile(sendNo, queryQueue);
		}
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("cu") * 1024;
		model.put("bodySize", bodySize);
	}
	
}
