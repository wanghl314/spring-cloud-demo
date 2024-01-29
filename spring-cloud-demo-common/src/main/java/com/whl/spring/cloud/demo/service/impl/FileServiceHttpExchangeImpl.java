package com.whl.spring.cloud.demo.service.impl;

import com.whl.spring.cloud.demo.bean.FileInfo;
import com.whl.spring.cloud.demo.service.FileService;
import com.whl.spring.cloud.demo.service.FileServiceHttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileServiceHttpExchangeImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceHttpExchangeImpl.class);

    private final FileServiceHttpExchange httpExchange;

    public FileServiceHttpExchangeImpl(FileServiceHttpExchange fileServiceV2) {
        this.httpExchange = fileServiceV2;
    }

    @Override
    public FileInfo upload(MultipartFile file) throws IOException {
        return this.upload(file, file.getOriginalFilename());
    }

    @Override
    public FileInfo upload(MultipartFile file, String name) throws IOException {
        return this.httpExchange.upload(file, name);
    }

    @Override
    public FileInfo download(String name) throws IOException {
        FileInfo fileInfo = this.httpExchange.get(name);

        if (fileInfo != null) {
            fileInfo.setFetchStream(() -> {
                try {
                    Resource resource = this.httpExchange.download(name);

                    if (resource != null) {
                        return resource.getInputStream();
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            });
        }
        return fileInfo;
    }

}
