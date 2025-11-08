package sh.thered.gamingchair.client.mods;

import com.mojang.blaze3d.systems.RenderSystem;
//import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import com.mojang.blaze3d.vertex.VertexFormat;
//? if 1.21.10 {
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.render.state.OutlineRenderState;
//?}
import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.render.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.debug.DebugInfo;
import sh.thered.gamingchair.client.debug.DebugInfoCollectionBuilder;

import java.util.Collection;
import java.util.Objects;

public class BlockHighlighter extends Mod {
//    This class is specifically meant for highlighting blocks that you are looking at.
    static String name = "gc.blockhighlighter";
    static String description = "Highlights blocks you're looking at more clearly.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    static MinecraftClient mc = MinecraftClient.getInstance();

    //? if 1.21.10 {
    public static boolean cycle(WorldRenderContext context, OutlineRenderState outlineRenderState) {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("blockhighlighter");
            return true;
        }

        // NEW SYSTEM: use outlineRenderState
        VoxelShape voxelShape = outlineRenderState.collisionShape();

//        HitResult hitResult = mc.crosshairTarget;
//        Camera camera = context.gameRenderer().getCamera();
//        float yaw = camera.getYaw() + 180.0F;
//        float pitch = camera.getPitch();
//        float yaw_fixed = ((yaw % 360) + 360) % 360;
//        Quaternionf quaternion_yaw = new Quaternionf(0f, 1f, 0f, yaw_fixed);
//        Quaternionf quaternion_pitch = new Quaternionf(1f, 0f, 0f, pitch);
//
//        Debugger.removeDebugInfoCollection("blockhighlighter");
//
//        switch(Objects.requireNonNull(hitResult).getType()) {
//            case MISS:
//                Debugger.removeDebugInfoCollection("blockhighlighter");
//                break;
//            case ENTITY:
//                Collection<DebugInfo> debugInfoCollection = new DebugInfoCollectionBuilder("blockhighlighter")
//                    .add("x", "-", 0)
//                    .add("y", "-", 1)
//                    .add("z", "-", 2)
//                    .add("yaw", String.valueOf(yaw), 3)
//                    .add("pitch", String.valueOf(pitch), 4)
//                    .add("yaw_fixed", String.valueOf(yaw_fixed), 5)
//                    .add("ray_x", String.valueOf(hitResult.getPos().getX()), 6)
//                    .add("ray_y", String.valueOf(hitResult.getPos().getY()), 7)
//                    .add("ray_z", String.valueOf(hitResult.getPos().getZ()), 8)
//                    .add("targetPosition", "-", 9)
//                    .add("quaternion_pitch", "-", 10)
//                    .build();
//                Debugger.appendDebugInfoCollection("blockhighlighter", debugInfoCollection);
//                break;
//            case BLOCK:
//                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
//                float x = blockHitResult.getBlockPos().getX();
//                float y = blockHitResult.getBlockPos().getY();
//                float z = blockHitResult.getBlockPos().getZ();
//
//                Vec3d targetPosition = new Vec3d(x, y, z);
//                Vec3d transformedPosition = targetPosition.subtract(camera.getPos());
//
//                Debugger.removeDebugInfoCollection("blockhighlighter");
//
//                Collection<DebugInfo> debugInfoCollection1 = new DebugInfoCollectionBuilder("blockhighlighter")
//                    .add("x", String.valueOf(x), 0)
//                    .add("y", String.valueOf(y), 1)
//                    .add("z", String.valueOf(z), 2)
//                    .add("yaw", String.valueOf(yaw), 3)
//                    .add("pitch", String.valueOf(pitch), 4)
//                    .add("yaw_fixed", String.valueOf(yaw_fixed), 5)
//                    .add("ray_x", String.valueOf(hitResult.getPos().getX()), 6)
//                    .add("ray_y", String.valueOf(hitResult.getPos().getY()), 7)
//                    .add("ray_z", String.valueOf(hitResult.getPos().getZ()), 8)
//                    .add("targetPosition", String.valueOf(targetPosition), 9)
//                    .add("quaternion_pitch", String.valueOf(quaternion_pitch), 10)
//                    .build();
//
//                Debugger.appendDebugInfoCollection("blockhighlighter", debugInfoCollection1);
//
//                MatrixStack matrixStack = new MatrixStack();
//
//                matrixStack.multiply(quaternion_pitch);
//                matrixStack.multiply(quaternion_yaw);
//                matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
//
//                Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
//                Tessellator tessellator = Tessellator.getInstance();

//                RenderSystem.disableCull();
//                Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//
//                float red = 0f;
//                float green = 1f;
//                float blue = 1f;
//                float alpha = 0.5f;
//
////                one side
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
////                other side
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
////                left
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
////                right
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
////                top
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, -0.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, 1.01F, -0.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
////                bottom
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(0f, 0f).next();
//                buffer.vertex(positionMatrix, -0.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(0f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, 1.01F).color(red, green, blue, alpha).texture(1f, 1f).next();
//                buffer.vertex(positionMatrix, 1.01F, -0.01F, -0.01F).color(red, green, blue, alpha).texture(1f, 0f).next();
//
//                tessellator.draw();
//                RenderSystem.enableCull();
//                break;
//        }
        return false;
    }
    //?} else {
    public static boolean cycle() {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("blockhighlighter");
            return true;
        }
        return false;
    }
    //?}
}
