package com.whl.spring.cloud.demo;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.whl.spring.cloud.demo.bean.FileInfo;

public interface FileService {
    FileInfo upload(MultipartFile file) throws IOException;

    FileInfo upload(MultipartFile file, String name) throws IOException;

    FileInfo download(String name) throws IOException;
}
