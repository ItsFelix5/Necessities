package necessities.extension;

import necessities.entity.LashingPotatoHookEntity;
import net.minecraft.text.Text;

public interface PlayerEntityExtension {
    void necessities$setNoClipTicks(int ticks);
    LashingPotatoHookEntity necessities$getLashingPotatoHook();
    void necessities$setLashingPotatoHook(LashingPotatoHookEntity hook);
    void necessities$setName(Text name);
    boolean necessities$getOffhand();
    void necessities$setOffhand(boolean offhand);
}
