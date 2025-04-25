package io.github.vishalmysore;

class TaskPushNotificationConfigRequest {
    private String id;
    private TaskPushNotificationConfig pushNotificationConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskPushNotificationConfig getPushNotificationConfig() {
        return pushNotificationConfig;
    }

    public void setPushNotificationConfig(TaskPushNotificationConfig pushNotificationConfig) {
        this.pushNotificationConfig = pushNotificationConfig;
    }
}

