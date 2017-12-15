package michael_juarez.foodtrucksandmore.utilities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.util.Log;
import android.widget.Toast;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotContract;
import michael_juarez.foodtrucksandmore.data.FoodSpotDbHelper;
import michael_juarez.foodtrucksandmore.data.FoodStyle;

import static android.R.attr.id;
import static android.R.attr.process;

/**
 * Created by user on 12/10/2017.
 */

public class FavoritesDbUtility {
    private static final int LOADER_INSERT_FAVORITES = 101;

    private static FavoritesDbUtility mFavoritesDbUtility;
    private SQLiteDatabase mDb;
    private FoodSpot mFoodSpot;

    public static FavoritesDbUtility getInstance(Context context) {
        if (mFavoritesDbUtility == null)
            mFavoritesDbUtility = new FavoritesDbUtility(context);

        return mFavoritesDbUtility;
    }

    private FavoritesDbUtility(Context context) {
        FoodSpotDbHelper foodSpotDbHelper = new FoodSpotDbHelper(context);
        mDb = foodSpotDbHelper.getWritableDatabase();
    }

    public List<FoodSpot> getFavoriteList(Context context, boolean filter) {
        List<FoodSpot> foodSpotList = new ArrayList();

        /*//SQL Version
        Cursor cursor = getAllFoodSpotsCursor();*/

        Cursor cursor = context.getContentResolver().query(FoodSpotContract.FoodspotEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        //If no data, then return null
        if (!cursor.moveToFirst())
            return null;

        for (int i = 0; i < cursor.getCount(); i++) {
            FoodSpot foodSpot = new FoodSpot();

            foodSpot.setAddress(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ADDRESS)));
            foodSpot.setCity(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ADDRESS)));

            foodSpot.setDescription(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_DESCRIPTION)));
            foodSpot.setAddress(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ADDRESS)));
            foodSpot.setClose_time(convertJsonStringToArray(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_CLOSETIME))));
            foodSpot.setEmail(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_EMAIL)));
            foodSpot.setFood_type(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_FOOD_TYPE)));
            foodSpot.setImage_location(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_IMAGE_LOCATION)));
            foodSpot.setMenu_location(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_MENU_LOCATION)));
            foodSpot.setName(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_NAME)));
            foodSpot.setOpen_time(convertJsonStringToArray(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_OPEN_TIME))));
            foodSpot.setState(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_STATE)));
            foodSpot.setWebsite(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_WEBSITE)));
            foodSpot.setZip(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ZIP)));
            foodSpot.setLat(cursor.getDouble(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_LAT)));
            foodSpot.setLng(cursor.getDouble(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_LNG)));
            foodSpot.setDistanceText(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_DISTANCE_TEXT)));
            foodSpot.setKey_geofire(cursor.getString(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY)));
            foodSpot.setId_local(cursor.getInt(cursor.getColumnIndex(FoodSpotContract.FoodspotEntry._ID)));
            foodSpot.setFavorite(true);

            foodSpotList.add(foodSpot);
            cursor.moveToNext();
        }

        if (filter) {
            FoodStyleDb foodStyleListData = FoodStyleDb.getInstance();
            ArrayList<FoodStyle> filteredStyleArrayList = foodStyleListData.getFilteredFoodStyleList();
            ArrayList<FoodSpot> filteredFoodSpotList = new ArrayList<>();

            if (foodSpotList.size() > 0) {
                for (int i = 0; i < foodSpotList.size(); i++) {
                    FoodSpot foodSpot = foodSpotList.get(i);
                    for (FoodStyle fs : filteredStyleArrayList) {
                        if (fs.isSelected() && fs.getName().equals(foodSpot.getFood_type())) {
                            filteredFoodSpotList.add(foodSpot);
                            break;
                        }
                    }
                }
            }
            foodSpotList = filteredFoodSpotList;
        }

        return foodSpotList;
    }

    /*public Cursor getAllFoodSpotsCursor() {
        //SQL VERSION
        Cursor cursor = mDb.query(
                FoodSpotContract.FoodspotEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }*/

    public boolean insertFavorite(Context context, FoodSpot foodSpot) {

        mFoodSpot = foodSpot;

        ContentValues cv = new ContentValues();

        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_DESCRIPTION, mFoodSpot.getDescription());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ADDRESS, mFoodSpot.getAddress());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_CITY, mFoodSpot.getCity());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_CLOSETIME, convertToJsonArray(mFoodSpot.getClose_time()));
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_EMAIL, mFoodSpot.getEmail());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_FOOD_TYPE, mFoodSpot.getFood_type());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_IMAGE_LOCATION, mFoodSpot.getImage_location());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_MENU_LOCATION, mFoodSpot.getMenu_location());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_NAME, mFoodSpot.getName());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_OPEN_TIME, convertToJsonArray(mFoodSpot.getOpen_time()));
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_STATE, mFoodSpot.getState());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_WEBSITE, mFoodSpot.getWebsite());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_ZIP, mFoodSpot.getZip());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_LAT, mFoodSpot.getLat());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_LNG, mFoodSpot.getLng());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_DISTANCE_TEXT, mFoodSpot.getDistanceText());
        cv.put(FoodSpotContract.FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY, mFoodSpot.getKey_geofire());

        Uri uri = context.getContentResolver().insert(FoodSpotContract.FoodspotEntry.CONTENT_URI, cv);

        return (uri != null);

        ///return mDb.insert(FoodSpotContract.FoodspotEntry.TABLE_NAME, null, cv) > 0;
    }

    public boolean removeFavorite(Context context, FoodSpot foodspot) {
        String mId = foodspot.getKey_geofire();

        Uri uri = FoodSpotContract.FoodspotEntry.CONTENT_URI.buildUpon().appendPath(mId).build();

        return context.getContentResolver().delete(uri,
                FoodSpotContract.FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY + "='" + foodspot.getKey_geofire() + "'",
                null) > 0;
        /*return mDb.delete(FoodSpotContract.FoodspotEntry.TABLE_NAME,
                FoodSpotContract.FoodspotEntry.COLUMN_NAME_GEOFIRE_KEY + "='" +
                foodspot.getKey_geofire() + "'", null) > 0;*/
    }

    public static String convertToJsonArray(List<String> listToConvert) {
        JSONArray jsonArray = new JSONArray(Arrays.asList(listToConvert));
        return jsonArray.toString();
    }

    public static ArrayList<String> convertJsonStringToArray(String jsonString) {
        ArrayList<String> convertedList = new ArrayList();

        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                convertedList.add(jsonArray.getString(i));
            }
            return convertedList;
        } catch (Exception e) {
            Log.d("FavoritesDBUtility", "Error converting Json String to ArrayList");
        }

        return convertedList;

    }


}
