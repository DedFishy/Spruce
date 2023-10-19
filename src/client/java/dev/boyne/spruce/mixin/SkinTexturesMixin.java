package dev.boyne.spruce.mixin;

import dev.boyne.spruce.SprucePlayerListEntry;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.message.MessageVerifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public class SkinTexturesMixin {

    @Shadow
    Map<UUID, PlayerListEntry> playerListEntries;

    @Inject(method="getPlayerListEntry(Ljava/util/UUID;)Lnet/minecraft/client/network/PlayerListEntry;", at = @At("HEAD"), cancellable = true)
    public void getPlayerListEntry(UUID uuid, CallbackInfoReturnable<PlayerListEntry> cir) {
        PlayerListEntry playerListEntry = playerListEntries.get(uuid);
        PlayerListEntry newPlayerListEntry = new SprucePlayerListEntry(playerListEntry.getProfile(), playerListEntry.getMessageVerifier() != MessageVerifier.UNVERIFIED);
        cir.setReturnValue(newPlayerListEntry);
    }
}
