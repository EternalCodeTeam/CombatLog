package com.eternalcode.combat.notification.implementation;

import com.eternalcode.combat.notification.Notification;
import com.eternalcode.combat.notification.NotificationType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public record BossBarNotification(String message, float progress, BossBar.Color color, BossBar.Overlay overlay) implements Notification {

    public BossBar create(Component name) {
        float replacedProgress = this.progress < 0.0F ? BossBar.MAX_PROGRESS : this.progress;

        return BossBar.bossBar(name, replacedProgress, this.color, this.overlay);
    }

    @Override
    public NotificationType type() {
        return NotificationType.BOSS_BAR;
    }
}
