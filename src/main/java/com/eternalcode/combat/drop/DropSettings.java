package com.eternalcode.combat.drop;

import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;

@Contextual
public class DropSettings {

    @Description({
        "# UNCHANGED - The default way of item drop defined by the engine",
        "# PERCENT - Drops a fixed percentage of items",
        "# PLAYERS_HEALTH - Drops inverted percentage of the player's health (i.e. if the player has, for example, 80% HP, he will drop 20% of items. Only works when the player escapes from combat by quiting game)"
    })
    public DropType dropType = DropType.UNCHANGED;

    @Description("# What percentage of items should drop from the player? (Only if Drop Type is set to PERCENT)")
    public int dropItemPercent = 100;

    @Description("# This option is responsible for the lowest percentage of the player that can drop (i.e. if the player leaves the game while he has 100% of his HP, the percentage of items that is set in this option will drop, if you set this option to 0, then nothing will drop from such a player)")
    public int playersHealthPercentClamp = 20;

    @Description("# Does the drop modification affect the experience drop?")
    public boolean affectExperience = false;
}
