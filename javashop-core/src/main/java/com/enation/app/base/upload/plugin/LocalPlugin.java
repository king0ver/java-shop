package com.enation.app.base.upload.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.base.upload.service.AbstractUploadPlugin;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;

/**
 * 本地上传插件
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日下午7:59:23
 *
 */
@SuppressWarnings("unchecked")
@Component
public class LocalPlugin extends AbstractUploadPlugin implements IUploader {
	
	@Override
	public List<ConfigItem> definitionConfigItem() {
		List<ConfigItem> list = new ArrayList<>();
		ConfigItem nginxOpen = new ConfigItem();
		nginxOpen.setType("radio");
		nginxOpen.setName("nginx_open");
		nginxOpen.setText("nginx支持");
		ConfigItem resourceUrl = new ConfigItem();
		resourceUrl.setType("text");
		resourceUrl.setName("static_server_domain");
		resourceUrl.setText("域名");
		ConfigItem serviceUrl = new ConfigItem();
		serviceUrl.setType("text");
		serviceUrl.setName("static_server_path");
		serviceUrl.setText("路径");
		list.add(nginxOpen);
		list.add(resourceUrl);
		list.add(serviceUrl);
		return list;
	}

	@Override
	public String getPluginId() {
		return "localPlugin";
	}

	/**
	 * 删除本地图片
	 * 
	 * @param filePath
	 *            文件全路径
	 */
	@Override
	public void deleteFile(String filePath) {
		FileUtil.delete(filePath);
		FileUtil.delete(StaticResourcesUtil.getThumbPath(filePath, "_thumbnail"));
	}

	/** 获取时间 */
	private String getTimePath() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int date = now.get(Calendar.DAY_OF_MONTH);
		int minute = now.get(Calendar.HOUR_OF_DAY);
		String filePath = "";
		if (year != 0) {
			filePath += year + "/";
		}
		if (month != 0) {
			filePath += month + "/";
		}
		if (date != 0) {
			filePath += date + "/";
		}
		if (minute != 0) {
			filePath += minute + "/";
		}
		return filePath;
	}

	@Override
	public String upload(MultipartFile stream) {
		/** 获取数据库中对应参数 */
		Map<String, String> params = this.getConfig();
		/** nginx是否开启 */
		String nginx_is_open = StringUtil.toString(params.get("nginx_open"), false);
		/** 域名 */
		String static_server_domain = StringUtil.toString(params.get("static_server_domain"));
		/** 文件路径 */
		String static_server_path = StringUtil.toString(params.get("static_server_path"));
		/**
		 * 参数校验
		 */
		if (stream == null) {
			throw new IllegalArgumentException("file or filename object is null");
		}

		/** 获取文件名词 */
		String fileName = stream.getOriginalFilename();
		/** 获取文件后缀 */
		String ext = FileUtil.getFileExt(fileName);
		/** 拼接文件名 */
		fileName = DateUtil.toString(new Date(), "mmss") + StringUtil.getRandStr(4) + "." + ext;
		/** 返回浏览器路径 */
		String path = null;
		/** 入库路径 */
		String filePath = null;
		/** 拼接路径 */
		if (nginx_is_open.equals("1")) {
			path = static_server_domain + "/";
			filePath = static_server_path + "/";
		} else {
			path = RequestUtil.getDomain() + "/statics/attachment/";
			filePath = StringUtil.getRootPath() + "/statics/attachment/";
		}
		/** 获取当前时间 */
		String timePath = this.getTimePath();
		/** 拼接返回浏览器路径 */
		path += timePath + fileName;
		/** 拼接入库路径及文件名 */
		filePath += timePath;
		filePath += fileName;
		/** 写入文件 */
		FileUtil.write(stream, filePath);
		/** 返回浏览器 */
		return path;
	}

	/**
	 * 生成缩略图全路径 本地存储缩略图格式为 原图片名称_宽x高.原图片名称后缀
	 * 如原图路径为/User/2017/03/04/original.jpg，需要生成100x100的图片 则缩略图全路径为
	 * /User/2017/03/04/original.jpg_100x100.jpg
	 * 
	 */
	@Override
	public String getThumbnailUrl(String url, Integer width, Integer height) {
		/** 截图原图后缀 */
		String suffix = url.substring(url.lastIndexOf("."), url.length());
		/** 缩略图全路径 */
		String thumbnailPah = url + "_" + width + "x" + height + suffix;
		/** 返回缩略图全路径 */
		return thumbnailPah;

	}

	@Override
	public String getPluginName() {
		return "本地存储";
	}

	@Override
	public Integer getIsOpen() {
		return 1;
	}

}
