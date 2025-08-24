package com.dj.digitalplatform.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.dj.digitalplatform.config.CosClientConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


import java.io.File;

@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * Upload object
     *
     * @param key  Unique key
     * @param file File
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * Download object
     *
     * @param key Unique key
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }
}