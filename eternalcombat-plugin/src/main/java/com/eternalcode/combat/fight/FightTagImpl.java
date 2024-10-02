package com.eternalcode.combat.fight;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class FightTagImpl implements FightTag {

    private final UUID taggedPlayer;
    private final Instant endOfCombatLog;
    private final @Nullable UUID tagger;

    FightTagImpl(UUID personToAddCombat, Instant endOfCombatLog, @Nullable UUID tagger) {
        this.taggedPlayer = personToAddCombat;
        this.endOfCombatLog = endOfCombatLog;
        this.tagger = tagger;
    }

    @Override
    public UUID getTaggedPlayer() {
        return this.taggedPlayer;
    }

    @Override
    public Instant getEndOfCombatLog() {
        return this.endOfCombatLog;
    }

    @Override
    public boolean isExpired() {
        return Instant.now().isAfter(this.endOfCombatLog);
    }

    @Override
    public Duration getRemainingDuration() {
        Duration between = Duration.between(Instant.now(), this.endOfCombatLog);

        if (between.isNegative()) {
            return Duration.ZERO;
        }

        return between;
    }

    @ApiStatus.Experimental
    @Override
    public @Nullable UUID getTagger() {
        return this.tagger;
    }

}
