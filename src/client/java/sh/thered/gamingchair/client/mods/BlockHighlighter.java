package sh.thered.gamingchair.client.mods;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.ModConfig;
import sh.thered.gamingchair.client.Utils;
import sh.thered.gamingchair.client.debug.DebugInfo;
import sh.thered.gamingchair.client.debug.DebugInfoCollectionBuilder;
import sh.thered.gamingchair.client.mods.Debugger;

import java.util.Collection;
import java.util.List;

public class BlockHighlighter extends Mod {
    static String name = "gc.blockhighlighter";
    static String description = "Highlights blocks you're looking at more clearly.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public void init() {
        ModConfig.setOption(getName(), "rainbow", true);
        ModConfig.setOption(getName(), "exact_hitbox", true);
    }

    static Minecraft mc = Minecraft.getInstance();

    public static boolean cycle(PoseStack poseStack) {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("blockhighlighter");
            return true;
        }

        HitResult hitResult = mc.hitResult;
        Camera camera = mc.gameRenderer.getMainCamera();

        if (hitResult == null) {
            Debugger.removeDebugInfoCollection("blockhighlighter");
            return false;
        }

        float yaw = camera.yRot() + 180.0F;
        float pitch = camera.xRot();
        float yaw_fixed = ((yaw % 360) + 360) % 360;

        Debugger.removeDebugInfoCollection("blockhighlighter");

        switch(hitResult.getType()) {
            case MISS:
                Debugger.removeDebugInfoCollection("blockhighlighter");
                break;
            case ENTITY:
                Collection<DebugInfo> debugInfoCollection = new DebugInfoCollectionBuilder("blockhighlighter")
                        .add("x", "-", 0)
                        .add("y", "-", 1)
                        .add("z", "-", 2)
                        .add("yaw", String.valueOf(yaw), 3)
                        .add("pitch", String.valueOf(pitch), 4)
                        .add("yaw_fixed", String.valueOf(yaw_fixed), 5)
                        .add("ray_x", String.valueOf(hitResult.getLocation().x), 6)
                        .add("ray_y", String.valueOf(hitResult.getLocation().y), 7)
                        .add("ray_z", String.valueOf(hitResult.getLocation().z), 8)
                        .add("targetPosition", "-", 9)
                        .build();
                Debugger.appendDebugInfoCollection("blockhighlighter", debugInfoCollection);
                break;
            case BLOCK:
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                float x = blockPos.getX();
                float y = blockPos.getY();
                float z = blockPos.getZ();

                Vec3 targetPosition = new Vec3(x, y, z);
                Vec3 camPos = camera.position();

                Debugger.removeDebugInfoCollection("blockhighlighter");

                Collection<DebugInfo> debugInfoCollection1 = new DebugInfoCollectionBuilder("blockhighlighter")
                        .add("x", String.valueOf(x), 0)
                        .add("y", String.valueOf(y), 1)
                        .add("z", String.valueOf(z), 2)
                        .add("yaw", String.valueOf(yaw), 3)
                        .add("pitch", String.valueOf(pitch), 4)
                        .add("yaw_fixed", String.valueOf(yaw_fixed), 5)
                        .add("ray_x", String.valueOf(hitResult.getLocation().x), 6)
                        .add("ray_y", String.valueOf(hitResult.getLocation().y), 7)
                        .add("ray_z", String.valueOf(hitResult.getLocation().z), 8)
                        .add("targetPosition", String.valueOf(targetPosition), 9)
                        .build();

                Debugger.appendDebugInfoCollection("blockhighlighter", debugInfoCollection1);

                poseStack.pushPose();
                poseStack.translate(targetPosition.x - camPos.x, targetPosition.y - camPos.y, targetPosition.z - camPos.z);
                Matrix4f pose = poseStack.last().pose();

                MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

                boolean USE_RAINBOW = (boolean) ModConfig.getOptionAssertive(name, "rainbow");
                boolean EXACT_HITBOX = (boolean) ModConfig.getOptionAssertive(name, "exact_hitbox");

                int colorHex = USE_RAINBOW ? Utils.getRainbowInt() : 0x00FFFF;
                float r = ((colorHex >> 16) & 0xFF) / 255.0f;
                float g = ((colorHex >> 8) & 0xFF) / 255.0f;
                float b = (colorHex & 0xFF) / 255.0f;
                float lineAlpha = 0.8f;
                float fillAlpha = 0.2f;

                List<AABB> boxes;
                if (EXACT_HITBOX && mc.level != null) {
                    BlockState state = mc.level.getBlockState(blockPos);
                    VoxelShape shape = state.getShape(mc.level, blockPos, CollisionContext.of(mc.player));
                    boxes = shape.isEmpty() ? List.of(new AABB(0, 0, 0, 1, 1, 1)) : shape.toAabbs();
                } else {
                    boxes = List.of(new AABB(0, 0, 0, 1, 1, 1));
                }

                float inflate = EXACT_HITBOX ? 0.003f : 0.01f;

                // Faces
                VertexConsumer fillBuffer = bufferSource.getBuffer(RenderTypes.debugFilledBox());
                for (AABB box : boxes) {
                    float minX = (float) box.minX - inflate;
                    float minY = (float) box.minY - inflate;
                    float minZ = (float) box.minZ - inflate;
                    float maxX = (float) box.maxX + inflate;
                    float maxY = (float) box.maxY + inflate;
                    float maxZ = (float) box.maxZ + inflate;
                    drawBoxQuads(fillBuffer, pose, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, fillAlpha);
                }
                bufferSource.endBatch(RenderTypes.debugFilledBox());

                // Edges
                VertexConsumer lineBuffer = bufferSource.getBuffer(RenderTypes.lines());
                for (AABB box : boxes) {
                    float minX = (float) box.minX - inflate;
                    float minY = (float) box.minY - inflate;
                    float minZ = (float) box.minZ - inflate;
                    float maxX = (float) box.maxX + inflate;
                    float maxY = (float) box.maxY + inflate;
                    float maxZ = (float) box.maxZ + inflate;
                    drawBoxLines(lineBuffer, pose, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, lineAlpha);
                }
                bufferSource.endBatch(RenderTypes.lines());

                poseStack.popPose();
                break;
        }
        return false;
    }

    private static void drawBoxQuads(VertexConsumer buffer, Matrix4f pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a) {
        // Bottom
        addQuad(buffer, pose, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        // Top
        addQuad(buffer, pose, minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, r, g, b, a);
        // North
        addQuad(buffer, pose, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, maxX, minY, minZ, r, g, b, a);
        // South
        addQuad(buffer, pose, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        // West
        addQuad(buffer, pose, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);
        // East
        addQuad(buffer, pose, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, maxX, minY, maxZ, r, g, b, a);
    }

    private static void drawBoxLines(VertexConsumer buffer, Matrix4f pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a) {
        // Bottom square
        addLine(buffer, pose, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        addLine(buffer, pose, minX, minY, maxZ, maxX, minY, maxZ, r, g, b, a);
        addLine(buffer, pose, minX, minY, minZ, minX, minY, maxZ, r, g, b, a);
        addLine(buffer, pose, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        // Top square
        addLine(buffer, pose, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, pose, minX, maxY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        addLine(buffer, pose, minX, maxY, minZ, minX, maxY, maxZ, r, g, b, a);
        addLine(buffer, pose, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        // Vertical pillars
        addLine(buffer, pose, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        addLine(buffer, pose, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        addLine(buffer, pose, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
        addLine(buffer, pose, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
    }

    private static void addLine(VertexConsumer buffer, Matrix4f pose, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        float nx = x2 - x1;
        float ny = y2 - y1;
        float nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= len; ny /= len; nz /= len;

        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a).setNormal(nx, ny, nz).setLineWidth(2.0F);
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a).setNormal(nx, ny, nz).setLineWidth(2.0F);
    }

    private static void addQuad(VertexConsumer buffer, Matrix4f pose, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float r, float g, float b, float a) {
        buffer.addVertex(pose, x1, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(pose, x2, y2, z2).setColor(r, g, b, a);
        buffer.addVertex(pose, x3, y3, z3).setColor(r, g, b, a);
        buffer.addVertex(pose, x4, y4, z4).setColor(r, g, b, a);
    }
}