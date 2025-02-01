package magiclibrary.networking;

import magiclibrary.MagicLibrary;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record UpdateDatesS2CPayload(List<Date> dates) implements CustomPayload {
    public static Id<UpdateDatesS2CPayload> ID = new CustomPayload.Id<>(MagicLibrary.id("update_dates_s2c_payload"));
    public static PacketCodec<RegistryByteBuf, UpdateDatesS2CPayload> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, UpdateDatesS2CPayload value) {
            buf.writeLong(value.dates().size());
            for (var date: value.dates()) {
                var seconds = date.toInstant().getEpochSecond();
                buf.writeLong(seconds);
            }
        }

        @Override
        public UpdateDatesS2CPayload decode(RegistryByteBuf buf) {
            var size = buf.readLong();
            var dates = new ArrayList<Date>();
            for (var i=0; i<size; ++i) {
                var seconds = buf.readLong();
                dates.add(Date.from(Instant.ofEpochSecond(seconds)));
            }
            return new UpdateDatesS2CPayload(dates);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
