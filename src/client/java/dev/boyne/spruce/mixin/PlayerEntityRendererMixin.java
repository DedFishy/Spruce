package dev.boyne.spruce.mixin;

import dev.boyne.spruce.JSONFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }



    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
      System.out.println("Inserted into player entity renderer >:)");
      super.addFeature(new JSONFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(AbstractClientPlayerEntity entity) {
        return entity.getSkinTextures().texture();
    }
}
