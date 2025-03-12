package necessities.mixin;

import necessities.Attributes;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "setupTransforms", at = @At("TAIL"))
    private void setupTransforms(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale, CallbackInfo ci) {
        double width = entity.getAttributeValue(Attributes.WIDTH);
        double height = entity.getAttributeValue(Attributes.HEIGHT);
        double roll = entity.getAttributeValue(Attributes.ROLL);

        matrices.scale((float) width, (float) height, 1);
        if (roll > 110 || roll < -110) matrices.translate(0, height + 0.1, 0);
        else if (roll > 90 || roll < -90) matrices.translate(0, height / 2, 0);
        else if (roll > 75 || roll < -75) matrices.translate(0, width * 0.9, 0);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) roll));
    }
}
