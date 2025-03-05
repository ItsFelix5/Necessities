package necessities;

import necessities.entity.ModEntities;
import necessities.narrate.NarratePayload;
import necessities.particles.ModParticles;
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
