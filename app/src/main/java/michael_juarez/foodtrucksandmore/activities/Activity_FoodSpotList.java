package michael_juarez.foodtrucksandmore.activities;


import butterknife.ButterKnife;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import static android.R.attr.tag;


public class Activity_FoodSpotList extends Activity_SingleFragmentActivity {
    private static final String TAG = "Activity_FoodSpotList";
    public static final String KEY_COORDINATES = "Activity_FoodSpotList.Key_Coordinates";
    public static final String KEY_FRAGMENT_ID = "Activity_FoodSpotList.KEY_FRAGMENT_ID";

    private Fragment_FoodSpotList mFoodSpotListFragment;

    @Override
    protected Fragment createFragment(Bundle savedInstanceState) {

        mFoodSpotListFragment = new Fragment_FoodSpotList();

        //Get latitude/longitude coordinates from Fragment_Address class
        Intent intent = getIntent();
        String[] address = intent.getStringArrayExtra(KEY_COORDINATES);

        //Create new Bundle that will pass in the coordinates to the Fragment class
        Bundle bundle = new Bundle();
        bundle.putStringArray(Fragment_FoodSpotList.KEY_COORDINATES, address);
        mFoodSpotListFragment.setArguments(bundle);

        return mFoodSpotListFragment;
    }

    @Override
    protected void setupToolbar() {

        mToolbar.setNavigationIcon(R.drawable.ic_home);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Activity_Address.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Activity_Address.class);
        startActivity(intent);
    }
}
