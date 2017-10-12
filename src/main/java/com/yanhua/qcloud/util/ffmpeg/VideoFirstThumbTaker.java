package com.yanhua.qcloud.util.ffmpeg;


import com.yanhua.qcloud.util.PropUtils;

import java.io.IOException;

/***
 *
 * 得到第一秒（也是第一帧）图片
 */
public class VideoFirstThumbTaker extends VideoThumbTaker
{
	public VideoFirstThumbTaker(String ffmpegApp)
	{
		super(ffmpegApp);
	}

	public String getThumb(String videoFilename, String thumbFilename) throws IOException, InterruptedException
	{
		VideoInfo videoInfo = new VideoInfo(PropUtils.getConfig("ffmpeg"));
		videoInfo.getInfo(videoFilename);
		super.getThumb(videoFilename, thumbFilename, videoInfo.getWidth(), videoInfo.getHeigt(), 0, 0, 1);
		return thumbFilename;
	}

	public static void main(String[] args) {
		VideoFirstThumbTaker videoFirstThumbTaker = new VideoFirstThumbTaker("D:\\testsave\\ffmpeg-20170811-5859b5b-win64-static\\bin\\ffmpeg.exe");
		try {
			String localPath = PropUtils.getConfig("WfileBasePath") + "\\testThumb6.jpg";
			videoFirstThumbTaker.getThumb("D:\\testsave\\testUploadyun.mp4", localPath);
			System.out.println("done");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}