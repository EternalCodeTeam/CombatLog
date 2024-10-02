package com.eternalcode.combat.fight.bossbar;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class FightBossBarRegistry {

    private final Map<UUID, FightBossBar> fightBossBars = new ConcurrentHashMap<>();

    void add(UUID uuid, FightBossBar fightBossBar) {
        this.fightBossBars.put(uuid, fightBossBar);
    }

    void remove(UUID uuid) {
        this.fightBossBars.remove(uuid);
    }

    boolean hasFightBossBar(UUID uuid) {
        return this.fightBossBars.containsKey(uuid);
    }

    Optional<FightBossBar> getFightBossBar(UUID uuid) {
        return Optional.ofNullable(this.fightBossBars.get(uuid));
    }
}
