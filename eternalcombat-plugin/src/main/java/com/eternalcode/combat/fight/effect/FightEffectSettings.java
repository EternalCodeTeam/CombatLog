package com.eternalcode.combat.fight.effect;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class FightEffectSettings extends OkaeriConfig {

    @Comment({"# Do you want to add effects to players in combat?"})
    public boolean customEffectsEnabled = false;

    @Comment({
        "# If the option above is set to true, you can add effects to players in combat below",
        "# You can find a list of all potion effects here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html",
        "# Correct format: 'EFFECT_TYPE:AMPLIFIER' Amplifier strength starts from 0, so level 1 gives effect strength 2",
        "# Example: SPEED:1, DAMAGE_RESISTANCE:0",
    })
    public Map<PotionEffectType, Integer> customEffects = Map.of(
        PotionEffectType.SPEED, 1,
        PotionEffectType.DAMAGE_RESISTANCE, 0
    );

}
