package com.yanhua.entity;

import java.sql.Timestamp;

public class Video {
    private Integer id;

    private String videodesc;

    private Timestamp uploadtime;

    private String videourl;

    private String videoname;

    private Integer userid;

    private String coverurl;

    private String fileid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideodesc() {
        return videodesc;
    }

    public void setVideodesc(String videodesc) {
        this.videodesc = videodesc == null ? null : videodesc.trim();
    }

	/**
	 * 获取uploadtime
	 *
	 * @return uploadtime
	 */
	public Timestamp getUploadtime() {
		return uploadtime;
	}

	/**
	 * 设置uploadtime
	 *
	 * @param uploadtime
	 */
	public void setUploadtime(Timestamp uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl == null ? null : videourl.trim();
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname == null ? null : videoname.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl == null ? null : coverurl.trim();
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid == null ? null : fileid.trim();
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", videodesc:" + videodesc +
                ", uploadtime:" + uploadtime +
                ", videourl:" + videourl +
                ", videoname:" + videoname +
                ", userid:" + userid +
                ", coverurl:" + coverurl +
                ", fileid:" + fileid +
                '}';
    }
}