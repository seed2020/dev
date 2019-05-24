package com.innobiz.orange.web.cm.crypto.rsa;

/** RSA 키를 담을 객체 */
public class RsaKey {
	
	/** Public Key */
	private String publicKey;
	
	/** Private Key */
	private String privateKey;
	
	/** Modulus */
	private String modulus;
	
	/** 생성자 */
	public RsaKey(String publicKey, String privateKey, String modulus) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.modulus = modulus;
	}

	/** Public Key */
	public String getPublicKey() {
		return publicKey;
	}

	/** Private Key */
	public String getPrivateKey() {
		return privateKey;
	}

	/** Modulus */
	public String getModulus() {
		return modulus;
	}
	
	/** 한번에 암/복호화 할 수 있는 바이트 수 */
	public int getBlockSize() {
		return (Math.round(modulus.length() / 32F) * 16);
	}
}
