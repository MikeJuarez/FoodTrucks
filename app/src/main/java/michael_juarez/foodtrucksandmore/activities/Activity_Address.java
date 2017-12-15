package michael_juarez.foodtrucksandmore.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import michael_juarez.foodtrucksandmore.fragments.Fragment_Address;
import michael_juarez.foodtrucksandmore.fragments.Fragment_FoodSpotList;

/**
 * Created by user on 11/11/2017.
 */

public class Activity_Address extends Activity_SingleFragmentActivity{

    @Override
    protected Fragment createFragment(Bundle savedInstanceState) {
        return new Fragment_Address();
    }
}
