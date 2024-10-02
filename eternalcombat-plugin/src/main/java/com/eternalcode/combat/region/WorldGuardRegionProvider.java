package com.eternalcode.combat.region;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.Optional;
import java.util.TreeSet;
import org.bukkit.Location;

import java.util.List;

public class WorldGuardRegionProvider implements RegionProvider {

    private final RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
    private final TreeSet<String> regions = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private final PluginConfig pluginConfig;

    public WorldGuardRegionProvider(List<String> regions, PluginConfig pluginConfig) {
        this.regions.addAll(regions);
        this.pluginConfig = pluginConfig;
    }

    @Override
    public Optional<Region> getRegion(Location location) {
        RegionQuery regionQuery = this.regionContainer.createQuery();
        ApplicableRegionSet applicableRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));

        for (ProtectedRegion region : applicableRegions.getRegions()) {
            if (!this.isCombatRegion(region)) {
                continue;
            }

            return Optional.of(new RegionImpl(location, region));
        }

        return Optional.empty();
    }

    private boolean isCombatRegion(ProtectedRegion region) {
        if (this.regions.contains(region.getId())) {
            return true;
        }

        if (this.pluginConfig.settings.shouldPreventPvpRegions) {
            StateFlag.State flag = region.getFlag(Flags.PVP);

            if (flag != null) {
                return flag.equals(StateFlag.State.DENY);
            }
        }

        return false;
    }

    private record RegionImpl(Location contextLocation, ProtectedRegion region) implements Region {
        @Override
        public Location getCenter() {
            BlockVector3 min = this.region.getMinimumPoint();
            BlockVector3 max = this.region.getMaximumPoint();

            double x = (double) (min.getX() + max.getX()) / 2;
            double z = (double) (min.getZ() + max.getZ()) / 2;

            return new Location(this.contextLocation.getWorld(), x, this.contextLocation.getY(), z);
        }
    }

}
