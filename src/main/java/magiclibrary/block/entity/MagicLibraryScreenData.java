package magiclibrary.block.entity;

import magiclibrary.common.EnchantmentsContainer;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record MagicLibraryScreenData(int points, String enchantments) {
    public static final PacketCodec<RegistryByteBuf, MagicLibraryScreenData> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MagicLibraryScreenData::points,
            PacketCodecs.STRING, MagicLibraryScreenData::enchantments,
            MagicLibraryScreenData::new);

}
