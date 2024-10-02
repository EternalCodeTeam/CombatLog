package com.eternalcode.combat.fight;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.event.CancelTagReason;
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
import java.time.Duration;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

@Command(name = "combatlog", aliases = "combat")
public class FightTagCommand {

    private final FightManager fightManager;
    private final NotificationAnnouncer announcer;
    private final PluginConfig config;

    public FightTagCommand(FightManager fightManager, NotificationAnnouncer announcer, PluginConfig config) {
        this.fightManager = fightManager;
        this.announcer = announcer;
        this.config = config;
    }

    @Execute(name = "status")
    @Permission("eternalcombat.status")
    void status(@Context CommandSender sender, @Arg Player target) {
        UUID targetUniqueId = target.getUniqueId();
        PluginConfig.Messages messages = this.config.messages;

        Formatter formatter = new Formatter()
            .register("{PLAYER}", target.getName());

        this.announcer.sendMessage(sender, this.fightManager.isInCombat(targetUniqueId)
            ? formatter.format(messages.admin.playerInCombat)
            : formatter.format(messages.admin.playerNotInCombat));
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
            CancelTagReason cancelReason = event.getCancelReason();

            if (cancelReason == CancelTagReason.TAGOUT) {
                this.announcer.sendMessage(sender, this.config.messages.admin.adminTagOutCanceled);
                return;
            }

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
            CancelTagReason cancelReason = firstTagEvent.getCancelReason();

            if (cancelReason == CancelTagReason.TAGOUT) {
                this.announcer.sendMessage(sender, this.config.messages.admin.adminTagOutCanceled);
                return;
            }

            return;
        }

        if (secondTagEvent.isCancelled()) {
            CancelTagReason cancelReason = secondTagEvent.getCancelReason();

            if (cancelReason == CancelTagReason.TAGOUT) {
                this.announcer.sendMessage(sender, this.config.messages.admin.adminTagOutCanceled);
                return;
            }

            return;
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
