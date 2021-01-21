package com.lty.educenter.controller;


import com.lty.commonutils.JwtUtils;
import com.lty.commonutils.R;
import com.lty.commonutils.vo.UcenterMemberOrder;
import com.lty.educenter.entity.UcenterMember;
import com.lty.educenter.entity.vo.RegisterVo;
import com.lty.educenter.service.UcenterMemberService;
import com.lty.servicebase.exceptionhandler.GuLiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author lty
 * @since 2021-01-15
 */
@Controller
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录方法
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        String token = ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }
    //注册方法
    @PostMapping("register")
    public R registerUser(@PathVariable RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }
    //通过token获取用户信息，密码登录方式
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        try {
            //JwtUtils.getMemberIdByJwtToken(request) 作用：根据token获取会员id
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember member = ucenterMemberService.getLoginInfo(memberId);
            return R.ok().data("userInfo",member);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuLiException(20001,"error");
        }
    }
    //通过token获取用户信息，微信登录方式
    @GetMapping("getWxInfo")
    public R getWxInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            UcenterMember member = ucenterMemberService.getById(memberId);
            return R.ok().data("userInfo",member);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuLiException(20001,"error");
        }
    }
    //根据token字符串获取用户信息，用户评论使用
    @ApiOperation(value = "根据用户id获取用户信息 课程评论用")
    @PostMapping("getInfoUc/{memberId}")
    public UcenterMemberOrder getInfo(@PathVariable String memberId) {

        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        System.out.println(ucenterMember);
        //把ucenterMember对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberOrder);
        return ucenterMemberOrder;
    }
}

