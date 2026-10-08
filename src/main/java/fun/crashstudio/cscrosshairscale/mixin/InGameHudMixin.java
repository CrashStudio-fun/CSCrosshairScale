package fun.crashstudio.cscrosshairscale.mixin;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private boolean cscrosshairscale$scaled = false;

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;createNewRootLayer()V",
                    shift = At.Shift.AFTER
            )
    )
    private void cscrosshairscale$beforeCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.cscrosshairscale$scaled) {
            context.getMatrices().popMatrix();
            this.cscrosshairscale$scaled = false;
        }

        float scale = ModConfig.get().scale;
        if (Math.abs(scale - 1.0f) > 0.001f) {
            float cx = context.getScaledWindowWidth() / 2.0f;
            float cy = context.getScaledWindowHeight() / 2.0f;
            Matrix3x2fStack matrices = context.getMatrices();
            matrices.pushMatrix();
            matrices.translate(cx, cy);
            matrices.scale(scale, scale);
            matrices.translate(-cx, -cy);
            this.cscrosshairscale$scaled = true;
        }
    }

    @Inject(
            method = "renderCrosshair",
            at = @At("RETURN")
    )
    private void cscrosshairscale$afterRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.cscrosshairscale$scaled) {
            context.getMatrices().popMatrix();
            this.cscrosshairscale$scaled = false;
        }
    }
}
