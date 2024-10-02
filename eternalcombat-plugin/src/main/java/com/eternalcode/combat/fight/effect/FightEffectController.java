package com.eternalcode.combat.fight.effect;

import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.event.CauseOfUnTag;
import com.eternalcode.combat.fight.event.FightTagEvent;
import com.eternalcode.combat.fight.event.FightUntagEvent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

public class FightEffectController implements Listener {

    private final FightEffectService effectService;
    private final FightEffectSettings effectSettings;
    private final FightManager fightManager;
    private final Server server;

    public FightEffectController(FightEffectSettings settings, FightEffectService effectService, FightManager fightManager, Server server) {
        this.effectSettings = settings;
        this.effectService = effectService;
        this.fightManager = fightManager;
        this.server = server;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTag(FightTagEvent event) {
        if (!this.effectSettings.customEffectsEnabled) {
            return;
        }

        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            return;
        }

        this.effectSettings.customEffects.forEach((key, value) ->
            this.effectService.applyCustomEffect(player, key, value));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(FightUntagEvent event) {
        if (!this.effectSettings.customEffectsEnabled) {
            return;
        }

        if (event.getCause() == CauseOfUnTag.LOGOUT) {
            Player player = Bukkit.getPlayer(event.getPlayer());

            assert player != null;
            this.effectService.clearStoredEffects(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUntag(FightUntagEvent event) {
        if (!this.effectSettings.customEffectsEnabled) {
            return;
        }

        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            return;
        }

        this.effectSettings.customEffects.forEach((key, value) -> this.effectService.removeCustomEffect(player, key, value));

        this.effectService.restoreActiveEffects(player);
    }

    @EventHandler
    public void onDeath(FightUntagEvent event) {
        if (!this.effectSettings.customEffectsEnabled) {
            return;
        }
        if (event.getCause() == CauseOfUnTag.DEATH_BY_PLAYER || event.getCause() == CauseOfUnTag.DEATH) {
            Player player = Bukkit.getPlayer(event.getPlayer());

            assert player != null;
            this.effectService.clearStoredEffects(player);
        }
    }

    @EventHandler
    public void onEffectChange(EntityPotionEffectEvent event) {
        if (!this.effectSettings.customEffectsEnabled) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        PotionEffect newEffect = event.getNewEffect();
        PotionEffect oldEffect = event.getOldEffect();

        if (!this.isRemovedEffect(newEffect, oldEffect)) {
            return;
        }

        Integer customAmplifier = this.effectSettings.customEffects.get(oldEffect.getType());

        if (customAmplifier == null) {
            return;
        }

        player.addPotionEffect(new PotionEffect(oldEffect.getType(), -1, customAmplifier));
    }

    private boolean isRemovedEffect(PotionEffect newEffect, PotionEffect oldEffect) {
        return newEffect == null && oldEffect != null;
    }

}
