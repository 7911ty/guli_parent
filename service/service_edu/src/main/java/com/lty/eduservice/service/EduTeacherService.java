package com.lty.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-10
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> selectTeacherList();

    //前端分页查询讲师方法
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage);
}
