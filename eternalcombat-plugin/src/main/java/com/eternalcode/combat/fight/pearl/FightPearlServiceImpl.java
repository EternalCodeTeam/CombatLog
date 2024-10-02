package com.eternalcode.combat.fight.pearl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class FightPearlServiceImpl implements FightPearlService {

    private final FightPearlSettings pearlSettings;
    private final Cache<UUID, Instant> pearlDelays;

    public FightPearlServiceImpl(FightPearlSettings pearlSettings) {
        this.pearlSettings = pearlSettings;
        this.pearlDelays = Caffeine.newBuilder()
            .expireAfterWrite(pearlSettings.pearlThrowDelay)
            .build();
    }

    @Override
    public void markDelay(UUID uuid) {
        this.pearlDelays.put(uuid, Instant.now().plus(this.pearlSettings.pearlThrowDelay));
    }

    @Override
    public boolean hasDelay(UUID uuid) {
        return Instant.now().isBefore(this.getDelay(uuid));
    }

    @Override
    public Duration getRemainingDelay(UUID uuid) {
        return Duration.between(Instant.now(), this.getDelay(uuid));
    }

    @Override
    public Instant getDelay(UUID uuid) {
        return this.pearlDelays.asMap().getOrDefault(uuid, Instant.MIN);
    }
}
