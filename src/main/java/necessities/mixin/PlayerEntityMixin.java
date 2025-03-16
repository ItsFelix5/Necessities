package necessities.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import necessities.Attributes;
import necessities.entity.LashingPotatoHookEntity;
import necessities.extension.PlayerEntityExtension;
import necessities.item.DismemberPayload;
import necessities.item.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityExtension {
    @Unique
    private int noClipTicks = 0;
    @Unique
    private LashingPotatoHookEntity potatoHook;
    @Unique
    private Text name;
    @Unique
    private boolean offhand = true;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("TAIL"))
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(Attributes.JUMPS).add(Attributes.MAX_JUMPS);
    }

    @Shadow
    @NotNull
    public abstract ItemStack getWeaponStack();

    @Shadow public abstract PlayerInventory getInventory();

    @Override
    public void necessities$setNoClipTicks(int ticks) {
        if (ticks == 0 && !getWorld().getBlockState(getBlockPos().add(0, 1, 0)).isAir()) return;
        noClipTicks = ticks;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"))
    private boolean isSpectator(PlayerEntity instance) {
        if (noClipTicks > 0) necessities$setNoClipTicks(noClipTicks - 1);
        return noClipTicks > 0 || instance.isSpectator();
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void tickMovement(CallbackInfo ci) {
        if (potatoHook == null || !potatoHook.isHooked()) return;
        this.onLanding();
        if (!this.isLogicalSideForUpdatingMovement()) return;

        if (potatoHook.hookedEntity != null) {
            if (squaredDistanceTo(potatoHook.hookedEntity) <= 9) {
                this.addVelocity(this.getVelocity().multiply(-1));
                potatoHook.remove(RemovalReason.DISCARDED);
                return;
            }
            if (potatoHook.hookedEntity instanceof LivingEntity living && living.getMaxHealth() <= getMaxHealth()) {
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

    @Override
    public LashingPotatoHookEntity necessities$getLashingPotatoHook() {
        return potatoHook;
    }

    @Override
    public void necessities$setLashingPotatoHook(LashingPotatoHookEntity hook) {
        potatoHook = hook;
    }

    @Override
    public void necessities$setName(Text name) {
        this.name = name;
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<Text> cir) {
        if (name != null) cir.setReturnValue(name);
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getKnockbackAgainst(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)F"))
    private float getKnockbackAgainst(PlayerEntity instance, Entity entity, DamageSource damageSource, Operation<Float> original) {
        if (getWeaponStack().isOf(ModItems.BAT)) {
            float strength = (float) (Math.pow(getVelocity().length() + entity.getVelocity().length(), 2) * 2);

            float radPitch = getPitch() * MathHelper.RADIANS_PER_DEGREE;
            float radYaw = getYaw() * MathHelper.RADIANS_PER_DEGREE;

            entity.setVelocity(-MathHelper.cos(radPitch) * MathHelper.sin(radYaw) * strength,
                    -MathHelper.sin(radPitch) * strength,
                    MathHelper.cos(radPitch) * MathHelper.cos(radYaw) * strength);
            return 0f;
        }
        return original.call(instance, entity, damageSource);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(source.getWeaponStack() != null && source.getWeaponStack().isOf(ModItems.AXE_OF_DISMEMBERING) && getHealth() - amount <= 0) {
            offhand = false;
            if(!getWorld().isClient) getWorld().getPlayers().forEach(player-> ServerPlayNetworking.send((ServerPlayerEntity) player, new DismemberPayload(uuid.toString())));
            dropStack(getOffHandStack());
            getInventory().removeStack(PlayerInventory.OFF_HAND_SLOT);
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean necessities$getOffhand() {
        return offhand;
    }

    @Override
    public void necessities$setOffhand(boolean offhand) {
        this.offhand = offhand;
    }
}
