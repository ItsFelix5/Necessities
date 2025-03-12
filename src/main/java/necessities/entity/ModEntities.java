package necessities.entity;

import necessities.Main;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<ConfettiBombEntity> CONFETTI_BOMB = register("confetti_bomb",
            EntityType.Builder.<ConfettiBombEntity>create(ConfettiBombEntity::new, SpawnGroup.MISC).makeFireImmune().dimensions(0.25f, 0.25f));

    public static final EntityType<TomatoEntity> TOMATO = register("tomato",
            EntityType.Builder.<TomatoEntity>create(TomatoEntity::new, SpawnGroup.MISC).makeFireImmune().dimensions(0.25f, 0.25f));

    public static final EntityType<LashingPotatoHookEntity> LASHING_POTATO_HOOK = register(
            "lashing_potato_hook",
            EntityType.Builder.<LashingPotatoHookEntity>create(LashingPotatoHookEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .disableSummon()
                    .dimensions(0.5F, 0.5F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(5)
    );

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Main.id(name),
                builder.build(name)
        );
    }

    public static void clientInit() {
        EntityRendererRegistry.register(ModEntities.CONFETTI_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOMATO, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.LASHING_POTATO_HOOK, LashingPotatoHookEntityRenderer::new);
    }

    public static void init() {
    }
}
