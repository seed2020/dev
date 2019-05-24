package com.innobiz.orange.web.cm.crypto.rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;

/** RSA 키를 담을 객체 */
public class RsaKeySet {
	
	/** 랜덤 수 만들 객체 */
	private static final SecureRandom RANDOM = new SecureRandom();
	/** Key 파일 갯수 */
	private static final int FILE_COUNT = 64;
	/** Key 파일 내의 Key 수 */
	private static final int KEY_IN_FILE = 1024;
	/** 로드할 Key 갯수 */
	private static final int KEY_SET_SIZE = 128;
	
	/** next 호출 갯수 */
	private int nextCnt = 0;
	
	/** RsaKey를 담는 리스트 */
	private ArrayList<RsaKey> keyList = new ArrayList<RsaKey>();
	
	/** 다음 키를 리턴함 */
	public synchronized RsaKey next(){
		nextCnt++;
		int index = RANDOM.nextInt(keyList.size());
		return keyList.get(index);
	}
	
	/** 해당 키를 리턴함 */
	public RsaKey get(int index){
		return keyList.get(index);
	}
	
	/** 키셋의 리로드가 필요한지 여부 */
	public boolean needReload(){
		return keyList.size() / 2.5 < nextCnt;
	}

	/** 키셋을 로드함 */
	public static RsaKeySet load(int bits) throws IOException{
		return load(bits, RANDOM.nextInt(FILE_COUNT * KEY_IN_FILE / KEY_SET_SIZE));
	}
	
	/** 키셋을 로드함 */
	public static RsaKeySet load(int bits, int keySeq) throws IOException{
		
		RsaKeySet rsaKeySet = new RsaKeySet();
		
		int blockInFile = KEY_IN_FILE / KEY_SET_SIZE;
		int fileNo = keySeq / blockInFile;
		int skipCount = (keySeq % blockInFile) * KEY_SET_SIZE;
		
		int i=0, j=0, p, q;
		String line, keyExt = fileNo<10 ? "00"+fileNo : fileNo<100 ? "0"+fileNo : Integer.toString(fileNo);
		InputStream in = RsaKeySet.class.getResourceAsStream("./k"+bits+"/k"+bits+"."+keyExt);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		try {
			while((line = reader.readLine())!=null){
				
				if(i<skipCount){
					i++;
					continue;
				}
				
				if(j>=128){
					break;
				}
				
				//FOR JDK-1.5
				//if(line.length()==0) continue;
				
				//FOR JDK-1.6 or Higher
				//if(line.isEmpty()) continue;
				
				p = line.indexOf('|');
				q = line.indexOf('|', p+1);
				if(p<0 || q<p) continue;
				
				rsaKeySet.keyList.add(new RsaKey(
						line.substring(0, p),
						line.substring(p+1, q),
						line.substring(q+1) ));
				j++;
			}
		} finally {
			if(in!=null){
				try{ in.close(); } catch(Exception ignore){}
			}
		}
		
		return rsaKeySet;
	}
}
