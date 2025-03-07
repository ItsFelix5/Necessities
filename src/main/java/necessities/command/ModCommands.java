package necessities.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            NarrateCommand.register(dispatcher);
            IdentifyCommand.register(dispatcher);
        });
    }
}
