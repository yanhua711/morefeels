package com.yanhua.controller;

import com.yanhua.constant.VideoConstant;
import com.yanhua.qcloud.module.VideoParam;
import com.yanhua.qcloud.util.FileUtils;
import com.yanhua.qcloud.util.Json.JSONObject;
import com.yanhua.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author zhiwei.yan
 * @Description: TODO
 * @date:2017-08-19 17:21.
 */
@Controller
public class VideoController extends BaseController {

	@Autowired(required = false)
	private IVideoService videoService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String indexVO() {
		return "index";
	}

	@RequestMapping(value = "uploadVideoView", method = RequestMethod.GET)
	public String uploadVideoVO() {
		return "/video/uploadVideo";
	}

	@RequestMapping(value = "saveVideo", method = RequestMethod.POST)
	@ResponseBody
	public String saveToCloud(@RequestParam MultipartFile[] files, VideoParam videoParam) {
		videoParam.setFileLocalPath(FileUtils.saveOfFileupload(files, videoParam));
		JSONObject json = new JSONObject();
		json.put("code", videoService.saveVideoInfo(files, videoParam));
		return json.toString();
	}

	@RequestMapping(value = "cloudCallBack", method = RequestMethod.POST)
	@ResponseBody
	public void updateVideoCoverUrl(HttpServletRequest request, VideoParam videoParam) {
		BufferedReader br = null;
		try {
			br = request.getReader();
			StringBuilder sb = new StringBuilder("");
			String str;
			while( (str = br.readLine()) != null){
				sb.append(str);
			}
			JSONObject callBackInfo = new JSONObject(sb.toString());
			logger.info("-------call back info:" + callBackInfo.toString());
			if (VideoConstant.VIDEO_PROCEDURESTATECHANGED.equals(callBackInfo.getString("eventType"))) {
				videoParam.setCloudCallBackInfo(sb.toString());
				videoService.updateByFileIdFromCallBack(videoParam);
			}
		} catch (IOException e) {
			logger.error("-------cloud call back stream read error:" + e.toString());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				logger.error("-------cloud call back stream close error:" + e.toString());
			}
		}
	}

	@RequestMapping(value = "videoListView", method = RequestMethod.GET)
	public String videoListVO(VideoParam videoParam, Model model) {
//		final List<Map<String, Object>> list = new ArrayList<>();
//		final Map<String, Object> map = new HashMap<>();
//		map.put("videoList", videoService.listVideo(videoParam.getUserId()));
//		list.add(map);
		model.addAttribute("videoList", videoService.listVideo(videoParam.getUserId()));
//		model.addAttribute("videoList", map);
		return "/video/videoList";
	}
}
