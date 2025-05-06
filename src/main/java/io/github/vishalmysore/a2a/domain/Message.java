package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;

    private String role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private List<Part> parts;

    @ElementCollection
    @CollectionTable(name = "message_metadata",
            joinColumns = @JoinColumn(name = "message_id"))
    @MapKeyColumn(name = "metadata_key", columnDefinition = "TEXT")
    @Column(name = "metadata_value")
    private Map<String, String> metadata;
}
