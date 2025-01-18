package magiclibrary.screenhandler;

import magiclibrary.MagicLibrary;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class MagicLibraryScreenHandlers {
    public static final ScreenHandlerType<MagicLibraryScreenHandler> MAGIC_LIBRARY = Registry.register(
        Registries.SCREEN_HANDLER, 
        MagicLibrary.id("magic_library"), 
        new ScreenHandlerType<>(MagicLibraryScreenHandler::new, FeatureSet.empty())
    );

    public static void registerScreenHandlers() {
    }
}
