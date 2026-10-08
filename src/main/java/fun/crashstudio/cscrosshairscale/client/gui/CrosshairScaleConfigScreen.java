package fun.crashstudio.cscrosshairscale.client.gui;

import fun.crashstudio.cscrosshairscale.config.ModConfig;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;

public class CrosshairScaleConfigScreen extends Screen {
    private static final Identifier CROSSHAIR_TEXTURE = Identifier.ofVanilla("hud/crosshair");

    private final Screen parent;
    private CrosshairScaleSliderWidget slider;

    public CrosshairScaleConfigScreen(Screen parent) {
        super(Text.translatable("cscrosshairscale.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int previewBoxHeight = 76;
        int previewBoxY = 32;
        int startY = previewBoxY + previewBoxHeight + 14;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonX = this.width / 2 - buttonWidth / 2;

        this.slider = new CrosshairScaleSliderWidget(buttonX, startY, buttonWidth, buttonHeight);
        this.addDrawableChild(this.slider);

        ButtonWidget resetButton = ButtonWidget.builder(
                Text.translatable("cscrosshairscale.config.reset"),
                button -> {
                    ModConfig.get().reset();
                    this.slider.syncFromConfig();
                }
        ).dimensions(buttonX, startY + 24, buttonWidth, buttonHeight).build();
        this.addDrawableChild(resetButton);

        int doneY = Math.max(startY + 50, this.height - 28);
        ButtonWidget doneButton = ButtonWidget.builder(
                ScreenTexts.DONE,
                button -> this.close()
        ).dimensions(buttonX, doneY, buttonWidth, buttonHeight).build();
        this.addDrawableChild(doneButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFFFFFF);

        int previewBoxWidth = 90;
        int previewBoxHeight = 76;
        int previewBoxX = this.width / 2 - previewBoxWidth / 2;
        int previewBoxY = 32;

        context.fill(previewBoxX, previewBoxY, previewBoxX + previewBoxWidth, previewBoxY + previewBoxHeight, 0x80000000);
        context.drawStrokedRectangle(previewBoxX, previewBoxY, previewBoxWidth, previewBoxHeight, 0xFF555555);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("cscrosshairscale.config.preview"), this.width / 2, previewBoxY - 11, 0xAAAAAA);

        int previewCenterX = this.width / 2;
        int previewCenterY = previewBoxY + previewBoxHeight / 2;

        float scale = ModConfig.get().scale;
        Matrix3x2fStack matrices = context.getMatrices();

        context.enableScissor(previewBoxX + 1, previewBoxY + 1, previewBoxX + previewBoxWidth - 1, previewBoxY + previewBoxHeight - 1);
        matrices.pushMatrix();
        matrices.translate(previewCenterX, previewCenterY);
        matrices.scale(scale, scale);
        matrices.translate(-previewCenterX, -previewCenterY);

        context.drawGuiTexture(RenderPipelines.CROSSHAIR, CROSSHAIR_TEXTURE, previewCenterX - 7, previewCenterY - 7, 15, 15);
        matrices.popMatrix();
        context.disableScissor();
    }

    @Override
    public void close() {
        ModConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
