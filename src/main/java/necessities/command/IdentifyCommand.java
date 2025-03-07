package necessities.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class IdentifyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("identify")
                .requires(source -> source.hasPermissionLevel(1))
                .then(CommandManager.argument("name", StringArgumentType.string())
                        .executes(context -> {
                            ((PlayerEntityExtension) context.getSource().getPlayerOrThrow()).necessities$setName(Text.literal(context.getArgument("name", String.class)));
                            return 1;
                        }))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ((PlayerEntityExtension) EntityArgumentType.getPlayer(context, "player")).necessities$setName(Text.literal(context.getArgument("name", String.class)));
                                    return 1;
                                }))));
    }
}
