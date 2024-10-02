package com.eternalcode.combat.bridge.placeholder;

import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.FightTag;
import com.eternalcode.combat.util.DurationUtil;
import com.eternalcode.commons.time.DurationParser;
import java.util.Optional;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FightTagPlaceholder extends PlaceholderExpansion {

    private final FightManager fightManager;
    private final Server server;
    private final Plugin plugin;
    private static final String IDENTIFIER = "eternalcombat";

    public FightTagPlaceholder(FightManager fightManager, Server server, Plugin plugin) {
        this.fightManager = fightManager;
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }


    @Override
    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equals("remaining_seconds")) {
            return this.getFightTag(player)
                .map(fightTagInter -> DurationUtil.format(fightTagInter.getRemainingDuration()))
                .orElse("");
        }

        if (identifier.equals("remaining_millis")) {
            return this.getFightTag(player)
                .map(fightTag -> DurationParser.TIME_UNITS.format(fightTag.getRemainingDuration()))
                .orElse("");
        }

        if (identifier.equals("opponent")) {
            return this.getTagger(player)
                .map(tagger -> tagger.getName())
                .orElse("");
        }

        if (identifier.equals("opponent_health")) {
            return this.getTagger(player)
                .map(tagger -> String.format("%.2f", tagger.getHealth()))
                .orElse("");
        }

        return null;
    }

    private @NotNull Optional<Player> getTagger(OfflinePlayer player) {
        return this.getFightTag(player)
            .map(fightTagInter -> fightTagInter.getTagger())
            .map(taggerId -> this.server.getPlayer(taggerId));
    }

    private Optional<FightTag> getFightTag(OfflinePlayer player) {
        Player onlinePlayer = player.getPlayer();

        if (onlinePlayer != null) {
            if (!this.fightManager.isInCombat(onlinePlayer.getUniqueId())) {
                return Optional.empty();
            }

            return Optional.of(this.fightManager.getTag(onlinePlayer.getUniqueId()));
        }

        return Optional.empty();
    }

}
