package michael_juarez.foodtrucksandmore.data;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/16/2017.
 */

public class FoodStyle {
    public static final String filter_all = "All";
    public static final String filter_american = "American";
    public static final String filter_latin = "Latin";
    public static final String filter_asian = "Asian";
    public static final String filter_italian = "Italian";
    public static final String filter_indian = "Indian";
    public static final String filter_dessert = "Dessert";
    public static final String filter_other = "Other";

    private String mName;
    private Drawable mDrawableLocation;
    private boolean mSelected;

    public FoodStyle(String name, Drawable drawable, boolean selected) {
        mName = name;
        mDrawableLocation = drawable;
        mSelected = selected;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Drawable getDrawableLocation() {
        return mDrawableLocation;
    }

    public void setDrawableLocation(Drawable drawableLocation) {
        mDrawableLocation = drawableLocation;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }
}
