package michael_juarez.foodtrucksandmore.data;

/**
 * Created by user on 12/12/2017.
 */

import android.content.ContentUris;
import android.support.annotation.NonNull;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static michael_juarez.foodtrucksandmore.data.FoodSpotContract.FoodspotEntry.TABLE_NAME;

/**
 * Created by user on 7/18/2017.
 */

public class FoodSpotContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int FAVORITE_FOODSPOTS_DIRECTORY = 100;
    public static final int FAVORITE_FOODSPOTS_WITH_ID = 101;

    private FoodSpotDbHelper mFavoritesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritesDbHelper = new FoodSpotDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor cursor;
        switch (match) {
            case FAVORITE_FOODSPOTS_DIRECTORY:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_FOODSPOTS_WITH_ID:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        long id;
        switch (match) {
            case FAVORITE_FOODSPOTS_DIRECTORY:
                id = db.insert(FoodSpotContract.FoodspotEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    //Success
                    returnUri = ContentUris.withAppendedId(FoodSpotContract.FoodspotEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case FAVORITE_FOODSPOTS_WITH_ID:
                String mId = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(FoodSpotContract.FoodspotEntry.TABLE_NAME,
                        selection,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Add matches with addURI
        //Directory
        uriMatcher.addURI(FoodSpotContract.AUTHORITY, FoodSpotContract.PATH_FAVORITES, FAVORITE_FOODSPOTS_DIRECTORY);
        //Single item
        uriMatcher.addURI(FoodSpotContract.AUTHORITY, FoodSpotContract.PATH_FAVORITES + "/*", FAVORITE_FOODSPOTS_WITH_ID);

        return uriMatcher;
    }
}
