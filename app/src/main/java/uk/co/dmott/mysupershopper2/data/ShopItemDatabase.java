package uk.co.dmott.mysupershopper2.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by david on 22/03/18.
 */

@Database(entities = {ShopItem.class}, version = 1)
public abstract class ShopItemDatabase extends RoomDatabase {

    public abstract ShopItemDao shopItemDao();

}
