package me.imbanana.advancementscreenplus.mixin.comp;

import betteradvancements.common.gui.BetterAdvancementWidget;
import me.imbanana.advancementscreenplus.config.ModConfig;
import me.imbanana.advancementscreenplus.mixin.accessor.BetterAdvancementWidgetAccessor;
import me.imbanana.advancementscreenplus.util.RenderUtils;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BetterAdvancementWidget.class)
public abstract class BetterAdvancementWidgetMixin {
    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Shadow
    private AdvancementProgress advancementProgress;

    @Inject(method = "drawConnection", at = @At(value = "HEAD"), cancellable = true)
    private void replaceRenderLines(DrawContext guiGraphics, BetterAdvancementWidget parent, int scrollX, int scrollY, boolean drawInside, CallbackInfo ci) {
        if (ModConfig.HANDLER.instance().shouldUseVnillaLines()) return;

        ci.cancel();

        RenderUtils.renderConnectionLine(
                guiGraphics,
                parent.getX(),
                parent.getY(),
                this.x,
                this.y,
                scrollX,
                scrollY,
                this.advancementProgress,
                ((BetterAdvancementWidgetAccessor) parent).getProgress()
        );
    }
}
