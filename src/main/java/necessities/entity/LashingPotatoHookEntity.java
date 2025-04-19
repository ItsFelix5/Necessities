package necessities.entity;

import necessities.extension.PlayerEntityExtension;
import necessities.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LashingPotatoHookEntity extends ProjectileEntity {
    public static final TrackedData<Boolean> HOOKED = DataTracker.registerData(LashingPotatoHookEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Float> LENGTH = DataTracker.registerData(LashingPotatoHookEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public Entity hookedEntity = null;

    public LashingPotatoHookEntity(EntityType<? extends LashingPotatoHookEntity> entityType, World world) {
        super(entityType, world);

    }

    public LashingPotatoHookEntity(World world, PlayerEntity playerEntity) {
        this(ModEntities.LASHING_POTATO_HOOK, world);
        this.setOwner(playerEntity);
        this.setPosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
        this.setVelocity(playerEntity.getRotationVec(1.0F).multiply(5.0));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(HOOKED, false);
        builder.add(LENGTH, 0.0F);
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
    }

    @Override
    public void tick() {
        super.tick();
        PlayerEntity playerEntity = this.getOwner();
        if (this.getWorld().isClient() || playerEntity != null && !playerEntity.isRemoved() && playerEntity.isAlive() && playerEntity.getMainHandStack().isOf(ModItems.LASHING_POTATO)) {
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }

            this.setPosition(hitResult.getPos());
            this.tryCheckBlockCollision();
        } else this.discard();
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && entity != getOwner();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if(getOwner() == null) {
            discard();
            return;
        }
        hookedEntity = entityHitResult.getEntity();
        this.setVelocity(Vec3d.ZERO);
        this.setHooked(true);
        this.setLength(Math.max((float) getOwner().getEyePos().subtract(hookedEntity.getPos()).length() * 0.5F - 3.0F, 1.5F));
        startRiding(hookedEntity);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(getOwner() == null) {
            discard();
            return;
        }
        super.onBlockHit(blockHitResult);
        this.setVelocity(Vec3d.ZERO);
        this.setHooked(true);
        this.setLength(Math.max((float) getOwner().getEyePos().subtract(blockHitResult.getPos()).length() * 0.5F - 3.0F, 1.5F));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("hooked", this.isHooked());
        nbt.putFloat("length", this.getLength());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setHooked(nbt.getBoolean("hooked"));
        this.setLength(nbt.getFloat("length"));
    }


    public boolean isHooked() {
        return this.getDataTracker().get(HOOKED);
    }

    private void setHooked(boolean bl) {
        this.getDataTracker().set(HOOKED, bl);
    }

    public float getLength() {
        return this.getDataTracker().get(LENGTH);
    }

    private void setLength(float f) {
        this.getDataTracker().set(LENGTH, f);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if(getOwner() != null) ((PlayerEntityExtension) getOwner()).necessities$setLashingPotatoHook(null);
        super.remove(reason);
    }

    @Override
    public void onRemoved() {
        if(getOwner() != null) ((PlayerEntityExtension) getOwner()).necessities$setLashingPotatoHook(null);
    }

    @Override
    public PlayerEntity getOwner() {
        return (PlayerEntity) super.getOwner();
    }

    @Override
    public void setOwner(Entity entity) {
        if (!(entity instanceof PlayerEntity)) {
            discard();
            return;
        }
        super.setOwner(entity);
        ((PlayerEntityExtension) entity).necessities$setLashingPotatoHook(this);
    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (getWorld().getEntityById(packet.getEntityData()) == null) this.kill();
    }
}
