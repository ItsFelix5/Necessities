package necessities.mixin;

import necessities.Attributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "updateSupportingBlockPos", at = @At("HEAD"))
    private void updateSupportingBlockPos(boolean onGround, Vec3d movement, CallbackInfo ci){
        //noinspection ConstantValue
        if(onGround && (Object) this instanceof PlayerEntity player) player.getAttributeInstance(Attributes.JUMPS).setBaseValue(0);
    }
}
