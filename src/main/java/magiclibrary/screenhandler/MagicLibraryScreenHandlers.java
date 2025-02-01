package magiclibrary.screenhandler;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryScreenData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class MagicLibraryScreenHandlers {
    public static final ScreenHandlerType<MagicLibraryScreenHandler> MAGIC_LIBRARY = Registry.register(
        Registries.SCREEN_HANDLER, 
        MagicLibrary.id("magic_library"), 
        new ExtendedScreenHandlerType<>(MagicLibraryScreenHandler::new, MagicLibraryScreenData.CODEC)
    );

    public static void registerScreenHandlers() {
    }
}
