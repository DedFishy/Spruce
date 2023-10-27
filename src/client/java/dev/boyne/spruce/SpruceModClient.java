package dev.boyne.spruce;

import de.jcm.discordgamesdk.activity.Activity;
import net.fabricmc.api.ClientModInitializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpruceModClient implements ClientModInitializer {
	public static DiscordIntegration discord;
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		discord = new DiscordIntegration();
		try {
			Files.createDirectories(Paths.get("capes"));
		} catch (IOException ignored) {}
		discord.start();

		Activity activity = new Activity();
		//activity.setDetails("");
		activity.setState("Loading into game");
		//activity.timestamps().setStart(Instant.now());
		//activity.party().size().setMaxSize(100);
		//activity.party().size().setCurrentSize(10);
		//activity.party().setID("Party!");
		//activity.secrets().setJoinSecret("Join!");

		activity.assets().setLargeImage("modern");
		activity.assets().setLargeText("Spruce");

		// Finally, update the current activity to our activity
		discord.setActivity(activity);
	}


}