package com.example.reggie.controller;

import com.example.reggie.common.FastFileStorageClientUtil;
import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @program: reggie
 * @description:
 * @author: CaoHaiyang
 * @create: 2022-07-24 18:21
 **/
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private FastFileStorageClientUtil fileStorageClientUtil;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String s = fileStorageClientUtil.uploadFileWithOutMetaData(file);
        return R.success(s);
    }

    @GetMapping(value = "/download", produces = "image/jpeg")
    public void download(String name, HttpServletResponse response) {
        InputStream inputStream = fileStorageClientUtil.download("group1", name);
        try (
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            log.error("下载图片失败{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
