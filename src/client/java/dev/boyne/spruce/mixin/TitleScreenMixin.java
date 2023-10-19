package dev.boyne.spruce.mixin;

import dev.boyne.spruce.TutorialScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TitleScreen.class: the class in which we are injecting
// extends: screen gives us addDrawableChild and setScreen
// Abstract: good practice for mixins
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    // method: the method in which we are injecting
    //at: where we are injecting
    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder((Text)Text.literal("Hello!"), button -> this.client.setScreen(
                new TutorialScreen()
        ))
                .dimensions(this.width / 2 - 100 + 205, y, 50, 20).build());

    }

    @Inject(at = @At("RETURN"), method = "render")
    private void renderNewText(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.drawTextWithShadow(this.textRenderer, "ReCape Client", 2, this.height - 20, 0xFFFFFF);

    }
}
