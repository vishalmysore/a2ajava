package io.github.vishalmysore;

class Capabilities {
    private boolean streaming;
    private boolean pushNotifications;
    private boolean stateTransitionHistory;

    public Capabilities() {
    }

    public Capabilities(boolean streaming, boolean pushNotifications, boolean stateTransitionHistory) {
        this.streaming = streaming;
        this.pushNotifications = pushNotifications;
        this.stateTransitionHistory = stateTransitionHistory;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public boolean isPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public boolean isStateTransitionHistory() {
        return stateTransitionHistory;
    }

    public void setStateTransitionHistory(boolean stateTransitionHistory) {
        this.stateTransitionHistory = stateTransitionHistory;
    }
}

