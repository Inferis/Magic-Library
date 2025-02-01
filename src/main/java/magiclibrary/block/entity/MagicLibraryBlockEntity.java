package magiclibrary.block.entity;

import magiclibrary.MagicLibrary;
import magiclibrary.block.MagicLibraryBlock;
import magiclibrary.common.EnchantmentsContainer;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MagicLibraryBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<MagicLibraryScreenData>, InventoryChangedListener {
    private EnchantmentsContainer enchantments = new EnchantmentsContainer();
    private int points = 0;
    private SimpleInventory importInventory;

    public MagicLibraryBlockEntity(BlockPos pos, BlockState state) {
        super(MagicLibraryBlockEntities.MAGIC_LIBRARY, pos, state);
        importInventory = new SimpleInventory(1);
        importInventory.addListener(this);
    }

    public void addPointsForLevel(int level, Enchantment enchantment) {
        points += (int)Math.pow(2, level-1) * enchantment.getAnvilCost();
        markDirty();
    }

    public void deductPointsForLevel(int level, Enchantment enchantment) {
        points -= (int)Math.pow(2, level-1) * enchantment.getAnvilCost();
        if (points < 0) {
            points = 0;
        }
        markDirty();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
        enchantments = EnchantmentsContainer.fromString(enchantmentsString);
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
        nbt.put("importInventory", importInventory.toNbtList(registries));
    }

    @Override
    protected void readNbt(NbtCompound nbt, WrapperLookup registries) {
        super.readNbt(nbt, registries);
        enchantments = EnchantmentsContainer.fromString(nbt.getString("enchantments"));
        points = nbt.getInt("points");
        if (nbt.contains("importInventory")) {
            importInventory.readNbtList(nbt.getList("importInventory", 10), registries);
        }
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MagicLibraryScreenHandler(syncId, playerInventory, importInventory, enchantments, points, getPos());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.magiclibrary.magic_library.title");
    }

    @Override
    public MagicLibraryScreenData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new MagicLibraryScreenData(points, getEnchantmentsString(), getPos());
    }

    public void importEnchantments(ItemStack stack) {
        if (!stack.isEmpty()) {
            // register enchantments
            var enchantments = EnchantmentHelper.getEnchantments(stack);
            for (var enchantment: enchantments.getEnchantments()) {
                var level = enchantments.getLevel(enchantment);
                registerEnchantment(enchantment.getIdAsString(), level);
                addPointsForLevel(level, enchantment.value());
            }

            var state = world.getBlockState(pos);
            if (state.getBlock() instanceof MagicLibraryBlock libraryBlock) {
                libraryBlock.updateFilled(world, pos, state);
            }
        }
    }

    public EnchantmentsContainer getEnchantments() {
        return enchantments.copy();
    }

    public ItemStack getImportStack() {
        return importInventory.getStack(0);
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        markDirty();
    }
}
