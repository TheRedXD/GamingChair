package sh.thered.gamingchair.client.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.state.gui.TiledBlitRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.thered.gamingchair.client.Mod;

@Mixin(value = TiledBlitRenderState.class, remap = false)
public class TiledBlitRenderStateMixin {
    @Unique
    private int custom_vertexCount = 0;

    @Inject(method = "buildVertices", at = @At("HEAD"), remap = false)
    private void resetVertexCounter(VertexConsumer consumer, CallbackInfo ci) {
        if (Mod.isDisabled("gc.anticrash")) return;
        this.custom_vertexCount = 0;
    }

    @Inject(
            method = "buildVertices",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;addVertexWith2DPose(Lorg/joml/Matrix3x2fc;FF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            remap = false
    )
    private void limitTiledVertices(VertexConsumer consumer, CallbackInfo ci) {
        if (Mod.isDisabled("gc.anticrash")) return;
        if (this.custom_vertexCount >= 16384) {
            ci.cancel();
        }

        this.custom_vertexCount++;
    }
}