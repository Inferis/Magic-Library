package magiclibrary.item.components;

import com.mojang.serialization.Codec;

import magiclibrary.MagicLibrary;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MagicLibraryComponents {
    public static final ComponentType<String> ENCHANTMENTS_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            MagicLibrary.id("enchantments"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );
    public static final ComponentType<Integer> POINTS_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            MagicLibrary.id("points"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void registerComponents() {
    }
}
