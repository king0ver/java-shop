package com.enation.eop.processor.facade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.tools.ant.taskdefs.Sleep;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.base.core.service.impl.SettingService;
import com.enation.eop.IEopProcessor;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.image.IThumbnailCreator;
import com.enation.framework.image.ThumbnailCreatorFactory;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 本地存储缩略图处理
 * 
 * @author zh
 * @version v1.0
 * @since v6.4.0 2017年8月16日 下午6:18:29
 */
public class ImageThumbnaiProcessor implements IEopProcessor {

	@Override
	public boolean process() {
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String path = httpRequest.getServletPath();
		/** 获取当前的图片名称 */
		String imgName = path.substring(path.lastIndexOf("/") + 1);
		// ****.jpg_111*111.jpg
		String reg = "(?i).+?\\.(jpg|gif|png|swf|jpeg)_\\d{1,4}+(x|\\*)\\d{1,4}+\\.(jpg|gif|png|swf|jpeg)";
		/** 判断是否为缩略图 */
		boolean flag = imgName.matches(reg);
		if (flag) {
			/** 访问本地路径，查看是否有缩略图 */
			String filePath = StringUtil.getRootPath() + path;
			File file = new File(filePath);

			if (file.exists()) {
				return false;
			} else {
				/** 没有找到此图片 需要在本地路径下查找原图 */
				String sourcePath = StringUtil.getRootPath() + path.replace(path.substring(path.lastIndexOf("_")), "");
				/** 获取图片宽高 */
				String exc = FileUtil.getFileExt(path);
				String otherPath = path.substring(path.lastIndexOf("_") + 1);
				otherPath = otherPath.replace("." + exc, "");
				String[] split;
				if (otherPath.indexOf("*") > 0) {
					split = otherPath.split("\\*");
				} else {
					split = otherPath.split("x");
				}

				Integer width = Integer.parseInt(split[0]);
				Integer height = Integer.parseInt(split[1]);
				/** 检测是否符合生成缩略图的尺寸 */
				if(check(width.toString(), height.toString())) {
					/** 生成缩略图 */
					IThumbnailCreator thumbnailCreator = ThumbnailCreatorFactory.getCreator(sourcePath, filePath);
					thumbnailCreator.resize(width, height);
					/** 将此文件输出 */
					this.write(file);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验是否匹配宽高
	 * @param width 宽度
	 * @param height 高度
	 * @return 是否匹配 
	 */
	private boolean check(String width,String height) {
		ISettingService settingService = SpringContextHolder.getBean("settingService");
		Map<String, String> setting = settingService.getSetting("photo"); // 这行报空指针异常
		//图片匹配
		if (setting.get("tiny_pic_width").equals(width)&&setting.get("tiny_pic_height").equals(height)) {
			return true;
		}if(setting.get("small_pic_width").equals(width)&&setting.get("small_pic_height").equals(height)) {
			return true;
		}if(setting.get("big_pic_width").equals(width)&&setting.get("big_pic_height").equals(height)) {
			return true;
		}if(setting.get("thumbnail_pic_width").equals(width)&&setting.get("thumbnail_pic_height").equals(height)) {
			return true;
		}
		return false;
	}
	/**
	 * 写入文件流
	 * @param file	文件file
	 */
	private void write(File file) {
		OutputStream outputStream = null;
		BufferedInputStream bufferedInputStream =null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);  
			bufferedInputStream = new BufferedInputStream( fileInputStream);  
			byte[] b = new byte[bufferedInputStream.available()];  
			bufferedInputStream.read(b);  
			outputStream = ThreadContextHolder.getHttpResponse().getOutputStream();  
			outputStream.write(b);  
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(outputStream != null) {
					outputStream.flush();  
					outputStream.close(); 
				}
				if(bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();  
			}

		}


	}

}
