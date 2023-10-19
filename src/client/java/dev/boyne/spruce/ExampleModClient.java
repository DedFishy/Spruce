package dev.boyne.spruce;

import de.jcm.discordgamesdk.activity.Activity;
import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
	public static DiscordIntegration discord = new DiscordIntegration();
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		discord.start();

		Activity activity = new Activity();
		//activity.setDetails("");
		activity.setState("Loading into game");
		//activity.timestamps().setStart(Instant.now());
		//activity.party().size().setMaxSize(100);
		//activity.party().size().setCurrentSize(10);
		//activity.party().setID("Party!");
		//activity.secrets().setJoinSecret("Join!");

		activity.assets().setLargeImage("logo-plain");
		activity.assets().setLargeText("ReCape");

		// Finally, update the current activity to our activity
		discord.setActivity(activity);
	}


}