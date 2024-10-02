package com.eternalcode.combat.notification.serializer;

import com.eternalcode.combat.notification.Notification;
import com.eternalcode.combat.notification.NotificationType;
import com.eternalcode.combat.notification.implementation.ActionBarNotification;
import com.eternalcode.combat.notification.implementation.BossBarNotification;
import com.eternalcode.combat.notification.implementation.ChatNotification;
import com.eternalcode.combat.notification.implementation.SubTitleNotification;
import com.eternalcode.combat.notification.implementation.TitleNotification;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.bossbar.BossBar;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class NotificationSerializer implements ObjectSerializer<Notification> {
    
    @Override
    public boolean supports(@NonNull Class<? super Notification> type) {
        return Notification.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Notification notification, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", notification.type(), NotificationType.class);
        data.add("message", notification.message(), String.class);

        if (notification instanceof BossBarNotification bossBarNotification) {
            data.add("progress", bossBarNotification.progress(), float.class);
            data.add("color", bossBarNotification.color(), BossBar.Color.class);
            data.add("overlay", bossBarNotification.overlay(), BossBar.Overlay.class);
        }
    }

    @Override
    public Notification deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        NotificationType type = data.get("type", NotificationType.class);
        String message = data.get("message", String.class);

        return switch (type) {
            case CHAT -> new ChatNotification(message);
            case ACTION_BAR -> new ActionBarNotification(message);

            case TITLE -> new TitleNotification(message);
            case SUB_TITLE -> new SubTitleNotification(message);

            case BOSS_BAR -> {
                float progress = Optional.ofNullable(data.get("progress", float.class))
                    .orElse(BossBar.MAX_PROGRESS);

                BossBar.Color color = Optional.ofNullable(data.get("color", BossBar.Color.class))
                    .orElse(BossBar.Color.RED);

                BossBar.Overlay overlay = Optional.ofNullable(data.get("overlay", BossBar.Overlay.class))
                    .orElse(BossBar.Overlay.PROGRESS);

                yield new BossBarNotification(message, progress, color, overlay);
            }
        };
    }
}
