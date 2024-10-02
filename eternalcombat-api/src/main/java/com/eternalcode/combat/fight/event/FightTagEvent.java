package com.eternalcode.combat.fight.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * This event is triggered when a player is tagged in a fight.
 */
public class FightTagEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UUID player;
    private final CauseOfTag cause;
    private boolean cancelled = false;
    private CancelTagReason cancelReason;

    public FightTagEvent(UUID player, CauseOfTag cause) {
        super(false);

        this.player = player;
        this.cause = cause;
    }

    /**
     * Gets the UUID of the player who was tagged.
     *
     * @return The UUID of the tagged player.
     */
    public UUID getPlayer() {
        return this.player;
    }

    /**
     * Gets the reason why the player was tagged.
     *
     * @return The cause of the combat tag as a {@link CauseOfTag}.
     */
    public CauseOfTag getCause() {
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

    /**
     * Gets the reason why the event was cancelled.
     *
     * @return The cancel reason.
     */
    public CancelTagReason getCancelReason() {
        return this.cancelReason;
    }

    /**
     * Sets the reason why the event was cancelled.
     *
     * @param cancelReason The cancel reason.
     */
    public void setCancelReason(CancelTagReason cancelReason) {
        this.cancelReason = cancelReason;
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
