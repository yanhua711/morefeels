package com.yanhua.service.impl;

import com.yanhua.constant.VideoConstant;
import com.yanhua.dao.IVideoDao;
import com.yanhua.entity.Video;
import com.yanhua.qcloud.module.VideoParam;
import com.yanhua.qcloud.util.FileUtils;
import com.yanhua.qcloud.util.Json.JSONArray;
import com.yanhua.qcloud.util.Json.JSONObject;
import com.yanhua.qcloud.video.UploadVideo;
import com.yanhua.service.IVideoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhiwei.yan
 * @Description: TODO
 * @date:2017-08-19 15:40.
 */
@Service("videoServiceImpl")
public class VideoServiceImpl implements IVideoService {

	private Logger logger = Logger.getLogger(VideoServiceImpl.class);

	@Autowired(required = false)
	private IVideoDao videoDao;

	public Video getVideoById(int videoId) {
		return videoDao.selectByPrimaryKey(videoId);
	}

	/**
	 * @Description:视频上传至腾讯云
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/20 0020 11:34
	 */
	@Override
	public int saveVideoInfo(MultipartFile[] files, VideoParam videoParam) {
		//1.下载至本地临时文件夹
		final String localPath = FileUtils.saveOfFileupload(files, videoParam);
		videoParam.setFileLocalPath(localPath);
		//截图设置
		videoParam.setCoverBySnapshot_definition(10);
		videoParam.setCoverBySnapshot_positionType("Time");
		videoParam.setCoverBySnapshot_position(1);

		//2.上传至腾讯云视频服务
		UploadVideo.saveToCloud(videoParam);
		//3.修改视频信息
		UploadVideo.modifyVideoInfo(videoParam);
		//4.截取视频第一帧设置为封面
		UploadVideo.screenshotByApi(videoParam);
		//5.获取视频信息
		UploadVideo.getVideoAllInfo(videoParam);
		//6.插入数据库
		final Video video = new Video();
		video.setFileid(videoParam.getFileId());
		video.setUploadtime(videoParam.getUpdateTime());
		video.setVideoname(videoParam.getFileName());
		video.setVideodesc(videoParam.getFileIntro());
		video.setVideourl(videoParam.getSourceVideoUrl());
		video.setUserid(videoParam.getUserId());
		return videoDao.saveVideo(video);
	}

	/**
	 * @Description:更新视频信息
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/20 0020 11:59
	 */
	@Override
	public int updateVideoInfo(VideoParam videoParam) {
		return 0;
	}

	@Override
	public List<Video> listVideo(int userId) {
		final List<Video> videoList = videoDao.listVideo(userId);
		JSONArray jsonArray = new JSONArray(videoList.toArray());
		logger.info("-------info : " + jsonArray);
		return videoList;
	}

	/**
	 * @Description:腾讯云回调更新视频封面图片
	 * @param
	 * @return
	 * @author zhiwei.yan
	 * @date 2017/8/20 0020 12:03
	 */
	@Override
	public int updateByFileIdFromCallBack(VideoParam videoParam) {
		final Video video = new Video();
		JSONObject callBackInfo = new JSONObject(videoParam.getCloudCallBackInfo());
		JSONObject callBackData = callBackInfo.getJSONObject("data");
		if (callBackData != null && callBackData.getInt("errCode") == 0) {
			video.setFileid(callBackData.getString("fileId"));
			JSONArray processTaskList = callBackData.getJSONArray("processTaskList");
			for (int i = 0; i < processTaskList.length(); i++) {
				JSONObject processTask = processTaskList.getJSONObject(i);
				if (processTask != null
						&& VideoConstant.VIDEO_TASK_COVERBYSNAPSHOT.equals(processTask.getString("taskType"))
						&& processTask.getInt("errCode") == 0) {
					video.setCoverurl(processTask.getJSONObject("output").getString("imageUrl"));
				}
			}
			return videoDao.updateByFileIdFromCallBack(video);
		}
		return 0;
	}


}
