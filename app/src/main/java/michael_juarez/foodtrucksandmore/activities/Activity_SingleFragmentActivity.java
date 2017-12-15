package michael_juarez.foodtrucksandmore.activities;

import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotDetails;

import static android.R.attr.fragment;
import static android.R.attr.type;
import static okhttp3.internal.Internal.instance;

/**
 * Created by user on 11/8/2017.
 */

public abstract class Activity_SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment(Bundle savedInstanceState);
    private static final String KEY_ACTIVITY_FOODSPOTLIST_FOODSPOTLISTARRAY = "Activity_SingleFragmentActivity.Fragment";
    private TextView mToolbarText;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setContentView(R.layout.activity_fragment);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        mToolbarText = (TextView) findViewById(R.id.toolbar_apptitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/app_font.ttf");
        mToolbarText.setTypeface(custom_font);

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState != null)
            mFragment = fm.getFragment(savedInstanceState, KEY_ACTIVITY_FOODSPOTLIST_FOODSPOTLISTARRAY);

        if (mFragment == null)
            mFragment = createFragment(savedInstanceState);

        fm.beginTransaction()
                .replace(R.id.fragment_container, mFragment, KEY_ACTIVITY_FOODSPOTLIST_FOODSPOTLISTARRAY)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        getSupportFragmentManager().putFragment(outstate, KEY_ACTIVITY_FOODSPOTLIST_FOODSPOTLISTARRAY, mFragment);
    }

}
