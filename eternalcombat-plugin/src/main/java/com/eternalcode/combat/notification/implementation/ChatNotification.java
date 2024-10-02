package com.eternalcode.combat.notification.implementation;

import com.eternalcode.combat.notification.Notification;
import com.eternalcode.combat.notification.NotificationType;

public record ChatNotification(String message) implements Notification {

    @Override
    public NotificationType type() {
        return NotificationType.CHAT;
    }
}
