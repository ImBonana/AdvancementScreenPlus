package me.imbanana.advancementscreenplus.mixin.comp;

import betteradvancements.common.gui.BetterAdvancementWidget;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.imbanana.advancementscreenplus.config.ModConfig;
import me.imbanana.advancementscreenplus.mixin.accessor.BetterAdvancementWidgetAccessor;
import me.imbanana.advancementscreenplus.util.RenderUtils;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
        if (ModConfig.HANDLER.instance().shouldUseVanillaLines()) return;

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

    @WrapOperation(
            method = "refreshHover",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/Text;copy()Lnet/minecraft/text/MutableText;"
            )
    )
    private MutableText injectDescription(Text instance, Operation<MutableText> original) {
        return instance.copy().append(Text.literal("\n\n")).append(Text.translatable("advancementscreenplus.screen.description.complete").formatted(Formatting.BLUE));
    }
}
