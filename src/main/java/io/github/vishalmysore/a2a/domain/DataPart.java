package io.github.vishalmysore.a2a.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;
@Entity
@DiscriminatorValue("data")
public class DataPart extends Part {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    private String type = "data";

    @Transient
    private Map<String, Object> data;
    @ElementCollection
    @CollectionTable(name = "data_part_metadata",
            joinColumns = @JoinColumn(name = "part_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value", columnDefinition = "TEXT")
    private Map<String, String> metadata;

    public String getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
