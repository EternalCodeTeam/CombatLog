package com.eternalcode.combat.command.handler;

import com.eternalcode.combat.notification.NotificationAnnouncer;
import com.eternalcode.combat.config.implementation.PluginConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;
import panda.utilities.text.Joiner;

public class PermissionMessage implements PermissionHandler<CommandSender> {

    private final PluginConfig config;
    private final NotificationAnnouncer notificationAnnouncer;

    public PermissionMessage(PluginConfig config, NotificationAnnouncer notificationAnnouncer) {
        this.config = config;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        String value = Joiner.on(", ")
            .join(requiredPermissions.getPermissions())
            .toString();

        Formatter formatter = new Formatter()
            .register("{PERMISSION}", value);

        this.notificationAnnouncer.sendMessage(commandSender, formatter.format(this.config.messages.noPermission));
    }

}
