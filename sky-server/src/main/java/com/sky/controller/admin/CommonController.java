package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            // 根据原始文件名取后缀，生成 OSS 对象名
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = dir + "/" + UUID.randomUUID() + ext;
            String url = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
