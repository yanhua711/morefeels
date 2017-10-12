package com.yanhua.service;/**
 * Created by zhiwei.yan on 2017/8/19 0019.
 */

import com.yanhua.entity.Video;
import com.yanhua.qcloud.module.VideoParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhiwei.yan
 * @Description: TODO
 * @date:2017-08-19 15:39.
 */
public interface IVideoService {

	Video getVideoById(int id);

	int saveVideoInfo(MultipartFile[] files, VideoParam videoParam);

	int updateVideoInfo(VideoParam videoParam);

	List<Video> listVideo(int userId);

	int updateByFileIdFromCallBack(VideoParam videoParam);
}
