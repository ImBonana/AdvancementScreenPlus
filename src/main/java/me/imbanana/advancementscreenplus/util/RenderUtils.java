package me.imbanana.advancementscreenplus.util;

import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.gui.DrawContext;

public class RenderUtils {
    public static void renderConnectionLine(DrawContext context, int startX, int startY, int endX, int endY, int x, int y, AdvancementProgress progress, AdvancementProgress parentProgress) {
        int parentX = x + startX + 15;
        int parentY = y + startY + 13;
        int thizX = x + endX + 15;
        int thizY = y + endY + 13;

        int color = parentProgress == null || !parentProgress.isDone() ? 0xFF999999 : progress != null && progress.isDone() ? 0xFF00BB00 : 0xFFFFFFFF;

        int xDist = Math.abs(thizX - parentX);
        int yDist = Math.abs(thizY - parentY);

        if (xDist > yDist) {
            context.drawHorizontalLine(parentX, thizX, parentY, color);
            context.drawHorizontalLine(parentX, thizX, parentY - 1, color);
            context.drawVerticalLine(thizX, parentY, thizY, color);
            context.drawVerticalLine(thizX + 1, parentY, thizY, color);
        } else {
            context.drawVerticalLine(parentX, parentY, thizY, color);
            context.drawVerticalLine(parentX + 1, parentY, thizY, color);
            context.drawHorizontalLine(parentX, thizX, thizY, color);
            context.drawHorizontalLine(parentX, thizX, thizY - 1, color);
        }
    }
}
