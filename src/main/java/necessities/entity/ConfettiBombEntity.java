package necessities.entity;

import necessities.Sounds;
import necessities.item.ModItems;
import necessities.particle.ModParticles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class ConfettiBombEntity extends ThrownItemEntity {
    public ConfettiBombEntity(EntityType<? extends ConfettiBombEntity> entityType, World world) {
        super(entityType, world);
    }

    public ConfettiBombEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.CONFETTI_BOMB, owner, world, stack);
    }

    public ConfettiBombEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntities.CONFETTI_BOMB, x, y, z, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CONFETTI_BOMB;
    }

    @Override
    public void tick() {
        super.tick();
        if (age > 30) explode();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        explode();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        explode();
    }

    public void explode() {
        if (!(getWorld() instanceof ServerWorld world)) return;
        Vec3d pos = getPos();

        for (ServerPlayerEntity player : world.getPlayers()) {
            Vec3d vec = player.getEyePos().subtract(pos);
            double length = vec.length();
            if (length < 16) {
                if (length != 0.0 && length <= 4) {
                    vec = vec.multiply((1.0 - length / 4) * 3 / length);
                    player.addVelocity(vec);
                } else vec = null;
                player.networkHandler.sendPacket(new ExplosionS2CPacket(pos, Optional.ofNullable(vec), ModParticles.CONFETTI, RegistryEntry.of(Sounds.TOOT)));
            }
        }
        discard();
    }
}
