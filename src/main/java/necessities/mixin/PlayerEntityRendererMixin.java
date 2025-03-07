package necessities.mixin;

import necessities.extension.PlayerEntityRenderStateExtension;
import necessities.item.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
    private void updateRenderState(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float f, CallbackInfo ci) {
        boolean yippee = player.isSneaking() && player.getInventory().getMainHandStack().isOf(ModItems.YIPPEE);
        ((PlayerEntityRenderStateExtension) state).necessities$getYippeeAnimationTime(yippee?1:-1);
        state.isInSneakingPose &= !yippee;
    }
}
