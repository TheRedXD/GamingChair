package sh.thered.gamingchair.client.mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActiveMods extends Mod {
    static String name = "gc.activemods";
    static String description = "Shows all currently active mods.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    public static List<String> texts;
    public static List<Integer> textX;
    public static List<Integer> textY;
    private static int posY;
    private static int longestLength;
    private static int pastScaledWidth;

    public static boolean shouldUpdate = true;

    static MinecraftClient mc = MinecraftClient.getInstance();
    public static void cycle(DrawContext drawContext, float v) {
        if(isDisabled(name)) return;
        if (!mc.getDebugHud().shouldShowDebugHud() && !mc.options.hudHidden) {
            int rainbowInt = Utils.getRainbowInt();
            List<String> activeStates = Mod.getActiveStates();
            activeStates.sort(String::compareTo);
            if(!activeStates.isEmpty()) {
                if (pastScaledWidth != mc.getWindow().getScaledWidth()) {
                    pastScaledWidth = mc.getWindow().getScaledWidth();
                    shouldUpdate = true;
                }
                if (shouldUpdate) {
                    texts = new ArrayList<>();
                    textX = new ArrayList<>();
                    textY = new ArrayList<>();
                    posY = 10;
                    int effectOffsetDown = 0;
                    String text = "Active mods:";
                    longestLength = mc.textRenderer.getWidth(text);

                    for (int i = -1; i < activeStates.size(); i++) {
                        if (i == -1) {
                            int newPositionX = mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(text) - 10;
                            int newPositionY = mc.textRenderer.getWrappedLinesHeight(text, mc.textRenderer.getWidth(text));
                            texts.add(text);
                            textX.add(newPositionX);
                            textY.add(posY + effectOffsetDown);
                            posY += newPositionY;
                            posY += newPositionY / 2;
                        } else {
                            int newPositionX = mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(activeStates.get(i)) - 10;
                            int newPositionY = mc.textRenderer.getWrappedLinesHeight(activeStates.get(i), mc.textRenderer.getWidth(activeStates.get(i)));
                            if (mc.textRenderer.getWidth(activeStates.get(i)) > longestLength)
                                longestLength = mc.textRenderer.getWidth(activeStates.get(i));
                            texts.add(activeStates.get(i));
                            textX.add(newPositionX);
                            textY.add(posY + effectOffsetDown);
                            posY += newPositionY;
                        }
                    }

                    shouldUpdate = false;
                }
                drawContext.fill(mc.getWindow().getScaledWidth() - longestLength - 15, 5, mc.getWindow().getScaledWidth() - 5, posY + 5, 0x80000000);
                for (int i = 0; i < texts.size(); i++) {
                    drawContext.drawText(mc.textRenderer, texts.get(i), textX.get(i), textY.get(i), rainbowInt, true);
                }
            }
        }
    }
    public static void update() {
        if(isDisabled(name)) return;
        shouldUpdate = true;
    }
}
