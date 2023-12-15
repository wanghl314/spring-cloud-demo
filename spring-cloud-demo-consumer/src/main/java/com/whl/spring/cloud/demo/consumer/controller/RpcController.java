package com.whl.spring.cloud.demo.consumer.controller;

import java.io.OutputStream;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.cloud.commons.io.IOUtils;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.whl.spring.cloud.demo.DemoService;
import com.whl.spring.cloud.demo.FileService;
import com.whl.spring.cloud.demo.UserService;
import com.whl.spring.cloud.demo.bean.FileInfo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rpc")
public class RpcController {
    private static Logger logger = LoggerFactory.getLogger(RpcController.class);

    @Autowired
    private ServerProperties serverProperties;

    @DubboReference
    private UserService userService;

    @DubboReference(group = "test")
    private DemoService demoService;

    @Autowired
    private FileService fileService;

    @GetMapping("/user")
    public String sayHello(String name) throws Exception {
        logger.info("sayHello, {}", name);
        this.userService.sayHello(name);
        return "SUCCESS";
    }

    @GetMapping("/demo")
    public String test(String name) throws Exception {
        logger.info("test, {}", name);
        return this.demoService.test(name);
    }

    @GetMapping("/demo2")
    public String test2(String name) throws Exception {
        logger.info("test2, {}", name);
        return this.demoService.test2(name);
    }

    @GetMapping("/demo3")
    public String test3(String name) throws Exception {
        logger.info("test3, {}", name);
        return this.demoService.test3(name);
    }

    @PostMapping("/upload")
    public FileInfo testUpload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        logger.info("upload");
        return this.fileService.upload(file, file.getOriginalFilename());
    }

    @GetMapping("/download/{name}")
    public void testDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String name) throws Exception {
        logger.info("download, {}", name);
        FileInfo fileInfo = this.fileService.download(name);

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

    @GetMapping("/testDegrade")
    public String testDegrade(String name) throws Exception {
        logger.info("testDegrade, {}", name);

        try {
            return this.demoService.testDegrade(name);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private String handleException(Exception e) throws Exception {
        boolean degrade = false;
        Throwable t = e;

        do {
            if (t instanceof DegradeException) {
                degrade = true;
                break;
            }
            t = t.getCause();
        } while (t != null);

        if (!degrade && e instanceof RuntimeException &&
                (BlockException.BLOCK_EXCEPTION_MSG_PREFIX + DegradeException.class.getSimpleName()).equals(e.getMessage())) {
            degrade = true;
        }

        if (degrade) {
            return "Dubbo: 触发降级保护";
        }
        throw e;
    }

}
