package necessities;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class Sounds {
    public static final SoundEvent DING = Registry.register(Registries.SOUND_EVENT, Main.id("ding"), SoundEvent.of(Main.id("ding")));
    public static final SoundEvent TOOT = Registry.register(Registries.SOUND_EVENT, Main.id("toot"), SoundEvent.of(Main.id("toot")));

    public static void init() {
    }
}
