package magiclibrary.screen;

import magiclibrary.MagicLibrary;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MagicLibraryScreen extends HandledScreen<MagicLibraryScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = MagicLibrary.id("textures/gui/container/magic_library.png");

    private int originX;
    private int originY;
    private int pointsY;

    public MagicLibraryScreen(MagicLibraryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 212;
        playerInventoryTitleY = backgroundHeight - 94;
        pointsY = 99;
    }

    @Override
    protected void init() {
        super.init();
        
        originX = (width - backgroundWidth) / 2;
        originY = (height - backgroundHeight) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, originX, originY, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        context.drawText(
            textRenderer, 
            Text.literal("" + handler.getPoints()).append(Text.translatable("screen.magic_library.text.points")), 
            titleX, pointsY, 
            0x009c00, 
            false);
    }
}
