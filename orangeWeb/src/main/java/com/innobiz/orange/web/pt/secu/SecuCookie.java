package com.innobiz.orange.web.pt.secu;

import java.io.Serializable;
import java.math.BigInteger;

import com.innobiz.orange.web.cm.crypto.rsa.RsaKey;
import com.innobiz.orange.web.cm.utils.StringUtil;

public class SecuCookie implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3456547161847625176L;

	private String secuCode = null;
	
	private BigInteger priKey = null;
	
	private BigInteger modKey = null;
	
	private int blockSize = 0;
	
	private long timeGap = 0;
	
	public SecuCookie(RsaKey rsaKey, long createTime){
		secuCode = StringUtil.getNextIntHexa();
		priKey = new BigInteger(rsaKey.getPrivateKey(), 16);
		modKey = new BigInteger(rsaKey.getModulus(), 16);
		blockSize = rsaKey.getBlockSize();
		timeGap = System.currentTimeMillis() - createTime;
	}

	public String getSecuCode() {
		return secuCode;
	}

	public BigInteger getPriKey() {
		return priKey;
	}

	public BigInteger getModKey() {
		return modKey;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public long getTimeGap() {
		return timeGap;
	}
	
	public String toString(){
		return timeGap+"|"+secuCode+"|"+modKey.toString(16)+"|"+priKey.toString(16);
	}
}
