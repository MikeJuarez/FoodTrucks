package michael_juarez.foodtrucksandmore.utilities;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodStyle;

/**
 * Created by user on 11/16/2017.
 */

public class FoodStyleDb {
    private static FoodStyleDb mFoodStyleDb;
    private static ArrayList<FoodStyle> mFoodStyleList;

    public static FoodStyleDb getInstance() {
        if (mFoodStyleDb == null) {
            mFoodStyleDb = new FoodStyleDb();
        }

        return mFoodStyleDb;
    }

    private FoodStyleDb() {}

    public void generateList(Context context) {
        mFoodStyleList = new ArrayList();

        mFoodStyleList.add(new FoodStyle("All", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_brown), true));
        mFoodStyleList.add(new FoodStyle("American", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_green), true));
        mFoodStyleList.add(new FoodStyle("Latin", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_pink), true));
        mFoodStyleList.add(new FoodStyle("Asian", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_purple), true));
        mFoodStyleList.add(new FoodStyle("Italian", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_red), true));
        mFoodStyleList.add(new FoodStyle("Indian", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_white), true));
        mFoodStyleList.add(new FoodStyle("Dessert", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_yellow), true));
        mFoodStyleList.add(new FoodStyle("Other", ContextCompat.getDrawable(context, R.drawable.ic_foodtruck_brown), true));
    }

    public ArrayList<FoodStyle> getFoodStyleList() {
        return mFoodStyleList;
    }

    public ArrayList<FoodStyle> getFilteredFoodStyleList( ) {
        for (FoodStyle fs : mFoodStyleList) {
            if (!fs.isSelected())
                fs.setSelected(false);
            else
                fs.setSelected(true);
        }

        return mFoodStyleList;
    }
}
