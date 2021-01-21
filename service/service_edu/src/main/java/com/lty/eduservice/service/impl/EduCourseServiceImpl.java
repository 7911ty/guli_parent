package com.lty.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.eduservice.entity.EduChapter;
import com.lty.eduservice.entity.EduCourse;
import com.lty.eduservice.entity.EduCourseDescription;
import com.lty.eduservice.entity.frontvo.CourseFrontVo;
import com.lty.eduservice.entity.frontvo.CourseWebVo;
import com.lty.eduservice.entity.vo.CourseInfoVo;
import com.lty.eduservice.entity.vo.CoursePublishVo;
import com.lty.eduservice.mapper.EduCourseMapper;
import com.lty.eduservice.service.EduChapterService;
import com.lty.eduservice.service.EduCourseDescriptionService;
import com.lty.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.eduservice.service.EduVideoService;
import com.lty.servicebase.exceptionhandler.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    //描述接口
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    //小节接口
    @Autowired
    private EduVideoService eduVideoService;
    //章节接口
    @Autowired
    private EduChapterService eduChapterService;
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.保存课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0){
            throw new GuLiException(20001,"添加课程信息失败.");
        }
        //获取添加成功后课程id
        String cid = eduCourse.getId();
        //2.保存课程详细信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        //手动设置课程详情的信息
        eduCourseDescription.setId(cid);
        courseDescriptionService.save(eduCourseDescription);

        return cid;
    }
    //根据课程id查询课程信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        //1.首先根据id去course表查询课程基本信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2.再去课程描述表查询描述信息
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);
        //将描述信息添加进去
        courseInfoVo.setDescription(eduCourseDescription.getDescription());

        return courseInfoVo;
    }
    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.先修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i == 0){
            throw new GuLiException(20001,"修该课程失败");
        }
        //2.在修改描述信息表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id获取课程发布信息
    @Override
    public CoursePublishVo getCoursePublishInfo(String id) {
        CoursePublishVo coursePublishVo = baseMapper.selectCoursePublishInfoById(id);
        return coursePublishVo;
    }

    //根据课程id删除课程
    @Override
    public void removeCourseById(String courseId) {
        //1.根据课程id先删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //2.根据课程id先删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //3.根据课程id先删除描述
        courseDescriptionService.removeById(courseId);
        //4.删除课程
        int i = baseMapper.deleteById(courseId);
        if (i == 0){
            throw new GuLiException(20001,"删除失败");
        }

    }

    @Cacheable(value = "course",key = "'courseList'")
    @Override
    public List<EduCourse> selectCourseList() {
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");
        List<EduCourse> courseList = baseMapper.selectList(courseQueryWrapper);
        return courseList;
    }

    //条件查询带分页查询
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件查询中一些选项是否选择
        //一级分类是否选择
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        //二级分类是否选择
        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())){
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        //判断是否按销量排序
        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        //判断是否选择按时间排序
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }

        //判断是否按价格排序
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }
        baseMapper.selectPage(coursePage,wrapper);//将查询到的结果存储在coursePage
        List<EduCourse> records = coursePage.getRecords();
        long current = coursePage.getCurrent();//当前页
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //1.根据课程id，查询课程基本信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.selectCourseBaseInfo(courseId);
    }


}
