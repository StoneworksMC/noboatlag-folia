package uk.cloudmc.microwavedram.noboatlag;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Raft;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

// Raft counterpart to CollisionlessBoat. Bamboo rafts are a separate NMS class
// (net.minecraft.world.entity.vehicle.Raft) from regular boats, so they need
// their own collisionless subclass. See CollisionlessBoat for the full explanation.

public class CollisionlessRaft extends Raft {
    public CollisionlessRaft(EntityType<? extends Raft> var0, Level var1, Supplier<Item> var2) {
        super(var0, var1, var2);
    }

    // Force all collision checks to fail
    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }
}
