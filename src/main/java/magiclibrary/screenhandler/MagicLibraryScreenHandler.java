package magiclibrary.screenhandler;

import magiclibrary.block.entity.MagicLibraryScreenData;
import magiclibrary.common.EnchantmentsContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class MagicLibraryScreenHandler extends ScreenHandler {
    private EnchantmentsContainer enchantmentsContainer;
    private int points;
    private BlockPos entityPos;
    private Inventory importImventory;

    public MagicLibraryScreenHandler(int syncId, PlayerInventory playerInventory, MagicLibraryScreenData data) {
        this(syncId, playerInventory, new SimpleInventory(1), EnchantmentsContainer.fromString(data.enchantments()), data.points(), data.entityPos());
    }

    public MagicLibraryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory importImventory, EnchantmentsContainer enchantmentsContainer, int points, BlockPos entityPos) {
        super(MagicLibraryScreenHandlers.MAGIC_LIBRARY, syncId);
        this.enchantmentsContainer = enchantmentsContainer;
        this.points = points;
        this.entityPos = entityPos;
        this.importImventory = importImventory;
        addSlot(new Slot(importImventory, 0, 192, 127));
        addPlayerSlots(playerInventory, 48, 150);
    }

    public int getPoints() {
        return points;
    }

    public EnchantmentsContainer getEnchantments() {
        return enchantmentsContainer;
    }

    public BlockPos getEntityPos() {
        return entityPos;
    }

    public void resetImportInventory() {
        importImventory.clear();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        var stack = getSlot(slot).getStack();
        if (!stack.isEmpty()) {
            if (slot != 0 && EnchantmentHelper.hasEnchantments(stack) && getSlot(0).getStack().isEmpty()) {
                if (insertItem(stack, 0, 1, false)) {
                    return stack;
                }
            }
            else if (slot == 0) {
                if (insertItem(stack, 1, 37, false)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public void deductPoints(int points) {
        this.points -= points;
        if (this.points < 0) {
            this.points = 0;
        }
    }

    public void updateEnchantmentsAndPoints(EnchantmentsContainer enchantmentsContainer, int points) {
        this.enchantmentsContainer = enchantmentsContainer;
        this.points = points;
    }
}
