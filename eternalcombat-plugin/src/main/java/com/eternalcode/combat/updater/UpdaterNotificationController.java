package com.eternalcode.combat.updater;

import com.eternalcode.combat.config.implementation.PluginConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.CompletableFuture;

public class UpdaterNotificationController implements Listener {

    private static final String NEW_VERSION_AVAILABLE = "<b><gradient:#8a1212:#fc6b03>EternalCombat:</gradient></b> <color:#fce303>New version of EternalCombat is available, please update!";
    private static final String RECEIVE_UPDATES_PERMISSION = "eternalcombat.receiveupdates";

    private final UpdaterService updaterService;
    private final PluginConfig pluginConfig;
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public UpdaterNotificationController(UpdaterService updaterService, PluginConfig pluginConfig, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.updaterService = updaterService;
        this.pluginConfig = pluginConfig;
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Audience audience = this.audienceProvider.player(player.getUniqueId());

        if (!player.hasPermission(RECEIVE_UPDATES_PERMISSION) || !this.pluginConfig.settings.shouldReceivePluginUpdates) {
            return;
        }

        CompletableFuture<Boolean> upToDate = this.updaterService.isUpToDate();

        upToDate.whenComplete((isUpToDate, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();

                return;
            }

            if (!isUpToDate) {
                audience.sendMessage(this.miniMessage.deserialize(NEW_VERSION_AVAILABLE));
            }
        });
    }

}
