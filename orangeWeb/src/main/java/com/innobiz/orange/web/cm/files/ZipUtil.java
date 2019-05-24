package com.innobiz.orange.web.cm.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.cm.vo.CommonFileVo;

@Component
/** ZIP 압축 처리용 UTIL */
public class ZipUtil {
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ZipUtil.class);

	/** fileVoList를 받아 ZIP 파일 생성 */
	public File makeZipFile(List<? extends CommonFileVo> fileVoList , String fileName) throws IOException {
		//temp 폴더 및 파일 생성
		File file, dir = new File(uploadManager.getTempDir());
		if(!dir.isDirectory()) dir.mkdirs();
		File zipFile = new File(dir, fileName == null ? ApConstant.DOWN_ZIP_FILE_NAME : fileName);
		
		// 중복 파일명을 비교하기 위한 맵
		Map<String,Integer> fileNmMap = new HashMap<String,Integer>();
		ZipOutputStream zos = null;
		try {
			// Charset.defaultCharset(), Charset.forName("UTF-8")
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			byte[] buffer = new byte[1024 * 4];
			int read;
			String dispNm = null;
			int p=0;
			for (CommonFileVo fileVo : fileVoList) {
				dispNm = fileVo.getDispNm();
				if(fileNmMap.containsKey(dispNm)) fileNmMap.put(dispNm, fileNmMap.get(fileVo.getDispNm())+1);
				else fileNmMap.put(dispNm, 0);
				String savePath = fileVo.getSavePath();
				file = new File(savePath);
				FileInputStream fis = new FileInputStream(file);
				if(fileNmMap.get(fileVo.getDispNm())>0) {
					p=dispNm.lastIndexOf(".");
					if(p>0) dispNm = dispNm.substring(0,p);
					dispNm+=" ("+fileNmMap.get(fileVo.getDispNm())+")";
					if(p>0) dispNm+= "."+fileVo.getDispNm().substring(p+1);
				}
				ZipEntry ze = new ZipEntry(dispNm);
				zos.putNextEntry(ze);
				zos.setLevel(9);
				// write
				while ((read = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, read);
				}
				fis.close();
				zos.closeEntry();
			}
			return zipFile;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} finally {
			if (zos != null) try { zos.close(); } catch (Exception e) {}
		}
	}
}
