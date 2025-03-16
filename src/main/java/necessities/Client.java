package necessities;

import necessities.command.NarratePayload;
import necessities.entity.LashingPotatoHookEntityRenderer;
import necessities.entity.ModEntities;
import necessities.extension.PlayerEntityExtension;
import necessities.item.DismemberPayload;
import necessities.item.ModItems;
import necessities.particle.ConfettiParticle;
import necessities.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.CONFETTI, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) ->
                new ConfettiParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider));

        EntityRendererRegistry.register(ModEntities.CONFETTI_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOMATO, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.LASHING_POTATO_HOOK, LashingPotatoHookEntityRenderer::new);

        ModelPredicateProviderRegistry.register(ModItems.YIPPEE, Main.id("on_head"),
                (stack, world, entity, seed) -> (entity != null && entity.getEquippedStack(EquipmentSlot.HEAD) == stack) ? 1.0F : 0.0F);

        ModelPredicateProviderRegistry.register(ModItems.LASHING_POTATO, Main.id("grappling"),
                (stack, world, entity, seed) -> (entity instanceof PlayerEntity player && ((PlayerEntityExtension) player).necessities$getLashingPotatoHook() != null
                        && player.getMainHandStack() == stack) ? 1.0F : 0.0F);

        ClientPlayNetworking.registerGlobalReceiver(NarratePayload.ID, (payload, ctx) -> ctx.client().getNarratorManager().narrator.say(payload.text(), false));
        ClientPlayNetworking.registerGlobalReceiver(DismemberPayload.ID,
                (payload, ctx) -> {
            if(ctx.client().world.getPlayerByUuid(UUID.fromString(payload.uuid())) instanceof PlayerEntityExtension ext) ext.necessities$setOffhand(false);
                });
    }
}
