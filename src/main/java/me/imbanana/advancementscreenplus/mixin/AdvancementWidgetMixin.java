package me.imbanana.advancementscreenplus.mixin;

import me.imbanana.advancementscreenplus.mixin.accessor.AdvancementWidgetAccessor;
import me.imbanana.advancementscreenplus.util.RenderUtils;
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
            RenderUtils.renderConnectionLine(
                    context,
                    this.parent.getX(),
                    this.parent.getY(),
                    this.x,
                    this.y,
                    x,
                    y,
                    this.progress,
                    ((AdvancementWidgetAccessor) this.parent).getProgress()
            );
        }

        for (AdvancementWidget advancementWidget : this.children) {
            advancementWidget.renderLines(context, x, y, border);
        }
    }
}
