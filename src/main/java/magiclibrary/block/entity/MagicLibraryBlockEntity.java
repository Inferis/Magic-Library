package magiclibrary.block.entity;

import magiclibrary.MagicLibrary;
import magiclibrary.common.EnchantmentsContainer;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MagicLibraryBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private EnchantmentsContainer enchantments = new EnchantmentsContainer();
    private int points = 0;

    public MagicLibraryBlockEntity(BlockPos pos, BlockState state) {
        super(MagicLibraryBlockEntities.MAGIC_LIBRARY, pos, state);
    }

    public void addPoints(int points) {
        this.points += points;
        markDirty();
    }

    public int getPoints() { 
        return points;
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
        nbt.putInt("points", points);
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registries) {
        super.readNbt(nbt, registries);
        enchantments.fromString(nbt.getString("enchantments"));
        points = nbt.getInt("points");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MagicLibraryScreenHandler(syncId, playerInventory, enchantments, points);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.magic_library.title");
    }
}
