package necessities.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import necessities.Attributes;
import necessities.entity.LashingPotatoHookEntity;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract void remove(Entity.RemovalReason reason);

    @Inject(method = "createLivingAttributes", at = @At("TAIL"))
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(Attributes.HEIGHT).add(Attributes.WIDTH).add(Attributes.ROLL);
    }

    @ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
    private EntityDimensions getDimensions(EntityDimensions original) {
        float height = (float) getAttributeValue(Attributes.HEIGHT);
        float width = (float) getAttributeValue(Attributes.WIDTH);
        return new EntityDimensions(
                original.width() * width, original.height() * height, original.eyeHeight() * height, original.attachments().scale(width, height, 1), false
        );
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getScale()F"))
    private float modifyScale(LivingEntity instance, Operation<Float> original){
        return original.call(instance) * (float) getAttributeValue(Attributes.HEIGHT) * (float) getAttributeValue(Attributes.WIDTH);
    }

    @WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"))
    private boolean isOnGround(LivingEntity instance, Operation<Boolean> original){
        return original.call(instance) || instance instanceof PlayerEntity && getAttributeValue(Attributes.MAX_JUMPS) > 1 && getAttributeValue(Attributes.JUMPS) < getAttributeValue(Attributes.MAX_JUMPS);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;jump()V"))
    private void jump(CallbackInfo ci) {
        if((Object) this instanceof PlayerEntity) {
            EntityAttributeInstance jumps = getAttributeInstance(Attributes.JUMPS);
            jumps.setBaseValue(jumps.getBaseValue() + 1);
        }
    }

    @Inject(method = "travelMidAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasNoDrag()Z"), cancellable = true)
    private void travelMidAir(Vec3d movementInput, CallbackInfo ci, @Local(ordinal = 1) Vec3d vec3d, @Local double d){
        if(!(this instanceof PlayerEntityExtension ext)) return;
        LashingPotatoHookEntity hook = ext.necessities$getLashingPotatoHook();
        if (hook != null && hook.isHooked() && !isOnGround()) {
            this.setVelocity(vec3d.x * 0.99F, d * 0.995F, vec3d.z * 0.99F);
            ci.cancel();
        }
    }
}
