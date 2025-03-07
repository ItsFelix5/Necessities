package necessities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class NarrateCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("narrate")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("text", StringArgumentType.greedyString())
                                .executes(context -> {
                                    ServerPlayNetworking.send(EntityArgumentType.getPlayer(context, "player"), new NarratePayload(context.getArgument("text", String.class)));
                                    return 1;
                                })))
                .then(CommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayNetworking.send(context.getSource().getPlayerOrThrow(), new NarratePayload(context.getArgument("text", String.class)));
                            return 1;
                        })));
    }
}
