package uk.co.dmott.mysupershopper2.newshop;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import uk.co.dmott.mysupershopper2.MySuperShopper2;
import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.list.ListActivity;

/**
 * Created by david on 28/03/18.
 */

public class NewShopFragment extends Fragment {

    private static final String TAG = "NewShopFragment";

    private List<ShopItem> listOfData;
    private SharedPreferences myPreferences;
    private String currentShop;
    private Context ctxt;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    //ShopItemCollectionViewModel shopItemCollectionViewModel;
    //private NewShopItemViewModel newShopItemViewModel;

    public NewShopFragment() {
    }
    public static NewShopFragment newInstance() {
        return new NewShopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MySuperShopper2) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myPreferences = (getActivity().getSharedPreferences("dmott.co.MySuperShopper2", Context.MODE_PRIVATE));

        SharedPreferences.Editor myEditor = myPreferences.edit();

        if (myPreferences.contains("LatestShopList"))
        {
            currentShop = myPreferences.getString("LatestShopList", "Default") ;
            Log.d(TAG, "Current shop in create/save is " + currentShop);

        }
        else
        {
            throw new IllegalArgumentException("Impossible error - no SharedPreferences");
        }



        //Set up and subscribe (observe) to the ViewModel
       // newShopItemViewModel = ViewModelProviders.of(this, viewModelFactory)
        //        .get(NewShopItemViewModel.class);

        //Set up and subscribe (observe) to the ViewModel
        //shopItemCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
       //         .get(ShopItemCollectionViewModel.class);



      //  });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newshop, container, false);


        final EditText etNewname = (EditText) v.findViewById(R.id.et_createnewshopname);
        final Button btnSavebutton = (Button) v.findViewById(R.id.btn_newshop_save);
        final Button btnCancelbutton = (Button) v.findViewById(R.id.btn_newshop_cancel);

        btnCancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cancelling the save activity", Toast.LENGTH_SHORT).show();
                Intent listActivityIntent = new Intent(getActivity(), ListActivity.class);
                startActivity(listActivityIntent);
            }
        });

        btnSavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Saving the new data", Toast.LENGTH_SHORT).show();
                String newShopName = etNewname.getText().toString();


                if (newShopName.isEmpty())
                {

                    Toast.makeText(getActivity(), "Please enter a shop name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d(TAG, "Set up the new value for LatestShop in SharedPreferences");
                    Log.d(TAG, "No need to save or delete anything but need to add to FullShopsList");

                    SharedPreferences.Editor myEditor = myPreferences.edit();

                    currentShop = myPreferences.getString("LatestShopList", "Default") ;
                    Log.d(TAG, "Changing currentShop from  " + currentShop + "to " + newShopName);
                    myEditor.putString("LatestShopList", newShopName);




                    List<ShopItem> newListOfData = new ArrayList<ShopItem>();
                    List<String> shopsHistoryList = new ArrayList<String>();
                    shopsHistoryList.add(currentShop);
                    Set<String> set = new HashSet<String>();
                    Set<String> getset = new HashSet<String>();
                    set.addAll(shopsHistoryList);

                    // add the old shop to the FullShopsList



                    if (myPreferences.contains("FullShopsList"))
                    {
                        Log.d(TAG, "In newShopFragment get the current set stored for FullShopsList");
                        getset = myPreferences.getStringSet("FullShopsList", set) ;
                        Log.d(TAG, "In newShopFragment saving the old shop to the Shops History = " + currentShop);
                        ArrayList<String> shopList = new ArrayList<>();
                        shopList.addAll(getset);

                        Log.d(TAG, "In newShopFragment - the current History of shops is  :-");
                        for(String s: shopList)
                        {
                            Log.d(TAG, "Shop History Item  - " + s);

                        }


                        shopList.add(currentShop);
                        set.clear();
                        set.addAll(shopList);
                        myEditor.putStringSet("FullShopsList", set);
                        myEditor.commit();



                    }
                    else
                    {
                        // history does not exist
                        myEditor.putStringSet("FullShopsList", set);
                        //currentShop = "Default";
                        Log.d(TAG, "In saveTheNewShopData - Current shop is not in myPreferences so creating FullShopsList ");
                        myEditor.commit();
                    }




                    Intent listActivityIntent = new Intent(getActivity(), ListActivity.class);
                    listActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(listActivityIntent);


                }


            }
        });

        return v;




    }

}
