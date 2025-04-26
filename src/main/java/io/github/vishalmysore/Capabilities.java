package io.github.vishalmysore;

import lombok.Data;

@Data
class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;


}

