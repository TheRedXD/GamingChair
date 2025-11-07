package sh.thered.gamingchair.client.mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.Utils;

import java.util.ArrayList;
import java.util.List;

public class Position extends Mod {
    static String name = "gc.position";

    public static List<String> texts;
    static MinecraftClient mc = MinecraftClient.getInstance();
    public static void cycle(DrawContext drawContext, float v) {
        if (isDisabled(name)) return;
        if (!mc.getDebugHud().shouldShowDebugHud() && !mc.options.hudHidden) {
            int posy = 10;

            if (Mod.isEnabled("gc.hud")) posy = 45;

            List<String> activeStates = Mod.getActiveStates();
            activeStates.sort(String::compareTo);

            assert mc.player != null;
            int le_x = mc.player.getBlockPos().getX();
            int le_y = mc.player.getBlockPos().getY();
            int le_z = mc.player.getBlockPos().getZ();
            float yaw = Utils.getYaw();
            String direction_nswe = Utils.getDirectionNSWE();
            String direction_XZ = Utils.getDirectionXZ();
            String text = "X: " + le_x + " Y: " + le_y + " Z: " + le_z + " | " + direction_nswe + " " + direction_XZ + " " + yaw;
            drawContext.drawText(mc.textRenderer, text, 10, posy, 0xffffffff, true);
        }
    }
}
