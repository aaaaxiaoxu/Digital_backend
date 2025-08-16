package com.dj.digitalplatform.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import com.dj.digitalplatform.config.CosClientConfig;
import com.dj.digitalplatform.exception.BusinessException;
import com.dj.digitalplatform.exception.ErrorCode;
import com.dj.digitalplatform.exception.ThrowUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    /**
     * Delete temporary file
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        // Delete temporary file
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }

    /**
     * Upload image
     *
     * @param multipartFile File
     * @param uploadPathPrefix Upload path prefix
     * @return Image URL
     */
    public String uploadImage(MultipartFile multipartFile, String uploadPathPrefix) {
        // Image upload path
        String uuid = RandomUtil.randomString(16);
        String originFilename = multipartFile.getOriginalFilename();
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;

        try {
            // Create temporary file
            file = File.createTempFile("temp_", "." + FileUtil.getSuffix(originFilename));
            multipartFile.transferTo(file);

            // Upload image
            cosManager.putObject(uploadPath, file);
            return cosClientConfig.getHost() + uploadPath;
        } catch (Exception e) {
            log.error("Image upload to object storage failed", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Upload failed");
        } finally {
            this.deleteTempFile(file);
        }
    }

    /**
     * Validate image file
     */
    public void validImage(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "File cannot be empty");
        // Check file size
        long fileSize = multipartFile.getSize();
        final long FIVE_M = 5 * 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > FIVE_M, ErrorCode.PARAMS_ERROR, "Image size cannot exceed 5M");
        // Check file extension
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // Allowed image file extensions
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix),
                ErrorCode.PARAMS_ERROR, "Image file type error");
    }
}