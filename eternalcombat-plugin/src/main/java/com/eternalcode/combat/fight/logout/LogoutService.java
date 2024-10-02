package com.eternalcode.combat.fight.logout;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LogoutService {

    private final Cache<UUID, Logout> logouts = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public void punishForLogout(Player player) {
        UUID uniqueId = player.getUniqueId();

        this.logouts.put(uniqueId, new Logout(uniqueId, player.getHealth()));
    }

    public Optional<Logout> nextLogoutFor(UUID player) {
        Logout logout = this.logouts.getIfPresent(player);

        if (logout == null) {
            return Optional.empty();
        }

        this.logouts.invalidate(player);
        return Optional.of(logout);
    }

    public boolean hasLoggedOut(UUID player) {
        return this.logouts.asMap().containsKey(player);
    }
}
