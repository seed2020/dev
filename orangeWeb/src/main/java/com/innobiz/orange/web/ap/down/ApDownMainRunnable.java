package com.innobiz.orange.web.ap.down;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;

/** 다운로드용 메인 런어블
 *   - 다운로드용 서브 쓰레드를 생성하며
 *   - 결재 데이터를 조회하여 결재번호와 파일명을 큐에 저장하여 서브 쓰레드가 가져가게 함 */
public class ApDownMainRunnable implements Runnable {
	
	/** 실행 정보 */
	private Map<String, String> runningProp = null;
	/** 중지여부 */
	private boolean stopFlag = false;
	/** 데이터 조회 완료 여부 */
	private boolean dataThreadDone = false;
	
	/** 문서 다운로드 서비스 */
	private ApDownSvc parentSvc;
	
	/** 진행수 */
	private int procCnt = 0;
	/** 완료수 */
	private int cmplCnt = 0;
	/** 오류수 */
	private int errCnt  = 0;
	/** 전채수 */
	private int tgtCnt  = 0;
	/** 서브 쓰레드 갯수 */
	private int subThreadCnt = 0;
	/** mhtml 리소스 */
	private MhtmlResource mhtmlResource = null;
	
	/** 다운로드 데이터 큐 */
	private LinkedList<ApDownData> dataQueue = new LinkedList<ApDownData>();
	/** 생성자 */
	public ApDownMainRunnable(ApDownSvc parentSvc, Map<String, String> prop){
		this.parentSvc = parentSvc;
		runningProp = prop;
		tgtCnt = Integer.parseInt(prop.get("tgtCnt"));
	}
	/** 프라퍼티 조회 */
	public Map<String, String> getProperty(){
		return runningProp;
	}
	/** 완료수 */
	public int getCmplCnt(){
		return cmplCnt;
	}
	/** 오류수 */
	public int getErrCnt(){
		return errCnt;
	}
	/** 전채수 */
	public int getTgtCnt(){
		return tgtCnt;
	}
	/** 완료수 증가 */
	public synchronized void addCmplCnt(){
		cmplCnt++;
	}
	/** 오류수 증가 */
	public synchronized void addErrCnt(){
		errCnt++;
	}
	/** 다운로드 중지 */
	public void doStop(){
		runningProp.put("stop", "Y");
		stopFlag = true;
	}
	/** 중지 여부 */
	public boolean isStop(){
		return stopFlag;
	}
	/** 다운로드 진행 여부 */
	public boolean canProcess(){
		return procCnt < tgtCnt;
	}
	/** 다음 데이터 조회 */
	public ApDownData getNextData(){
		synchronized (dataQueue) {
			if(!dataQueue.isEmpty()){
				procCnt++;
				return dataQueue.removeFirst();
			}
		}
		if(dataThreadDone && procCnt < tgtCnt){
			tgtCnt = procCnt;
		}
		return null;
	}
	/** 서브 쓰레드수 감소 */
	public synchronized void exitSubTread(){
		subThreadCnt--;
		if(subThreadCnt == 0){
			doStop();
			if(mhtmlResource!=null){
				mhtmlResource.release();
				mhtmlResource = null;
			}
		}
	}
	/** 메인 쓰레드와 서브 쓰레드가 종료 되었는지 여부 */
	public boolean isProcessDone(){
		return subThreadCnt == 0 && dataThreadDone;
	}
	
	/** 쓰레드용 런 함수 */
	@Override
	public void run() {
		
		if(tgtCnt==0) return;
		
		int pageNo = 1;
		int addCnt = 0;
		
		// 다운로도 폴더 생성
		String path = runningProp.get("baseDir")+runningProp.get("storeDir");
		File dir = new File(path);
		if(!dir.isDirectory()) dir.mkdirs();
		
		// 기초 데이터
		String fileType = runningProp.get("fileType");
		String ext = fileType.startsWith("html") ? ".html" : ".mhtml";
		
		// 조회용 데이터 생성
		ApOngdBVo apOngdBVo = parentSvc.createDownVo(
				runningProp.get("compId"),
				runningProp.get("durStrtDt"),
				runningProp.get("durEndDt"));
		apOngdBVo.setOrderBy("APV_NO");
		apOngdBVo.setPageRowCnt(100);
		
		String fileName;
		
		while(!stopFlag){
			
			try {
				// 큐 에 데이터가 30개 이상이면 대기함
				if(dataQueue.size()>30){
					try{ Thread.sleep(250); }
					catch(Exception ignore){}
				
				// 총 갯수 이상이면 중지 - 데이터 갯수가 일반적으로 맞지만, 진행중 결재가 난 건은 저장하지 않기 위해서
				} else if(addCnt >= tgtCnt){
					break;
					
				} else {
					
					// 데이터 조회
					apOngdBVo.setPageNo(pageNo);
					@SuppressWarnings("unchecked")
					List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)parentSvc.commonDao.queryList(apOngdBVo);
					
					// 조회된 데이터가 있으면
					if(apOngdBVoList != null){
						synchronized (dataQueue) {
							for(ApOngdBVo stored : apOngdBVoList){
								
								// 파일명 생성
								fileName = stored.getMakDeptNm()+" "+stored.getMakDt().substring(0,10).replace('-', '.')+" "+stored.getDocSubj();
								if(fileName.length()>45) fileName = fileName.substring(0, 45)+ext;
								else fileName = fileName+ext;
								
								// 총 갯수 이상이면 중지
								if(addCnt >= tgtCnt) break;
								
								// 큐에 데이터 저장
								addCnt++;
								dataQueue.add(new ApDownData(stored.getApvNo(), fileName));
							}
						}
						
						// 최초 한번 - 서브 쓰레드를 생성 실행 시킴
						if(pageNo==1){
							int i, count = ServerConfig.IS_LOC ? 2 : 3;
							MhtmlResource mhtmlResource = fileType.startsWith("html") ? null : new MhtmlResource();
							HtmlResource htmlResource = fileType.equals("htmlImage") || fileType.equals("htmlImageFile") ? new HtmlResource(runningProp) : null;
							ApDownSubRunnable subRunnable;
							for(i=0; i<count; i++){
								subRunnable = new ApDownSubRunnable(this);
								subRunnable.setConfig(runningProp, dir, mhtmlResource, htmlResource);
								new Thread(subRunnable).start();
								subThreadCnt++;
							}
						}
						
					// 조회된 데이터 없으면
					} else {
						break;
					}
					
					// 다음 페이지 조회 - 100 개씩
					pageNo++;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 데이터 조회 쓰레드 완료됨 표시
		dataThreadDone = true;
	}
}
