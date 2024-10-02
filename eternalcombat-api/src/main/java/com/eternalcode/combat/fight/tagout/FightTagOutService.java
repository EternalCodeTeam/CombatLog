package com.eternalcode.combat.fight.tagout;

import java.time.Duration;
import java.util.UUID;

public interface FightTagOutService {

    boolean isTaggedOut(UUID player);

    void unTagOut(UUID player);

    void tagOut(UUID player, Duration duration);
}
