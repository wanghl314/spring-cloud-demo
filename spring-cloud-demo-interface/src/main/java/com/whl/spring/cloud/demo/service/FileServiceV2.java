package com.whl.spring.cloud.demo.service;

import com.whl.spring.cloud.demo.bean.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;

@HttpExchange("http://spring-cloud-demo-provider/file")
public interface FileServiceV2 {
    @PostExchange("/upload")
    FileInfo upload(@RequestPart("file") MultipartFile file) throws IOException;

    @PostExchange("/upload")
    FileInfo upload(@RequestPart("file") MultipartFile file, @RequestParam("name") String name) throws IOException;

    @GetExchange("/get/{name}")
    FileInfo get(@PathVariable String name) throws IOException;

    @GetExchange("/download/{name}")
    Resource download(@PathVariable String name) throws IOException;
}
