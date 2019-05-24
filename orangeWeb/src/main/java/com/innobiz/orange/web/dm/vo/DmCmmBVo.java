package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.dm.utils.DmConstant;

/****** Object:  Vo - Date: 2015/06/12 10:11 ******/
/**
* 저장소 테이블 공통 VO 
*/
@SuppressWarnings("serial")
public class DmCmmBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;
	
	/** 테이블 prefix */ 
	private String prefix;
	
	/** 테이블명 */
	private String tableName;
	
	/** 생성자 */
	public DmCmmBVo(){
	}
	
	/** 생성자[저장소ID가 있으면 테이블 Prefix를 삽입해준다.] */
	public DmCmmBVo(String storId) {
		if(storId != null) {
			this.prefix = "";
			this.storId = storId;
		}else {
			this.prefix = DmConstant.COMM_PREFIX;
		}
	}
	
 	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	/** 테이블 prefix */ 
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/** 테이블명 */
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":공통]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(prefix!=null) { if(tab!=null) builder.append(tab); builder.append("prefix(테이블Prefix):").append(prefix).append('\n'); }
		super.toString(builder, tab);
	}

}
