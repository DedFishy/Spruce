package dev.boyne.spruce;

import dev.boyne.spruce.mixin.SprucePlayerListEntry;
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

    @Inject(method="getPlayerListEntry(Ljava/util/UUID;)Lnet/minecraft/client/network/PlayerListEntry;", at = @At("HEAD"))
    public void getPlayerListEntry(UUID uuid, CallbackInfoReturnable<PlayerListEntry> cir) {
        System.out.println("SET OUTPUT OF getPlayerListEntry TO CUSTOM ENTRY");
        PlayerListEntry playerListEntry = playerListEntries.get(uuid);
        PlayerListEntry newPlayerListEntry = new SprucePlayerListEntry(playerListEntry.getProfile(), playerListEntry.getMessageVerifier() != MessageVerifier.UNVERIFIED);
        cir.setReturnValue(newPlayerListEntry);
    }
    
    @Inject(method="getPlayerListEntry(Ljava/lang/String;)Lnet/minecraft/client/network/PlayerListEntry;", at = @At("HEAD"))
    public void getPlayerListEntryName(String profileName, CallbackInfoReturnable<PlayerListEntry> cir) {
        System.out.println("SET OUTPUT OF getPlayerListEntry TO CUSTOM ENTRY");
        PlayerListEntry playerListEntry = null;
        for (PlayerListEntry playerListEntryCandidate : this.playerListEntries.values()) {
            if (!playerListEntryCandidate.getProfile().getName().equals(profileName)) continue;
            playerListEntry = playerListEntryCandidate;
        }
        if (playerListEntry == null) {
            return;
        }
        PlayerListEntry newPlayerListEntry = new SprucePlayerListEntry(playerListEntry.getProfile(), playerListEntry.getMessageVerifier() != MessageVerifier.UNVERIFIED);
        cir.setReturnValue(newPlayerListEntry);
    }

    /*
    @Inject(method = "getSkinTextures", at = @At("HEAD"))
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        UUID uuid = ((AbstractClientPlayerEntity)(Object)this).getUuid();
        PlayerListEntry listEntry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(uuid);
        SkinTextures textures = listEntry.getSkinTextures();
        listEntry.getProfile().getId();
    }
    */
}
