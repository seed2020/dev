package com.innobiz.orange.web.cm.crypto.seed;

/**
 * 함호화용 Padding : ANSI X.923
 * 
 */
public class AnsiX923Padding implements CryptoPadding {
	
	/** 패딩 규칙 이름 */
	private String name = "ANSI-X.923-Padding";
	
	private final byte PADDING_VALUE = 0x00;
	
	public byte[] addPadding(byte[] source, int blockSize) {
		int paddingCnt = source.length % blockSize;
		byte[] paddingResult = null;
		
		if(paddingCnt != 0) {
			paddingResult = new byte[source.length + (blockSize - paddingCnt)];
			System.arraycopy(source, 0, paddingResult, 0, source.length);
			
			int addPaddingCnt = blockSize - paddingCnt;
			for(int i=0;i<addPaddingCnt;i++) {
				paddingResult[source.length + i] = PADDING_VALUE;
			}
		} else {
			paddingResult = source;
		}
		return paddingResult;
	}

	public byte[] removePadding(byte[] source, int blockSize) {
		int paddingCount = 0;
		for(int i=0; i<blockSize; i++){
			if(source[source.length - 1 -i] != PADDING_VALUE){
				paddingCount = i;
				break;
			}
		}
		
		if(paddingCount>0){
			byte[] paddingResult = new byte[source.length - paddingCount];
			System.arraycopy(source, 0, paddingResult, 0, paddingResult.length);
			return paddingResult;
		} else {
			return source;
		}
	}
	
	public String getName() {
		return name;
	}
	
}
