package necessities.mixin;

import necessities.Attributes;
import necessities.entity.LashingPotatoHookEntity;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityExtension {
    @Unique private int noClipTicks = 0;
    @Unique private LashingPotatoHookEntity potatoHook;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void necessities$setNoClipTicks(int ticks) {
        if(ticks == 0 && !getWorld().getBlockState(getBlockPos().add(0, 1, 0)).isAir()) return;
        noClipTicks = ticks;
        MinecraftClient.getInstance().chunkCullingEnabled = !(noClip = noClipTicks > 0);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"))
    private boolean isSpectator(PlayerEntity instance){
        if(noClipTicks > 0) necessities$setNoClipTicks(noClipTicks-1);
        return noClipTicks > 0 || instance.isSpectator();
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void tickMovement(CallbackInfo ci){
        if (potatoHook == null || !potatoHook.isHooked()) return;
        this.onLanding();
        if (!this.isLogicalSideForUpdatingMovement()) return;

        if(potatoHook.hookedEntity != null) {
            if(squaredDistanceTo(potatoHook.hookedEntity) <= 9) {
                this.addVelocity(this.getVelocity().multiply(-1));
                potatoHook.remove(RemovalReason.DISCARDED);
                return;
            }
            if(potatoHook.hookedEntity instanceof LivingEntity living && living.getMaxHealth() <= getMaxHealth()) {
                potatoHook.hookedEntity.addVelocity(new Vec3d(getX() - potatoHook.getX(), getY() - potatoHook.getY(), getZ() - potatoHook.getZ()).normalize().multiply(1.2));
                return;
            }
        }
        Vec3d vec3d = potatoHook.getPos().subtract(this.getEyePos());
        float g = potatoHook.getLength();
        double d = vec3d.length();
        if (d > g) {
            double e = d / g * 0.1;
            this.addVelocity(vec3d.multiply(1.0 / d).multiply(e, e * 1.1, e));
        }
    }

    @Inject(method = "createPlayerAttributes", at = @At("TAIL"))
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(Attributes.JUMPS).add(Attributes.MAX_JUMPS);
    }

    @Override
    public LashingPotatoHookEntity necessities$getLashingPotatoHook() {
        return potatoHook;
    }

    @Override
    public void necessities$setLashingPotatoHook(LashingPotatoHookEntity hook) {
        potatoHook = hook;
    }
}
