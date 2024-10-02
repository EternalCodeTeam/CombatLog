package com.eternalcode.combat.region;

import java.util.Optional;
import org.bukkit.Location;

public interface RegionProvider {

    Optional<Region> getRegion(Location location);

    default boolean isInRegion(Location location) {
        return this.getRegion(location).isPresent();
    }

}
