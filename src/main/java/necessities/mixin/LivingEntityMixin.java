package necessities.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import necessities.Attributes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

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
}
