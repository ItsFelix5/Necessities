package necessities.entity;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import necessities.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TomatoEntity extends ThrownItemEntity {
    public TomatoEntity(EntityType<? extends TomatoEntity> entityType, World world) {
        super(entityType, world);
    }

    public TomatoEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.TOMATO, owner, world, stack);
    }

    public TomatoEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntities.TOMATO, x, y, z, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TOMATO;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!(getWorld() instanceof ServerWorld world)) return;
        entityHitResult.getEntity().damage(world, this.getDamageSources().thrown(this, this.getOwner()), 0.0F);
        world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(getDefaultItem())), true, true, getX(), getY(), getZ(), 24, 0, 0, 0, 0.1);
        discard();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!(getWorld() instanceof ServerWorld world)) return;
        world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(getDefaultItem())), true, true, getX(), getY(), getZ(), 24, 0, 0, 0, 0.1);
        discard();
    }

    @Override
    public DoubleDoubleImmutablePair getKnockback(LivingEntity target, DamageSource source) {
        return DoubleDoubleImmutablePair.of(this.getVelocity().x * 3, this.getVelocity().z * 3);
    }
}
