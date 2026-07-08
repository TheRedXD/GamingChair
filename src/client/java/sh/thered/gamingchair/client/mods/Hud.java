package sh.thered.gamingchair.client.mods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.FormattedText;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.Utils;

import java.util.ArrayList;
import java.util.List;

public class Hud extends Mod {
    static String name = "gc.hud";
    static String description = "Shows a nice HUD element displaying the version of Gaming Chair.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    public List<String> texts = new ArrayList<String>();
    static Minecraft mc = Minecraft.getInstance();
    public static void cycle(GuiGraphicsExtractor drawContext, float v) {
        if(isDisabled(name)) return;
        // MinecraftClient.getInstance().currentScreen very useful when it comes to detecting if a screen is shown (loading screen and chat, etc.)
        if (!mc.getDebugOverlay().showDebugScreen() && !mc.options.hideGui) {

            String gamingChairVersion = String.valueOf(FabricLoader.getInstance().getModContainer("gamingchair").orElseThrow().getMetadata().getVersion());

            String text1 = " |)";
            String text2 = " |__";
            String text3 = " /\\";
            String text4 = "Gaming Chair";
            String text5 = "Version "+gamingChairVersion;
            String text6 = "Made by _TheRedex";

            int padding = 3;
            int posx = 10;
            int posy = 10;

            drawContext.fill(posx - padding, posy - padding, mc.font.width(text3+text6) + 30 + posx + padding - 1, mc.font.wordWrapHeight(FormattedText.of(text1), mc.font.width(text3+text6)) * 3 + posy + padding - 2, 0x80000000);

            int rainbowInt = Utils.getRainbowInt();

            drawContext.text(mc.font, text1, posx, posy, rainbowInt, false);
            drawContext.text(mc.font, text2, posx, posy+mc.font.wordWrapHeight(FormattedText.of(text2), mc.font.width(text2)), rainbowInt, false);
            drawContext.text(mc.font, text3, posx, posy+mc.font.wordWrapHeight(FormattedText.of(text3), mc.font.width(text3))*2, rainbowInt, false);
            drawContext.text(mc.font, text4, posx+30, posy, rainbowInt, false);
            drawContext.text(mc.font, text5, posx+30, posy+mc.font.wordWrapHeight(FormattedText.of(text2), mc.font.width(text2)), rainbowInt, false);
            drawContext.text(mc.font, text6, posx+30, posy+mc.font.wordWrapHeight(FormattedText.of(text3), mc.font.width(text3))*2, rainbowInt, false);

        }
    }
}
