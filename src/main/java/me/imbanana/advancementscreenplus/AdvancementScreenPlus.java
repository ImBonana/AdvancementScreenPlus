package me.imbanana.advancementscreenplus;

import me.imbanana.advancementscreenplus.positioner.FruchtermanReingoldAdvancementPositioner;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementPositioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AdvancementScreenPlus implements ModInitializer {
    public static final String MOD_ID = "advancementscreenplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

    }

    public static Consumer<Advancement> getPositioner() {
//        return AdvancementPositioner::arrangeForTree;
        return FruchtermanReingoldAdvancementPositioner::arrange;
    }
}
