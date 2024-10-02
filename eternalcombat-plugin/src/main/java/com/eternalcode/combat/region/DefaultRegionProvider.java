package com.eternalcode.combat.region;

import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.util.BlockVector;

public class DefaultRegionProvider implements RegionProvider {

    private final int radius;

    public DefaultRegionProvider(int radius) {
        this.radius = radius;
    }

    @Override
    public Optional<Region> getRegion(Location location) {
        Location spawnLocation = location.getWorld().getSpawnLocation();
        double x = spawnLocation.getX();
        double z = spawnLocation.getZ();

        BlockVector min = new BlockVector(x - this.radius, 0, z - this.radius);
        BlockVector max = new BlockVector(x + this.radius, 0, z + this.radius);

        if (this.contains(min, max, location.getX(), location.getZ())) {
            return Optional.of(() -> new Location(location.getWorld(), x, location.getY(), z));
        }

        return Optional.empty();
    }

    private boolean contains(BlockVector min, BlockVector max, double x, double z) {
        return x >= min.getX() && x < max.getX()
            && z >= min.getZ() && z < max.getZ();
    }

}
