package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryBlockEntity;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record ImportEnchantmentsC2SPayload(BlockPos entityPos, ItemStack itemStack) implements CustomPayload {
    public static Id<ImportEnchantmentsC2SPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("consume_enchantments_c2s_payload"));
    public static PacketCodec<RegistryByteBuf, ImportEnchantmentsC2SPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, ImportEnchantmentsC2SPayload::entityPos,
            ItemStack.PACKET_CODEC, ImportEnchantmentsC2SPayload::itemStack,
            ImportEnchantmentsC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            if (context.player().getWorld().getBlockEntity(entityPos()) instanceof MagicLibraryBlockEntity libraryBlockEntity) {
                libraryBlockEntity.importEnchantments(itemStack());
                if (context.player().currentScreenHandler instanceof MagicLibraryScreenHandler libraryScreenHandler) {
                    libraryScreenHandler.resetImportInventory();
                    libraryScreenHandler.updateEnchantments(libraryBlockEntity.getEnchantments());
                    ServerPlayNetworking.send(context.player(), new UpdateScreenS2CPayload(false));
                }
            }
        });
    }
}
