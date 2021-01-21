package com.lty.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.commonutils.R;
import com.lty.eduservice.entity.EduTeacher;
import com.lty.eduservice.entity.vo.TeacherQuery;
import com.lty.eduservice.service.EduTeacherService;
import com.lty.servicebase.exceptionhandler.GuLiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-10
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin   //解决跨域问题
public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService ;

    //查询所有教师数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id){
        boolean flog = eduTeacherService.removeById(id);
        if (flog){
            return R.ok();
        }else {
            return R.error();
        }
    }
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@PathVariable long current,@PathVariable long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page(current,limit);
        //调用方法实现分页
        eduTeacherService.page(pageTeacher,null);

        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        long total = pageTeacher.getTotal();//总记录数

        //返回统一结果时也可以使用map进行返回
        /*Map map = new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);*/
        return R.ok().data("total",total).data("rows",records);
    }
    //使用@PostMapping时要配合RequestBody使用
    //RequestBody表示使用json形式传输数据
    //4.条件查询带分页
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，不为空进行拼接
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified",end);
        }
        //通过创建时间进行排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现分页
        eduTeacherService.page(page,wrapper);

        List<EduTeacher> records = page.getRecords();//数据list集合
        long total = page.getTotal();//总记录数
        return R.ok().data("total",total).data("rows",records);
    }
    //添加讲师的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else{
            return R.error();
        }
    }
    //根据讲师id查询
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);

        return R.ok().data("teacher",teacher);
    }
    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacger(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

