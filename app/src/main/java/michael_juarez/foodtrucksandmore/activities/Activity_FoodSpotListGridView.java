package michael_juarez.foodtrucksandmore.activities;


import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotList;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotList_GridView;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;


public class Activity_FoodSpotListGridView extends Activity_SingleFragmentActivity {

    //public static final String KEY_COORDINATES = "Activity_FoodSpotList.Key_Coordinates";
    public static final String KEY_FOODSPOTLIST = "Activity_FoodSpotList.KEY_FOODSPOTLIST";

    private Fragment_FoodSpotList_GridView mFoodSpotListFragment;

    @Override
    protected Fragment createFragment(Bundle savedInstanceState) {
        mFoodSpotListFragment = new Fragment_FoodSpotList_GridView();

        //Get latitude/longitude coordinates from Fragment_Address class
        Intent intent = getIntent();
        //String[] address = intent.getStringArrayExtra(KEY_COORDINATES);
        ArrayList<FoodSpot> fsList = (ArrayList<FoodSpot>) intent.getSerializableExtra(KEY_FOODSPOTLIST);

        //Create new Bundle that will pass in the coordinates to the Fragment class
        Bundle bundle = new Bundle();
        bundle.putSerializable(Fragment_FoodSpotList_GridView.KEY_FOODSPOT_LIST, fsList);
        //bundle.putSerializable(Fragment_FoodSpotList_GridView.KEY_FOODSPOT_LIST, fsList);

        mFoodSpotListFragment.setArguments(bundle);

        return mFoodSpotListFragment;
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

}
