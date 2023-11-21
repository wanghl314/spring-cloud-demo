package com.whl.spring.cloud.demo.util;

import java.io.File;
import java.nio.file.Path;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

public class FileUtils {

    public static String getContentType(Path path) {
        return getContentType(path.toFile());
    }

    public static String getContentType(File file) {
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (file != null && file.exists()) {
            try {
                MediaType mediaType = MediaTypeFactory.getMediaType(new FileSystemResource(file)).orElse(MediaType.APPLICATION_OCTET_STREAM);
                contentType = mediaType.toString();
            } catch (Exception ignored) {
            }
        }
        return contentType;
    }

}
