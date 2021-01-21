package com.lty.eduservice.controller;

import com.lty.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController //交给spring管理
@RequestMapping("/eduservice/user") //请求地址
@CrossOrigin   //解决跨域问题
public class EduLoginController {
    //login
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }
    //info
    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avator","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608214500472&di=6d0ac1fb83353a0b4476d4e14ee18dc6&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg");
    }
}
