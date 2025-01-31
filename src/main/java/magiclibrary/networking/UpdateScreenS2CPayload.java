package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateScreenS2CPayload(boolean dummy) implements CustomPayload {
    public static Id<UpdateScreenS2CPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("update_screen_s2c_payload"));
    public static PacketCodec<RegistryByteBuf, UpdateScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, UpdateScreenS2CPayload::dummy,
            UpdateScreenS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
