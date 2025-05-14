package io.github.vishalmysore.a2a.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskCancelParams {
    private String id; // Task ID to cancel
}
