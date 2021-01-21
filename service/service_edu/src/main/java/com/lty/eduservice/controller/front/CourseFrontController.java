package com.lty.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduCourse;
import com.lty.eduservice.entity.EduTeacher;
import com.lty.eduservice.entity.chapter.ChapterVo;
import com.lty.eduservice.entity.frontvo.CourseFrontVo;
import com.lty.eduservice.entity.frontvo.CourseWebVo;
import com.lty.eduservice.service.EduChapterService;
import com.lty.eduservice.service.EduCourseService;
import com.lty.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description="前端课程管理")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    //条件查询带分页查询
    @PostMapping("getFrontCourseList/{page}/{limit}")
    //@RequestBody(required = false) 表示可以不传递该参数，可以有也可以没有
    public R getFrontCourseList(@PathVariable long page,@PathVariable long limit, @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage,courseFrontVo);
        return R.ok().data(map);
    }
    //根据课程di查询课程基本信息
    @GetMapping("getCourseFrontInfo/{courseId}")
    public R getCourseFrontInfo(@PathVariable String courseId){

        //1.根据课程id，查询课程基本信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //2.根据课程id，查询课程章节和小节
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoByCourseID(courseId);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVoList",chapterVoList);
    }

}
