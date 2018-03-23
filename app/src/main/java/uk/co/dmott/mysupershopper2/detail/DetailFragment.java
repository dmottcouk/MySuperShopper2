package uk.co.dmott.mysupershopper2.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.dmott.mysupershopper2.MySuperShopper2;
import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.viewmodel.ShopItemViewModel;

/**
 * Created by david on 22/03/18.
 */

public class DetailFragment extends Fragment {

    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private TextView dateAndTime;
    private TextView message;
    private ImageView coloredBackground;

    private String itemId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ShopItemViewModel shopItemViewModel;

    public DetailFragment() {
    }


    public static DetailFragment newInstance(String itemId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ITEM_ID, itemId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MySuperShopper2) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        Bundle args = getArguments();

        this.itemId = args.getString(EXTRA_ITEM_ID);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set up and subscribe (observe) to the ViewModel
        shopItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopItemViewModel.class);

        shopItemViewModel.getShopItemById(itemId).observe(this, new Observer<ShopItem>() {
            @Override
            public void onChanged(@Nullable ShopItem shopItem) {
                if (shopItem != null) {
                    dateAndTime.setText(shopItem.getItemId());
                    message.setText(shopItem.getShopItemName());
                    coloredBackground.setImageResource(shopItem.getShopItemType());
                }

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        dateAndTime = (TextView) v.findViewById(R.id.lbl_date_and_time_header);

        message = (TextView) v.findViewById(R.id.lbl_message_body);


        coloredBackground = (ImageView) v.findViewById(R.id.imv_colored_background);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}


