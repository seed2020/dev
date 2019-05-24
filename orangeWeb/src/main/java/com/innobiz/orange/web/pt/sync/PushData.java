package com.innobiz.orange.web.pt.sync;

public class PushData {

	private boolean hasPush = false;
	
	private boolean comp = false;
	
	private boolean org = false;
	
	private boolean code = false;
	
	private boolean allUser = false;
	
	private StringBuilder someUserBuilder = null;
	
	private StringBuilder delUserUidBuilder = null;
	
	private StringBuilder delRidBuilder = null;
	
	private StringBuilder delEmailBuilder = null;

	public boolean hasData(){
		return hasPush;
	}
	
	public void setComp() {
		hasPush = true;
		this.comp = true;
	}

	public void setOrg() {
		hasPush = true;
		this.org = true;
	}

	public void setCode() {
		hasPush = true;
		this.code = true;
	}

	public void setAllUser() {
		hasPush = true;
		this.allUser = true;
	}

	public void addUserUids(String userUids) {
		if(userUids!=null && !userUids.isEmpty()){
			hasPush = true;
			if(someUserBuilder == null) someUserBuilder = new StringBuilder();
			else someUserBuilder.append(',');
			someUserBuilder.append(userUids);	
		}
	}

	public void addDelUserUids(String delUserUids) {
		if(delUserUids!=null && !delUserUids.isEmpty()){
			hasPush = true;
			if(delUserUidBuilder == null) delUserUidBuilder = new StringBuilder();
			else delUserUidBuilder.append(',');
			delUserUidBuilder.append(delUserUids);	
		}
	}

	public void addDelRids(String delRids) {
		if(delRids!=null && !delRids.isEmpty()){
			hasPush = true;
			if(delRidBuilder == null) delRidBuilder = new StringBuilder();
			else delRidBuilder.append(',');
			delRidBuilder.append(delRids);	
		}
	}

	public void addDelEmails(String delEmails) {
		if(delEmails!=null && !delEmails.isEmpty()){
			hasPush = true;
			if(delEmailBuilder == null) delEmailBuilder = new StringBuilder();
			else delEmailBuilder.append(',');
			delEmailBuilder.append(delEmails);	
		}
	}
	
	public String toJson(String type){
		boolean appended = false;
		StringBuilder builder = new StringBuilder(256);
		builder.append('{');
		
		if(comp){
			if(appended) builder.append(',');
			appended = true;
			append(builder, "comp", "Y");
		}
		
		if(org){
			if(appended) builder.append(',');
			appended = true;
			append(builder, "org", "Y");
		}
		
		if(code){
			if(appended) builder.append(',');
			appended = true;
			append(builder, "code", "Y");
		}
		
		if("email".equals(type)){
			if(delEmailBuilder!=null){
				if(appended) builder.append(',');
				appended = true;
				append(builder, "delUsers", delEmailBuilder.toString());
			}
		} else if("userUid".equals(type)){
			if(delUserUidBuilder!=null){
				if(appended) builder.append(',');
				appended = true;
				append(builder, "delUsers", delUserUidBuilder.toString());
			}
		} else if("rid".equals(type)){
			if(delRidBuilder!=null){
				if(appended) builder.append(',');
				appended = true;
				append(builder, "delUsers", delRidBuilder.toString());
			}
		}
		
		if(allUser){
			if(appended) builder.append(',');
			appended = true;
			append(builder, "allUser", "Y");
		}
		
		if(someUserBuilder!=null){
			if(appended) builder.append(',');
			appended = true;
			append(builder, "someUsers", someUserBuilder.toString());
		}
		
		builder.append('}');
		return appended ? builder.toString() : null;
	}
	
	private void append(StringBuilder builder, String name, String value){
		builder.append('"').append(name).append("\":\"").append(value).append('"');
	}
	
}
