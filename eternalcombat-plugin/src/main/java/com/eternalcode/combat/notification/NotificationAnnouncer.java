package com.eternalcode.combat.notification;

import com.eternalcode.combat.notification.implementation.BossBarNotification;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

public final class NotificationAnnouncer {

    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public NotificationAnnouncer(AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void send(CommandSender sender, Notification notification, Formatter formatter) {
        Audience audience = this.audience(sender);
        Component message = this.miniMessage.deserialize(formatter.format(notification.message()));

        NotificationType type = notification.type();

        switch (type) {
            case CHAT -> audience.sendMessage(message);
            case ACTION_BAR -> audience.sendActionBar(message);

            case TITLE -> {
                Title title = Title.title(message, Component.empty());

                audience.showTitle(title);
            }

            case SUB_TITLE -> {
                Title subTitle = Title.title(Component.empty(), message);

                audience.showTitle(subTitle);
            }

            case BOSS_BAR -> {
                BossBarNotification bossBarNotification = (BossBarNotification) notification;
                BossBar bossBar = bossBarNotification.create(message);

                audience.showBossBar(bossBar);
            }

            default -> throw new IllegalStateException("Unknown notification type: " + type);
        }
    }

    public void sendMessage(CommandSender commandSender, String text) {
        Audience audience = this.audience(commandSender);
        Component message = this.miniMessage.deserialize(text);

        audience.sendMessage(message);
    }

    public void broadcast(String text) {
        Audience audience = this.audienceProvider.all();
        Component message = this.miniMessage.deserialize(text);

        audience.sendMessage(message);
    }

    private Audience audience(CommandSender sender) {
        if (sender instanceof Player player) {
            return this.audienceProvider.player(player.getUniqueId());
        }

        return this.audienceProvider.console();
    }
}
