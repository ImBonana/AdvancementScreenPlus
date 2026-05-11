package me.imbanana.advancementscreenplus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import me.imbanana.advancementscreenplus.screen.AdvancementListWidget;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AdvancementsScreen.class)
public abstract class AdvancementsScreenMixin extends Screen implements ClientAdvancementManager.Listener {
    @Shadow
    private @Nullable AdvancementTab selectedTab;

    @Shadow
    @Final
    private static Text EMPTY_TEXT;

    @Shadow
    @Final
    private static Text SAD_LABEL_TEXT;

    @Shadow
    @Final
    private Map<Advancement, AdvancementTab> tabs;

    @Unique
    private AdvancementListWidget tabButtons;
    @Unique
    private int tabsWidth = 200;

    protected AdvancementsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        this.tabsWidth = this.width / 4;

        this.tabButtons = new AdvancementListWidget((AdvancementsScreen) (Object) this, client, this.tabsWidth, this.height);

        for (AdvancementTab tab : this.tabs.values()) {
            this.tabButtons.addTab(tab);
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> this.close()).position(this.width - 25, 5).size(20, 20).build());
    }

    @Inject(method = "onRootAdded", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private void injectOnRootAdded(Advancement root, CallbackInfo ci, @Local AdvancementTab advancementTab) {
        if (this.tabButtons != null) this.tabButtons.addTab(advancementTab);
    }

    @Inject(method = "onClear", at = @At("HEAD"))
    private void injectOnClear(CallbackInfo ci) {
        if (this.tabButtons != null) this.tabButtons.clear();
    }

    @Definition(id = "button", local = @Local(type = int.class, argsOnly = true))
    @Expression("button == 0")
    @ModifyExpressionValue(
            method = "mouseClicked",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    private boolean disableDefaultTabs(boolean original) {
        return false;
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void injectMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if(this.tabButtons != null) {
            if (this.tabButtons.mouseClicked(mouseX, mouseY, button)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void injectMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        if(this.tabButtons != null) {
            if (this.tabButtons.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.tabButtons != null) {
            if (this.tabButtons.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.tabButtons != null) {
            if (this.tabButtons.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void replaceRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ci.cancel();

        this.renderBackgroundTexture(context);
        this.asp$renderAdvancementTree(context, this.tabsWidth, 0);
        this.asp$renderTabs(context, mouseX, mouseY, 0, 0, delta, this.tabsWidth);
        this.asp$renderWidgetToolTip(context, mouseX, mouseY, this.tabsWidth, 0);

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 500.0F);
        RenderSystem.enableDepthTest();
        super.render(context, mouseX, mouseY, delta);
        RenderSystem.disableDepthTest();
        context.getMatrices().pop();
    }

    @Unique
    private void asp$renderAdvancementTree(DrawContext context, int x, int y) {
        AdvancementTab advancementTab = this.selectedTab;
        if (advancementTab == null) {
            context.fill(x, y, this.width, this.height, -16777216);
            int textCenterX = x + (this.width - x) / 2;
            int textCenterY = y + (this.height - y) / 2;
            context.drawCenteredTextWithShadow(this.textRenderer, EMPTY_TEXT, textCenterX, textCenterY - this.textRenderer.fontHeight / 2, -1);
            context.drawCenteredTextWithShadow(this.textRenderer, SAD_LABEL_TEXT, textCenterX, textCenterY + this.textRenderer.fontHeight / 2, -1);
        } else {
            advancementTab.render(context, x, y);
        }
    }

    @Unique
    private void asp$renderTabs(DrawContext context, int mouseX, int mouseY, int x, int y, float delta, int tabWidth) {
        context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
        context.drawTexture(
                Screen.OPTIONS_BACKGROUND_TEXTURE,
                x,
                y,
                tabWidth - x,
                this.height - y,
                tabWidth,
                this.height,
                32,
                32
        );
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.tabButtons.render(context, mouseX, mouseY, delta);
    }

    @Unique
    private void asp$renderWidgetToolTip(DrawContext context, int mouseX, int mouseY, int x, int y) {
        if (this.selectedTab != null) {
            context.getMatrices().push();
            context.getMatrices().translate(x, y, 400.0F);
            RenderSystem.enableDepthTest();
            this.selectedTab.drawWidgetTooltip(context, mouseX - x, mouseY - y, x, y);
            RenderSystem.disableDepthTest();
            context.getMatrices().pop();
        }
    }
}
