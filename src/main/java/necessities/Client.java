package necessities;

import necessities.command.NarratePayload;
import necessities.entity.ModEntities;
import necessities.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModParticles.clientInit();
        ModEntities.clientInit();

        ClientPlayNetworking.registerGlobalReceiver(NarratePayload.ID, (payload, ctx) -> ctx.client().getNarratorManager().narrator.say(payload.text(), false));
    }
}
