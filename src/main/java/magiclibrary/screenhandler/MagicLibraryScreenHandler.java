package magiclibrary.screenhandler;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryScreenData;
import magiclibrary.common.EnchantmentsContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class MagicLibraryScreenHandler extends ScreenHandler {
    private EnchantmentsContainer enchantmentsContainer;
    private int points;
    private BlockPos entityPos;

    public MagicLibraryScreenHandler(int syncId, PlayerInventory playerInventory, MagicLibraryScreenData data) {
        this(syncId, playerInventory, EnchantmentsContainer.fromString(data.enchantments()), data.points(), data.entityPos());
    }

    @Override
    public void syncState() {
        MagicLibrary.LOGGER.info("syncState");
        super.syncState();
    }

    public MagicLibraryScreenHandler(int syncId, PlayerInventory playerInventory, EnchantmentsContainer enchantmentsContainer, int points, BlockPos entityPos) {
        super(MagicLibraryScreenHandlers.MAGIC_LIBRARY, syncId);
        this.enchantmentsContainer = enchantmentsContainer;
        this.points = points;
        this.entityPos = entityPos;
        addPlayerSlots(playerInventory, 48, 149);
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    public void deductPoints(int points) {
        this.points -= points;
        if (this.points < 0) {
            this.points = 0;
        }
    }
}
