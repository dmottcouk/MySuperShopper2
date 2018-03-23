package uk.co.dmott.mysupershopper2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.data.ShopItemRepository;

/**
 * Created by david on 22/03/18.
 */

public class ShopItemViewModel extends ViewModel {
    private ShopItemRepository repository;

    ShopItemViewModel(ShopItemRepository repository) {
        this.repository = repository;
    }

    public LiveData<ShopItem> getShopItemById(String itemId){
        return repository.getShopItem(itemId);
    }    
    
    
    
}
