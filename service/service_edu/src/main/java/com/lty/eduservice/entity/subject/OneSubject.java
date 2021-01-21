package com.lty.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//课程类别,一级分类
@Data
public class OneSubject {
    private String id;
    private String title;

    //一个一级分类有多个二级分类,使用集合存储
    private List<TwoSubject> children = new ArrayList<>();
}
