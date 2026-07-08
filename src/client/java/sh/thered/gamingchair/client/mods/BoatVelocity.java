package sh.thered.gamingchair.client.mods;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.Utils;
import sh.thered.gamingchair.client.debug.DebugInfo;
import sh.thered.gamingchair.client.debug.DebugInfoCollectionBuilder;
import sh.thered.gamingchair.client.mods.Debugger;

import java.util.Collection;

public class BoatVelocity extends Mod {
    static String name = "gc.boatvelocity";
    static String description = "Displays boat velocity by drawing a line towards the current movement direction of the boat.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    static Minecraft mc = Minecraft.getInstance();

    public static void cycle(PoseStack poseStack) {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("boatvelocity");
            return;
        }

        if(mc.player == null || mc.level == null || mc.player.getVehicle() == null || mc.player.getVehicle().getControllingPassenger() != mc.player) {
            Debugger.removeDebugInfoCollection("boatvelocity");
            return;
        }

        boolean USE_RAINBOW = true;
        int colorHex = USE_RAINBOW ? Utils.getRainbowInt() : 0xFF0000;
        float r = ((colorHex >> 16) & 0xFF) / 255.0f;
        float g = ((colorHex >> 8) & 0xFF) / 255.0f;
        float b = (colorHex & 0xFF) / 255.0f;
        float alpha = 1.0f;

        Vec3 velocity = mc.player.getVehicle().getDeltaMovement();
        double velocityX = velocity.x;
        double velocityY = velocity.y;
        double velocityZ = velocity.z;

        double velocityMagnitude = Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        if (velocityMagnitude > 0) {
            velocityX /= velocityMagnitude;
            velocityY /= velocityMagnitude;
            velocityZ /= velocityMagnitude;
        }

        if(Double.isNaN(velocityX)) velocityX = 0;
        if(Double.isNaN(velocityY)) velocityY = 0;
        if(Double.isNaN(velocityZ)) velocityZ = 0;

        Debugger.removeDebugInfoCollection("boatvelocity");

        Collection<DebugInfo> debugInfoCollection = new DebugInfoCollectionBuilder("boatvelocity")
                .add("velocityX", String.valueOf(velocityX), 0)
                .add("velocityY", String.valueOf(velocityY), 1)
                .add("velocityZ", String.valueOf(velocityZ), 2)
                .build();

        Debugger.appendDebugInfoCollection("boatvelocity", debugInfoCollection);

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.position();

        float tickDelta = mc.getDeltaTracker().getGameTimeDeltaTicks();

        double boatSmoothX = Mth.lerp(tickDelta, mc.player.getVehicle().xOld, mc.player.getVehicle().getX());
        double boatSmoothY = Mth.lerp(tickDelta, mc.player.getVehicle().yOld, mc.player.getVehicle().getY());
        double boatSmoothZ = Mth.lerp(tickDelta, mc.player.getVehicle().zOld, mc.player.getVehicle().getZ());

        double startX = boatSmoothX - cameraPos.x;
        double startY = (boatSmoothY + 0.5) - cameraPos.y;
        double startZ = boatSmoothZ - cameraPos.z;

        double endX = startX + (velocityX * 2.0);
        double endY = startY + (velocityY * 2.0);
        double endZ = startZ + (velocityZ * 2.0);

        poseStack.pushPose();
        Matrix4f positionMatrix = poseStack.last().pose();

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lines());

        buffer.addVertex(positionMatrix, (float) startX, (float) startY, (float) startZ)
                .setColor(r, g, b, alpha)
                .setNormal((float) velocityX, (float) velocityY, (float) velocityZ)
                .setLineWidth(3.0F);

        buffer.addVertex(positionMatrix, (float) endX, (float) endY, (float) endZ)
                .setColor(r, g, b, alpha)
                .setNormal((float) velocityX, (float) velocityY, (float) velocityZ)
                .setLineWidth(3.0F);

        bufferSource.endBatch(RenderTypes.lines());

        poseStack.popPose();
    }
}