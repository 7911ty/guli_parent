package com.lty.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lty.eduservice.entity.EduSubject;
import com.lty.eduservice.entity.excel.SubjectData;
import com.lty.eduservice.entity.subject.OneSubject;
import com.lty.eduservice.entity.subject.TwoSubject;
import com.lty.eduservice.listener.SubjectExcelListener;
import com.lty.eduservice.mapper.EduSubjectMapper;
import com.lty.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //课程分类列表(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        //1.先查询出一级所有数据
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //2.查询出二级所有数据
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建一个List集合用于存储最终封装数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3.封装一级分类
        //首先遍历所有一级数据,得到每一个一级对象的数据
        //将获取的对象,封装到List<OneSubject> finalSubjectList
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //循环取出一级分类所有数据
            EduSubject eduSubject = oneSubjectList.get(i);

            OneSubject oneSubject = new OneSubject();

            //oneSubject.setId(eduSubject.getId());
            //oneSubject.setTitle(eduSubject.getTitle());
            //把EduSubject里面的值取出来放到oneSubject里面去
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //多个OneSubject存放到finalSubjectList里面
            finalSubjectList.add(oneSubject);

            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //4.封装二级分类
            for (int m = 0; m < twoSubjectList.size(); m++) {
                //通过循环吧二级目录里面的数据取出来
                EduSubject tSubject = twoSubjectList.get(m);
                //判断二级数据的父id是不是当前一级数据
                if (tSubject.getParentId().equals(eduSubject.getId()) ){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
