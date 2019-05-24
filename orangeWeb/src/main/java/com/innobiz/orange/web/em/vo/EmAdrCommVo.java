package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 대표지번전체(PT_ZIPCD_B_TEMP) 테이블 VO
 */
@SuppressWarnings("serial")
public class EmAdrCommVo extends CommonVoImpl {	
	/** 추가 */
	private String label;
	private String value;
	
 	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
