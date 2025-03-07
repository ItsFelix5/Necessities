package necessities.mixin;

import necessities.particle.ModParticles;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Redirect(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    public void addParticle(ClientWorld world, ParticleEffect particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if(!particle.getType().equals(ModParticles.CONFETTI)) {
            world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
            return;
        }
        for (int i = 0; i < 15; i++) {
            world.addParticle(ParticleTypes.POOF, x, y, z, world.random.nextGaussian() / 10f, Math.abs(world.random.nextGaussian() / 10f), world.random.nextGaussian() / 10f);
        }
        for (int i = 0; i < 200; i++) {
            world.addParticle(particle, x, y, z, world.random.nextGaussian() / 8f, Math.abs(world.random.nextGaussian() / 8f), world.random.nextGaussian() / 8f);
        }
    }
}
