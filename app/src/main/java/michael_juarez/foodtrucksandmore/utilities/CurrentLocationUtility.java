package michael_juarez.foodtrucksandmore.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import michael_juarez.foodtrucksandmore.R;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by user on 11/15/2017.
 */

public class CurrentLocationUtility extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static Location mLastKnownLocation;

    private static UpdateCurrentLocationInterface mUpdateCurrentLocationInterface;
    private boolean permissionGranted = false;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1440;

    private static Activity mActivity;

    public CurrentLocationUtility (Activity activity, UpdateCurrentLocationInterface updateCurrentLocationInterface) {
        mUpdateCurrentLocationInterface = updateCurrentLocationInterface;
        mActivity = activity;
    }

    public interface UpdateCurrentLocationInterface {
        void updateCurrentLocation(Location location);
    }

    public void getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        mFusedLocationProviderClient = getFusedLocationProviderClient(mActivity);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        mUpdateCurrentLocationInterface.updateCurrentLocation(mLastKnownLocation);
                    } else {
                        mUpdateCurrentLocationInterface.updateCurrentLocation(null);
                    }
                }
            });
        } else {
            //Location Permission is currently denied, requesting it below
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    getLocation();
                } else {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_enable_location_perm), Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

}

/*


package michael_juarez.foodtrucksandmore.utilities;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.Toast;

        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.places.GeoDataClient;
        import com.google.android.gms.location.places.PlaceDetectionClient;
        import com.google.android.gms.location.places.Places;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;

        import michael_juarez.foodtrucksandmore.R;

        import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

*/
/**
 * Created by user on 11/15/2017.
 *//*


public class CurrentLocationUtility extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static Location mLastKnownLocation;

    private static CurrentLocationUtility mCurrentLocationUtility;

    private static UpdateCurrentLocationInterface mUpdateCurrentLocationInterface;
    private boolean permissionGranted = false;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1440;

    private static Activity mActivity;

    public static CurrentLocationUtility getInstance(Activity activity, UpdateCurrentLocationInterface updateCurrentLocationInterface) {
        if (mCurrentLocationUtility == null)
            mCurrentLocationUtility = new CurrentLocationUtility();
        mUpdateCurrentLocationInterface = updateCurrentLocationInterface;
        mActivity = activity;
        return mCurrentLocationUtility;
    }

    private CurrentLocationUtility() {}

    public interface UpdateCurrentLocationInterface {
        void updateCurrentLocation(Location location);
    }

    public void getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        mFusedLocationProviderClient = getFusedLocationProviderClient(mActivity);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        mUpdateCurrentLocationInterface.updateCurrentLocation(mLastKnownLocation);
                    } else {
                        mUpdateCurrentLocationInterface.updateCurrentLocation(null);
                    }
                }
            });
        } else {
            //Location Permission is currently denied, requesting it below
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    getLocation();
                } else {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.error_enable_location_perm), Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

}
*/

