package necessities.command;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record NarratePayload(String text) implements CustomPayload {
    public static final CustomPayload.Id<NarratePayload> ID = CustomPayload.id("narrate");
    public static final PacketCodec<PacketByteBuf, NarratePayload> CODEC = CustomPayload.codecOf(NarratePayload::write, NarratePayload::new);

    private NarratePayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.text);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
