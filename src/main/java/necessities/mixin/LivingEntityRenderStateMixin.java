package necessities.mixin;

import necessities.extensions.LivingEntityRenderStateExtension;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateExtension {
    @Unique public float heightMultiplier;
    @Unique public float widthMultiplier;
    @Unique public float roll;

    @Override
    public float necessities$getHeightMultiplier() {
        return heightMultiplier;
    }

    @Override
    public void necessities$setHeightMultiplier(float heightMultiplier) {
            this.heightMultiplier = heightMultiplier;
    }

    @Override
    public float necessities$getWidthMultiplier() {
        return widthMultiplier;
    }

    @Override
    public void necessities$setWidthMultiplier(float widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    @Override
    public float necessities$getRoll() {
        return roll;
    }

    @Override
    public void necessities$setRoll(float roll) {
        this.roll = roll;
    }
}
