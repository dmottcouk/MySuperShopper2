package uk.co.dmott.mysupershopper2.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by david on 22/03/18.
 */

public class ShopItemRepository {

    private final ShopItemDao ShopItemDao;

    @Inject
    public ShopItemRepository(ShopItemDao ShopItemDao){
        this.ShopItemDao = ShopItemDao;
    }


    public LiveData<List<ShopItem>> getListOfData(){
        return ShopItemDao.getShopItems();
    }


    public LiveData<List<ShopItem>> getListOfDataForShop(String ShopId){
        return ShopItemDao.getShopitemsForNamedShop(ShopId);
    }

    public LiveData<ShopItem> getShopItem(String itemId){
        return ShopItemDao.getShopItemById(itemId);
    }

    public Long createNewShopItem(ShopItem ShopItem){
        return ShopItemDao.insertShopItem(ShopItem);
    }

    public void deleteShopItem(ShopItem ShopItem){
        ShopItemDao.deleteShopItem(ShopItem);
    }    
    
    
    
    
    
    
}
