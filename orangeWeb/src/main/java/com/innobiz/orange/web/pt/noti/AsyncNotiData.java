package com.innobiz.orange.web.pt.noti;

import java.io.IOException;
import java.util.Map;

import com.innobiz.orange.web.cm.utils.HttpClient;

/** 비동기 알림 데이터 */
public class AsyncNotiData {
	
	/** URL */
	private String url;
	/** 파라미터 */
	private Map<String, String> param;
	/** http header */
	private Map<String, String> header;
	/** 인코딩 charactor set */
	private String charset;
	/** 재시도 여부 */
	private boolean retry;
	/** post 방식으로 발송 할지 여부 */
	private boolean isPost = false;
	
	/** 최종 실행 시간 */
	private long lastExecTime = 0;
	/** 결과 확인 핸들러 */
	private AsyncNotiHandler asyncNotiHandler;
	/** 확장용 데이터 */
	private Map<String, String> exMap = null;
	
	/** 생성자 */
	public AsyncNotiData(String url, Map<String, String> header, String charset, boolean retry){
		this.url = url;
		this.header = header;
		this.charset = charset;
		this.retry = retry;
	}
	/** 생성자 */
	public AsyncNotiData(String url, Map<String, String> param, Map<String, String> header, String charset, boolean retry){
		this.url = url;
		this.param = param;
		this.header = header;
		this.charset = charset;
		this.retry = retry;
	}
	/** 생성자 */
	public AsyncNotiData(Map<String, String> exMap){
		this.exMap = exMap;
	}
	/** post 방식으로 보낼지 여부 세팅 */
	public void setPost(boolean isPost){
		this.isPost = isPost;
	}
	/** 비동기 알림 발송 */
	public String send() throws IOException {
		if(url!=null && !url.isEmpty()){
			if(isPost){
				return new HttpClient().sendPost(url, param, header, charset);
			} else {
				return new HttpClient().sendGet(url, header, charset);
			}
		}
		return null;
	}
	/** 실패시 재시도 여부 */
	public boolean isRetry() {
		return retry;
	}

	/** 최종 실행 시간 */
	public long getLastExecTime() {
		return lastExecTime;
	}
	/** 최종 실행 시간 */
	public void setLastExecTime(long lastExecTime) {
		this.lastExecTime = lastExecTime;
	}
	/** 결과 확인 핸들러 */
	public AsyncNotiHandler getAsyncNotiHandler() {
		return asyncNotiHandler;
	}
	/** 결과 확인 핸들러 */
	public void setAsyncNotiHandler(AsyncNotiHandler asyncNotiHandler) {
		this.asyncNotiHandler = asyncNotiHandler;
	}
	
	/** URL 방식 여부 */
	public boolean hasUrl(){
		return url!=null && !url.isEmpty();
	}
	/** 확장데이터가 있는지 여부 */
	public boolean hasExData(){
		return exMap!=null && !exMap.isEmpty();
	}
	/** 확장 데이터 */
	public Map<String, String> getExMap(){
		return exMap;
	}
	
	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":비동기 알림 데이터]\n");
		if(url!=null) { builder.append("url(URL):").append(url).append('\n'); }
		if(param!=null) { builder.append("param(파라미터):").append(param).append('\n'); }
		if(header!=null) { builder.append("header(해더):").append(header).append('\n'); }
		if(charset!=null) { builder.append("charset(인코딩):").append(charset).append('\n'); }
		if(retry) { builder.append("retry(재시도여부):").append(retry).append('\n'); }
		if(isPost) { builder.append("isPost(POST 여부):").append(isPost).append('\n'); }
		if(lastExecTime!=0) { builder.append("lastExecTime(최종실행시간):").append(lastExecTime).append('\n'); }
		if(exMap!=null) { builder.append("exMap(확장데이터):").append(exMap).append('\n'); }
		return builder.toString();
	}
}
