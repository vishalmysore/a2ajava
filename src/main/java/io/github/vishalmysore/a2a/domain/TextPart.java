package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("text")
public class TextPart extends Part {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    private String type = "text";
    private String text;

    @ElementCollection
    @CollectionTable(name = "text_part_metadata",
            joinColumns = @JoinColumn(name = "part_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata = new HashMap<>();

    // Update getter and setter to use String instead of Object
    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    // Other getters and setters remain the same
    @Override
    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
}