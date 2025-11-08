package sh.thered.gamingchair.client.mods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
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
    static MinecraftClient mc = MinecraftClient.getInstance();
    public static void cycle(DrawContext drawContext, float v) {
        if(isDisabled(name)) return;
        // MinecraftClient.getInstance().currentScreen very useful when it comes to detecting if a screen is shown (loading screen and chat, etc.)
        if (!mc.getDebugHud().shouldShowDebugHud() && !mc.options.hudHidden) {

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

            drawContext.fill(posx - padding, posy - padding, mc.textRenderer.getWidth(text3+text6) + 30 + posx + padding - 1, mc.textRenderer.getWrappedLinesHeight(text1, mc.textRenderer.getWidth(text3+text6)) * 3 + posy + padding - 2, 0x80000000);

            int rainbowInt = Utils.getRainbowInt();

            drawContext.drawText(mc.textRenderer, text1, posx, posy, rainbowInt, false);
            drawContext.drawText(mc.textRenderer, text2, posx, posy+mc.textRenderer.getWrappedLinesHeight(text2, mc.textRenderer.getWidth(text2)), rainbowInt, false);
            drawContext.drawText(mc.textRenderer, text3, posx, posy+mc.textRenderer.getWrappedLinesHeight(text3, mc.textRenderer.getWidth(text3))*2, rainbowInt, false);
            drawContext.drawText(mc.textRenderer, text4, posx+30, posy, rainbowInt, false);
            drawContext.drawText(mc.textRenderer, text5, posx+30, posy+mc.textRenderer.getWrappedLinesHeight(text2, mc.textRenderer.getWidth(text2)), rainbowInt, false);
            drawContext.drawText(mc.textRenderer, text6, posx+30, posy+mc.textRenderer.getWrappedLinesHeight(text3, mc.textRenderer.getWidth(text3))*2, rainbowInt, false);

        }
    }
}
