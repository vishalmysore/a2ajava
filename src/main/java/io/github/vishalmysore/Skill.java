package io.github.vishalmysore;

class Skill {
    private String id;
    private String name;
    private String description;
    private String[] tags;
    private String[] examples;
    private String[] inputModes;
    private String[] outputModes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getExamples() {
        return examples;
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
    }

    public String[] getInputModes() {
        return inputModes;
    }

    public void setInputModes(String[] inputModes) {
        this.inputModes = inputModes;
    }

    public String[] getOutputModes() {
        return outputModes;
    }

    public void setOutputModes(String[] outputModes) {
        this.outputModes = outputModes;
    }
}

