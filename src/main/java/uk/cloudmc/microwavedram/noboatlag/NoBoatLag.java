package uk.cloudmc.microwavedram.noboatlag;

import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.craftbukkit.entity.CraftChestBoat;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class NoBoatLag extends JavaPlugin implements Listener, CommandExecutor {

    // Cancel the placement of boats, and instead spawn one of our Collisionless ones.
    @EventHandler
    public void onEntityPlace(EntityPlaceEvent entityPlaceEvent) {
        if (entityPlaceEvent.getEntity() instanceof CraftBoat && !(entityPlaceEvent.getEntity() instanceof CraftChestBoat)) {
            Boat boat = (Boat) entityPlaceEvent.getEntity();

            AbstractBoat abstractBoat = ((CraftBoat) boat).getHandle();

            EntityType<?> type = abstractBoat.getType();

            // Folia: EntityPlaceEvent fires on the region thread that owns the boat's
            // location, so spawnBoat()'s addFreshEntity() call below runs on the correct
            // thread. Keep this synchronous - do NOT hand it to a global/async scheduler.
            spawnBoat(boat.getLocation(), type);
            Player player = entityPlaceEvent.getPlayer();
            assert player != null;
            PlayerInventory inventory = player.getInventory();

            if (player.getGameMode() != GameMode.CREATIVE) {
                if (entityPlaceEvent.getHand() == EquipmentSlot.HAND) {
                    inventory.setItemInMainHand(null);
                } else if (entityPlaceEvent.getHand() == EquipmentSlot.OFF_HAND) {
                    inventory.setItemInOffHand(null);
                }
            }

            // Prevent the real boat from spawning
            entityPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("open_boat_utils_interpolation_fix")) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeShort(29); // PacketId
                out.writeShort(1); // Enabled
            } catch (IOException e) {
                e.printStackTrace();
            }

            event.getPlayer().sendPluginMessage(
                    this,
                    "openboatutils:settings",
                    b.toByteArray()
            );
        }
    }

    // Spawn one of our collisionless boats at a location.
    public void spawnBoat(Location location, net.minecraft.world.entity.EntityType<?> entityType) {
        ServerLevel level = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();

        Item dropItem;

        if (entityType == EntityTypes.OAK_BOAT) {
            dropItem = Items.OAK_BOAT;
        } else if (entityType == EntityTypes.BIRCH_BOAT) {
            dropItem = Items.BIRCH_BOAT;
        } else if (entityType == EntityTypes.SPRUCE_BOAT) {
            dropItem = Items.SPRUCE_BOAT;
        } else if (entityType == EntityTypes.JUNGLE_BOAT) {
            dropItem = Items.JUNGLE_BOAT;
        } else if (entityType == EntityTypes.ACACIA_BOAT) {
            dropItem = Items.ACACIA_BOAT;
        } else if (entityType == EntityTypes.DARK_OAK_BOAT) {
            dropItem = Items.DARK_OAK_BOAT;
        } else if (entityType == EntityTypes.MANGROVE_BOAT) {
            dropItem = Items.MANGROVE_BOAT;
        } else if (entityType == EntityTypes.CHERRY_BOAT) {
            dropItem = Items.CHERRY_BOAT;
        } else if (entityType == EntityTypes.BAMBOO_RAFT) {
            dropItem = Items.BAMBOO_RAFT;
        } else if (entityType == EntityTypes.PALE_OAK_BOAT) {
            dropItem = Items.PALE_OAK_BOAT;
        } else if (entityType == EntityType.OAK_CHEST_BOAT) {
            dropItem = Items.OAK_CHEST_BOAT;
        } else if (entityType == EntityType.BIRCH_CHEST_BOAT) {
            dropItem = Items.BIRCH_CHEST_BOAT;
        } else if (entityType == EntityType.SPRUCE_CHEST_BOAT) {
            dropItem = Items.SPRUCE_CHEST_BOAT;
        } else if (entityType == EntityType.JUNGLE_CHEST_BOAT) {
            dropItem = Items.JUNGLE_CHEST_BOAT;
        } else if (entityType == EntityType.ACACIA_CHEST_BOAT) {
            dropItem = Items.ACACIA_CHEST_BOAT;
        } else if (entityType == EntityType.DARK_OAK_CHEST_BOAT) {
            dropItem = Items.DARK_OAK_CHEST_BOAT;
        } else if (entityType == EntityType.MANGROVE_CHEST_BOAT) {
            dropItem = Items.MANGROVE_CHEST_BOAT;
        } else if (entityType == EntityType.CHERRY_CHEST_BOAT) {
            dropItem = Items.CHERRY_CHEST_BOAT;
        } else if (entityType == EntityType.BAMBOO_CHEST_RAFT) {
            dropItem = Items.BAMBOO_CHEST_RAFT;
        } else if (entityType == EntityType.PALE_OAK_CHEST_BOAT) {
            dropItem = Items.PALE_OAK_CHEST_BOAT;
        } else {
            dropItem = Items.DIRT;
        }

        if (entityType == EntityTypes.BAMBOO_RAFT) {
            CollisionlessRaft raft = new CollisionlessRaft((EntityType<? extends net.minecraft.world.entity.vehicle.boat.Raft>) entityType, level, () -> dropItem);

            float yaw = Location.normalizeYaw(location.getYaw());
            raft.setYRot(yaw);
            raft.yRotO = yaw;
            raft.setYHeadRot(yaw);

            raft.teleportTo(location.getX(), location.getY(), location.getZ());

            level.addFreshEntity(raft, CreatureSpawnEvent.SpawnReason.COMMAND);
        } else if (entityType == EntityType.BAMBOO_CHEST_RAFT) {
            CollisionlessRaft raft = new CollisionlessChestRaft((EntityType<? extends net.minecraft.world.entity.vehicle.boat.ChestRaft>) entityType, level, () -> dropItem);

            float yaw = Location.normalizeYaw(location.getYaw());
            raft.setYRot(yaw);
            raft.yRotO = yaw;
            raft.setYHeadRot(yaw);

            raft.teleportTo(location.getX(), location.getY(), location.getZ());

            level.addFreshEntity(raft, CreatureSpawnEvent.SpawnReason.COMMAND);
        } else if (entityType == EntityType.CHEST_BOAT) {
            CollisionlessRaft raft = new CollisionlessChestBoat((EntityType<? extends net.minecraft.world.entity.vehicle.boat.ChestBoat>) entityType, level, () -> dropItem);

            float yaw = Location.normalizeYaw(location.getYaw());
            raft.setYRot(yaw);
            raft.yRotO = yaw;
            raft.setYHeadRot(yaw);

            raft.teleportTo(location.getX(), location.getY(), location.getZ());

            level.addFreshEntity(raft, CreatureSpawnEvent.SpawnReason.COMMAND);
        } else {
            CollisionlessBoat boat = new CollisionlessBoat((EntityType<? extends net.minecraft.world.entity.vehicle.boat.Boat>) entityType, level, () -> dropItem);
            float yaw = Location.normalizeYaw(location.getYaw());
            boat.setYRot(yaw);
            boat.yRotO = yaw;
            boat.setYHeadRot(yaw);

            boat.teleportTo(location.getX(), location.getY(), location.getZ());

            level.addFreshEntity(boat, CreatureSpawnEvent.SpawnReason.COMMAND);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("noboatlag")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage("noboatlag: Configuration reloaded.");
                return true;
            } else {
                sender.sendMessage("Usage: /noboatlag reload");
                return true;
            }
        }

        return false;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        if (getConfig().getBoolean("open_boat_utils_interpolation_fix")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "openboatutils:settings");
        }

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {}
}
