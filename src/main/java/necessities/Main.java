package necessities;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {
    public static Identifier id(String path) {
        return Identifier.of("necessities", path);
    }

    @Override
    public void onInitialize() {
        Attributes.init();
    }
}
