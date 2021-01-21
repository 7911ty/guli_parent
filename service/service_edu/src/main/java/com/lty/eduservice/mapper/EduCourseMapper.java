package com.lty.eduservice.mapper;

import com.lty.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lty.eduservice.entity.frontvo.CourseWebVo;
import com.lty.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    CoursePublishVo selectCoursePublishInfoById(String id);
    //1.根据课程id，查询课程基本信息
    CourseWebVo selectCourseBaseInfo(String courseId);
}
