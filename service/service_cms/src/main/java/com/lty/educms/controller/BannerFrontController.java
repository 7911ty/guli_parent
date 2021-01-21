package com.lty.educms.controller;


import com.lty.commonutils.R;
import com.lty.educms.entity.CrmBanner;
import com.lty.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端显示时调用接口
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin  //解决跨域问题
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> list =  bannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

