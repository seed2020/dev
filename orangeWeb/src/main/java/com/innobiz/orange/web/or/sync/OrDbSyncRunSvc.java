package com.innobiz.orange.web.or.sync;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.or.vo.OrDbIntgDVo;

/** 조직도 DB 동기화 실행 서비스 */
@Service
public class OrDbSyncRunSvc {
	
	/** 공통 DB 처리용 서비스  */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직도 DB 동기화 서비스 */
	@Autowired
	private OrDbSyncSvc orDbSyncSvc;
	
	/** 설정 정보를 마지막으로 조회한 시간 */
	private long LastConfigTime = 0L;
	
	/** 설정된 동기화 데이터 목록 */
	private List<OrDbSyncData> orDbSyncList = null;
	
	/** 년월일 포멧 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	/** 시분 포멧 */
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
	
	/** 스케쥴된 동기화 실행 */
	@Scheduled(fixedDelay=60000)
	public void runScheduledSync(){
		
		// 한대의 WAS 에서만 실행 되도록 함
		if(!ServerConfig.IS_PRIME || ServerConfig.IS_MOBILE) return;
		
		try {
			
			// 설정이 변경 되면 다시 로드함
			if(LastConfigTime==0){
				loadConfig();
			} else {
				if(OrDbSyncUtil.isConfigExpired(LastConfigTime)){
					loadConfig();
				}
			}
			if(orDbSyncList == null) return;
			
			// 현재 시간
			long currMillis = System.currentTimeMillis();
			String currYmd = dateFormat.format(new Date(currMillis));
			String currTime = timeFormat.format(new Date(currMillis));
			
			// 연계 시간
			String runTime;
			for(OrDbSyncData data : orDbSyncList){
				
				// 연계시간
				runTime = data.getRunTime();
				// 오늘 연계 안했고 && 연계시간이 지났으면
				if(!currYmd.equals(data.getLastRunYmd()) && currTime.compareTo(runTime) >= 0){
					
					// 오늘 동기화 했다고 표기
					data.setLastRunYmd(currYmd);
					
					// 동기화 실행
					orDbSyncSvc.sync(data.getCompId());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** DB에 설정된 동기화 설정 정보를 로드함 */
	private void loadConfig() throws SQLException{
		
		long currMillis = System.currentTimeMillis();
		String currYmd = dateFormat.format(new Date(currMillis));
		String currTime = timeFormat.format(new Date(currMillis));
		
		LastConfigTime = currMillis;
		
		// DB연계상세(OR_DB_INTG_D) 테이블 - 조회
		OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
		orDbIntgDVo.setUseYn("Y");
		@SuppressWarnings("unchecked")
		List<OrDbIntgDVo> list = (List<OrDbIntgDVo>)commonSvc.queryList(orDbIntgDVo);
		
		List<OrDbSyncData> orDbSyncList = new ArrayList<OrDbSyncData>();
		OrDbSyncData data;
		
		// 연계 시간
		String intgTm;
		if(list != null){
			for(OrDbIntgDVo storedOrDbIntgDVo : list){
				
				// 연계시간 이 설정되지 않으면, 연계 안함
				intgTm = storedOrDbIntgDVo.getIntgTm();
				if(intgTm==null || intgTm.isEmpty()) continue;
				
				data = new OrDbSyncData();
				data.setCompId(storedOrDbIntgDVo.getCompId());
				data.setRunTime(intgTm);
				
				// 현재 시간이 연계시간보다 크면 - 오늘은 연계 했다고 표기함 - 오늘 연계 안하도록 함
				if(currTime.compareTo(intgTm) >= 0){
					data.setLastRunYmd(currYmd);
				}
				orDbSyncList.add(data);
			}
		}
		this.orDbSyncList = orDbSyncList;
	}
}
