package me.imbanana.advancementscreenplus.mixin;

import com.google.gson.Gson;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import net.minecraft.advancement.Advancement;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.server.ServerAdvancementLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerAdvancementLoader.class)
public abstract class ServerAdvancementLoaderMixin extends JsonDataLoader {

    public ServerAdvancementLoaderMixin(Gson gson, String dataType) {
        super(gson, dataType);
    }

    @WrapWithCondition(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancement/AdvancementPositioner;arrangeForTree(Lnet/minecraft/advancement/Advancement;)V"
            )
    )
    private static boolean injectPositioner(Advancement root) {
        AdvancementScreenPlus.getPositioner().accept(root);
        return false;
    }
}
