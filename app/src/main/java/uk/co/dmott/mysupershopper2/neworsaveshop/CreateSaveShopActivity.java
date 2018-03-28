package uk.co.dmott.mysupershopper2.neworsaveshop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.util.BaseActivity;

public class CreateSaveShopActivity extends BaseActivity {

    private static final String CREATESAVESHOP_FRAG = "CREATESAVESHOP_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworsaveshop);


        FragmentManager manager = getSupportFragmentManager();

        CreateSaveShopFragment fragment = (CreateSaveShopFragment) manager.findFragmentByTag(CREATESAVESHOP_FRAG);

        if (fragment == null) {
            fragment = CreateSaveShopFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.root_activity_neworsaveshop,
                CREATESAVESHOP_FRAG
        );

    }


}
