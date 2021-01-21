package com.lty.eduservice.client;

import com.lty.commonutils.R;
import com.lty.eduservice.client.impl.VodFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class) //调用服务
public interface VodClient {
    //根据视频id删除阿里云视频
    @DeleteMapping("/eduvod/video/deleteAlyVideo/{id}")
    public R deleteAlyVideo(@PathVariable("id") String id);

    //定义删除多个视频的方法,供在删除课程时使用
    //根据传来的多个视频id,删除多个视频
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
