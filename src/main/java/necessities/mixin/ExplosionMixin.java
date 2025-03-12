package necessities.mixin;

import necessities.particle.ModParticles;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Redirect(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    public void addParticle(World world, ParticleEffect particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (!particle.getType().equals(ModParticles.CONFETTI) || !world.isClient) {
            world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
            return;
        }
        for (int i = 0; i < 15; i++) world.addParticle(ParticleTypes.POOF, x, y, z, world.random.nextGaussian() / 10f, Math.abs(world.random.nextGaussian() / 10f), world.random.nextGaussian() / 10f);
        for (int i = 0; i < 200; i++) world.addParticle(particle, x, y, z, world.random.nextGaussian() / 8f, Math.abs(world.random.nextGaussian() / 8f), world.random.nextGaussian() / 8f);
    }
}
