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
import org.jetbrains.annotations.NotNull;

public class FightTagPlaceholder extends PlaceholderExpansion {

    private final FightManager fightManager;
    private final Server server;

    public FightTagPlaceholder(FightManager fightManager, Server server) {
        this.fightManager = fightManager;
        this.server = server;
    }

    @Override
    public boolean canRegister() {
        return true;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "eternalcombat";
    }

    @Override
    public @NotNull String getAuthor() {
        return "EternalCode";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equals("remaining_seconds")) {
            return this.getFightTag(player)
                .map(fightTag -> DurationUtil.format(fightTag.getRemainingDuration()))
                .orElse("");
        }

        if (identifier.equals("remaining_millis")) {
            return this.getFightTag(player)
                .map(fightTag -> DurationParser.TIME_UNITS.format(fightTag.getRemainingDuration()))
                .orElse("");
        }

        if (identifier.equals("opponent")) {
            return getTagger(player)
                .map(tagger -> tagger.getName())
                .orElse("");
        }

        if (identifier.equals("opponent_health")) {
            return getTagger(player)
                .map(tagger -> String.valueOf(tagger.getHealth()))
                .orElse("");
        }

        return null;
    }

    private @NotNull Optional<Player> getTagger(OfflinePlayer player) {
        return this.getFightTag(player)
            .map(fightTag -> fightTag.getTagger())
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