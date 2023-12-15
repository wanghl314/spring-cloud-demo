package com.whl.spring.cloud.demo.provider.controller;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.cloud.commons.io.IOUtils;
import com.whl.spring.cloud.demo.bean.FileInfo;
import com.whl.spring.cloud.demo.util.FileUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ServerProperties serverProperties;

    @PostMapping("/upload")
    public FileInfo upload(@RequestParam("file") MultipartFile file, String name) throws Exception {
        logger.info("upload");
        name = StringUtils.defaultIfBlank(name, file.getOriginalFilename());
        String suffix = "";

        if (StringUtils.contains(name, '.')) {
            suffix = name.substring(name.lastIndexOf("."));
        }
        String storeFileName = UUID.randomUUID() + suffix;
        Path path = this.getFileStorePath();
        Path storePath = path.resolve(storeFileName);

        if (!Files.exists(storePath.getParent())) {
            Files.createDirectories(storePath.getParent());
        }
        file.transferTo(storePath);
        File storeFile = storePath.toFile();
        return new FileInfo(storeFileName, storeFile.length(), storeFile.lastModified(), file.getContentType());
    }

    @GetMapping("/get")
    public FileInfo get(HttpServletRequest request, HttpServletResponse response, @RequestParam String name) throws Exception {
        logger.info("get, {}", name);
        Path path = this.getFileStorePath();
        Path file = path.resolve(name);

        if (Files.exists(file)) {
            File f = file.toFile();
            long size = f.length();
            long lastModified = f.lastModified();
            String contentType = FileUtils.getContentType(f);
            return new FileInfo(name, size, lastModified, contentType);
        }
        return null;
    }


    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestParam String name) throws Exception {
        logger.info("download, {}", name);
        Path path = this.getFileStorePath();
        Path file = path.resolve(name);

        if (file != null) {
            File storeFile = file.toFile();
            response.setHeader("Content-Type", FileUtils.getContentType(storeFile));
            response.setHeader("Content-Disposition", "attachment; filename=" + name);
            response.setHeader("Content-Length", String.valueOf(storeFile.length()));
            response.setHeader("Cache-Control", "public,max-age=604800");

            try (OutputStream os = response.getOutputStream()) {
                IOUtils.copyLarge(Files.newInputStream(file), os);
                os.flush();
            }
            return;
        }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_NOT_FOUND);
        String errorPath = this.serverProperties.getError().getPath();
        request.getRequestDispatcher(errorPath).forward(request, response);
    }

    private Path getFileStorePath() {
        return Paths.get(System.getProperty("user.dir"), "upload");
    }

}
