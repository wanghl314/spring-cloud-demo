package com.whl.spring.cloud.demo.bean;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

public class FileInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8456789503153085438L;

    private String name;

    private long size;

    private long lastModified;

    private String contentType;

    transient private Supplier<InputStream> fetchStream;

    public FileInfo() {
    }

    public FileInfo(String name, long size, long lastModified, String contentType) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        this.contentType = contentType;
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

    public void setFetchStream(Supplier<InputStream> fetchStream) {
        this.fetchStream = fetchStream;
    }

    public InputStream getInputStream() {
        if (this.fetchStream != null) {
            return this.fetchStream.get();
        }
        return null;
    }

}
