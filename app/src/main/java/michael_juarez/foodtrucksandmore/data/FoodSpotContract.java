package michael_juarez.foodtrucksandmore.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 12/9/2017.
 */

public final class FoodSpotContract {

    public static final String AUTHORITY = "michael_juarez.foodtrucksandmore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    private FoodSpotContract() {}

    public static class FoodspotEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_CLOSETIME = "closetime";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_FOOD_TYPE = "foodtype";
        public static final String COLUMN_NAME_IMAGE_LOCATION = "imagelocation";
        public static final String COLUMN_NAME_MENU_LOCATION = "menulocation";
        public static final String COLUMN_NAME_OPEN_TIME = "opentime";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_WEBSITE = "website";
        public static final String COLUMN_NAME_ZIP = "zip";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_DISTANCE_TEXT = "distance";
        public static final String COLUMN_NAME_GEOFIRE_KEY = "geofirekey";
       }

}
