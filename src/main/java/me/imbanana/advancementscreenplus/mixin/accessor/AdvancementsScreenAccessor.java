package me.imbanana.advancementscreenplus.mixin.accessor;

import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.network.ClientAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AdvancementsScreen.class)
public interface AdvancementsScreenAccessor {
    @Accessor(value = "advancementHandler")
    ClientAdvancementManager getClientAdvancementManager();
}
