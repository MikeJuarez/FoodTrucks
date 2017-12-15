package michael_juarez.foodtrucksandmore.data;

import java.io.Serializable;

/**
 * Created by user on 11/25/2017.
 */

public class FoodSpotLatLng implements Serializable {
    private double mLat;
    private double mLng;

    public FoodSpotLatLng(double v, double v1) {
        mLat = v;
        mLng = v1;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }
}
