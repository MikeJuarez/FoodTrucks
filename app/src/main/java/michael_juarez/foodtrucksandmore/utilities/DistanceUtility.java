package michael_juarez.foodtrucksandmore.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.DistanceObject;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 11/12/2017.
 */

public class DistanceUtility {
    private List<FoodSpot> mFoodSpotList;
    private FoodSpotLatLng mOriginPoint;
    private DistanceUtilityInterface mDui;
    private DistanceTask mTask;

    public DistanceUtility(List<FoodSpot> foodSpotList, FoodSpotLatLng originPoint, DistanceUtilityInterface dui) {
        mFoodSpotList = foodSpotList;
        mOriginPoint = originPoint;
        mDui = dui;
    }

    public interface DistanceUtilityInterface {
        public void updateAdapters();

        public void stopTask();
    }

    public void getDistance(Context context) {
        mTask = new DistanceTask();
        mTask.execute(context);
    }

    public void stopTask() {

        if (mTask != null)
            mTask.cancel(true);
    }

    private class DistanceTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0];

            for (int i = 0; i < mFoodSpotList.size(); i++) {
                Response response = null;
                String result = "";
                FoodSpot fs = mFoodSpotList.get(i);
                LatLng point2 = null;

                try {
                    //point2 = new LatLng(Double.parseDouble(fs.getLat()), Double.parseDouble(fs.getLongitude()));
                    point2 = new LatLng(fs.getLat(), fs.getLng());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String preURL = context.getResources().getString(R.string.distance_matrix_pre);
                String API_KEY = context.getResources().getString(R.string.api_key);

                OkHttpClient client = new OkHttpClient();

                Uri builtUri = Uri.parse(preURL).buildUpon()
                        .appendQueryParameter("origins", "" + mOriginPoint.getLat() + "," + mOriginPoint.getLng())
                        .appendQueryParameter("destinations", "" + point2.latitude + "," + point2.longitude)
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

                DistanceObject distanceObject = new Gson().fromJson(result, DistanceObject.class);
                mFoodSpotList.get(i).setDistance(distanceObject.getRows().get(0).getElements().get(0).getDistance().getValue());
                mFoodSpotList.get(i).setDistanceText(distanceObject.getRows().get(0).getElements().get(0).getDistance().getText());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            super.onPostExecute(results);
            Collections.sort(mFoodSpotList);
            mDui.updateAdapters();
        }
    }
}
