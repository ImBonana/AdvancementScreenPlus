package me.imbanana.advancementscreenplus;

import me.imbanana.advancementscreenplus.config.ModConfig;
import me.imbanana.advancementscreenplus.positioner.FruchtermanReingoldAdvancementPositioner;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.Advancement;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AdvancementScreenPlus implements ModInitializer {
    public static final String MOD_ID = "advancementscreenplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfig.HANDLER.load();
    }

    public static Consumer<Advancement> getPositioner() {
//        return AdvancementPositioner::arrangeForTree;
        return ModConfig.HANDLER.instance().getLayoutAlgorithm().getPositioner();
    }

    public static Identifier idOf(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
