package necessities.mixin;

import necessities.extension.PlayerEntityRenderStateExtension;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateExtension {
    @Unique private int yippeeAnimationTime = 0;
    @Unique
    private boolean hand = true;

    @Override
    public int necessities$getYippeeAnimationTime(int delta) {
        yippeeAnimationTime = Math.clamp(yippeeAnimationTime + delta, 0, 25);
        return yippeeAnimationTime;
    }

    @Override
    public void necessities$setOffhand(boolean hand) {
        this.hand = hand;
    }

    @Override
    public boolean necessities$getOffhand() {
        return hand;
    }
}
