package magiclibrary.block.entity;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.BlockPos;

public record MagicLibraryScreenData(int points, String enchantments, BlockPos entityPos) {
    public static final PacketCodec<RegistryByteBuf, MagicLibraryScreenData> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, MagicLibraryScreenData::points,
            PacketCodecs.STRING, MagicLibraryScreenData::enchantments,
            BlockPos.PACKET_CODEC, MagicLibraryScreenData::entityPos,
            MagicLibraryScreenData::new);

}
