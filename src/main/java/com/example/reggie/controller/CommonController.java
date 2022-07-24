package com.example.reggie.controller;

import com.example.reggie.common.FastFileStorageClientUtil;
import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
    public R<String> upload(MultipartFile file){
        String s = fileStorageClientUtil.uploadFileWithOutMetaData(file);
        return R.success(s);
    }

    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response){

    }
}
