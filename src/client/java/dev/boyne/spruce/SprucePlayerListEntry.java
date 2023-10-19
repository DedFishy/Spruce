package dev.boyne.spruce.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

public class SprucePlayerListEntry extends PlayerListEntry {

    private java.util.function.Supplier<SkinTextures> texturesSupplier;
    public SprucePlayerListEntry(GameProfile profile, boolean secureChatEnforced) {
        super(profile, secureChatEnforced);
        SkinTextures defaultTextures = this.getSkinTextures();
        SkinTextures newTextures = new SkinTextures(
                defaultTextures.texture(),
                defaultTextures.textureUrl(),
                new Identifier("spruce", "capes/" + profile.getId().toString()),
                defaultTextures.elytraTexture(),
                defaultTextures.model(),
                defaultTextures.secure()
        );
    }
}
