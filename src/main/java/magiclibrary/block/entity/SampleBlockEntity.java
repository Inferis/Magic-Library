package magiclibrary.block.entity;

import magiclibrary.screenhandler.SampleScreenData;
import magiclibrary.screenhandler.SampleScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<SampleScreenData> {
    private List<Date> dates = new ArrayList<>();

    public SampleBlockEntity(BlockPos pos, BlockState state) {
        super(MagicLibraryBlockEntities.SAMPLE, pos, state);
    }

    public void addDate() {
        dates.add(new Date());
    }

    public List<Date> getDates() {
        return dates;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SampleScreenHandler(syncId, playerInventory, dates, pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Sample");
    }

    @Override
    public SampleScreenData getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new SampleScreenData(dates, pos);
    }
}
