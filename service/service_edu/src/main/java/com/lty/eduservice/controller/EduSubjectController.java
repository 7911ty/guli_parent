package com.lty.eduservice.controller;


import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduSubject;
import com.lty.eduservice.entity.subject.OneSubject;
import com.lty.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }
    //课程分类展示
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list =  eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

