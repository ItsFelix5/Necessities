package necessities;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class Attributes {
    public static final RegistryEntry.Reference<EntityAttribute> HEIGHT = register("height", 1.0, 0.01, 32);
    public static final RegistryEntry.Reference<EntityAttribute> WIDTH = register("width", 1.0, 0.01, 32);

    public static final RegistryEntry.Reference<EntityAttribute> ROLL = register("roll", 0, -180.0, 180.0);

    public static final RegistryEntry.Reference<EntityAttribute> MAX_JUMPS = register("max_jumps", 1, 1, 100);
    public static final RegistryEntry.Reference<EntityAttribute> JUMPS = register("jumps", 0, 0, 99);

    private static RegistryEntry.Reference<EntityAttribute> register(String name, double fallback, double min, double max) {
        return Registry.registerReference(Registries.ATTRIBUTE, Main.id(name),
                new ClampedEntityAttribute(
                        "attribute.necessities." + name,
                        fallback,
                        min,
                        max
                ).setTracked(true));
    }

    public static void init() {
    }
}
