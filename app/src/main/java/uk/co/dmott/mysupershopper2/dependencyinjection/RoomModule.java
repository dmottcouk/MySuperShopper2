package uk.co.dmott.mysupershopper2.dependencyinjection;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.dmott.mysupershopper2.data.ShopItemDao;
import uk.co.dmott.mysupershopper2.data.ShopItemDatabase;
import uk.co.dmott.mysupershopper2.data.ShopItemRepository;
import uk.co.dmott.mysupershopper2.viewmodel.CustomViewModelFactory;

/**
 * Created by david on 22/03/18.
 */

@Module
public class RoomModule {

    private final ShopItemDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                ShopItemDatabase.class,
                "ShopItem.db"
        ).build();
    }

    @Provides
    @Singleton
    ShopItemRepository provideShopItemRepository(ShopItemDao shopItemDao){
        return new ShopItemRepository(shopItemDao);
    }

    @Provides
    @Singleton
    ShopItemDao provideShopItemDao(ShopItemDatabase database){
        return database.shopItemDao();
    }

    @Provides
    @Singleton
    ShopItemDatabase provideShopItemDatabase(Application application){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(ShopItemRepository repository){
        return new CustomViewModelFactory(repository);
    }






}
