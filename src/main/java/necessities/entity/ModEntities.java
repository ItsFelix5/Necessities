package necessities.entity;

import necessities.Main;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModEntities {
    public static final EntityType<ConfettiBombEntity> CONFETTI_BOMB = register("confetti_bomb",
            EntityType.Builder.<ConfettiBombEntity>create(ConfettiBombEntity::new, SpawnGroup.MISC).makeFireImmune().dropsNothing().dimensions(0.25f, 0.25f));

    public static final EntityType<TomatoEntity> TOMATO = register("tomato",
            EntityType.Builder.<TomatoEntity>create(TomatoEntity::new, SpawnGroup.MISC).makeFireImmune().dropsNothing().dimensions(0.25f, 0.25f));

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Main.id(name),
                builder.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Main.id(name)))
        );
    }

    public static void clientInit() {
        EntityRendererRegistry.register(ModEntities.CONFETTI_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOMATO, FlyingItemEntityRenderer::new);
    }

    public static void init() {
    }
}
