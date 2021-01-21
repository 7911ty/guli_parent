package com.lty.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.commonutils.R;
import com.lty.educms.entity.CrmBanner;
import com.lty.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lty
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin  //解决跨域问题
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //分页查询
    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        bannerService.page(pageBanner,null);

        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }
    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }
    //增加轮播图
    @ApiOperation(value = "新增Banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }
    //删除
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("removeBanner/{id}")
    public R removeBanner(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }
}

