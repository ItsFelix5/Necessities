package necessities;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class Attributes {
    public static final RegistryEntry.Reference<EntityAttribute> HEIGHT = Registry.registerReference(Registries.ATTRIBUTE, Main.id("height"),
            new ClampedEntityAttribute(
                    "attribute.height",
                    1.0,
                    0.01,
                    32
            ).setTracked(true));

    public static final RegistryEntry.Reference<EntityAttribute> WIDTH = Registry.registerReference(Registries.ATTRIBUTE, Main.id("width"),
            new ClampedEntityAttribute(
                    "attribute.width",
                    1.0,
                    0.01,
                    32
            ).setTracked(true));

    public static final RegistryEntry.Reference<EntityAttribute> ROLL = Registry.registerReference(Registries.ATTRIBUTE, Main.id("roll"),
            new ClampedEntityAttribute(
                    "attribute.roll",
                    0,
                    -180.0,
                    180.0
            ).setTracked(true));

    public static void init() {}
}
