package michael_juarez.foodtrucksandmore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import michael_juarez.foodtrucksandmore.data.FoodSpotContract.FoodspotEntry;

import static android.R.attr.version;
import static michael_juarez.foodtrucksandmore.data.FoodSpotContract.FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY;

/**
 * Created by user on 12/9/2017.
 */

public class FoodSpotDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "foodspot.db";
    private static final int DATABASE_VERSION = 1;

    public FoodSpotDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FILTER_TABLE = "CREATE TABLE " +
                FoodspotEntry.TABLE_NAME + "(" +
                FoodspotEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FoodspotEntry.COLUMN_NAME_NAME + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_DESCRIPTION + " STRING, " +
                FoodspotEntry.COLUMN_NAME_ADDRESS + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_CITY + " STRING, " +
                FoodspotEntry.COLUMN_NAME_CLOSETIME + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_EMAIL + " STRING, " +
                FoodspotEntry.COLUMN_NAME_FOOD_TYPE + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_IMAGE_LOCATION + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_MENU_LOCATION + " STRING, " +
                FoodspotEntry.COLUMN_NAME_OPEN_TIME + " STRING NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_STATE + " STRING, " +
                FoodspotEntry.COLUMN_NAME_WEBSITE + " STRING, " +
                FoodspotEntry.COLUMN_NAME_ZIP + " STRING, " +
                FoodspotEntry.COLUMN_NAME_LAT + " REAL NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_LNG + " REAL NOT NULL, " +
                FoodspotEntry.COLUMN_NAME_DISTANCE_TEXT + " STRING, " +
                FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY + " STRING NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FILTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
