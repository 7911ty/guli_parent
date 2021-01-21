package com.lty.eduservice.client;

import com.lty.commonutils.vo.UcenterMemberOrder;
import com.lty.eduservice.client.impl.UcenterFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Component
@FeignClient(name = "service-ucenter", fallback = UcenterFileDegradeFeignClient.class) //调用服务
public interface
UcenterClient {
    //根据token字符串获取用户信息
    @PostMapping("/educenter/member/getInfoUc/{memberId}")
    public UcenterMemberOrder getInfo(@PathVariable("memberId") String memberId);
}
