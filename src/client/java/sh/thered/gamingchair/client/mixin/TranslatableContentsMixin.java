package sh.thered.gamingchair.client.mixin;

import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sh.thered.gamingchair.client.Mod;

import java.util.Optional;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {

    @Unique
    private static final ThreadLocal<Integer> custom_translationVisits = ThreadLocal.withInitial(() -> 0);

    @Unique
    private static final ThreadLocal<Long> custom_lastResetTime = ThreadLocal.withInitial(() -> 0L);

    @Inject(method = "visit*", at = @At("HEAD"), cancellable = true)
    private void limitExploitVisits(CallbackInfoReturnable<Optional<?>> cir) {
        if (Mod.isDisabled("gc.anticrash")) return;
        long now = System.currentTimeMillis();

        if (now - custom_lastResetTime.get() > 50) {
            custom_lastResetTime.set(now);
            custom_translationVisits.set(0);
        }

        int visits = custom_translationVisits.get() + 1;
        custom_translationVisits.set(visits);

        if (visits > 5000) {
            cir.setReturnValue(Optional.empty());
        }
    }
}