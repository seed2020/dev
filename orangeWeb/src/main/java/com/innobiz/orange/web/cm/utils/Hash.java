package com.innobiz.orange.web.cm.utils;

public class Hash {

	public static int hashId(String txt){
		return hashId(txt, 3);
	}
	private static int hashId(String txt, int block){
		int start=0, size = txt.length(), len = size<=block ? size : (size+4-block)%4;
		if(len==0) len = 4;
		
		int sum=0, hash = 0, shift=size>10 ? 8 : size-1;
		while(true){
			if(start+block>=size){
				hash = hashId(txt, start, size);
				return sum==0 ? hash : (((sum<<shift)^sum)>>shift)*46656 + hash;
			} else {
				hash = hashId(txt, start, start+len);
				sum = sum==0 ? hash : ((0^((sum>>5)^(sum>>19))^sum)<<21) | hash;
				start += len;
				len = 4;
			}
		}
	}
	private static int hashId(String txt, int start, int end){
		int sum = 0;
		int c;
		for(;start<end;start++){
			c = txt.charAt(start);
			if		(c<'0') throw new IllegalArgumentException(illegalId + " : "+txt);
			else if (c<='9') c -= 48;
			else if (c<'A') throw new IllegalArgumentException(illegalId + " : "+txt);
			else if (c<='Z') c -= 55;
			else throw new IllegalArgumentException(illegalId + " : "+txt);
			sum = (sum * 36) + c;
		}
		return sum;
	}
	
	public static int hashUid(String txt){
		return hashUid(txt, 2);
	}
	private static int hashUid(String txt, int block) {
		int start=0, size = txt.length(), len = size<=block ? size : (size+4-block)%4;
		if(len==0) len = 4;
		
		int sum=0, hash = 0, shift=12, sumShift = 20, resultShift=block*6;
		while(true){
			if(start+block>=size){
				hash = hashUid(txt, start, size);
				return sum==0 ? hash : (((sum<<shift)^sum)>>shift)<<resultShift | hash;
			} else {
				hash = hashUid(txt, start, start+len);
				sum = sum==0 ? hash : ((0^(sum>>sumShift)^sum)<<24) | hash;
				start += len;
				len = 4;
			}
		}
	}
	private static int hashUid(String txt, int start, int end){
		int sum = 0;
		int c;
		for(;start<end;start++){
			c = txt.charAt(start);
			if(c=='.') c = 62;
			else if(c=='_') c = 63;
			else if(c<'0') throw new IllegalArgumentException(illegalUid + " : "+txt);
			else if(c<='9') c -= 48;
			else if(c<'A') throw new IllegalArgumentException(illegalUid + " : "+txt);
			else if(c<='Z') c -= 55;
			else if(c<'a') throw new IllegalArgumentException(illegalUid + " : "+txt);
			else if(c<='z') c -= 61;
			else throw new IllegalArgumentException(illegalUid + " : "+txt);
			sum = (sum<<6) + c;
		}
		return sum;
	}
	
	public static long hashLongUid(String txt){
		int start=0, size = txt.length(), block=7, len = size % block;
		if(len==0) len = block;
		
		int sum=0, hash=0;
		while(true){
			if(start+block>=size){
				hash = hashUid(txt.substring(start, size));
				if(sum==0) return hash;
				return (((long)sum)<<32) + ((long)hash);
			} else {
				hash = hashUid(txt.substring(start, start+len));
				sum = sum==0 ? hash : 0 ^ sum ^ hash;
				start += len;
				len = block;
			}
		}
	}
	
	private final static String illegalId = "id should be [0-9][A-Z]";
	private final static String illegalUid = "userUid/odurUid should be [0-9][A-Z][a-z]";
}
