package com.innobiz.orange.mobile.pt.secu;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/** 브라우저 정보를 담는 객체 */
public class MoBrowser {

	/** Chrome */
	private boolean chrome = false;
	/** customized Chrome */
	private boolean custChrome = false;
	/** Daum */
	private boolean daum = false;
	/** Naver */
	private boolean naver = false;
	/** SAMSUNG */
	private boolean samsung = false;
	/** LG */
	private boolean lg = false;
	/** Mobile */
	private boolean mobile = false;
	/** MS Explore */
	private boolean ie = false;
	/** MS Explore ver 11 or later */
	private boolean ie11 = false;
	/** Opera */
	private boolean opera = false;
	/** Firefox */
	private boolean firefox = false;
	/** Safari */
	private boolean safari = false;
	/** 기타 */
	private boolean unknown = false;
//	/** Tablet */
//	private boolean tablet = false;

	/** 브라우저 정보를 세팅함 */
	public static void setBrowser(HttpServletRequest request){
		String uagnt = request.getHeader("User-Agent");
		MoBrowser browser = new MoBrowser();
		if(uagnt.indexOf("Opera")>=0 || uagnt.indexOf("OPR/")>=0){
			browser.opera = true;
		} else if(uagnt.indexOf("Safari")>=0){
			if(uagnt.indexOf("Chrome")>=0 || uagnt.indexOf("CriOS")>=0){
				String lower = uagnt.toLowerCase();
				if(lower.indexOf("naver")>=0){
					browser.naver = true;
					browser.custChrome = true;
				} else if(lower.indexOf("daum")>=0){
					browser.daum = true;
					browser.custChrome = true;
				} else if(uagnt.indexOf("SAMSUNG")>=0){
					browser.samsung = true;
					browser.custChrome = true;
				} else if(uagnt.indexOf("LG")>=0){
					browser.lg = true;
					browser.custChrome = true;
				} else if(uagnt.indexOf("HUAWEI")>=0 || uagnt.indexOf("HTC")>=0 || uagnt.indexOf("ASUS")>=0 || uagnt.indexOf("Dell")>=0
							|| uagnt.indexOf("BlackBerry")>=0 || uagnt.indexOf("Nokia")>=0|| uagnt.indexOf("Fennec")>=0|| uagnt.indexOf("Haier")>=0){
					browser.custChrome = true;
				} else {
					int p, q, r;
					p = uagnt.indexOf("Android");
					q = uagnt.indexOf("Mobile");
					if(p>0 && q>0){
						browser.custChrome = true;
					} else if(p>0){
						p = uagnt.indexOf(';',p);
						q = uagnt.indexOf("Build");
						if(q>0){
							String s = uagnt.substring(p+1, q).trim();
							if(s.indexOf(';')>0 || s.indexOf(' ')>0){
								browser.custChrome = true;
							}
						}
					}
					
					if(!browser.custChrome){
						p = uagnt.indexOf("Safari");
						r = uagnt.indexOf(' ', p);
						if(r>0){
							browser.custChrome = true;
						}
					}
					if(!browser.custChrome){
						browser.chrome = true;
					}
				}
			} else {
				browser.safari = true;
			}
		} else if(uagnt.indexOf("Opera")>=0 || uagnt.indexOf("OPR/")>=0){
			browser.opera = true;
		} else if(uagnt.indexOf("Firefox")>=0){
			browser.firefox = true;
		} else if(uagnt.indexOf("Trident")>=0){
			browser.ie = true;
			browser.ie11 = true;
		} else if(uagnt.indexOf("MSIE")>=0){
			browser.ie = true;
		} else {
			browser.unknown = true;
		}
		
		browser.mobile = browser.custChrome || isMobile(uagnt);
		request.setAttribute("browser", browser);
	}
	
	public boolean isIe() {
		return ie;
	}

	public boolean isIe11() {
		return ie11;
	}

	public boolean isOpera() {
		return opera;
	}

	public boolean isChrome() {
		return chrome;
	}

	public boolean isCustChrome() {
		return custChrome;
	}

	public boolean isFirefox() {
		return firefox;
	}

	public boolean isSafari() {
		return safari;
	}

	public boolean isUnknown() {
		return unknown;
	}

	public boolean isNaver() {
		return naver;
	}

	public boolean isDaum() {
		return daum;
	}

	public boolean isSamsung() {
		return samsung;
	}

	public boolean isLg() {
		return lg;
	}

	public boolean isMobile() {
		return mobile;
	}
	
	private final static Pattern mobile_b = Pattern.compile("android.+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private final static Pattern mobile_v = Pattern.compile("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private final static Pattern mobile_tablet_b = Pattern.compile("android|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(ad|hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino|playbook|silk", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private final static Pattern mobile_tablet_v = Pattern.compile("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	
	/** 모바일 체크 */
	public static boolean isMobile(String userAgent){
		return userAgent != null && (mobile_b.matcher(userAgent).find() ||
				userAgent.length() >= 4 && mobile_v.matcher(userAgent.substring(0, 4)).find());
	}
	/** 테블릿 체크 */
	public static boolean isMobileOrTablet(String userAgent) {
		return userAgent != null && (mobile_tablet_b.matcher(userAgent).find() ||
				userAgent.length() >= 4 && mobile_tablet_v.matcher(userAgent.substring(0, 4)).find());
	}
}
