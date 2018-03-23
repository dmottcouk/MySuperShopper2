package uk.co.dmott.mysupershopper2.dependencyinjection;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import uk.co.dmott.mysupershopper2.MySuperShopper2;

/**
 * Created by david on 22/03/18.
 */
@Module
public class ApplicationModule {


    private final MySuperShopper2 application;
    public ApplicationModule(MySuperShopper2 application) {
        this.application = application;
    }

    @Provides
    MySuperShopper2 provideMySuperShopper2Application(){
        return application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }





}
