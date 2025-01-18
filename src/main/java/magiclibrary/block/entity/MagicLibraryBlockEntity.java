package magiclibrary.block.entity;

import magiclibrary.MagicLibrary;
import magiclibrary.common.EnchantmentsContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.math.BlockPos;

public class MagicLibraryBlockEntity extends BlockEntity {
    private EnchantmentsContainer enchantments = new EnchantmentsContainer();

    public MagicLibraryBlockEntity(BlockPos pos, BlockState state) {
        super(MagicLibraryBlockEntities.MAGIC_LIBRARY, pos, state);
    }

    public boolean registerEnchantment(String identifier, int level) {
        if (enchantments.put(identifier, level)) {
            markDirty();
            return true;
        }
        else {
            return false;
        }
    }

    public int getEnchantmentsSize() {
        return enchantments.getIdentifiers().size();
    }

    public String getEnchantmentsString() {
        return enchantments.toString();
    }

    public void setEnchantmentsString(String enchantmentsString) {
        enchantments.fromString(enchantmentsString);
        markDirty();
    }

    @SuppressWarnings("unused")
    private void logEnchantments() {
        MagicLibrary.LOGGER.info("Enchantments in library:");
        for (var identifier: enchantments.getIdentifiers()) {
            MagicLibrary.LOGGER.info("* " + identifier + " " + enchantments.get(identifier));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putString("enchantments", enchantments.toString());
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if (nbt.contains("enchantments")) {
            enchantments.fromString(nbt.getString("enchantments"));
        }
    }
}
