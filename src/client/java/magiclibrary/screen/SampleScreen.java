package magiclibrary.screen;

import magiclibrary.MagicLibrary;
import magiclibrary.networking.AddDateC2SPayload;
import magiclibrary.screenhandler.SampleScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SampleScreen extends HandledScreen<SampleScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = MagicLibrary.id("textures/gui/container/sample.png");

    private int originX;
    private int originY;
    private DateListWidget listWidget;

    public SampleScreen(SampleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundWidth = 176;
        backgroundHeight = 166;
        playerInventoryTitleY = backgroundHeight - 94;
    }

    protected void init() {
        super.init();

        originX = (width - backgroundWidth) / 2;
        originY = (height - backgroundHeight) / 2;

        createListWidget(getDateEntries());

        var addButtonWidget = addDrawableChild(ButtonWidget.builder(Text.literal("Add"), button -> {
            ClientPlayNetworking.send(new AddDateC2SPayload());
        }).size(32, 50).build());
        addButtonWidget.setPosition(originX + 135, originY + 18);
    }

    private void createListWidget(List<DateListWidget.DateEntry> dateEntries) {
        listWidget = addDrawableChild(new DateListWidget(client, 120, 48, originX + 8, originY + 19, 12));
        listWidget.setPosition(originX + 8, originY + 19);

        dateEntries.forEach(entry -> listWidget.addEntry(entry));
    }

    private List<DateListWidget.DateEntry> getDateEntries() {
        return getScreenHandler().getDates().stream().map(d -> DateListWidget.DateEntry.of(d, getTextRenderer())).toList();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, originX, originY, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    public void refresh() {
        var y = listWidget.getScrollY();
        clearAndInit();
        listWidget.setScrollY(y);
    }
}
