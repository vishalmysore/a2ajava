package io.github.vishalmysore.a2a.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "file_info")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String mimeType;

    @Column(columnDefinition = "TEXT")
    private String bytes;  // base64 encoded

    private String uri;
}