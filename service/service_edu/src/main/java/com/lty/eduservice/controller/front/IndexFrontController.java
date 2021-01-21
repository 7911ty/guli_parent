package com.lty.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduCourse;
import com.lty.eduservice.entity.EduTeacher;
import com.lty.eduservice.service.EduCourseService;
import com.lty.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    EduTeacherService teacherService;

    //查询8条热门课程，4个名师
    /*@GetMapping("index")
    public R index(){
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(courseQueryWrapper);

        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("id");
        teacherQueryWrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherQueryWrapper);

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }*/
    //查询8条热门课程，4个名师
    @GetMapping("index")
    public R index(){

        List<EduCourse> courseList = courseService.selectCourseList();//


        List<EduTeacher> teacherList = teacherService.selectTeacherList();//teacherQueryWrapper

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
