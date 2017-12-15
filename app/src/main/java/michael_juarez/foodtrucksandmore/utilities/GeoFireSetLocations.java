package michael_juarez.foodtrucksandmore.utilities;

import android.content.Context;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 11/10/2017.
 */

public class GeoFireSetLocations implements AddressToPointUtility.AddressToPointInterface {
    public static void setLocations(LatLng originPoint, final Context context) {
        final FoodSpotDb mFoodSpotDb = FoodSpotDb.getInstance(new FoodSpotLatLng(originPoint.latitude, originPoint.longitude), null);

        DatabaseReference mFoodTrucksRef = mFoodSpotDb.getsDatabase().getReference();
        DatabaseReference mRef = mFoodTrucksRef.child("food_trucks");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference geofireDbRef = mFoodSpotDb.getsDatabase().getReference("path/to/geofire");
                GeoFire geoFire = new GeoFire(geofireDbRef);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FoodSpot foodSpot = postSnapshot.getValue(FoodSpot.class);

                    //Log.d("GeoFireSetLocations:37", foodSpot.getLat() + "\n" + foodSpot.getLongitude());
                    //geoFire.setLocation(postSnapshot.getKey(), new GeoLocation(Double.parseDouble(foodSpot.getLat()), Double.parseDouble(foodSpot.getLongitude())));
                    geoFire.setLocation(postSnapshot.getKey(), new GeoLocation(foodSpot.getLat(), foodSpot.getLng()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    @Override
    public void update(LatLng point) {

    }
}
