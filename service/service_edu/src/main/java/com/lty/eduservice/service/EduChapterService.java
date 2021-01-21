package com.lty.eduservice.service;

import com.lty.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.eduservice.entity.chapter.ChapterVo;
import com.lty.eduservice.entity.vo.CourseInfoVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
public interface EduChapterService extends IService<EduChapter> {

    //课程大纲列表,根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseID(String courseId);

    //根据章节id进行删除
    boolean deleteChapter(String chapterId);
    //根据课程id进行删除
    void removeChapterByCourseId(String courseId);
}
