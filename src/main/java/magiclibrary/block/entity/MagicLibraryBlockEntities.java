package magiclibrary.block.entity;

import magiclibrary.MagicLibrary;
import magiclibrary.block.MagicLibraryBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MagicLibraryBlockEntities {
    public static BlockEntityType<MagicLibraryBlockEntity> MAGIC_LIBRARY = register(
            "magic_library",
            FabricBlockEntityTypeBuilder.<MagicLibraryBlockEntity>create(MagicLibraryBlockEntity::new, MagicLibraryBlocks.MAGIC_LIBRARY).build()
    );
    public static BlockEntityType<SampleBlockEntity> SAMPLE = register(
            "sample",
            FabricBlockEntityTypeBuilder.<SampleBlockEntity>create(SampleBlockEntity::new, MagicLibraryBlocks.SAMPLE).build()
    );

    private static <T extends BlockEntityType<?>> T register(String identifier, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, MagicLibrary.id(identifier), blockEntityType);
    }

    public static void registerBlockEntities() {
    }
}
