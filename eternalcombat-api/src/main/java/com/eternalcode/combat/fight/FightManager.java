package com.eternalcode.combat.fight;

import com.eternalcode.combat.fight.event.CauseOfTag;
import com.eternalcode.combat.fight.event.CauseOfUnTag;
import com.eternalcode.combat.fight.event.FightTagEvent;
import com.eternalcode.combat.fight.event.FightUntagEvent;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface FightManager {

    boolean isInCombat(UUID player);

    FightTag getTag(UUID target);

    Collection<FightTag> getFights();

    @ApiStatus.Experimental
    FightTagEvent tag(UUID target, Duration delay, CauseOfTag causeOfTag, @Nullable UUID tagger);

    FightTagEvent tag(UUID target, Duration delay, CauseOfTag causeOfTag);

    FightUntagEvent untag(UUID player, CauseOfUnTag causeOfUnTag);

    void untagAll();
}
