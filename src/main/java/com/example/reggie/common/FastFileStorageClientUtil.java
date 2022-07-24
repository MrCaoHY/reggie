package com.example.reggie.common;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: reggie
 * @description: dfs工具类
 * @author: CaoHaiyang
 * @create: 2022-07-24 17:13
 **/
@Slf4j
@Component
public class FastFileStorageClientUtil {
    @Autowired
    protected FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Value("${fdfs.reqHost}")
    private String reqHost;

    @Value("${fdfs.reqPort}")
    private String reqPort;


    /*******************************************上传文件/图片***********************************************************/

    /**
     * 上传文件 并且设置 MetaData
     *
     * @param file
     * @param metaDataSet
     * @return
     */
    public String uploadFileAndMetaData(MultipartFile file, Set<MetaData> metaDataSet) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            //上传文件和MetaData
            StorePath storePath = storageClient.uploadFile(in, file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
            //获取 MetaData
            //Set<MetaData> metadata = storageClient.getMetadata(storePath.getGroup(), storePath.getPath());
            //删除文件
            //storageClient.deleteFile(storePath.getGroup(),storePath.getPath());

            //上传文件路径
            return getResAccessUrl(storePath);
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 上传文件 不带 MetaData
     *
     * @param file
     * @return
     */
    public String uploadFileWithOutMetaData(MultipartFile file) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            //上传文件和MetaData
            StorePath storePath = storageClient.uploadFile(in, file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);
            //删除文件
            //storageClient.deleteFile(storePath.getGroup(),storePath.getPath());
            String fileUrl = getResAccessUrl(storePath);
            log.info("上传文件可访问路径：" + fileUrl);
            //上传文件路径
//            return fileUrl;
            return storePath.getFullPath();
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
            //throw new CustomException("文件上传失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 上传图片 并 生成缩略图
     * 缩略图为上传文件名 + 缩略图后缀 _150×150，如xxx.jpg，缩略图为 xxx_150×150.jpg
     * <p>
     * 实际样例如下：
     * http://localhost:8098/M00/00/17/rBEAAl33pQaAWNQNAAHYvQQn-YE374.jpg
     * http://localhost:8098/M00/00/17/rBEAAl33pQaAWNQNAAHYvQQn-YE374_150x150.jpg
     *
     * @param file
     * @return
     */
    public String uploadImageAndCrtThumbImage(MultipartFile file) {
        StorePath storePath = null;
        InputStream in = null;
        //StringBuilder stringBuilder = new StringBuilder();
        try {
            in = file.getInputStream();
            storePath = storageClient.uploadImageAndCrtThumbImage(in, file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);
            //获取从文件路径  即缩略图路径
            String slavePath = thumbImageConfig.getThumbImagePath(storePath.getPath());
            String masterUrl = getResAccessUrl(storePath);
            log.info("图片路径：" + storePath);
            log.info("图片可访问路径：" + masterUrl);
            log.info("缩略图路径：" + slavePath);
            return masterUrl;
        } catch (IOException e) {
            log.error("上传图片及生成缩略图失败：" + e.getMessage());
        } finally {
            //关流
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("输入流关闭失败：" + e.getMessage());
                }
            }
        }
        return null;
    }


    /**
     * 下载文件
     *
     * @param groupName
     * @param path
     * @return
     */
    public InputStream download(String groupName, String path) {
        InputStream ins = null;
        try {
            ins = storageClient.downloadFile(groupName, path, new DownloadCallback<InputStream>() {
                @Override
                public InputStream recv(InputStream ins) throws IOException {
                    // 将此ins返回给上面的ins
                    return ins;
                }
            });
        } catch (FdfsServerException e) {
            //不起作用
            log.error("文件不存在，下载失败：" + e.getErrorCode());
            throw new CustomException("文件不存在，下载失败：" + e.getErrorCode());
        }
        return null;
    }

    /*******************************************删除文件**********************************************************/
    public void delFile(String filePath) {
        storageClient.deleteFile(filePath);
    }


    /*******************************************公共方法**********************************************************/

    /**
     * 创建 元数据
     *
     * @return
     */
    private Set<MetaData> createMetaData() {
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "tobato"));
        metaDataSet.add(new MetaData("CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return metaDataSet;
    }

    /**
     * 封装文件完整URL地址
     *
     * @param storePath
     * @return
     */
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = "http://" + reqHost + ":" + reqPort + "/" + storePath.getFullPath();
        return fileUrl;
    }

}


