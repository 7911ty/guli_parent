package com.lty.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.lty.commonutils.R;
import com.lty.servicebase.exceptionhandler.GuLiException;
import com.lty.vod.service.VideoService;
import com.lty.vod.utils.ConstantPropertiesUtil;
import com.lty.vod.utils.InitVideoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin //跨域
@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VideoService videoService;

    //上传视频到阿里云
    @PostMapping("uploadAlyVideo")
    public R uploadAlyiVideo(MultipartFile file) {
        String videoId = videoService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }
    //根据视频id删除阿里云视频
    @DeleteMapping("deleteAlyVideo/{id}")
    public R deleteAlyVideo(@PathVariable String id){
        videoService.removeVideo(id);
        return R.ok();
    }
    //根据传来的多个视频id,删除多个视频
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List videoIdList){
        videoService.removeMoveVideo(videoIdList);
        return R.ok();
    }
    //获取视频播放凭证
    @GetMapping("getVideoPlayAuth/{id}")
    public R getVideoPlayAuth(@PathVariable String id){

        //获取阿里云存储相关常量
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        //创建初始化对象
        DefaultAcsClient client = InitVideoClient.initVodClient(accessKeyId, accessKeySecret);
        //创建获取视频凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        request.setVideoId(id);
        request.setAuthInfoTimeout(3000L);

        try {
            response =  client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuLiException(20001,"获取视频凭证失败");
        }

    }
}
