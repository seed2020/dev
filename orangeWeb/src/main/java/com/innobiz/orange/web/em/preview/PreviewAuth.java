package com.innobiz.orange.web.em.preview;

import com.innobiz.orange.web.pt.secu.CRC32;

public class PreviewAuth {

	private String userUid;
	private String fileId;
	
	public PreviewAuth(String userUid, String fileId){
		this.userUid = userUid;
		this.fileId = fileId;
	}
	
	@Override
	public int hashCode() {
		return CRC32.hash((userUid+fileId).getBytes());
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		if(!this.getClass().equals(obj.getClass())) return false;
		
		return this.hashCode() == obj.hashCode();
	}
}
