package magiclibrary.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

import java.util.Date;
import java.util.List;

public class SampleScreenHandler extends ScreenHandler {
    private List<Date> dates;
    private BlockPos entityPos;

    protected SampleScreenHandler(int syncId, PlayerInventory inventory, SampleScreenData data) {
        this(syncId, inventory, data.dates(), data.entityPos());
    }

    public SampleScreenHandler(int syncId, PlayerInventory inventory, List<Date> dates, BlockPos entityPos) {
        super(MagicLibraryScreenHandlers.SAMPLE, syncId);

        this.dates = dates;
        this.entityPos = entityPos;

        addPlayerSlots(inventory, 8, 84);
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public BlockPos getEntityPos() {
        return entityPos;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
