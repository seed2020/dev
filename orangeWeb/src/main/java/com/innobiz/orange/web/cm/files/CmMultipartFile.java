package com.innobiz.orange.web.cm.files;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/** MultipartFile */
public class CmMultipartFile extends CommonsMultipartFile {

	/** serialVersionUID */
	private static final long serialVersionUID = 8666674979667785727L;

	/** 저장경로 */
	private String savePath;

	/** 확장자 */
	private String ext;

	/** 생성자 */
	public CmMultipartFile(FileItem fileItem) {
		super(fileItem);
	}

	/** 생성자 */
	public CmMultipartFile(FileItem fileItem, String savePath, String ext) {
		super(fileItem);
		this.savePath = savePath;
		this.ext = ext;
	}

	/** 저장경로 */
	public String getSavePath() {
		return savePath;
	}

	/** 저장경로 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/** 확장자 */
	public String getExt() {
		return ext;
	}
}
