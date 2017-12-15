package michael_juarez.foodtrucksandmore.utilities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;

import static android.R.attr.width;
import static android.R.string.cancel;

/**
 * Created by user on 11/25/2017.
 */

public class GenerateMarkersUtility implements Runnable{
    private GoogleMap mMap;
    private FoodSpotLatLng mOriginPoint;
    private ArrayList<Marker> mMarkerList;
    private CardView mCardDetailsView;
    private Activity mActivity;
    private ArrayList<FoodSpot> mFoodSpotList;
    private Runnable mRunnable;
    private Handler mHandler;
    private Marker mOriginPointMarker;

    public GenerateMarkersUtility(GoogleMap map, FoodSpotLatLng originPoint, CardView mapContainer, Activity activity, ArrayList<FoodSpot> foodSpotList) {
        mMap = map;
        mOriginPoint = originPoint;
        mCardDetailsView = mapContainer;
        mActivity = activity;
        mFoodSpotList = foodSpotList;

        mHandler = new Handler();
        mRunnable = this;
        mMarkerList = new ArrayList();
    }

    public void getMarkerList() {
        mRunnable.run();
    }

    public void updateOriginMaker(FoodSpotLatLng location) {
        if (mOriginPointMarker != null)
            mOriginPointMarker.remove();

        if (mMarkerList != null) {
            mOriginPointMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLng()))
                    .title(String.valueOf(mActivity.getBaseContext().getResources().getString(R.string.point_of_interest))));
            mMarkerList.set(0, mOriginPointMarker);
        }
    }

    @Override
    public void run() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Add origin point marker to list
                mOriginPointMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mOriginPoint.getLat(), mOriginPoint.getLng()))
                        .title(String.valueOf(R.string.point_of_interest)));
                mMarkerList.add(mOriginPointMarker);

                // While iterating through FoodSpot List, create GoogleMarkers from each FoodSpot's
                // Latitude and Longitude points.  Then add the markers to the marker list
                for (FoodSpot fs : mFoodSpotList) {
                    double fsLatitude = 0;
                    double fsLongitude = 0;
                    try {
                        fsLatitude = fs.getLat();
                        fsLongitude = fs.getLng();

                    } catch (Exception e) {
                        Toast.makeText(mActivity, "Error Loading Markers", Toast.LENGTH_LONG);
                    }
                    Marker fsMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(fsLatitude, fsLongitude))
                            .title(fs.getName()));
                    mMarkerList.add(fsMarker);
                }

                // Zoom the Map out so that only the populated markers on the Map are visible
                // within the Map viewport.
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkerList) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int width = mCardDetailsView.getMeasuredWidth();
                int height = mCardDetailsView.getMeasuredHeight();
                int padding = (int) (height * 0.10);
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cu);
            }
        });
    }

    public void updateMarkerList(ArrayList<FoodSpot> updatedList) {
        if (mMap == null)
            return;

        mMap.clear();
        mMarkerList.clear();
        mFoodSpotList = updatedList;
        run();
    }

    public void cancelTask(){
        mHandler.removeCallbacks(mRunnable);
    }

}
