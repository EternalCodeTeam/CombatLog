package com.eternalcode.combat.fight.bossbar;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;

public record FightBossBar(Audience audience, BossBar bossBar, float progress, Duration combatDuration) {
}
