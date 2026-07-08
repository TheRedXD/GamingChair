package sh.thered.gamingchair.client.mods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
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

    static Minecraft mc = Minecraft.getInstance();
    public static void cycle(GuiGraphicsExtractor drawContext, float v) {
        if(isDisabled(name)) return;
        if (!mc.getDebugOverlay().showDebugScreen() && !mc.options.hideGui) {
            int rainbowInt = Utils.getRainbowInt();
            List<String> activeStates = Mod.getActiveStates();
            activeStates.sort(String::compareTo);
            if(!activeStates.isEmpty()) {
                if (pastScaledWidth != mc.getWindow().getGuiScaledWidth()) {
                    pastScaledWidth = mc.getWindow().getGuiScaledWidth();
                    shouldUpdate = true;
                }
                if (shouldUpdate) {
                    texts = new ArrayList<>();
                    textX = new ArrayList<>();
                    textY = new ArrayList<>();
                    posY = 10;
                    int effectOffsetDown = 0;
                    String text = "Active mods:";
                    longestLength = mc.font.width(text);

                    for (int i = -1; i < activeStates.size(); i++) {
                        if (i == -1) {
                            int newPositionX = mc.getWindow().getGuiScaledWidth() - mc.font.width(text) - 10;
                            int newPositionY = mc.font.wordWrapHeight(FormattedText.of(text), mc.font.width(text));
                            texts.add(text);
                            textX.add(newPositionX);
                            textY.add(posY + effectOffsetDown);
                            posY += newPositionY;
                            posY += newPositionY / 2;
                        } else {
                            int newPositionX = mc.getWindow().getGuiScaledWidth() - mc.font.width(activeStates.get(i)) - 10;
                            int newPositionY = mc.font.wordWrapHeight(FormattedText.of(activeStates.get(i)), mc.font.width(activeStates.get(i)));
                            if (mc.font.width(activeStates.get(i)) > longestLength)
                                longestLength = mc.font.width(activeStates.get(i));
                            texts.add(activeStates.get(i));
                            textX.add(newPositionX);
                            textY.add(posY + effectOffsetDown);
                            posY += newPositionY;
                        }
                    }

                    shouldUpdate = false;
                }
                drawContext.fill(mc.getWindow().getGuiScaledWidth() - longestLength - 15, 5, mc.getWindow().getGuiScaledWidth() - 5, posY + 5, 0x80000000);
                for (int i = 0; i < texts.size(); i++) {
                    drawContext.text(mc.font, texts.get(i), textX.get(i), textY.get(i), rainbowInt, true);
                }
            }
        }
    }
    public static void update() {
        if(isDisabled(name)) return;
        shouldUpdate = true;
    }
}
