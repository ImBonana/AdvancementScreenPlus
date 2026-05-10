package me.imbanana.advancementscreenplus.screen;

import com.google.common.collect.ImmutableList;
import me.imbanana.advancementscreenplus.AdvancementScreenPlus;
import me.imbanana.advancementscreenplus.mixin.accessor.AdvancementsScreenAccessor;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.IconButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class AdvancementListWidget extends ElementListWidget<AdvancementListWidget.Entry> {

    private final AdvancementsScreen parent;

    public AdvancementListWidget(AdvancementsScreen parent, MinecraftClient client, int width, int height) {
        super(client, width, height, 0, height, 20);

        this.parent = parent;
    }

    public void addTab(AdvancementTab tab) {
        this.addEntry(new TabEntry(tab));
    }

    public void clear() {
        this.clearEntries();
    }

    @Override
    public int getRowWidth() {
        return this.width - 10;
    }

    protected void deselectAll() {
        for (Entry entry : this.children()) {
            entry.deselect();
        }
    }

    public abstract static class Entry extends ElementListWidget.Entry<AdvancementListWidget.Entry> {
        abstract void deselect();
    }

    public class TabEntry extends Entry {

        private final AdvancementButtonWidget button;

        public TabEntry(AdvancementTab advancementTab) {
            this.button = AdvancementButtonWidget.builder(
                    advancementTab.getTitle(),
                    advancementTab.getDisplay().getIcon(),
                    btn -> {
                        ((AdvancementsScreenAccessor) AdvancementListWidget.this.parent).getClientAdvancementManager().selectTab(advancementTab.getRoot(), true);
                        AdvancementListWidget.this.deselectAll();
                        ((AdvancementButtonWidget) btn).setSelected(true);
                    }
                )
                .tooltip(Tooltip.of(advancementTab.getTitle()))
                .dimensions(0, 0, AdvancementListWidget.this.getRowWidth(), AdvancementListWidget.this.itemHeight)
                .build();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.button);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(button);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.button.setPosition(x, y);
            this.button.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void deselect() {
            this.button.setSelected(false);
        }
    }
}
