package com.eternalcode.combat.fight.effect;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Manages custom potion effects on players during combat.
 * Active effects before combat are stored and restored after combat ends.
 */
public interface FightEffectService {

    void removeCustomEffect(Player player, PotionEffectType type, Integer amplifier);

    void applyCustomEffect(Player player, PotionEffectType type, Integer amplifier);

    List<PotionEffect> getCurrentEffects(Player player);

    void clearStoredEffects(Player player);

    void restoreActiveEffects(Player player);

    void storeActiveEffect(Player player, PotionEffect effect);
}
