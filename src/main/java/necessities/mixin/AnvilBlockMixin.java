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
public class AnvilBlockMixin {
    @WrapOperation(method = "method_32879", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;serverDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private static void serverDamage(Entity instance, DamageSource source, float amount, Operation<Void> original) {
        original.call(instance, source, amount);
        if(source.getTypeRegistryEntry().matchesKey(DamageTypes.FALLING_ANVIL) && instance instanceof LivingEntity) {
            EntityAttributeInstance attribute = ((LivingEntity) instance).getAttributeInstance(Attributes.HEIGHT);
            attribute.setBaseValue(attribute.getBaseValue() - amount / 40);
        }
    }
}
