package com.eternalcode.combat.command;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.event.CauseOfTag;
import com.eternalcode.combat.fight.event.CauseOfUnTag;
import com.eternalcode.combat.fight.event.FightTagEvent;
import com.eternalcode.combat.fight.event.FightUntagEvent;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.time.Duration;
import java.util.UUID;

@Command(name = "combatlog", aliases = "combat")
public class CombatTagUntagCommand {

    private final FightManager fightManager;
    private final NotificationAnnouncer announcer;
    private final PluginConfig config;

    public CombatTagUntagCommand(FightManager fightManager, NotificationAnnouncer announcer, PluginConfig config) {
        this.fightManager = fightManager;
        this.announcer = announcer;
        this.config = config;
    }

    @Execute(name = "tag")
    @Permission("eternalcombat.tag")
    void tag(@Context CommandSender sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId(); 
        Duration time = this.config.settings.combatDuration;

        Formatter formatter = new Formatter()
            .register("{PLAYER}", target.getName());

        FightTagEvent event = this.fightManager.tag(targetUniqueId, time, CauseOfTag.COMMAND);

        if (event.isCancelled()) {
            this.announcer.sendMessage(sender, event.getCancelMessage());
            return;
        }

        String format = formatter.format(this.config.messages.admin.adminTagPlayer);
        this.announcer.sendMessage(sender, format);
    }

    @Execute(name = "tag")
    @Permission("eternalcombat.tag")
    void tagMultiple(@Context CommandSender sender, @Arg Player firstTarget, @Arg Player secondTarget) {
        Duration combatTime = this.config.settings.combatDuration;
        PluginConfig.Messages messages = this.config.messages;
        
        if (sender.equals(firstTarget) || sender.equals(secondTarget)) {
            this.announcer.sendMessage(sender, messages.admin.adminCannotTagSelf);
            return;
        }

        FightTagEvent firstTagEvent = this.fightManager.tag(firstTarget.getUniqueId(), combatTime, CauseOfTag.COMMAND);
        FightTagEvent secondTagEvent = this.fightManager.tag(secondTarget.getUniqueId(), combatTime, CauseOfTag.COMMAND);

        Formatter formatter = new Formatter()
            .register("{FIRST_PLAYER}", firstTarget.getName())
            .register("{SECOND_PLAYER}", secondTarget.getName());

        String format = formatter.format(messages.admin.adminTagMultiplePlayers);

        if (firstTagEvent.isCancelled()) {
            this.announcer.sendMessage(sender, firstTagEvent.getCancelMessage());
        }

        if (secondTagEvent.isCancelled()) {
            this.announcer.sendMessage(sender, firstTagEvent.getCancelMessage());
        }

        if (firstTagEvent.isCancelled() && secondTagEvent.isCancelled()) {
            return;
        }

        this.announcer.sendMessage(sender, format);
    }

    @Execute(name = "untag")
    @Permission("eternalcombat.untag")
    void untag(@Context Player sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();

        if (!this.fightManager.isInCombat(targetUniqueId)) {
            this.announcer.sendMessage(sender, this.config.messages.admin.adminPlayerNotInCombat);
            return;
        }

        FightUntagEvent event = this.fightManager.untag(targetUniqueId, CauseOfUnTag.COMMAND);
        if (event.isCancelled()) {
            return;
        }

        Formatter formatter = new Formatter()
            .register("{PLAYER}", target.getName());

        String format = formatter.format(this.config.messages.admin.adminUntagPlayer);

        this.announcer.sendMessage(sender, format);
    }
}