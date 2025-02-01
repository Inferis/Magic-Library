package magiclibrary;

import magiclibrary.networking.UpdateDatesS2CPayload;
import magiclibrary.networking.UpdateScreenS2CPayload;
import magiclibrary.screen.MagicLibraryScreen;
import magiclibrary.screen.SampleScreen;
import magiclibrary.screenhandler.MagicLibraryScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class MagicLibraryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(MagicLibraryScreenHandlers.MAGIC_LIBRARY, MagicLibraryScreen::new);
		HandledScreens.register(MagicLibraryScreenHandlers.SAMPLE, SampleScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(UpdateScreenS2CPayload.ID, MagicLibraryClient::handleUpdateScreenPayload);
		ClientPlayNetworking.registerGlobalReceiver(UpdateDatesS2CPayload.ID, MagicLibraryClient::handleUpdateDatesPayload);
	}

	private static void handleUpdateDatesPayload(UpdateDatesS2CPayload payload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (context.client().currentScreen instanceof SampleScreen sampleScreen) {
				sampleScreen.getScreenHandler().setDates(payload.dates());
				sampleScreen.refresh();
			}
		});
	}

	private static void handleUpdateScreenPayload(UpdateScreenS2CPayload payload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (context.client().currentScreen instanceof MagicLibraryScreen magicLibraryScreen) {
				magicLibraryScreen.update(payload.enchantmentsString(), payload.points());
			}
		});
	}
}