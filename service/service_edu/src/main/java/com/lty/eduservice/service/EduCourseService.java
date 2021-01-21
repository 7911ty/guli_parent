package com.lty.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.eduservice.entity.frontvo.CourseFrontVo;
import com.lty.eduservice.entity.frontvo.CourseWebVo;
import com.lty.eduservice.entity.vo.CourseInfoVo;
import com.lty.eduservice.entity.vo.CoursePublishVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息
    String saveCourseInfo(CourseInfoVo courseInfoVo);
    //根据课程id查询课程信息
    CourseInfoVo getCourseInfo(String courseId);
    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);
    //根据课程id获取课程发布信息
    CoursePublishVo getCoursePublishInfo(String id);
    //根据课程id删除课程
    void removeCourseById(String courseId);

    List<EduCourse> selectCourseList();
    //条件查询带分页查询
    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);
    //1.根据课程id，查询课程基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
