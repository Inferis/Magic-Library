package magiclibrary.networking;

import magiclibrary.MagicLibrary;
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

public record ExtractEnchantmentC2SPayload(String identifier, int level) implements CustomPayload {
    public static Id<ExtractEnchantmentC2SPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("extract_enchantment_c2s_payload"));
    public static PacketCodec<RegistryByteBuf, ExtractEnchantmentC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ExtractEnchantmentC2SPayload::identifier,
            PacketCodecs.INTEGER, ExtractEnchantmentC2SPayload::level,
            ExtractEnchantmentC2SPayload::new);

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
                enchantmentEntry.ifPresent(enchantment -> builder.add(enchantment, level()));
            });
            if (!context.player().giveItemStack(stack)) {
                context.player().dropItem(stack, false, false);
            }

        });
    }
}
