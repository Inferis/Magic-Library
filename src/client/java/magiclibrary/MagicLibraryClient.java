package magiclibrary;

import magiclibrary.networking.UpdateScreenS2CPayload;
import magiclibrary.screen.MagicLibraryScreen;
import magiclibrary.screenhandler.MagicLibraryScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class MagicLibraryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        HandledScreens.register(MagicLibraryScreenHandlers.MAGIC_LIBRARY, MagicLibraryScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(UpdateScreenS2CPayload.ID, MagicLibraryClient::handleUpdateScreenPayload);
	}

	private static void handleUpdateScreenPayload(UpdateScreenS2CPayload updateScreenS2CPayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (context.client().currentScreen instanceof MagicLibraryScreen magicLibraryScreen) {
				magicLibraryScreen.update();
			}
		});
	}
}