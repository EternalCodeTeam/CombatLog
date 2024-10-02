package com.eternalcode.combat.fight.effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class FightEffectServiceImpl implements FightEffectService {

    private final Map<UUID, List<PotionEffect>> activeEffects = new HashMap<>();
    private static final int INFINITE_DURATION = -1;

    @Override
    public void storeActiveEffect(Player player, PotionEffect effect) {
        List<PotionEffect> effects = this.activeEffects.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

        effects.add(effect);
    }

    @Override
    public void restoreActiveEffects(Player player) {
        List<PotionEffect> currentEffects = this.getCurrentEffects(player);

        for (PotionEffect effect : currentEffects) {
            player.addPotionEffect(effect);
        }

        this.clearStoredEffects(player);
    }

    @Override
    public void clearStoredEffects(Player player) {
        this.activeEffects.remove(player.getUniqueId());
    }

    @Override
    public List<PotionEffect> getCurrentEffects(Player player) {
        return this.activeEffects.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    @Override
    public void applyCustomEffect(Player player, PotionEffectType type, Integer amplifier) {
        PotionEffect activeEffect = player.getPotionEffect(type);

        if (activeEffect == null) {
            player.addPotionEffect(new PotionEffect(type, INFINITE_DURATION, amplifier));
            return;
        }

        if (activeEffect.getAmplifier() > amplifier) {
            return;
        }

        if (activeEffect.getDuration() == -1) {
            return;
        }

        this.storeActiveEffect(player, activeEffect);
        player.addPotionEffect(new PotionEffect(type, INFINITE_DURATION, amplifier));
    }

    @Override
    public void removeCustomEffect(Player player, PotionEffectType type, Integer amplifier) {
        PotionEffect activeEffect = player.getPotionEffect(type);

        if (activeEffect == null) {
            return;
        }

        if (activeEffect.getAmplifier() != amplifier) {
            return;
        }

        player.removePotionEffect(type);
    }
}
