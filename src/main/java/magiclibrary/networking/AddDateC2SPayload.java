package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryBlockEntity;
import magiclibrary.block.entity.SampleBlockEntity;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import magiclibrary.screenhandler.SampleScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record AddDateC2SPayload() implements CustomPayload {
    public static Id<AddDateC2SPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("add_date_c2s_payload"));
    public static PacketCodec<RegistryByteBuf, AddDateC2SPayload> CODEC = PacketCodec.unit(new AddDateC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            var screenHandler = context.player().currentScreenHandler;
            if (screenHandler instanceof SampleScreenHandler sampleScreenHandler) {
                var pos = sampleScreenHandler.getEntityPos();
                var blockEntity = context.player().getWorld().getBlockEntity(pos);
                if (blockEntity instanceof SampleBlockEntity sampleBlockEntity) {
                    // Add a date
                    sampleBlockEntity.addDate();
                    // update server side screen handler
                    sampleScreenHandler.setDates(sampleBlockEntity.getDates());
                    // update client side screen handler
                    ServerPlayNetworking.send(context.player(), new UpdateDatesS2CPayload(sampleBlockEntity.getDates()));
                }
            }
        });
    }
}
