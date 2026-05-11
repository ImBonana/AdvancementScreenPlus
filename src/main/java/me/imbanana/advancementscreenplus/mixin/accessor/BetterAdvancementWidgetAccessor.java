package me.imbanana.advancementscreenplus.mixin.accessor;

import betteradvancements.common.gui.BetterAdvancementWidget;
import net.minecraft.advancement.AdvancementProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BetterAdvancementWidget.class)
public interface BetterAdvancementWidgetAccessor {
    @Accessor("advancementProgress")
    AdvancementProgress getProgress();
}
