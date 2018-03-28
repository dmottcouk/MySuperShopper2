package uk.co.dmott.mysupershopper2.list;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.dmott.mysupershopper2.MySuperShopper2;
import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.create.CreateActivity;
import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.detail.DetailActivity;
import uk.co.dmott.mysupershopper2.helper.ItemTouchHelperAdapter;
import uk.co.dmott.mysupershopper2.helper.ItemTouchHelperViewHolder;
import uk.co.dmott.mysupershopper2.helper.OnStartDragListener;
import uk.co.dmott.mysupershopper2.helper.SimpleItemTouchHelperCallback;
import uk.co.dmott.mysupershopper2.neworsaveshop.CreateSaveShopActivity;
import uk.co.dmott.mysupershopper2.newshop.NewShopActivity;
import uk.co.dmott.mysupershopper2.viewmodel.ShopItemCollectionViewModel;

/**
 * Created by david on 22/03/18.
 */

public class ListFragment extends Fragment implements OnStartDragListener{

    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private static final String TAG = "ListFragment";

    private ItemTouchHelper mItemTouchHelper;

    private List<ShopItem> listOfData;

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Toolbar toolbar;
    private SharedPreferences myPreferences;
    private String currentShop;
    private Context ctxt;
    private boolean dialogCancelled;

    private Boolean newShoponSave;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ShopItemCollectionViewModel shopItemCollectionViewModel;

    public ListFragment() {
    }
    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MySuperShopper2) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        setHasOptionsMenu(true);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart  ");


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myPreferences = (getActivity().getSharedPreferences("dmott.co.MySuperShopper2", Context.MODE_PRIVATE));

        SharedPreferences.Editor myEditor = myPreferences.edit();

        if (myPreferences.contains("LatestShopList"))
        {
            currentShop = myPreferences.getString("LatestShopList", "Default") ;
            Log.d(TAG, "In onActivityCreated  Current shop is " + currentShop);

        }
        else
        {
            myEditor.putString("LatestShopList", "Default");
            currentShop = "Default";
            Log.d(TAG, "Current shop is not in myPreferences so creating LatestShopList ");
            myEditor.commit();
        }


        //Set up and subscribe (observe) to the ViewModel
        shopItemCollectionViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopItemCollectionViewModel.class);

        Log.d(TAG, "In onActivityCreated - observe shop  " + currentShop);

 //       shopItemCollectionViewModel.getShopItemsForNamedShop("Default").observe(this, new Observer<List<ShopItem>>() {
        shopItemCollectionViewModel.getShopItemsForNamedShop(currentShop).observe(this, new Observer<List<ShopItem>>() {

        @Override
            public void onChanged(@Nullable List<ShopItem> ShopItems) {
                if (ListFragment.this.listOfData == null) {
                    Log.d(TAG, "In OnChanged of onActivityCreated");
                    setListData(ShopItems);
                }
            }
        });

      //  setListData(ShopItems);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rec_list_activity);
        layoutInflater = getActivity().getLayoutInflater();
        toolbar = (Toolbar) v.findViewById(R.id.tlb_list_activity);

        toolbar.setTitle(R.string.title_toolbar);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);
        toolbar.setTitleMarginStart(72);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.menu_main);

        FloatingActionButton fabulous = (FloatingActionButton) v.findViewById(R.id.fab_create_new_item);

        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateActivity();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {

        ctxt = context;


        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void startDetailActivity(String itemId, View viewRoot) {
        Activity container = getActivity();
        Intent i = new Intent(container, DetailActivity.class);
        i.putExtra(EXTRA_ITEM_ID, itemId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            container.getWindow().setEnterTransition(new Fade(Fade.IN));
            container.getWindow().setEnterTransition(new Fade(Fade.OUT));

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(container,
                            new Pair<>(viewRoot.findViewById(R.id.imv_list_item_circle),
                                    getString(R.string.transition_drawable)),
                            new Pair<>(viewRoot.findViewById(R.id.lbl_message),
                                    getString(R.string.transition_message)),
                            new Pair<>(viewRoot.findViewById(R.id.lbl_date_and_time),
                                    getString(R.string.transition_time_and_date)));

            startActivity(i, options.toBundle());

        } else {
            startActivity(i);
        }
    }

    public void startCreateActivity() {

        Intent intent = new Intent(getActivity(), CreateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(new Intent(getActivity(), CreateActivity.class));
        startActivity(intent);
    }



/*

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CustomAdapter2 adapter = new CustomAdapter2(getActivity(), this);

        //RecyclerView recyclerView = (RecyclerView) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_list_activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

*/



    public void setListData(List<ShopItem> listOfData) {
        Log.d(TAG, "In setListData");
        this.listOfData = listOfData;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
 //       adapter = new CustomAdapter();
 //       adapter = new CustomAdapter2(getActivity(), this);
 //       recyclerView.setAdapter(adapter);


        CustomAdapter2 adapter = new CustomAdapter2(getActivity(), this);

        //RecyclerView recyclerView = (RecyclerView) view;

        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);






        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        R.drawable.divider_white
                )
        );

        recyclerView.addItemDecoration(
                itemDecoration
        );


 //       ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
 //       itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ItemViewHolder>
            implements ItemTouchHelperAdapter {

        private final OnStartDragListener mDragStartListener;

        public CustomAdapter2(Context context, OnStartDragListener dragStartListener) {
            mDragStartListener = dragStartListener;
           // mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
        }


        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            ShopItem currentItem = listOfData.get(position);
            holder.coloredCircle.setImageResource(currentItem.getShopItemType());
            String shopName = currentItem.getShopName();
            Log.d(TAG, "Shop name is " + shopName);

            holder.message.setText(
                    currentItem.getShopItemName()
            );
            holder.dateAndTime.setText(currentShop + "  ," +
                    currentItem.getItemId()
            );
            holder.loading.setVisibility(View.INVISIBLE);

            // Start a drag whenever the handle view it touched
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
/*
        public void refreshEvents(List<MyEvent> events) {
            this.events.clear();
            this.events.addAll(events);
            notifyDataSetChanged();
        }
*/

        @Override
        public void onItemDismiss(int position) {
            listOfData.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            //Collections.swap(mItems, fromPosition, toPosition);
            ShopItem item = listOfData.get(fromPosition);
            listOfData.remove(fromPosition);
            listOfData.add(toPosition, item);


            notifyItemMoved(fromPosition, toPosition);

            int index = 0;
            Log.d(TAG, "onItemMove ");

            for (ShopItem itm :listOfData) {
                Log.d(TAG, "Index " + index);
                Log.d(TAG, "Shop name " + itm.getShopName());
                Log.d(TAG, "Item name " + itm.getShopItemName());
                Log.d(TAG, "Item id " + itm.getItemId());
                Log.d(TAG, "Drawable id " + itm.getShopItemType());
                index++;
            }





            return true;
        }
        @Override
        public int getItemCount() {
            return listOfData.size();
        }

        /**
         * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
         * "handle" view that initiates a drag event when touched.
         */
        public class ItemViewHolder extends RecyclerView.ViewHolder implements
                ItemTouchHelperViewHolder {


            private CircleImageView coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;
            private ProgressBar loading;
            public final ImageView handleView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                this.coloredCircle = (CircleImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);
                this.loading = (ProgressBar) itemView.findViewById(R.id.pro_item_data);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);
                handleView = (ImageView) itemView.findViewById(R.id.imv_date_and_time);
            }

            @Override
            public void onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear() {
                itemView.setBackgroundColor(0);
            }
        }

    }



    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements ItemTouchHelperAdapter {//6

        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_data, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomAdapter.CustomViewHolder holder, int position) {
            //11. and now the ViewHolder data
            ShopItem currentItem = listOfData.get(position);

            holder.coloredCircle.setImageResource(currentItem.getShopItemType());


            holder.message.setText(
                    currentItem.getShopItemName()
            );

            holder.dateAndTime.setText(
                    currentItem.getItemId()
            );

            holder.loading.setVisibility(View.INVISIBLE);
        }

        public void moveitem(int oldPos, int newPos) {
            ShopItem item = listOfData.get(oldPos);
            listOfData.remove(oldPos);
            listOfData.add(newPos, item);
            notifyItemMoved(oldPos, newPos);
            int index = 0;
            Log.d(TAG, "moveitem ");

            for (ShopItem itm :listOfData) {
                Log.d(TAG, "Index " + index);
                Log.d(TAG, "Old Shop name " + itm.getShopName());
                Log.d(TAG, "Old Item name " + itm.getShopItemName());
                Log.d(TAG, "Old Item id " + itm.getItemId());
                Log.d(TAG, "Old Drawable id " + itm.getShopItemType());
                index++;
            }


        }




        @Override
        public int getItemCount() {
            // 12. Returning 0 here will tell our Adapter not to make any Items. Let's fix that.
            return listOfData.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            return false;
        }

        @Override
        public void onItemDismiss(int position) {

        }


        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //10. now that we've made our layouts, let's bind them
            private CircleImageView coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;
            private ProgressBar loading;

            public CustomViewHolder(View itemView) {
                super(itemView);
                this.coloredCircle = (CircleImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);
                this.loading = (ProgressBar) itemView.findViewById(R.id.pro_item_data);

                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);
                /*
                We can pass "this" as an Argument, because "this", which refers to the Current
                Instance of type CustomViewHolder currently conforms to (implements) the
                View.OnClickListener interface. I have a Video on my channel which goes into
                Interfaces with Detailed Examples.

                Search "Android WTF: Java Interfaces by Example"
                 */
                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //getAdapterPosition() get's an Integer based on which the position of the current
                //ViewHolder (this) in the Adapter. This is how we get the correct Data.
                ShopItem listItem = listOfData.get(
                        this.getAdapterPosition()
                );

                startDetailActivity(listItem.getItemId(), v);

            }
        }

    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                adapter.moveitem(viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                shopItemCollectionViewModel.deleteShopItem(
                        listOfData.get(position)
                );

                //ensure View is consistent with underlying data
                listOfData.remove(position);
                adapter.notifyItemRemoved(position);


            }
        };
        return simpleItemTouchCallback;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.mynewbutton:
                Toast.makeText(getActivity(), "Pressed new button", Toast.LENGTH_SHORT).show();
/**

                final View getnewShopView = layoutInflater.inflate(R.layout.newshop_create, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(ctxt).create();
                alertDialog.setTitle("Your Title Here");
                //alertDialog.setIcon("Icon id here");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Your Message Here");


                final EditText etComments = (EditText) getnewShopView.findViewById(R.id.etNewShop);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "New shop alert .. positive button");
                            String newShopName = etComments.getText().toString();
                            if (!newShopName.isEmpty()) {

                                myPreferences = (getActivity().getSharedPreferences("dmott.co.MySuperShopper2", Context.MODE_PRIVATE));

                                SharedPreferences.Editor myEditor = myPreferences.edit();
                                myEditor.putString("LatestShopList", etComments.getText().toString());
                                Log.d(TAG, "In the New shop dialog - saving new shop to Preferences ");
                                myEditor.commit();


                                dialogCancelled = false;
                            }
                            else
                                dialogCancelled = true;
                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "New shop alert .. negative button");
                        dialogCancelled= true;
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setView(getnewShopView);
                alertDialog.show();
                Log.d(TAG, "New shop finished doing dialog");
 /
                if (!dialogCancelled)
                {
                    Log.d(TAG, "New shop restarting the activity");
                    getActivity().finish();
                    startActivity(getActivity().getIntent());

                }
                else
                {
                    Log.d(TAG, "Dont restart the activity");
                }
/
 */

                Intent newShopIntent = new Intent(getActivity(), NewShopActivity.class);
                //Bundle myBundle = new Bundle();
                //myBundle.putParcelableArrayList("data",  (ArrayList<? extends Parcelable>)listOfData); // Be sure con is not null here
                //createSaveIntent.putParcelableArrayListExtra("dmott.co.uk.shopitems",(ArrayList<? extends Parcelable>)listOfData);
                newShopIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(newShopIntent);





                break;
            case R.id.mysavebutton:
/**
                Toast.makeText(getActivity(), "Pressed save button", Toast.LENGTH_SHORT).show();
                final View getsaveShopView = layoutInflater.inflate(R.layout.saveshop_dialog, null);
                final AlertDialog alertDialog2 = new AlertDialog.Builder(ctxt).create();
                alertDialog2.setTitle("Save or Save as New");
                //alertDialog.setIcon("Icon id here");
                alertDialog2.setCancelable(false);
                alertDialog2.setMessage("Click the checkbox to save as new Shop list");


                final EditText etNewname = (EditText) getsaveShopView.findViewById(R.id.et_saveasnewname);
                final CheckBox cbSaveasNew = (CheckBox) getsaveShopView.findViewById(R.id.cb_saveasnew);

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



                alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "New shop alert .. positive button");

                        // if new Shop then we need to



                    }
                });


                alertDialog2.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "New shop alert .. negative button");
                        alertDialog2.dismiss();
                    }
                });


                alertDialog2.setView(getsaveShopView);
                alertDialog2.show();
*/

                Intent createSaveIntent = new Intent(getActivity(), CreateSaveShopActivity.class);
                //Bundle myBundle = new Bundle();
                //myBundle.putParcelableArrayList("data",  (ArrayList<? extends Parcelable>)listOfData); // Be sure con is not null here
                createSaveIntent.putParcelableArrayListExtra("dmott.co.uk.shopitems",(ArrayList<? extends Parcelable>)listOfData);

                createSaveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(createSaveIntent);


                break;

            case R.id.action_new:
                Toast.makeText(getActivity(), "menu = new", Toast.LENGTH_SHORT).show();

                break;
            case R.id.action_save:
                Toast.makeText(getActivity(), "menu = save", Toast.LENGTH_SHORT).show();
                // Do Fragment menu item stuff here
                break;

            case R.id.action_choose:
                Toast.makeText(getActivity(), "menu = choose", Toast.LENGTH_SHORT).show();
                // Do Fragment menu item stuff here
                break;
            case R.id.action_refresh:
                Toast.makeText(getActivity(), "menu = refresh", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(getActivity().getIntent());
                // Do Fragment menu item stuff here
                break;
            default:
                break;
        }

        return false;
    }



/**

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/


}
