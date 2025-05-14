package io.github.vishalmysore.a2a.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;



}

