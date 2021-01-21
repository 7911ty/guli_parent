package com.lty.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.lty.commonutils.R;
import com.lty.servicebase.exceptionhandler.GuLiException;
import com.lty.vod.service.VideoService;
import com.lty.vod.utils.ConstantPropertiesUtil;
import com.lty.vod.utils.InitVideoClient;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    //上传视频到阿里云
    @Override
    public String uploadVideo(MultipartFile file) {
        try {

            String fileName = file.getOriginalFilename();  //视频原来的名字
            String title = fileName.substring(0, fileName.lastIndexOf("."));//视频上传在阿里云显示的名字
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = "";
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //根据视频id删除阿里云视频
    @Override
    public  void removeVideo(String id) {
        try {
            //初初始化对象
            DefaultAcsClient client = InitVideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除对此象的request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request里面设置视屏id
            request.setVideoIds(id);
            //调用初始化方法删除视频
            client.getAcsResponse(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GuLiException(20001, "删除失败");
        }

    }

    //根据传来的多个视频id,删除多个视频
    @Override
    public void removeMoveVideo(List<String> videoIdList) {
        try {
            //初初始化对象
            DefaultAcsClient client = InitVideoClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除对此象的request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request里面设置视屏id
            //把list中的视频id转换成 1,2,3 这种才可以传入 request.setVideoIds();
            String videoIds = StringUtil.join(videoIdList.toArray(), ",");
            request.setVideoIds(videoIds);
            //调用初始化方法删除视频
            client.getAcsResponse(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GuLiException(20001, "删除失败");
        }

    }
}
