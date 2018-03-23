package uk.co.dmott.mysupershopper2.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by david on 22/03/18.
 */
@Dao
public interface ShopItemDao {

    @Query("SELECT * FROM ShopItem WHERE itemId = :itemId")
    LiveData<ShopItem> getShopItemById(String itemId);

    /**
     * Get all entities of type ShopItem
     * @return
     */
    @Query("SELECT * FROM ShopItem")
    LiveData<List<ShopItem>> getShopItems();
    /* query for items for a parrticular shop
*/

    @Query("SELECT * FROM ShopItem WHERE shopName = :shopId")
    LiveData<List<ShopItem>> getShops(String shopId);


    /**
     * Insert a new ShopItem
     * @param ShopItem
     */
    @Insert(onConflict = REPLACE)
    Long insertShopItem(ShopItem ShopItem);

    /**
     * Delete a given ShopItem
     * @param ShopItem
     */
    @Delete
    void deleteShopItem(ShopItem ShopItem);   
    

    
}
