package com.eternalcode.combat.fight;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface FightTag {

    @Nullable
    @ApiStatus.Experimental
    UUID getTagger();

    Duration getRemainingDuration();

    boolean isExpired();

    Instant getEndOfCombatLog();

    UUID getTaggedPlayer();
}
