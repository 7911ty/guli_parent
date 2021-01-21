package com.lty.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lty.eduservice.entity.EduChapter;
import com.lty.eduservice.entity.EduVideo;
import com.lty.eduservice.entity.chapter.ChapterVo;
import com.lty.eduservice.entity.chapter.VideoVo;
import com.lty.eduservice.entity.vo.CourseInfoVo;
import com.lty.eduservice.mapper.EduChapterMapper;
import com.lty.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.eduservice.service.EduVideoService;
import com.lty.servicebase.exceptionhandler.GuLiException;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-18
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //小结接口
    @Autowired
    private EduVideoService eduVideoService;

    //课程大纲列表,根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseID(String courseId) {

        //1.查询出大纲列表数据
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2.查询出小结列表数据
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);
        //创建一个List存储最终封装结果
        List<ChapterVo> finalList = new ArrayList<>();
        //3.封装大纲的数据
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //eduChapter值复制到chapterVo中
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            
            finalList.add(chapterVo);
            //4.封装小结的数据
            //创建一个List存储小结封装数据
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int m = 0; m < eduVideoList.size(); m++) {
                EduVideo eduVideo = eduVideoList.get(m);
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }

        return finalList;
    }

    //根据章节id进行删除
    @Override
    public boolean deleteChapter(String chapterId) {
        //先根据chapterId查询小节,如果有小节,就无法删除,没有小节就可以删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);
        if (count>0){//章节下面有小节,不能删除
            throw new GuLiException(20001,"删除失败.");
        }else {//章节下面没有小节,可以直接删除
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }
    //根据课程id进行删除
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wraper = new QueryWrapper<>();
        wraper.eq("course_id",courseId);
        baseMapper.delete(wraper);
    }


}
