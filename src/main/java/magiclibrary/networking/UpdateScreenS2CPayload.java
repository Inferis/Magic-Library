package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateScreenS2CPayload(String enchantmentsString, int points) implements CustomPayload {
    public static Id<UpdateScreenS2CPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("update_screen_s2c_payload"));
    public static PacketCodec<RegistryByteBuf, UpdateScreenS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, UpdateScreenS2CPayload::enchantmentsString,
            PacketCodecs.INTEGER, UpdateScreenS2CPayload::points,
            UpdateScreenS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
