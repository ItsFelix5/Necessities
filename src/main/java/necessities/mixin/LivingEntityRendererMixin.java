package necessities.mixin;

import necessities.Attributes;
import necessities.extension.LivingEntityRenderStateExtension;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void setupTransforms(LivingEntity livingEntity, LivingEntityRenderState state, float f, CallbackInfo ci) {
        ((LivingEntityRenderStateExtension) state).necessities$setHeightMultiplier((float) livingEntity.getAttributeValue(Attributes.HEIGHT));
        ((LivingEntityRenderStateExtension) state).necessities$setWidthMultiplier((float) livingEntity.getAttributeValue(Attributes.WIDTH));
        ((LivingEntityRenderStateExtension) state).necessities$setRoll((float) livingEntity.getAttributeValue(Attributes.ROLL));
    }

    @Inject(method = "setupTransforms", at = @At("TAIL"))
    private void setupTransforms(LivingEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight, CallbackInfo ci) {
        LivingEntityRenderStateExtension ext = (LivingEntityRenderStateExtension) state;
        matrices.scale(ext.necessities$getWidthMultiplier(), ext.necessities$getHeightMultiplier(), 1);
        double roll = ext.necessities$getRoll();
        if(roll > 110 || roll < -110) matrices.translate(0, state.height + 0.1, 0);
        else if(roll > 90 || roll < -90) matrices.translate(0, state.height / 2, 0);
        else if(roll > 75 || roll < -75) matrices.translate(0, state.width * 0.9, 0);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) roll));
    }
}
