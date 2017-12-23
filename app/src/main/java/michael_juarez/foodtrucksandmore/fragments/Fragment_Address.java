package michael_juarez.foodtrucksandmore.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.activities.Activity_FoodSpotList;
import michael_juarez.foodtrucksandmore.utilities.Calculations;
import michael_juarez.foodtrucksandmore.utilities.CurrentLocationUtility;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by user on 11/11/2017.
 */

public class Fragment_Address extends Fragment implements CurrentLocationUtility.UpdateCurrentLocationInterface {
    private Unbinder unbinder;

    @BindView(R.id.fragment_address_zipcode_et)
    EditText mAddressEditText;

    @BindView(R.id.fragment_address_hungry_textview)
    TextView mHungryTextView;

    @BindView(R.id.fragment_address_showme_button)
    Button mShowMeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        unbinder = ButterKnife.bind(this, view);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/app_font.ttf");
        mHungryTextView.setTypeface(custom_font);
        mShowMeButton.setTypeface(custom_font);
        return view;
    }

    //Clicked Current Location Button
    @OnClick(R.id.address_current_location)
    public void clickCurrentLocation() {
        mShowMeButton.setEnabled(false);
        //CurrentLocationUtility currentLocationUtility = CurrentLocationUtility.getInstance(getActivity(), this);
        CurrentLocationUtility currentLocationUtility = new CurrentLocationUtility(getActivity(), this);
        currentLocationUtility.getLocation();
    }

    //Gets response from Interface Method in CurrentLocationUtility
    @Override
    public void updateCurrentLocation(Location location) {
        String address = "";

        if (location == null) {
            makeToast(getResources().getString(R.string.error_current_location));
            return;
        }

        address = getLocationFromPoint(location);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (latLng == null) {
            makeToast(getResources().getString(R.string.error_retrieving_address));
            return;
        }

        startFoodSpotList(latLng, address);
    }

    //Clicked ShowMe Button
    @OnClick(R.id.fragment_address_showme_button)
    public void showMeButtonClick() {
        String address = mAddressEditText.getText().toString();
        if (address != null && !address.isEmpty()) {
            LatLng addressPoint = getLocationFromAddress(address);
            if (addressPoint != null)
                startFoodSpotList(addressPoint, address);
            else
                makeToast(getActivity().getResources().getString(R.string.error_retrieving_address));
        }
        else
            makeToast(getResources().getString(R.string.error_blank_address));
    }


    //This is called when showMeButton is clicked
    private void startFoodSpotList(LatLng latLng, String address) {
        //Convert latLng into String[]
        String[] coordinates = getLatLong(latLng);

        if (coordinates != null) {
            if (address != null && !address.isEmpty())
                mAddressEditText.setText(address);
            Intent intent = new Intent(getActivity(), Activity_FoodSpotList.class);
            intent.putExtra(Activity_FoodSpotList.KEY_COORDINATES, coordinates);
            startActivity(intent);
        } else
            makeToast(getResources().getString(R.string.error_retrieving_address));
    }

    private String[] getLatLong(LatLng latLng) {
        String[] addressArr = new String[2];
        addressArr[0] = "" + latLng.latitude;
        addressArr[1] = "" + latLng.longitude;
        return addressArr;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1;
        Address location;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);

            if (address != null)
                location = address.get(0);
            else {
                makeToast(getActivity().getResources().getString(R.string.error_retrieving_address));
                return null;
            }

            location.setLatitude(Calculations.round(location.getLatitude(), 7));
            location.setLongitude(Calculations.round(location.getLongitude(), 7));

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            makeToast(getActivity().getResources().getString(R.string.error_retrieving_address));
            return null;
        }

        return p1 != null
                ? p1
                : null;
    }

    public String getLocationFromPoint(Location location) {
        Geocoder coder = new Geocoder(getActivity(), Locale.US);
        List<Address> address = null;

        try {
            // May throw an IOException
            address = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (address == null)
                return "";

        } catch (IOException ex) {
            return "";
        }

        return address.get(0).getAddressLine(0);
    }

    private void makeToast(String toastMessage) {
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
