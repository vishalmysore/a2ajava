package io.github.vishalmysore.domain;

import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private String mimeType;
    private String bytes;  // base64 encoded
    private String uri;


}