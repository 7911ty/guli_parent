package com.lty.educms.service;

import com.lty.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author lty
 * @since 2021-01-13
 */
public interface CrmBannerService extends IService<CrmBanner> {


    List<CrmBanner> selectAllBanner();
}
