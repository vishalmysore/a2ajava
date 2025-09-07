package io.github.vishalmysore.common.test;

import io.github.vishalmysore.common.CommonClientResponse;
import java.util.Map;

public class TestCommonClientResponse implements CommonClientResponse {
    private String textResult;
    private String error;
    private Map<String, Object> jsonData;

    @Override
    public String getTextResult() {
        return textResult;
    }

    public void setTextResult(String textResult) {
        this.textResult = textResult;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Object> getJsonData() {
        return jsonData;
    }

    public void setJsonData(Map<String, Object> jsonData) {
        this.jsonData = jsonData;
    }
}
