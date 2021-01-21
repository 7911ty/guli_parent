package com.lty.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lty.oss.service.OssService;
import com.lty.oss.utils.ConstantPropertiesUtil;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceimpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        //返回的地址
        String url = "";
        // 创建OSSClient实例。
        OSS ossClient = null;
        try {

            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取文件上传输入流
            InputStream inputStream = file.getInputStream();
            //1.在文件里面添加唯一的值 , replaceAll("-","");将uuid生成名字中"-"去掉
            val uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String fileName = file.getOriginalFilename();
            //使用new Date(),如果并发量大会,在同一毫秒上传相同名字的文件会进行覆盖
            //使用UUID
            // 获取文件名称
            //String fileName =new Date() + file.getOriginalFilename() ;
            fileName = uuid + fileName;
            //2.把文件按日期进行分类
            // 2020/12/27/01.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName;

            // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);


            //第一个参数: Bucket名称
            //第二个参数: 上传到oss的文件路径和文件名称
            //第三个参数: 上传文件输入流
            // 上传文件。
            ossClient.putObject(bucketName, fileName, inputStream);

            //把上传文件的路径返回
            //需要手动拼接出来
            url = "http://" + bucketName + "." + endpoint + "/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        return url;
    }
}
