package com.lty.eduservice.service;

import com.lty.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程类别
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    //课程分类列表(树形)
    List<OneSubject> getAllOneTwoSubject();
}
