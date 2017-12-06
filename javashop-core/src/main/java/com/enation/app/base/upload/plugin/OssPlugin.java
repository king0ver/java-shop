package com.enation.app.base.upload.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.enation.app.base.core.model.ConfigItem;

import com.enation.app.base.upload.service.AbstractUploadPlugin;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

/**
 * 阿里云oss文件上传插件
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日下午8:03:16
 *
 */
@SuppressWarnings("unchecked")
@Component
public class OssPlugin extends AbstractUploadPlugin implements IUploader {


	protected static final Log LOGER = LogFactory.getLog(OssPlugin.class);

	@Override
	public List<ConfigItem> definitionConfigItem() {
		List<ConfigItem> list = new ArrayList();

		ConfigItem endPoint = new ConfigItem();
		endPoint.setType("text");
		endPoint.setName("endpoint");
		endPoint.setText("域名");

		ConfigItem buketName = new ConfigItem();
		buketName.setType("text");
		buketName.setName("bucketName");
		buketName.setText("储存空间");

		ConfigItem picLocation = new ConfigItem();
		picLocation.setType("text");
		picLocation.setName("picLocation");
		picLocation.setText("二级路径");

		ConfigItem accessKeyId = new ConfigItem();
		accessKeyId.setType("text");
		accessKeyId.setName("accessKeyId");
		accessKeyId.setText("密钥id");

		ConfigItem accessKeySecret = new ConfigItem();
		accessKeySecret.setType("text");
		accessKeySecret.setName("accessKeySecret");
		accessKeySecret.setText("密钥");

		list.add(endPoint);
		list.add(buketName);
		list.add(picLocation);
		list.add(accessKeyId);
		list.add(accessKeySecret);

		return list;
	}

	@Override
	public String getPluginId() {
		return "ossPlugin";
	}

	@Override
	public String upload(MultipartFile stream) {
		/** 获取oss存储配置信息 */
		Map<String, String> params = this.getConfig();
		String endpoint = StringUtil.toString(params.get("endpoint"));
		String bucketName = StringUtil.toString(params.get("bucketName"));
		String picLocation = StringUtil.toString(params.get("picLocation"));
		String accessKeyId = StringUtil.toString(params.get("accessKeyId"));
		String accessKeySecret = StringUtil.toString(params.get("accessKeySecret"));
		/** 获取文件名词 */
		String name = stream.getOriginalFilename();
		/** 获取文件后缀 */
		String ext = FileUtil.getFileExt(name);
		/** 将文件MultipartFile转为InputStream */
		InputStream input = null;
		try {
			input = stream.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 拼接文件的文件名 */
		String fileName = picLocation + UUID.randomUUID().toString().toUpperCase().replace("-", "") + "." + ext; // 文件名，根据UUID来
		return putObject(input, ext, fileName, endpoint, bucketName, picLocation, accessKeyId, accessKeySecret);
	}

	@Override
	public void deleteFile(String filePath) {
		/** 获取oss存储配置信息 */
		Map<String, String> params = this.getConfig();
		String endpoint = StringUtil.toString(params.get("endpoint"));
		String accessKeyId = StringUtil.toString(params.get("accessKeyId"));
		String accessKeySecret = StringUtil.toString(params.get("accessKeySecret"));

		this.delete(filePath, endpoint, accessKeyId, accessKeySecret);
	}

	/**
	 * 删除上传文件
	 * 
	 * @param filePath
	 *            删除文件全路径
	 * @param endpoint
	 *            储存空间名称
	 * @param accessKeyId
	 *            密钥id
	 * @param accessKeySecret
	 *            密钥
	 * @return
	 */
	public boolean delete(String filePath, String endpoint, String accessKeyId, String accessKeySecret) {
		String bucketNames = this.getBucketName(filePath); // 根据url获取bucketName
		String fileName = this.getFileName(filePath); // 根据url获取fileName
		if (bucketNames == null || fileName == null)
			return false;
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			GenericRequest request = new DeleteObjectsRequest(bucketNames).withKey(fileName);
			ossClient.deleteObject(request);
		} catch (Exception oe) {
			oe.printStackTrace();
			return false;
		} finally {
			ossClient.shutdown();
		}
		return true;
	}

	/**
	 * 上传图片
	 * 
	 * @param input
	 *            上传图片文件的输入流
	 * @param fileType
	 *            文件类型，也就是后缀
	 * @param fileName
	 *            文件名称
	 * @param endpoint
	 *            域名
	 * @param bucketName
	 *            储存空间名称
	 * @param picLocation
	 *            二级路径名称
	 * @param accessKeyId
	 *            密钥id
	 * @param accessKeySecret
	 *            密钥
	 * @return 访问图片的url路径
	 */
	private String putObject(InputStream input, String fileType, String fileName, String endpoint, String bucketName,
			String picLocation, String accessKeyId, String accessKeySecret) {
		String urls = null; // 默认null
		String saveUrl = null; // 最终返回的路径
		OSSClient ossClient = null;
		try {
			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			ObjectMetadata meta = new ObjectMetadata(); // 创建上传Object的Metadata
			meta.setContentType(this.contentType(fileType)); // 设置上传内容类型
			meta.setCacheControl("no-cache"); // 被下载时网页的缓存行为
			PutObjectRequest request = new PutObjectRequest(bucketName, fileName, input, meta); // 创建上传请求
			ossClient.putObject(request);
			/** 设置Object权限 */
			boolean found = ossClient.doesObjectExist(bucketName, fileName);
			if (found = true) {
				ossClient.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
			}
			Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
			URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
			/** 对返回的签名url处理获取最终展示用的url */
			urls = url.toString();
			String[] strs = urls.split("\\?");
			for (int i = 0, len = strs.length; i < len; i++) {
				saveUrl = strs[0].toString();
			}
			LOGER.info("OSS上传成功的地址" + saveUrl);
		} catch (OSSException oe) {
			LOGER.error("OSSException异常");
			oe.printStackTrace();
			return null;
		} catch (ClientException ce) {
			LOGER.error("ClientException异常");
			ce.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ossClient.shutdown();
		}
		return saveUrl;
	}

	/**
	 * 根据url获取bucketName
	 * 
	 * @param fileUrl
	 *            文件url
	 * @return String bucketName 域名
	 */
	private static String getBucketName(String fileUrl) {
		String http = "http://";
		String https = "https://";
		int httpIndex = fileUrl.indexOf(http);
		int httpsIndex = fileUrl.indexOf(https);
		int startIndex = 0;
		if (httpIndex == -1) {
			if (httpsIndex == -1) {
				return null;
			} else {
				startIndex = httpsIndex + https.length();
			}
		} else {
			startIndex = httpIndex + http.length();
		}
		int endIndex = fileUrl.indexOf(".oss-");
		return fileUrl.substring(startIndex, endIndex);
	}

	/**
	 * 根据url获取fileName
	 * 
	 * @param fileUrl
	 *            文件url
	 * @return String fileName 文件名称
	 */
	private static String getFileName(String fileUrl) {
		String str = "aliyuncs.com/";
		int beginIndex = fileUrl.indexOf(str);
		if (beginIndex == -1)
			return null;
		return fileUrl.substring(beginIndex + str.length());
	}

	/**
	 * 获取文件类型
	 * 
	 * @param FileType
	 *            文件后缀
	 * @return String 文件后缀需要在请求存储时进行转换
	 */
	private static String contentType(String fileType) {
		fileType = fileType.toLowerCase();
		String contentType = "";
		switch (fileType) {
		case "bmp":
			contentType = "image/bmp";
			break;
		case "gif":
			contentType = "image/gif";
			break;
		case "png":
		case "jpeg":
		case "jpg":
			contentType = "image/jpeg";
			break;
		case "html":
			contentType = "text/html";
			break;
		case "txt":
			contentType = "text/plain";
			break;
		case "vsd":
			contentType = "application/vnd.visio";
			break;
		case "ppt":
		case "pptx":
			contentType = "application/vnd.ms-powerpoint";
			break;
		case "doc":
		case "docx":
			contentType = "application/msword";
			break;
		case "xml":
			contentType = "text/xml";
			break;
		case "mp4":
			contentType = "video/mp4";
			break;
		default:
			contentType = "application/octet-stream";
			break;
		}
		return contentType;
	}

	@Override
	public String getThumbnailUrl(String url, Integer width, Integer height) {
		/** 缩略图全路径 */
		String thumbnailPah = url + "_" + width + "x" + height;
		/** 返回缩略图全路径 */
		return thumbnailPah;
	}

	@Override
	public String getPluginName() {
		return "oss云存储";
	}

	@Override
	public Integer getIsOpen() {
		return 0;
	}

}
