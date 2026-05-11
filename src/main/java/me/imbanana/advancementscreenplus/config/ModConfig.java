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

    private int fruchtermanReingoldIterations = 150;
    private int fruchtermanReingoldRepulsion = 3;
    private float fruchtermanReingoldCenterSpacing = 2;
    private float fruchtermanReingoldSpacing = 1;

    public void setDrawArrows(boolean drawArrows) {
        this.drawArrows = drawArrows;
    }

    public boolean shouldDrawArrows() {
        return this.drawArrows;
    }

    public boolean shouldUseVanillaLines() {
        return this.useVanillaLines;
    }

    public void setUseVanillaLines(boolean useVanillaLines) {
        this.useVanillaLines = useVanillaLines;
    }

    public int getFruchtermanReingoldIterations() {
        return this.fruchtermanReingoldIterations;
    }

    public void setFruchtermanReingoldIterations(int fruchtermanReingoldIterations) {
        this.fruchtermanReingoldIterations = fruchtermanReingoldIterations;
    }

    public int getFruchtermanReingoldRepulsion() {
        return this.fruchtermanReingoldRepulsion;
    }

    public void setFruchtermanReingoldRepulsion(int fruchtermanReingoldRepulsion) {
        this.fruchtermanReingoldRepulsion = fruchtermanReingoldRepulsion;
    }

    public float getFruchtermanReingoldCenterSpacing() {
        return this.fruchtermanReingoldCenterSpacing;
    }

    public void setFruchtermanReingoldCenterSpacing(float fruchtermanReingoldCenterSpacing) {
        this.fruchtermanReingoldCenterSpacing = fruchtermanReingoldCenterSpacing;
    }

    public float getFruchtermanReingoldSpacing() {
        return this.fruchtermanReingoldSpacing;
    }

    public void setFruchtermanReingoldSpacing(float fruchtermanReingoldSpacing) {
        this.fruchtermanReingoldSpacing = fruchtermanReingoldSpacing;
    }

    public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        return this.layoutAlgorithm;
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
