package com.eternalcode.combat.config.implementation;

import com.eternalcode.combat.WhitelistBlacklistMode;
import com.eternalcode.combat.fight.drop.DropSettings;
import com.eternalcode.combat.fight.effect.FightEffectSettings;
import com.eternalcode.combat.fight.pearl.FightPearlSettings;
import com.eternalcode.combat.notification.Notification;
import com.eternalcode.combat.notification.implementation.ActionBarNotification;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class PluginConfig extends OkaeriConfig {

    @Comment("# Do you want to change the plugin settings?")
    public Settings settings = new Settings();

    @Comment({" ", "# Ender pearl settings"})
    public FightPearlSettings pearl = new FightPearlSettings();

    @Comment({" ", "# Custom effects settings"})
    public FightEffectSettings effect = new FightEffectSettings();

    @Comment({" ", "# Set a custom way for a player's items to drop on death (if in combat)"})
    public DropSettings dropSettings = new DropSettings();

    public static class Settings extends OkaeriConfig {

        @Comment("# Whether the player should receive information about new plugin updates upon joining the server")
        public boolean shouldReceivePluginUpdates = true;

        @Comment("# The duration of combat in seconds")
        public Duration combatDuration = Duration.ofSeconds(20);

        @Comment("# List of worlds to ignore")
        public List<String> worldsToIgnore = List.of(
            "your_world"
        );

        @Comment("# List of regions to block")
        public List<String> blockedRegions = Collections.singletonList("your_region");

        @Comment({
            "# Set the knock multiplier for the blocked region",
            "# Values can be decimal. Do NOT use negative values.",
            "# Setting it around 1 knocks the player around 2-4 blocks away."
        })
        public double blockedRegionKnockMultiplier = 1;

        @Comment({"# Should the player be prevented from entering regions with WorldGuard flag PVP set to DENY "})
        public boolean shouldPreventPvpRegions = true;

        @Comment("# Set the radius of the blocked region if you aren't using WorldGuard basen on default spawn region!")
        public int blockedRegionRadius = 10;

        @Comment("# Release attacker after victim dies?")
        public boolean shouldReleaseAttacker = true;

        @Comment({"# If you want to exclude admins from combat, ",
            "# Setting this to true - admins cannot be tagged and will not tag other players on hit",
            "# Setting this to false - admins can be tagged and can tag other players on hit"
        })
        public boolean excludeAdminFromCombat = false;

        @Comment("# Command blocking mode, available modes: WHITELIST, BLACKLIST")
        public WhitelistBlacklistMode commandBlockingMode = WhitelistBlacklistMode.BLACKLIST;

        @Comment({
            "# List of commands based on the mode above",
            "# Based on BLACKLIST mode, all commands in the list are blocked, and all others are allowed",
            "# Based on WHITELIST mode, all commands in the list are allowed, and all others are blocked",
        })
        public List<String> blockedCommands = List.of(
            "gamemode",
            "spawn",
            "tp"
        );

        @Comment("# Block the use of elytra?")
        public boolean shouldPreventElytraUsage = true;

        @Comment("# Block flying? (flying players will fall to the ground)")
        public boolean shouldPreventFlying = true;

        @Comment("# Disable the use of elytra on damage?")
        public boolean shouldElytraDisableOnDamage = true;

        @Comment("# Block the opening of inventory?")
        public boolean shouldPreventInventoryOpening = true;

        @Comment("# Whether to block the placement of blocks?")
        public boolean shouldPreventBlockPlacing = true;

        @Comment({ "# Block the placement of blocks above or below a certain Y coordinate",
            "# Select the mode for block placing, available modes: ABOVE, BELOW"
        })
        public BlockPlacingMode blockPlacingMode = BlockPlacingMode.ABOVE;

        @Comment("# Block placing mode custom name used if messages")
        public String blockPlacingModeName = "above";

        @Comment("# Set the Y coordinate for block placing relative to mode selected above")
        public int blockPlacingYCoordinate = 40;

        public enum BlockPlacingMode {
            ABOVE,
            BELOW
        }

        @Comment({
            "# Disable placing specific blocks?",
            "# If you want to block all blocks, enable shouldPreventBlockPlacing and make this list empty",
            "# If you want to disable placing only specific blocks, enable shouldPreventBlockPlacing and add blocks to this list above",
            "# If you want to disable this feature completely, disable shouldPreventBlockPlacing option above",
        })
        public List<Material> specificBlocksToPreventPlacing = List.of();

        @Comment("# Do You want to enable combat log for non-player causes of damage? - Set to false to disable")
        public boolean shouldEnableDamageCauses = false;

        @Comment("# Select the mode for damage causes, available modes: WHITELIST, BLACKLIST")
        public WhitelistBlacklistMode damageCausesMode = WhitelistBlacklistMode.WHITELIST;

        @Comment({
            "# After selecting the mode above, select the causes of damage to be logged",
            "# You can find a list of all causes here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html",
            "# While using Whitelist mode the player will get a combat log after the damage from the list below",
            "# While using Blacklist mode the player will get a combat log after any damage non-listed below",
        })
        public List<EntityDamageEvent.DamageCause> damageCausesToLog = List.of(
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.CONTACT,
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK
        );

        @Comment({
            "# After what type of projectile entity should not tag the player as fighter?",
            "# You can find a list of all entity types here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html"
        })
        public List<EntityType> disabledProjectileEntities = List.of(
            EntityType.ENDER_PEARL
        );

    }

    @Comment({" ", "# Do you want to change the plugin messages?"})
    public Messages messages = new Messages();

    public static class Messages extends OkaeriConfig {

        @Comment("# Do you want to change the admin messages?")
        public AdminMessages admin = new AdminMessages();

        @Comment({
            " ",
            "# Combat log notification",
            "# You can use {TIME} variable to display the time left in combat",
            "# Notification types: CHAT, ACTION_BAR, TITLE, SUB_TITLE, BOSS_BAR",
            " ",
            "# BossBar progress: This is the value of the progress bar. Set it to -1.0 to show the remaining combat time",
            "# BossBar colors: https://javadoc.io/static/net.kyori/adventure-api/4.14.0/net/kyori/adventure/bossbar/BossBar.Color.html",
            "# BossBar overlays: https://javadoc.io/static/net.kyori/adventure-api/4.14.0/net/kyori/adventure/bossbar/BossBar.Overlay.html"
        })
        public Notification combatNotification = new ActionBarNotification("&dCombat ends in: &f{TIME}");

        @Comment("# Message sent when the player does not have permission to perform a command")
        public String noPermission = "&cYou don't have permission to perform this command!";

        @Comment("# Message sent when the specified player could not be found")
        public String playerNotFound = "&cThe specified player could not be found!";

        @Comment("# Message sent when the player enters combat")
        public String playerTagged = "&cYou are in combat, do not leave the server!";

        @Comment("# Message sent when the player leaves combat")
        public String playerUntagged = "&aYou are no longer in combat! You can safely leave the server.";

        @Comment("# This is broadcast when the player is in combat and logs out")
        public String playerLoggedOutDuringCombat = "&c{PLAYER} logged off during the fight!";

        @Comment({
            "# Message sent when the player is in combat and tries to use a disabled command",
            "# you can configure the list of disabled commands in the blockedCommands section of the config.yml file"
        })
        public String commandDisabledDuringCombat = "&cUsing this command during combat is prohibited!";

        @Comment("# Message sent when player tries to use a command with invalid arguments")
        public String invalidCommandUsage = "&7Correct usage: &e{USAGE}";

        @Comment("# Message sent when player tries to open inventory, but the inventory open is blocked")
        public String inventoryBlockedDuringCombat = "&cYou cannot open this inventory during combat!";

        @Comment({"# Message sent when player tries to place a block, but the block place is blocked",
        "# Placeholder {Y} is replaced with the Y coordinate set in the config",
        "# Placeholder {MODE} is replaced with the mode set in the config"})
        public String blockPlacingBlockedDuringCombat = "&cYou cannot place {MODE} {Y} coordinate during combat!";

        @Comment("# Message sent when player tries to enter a region")
        public String cantEnterOnRegion = "&cYou can't enter this region during combat!";

        public static class AdminMessages extends OkaeriConfig {
            @Comment("# Message sent when console tries to use a command that is only for players")
            public String onlyForPlayers = "&cThis command is only available to players!";

            @Comment("# Message sent to admin when they tag a player")
            public String adminTagPlayer = "&7You have tagged &e{PLAYER}";

            @Comment("# Message sent when a player is tagged by an admin")
            public String adminTagMultiplePlayers = "&7You have tagged &e{FIRST_PLAYER}&7 and &e{SECOND_PLAYER}&7.";

            @Comment("# Message sent to admin when they remove a player from combat")
            public String adminUntagPlayer = "&7You have removed &e{PLAYER}&7 from the fight.";

            @Comment("# Message sent when the player is not in combat")
            public String adminPlayerNotInCombat = "&cThis player is not in combat!";

            @Comment("# Message sent when the player is in combat")
            public String playerInCombat = "&c{PLAYER} is currently in combat!";

            @Comment("# Message sent when a player is not in combat")
            public String playerNotInCombat = "&a{PLAYER} is not currently in combat.";

            @Comment("# Message sent when an admin tries to tag themselves")
            public String adminCannotTagSelf = "&cYou cannot tag yourself!";

            @Comment("# Message sent when an admin disables the ability to get tagged for some time")
            public String adminTagOutSelf = "&7Successfully disabled tag for Yourself! You will be taggable after &e{TIME} ";

            @Comment("# Message sent when an admin disables the ability to get tagged for some time for other player")
            public String adminTagOut = "&7Successfully disabled tag for &e{PLAYER}! They will be taggable after &e{TIME} ";

            @Comment("# Message sent to the player whom the ability to get tagged for some time has been disabled")
            public String playerTagOut = "&7You will be taggable in &e{TIME} !";

            @Comment("# Message sent when an admin reenables the ability to get tagged for the player")
            public String adminTagOutOff = "&7Successfully enabled tag for &e{PLAYER}!";

            @Comment("# Message sent to the player whom the ability to get tagged has been reenabled")
            public String playerTagOutOff = "&7You are now taggable!";

            @Comment("# Message sent when player cannot be tagged because they have enabled tag-out")
            public String adminTagOutCanceled = "&cCannot tag this player due to tag-out!";
        }
    }
}
