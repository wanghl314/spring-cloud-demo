package com.whl.spring.cloud.demo.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.whl.spring.cloud.demo.bean.FileInfo;
import com.whl.spring.cloud.demo.service.FileService;

public class FileServiceImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private final RestTemplate restTemplate;

    public FileServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FileInfo upload(MultipartFile file) throws IOException {
        return this.upload(file, file.getOriginalFilename());
    }

    @Override
    public FileInfo upload(MultipartFile file, String name) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
        form.add("file", file.getResource());
        form.add("name", name);

        return this.restTemplate.postForObject("http://spring-cloud-demo-provider/file/upload", new HttpEntity<>(form, headers), FileInfo.class);
    }

    @Override
    public FileInfo download(String name) throws IOException {
        FileInfo fileInfo = this.restTemplate.getForObject("http://spring-cloud-demo-provider/file/get?name=" + name, FileInfo.class);

        if (fileInfo != null) {
            fileInfo.setFetchStream(() -> {
                try {
                    URI uri = new URI("http://spring-cloud-demo-provider/file/download?name=" + name);
                    ClientHttpRequest httpRequest = restTemplate.getRequestFactory().createRequest(uri, HttpMethod.GET);
                    ClientHttpResponse httpResponse = httpRequest.execute();
                    return httpResponse.getBody();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            });
        }
        return fileInfo;
    }

}
