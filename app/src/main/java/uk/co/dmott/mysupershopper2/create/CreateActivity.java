package uk.co.dmott.mysupershopper2.create;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import uk.co.dmott.mysupershopper2.R;
import uk.co.dmott.mysupershopper2.util.BaseActivity;

/**
 * Created by david on 22/03/18.
 */

public class CreateActivity extends BaseActivity {
    private static final String CREATE_FRAG = "CREATE_FRAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        FragmentManager manager = getSupportFragmentManager();

        CreateFragment fragment = (CreateFragment) manager.findFragmentByTag(CREATE_FRAG);

        if (fragment == null) {
            fragment = CreateFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.root_activity_create,
                CREATE_FRAG
        );

    }





}
