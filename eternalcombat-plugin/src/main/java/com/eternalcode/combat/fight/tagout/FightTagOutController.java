package com.eternalcode.combat.fight.tagout;

import com.eternalcode.combat.fight.event.CancelTagReason;
import com.eternalcode.combat.fight.event.FightTagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class FightTagOutController implements Listener {

    private final FightTagOutService tagOutService;

    public FightTagOutController(FightTagOutService tagOutService) {
        this.tagOutService = tagOutService;
    }

    @EventHandler
    void onTagOut(FightTagEvent event) {
        UUID uniqueId = event.getPlayer();

        if (this.tagOutService.isTaggedOut(uniqueId)) {
            event.setCancelReason(CancelTagReason.TAGOUT);
            event.setCancelled(true);
        }
    }

}
