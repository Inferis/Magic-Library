package magiclibrary.item;

import magiclibrary.MagicLibrary;
import magiclibrary.block.MagicLibraryBlock;
import magiclibrary.block.MagicLibraryBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MagicLibraryItems {
    public static MagicLibraryBlockItem MAGIC_LIBRARY;
    public static SampleBlockItem SAMPLE;

    interface ItemMaker<T extends Item> {
        T makeItem(RegistryKey<Item> key);
    }

    private static <T extends Item> T registerItem(String name, ItemMaker<T> itemMaker) {
        MagicLibrary.LOGGER.info("Registering item: " + MagicLibrary.MODID + ":" + name);

        var identifier = MagicLibrary.id(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, identifier);
        T item = itemMaker.makeItem(key);
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void registerItems() {
        MAGIC_LIBRARY = registerItem("magic_library", key -> {
            return new MagicLibraryBlockItem(MagicLibraryBlocks.MAGIC_LIBRARY, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(key));
        });
        SAMPLE = registerItem("sample", key -> {
            return new SampleBlockItem(MagicLibraryBlocks.SAMPLE, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(key));
        });
    }

    public static void registerItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.add(MAGIC_LIBRARY);
        });
    }
}
