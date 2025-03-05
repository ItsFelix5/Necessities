package necessities.particles;

import necessities.Main;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {
    public static SimpleParticleType CONFETTI = Registry.register(Registries.PARTICLE_TYPE, Main.id("confetti"), FabricParticleTypes.simple(true));

    public static void clientInit() {
        ParticleFactoryRegistry.getInstance().register(CONFETTI, provider->(parameters, world, x, y, z, velocityX, velocityY, velocityZ) ->
                new ConfettiParticle(world, x, y, z, provider));
    }

    public static void init() {
    }
}
