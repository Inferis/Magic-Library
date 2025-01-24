package magiclibrary.screen;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import magiclibrary.MagicLibrary;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

public class EnchantmentListWidget extends ElementListWidget<EnchantmentListWidget.EnchantmentEntry> {
    private int x = 0;

    public EnchantmentListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.x = x;
    }
    
    public int addEntry(EnchantmentEntry entry) {
        entry.x = x;
        return super.addEntry(entry);
    }
    
    @Override
    public int getRowWidth() {
        return width - 32;
    }

    public static class EnchantmentEntry extends Entry<EnchantmentEntry> {
        public final Enchantment enchantment;
        public final int points;
        public final int level;
        public final boolean active;

        public int x = 0;
        private final List<ClickableWidget> children = new ObjectArrayList<>();

        public static EnchantmentEntry of(Enchantment enchantment, int level, int maxPoints, TextRenderer textRenderer, Consumer<EnchantmentEntry> onPressed) {
            return new EnchantmentEntry(enchantment, level, (int)Math.pow(2, level-1), maxPoints, textRenderer, onPressed);
        }
    
        private EnchantmentEntry(Enchantment enchantment, int level, int points, int maxPoints, TextRenderer textRenderer, Consumer<EnchantmentEntry> onPressed) {
            this.points = points;
            this.enchantment = enchantment;
            this.level = level;
            this.active = points <= maxPoints;

            var pointsText = MutableText.of(Text.of(points + " ").getContent());
            pointsText.append(Text.literal("pts"));

            var pointsButtonWidget = ButtonWidget.builder(pointsText, button -> {
                onPressed.accept(this);
            }).size(48, 18).build();
            pointsButtonWidget.active = this.active;
            pointsButtonWidget.setPosition(0, 1);

            var levelText = "0";
            switch (level) {
                case 1: levelText = "I"; break;
                case 2: levelText = "II"; break;
                case 3: levelText = "III"; break;
                case 4: levelText = "IV"; break;
                case 5: levelText = "V"; break;
                case 6: levelText = "VI"; break;
                case 7: levelText = "VII"; break;
                case 8: levelText = "VIII"; break;
                case 9: levelText = "IX"; break;
                case 10: levelText = "X"; break;
                default: levelText = "" + level; break;
            }

            var nameText = MutableText.of(enchantment.description().getContent());
            nameText.append(" " + levelText);

            var nameTextWidget = new TextWidget(nameText, textRenderer);
            nameTextWidget.alignLeft();
            nameTextWidget.setDimensionsAndPosition(100, 18,52, 1);
            children.add(pointsButtonWidget);
            children.add(nameTextWidget);
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            var pointsButton = children.getFirst();
            pointsButton.setPosition(x + 164, y-2);
            pointsButton.setDimensions(48, 18);
            pointsButton.render(context, mouseX, mouseY, tickDelta);

            var nameText = children.getLast();
            nameText.setPosition(x - 14, y-1);
            nameText.setDimensions(150, 18);
            nameText.render(context, mouseX, mouseY, tickDelta);
        }

    }
}
