package com.yanhua.dao;


import com.yanhua.entity.Video;

import java.util.List;

public interface IVideoDao {
    int deleteByPrimaryKey(Integer id);

    int saveVideo(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    int updateByFileIdFromCallBack(Video video);

	List<Video> listVideo(int userId);
}