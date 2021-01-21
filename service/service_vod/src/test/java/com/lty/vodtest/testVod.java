package com.lty.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;

import java.util.List;

public class testVod {
    @Test
    public void TestVod01() {
        String accessKeyId = "LTAI4GKd2oYDdDSfZxFTcR6F";
        String accessKeySecret = "gcIHLHq5GiqTsiS4hqc0w9sr8jXz57";
        String title = "6 - What If I Want to Move Faster-java-sdk";
        String fileName = "D:\\studyjava\\谷粒学院\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4";

        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    //根据视频id获取播放凭证
    public void getPlayAuth(){

        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GKd2oYDdDSfZxFTcR6F", "gcIHLHq5GiqTsiS4hqc0w9sr8jXz57");
        //创建获取视频凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        request.setVideoId("1e74b7454a4c4df086288fc56be2625e");
        request.setAuthInfoTimeout(3000L);

        try {
            response =  client.getAcsResponse(request);
            System.out.println("playauth: " + response.getPlayAuth());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    //根据视频id获得视频播放地址
    public void getPlayUrl(){

        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GKd2oYDdDSfZxFTcR6F", "gcIHLHq5GiqTsiS4hqc0w9sr8jXz57");
        //创建获取视频地址的request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        request.setVideoId("fb767afbf37147609eca4cd9057466dd");

        try {
            response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test02(){
        getPlayAuth();
    }
}
