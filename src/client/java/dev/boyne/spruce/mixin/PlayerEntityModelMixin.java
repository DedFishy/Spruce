package dev.boyne.spruce.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin {



    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(ModelPart root, boolean thinArms, CallbackInfo ci) {

    }

}
