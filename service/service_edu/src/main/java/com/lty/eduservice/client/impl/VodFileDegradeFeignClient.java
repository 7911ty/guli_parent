package com.lty.eduservice.client.impl;

import com.lty.commonutils.R;
import com.lty.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteAlyVideo(String id) {
        return R.error().message("删除失败");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除失败");
    }
}
