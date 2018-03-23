package uk.co.dmott.mysupershopper2.create;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import uk.co.dmott.mysupershopper2.MySuperShopper2;
import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.data.ShopItem;
import uk.co.dmott.mysupershopper2.list.ListActivity;
import uk.co.dmott.mysupershopper2.viewmodel.NewShopItemViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * Created by david on 22/03/18.
 */

public class CreateFragment extends Fragment {
    private ViewPager drawablePager;
    private CirclePageIndicator pageIndicator;
    private ImageButton back;
    private ImageButton done;
    private EditText messageInput;
    private Button speak;
    private TextView category;

    private PagerAdapter pagerAdapter;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private NewShopItemViewModel newShopItemViewModel;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((MySuperShopper2) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set up and subscribe (observe) to the ViewModel
        newShopItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewShopItemViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create2, container, false);

        back = (ImageButton) v.findViewById(R.id.imb_create_back);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListActivity();
            }
        });

        speak = (Button)v.findViewById(R.id.btn_speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInputActivity();
            }
        });



        done = (ImageButton) v.findViewById(R.id.imb_create_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopItem listItem = new ShopItem(
                        getDate(),
                        messageInput.getText().toString(),
                        "Default",
                        getDrawableResource(drawablePager.getCurrentItem())
                );
                newShopItemViewModel.addNewItemToDatabase(listItem);

                startListActivity();
            }
        });

        category = (TextView) v.findViewById(R.id.tv_category);

        messageInput = (EditText) v.findViewById(R.id.edt_create_message);

        drawablePager = (ViewPager) v.findViewById(R.id.vp_create_drawable);

        drawablePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        category.setText("Vegetables and Fruit");
                        break;
                    case 1:
                        category.setText("Drink");
                        break;
                    case 2:
                        category.setText("Frozen/Chilled");
                        break;
                    case 3:
                        category.setText("Tins/Jars/Packets");
                        break;
                    case 4:
                        category.setText("Other/Houseware/Clearers");
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pagerAdapter = new DrawablePagerAdapter();
        drawablePager.setAdapter(pagerAdapter);

        pageIndicator = (CirclePageIndicator) v.findViewById(R.id.vpi_create_drawable);
        pageIndicator.setViewPager(drawablePager);

        return v;
    }

    private void startVoiceInputActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }



    }

    // Receiving speech input

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageInput.setText(result.get(0));
                }
                break;
            }

        }
    }





    public int getDrawableResource (int pagerItemPosition){
        switch (pagerItemPosition){
            case 0:
                return R.drawable.vegandfruitfinal;
            case 1:
                return R.drawable.drinksfinal;
            case 2:
                return R.drawable.frozenchilledfinal;
            case 3:
                return R.drawable.tinsjarsfinal;
            case 4:
                return R.drawable.otherfinal;
            default:
                return 0;
        }
    }

    private void startListActivity() {
        startActivity(new Intent(getActivity(), ListActivity.class));
    }

    private class DrawablePagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ImageView pagerItem = (ImageView) inflater.inflate(R.layout.item_drawable_pager,
                    container,
                    false);

            switch (position) {
                case 0:
                    pagerItem.setImageResource(R.drawable.vegandfruitfinal);
                    break;
                case 1:
                    pagerItem.setImageResource(R.drawable.drinksfinal);
                    break;
                case 2:
                    pagerItem.setImageResource(R.drawable.frozenchilledfinal);
                    break;
                case 3:
                    pagerItem.setImageResource(R.drawable.tinsjarsfinal);
                    break;
                case 4:
                    pagerItem.setImageResource(R.drawable.otherfinal);
                    break;
            }

            container.addView(pagerItem);
            return pagerItem;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public String getDate() {

        Date currentDate = Calendar.getInstance().getTime();

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd/kk:mm:ss");

        return format.format(currentDate);
    }








}
