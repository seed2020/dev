package com.innobiz.orange.web.pt.sync;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 조직도 사용자 Push 방식 동기화 서비스 */
@Service
public class PushSyncSvc {

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 씽크 번호 - 랜덤 생성 했다가 sync(int) 함수로 같은 번호가 호출되면 동기화 진행 */
	private int syncNo = 1;
	
	/** 씽크 요청 번호 - syncNo 와 같으면 씽크 진행 */
	private int requiredSyncNo = -1;
	
	/** 동기화 대기 초 */
	private int waitSec = -1;
	
	/** 씽크 할 데이터 */
	private PushData pushData = new PushData();
	
	/** 씽크 요청할 url 목록 */
	private List<PushRequest> pushRequestList;
	
	/** 삭제 사용자의 email을 동기화 삭제 데이터로 사용하는지 여부 */
	private boolean emailDel = false;
	
	/** 삭제 사용자의 rid를 동기화 삭제 데이터로 사용하는지 여부 */
	private boolean ridDel = false;
	
	/** 삭제 사용자의 userUid를 동기화 삭제 데이터로 사용하는지 여부 */
	private boolean userUidDel = false;
	
	/** http request header 정보 - 보안을 위해 설정 */
	private Map<String, String> header;
	
	public PushSyncSvc(){
		header = new HashMap<String, String>();
		header.put("User-Agent", SyncConstant.USER_AGENT+"/"+SyncConstant.VERSION);
	}
	
	/** XML에 설정된 Push 설정정보를 세팅함 - servlet-context.xml */
	@Resource(name = "pushRequestList")
	public void setPushRequestList(List<PushRequest> pushRequestList){
		this.pushRequestList = pushRequestList;
		if(pushRequestList != null){
			for(PushRequest pushRequest : pushRequestList){
				if(pushRequest.isValid()){
					if("email".equals(pushRequest.getDeleteType())){
						emailDel = true;
					} else if("rid".equals(pushRequest.getDeleteType())){
						ridDel = true;
					} else if("userUid".equals(pushRequest.getDeleteType())){
						userUidDel = true;
					}
				}
			}
		}
	}
	
	/** 동기화 일시정지 - 동기화 데이터가 여러개 있을 경우 사용 */
	public int holdSync(){
		waitSec = -1;
		syncNo = StringUtil.getNextInt();
		return syncNo;
	}
	
	/** 동기화 일시정지 해제 - 동기화 진행 */
	public void syncByNo(int syncNo){
		requiredSyncNo = syncNo;
	}
	
	/** 지금 동기화 진행 */
	public void syncNow(){
		requiredSyncNo = syncNo;
	}
	
	/** 지금 동기화 진행 */
	public void syncAfter(int waitSec){
		this.waitSec = waitSec;
	}
	
	/** 스케쥴러에 의한 씽크 진행 */
	@Scheduled(fixedDelay=1000)
	public void run() {
		
		if(waitSec>0){
			waitSec--;
		}
		
		if(syncNo == requiredSyncNo || waitSec == 0){
			waitSec = -1;
			requiredSyncNo = -1;
			
			if(pushData.hasData()){
				PushData data = pushData;
				pushData = new PushData();
				String encrypted;
				
				if(pushRequestList != null){
					for(PushRequest pushRequest : pushRequestList){
						if(pushRequest.isValid()){
							try {
								if(isDebug()){
									System.out.println("MAIL ORG PUSH - "+data.toJson(pushRequest.getDeleteType()));
								}
								encrypted = License.ins.encryptIntegration(data.toJson(pushRequest.getDeleteType()));
								sendPushData(pushRequest.getUrl(), encrypted);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	/** 해당 URL을 암호화 데이터로 호출함 */
	private void sendPushData(String url, String encrypted) throws IOException {
		HttpClient http = new HttpClient();
		Map<String, String> param = new HashMap<String, String>();
		param.put("secuData", encrypted);
		http.sendPost(url, param, header, "UTF-8");
	}

	/** 이메일 정보로 삭제 정보를 관리하는지 여부 */
	public boolean hasEmailDel() {
		return emailDel;
	}
	
	/** rid 정보로 삭제 정보를 관리하는지 여부 */
	public boolean hasRidDel() {
		return ridDel;
	}
	
	/** userUid 정보로 삭제 정보를 관리하는지 여부 */
	public boolean hasUserUidDel() {
		return userUidDel;
	}
	
	/** 동기화가 있는지 여부 */
	public boolean hasSync(){
		return pushRequestList!=null && !pushRequestList.isEmpty();
	}
	
	/** 회사 정보 동기화 */
	public void syncComp(){
		pushData.setComp();
		syncNow();
	}
	
	/** 코드 정보 동기화 */
	public void syncCode(){
		pushData.setCode();
		syncAfter(1);
	}
	
	/** 조직도 정보 동기화 */
	public void syncOrg(){
		pushData.setOrg();
		syncAfter(1);
	}
	
	/** 사용자 조직도 전체 데이터 씽크 진행 */
	public void syncAll(){
		holdSync();
		pushData.setComp();
		pushData.setCode();
		pushData.setOrg();
		pushData.setAllUser();
		syncNow();
	}
	
	/** 사용자 동기화 */
	public void syncUsers(String userUids, String delEmails, String delUserUids, String delRids){
		holdSync();
		pushData.addUserUids(userUids);
		pushData.addDelEmails(delEmails);
		pushData.addDelUserUids(delUserUids);
		pushData.addDelRids(delRids);
		syncNow();
	}
	
	/** [시스템설정] 조직 메일 동기화 로그 */
	private boolean isDebug(){
		try {
			return "Y".equals(ptSysSvc.getSysPlocMap().get("orgMailSyncLogEnable"));
		} catch (SQLException e) {
			return false;
		}
	}
	
}
