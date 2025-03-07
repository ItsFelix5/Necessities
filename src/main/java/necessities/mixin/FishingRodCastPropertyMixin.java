package necessities.mixin;

import necessities.extension.PlayerEntityExtension;
import necessities.item.ModItems;
import net.minecraft.client.render.item.property.bool.FishingRodCastProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodCastProperty.class)
public class FishingRodCastPropertyMixin {
    @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
    private void getValue(ItemStack stack, ClientWorld world, LivingEntity user, int seed, ModelTransformationMode modelTransformationMode, CallbackInfoReturnable<Boolean> cir){
        if(stack.isOf(ModItems.LASHING_POTATO) && user instanceof PlayerEntity player && ((PlayerEntityExtension) player).necessities$getLashingPotatoHook() != null
                && (player.getMainHandStack() == stack || player.getOffHandStack() == stack)) {
            cir.setReturnValue(true);
        }
    }
}
