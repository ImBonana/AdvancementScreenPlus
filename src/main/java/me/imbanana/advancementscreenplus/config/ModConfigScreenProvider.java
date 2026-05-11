package me.imbanana.advancementscreenplus.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import net.minecraft.text.Text;

public class ModConfigScreenProvider implements ModMenuApi {
    public static boolean shouldRenderArrows = false;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.create(ModConfig.HANDLER, (defaults, config, builder) ->
                builder.title(createText("title"))
                .category(ConfigCategory.createBuilder()
                        .name(createText("title"))
                        .option(Option.<Boolean>createBuilder()
                                .name(createText("vanilla_lines"))
                                .description(OptionDescription.of(createText("vanilla_lines.desc")))
                                .binding(defaults.shouldUseVnillaLines(), config::shouldUseVnillaLines, config::setUseVanillaLines)
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
        ).generateScreen(parentScreen);
    }

    private static Text createText(String path) {
        return Text.translatable("config." + AdvancementScreenPlus.MOD_ID + "." + path);
    }
}
