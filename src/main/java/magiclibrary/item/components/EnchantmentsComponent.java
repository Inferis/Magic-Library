// package magiclibrary.item.components;

// import com.mojang.serialization.Codec;
// import com.mojang.serialization.codecs.RecordCodecBuilder;

// public record EnchantmentsComponent(String enchantmentStream) {
//     public static final Codec<EnchantmentsComponent> CODEC = RecordCodecBuilder.create(builder -> {
// 	return builder.group(
// 		Codec.STRING.fieldOf("enchantments").forGetter(EnchantmentsComponent::enchantmentStream),
// 	).apply(builder, EnchantmentsComponent::new);
// });

// }
