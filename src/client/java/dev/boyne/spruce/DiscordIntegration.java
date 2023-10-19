package dev.boyne.spruce;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;

public class DiscordIntegration extends Thread {
    Core core;
    Activity activity;
    public DiscordIntegration() {
        CreateParams params = new CreateParams();
        params.setClientID(1157433122468667494L);
        params.setFlags(CreateParams.getDefaultFlags());
        // Create the Core
        core = new Core(params);
    }

    public void setActivityState(String state) {
        Activity oldActivity = activity;

        oldActivity.setState(state);

        setActivity(oldActivity);
    }

    public Activity getActivity() {
        return activity;
    }

    public void run() {
        // Set parameters for the Core


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
        activity = newActivity;
        core.activityManager().updateActivity(newActivity);
    }
}
