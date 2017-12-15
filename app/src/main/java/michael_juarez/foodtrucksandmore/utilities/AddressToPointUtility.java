package michael_juarez.foodtrucksandmore.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.AddressComponent;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 11/20/2017.
 */

public class AddressToPointUtility {

    private String mAddress;
    private String mCity;
    private String mZip;
    private String mState;
    private LatLng mPoint;
    private Context mContext;
    private FoodSpot mFoodSpot;
    private AddressTask mAddressTask;
    private AddressToPointInterface mAddressToPointInterface;

    public AddressToPointUtility(AddressToPointInterface addressToPointInterface, FoodSpot fs, Context context) {
        mFoodSpot = fs;
        mAddress = fs.getAddress();
        mCity = fs.getCity();
        mZip = fs.getZip();
        mState = fs.getState();
        mContext = context;
        mAddressToPointInterface = addressToPointInterface;
    }

    public interface AddressToPointInterface {
        public void update(LatLng point);
    }


    public void generatePoint() {
        mAddressTask = new AddressTask();
        mAddressTask.execute();
    }


    public LatLng getPoint() {
        return mPoint;
    }


    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public void setPoint(LatLng point) {
        mPoint = point;
    }

    private class AddressTask extends AsyncTask<Void, Void, LatLng> {

        @Override
        protected LatLng doInBackground(Void... voids) {

            Response response = null;
            String result = "";
            LatLng point = null;

            String add = mFoodSpot.getAddress() + ",";
            String city = mFoodSpot.getCity() + ",";
            String zip = mFoodSpot.getZip() + ",";
            String state = mFoodSpot.getState();

            String preURL = mContext.getResources().getString(R.string.reverse_geocoding_pre)
                    + add
                    + city
                    + zip
                    + state;
            String API_KEY = mContext.getResources().getString(R.string.api_key);

            OkHttpClient client = new OkHttpClient();


            Uri builtUri = Uri.parse(preURL).buildUpon()
                    .appendQueryParameter("key", API_KEY)
                    .build();

            URL url = null;

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            AddressComponent.Geometry geometry = new Gson().fromJson(result, AddressComponent.Geometry.class);

            AddressComponent.Location location = geometry.getLocation();
            point = new LatLng(location.getLat(), location.getLng());
            return point;
        }

        @Override
        protected void onPostExecute(LatLng results) {
            super.onPostExecute(results);
            mAddressToPointInterface.update(results);
        }
    }
}
