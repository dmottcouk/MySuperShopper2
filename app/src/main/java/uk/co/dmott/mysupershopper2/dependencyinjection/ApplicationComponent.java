package uk.co.dmott.mysupershopper2.dependencyinjection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.dmott.mysupershopper2.create.CreateFragment;
import uk.co.dmott.mysupershopper2.detail.DetailFragment;
import uk.co.dmott.mysupershopper2.list.ListFragment;
import uk.co.dmott.mysupershopper2.neworsaveshop.CreateSaveShopFragment;
import uk.co.dmott.mysupershopper2.newshop.NewShopFragment;

/**
 * Created by david on 22/03/18.
 */


@Singleton
@Component(modules = {ApplicationModule.class, RoomModule.class})
public interface ApplicationComponent {

    void inject(ListFragment listFragment);
    void inject(CreateFragment createFragment);
    void inject(DetailFragment detailFragment);
    void inject(CreateSaveShopFragment createsaveShopFragment);
    void inject(NewShopFragment NewShopFragment);

    Application application();




}
