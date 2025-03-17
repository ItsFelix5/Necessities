package necessities.mixin;

import necessities.particle.ModParticles;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Explosion.class, priority = 10)
public class ExplosionMixin {
    @Shadow @Final private ParticleEffect particle;
    @Shadow @Final private World world;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;

    @Inject(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), cancellable = true)
    public void addParticle(boolean particles, CallbackInfo ci) {
        if (!particle.getType().equals(ModParticles.CONFETTI) || !world.isClient) return;
        for (int i = 0; i < 15; i++) world.addParticle(ParticleTypes.POOF, x, y, z, world.random.nextGaussian() / 10f, Math.abs(world.random.nextGaussian() / 10f), world.random.nextGaussian() / 10f);
        for (int i = 0; i < 200; i++) world.addParticle(particle, x, y, z, world.random.nextGaussian() / 8f, Math.abs(world.random.nextGaussian() / 8f), world.random.nextGaussian() / 8f);
        ci.cancel();
    }
}
