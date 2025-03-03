package necessities.mixin;

import necessities.LivingEntityRenderStateExtension;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateExtension {
    @Unique public float heightMultiplier;
    @Unique public float widthMultiplier;
    @Unique public float roll;

    @Override
    public float getHeightMultiplier() {
        return heightMultiplier;
    }

    @Override
    public void setHeightMultiplier(float heightMultiplier) {
            this.heightMultiplier = heightMultiplier;
    }

    @Override
    public float getWidthMultiplier() {
        return widthMultiplier;
    }

    @Override
    public void setWidthMultiplier(float widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    @Override
    public float getRoll() {
        return roll;
    }

    @Override
    public void setRoll(float roll) {
        this.roll = roll;
    }
}
