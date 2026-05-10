package me.imbanana.advancementscreenplus.positioner;

import net.minecraft.advancement.Advancement;

import java.util.*;
import java.util.stream.StreamSupport;

public class FruchtermanReingoldAdvancementPositioner {
    private List<Advancement> allAdvancements = new ArrayList<>();
    private Advancement root;

    public FruchtermanReingoldAdvancementPositioner(Advancement root) {
        this.root = root;

        Set<Advancement> visited = new LinkedHashSet<>();
        Queue<Advancement> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            Advancement advancement = queue.poll();
            if (visited.add(advancement)) {
                this.allAdvancements.add(advancement);
                queue.addAll(StreamSupport.stream(advancement.getChildren().spliterator(), false).toList());
            }
        }
    }

    public void calc(int iterations, int repulsion, int centerSpacing, double spacing) {
        if (this.allAdvancements.isEmpty()) return;

        int advancementCount = this.allAdvancements.size();

        for (int i = 0; i < advancementCount; i++) {
            double angle = 2 * Math.PI * i / advancementCount;
            Advancement advancement = this.allAdvancements.get(i);
            if (advancement.getDisplay() != null) {
                advancement.getDisplay().setPos(
                        (float) (0.5f + Math.cos(angle) * 0.3f),
                        (float) (0.5f + Math.sin(angle) * 0.3f)
                );
            }
        }

        Map<Advancement, double[]> displacement = new HashMap<>();
        double k = spacing;

        for (int iter = 0; iter < iterations; iter++) {
            double temp = repulsion * 0.15 * (1 - (double) iter / iterations);

            for (Advancement advancement : allAdvancements) {
                displacement.put(advancement, new double[] {0, 0});
            }

            outside: for (int i = 0; i < advancementCount; i++) {
                inside: for (int j = i + 1; j < advancementCount; j++) {
                    Advancement first = this.allAdvancements.get(i);
                    Advancement last = this.allAdvancements.get(j);

                    if (first.getDisplay() == null) continue outside;
                    if (last.getDisplay() == null) continue inside;

                    double firstX = first.getDisplay().getX();
                    double firstY = first.getDisplay().getY();
                    double lastX = last.getDisplay().getX();
                    double lastY = last.getDisplay().getY();

                    double deltaX = firstX - lastX;
                    double deltaY = firstY - lastY;

                    double distance = Math.max(Math.sqrt(deltaX*deltaX + deltaY*deltaY), 0.01);
                    double force = (k * k) / distance;

                    double[] firstDisplacement = displacement.get(first);
                    double[] lastDisplacement = displacement.get(last);
                    firstDisplacement[0] += (deltaX / distance) * force;
                    firstDisplacement[1] += (deltaY / distance) * force;
                    lastDisplacement[0] -= (deltaX / distance) * force;
                    lastDisplacement[1] -= (deltaY / distance) * force;
                }
            }

            parentLoop: for (Advancement parent : this.allAdvancements) {
                childLoop: for (Advancement child : parent.getChildren()) {
                    if (parent.getDisplay() == null) continue parentLoop;
                    if (child.getDisplay() == null) continue childLoop;

                    double childX = child.getDisplay().getX();
                    double childY = child.getDisplay().getY();
                    double parentX = parent.getDisplay().getX();
                    double parentY = parent.getDisplay().getY();

                    double deltaX = childX - parentX;
                    double deltaY = childY - parentY;

                    double distance = Math.max(Math.sqrt(deltaX*deltaX + deltaY*deltaY), 0.01);
                    double force = (distance * distance) / k;

                    double[] parentDisplacement = displacement.get(parent);
                    double[] childDisplacement = displacement.get(child);
                    parentDisplacement[0] += (deltaX / distance) * force;
                    parentDisplacement[1] += (deltaY / distance) * force;
                    childDisplacement[0] -= (deltaX / distance) * force;
                    childDisplacement[1] -= (deltaY / distance) * force;
                }
            }

            for (Advancement child : root.getChildren()) {
                Advancement first = root;
                Advancement last = child;

                if (first.getDisplay() == null) break;
                if (last.getDisplay() == null) continue;

                double firstX = first.getDisplay().getX();
                double firstY = first.getDisplay().getY();
                double lastX = last.getDisplay().getX();
                double lastY = last.getDisplay().getY();

                double deltaX = firstX - lastX;
                double deltaY = firstY - lastY;

                double distance = Math.max(Math.sqrt(deltaX*deltaX + deltaY*deltaY), 0.01);
                double force = (centerSpacing * centerSpacing) / distance;

                double[] firstDisplacement = displacement.get(first);
                double[] lastDisplacement = displacement.get(last);
                firstDisplacement[0] += (deltaX / distance) * force;
                firstDisplacement[1] += (deltaY / distance) * force;
                lastDisplacement[0] -= (deltaX / distance) * force;
                lastDisplacement[1] -= (deltaY / distance) * force;
            }

            for (Advancement advancement : this.allAdvancements) {
                if(advancement == root) continue;
                if(advancement.getDisplay() == null) continue;
                double[] advancementDisplacement = displacement.get(advancement);

                double advancementX = advancement.getDisplay().getX();
                double advancementY = advancement.getDisplay().getY();

                double mag = Math.max(Math.sqrt(advancementDisplacement[0] * advancementDisplacement[0] + advancementDisplacement[1] * advancementDisplacement[1]), 0.01);

                double move = Math.min(mag, temp);
                advancementX += (advancementDisplacement[0] / mag) * move;
                advancementY += (advancementDisplacement[1] / mag) * move;

                advancement.getDisplay().setPos((float) advancementX, (float) advancementY);
            }
        }
    }

    public void alignWithGrid(int gridSize) {
        for (Advancement advancement : this.allAdvancements) {
            if (advancement.getDisplay() == null) continue;

            float advancementX = advancement.getDisplay().getX();
            float advancementY = advancement.getDisplay().getY();

            advancementX = (float) Math.floor(advancementX / gridSize) * gridSize + gridSize / 2f;
            advancementY = (float) Math.floor(advancementY / gridSize) * gridSize + gridSize / 2f;

            advancement.getDisplay().setPos(advancementX, advancementY);
        }
    }

    public static void arrange(Advancement root) {
        if (root.getDisplay() == null) {
            throw new IllegalArgumentException("Can't position children of an invisible root!");
        } else {
            FruchtermanReingoldAdvancementPositioner positioner = new FruchtermanReingoldAdvancementPositioner(root);

            positioner.calc(150, 3, 2, 1);
            positioner.alignWithGrid(1);
        }
    }
}
