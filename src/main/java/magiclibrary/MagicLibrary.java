package magiclibrary;

import net.fabricmc.api.ModInitializer;
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
	}
}