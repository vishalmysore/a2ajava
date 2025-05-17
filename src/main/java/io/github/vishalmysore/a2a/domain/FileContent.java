package io.github.vishalmysore.a2a.domain;


import lombok.Data;
import lombok.ToString;

/**
 * Represents the content of a file, either as base64 encoded bytes or a URI.\n\n
 * Ensures that either 'bytes' or 'uri' is provided, but not both.
 */

@Data

@ToString
public class FileContent {

    private String id;

    private String name;
    private String mimeType;


    private String bytes;  // base64 encoded

    private String uri;
}