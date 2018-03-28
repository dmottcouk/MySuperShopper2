package uk.co.dmott.mysupershopper2.neworsaveshop;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import uk.co.dmott.mysupershopper2.MySuperShopper2;
import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.list.ListActivity;
import uk.co.dmott.mysupershopper2.viewmodel.NewShopItemViewModel;
import uk.co.dmott.mysupershopper2.viewmodel.ShopItemCollectionViewModel;

/**
 * Created by david on 27/03/18.
 */

public class CreateSaveShopFragment extends Fragment {

    private static final String TAG = "CreateSaveShopFragment";

    private Boolean newShoponSave;
    private List<ShopItem> listOfData;
    private List<ShopItem> intentListOfData;
    private SharedPreferences myPreferences;
    private String currentShop;
    private Context ctxt;


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ShopItemCollectionViewModel shopItemCollectionViewModel;
    private NewShopItemViewModel newShopItemViewModel;

    public CreateSaveShopFragment() {
    }
    public static CreateSaveShopFragment newInstance() {
        return new CreateSaveShopFragment();
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
        newShopItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewShopItemViewModel.class);

        //Set up and subscribe (observe) to the ViewModel
        shopItemCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopItemCollectionViewModel.class);

        //       shopItemCollectionViewModel.getShopItemsForNamedShop("Default").observe(this, new Observer<List<ShopItem>>() {
        shopItemCollectionViewModel.getShopItemsForNamedShop(currentShop).observe(this, new Observer<List<ShopItem>>() {

            @Override
            public void onChanged(@Nullable List<ShopItem> ShopItems) {
                if (CreateSaveShopFragment.this.listOfData == null) {
                    Log.d(TAG, "In OnChanged of CreateSaveShopFragment");
                    setListData(ShopItems);
                }
            }
        });

    }

    public void setListData(List<ShopItem> listOfData) {
        this.listOfData = listOfData;
        Log.d(TAG, "setListData in createSave");
        Log.d(TAG, "number of elements to be saved = " + listOfData.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_neworsaveshop, container, false);

        intentListOfData = getActivity().getIntent().getParcelableArrayListExtra("dmott.co.uk.shopitems");
        //getActivity().getIntent().getExtras().getString("name");





        final EditText etNewname = (EditText) v.findViewById(R.id.et_createsavenewshopname);
        final CheckBox cbSaveasNew = (CheckBox) v.findViewById(R.id.cb_createnewshop);
        final Button btnSavebutton = (Button)v.findViewById(R.id.btn_save);
        final Button btnCancelbutton = (Button)v.findViewById(R.id.btn_cancel);

        cbSaveasNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "Checkbox listener change b = " + String.valueOf(b));
                if (b == true)
                {
                    etNewname.setEnabled(true);
                    newShoponSave = true;
                }
                else
                {
                    etNewname.setEnabled(false);
                    newShoponSave = false;
                }

            }
        });

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
                String newShopName = "Default";
                if(cbSaveasNew.isChecked()){
                    Log.d(TAG, "Saving as new new shop data");
                    newShopName = etNewname.getText().toString();
                    Log.d(TAG, "New database shop name is " + newShopName);
                    if (newShopName.isEmpty())
                    {

                        Toast.makeText(getActivity(), "Please enter a shop name", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d(TAG, "Save the new shop data call saveTheNewShopData");
                        saveTheNewShopData(newShopName);
                    }


                }
                else {
                    Log.d(TAG, "Replace the current shop data call saveTheCurrentShopData ");
                    newShopName= currentShop;
                    saveTheCurrentShopData(newShopName);

                }

                SharedPreferences.Editor myEditor = myPreferences.edit();

                currentShop = myPreferences.getString("LatestShopList", "Default") ;
                Log.d(TAG, "Changing currentShop from  " + currentShop + "to " + newShopName);
                myEditor.putString("LatestShopList", newShopName);
                myEditor.commit();

                Intent listActivityIntent = new Intent(getActivity(), ListActivity.class);
                listActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(listActivityIntent);




            }
        });

        return v;
    }


    private void saveTheNewShopData (String  databaseShopName)
    {
        Log.d(TAG, "saveTheNewShopData - Saving as new new shop data + new shop name is" + databaseShopName);
        int index = 0;
        int listSize = listOfData.size();

        List<ShopItem> newListOfData = new ArrayList<ShopItem>();
        List<String> shopsHistoryList = new ArrayList<String>();
        shopsHistoryList.add(currentShop);
        Set<String> set = new HashSet<String>();
        Set<String> getset = new HashSet<String>();
        set.addAll(shopsHistoryList);

        // add the old shop to the FullShopsList

        myPreferences = (getActivity().getSharedPreferences("dmott.co.MySuperShopper2", Context.MODE_PRIVATE));

        SharedPreferences.Editor myEditor = myPreferences.edit();

        if (myPreferences.contains("FullShopsList"))
        {
            Log.d(TAG, "In saveTheNewShopData get the current set stored for FullShopsList");
            getset = myPreferences.getStringSet("FullShopsList", set) ;
            Log.d(TAG, "In saveTheNewShopData saving the old shop to the Shops History = " + currentShop);
            ArrayList<String> shopList = new ArrayList<>();
            shopList.addAll(getset);

            Log.d(TAG, "In saveTheNewShopData - the current History of shops is  :-");
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




        for (ShopItem itm :intentListOfData){
            Log.d(TAG, "Index " + index);
            Log.d(TAG, "Old Shop name " + itm.getShopName());
            Log.d(TAG, "Old Item name " + itm.getShopItemName());
            String itmName = itm.getShopItemName();
            Log.d(TAG, "Old Item id " + itm.getItemId());
            Log.d(TAG, "Old Drawable id " + itm.getShopItemType());
            int itmType = itm.getShopItemType();

            //String newShopName = newShopName;
            // set the new database data up with the new Shop name
            ShopItem listItem = new ShopItem(
                    getDate()+index,
                    itmName,
                    databaseShopName,
                    itmType);

            newListOfData.add(listItem);
            index++;

        }
        index = 0;
        for (ShopItem itm :newListOfData) {

            Log.d(TAG, "Index " + index);
            Log.d(TAG, "New Shop name " + itm.getShopName());
            Log.d(TAG, "New Item name " + itm.getShopItemName());
            Log.d(TAG, "New Item id " + itm.getItemId());
            Log.d(TAG, "New Drawable id " + itm.getShopItemType());

            Log.d(TAG, "Add item to the database");

            newShopItemViewModel.addNewItemToDatabase(itm);
            index++;

        }


    }
    private void saveTheCurrentShopData (String restoreShopName)
    {

        List<ShopItem> newListOfData = new ArrayList<ShopItem>();
        // we need to delete the current shop items and then readd new ones
        Log.d(TAG, "saveTheCurrentShopData - Saving as current shop data");
        int index = 0;
        String shopNameToKeep = null;


        for (ShopItem itm :intentListOfData) {

            Log.d(TAG, "Index " + index);
            Log.d(TAG, "Shop name to delete then restore " + itm.getShopName());
            shopNameToKeep = itm.getShopName();
            Log.d(TAG, "Item name to delete " + itm.getShopItemName());
            String itmName = itm.getShopItemName();
            Log.d(TAG, "Item id to delete " + itm.getItemId());
            Log.d(TAG, "Drawable id to delete" + itm.getShopItemType());
            int itmType = itm.getShopItemType();

            Log.d(TAG, "Delete item to be replaced from the database");

            ShopItem listItem = new ShopItem(
                    getDate()+index,
                    itmName,
                    shopNameToKeep,
                    itmType);

            newListOfData.add(listItem);
            // delete the item from the database and then add it back
//            shopItemCollectionViewModel.deleteShopItem(itm);
            index++;

        }
        shopItemCollectionViewModel.deleteShopItemsForNamedShop(restoreShopName);




        index = 0;
        for (ShopItem itm :newListOfData) {

            Log.d(TAG, "Index " + index);
            Log.d(TAG, "Replacement Shop name " + itm.getShopName());
            Log.d(TAG, "Replacement Item name " + itm.getShopItemName());
            Log.d(TAG, "Replacement Item id " + itm.getItemId());
            Log.d(TAG, "Replacement Drawable id " + itm.getShopItemType());

            Log.d(TAG, "Replace  item to the database");

            newShopItemViewModel.addNewItemToDatabase(itm);
            index++;

        }

    }

    public String getDate() {

        Date currentDate = Calendar.getInstance().getTime();

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd/kk:mm:ss");

        return format.format(currentDate);
    }



}
