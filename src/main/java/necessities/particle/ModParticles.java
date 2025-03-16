package necessities.particle;

import necessities.Main;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {
    public static final SimpleParticleType CONFETTI = Registry.register(Registries.PARTICLE_TYPE, Main.id("confetti"), FabricParticleTypes.simple(true));

    public static void init() {
    }
}
