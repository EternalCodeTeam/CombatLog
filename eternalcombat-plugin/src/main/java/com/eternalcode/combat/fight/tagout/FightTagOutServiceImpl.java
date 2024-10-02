package com.eternalcode.combat.fight.tagout;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FightTagOutServiceImpl implements FightTagOutService {

    private final Map<UUID, Instant> tagOuts = new HashMap<>();

    @Override
    public void tagOut(UUID player, Duration duration) {
        Instant endTime = Instant.now().plus(duration);

        this.tagOuts.put(player, endTime);
    }

    @Override
    public void unTagOut(UUID player) {
        this.tagOuts.remove(player);
    }

    @Override
    public boolean isTaggedOut(UUID player) {
        Instant endTime = this.tagOuts.get(player);

        if (endTime == null) {
            return false;
        }
        Instant now = Instant.now();

        return now.isBefore(endTime);
    }

}
