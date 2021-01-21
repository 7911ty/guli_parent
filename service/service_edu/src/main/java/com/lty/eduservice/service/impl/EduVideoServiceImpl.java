package com.lty.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lty.commonutils.R;
import com.lty.eduservice.client.VodClient;
import com.lty.eduservice.entity.EduVideo;
import com.lty.eduservice.mapper.EduVideoMapper;
import com.lty.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.servicebase.exceptionhandler.GuLiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    //根据课程id删除小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        //先根据课程id查询出每个小节下的所有视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapperVideo);
        //将List<EduVideo>转换成List<String>形式
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            //有视频才把视频id加入
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }
        if (videoIds.size() > 0) {
            //根据查询出的所有视频id,先删除小节的视频
            vodClient.deleteBatch(videoIds);
        }
        //在根据视频id删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }

    //根据小节id删除小节,如果小节里有视频也要把视频删除
    @Override
    public void removeVideoByVideoId(String videoId) {
        //现根据小节id ,videoId获取到小节对象,在小节对象中有视频的源id
        EduVideo eduVideo = baseMapper.selectById(videoId);

        if (eduVideo.getVideoSourceId() != null) {//判断是否有视频在小节中
            String videoSourceId = eduVideo.getVideoSourceId();
            R result = vodClient.deleteAlyVideo(videoSourceId);
            if (result.getCode() == 20001){
                throw new GuLiException(20001,"视频删除失败,熔断器");
            }
        }
        //删除完视频,再把小节删除
        baseMapper.deleteById(videoId);
    }
}
