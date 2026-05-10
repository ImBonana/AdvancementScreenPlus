package me.imbanana.advancementscreenplus.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class AdvancementButtonWidget extends ButtonWidget {
    private ItemStack icon;
    private boolean selected = false;

    public static AdvancementButtonWidget.Builder builder(Text message, ItemStack icon, ButtonWidget.PressAction onPress) {
        return new AdvancementButtonWidget.Builder(message, icon, onPress);
    }

    protected AdvancementButtonWidget(int x, int y, int width, int height, Text message, ItemStack icon, boolean selected, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
        this.icon = icon;
        this.selected = selected;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int color = this.selected ? 0xFFFFFFFF : 0xFFAAAAAA;
        this.drawScrollableText(context, minecraftClient.textRenderer, 24, color);
        context.drawItemWithoutEntity(this.icon, this.getX() + 2, this.getY() + 2);
    }

    @Override
    protected void drawScrollableText(DrawContext context, TextRenderer textRenderer, int xMargin, int color) {
        int i = this.getX() + xMargin;
        int j = this.getX() + this.getWidth() - xMargin;
        drawScrollableText(context, textRenderer, this.getMessage(), i, this.getY(), j, this.getY() + this.getHeight(), color);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    protected static void drawScrollableText(DrawContext context, TextRenderer textRenderer, Text text, int left, int top, int right, int bottom, int color) {
        int i = textRenderer.getWidth(text);
        int j = (top + bottom - 9) / 2 + 1;
        int k = right - left;
        if (i > k) {
            int l = i - k;
            double d = Util.getMeasuringTimeMs() / 1000.0;
            double e = Math.max(l * 0.5, 3.0);
            double f = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d / e)) / 2.0 + 0.5;
            double g = MathHelper.lerp(f, 0.0, (double)l);
            context.enableScissor(left, top, right, bottom);
            context.drawTextWithShadow(textRenderer, text, left - (int)g, j, color);
            context.disableScissor();
        } else {
            context.drawTextWithShadow(textRenderer, text, left, j, color);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final ItemStack icon;
        private final ButtonWidget.PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private boolean selected = false;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ButtonWidget.NarrationSupplier narrationSupplier = ButtonWidget.DEFAULT_NARRATION_SUPPLIER;

        public Builder(Text message, ItemStack icon, ButtonWidget.PressAction onPress) {
            this.message = message;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public Builder narrationSupplier(ButtonWidget.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public AdvancementButtonWidget build() {
            AdvancementButtonWidget buttonWidget = new AdvancementButtonWidget(this.x, this.y, this.width, this.height, this.message, this.icon, this.selected, this.onPress, this.narrationSupplier);
            buttonWidget.setTooltip(this.tooltip);
            return buttonWidget;
        }
    }
}
