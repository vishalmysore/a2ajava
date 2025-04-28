package io.github.vishalmysore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;



}

