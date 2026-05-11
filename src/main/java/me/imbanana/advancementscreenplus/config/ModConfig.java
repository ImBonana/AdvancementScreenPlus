package me.imbanana.advancementscreenplus.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import me.imbanana.advancementscreenplus.positioner.FruchtermanReingoldAdvancementPositioner;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementPositioner;

import java.util.function.Consumer;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(AdvancementScreenPlus.idOf("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(AdvancementScreenPlus.MOD_ID + ".json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()
            )
            .build();

    @SerialEntry
    private boolean useVanillaLines = false;

    @SerialEntry
    private boolean drawArrows = false;

    @SerialEntry
    private LayoutAlgorithm layoutAlgorithm = LayoutAlgorithm.FRUCHTERMAN_REINGOLD;

    public void setDrawArrows(boolean drawArrows) {
        this.drawArrows = drawArrows;
    }

    public boolean shouldDrawArrows() {
        return drawArrows;
    }

    public boolean shouldUseVnillaLines() {
        return this.useVanillaLines;
    }

    public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }

    public void setUseVanillaLines(boolean useVanillaLines) {
        this.useVanillaLines = useVanillaLines;
    }

    public enum LayoutAlgorithm {
        VANILLA(AdvancementPositioner::arrangeForTree),
        FRUCHTERMAN_REINGOLD(FruchtermanReingoldAdvancementPositioner::arrange);

        private final Consumer<Advancement> positioner;

        LayoutAlgorithm(Consumer<Advancement> positioner) {
            this.positioner = positioner;
        }

        public Consumer<Advancement> getPositioner() {
            return this.positioner;
        }
    }
}
