package com.yanhua.qcloud.video; /**
 * Created by zhiwei.yan on 2017/8/15 0015.
 */

import com.yanhua.qcloud.QcloudApiModuleCenter;
import com.yanhua.qcloud.module.VideoParam;
import com.yanhua.qcloud.module.Vod;
import com.yanhua.qcloud.util.Json.JSONArray;
import com.yanhua.qcloud.util.Json.JSONObject;
import com.yanhua.qcloud.util.PropUtils;
import com.yanhua.qcloud.util.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * @author zhiwei.yan
 * @Description: 本地上传至腾讯云服务
 * @date:2017/8/15 0015 16:58
 */
public class UploadVideo {

	private static final Logger LOG = LoggerFactory.getLogger(UploadVideo.class);

	private static final String SECRETID = PropUtils.getConfig("secretId");

	private static final String SECRETKEY = PropUtils.getConfig("secretKey");

	private static final String DEFAULT_REGION = PropUtils.getConfig("defaultRegion");

	/**
	 * 域名：vod.qcloud.com
	 * 接口名: MultipartUploadVodFile
	 * @Description:本地上传至腾讯云
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/16 0016 8:49
	 */
	public static void saveToCloud (VideoParam uploadParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "POST");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);
		try{
			LOG.info("-------starting...upload");
			String fileName = uploadParam.getFileLocalPath();
			//文件大小
			long fileSize = new File(fileName).length();
			//文件sha1值
			String fileSHA1 = SHA1.fileNameToSHA(fileName);
			//每次上传字节数，可自定义
			int fixDataSize = 1024*1024*5;
			//切片上传：最小片字节数（默认不变）,如果：dataSize + offset > fileSize,把这个值变小即可
			int firstDataSize = 1024*10;
			//
			int tmpDataSize = firstDataSize;
			long remainderSize = fileSize;
			int tmpOffset = 0;
			int code, flag;
			String result;
			if(remainderSize<=0){
				LOG.debug("wrong file path...");
			}
			long startTime = System.currentTimeMillis();
			while (remainderSize>0) {
				TreeMap<String, Object> params = new TreeMap<String, Object>();

				params.put("fileSha", fileSHA1);
				params.put("fileType", uploadParam.getFileType());
				params.put("fileName", uploadParam.getFileName());
				params.put("fileSize", fileSize);
				params.put("dataSize", tmpDataSize);
				params.put("offset", tmpOffset);
				params.put("file", fileName);
				params.put("isTranscode", 0);
				params.put("isScreenshot", 0);
				params.put("isWatermark", 0);

				result = module.call("MultipartUploadVodFile", params);
				LOG.info(result);
				JSONObject json_result = new JSONObject(result);
				code = json_result.getInt("code");
				if (code == -3002) {               //服务器异常返回，需要重试上传(offset=0, dataSize=10K,满足大多数视频的上传)
					tmpDataSize = firstDataSize;
					tmpOffset = 0;
					continue;
				} else if (code != 0) {
					return;
				}
				flag = json_result.getInt("flag");
				if (flag == 1) {
					uploadParam.setFileId(json_result.getString("fileId"));
					break;
				} else {
					tmpOffset = Integer.parseInt(json_result.getString("offset"));
				}
				remainderSize = fileSize - tmpOffset;
				if (fixDataSize < remainderSize) {
					tmpDataSize = fixDataSize;
				} else {
					tmpDataSize = (int) remainderSize;
				}
			}
			LOG.info("-------this video cost time : " + (System.currentTimeMillis() - startTime) + "ms");
			LOG.info("-------upload end...");
//			final File tempVideo = new File(uploadParam.getFileLocalPath());
//			if ()
		}
		catch (Exception e) {
			e.printStackTrace();
			LOG.error("error..."+e.toString());
		}
	}

	/**
	 * 域名：vod.api.qcloud.com
	 * 接口名: DescribeVodPlayUrls
	 * @Description：通过fileId获取视频播放url
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/16 0016 9:15
	 */
	public static String getVideoUrl(VideoParam videoParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "GET");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);

		try{
			String result = null;
			LOG.info("-------starting...DescribeVodPlayUrls");
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("fileId", videoParam.getFileId());

			result = module.call("DescribeVodPlayUrls", params);
			LOG.info(result);
			JSONObject json_result = new JSONObject(result);
			LOG.info("-------end DescribeVodPlayUrls...");
			JSONArray jsonArray = json_result.getJSONArray("playSet");
			JSONObject json_playset = jsonArray.getJSONObject(0);
			return json_playset.getString("url");
		}
		catch (Exception e) {
			LOG.debug("-------DescribeVodPlayUrls error..."+e.toString());
		}
		return null;
	}

	/**
	 * 域名: vod.api.qcloud.com
	 * 接口名: DescribeVodCover
	 * @Description:为视频设置显示封面
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/16 0016 10:23
	 */
//	public static String describeVideo(DescribeVideoParam describeVideoParam) {
//		QcloudApiModuleCenter module = getModule("POST");
//		try{
//			VideoFirstThumbTaker firstThumbTaker = new VideoFirstThumbTaker(PropUtils.getConfig("ffmpeg"));
//			firstThumbTaker.getThumb(describeVideoParam.getVideoLocalPath(), describeVideoParam.getImgLocalPath());
//
//			String result = null;
//			LOG.info("-------starting...");
//			TreeMap<String, Object> params = new TreeMap<String, Object>();
//			params.put("fileId", describeVideoParam.getFileId());
//			params.put("type", describeVideoParam.getType());
//			params.put("para", describeVideoParam.getImgLocalPath());
//			if (describeVideoParam.getType() != 1) {
//				params.put("imageData", FileUtils.encodeBase64File(describeVideoParam.getImgLocalPath()));
//			}
//
//			result = module.call("DescribeVodCover", params);
//			LOG.info(result);
////			JSONObject json_result = new JSONObject(result);
////			int code = json_result.getInt("code");
//			LOG.info("end...");
//			return result;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			LOG.debug("-------error..."+e.toString());
//		}
//		return "";
//	}

	/**
	 * 域名：vod.api.qcloud.com
	 * 接口名: ModifyVodInfo
	 * @Description:修改视频信息
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/16 0016 10:41
	 */
	public static void modifyVideoInfo(VideoParam modifyVideoParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "POST");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);
		try{
			String result;
			LOG.info("-------starting...ModifyVodInfo");
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("fileId", modifyVideoParam.getFileId());
			params.put("fileName", modifyVideoParam.getFileName());
			params.put("fileIntro", modifyVideoParam.getFileIntro());
			//暂不支持
//			params.put("classId", modifyVideoParam.getClassId());

			result = module.call("ModifyVodInfo", params);
			LOG.info(result);
			JSONObject json_result = new JSONObject(result);
			int code = json_result.getInt("code");
			if (code == 0) {

			} else {
				LOG.warn("-------failed to ModifyVodInfo...");
				return;
			}
			LOG.info("-------end...ModifyVodInfo");
		}
		catch (Exception e) {
			LOG.error("-------ModifyVodInfo error..."+e.toString());
		}
	}

	/**
	 * 域名：vod.api.qcloud.com
	 * 接口名: DescribeVodInfo
	 * @Description: 获取视频信息列表
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/16 0016 13:57
	 */
	public static String getVideoList(VideoParam videoListParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "POST");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);
		try{
			String result;
			LOG.info("-------starting...DescribeVodInfo");
			TreeMap<String, Object> params = new TreeMap<String, Object>();
			params.put("pageNo", videoListParam.getPageNo());
			params.put("pageSize", videoListParam.getPageSize());

			result = module.call("DescribeVodInfo", params);
			LOG.info(result);
			JSONObject json_result = new JSONObject(result);
			return result;
		}
		catch (Exception e) {
			LOG.error("-------DescribeVodInfo error..."+e.toString());
		}
		return "";
	}


	/**
	 * ProcessFile
	 * @Description:使用腾讯云进行截图(此方法需要完善，转码部分未添加参数)
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/17 0017 10:48
	 */
	public static void screenshotByApi(VideoParam screenshotParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "POST");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);

		try {
			String result;
			TreeMap<String, Object> config = new TreeMap<>();
			LOG.info("-------starting...ProcessFile");
			config.put("fileId", screenshotParam.getFileId());
			if (screenshotParam.getSampleSnapshot_definition() != null && screenshotParam.getSampleSnapshot_definition().intValue() == 10) {
				config.put("sampleSnapshot.definition", screenshotParam.getSampleSnapshot_definition());
			} else {
				config.put("coverBySnapshot.definition", screenshotParam.getCoverBySnapshot_definition());
				config.put("coverBySnapshot.positionType", screenshotParam.getCoverBySnapshot_positionType());
				config.put("coverBySnapshot.position", screenshotParam.getCoverBySnapshot_position());
			}

			result = module.call("ProcessFile", config);
			LOG.info(result);
			LOG.info("-------end...ProcessFile");
		} catch (Exception e) {
			LOG.debug("-------ProcessFile error: " + e.toString());
		}
	}

	/**
	 * GetVideoInfo
	 * @Description:获取视频的所有信息V2
	 * @param videoParam
	 * @return String
	 * @author zhiwei.yan
	 * @date 2017/8/17 0017 11:12
	 */
	public static void getVideoAllInfo(VideoParam videoParam) {
		TreeMap<String, Object> moduleConfig = new TreeMap<>();
		moduleConfig.put("SecretId", SECRETID);
		moduleConfig.put("SecretKey", SECRETKEY);
		moduleConfig.put("RequestMethod", "GET");
		moduleConfig.put("DefaultRegion", DEFAULT_REGION);
		QcloudApiModuleCenter module =  new QcloudApiModuleCenter(new Vod(), moduleConfig);
		try {
			String result;
			LOG.info("-------starting...GetVideoInfo");
			TreeMap<String, Object> config = new TreeMap<>();

			config.put("fileId", videoParam.getFileId());
			result = module.call("GetVideoInfo", config);
			JSONObject returnInfo = new JSONObject(result);
			if (returnInfo != null && returnInfo.getInt("code") == 0) {
				JSONObject videoBasicInfo = returnInfo.getJSONObject("basicInfo");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final String uploadTime = sdf.format(new Date(videoBasicInfo.getLong("createTime") * 1000));
				videoParam.setUpdateTime(Timestamp.valueOf(uploadTime));
				videoParam.setSourceVideoUrl(videoBasicInfo.getString("sourceVideoUrl"));
				videoParam.setFileType(videoBasicInfo.getString("type"));
			}
			LOG.info(result);
			LOG.info("-------end...GetVideoInfo");
		} catch (Exception e) {
			LOG.debug("-------GetVideoInfo error: " + e.toString());
		}
	}


}
