package com.lty.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduCourse;
import com.lty.eduservice.entity.EduTeacher;
import com.lty.eduservice.entity.vo.CourseInfoVo;
import com.lty.eduservice.entity.vo.CoursePublishVo;
import com.lty.eduservice.service.EduChapterService;
import com.lty.eduservice.service.EduCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    //分页查询
    @ApiOperation(value = "分页课程列表")
    @GetMapping("pageCourse/{current}/{limit}")
    public R pageCourse(@PathVariable long current,@PathVariable long limit){
        //创建page对象
        Page<EduCourse> pageCourse = new Page(current,limit);
        //调用方法实现分页
        eduCourseService.page(pageCourse,null);

        List<EduCourse> records = pageCourse.getRecords();//数据list集合
        long total = pageCourse.getTotal();//总记录数

        //返回统一结果时也可以使用map进行返回
        /*Map map = new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);*/
        return R.ok().data("total",total).data("rows",records);
    }
    //查询所有课程信息
    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }
    //添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }
    //根据课程id查询课程信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }
    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }
    //根据课程id获取课程发布信息
    @GetMapping("coursePublishInfo/{id}")
    public R coursePublishInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.getCoursePublishInfo(id);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }
    //课程最终发布
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程的状态码 ,Noraml表示发布,
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }
    //根据课程id删除课程
    @DeleteMapping("deleteCourseById/{courseId}")
    public R deleteCourseById(@PathVariable String courseId){
        eduCourseService.removeCourseById(courseId);
        return R.ok();
    }
}

