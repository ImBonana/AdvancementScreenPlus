package me.imbanana.advancementscreenplus.mixin;

import me.imbanana.advancementscreenplus.mixin.accessor.AdvancementWidgetAccessor;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AdvancementWidget.class)
public abstract class AdvancementWidgetMixin {
    @Shadow
    private @Nullable AdvancementWidget parent;

    @Shadow
    @Final
    private List<AdvancementWidget> children;

    @Shadow
    @Final
    private int x;

    @Shadow
    @Final
    private int y;

    @Shadow
    private @Nullable AdvancementProgress progress;

    @Inject(method = "renderLines", at = @At("HEAD"), cancellable = true)
    private void replaceRenderLines(DrawContext context, int x, int y, boolean border, CallbackInfo ci) {
        ci.cancel();

        if (parent != null) {
            int parentX = x + this.parent.getX() + 15;
            int parentY = y + this.parent.getY() + 13;
            int thizX = x + this.x + 15;
            int thizY = y + this.y + 13;

            AdvancementProgress parentProgress = ((AdvancementWidgetAccessor) this.parent).getProgress();

            int color = parentProgress == null || !parentProgress.isDone() ? 0xFF999999 : this.progress != null && this.progress.isDone() ? 0xFF00BB00 : 0xFFFFFFFF;

            int xDist = Math.abs(thizX - parentX);
            int yDist = Math.abs(thizY - parentY);

            if (xDist > yDist) {
                context.drawHorizontalLine(thizX, parentX, thizY, color);
                context.drawHorizontalLine(thizX, parentX, thizY - 1, color);
                context.drawVerticalLine(parentX, thizY, parentY, color);
                context.drawVerticalLine(parentX + 1, thizY, parentY, color);
            } else {
                context.drawVerticalLine(parentX, thizY, parentY, color);
                context.drawVerticalLine(parentX + 1, thizY, parentY, color);
                context.drawHorizontalLine(thizX, parentX, thizY, color);
                context.drawHorizontalLine(thizX, parentX, thizY - 1, color);
            }
        }

        for (AdvancementWidget advancementWidget : this.children) {
            advancementWidget.renderLines(context, x, y, border);
        }
    }
}
