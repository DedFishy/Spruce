package dev.boyne.spruce;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;

import java.io.FileNotFoundException;

public class DiscordIntegration extends Thread {
    Core core;
    Activity activity;

    boolean discordActive = false;
    public DiscordIntegration() {
        try {
            CreateParams params = new CreateParams();
            params.setClientID(1163500538458034206L);
            params.setFlags(CreateParams.getDefaultFlags());
            // Create the Core
            core = new Core(params);
            discordActive = true;
        } catch (RuntimeException e) {
            discordActive = false;
            System.out.println("WARNING: Discord could not be initialized. Is it running?");
        }
    }

    public void setActivityState(String state) {
        if (!discordActive) return;
        Activity oldActivity = activity;

        oldActivity.setState(state);

        setActivity(oldActivity);
    }

    public Activity getActivity() {
        return activity;
    }

    public void run() {
        // Set parameters for the Core
        if (!discordActive) return;


        while(true)
        {
            try
            {
                // Sleep a bit to save CPU
                Thread.sleep(16);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setActivity(Activity newActivity) {
        if (!discordActive) return;

        activity = newActivity;
        core.activityManager().updateActivity(newActivity);
    }
}
