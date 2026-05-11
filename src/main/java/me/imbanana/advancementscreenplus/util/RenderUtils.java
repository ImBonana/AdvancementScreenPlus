package me.imbanana.advancementscreenplus.util;

import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import me.imbanana.advancementscreenplus.config.ModConfig;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class RenderUtils {
    public static void renderConnectionLine(DrawContext context, int startX, int startY, int endX, int endY, int x, int y, AdvancementProgress progress, AdvancementProgress parentProgress) {
        int parentX = x + startX + 15;
        int parentY = y + startY + 13;
        int thizX = x + endX + 15;
        int thizY = y + endY + 13;

        int color = parentProgress == null || !parentProgress.isDone() ? 0xFF999999 : progress != null && progress.isDone() ? 0xFF00BB00 : 0xFFFFFFFF;

        float colorRed = ColorHelper.Argb.getRed(color) / 255f;
        float colorGreen = ColorHelper.Argb.getGreen(color) / 255f;
        float colorBlue = ColorHelper.Argb.getBlue(color) / 255f;

        boolean shouldDrawArrows = ModConfig.HANDLER.instance().shouldDrawArrows();

        int xDist = Math.abs(thizX - parentX);
        int yDist = Math.abs(thizY - parentY);

        if (xDist > yDist) {
            context.drawHorizontalLine(parentX, thizX, parentY, color);
            context.drawHorizontalLine(parentX, thizX, parentY - 1, color);
            context.drawVerticalLine(thizX, parentY, thizY, color);
            context.drawVerticalLine(thizX + 1, parentY, thizY, color);

            context.fill(thizX, parentY - 1, thizX + 2, parentY + 1, color);

            if (shouldDrawArrows) {
                context.setShaderColor(colorRed, colorGreen, colorBlue, 1.0F);
                if (thizY > parentY)
                    drawArrow(context, thizX - 7, thizY - 23, ArrowDir.DOWN);
                else if (thizY < parentY)
                    drawArrow(context, thizX - 7, thizY + 6, ArrowDir.UP);
                else if (thizX > parentX)
                    drawArrow(context, thizX - 22, thizY - 8, ArrowDir.RIGHT);
                else
                    drawArrow(context, thizX + 7, thizY - 8, ArrowDir.LEFT);
                context.setShaderColor(1F, 1F, 1F, 1.0F);
            }
        } else {
            context.drawVerticalLine(parentX, parentY, thizY, color);
            context.drawVerticalLine(parentX + 1, parentY, thizY, color);
            context.drawHorizontalLine(parentX, thizX, thizY, color);
            context.drawHorizontalLine(parentX, thizX, thizY - 1, color);

            context.fill(parentX, thizY - 1, parentX + 2, thizY + 1, color);

            if (shouldDrawArrows) {
                context.setShaderColor(colorRed, colorGreen, colorBlue, 1.0F);
                if (thizX > parentX)
                    drawArrow(context, thizX - 22, thizY - 8, ArrowDir.RIGHT);
                else if (thizX < parentX)
                    drawArrow(context, thizX + 7, thizY - 8, ArrowDir.LEFT);
                else if (thizY > parentY)
                    drawArrow(context, thizX - 7, thizY - 23, ArrowDir.DOWN);
                else
                    drawArrow(context, thizX - 7, thizY + 6, ArrowDir.UP);
                context.setShaderColor(1F, 1F, 1F, 1.0F);
            }
        }
    }

    private static void drawArrow(DrawContext context, int x, int y, ArrowDir dir) {
        Identifier sprite = new Identifier(AdvancementScreenPlus.MOD_ID, "textures/gui/arrow.png");
        context.drawTexture(sprite, x, y, 1, dir.getXOffset() * 16, dir.getYOffset() * 16, 16, 16, 32, 32);
    }

    private enum ArrowDir {
        RIGHT(0, 0),
        UP(1, 0),
        LEFT(0, 1),
        DOWN(1, 1);

        private final int xOffset;
        private final int yOffset;

        ArrowDir(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public int getYOffset() {
            return this.yOffset;
        }

        public int getXOffset() {
            return this.xOffset;
        }
    }
}
