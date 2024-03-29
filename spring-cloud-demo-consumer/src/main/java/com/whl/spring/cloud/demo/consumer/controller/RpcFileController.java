package com.whl.spring.cloud.demo.consumer.controller;

import com.alibaba.cloud.commons.io.IOUtils;
import com.whl.spring.cloud.demo.bean.FileInfo;
import com.whl.spring.cloud.demo.service.FileService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/rpc/file")
public class RpcFileController {
    private static Logger logger = LoggerFactory.getLogger(RpcFileController.class);

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    @Qualifier("fileServiceHttpExchangeRestTemplateImpl")
    private FileService fileServiceHttpExchangeRestTemplateImpl;

    @Autowired(required = false)
    @Qualifier("fileServiceHttpExchangeRestClientImpl")
    private FileService fileServiceHttpExchangeRestClientImpl;

    @Autowired(required = false)
    @Qualifier("fileServiceHttpExchangeWebClientImpl")
    private FileService fileServiceHttpExchangeWebClientImpl;

    @PostMapping("/upload")
    public FileInfo testUpload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        logger.info("upload");

        if (this.fileServiceHttpExchangeWebClientImpl != null) {
            return this.fileServiceHttpExchangeWebClientImpl.upload(file, file.getOriginalFilename());
        } else if (this.fileServiceHttpExchangeRestClientImpl != null) {
            return this.fileServiceHttpExchangeRestClientImpl.upload(file, file.getOriginalFilename());
        }
        return this.fileServiceHttpExchangeRestTemplateImpl.upload(file, file.getOriginalFilename());
    }

    @GetMapping("/download/{name}")
    public void testDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String name) throws Exception {
        logger.info("download, {}", name);
        FileInfo fileInfo = null;

        try {
            fileInfo = this.fileServiceHttpExchangeRestTemplateImpl.download(name);
            logger.info("HttpExchangeRestTemplate: {}", fileInfo);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        if (this.fileServiceHttpExchangeRestClientImpl != null) {
            try {
                fileInfo = this.fileServiceHttpExchangeRestClientImpl.download(name);
                logger.info("HttpExchangeRestClient: {}", fileInfo);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (this.fileServiceHttpExchangeWebClientImpl != null) {
            try {
                fileInfo = this.fileServiceHttpExchangeWebClientImpl.download(name);
                logger.info("HttpExchangeWebClient: {}", fileInfo);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (fileInfo != null) {
            response.setHeader("Content-Type", fileInfo.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" + name);
            response.setHeader("Content-Length", String.valueOf(fileInfo.getSize()));
            response.setHeader("Cache-Control", "public,max-age=604800");

            try (OutputStream os = response.getOutputStream()) {
                IOUtils.copyLarge(fileInfo.getInputStream(), os);
                os.flush();
            }
            return;
        }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_NOT_FOUND);
        String errorPath = this.serverProperties.getError().getPath();
        request.getRequestDispatcher(errorPath).forward(request, response);
    }

}
