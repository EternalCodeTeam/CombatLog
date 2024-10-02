package com.eternalcode.combat.fight.tagout;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import com.eternalcode.combat.util.DurationUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

@Command(name = "tagout", aliases = "tagimmunity")
public class FightTagOutCommand {

    private final FightTagOutService fightTagOutService;
    private final NotificationAnnouncer announcer;
    private final PluginConfig config;

    public FightTagOutCommand(
        FightTagOutService fightTagOutService,
        NotificationAnnouncer announcer,
        PluginConfig config
    ) {
        this.fightTagOutService = fightTagOutService;
        this.announcer = announcer;
        this.config = config;
    }

    @Execute
    @Permission("eternalcombat.tagout")
    void tagout(@Context Player sender, @Arg Duration time) {
        UUID targetUniqueId = sender.getUniqueId();

        Formatter formatter = new Formatter()
            .register("{PLAYER}", sender.getName())
            .register("{TIME}", DurationUtil.format(time));

        this.fightTagOutService.tagOut(targetUniqueId, time);

        String format = formatter.format(this.config.messages.admin.adminTagOutSelf);
        this.announcer.sendMessage(sender, format);
    }

    @Execute
    @Permission("eternalcombat.tagout")
    void tagout(@Context Player sender, @Arg Player target, @Arg Duration time) {
        UUID targetUniqueId = target.getUniqueId();

        Instant now = Instant.now();
        Duration remaining = Duration.between(now, now.plus(time));

        Formatter formatter = new Formatter()
            .register("{PLAYER}", target.getName())
            .register("{TIME}", DurationUtil.format(remaining));

        this.fightTagOutService.tagOut(targetUniqueId, time);

        String adminTagOutFormat = formatter.format(this.config.messages.admin.adminTagOut);
        this.announcer.sendMessage(sender, adminTagOutFormat);

        String playerTagOutFormat = formatter.format(this.config.messages.admin.playerTagOut);
        this.announcer.sendMessage(target, playerTagOutFormat);
    }

    @Execute(name = "remove")
    @Permission("eternalcombat.tagout")
    void untagout(@Context Player sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();

        this.fightTagOutService.unTagOut(targetUniqueId);

        Formatter formatter = new Formatter()
            .register("{PLAYER}", target.getName());

        if (!targetUniqueId.equals(sender.getUniqueId())) {
            String adminUnTagOutFormat = formatter.format(this.config.messages.admin.adminTagOutOff);
            this.announcer.sendMessage(sender, adminUnTagOutFormat);
        }

        this.announcer.sendMessage(target, this.config.messages.admin.playerTagOutOff);
    }

    @Execute(name = "remove")
    @Permission("eternalcombat.tagout")
    void untagout(@Context Player sender) {
        UUID senderUniqueId = sender.getUniqueId();

        this.fightTagOutService.unTagOut(senderUniqueId);

        this.announcer.sendMessage(sender, this.config.messages.admin.playerTagOutOff);
    }
}

