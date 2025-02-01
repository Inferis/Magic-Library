package magiclibrary.block;

import magiclibrary.MagicLibrary;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class MagicLibraryBlocks {
    public static MagicLibraryBlock MAGIC_LIBRARY;
    public static SampleBlock SAMPLE;

    interface BlockMaker<T extends Block> {
        T makeBlock(RegistryKey<Block> key);
    }

    private static <T extends Block> T registerBlock(String name, BlockMaker<T> blockMaker) {
        MagicLibrary.LOGGER.info("Registering block: " + MagicLibrary.MODID + ":" + name);

        var identifier = MagicLibrary.id(name);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        return Registry.register(
            Registries.BLOCK,
            identifier,
            blockMaker.makeBlock(key));
    }

    public static void registerBlocks() {
        MAGIC_LIBRARY = registerBlock("magic_library", key -> {
            return new MagicLibraryBlock(AbstractBlock.Settings.copy(Blocks.BOOKSHELF).registryKey(key));
        });
        SAMPLE = registerBlock("sample", key -> {
            return new SampleBlock(AbstractBlock.Settings.copy(Blocks.STONE).registryKey(key));
        });
    }

}
