package io.github.vishalmysore.a2a.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents the content of a file, either as base64 encoded bytes or a URI.\n\n
 * Ensures that either 'bytes' or 'uri' is provided, but not both.
 */
@Entity
@Data
@Table(name = "file_info")
public class FileContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String mimeType;

    @Column(columnDefinition = "TEXT")
    private String bytes;  // base64 encoded

    private String uri;
}