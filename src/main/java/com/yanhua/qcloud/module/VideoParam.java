package com.yanhua.qcloud.module;

import java.sql.Timestamp;

/**
 * @author zhiwei.yan
 * @Description: 视频上传参数类
 * @date:2017-08-18 10:49.
 */
public class VideoParam {

	/**
	 * 视频文件本地名称，如果包含中文空格，则需要使用rawurlencode编码，长度在40个字符以内，不得包含\ / : * ? “ < > | 等字符
	 */
	private String fileName;

	/**
	 *  视频文件的类型，根据后缀区分
	 */
	private String fileType;

	/**
	 * 非必须
	 * 视频的标签列表
	 */
	private String tags_n;

	/**
	 * 视频播放地址
	 */
	private String sourceVideoUrl;

	/**
	 * 非必须
	 * 是否转码，0：否，1：是，默认为0；如果不执行转码，可在上传后，在管理控制台视频文件管理进行转码；
	 */
	private String isTranscode;

	/**
	 * 非必须
	 * 是否截图，0：否，1：是，默认为0
	 */
	private String isScreenshot;

	/**
	 * 非必须
	 * 是否打水印，0：否，1：是，默认为0；如果选择打水印，请务必在管理控制台提前完成水印文件选择和位置设定，否则可能导致上传失败；
	 */
	private String isWatermark;

	/**
	 * 本地文件路径
	 */
	private String fileLocalPath;

	/**
	 * 文件描述
	 */
	private String fileIntro;

	/**
	 * 分类id
	 */
	private int classId;

	/**
	 * 分页页号
	 */
	private int pageNo;

	/**
	 * 分页大小，范围在10-100之间
	 */
	private int pageSize;

	/**
	 * 文件ID
	 */
	private String fileId;

	/**
	 * 转码输出模板号
	 */
	private String transcode_definition;

	/**
	 * 采样截图模板号(10)
	 */
	private Integer sampleSnapshot_definition;

	/**
	 * 指定时间点截图模板号 (10)
	 */
	private Integer coverBySnapshot_definition;

	/**
	 * 截图方式。Time：依照时间点截图；Percent：依照百分比截图。
	 */
	private String coverBySnapshot_positionType;

	/**
	 * 截图位置。对于依照时间点截图，该值表示指定视频第几秒的截图作为封面；对于依照百分比截图，
	 * 该值表示使用视频百分之多少的截图作为封面。截图位置。对于依照时间点截图，该值表示指定视频第几秒的截图作为封面；
	 * 对于依照百分比截图，该值表示使用视频百分之多少的截图作为封面。
	 */
	private Integer coverBySnapshot_position;

	/**
	 * 任务流状态变更通知模式。
	 * Finish：只有当任务流全部执行完毕时，才发起一次事件通知；
	 * Change：只要任务流中每个子任务的状态发生变化，都进行事件通知。
	 * 默认为Finish。
	 */
	private String notifyMode;

	/**
	 * 视频上传时间
	 */
	private Timestamp updateTime;

	/**
	 * 用户id
	 */
	private int userId;

	/**
	 * 腾讯云回调信息
	 */
	private String cloudCallBackInfo;

	/**
	 * 视频封面url
	 */
	private String videoCoverUrl;

	/**
	 * 获取fileName
	 *
	 * @return fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置fileName
	 *
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取fileType
	 *
	 * @return fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * 设置fileType
	 *
	 * @param fileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取tags_n
	 *
	 * @return tags_n
	 */
	public String getTags_n() {
		return tags_n;
	}

	/**
	 * 设置tags_n
	 *
	 * @param tags_n
	 */
	public void setTags_n(String tags_n) {
		this.tags_n = tags_n;
	}

	/**
	 * 获取isTranscode
	 *
	 * @return isTranscode
	 */
	public String getIsTranscode() {
		return isTranscode;
	}

	/**
	 * 设置isTranscode
	 *
	 * @param isTranscode
	 */
	public void setIsTranscode(String isTranscode) {
		this.isTranscode = isTranscode;
	}

	/**
	 * 获取isScreenshot
	 *
	 * @return isScreenshot
	 */
	public String getIsScreenshot() {
		return isScreenshot;
	}

	/**
	 * 设置isScreenshot
	 *
	 * @param isScreenshot
	 */
	public void setIsScreenshot(String isScreenshot) {
		this.isScreenshot = isScreenshot;
	}

	/**
	 * 获取isWatermark
	 *
	 * @return isWatermark
	 */
	public String getIsWatermark() {
		return isWatermark;
	}

	/**
	 * 设置isWatermark
	 *
	 * @param isWatermark
	 */
	public void setIsWatermark(String isWatermark) {
		this.isWatermark = isWatermark;
	}

	/**
	 * 获取fileLocalPath
	 *
	 * @return fileLocalPath
	 */
	public String getFileLocalPath() {
		return fileLocalPath;
	}

	/**
	 * 设置fileLocalPath
	 *
	 * @param fileLocalPath
	 */
	public void setFileLocalPath(String fileLocalPath) {
		this.fileLocalPath = fileLocalPath;
	}

	/**
	 * 获取fileIntro
	 *
	 * @return fileIntro
	 */
	public String getFileIntro() {
		return fileIntro;
	}

	/**
	 * 设置fileIntro
	 *
	 * @param fileIntro
	 */
	public void setFileIntro(String fileIntro) {
		this.fileIntro = fileIntro;
	}

	/**
	 * 获取classId
	 *
	 * @return classId
	 */
	public int getClassId() {
		return classId;
	}

	/**
	 * 设置classId
	 *
	 * @param classId
	 */
	public void setClassId(int classId) {
		this.classId = classId;
	}

	/**
	 * 获取pageNo
	 *
	 * @return pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置pageNo
	 *
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 获取pageSize
	 *
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置pageSize
	 *
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取fileId
	 *
	 * @return fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * 设置fileId
	 *
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 获取transcode_definition
	 *
	 * @return transcode_definition
	 */
	public String getTranscode_definition() {
		return transcode_definition;
	}

	/**
	 * 设置transcode_definition
	 *
	 * @param transcode_definition
	 */
	public void setTranscode_definition(String transcode_definition) {
		this.transcode_definition = transcode_definition;
	}

	/**
	 * 获取sampleSnapshot_definition
	 *
	 * @return sampleSnapshot_definition
	 */
	public Integer getSampleSnapshot_definition() {
		return sampleSnapshot_definition;
	}

	/**
	 * 设置sampleSnapshot_definition
	 *
	 * @param sampleSnapshot_definition
	 */
	public void setSampleSnapshot_definition(Integer sampleSnapshot_definition) {
		this.sampleSnapshot_definition = sampleSnapshot_definition;
	}

	/**
	 * 获取coverBySnapshot_definition
	 *
	 * @return coverBySnapshot_definition
	 */
	public Integer getCoverBySnapshot_definition() {
		return coverBySnapshot_definition;
	}

	/**
	 * 设置coverBySnapshot_definition
	 *
	 * @param coverBySnapshot_definition
	 */
	public void setCoverBySnapshot_definition(Integer coverBySnapshot_definition) {
		this.coverBySnapshot_definition = coverBySnapshot_definition;
	}

	/**
	 * 获取coverBySnapshot_positionType
	 *
	 * @return coverBySnapshot_positionType
	 */
	public String getCoverBySnapshot_positionType() {
		return coverBySnapshot_positionType;
	}

	/**
	 * 设置coverBySnapshot_positionType
	 *
	 * @param coverBySnapshot_positionType
	 */
	public void setCoverBySnapshot_positionType(String coverBySnapshot_positionType) {
		this.coverBySnapshot_positionType = coverBySnapshot_positionType;
	}

	/**
	 * 获取coverBySnapshot_position
	 *
	 * @return coverBySnapshot_position
	 */
	public Integer getCoverBySnapshot_position() {
		return coverBySnapshot_position;
	}

	/**
	 * 设置coverBySnapshot_position
	 *
	 * @param coverBySnapshot_position
	 */
	public void setCoverBySnapshot_position(Integer coverBySnapshot_position) {
		this.coverBySnapshot_position = coverBySnapshot_position;
	}

	/**
	 * 获取notifyMode
	 *
	 * @return notifyMode
	 */
	public String getNotifyMode() {
		return notifyMode;
	}

	/**
	 * 设置notifyMode
	 *
	 * @param notifyMode
	 */
	public void setNotifyMode(String notifyMode) {
		this.notifyMode = notifyMode;
	}

	/**
	 * 获取updateTime
	 *
	 * @return updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置updateTime
	 *
	 * @param updateTime
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取sourceVideoUrl
	 *
	 * @return sourceVideoUrl
	 */
	public String getSourceVideoUrl() {
		return sourceVideoUrl;
	}

	/**
	 * 设置sourceVideoUrl
	 *
	 * @param sourceVideoUrl
	 */
	public void setSourceVideoUrl(String sourceVideoUrl) {
		this.sourceVideoUrl = sourceVideoUrl;
	}

	/**
	 * 获取userId
	 *
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * 设置userId
	 *
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 获取cloudCallBackInfo
	 *
	 * @return cloudCallBackInfo
	 */
	public String getCloudCallBackInfo() {
		return cloudCallBackInfo;
	}

	/**
	 * 设置cloudCallBackInfo
	 *
	 * @param cloudCallBackInfo
	 */
	public void setCloudCallBackInfo(String cloudCallBackInfo) {
		this.cloudCallBackInfo = cloudCallBackInfo;
	}
}
