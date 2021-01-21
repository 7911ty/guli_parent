package com.lty.msmservice.controller;

import com.lty.commonutils.R;
import com.lty.msmservice.service.MsmService;
import com.lty.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取验证码的方法
     * @param phone
     * @return
     */
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //1.从redis中获取验证码如果存在就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        //2.如果redis获取不到，就使用阿里云获取验证码
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> parm = new HashMap<>();
        parm.put("code", code);
        //调用service方法进行短信发送
        boolean isSend = msmService.send(parm, phone);
        if (isSend) {
            //发送成功后设置验证码的存活时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败。");
        }
    }
}
