package com.whl.spring.cloud.demo;

import java.io.IOException;
import java.io.InputStream;

import com.whl.spring.cloud.demo.bean.FileInfo;

public interface FileService {
    FileInfo upload(String name, InputStream is) throws IOException;

    InputStream download(String name) throws IOException;
}
