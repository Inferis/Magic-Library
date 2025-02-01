package magiclibrary.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DateListWidget extends ElementListWidget<DateListWidget.DateEntry> {
    private int x = 0;

    public DateListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.x = x;
    }

    public int addEntry(DateListWidget.DateEntry entry) {
        entry.x = x;
        return super.addEntry(entry);
    }

    @Override
    public int getRowWidth() {
        return width - 32;
    }

    public static class DateEntry extends Entry<DateListWidget.DateEntry> {
        private final List<ClickableWidget> children = new ObjectArrayList<>();
        private int x = 0;

        public static DateEntry of(Date date, TextRenderer textRenderer) {
            return new DateEntry(date, textRenderer);
        }

        private DateEntry(Date date, TextRenderer textRenderer) {
            var dateString = DateFormat.getDateTimeInstance().format(date);
            var textWidget = new TextWidget(Text.literal(dateString), textRenderer);
            textWidget.alignLeft();
            children.add(textWidget);
        }

        @Override
        public List<? extends Element> children() { return children; }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            var dateText = children.getFirst();
            dateText.setPosition(x - 14, y-1);
            dateText.setDimensions(110, 16);
            dateText.render(context, mouseX, mouseY, tickDelta);
        }
    }
}
