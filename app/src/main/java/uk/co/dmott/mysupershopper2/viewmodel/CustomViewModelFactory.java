package uk.co.dmott.mysupershopper2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import uk.co.dmott.mysupershopper2.data.ShopItemRepository;

/**
 * Created by david on 22/03/18.
 */

public class CustomViewModelFactory implements ViewModelProvider.Factory{
    private final ShopItemRepository repository;

    @Inject
    public CustomViewModelFactory(ShopItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ShopItemCollectionViewModel.class))
            return (T) new ShopItemCollectionViewModel(repository);

        else if (modelClass.isAssignableFrom(ShopItemViewModel.class))
            return (T) new ShopItemViewModel(repository);

        else if (modelClass.isAssignableFrom(NewShopItemViewModel.class))
            return (T) new NewShopItemViewModel(repository);

        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }




}
