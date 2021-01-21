package com.lty.educenter.service;

import com.lty.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lty
 * @since 2021-01-15
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //用户登录
    String login(UcenterMember ucenterMember);

    //用户注册 方法
    void register(RegisterVo registerVo);
    //通过token获取用户信息
    UcenterMember getLoginInfo(String memberId);

    //通过接口利用openid查询该该微信是否注册过
    UcenterMember getByOpenid(String openId);
}
