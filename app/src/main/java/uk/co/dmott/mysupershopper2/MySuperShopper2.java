package uk.co.dmott.mysupershopper2;

import android.app.Application;

import uk.co.dmott.mysupershopper2.dependencyinjection.ApplicationComponent;
import uk.co.dmott.mysupershopper2.dependencyinjection.ApplicationModule;
import uk.co.dmott.mysupershopper2.dependencyinjection.DaggerApplicationComponent;
import uk.co.dmott.mysupershopper2.dependencyinjection.RoomModule;

/**
 * Created by david on 22/03/18.
 */

public class MySuperShopper2 extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }





}
