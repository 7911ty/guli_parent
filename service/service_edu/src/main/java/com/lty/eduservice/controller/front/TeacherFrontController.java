package com.lty.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduCourse;
import com.lty.eduservice.entity.EduTeacher;
import com.lty.eduservice.service.EduCourseService;
import com.lty.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description="前端讲师管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    //1.前端分页查询讲师方法
    @GetMapping("GetTeacherFrontList/{page}/{limit}")
    public R getTeacherFront(@PathVariable long page ,@PathVariable long limit){

        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(teacherPage);
        return R.ok().data(map);
    }
    //2.前端讲师详情查询
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        //1.根据id查询讲师的主要信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        //2.根据讲师id查询讲师的所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        return R.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);
    }

}
