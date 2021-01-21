package com.lty.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    //上传视频到阿里云
    String uploadVideo(MultipartFile file);
    //根据视频id删除阿里云视频
    void removeVideo(String id);

    //根据传来的多个视频id,删除多个视频
    void removeMoveVideo(List<String> videoIdList);
}
