package com.eternalcode.combat;

import com.eternalcode.combat.config.ConfigService;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.time.Duration;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Formatter;

@Command(name = "combatlog", aliases = "combat")
@Permission("eternalcombat.reload")
public class EternalCombatReloadCommand {

    private static final String RELOAD_MESSAGE = "<b><gradient:#8a1212:#fc6b03>EternalCombat:</gradient></b> Reloaded EternalCombat in <color:#fce303>{TIME}ms!</color>";

    private final ConfigService configService;
    private final NotificationAnnouncer announcer;

    public EternalCombatReloadCommand(ConfigService configService, NotificationAnnouncer announcer) {
        this.configService = configService;
        this.announcer = announcer;
    }

    @Async
    @Execute(name = "reload")
    void execute(@Context CommandSender sender) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        this.configService.reload();

        Duration elapsed = stopwatch.elapsed();
        Formatter format = new Formatter().register("{TIME}", elapsed.toMillis());
        this.announcer.sendMessage(sender, format.format(RELOAD_MESSAGE));
    }
}
