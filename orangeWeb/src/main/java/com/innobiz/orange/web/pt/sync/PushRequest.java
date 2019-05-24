package com.innobiz.orange.web.pt.sync;

public class PushRequest {

	protected String deleteType;
	
	protected String url;
	
	public void setDeleteType(String deleteType){
		if(!"email".equals(deleteType) && !"userUid".equals(deleteType) && !"rid".equals(deleteType)){
			throw new IllegalArgumentException("attribute \"deleteType\" must be one of \"email\", \"userUid\", \"rid\" !");
		}
		this.deleteType = deleteType;
	}

	public void setUrl(String url) {
		if(url==null || url.isEmpty() || !url.startsWith("http")){
			throw new IllegalArgumentException("attribute \"url\" must start with \"http\" !");
		}
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String getDeleteType() {
		return deleteType;
	}
	
	public boolean isValid(){
		return deleteType!= null && url != null;
	}
}
