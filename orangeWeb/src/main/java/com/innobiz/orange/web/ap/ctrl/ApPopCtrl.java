package com.innobiz.orange.web.ap.ctrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 팝업 컨트롤러 - 결재 */
@Controller
public class ApPopCtrl {

//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(ApPopCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;

	/** [팝업] 사진,도장,사인 - 업로드용 팝업 */
	@RequestMapping(value = "/ap/{path}/setImagePop")
	public String setImagePop(HttpServletRequest request,
					ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/pop/setImagePop");
	}
	
	/** [히든프레임] 사진,도장,사인 업로드 */
	@RequestMapping(value = "/ap/{path}/transImage")
	public String transImage(HttpServletRequest request,
			@Parameter(name="side", required=false) String side,
			@Parameter(name="maxSize", required=false) String maxSize,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "ap");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			
			// 업로드 경로
			String userImgTypCd = paramMap.get("userImgTypCd");// 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
			String path = 
				"01".equals(userImgTypCd) ? "images/upload/or/stamp" :
					"02".equals(userImgTypCd) ? "images/upload/or/sign" :
						"03".equals(userImgTypCd) ? "images/upload/or/photo" :
							"images/upload/or/org";
							
			DistHandler distHandler = distManager.createHandler(path, locale);//업로드 경로 설정
			String distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("photo"));// file-tag 의 name
			distHandler.distribute();
			
			QueryQueue queryQueue = new QueryQueue();
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			VoUtil.fromMap(orUserImgDVo, paramMap);
			
			File imgFile = fileMap.get("photo");
			if(fileMap.get("photo")!=null){
				BufferedImage bimg = ImageIO.read(imgFile);
				orUserImgDVo.setImgWdth(Integer.toString(bimg.getWidth()));
				orUserImgDVo.setImgHght(Integer.toString(bimg.getHeight()));
				orUserImgDVo.setImgPath(distPath);
			}
			
			UserVo userVo = LoginSession.getUser(request);
			orUserImgDVo.setModrUid(userVo.getUserUid());
			orUserImgDVo.setModDt("sysdate");
			queryQueue.store(orUserImgDVo);
			
			commonSvc.execute(queryQueue);
			
			String extraParam = "";
			if(side!=null || maxSize!=null){
				if("height".equals(side)){
					extraParam = ", 'height', " + Math.min(Integer.parseInt(orUserImgDVo.getImgHght()), Integer.parseInt(maxSize));
				} else if("width".equals(side)){
					extraParam = ", 'width', " + Math.min(Integer.parseInt(orUserImgDVo.getImgWdth()), Integer.parseInt(maxSize));
				}
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.setUserImage('"+userVo.getUserUid()+"', '"+orUserImgDVo.getUserImgTypCd()+"', '"+request.getAttribute("_cxPth")+distPath+"'"+extraParam+");");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
	
		return LayoutUtil.getResultJsp();
	}
	
	
}
