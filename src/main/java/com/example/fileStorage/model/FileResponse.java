package com.example.fileStorage.model;

public class FileResponse {
    private String key;
    private String url;
    private String contentType;
    private Long size;

    public FileResponse() {
    }

    public FileResponse(String key, String url, String contentType, Long size) {
        this.key = key;
        this.url = url;
        this.contentType = contentType;
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}