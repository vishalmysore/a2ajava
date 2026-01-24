package io.github.vishalmysore.a2ui;

import io.github.vishalmysore.common.CommonClientResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class A2UIResponse implements CommonClientResponse {
    private Map<String, Object> responseData;

}
