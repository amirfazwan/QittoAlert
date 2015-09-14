package helper;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by amirfazwan on 10/9/15.
 */
public class ParseHelper extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "LEKvVz6itMB5FayjYaujnIfBQBQmkUcWaLRFCUbq", "aTuxfLhszpQfngRToPpl0mPJjr1BRjBzYKt1bTpl");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
