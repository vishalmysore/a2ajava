package io.github.vishalmysore;

import lombok.Data;

@Data
class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;


    public Capabilities(boolean b, boolean b1, boolean b2) {
    }
}

