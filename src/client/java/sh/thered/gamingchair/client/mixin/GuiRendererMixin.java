package sh.thered.gamingchair.client.mixin;

import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.RenderGuard;

@Mixin(value = GuiRenderer.class, remap = false)
public class GuiRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void resetGlyphCount(CallbackInfo ci) {
        if (Mod.isDisabled("gc.anticrash")) return;
        RenderGuard.glyphCount = 0;
    }
}