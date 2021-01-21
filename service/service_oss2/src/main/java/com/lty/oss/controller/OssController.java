package com.lty.oss.controller;

import com.lty.commonutils.R;
import com.lty.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    /*
    * @Autowired service层忘了加@service
    * */
    @Autowired
    private OssService ossService;

    //上传头像的方法
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //MultipartFile 上传文件
        //url为返回的上传到oss的路径
        String url =  ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
