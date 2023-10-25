package dev.boyne.spruce;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class SprucePlayerListEntry extends PlayerListEntry {

    private final GameProfile profile;

    SkinTextures newTextures = null;

    private boolean startedLoading = false;
    private boolean finishedLoading = false;
    private NativeImage DownloadFile(String url) throws IOException {
        InputStream fileStream = URI.create(url).toURL().openStream();
        NativeImage image = NativeImage.read(fileStream);

        return image;
    }

    private void LoadCape(String displayName, String uuid) {
        try {
            NativeImage image = DownloadFile("http://" + SpruceMod.address + "/capes/" + displayName + ".png");
            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            Identifier textureId = new Identifier("spruce", "capes/" + uuid);
            textureManager.registerTexture(textureId, texture);
            finishedLoading = true;

        } catch (IOException ignored) {}
    }

    public SprucePlayerListEntry(GameProfile profile, boolean secureChatEnforced) {
        super(profile, secureChatEnforced);

        this.profile = profile;

        SkinTextures defaultTextures = this.getSkinTextures();

        Identifier capeTexture = defaultTextures.capeTexture();
        if (capeTexture == null && !startedLoading) {
            startedLoading = true;
            LoadCape(profile.getName(), profile.getId().toString());
        }

    }


    public SkinTextures getSkinTextures() {
        SkinTextures defaultTextures = super.getSkinTextures();
        if (!finishedLoading) return defaultTextures;
        else {
            // Load defaults if there are no new textures or if we have the wrong skin
            if (newTextures == null || defaultTextures.texture() != newTextures.texture()) {
                newTextures = new SkinTextures(
                        defaultTextures.texture(),
                        defaultTextures.textureUrl(),
                        new Identifier("spruce", "capes/" + profile.getId().toString()),
                        new Identifier("spruce", "capes/" + profile.getId().toString()),
                        defaultTextures.model(),
                        defaultTextures.secure()
                );
            }
            return newTextures;
        }
        /*
        if (this.texturesSupplier == null) {
            System.out.println("Returning default texture supplier");

            return super.getSkinTextures();
        } else {
            System.out.println("Returning drop-in texture supplier");

            return this.texturesSupplier.get();
        }

         */
    }
}
