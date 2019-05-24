package com.innobiz.orange.web.cm.utils;

import java.io.File;

/** 파일/디렉토리 삭제 유틸 */
public class FileCleaner {
	
	/** 해당 경로 삭제 */
	public static void delete(String path){
		try {
			deleteDir(new File(path));
		} catch(Exception ignore){}
	}
	
	/** 폴더/파일 삭제 - 하위 폴더의 파일을 삭제하고 자신도 삭제함 */
	private static void deleteDir(File dir){
		if(dir.isFile()) dir.delete();
		else {
			File[] files = dir.listFiles();
			if(files != null){
				for(File file : files){
					if(file.isFile()) file.delete();
					else if(file.isDirectory()){
						deleteDir(file);
					}
				}
			}
			dir.delete();
		}
	}
}
