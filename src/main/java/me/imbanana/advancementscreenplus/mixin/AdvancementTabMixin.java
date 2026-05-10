package me.imbanana.advancementscreenplus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AdvancementTab.class)
public abstract class AdvancementTabMixin {
    @Shadow
    private double originX;

    @Shadow
    private double originY;

    @Shadow
    @Final
    private AdvancementsScreen screen;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(double offsetX, double offsetY, CallbackInfo ci) {
        this.originX += offsetX;
        this.originY += offsetY;
        ci.cancel();
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;originX:D",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void replaceOriginPosX(AdvancementTab instance, double value, @Local(name = "x", argsOnly = true) int x) {
        this.originX = (this.screen.width - x) / 2f;
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;originY:D",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void replaceOriginPosY(AdvancementTab instance, double value, @Local(name = "y", argsOnly = true) int y) {
        this.originY = (this.screen.height - y) / 2f;
    }

    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V"
            )
    )
    private void replaceScissorArgs(Args args) {
        args.set(2, screen.width);
        args.set(3, screen.height);
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 15))
    private int changeBackgroundRepeatCountWidth(int constant, @Local(name = "x", argsOnly = true) int x) {
        return (screen.width - x) / 16 + 1;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 8))
    private int changeBackgroundRepeatCountHeight(int constant, @Local(name = "y", argsOnly = true) int y) {
        return (screen.height - y) / 16 + 1;
    }

    @ModifyConstant(method = "drawWidgetTooltip", constant = @Constant(intValue = 234))
    private int changeWidth(int constant, @Local(name = "x", argsOnly = true) int x) {
        return screen.width - x;
    }

    @ModifyConstant(method = "drawWidgetTooltip", constant = @Constant(intValue = 113))
    private int changeHeight(int constant, @Local(name = "y", argsOnly = true) int y) {
        return screen.height - y;
    }
}
