package sh.thered.gamingchair.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class Utils {
    static MinecraftClient mc = MinecraftClient.getInstance();
    public static float getYaw() {
        assert mc.player != null;
        float yaw_bonk = ((mc.player.getYaw() + 180) % 360) - 180;
        float yaw = yaw_bonk;
        if (yaw_bonk < -180) {
            yaw = ((mc.player.getYaw() + 180) % 360) + 180;
        }
        return yaw;
    }
    public static String getDirectionXZ() {
        float yaw = getYaw();
        String direction_XZ = "??";
        if (yaw >= -45 && yaw <= 45) {
            direction_XZ = "+Z";
        } else if (yaw >= 45 && yaw <= 135) {
            direction_XZ = "-X";
        } else if ((yaw >= 135 && yaw <= 180) || (yaw >= -180 && yaw <= -135)) {
            direction_XZ = "-Z";
        } else if (yaw >= -135 && yaw <= -45) {
            direction_XZ = "+X";
        }
        return direction_XZ;
    }
    public static String getDirectionNSWE() {
        float yaw = getYaw();
        String direction_nswe = "unknown";
        if (yaw >= -45 && yaw <= 45) {
            direction_nswe = "south";
        } else if (yaw >= 45 && yaw <= 135) {
            direction_nswe = "west";
        } else if ((yaw >= 135 && yaw <= 180) || (yaw >= -180 && yaw <= -135)) {
            direction_nswe = "north";
        } else if (yaw >= -135 && yaw <= -45) {
            direction_nswe = "east";
        }
        return direction_nswe;
    }
    public static int getRainbowInt() {
        float x = System.currentTimeMillis() % 2000 / 1000F;
        float red = 0.5F + 0.5F * MathHelper.sin(x * (float) Math.PI);
        float green = 0.5F + 0.5F * MathHelper.sin((x + 4F / 3F) * (float) Math.PI);
        float blue = 0.5F + 0.5F * MathHelper.sin((x + 8F / 3F) * (float) Math.PI);
        return 0x04 << 16 |
                0xFF << 24 |
                (int) Math.min(red*255+50, 255) << 16 |
                (int) Math.min(green*255+50, 255) << 8 |
                (int) Math.min(blue*255+50, 255);
    }
}
