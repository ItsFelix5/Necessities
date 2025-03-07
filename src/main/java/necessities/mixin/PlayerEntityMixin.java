package necessities.mixin;

import necessities.Attributes;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityExtension {
    @Unique private int noClipTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void necessities$setNoClipTicks(int ticks) {
        if(ticks == 0 && !getWorld().getBlockState(getBlockPos().add(0, 1, 0)).isAir()) return;
        noClipTicks = ticks;
        MinecraftClient.getInstance().chunkCullingEnabled = !(noClip = noClipTicks > 0);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"))
    private boolean isSpectator(PlayerEntity instance){
        if(noClipTicks > 0) necessities$setNoClipTicks(noClipTicks-1);
        return noClipTicks > 0 || instance.isSpectator();
    }

    @Inject(method = "createPlayerAttributes", at = @At("TAIL"))
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(Attributes.JUMPS).add(Attributes.MAX_JUMPS);
    }
}
