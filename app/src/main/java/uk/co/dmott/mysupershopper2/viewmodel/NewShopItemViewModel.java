package uk.co.dmott.mysupershopper2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.data.ShopItemRepository;

/**
 * Created by david on 22/03/18.
 */

public class NewShopItemViewModel extends ViewModel {

    private ShopItemRepository repository;

    NewShopItemViewModel(ShopItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Attach our LiveData to the Database
     */
    public void addNewItemToDatabase(ShopItem ShopItem){
        new AddItemTask().execute(ShopItem);
    }

    private class AddItemTask extends AsyncTask<ShopItem, Void, Void> {

        @Override
        protected Void doInBackground(ShopItem... item) {
            repository.createNewShopItem(item[0]);
            return null;
        }
    }    
    
    
    
    
}
