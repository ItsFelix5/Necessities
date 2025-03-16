package necessities.item;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record DismemberPayload(String uuid) implements CustomPayload {
    public static final Id<DismemberPayload> ID = CustomPayload.id("dismember");
    public static final PacketCodec<PacketByteBuf, DismemberPayload> CODEC = CustomPayload.codecOf(DismemberPayload::write, DismemberPayload::new);

    private DismemberPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.uuid);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
