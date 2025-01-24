package magiclibrary.screen;

import java.beans.Encoder;
import java.util.ArrayList;
import java.util.List;

import magiclibrary.MagicLibrary;
import magiclibrary.networking.ExtractEnchantmentC2SPayload;
import magiclibrary.screen.EnchantmentListWidget.EnchantmentEntry;
import magiclibrary.screenhandler.MagicLibraryScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MagicLibraryScreen extends HandledScreen<MagicLibraryScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = MagicLibrary.id("textures/gui/container/magic_library.png");

    private int originX;
    private int originY;
    private int pointsY;
    private EnchantmentListWidget listWidget;
    private TextWidget pointsWidget;

    public MagicLibraryScreen(MagicLibraryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 256;
        backgroundHeight = 231;
        playerInventoryTitleY = backgroundHeight - 94;
        playerInventoryTitleX += 40;
        pointsY = titleY;
    }

    @Override
    protected void init() {
        super.init();

        originX = (width - backgroundWidth) / 2;
        originY = (height - backgroundHeight) / 2;

        listWidget = addDrawableChild(new EnchantmentListWidget(client, 240, 106, originX + 8, originY + 19, 20));
        listWidget.setPosition(originX + 8, originY + 19);

        for (var entry: getCurrentEntries()) {
            listWidget.addEntry(entry);
        }
   }

    private List<EnchantmentEntry> getCurrentEntries() {
        var result = new ArrayList<EnchantmentEntry>();

        var enchantmentRegistry = client.world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if (enchantmentRegistry.isPresent()) {
            for (var identifier : handler.getEnchantments().getIdentifiers()) {
                var enchantment = enchantmentRegistry.get().getEntry(Identifier.of(identifier));
                if (enchantment.isPresent()) {
                    var maxLevel = handler.getEnchantments().get(identifier);
                    for (var level = 1; level <= maxLevel; level++) {
                        int level2 = level;
                        var entry = EnchantmentEntry.of(enchantment.get().value(), level, handler.getPoints(), getTextRenderer(), pressedEntry -> {
                            handler.deductPoints(pressedEntry.points);
                            ClientPlayNetworking.send(new ExtractEnchantmentC2SPayload(identifier, level2, handler.getEntityPos()));
                            var y = listWidget.getScrollY();
                            clearAndInit();
                            listWidget.setScrollY(y);
                        });
                        result.add(entry);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseX >= originX + 8 && mouseX < originX + 248 && mouseY >= originY + 19 && mouseY < 113) {
            listWidget.setScrollY(listWidget.getScrollY() + verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, originX, originY, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        Text pointsText;
        switch (handler.getPoints()) {
            case 0:
                pointsText = Text.translatable("screen.magic_library.text.no_points");
                break;
            case 1:
                pointsText = Text.translatable("screen.magic_library.text.one_point");
                break;
            default:
                pointsText = Text.literal("" + handler.getPoints()).append(Text.translatable("screen.magic_library.text.points"));
                break;
        }

        int width = (int)(getTextRenderer().getTextHandler().getWidth(pointsText));
        context.drawText(
            textRenderer,
            pointsText,
            248 - width, pointsY,
            0x009c00,
            false);
    }
}
