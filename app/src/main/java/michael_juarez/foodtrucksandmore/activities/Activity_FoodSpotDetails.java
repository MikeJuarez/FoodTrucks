package michael_juarez.foodtrucksandmore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotDetails;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotList;

import static michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotDetails.KEY_IMAGE_LINK;

/**
 * Created by user on 11/14/2017.
 */

public class Activity_FoodSpotDetails extends Activity_SingleFragmentActivity {
    //public static final String KEY_ACTIVITY_FOODSPOTS_IMAGELINK = "Activity_FoodSpotDetails.ImageLink";
    public static final String KEY_ACTIVITY_FOODSPOTDETAILS_FOODSPOT = "Activity_FoodSpotDetails.FoodSpot";


    @Override
    protected Fragment createFragment(Bundle SavedInstanceState) {

        FoodSpot fs = (FoodSpot) getIntent().getSerializableExtra(KEY_ACTIVITY_FOODSPOTDETAILS_FOODSPOT);

        Bundle bundle = new Bundle();

        bundle.putSerializable(Fragment_FoodSpotDetails.KEY_FOODSPOT, fs);

        Fragment_FoodSpotDetails fragment = new Fragment_FoodSpotDetails();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
    }


}
