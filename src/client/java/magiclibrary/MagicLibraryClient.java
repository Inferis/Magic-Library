package magiclibrary;

import magiclibrary.screen.MagicLibraryScreen;
import magiclibrary.screenhandler.MagicLibraryScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class MagicLibraryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(MagicLibraryScreenHandlers.MAGIC_LIBRARY, MagicLibraryScreen::new);
	}
}