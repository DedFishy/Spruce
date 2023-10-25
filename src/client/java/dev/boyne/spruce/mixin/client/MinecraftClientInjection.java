package dev.boyne.spruce.mixin.client;

import dev.boyne.spruce.SpruceModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.WorldSavePath;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(MinecraftClient.class)
public class MinecraftClientInjection {
	@Inject(at = @At("HEAD"), method = "run")
	private void run(CallbackInfo info) {
		// This code is injected into the start of MinecraftClient.run()V

	}

	@Inject(at = @At("TAIL"), method = "setScreen", locals = LocalCapture.CAPTURE_FAILHARD)
	private void setScreen(@Nullable Screen screen, CallbackInfo info) {
		if (screen instanceof TitleScreen) {
			SpruceModClient.discord.setActivityState("On title screen");
		} else if (screen instanceof RealmsMainScreen) {
			SpruceModClient.discord.setActivityState("On Realms screen");
		} else if (screen instanceof MultiplayerScreen) {
			SpruceModClient.discord.setActivityState("On multiplayer screen");
		} else if (screen instanceof SelectWorldScreen) {
			SpruceModClient.discord.setActivityState("On singleplayer screen");
		} else if (screen instanceof LevelLoadingScreen) {
			SpruceModClient.discord.setActivityState("Loading into world");
		}
	}

	@Inject(at = @At("HEAD"), method = "joinWorld", locals = LocalCapture.CAPTURE_FAILHARD)
	private void joinWorld(ClientWorld world, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		String state = "?";
		if (client.isConnectedToLocalServer()) {
			state = "Playing singleplayer in world \"" + Objects.requireNonNull(client.getServer()).getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString() + "\"";
		} else {
			String ip;
			try {
				ip = client.getServer().getServerIp();
			} catch (NullPointerException e) {
				ip = "";
			}
			state = "Playing on server";
			if (!ip.equals("")) {
				state += " " + ip;
			}
		}

		SpruceModClient.discord.setActivityState(state);
	}
}