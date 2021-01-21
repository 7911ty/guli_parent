package com.lty.eduservice.service;

import com.lty.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
public interface EduVideoService extends IService<EduVideo> {

    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);
    //根据小节id删除小节,如果小节里有视频也要把视频删除
    void removeVideoByVideoId(String videoId);
}
