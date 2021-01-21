package com.lty.oss.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface OssService {
    //上传文件到oss
    String uploadFileAvatar(MultipartFile file);
}
