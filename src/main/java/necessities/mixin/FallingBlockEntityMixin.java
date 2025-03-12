package necessities.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import necessities.Attributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
    @WrapOperation(method = "method_32879", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private static boolean damage(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (source.getTypeRegistryEntry().matchesKey(DamageTypes.FALLING_ANVIL) && instance instanceof LivingEntity living) {
            EntityAttributeInstance attribute = living.getAttributeInstance(Attributes.HEIGHT);
            attribute.setBaseValue(attribute.getBaseValue() - amount / 40);
        }
        return original.call(instance, source, amount);
    }
}
