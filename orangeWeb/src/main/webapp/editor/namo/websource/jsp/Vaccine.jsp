<%@ page import="java.io.*" %>
<%@ page import="java.util.ArrayList" %>

<%!
public String checkVirusFile(String strFilePath, String strCopyPath, String strVaccinePath)
{
	String strRet = "";
	BufferedReader br = null;
	InputStreamReader isr = null;
	try {
		String[] cmdArray = new String[3];
		String strPath = "";

		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0){
			strPath = strVaccinePath + "\\" + "namo_vse.exe";
		}else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0){
			strPath = strVaccinePath + "/" + "namo_vse";
		}

		File file = new File(strPath);
		if (!file.isFile()) {
			//System.out.println(file.getCanonicalPath().toString());
			return strRet;
		}
		String strCpyFile = strCopyPath + fileNameTimeSetting();
		if (fileCopy(strFilePath,  strCpyFile) != 1) {
			return strRet;
		}
		cmdArray[0] = strPath;
		cmdArray[1] = "--scanFile";
		cmdArray[2] = strCpyFile;
		Process p = Runtime.getRuntime().exec(cmdArray);
		isr = new InputStreamReader(p.getInputStream());
		br = new BufferedReader(isr);
		String line = null;
		ArrayList<String> ar = new ArrayList<String>();
		while((line = br.readLine()) != null){
			ar.add(line);
		}
		int nLen = ar.size();
		if (nLen > 0) {
			for (int cnt = 0; cnt < nLen ; cnt++) {
				String str = ar.get(cnt);
				String[] strArray = str.split(":");
				String name = strArray[0].trim();
				if (name.equalsIgnoreCase("Detection Name")){
					strRet = strArray[1].trim();
				}
			}
		}
		File fileCpy = new File(strCpyFile);
		fileCpy.delete();
	} catch(java.io.IOException e){
		//System.out.println(e);
	}finally{
		try{
			if( br != null){
				br.close();
				br = null;
			}
			if( isr != null){
				isr.close();
				isr = null;
			}	
		}catch(java.io.IOException err1){
			//System.out.println("An internal exception occured!!");
		}
	}
	return strRet;
}

%>
