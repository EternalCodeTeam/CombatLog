package com.eternalcode.combat.fight.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * This event is triggered when a player is untagged during a fight.
 */
public class FightUntagEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UUID player;
    private final CauseOfUnTag cause;
    private boolean cancelled;

    public FightUntagEvent(UUID player, CauseOfUnTag cause) {
        super(false);

        this.player = player;
        this.cause = cause;
    }

    /**
     * Gets the UUID of the player who was untagged.
     *
     * @return The UUID of the untagged player.
     */
    public UUID getPlayer() {
        return this.player;
    }

    /**
     * Gets the reason for why the player was untagged.
     *
     * @return The cause of untagging as a {@link CauseOfUnTag}.
     */
    public CauseOfUnTag getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
