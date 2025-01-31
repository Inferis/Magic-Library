package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ExportEnchantmentC2SPayload(String identifier, int level, BlockPos entityPos) implements CustomPayload {
    public static Id<ExportEnchantmentC2SPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("export_enchantment_c2s_payload"));
    public static PacketCodec<RegistryByteBuf, ExportEnchantmentC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ExportEnchantmentC2SPayload::identifier,
            PacketCodecs.INTEGER, ExportEnchantmentC2SPayload::level,
            BlockPos.PACKET_CODEC, ExportEnchantmentC2SPayload::entityPos,
            ExportEnchantmentC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var stack = new ItemStack(Items.ENCHANTED_BOOK);
            var enchantmentRegistry = context.player().getWorld().getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
            EnchantmentHelper.apply(stack, builder -> {
                var enchantmentEntry = enchantmentRegistry.flatMap(registry -> registry.getEntry(Identifier.of(identifier)));
                enchantmentEntry.ifPresent(enchantment -> {
                    builder.add(enchantment, level());
                    if (context.player().getWorld().getBlockEntity(entityPos()) instanceof MagicLibraryBlockEntity libraryBlockEntity) {
                        libraryBlockEntity.deductPointsForLevel(level, enchantment.value());
                    }
                });
            });
            if (!context.player().giveItemStack(stack)) {
                context.player().dropItem(stack, false, false);
            }
        });
    }
}
