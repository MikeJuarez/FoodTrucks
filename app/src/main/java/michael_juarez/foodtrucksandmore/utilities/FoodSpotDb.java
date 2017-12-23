package michael_juarez.foodtrucksandmore.utilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;
import michael_juarez.foodtrucksandmore.data.FoodStyle;

import static android.media.CamcorderProfile.get;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by user on 11/9/2017.
 */

public class FoodSpotDb implements DistanceUtility.DistanceUtilityInterface {

    private FirebaseDatabase sDatabase;
    private ArrayList<FoodSpot> mUnfilteredList;
    private static FoodSpotLatLng mOriginPoint;
    private DistanceUtility mDistanceUtility;
    private static FoodSpotDbInterface mFoodSpotDbInterface;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private static FoodSpotDb mFoodSpotDb;
    private ArrayList<FoodSpot> mFilteredList;


    public static FoodSpotDb getInstance(FoodSpotLatLng originPoint, FoodSpotDbInterface foodSpotDbInterface) {
        if (mFoodSpotDb == null)
            mFoodSpotDb = new FoodSpotDb();

        mFoodSpotDbInterface = foodSpotDbInterface;
        mOriginPoint = originPoint;

        return mFoodSpotDb;
    }

    public FoodSpotDb() {
        sDatabase = FirebaseDatabase.getInstance();
    }

    public interface FoodSpotDbInterface {
        void updateAdapters(ArrayList<FoodSpot> filteredList, boolean animateList);
    }

    public FirebaseDatabase getsDatabase() {
        return sDatabase;
    }

    public ArrayList<FoodSpot> getFilteredList(boolean processList) {
        if (!processList)
            return mFilteredList;

        mFilteredList = new ArrayList<>();

        FoodStyleDb foodStyleListData = FoodStyleDb.getInstance();
        ArrayList<FoodStyle> filteredStyleArrayList = foodStyleListData.getFilteredFoodStyleList();

        if (mUnfilteredList.size() > 0) {
            for (int i = 0; i < mUnfilteredList.size(); i++) {
                FoodSpot foodSpot = mUnfilteredList.get(i);
                for (FoodStyle fs : filteredStyleArrayList) {
                    if (fs.isSelected() && fs.getName().equals(foodSpot.getFood_type())) {
                        mFilteredList.add(foodSpot);
                        break;
                    }
                }
            }
        }

        return mFilteredList;
    }



    public void readFromDB(final Context context) {
        mUnfilteredList = new ArrayList();

        DatabaseReference geofireDbRef = sDatabase.getReference("path/to/geofire");
        geoFire = new GeoFire(geofireDbRef);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(mOriginPoint.getLat(), mOriginPoint.getLng()), 25);
        Log.d(TAG, "mOriginPoint: Lat: " + mOriginPoint.getLat() + ", Long: " + mOriginPoint.getLng());

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            private DatabaseReference mFoodTrucksRef = sDatabase.getReference("food_trucks");
            private List<String> mLocalFoodSpotsKeyList = new ArrayList();

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                mLocalFoodSpotsKeyList.add(key);
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                //After GeoFire key list is populated, populate FoodSpot list if GeoFire key list matches what's in the database.
                getFoodSpot(mLocalFoodSpotsKeyList);
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }

            /*
                This method takes in a list of Geofire keys and matches the keys
                to the Food Spot in the Database.  After matching, it then builds a mUnfilteredList
                of FoodSpots that will populate into the RecyclerView
             */
            private void getFoodSpot(List<String> keyList) {
                final int keyListSize = keyList.size();
                for (String key : keyList) {
                    mFoodTrucksRef.orderByKey().equalTo(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            FoodSpot foodspot = dataSnapshot.getValue(FoodSpot.class);
                            foodspot.setKey_geofire(dataSnapshot.getKey().replaceAll("-",""));
                            mUnfilteredList.add(foodspot);
                            mFoodTrucksRef.removeEventListener(this);

                            if (keyListSize == mUnfilteredList.size()) {
                                mFilteredList = mUnfilteredList;
                                setupDistanceUtility(context);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void setupDistanceUtility(Context context) {
        mDistanceUtility = new DistanceUtility(mUnfilteredList, mOriginPoint, this);
        mDistanceUtility.getDistance(context);
    }

    public void stopTask() {
        geoQuery.removeAllListeners();
        if (mDistanceUtility != null)
            mDistanceUtility.stopTask();
    }

    //Called after Distance is done calculating
    @Override
    public void updateAdapters() {
        mFoodSpotDbInterface.updateAdapters(mFilteredList, true);
        geoQuery.removeAllListeners();
    }
}
