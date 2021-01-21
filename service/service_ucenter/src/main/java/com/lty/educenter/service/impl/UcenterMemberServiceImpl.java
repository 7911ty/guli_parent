package com.lty.educenter.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lty.commonutils.JwtUtils;
import com.lty.commonutils.MD5;
import com.lty.educenter.entity.UcenterMember;
import com.lty.educenter.entity.vo.RegisterVo;
import com.lty.educenter.mapper.UcenterMemberMapper;
import com.lty.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.servicebase.exceptionhandler.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author lty
 * @since 2021-01-15
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();
        //首先验证前端传过来的用户对象是否有手机号和密码
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuLiException(2001,"登陆失败");
        }

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        //通过basemapper，查询给手机号对应的用户对象
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);

        //对查询到的对象进行校验//校验手机号
        if (ucenterMember == null){
            throw new GuLiException(2001,"登陆失败");
        }

        //检验密码
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuLiException(2001,"登陆失败");
        }
        //验证是否被限制
        if (ucenterMember.getIsDeleted()){
            throw new GuLiException(2001,"登陆失败");
        }
        //走到这就是可以登录
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getMobile(), ucenterMember.getPassword());

        return jwtToken;
    }
    //用户注册方法
    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();     //验证码
        String mobile = registerVo.getMobile(); //手机号
        String password = registerVo.getPassword();//密码
        String nickname = registerVo.getNickname();//昵称
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) ||StringUtils.isEmpty(password) ){
            throw new GuLiException(20001,"注册失败。");
        }
        //验证码校验
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobileCode)){
            throw new GuLiException(20001,"注册失败。");
        }
        //手机号验证，验证是否已经注册
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count.intValue()>0){
            throw new GuLiException(20001,"手机号已经注册，请登录。");
        }
        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        baseMapper.insert(member);
    }
    //通过token获取用户信息
    @Override
    public UcenterMember getLoginInfo(String memberId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",memberId);
        UcenterMember member = baseMapper.selectOne(wrapper);

        return member;

    }

    //通过接口利用openid查询该该微信是否注册过
    @Override
    public UcenterMember getByOpenid(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        return ucenterMember;
    }
}
