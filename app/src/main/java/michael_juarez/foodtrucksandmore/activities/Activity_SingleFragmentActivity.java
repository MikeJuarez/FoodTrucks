package michael_juarez.foodtrucksandmore.activities;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.ButterKnife;
import michael_juarez.foodtrucksandmore.R;

/**
 * Created by user on 11/8/2017.
 */

public abstract class Activity_SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment(Bundle savedInstanceState);
    protected abstract void setupToolbar();
    private static final String KEY_ACTIVITY_FOODSPOTLIST_FOODSPOTLISTARRAY = "Activity_SingleFragmentActivity.Fragment";
    private TextView mToolbarText;
    private Fragment mFragment;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setContentView(R.layout.activity_fragment);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        mToolbarText = (TextView) findViewById(R.id.main_toolbar_apptitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/app_font.ttf");
        mToolbarText.setTypeface(custom_font);
        setSupportActionBar(mToolbar);
        setupToolbar();


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
