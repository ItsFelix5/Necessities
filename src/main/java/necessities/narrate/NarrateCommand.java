package necessities.narrate;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class NarrateCommand {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(NarratePayload.ID, NarratePayload.CODEC);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("narrate")
                .then(CommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayNetworking.send(context.getSource().getPlayerOrThrow(), new NarratePayload(context.getArgument("text", String.class)));
                            return 1;
                        }))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("text", StringArgumentType.greedyString())
                                .executes(context -> {
                                    ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "player"), new NarratePayload(context.getArgument("text", String.class)));
                                    return 1;
                                })))));
    }
}
