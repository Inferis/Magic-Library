package magiclibrary.screenhandler;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record SampleScreenData(List<Date> dates, BlockPos entityPos) {
    public static final PacketCodec<RegistryByteBuf, SampleScreenData> CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, SampleScreenData value) {
            BlockPos.PACKET_CODEC.encode(buf, value.entityPos());

            buf.writeLong(value.dates().size());
            for (var date: value.dates()) {
                var seconds = date.toInstant().getEpochSecond();
                buf.writeLong(seconds);
            }
        }

        @Override
        public SampleScreenData decode(RegistryByteBuf buf) {
            var entityPos = BlockPos.PACKET_CODEC.decode(buf);

            var size = buf.readLong();
            var dates = new ArrayList<Date>();
            for (var i=0; i<size; ++i) {
                var seconds = buf.readLong();
                dates.add(Date.from(Instant.ofEpochSecond(seconds)));
            }
            return new SampleScreenData(dates, entityPos);
        }
    };
}
