package com.eternalcode.combat.fight.event;

public enum CauseOfTag {

    /**
     * The player was tagged in combat as a result of an attack by another player.
     */
    PLAYER,

    /**
     * The player was tagged in combat due to an interaction with a non-player entity
     * (e.g., mobs or environmental damage).
     */
    NON_PLAYER,

    /**
     * A command was executed to apply a combat tag to the player.
     */
    COMMAND,

    /**
     * A custom cause, typically defined by external plugins or systems, applied the combat tag.
     */
    CUSTOM
}
