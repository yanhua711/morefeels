package com.yanhua.qcloud.util;

public class FileParm {

	/**
	 * 服务器上存放的相对路径
	 */
	private String relativePath;

	/**
	 * 文件名
	 */
	private String fileNameUpload;

	/**
	 * 图片尺寸
	 */
	private String pictureSize;

	/**
	 * 公司编码
	 */
	private String fileSource;

	/**
	 * 上传图片路径
	 */
	private String uploadUrl;

	/**
	 * 获取服务器上存放的相对路径
	 *
	 * @return relativePath 服务器上存放的相对路径
	 */
	public String getRelativePath() {
		return this.relativePath;
	}

	/**
	 * 设置服务器上存放的相对路径
	 *
	 * @param relativePath
	 *            服务器上存放的相对路径
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * 获取文件名
	 *
	 * @return fileNameUpload 文件名
	 */
	public String getFileNameUpload() {
		return this.fileNameUpload;
	}

	/**
	 * 设置文件名
	 *
	 * @param fileNameUpload
	 *            文件名
	 */
	public void setFileNameUpload(String fileNameUpload) {
		this.fileNameUpload = fileNameUpload;
	}

	/**
	 * 获取图片尺寸
	 *
	 * @return pictureSize 图片尺寸
	 */
	public String getPictureSize() {
		return this.pictureSize;
	}

	/**
	 * 设置图片尺寸
	 *
	 * @param pictureSize
	 *            图片尺寸
	 */
	public void setPictureSize(String pictureSize) {
		this.pictureSize = pictureSize;
	}

	/**
	 * 获取上传图片路径
	 *
	 * @return uploadUrl 上传图片路径
	 */
	public String getUploadUrl() {
		return this.uploadUrl;
	}

	/**
	 * 设置上传图片路径
	 *
	 * @param uploadUrl
	 *            上传图片路径
	 */
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	/**
	 * 获取公司编码
	 *
	 * @return fileSource 公司编码
	 */
	public String getFileSource() {
		return this.fileSource;
	}

	/**
	 * 设置公司编码
	 *
	 * @param fileSource
	 *            公司编码
	 */
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

}
