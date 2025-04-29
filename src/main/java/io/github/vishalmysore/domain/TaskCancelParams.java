package io.github.vishalmysore.domain;

import lombok.Data;

@Data
public class TaskCancelParams {
    private String id; // Task ID to cancel
}
