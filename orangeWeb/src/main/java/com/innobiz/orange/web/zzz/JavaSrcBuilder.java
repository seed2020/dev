package com.innobiz.orange.web.zzz;

public class JavaSrcBuilder {

	public static final char ZERO = 0;
	
	boolean lineHasNoChar = true;
	
	boolean leadingSpaceAdded = false;
	private StringBuilder leadingSpace = new StringBuilder(32);
	
	private StringBuilder builder = null;
	
	
	public JavaSrcBuilder(int size){
		builder = new StringBuilder(size+1);
	}
	
	public JavaSrcBuilder append(char c){
		
		if(lineHasNoChar){
			if(c==' ' || c=='\t'){
				addLineSpace(c);
			} else {
				flushLineSpace();
				builder.append(c);
				if(c=='\r' || c=='\n'){
					lineHasNoChar = true;
				} else {
					lineHasNoChar = false;
				}
			}
		} else if(c=='\r' || c=='\n'){
			flushLineSpace();
			lineHasNoChar = true;
			builder.append(c);
		} else {
			builder.append(c);
		}
		
		return this;
	}
	
	public void appendIfHasChar(char c1, char c2){
		if(lineHasNoChar){
			discardLineSpace();
		} else {
			builder.append(c1);
			if(c2!=ZERO) builder.append(c2);
		}
	}
	
	public String toString(){
		flushLineSpace();
		return builder.toString();
	}
	
	private void addLineSpace(char c){
		leadingSpaceAdded = true;
		leadingSpace.append(c);
	}
	
	private void flushLineSpace(){
		if(leadingSpaceAdded){
			builder.append(leadingSpace);
			leadingSpaceAdded = false;
			leadingSpace.delete(0, leadingSpace.length());
		}
	}
	
	private void discardLineSpace(){
		leadingSpaceAdded = false;
		leadingSpace.delete(0, leadingSpace.length());
	}
	
	public void rtrimLn(){
		char c;
		for(int i = builder.length() - 1; i>=0; i--){
			c = builder.charAt(i);
			if(c==' ' || c=='\t'){
				builder.deleteCharAt(i);
			} else {
				break;
			}
		}
	}
}
