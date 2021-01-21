package com.lty.eduservice.client.impl;

import com.lty.commonutils.vo.UcenterMemberOrder;
import com.lty.eduservice.client.UcenterClient;
import org.springframework.stereotype.Component;



@Component
public class UcenterFileDegradeFeignClient implements UcenterClient {

    @Override
    public UcenterMemberOrder getInfo(String id) {
        return null;
    }
}
