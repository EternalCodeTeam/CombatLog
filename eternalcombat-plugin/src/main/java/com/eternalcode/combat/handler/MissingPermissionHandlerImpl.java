package com.eternalcode.combat.handler;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

public class MissingPermissionHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    private final PluginConfig config;
    private final NotificationAnnouncer announcer;

    public MissingPermissionHandlerImpl(PluginConfig config, NotificationAnnouncer announcer) {
        this.config = config;
        this.announcer = announcer;
    }

    @Override
    public void handle(
        Invocation<CommandSender> invocation,
        MissingPermissions missingPermissions,
        ResultHandlerChain<CommandSender> resultHandlerChain
    ) {
        String joinedText = missingPermissions.asJoinedText();

        Formatter formatter = new Formatter()
            .register("{PERMISSION}", joinedText);

        this.announcer.sendMessage(invocation.sender(), formatter.format(this.config.messages.noPermission));
    }
}
