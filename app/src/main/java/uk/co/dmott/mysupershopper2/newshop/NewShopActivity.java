package uk.co.dmott.mysupershopper2.newshop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.util.BaseActivity;

/**
 * Created by david on 28/03/18.
 */

public class NewShopActivity extends BaseActivity {

    private static final String NEWSHOP_FRAG = "NEWSHOP_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newshop);


        FragmentManager manager = getSupportFragmentManager();

        NewShopFragment fragment = (NewShopFragment) manager.findFragmentByTag(NEWSHOP_FRAG);

        if (fragment == null) {
            fragment = NewShopFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.root_activity_newshop,
                NEWSHOP_FRAG
        );

    }
}
