package me.imbanana.advancementscreenplus.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import net.minecraft.text.Text;

public class ModConfigScreenProvider implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.create(ModConfig.HANDLER, (defaults, config, builder) ->
                builder.title(createText("title"))
                .category(ConfigCategory.createBuilder()
                        .name(createText("title"))
                        .option(Option.<Boolean>createBuilder()
                                .name(createText("vanilla_lines"))
                                .description(OptionDescription.of(createText("vanilla_lines.desc")))
                                .binding(defaults.shouldUseVanillaLines(), config::shouldUseVanillaLines, config::setUseVanillaLines)
                                .controller(TickBoxControllerBuilderImpl::new)
                                .build()
                        )
                        .option(Option.<Boolean>createBuilder()
                                .name(createText("draw_arrows"))
                                .description(OptionDescription.of(createText("draw_arrows.desc")))
                                .binding(defaults.shouldDrawArrows(), config::shouldDrawArrows, config::setDrawArrows)
                                .controller(TickBoxControllerBuilderImpl::new)
                                .build()
                        )
                        .option(Option.<ModConfig.LayoutAlgorithm>createBuilder()
                                .name(createText("layout_algorithm"))
                                .description(OptionDescription.of(createText("layout_algorithm.desc")))
                                .binding(defaults.getLayoutAlgorithm(), config::getLayoutAlgorithm, config::setLayoutAlgorithm)
                                .controller(option -> EnumControllerBuilder.create(option)
                                        .enumClass(ModConfig.LayoutAlgorithm.class)
                                        .formatValue(v -> createText("layout_algorithm.option." + v.name().toLowerCase()))
                                )
                                .build()
                        )
                        .build()
                )
                .category(ConfigCategory.createBuilder()
                        .name(createText("layout"))
                        .group(OptionGroup.createBuilder()
                                .name(createText("layout.fruchterman_reingold"))
                                .description(OptionDescription.of(createText("layout.fruchterman_reingold.desc")))
                                .option(LabelOption.create(createText("layout.fruchterman_reingold.desc")))
                                .option(Option.<Integer>createBuilder()
                                        .name(createText("layout.fruchterman_reingold.iterations"))
                                        .binding(defaults.getFruchtermanReingoldIterations(), config::getFruchtermanReingoldIterations, config::setFruchtermanReingoldIterations)
                                        .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                .range(0, 1000)
                                                .step(1)
                                                .formatValue(v -> Text.literal(v + " Units"))
                                        )
                                        .build()
                                )
                                .option(Option.<Integer>createBuilder()
                                        .name(createText("layout.fruchterman_reingold.repulsion"))
                                        .binding(defaults.getFruchtermanReingoldRepulsion(), config::getFruchtermanReingoldRepulsion, config::setFruchtermanReingoldRepulsion)
                                        .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                .range(0, 100)
                                                .step(1)
                                                .formatValue(v -> Text.literal(v + " Units"))
                                        )
                                        .build()
                                )
                                .option(Option.<Float>createBuilder()
                                        .name(createText("layout.fruchterman_reingold.center_spacing"))
                                        .binding(defaults.getFruchtermanReingoldCenterSpacing(), config::getFruchtermanReingoldCenterSpacing, config::setFruchtermanReingoldCenterSpacing)
                                        .controller(option -> FloatSliderControllerBuilder.create(option)
                                                .range(0f, 10f)
                                                .step(0.5f)
                                                .formatValue(v -> Text.literal("%.1f Units".formatted(v)))
                                        )
                                        .build()
                                )
                                .option(Option.<Float>createBuilder()
                                        .name(createText("layout.fruchterman_reingold.spacing"))
                                        .binding(defaults.getFruchtermanReingoldSpacing(), config::getFruchtermanReingoldSpacing, config::setFruchtermanReingoldSpacing)
                                        .controller(option -> FloatSliderControllerBuilder.create(option)
                                                .range(0f, 10f)
                                                .step(0.5f)
                                                .formatValue(v -> Text.literal("%.1f Units".formatted(v)))
                                        )
                                        .build()
                                )
                                .build()
                        ).build()
                )
        ).generateScreen(parentScreen);
    }

    private static Text createText(String path) {
        return Text.translatable("config." + AdvancementScreenPlus.MOD_ID + "." + path);
    }
}
