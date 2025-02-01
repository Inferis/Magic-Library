package magiclibrary;

import magiclibrary.networking.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import magiclibrary.block.MagicLibraryBlocks;
import magiclibrary.block.entity.MagicLibraryBlockEntities;
import magiclibrary.item.MagicLibraryItems;
import magiclibrary.item.components.MagicLibraryComponents;
import magiclibrary.screenhandler.MagicLibraryScreenHandlers;

public class MagicLibrary implements ModInitializer {
	public static final String MODID = "magiclibrary";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Identifier id(String id) {
		return Identifier.of(MODID, id);
	}

	@Override
	public void onInitialize() {
		MagicLibraryBlocks.registerBlocks();
		MagicLibraryBlockEntities.registerBlockEntities();
		MagicLibraryItems.registerItems();
		MagicLibraryItems.registerItemGroups();
		MagicLibraryComponents.registerComponents();
		MagicLibraryScreenHandlers.registerScreenHandlers();

		registerNetworkPayloads();
		registerNetworkReceivers();
	}

	private void registerNetworkPayloads() {
		PayloadTypeRegistry.playC2S().register(ExportEnchantmentC2SPayload.ID, ExportEnchantmentC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ImportEnchantmentsC2SPayload.ID, ImportEnchantmentsC2SPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateScreenS2CPayload.ID, UpdateScreenS2CPayload.CODEC);

		PayloadTypeRegistry.playC2S().register(AddDateC2SPayload.ID, AddDateC2SPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateDatesS2CPayload.ID, UpdateDatesS2CPayload.CODEC);
	}

	private void registerNetworkReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(ExportEnchantmentC2SPayload.ID, ExportEnchantmentC2SPayload::handle);
		ServerPlayNetworking.registerGlobalReceiver(ImportEnchantmentsC2SPayload.ID, ImportEnchantmentsC2SPayload::handle);

		ServerPlayNetworking.registerGlobalReceiver(AddDateC2SPayload.ID, AddDateC2SPayload::handle);
	}
}