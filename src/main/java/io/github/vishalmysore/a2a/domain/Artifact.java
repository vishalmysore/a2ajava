package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@ToString
public class Artifact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "artifact_id")
    private List<Part> parts;
    @ElementCollection
    @CollectionTable(name = "artifact_metadata",
            joinColumns = @JoinColumn(name = "artifact_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata = new HashMap<>();
    private int index;
    private boolean append;
    private boolean lastChunk;


}
