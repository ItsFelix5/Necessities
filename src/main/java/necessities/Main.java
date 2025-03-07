package necessities;

import necessities.block.ModBlocks;
import necessities.command.ModCommands;
import necessities.command.NarratePayload;
import necessities.entity.ModEntities;
import necessities.item.ModItems;
import necessities.particle.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class Main implements ModInitializer {
    public static Identifier id(String path) {
        return Identifier.of("necessities", path);
    }

    @Override
    public void onInitialize() {
        Attributes.init();
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        Sounds.init();
        ModParticles.init();
        ModCommands.init();
        PayloadTypeRegistry.playS2C().register(NarratePayload.ID, NarratePayload.CODEC);
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, (list)->list.add(new TradeOffers.SellItemFactory(ModItems.TOMATO, 2, 16, 4)));
    }
}
