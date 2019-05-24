package com.innobiz.orange.web.cm.utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Component;

import com.innobiz.orange.web.cm.config.ServerConfig;

/** 
 * 파일 변환
 */
@Component
public class ConvertUtil {
	private static final String SN3HCV_HOME = ServerConfig.IS_LINUX ? "/app/synap/sn3hcv/" : "d:/app/synap/sn3hcv/";

    private static final String SN3HCV = SN3HCV_HOME + (ServerConfig.IS_LINUX ? "sn3hcv_exe" : "sn3hcv.exe");

    private static final String TEMPLATE = SN3HCV_HOME + "template";

    private static final String MODULES = SN3HCV_HOME + "modules";
    
	/** 
     * 변환호출
     * inputFile : 변환대상 파일의 절대경로
     * outputPath : 변환된 HTML 파일 저장경로
     * resultName : 생성할 HTML 파일명 (ex; sample => sample.htm 파일 생성됨)
     * return : 0 (변환성공) 0이외의 값 (변환실패)
     */
    public int convertToHtml(String inputFile, String outputPath, String resultName, String title, String downUrl) {
        // 기존 변환결과 존재여부 확인
        File htmlFile = new File(outputPath + "/" + resultName + ".xml");
        // 기존 변환결과가 존재하지 않을 경우 변환실행
        if(!htmlFile.exists()) {
            File fileDir = null;
            for(String dir : new String[]{SN3HCV_HOME, TEMPLATE, MODULES}){
            	fileDir = new File(dir);        	
    			if(!fileDir.isDirectory()){
    				return 1;
    			}
            }
        	int arrayCnt = 8;
        	if(title!= null) arrayCnt+=2;
        	if(downUrl!= null) arrayCnt+=2;
        	String[] cmd = new String[arrayCnt];
        	int i=0;
        	for(String val : new String[]{SN3HCV, "-t", TEMPLATE, "-mod_path", MODULES}){
        		cmd[i] = val;
        		i++;
        	}
        	if(title!=null){
        		cmd[i++] = "-title";
        		cmd[i++] = "\""+title+"\"";
        	}
        	if(downUrl!=null){
        		cmd[i++] = "-down";
        		cmd[i++] = downUrl;
        	}
        	for(String val : new String[]{inputFile, outputPath, resultName}){
        		cmd[i++] = val;
        	}
        	/*for(String txt : cmd){
        		System.out.println("txt : "+txt);
        	}*/
            try {
                Timer t = new Timer();
                Process proc = Runtime.getRuntime().exec(cmd);
                TimerTask killer = new TimeoutProcessKiller(proc);
                t.schedule(killer, 60000); // 20초 (변환이 20초 안에 완료되지 않으면 프로세스 종료)
                int exitValue = proc.waitFor();
                killer.cancel();
                return exitValue;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        else {
            return 0;   // 기존 변환결과가 존재함. 정상 변환으로 처리
        }
    }
    
    // 저장경로에 연월일 폴더 생성[파일의 등록일자 YYYYMMDD]
    public String makeDir(String prefixDir, String dir, String day) {
    	if(prefixDir!=null && !prefixDir.isEmpty()) dir+=File.separator+prefixDir;
    	dir = dir.replace('\\', '/');
		//SimpleDateFormat fmt = new SimpleDateFormat("/yyyyMMdd");
		day = day.replaceAll("[-: ]", "");
		//String dateString = fmt.format(new Date(System.currentTimeMillis()));
		try{
			String dateString = File.separator+day.substring(0,8);
			File targetDir = new File(dir + dateString);
			
	        if(!targetDir.exists()) {
	            targetDir.mkdirs();
	        }
	        return prefixDir+dateString;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
        //return targetDir.getAbsolutePath();
    }
    
    private class TimeoutProcessKiller extends TimerTask {
        private Process p;
        public TimeoutProcessKiller(Process p) {
            this.p = p;
        }
        @Override
        public void run() {
            p.destroy();
        }
    }
}
