package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.util.Map;

@Entity
@DiscriminatorValue("file")
public class FilePart extends Part {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    private String type = "file";
    @OneToOne
    private FileContent file;
    @ElementCollection
    @CollectionTable(name = "file_part_metadata",
            joinColumns = @JoinColumn(name = "part_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata;

    public String getType() {
        return type;
    }

    public FileContent getFile() {
        return file;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setFile(FileContent file) {
        this.file = file;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
