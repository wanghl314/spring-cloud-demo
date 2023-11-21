package com.whl.spring.cloud.demo.bean;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

public class FileInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8456789503153085438L;

    private String name;

    private long size;

    private long lastModified;

    private String contentType;

    private InputStream inputStream;

    public FileInfo() {
    }

    public FileInfo(String name, long size, long lastModified, String contentType, InputStream inputStream) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
