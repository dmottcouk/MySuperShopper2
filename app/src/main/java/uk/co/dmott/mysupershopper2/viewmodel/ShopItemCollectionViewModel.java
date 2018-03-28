package uk.co.dmott.mysupershopper2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.data.ShopItemRepository;

/**
 * Created by david on 22/03/18.
 */

public class ShopItemCollectionViewModel extends ViewModel {

    private ShopItemRepository repository;

    ShopItemCollectionViewModel(ShopItemRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<ShopItem>> getShopItems() {
        return repository.getListOfData();
    }

    public void deleteShopItem(ShopItem ShopItem) {
        DeleteItemTask deleteItemTask = new DeleteItemTask();
        deleteItemTask.execute(ShopItem);
    }
    public void deleteShopItemsForNamedShop(String ShopName) {
        DeleteItemForNamedShopTask deleteItemForNamedShopTask = new DeleteItemForNamedShopTask();
        deleteItemForNamedShopTask.execute(ShopName);
    }


    public LiveData<List<ShopItem>> getShopItemsForNamedShop(String shopName) {
        //return repository.getListOfData();
        return repository.getListOfDataForShop(shopName);

    }

    private class DeleteItemTask extends AsyncTask<ShopItem, Void, Void> {

        @Override
        protected Void doInBackground(ShopItem... item) {
            repository.deleteShopItem(item[0]);
            return null;
        }
    }

    private class DeleteItemForNamedShopTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... item) {
            repository.deleteShopItemForShop(item[0]);
            return null;
        }
    }
    
}
