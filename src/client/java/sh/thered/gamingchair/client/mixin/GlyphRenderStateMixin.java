package sh.thered.gamingchair.client.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.RenderGuard;

@Mixin(value = GlyphRenderState.class, remap = false)
public class GlyphRenderStateMixin {

    @Inject(method = "buildVertices", at = @At("HEAD"), cancellable = true)
    private void limitGlyphVertices(VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (Mod.isDisabled("gc.anticrash")) return;
        if (RenderGuard.glyphCount > 50000) {
            ci.cancel();
            return;
        }
        RenderGuard.glyphCount++;
    }
}