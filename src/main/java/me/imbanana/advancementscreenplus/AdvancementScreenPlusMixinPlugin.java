package me.imbanana.advancementscreenplus;

import me.imbanana.advancementscreenplus.mixin.accessor.BetterAdvancementWidgetAccessor;
import me.imbanana.advancementscreenplus.mixin.comp.BetterAdvancementWidgetMixin;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class AdvancementScreenPlusMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals(BetterAdvancementWidgetAccessor.class.getName()) || mixinClassName.equals(BetterAdvancementWidgetMixin.class.getName())) {
            return FabricLoader.getInstance().isModLoaded("betteradvancements");
        }

        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
