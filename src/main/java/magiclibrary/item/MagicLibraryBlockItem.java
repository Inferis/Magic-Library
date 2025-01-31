package magiclibrary.item;

import magiclibrary.MagicLibrary;
import magiclibrary.common.EnchantmentsContainer;
import magiclibrary.item.components.MagicLibraryComponents;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class MagicLibraryBlockItem extends BlockItem {
    public MagicLibraryBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        var points = stack.get(MagicLibraryComponents.POINTS_COMPONENT);
        var pointsText = Text.translatable("tooltip.magiclibrary.points", points);
        tooltip.add(pointsText);

        var enchantments = EnchantmentsContainer.fromString(stack.get(MagicLibraryComponents.ENCHANTMENTS_COMPONENT));
        var enchantmentsText = Text.translatable("tooltip.magiclibrary.enchantments.title");
        for (var identifier: enchantments.getIdentifiers()) {
            enchantmentsText.append("\n");
            enchantmentsText.append(Text.translatable("tooltip.magiclibrary.enchantment", identifier, enchantments.get(identifier)));
        }
        tooltip.add(enchantmentsText);
    }
}
